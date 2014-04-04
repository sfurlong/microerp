<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page import="java.net.*"%>
<html>
<head>
<title>Vendor BackOrder By Vendor</title>
</head>

<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.focus()">
<center>
<FORM  action=xmlrptservlet method=POST>
<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor="#99cc00" width=100%>
  <tr bgcolor="#99cc00">
	<td colspan=2 align=center><H1>Vendor BackOrder By Vendor</H1></td>
  </tr>

  <tr>
	<td colspan=2 align=center><H2>Report Parameters</H2></td>
  </tr>
  <tr>
  <td colspan=2 align=center>
	Enter an <a href="#" onClick="window.open('vendors.jsp', 'vendors', 'WIDTH=350,HEIGHT=400, scrollbars=1')">
		vendor number</a>:
	<input name=parm0 value="<%=(request.getParameter("VEND_ID")!=null?request.getParameter("VEND_ID"):"\"\"")%>" size=15>
  </td>
  </tr>
  <tr><td><br><br><br></td></tr>
  <tr>
	<td colspan=2 align=center><input type=checkbox name=excel value="true">
		<font size=2>View in MS-Excel format</font></td>
  </tr>
  <tr>
	<td colspan=2 align=center><font size=1><a href="help.html#excel">
		What's this?</a></font></td>
  </tr>
  <tr><td><br><br><br></td></tr>

  <tr>
	<td align="center"><input type=hidden  value="vendorBackOrderByVendor.xml" name=rptName  size=15>
		<input type=submit value=Submit> &nbsp;</td>
  </tr>
  <tr><td><br><br><br></td></tr>

</form>
</TABLE>

</center>
</body>
</html>