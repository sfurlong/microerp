<html>
<head>
<title>Transactions</title>
</head>

<%@ page language="java" import="dai.shared.csAdapters.*,
	dai.shared.cmnSvcs.*, dai.shared.businessObjs.*, dai.server.servlets.*,
	java.util.Vector;" %>

<%  csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
%>

<% String acctId = request.getParameter("acctId");
   String startDate = "" + session.getValue("startDate");
   String endDate = "" + session.getValue("endDate");
   String prevStart = "" + session.getValue("prevStart");
   String prevEnd = "" + session.getValue("prevEnd");
   String display = "";
   String showDates = "";
   String sqlStatement = "select ID, TRANS_DATE, TRANS_REF, TRANS_TYPE, DEBIT, CREDIT, NOTE1 from ACCOUNT_DETAIL";
   String whereClause = " where ID like '" + acctId + "'";
   Vector results = new Vector();
   Vector tempVect = new Vector();
   double numDebit = 0;
   double numCredit = 0;
   double total = 0;
   double totDebit = 0;
   double totCredit = 0;


   //GET CURRENT DATA
   if ((!startDate.trim().equals("null")) && (!endDate.trim().equals("null")))
   {
     whereClause += " and TRANS_DATE between '" + startDate + "' and '" + endDate + "'";
     showDates = "From " + startDate + " to " + endDate;
   }
   sqlStatement += whereClause + " order by TRANS_DATE, DETAIL_ID";
   results = dbAdapter.getDynamicSQLResults(security, sqlStatement);


   for (int i=0; i < results.size(); i++)
   {
     tempVect = (Vector) results.get(i);
     transactionObj tempObj = new transactionObj(tempVect);
     tempObj.setBalance();

     numDebit = Double.parseDouble(tempObj.getDebit());
     totDebit += numDebit;
     numCredit = Double.parseDouble(tempObj.getCredit());
     totCredit += numCredit;

     display += "<tr>";
     display += "<td align=left><FONT size=2>" + tempObj.getID() + "</FONT></td>";
     display += "<td align=left><FONT size=2>" + tempObj.getName() + "</FONT></td>";
     display += "<td align=left><FONT size=2>" + tempObj.getDate() + "</FONT></td>";
     display += "<td align=left><FONT size=2>" + tempObj.getTrans_Ref() + "</FONT></td>";
     display += "<td align=left><FONT size=2>" + tempObj.getType() + "</FONT></td>";
     display += "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(numDebit, true) + "</FONT></td>";
     display += "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(numCredit, true) + "</FONT></td>";
     display += "<td align=left><FONT size=2>" + tempObj.getNote() + "</FONT></td>";
     display += "</tr>";
   }
%>


<center>
<h1>Transactions For Account #: <%=acctId%>
<br>
<%=showDates%>
<hr></h1>
</center>
<body>
<table border=1 width=85% >
<tr bgcolor=darkblue>
	<td><FONT color=white><B><center><U>Account Id</U></center></B></td>
	<td><FONT color=white><B><center><U>Account Name</U></center></B></td>
	<td><FONT color=white><B><center><U>Date</U></center></B></td>
	<td><FONT color=white><B><center><U>Trans. Ref</U></center></B></td>
	<td><FONT color=white><B><center><U>Trans. Type</U></center></B></td>
	<td><FONT color=white><B><center><U>Debit</U></center></B></td>
	<td><FONT color=white><B><center><U>Credit</U></center></B></td>
	<td><FONT color=white><B><center><U>Note</center></U></B></td>
</tr>
<%=display%>
<tr><td></td></tr>
<tr><td></td><td></td><td></td><td></td>
	<td align=right><FONT size=2>Totals:</FONT></td>
	<td align=right><FONT size=2><%=daiFormatUtil.doubleToCurrency(totDebit, true)%></FONT></td>
	<td align=right><FONT size=2><%=daiFormatUtil.doubleToCurrency(totCredit, true)%></FONT></td>
</tr>
</table>
</body>
</html>