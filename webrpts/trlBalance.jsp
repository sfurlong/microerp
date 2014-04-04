<html>
<head>
<title>Trial Balance Results</title>
</head>


<%@page language="java" import="dai.shared.csAdapters.*,
	dai.server.servlets.*, dai.shared.cmnSvcs.*,
	java.util.Vector;" %>

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

<jsp:useBean id="trlBalanceBean" scope="session"
class="dai.server.servlets.trlBalanceBean" />

<% //VARIABLE DECLARATIONS
   csSecurity security = (csSecurity) session.getValue("csSecurity");
   csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
   csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
   SessionMetaData _sessionMeta = SessionMetaData.getInstance();
   FinanceAcctsDataCache _financeCache = FinanceAcctsDataCache.getInstance();
   trlBalanceObj processResults = new trlBalanceObj();

   String startDate = request.getParameter("parm0");
   String endDate = request.getParameter("parm1");
   String showDates="";
   String sqlStatement="select ID from ACCOUNT where IS_SUBACCOUNT!='Y' order by ID";
   String temp="";
   String display="";
   Vector results = new Vector();
 %>

<% //USER VALIDATION
   String userValid= (String) session.getValue("userValid");
   if (!(userValid.equals("true")))
    { 
      response.sendRedirect("http://" + _sessionMeta.getWebRptsHost() + "/error401.html");
    }

   boolean rptValid = false;
   String userId = (String) session.getValue("userId");
   String rightsSql = "select READ_PERMISSION from USER_WEBREPORTS_SECURITY where "+
	"COMPONENT_ID='TrialBalance' and ID='"+userId+"'";
   try {
	Vector rights = dbAdapter.getDynamicSQLResults(security, rightsSql);
	if (rights.get(0).toString().equals("[Y]"))
	  rptValid=true;
   } catch (Exception e) {
	rptValid=false;
	e.printStackTrace();
   }
   System.out.println("rptValid="+rptValid);
   if (!(rptValid))
   {
     response.sendRedirect("http://" + _sessionMeta.getWebRptsHost() + "/error401.html");
   } %>

<% //GET ALL MASTER ACCT ID'S
   results = dbAdapter.getDynamicSQLResults(security, sqlStatement);
   String masterID[] = new String[results.size()];
   for (int i =0; i < results.size();i++)
    {
      temp=results.get(i).toString();
      masterID[i]=temp.substring(1, (temp.indexOf("]")));
    } %>

<%  sqlStatement="select ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL";
    String whereClause = "";
    session.putValue("startDate", startDate);
    session.putValue("endDate", endDate);
    if (!(startDate.equals("")) && !(endDate.equals("")))
    {
      whereClause = " TRANS_DATE between '" + startDate + "' and '" + endDate + "'";
      sqlStatement += " where " + whereClause;
      showDates = "From: " + startDate + " To: " + endDate + "<br>";
    }
   sqlStatement +=" group by ID";
   results = dbAdapter.getDynamicSQLResults(security, sqlStatement);
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
	<!--<td><FONT color=white><B><center><U>Sub Acct Balance</center></U></B></td>-->
	<td><FONT color=white><B><center><U>Balance</center></U></B></td>
</tr>
<%=trlBalanceBean.getFinalResults(masterID, results, whereClause, security) %>
</table>
</body>
</html>
