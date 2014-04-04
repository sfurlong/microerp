<html>
<head>
<title>Accounts Payable By Account Id Results</title>
</head>


<%@page language="java" import="dai.shared.csAdapters.*,
	dai.server.servlets.*, dai.shared.cmnSvcs.*, java.util.*;" %>

<% String excel = "false";
   if (request.getParameter("excel")!=null) {
     excel = request.getParameter("excel");
   }
   String content = "text/html";
   if (excel.trim().equals("true"))
   {
     content = "application/x-msexcel";
     response.setHeader("Content-disposition", "attachment");
   }
   response.setContentType(content); %>


<% //VARIABLE DECLARATIONS
   csSecurity security = (csSecurity) session.getValue("csSecurity");
   csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
   csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

   String startDate = request.getParameter("parm0");
   String endDate = request.getParameter("parm1");
   String sqlStatement="";
   Vector results = new Vector();
   String prevAcct = "";
   boolean same=false;
   double groupTotal = 0;
   double shippingTotal = 0;
   double grandTotal = 0;
   String shipId="";
   String shipName="";
   String display="";
 %>


<% //Create and execute SQL statement

   sqlStatement="select total_shipping_charges, acctid, acctname, vendor_name, payment_due_date, id, invoice_num, (total_value - total_payments_posted) from payment_voucher"
   + " where (total_payments_posted is null or (total_payments_posted < total_value) or (total_payments_posted > total_value))"
   + " and (is_voided is null or is_voided = 'N') order by acctid, vendor_name, payment_due_date";

   System.out.println(sqlStatement);
   results = dbAdapter.getDynamicSQLResults(security, sqlStatement);
%>

<center>
<h1>Accounts Payable By Account Id
<br>
<hr></h1>
</center>
<body>
<table border=1 width=85%>
<tr bgcolor=darkblue>
	<td><FONT color=white><B><center><U>Account Id</U></center></B></td>
	<td><FONT color=white><B><center><U>Account Name</U></center></B></td>
	<td><FONT color=white><B><center><U>Vendor Name</center></U></B></td>
	<td><FONT color=white><B><center><U>Date Due</center></U></B></td>
	<td><FONT color=white><B><center><U>Voucher Id</center></U></B></td>
	<td><FONT color=white><B><center><U>Invoice Num</center></U></B></td>
	<td><FONT color=white><B><center><U>Amt Due</center></U></B></td>
</tr>

<% //process results for display
   for (int i=0; i < results.size(); i++)
   {
     same = false;
     Vector colVect = (Vector) results.get(i);

     //get shipping charges and add to running total
     String temp = "" + colVect.get(0);
     if ((temp.trim().equals("")) || (temp.trim().equals("null")))
       temp = "0";
     shippingTotal += Double.parseDouble(temp);

     display+="<tr>";
     //display acctid
     //compare to previous record's acctid
     if(colVect.get(1).toString().equals(prevAcct))
     {
        same = true;
        display+="<td></td>";
     }
     else
     {
       same = false;
       prevAcct=colVect.get(1).toString();

       if (i > 0) //not first record
         display+="<td></td><td></td><td></td><td></td><td></td><td align=right><FONT size=2>Total:</font></td>"
           + "<td align=right><FONT size=2><u>" + daiFormatUtil.doubleToCurrency(groupTotal, true) + "</u></font></td></tr><tr>";

       display+="<td align=left><FONT size=2>" + colVect.get(1).toString() + "</font></td>";
       groupTotal=0;
     }

     //display acct name
     if (same)
       display+="<td></td>";
     else
       display+="<td align=left><FONT size=2>" + colVect.get(2).toString() + "</font></td></tr><tr><td></td><td></td>";


     //display vendor name, due date and voucher id
     display+="<td align=left><FONT size=2>" + colVect.get(3).toString() + "</font></td>";
     display+="<td align=left><FONT size=2>" + colVect.get(4).toString() + "</font></td>";
     display+="<td align=left><FONT size=2>" + colVect.get(5).toString() + "</font></td>";

     //display invoice number, replace nulls with "-"
     String temp2 = "" + colVect.get(6);
     if ((temp2.trim().equals("")) || (temp2.trim().equals("null")))
       display+="<td align=center><FONT size=2>-</font></td>";
     else
       display+="<td align=left><FONT size=2>" + temp2 + "</font></td>";

     //display amt due and add it to running grand/group totals (subtract shipping charges from each)
     display+="<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency((Double.parseDouble(colVect.get(7).toString()) - Double.parseDouble(temp)), true) + "</font></td>";
     grandTotal += Double.parseDouble(colVect.get(7).toString()) - Double.parseDouble(temp);
     groupTotal += Double.parseDouble(colVect.get(7).toString()) - Double.parseDouble(temp);

   }//end for i

   //display the last group total
   display+="</tr><td></td><td></td><td></td><td></td><td></td><td align=right><FONT size=2>Total:</font></td>"
           + "<td align=right><FONT size=2><u>" + daiFormatUtil.doubleToCurrency(groupTotal, true) + "</u></font></td></tr><tr>";


   //get shipping account info
   sqlStatement="select shipping_in_id, shipping_in_name from default_accounts";
   results = dbAdapter.getDynamicSQLResults(security, sqlStatement);
   for (int i=0; i < results.size(); i++)
   {
     shipId = ((Vector) results.get(i)).get(0).toString();
     shipName = ((Vector) results.get(i)).get(1).toString();
   }

   //display totals
   display+="<tr><td align=left><FONT size=2>" + shipId + "</font></td><td align=left><FONT size=2>" + shipName + "</font></td></tr>"
   + "<tr><td></td><td></td><td align=center><FONT size=2>-</font></td><td align=center><FONT size=2>-</font></td>"
   + "<td align=center><FONT size=2>-</font></td><td align=right><FONT size=2>Total Shipping Charges:</font></td>"
   + "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(shippingTotal, true) + "</font></td></tr>";
   display+="<tr><td></td><td></td><td></td><td></td><td></td><td align=right><FONT size=2>Grand Total:</font></td>"
   + "<td align=right><font size=2>" + daiFormatUtil.doubleToCurrency(grandTotal, true) + "</font></td></tr>";
%>

<%=display%>
</table>
</body>
</html>