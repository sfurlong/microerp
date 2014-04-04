
//Title:        order
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI for Entry/Update of Orders

package dai.client.ui.businessTrans.purchOrder;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dai.client.clientShared.daiWizardFrame;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;

public class ReceiveInventoryFrame extends daiWizardFrame
{
    ReceiveInventoryPOSelectPanel purchOrderSelectPanel;
    ReceiveInventoryItemSelectPanel  itemSelectPanel = new ReceiveInventoryItemSelectPanel();


    int _currentPanelNum = 0;
    Logger LOGGER;
    SessionMetaData _sessionMeta;

    public ReceiveInventoryFrame(JFrame container)
    {
        super(container);
        try
        {
            purchOrderSelectPanel = new ReceiveInventoryPOSelectPanel(container, this);
            jbInit();
        }
        catch(Exception ex)
        {
            LOGGER.logError(container, ex.getLocalizedMessage());
        }
    }

    private void jbInit() throws Exception
    {
        LOGGER = LOGGER.getInstance();

        //Update the Banner Panel
        bannerPanel.setBannerLeftText("Receive Inventory Wizard");
        this.setTitle("Receive Inventory Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(purchOrderSelectPanel, "purchOrderSelectPanel"),
                                        new WizardPanel(itemSelectPanel, "itemSelectPanel"),
                                        };
        CONTENT_PANEL.add(wizardPanels[0].panelRef, wizardPanels[0].panelName);
        CONTENT_PANEL.add(wizardPanels[1].panelRef, wizardPanels[1].panelName);

        centerFrame();
        show();
    }

    protected void button_cancel_actionPerformed() {
//        this.dispose();
          //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            purchOrderSelectPanel.resetPanel();
    }

    protected void button_prev_actionPerformed() {
        if (_currentPanelNum > 0) {
            _currentPanelNum--;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        } else {
            button_prev.setEnabled(false);
        }
    }

    protected void button_next_actionPerformed() {
        if (wizardPanels[_currentPanelNum].panelRef.panelDataIsValid())
        {
            button_prev.setEnabled(true);
            _currentPanelNum++;
            if (_currentPanelNum == 1) {
                button_post.setEnabled(true);
                button_next.setEnabled(false);
            }
            if (wizardPanels[_currentPanelNum].panelName.equals("itemSelectPanel")) {
                String poId = purchOrderSelectPanel.getPOId();
                itemSelectPanel.updateItemGrid(poId);
            }
            wizardPanels[_currentPanelNum].panelRef.doPreDisplayProcessing(null);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        }
    }

    protected void button_post_actionPerformed() {
        //First make sure that final panel data is valid.
        if (!wizardPanels[_currentPanelNum].panelRef.panelDataIsValid()) {
            return;
        }

        DBRecSet selectedItemAttribs =
                    itemSelectPanel.getSelectedItemsToShip(purchOrderSelectPanel.getDateReceived());
        try {
            csPurchOrderAdapterFactory poAdapterFactory = csPurchOrderAdapterFactory.getInstance();
            csPurchOrderAdapter poAdapter = poAdapterFactory.getPurchOrderAdapter();
            SessionMetaData sessionMeta = SessionMetaData.getInstance();

            //These attribs are passed to the ReceiveInventory API
            //*      purch_orderObj.ID
            //*      purch_orderObj.VENDOR_NAME
            //*      purch_order_itemObj.ITEM_ID
            //*      purch_order_itemObj.PURCH_PRICE
            //*      purch_order_itemObj.QTY_ORDERED
            //*      purch_order_itemObj.QTY_RECEIVED
            //*      purch_order_itemObj._QTY_TO_RECEIVE
            //*      purch_order_itemObj.IS_ITEM_REPAIR
            //*      purch_order_item_rcv_histObj.DATE_RECEIVED
            poAdapter.receiveInventory(sessionMeta.getClientServerSecurity(),
                                                        selectedItemAttribs);

            //Let the user know that the Shipment was created.
            JOptionPane.showMessageDialog(  this,
                                        "Posted Items to Inventory.",
                                        "Posted Inventory",
                                        JOptionPane.INFORMATION_MESSAGE, null);

            //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            purchOrderSelectPanel.resetPanel();

        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not Post Items To Inventory.\n"+ex.getLocalizedMessage());
        }
    }
}


