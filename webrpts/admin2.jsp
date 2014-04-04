<html>
<head>
<title>Report Rights</title>
</head>

<%@ page language="java" import="dai.shared.csAdapters.*,
	dai.shared.cmnSvcs.*, dai.shared.businessObjs.*;" %>

<body onUnload="opener.focus()">

<%  csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    csSecurity security = (csSecurity) session.getValue("csSecurity");

    String rptRights[] = {};
    if (request.getParameterValues("rptRights") != null)
        rptRights = request.getParameterValues("rptRights");

    String userId = request.getParameter("USERID");

    //First remove all the existing settings.
    String sqlStmt = " delete from webrpts_security "+
	                " where userid='" + userId + "'";
    dbAdapter.execDynamicSQL(security, sqlStmt);


    for (int i=0; i < rptRights.length; i++)
    {
        sqlStmt = " insert into webrpts_security (id, locality, userid, has_read_access) " +
                  " values ('" + rptRights[i] + "', '" +
                  webrpts_securityObj.getObjLocality() + "'," +
                  "'" + userId + "'," +
                  "'Y')";
        dbAdapter.execDynamicSQL(security, sqlStmt); 
    }

%>

Changes made.
<br><br>
<a href="#" onclick="window.close()">Go back</a>

</body>
</html>
