
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
import dai.shared.businessObjs.global_settings_pay_methodsObj;
import dai.shared.businessObjs.payment_voucherObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;

public class PayBillsFrame extends daiWizardFrame
{
    PayBillsDateEntryPanel dateEntryPanel;
    PayBillsSelectPanel paySelectPanel;

    int _currentPanelNum = 0;
    Logger LOGGER;
    SessionMetaData _sessionMeta;

    public PayBillsFrame(JFrame container)
    {
        super(container);
        try
        {
            dateEntryPanel = new PayBillsDateEntryPanel(container, this);
            paySelectPanel = new PayBillsSelectPanel(container, this);
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
        bannerPanel.setBannerLeftText("Pay Bills Wizard");
        this.setTitle("Pay Bills Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(dateEntryPanel, "dateEntryPanel"),
                                        new WizardPanel(paySelectPanel, "paySelectPanel"),
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
            dateEntryPanel.resetPanel();
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
//                acctsPanel.setExpenseAcct(billEntryPanel.getExpenseAcctId(),
//                                        billEntryPanel.getExpenseAcctName());
                button_post.setEnabled(true);
                button_next.setEnabled(false);
            }
            wizardPanels[_currentPanelNum].panelRef.doPreDisplayProcessing(null);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        }
    }

    protected void button_post_actionPerformed() {
        //Make sure the existing panel was filled out correctly befor
        //continuing.
        if (!wizardPanels[_currentPanelNum].panelRef.panelDataIsValid()) {
            return;
        }

        try {
            csPurchOrderAdapterFactory  purchOrdAdapterFactory = csPurchOrderAdapterFactory.getInstance();
            csPurchOrderAdapter         purchOrdAdapter = purchOrdAdapterFactory.getPurchOrderAdapter();
            SessionMetaData             sessionMeta = SessionMetaData.getInstance();

            DBRecSet payVouchers = paySelectPanel.getPayVoucherObjs();
            DBRec attribSet = new DBRec();

            for (int i=0; i<payVouchers.getSize(); i++) {
                attribSet = payVouchers.getRec(i);
                attribSet.addAttrib(new DBAttributes(payment_voucherObj.PAY_FROM_ACCT_ID, dateEntryPanel.getPayAcctId()));
                attribSet.addAttrib(new DBAttributes(payment_voucherObj.PAY_FROM_ACCT_NAME, dateEntryPanel.getPayAcctName()));
                attribSet.addAttrib(new DBAttributes(payment_voucherObj.PAYMENT_METHOD, dateEntryPanel.getPayMethod()));
                attribSet.addAttrib(new DBAttributes(payment_voucherObj.DATE_PAID, daiFormatUtil.getCurrentDate()));
                if (dateEntryPanel.getPayMethod().equals(global_settings_pay_methodsObj.PAY_METHOD_TYPE_CHECK)){
                    attribSet.addAttrib(new DBAttributes(payment_voucherObj.PRINT_CHECK, "Y"));
                }
            }

            purchOrdAdapter.postPurchOrderPayments(sessionMeta.getClientServerSecurity(),
                                                    payVouchers);

            //Let the user know that the everything was posted.
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(  this,
                                        payVouchers.getSize() + " Payment(s) Posted",
                                        "Payments",
                                        JOptionPane.INFORMATION_MESSAGE, null);
           //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            dateEntryPanel.resetPanel();
        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not Post Payments.\n"+ex.getLocalizedMessage());
        }
    }
}

