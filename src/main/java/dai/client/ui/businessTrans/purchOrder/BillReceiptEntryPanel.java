
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.businessObjs.vendorObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCheckBox;
import daiBeans.daiCurrencyField;
import daiBeans.daiDataModifiedEvent;
import daiBeans.daiDataModifiedListener;
import daiBeans.daiDateField;
import daiBeans.daiGroupBox;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiPayTermsComboBox;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;

public class BillReceiptEntryPanel extends daiWizardPanel
{
    SessionMetaData sessionMeta;
    csDBAdapterFactory dbAdapterFactory = null;
    csDBAdapter dbAdapter = null;

    XYLayout xYLayout1 = new XYLayout();
    DefaultListModel listModel_right = new DefaultListModel();
    DefaultListModel listModel_left = new DefaultListModel();

    Logger _logger;
    daiTextField daiTextField_vendorName = new daiTextField();
    daiCurrencyField daiCurrency_amtDue = new daiCurrencyField();
    daiLabel daiLabel_vendorId = new daiLabel("Vendor Id:");
    daiTextField daiTextField_vendorId = new daiTextField();
    daiLabel daiLabel_amtDue = new daiLabel("Total Amt Due:");

    BillReceiptFrame _CONTAINER_FRAME = null;
    daiLabel daiLabel_dateDue = new daiLabel();
    daiLabel daiLabel_refNum = new daiLabel();
    daiLabel daiLabel_payTerms = new daiLabel();
    daiDateField daiDateField_dateDue = new daiDateField();
    daiTextField daiTextField_refNum = new daiTextField();
    daiPayTermsComboBox daiComboBox_payTerms = new daiPayTermsComboBox();
    daiTextArea textArea_note   = new daiTextArea();
    daiLabel daiLabel_note = new daiLabel();

    String expenseAcctId = "";
    String expenseAcctName = "";
    daiLabel daiLabel_refDate = new daiLabel();
    daiDateField daiDateField_refDate = new daiDateField();

    daiGroupBox groupBox_multiPay = new daiGroupBox("Multiple Payments Option");
    daiCheckBox checkBox_multiPay = new daiCheckBox("Create Multiple Payment Vouchers");
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    daiNumField daiNumField_numPayments = new daiNumField();
    daiLabel daiLabel_numPayments = new daiLabel("Num Payments:");

    JFrame CONTAINER = null;

