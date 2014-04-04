<html>
<head>
<title>Item Numbers</title>
</head>

<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*, dai.shared.businessObjs.*,
    dai.shared.cmnSvcs.*,java.util.Vector;"
%>

<%
    csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    String sqlStmt = "select distinct cust.ID, cust.NAME from CUSTOMER cust, CUST_ORDER_ITEM coi, CUST_ORDER co"
	    + " where cust.ID is not null and coi.ID=co.ID and co.CUSTOMER_ID=cust.ID order by cust.ID";
    String temp = "";
    String temp2 = "";
    Vector tempVect= new Vector();
    Vector item = dbAdapter.getDynamicSQLResults(security, sqlStmt); %>

<body bgcolor=lightgreen>
<table border=1, cellpadding=0, cellspacing=0, width=100% bgcolor=white>
  <tr bgcolor=darkblue>
	<td><FONT color=white>Cust #</FONT></td>
	<td><FONT color=white>Customer Name</FONT></td>
  </tr>

<% for (int i=0; i < item.size(); i ++)
   {
     tempVect = (Vector) item.get(i);
     temp = "" + tempVect.get(0);
     temp2 = "" + tempVect.get(1); %>

  <tr>
        <td><FONT size=2><%=temp %></FONT></td>
        <td><FONT size=2><%=temp2%></FONT></td>
  </tr>
<% } %>

</table>
</body>
</html>