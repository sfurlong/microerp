<html>
<head>
<title>Sales By State Or Country</title>
</head>

<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*, dai.shared.businessObjs.*,
    dai.shared.cmnSvcs.*,java.util.Vector;" 
%>

<% String excel = "false";
   if (request.getParameter("excel")!=null) {
     excel = request.getParameter("excel");
   }
   String content = "text/html";
   if (excel.trim().equals("true"))
   {
     content = "application/x-msexcel";
     //response.setHeader("Content-disposition", "attachment");
   }
   response.setContentType(content); %>


<% //VARIABLE DECLARATIONS
   csSecurity _security = (csSecurity) session.getValue("csSecurity");
   csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
   csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
   SessionMetaData _sessionMeta = SessionMetaData.getInstance();

   String state = request.getParameter("state");
   String country = request.getParameter("country");
   String startDate = request.getParameter("parm0");
   String endDate = request.getParameter("parm1");
   String display="";
   String temp="";
   String sqlStatement="select SHIPTO_STATE_CODE, SHIPTO_COUNTRY_CODE,"
	+ " CUSTOMER_NAME, SUBTOTAL, TOTAL_SHIPPING, TOTAL_TAX, TOTAL_VALUE from SHIPMENT";
   double total = 0;
   Vector results = new Vector(); %>

<% //USER VALIDATION
   String userValid= (String) session.getValue("userValid");
   if (!(userValid.equals("true")))
    {
      response.sendRedirect("http://" + _sessionMeta.getWebRptsHost() + "/error401.html");
    }

   boolean rptValid = false;
   String userId = (String) session.getValue("userId");
   String rightsSql = "select HAS_READ_ACCESS from WEBRPTS_SECURITY SEC left join WEBRPTS_RPT_SETTINGS RPTS " +
	"on SEC.ID = RPTS.ID where RPT_NAME='Sales By State Or Country' and SEC.USERID='"+userId+"'";
   try {
	Vector rights = dbAdapter.getDynamicSQLResults(_security, rightsSql);
        Vector tempVect = (Vector) rights.get(0);
	if (tempVect.get(0).toString().equals("Y"))
	  rptValid=true;
   } catch (Exception e) {
	rptValid=false;
	e.printStackTrace();
   }
   System.out.println("rptValid="+rptValid);
   if (!(rptValid))
   {
     response.sendRedirect("http://" + _sessionMeta.getWebRptsHost() + "/error401.html");
   } %>

<% if (state.equals(""))
   {
     if (!country.equals(""))
     {
       display += country;
       sqlStatement += " where SHIPTO_COUNTRY_CODE = '" + country + "'" +
                        " and (is_canceled = 'N' or is_canceled is null)";
     }
     else
     {
       display = "Outside U.S";
       sqlStatement += " where SHIPTO_COUNTRY_CODE != 'US' and SHIPTO_COUNTRY_CODE is not null" +
                        " and (is_canceled = 'N' or is_canceled is null)";
     }
   }
   else
   {
     display += state;
     sqlStatement += " where SHIPTO_STATE_CODE like '" + state + "'";
   }

   if (!(startDate.equals("")) && !(endDate.equals("")))
   {
     sqlStatement +=" and DATE_CREATED between \'" + startDate + '\" and \'" + endDate + "\'";
     display += "   From: " + startDate + " To: " + endDate + "<br>";
   }

   sqlStatement += " order by SHIPTO_COUNTRY_CODE, SHIPTO_STATE_CODE";
   results = dbAdapter.getDynamicSQLResults(_security, sqlStatement);
%>


<center>
<h1>Sales By State or Country
<br><%=display%><hr></h1>
</center>

<body>
<table border=1 width=85% >
  <tr bgcolor=darkblue>
	<td><FONT color=white><b><center><u>State</u></center></b></font></td>
	<td><FONT color=white><b><center><u>Country</u></center></b></font></td>
	<td><FONT color=white><b><center><u>Customer</u></center></b></font></td>
	<td><FONT color=white><b><center><u>Total Product</u></center></b></font></td>
	<td><FONT color=white><b><center><u>Shipping Cost</u></center></b></font></td>
	<td><FONT color=white><b><center><u>Tax</u></center></b></font></td>
	<td><FONT color=white><b><center><u>Total Sales</u></center></b></font></td>
  </tr>
<% for (int i=0; i < results.size(); i ++)
   {
     Vector colVect = (Vector)results.elementAt(i);

     String stateCol = (String)colVect.elementAt(0);
     String countryCol = (String)colVect.elementAt(1);
     String custCol = (String)colVect.elementAt(2);
     String totProductCol = (String)colVect.elementAt(3);
     if (totProductCol == null) totProductCol = "0.0";
     String totShippingCol = (String)colVect.elementAt(4);
     if (totShippingCol == null) totShippingCol = "0.0";
     String totTaxCol = (String)colVect.elementAt(5);
     if (totTaxCol == null) totTaxCol = "0.0";
     String totAmtCol = (String)colVect.elementAt(6);
     if (totAmtCol == null) totAmtCol = "0.0";
%>
  <tr>
	<td><font size=2><%=stateCol%></font></td>
	<td><font size=2><%=countryCol%></font></td>
	<td><font size=2><%=custCol%></font></td>
	<td align=right><font size=2><%=daiFormatUtil.doubleToCurrency(
		Double.parseDouble(totProductCol), true)%></font></td>
	<td align=right><font size=2><%=daiFormatUtil.doubleToCurrency(
		Double.parseDouble(totShippingCol), true)%></font></td>
	<td align=right><font size=2><%=daiFormatUtil.doubleToCurrency(
		Double.parseDouble(totTaxCol), true)%></font></td>
	<td align=right><font size=2><%=daiFormatUtil.doubleToCurrency(
		Double.parseDouble(totAmtCol), true)%></font></td>
			<% total += Double.parseDouble(totAmtCol); %>
  </tr>
<% } %>
  <tr>
	<td></td><td></td><td></td><td></td><td></td>
	<td align=right><font size=2>Total:</font></td>
	<td align=right><font size=2><%=daiFormatUtil.doubleToCurrency(total, true)%></font></td>
  </tr>

</table>
</body>
</html>