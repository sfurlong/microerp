<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
	<title>Altaprise, Inc. WebReports</title>
</head>
<SCRIPT language=javascript>

synopened=false;

//Rollovers
bname = navigator.appName;
bvers = parseInt(navigator.appVersion);
if (bname == "Netscape" && bvers >=3) {
   version = "n3";
}

else if (bname == "Microsoft Internet Explorer" && bvers >=3) {
   version = "n3";
}

else {
   version ="n2";
}

if (version == "n3") {
   financial_on      = new Image;
   financial_on.src  = "images/tab_financial_mo.gif";
   financial_off     = new Image;
   financial_off.src = "images/tab_financial.gif";
   
   acctrec_on      = new Image;
   acctrec_on.src  = "images/tab_acct_receive_mo.gif";
   acctrec_off     = new Image;
   acctrec_off.src = "images/tab_acct_receive.gif";
   
   acctpay_on      = new Image;
   acctpay_on.src  = "images/tab_acct_pay_mo.gif";
   acctpay_off     = new Image;
   acctpay_off.src = "images/tab_acct_pay.gif";

   inventory_on      = new Image;
   inventory_on.src  = "images/tab_inventory_mo.gif";
   inventory_off     = new Image;
   inventory_off.src = "images/tab_inventory.gif";

   dynamic_on      = new Image;
   dynamic_on.src  = "images/tab_dynamic_mo.gif";
   dynamic_off     = new Image;
   dynamic_off.src = "images/tab_dynamic.gif";

   admin_on      = new Image;
   admin_on.src  = "images/tab_admin_mo.gif";
   admin_off     = new Image;
   admin_off.src = "images/tab_admin.gif";
}


function active(iname) {
  if (version == "n3") {
     imagesOn = eval(iname + "_on.src");
     document[iname].src = imagesOn;
  }
}


function inactive(iname) {
  if (version == "n3") {
     imagesOff = eval(iname + "_off.src");
     document[iname].src = imagesOff;
  }
}

function opening(file, name)
   {
    //Change options components  to modify pop-up windows look
	options = "toolbar=1,status=0,menubar=1,scrollbars," +
			"resizable,location=0, width=770,height=415,top=20,left=0";  
   	window.open(file,name,options);
   }


</SCRIPT>

<%@ page language="java" import="dai.shared.csAdapters.*,
	dai.shared.cmnSvcs.*, dai.shared.businessObjs.*;" %>

<% 
	String userId = (String) session.getAttribute("userId");
	//Check if the user's session is still valid.  If not, redirect to the 
	//timeout page.
	if ((session == null) || (userId == null)) { 
    	response.sendRedirect("webrptslogin.jsp"); 
    } 
	
    csSecurity security = (csSecurity) session.getAttribute("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
	String tabId = request.getParameter("TABID");

	//check if user is administrator
    String sqlStmt = "select is_administrator from user_profile " + 
                    " where id = '" + userId + "' and " + 
                    " locality = '" + user_profileObj.getObjLocality() + "'";
    DBRecSet rptRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);
    String isAdmin = "N";
    if (rptRecSet.getSize() > 0) {
        isAdmin = rptRecSet.getRec(0).getAttribVal(user_profileObj.IS_ADMINISTRATOR);
    }
    
    //Now get all the reports on this tab that this user has permissions for.
    sqlStmt = " select rpts.id, rpt_name, rpt_url, has_read_access " +
                     " from webrpts_rpt_settings rpts left join webrpts_security sec on rpts.id = sec.id " +
                     " where sec.userid = '" + userId + "' and tab_id = '"+tabId+"' and " +
                     " sec.has_read_access = 'Y' ";
    rptRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);

%>

