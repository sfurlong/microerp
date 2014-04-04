<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<title>Purchase Orders By Vendor</title>

<script language="JavaScript" src="dateScript.js"></script>

</head>

<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*, dai.shared.businessObjs.*,
    dai.shared.cmnSvcs.*;" 
%>

<%  
    csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    
    String sqlStmt = "select distinct VENDOR_NAME from PURCH_ORDER where VENDOR_NAME is not null";
    DBRecSet vendorRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);

%>

<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.dateForm.parm2.focus()">
<center>

<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor="#CCFFCC" width=100%>
  <tr bgcolor="#CCFFCC">
	<td colspan=2 align=center><H1>Purchase Order By Vendor</H1></td>
  </tr>

  <tr>
	<td colspan=2 align=center><H3>Report Parameters</H3></td>
  </tr>
  <tr><td><br></td></tr>

  <tr>  <FORM  name=dateForm action=xmlrptservlet method=POST>
	<td colspan=2 align=center>Select a vendor:</td>
  </tr>
  <tr>
	<td colspan=2 align=center><select name=parm2 size=5>
	    <% 
        for (int i=0; i<vendorRecSet.getSize(); i++) { 
            String vendorName = vendorRecSet.getRec(i).getAttribVal(purch_orderObj.VENDOR_NAME);
        %>
             <option value= "<%=vendorName%>"> <%=vendorName%>
	    <% } %>
	</select></td>
  </tr>
  <tr><td><br><br></td></tr>

  <tr>
	<td align="center">Start Date: <input type=text name=parm0 size=15>
		<a href="#" onClick="window.dateField = document.dateForm.parm0;window.open('calendar.html','cal','WIDTH=200,HEIGHT=280')">
		<IMG SRC="images/calendar.gif" BORDER=0></a></td>
	<td align=center>End Date:<input type=text name=parm1 size=15>
		<a href="#" onClick="window.dateField = document.dateForm.parm1;window.open('calendar.html','cal','WIDTH=200,HEIGHT=280')">
		<IMG SRC="images/calendar.gif" BORDER=0></a></td>
  </tr>
  <tr><td><br><br></td></tr>

  <tr>
	<td colspan=2 align=center>Choose a date range:
          <select name="range" onchange="showDates(range, parm0, parm1);">
          <option value="null">- - - - - - - - - - -
	  <option value="currmonth">Month To Date
          <option value="curryear">Year To Date
	  <option value="lastmonth">Last Month
          <option value="null">- - - - - - - - - - -
          <option value="jan">January   <option value="feb">February
          <option value="mar">March     <option value="apr">April
          <option value="may">May       <option value="jun">June
          <option value="jul">July      <option value="aug">August
          <option value="sep">September <option value="oct">October
          <option value="nov">November  <option value="dec">December
          </select> </td>
  </tr>
  <tr><td><br><br></td></tr>

  <tr>
	<td colspan=2 align=center><input type=checkbox name=excel value="true">
		<font size=2>View in MS-Excel format</font></td>
  </tr>
  <tr>
	<td colspan=2 align=center><font size=1><a href="help.html#excel">
		What's this?</a></font></td>
  </tr>
  <tr><td><br><br></td></tr>

  <tr>
	<td align="right"><input type=hidden  value="purchOrderByVendor.xml" name=rptName  size=15>
		<input type=submit value=Submit> &nbsp;</td> 
	<td align="left"><input type=reset value=Reset> &nbsp;</td>
  </tr>
  <tr><td><br><br><br><br></td></tr>

</form>
</TABLE>

</center>
</body>
</html>