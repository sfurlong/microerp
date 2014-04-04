<html>
<head>
<title>Dynamic Reports</title>
<!--Beginning of javascript function-->

<SCRIPT language="JavaScript">
  <!--
  function opening(file, name)
   {
    //Change options components  to modify pop-up windows look
	options = "toolbar=1,status=0,menubar=1,scrollbars," +
			"resizable,location=0, width=770,height=415,top=20,left=0";  
   	window.open(file,name,options);
   }
  // -->
  </SCRIPT>

  <!--End of Script-->

</head>


<%@ page language="java" import="dai.shared.csAdapters.*,
	dai.shared.cmnSvcs.*, dai.shared.businessObjs.*, java.util.Vector;" %>

<% 
    csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

    //check if user is administrator
    String userId = (String) session.getValue("userId");
    String s_hasPermissions = null;
    String exp = null;
    String sqlStmt = "select is_administrator from user_profile " + 
                    " where id = '" + userId + "' and " + 
                    " locality = '" + user_profileObj.getObjLocality() + "'";
    DBRecSet rptRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);
    String isAdmin = null;
    if (rptRecSet.getSize() > 0) {
        isAdmin = rptRecSet.getRec(0).getAttribVal(user_profileObj.IS_ADMINISTRATOR);
    }
    
    //Now get all the reports on this tab that this user has permissions for.
    sqlStmt = " select rpts.id, rpt_name, rpt_url, has_read_access " +
                     " from webrpts_rpt_settings rpts left join webrpts_security sec on rpts.id = sec.id " +
                     " where sec.userid = '" + userId + "' and tab_id = 'ADD_HOC' and " +
                     " sec.has_read_access = 'Y' ";
    rptRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);

    //get any saved custom reports
    UserSavedReportsManager userSavedRptMgr = UserSavedReportsManager.getInstance();
    Vector rptTitles = userSavedRptMgr.getTitles(userId);
    Vector tempVect = new Vector();
    String savedRpts[] = new String[rptTitles.size()];
    String temp = "";
    for(int i=0; i < rptTitles.size(); i ++)
    {
        //temp = rptTitles.get(i).toString();
        tempVect = (Vector) rptTitles.get(i);
        //savedRpts[i] = temp.substring(1, temp.indexOf("]"));
        savedRpts[i] = "" + tempVect.get(0); 
    } 
%>
      

<!------------START OF HTML-------------->
<body leftmargin="75" topmargin="10">
<center>
<!-- Begin of outer Table -->
<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor=#ff9900>

<tr>
	<a href="http://www.digitalartifacts.com"><img src="images/dailogosmall.gif" border=0 alt="Digital Artifacts"></a>
	<img src="images/webRpts.gif" border=0>
<% if (isAdmin != null && isAdmin.equals("Y")) { %>
  	<input type=button onclick="opening('admin0.jsp', 'admin');" value="Admin area">
   <% } %>
</tr>
<hr><BR>

<tr>
<IMG align=absBottom border=0 src="images/tab.gif" useMap=#map> 
</tr>

<tr bgcolor=#ff9900>
	<td><BR>

<!-- Tab content area -->
<UL>

<%  
    for (int i=0; i<rptRecSet.getSize(); i++) {
        String rptUrl = rptRecSet.getRec(i).getAttribVal(webrpts_rpt_settingsObj.RPT_URL);
        String rptName = rptRecSet.getRec(i).getAttribVal(webrpts_rpt_settingsObj.RPT_NAME);
%>
            <li>
                <A href="#" onclick="opening('<%=rptUrl%>', 'invrpt<%=i%>')">
                    <%=rptName%>
                </A>
            </li>
<%  }


   for (int i=0; i < savedRpts.length; i++)
   { %>
	<li><A href="#" onclick="opening('custom.jsp?title=<%=savedRpts[i]%>', 'dynrpt<%=i+2%>')"><%=savedRpts[i]%></A>
	    A saved custom report.
<% } %> 

</UL>
	</td>
<tr> 
    <td>
	<% String display = "";
   	   for (int i=0; i< (14-rptRecSet.getSize()-rptTitles.size()); i++)
   	   {
     	     display += "<br>";
   	   }
        %>
    <%=display%>
    </td>
</tr>

<!-- End of Tab Content -->
<tr align="center" bgcolor=#ffffff>
<td><BR><BR><hr></td>
</tr>

<tr align="center" bgcolor=#ffffff>
<td>
<font face='Arial' color=#000080 size=1>Copyright Digital Artifacts, Inc. 2000-2004</font>
</td>
</tr>


<!--End of Outer Table-->
</TABLE>
</center>

<map name="map">
<area shape="RECT" alt="Financials" coords="1,0,85,25" href="financials.jsp" title="Financials">
<area shape="RECT" alt="Accounts Receivable" coords="92,0,230,25" href="acc_rec.jsp" title="Accounts Receivable">
<area shape="RECT" alt="Accounts Payable" coords="238,0,361,25" href="acc_pay.jsp" title="Accounts Payable">
<area shape="RECT" alt="Inventory" coords="369,0,458,25" href="inventory.jsp" title="Inventory">
<area shape="RECT" alt="Dynamic Reports" coords="464,0,614,25" href="dyn_rpts.jsp" title="Dynamic Reports">
</map>

</body>
</html>