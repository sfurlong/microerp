
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;

import javax.swing.JOptionPane;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.global_settings_pay_methodsObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiCurrencyField;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiMaskField;
import daiBeans.daiPayMethodsComboBox;
import daiBeans.daiTextField;

public class CashReceiptPaymentPanel extends daiWizardPanel
{
    SessionMetaData sessionMeta;
    csDBAdapterFactory dbAdapterFactory = null;
    csDBAdapter dbAdapter = null;


    Logger _logger;
    daiLabel daiLabel_checkNo = new daiLabel("Check No:");
    daiPayMethodsComboBox daiComboBox_payMethod = new daiPayMethodsComboBox();
    daiLabel daiLabel_paymentAmt = new daiLabel("Payment Amt:");
    daiLabel daiLabel_payType = new daiLabel("Payment Method:");
    daiLabel daiLabel_ccNum = new daiLabel("Credit Card Num:");
    daiLabel daiLabel_ccExp = new daiLabel("Credit Cart Exp:");
    daiCurrencyField currencyField_paymentAmt = new daiCurrencyField();
    daiTextField daiTextField_checkNo = new daiTextField();
    daiTextField daiTextField_ccNum = new daiTextField();
    daiMaskField daiDateField_ccExp = new daiMaskField("##/##");
    daiLabel daiLabel_dateReceived = new daiLabel("Date Payment Received:");
    daiDateField daiDateField_dateReceived = new daiDateField();
    String _totAmtDue = "0.00";
    GroupBox groupBox_dateAmt = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();
    GroupBox groupBox_payment = new GroupBox();
    XYLayout xYLayout3 = new XYLayout();
    GridLayout gridLayout1 = new GridLayout();

    public CashReceiptPaymentPanel()
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
        _logger = Logger.getInstance();
        sessionMeta = SessionMetaData.getInstance();
        dbAdapterFactory = csDBAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();

        daiTextField_checkNo.setDisabled(true);
        daiTextField_ccNum.setDisabled(true);
        daiDateField_ccExp.setDisabled(true);


        this.setLayout(gridLayout1);
        daiComboBox_payMethod.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                daiComboBox_payMethod_itemStateChanged(e);
            }
        });
        groupBox_dateAmt.setLayout(xYLayout2);
        groupBox_payment.setLayout(xYLayout3);

        daiDateField_dateReceived.setCurrentDate(true);

        gridLayout1.setColumns(1);
        gridLayout1.setRows(2);
        groupBox_dateAmt.add(daiLabel_dateReceived, new XYConstraints(67, 25, -1, -1));
        groupBox_dateAmt.add(daiLabel_paymentAmt, new XYConstraints(118, 55, -1, -1));
        groupBox_dateAmt.add(daiDateField_dateReceived, new XYConstraints(198, 23, -1, -1));
        groupBox_dateAmt.add(currencyField_paymentAmt, new XYConstraints(198, 53, -1, -1));

        groupBox_payment.add(daiLabel_ccNum, new XYConstraints(97, 53, -1, -1));
        groupBox_payment.add(daiLabel_ccExp, new XYConstraints(103, 81, -1, -1));
        groupBox_payment.add(daiLabel_checkNo, new XYConstraints(129, 26, -1, -1));
        groupBox_payment.add(daiLabel_payType, new XYConstraints(96, 0, -1, -1));
        groupBox_payment.add(daiComboBox_payMethod, new XYConstraints(197, 0, 100, -1));
        groupBox_payment.add(daiTextField_checkNo, new XYConstraints(197, 27, -1, -1));
        groupBox_payment.add(daiTextField_ccNum, new XYConstraints(197, 53, 144, -1));
        groupBox_payment.add(daiDateField_ccExp, new XYConstraints(197, 80, 55, -1));

        this.add(groupBox_dateAmt, null);
        this.add(groupBox_payment, null);

        daiDateField_dateReceived.requestFocus();
        //Default the paymethod to CHECK
        daiComboBox_payMethod.setSelectedIndex(0);
        
    }

    public boolean panelDataIsValid()
    {
        String dateReceived = daiDateField_dateReceived.getText();
        if (dateReceived == null || dateReceived.trim().length() == 0) {
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter the Date payment was received.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        String payAmt = currencyField_paymentAmt.getText();
        if (payAmt == null || payAmt.length() == 0) {
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a valid Payment Amt.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        String payMethod = (String)daiComboBox_payMethod.getSelectedItem();
        if (payMethod == null || payMethod.trim().length() == 0) {
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a Payment Method.",
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

    public void setTotalAmtDue(String totAmtDue)
    {
        _totAmtDue = totAmtDue;
    }

    public String getPayMethod() {
        return daiComboBox_payMethod.getText();
    }

    public String getPaymentAmt() {
        return currencyField_paymentAmt.getText();
    }

    public String getCheckNum() {
        return daiTextField_checkNo.getText();
    }

    public String getCreditCardNum() {
        return daiTextField_ccNum.getText();
    }

    public String getDateReceived() {
        return daiDateField_dateReceived.getText();
    }

    public String getCreditCardExp() {
        return daiDateField_ccExp.getText();
    }

    public void resetPanel() {
        daiDateField_dateReceived.setCurrentDate(true);
        currencyField_paymentAmt.setText(null);
        //Default the paymethod to CHECK
        daiComboBox_payMethod.setSelectedIndex(0);
        daiTextField_checkNo.setText(null);
        daiTextField_ccNum.setText(null);
        daiDateField_ccExp.setText(null);

        daiDateField_dateReceived.requestFocus();
    }

    void daiComboBox_payMethod_itemStateChanged(ItemEvent e)
    {
        String selectedItem = (String)e.getItem();

        String payMethodType = daiComboBox_payMethod.getPayMethodType(selectedItem);
        if (selectedItem.equals(global_settings_pay_methodsObj.PAY_METHOD_TYPE_CHECK)) {
            daiTextField_checkNo.setDisabled(false);
            daiTextField_ccNum.setDisabled(true);
            daiDateField_ccExp.setDisabled(true);
        } else {
            daiTextField_checkNo.setDisabled(true);
            daiTextField_ccNum.setDisabled(false);
            daiDateField_ccExp.setDisabled(false);
        }
    }
}


