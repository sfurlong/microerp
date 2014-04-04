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
    String _beginDate   = request.getParameter("BEGIN_DATE");
    String _endDate   = request.getParameter("END_DATE");
    String _custId      = request.getParameter("CUST_ID");
    if (_custId.trim().equals("")) _custId = "%";
    String _custName    = request.getParameter("CUST_NAME");
    if (_custName.trim().equals("")) _custName = "%";
    String _ordId       = request.getParameter("CUST_ORD_ID");
    if (_ordId.trim().equals("")) _ordId = "%";
    String _custPOId    = request.getParameter("CUST_PO_ID");
    if (_custPOId.trim().equals("")) _custPOId = "%";
    if(_custName.indexOf("'") != -1) {
      int pos=_custName.indexOf("'");
      _custName = (_custName.substring(0,pos) + "'" + _custName.substring(pos));
    }



    csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

    String sqlStmt = "select cust_order.ID, customer_id, customer_name, po_num, cust_order.date_created, " +
            " item_id, qty_ordered, qty_shipped, expected_ship_date " +
            " from cust_order co, cust_order_item coi " +
            " where customer_id like '" + _custId + "%' and " +
                   " customer_name like '" + _custName  + "%' and " +
                   " po_num like '" + _custPOId + "%' and " +
                   " co.id like '" + _ordId + "%' and " +
                   " co.id = coi.id and co.locality = coi.locality ";
    if (!_beginDate.trim().equals("") && !_endDate.trim().equals("")) {

        sqlStmt += " and date_created between '" + _beginDate + "' and '" + _endDate + "'";
    }

    sqlStmt += " order by co.id, co.date_created, item_id";
    DBRecSet ordRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);

%>

<center>
<h1>Order Status</h1>
<hr>
</center>
<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.focus()">
<center>

<FORM  name=dateForm action=xmlrptservlet method=POST>
<TABLE border=1>
    <tr bgcolor=darkblue>
        <td align=center><FONT color=white><B><U>Order Id</font></b></u></td>
        <td align=center><FONT color=white><B><U>Order Date</font></b></u></td>
        <td align=center><FONT color=white><B><U>Customer PO Num</font></b></u></td>
        <td align=center><FONT color=white><B><U>Customer Id</font></b></u></td>
        <td align=center><FONT color=white><B><U>Customer Name</font></b></u></td>
        <td align=center><FONT color=white><B><U>Item Id</font></b></u></td>
        <td align=center><FONT color=white><B><U>Qty Ord</font></b></u></td>
        <td align=center><FONT color=white><B><U>Qty Ship</font></b></u></td>
        <td align=center><FONT color=white><B><U>Est. Ship</font></b></u></td>
        <td align=center><FONT color=white><B><U>Shipped?</font></b></u></td>
    </tr>
	    <%
        for (int i=0; i<ordRecSet.getSize(); i++) {
            String dateCreated = ordRecSet.getRec(i).getAttribVal(cust_orderObj.DATE_CREATED);
            String custId = ordRecSet.getRec(i).getAttribVal(cust_orderObj.CUSTOMER_ID);
            String custName = ordRecSet.getRec(i).getAttribVal(cust_orderObj.CUSTOMER_NAME);
            String custPOId = ordRecSet.getRec(i).getAttribVal(cust_orderObj.PO_NUM);
            String ordId   = ordRecSet.getRec(i).getAttribVal(cust_orderObj.ID);
            String qtyOrd   = ordRecSet.getRec(i).getAttribVal(cust_order_itemObj.QTY_ORDERED);
            String itemId   = ordRecSet.getRec(i).getAttribVal(cust_order_itemObj.ITEM_ID);
            String qtyShip   = ordRecSet.getRec(i).getAttribVal(cust_order_itemObj.QTY_SHIPPED);
            if (qtyShip == null) qtyShip = "-";
            String shipDate   = ordRecSet.getRec(i).getAttribVal(cust_order_itemObj.EXPECTED_SHIP_DATE);
            if (shipDate == null) shipDate = "-";
            if (custPOId == null) custPOId = "<center>-</center>";
        %>
        <%
            boolean hasShipped = false;
            String  shippedString = "-";
            try {
                int i_qtyShip = Integer.parseInt(qtyShip);
                if (i_qtyShip > 0) shippedString = "<a href='orderStatusRpt3.jsp?CUST_ORD_ID=" + ordId + "'>View Shipment</a>";
            } catch(Exception e) {
                hasShipped = false;
            }
        %>
            <tr>
            <td><%=ordId%></td>
            <td><%=dateCreated%></td>
            <td><%=custPOId%></td>
            <td><%=custId%></td>
            <td><%=custName%></td>
            <td><%=itemId%></td>
            <td align=center><%=qtyOrd%></td>
            <td align=center><%=qtyShip%></td>
            <td align=center><%=shipDate%></td>
            <td align=center><%=shippedString%></td>

            </tr>

	    <% } %>
    </td>
  </tr>


</TABLE>
</form>

</center>
</body>
</html>