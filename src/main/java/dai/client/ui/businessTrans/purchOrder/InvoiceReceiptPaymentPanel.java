
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.payment_voucherObj;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiCheckBox;
import daiBeans.daiDataModifiedEvent;
import daiBeans.daiDataModifiedListener;
import daiBeans.daiDateField;
import daiBeans.daiGroupBox;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiPayTermsComboBox;
import daiBeans.daiTextField;

public class InvoiceReceiptPaymentPanel extends daiWizardPanel
{
    SessionMetaData sessionMeta;
    csDBAdapterFactory dbAdapterFactory = null;
    csDBAdapter dbAdapter = null;

    XYLayout xYLayout1 = new XYLayout();

    Logger _logger;
    daiPayTermsComboBox daiComboBox_payTerms = new daiPayTermsComboBox();
    daiLabel daiLabel_payTerms = new daiLabel("Payment Terms:");
    daiLabel daiLabel_payDueDate = new daiLabel("Payment Due Date:");
    daiDateField daiDateField_payDueDate = new daiDateField();
    daiDateField daiDateField_invoiceDate = new daiDateField();
    daiLabel daiLabel_invoiceDate = new daiLabel();
    daiTextField daiTextField_invoiceNum = new daiTextField();
    daiLabel daiLabel_invoiceNum = new daiLabel();

    InvoiceReceiptFrame CONTAINER_FRAME = null;
    daiGroupBox groupBox_multiPay = new daiGroupBox("Multiple Payments Option");
    daiCheckBox checkBox_multiPay = new daiCheckBox("Create Multiple Payment Vouchers");
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    daiNumField daiNumField_numPayments = new daiNumField();
    daiLabel daiLabel_numPayments = new daiLabel("Num Payments:");
    JFrame CONTAINER = null;

