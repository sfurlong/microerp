
//Title:        order
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI for Entry/Update of Orders

package dai.client.ui.businessTrans.shipment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dai.client.clientShared.daiWizardFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.businessObjs.shipment_itemObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class CreateCreditMemoFrame extends daiWizardFrame
{
    CreateCreditMemoSelectPanel shipmentSelectPanel;
    CreateCreditMemoItemSelectPanel  itemSelectPanel = new CreateCreditMemoItemSelectPanel();
    CreateCreditMemoChargesPanel  chargesPanel = new CreateCreditMemoChargesPanel();
    CreateCreditMemoAcctsPanel  acctsPanel = new CreateCreditMemoAcctsPanel();

    Logger LOGGER;
    SessionMetaData _sessionMeta;
    int _currentPanelNum = 0;

    public CreateCreditMemoFrame(JFrame container)
    {
        super(container);

        try
        {
            shipmentSelectPanel = new CreateCreditMemoSelectPanel(container);
            jbInit();
        }
        catch(Exception ex)
        {
            LOGGER.logError(CONTAINER, ex.getLocalizedMessage());
        }
    }

    private void jbInit() throws Exception
    {
        LOGGER = LOGGER.getInstance();
        _sessionMeta = _sessionMeta.getInstance();

        //Update the Banner Panel
        bannerPanel.setBannerLeftText("Create Credit Memo Wizard");
        this.setTitle("Create Credit Memo Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(shipmentSelectPanel, "shipmentSelectPanel"),
                                        new WizardPanel(itemSelectPanel, "itemSelectPanel"),
                                        new WizardPanel(chargesPanel, "chargesPanel"),
                                        new WizardPanel(acctsPanel, "acctsPanel"),
                                        };
        CONTENT_PANEL.add(wizardPanels[0].panelRef, wizardPanels[0].panelName);
        CONTENT_PANEL.add(wizardPanels[1].panelRef, wizardPanels[1].panelName);
        CONTENT_PANEL.add(wizardPanels[2].panelRef, wizardPanels[2].panelName);
        CONTENT_PANEL.add(wizardPanels[3].panelRef, wizardPanels[3].panelName);

        centerFrame();
        show();
    }

    protected void button_cancel_actionPerformed()
    {
          _currentPanelNum = 0;
          button_post.setEnabled(false);
          button_next.setEnabled(true);
          setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
          button_prev.setEnabled(false);
          shipmentSelectPanel.resetPanel();
    }

    protected void button_prev_actionPerformed()
    {
        if (_currentPanelNum > 0) {
            _currentPanelNum--;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        } else {
            button_prev.setEnabled(false);
        }
    }

    protected void button_next_actionPerformed()
    {
        if (wizardPanels[_currentPanelNum].panelRef.panelDataIsValid())
        {
            button_prev.setEnabled(true);
            _currentPanelNum++;
            if (_currentPanelNum == 3) {
                button_post.setEnabled(true);
                button_next.setEnabled(false);
            }
            if (wizardPanels[_currentPanelNum].panelName.equals("itemSelectPanel")) {
                String shipmentId = shipmentSelectPanel.getSelectedShipment();
                itemSelectPanel.updateItemGrid(shipmentId);
            }
            if (wizardPanels[_currentPanelNum].panelName.equals("chargesPanel")) {
                double subTot = itemSelectPanel.getSelectedSubtotal();
                chargesPanel.setSubTotal(subTot);
            }
            wizardPanels[_currentPanelNum].panelRef.doPreDisplayProcessing(null);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        }
    }

    protected void button_post_actionPerformed()
    {

        DBRecSet selectedItems = itemSelectPanel.getSelectedItems();
        int numSelectedItems = selectedItems.getSize();
        String shipId = shipmentSelectPanel.getSelectedShipment();

        //There may not have been any line items selected.
        if (numSelectedItems > 0) {
            for (int i=0; i<numSelectedItems; i++) {
                DBRec attribSet = selectedItems.getRec(i);
                attribSet.addAttrib(new DBAttributes(shipment_itemObj.ID, shipId));
                attribSet.addAttrib(new DBAttributes(shipmentObj.SUBTOTAL, chargesPanel.getSubTot()));
                attribSet.addAttrib(new DBAttributes(shipmentObj.TOTAL_TAX, chargesPanel.getTaxCredits()));
                attribSet.addAttrib(new DBAttributes(shipmentObj.TOTAL_SHIPPING, chargesPanel.getShipCredits()));
                attribSet.addAttrib(new DBAttributes(shipmentObj.TOTAL_VALUE, chargesPanel.getTotCreditAmt()));
                attribSet.addAttrib(new DBAttributes(default_accountsObj.ACCTS_RECEIVABLE_ID, acctsPanel.getAR_acctId()));
                attribSet.addAttrib(new DBAttributes(default_accountsObj.SALES_TAX_PAYABLE_ID, acctsPanel.getTax_acctId()));
                attribSet.addAttrib(new DBAttributes(default_accountsObj.SHIPPING_OUT_ID, acctsPanel.getShipping_acctId()));
            }
        } else {
                DBRec attribSet = new DBRec();
                attribSet.addAttrib(new DBAttributes(shipment_itemObj.ID, shipId));
                attribSet.addAttrib(new DBAttributes(shipmentObj.SUBTOTAL, chargesPanel.getSubTot()));
                attribSet.addAttrib(new DBAttributes(shipmentObj.TOTAL_TAX, chargesPanel.getTaxCredits()));
                attribSet.addAttrib(new DBAttributes(shipmentObj.TOTAL_SHIPPING, chargesPanel.getShipCredits()));
                attribSet.addAttrib(new DBAttributes(shipmentObj.TOTAL_VALUE, chargesPanel.getTotCreditAmt()));
                attribSet.addAttrib(new DBAttributes(default_accountsObj.ACCTS_RECEIVABLE_ID, acctsPanel.getAR_acctId()));
                attribSet.addAttrib(new DBAttributes(default_accountsObj.SALES_TAX_PAYABLE_ID, acctsPanel.getTax_acctId()));
                attribSet.addAttrib(new DBAttributes(default_accountsObj.SHIPPING_OUT_ID, acctsPanel.getShipping_acctId()));
                selectedItems.addRec(attribSet);
        }

        try {
            csShipmentAdapterFactory shipmentAdapterFactory = csShipmentAdapterFactory.getInstance();
            csShipmentAdapter shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();

            //Call the Create Credit Memo Service.
            //shipmentObj.SUBTOTAL -- will be negative amt
            //shipmentObj.TOTAL_TAX -- will be negative amt
            //shipmentObj.TOTAL_SHIPPING -- will be negative amt
            //shipmentObj.TOTAL_VALUE  -- will be negative amt
            //shipment_itemObj.ID
            //shipment_itemObj.DETAIL_ID
            //shipment_itemObj.QTY_ORDERED -- holds qtyShipped from orig shipment
            //shipment_itemObj.QTY_SHIPPED -- holds qtyToCredit
            //shipment_itemObj.UNIT_PRICE -- Holds Unit Price Amt to credit *Negative value*
            //shipment_itemObj.EXTENDED_PRICE -- Holds Extended Amt to credit *Negative value*
            //default_accountsObj.SHIPPING_OUT_ID
            //default_accountsObj.ACCTS_RECEIVABLE_ID
            //default_accountsObj.SALES_TAX_PAYABLE_ID
            String creditMemoId = shipmentAdapter.createCreditMemo(_sessionMeta.getClientServerSecurity(),
                                                                selectedItems);

            //Let the user know that the Shipment was created.
            JOptionPane.showMessageDialog(  this,
                                        "Created new Credit Memo:  " + creditMemoId,
                                        "New Credit Memo Created",
                                        JOptionPane.INFORMATION_MESSAGE, null);
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            shipmentSelectPanel.resetPanel();
        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not create Credit Memo.\n"+ex.getLocalizedMessage());
        }
    }
}


