<html>
<head>
<title>Admin Tool</title>
</head>

<%@ page language="java" import="dai.shared.csAdapters.*,
	dai.shared.cmnSvcs.*, dai.shared.businessObjs.*;" %>

<body>

<%  csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    SessionMetaData sessionMeta = SessionMetaData.getInstance();
    csSecurity security = (csSecurity) session.getValue("csSecurity");
      
    //Make sure this is an Admin user.
    String userId = (String) session.getValue("userId");
    String sqlStmt= " select is_administrator " +
                   " from user_profile " +
                   " where id = '" + userId + "'";
    DBRecSet adminRecs = dbAdapter.execDynamicSQL(security, sqlStmt);
    String isAdmin = adminRecs.getRec(0).getAttribVal(user_profileObj.IS_ADMINISTRATOR);
    if (isAdmin != null && isAdmin.equals("Y")) {
        //Continue
    } else {
        response.sendRedirect("http://" + sessionMeta.getWebRptsHost() + "/error401.html");
    }
    
    sqlStmt = "select ID from user_profile order by id";
    DBRecSet userRecSet = dbAdapter.execDynamicSQL(security, sqlStmt); %>

<form action=admin1.jsp>
<table border=0 cellspacing=0 cellpadding=0 width=100% >
    <tr>
	    <td align=center><h1>Web Reports Administration</h1></td>
    </tr>
    <tr><td><hr></td></tr>
</table>

<br>

<table border=0 cellpadding=0 cellspacing=0 bgcolor=#6290D8 width=100% >
<tr><td><br></td></tr>
<tr>
	<td align=center>This administrator tool allows you to choose which reports a specific
		user may access.</td>
</tr>
<tr>
	<td align=center><br>First, select a user from the list below:</td>
<tr><td><br><br></td></tr>
<tr>
	<td align=center>
    <select name=USERID>
        <% for (int i=0; i < userRecSet.getSize(); i++) { %>
	        <option value=<%=userRecSet.getRec(i).getAttribVal(user_profileObj.ID)%>>
	        <%=userRecSet.getRec(i).getAttribVal(user_profileObj.ID)%><br>
        <% } %>
</td></tr>
<tr><td><br></td></tr>
<tr>
	<td align=center>
        <input type=submit value=Continue>
    </td>
</tr>
<tr><td><br><br><br><br><br><br><br><br>
</table>

</form>
</body>
</html>
