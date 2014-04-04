<html>
<head>
<title>Column Selection </title>
</head>

<body leftmargin="75" topmargin="10" onload="window.focus();">
<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*, dai.shared.businessObjs.*,
    dai.shared.cmnSvcs.*;" 
%>

<% DBAttributes columnNames[]={};
   String selectedTable="" + request.getParameter("table");
   String userValid = "" + session.getValue("userValid");
   csSecurity security = (csSecurity) session.getValue("csSecurity");
%>

<% if (true) //userValid.equals("true"))
   {
     try{
	    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
	    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

	    columnNames = dbAdapter.getColumnNames(security, selectedTable);
      } catch (Exception e) {
        System.out.println(e);
      } %>

<table border=0 cellspacing=0 cellpadding=0 width=100% >
<tr>
	<td align=center><h1>Creating Dynamic Reports</h1></td>
</tr>
<tr><td><hr></td></tr>
</table><br>

<table border=0 cellspacing=5 cellpadding=0 bgcolor=#ff9900 width=100% >
<tr>
	<td align=center><h1>Step 2</h1></td>
</tr>
<tr><td></td></tr>
<tr> <form action="results.jsp" method=post>
	<td><font size=4>Select which column(s) you want to query in </font><%=selectedTable%>: </font></td>
</tr>
<tr><td><br></td></tr>

<tr>
	<td align=center> <select name=column size=8 multiple>
		<option value=<%=columnNames[0].getName()%> SELECTED> <%=columnNames[0].getName()%>

	    <% //displays a list of all the columns in the selected table
	      for (int i=1; i < columnNames.length; i++)
              { %>
		<option value=<%=columnNames[i].getName()%> > <%=columnNames[i].getName()%><br>
           <% } %> </select> </td>
</tr>
<tr><td><br></td></tr>

<tr>
	<td colspan=2 align=center><font size=2>(To choose multiple columns, hold down the
		CTRL-key and click on the column names.)</font></td>
</tr>

<tr>
	<td align=center><br><br><input type=submit value="Continue">
		<input type=hidden name=table value=<%=selectedTable%>></td>
</tr>
<tr><td><br><br></td></tr>

</form>
</table>

<% }
   else
   { %>
<h1>Access Denied</h1>
You do not have permission to view this page.
<% } %>

</body>
</html>