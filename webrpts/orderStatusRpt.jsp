<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<title>Order Status Report</title>

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

    String sqlStmt = "select ID, DESCRIPTION from account order by id";
    DBRecSet acctRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);

%>

<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.focus()" bgcolor="#33cccc">
<center>
    <H1>Order Status Report</H1>
    <hr>
</center>

<center>

<FORM  name=dateForm action=orderStatusRpt2.jsp method=POST>
<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 bgcolor="#33cccc" width=100%>


  <tr>
  <td colspan=3 align=center>
  Enter one or more filtering criteria.
  </td>
  </tr>

  <tr>
  <td colspan=3 align=center>
  <table>
  <tr>
    <td colspan=3 align=right>Cust PO Id:</td>
    <td><input name="CUST_PO_ID" value=<%=(request.getParameter("CUST_PO_ID")!=null?request.getParameter("CUST_PO_ID"):"\"\"")%>></td>
  </tr>
  <tr>
    <td colspan=3 align=right>Order Id:</td>
    <td><input name="CUST_ORD_ID" value=<%=(request.getParameter("CUST_ORD_ID")!=null?request.getParameter("CUST_ORD_ID"):"\"\"")%>></td>
  </tr>
  <tr>
    <td colspan=3 align=right>Cust Id:</td>
    <td><input name="CUST_ID" value=<%=(request.getParameter("CUST_ID")!=null?request.getParameter("CUST_ID"):"\"\"")%>></td>
  </tr>
  <tr>
    <td colspan=3 align=right>Cust Name:</td>
    <td><input name="CUST_NAME" value=<%=(request.getParameter("CUST_NAME")!=null?request.getParameter("CUST_NAME"):"\"\"")%>></td>
  </tr>
  </table>
  </td>
  </tr>

  <tr><td><br></td></tr>

  <tr>
	<td align="center">Start Date: <input type=text name="BEGIN_DATE" size=15>
		<a href="#" onClick="window.dateField = document.dateForm.BEGIN_DATE;window.open('calendar.html','cal','WIDTH=200,HEIGHT=280')">
		<IMG SRC="images/calendar.gif" BORDER=0></a></td>
	<td align=center>End Date:<input type=text name="END_DATE" size=15>
		<a href="#" onClick="window.dateField = document.dateForm.END_DATE;window.open('calendar.html','cal','WIDTH=200,HEIGHT=280')">
		<IMG SRC="images/calendar.gif" BORDER=0></a></td>

	<td colspan=2 align=center>Choose a date range:
          <select name="range" onchange="showDates(range, BEGIN_DATE, END_DATE);">
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


  <tr><td><br></td></tr>

</TABLE>

    <center>
	<input type=submit value=Submit>
    <input type=reset value=Reset>
    </center>

</form>

</center>
</body>
</html>