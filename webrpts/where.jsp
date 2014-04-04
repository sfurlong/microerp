<html>
<head>
<title>Restrict Data </title>
</head>

                  
<body leftmargin="75" topmargin="10" onload="window.focus();">
<%@ page language="java"%>

<% String selectedColumns= "" + (String) session.getValue("selectedColumns");
   String columnNames[]=new String [(selectedColumns.length())];
   int i=0;
   int space=0; %>

<% while (selectedColumns.length() > 0)
   {
     space = selectedColumns.indexOf (" "); 
     columnNames[i]=selectedColumns.substring(0, space);
     selectedColumns = selectedColumns.substring((space+1));
     i++;
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
	<td colspan=2 align=center><h1>Step 3</h1></td>
</tr>

<tr>
	<td colspan=2 align=center><font size=5>Restricting your data</font></td>
</tr>

<tr>
	<td colspan=2><br>Select which column to restrict:</td>
</tr>

<tr> <form action="order.jsp" method=post>
	<td colspan=2><select name=restrictedColumn>
		<option value="none">Select None<br>

		<% //displays all columns in the selected table
                   for (int j=0; j < i; j++)
		   { %>
		     <option value =<%=columnNames[j]%>><%=columnNames[j]%><br>
		<% } %> </td>
</tr>
<tr><td><br><br></td></tr>

<tr>
	<td colspan=2>Now select an operator:</td>
</tr>

<tr>
	<td colspan=2> <select name=operator>
	<option value="="> = <br>	<option value="!="> != <br>
	<option value=">"> > <br>	<option value="<"> < <br>
	<option value=">="> >= <br>	<option value="<="> <= <br>
	<option value="like"> like <br>	<option value="is not null"> is not null <br>
		<option value="between"> between <br> </select></td>
</tr>
<tr><td><br><br></td></tr>

<tr>
	<td colspan=2>Finally enter any necessary limits.<br><font size=2>If you selected the
		"is not null" operator, leave both fields blank.<br>You only need to fill in
		the 2nd field if you selected the "between" operator.</td>
</tr>
<tr><td><br><br></td></tr>

<tr>
	<td align=center><input type=text name="fieldA" size=5></td>
	<td align=center><input type=text name="fieldB" size=5></td>
</tr>
<tr><td><br><br></td></tr>

<tr>
	<td colspan=2 align=center><input type=submit value=Continue></td>
</tr>

</form>
</table>
</body>
</html>
