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
    String _shipmentId = request.getParameter("SHIPMENT_ID");
    csSecurity security = (csSecurity) session.getValue("csSecurity");
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
    
    String sqlStmt = "select ID, customer_id, customer_name, " +
            " shipto_addr1, shipto_addr2, shipto_addr3, shipto_addr4, " +
            " shipto_city, shipto_state_code, shipto_zip, shipto_country_code, shipto_country_name," +
            " billto_addr1, billto_addr2, billto_addr3, billto_addr4, " +
            " billto_city, billto_state_code, billto_zip, billto_country_code, billto_country_name" +
            " from shipment " +
            " where id = '" + _shipmentId + "' and " + 
                   " locality = '" + shipmentObj.getObjLocality() + "'";
    System.out.println(sqlStmt);
    DBRecSet shipRecSet = dbAdapter.execDynamicSQL(security, sqlStmt);

%>

<center>
<h1>Header Detail For Shipment <%=_shipmentId%></h1>
<hr>
</center>
<body leftmargin=15% topmargin=10% bottommargin=10% onload="window.focus()">

<center>
<TABLE>
	    <% 
        for (int i=0; i<shipRecSet.getSize(); i++) {
            String shiptoAddr1 = shipRecSet.getRec(i).getAttribVal(shipmentObj.SHIPTO_ADDR1);
            String shiptoAddr2 = shipRecSet.getRec(i).getAttribVal(shipmentObj.SHIPTO_ADDR2);
            String shiptoCity = shipRecSet.getRec(i).getAttribVal(shipmentObj.SHIPTO_CITY);
            String shiptoState = shipRecSet.getRec(i).getAttribVal(shipmentObj.SHIPTO_STATE_CODE);
            String shiptoZip = shipRecSet.getRec(i).getAttribVal(shipmentObj.SHIPTO_ZIP);
            
            String billtoAddr1 = shipRecSet.getRec(i).getAttribVal(shipmentObj.BILLTO_ADDR1);
            String billtoAddr2 = shipRecSet.getRec(i).getAttribVal(shipmentObj.BILLTO_ADDR2);
            String billtoCity = shipRecSet.getRec(i).getAttribVal(shipmentObj.BILLTO_CITY);
            String billtoState = shipRecSet.getRec(i).getAttribVal(shipmentObj.BILLTO_STATE_CODE);
            String billtoZip = shipRecSet.getRec(i).getAttribVal(shipmentObj.BILLTO_ZIP);
        %>
            <tr><td>
            
            <table>
            <tr> 
            <td align="center"><B>Bill To:</B></td>
            </tr>
            
            <tr> 
            <td><%=billtoAddr1%></td>
            </tr>
            
            <% if (billtoAddr2 != null) { %>
                <tr> 
                <td><%=billtoAddr2%></td>
                </tr>
            <% } %>
	    
            <tr> 
            <td><%=billtoCity + ", " + billtoState + "  " + billtoZip %></td>
            </tr>
            </table>
            
            </td>

            <td><br></td>     
            <td><br></td>     
            <td><br></td>     
            
            <td>
            
            <table>
            <tr> 
            <td align="center"><B>Ship To:</B></td>
            </tr>
            
            <tr> 
            <td><%=shiptoAddr1%></td>
            </tr>
            
            <% if (shiptoAddr2 != null) { %>
                <tr> 
                <td><%=shiptoAddr2%></td>
                </tr>
            <% } %>
	    
            <tr> 
            <td><%=shiptoCity + ", " + shiptoState + "  " + shiptoZip %></td>
            </tr>
            </table>
            
            </td>
                   
            
            </tr>

        <% } %>
    </td>
  </tr>
    
</TABLE>

</center>

</body>
</html>