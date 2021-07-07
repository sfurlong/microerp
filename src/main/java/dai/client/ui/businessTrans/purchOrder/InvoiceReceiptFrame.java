
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
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.payment_voucherObj;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;

public class InvoiceReceiptFrame extends daiWizardFrame
{
    InvoiceReceiptSelectionPanel invSelectPanel;
    InvoiceReceiptPaymentPanel invPayPanel;
    InvoiceReceiptAccountsPanel acctsPanel = new InvoiceReceiptAccountsPanel();

    int _currentPanelNum = 0;
    Logger LOGGER;
    SessionMetaData _sessionMeta;

    public InvoiceReceiptFrame(JFrame container)
    {
        super(container);
        try
        {
            invSelectPanel = new InvoiceReceiptSelectionPanel(container, this);
            invPayPanel = new InvoiceReceiptPaymentPanel(container, this);
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
        bannerPanel.setBannerLeftText("Invoice Receipt Wizard");
        this.setTitle("Invoice Receipt Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(invSelectPanel, "invSelectPanel"),
                                        new WizardPanel(invPayPanel, "invPayPanel"),
                                        new WizardPanel(acctsPanel, "acctsPanel"),
                                        };
        CONTENT_PANEL.add(wizardPanels[0].panelRef, wizardPanels[0].panelName);
        CONTENT_PANEL.add(wizardPanels[1].panelRef, wizardPanels[1].panelName);
        CONTENT_PANEL.add(wizardPanels[2].panelRef, wizardPanels[2].panelName);

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
            invSelectPanel.resetPanel();
            invPayPanel.resetPanel();
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

    protected void button_post_actionPerformed() {
        purch_orderObj poObj = invSelectPanel.getSelectedPurchOrderObj();
	    DBRecSet poData = new DBRecSet();
        DBRec attribSet = new DBRec();
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.PURCH_ORDER_ID, poObj.get_id()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.VENDOR_ID, poObj.get_vendor_id()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.VENDOR_NAME, poObj.get_vendor_name()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.PAYMENT_TERMS, invPayPanel.getPayTerms()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.PAYMENT_DUE_DATE, invPayPanel.getPayDueDate()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.TOTAL_VALUE, invSelectPanel.getPaymentAmt()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.INVOICE_NUM, invPayPanel.getInvoiceNum()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.INVOICE_DATE, invPayPanel.getInvoiceDate()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.TOTAL_SHIPPING_CHARGES, invSelectPanel.getShipAmt()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.SUBTOTAL_AMT, invSelectPanel.getSubtotal()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj._SHIP_CHARGES_ACCT_ID, acctsPanel.getShipAcctId()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj._SHIP_CHARGES_ACCT_NAME, acctsPanel.getShipAcctName()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.ACCTID, acctsPanel.getCOGSAcctId()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.ACCTNAME, acctsPanel.getCOGSAcctName()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj._NUM_PAYMENTS, Integer.toString(invPayPanel.getNumPayments())));
        poData.addRec(attribSet);

        try {
            csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
            csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();
            SessionMetaData     sessionMeta = SessionMetaData.getInstance();
            csPurchOrderAdapterFactory purchOrderFactory = csPurchOrderAdapterFactory.getInstance();
            csPurchOrderAdapter purchOrderAdapter = purchOrderFactory.getPurchOrderAdapter();

            String[] payVoucherIds = purchOrderAdapter.postInvoiceReceipt(sessionMeta.getClientServerSecurity(),
                                                poData);
            //Let the user know that the Shipment was created.
            String s_payVouchers = "";
            for (int i=0; i<payVoucherIds.length; i++) {
                if (i == 0) {
                    s_payVouchers += payVoucherIds[i];
                } else {
                    s_payVouchers += ", " + payVoucherIds[i];
                }
            }
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(  this,
                                        "Invoice Receipt Posted.  New Pay Voucher#: " + s_payVouchers,
                                        "Invoice Receipt Posted",
                                        JOptionPane.INFORMATION_MESSAGE, null);

            //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            invSelectPanel.resetPanel();
            invPayPanel.resetPanel();

        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not Post Invoice Receipt.\n"+ex.getLocalizedMessage());
        }
    }
}