    public BillReceiptEntryPanel(JFrame container, BillReceiptFrame containerFrame)
    {
        CONTAINER = container;
        _CONTAINER_FRAME = containerFrame;

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

        xYLayout1.setHeight(276);
        xYLayout1.setWidth(551);

        this.setLayout(xYLayout1);

        //Set some defaults
        daiCurrency_amtDue.setText("0.00");
        daiComboBox_payTerms.setSelectedIndex(0);  //Default to net 30
        daiComboBox_payTerms.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e)
            {
                adjustDateDue();
            }
        });

        daiLabel_amtDue.setText("Amt Due:");
        daiLabel_amtDue.setFont(new java.awt.Font("Dialog", 1, 11));
        daiLabel_dateDue.setText("Date Due:");
        daiLabel_refNum.setText("Ref Num:");
        daiLabel_payTerms.setText("Pay Terms");
        daiLabel_vendorId.setHREFstyle(true);
        daiLabel_vendorId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_vendorId_daiAction(e);
            }
        });
        daiLabel_note.setText("Note:");
        daiTextField_vendorId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e)
            {
                if (!e.isTemporary()) {
                    daiTextField_vendorId_keyPressed(e);
                }
            }
            });        

        daiLabel_refDate.setText("Bill Date:");
        daiDateField_refDate.setText(daiFormatUtil.getCurrentDate());
        daiDateField_refDate.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(FocusEvent e) {
                daiDateField_refDate_focusLost(e);
            }
        });
        daiDateField_refDate.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e)
            {
                adjustDateDue();
            }
        });
        daiDateField_dateDue.setText(daiFormatUtil.getCurrentDatePlusNumDays(30));

        groupBox_multiPay.setLayout(gridBagLayout1);

        this.add(daiLabel_vendorId, new XYConstraints(59, 29, -1, -1));
        this.add(daiLabel_refNum, new XYConstraints(65, 89, -1, -1));
        this.add(daiLabel_note, new XYConstraints(84, 203, -1, -1));
        this.add(daiLabel_amtDue, new XYConstraints(59, 175, -1, -1));
        this.add(daiLabel_refDate, new XYConstraints(68, 59, -1, -1));
        this.add(daiLabel_payTerms, new XYConstraints(58, 118, -1, -1));
        this.add(daiLabel_dateDue, new XYConstraints(62, 145, -1, -1));
        this.add(daiTextField_vendorId, new XYConstraints(115, 28, 127, -1));
        this.add(daiTextField_vendorName, new XYConstraints(245, 28, 276, -1));
        this.add(daiDateField_refDate, new XYConstraints(115, 57, -1, -1));
        this.add(daiTextField_refNum, new XYConstraints(115, 86, -1, -1));
        this.add(daiComboBox_payTerms, new XYConstraints(115, 114, 128, -1));
        this.add(daiDateField_dateDue, new XYConstraints(115, 144, -1, -1));
        this.add(daiCurrency_amtDue, new XYConstraints(115, 173, -1, -1));
        this.add(textArea_note, new XYConstraints(115, 203, 412, 49));

        this.add(groupBox_multiPay, new XYConstraints(266, 76, 252, 92));
        groupBox_multiPay.add(daiLabel_numPayments, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        groupBox_multiPay.add(checkBox_multiPay, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, -20, 2, 31), 0, 0));
        groupBox_multiPay.add(daiNumField_numPayments, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    }

    public boolean panelDataIsValid()
    {
        //Must enter a valid Purchase Order Id.
        if (daiTextField_vendorId.getText() == null) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a Vendor Id.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        if (daiDateField_dateDue.isNull()) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a valid Date Due.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        String payTerms = (String)daiComboBox_payTerms.getSelectedItem();
        if (payTerms == null || payTerms.length() == 0) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter valid Payment Terms.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        //Make sure the amount due is correct.
        double amtDue = Double.parseDouble(daiCurrency_amtDue.getText());
        if (amtDue == 0) {
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a non-zero amout for Amount Due.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        //Make sure the Bill Date is correct.
        String billDate = daiDateField_refDate.getText();
        if (billDate == null || billDate.trim().length() == 0) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a valid Bill Date.",
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

    //Getter's
    public String getVendorId()
    {
        return daiTextField_vendorId.getText();
    }

    public String getVendorName()
    {
        return daiTextField_vendorName.getText();
    }

    public String getDueDate()
    {
        return daiDateField_dateDue.getText();
    }

    public String getRefNum()
    {
        return daiTextField_refNum.getText();
    }

    public String getRefDate() {
        return daiDateField_refDate.getText();
    }

    public String getPayTerms()
    {
        return (String)daiComboBox_payTerms.getSelectedItem();
    }

    public String getAmtDue()
    {
        return daiCurrency_amtDue.getText();
    }

    public String getExpenseAcctId()
    {
        return expenseAcctId;
    }

    public String getExpenseAcctName()
    {
        return expenseAcctName;
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
        daiTextField_vendorId.setText(null);
        daiTextField_refNum.setText(null);
        textArea_note.setText(null);
        daiTextField_vendorName.setText(null);
        daiCurrency_amtDue.setText("0.00");
        daiComboBox_payTerms.setSelectedIndex(0);  //Default to net 30
        daiDateField_refDate.setText(daiFormatUtil.getCurrentDate());
        daiDateField_dateDue.setText(daiFormatUtil.getCurrentDatePlusNumDays(30));
        checkBox_multiPay.setSelected(false);
        daiNumField_numPayments.setText(null);

        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText("");
        }
        daiTextField_vendorId.requestFocus();
    }

    private void daiLabel_vendorId_daiAction(daiActionEvent e)
    {
		vendorObj tempObj = new vendorObj();
        String id = daiTextField_vendorId.getText();
        String name = daiTextField_vendorName.getText();

        DBAttributes attribs1 = new DBAttributes(vendorObj.ID, id, "Vendor Id", 100);
        DBAttributes attribs2 = new DBAttributes(vendorObj.NAME, name, "Vendor Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, " order by " + vendorObj.NAME);
		chooser.show();
        vendorObj chosenObj = (vendorObj)chooser.getChosenObj();
        if (chosenObj != null) {
            populatePanel(chosenObj);
        }
    	chooser.dispose();
    }

    private void daiTextField_vendorId_keyPressed(FocusEvent e)
    {
		String id = daiTextField_vendorId.getText();

		if (id != null && id.trim().length() > 0)
		{
    		String exp = " locality = " + "'" + vendorObj.getObjLocality() + "'" +
                        " and id = '" + id + "' ";
            try {
                Vector vendorObjsVect = dbAdapter.queryByExpression( sessionMeta.getClientServerSecurity(),
                                                                    new vendorObj(),
                                                                    exp);
                //Does the input vendor exist?
                if (vendorObjsVect.size() == 0) {
                    //Let the user know that the vendor Does not exist.
                    JOptionPane.showMessageDialog(this  ,
                                        "Selected Vendor Does Not Exist.  Please try another.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);

                    //Clear out the entry field.
                    daiTextField_vendorId.setText(null);
                } else {
        			//Disable the Trans ID text field.
                    vendorObj venObj = (vendorObj)vendorObjsVect.firstElement();
                    populatePanel(venObj);
                }
            } catch (Exception ex) {
                _logger.logError(CONTAINER, "Could not get Vendor Data.\n"+ex.getLocalizedMessage());
            }
		}
    }

    private void populatePanel(vendorObj obj) {
        daiTextField_vendorId.setText(obj.get_id());
        daiTextField_vendorName.setText(obj.get_name());

        //Do some double duty here.  Save off some expense acct data
        //so the other panels don't have to do a query later on.
        expenseAcctId = obj.get_primary_acct();
        expenseAcctName = obj.get_primary_acct_name();

        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText(": " + obj.get_id());
        }

        //Set the focus to the next entry field
        daiDateField_refDate.requestFocus();
    }

    private void adjustDateDue()
    {
        String payTerms = daiComboBox_payTerms.getText();
        if (payTerms.equals(purch_orderObj.PAY_TERMS_NET30) ||
            payTerms.equals(purch_orderObj.PAY_TERMS_NET30_1_10) ||
            payTerms.equals(purch_orderObj.PAY_TERMS_NET30_2_10))
        {
            daiDateField_dateDue.setText(daiFormatUtil.adjustDate(daiDateField_refDate.getText(), 30));
        } else if (payTerms.equals(purch_orderObj.PAY_TERMS_NET45)) {
            daiDateField_dateDue.setText(daiFormatUtil.adjustDate(daiDateField_refDate.getText(), 45));
        } else if (payTerms.equals(purch_orderObj.PAY_TERMS_NET15)) {
            daiDateField_dateDue.setText(daiFormatUtil.adjustDate(daiDateField_refDate.getText(), 15));
        } else if (payTerms.equals(purch_orderObj.PAY_TERMS_NET60)) {
            daiDateField_dateDue.setText(daiFormatUtil.adjustDate(daiDateField_refDate.getText(), 60));
        } else {
            daiDateField_dateDue.setText(daiDateField_refDate.getText());
        }
    }

    void daiDateField_refDate_focusLost(FocusEvent e) {
        if (e.isTemporary()) return;
    }
}

