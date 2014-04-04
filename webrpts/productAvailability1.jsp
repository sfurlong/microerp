<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<title>Product Availability</title>

<script language="JavaScript" src="dateScript.js"></script>
</head>

<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.focus()">
<center>

<FORM  name=dateForm action=productAvailability2.jsp method=POST>

<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor="#CCFFCC" width=100%>
  <tr bgcolor="#CCFFCC">
	<td colspan=2 align=center><H1>Product Availability</H1></td>
  </tr>

  <tr><td><br></td></tr>

  <tr>
	<td colspan=2 align=center>Enter an <a href="#" onClick="window.open('items.jsp', 'item', 'WIDTH=350,HEIGHT=400, scrollbars=1')">
		item number</a>:</td>
  </tr>

  <tr>
	<td colspan=2 align=center><input type=text name="ITEM_ID" value="<%=(request.getParameter("ITEM_ID")!=null?request.getParameter("ITEM_ID"):"\"\"")%>" size=15></td>
  </tr>
  <tr><td><br></td></tr>

  <tr>
	<td colspan=2 align=center><input type=checkbox name=excel value="true">
		<font size=2>View in MS-Excel format</font></td>
  </tr>
  <tr>
	<td colspan=2 align=center><font size=1><a href="help.html#excel">
		What's this?</a></font></td>
  </tr>
  <tr><td><br></td></tr>

  <tr>
	<td align="right">
		<input type=submit value=Submit> &nbsp;
        </td>
	<td align="left">
                <input type=reset value=Reset> &nbsp;
        </td>
  </tr>
  <tr><td><br><br></td></tr>

</TABLE>
</form>

</center>
</body>
</html>