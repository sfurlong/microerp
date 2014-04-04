<html>
<head>
<title> Save confirmation </title>

<body>
<%@page language="java" import="dai.shared.cmnSvcs.*;" %>

<% UserSavedReportsManager userSavedRptMgr = UserSavedReportsManager.getInstance();
   String userId=(String)session.getValue("userId");
   String title=request.getParameter("title");
   String sqlStmt=(String)session.getValue("sqlStmt");//request.getParameter("sqlStmt");
   
   String display=""+userSavedRptMgr.saveRpt(userId, title, sqlStmt); %>

<br>
<center>
<font size=4><%=display%></font>
<br><br><br>
<input type=button value="Close" onClick="window.close()">
</center>
</body>
</html>