<html>
<head>
<title> Sort Data </title>
</head>

<body onload="window.focus();">

<%@ page language="java" %>

<% String restrictedColumn = request.getParameter("restrictedColumn");
   String operator = request.getParameter("operator");
   String fieldA = request.getParameter("fieldA");
   String fieldB = request.getParameter("fieldB");
   String clause = "";
   String selectedColumns= "" + (String) session.getValue("selectedColumns");
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

<% //create sql "where" clause
   if (!(restrictedColumn.equals("none")))
   {
     clause= "where " + restrictedColumn + " " + operator;
     if (!(operator.equals("is not null")))
     {
       clause += " '" + fieldA + "'";
       if (operator.equals("between"))
         clause += " and '" + fieldB + "'";
     }
   clause+=" ";
   }
   session.putValue("clause", clause); %>

<center>
<table border=0 cellspacing=0 cellpadding=0 width=100% >
<tr>
	<td align=center><h1>Creating Dynamic Reports</h1></td>
</tr>
<tr><td><hr></td></tr>
</table><br>

<table border=0 cellspacing=5 cellpadding=0 bgcolor=#ff9900 width=100% >
<tr>
	<td align=center><h1>Step 4</h1></td>
</tr>
<tr><td></td></tr>

<tr>
	<td>Choose which column to sort by.</td>
</tr>

<tr>
	<td><font size=1><a href="help.html#sort">What does this do?</font></a></td>
</tr>
<tr><td><br></td></tr>

<tr><form action="dynamicsqlservlet.jsp" method=post>
	<td> <select name=sortColumn>
		<option value="null">Select None<br>
		<% //displays all columns in the selected table
                   for (int j=0; j < i; j++)
		   { %>
		     <option value =<%=columnNames[j]%>><%=columnNames[j]%><br>
		<% } %> </td>
</tr>
<tr><td><br><br></td></tr>

<tr>
	<td>Would you like the results shown in ascending or descending order? </td>
</tr>
<tr><td><br></td></tr>

<tr>
	<td align=center><input type=radio name=orderType value=ascending CHECKED>ascending
	<input type=radio name=orderType value=descending>descending</td>
</tr>
<tr><td><br><br></td></tr>

<tr>
	<td colspan=2 align=center><input type=submit value=Continue></td>
</tr>
<tr><td><br><br><br></td></tr>

</form>
</table>
</body>
</html>