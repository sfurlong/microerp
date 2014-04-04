<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<title>Customer Payment History</title>

<script language="JavaScript" src="dateScript.js"></script>

</head>

<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*,
    dai.shared.cmnSvcs.*, java.util.*" 
%>


<%  
    csSecurity security = (csSecurity) session.getAttribute("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

    String custId = request.getParameter("parm0");
    String custLocality = shipmentObj.getObjLocality();
    String custNameSQL = " select name from customer where id = '" + custId + "' and locality = '" + custLocality + "'";
    
    Vector vect = dbAdapter.getDynamicSQLResults(security, custNameSQL);
    String custName = (String)((Vector)vect.elementAt(0)).elementAt(0);
    
    String sqlStmt = "SELECT SHIPMENT.DATE_CREATED, SHIPMENT.ID, SHIPMENT.PO_NUM, SHIPMENT.CUSTOMER_ID, " +
                    " SHIPMENT.TOTAL_VALUE, SHIPMENT.TOTAL_CASH_RECEIVED, CASH_RECEIPT.DATE_RECEIVED, " +
                    " CASH_RECEIPT.DATE_RECEIVED - SHIPMENT.DATE_CREATED, " +
                    " SHIPMENT.TOTAL_VALUE - SHIPMENT.TOTAL_CASH_RECEIVED, SHIPMENT.CUSTOMER_NAME " +
                    " FROM " +
                    " SHIPMENT left join CASH_RECEIPT on shipment.id = cash_receipt.shipment_id and " +
                    " SHIPMENT.LOCALITY = CASH_RECEIPT.LOCALITY " +
                    " WHERE " + 
                    " SHIPMENT.CUSTOMER_ID = '" + custId + "' AND " + 
                    " SHIPMENT.LOCALITY = '" + custLocality + "' and " + 
                    " (shipment.is_canceled is null or shipment.is_canceled = 'N') " + 
                    " ORDER BY " + 
                    " SHIPMENT.DATE_CREATED ASC, " + 
                    " SHIPMENT.ID ASC ";

    System.out.println(sqlStmt);
    
    Vector rowsVect = dbAdapter.getDynamicSQLResults(security, sqlStmt);
    
%>

<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.dateForm.parm0.focus()">

<center>
<center>
<h1>Customer Statement for<BR> <%=custName%></h1>
</center>

<hr>

<TABLE BORDER=1 width=650>
    <tr bgcolor=darkblue>
	    <td>
        <FONT color=white><b>        
        <u>Date</u>
        </b></FONT>
        </td>
	    <td>
        <FONT color=white><b>        
        <u>Invoice</u>
        </b></FONT>
        </td>
	    <td>
        <FONT color=white><b>        
        <u>PO Number</u>
        </b></FONT>
        </td>
	    <td>
        <FONT color=white><b>        
        <u>Days Paid</u>
        </b></FONT>
        </td>
	    <td>
        <FONT color=white><b>        
        <u>Amt Due</u>
        </b></FONT>
        </td>
	    <td>
        <FONT color=white><b>        
        <u>Amt Paid</u>
        </b></FONT>
        </td>
	    <td>
        <FONT color=white><b>        
        <u>Balance</u>
        </b></FONT>
        </td>
    </tr>

    <%
    double totBalance = 0;
    for (int i=0; i<rowsVect.size(); i++) {
        Vector colVect = (Vector)rowsVect.elementAt(i);
        String daysPaid = (String)colVect.elementAt(7);
        if (daysPaid == null) {
            daysPaid = "<center>-</center>";
        } else {
        	daysPaid = new Long((long)Double.parseDouble(daysPaid)).toString();
        }

        String s_amtDue = (String)colVect.elementAt(4);
        String s_amtPaid = (String)colVect.elementAt(5);
        String s_balance = (String)colVect.elementAt(8);
        if (s_amtDue == null) s_amtDue = "0.00";
        if (s_amtPaid == null) s_amtPaid = "0.00";
        if (s_balance == null) s_balance = "0.00";
        double d_amtDue = Double.parseDouble(s_amtDue);
        double d_amtPaid = Double.parseDouble(s_amtPaid);
        double d_balance = Double.parseDouble(s_balance);
        s_amtDue = daiFormatUtil.doubleToCurrency(d_amtDue, false);
        s_amtPaid = daiFormatUtil.doubleToCurrency(d_amtPaid, false);
        s_balance = daiFormatUtil.doubleToCurrency(d_balance, false);
        totBalance += d_balance;

    %>
        <tr>
            <td><%=(String)colVect.elementAt(0)%></td>
            <td><%=(String)colVect.elementAt(1)%></td>
            <td><%=(String)colVect.elementAt(2)%></td>
            <td align=right><%=daysPaid%></td>
            <td align=right><%=s_amtDue%></td>
            <td align=right><%=s_amtPaid%></td>
            <td align=right><%=s_balance%></td>
        </tr>
    <%}%>
    
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td></td>
    <td><b>Total:</b></td>
    <td align=right><%=daiFormatUtil.doubleToCurrency(totBalance, true)%></td>
    </tr>
    
  
</TABLE>

</body>
</html>