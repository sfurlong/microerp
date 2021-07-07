
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.businessObjs.purch_order_itemObj;
import dai.shared.businessObjs.purch_order_item_rcv_histObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;
import daiBeans.daiGrid;

public class ReceiveInventoryItemSelectPanel extends daiWizardPanel
{

    daiGrid _grid = new daiGrid();
    DBRecSet  _po_attribs_collection;
    SessionMetaData sessionMeta;
    csPurchOrderAdapterFactory _purchOrderFactory;
    csPurchOrderAdapter _purchOrderAdapter;
    String  _vendorId;

    Logger LOGGER;

    //Grid locations
    final int gridColSelected       = 0;
    final int gridColPOId           = 1;
   	final int gridColPODate         = 2;
    final int gridColItemId         = 3;
    final int gridColRepair         = 4;
    final int gridColPurchPrice     = 5;
    final int gridColQtyOrdered     = 6;
	final int gridColQtyReceived    = 7;
    final int gridColQtyToReceive   = 8;
    BorderLayout borderLayout1 = new BorderLayout();

    public ReceiveInventoryItemSelectPanel()
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
        _purchOrderFactory = _purchOrderFactory.getInstance();
        _purchOrderAdapter = _purchOrderFactory.getPurchOrderAdapter();
        sessionMeta = SessionMetaData.getInstance();
        LOGGER = Logger.getInstance();

        _grid.createColumns(new int[]{daiGrid.CHECKBOX_COL_TYPE,
                                     daiGrid.CHAR_COL_TYPE,
                                     daiGrid.DATE_COL_TYPE,
                                     daiGrid.CHAR_COL_TYPE,
                                     daiGrid.CHECKBOX_COL_TYPE,
                                     daiGrid.DOUBLE_COL_TYPE,
                                     daiGrid.INTEGER_COL_TYPE,
                                     daiGrid.INTEGER_COL_TYPE,
                                     daiGrid.INTEGER_COL_TYPE});
        _grid.setHeaderNames(new String[]{"Select", "PO Id", "PO Date", "Item Id",
                    "Repair", "Purch Price", "Qty Ordered", "Prev. Received",
                    "Qty Recieve"});
        //Disable most of the Grid entry fields
        _grid.setColEditable(gridColPOId, false);
        _grid.setColEditable(gridColPODate, false);
        _grid.setColEditable(gridColItemId, false);
        _grid.setColEditable(gridColPurchPrice, false);
        _grid.setColEditable(gridColQtyOrdered, false);
        _grid.setColEditable(gridColQtyReceived, false);
        _grid.setColEditable(gridColRepair, false);
        //Resize some of the Grid fields
        _grid.setColumnSize(gridColSelected, 50);
        _grid.setColumnSize(gridColRepair, 50);
        _grid.setColumnSize(gridColItemId, daiGrid.DEFAULT_ID_COL_WIDTH);
        //Turn off row selection
        _grid.allowRowSelection(false);

