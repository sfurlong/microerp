<html>
<head>
<title>Table Selection </title>
</head>

<body leftmargin="75" topmargin="10" onload="window.focus();">
<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*, dai.shared.businessObjs.*,
    dai.shared.cmnSvcs.*;" 
%>

<% String tableNames[]={};
   String userId= "" + (String) session.getValue("userId");
   String passwd="" + (String) session.getValue("passwd");
   String userValid="" + (String) session.getValue("userValid"); %>

<% if (true) //userValid.equals("true"))
   {
     try{
        csSecurity security = (csSecurity) session.getValue("csSecurity");
        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
	    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

	    tableNames = dbAdapter.getTableNames(security);
	    session.putValue("security", security);
     } catch (Exception e) {
	    System.out.println(e);
     } %>

<center>
<table border=0 cellspacing=0 cellpadding=0 width=100% >
<tr>
	<td align=center><h1>Creating Dynamic Reports</h1></td>
</tr>
<tr><td><hr></td></tr>
</table><br>

<table border=0 cellspacing=5 cellpadding=0 bgcolor=#ff9900 width=100% >
<tr>
	<td align=center><h1>Step 1</h1></td>
</tr>
<tr><td></td></tr>

<tr> <form action="columns.jsp" method=post>
	<td><font size=4>Select a table to query:</font></td>
</tr>
<tr><td><br></td></tr>

<tr>
	<td align=center> <select name=table size=8>
		<option value=<%=tableNames[0]%> SELECTED> <%=tableNames[0]%>

	    <% //displays a list of all tables in the db
	      for (int i=1; i < tableNames.length; i++)
              { %>
		<option value=<%=tableNames[i]%> > <%=tableNames[i]%> <br>
           <% } %> </select> </td>
</tr>

<tr>
	<td colspan=2 align=center><br><br><br><input type=submit value="Continue"></td>
</tr>
<tr><td><br><br><br></td></tr>

</form>
</table>
</center>

<% }
   else
   { %>
   <h1>Access Denied</h1>
You do not have permission to view this page.
<% } %>

</body>
</html>