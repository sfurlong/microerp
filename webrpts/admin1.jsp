<html>
<head>
<title>Admin Tool</title>
</head>


<%@ page language="java" import="dai.shared.csAdapters.*,
	dai.shared.cmnSvcs.*,
    dai.shared.businessObjs.*;" %>



<body>

<% 
    String userId = request.getParameter("USERID");
    
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    csSecurity security = (csSecurity) session.getValue("csSecurity");

    
    //check if user is administrator
    String sqlStmt = " select rpts.id, rpt_name, has_read_access, userid " +
                     " from webrpts_rpt_settings rpts left join webrpts_security sec on rpts.id = sec.id " +
                     " and sec.userid = '" + userId + "'";
    DBRecSet recSet = dbAdapter.execDynamicSQL(security, sqlStmt);
    
%>


<table border=0 cellspacing=0 cellpadding=0 width=100% >

<tr>
    <td align=center><h1>Web Reports Administration</h1></td>
</tr>

<tr><td><hr></td></tr>

</table><br>

<form action="admin2.jsp" method=post>

<center>

<table border=0 cellspacing=0 cellpadding=0 bgcolor=#6290D8 width=100% >

<tr>

<%  for (int i=0; i < recSet.getSize(); i++) {
        String rptId = recSet.getRec(i).getAttribVal(webrpts_rpt_settingsObj.ID);
        String rptName = recSet.getRec(i).getAttribVal(webrpts_rpt_settingsObj.RPT_NAME);
        String permission = recSet.getRec(i).getAttribVal(webrpts_securityObj.HAS_READ_ACCESS);
%>
        <td>
            <input  type=checkbox 
                    name=rptRights 
                    value=<%=rptId%> <%=permission != null && permission.equals("Y") ? "CHECKED" : ""%>>
            <%=rptName%>
        </td>

<%      if ((i%2) != 0) { %>
            </tr><tr>
<%   
        }
    } 
%>

</tr>
<tr>
	<td colspan=2 align=center><br>
        <input type=submit value=Submit>
    </td>
</tr>

</table>

<input name="USERID" type="hidden" value=<%=userId%>>

</form>

</body>
</html>
