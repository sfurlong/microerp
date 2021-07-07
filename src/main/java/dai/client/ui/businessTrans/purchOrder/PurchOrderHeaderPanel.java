
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;


import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;

import com.borland.jbcl.control.BevelPanel;
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
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.carrierObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.locationObj;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.businessObjs.user_profileObj;
import dai.shared.businessObjs.vendorObj;
import dai.shared.businessObjs.vendor_contactObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCurrencyField;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiPayTermsComboBox;
import daiBeans.daiQueryTextField;
import daiBeans.daiRadioButton;
import daiBeans.daiTextField;


public class PurchOrderHeaderPanel extends daiHeaderPanel
{
	//Panel Controls.
	BevelPanel bevelPanel_totals = new BevelPanel();
	BevelPanel bevelPanel2 = new BevelPanel();

	//Layout Controls
	XYLayout xYLayout1 = new XYLayout();

	//Field Controls
	daiCurrencyField fieldControl_totalValue = new daiCurrencyField();
	daiCurrencyField fieldControl_tax = new daiCurrencyField();
	daiCurrencyField fieldControl_shipping = new daiCurrencyField();
	daiCurrencyField fieldControl_discount = new daiCurrencyField();
	daiCurrencyField fieldControl_subtotal = new daiCurrencyField();
	daiDateField fieldControl_paymentDueDate = new daiBeans.daiDateField();
	daiDateField fieldControl_dateShipped = new daiBeans.daiDateField();
	daiDateField fieldControl_datePromised = new daiBeans.daiDateField();
	daiDateField fieldControl_dateNeeded = new daiBeans.daiDateField();
	daiPayTermsComboBox  fieldControl_paymentTerms = new daiPayTermsComboBox();
	daiTextField fieldControl_airBillNum = new daiBeans.daiTextField();
    daiQueryTextField fieldControl_vendorId = new daiQueryTextField(new vendorObj());
    daiTextField fieldControl_vendorName = new daiTextField();
    daiTextField fieldControl_defaultShipId = new daiTextField();
    daiTextField fieldControl_defaultShipName = new daiTextField();
    ButtonGroup buttonGroup_shipLoc = new ButtonGroup();
    daiRadioButton radioButton_warehouseShip = new daiRadioButton("Ship To Warehouse");
    daiRadioButton radioButton_dropShip = new daiRadioButton("Drop Ship To Customer");

	//Label Controls
	daiLabel daiLabel_totalValue = new daiLabel();
	daiLabel daiLabel_tax = new daiLabel();
	daiLabel daiLabel_shipping = new daiLabel();
	daiLabel daiLabel_discount = new daiLabel();
	daiLabel daiLabel_subtotal = new daiLabel();
	daiLabel daiLabel_datePromised = new daiLabel();
	daiLabel daiLabel_paymentDueDate = new daiLabel("Payment Due Date:");
	daiLabel daiLabel_dateShipped = new daiLabel();
	daiLabel daiLabel_dateNeeded = new daiLabel();
	daiLabel daiLabel_airBillNum = new daiLabel();
	daiLabel daiLabel_paymentTerms = new daiLabel();
	daiLabel daiLabel_id = new daiLabel("PO Id:");
    daiLabel daiLabel_vendorId = new daiLabel("Vendor Id:");
    daiLabel daiLabel_shipToId = new daiLabel("My Ship Loc Id:");

	//Misc Controls

	//Customer Classes
	String TRANS_ID;
	String TRANS_TYPE;
	BevelPanel bevelPanel3 = new BevelPanel();
	SessionMetaData sessionMeta;
	XYLayout xYLayout2 = new XYLayout();
	XYLayout xYLayout3 = new XYLayout();
	daiBeans.daiUserIdDateCreatedPanel daiUserIdDateCreatedPanel = new daiBeans.daiUserIdDateCreatedPanel();
    daiLabel daiLabel_carrierId = new daiLabel();
    daiTextField daiTextField_carrierId = new daiTextField();
    daiTextField daiTextField_carrierName = new daiTextField();
    GroupBox groupBox_vendorData = new GroupBox();
    XYLayout xYLayout4 = new XYLayout();
    GroupBox groupBox_shipTo = new GroupBox();
    XYLayout xYLayout5 = new XYLayout();
    GroupBox groupBox_termsDates = new GroupBox();
    XYLayout xYLayout6 = new XYLayout();
    GroupBox groupBox_totals = new GroupBox();
    XYLayout xYLayout7 = new XYLayout();
    daiQueryTextField daiTextField_custId = new daiQueryTextField(new customerObj());
    daiTextField daiTextField_custName = new daiTextField();
    daiLabel daiLabel_custId = new daiLabel();