    public InvoiceReceiptPaymentPanel(JFrame container, InvoiceReceiptFrame frame)
    {
        CONTAINER = container;
        CONTAINER_FRAME = frame;
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
        _logger = Logger.getInstance();
        sessionMeta = SessionMetaData.getInstance();
        dbAdapterFactory = csDBAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();

        xYLayout1.setHeight(298);
        xYLayout1.setWidth(567);


        //Add Payment Types to ComboBox
        daiComboBox_payTerms.setSelectedIndex(daiPayTermsComboBox.NET30_INDEX);
        daiComboBox_payTerms.setEditable(false);
        daiComboBox_payTerms.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e)
            {
                adjustDateDue();
            }
        });

        this.setLayout(xYLayout1);

        //Default the date due plus 30 days
        daiDateField_payDueDate.setText(daiFormatUtil.getCurrentDatePlusNumDays(30));
        daiDateField_invoiceDate.setCurrentDate(true);
        daiDateField_invoiceDate.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e)
            {
                adjustDateDue();
            }
        });

        daiLabel_invoiceDate.setText("Invoice Date:");
        daiLabel_invoiceNum.setText("Invoice Num:");


        groupBox_multiPay.setLayout(gridBagLayout1);

        daiNumField_numPayments.setPreferredSize(new Dimension(-1, 20));
        this.add(daiLabel_invoiceDate, new XYConstraints(167, 94, -1, -1));
        this.add(daiLabel_invoiceNum, new XYConstraints(168, 122, -1, -1));
        this.add(daiLabel_payDueDate, new XYConstraints(138, 149, -1, -1));
        this.add(daiLabel_payTerms, new XYConstraints(152, 55, -1, -1));
        this.add(daiComboBox_payTerms, new XYConstraints(231, 52, 119, -1));
        this.add(daiDateField_invoiceDate, new XYConstraints(231, 93, -1, -1));
        this.add(daiTextField_invoiceNum, new XYConstraints(231, 119, -1, -1));
        this.add(daiDateField_payDueDate, new XYConstraints(231, 146, -1, -1));

        this.add(groupBox_multiPay, new XYConstraints(135, 178, 345, 105));
        groupBox_multiPay.add(daiLabel_numPayments, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        groupBox_multiPay.add(checkBox_multiPay, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, -22, 2, 31), 0, 0));
        groupBox_multiPay.add(daiNumField_numPayments, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    public boolean panelDataIsValid()
    {
        //Must enter a valid Pay Due Date.
        if (daiDateField_payDueDate.getText() == null) {
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a Payment Due Date.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        //Must enter a valid INvoice Date.
        if (daiDateField_invoiceDate.getText() == null) {
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter an Invoice Date.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        String invNum = daiTextField_invoiceNum.getText();
        if (invNum == null) {
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter an Invoice Number.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        //Check to see if the invoice has been received yet.
        String vendId = CONTAINER_FRAME.invSelectPanel.daiTextField_vendorId.getText();
        Vector idVect = null;
        try {
            idVect = dbAdapter.getAllIds(sessionMeta.getClientServerSecurity(),
                            payment_voucherObj.TABLE_NAME,
                            payment_voucherObj.INVOICE_NUM + "='"+ invNum+"' and " +
                            payment_voucherObj.VENDOR_ID + "='" + vendId+"' and " +
                            " locality='"+payment_voucherObj.getObjLocality()+"' and " +
                            " (is_voided is null or is_voided = 'N')");
        } catch (Exception e) {
            _logger.logError(CONTAINER, "Could not check if Invoice has already been received.\n"+
                            e.getLocalizedMessage());
        }
        if (idVect.size() > 0) {
            JOptionPane.showMessageDialog(this  ,
                                      "This vendor invoice has already been received.\n"+
                                      "If you would like to re-receive it, please void the Payment Voucher " +
                                      idVect.firstElement() + " first.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        if (checkBox_multiPay.isSelected()) {
            String s_numPayments = daiNumField_numPayments.getText();
            if (s_numPayments == null) s_numPayments = "0";
            int i_numPayments = Integer.parseInt(s_numPayments);
            if (i_numPayments <= 0) {
               JOptionPane.showMessageDialog(this  ,
                                      "A positive value is required, if choosing to create multiple Payment Vouchers.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
                return false;
            }
        }

        return true;
    }
    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }

    public String getPayTerms()
    {
        return (String)daiComboBox_payTerms.getSelectedItem();
    }

    public String getPayDueDate()
    {
        return daiDateField_payDueDate.getText();
    }

    public String getInvoiceNum() {
        return daiTextField_invoiceNum.getText();
    }

    public String getInvoiceDate() {
        return daiDateField_invoiceDate.getText();
    }

    public int getNumPayments() {
        String s_numPayments = daiNumField_numPayments.getText();
        if (s_numPayments == null) s_numPayments = "0";
        int i_numPayments = Integer.parseInt(s_numPayments);
        if (checkBox_multiPay.isSelected()) {
            return i_numPayments;
        } else {
            return 1;
        }
    }

    public void resetPanel() {
        daiComboBox_payTerms.setSelectedIndex(0);  //Default to net 30
        daiTextField_invoiceNum.setText(null);
        checkBox_multiPay.setSelected(false);
        daiNumField_numPayments.setText(null);

        //Update the Banner
        if (CONTAINER_FRAME != null) {
            CONTAINER_FRAME.setBannerRightText("");
        }
    }

    private void adjustDateDue()
    {
        String payTerms = daiComboBox_payTerms.getText();
        if (payTerms.equals(purch_orderObj.PAY_TERMS_NET30) ||
            payTerms.equals(purch_orderObj.PAY_TERMS_NET30_1_10) ||
            payTerms.equals(purch_orderObj.PAY_TERMS_NET30_2_10))
        {
            daiDateField_payDueDate.setText(daiFormatUtil.adjustDate(daiDateField_invoiceDate.getText(), 30));
        } else if (payTerms.equals(purch_orderObj.PAY_TERMS_NET45)) {
            daiDateField_payDueDate.setText(daiFormatUtil.adjustDate(daiDateField_invoiceDate.getText(), 45));
        } else if (payTerms.equals(purch_orderObj.PAY_TERMS_NET15)) {
            daiDateField_payDueDate.setText(daiFormatUtil.adjustDate(daiDateField_invoiceDate.getText(), 15));
        } else if (payTerms.equals(purch_orderObj.PAY_TERMS_NET60)) {
            daiDateField_payDueDate.setText(daiFormatUtil.adjustDate(daiDateField_invoiceDate.getText(), 60));
        } else {
            daiDateField_payDueDate.setText(daiDateField_invoiceDate.getText());
        }
    }
}


