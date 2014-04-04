<html>
<head>
<title>results</title>
</head>

<body onload="window.focus();">

<% //creates the "select...from" part of a SQL statement

   String column[] = request.getParameterValues("column");
   String table = request.getParameter("table");
   String statement ="select ";
   String selectedColumns ="";

   for (int i=0; i< column.length;i++)
   {
     statement+=column[i] + ", ";
     selectedColumns+=column[i] + " ";
   }
   statement=statement.substring(0, ((statement.length()) -2));
   statement+=" from " + table; 
%>

<% session.putValue("statement", statement);
   session.putValue("selectedColumns", selectedColumns); %>

<jsp:forward page="DynamicSQLServlet.jsp"/>

</body>
</html>