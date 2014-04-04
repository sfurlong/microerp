<html>
<head>
<title>Custom Report</title>
</head>

<%@ import="dai.shared.cmnSvcs.*" %>

<% 
   UserSavedReportsManager userSavedRptMgr = UserSavedReportsManager.getInstance();
   String title = request.getParameter("title");
   String userId = (String) session.getValue("userId");
   String sqlStatement = userSavedRptMgr.getSQLStatement(userId, title); 
%>

<body>			 
<form name=myForm action=dynamicsqlservlet method=post>
<center>
<textarea name=sqlStmt cols=60 rows=6><%=sqlStatement%></textarea>
<br><br><br>
<input type=submit value="Run <%=title%>">
</center>
</form>
</body>
</html>