	//Constructor.
	public PurchOrderHeaderPanel(JFrame container, daiFrame parentFrame, purch_orderObj obj) {

		super(container, parentFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			LOGGER.logError(CONTAINER, "Could not initialize headerPanel.\n" + ex);
		}
	}

	//Initialize Controls
	void jbInit() throws Exception {

        sessionMeta = SessionMetaData.getInstance();

		fieldControl_tax.addFocusListener(new java.awt.event.FocusAdapter(){
										  public void focusLost(FocusEvent e){
										  fieldControl_tax_focusLost(e);}});
		fieldControl_shipping.addFocusListener(new java.awt.event.FocusAdapter(){
											   public void focusLost(FocusEvent e){
											   fieldControl_shipping_focusLost(e);}});
		fieldControl_subtotal.addFocusListener(new java.awt.event.FocusAdapter(){
											   public void focusLost(FocusEvent e){
											   fieldControl_subtotal_focusLost(e);}});
		fieldControl_discount.addFocusListener(new java.awt.event.FocusAdapter(){
											   public void focusLost(FocusEvent e){
											   fieldControl_discount_focusLost(e);}});

		daiLabel_totalValue.setText("Total Value:");
		daiLabel_tax.setText("Tax:");
		daiLabel_shipping.setText("Shipping:");
		daiLabel_subtotal.setText("Subtotal:");
		daiLabel_discount.setText("Discount:");
		daiLabel_datePromised.setText("Date Promised:");
		daiLabel_paymentTerms.setText("Payment Terms:");
		daiLabel_datePromised.setText("Est Ship Date:");
		daiLabel_dateNeeded.setText("Date Needed:");

        fieldControl_vendorName.setDisabled(true);
        fieldControl_defaultShipName.setDisabled(true);
        fieldControl_subtotal.setDisabled(true);
        fieldControl_totalValue.setDisabled(true);
        daiTextField_carrierName.setDisabled(true);

		xYLayout1.setHeight(401);
		xYLayout1.setWidth(500);
        daiLabel_vendorId.setHREFstyle(true);
        daiLabel_vendorId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_vendorId_daiAction(e);
            }
        });
        fieldControl_vendorId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                fieldControl_vendorId_daiActionEvent(e);
            }
        });

		daiLabel_shipToId.setHREFstyle(true);
        daiLabel_shipToId.setText("Ship To Location:");
        daiLabel_shipToId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_shipToId_daiAction(e);
            }
        });
        daiLabel_carrierId.setHREFstyle(true);
        daiLabel_carrierId.setText("Carrier Id:");
        daiLabel_carrierId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_carrierId_daiAction(e);
            }
        });
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(FocusEvent e) {
                ID_TEXT_FIELD_focusLost(e);
            }
        });
        daiLabel_custId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_custId_daiAction(e);
            }
        });
        daiTextField_custId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiTextField_custId_daiActionEvent(e);
            }
        });

        groupBox_vendorData.setLabel("Vendor");
        groupBox_vendorData.setLayout(xYLayout4);
        groupBox_shipTo.setLabel("Ship To");
        groupBox_shipTo.setLayout(xYLayout5);
        groupBox_termsDates.setLabel("Pay Terms/Dates");
        groupBox_termsDates.setLayout(xYLayout6);
        groupBox_totals.setLabel("Totals");
        groupBox_totals.setLayout(xYLayout7);
        daiTextField_custId.setText("");
        daiTextField_custName.setText("");
        daiLabel_custId.setHREFstyle(true);
        daiLabel_custId.setText("Customer:");
        buttonGroup_shipLoc.add(radioButton_warehouseShip);
        buttonGroup_shipLoc.add(radioButton_dropShip);
        radioButton_warehouseShip.setSelected(true);

        groupBox_vendorData.add(daiLabel_vendorId, new XYConstraints(-5, 5, -1, -1));
        groupBox_vendorData.add(daiLabel_carrierId, new XYConstraints(-2, 31, -1, -1));
        groupBox_vendorData.add(fieldControl_vendorId, new XYConstraints(51, 4, 119, -1));
        groupBox_vendorData.add(fieldControl_vendorName, new XYConstraints(175, 4, 259, -1));
        groupBox_vendorData.add(daiTextField_carrierId, new XYConstraints(51, 30, 119, -1));
        groupBox_vendorData.add(daiTextField_carrierName, new XYConstraints(175, 30, 259, -1));

        groupBox_shipTo.add(radioButton_warehouseShip, new XYConstraints(-5, 0, -1, -1));
        groupBox_shipTo.add(radioButton_dropShip, new XYConstraints(117, 0, -1, -1));
        groupBox_shipTo.add(daiLabel_shipToId, new XYConstraints(-7, 30, -1, -1));
        groupBox_shipTo.add(fieldControl_defaultShipId, new XYConstraints(81, 28, 114, -1));
        groupBox_shipTo.add(fieldControl_defaultShipName, new XYConstraints(198, 28, 241, -1));
        groupBox_shipTo.add(daiTextField_custId, new XYConstraints(81, 52, 114, -1));
        groupBox_shipTo.add(daiTextField_custName, new XYConstraints(198, 52, 241, -1));
        groupBox_shipTo.add(daiLabel_custId, new XYConstraints(27, 54, -1, -1));

        groupBox_termsDates.add(daiLabel_datePromised, new XYConstraints(6, 47, 81, -1));
        groupBox_termsDates.add(daiLabel_paymentDueDate, new XYConstraints(-11, 24, 98, -1));
        groupBox_termsDates.add(daiLabel_paymentTerms, new XYConstraints(6, 0, 81, -1));
        groupBox_termsDates.add(daiLabel_dateNeeded, new XYConstraints(6, 71, 81, -1));
        groupBox_termsDates.add(fieldControl_paymentTerms, new XYConstraints(91, 0, 107, -1));
        groupBox_termsDates.add(fieldControl_paymentDueDate, new XYConstraints(91, 24, -1, -1));
        groupBox_termsDates.add(fieldControl_datePromised, new XYConstraints(91, 48, -1, -1));
        groupBox_termsDates.add(fieldControl_dateNeeded, new XYConstraints(91, 71, -1, -1));

        groupBox_totals.add(daiLabel_totalValue, new XYConstraints(-1, 92, -1, -1));
        groupBox_totals.add(daiLabel_subtotal, new XYConstraints(14, 4, -1, -1));
        groupBox_totals.add(daiLabel_tax, new XYConstraints(35, 26, -1, -1));
        groupBox_totals.add(daiLabel_shipping, new XYConstraints(12, 47, -1, -1));
        groupBox_totals.add(daiLabel_discount, new XYConstraints(11, 71, -1, -1));
        groupBox_totals.add(fieldControl_subtotal, new XYConstraints(63, 0, -1, -1));
        groupBox_totals.add(fieldControl_tax, new XYConstraints(63, 24, -1, -1));
        groupBox_totals.add(fieldControl_shipping, new XYConstraints(63, 48, -1, -1));
        groupBox_totals.add(fieldControl_discount, new XYConstraints(63, 71, -1, -1));
        groupBox_totals.add(fieldControl_totalValue, new XYConstraints(63, 95, -1, -1));


        //Decorate the Panel
		this.setLayout(xYLayout1);
		this.setPreferredSize(new Dimension(591, 400));
		this.setMinimumSize(new Dimension(591, 400));
		this.setBackground(daiColors.PanelColor);
        this.add(daiLabel_id, new XYConstraints(4, 16, 52, -1));
        this.add(daiUserIdDateCreatedPanel, new XYConstraints(362, 0, 155, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(60, 14, -1, -1));
        this.add(groupBox_vendorData, new XYConstraints(19, 47, 473, 84));
        this.add(groupBox_shipTo, new XYConstraints(19, 131, -1, 107));
        this.add(groupBox_termsDates, new XYConstraints(32, 242, 230, -1));
        this.add(groupBox_totals, new XYConstraints(269, 241, 199, 145));

        setFieldsDisabled(true);
	}

    public void resetTabEntrySeq() {
        if (!ID_TEXT_FIELD.isDisabled()) {
            ID_TEXT_FIELD.requestFocus();
        } else {
            fieldControl_vendorId.requestFocus();
        }
    }

	//Override base class method so we can do some extra logic afterword.
	public int refresh()
	{
		//Call the base class method first.
		super.refresh();

		//Enable the Trans ID text field.
		ID_TEXT_FIELD.setText(null);
		ID_TEXT_FIELD.setDisabled(false);
        setFieldsDisabled(true);
        CONTAINER_FRAME.setTabsEnabled(false);

		return 0;
	}

    public String getTransId() {
        return ID_TEXT_FIELD.getText();
    }

	protected BusinessObject getNewBusinessObjInstance()
	{

		purch_orderObj obj = new purch_orderObj();
		purch_orderObj tempObj = (purch_orderObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.getObjLocality());
		return obj;
	}

	//Override base class query because we want to do some extra work.
	public int query(String id)
	{
		//Call the base class query then do our extended logic.
		super.query(id);

		//Set and Disable the Trans ID text field.
		ID_TEXT_FIELD.setText(id);
		ID_TEXT_FIELD.setDisabled(true);

        setFieldsDisabled(false);
        CONTAINER_FRAME.setTabsEnabled(true);
		return 0;
	}

	//Override base class query because we want to do some extra work.
	public int persistPanelData()
    {
  		//Call the base class insert then do our extended logic.
		int ret = super.persistPanelData();
        //The extended logic.
        ID_TEXT_FIELD.setDisabled(true);

        setFieldsDisabled(false);
        CONTAINER_FRAME.setTabsEnabled(true);
        return ret;
    }

	protected void update_UI(BusinessObject bobj)
	{
		purch_orderObj obj = (purch_orderObj)bobj;

        ID_TEXT_FIELD.setText(obj.get_id());
		daiUserIdDateCreatedPanel.setUserId(obj.get_created_by());
		daiUserIdDateCreatedPanel.setDateCreated(obj.get_date_created());
		fieldControl_discount.setText(obj.get_total_discount());
		fieldControl_shipping.setText(obj.get_total_shipping());
		fieldControl_subtotal.setText(obj.get_subtotal());
		fieldControl_tax.setText(obj.get_total_tax());
		fieldControl_totalValue.setText(obj.get_total_value());
		fieldControl_airBillNum.setText(obj.get_air_bill_num());
		fieldControl_dateNeeded.setText(obj.get_date_needed());
		fieldControl_datePromised.setText(obj.get_date_promised());
		fieldControl_paymentTerms.setSelectedItem(obj.get_payment_terms());
        fieldControl_vendorId.setText(obj.get_vendor_id());
        fieldControl_vendorName.setText(obj.get_vendor_name());
        fieldControl_defaultShipId.setText(obj.get_shipto_id());
        fieldControl_defaultShipName.setText(obj.get_shipto_name());
        daiTextField_carrierId.setText(obj.get_carrier_id());
        daiTextField_carrierName.setText(obj.get_carrier_name());
        daiTextField_custId.setText(obj.get_cust_id());
        daiTextField_custName.setText(obj.get_cust_name());
        fieldControl_paymentDueDate.setText(obj.get_payment_due_date());

        if (obj.get_is_direct_cust_ship() == null || obj.get_is_direct_cust_ship().equals("N")) {
            radioButton_warehouseShip.setSelected(true);
        } else {
            radioButton_dropShip.setSelected(true);
        }

		BUSINESS_OBJ = obj;
	}

	public void update_BusinessObj()
	{
		purch_orderObj obj = (purch_orderObj)BUSINESS_OBJ;

		obj.set_created_by(daiUserIdDateCreatedPanel.getUserId());
		obj.set_total_discount(fieldControl_discount.getText());
		obj.set_total_shipping(fieldControl_shipping.getText());
		obj.set_subtotal(fieldControl_subtotal.getText());
		obj.set_total_tax(fieldControl_tax.getText());
		obj.set_total_value(fieldControl_totalValue.getText());
		obj.set_air_bill_num(fieldControl_airBillNum.getText());
		obj.set_date_needed(fieldControl_dateNeeded.getText());
		obj.set_date_promised(fieldControl_datePromised.getText());
		obj.set_payment_terms((String)fieldControl_paymentTerms.getSelectedItem());
        obj.set_vendor_id(fieldControl_vendorId.getText());
        obj.set_vendor_name(fieldControl_vendorName.getText());
        obj.set_shipto_id(fieldControl_defaultShipId.getText());
        obj.set_shipto_name(fieldControl_defaultShipName.getText());
        obj.set_carrier_id(daiTextField_carrierId.getText());
        obj.set_carrier_name(daiTextField_carrierName.getText());
        obj.set_cust_id(daiTextField_custId.getText());
        obj.set_cust_name(daiTextField_custName.getText());
        obj.set_payment_due_date(fieldControl_paymentDueDate.getText());

        if (radioButton_warehouseShip.isSelected()) {
            obj.set_is_direct_cust_ship("N");
        } else {
            obj.set_is_direct_cust_ship("Y");
        }

        //This is always set to type PURCHASE because to differenciate
        //between entering a PO and EXPENSE type of purchase.
        obj.set_purch_order_type(obj.PURCH_ORDER_TYPE_PURCHASE);

		BUSINESS_OBJ = obj;
	}

	void refreshTotalValue()
	{
		Float discount = new Float(0);
		Float shipping = new Float(0);
		Float subtotal = new Float(0);
		Float tax      = new Float(0);
		if (fieldControl_discount.getText() != null )
		{
			discount = new Float(fieldControl_discount.getText());
		}
		if (fieldControl_shipping.getText() != null)
		{
			shipping = new Float(fieldControl_shipping.getText());
		}
		if (fieldControl_subtotal.getText() != null)
		{
			subtotal = new Float(fieldControl_subtotal.getText());
		}
		if (fieldControl_tax.getText() != null)
		{
			tax = new Float(fieldControl_tax.getText());
		}
		float total = (subtotal.floatValue() - discount.floatValue()) +
					  tax.floatValue() + shipping.floatValue();

		fieldControl_totalValue.setText(Float.toString(total));
	}

	void fieldControl_subtotal_focusLost(FocusEvent e)
	{
		//Refresh Total Value.
		refreshTotalValue();
	}

	void fieldControl_tax_focusLost(FocusEvent e)
	{
		//Refresh Total Value.
		refreshTotalValue();
	}

	void fieldControl_shipping_focusLost(FocusEvent e)
	{
		//Refresh Total Value.
		refreshTotalValue();
	}

	void fieldControl_discount_focusLost(FocusEvent e)
	{
		//Refresh Total Value.
		refreshTotalValue();
	}

	public void setTransSubtotal(Float subtotal)
	{
		fieldControl_subtotal.setText(subtotal.toString());
		refreshTotalValue();
	}

    public String getVendorId() {
        return fieldControl_vendorId.getText();
    }

    public String getCustId() {
        return daiTextField_custId.getText();
    }

    public String getCarrierId() {
        return daiTextField_carrierId.getText();
    }

    void ID_TEXT_FIELD_focusLost(FocusEvent e) {
        if (e.isTemporary()) return;

		String id = ID_TEXT_FIELD.getText();
		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
			//Disable the Trans ID text field.
			ID_TEXT_FIELD.setDisabled(true);

            setFieldsDisabled(false);
            CONTAINER_FRAME.setTabsEnabled(true);

            //If ret is zero, then this is a new record.  Values
            //greater than zero indicate that the record already existed
            if (CONTAINER_FRAME.callBackInsertNewId(id) == 0) {
                populateShiptoBillto();


                //set payment terms to Net 30 by default
                fieldControl_paymentTerms.setText("Net 30");
            }
		}
}

    void daiLabel_vendorId_daiAction(daiActionEvent e)
    {
		vendorObj tempObj = new vendorObj();
        String id = fieldControl_vendorId.getText();
        String name = fieldControl_vendorName.getText();

        DBAttributes attrib1 = new DBAttributes(vendorObj.ID, id, "Id", 100);
        DBAttributes attrib2 = new DBAttributes(vendorObj.NAME, name, "Vendor Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, " order by NAME ");
        chooser.show();
        vendorObj chosenObj = (vendorObj)chooser.getChosenObj();
        chooser.dispose();
        copyVendorAttribs(chosenObj);
    }

    private void fieldControl_vendorId_daiActionEvent(daiActionEvent e)
    {
        copyVendorAttribs((vendorObj)e.getSource());
    }

    private void copyVendorAttribs(vendorObj obj) {
        boolean isObjNull = false;
        String id = fieldControl_vendorId.getText();
        if (obj == null) isObjNull = true;
        if (isObjNull && id != null)
        {
            //Do nothing
        } else
        {
            fieldControl_vendorId.setText(isObjNull ? null : obj.get_id());
            fieldControl_vendorName.setText(isObjNull ? null : obj.get_name());
            purch_orderObj purchOrdObj = (purch_orderObj)BUSINESS_OBJ;
    		purchOrdObj.set_vendor_addr1(isObjNull ? null : obj.get_shipto_addr1());
    		purchOrdObj.set_vendor_addr2(isObjNull ? null : obj.get_shipto_addr2());
    		purchOrdObj.set_vendor_addr3(isObjNull ? null : obj.get_shipto_addr3());
    		purchOrdObj.set_vendor_addr4(isObjNull ? null : obj.get_shipto_addr4());
    		purchOrdObj.set_vendor_city(isObjNull ? null : obj.get_shipto_city());
    		purchOrdObj.set_vendor_state_code(isObjNull ? null : obj.get_shipto_state_code());
    		purchOrdObj.set_vendor_zip(isObjNull ? null : obj.get_shipto_zip());
	    	purchOrdObj.set_vendor_country_code(isObjNull ? null : obj.get_shipto_country_code());
    		purchOrdObj.set_vendor_country_name(isObjNull ? null : obj.get_shipto_country_name());
            //Populate name and fax
            getVendorContact(purchOrdObj, obj.get_id());
            //purchOrdObj.set_vendor_contact(getVendorContact(obj.get_id()));
            daiHeaderSubPanel vendorPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(2);
            BUSINESS_OBJ = purchOrdObj;
            vendorPanel.update_UI(BUSINESS_OBJ);;
        }
    }

    private String getVendorContact(purch_orderObj poObj, String vendorId) {
        String ret = null;
        String sqlStmt = " select " + vendor_contactObj.NAME + "," +
                        vendor_contactObj.FAX1 + ", " +
                        vendor_contactObj.IS_PRIMARY +
                        " from " + vendor_contactObj.TABLE_NAME + " where " +
                        " id = '" + vendorId + "' and locality = '" +
                        vendor_contactObj.getObjLocality() + "'" +
                        " order by detail_id ";
        try {
            DBRecSet attribSet = _dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
                                                                    sqlStmt);
            for (int i=0; i<attribSet.getSize(); i++) {
                DBRec attribs = attribSet.getRec(i);
                String name = attribs.getAttribVal(vendor_contactObj.NAME);
                String fax = attribs.getAttribVal(vendor_contactObj.FAX1);
                if (name == null) name = "";
                if (fax == null) fax = "";
                String isPrime = attribs.getAttribVal(vendor_contactObj.IS_PRIMARY);
                //Default to the first contact, if non are marked as primary.
                if (i == 0) {
                    poObj.set_vendor_contact(name);
                    poObj.set_vendor_fax(fax);
                    ret = name;
                }
                if (isPrime != null && isPrime.equals("Y"))
                {
                    poObj.set_vendor_contact(name);
                    poObj.set_vendor_fax(fax);
                    ret = name;
                }
            }
        } catch (Exception e) {
            LOGGER.logError(CONTAINER, "Could not get Vendor Contact Info.\n" +
                                            e.getLocalizedMessage());
        }
        return ret;
    }

    private String getBillToContact() {
        String ret = null;
        String sqlStmt = " select " + user_profileObj.USER_NAME +
                        " from " + user_profileObj.TABLE_NAME + " where " +
                        " id = '" + sessionMeta.getUserId() + "'";

        try {
            DBRecSet attribSet = _dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
                                                                    sqlStmt);
            if (attribSet.getSize() > 0) {
                ret = attribSet.getRec(0).getAttribVal(user_profileObj.USER_NAME);
            }
        } catch (Exception e) {
            LOGGER.logError(CONTAINER, "Could not get Bill To Contact Info.\n" +
                                            e.getLocalizedMessage());
        }
        return ret;
    }

    void daiLabel_shipToId_daiAction(daiActionEvent e) {
        if (radioButton_warehouseShip.isSelected()) {
            populate_warehouseShipTo();
        } else {
            populate_custShipTo();
        }
    }

    private void populate_warehouseShipTo() {
		locationObj tempObj = new locationObj();
        String id = fieldControl_defaultShipId.getText();

        DBAttributes attribs1 = new DBAttributes(tempObj.ID, id, "Location Id", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
											  new DBAttributes[]{attribs1},
                                              null, null);
		chooser.show();
        locationObj chosenObj = (locationObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_defaultShipId.setText(chosenObj.get_id());
            fieldControl_defaultShipName.setText(chosenObj.get_description());
            purch_orderObj purchOrdObj = (purch_orderObj)BUSINESS_OBJ;
    		purchOrdObj.set_shipto_addr1(chosenObj.get_shipto_addr1());
    		purchOrdObj.set_shipto_addr2(chosenObj.get_shipto_addr2());
    		purchOrdObj.set_shipto_addr3(chosenObj.get_shipto_addr3());
    		purchOrdObj.set_shipto_addr4(chosenObj.get_shipto_addr4());
    		purchOrdObj.set_shipto_city(chosenObj.get_shipto_city());
    		purchOrdObj.set_shipto_state_code(chosenObj.get_shipto_state_code());
    		purchOrdObj.set_shipto_zip(chosenObj.get_shipto_zip());
	    	purchOrdObj.set_shipto_country_code(chosenObj.get_shipto_country_code());
    		purchOrdObj.set_shipto_country_name(chosenObj.get_shipto_country_name());
            daiHeaderSubPanel shiptoPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(3);
            BUSINESS_OBJ = purchOrdObj;
            shiptoPanel.update_UI(BUSINESS_OBJ);;
        }
    	chooser.dispose();
    }

    private void populate_custShipTo() {
		customerObj tempObj = new customerObj();
        String id = fieldControl_defaultShipId.getText();
        String name = fieldControl_defaultShipName.getText();

        DBAttributes attribs1 = new DBAttributes(customerObj.ID, id, "Cust Id", 100);
        DBAttributes attribs2 = new DBAttributes(customerObj.NAME, name, "Customer Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
		chooser.show();
        customerObj chosenObj = (customerObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_defaultShipId.setText(chosenObj.get_id());
            fieldControl_defaultShipName.setText(chosenObj.get_name());
            purch_orderObj purchOrdObj = (purch_orderObj)BUSINESS_OBJ;
    		purchOrdObj.set_shipto_addr1(chosenObj.get_shipto_addr1());
    		purchOrdObj.set_shipto_addr2(chosenObj.get_shipto_addr2());
    		purchOrdObj.set_shipto_addr3(chosenObj.get_shipto_addr3());
    		purchOrdObj.set_shipto_addr4(chosenObj.get_shipto_addr4());
    		purchOrdObj.set_shipto_city(chosenObj.get_shipto_city());
    		purchOrdObj.set_shipto_state_code(chosenObj.get_shipto_state_code());
    		purchOrdObj.set_shipto_zip(chosenObj.get_shipto_zip());
	    	purchOrdObj.set_shipto_country_code(chosenObj.get_shipto_country_code());
    		purchOrdObj.set_shipto_country_name(chosenObj.get_shipto_country_name());
            daiHeaderSubPanel shiptoPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(3);
            BUSINESS_OBJ = purchOrdObj;
            shiptoPanel.update_UI(BUSINESS_OBJ);;
        }
    	chooser.dispose();
    }

    private void populateShiptoBillto() {
        //Insert the bill to info.
        //NOTE:  locality table does not use a locality field. (it does exist
        //in the table though, just for compatibility)
        String exp = locationObj.IS_PRIMARY_LOC + "= 'Y'";

        try {
            Vector vect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                                                        new locationObj(),
                                                        exp);

            if (vect.size() > 0) {
                locationObj locObj = (locationObj)vect.firstElement();

                purch_orderObj purchOrdObj = (purch_orderObj)BUSINESS_OBJ;
        		purchOrdObj.set_billto_addr1(locObj.get_billto_addr1());
        		purchOrdObj.set_billto_addr2(locObj.get_billto_addr2());
    	    	purchOrdObj.set_billto_addr3(locObj.get_billto_addr3());
    		    purchOrdObj.set_billto_addr4(locObj.get_billto_addr4());
        		purchOrdObj.set_billto_city(locObj.get_billto_city());
        		purchOrdObj.set_billto_state_code(locObj.get_billto_state_code());
    	    	purchOrdObj.set_billto_zip(locObj.get_billto_zip());
	    	    purchOrdObj.set_billto_country_code(locObj.get_billto_country_code());
        		purchOrdObj.set_billto_country_name(locObj.get_billto_country_name());
                purchOrdObj.set_our_contact(getBillToContact());

                fieldControl_defaultShipId.setText(locObj.get_id());
                fieldControl_defaultShipName.setText(locObj.get_description());
        		purchOrdObj.set_shipto_addr1(locObj.get_shipto_addr1());
        		purchOrdObj.set_shipto_addr2(locObj.get_shipto_addr2());
    	    	purchOrdObj.set_shipto_addr3(locObj.get_shipto_addr3());
    		    purchOrdObj.set_shipto_addr4(locObj.get_shipto_addr4());
        		purchOrdObj.set_shipto_city(locObj.get_shipto_city());
        		purchOrdObj.set_shipto_state_code(locObj.get_shipto_state_code());
    	    	purchOrdObj.set_shipto_zip(locObj.get_shipto_zip());
	    	    purchOrdObj.set_shipto_country_code(locObj.get_shipto_country_code());
        		purchOrdObj.set_shipto_country_name(locObj.get_shipto_country_name());

                daiHeaderSubPanel billtoPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(4);
                daiHeaderSubPanel shiptoPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(3);
                BUSINESS_OBJ = purchOrdObj;
                billtoPanel.update_UI(BUSINESS_OBJ);;
                shiptoPanel.update_UI(BUSINESS_OBJ);;
            } else {
                LOGGER.logError(CONTAINER, "Could not find a Primary Corporate Location.\n" +
                                                "Please select a Primary Location in the Corporate Location\n" +
                                                "Window of the System Administration Module.");
            }
        } catch (Exception e) {
            LOGGER.logError(CONTAINER, "PurchOrderHeaderPanel::populateBillTo failure\n"+e.getLocalizedMessage());
        }
    }

    void daiLabel_carrierId_daiAction(daiActionEvent e) {
		carrierObj tempObj = new carrierObj();
        String id = daiTextField_carrierId.getText();
        String name = daiTextField_carrierName.getText();

        DBAttributes attrib1 = new DBAttributes(carrierObj.ID, id, "Id", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[] {attrib1},
											  null, " order by priority, id");

		chooser.show();
        carrierObj chosenObj = (carrierObj)chooser.getChosenObj();
        if (chosenObj != null) {
            daiTextField_carrierId.setText(chosenObj.get_id());
            daiTextField_carrierName.setText(chosenObj.get_name());
        }
    	chooser.dispose();
    }

    private void daiTextField_custId_daiActionEvent(daiActionEvent e)
    {
        copyCustAttribs((customerObj)e.getSource());
    }

    private void copyCustAttribs(customerObj obj) {
        boolean isObjNull = false;
        String id = daiTextField_custId.getText();
        if (obj == null) isObjNull = true;
        if (isObjNull && id != null)
        {
            //Do nothing
        } else
        {
            daiTextField_custId.setText(isObjNull ? null : obj.get_id());
            daiTextField_custName.setText(isObjNull ? null : obj.get_name());
        }
    }

    private void daiLabel_custId_daiAction(daiActionEvent e) {
		customerObj tempObj = new customerObj();
        String id = daiTextField_custId.getText();
        String name = daiTextField_custName.getText();

        DBAttributes attribs1 = new DBAttributes(customerObj.ID, id, "Cust Id", 100);
        DBAttributes attribs2 = new DBAttributes(customerObj.NAME, name, "Customer Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
        chooser.show();
        customerObj chosenObj = (customerObj)chooser.getChosenObj();
        chooser.dispose();
        copyCustAttribs(chosenObj);
    }

    private void setFieldsDisabled(boolean flag) {
    	fieldControl_tax.setDisabled(flag);
	    fieldControl_shipping.setDisabled(flag);
	    fieldControl_discount.setDisabled(flag);
	    fieldControl_paymentDueDate.setDisabled(flag);
	    fieldControl_dateShipped.setDisabled(flag);
	    fieldControl_datePromised.setDisabled(flag);
	    fieldControl_dateNeeded.setDisabled(flag);
	    fieldControl_paymentTerms.setDisabled(flag);;
	    fieldControl_airBillNum.setDisabled(flag);
        fieldControl_vendorId.setDisabled(flag);
        fieldControl_vendorName.setDisabled(flag);
        fieldControl_defaultShipId.setDisabled(flag);
        fieldControl_defaultShipName.setDisabled(flag);
        radioButton_warehouseShip.setDisabled(flag);
        radioButton_dropShip.setDisabled(flag);
        daiTextField_carrierId.setDisabled(flag);
        daiTextField_carrierName.setDisabled(flag);
        daiLabel_vendorId.setDisabled(flag);
        daiLabel_carrierId.setDisabled(flag);
        daiLabel_shipToId.setDisabled(flag);
        daiLabel_custId.setDisabled(flag);
        daiTextField_custId.setDisabled(flag);
        daiTextField_custName.setDisabled(flag);
    }
}
