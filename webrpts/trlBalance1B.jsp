<html>
<head>
<title>Subaccounts</title>
</head>

<%@ page language="java" import="dai.shared.csAdapters.*,
	dai.shared.cmnSvcs.*, dai.shared.businessObjs.*, dai.server.servlets.*,
	java.util.Vector;" %>

<%  csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
%>

<% String masterId = request.getParameter("masterId");
   String startDate = "" + session.getValue("startDate");
   String endDate = "" + session.getValue("endDate");

   String display = "";
   String master = "";
   String showDates = "";
   String sqlStatement = "select ACCOUNT_DETAIL.ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL ad left join ACCOUNT a on ad.ID = a.id";
   String whereClause = " where";
   String temp = "";
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
     whereClause += " ad.TRANS_DATE between '" + startDate + "' and '" + endDate + "' and";
     showDates = "From " + startDate + " to " + endDate;
   }
   whereClause += " a.SUBACCOUNT like '" + masterId + "'";
   sqlStatement += whereClause + " group by ad.ID";
   results = dbAdapter.getDynamicSQLResults(security, sqlStatement);
   
   for (int i=0; i < results.size(); i++)
   {
     tempVect = (Vector) results.get(i);
     trlBalanceObj tempObj = new trlBalanceObj(tempVect);
     tempObj.setBalance();
     total += tempObj.getBalance();
     numDebit = Double.parseDouble(tempObj.getDebit());
     totDebit += numDebit;
     numCredit = Double.parseDouble(tempObj.getCredit());
     totCredit += numCredit;

     display += "<tr>";
     display += "<td align=left><FONT size=2><a href='transaction.jsp?acctId=" + tempObj.getID() + "'>" + tempObj.getID() + "</a></FONT></td>";
     display += "<td align=left><FONT size=2>" + tempObj.getName() + "</FONT></td>";
     display += "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(numDebit, true) + "</FONT></td>";
     display += "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(numCredit, true) + "</FONT></td>";
     display += "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(tempObj.getBalance(), true) + "</FONT></td>";
     display += "</tr>";
   }//end for i



   //GET MASTER ACCT DATA
   sqlStatement = "select ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL where";
   if ((!startDate.trim().equals("null")) && (!endDate.trim().equals("null")))
     whereClause = " TRANS_DATE between '" + startDate + "' and '" + endDate + "' and";
   sqlStatement += whereClause + " ID like '" + masterId + "' group by ID";
   results= dbAdapter.getDynamicSQLResults(security, sqlStatement);

   if(results.size() > 0)
   {
     tempVect = (Vector) results.get(0);
     trlBalanceObj tempObj = new trlBalanceObj(tempVect);
     tempObj.setBalance();
     total += tempObj.getBalance();
     numDebit = Double.parseDouble(tempObj.getDebit());
     totDebit += numDebit;
     numCredit = Double.parseDouble(tempObj.getCredit());
     totCredit += numCredit;

     master = "<tr>";
     master +="<td align=left><FONT size=2><a href='transaction.jsp?acctId=" + tempObj.getID() + "'>" + tempObj.getID() + "</a></FONT></td>";
     master +="<td align=left><FONT size=2>" + tempObj.getName() + "</FONT></td>";
     master +="<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(numDebit, true) + "</FONT></td>";
     master +="<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(numCredit, true) + "</FONT></td>";
     master +="<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(tempObj.getBalance(), true) + "</FONT></td>";
     master +="</tr>";
  }
%>


<center>
<h1>Trial Balance For Account #: <%=masterId%>
<br>
<%=showDates%>
<hr></h1>
</center>
<body>
<table border=1 width=85% >
<tr bgcolor=darkblue>
	<td><FONT color=white><B><center><U>Account Id</U></center></B></td>
	<td><FONT color=white><B><center><U>Account Name</U></center></B></td>
	<td><FONT color=white><B><center><U>Debit</U></center></B></td>
	<td><FONT color=white><B><center><U>Credit</U></center></B></td>
	<td><FONT color=white><B><center><U>Balance</center></U></B></td>
</tr>
<%=master%>
<%=display%>
<tr><td></td></tr>
<tr><td></td>
	<td align=right><FONT size=2>Totals:</FONT></td>
	<td align=right><FONT size=2><%=daiFormatUtil.doubleToCurrency(totDebit, true)%></FONT></td>
	<td align=right><FONT size=2><%=daiFormatUtil.doubleToCurrency(totCredit, true)%></FONT></td>
        <td align=right><FONT size=2><%=daiFormatUtil.doubleToCurrency(total, true)%></FONT></td>
</tr>
</table>
</body>
</html>