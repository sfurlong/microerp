
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
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;

public class BillReceiptFrame extends daiWizardFrame
{
    BillReceiptEntryPanel billEntryPanel;
    BillReceiptAccountsPanel acctsPanel = new BillReceiptAccountsPanel();

    int _currentPanelNum = 0;
    Logger LOGGER;
    SessionMetaData _sessionMeta;

    public BillReceiptFrame(JFrame container)
    {
        super(container);
        try
        {
            billEntryPanel = new BillReceiptEntryPanel(container, this);
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
        bannerPanel.setBannerLeftText("Bill Receipt Wizard");
        //this.setTitle("Bill Receipt Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(billEntryPanel, "billEntryPanel"),
                                        new WizardPanel(acctsPanel, "acctsPanel"),
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
            billEntryPanel.resetPanel();
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
            if (_currentPanelNum == 1) {
                acctsPanel.setExpenseAcct(billEntryPanel.getExpenseAcctId(),
                                        billEntryPanel.getExpenseAcctName());
                button_post.setEnabled(true);
                button_next.setEnabled(false);
            }
            wizardPanels[_currentPanelNum].panelRef.doPreDisplayProcessing(null);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        }
    }

    protected void button_post_actionPerformed() {

	    DBRecSet vouchData = new DBRecSet();
        DBRec attribSet = new DBRec();

        attribSet.addAttrib(new DBAttributes(payment_voucherObj.PURCH_ORDER_ID, "N/A"));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.VENDOR_ID, billEntryPanel.getVendorId()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.VENDOR_NAME, billEntryPanel.getVendorName()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.ACCTID, acctsPanel.getExpenseAcctId()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.ACCTNAME, acctsPanel.getExpenseAcctName()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.PAYMENT_TERMS, billEntryPanel.getPayTerms()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.PAYMENT_DUE_DATE, billEntryPanel.getDueDate()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.TOTAL_VALUE, billEntryPanel.getAmtDue()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.INVOICE_NUM, billEntryPanel.getRefNum()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj.INVOICE_DATE, billEntryPanel.getRefDate()));
        attribSet.addAttrib(new DBAttributes(payment_voucherObj._NUM_PAYMENTS, Integer.toString(billEntryPanel.getNumPayments())));

        vouchData.addRec(attribSet);

        try {
            SessionMetaData     sessionMeta = SessionMetaData.getInstance();
            csPurchOrderAdapterFactory purchOrderFactory = csPurchOrderAdapterFactory.getInstance();
            csPurchOrderAdapter purchOrderAdapter = purchOrderFactory.getPurchOrderAdapter();

            String payVoucherIds[] = purchOrderAdapter.postBillReceipt(sessionMeta.getClientServerSecurity(),
                                               vouchData);

            //Let the user know that the Shipment was created.
            JOptionPane userDlg = new JOptionPane();
            String s_payVouchers = "";
            for (int i=0; i<payVoucherIds.length; i++) {
                if (i == 0) {
                    s_payVouchers += payVoucherIds[i];
                } else {
                    s_payVouchers += ", " + payVoucherIds[i];
                }
            }
            userDlg.showMessageDialog(  this,
                                        "Bill Receipt Posted.  New Pay Voucher(s)#: " + s_payVouchers,
                                        "Bill Receipt Posted",
                                        JOptionPane.INFORMATION_MESSAGE, null);

            //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            billEntryPanel.resetPanel();

        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not Post Bill Receipt.\n"+ex.getLocalizedMessage());
        }
    }
}

