<HTML>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=windows-1252">
<TITLE>Cash Receipts (Credit)</TITLE>

<script language="JavaScript" src="dateScript.js"></script>

</HEAD>

<BODY>
<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*, dai.shared.businessObjs.*,
    dai.shared.cmnSvcs.*,java.util.Vector;" %>


<% 

csSecurity security = (csSecurity) session.getValue("csSecurity");
csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
String sqlExp = " pay_method_type = 'CREDIT CARD' order by pay_method_id ";
Vector vect = dbAdapter.queryByExpression(security, new global_settings_pay_methodsObj(), sqlExp);
String[] creditCards = new String[vect.size()];

for (int i=0; i<creditCards.length; i++) {
    global_settings_pay_methodsObj obj = (global_settings_pay_methodsObj)vect.elementAt(i);
    creditCards[i] = obj.get_pay_method_id(); 
} 
%>

<center>
<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor="#CCFFCC" width=100%>
<tr bgcolor="#CCFFCC">
	<td colspan=2 align=center><H1>Cash Receipts (Credit)</H1></td>
</tr>

<tr>
	<td colspan=2 align=center><H3>Report Parameters</H3></td>
</tr>
<tr><td><br></td></tr>

<FORM  name=dateForm action=xmlrptservlet method=POST>

<tr>
    <td colspan=2 align=center>
    <SELECT NAME="parm2" SIZE=5>
    <% //for each option, value is first 8 chars of returned string
       for(int i=0; i<creditCards.length; i++) { %>
         <OPTION VALUE= <%= creditCards[i] %> > <%= creditCards[i] %> <BR> <%  
       } %>
    </SELECT></td>
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
	<td align="right"><input type=hidden  value="cashReceiptsCredit.xml" name=rptName  size=15>
		<input type=submit value=Submit> &nbsp;</td> 
	<td align="left"><input type=reset value=Reset> &nbsp;</td>
</tr>
<tr><td><br><br></td></tr>
</table>
</form>

</center>
</BODY>
</HTML>