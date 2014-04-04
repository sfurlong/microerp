<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<title>Save Dynamic Reports</title>
</head>

<% String stmt="" + request.getParameter("stmt"); %>
<body leftmargin=15% topmargin=10% bottommargin=5% onload="window.focus();">
<center>

<table border=0 cellspacing=0 cellpadding=0 width=100% >
<tr>
	<td align=center><h1>Saving Dynamic Reports</h1></td>
</tr>
<tr><td><hr></td></tr>
</table><br>

<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor=#ff9900 width=100%>
<tr><td colspan=4><br></td></tr>

<tr><FORM action="saveRpt2.jsp" method="post">
	<td align=center width=90><font size=4>Title:</font></td>
	<td><input type=text name="title" size=40></td>
</tr>
<tr><td><br><br></td></tr>

<tr>
	<td align=center width=90><font size=4>Enter your SQL statement:</font></td>
	<td><TEXTAREA cols=40 name=sqlStmt rows=10><%=stmt%></TEXTAREA></td>
</tr>
<tr><td><br></td></tr>

<tr>
	<td align=center><a href="help.html#save"><font size=2> Need help?</font></a></td>
</tr>
<tr><td><br><br></td></tr>

<tr bgcolor=#ff9900>
	<td colspan=4 align=center><input type=submit value="Submit"></td>
</tr>
</TABLE></form></center>

</body>
</html>