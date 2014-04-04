<html>
<head>
<title>Trial Balance Results</title>
</head>


<%@page language="java" import="dai.shared.csAdapters.*,
	dai.server.servlets.*, dai.shared.cmnSvcs.*,
	java.util.Vector;" %>

<% 
   String excel = "false";
   if (request.getParameter("excel")!=null) {
     excel = request.getParameter("excel");
   }
   String content = "text/html";
   if (excel.trim().equals("true"))
   {
     content = "application/x-msexcel";
     response.setHeader("Content-disposition", "attachment");
   }
   response.setContentType(content); 
%>

<jsp:useBean id="trlBalanceBean2" scope="session"
class="dai.server.servlets.trlBalanceBean2" />

<jsp:useBean id="dateBean" scope="session"
class="dai.server.servlets.dateBean" />

<% //VARIABLE DECLARATIONS
   csSecurity security = (csSecurity) session.getValue("csSecurity");
   csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
   csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
   SessionMetaData _sessionMeta = SessionMetaData.getInstance();
   FinanceAcctsDataCache _financeCache = FinanceAcctsDataCache.getInstance();

   String startDate = request.getParameter("parm0");
   String endDate = request.getParameter("parm1");
   String prevStart = "";
   String prevEnd = "";
   String showDates="";
   String sqlStatement="select ID from ACCOUNT where IS_SUBACCOUNT!='Y' order by ID";
   String temp="";
   String temp2="";
   String display="";
   Vector results = new Vector();
   Vector prevMonthResults = new Vector();
   Vector tempVect = new Vector();
   boolean found = false;
   int index=0;
 %>

<% //GET ALL MASTER ACCT ID'S
   results = dbAdapter.getDynamicSQLResults(security, sqlStatement);
   String masterID[] = new String[results.size()];
   for (int i =0; i < results.size();i++)
    {
      tempVect=(Vector) results.get(i);
      masterID[i]=tempVect.get(0).toString();
    } %>

<%  //GET CURRENT MONTH'S DATA
    sqlStatement="select ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL ";
    String whereClause = "";
    String dateClause = "";
    if (!(endDate.equals("")))
    {
      temp = endDate.substring(endDate.lastIndexOf("/")+1);
      startDate = "01/01/" + temp;
      session.putValue("startDate", startDate);
      session.putValue("endDate", endDate);
      prevEnd = dateBean.prevMonthEnd(endDate);
      temp = prevEnd.substring(prevEnd.lastIndexOf("/")+1);
      prevStart = "01/01/" + temp;
      session.putValue("prevStart", prevStart);
      session.putValue("prevEnd", prevEnd);

      whereClause = "where TRANS_DATE between '" + startDate + "' and '" + endDate + "'";
      dateClause = "TRANS_DATE between '" + startDate + "' and '" + endDate + "'";
      showDates = "From: " + startDate + " To: " + endDate + "<br>";
    }
   sqlStatement += whereClause + " group by ID";
   results = dbAdapter.getDynamicSQLResults(security, sqlStatement);
%>

<% //GET LAST MONTH'S DATA
   sqlStatement="select ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL";
   String whereClause2 = "";
   if (!(startDate.equals("")) && !(endDate.equals("")))
      whereClause2 = " where TRANS_DATE between '" + prevStart + "' and '" + prevEnd + "'";   
   sqlStatement += whereClause2 + " group by ID";
   prevMonthResults = dbAdapter.getDynamicSQLResults(security, sqlStatement);
%>

<% //COMBINE DATA
   for (int i = 0; i < results.size(); i++)
   {
     found = false;
     for(int j = 0; j < prevMonthResults.size(); j++)
     {
       tempVect = (Vector) results.get(i);
       temp = tempVect.get(0).toString();
       tempVect = (Vector) prevMonthResults.get(j);
       temp2 = tempVect.get(0).toString();
       if (temp.equals(temp2))
       {
         found  = true;
         index = j;
       }
     }

     if (found) //acct has info from last month
     {
	tempVect = (Vector) prevMonthResults.get(index);
        temp = "" + tempVect.get(1);  //prevDebit
        temp2 = "" +tempVect.get(2); //prevCredit
     }
     else
     {
        temp = "0";  //no prevDebit
        temp2 = "0"; //no prevCredit
     }

     tempVect = (Vector) results.get(i);
     tempVect.addElement(temp);
     tempVect.addElement(temp2);
     results.set(i, tempVect);
   }
%>

<center>
<h1>Trial Balance
<br>
<%=showDates%>
<hr></h1>
</center>
<body>
<table border=1 width=85% >
<tr bgcolor=darkblue>
	<td><FONT color=white><B><center><U>Account Id</U></center></B></td>
	<td><FONT color=white><B><center><U>Account Name</U></center></B></td>
	<td><FONT color=white><B><center><U>Debit</center></U></B></td>
	<td><FONT color=white><B><center><U>Credit</center></U></B></td>
	<td><FONT color=white><B><center><U>Ending Balance</center></U></B></td>
	<td><FONT color=white><B><center><U>Prev Month Ending Balance</center></U></B></td>
	<td><FONT color=white><B><center><U>Difference</center></U></B></td>
</tr>
<%=trlBalanceBean2.getFinalResults(masterID, results, dateClause, security)%>
</table>
</body>
</html>
