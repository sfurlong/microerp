
//Title:        order
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI for Entry/Update of Orders

package dai.client.ui.sysAdmin;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import pv.jfcx.JPVButton;

import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiBannerPanel;
import dai.client.clientShared.daiWizardPanel;
import dai.shared.cmnSvcs.Logger;

public class JournalEntryFrame extends JFrame
{
    JournalEntryPanel entryPanel = new JournalEntryPanel();

    JPanel buttonPanel = new JPanel();
    JPVButton button_cancel = new JPVButton();
    JPVButton button_prev = new JPVButton();
    JPVButton button_next = new JPVButton();
    JPVButton button_finished = new JPVButton();
    daiBannerPanel  bannerPanel = new daiBannerPanel();

    JPanel jPanel_contentPanel = new JPanel();
    CardLayout cardLayout1 = new CardLayout();
    WizardPanel[]    wizardPanels;
    int         currentPanelNum = 0;

    Logger LOGGER;
    XYLayout xYLayout1 = new XYLayout();
    BoxLayout2 boxLayout21 = new BoxLayout2();

    public JournalEntryFrame()
    {
        super();
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            LOGGER.logError(this, ex.getLocalizedMessage());
        }
    }

    private void jbInit() throws Exception
    {
        LOGGER = LOGGER.getInstance();

        //Update the Banner Panel
        bannerPanel.setBannerLeftText("Journal Entry Wizard");
        this.setTitle("Journal Entry Wizard");
        this.getContentPane().setLayout(boxLayout21);
        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setMaximumSize(new Dimension(32767, 33));
        buttonPanel.setMinimumSize(new Dimension(600, 33));
        buttonPanel.setPreferredSize(new Dimension(600, 33));
        buttonPanel.setLayout(xYLayout1);
        button_next.setText("Next >>");
        button_next.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_next_actionPerformed(e);
            }
        });
        button_prev.setText("<< Prev");
        button_prev.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_prev_actionPerformed(e);
            }
        });
        button_cancel.setText("Cancel");
        button_cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_cancel_actionPerformed(e);
            }
        });
        button_finished.setText("Done");
        button_finished.setEnabled(false);
        button_finished.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_finished_actionPerformed(e);
            }
        });
        jPanel_contentPanel.setLayout(cardLayout1);
        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        buttonPanel.add(button_finished, new XYConstraints(395, 3, -1, -1));
        buttonPanel.add(button_prev, new XYConstraints(213, 3, -1, -1));
        buttonPanel.add(button_next, new XYConstraints(300, 3, -1, -1));
        buttonPanel.add(button_cancel, new XYConstraints(120, 3, -1, -1));


        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(entryPanel, "entryPanel"),
                                        };
        jPanel_contentPanel.add(wizardPanels[0].panelRef, wizardPanels[0].panelName);

        button_prev.setEnabled(false);

        this.getContentPane().add(bannerPanel, null);
        this.getContentPane().add(jPanel_contentPanel, null);
        this.getContentPane().add(buttonPanel, null);

        centerFrame();
        show();
    }

    public void setBannerLeftText(String t)
    {
        bannerPanel.setBannerLeftText(t);
    }

    public String getBannerLeftText()
    {
        return bannerPanel.getBannerLeftText();
    }

    public void setBannerRightText(String t)
    {
        bannerPanel.setBannerRightText(t);
    }

    public String getBannerRightText()
    {
        return bannerPanel.getBannerRightText();
    }

	private void centerFrame()
	{
		//Center the window
        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
	}

    class WizardPanel {
        public daiWizardPanel  panelRef;
        public String  panelName;

        WizardPanel(daiWizardPanel panel, String name) {
            panelRef = panel;
            panelName = name;
        }
    }

    void button_cancel_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    void button_prev_actionPerformed(ActionEvent e) {
        if (currentPanelNum > 0) {
            currentPanelNum--;
            button_finished.setEnabled(false);
            button_next.setEnabled(true);
            cardLayout1.show(jPanel_contentPanel, wizardPanels[currentPanelNum].panelName);
        } else {
            button_prev.setEnabled(false);
        }
    }

    void button_next_actionPerformed(ActionEvent e) {
        //If the data on the current panel is valid we can move to the
        //next panel.
        if (wizardPanels[currentPanelNum].panelRef.panelDataIsValid())
        {
            button_prev.setEnabled(true);
            currentPanelNum++;
            if (currentPanelNum == 1) {
//                acctsPanel.setExpenseAcct(billEntryPanel.getExpenseAcctId(),
//                                        billEntryPanel.getExpenseAcctName());
                button_finished.setEnabled(true);
                button_next.setEnabled(false);
            }
            wizardPanels[currentPanelNum].panelRef.doPreDisplayProcessing(null);
            cardLayout1.show(jPanel_contentPanel, wizardPanels[currentPanelNum].panelName);
        }
    }

    void button_finished_actionPerformed(ActionEvent e) {
/*
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

        vouchData.addAttribSet(attribSet);

        try {
            SessionMetaData     sessionMeta = SessionMetaData.getInstance();
            csPurchOrderAdapterFactory purchOrderFactory = csPurchOrderAdapterFactory.getInstance();
            csPurchOrderAdapter purchOrderAdapter = purchOrderFactory.getPurchOrderAdapter();

            String payVoucherId = purchOrderAdapter.postInvoiceReceipt(sessionMeta.getClientServerSecurity(),
                                               vouchData, false);

            //Let the user know that the Shipment was created.
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(  this,
                                        "Bill Receipt Posted.  New Pay Voucher#: " + payVoucherId,
                                        "Bill Receipt Posted",
                                        JOptionPane.INFORMATION_MESSAGE, null);
           this.dispose();
        } catch (Exception ex) {
            LOGGER.logError(this, "Could not Post Bill Receipt.\n"+ex.getLocalizedMessage());
        }
*/
    }
}