<body>
<table cellspacing="0" cellpadding="0" border="0" width=100%>
	<tr>
		<td colspan="2"><img src="images/page-header.gif" border="0" alt="Altaprise, Inc."></td>
	</tr>
	<tr>
		<td colspan="2" align="left">
		<% if (tabId.equals("FINANCIAL")) { %>
			<img src="images/tab_financial_sel.gif" width="75" height="25" border="0" alt="Financials" name="financial">
		<% } else { %>
			<a href="webrptsmain.jsp?TABID=FINANCIAL" onMouseOver="active('financial')" onMouseOut="inactive('financial')">
			<img src="images/tab_financial.gif" width="75" height="25" border="0" alt="Financials" name="financial"></a>
		<% } %>		
			
		<% if (tabId.equals("ACCT_REC")) { %>
			<img src="images/tab_acct_receive_sel.gif" width="133" height="25" border="0" alt="Accounts Receivable" name="acctrec">
		<% } else { %>
			<a href="webrptsmain.jsp?TABID=ACCT_REC" onMouseOver="active('acctrec')" onMouseOut="inactive('acctrec')">
			<img src="images/tab_acct_receive.gif" width="133" height="25" border="0" alt="Accounts Receivable" name="acctrec"></a>
		<% } %>		
			 
		<% if (tabId.equals("ACCT_PAY")) { %>
			<img src="images/tab_acct_pay_sel.gif" width="117" height="25" border="0" alt="Accounts Payable" name="acctpay">
		<% } else { %>
			<a href="webrptsmain.jsp?TABID=ACCT_PAY" onMouseOver="active('acctpay')" onMouseOut="inactive('acctpay')">
			<img src="images/tab_acct_pay.gif" width="117" height="25" border="0" alt="Accounts Payable" name="acctpay"></a>
		<% } %>		

		<% if (tabId.equals("INVENTORY")) { %>
			<img src="images/tab_inventory_sel.gif" width="71" height="25" border="0" alt="Inventory" name="inventory">
		<% } else { %>
			<a href="webrptsmain.jsp?TABID=INVENTORY" onMouseOver="active('inventory')" onMouseOut="inactive('inventory')">
			<img src="images/tab_inventory.gif" width="71" height="25" border="0" alt="Inventory" name="inventory"></a>
		<% } %>		

		<% if (tabId.equals("ADD_HOC")) { %>
			<img src="images/tab_dynamic_sel.gif" width="113" height="25" border="0" alt="Dynamic Reports" name="dynamic">		<% } else { %>
			<a href="webrptsmain.jsp?TABID=ADD_HOC" onMouseOver="active('dynamic')" onMouseOut="inactive('dynamic')">
			<img src="images/tab_dynamic.gif" width="113" height="25" border="0" alt="Dynamic Reports" name="dynamic"></a>
		<% } %>		
		
		<% if (isAdmin.equals("Y")) { %>
			<% if (tabId.equals("ADMIN")) { %>
				<img src="images/tab_admin_sel.gif" width="113" height="25" border="0" alt="WebRpts Administration" name="admin">
			<% } else { %>
				<a href="webrptsmain.jsp?TABID=ADMIN" onMouseOver="active('admin')" onMouseOut="inactive('admin')">
				<img src="images/tab_admin.gif" width="113" height="25" border="0" alt="WebRpts Administration" name="admin"></a>
			<% } %>		
		<% } %>		
		</td>
	</tr>
<!-- Tab content area -->
	<tr>
		<td bgcolor="#666699"><br>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
			<tr>
			<td bgcolor="#666699"><br></td>
			<td>
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
     
    if (rptRecSet.getSize() <= 0 && !tabId.equals("ADMIN")) {
%>
        <center>
        No Reports For This Tab
        </center>
<%        
    }
	
	if (isAdmin.equals("Y") && tabId.equals("ADMIN")) {
%>
            <li>
                <A href="#" onclick="opening('admin0.jsp', 'Administration')">
                    WebRpts Administration
                </A>
            </li>
<%        
    }
%>
	
			</td>
			<td bgcolor="#666699"><br></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td bgcolor="#666699"><br></td>
	</tr>

<!-- End of Tab Content -->	

</table>


</body>
</html>
