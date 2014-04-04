<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<title>Order Status Report</title>

<script language="JavaScript" src="dateScript.js"></script>

</head>

<%@ page language="java" import="dai.shared.businessObjs.*,
    dai.shared.csAdapters.*,dai.shared.cmnSvcs.*;" 
%>

<%  
    String _ordId       = request.getParameter("CUST_ORD_ID");
    csSecurity security = (csSecurity) session.getAttribute("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    
    String sqlStmt = "select shipment.ID, customer_id, customer_name, air_bill_num, shipment.date_created, is_canceled, " +
            " item_id, qty_ordered, qty_shipped " +
            " from shipment s, shipment_item si " +
            " where order_num = '" + _ordId + "' and " + 
                   " s.id = si.id and s.locality = si.locality" +
                   " order by s.id, shipment.date_created, item_id";
    System.out.println(sqlStmt);
    DBRecSet shipRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);

%>

<center>
<h1>Shipments for Order <%=_ordId%></h1>
<hr>
</center>
<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.focus()">
<center>

<FORM  name=dateForm action=xmlrptservlet method=POST>
<TABLE border=1>
    <tr bgcolor=darkblue>
        <td align=center><FONT color=white><B><U>Invoice#</font></B></U></td>
        <td align=center><FONT color=white><B><U>Date Shipped</font></B></U></td>
        <td align=center><FONT color=white><B><U>Customer Id</font></B></U></td>
        <td align=center><FONT color=white><B><U>Customer Name</font></B></U></td>
        <td align=center><FONT color=white><B><U>Item Id</font></B></U></td>
        <td align=center><FONT color=white><B><U>Qty Ord</font></B></U></td>
        <td align=center><FONT color=white><B><U>Qty Ship</font></B></U></td>
        <td align=center><FONT color=white><B><U>Tracking#</font></B></U></td>
    </tr>
	    <% 
        for (int i=0; i<shipRecSet.getSize(); i++) {
            String dateCreated = shipRecSet.getRec(i).getAttribVal(shipmentObj.DATE_CREATED);
            String custId = shipRecSet.getRec(i).getAttribVal(shipmentObj.CUSTOMER_ID);
            String custName = shipRecSet.getRec(i).getAttribVal(shipmentObj.CUSTOMER_NAME);
            String trackNum = shipRecSet.getRec(i).getAttribVal(shipmentObj.AIR_BILL_NUM);
            String itemId = shipRecSet.getRec(i).getAttribVal(shipment_itemObj.ITEM_ID);
            String shipId   = shipRecSet.getRec(i).getAttribVal(shipmentObj.ID);
            String isCanceled   = shipRecSet.getRec(i).getAttribVal(shipmentObj.IS_CANCELED);
            String qtyOrd   = shipRecSet.getRec(i).getAttribVal(shipment_itemObj.QTY_ORDERED);
            String qtyShip   = shipRecSet.getRec(i).getAttribVal(shipment_itemObj.QTY_SHIPPED);
            if (itemId == null) itemId = "<center>-</center>";
            if (qtyOrd == null) qtyOrd = "-";
            if (qtyShip == null) qtyShip = "-";
            if (trackNum == null) trackNum = "-";
        %>
            <tr> 
            <td><%="<a href=orderStatusRpt4.jsp?SHIPMENT_ID="+shipId+">"+shipId+"</a>"%></td>
            <td><%=dateCreated%></td>
            <td><%=custId%></td>
            <td><%=custName%></td>
            <td><%=itemId%></td>
            <td align=center><%=qtyOrd%></td>
            <td align=center><%=qtyShip%></td>
            <td align=center><%=trackNum%></td>
            <% if (isCanceled != null && isCanceled.equals("Y")) {
            %>
                <td align=center><FONT color=red>CANCELED</font><td>
            <% } %>
                
            </tr>
            
	    <% } %>
    </td>
  </tr>
    

</TABLE>
</form>

</center>
</body>
</html>