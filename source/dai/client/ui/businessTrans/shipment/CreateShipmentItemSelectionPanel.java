
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
import dai.shared.businessObjs.cust_orderObj;
import dai.shared.businessObjs.cust_order_itemObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;
import daiBeans.daiGrid;

public class CreateShipmentItemSelectionPanel extends daiWizardPanel
{

    daiGrid _grid = new daiGrid();
    Vector  ORDER_ITEM_OBJS;
    SessionMetaData sessionMeta;
    csShipmentAdapterFactory shipmentAdapterFactory;
    csShipmentAdapter shipmentAdapter;

    Logger LOGGER;

    //Grid locations
    final int gridColShip         = 0;
    final int gridColOrdId        = 1;
   	final int gridColItem         = 2;
    final int gridColQtyOrdered   = 3;
	final int gridColQtyShipped   = 4;
    final int gridColQtyToShip    = 5;
	final int gridColQtyBackorder = 6;
	final int gridColExtendPrice  = 7;
	final int gridColAcctId       = 8;
    BorderLayout borderLayout1 = new BorderLayout();

    public CreateShipmentItemSelectionPanel()
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

        _grid.createColumns(new int[]{daiGrid.CHECKBOX_COL_TYPE,
        		daiGrid.CHAR_COL_TYPE,
        		daiGrid.CHAR_COL_TYPE,
        		daiGrid.INTEGER_COL_TYPE,
        		daiGrid.INTEGER_COL_TYPE,
        		daiGrid.INTEGER_COL_TYPE,
        		daiGrid.INTEGER_COL_TYPE,
        		daiGrid.DOUBLE_COL_TYPE,
                daiGrid.CHAR_COL_TYPE});
        _grid.setHeaderNames(new String[]{"Ship", "Order Id", "Item Id", "Qty Ord", "Qty Shipped",
                                        "Qty To Ship", "Qty BackOrd", "Extend Price", "Acct Id"});
        //Disable most of the Grid entry fields
        _grid.setColEditable(gridColOrdId, false);
        _grid.setColEditable(gridColItem, false);
        _grid.setColEditable(gridColQtyOrdered, false);
        _grid.setColEditable(gridColQtyShipped, false);
        _grid.setColEditable(gridColQtyBackorder, false);
        _grid.setColEditable(gridColExtendPrice, false);
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
        boolean ret = false;

        //Make sure the user selected at least one row to ship.
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColShip).equals(Boolean.TRUE))
            {
                ret = true; //At least one has been checked.
                //The next stmt ensures that the cell that is being edited is
                //committed.
                _grid.stopCellEditing(i, gridColQtyToShip);
                //Let's also make sure the user selected a proper QTY to ship.
                Integer qtyToShip   = (Integer)_grid.get(i, gridColQtyToShip);
                Integer qtyOrd      = (Integer)_grid.get(i, gridColQtyOrdered);
                Integer qtyShipped  = (Integer)_grid.get(i, gridColQtyShipped);
                int     qtyAvailToShip = qtyOrd.intValue() - qtyShipped.intValue();
                if (qtyToShip.intValue() == 0 || qtyToShip.intValue() > qtyAvailToShip) {
                    ret = false;
                    break;
                }
            }
        }
        //Make sure that some of the items were chosen for shipment.
        if (!ret)
        {
            JOptionPane.showMessageDialog(  this,
                                        "No Items were selected for Shipment or invalid Qty To Ship.\n" +
                                        "Please select Item(s) to ship before continuing.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

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

    public Vector getSelectedItemsToShip()
    {

        //Update the order item business objects with the values from the grid
        //This will be to set things like qty_ship, qty_backord...
        update_BusinessObjs();

        Vector selectedItemObjs = new Vector();

        //For each Order Item Selected on the Grid, add the order item
        //business object to a vector which will be sent to the Create
        //Shipment Service.
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColShip).equals(Boolean.TRUE))
            {
                selectedItemObjs.addElement(ORDER_ITEM_OBJS.elementAt(i));
            }
        }

        return selectedItemObjs;
    }

	public void updateItemGrid(String[] orderIds)
	{
        _grid.removeAllRows();

        try {
            //Use the Shipment Service to get the Order Items that still
            //have quantities left to ship.
            //!!For now only using the first Order Id in the list.
            ORDER_ITEM_OBJS = shipmentAdapter.getShipableOrderItems(sessionMeta.getClientServerSecurity(),
                                                                    orderIds[0],
                                                                    cust_orderObj.getObjLocality());

            if (ORDER_ITEM_OBJS.size() <= 0)
            {
                JOptionPane.showMessageDialog(  this,
                                        "No Order Items to Ship for:  " + orderIds[0],
                                        "Message",
                                        JOptionPane.INFORMATION_MESSAGE, null);
            }

            for (int i=0; i<ORDER_ITEM_OBJS.size(); i++)
            {
                _grid.addRow();

                cust_order_itemObj obj = (cust_order_itemObj)ORDER_ITEM_OBJS.elementAt(i);

                //Populate the Grid Columns.
                //Make sure any columns needed for calculations are not null.
                String qtyOrdered = obj.get_qty_ordered();
                if (qtyOrdered == null || qtyOrdered.length() == 0) qtyOrdered = "0";
                String qtyShipped = obj.get_qty_shipped();
                if (qtyShipped == null || qtyShipped.length() == 0) qtyShipped = "0";
                String qtyBackOrder = obj.get_qty_backorder();
                if (qtyBackOrder == null || qtyBackOrder.length() == 0) qtyBackOrder = "0";

                _grid.set(i, gridColOrdId, obj.get_id());
                _grid.set(i, gridColItem, obj.get_item_id());
                _grid.set(i, gridColQtyOrdered, new Integer(qtyOrdered));
                _grid.set(i, gridColQtyShipped, new Integer(qtyShipped));
                _grid.set(i, gridColQtyBackorder, new Integer(qtyBackOrder));
                _grid.set(i, gridColExtendPrice, obj.get_extended_price());
                _grid.set(i, gridColAcctId, obj.get_account_id());
            }

        } catch (Exception ex) {
            LOGGER.logError(ex.getLocalizedMessage());
        }
	}

	private Vector update_BusinessObjs()
	{
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColShip).equals(Boolean.TRUE))
            {
                //Update the BusinessObject from the Grid.
                //This will be to set things like qty_ship, qty_backord...
                cust_order_itemObj obj = (cust_order_itemObj)ORDER_ITEM_OBJS.elementAt(i);

                //Update the QtyOrd and QtyBackOrd in the BusinessObj.
                Integer qtyToShip  = (Integer)_grid.get(i, gridColQtyToShip);

                //Set Business Object with the new values.
                obj._set_qty_to_ship(qtyToShip.intValue());

                ORDER_ITEM_OBJS.setElementAt(obj, i);
            }
        }

        return ORDER_ITEM_OBJS;
	}
}