        this.setLayout(borderLayout1);
        this.add(_grid, BorderLayout.CENTER);
    }

    public boolean panelDataIsValid ()
    {
        boolean ret = false;

        //Make sure that some of the items were chosen for shipment.
        //Make sure the user selected at least one row to ship.
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColSelected).equals(Boolean.TRUE))
            {
                ret = true;
            }
        }
        if (!ret)
        {
            JOptionPane.showMessageDialog(  this,
                                        "No Items were selected.\n" +
                                        "Please select an Item(s) before continuing.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);
            return false;
        }


        //Now make sure that the proper qtys were selected.
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColSelected).equals(Boolean.TRUE))
            {
                _grid.stopCellEditing(i, gridColQtyToReceive);
                //Let's also make sure the user selected a proper QTY to ship.
                Integer i_qtyRcv = (Integer)_grid.get(i, gridColQtyToReceive);
                Integer i_qtyPrvRcv = new Integer((String)_grid.get(i, gridColQtyReceived));
                Integer i_qtyOrd = new Integer((String)_grid.get(i, gridColQtyOrdered));
                int qtyRcv = 0;
                int qtyOrd = 0;
                int qtyPrvRcv = 0;
                if (i_qtyRcv != null) qtyRcv = i_qtyRcv.intValue();
                if (i_qtyPrvRcv != null) qtyPrvRcv = i_qtyPrvRcv.intValue();
                if (i_qtyOrd != null) qtyOrd = i_qtyOrd.intValue();

                if (!(qtyRcv > 0 && (qtyRcv + qtyPrvRcv) <= qtyOrd)) {
                    ret = false;
                }
            }
        }
        if (!ret)
        {
            JOptionPane.showMessageDialog(  this,
                                        "An invalid Qty was chosen.\n" +
                                        "Please select a valid Qty to receive before continuing.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        return true;
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }

    public DBRecSet getSelectedItemsToShip(String dateReceived)
    {
        DBRecSet ret = new DBRecSet();
        DBRec   selectedAttribSet;
        DBAttributes  qtyToReceiveAttrib;
        DBAttributes  isRepairFlagAttrib;
        DBAttributes  vendorNameAttrib = new DBAttributes(purch_orderObj.VENDOR_NAME,
                                            _vendorId,
                                            BusinessObject.DATA_TYPE_VARCHAR,
                                            true);
        DBAttributes  dateReceivedAttrib = new DBAttributes(purch_order_item_rcv_histObj.DATE_RECEIVED,
                                            dateReceived,
                                            BusinessObject.DATA_TYPE_DATE,
                                            true);
        Integer     qtyToReceive = null;
        String      isRepair = null;

        //Only include selected rows
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            qtyToReceive = (Integer)_grid.get(i, gridColQtyToReceive);
            if (_grid.get(i, gridColRepair).equals(Boolean.TRUE)) {
                isRepair = "Y";
            }

            if (_grid.get(i, gridColSelected).equals(Boolean.TRUE))
            {
                selectedAttribSet = _po_attribs_collection.getRec(i);
                qtyToReceiveAttrib = new DBAttributes(purch_order_itemObj._QTY_TO_RECEIVE,
                                            qtyToReceive.toString(),
                                            BusinessObject.DATA_TYPE_VARCHAR,
                                            false);
                isRepairFlagAttrib = new DBAttributes(purch_order_itemObj.IS_ITEM_REPAIR,
                                                    isRepair,
                                                    BusinessObject.DATA_TYPE_CHAR,
                                                    true);
                selectedAttribSet.addAttrib(qtyToReceiveAttrib);
                selectedAttribSet.addAttrib(vendorNameAttrib);
                selectedAttribSet.addAttrib(dateReceivedAttrib);
                selectedAttribSet.addAttrib(isRepairFlagAttrib);
                ret.addRec(selectedAttribSet);
            }
        }

        return ret;
    }

	public void updateItemGrid(String vendorId)
	{
        _grid.removeAllRows();

        _vendorId = vendorId;

        try {
            //Get the the PO Line Items in which all Ordered Items have not
            //been received.  We assume we are getting the following attributes
            //in the collection:
            //      purch_orderObj.ID
            //      purch_orderObj.DATE_CREATED
            //      purch_order_itemObj.ITEM_ID
            //      purch_order_itemObj.PURCH_PRICE
            //      purch_order_itemObj.QTY_ORDERED
            //      purch_order_itemObj.QTY_RECEIVED
            //      purch_order_itemObj.IS_ITEM_REPAIR
            _po_attribs_collection = _purchOrderAdapter.getInventoryReceivablePOItems(sessionMeta.getClientServerSecurity(),
                                                                    vendorId);

            if (_po_attribs_collection.getSize() <= 0)
            {
                JOptionPane.showMessageDialog(  this,
                                        "No Inventory to Receive for:  " + vendorId,
                                        "Message",
                                        JOptionPane.INFORMATION_MESSAGE, null);
            }

            for (int i=0; i<_po_attribs_collection.getSize(); i++)
            {
                _grid.addRow();

                DBRec attribSet = _po_attribs_collection.getRec(i);

                //Populate the Grid Columns.
                //Make sure any columns needed for calculations are not null.
                String qtyOrdered = attribSet.getAttribVal(purch_order_itemObj.QTY_ORDERED);
                String qtyReceived = attribSet.getAttribVal(purch_order_itemObj.QTY_RECEIVED);
                if (qtyOrdered == null) qtyOrdered = "0";
                if (qtyReceived == null) qtyReceived = "0";

                _grid.set(i, gridColPOId, attribSet.getAttribVal(purch_orderObj.ID));
                _grid.set(i, gridColPODate, attribSet.getAttribVal(purch_orderObj.DATE_CREATED));
                _grid.set(i, gridColItemId, attribSet.getAttribVal(purch_order_itemObj.ITEM_ID));

                String isRepair = attribSet.getAttribVal(purch_order_itemObj.IS_ITEM_REPAIR);
                if (isRepair != null && isRepair.equals("Y")) {
                    _grid.set(i, gridColRepair, Boolean.TRUE);
                }
                _grid.set(i, gridColQtyOrdered, qtyOrdered);
                _grid.set(i, gridColQtyReceived, qtyReceived);
                _grid.set(i, gridColPurchPrice, attribSet.getAttribVal(purch_order_itemObj.PURCH_PRICE));
            }

        } catch (Exception ex) {
            LOGGER.logError(ex.getLocalizedMessage());
        }
	}
}


