
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JOptionPane;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.shipment_itemObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;
import daiBeans.daiGrid;

public class CreateCreditMemoItemSelectPanel extends daiWizardPanel
{

    daiGrid _grid = new daiGrid();
    Vector  ORDER_ITEM_OBJS;
    SessionMetaData sessionMeta;
    csShipmentAdapterFactory shipmentAdapterFactory;
    csShipmentAdapter shipmentAdapter;

    Logger LOGGER;

    //Grid locations
    final int gridColDetailId     = 0;
    final int gridColShip         = 1;
   	final int gridColItem         = 2;
    final int gridColAcctId       = 3;
    final int gridColQtyOrdered   = 4;
	final int gridColQtyShipped   = 5;
	final int gridColUnitPrice    = 6;
    final int gridColQtyCredit    = 7;
    final int gridColCreditAmt    = 8;
    BorderLayout borderLayout1 = new BorderLayout();

    public CreateCreditMemoItemSelectPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        shipmentAdapterFactory = csShipmentAdapterFactory.getInstance();
        shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();
        sessionMeta = SessionMetaData.getInstance();
        LOGGER = Logger.getInstance();

        _grid.createColumns(new int[]{daiGrid.CHAR_COL_TYPE,
                daiGrid.CHECKBOX_COL_TYPE,
                daiGrid.CHAR_COL_TYPE,
                daiGrid.CHAR_COL_TYPE,
                daiGrid.INTEGER_COL_TYPE,
                daiGrid.INTEGER_COL_TYPE,
                daiGrid.DOUBLE_COL_TYPE,
                daiGrid.INTEGER_COL_TYPE,
                daiGrid.DOUBLE_COL_TYPE});
        _grid.setHeaderNames(new String[]{" ", "Ship", "Item Id", "Acct Id",
                                        "Qty Ord", "Qty Shipped",
                                        "Unit Price", "Qty To Credit", "Unit Credit"});
        //Disable most of the Grid entry fields
        _grid.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);
        _grid.setColEditable(gridColItem, false);
        _grid.setColEditable(gridColQtyOrdered, false);
        _grid.setColEditable(gridColQtyShipped, false);
        _grid.setColEditable(gridColUnitPrice, false);
        _grid.setColEditable(gridColAcctId, false);
        //Resize some of the Grid fields
        _grid.setColumnSize(gridColShip, 50);
        //Turn off row selection
        _grid.allowRowSelection(false);

        this.setLayout(borderLayout1);
        this.add(_grid, BorderLayout.CENTER);
    }

    public boolean panelDataIsValid ()
    {
        boolean ret = true;

        //Make sure an AcctId exists.
        ret = true;
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColShip).equals(Boolean.TRUE))
            {
                String acctId = (String) _grid.get(i, gridColAcctId);
                if (acctId == null || acctId.length() == 0) {
                    JOptionPane.showMessageDialog(  this,
                                        "One of the selected items has an empty Account Id.\n" +
                                        "Please go back to the Order Entry Module and select an Accout Id for that Item.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);
                    return false;
                }
            }
        }

        return ret;
    }
    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }

    public DBRecSet getSelectedItems()
    {
        //For each Shipment Item Selected on the Grid, add the order item
        //business object to a vector which will be sent to the
        //Shipment Service.
        DBRecSet itemAttribSet = new DBRecSet();
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColShip).equals(Boolean.TRUE))
            {
                Integer i_qtyShip = (Integer)_grid.get(i, gridColQtyShipped);
                Integer i_qtyCred = (Integer)_grid.get(i, gridColQtyCredit);
                Double D_creditAmt = (Double)_grid.get(i, gridColCreditAmt);
                double d_creditAmt = D_creditAmt.doubleValue() * -1;
                double d_extdCred = d_creditAmt * i_qtyCred.intValue();

                DBRec attribSet = new DBRec();
                DBAttributes detailIdAttrib = new DBAttributes(shipment_itemObj.DETAIL_ID, (String)_grid.get(i, gridColDetailId));
                DBAttributes qtyShipAttrib = new DBAttributes(shipment_itemObj.QTY_ORDERED, i_qtyShip.toString());
                DBAttributes qtyCredAttrib = new DBAttributes(shipment_itemObj.QTY_SHIPPED, i_qtyCred.toString());
                DBAttributes unitCredAttrib = new DBAttributes(shipment_itemObj.UNIT_PRICE, Double.toString(d_creditAmt));
                DBAttributes extdCredAttrib = new DBAttributes(shipment_itemObj.EXTENDED_PRICE, Double.toString(d_extdCred));
                attribSet.addAttrib(qtyShipAttrib);
                attribSet.addAttrib(qtyCredAttrib);
                attribSet.addAttrib(detailIdAttrib);
                attribSet.addAttrib(unitCredAttrib);
                attribSet.addAttrib(extdCredAttrib);
                itemAttribSet.addRec(attribSet);
            }
        }
        return itemAttribSet;
    }

    public double getSelectedSubtotal()
    {
        double subTot = 0.0;
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColShip).equals(Boolean.TRUE))
            {
                Double d_creditAmt = (Double)_grid.get(i, gridColCreditAmt);
                Integer i_qtyCredit = (Integer)_grid.get(i, gridColQtyCredit);

                subTot = subTot + (d_creditAmt.doubleValue() * i_qtyCredit.intValue());
            }
        }
        return subTot;
    }

	public void updateItemGrid(String shipmentId)
	{
        _grid.removeAllRows();

        try {
            //Use the DBService Service to get the Shipment Items were interested in.
            csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
            csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

            ORDER_ITEM_OBJS = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                        new shipment_itemObj(),
                                        " id = '" + shipmentId+"'");

            if (ORDER_ITEM_OBJS.size() <= 0)
            {
                JOptionPane.showMessageDialog(  this,
                                        "No Items for Shipment:  " + shipmentId,
                                        "Message",
                                        JOptionPane.INFORMATION_MESSAGE, null);
            }

            for (int i=0; i<ORDER_ITEM_OBJS.size(); i++)
            {
                _grid.addRow();

                shipment_itemObj obj = (shipment_itemObj)ORDER_ITEM_OBJS.elementAt(i);

                //Populate the Grid Columns.
                //Make sure any columns needed for calculations are not null.
                String qtyOrdered = obj.get_qty_ordered();
                if (qtyOrdered == null || qtyOrdered.length() == 0) qtyOrdered = "0";
                String qtyShipped = obj.get_qty_shipped();
                if (qtyShipped == null || qtyShipped.length() == 0) qtyShipped = "0";
                String qtyBackOrder = obj.get_qty_backorder();
                if (qtyBackOrder == null || qtyBackOrder.length() == 0) qtyBackOrder = "0";
                String unitPrice = obj.get_unit_price();
                if (unitPrice == null || unitPrice.length() == 0) unitPrice = "0";

                _grid.set(i, gridColDetailId, obj.get_detail_id());
                _grid.set(i, gridColItem, obj.get_item_id());
                _grid.set(i, gridColQtyOrdered, new Integer(qtyOrdered));
                _grid.set(i, gridColQtyShipped, new Integer(qtyShipped));
                _grid.set(i, gridColUnitPrice, new Double(unitPrice));
                _grid.set(i, gridColAcctId, obj.get_account_id());
            }

        } catch (Exception ex) {
            LOGGER.logError(null, ex.getLocalizedMessage());
        }
	}
}
