<html>
<head>
<title>General Ledger</title>

<script language="JavaScript" src="dateScript.js"> </script>

</head>

<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*, dai.shared.businessObjs.*,
    dai.shared.cmnSvcs.*, java.util.Vector;" %>

<%  
    csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    String sqlStmt = "select distinct ID, DESCRIPTION from ACCOUNT";
    Vector vect = dbAdapter.getDynamicSQLResults(security, sqlStmt);
    String[] acctCodes = new String [vect.size()];
    String[] acctNames = new String [vect.size()]; 
    String temp ="";

    for (int i=0; i < acctCodes.length; i++)
    {
      temp=vect.elementAt(i).toString();
      acctCodes[i]=temp.substring(1, (temp.indexOf(",")));
      acctNames[i]=temp.substring(temp.indexOf(",")+1, temp.length()-1);
    } %>

<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.focus()">
<center>

<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0  bgcolor="#cc99ff" width=100%>
  <tr>
	<td colspan=2 align=center><H1>General Ledger</H1></td>
  </tr>

  <tr>
	<td colspan=2 align=center><H2>Report Parameters</H2></td>
  </tr>
  <tr><td><br></td></tr>

  <tr>  <FORM  name=dateForm action=xmlrptservlet method=POST>
	<td colspan=2 align=center>Select an account:</td>
  </tr>
  <tr>
	<td colspan=2 align=center><select name=parm2 size=5>
<!--		<option value= "%" selected>All Accounts<br>-->
	<% for (int i=0; i < acctCodes.length; i++) { %>
             	<option value= "<%=acctCodes[i] %>" <%=(i==0?"selected":"")%> > <%=acctCodes[i]%> <%=acctNames[i]%><br>
	<% } %>
	</select></td>
  </tr>
  <tr><td><br><br></td></tr>
  <tr>
	<td align="center">Start Date: <input type=text name=parm0 size=15>
		<a href="#" onClick="window.dateField = document.dateForm.parm0;window.open('calendar.html','cal','WIDTH=200,HEIGHT=280')">
		<IMG SRC="images/calendar.gif" BORDER=0></a></td>
	<td align="center">End Date:<input type=text name=parm1 size=15>
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
	<td align="right"><input type=hidden  value="gl.xml" name=rptName  size=15>
		<input type=submit value=Submit> &nbsp;</td> 
	<td align="left"><input type=reset value=Reset> &nbsp;</td>
  </tr>
  <tr><td>&nbsp;<br><br></td><td>&nbsp</td></tr>

</form>
</TABLE>

</center>
</body>
</html>