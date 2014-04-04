
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
import dai.shared.businessObjs.cash_receiptObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class CashReceiptFrame extends daiWizardFrame
{
    CashReceiptSelectionPanel shipSelectPanel;
    CashReceiptPaymentPanel shipPayPanel = new CashReceiptPaymentPanel();
    CashReceiptDefaultAcctsPanel acctsPanel = new CashReceiptDefaultAcctsPanel();

    JFrame CONTAINER;
    Logger LOGGER;
    SessionMetaData _sessionMeta;
    int _currentPanelNum = 0;

    public CashReceiptFrame(JFrame container)
    {

        super(container);
        CONTAINER = container;
        try
        {
            shipSelectPanel = new CashReceiptSelectionPanel(container, this);
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
        bannerPanel.setBannerLeftText("Receive Payment Wizard");
        this.setTitle("Cash Receipt Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(shipSelectPanel, "shipSelectPanel"),
                                        new WizardPanel(shipPayPanel, "shipPayPanel"),
                                        new WizardPanel(acctsPanel, "acctsPanel"),
                                        };
        CONTENT_PANEL.add(wizardPanels[0].panelRef, wizardPanels[0].panelName);
        CONTENT_PANEL.add(wizardPanels[1].panelRef, wizardPanels[1].panelName);
        CONTENT_PANEL.add(wizardPanels[2].panelRef, wizardPanels[2].panelName);

        centerFrame();
        show();

        shipSelectPanel.requestFocus();
    }

    protected void button_cancel_actionPerformed()
    {
//        this.dispose();
          //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            shipPayPanel.resetPanel();
            shipSelectPanel.resetPanel();
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
        //If the data on the current panel is valid we can move to the
        //next panel.
        if (wizardPanels[_currentPanelNum].panelRef.panelDataIsValid())
        {
            button_prev.setEnabled(true);
            _currentPanelNum++;
            if (_currentPanelNum == 2) {
                button_post.setEnabled(true);
                button_next.setEnabled(false);
            }
            wizardPanels[_currentPanelNum].panelRef.doPreDisplayProcessing(null);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        }
    }

    protected void button_post_actionPerformed()
    {
        try {
            csShipmentAdapterFactory shipmentAdapterFactory = csShipmentAdapterFactory.getInstance();
            csShipmentAdapter shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();

            DBRecSet cashRcptData = new DBRecSet();
            DBRec attribs = new DBRec();

            attribs.addAttrib(new DBAttributes(cash_receiptObj.SHIPMENT_ID, shipSelectPanel.getSelectedShipment()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.PAYMENT_METHOD, shipPayPanel.getPayMethod()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.PAYMENT_AMT, shipPayPanel.getPaymentAmt()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.CHECK_NUM, shipPayPanel.getCheckNum()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.CC_NUM, shipPayPanel.getCreditCardNum()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.CC_EXP_DATE, shipPayPanel.getCreditCardExp()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.DATE_RECEIVED, shipPayPanel.getDateReceived()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.CUST_ID, shipSelectPanel.getCustId()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.CUST_NAME, shipSelectPanel.getCustName()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.RECEIVABLE_ACCT_ID, acctsPanel.getReceivableAcctId()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.RECEIVABLE_ACCT_NAME, acctsPanel.getReceivableAcctName()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.DEPOSIT_ACCT_ID, acctsPanel.getDepositAcctId()));
            attribs.addAttrib(new DBAttributes(cash_receiptObj.DEPOSIT_ACCT_NAME, acctsPanel.getDepositAcctName()));
            String isPrepaid = shipSelectPanel.isOrderPrePayment() ? "Y" : "N";
            attribs.addAttrib(new DBAttributes(cash_receiptObj._IS_PREPAID_ORDER, isPrepaid));

            cashRcptData.addRec(attribs);

            //The service will exept the following attributes
            //cash_receiptObj.SHIPMENT_ID
            //cash_receiptObj.PAYMENT_METHOD
            //cash_receiptObj.CHECK_NUM
            //cash_receiptObj.PAYMENT_AMT
            //cash_receiptObj.CC_NUM
            //cash_receiptObj.CC_EXP_DATE
            //cash_receiptObj.DATE_RECEIVED
            //cash_receiptObj.RECEIVABLE_ACCT_ID
            //cash_receiptObj.RECEIVABLE_ACCT_NAME
            //cash_receiptObj.DEPOSIT_ACCT_ID
            //cash_receiptObj.DEPOSIT_ACCT_NAME
            //cash_receiptObj.CUST_ID
            //cash_receiptObj.CUST_NAME
            //cash_receiptObj.NOTE
            //cash_receiptObj._IS_PREPAID_ORDER
            String cashRcptId = shipmentAdapter.createCashReceipt(  _sessionMeta.getClientServerSecurity(),
                                                                    cashRcptData);

            //Let the user know that the Shipment was created.
            JOptionPane.showMessageDialog(  this,
                                        "New Cash Receipt Posted:  " + cashRcptId,
                                        "Cash Receipt Posted",
                                        JOptionPane.INFORMATION_MESSAGE, null);

            //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            shipPayPanel.resetPanel();
            shipSelectPanel.requestFocus();
            shipSelectPanel.resetPanel();

        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not create Cash Receipt.\n"+ex.getLocalizedMessage());
        }
    }
}

