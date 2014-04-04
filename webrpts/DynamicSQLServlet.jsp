<html>
<head>
	<title>Dynamic Reports</title>
</head>

<body leftmargin=15% topmargin=10% bottommargin=5% onload="window.focus()">

<% //this code displays the SQL statement and lets the user choose to edit or submit it

   String statement ="" + (String) session.getValue("statement");
   String clause = "" + (String) session.getValue("clause");
   String sortColumn = "" + (String) request.getParameter("sortColumn");
   String orderType = "" + (String) request.getParameter("orderType");

   //add the "where" and "order by" parts to the SQL statement as appropriate
   if ((orderType.equals("descending"))&&(!(sortColumn.equals("null"))))
     sortColumn += " desc";

   if (!(sortColumn.equals("null")))
     clause+="order by " + sortColumn;

   if (!(clause.equals("null")))
     statement+=" "+clause; 
%>

<center>
<table border=0 cellspacing=0 cellpadding=0 width=100% >
<tr>
	<td align=center><h1>Creating Dynamic Reports</h1></td>
</tr>
<tr><td><hr></td></tr>
</table><br>

<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor=#ff9900 width=100%>
<tr><FORM action=dynamicsqlservlet method="post">
	<td><font size=4>Here is your SQL statement so far:</font></td>
</tr>
<tr><td><br></td></tr>

<tr>
	<td><table border=0 CELLSPACING=0 CELLPADDING=0 bgcolor=#ff9900 width=100%>
	<tr>
		<td align=center><TEXTAREA cols=30 name=sqlStmt rows=10><%=statement%></TEXTAREA></td>
	</tr>
	<tr><td><br><br></td></tr>

	<tr>
		<td colspan=2 align=center>Click <a href="where.jsp">here</a> to place
			restrictions on your query.</td>
	</tr>
	<tr><td><br></td></tr>

	<tr>
		<td align=center><font size=1><a href="help.html">What are restrictions?</a></font></td>
		<td></td>
	</tr>
	<tr><td><br><br><br></td></tr>
	
	<tr>
		<td colspan=2 align=center>Or submit it as is:</td>
	</tr>
	</table></td>
</tr>
<tr><td><br></td></tr>

<tr>
	<td align=center><input type=submit value="Submit"></td>
</tr>
<tr><td><br><br></td></tr>
</TABLE></form></center>

<% //clean up sql statement
   clause="";
   session.putValue("clause", clause); %>

</body>
</html>