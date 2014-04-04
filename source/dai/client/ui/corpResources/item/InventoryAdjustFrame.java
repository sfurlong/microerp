
//Title:        InventoryAdjustFrame
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI for Entry/Update of Orders

package dai.client.ui.corpResources.item;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dai.client.clientShared.daiWizardFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.itemObj;
import dai.shared.businessObjs.item_inventoryObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csInventoryAdapter;
import dai.shared.csAdapters.csInventoryAdapterFactory;

public class InventoryAdjustFrame extends daiWizardFrame {

    InventoryAdjustSelectPanel itemSelectPanel;

    Logger LOGGER;
    SessionMetaData _sessionMeta;
    int _currentPanelNum = 0;

    public InventoryAdjustFrame(JFrame container)
    {
        super(container);
        try
        {
            itemSelectPanel = new InventoryAdjustSelectPanel(container, this);
            jbInit();
        }
        catch(Exception ex)
        {
            LOGGER.logError(container, ex.getLocalizedMessage());
        }
    }

    private void jbInit() throws Exception
    {
        LOGGER = Logger.getInstance();
        _sessionMeta = SessionMetaData.getInstance();

        //Hide the next and prev buttons
        button_prev.setVisible(false);
        button_next.setVisible(false);
        button_post.setEnabled(true);

        //Update the Banner Panel
        bannerPanel.setBannerLeftText("Adjust Inventory Wizard");
        this.setTitle("Adjust Inventory Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(itemSelectPanel, "itemSelectPanel"),
                                        };
        CONTENT_PANEL.add(wizardPanels[0].panelRef, wizardPanels[0].panelName);

        centerFrame();
        show();

        itemSelectPanel.requestFocus();
    }

    protected void button_cancel_actionPerformed()
    {
          _currentPanelNum = 0;
          button_post.setEnabled(true);
          button_next.setEnabled(true);
          setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
          button_prev.setEnabled(false);
          itemSelectPanel.resetPanel();
    }

    protected void button_prev_actionPerformed()
    {
        //Nothing to do.
    }

    protected void button_next_actionPerformed()
    {
        //Nothing to do.
    }

    protected void button_post_actionPerformed()
    {
        if (!itemSelectPanel.panelDataIsValid()) return;

        try {
            csInventoryAdapterFactory inventoryAdapterFactory = csInventoryAdapterFactory.getInstance();
            csInventoryAdapter inventoryAdapter = inventoryAdapterFactory.getInventoryAdapter();

            DBRecSet itemData = new DBRecSet();
            DBRec attribs = new DBRec();

            attribs.addAttrib(new DBAttributes(itemObj.ID, itemSelectPanel.getSelectedItemId()));
            attribs.addAttrib(new DBAttributes(itemObj.BACKORDER_TO_CUST_QTY, itemSelectPanel.getQtyCustBack()));
            attribs.addAttrib(new DBAttributes(itemObj.BACKORDER_TO_VENDOR_QTY, itemSelectPanel.getQtyVendBack()));
            attribs.addAttrib(new DBAttributes(itemObj.ONHAND_QTY, itemSelectPanel.getQtyOnHand()));
            attribs.addAttrib(new DBAttributes(item_inventoryObj.ADJUSTMENT_TYPE, item_inventoryObj.ADJUST_TYPE_MANUAL_ADJUSTMENT));
            attribs.addAttrib(new DBAttributes(item_inventoryObj.DO_MANUAL_ADJUST_CUST_BACK, itemSelectPanel.getDoCustBack()));
            attribs.addAttrib(new DBAttributes(item_inventoryObj.DO_MANUAL_ADJUST_VEND_BACK, itemSelectPanel.getDoVendBack()));
            attribs.addAttrib(new DBAttributes(item_inventoryObj.DO_MANUAL_ADJUST_ONHAND, itemSelectPanel.getDoOnHand()));
            attribs.addAttrib(new DBAttributes(item_inventoryObj.NOTE, itemSelectPanel.getNote()));

            itemData.addRec(attribs);

            // Send the following attributes to the service.
            // itemObj.id
            // itemObj.BACKORDER_TO_CUST_QTY - this will represent qty to add/subtract
            // itemObj.BACKORDER_TO_VENDOR_QTY - this will represent qty to add/subtract
            // itemObj.ONHAND_QTY - this will represent the qty to add/subtract
            // item_inventoryObj.ADJUSTMENT_REASON
            // item_inventoryObj.ADJUSTMENT_TYPE
            // item_inventoryObj.DO_MANUAL_ADJUST_CUST_BACK
            // item_inventoryObj.DO_MANUAL_ADJUST_VEND_BACK
            // item_inventoryObj.DO_MANUAL_ADJUST_ONHAND
            // item_inventoryObj.note;
            inventoryAdapter.postInventoryAdjustment(_sessionMeta.getClientServerSecurity(),
                                                                    itemData);

            //Let the user know that the data was posted.
            JOptionPane.showMessageDialog(  this,
                                        "Inventory Adjustment Posted",
                                        "Inventory Adjustment",
                                        JOptionPane.INFORMATION_MESSAGE, null);

            itemSelectPanel.resetPanel();
        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not post inventory.\n"+ex.getLocalizedMessage());
        }
    }
}

