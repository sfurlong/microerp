<html>
<head>
<title>Product Availability</title>
</head>


<%@page language="java" import="dai.shared.csAdapters.*,
	dai.server.servlets.*, dai.shared.cmnSvcs.*,
    dai.shared.businessObjs.*; " %>

<% String excel = "false";
   if (request.getParameter("excel")!=null) {
     excel = request.getParameter("excel");
   }
   String content = "text/html";
   if (excel.trim().equals("true"))
   {
     content = "application/x-msexcel";
     response.setHeader("Content-disposition", "attachment");
   }
   response.setContentType(content);
%>

<% //VARIABLE DECLARATIONS
   csSecurity security = (csSecurity) session.getValue("csSecurity");
   csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
   csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
   SessionMetaData _sessionMeta = SessionMetaData.getInstance();

   String itemId = request.getParameter("ITEM_ID");
   String startDate = request.getParameter("parm0");
   String endDate = request.getParameter("parm1");
   String showDates="";
   String sqlStmt = "select id, onhand_qty, backorder_to_cust_qty, backorder_to_vendor_qty " +
                        " from item where id like '%" + itemId + "%'";

   DBRecSet invRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);
%>

<center>
<h1>Product Availability for <%=itemId%></h1>
<hr>
</center>

<body>

<h2><B>Current Inventory</center></h2>
<table border=1 width=85% >
<tr bgcolor=darkblue>
    <td><FONT color=white><B><center><U>Item Id</u></center></b></td>
    <td><FONT color=white><B><center><U>Qty Onhand</u></center></b></td>
    <td><FONT color=white><B><center><U>Qty Cust Backorder</u></center></b></td>
    <td><FONT color=white><B><center><U>Qty Vendor Backorder</u></center></b></td>
</tr>
<% for (int i=0; i<invRecSet.getSize(); i++) {
        String invItemId = invRecSet.getRec(i).getAttribVal(itemObj.ID);
        String s_qtyOnHand = invRecSet.getRec(i).getAttribVal(itemObj.ONHAND_QTY);
        if (s_qtyOnHand == null) s_qtyOnHand = "0.0";
        String s_qtyCustBack = invRecSet.getRec(i).getAttribVal(itemObj.BACKORDER_TO_CUST_QTY);
        if (s_qtyCustBack == null) s_qtyCustBack = "0.0";
        String s_qtyVendBack = invRecSet.getRec(i).getAttribVal(itemObj.BACKORDER_TO_VENDOR_QTY);
        if (s_qtyVendBack == null) s_qtyVendBack = "0.0";
%>
    <tr>
        <td><%=invItemId%></td>
        <td align="right"><%=(int)Double.parseDouble(s_qtyOnHand)%></td>
        <td align="right"><%=(int)Double.parseDouble(s_qtyCustBack)%></td>
        <td align="right"><%=(int)Double.parseDouble(s_qtyVendBack)%></td>
    </tr>
<% } %>
</table>

<p>
<h2><center>Un-Shipped Customer Orders</center></h2>
<%
   sqlStmt = "select cust_order_item.id, item_id, qty_ordered, qty_shipped, expected_ship_date, customer_name  " +
                        " from cust_order_item, cust_order where " +
                        " cust_order.id = cust_order_item.id and " +
                        " cust_order.locality = cust_order_item.locality and " +
                        " item_id like '" + itemId + "' and " +
                        " qty_shipped < qty_ordered order by cust_order_item.id, item_id";

   DBRecSet ordRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);
%>
<table border=1 width=85% >
<tr bgcolor=darkblue>
    <td><FONT color=white><B><center><U>Order#</u></center></b></td>
    <td><FONT color=white><B><center><U>Item Id</u></center></b></td>
    <td><FONT color=white><B><center><U>Customer Name</u></center></b></td>
    <td><FONT color=white><B><center><U>Expected Ship Date</u></center></b></td>
    <td><FONT color=white><B><center><U>Qty Ordered</u></center></b></td>
    <td><FONT color=white><B><center><U>Qty Shipped</u></center></b></td>
</tr>
<% for (int i=0; i<ordRecSet.getSize(); i++) { %>

    <tr>
        <td><%=ordRecSet.getRec(i).getAttribVal(cust_order_itemObj.ID)%></td>
        <td><%=ordRecSet.getRec(i).getAttribVal(cust_order_itemObj.ITEM_ID)%></td>
        <td><%=ordRecSet.getRec(i).getAttribVal(cust_orderObj.CUSTOMER_NAME)%></td>
        <td><center>-</center></td>
        <td align="right"><%=ordRecSet.getRec(i).getAttribVal(cust_order_itemObj.QTY_ORDERED)%></td>
        <td align="right"><%=ordRecSet.getRec(i).getAttribVal(cust_order_itemObj.QTY_SHIPPED)%></td>
    </tr>
<% } %>
</table>

<p>
<h2><center>Un-Received Purchases</center></h2>
<%
   sqlStmt = "select purch_order_item.id, item_id, qty_ordered, qty_received, date_item_expected, cust_name  " +
                        " from purch_order_item, purch_order " +
                        " where purch_order.id = purch_order_item.id and " +
                        " purch_order.locality = purch_order_item.locality and " +
                        " item_id like '" + itemId + "' and " +
                        " qty_received < qty_ordered order by purch_order.id, item_id";

   DBRecSet purchRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);
%>
<table border=1 width=85% >
<tr bgcolor=darkblue>
    <td><FONT color=white><B><center><U>PO#</u></center></b></td>
    <td><FONT color=white><B><center><U>Item Id</u></center></b></td>
    <td><FONT color=white><B><center><U>Customer Name</u></center></b></td>
    <td><FONT color=white><B><center><U>Vendor Ship Date</u></center></b></td>
    <td><FONT color=white><B><center><U>Qty Ordered</u></center></b></td>
    <td><FONT color=white><B><center><U>Qty Received</u></center></b></td>
</tr>
<% for (int i=0; i<purchRecSet.getSize(); i++) {

    String poCustName = purchRecSet.getRec(i).getAttribVal(purch_orderObj.CUST_NAME);
    if (poCustName == null) poCustName = "STOCK";
    String vendShipDate = purchRecSet.getRec(i).getAttribVal(purch_order_itemObj.DATE_ITEM_EXPECTED);
    if (vendShipDate == null) vendShipDate = "<center>-</center>";
%>

    <tr>
        <td><%=purchRecSet.getRec(i).getAttribVal(purch_order_itemObj.ID)%></td>
        <td><%=purchRecSet.getRec(i).getAttribVal(purch_order_itemObj.ITEM_ID)%></td>
        <td><%=poCustName%></td>
        <td align="right"><%=vendShipDate%></td>
        <td align="right"><%=purchRecSet.getRec(i).getAttribVal(purch_order_itemObj.QTY_ORDERED)%></td>
        <td align="right"><%=purchRecSet.getRec(i).getAttribVal(purch_order_itemObj.QTY_RECEIVED)%></td>
    </tr>
<% } %>
</table>

</body>
</html>
