<html>
<head>
<title>Profit/Loss Results</title>
</head>


<%@page language="java" import="dai.shared.csAdapters.*,
	dai.server.servlets.*, dai.shared.cmnSvcs.*,java.util.Vector;" %>

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

<jsp:useBean id="profitLossBean" scope="session"
class="dai.server.servlets.profitLossBean" />

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
   String whereClause="";
   String whereClause2="";
   String dateClause="";
   String acctTypes[] = {"INCOME", "COGS", "EXPENSE"};  //each type of account
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
      tempVect = (Vector) results.get(i);
      masterID[i] = tempVect.get(0).toString();
      //temp=results.get(i).toString();
      //masterID[i]=temp.substring(1, (temp.indexOf("]")));
    } %>

<% //GET DATA FOR EACH ACCT TYPE
    for (int i=0; i < acctTypes.length; i++)
    {
      //GET CURRENT DATA
      sqlStatement="select ACCOUNT_DETAIL.ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL ad left join ACCOUNT a on ad.ID = a.id";
      whereClause = " where";
      if (!(endDate.equals("")))
      {
        temp = endDate.substring(endDate.lastIndexOf("/")+1);
        //startDate = "01/01/" + temp;
        session.putValue("startDate", startDate);
		session.putValue("endDate", endDate);
        prevEnd = dateBean.prevMonthEnd(endDate);
        //temp = prevEnd.substring(0, prevEnd.indexOf("/"));
        //prevStart = "" + temp + "/01/";
        //temp = prevEnd.substring(prevEnd.lastIndexOf("/")+1);
        //prevStart += temp;
		session.putValue("prevStart", prevStart);
		session.putValue("prevEnd", prevEnd);

        whereClause += " ad.TRANS_DATE between '" + startDate + "' and '" + endDate + "' and";
        dateClause = "TRANS_DATE between '" + startDate + "' and '" + endDate + "'";
        showDates = "From: " + startDate + " To: " + endDate + "<br>";
      }
      whereClause += " a.ACCOUNT_TYPE like '" + acctTypes[i] + "'";
      sqlStatement += whereClause + " group by ad.ID";
      results = dbAdapter.getDynamicSQLResults(security, sqlStatement);

      //GET LAST MONTH'S DATA
      sqlStatement="select ACCOUNT_DETAIL.ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL ad left join ACCOUNT a on ad.ID = a.id";
      whereClause2 = " where";
      if (!(prevStart.equals("")) && !(prevEnd.equals("")))
      {
        whereClause2 += " ad.TRANS_DATE between '" + prevStart + "' and '" + prevEnd + "' and";
      }
      whereClause2 += " a.ACCOUNT_TYPE like '" + acctTypes[i] + "'";
      sqlStatement += whereClause2 + " group by ad.ID";
      prevMonthResults = dbAdapter.getDynamicSQLResults(security, sqlStatement);

      //COMBINE DATA
      for (int x = 0; x < results.size(); x++)
      {
        found = false;
        for(int y = 0; y < prevMonthResults.size(); y++)
        {
          //temp = results.get(x).toString();
          //temp = temp.substring(1, (temp.indexOf(",")));
          tempVect = (Vector) results.get(x);
          temp = tempVect.get(0).toString();
          //temp2 = prevMonthResults.get(y).toString();
          //temp2 = temp2.substring(1, (temp2.indexOf(",")));
          tempVect = (Vector) prevMonthResults.get(y);
          temp2 = tempVect.get(0).toString();
          if (temp.equals(temp2))
          {
            found  = true;
            index = y;
          }
        }//end for y

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

        tempVect = (Vector) results.get(x);
        tempVect.addElement(temp);
        tempVect.addElement(temp2);
        results.set(x, tempVect);
      }//end for x

      display += profitLossBean.getFinalResults(masterID, results, dateClause, security, acctTypes[i]);
    }//end for i
%>



<center>
<h1>Profit/Loss
<br>
<%=showDates%>
<hr></h1>
</center>
<body>
<table border=1 width=85% >
<tr bgcolor=darkblue>
	<td><FONT color=white><B><center><U>Account Type</U></center></B></td>
	<td><FONT color=white><B><center><U>Account Id</U></center></B></td>
	<td><FONT color=white><B><center><U>Account Name</U></center></B></td>
	<td><FONT color=white><B><center><U>Balance</center></U></B></td>
</tr>
<%=display%>
</table>
</body>
</html>