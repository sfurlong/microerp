
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.Color;
import java.awt.event.FocusEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.locationObj;
import dai.shared.businessObjs.payment_voucherObj;
import dai.shared.businessObjs.vendorObj;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCheckBox;
import daiBeans.daiComboBox;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiQueryTextField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;

public class PayVoucherPanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();
    daiTextArea textAreaControl_note1 = new daiTextArea();

	daiLabel daiLabel_id = new daiLabel("Id:");
    daiLabel daiLabel_note1 = new daiBeans.daiLabel("Note1:");

    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();
    GroupBox groupBox_billInfo = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();
    daiTextField daiTextField_invoiceNum = new daiTextField();
    daiLabel daiLabel_invoiceNum = new daiLabel();
    daiLabel daiLabel_invoiceDate = new daiLabel();
    daiDateField daiDateField_invoiceDate = new daiDateField();
    daiTextField daiTextField_checkNum = new daiTextField();
    daiTextField daiTextField_acctId = new daiTextField();
    daiTextField daiTextField_acctName = new daiTextField();
    daiLabel daiLabel_checkNum = new daiLabel();
    daiLabel daiLabel_withdrawlAcct = new daiLabel();
    daiComboBox daiComboBox_payMethod = new daiComboBox();
    daiLabel daiLabel_payMethod = new daiLabel();
    daiCheckBox checkBox_printCheck = new daiCheckBox("Print Check");
    GroupBox groupBox_payRelated = new GroupBox();
    XYLayout xYLayout3 = new XYLayout();
    daiLabel daiLabel_purchOrdId = new daiLabel();
    daiLabel daiLabel_billTotAmt = new daiLabel();
    daiLabel daiLabel_vendorId = new daiLabel();
    daiTextField daiTextField_purchOrdId = new daiTextField();
    daiTextField daiTextField_billTotAmt = new daiTextField();
    daiQueryTextField daiTextField_vendorId = new daiQueryTextField(new vendorObj());
    daiTextField daiTextField_vendorName = new daiTextField();
    daiTextField daiTextField_totPaymentsPosted = new daiTextField();
    daiLabel daiLabel_totPaymentsPosted = new daiLabel();
    daiDateField daiDateField_payDueDate = new daiDateField();
    daiLabel daiLabel_payDueDate = new daiLabel();
    daiLabel daiLabel_acctid = new daiLabel();
    daiTextField daiTextField_payFromAcctId = new daiTextField();
    daiTextField daiTextField_payFromAcctName = new daiTextField();
    daiLabel daiLabel_datePaid = new daiLabel();
    daiDateField daiDateField_datePaid = new daiDateField();
    JLabel jLabel_void = new JLabel();
    daiLabel daiLabel_shipCharges = new daiLabel("Shipping Charges:");
    daiTextField daiTextField_shipCharges = new daiTextField();
    daiDateField daiDateField_checkSentDate = new daiDateField();


	public PayVoucherPanel(JFrame container, daiFrame parentFrame, payment_voucherObj obj)
	{
		super(container, parentFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	void jbInit() throws Exception
	{
		setLayout(xYLayout2);
		xYLayout2.setHeight(408);
		xYLayout2.setWidth(578);

        daiLabel_note1.setText("Note:");

        this.setBackground(daiColors.PanelColor);
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                ID_TEXT_FIELD_focusLost(e);
            }
        });

        daiLabel_vendorId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_vendorId_daiAction(e);
            }
        });
        daiTextField_vendorId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiTextField_vendorId_daiActionEvent(e);
            }
        });

        groupBox_billInfo.setLayout(xYLayout1);
        daiLabel_invoiceNum.setText("Invoice/Ref Num:");
        daiLabel_invoiceDate.setText("Invoice Date:");
        daiLabel_checkNum.setText("Check Num:");
        daiLabel_withdrawlAcct.setText("Pay From Acct:");
        daiLabel_payMethod.setText("Pay Method:");
        checkBox_printCheck.setOpaque(false);
        groupBox_payRelated.setLabel("Payment Info");
        groupBox_payRelated.setLayout(xYLayout3);
        groupBox_billInfo.setLabel("Bill Info");
        daiLabel_purchOrdId.setText("Purch Order Id:");
        daiLabel_billTotAmt.setText("Bill Total Amt:");
        daiLabel_vendorId.setText("Vendor Id:");
        daiLabel_totPaymentsPosted.setText("Total Payments Made:");
        daiLabel_id.setText("Voucher Num:");
        daiLabel_payDueDate.setText("Payment Due Date:");
        daiLabel_acctid.setText("Expnse Acct Id:");
        daiLabel_datePaid.setText("Date Paid:");
        daiLabel_vendorId.setHREFstyle(true);

        jLabel_void.setFont(new java.awt.Font("Dialog", 1, 24));
        jLabel_void.setForeground(Color.red);
        jLabel_void.setText("VOID");
        jLabel_void.setVisible(false);

        groupBox_billInfo.add(daiLabel_purchOrdId, new XYConstraints(8, 7, -1, -1));
        groupBox_billInfo.add(daiLabel_invoiceNum, new XYConstraints(0, 29, -1, -1));
        groupBox_billInfo.add(daiLabel_invoiceDate, new XYConstraints(220, 29, -1, -1));
        groupBox_billInfo.add(daiLabel_vendorId, new XYConstraints(31, 80, -1, -1));
        groupBox_billInfo.add(daiLabel_billTotAmt, new XYConstraints(17, 55, -1, -1));
        groupBox_billInfo.add(daiLabel_payDueDate, new XYConstraints(191, 6, -1, -1));
        groupBox_billInfo.add(daiLabel_note1, new XYConstraints(56, 126, -1, -1));
        groupBox_billInfo.add(daiLabel_acctid, new XYConstraints(5, 103, -1, -1));
        groupBox_billInfo.add(daiTextField_purchOrdId, new XYConstraints(87, 4, -1, -1));
        groupBox_billInfo.add(daiDateField_payDueDate, new XYConstraints(287, 2, -1, -1));
        groupBox_billInfo.add(daiTextField_invoiceNum, new XYConstraints(87, 29, -1, -1));
        groupBox_billInfo.add(daiDateField_invoiceDate, new XYConstraints(287, 27, -1, -1));
        groupBox_billInfo.add(daiTextField_billTotAmt, new XYConstraints(87, 54, -1, -1));
        groupBox_billInfo.add(daiTextField_vendorId, new XYConstraints(87, 78, 118, -1));
        groupBox_billInfo.add(daiTextField_vendorName, new XYConstraints(207, 78, 221, -1));
        groupBox_billInfo.add(daiTextField_acctId, new XYConstraints(87, 101, 118, -1));
        groupBox_billInfo.add(daiTextField_acctName, new XYConstraints(207, 101, 221, -1));
        groupBox_billInfo.add(textAreaControl_note1, new XYConstraints(88, 124, 338, 49));
        groupBox_billInfo.add(daiTextField_shipCharges, new XYConstraints(287, 52, -1, -1));
        groupBox_billInfo.add(daiLabel_shipCharges, new XYConstraints(194, 54, -1, -1));

        groupBox_payRelated.add(daiLabel_totPaymentsPosted, new XYConstraints(-2, 3, -1, -1));
        groupBox_payRelated.add(daiLabel_payMethod, new XYConstraints(44, 28, -1, -1));
        groupBox_payRelated.add(daiLabel_checkNum, new XYConstraints(46, 53, -1, -1));
        groupBox_payRelated.add(daiLabel_withdrawlAcct, new XYConstraints(21, 79, 82, -1));
        groupBox_payRelated.add(daiLabel_datePaid, new XYConstraints(232, 3, -1, -1));
        groupBox_payRelated.add(new daiLabel("Date Sent:"), new XYConstraints(232, 28, -1, -1));
        groupBox_payRelated.add(daiTextField_totPaymentsPosted, new XYConstraints(112, 1, -1, -1));
        groupBox_payRelated.add(daiDateField_datePaid, new XYConstraints(285, 1, -1, -1));
        groupBox_payRelated.add(daiDateField_checkSentDate, new XYConstraints(285, 28, -1, -1));
        groupBox_payRelated.add(daiComboBox_payMethod, new XYConstraints(112, 26, 114, -1));
        groupBox_payRelated.add(daiTextField_checkNum, new XYConstraints(112, 53, -1, -1));
        groupBox_payRelated.add(checkBox_printCheck, new XYConstraints(232, 51, -1, -1));
        groupBox_payRelated.add(daiTextField_payFromAcctId, new XYConstraints(112, 78, -1, -1));
        groupBox_payRelated.add(daiTextField_payFromAcctName, new XYConstraints(218, 78, 207, -1));
        this.add(jLabel_void, new XYConstraints(235, 15, -1, -1));

        this.add(userIdDateCreatedPanel, new XYConstraints(333, 7, -1, -1));
        this.add(daiLabel_id, new XYConstraints(30, 25, -1, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(102, 24, 104, -1));
        this.add(groupBox_billInfo, new XYConstraints(32, 62, 467, 202));
        this.add(groupBox_payRelated, new XYConstraints(34, 269, 467, 127));
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        locationObj obj = new locationObj();
        locationObj tempObj = (locationObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
        obj.set_locality(locationObj.getObjLocality());

        return obj;
    }

	public int refresh()
	{

		//Call the base class method first.
		super.refresh();

		//Enable the ID text field.
		ID_TEXT_FIELD.setDisabled(false);

		return 0;
	}

	public int query(String id)
	{
		//Call the base class query then do our extended logic.
		super.query(id);

		//Disable the Trans ID text field.
		ID_TEXT_FIELD.setDisabled(true);
		return 0;
	}

    public boolean voidPayment() {

        if (ID_TEXT_FIELD.getText() == null) return false;

   		payment_voucherObj obj = (payment_voucherObj)BUSINESS_OBJ;

        String isVoided = obj.get_is_voided();
        if (isVoided != null && isVoided.equals("Y")) {
            JOptionPane.showMessageDialog(CONTAINER_FRAME,
                                          "Already voided.",
                                          "Warning",
                                           JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        DBRec vouchAttribs = new DBRec();
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.TOTAL_PAYMENTS_POSTED, daiTextField_totPaymentsPosted.getText()));
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.PAYMENT_AMT, daiTextField_billTotAmt.getText()));
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.TOTAL_SHIPPING_CHARGES, daiTextField_shipCharges.getText()));
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.CHECK_NUM, daiTextField_checkNum.getText()));
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.PAY_FROM_ACCT_ID, daiTextField_payFromAcctId.getText()));
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.ACCTID, daiTextField_acctId.getText()));
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.PURCH_ORDER_ID, daiTextField_purchOrdId.getText()));
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.ID, ID_TEXT_FIELD.getText()));
        vouchAttribs.addAttrib(new DBAttributes(payment_voucherObj.VENDOR_NAME, daiTextField_vendorName.getText()));

        //Void the Payment
        csPurchOrderAdapter poAdapter = csPurchOrderAdapterFactory.getInstance().getPurchOrderAdapter();
        try {
            //Expects the following attributes.
            // payment_voucherObj.TOTAL_PAYMENTS_POSTED
            // payment_voucherObj.PAYMENT_AMT
            // payment_voucherObj.TOTAL_SHIPPING_CHARGES
            // payment_voucherObj.CHECK_NUM
            // payment_voucherObj.PAY_FROM_ACCT_ID
            // payment_voucherObj.ACCT_ID
            // payment_voucherObj.PURCH_ORDER_ID
            // payment_voucherObj.ID
            // payment_voucherObj.VENDOR_NAME
            poAdapter.voidPurchOrderPayment(_sessionMeta.getClientServerSecurity(), vouchAttribs);

            obj.set_is_voided("Y");
            jLabel_void.setVisible(true);

            //Set the dirty flag so that this panel data will get saved
            this._panelIsDirty = true;

        } catch (Exception e) {
            LOGGER.logError(CONTAINER, "Could not Void Payments.\n"+e.getLocalizedMessage());
            return false;
        }
        return true;
    }

	protected void update_UI(BusinessObject bobj)
	{
		payment_voucherObj obj = (payment_voucherObj)bobj;

		ID_TEXT_FIELD.setText(obj.get_id());
        userIdDateCreatedPanel.setUserId(obj.get_created_by());
        userIdDateCreatedPanel.setDateCreated(obj.get_date_created());

		daiTextField_acctId.setText(obj.get_acctid());
        daiTextField_acctName.setText(obj.get_acctname());
        daiTextField_checkNum.setText(obj.get_check_num());
        daiTextField_invoiceNum.setText(obj.get_invoice_num());
        daiTextField_purchOrdId.setText(obj.get_purch_order_id());
        daiDateField_invoiceDate.setText(obj.get_invoice_date());
        daiComboBox_payMethod.setText(obj.get_payment_method());
        textAreaControl_note1.setText(obj.get_note());
        daiTextField_purchOrdId.setText(obj.get_purch_order_id());
        daiTextField_billTotAmt.setText(obj.get_total_value());
        daiTextField_totPaymentsPosted.setText(obj.get_total_payments_posted());
        daiTextField_vendorId.setText(obj.get_vendor_id());
        daiTextField_vendorName.setText(obj.get_vendor_name());
        daiDateField_payDueDate.setText(obj.get_payment_due_date());
        daiDateField_datePaid.setText(obj.get_date_paid());
        daiTextField_payFromAcctId.setText(obj.get_pay_from_acct_id());
        daiTextField_payFromAcctName.setText(obj.get_pay_from_acct_name());
        daiTextField_shipCharges.setText(obj.get_total_shipping_charges());
        checkBox_printCheck.setValue(obj.get_print_check());
        daiDateField_checkSentDate.setText(obj.get_date_check_sent());

        //Check to see if this voucher has been voided
        if (obj.get_is_voided() != null && obj.get_is_voided().equals("Y")) {
            jLabel_void.setVisible(true);
        } else {
            jLabel_void.setVisible(false);
        }

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		payment_voucherObj obj = (payment_voucherObj)BUSINESS_OBJ;

		ID_TEXT_FIELD.setText(obj.get_id());
        obj.set_created_by(userIdDateCreatedPanel.getUserId());
        obj.set_date_created(userIdDateCreatedPanel.getDateCreated());
		obj.set_acctid(daiTextField_acctId.getText());
        obj.set_acctname( daiTextField_acctName.getText());
        obj.set_check_num(daiTextField_checkNum.getText());
        obj.set_invoice_num(daiTextField_invoiceNum.getText());
        obj.set_purch_order_id(daiTextField_purchOrdId.getText());
        obj.set_invoice_date(daiDateField_invoiceDate.getText());
        obj.set_payment_method(daiComboBox_payMethod.getText());
        obj.set_note(textAreaControl_note1.getText());
        obj.set_total_value(daiTextField_billTotAmt.getText());
        obj.set_total_payments_posted(daiTextField_totPaymentsPosted.getText());
        obj.set_vendor_id(daiTextField_vendorId.getText());
        obj.set_vendor_name(daiTextField_vendorName.getText());
        obj.set_payment_due_date(daiDateField_payDueDate.getText());
        obj.set_date_paid(daiDateField_datePaid.getText());
        obj.set_pay_from_acct_id(daiTextField_payFromAcctId.getText());
        obj.set_pay_from_acct_name(daiTextField_payFromAcctName.getText());
        obj.set_print_check(checkBox_printCheck.getValue());
        obj.set_total_shipping_charges(daiTextField_shipCharges.getText());
        obj.set_date_check_sent(daiDateField_checkSentDate.getText());

		BUSINESS_OBJ = obj;
	}

    void ID_TEXT_FIELD_focusLost(FocusEvent e) {
        if (e.isTemporary()) return;

		String id = ID_TEXT_FIELD.getText();
		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
	    	CONTAINER_FRAME.callBackInsertNewId(id);
    		//Disable the Trans ID text field.
	   		ID_TEXT_FIELD.setDisabled(true);
		}
    }

    private void daiLabel_vendorId_daiAction(daiActionEvent e)
    {
        vendorObj tempObj = new vendorObj();
        String id = daiTextField_vendorId.getText();
        String name = daiTextField_vendorName.getText();

        DBAttributes attribs1 = new DBAttributes(vendorObj.ID, id, "Vendor ID", 100);
        DBAttributes attribs2 = new DBAttributes(vendorObj.NAME, name, "Vendor Name", 200);
        DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
                                              tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
        chooser.setVisible(true);
        vendorObj chosenObj = (vendorObj)chooser.getChosenObj();
        chooser.dispose();
        copyVendorAttribs(chosenObj);
    }

    private void daiTextField_vendorId_daiActionEvent(daiActionEvent e)
    {
        copyVendorAttribs((vendorObj)e.getSource());
    }

    private void copyVendorAttribs(vendorObj obj) {
        boolean isObjNull = false;
        String id = daiTextField_vendorId.getText();
        if (obj == null) isObjNull = true;
        if (isObjNull && (id != null && id.trim().length() > 0))
        {
            //Do nothing
        } else
        {
            daiTextField_vendorId.setText(isObjNull ? null : obj.get_id());
            daiTextField_vendorName.setText(isObjNull ? null : obj.get_name());
            payment_voucherObj payVouchObj = (payment_voucherObj)BUSINESS_OBJ;
            String rt_sameas_st = (isObjNull ? null : obj.get_is_remitto_sameas_shipto());
            if (rt_sameas_st != null && rt_sameas_st.equals("Y"))
            {
                payVouchObj.set_vendor_addr1(isObjNull ? null : obj.get_shipto_addr1());
                payVouchObj.set_vendor_addr2(isObjNull ? null : obj.get_shipto_addr2());
                payVouchObj.set_vendor_addr3(isObjNull ? null : obj.get_shipto_addr3());
                payVouchObj.set_vendor_addr4(isObjNull ? null : obj.get_shipto_addr4());
                payVouchObj.set_vendor_city(isObjNull ? null : obj.get_shipto_city());
                payVouchObj.set_vendor_state_code(isObjNull ? null : obj.get_shipto_state_code());
                payVouchObj.set_vendor_zip(isObjNull ? null : obj.get_shipto_zip());
                payVouchObj.set_vendor_country_code(isObjNull ? null : obj.get_shipto_country_code());
                payVouchObj.set_vendor_country_name(isObjNull ? null : obj.get_shipto_country_name());
            } else
            {
                payVouchObj.set_vendor_addr1(isObjNull ? null : obj.get_remit_addr1());
                payVouchObj.set_vendor_addr2(isObjNull ? null : obj.get_remit_addr2());
                payVouchObj.set_vendor_addr3(isObjNull ? null : obj.get_remit_addr3());
                payVouchObj.set_vendor_addr4(isObjNull ? null : obj.get_remit_addr4());
                payVouchObj.set_vendor_city(isObjNull ? null : obj.get_remit_city());
                payVouchObj.set_vendor_state_code(isObjNull ? null : obj.get_remit_state_code());
                payVouchObj.set_vendor_zip(isObjNull ? null : obj.get_remit_zip());
                payVouchObj.set_vendor_country_code(isObjNull ? null : obj.get_remit_country_code());
                payVouchObj.set_vendor_country_name(isObjNull ? null : obj.get_remit_country_name());
            }
            payVouchObj.set_our_acct_no_with_vendor(isObjNull ? null : obj.get_our_acct_no_with_vendor());

            daiHeaderSubPanel vendorAddrPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(1);
            BUSINESS_OBJ = payVouchObj;
            vendorAddrPanel.update_UI(BUSINESS_OBJ);;
        }
    }
}

