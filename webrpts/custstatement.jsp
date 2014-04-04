<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<title>Customer Payment History</title>

<script language="JavaScript" src="dateScript.js"></script>

</head>

<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*, dai.shared.businessObjs.*,
    dai.shared.cmnSvcs.*,java.util.Vector;" 
%>


<%  
    csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    String sqlStmt = "select distinct CUSTOMER_ID, CUSTOMER_NAME from SHIPMENT order by CUSTOMER_NAME";
    Vector rowVect = dbAdapter.getDynamicSQLResults(security, sqlStmt);

%>

<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.dateForm.parm0.focus()">
<center>

<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor="#CCFFCC" width=100%>
  <tr bgcolor="#CCFFCC">
	<td colspan=2 align=center><H1>Customer Statement</H1></td>
  </tr>

  <tr>
	<td colspan=2 align=center><H3>Report Parameters</H3></td>
  </tr>
  <tr><td><br></td></tr>

  <tr>  
  <FORM  name=dateForm action="custstatement2.jsp" method=POST>
	<td colspan=2 align=center>Select a customer:</td>
  </tr>
  <tr>
	<td colspan=2 align=center>
    <select name=parm0 size=5>
	<% for (int i=0; i < rowVect.size(); i++) { 
        Vector colVect = (Vector)rowVect.elementAt(i);
        String custId = (String)colVect.elementAt(0);
        String custName = (String)colVect.elementAt(1);
    %>
             <option value= "<%=custId%>" > <%=custId + "-" + custName%><br>
	<% } %>
	</select></td>
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
	<td align="right"><input type=hidden  value="custPaymentHist.xml" name=rptName  size=15>
		<input type=submit value=Submit> &nbsp;</td> 
	<td align="left"><input type=reset value=Reset> &nbsp;</td>
  </tr>
  <tr><td><br><br><br></td></tr>

</form>
</TABLE>

</center>
</body>
</html>