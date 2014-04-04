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
    String sqlStmt = "select distinct ITEM_ID, DESCRIPTION1 from PURCH_ORDER_ITEM"
	    + " where ITEM_ID is not null order by ITEM_ID";
    String temp = "";
    String temp2 = "";
    Vector tempVect= new Vector();
    Vector item = dbAdapter.getDynamicSQLResults(security, sqlStmt); %>

<body bgcolor=lightgreen>
<table border=1, cellpadding=0, cellspacing=0, width=100% bgcolor=white>
  <tr bgcolor=darkblue>
	<td><FONT color=white>Item #</FONT></td>
	<td><FONT color=white>Item Description</FONT></td>
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