
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.order;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.util.Calendar;

import javax.swing.JFrame;

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
import dai.shared.businessObjs.cust_orderObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.customer_contactObj;
import dai.shared.businessObjs.user_profileObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCurrencyField;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiPayTermsComboBox;
import daiBeans.daiQueryTextField;
import daiBeans.daiTextField;


public class OrderHeaderPanel extends daiHeaderPanel
{
    //Layout Controls
    XYLayout xYLayout1 = new XYLayout();

    //Field Controls
    daiCurrencyField fieldControl_totalValue = new daiCurrencyField();
    daiCurrencyField fieldControl_tax = new daiCurrencyField();
    daiCurrencyField fieldControl_shipping = new daiCurrencyField();
    daiCurrencyField fieldControl_discount = new daiCurrencyField();
    daiCurrencyField fieldControl_subtotal = new daiCurrencyField();
    daiDateField fieldControl_datePromised = new daiBeans.daiDateField();
    daiDateField fieldControl_dateNeeded = new daiBeans.daiDateField();
    daiDateField fieldControl_revisedDate = new daiDateField();
    daiPayTermsComboBox fieldControl_paymentTerms = new daiPayTermsComboBox();
    daiQueryTextField daiTextField_custId = new daiQueryTextField(new customerObj());
    daiTextField daiTextField_custName = new daiTextField();
    daiQueryTextField daiTextField_carrierId = new daiQueryTextField(new carrierObj());
    daiTextField daiTextField_carrierName = new daiTextField();
    daiTextField daiTextField_custPO = new daiTextField();
    daiDateField daiDateField_custPODate = new daiDateField();
    daiTextField daiTextField_fob = new daiTextField();

    //Label Controls
    daiLabel daiLabel_totalValue = new daiLabel();
    daiLabel daiLabel_tax = new daiLabel();
    daiLabel daiLabel_shipping = new daiLabel();
    daiLabel daiLabel_discount = new daiLabel();
    daiLabel daiLabel_subtotal = new daiLabel();
    daiLabel daiLabel_datePromised = new daiLabel();
    daiLabel daiLabel_dateNeeded = new daiLabel();
    daiLabel daiLabel_paymentTerms = new daiLabel();
    daiLabel daiLabel_custId = new daiLabel("Customer:");
    daiLabel daiLabel_carrierId = new daiLabel("Carrier:");
    daiLabel daiLabel_custPO = new daiLabel("Cust. PO:");
    daiLabel daiLabel_custPODate = new daiLabel("Cust. PO Date:");
    daiLabel daiLabel_fob = new daiLabel("FOB:");
    daiLabel daiLabel_revisedDate = new daiLabel("Revised Date:");

    //Misc Controls

    //Customer Classes
    String TRANS_ID;
    String TRANS_TYPE;
    SessionMetaData sessionMeta;
    daiLabel daiLabel_id = new daiLabel();
    daiBeans.daiUserIdDateCreatedPanel daiUserIdDateCreatedPanel = new daiBeans.daiUserIdDateCreatedPanel();
    GroupBox groupBox_custData = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();
    GroupBox groupBox_termsData = new GroupBox();
    GroupBox groupBox_totalsData = new GroupBox();
    XYLayout xYLayout4 = new XYLayout();
    GridLayout gridLayout1 = new GridLayout();
    daiCurrencyField daiCurrencyField_amtPrepaid = new daiCurrencyField();
    daiLabel daiLabel_prepaidAmt = new daiLabel();

    //Constructor.
    public OrderHeaderPanel(JFrame container, daiFrame parentFrame, cust_orderObj obj) {

        super(container, parentFrame, obj);

        try
        {
            jbInit();
        } catch (Exception ex)
        {
            LOGGER.logError(CONTAINER, "Could not initialize headerPanel.\n" + ex);
            ex.printStackTrace();
        }
    }

    //Initialize Controls
    void jbInit() throws Exception {

        sessionMeta = sessionMeta.getInstance();

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

        daiCurrencyField_amtPrepaid.setBackground(Color.lightGray);
        daiLabel_totalValue.setText("Total Value:");
        daiLabel_tax.setText("Tax:");
        daiLabel_shipping.setText("Shipping:");
        daiLabel_subtotal.setText("Subtotal:");
        daiLabel_discount.setText("Discount:");
        daiLabel_datePromised.setText("Date Promised:");
        daiLabel_paymentTerms.setText("Payment Terms:");
        daiLabel_datePromised.setText("Est Ship Date:");
        daiLabel_dateNeeded.setText("Date Needed:");
        fieldControl_subtotal.setDisabled(true);
        fieldControl_totalValue.setDisabled(true);
        xYLayout1.setHeight(339);
        xYLayout1.setWidth(591);
        daiLabel_id.setText("Order Id:");
        daiLabel_custId.setHREFstyle(true);
        daiLabel_custId.adddaiActionListener(new daiActionListener()
                                             {
                                                 public void daiActionEvent(daiActionEvent e) {
                                                     daiLabel_custId_daiActionEvent(e);
                                                 }
                                             });
        daiTextField_custId.adddaiActionListener(new daiActionListener() {
                                                     public void daiActionEvent(daiActionEvent e) {
                                                         daiTextField_custId_daiActionEvent(e);
                                                     }
                                                 });
        daiTextField_carrierId.adddaiActionListener(new daiActionListener() {
                                                        public void daiActionEvent(daiActionEvent e) {
                                                            daiTextField_carrierId_daiActionEvent(e);
                                                        }
                                                    });
        daiLabel_carrierId.setHREFstyle(true);
        daiLabel_carrierId.adddaiActionListener(new daiActionListener()
                                                {
                                                    public void daiActionEvent(daiActionEvent e) {
                                                        daiLabel_carrierId_daiActionEvent(e);
                                                    }
                                                });
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
                                             {

                                                 public void focusLost(FocusEvent e)
                                                 {
                                                     ID_TEXT_FIELD_focusLost(e);
                                                 }
                                             });
        groupBox_custData.setLayout(xYLayout2);
        groupBox_termsData.setLayout(gridLayout1);
        groupBox_totalsData.setLayout(xYLayout4);


        //Entry Controls
        groupBox_custData.setLabel("Customer");
        groupBox_termsData.setLabel("Pay Terms/ Dates");
        groupBox_totalsData.setLabel("Totals");
        daiTextField_fob.setMaximumSize(new Dimension(2147483647, 24));
        gridLayout1.setColumns(2);
        gridLayout1.setHgap(5);
        gridLayout1.setRows(5);
        gridLayout1.setVgap(5);
        daiCurrencyField_amtPrepaid.setText("");
        daiLabel_prepaidAmt.setText("Amt Prepaid:");
        groupBox_custData.add(daiLabel_custId, new XYConstraints(7, 5, -1, -1));
        groupBox_custData.add(daiTextField_custId, new XYConstraints(62, 3, -1, -1));
        groupBox_custData.add(daiTextField_custName, new XYConstraints(166, 3, 254, -1));
        groupBox_custData.add(daiLabel_custPO, new XYConstraints(11, 30, -1, -1));

        groupBox_custData.add(daiTextField_custPO, new XYConstraints(62, 29, 130, -1));
        groupBox_custData.add(daiLabel_custPODate, new XYConstraints(196, 31, -1, -1));
        groupBox_custData.add(daiDateField_custPODate, new XYConstraints(274, 29, -1, -1));

        groupBox_custData.add(daiLabel_carrierId, new XYConstraints(20, 59, -1, -1));
        groupBox_custData.add(daiTextField_carrierId, new XYConstraints(62, 57, -1, -1));
        groupBox_custData.add(daiTextField_carrierName, new XYConstraints(166, 57, 256, -1));
        groupBox_totalsData.add(daiLabel_subtotal, new XYConstraints(-4, 2, 68, -1));
        groupBox_totalsData.add(daiLabel_tax, new XYConstraints(-4, 25, 68, -1));
        groupBox_totalsData.add(daiLabel_shipping, new XYConstraints(-4, 47, 68, -1));
        groupBox_totalsData.add(daiLabel_discount, new XYConstraints(-4, 70, 68, -1));
        groupBox_totalsData.add(daiLabel_totalValue, new XYConstraints(-4, 92, 68, -1));
        groupBox_totalsData.add(daiLabel_prepaidAmt, new XYConstraints(-4, 114, 68, -1));
        groupBox_totalsData.add(fieldControl_subtotal, new XYConstraints(69, 2, -1, -1));
        groupBox_totalsData.add(fieldControl_tax, new XYConstraints(69, 24, -1, -1));
        groupBox_totalsData.add(fieldControl_shipping, new XYConstraints(69, 46, -1, -1));
        groupBox_totalsData.add(fieldControl_discount, new XYConstraints(69, 68, -1, -1));
        groupBox_totalsData.add(fieldControl_totalValue, new XYConstraints(69, 91, -1, -1));
        groupBox_totalsData.add(daiCurrencyField_amtPrepaid, new XYConstraints(69, 113, -1, -1));
        groupBox_termsData.add(daiLabel_paymentTerms, null);
        groupBox_termsData.add(fieldControl_paymentTerms, null);
        groupBox_termsData.add(daiLabel_fob, null);
        groupBox_termsData.add(daiTextField_fob, null);
        groupBox_termsData.add(daiLabel_datePromised, null);
        groupBox_termsData.add(fieldControl_datePromised, null);
        groupBox_termsData.add(daiLabel_dateNeeded, null);
        groupBox_termsData.add(fieldControl_dateNeeded, null);
        groupBox_termsData.add(daiLabel_revisedDate, null);
        groupBox_termsData.add(fieldControl_revisedDate, null);

        //Decorate the frame
        this.setLayout(xYLayout1);
        //this.setPreferredSize(new Dimension(591, 500));
        //this.setMinimumSize(new Dimension(591, 400));
        this.setBackground(daiColors.PanelColor);
        this.add(ID_TEXT_FIELD, new XYConstraints(100, 29, -1, -1));
        this.add(daiLabel_id, new XYConstraints(44, 31, 52, -1));
        this.add(groupBox_custData, new XYConstraints(31, 63, 466, 114));
        this.add(daiUserIdDateCreatedPanel, new XYConstraints(334, 0, 155, -1));
        this.add(groupBox_termsData, new XYConstraints(24, 179, -1, 150));
        this.add(groupBox_totalsData, new XYConstraints(266, 178, 210, 162));

        setFieldsDisabled(true);
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

    public void resetTabEntrySeq() {
        if (!ID_TEXT_FIELD.isDisabled()) {
            ID_TEXT_FIELD.requestFocus();
        } else {
            daiTextField_custId.requestFocus();
        }
    }

    public String getCustId() {
        return daiTextField_custId.getText();
    }

    public String getCarrierId() {
        return daiTextField_carrierId.getText();
    }

    public String getTransId() {
        return ID_TEXT_FIELD.getText();
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        cust_orderObj obj = new cust_orderObj();
        cust_orderObj tempObj = (cust_orderObj)BUSINESS_OBJ;

        //Set the Primary Keys for the new Item Object.
        obj.set_id(tempObj.get_id());
        obj.set_locality(tempObj.get_locality());

        return obj;
    }

    //Override base class method because we want to do some extra work.
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
        cust_orderObj obj = (cust_orderObj)bobj;

        ID_TEXT_FIELD.setText(obj.get_id());
        daiUserIdDateCreatedPanel.setUserId(obj.get_created_by());
        daiUserIdDateCreatedPanel.setDateCreated(obj.get_date_created());
        fieldControl_discount.setText(obj.get_total_discount());
        fieldControl_shipping.setText(obj.get_total_shipping());
        fieldControl_subtotal.setText(obj.get_subtotal());
        fieldControl_tax.setText(obj.get_total_tax());
        fieldControl_totalValue.setText(obj.get_total_value());
        fieldControl_dateNeeded.setText(obj.get_date_needed());
        fieldControl_revisedDate.setText(obj.get_revised_date_promise());
        fieldControl_datePromised.setText(obj.get_date_promised());
        fieldControl_paymentTerms.setSelectedItem(obj.get_payment_terms());
        daiTextField_custId.setText(obj.get_customer_id());
        daiTextField_custName.setText(obj.get_customer_name());
        daiTextField_carrierId.setText(obj.get_carrier_id());
        daiTextField_carrierName.setText(obj.get_carrier_name());
        daiTextField_custPO.setText(obj.get_po_num());
        daiDateField_custPODate.setText(obj.get_po_date());
        daiCurrencyField_amtPrepaid.setText(obj.get_total_cash_received());
        daiTextField_fob.setText(obj.get_fob());

        BUSINESS_OBJ = obj;
    }

    public void update_BusinessObj()
    {
        cust_orderObj obj = (cust_orderObj)BUSINESS_OBJ;

        obj.set_created_by(daiUserIdDateCreatedPanel.getUserId());
        obj.set_total_discount(fieldControl_discount.getText());
        obj.set_total_shipping(fieldControl_shipping.getText());
        obj.set_subtotal(fieldControl_subtotal.getText());
        obj.set_total_tax(fieldControl_tax.getText());
        obj.set_total_value(fieldControl_totalValue.getText());
        obj.set_date_needed(fieldControl_dateNeeded.getText());
        obj.set_date_promised(fieldControl_datePromised.getText());
        obj.set_revised_date_promise(fieldControl_revisedDate.getText());
        obj.set_payment_terms((String)fieldControl_paymentTerms.getSelectedItem());
        obj.set_customer_id(daiTextField_custId.getText());
        obj.set_customer_name(daiTextField_custName.getText());
        obj.set_carrier_id(daiTextField_carrierId.getText());
        obj.set_carrier_name(daiTextField_carrierName.getText());
        obj.set_po_num(daiTextField_custPO.getText());
        obj.set_po_date(daiDateField_custPODate.getText());
        obj.set_total_cash_received(daiCurrencyField_amtPrepaid.getText());
        obj.set_fob(daiTextField_fob.getText());
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

    //returns today's date
    public String getCurrentDate()
    {
        Calendar now = Calendar.getInstance();
        String currDate ="";

        currDate = now.get(now.MONTH)+1 + "/" +
						 now.get(now.DAY_OF_MONTH) + "/" +
						 now.get(now.YEAR);

        return currDate;
    }

    void ID_TEXT_FIELD_focusLost(FocusEvent e)
    {
        if (!e.isTemporary())
        {
            String id = ID_TEXT_FIELD.getText();
            int exists = 0;
            if (id != null && !ID_TEXT_FIELD.isDisabled())
            {
                id = id.toUpperCase();
                ID_TEXT_FIELD.setText(id);
                //Disable the Trans ID text field.
                ID_TEXT_FIELD.setDisabled(true);

                exists = CONTAINER_FRAME.callBackInsertNewId(id);

                setFieldsDisabled(false);
                CONTAINER_FRAME.setTabsEnabled(true);

                if (exists == 0) // the ID did not already exist
                {
                  //set custPODate to today's date by default
                  daiDateField_custPODate.setText(getCurrentDate());
                }
            }
        }
    }

    void daiLabel_custId_daiActionEvent(daiActionEvent e)
    {
        customerObj tempObj = new customerObj();
        String id = daiTextField_custId.getText();
        String name = daiTextField_custName.getText();

        DBAttributes attribs1 = new DBAttributes(customerObj.ID, id, "Cust ID", 100);
        DBAttributes attribs2 = new DBAttributes(customerObj.NAME, name, "Cust Name", 200);
        DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
                                              tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
        chooser.show();
        customerObj chosenObj = (customerObj)chooser.getChosenObj();
        chooser.dispose();
        copyCustAttribs(chosenObj);
    }

    private void daiTextField_custId_daiActionEvent(daiActionEvent e)
    {
        copyCustAttribs((customerObj)e.getSource());
    }

    private void copyCustAttribs(customerObj obj) {
        boolean isObjNull = false;
        String id = daiTextField_custId.getText();
        if (obj == null) isObjNull = true;
        if (isObjNull && (id != null && id.trim().length() > 0))
        {
            //Do nothing
        } else
        {
            daiTextField_custId.setText(isObjNull ? null : obj.get_id());
            daiTextField_custName.setText(isObjNull ? null : obj.get_name());
            cust_orderObj custOrdObj = (cust_orderObj)BUSINESS_OBJ;
            custOrdObj.set_customer_id(isObjNull ? null : obj.get_id());
            custOrdObj.set_shipto_attn(isObjNull ? null : obj.get_shipto_attn());
            custOrdObj.set_shipto_addr1(isObjNull ? null : obj.get_shipto_addr1());
            custOrdObj.set_shipto_addr2(isObjNull ? null : obj.get_shipto_addr2());
            custOrdObj.set_shipto_addr3(isObjNull ? null : obj.get_shipto_addr3());
            custOrdObj.set_shipto_addr4(isObjNull ? null : obj.get_shipto_addr4());
            custOrdObj.set_shipto_city(isObjNull ? null : obj.get_shipto_city());
            custOrdObj.set_shipto_state_code(isObjNull ? null : obj.get_shipto_state_code());
            custOrdObj.set_shipto_zip(isObjNull ? null : obj.get_shipto_zip());
            custOrdObj.set_shipto_country_code(isObjNull ? null : obj.get_shipto_country_code());
            custOrdObj.set_shipto_country_name(isObjNull ? null : obj.get_shipto_country_name());
            custOrdObj.set_our_contact(getOurContact());
            //Populate the cust contact info.
            getCustContact(obj.get_id(), custOrdObj);

            String bt_is_st = (isObjNull ? null : obj.get_billto_is_shipto());

            if (bt_is_st != null && bt_is_st.equals("Y"))
            {
                custOrdObj.set_billto_attn(isObjNull ? null : obj.get_billto_attn());
                custOrdObj.set_billto_addr1(isObjNull ? null : obj.get_shipto_addr1());
                custOrdObj.set_billto_addr2(isObjNull ? null : obj.get_shipto_addr2());
                custOrdObj.set_billto_addr3(isObjNull ? null : obj.get_shipto_addr3());
                custOrdObj.set_billto_addr4(isObjNull ? null : obj.get_shipto_addr4());
                custOrdObj.set_billto_city(isObjNull ? null : obj.get_shipto_city());
                custOrdObj.set_billto_state_code(isObjNull ? null : obj.get_shipto_state_code());
                custOrdObj.set_billto_zip(isObjNull ? null : obj.get_shipto_zip());
                custOrdObj.set_billto_country_code(isObjNull ? null : obj.get_shipto_country_code());
                custOrdObj.set_billto_country_name(isObjNull ? null : obj.get_shipto_country_name());
            } else
            {
                custOrdObj.set_billto_attn(isObjNull ? null : obj.get_billto_attn());
                custOrdObj.set_billto_addr1(isObjNull ? null : obj.get_billto_addr1());
                custOrdObj.set_billto_addr2(isObjNull ? null : obj.get_billto_addr2());
                custOrdObj.set_billto_addr3(isObjNull ? null : obj.get_billto_addr3());
                custOrdObj.set_billto_addr4(isObjNull ? null : obj.get_billto_addr4());
                custOrdObj.set_billto_city(isObjNull ? null : obj.get_billto_city());
                custOrdObj.set_billto_state_code(isObjNull ? null : obj.get_billto_state_code());
                custOrdObj.set_billto_zip(isObjNull ? null : obj.get_billto_zip());
                custOrdObj.set_billto_country_code(isObjNull ? null : obj.get_billto_country_code());
                custOrdObj.set_billto_country_name(isObjNull ? null : obj.get_billto_country_name());
            }
            daiHeaderSubPanel shiptoBilltoPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(2);
            BUSINESS_OBJ = custOrdObj;
            shiptoBilltoPanel.update_UI(BUSINESS_OBJ);;
        }
    }

    private String getCustContact(String custId, cust_orderObj custOrdObj) {
        String ret = null;
        String sqlStmt = " select " + customer_contactObj.NAME + "," +
                        customer_contactObj.FAX1 + ", " +
                        customer_contactObj.PHONE1 + ", " +
                        customer_contactObj.IS_PRIMARY +
                        " from " + customer_contactObj.TABLE_NAME + " where " +
                        " id = '" + custId + "' and locality = '" +
                        customer_contactObj.getObjLocality() + "'" +
                        " order by detail_id ";
        try {
            DBRecSet attribSet = _dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
                                                                    sqlStmt);
            for (int i=0; i<attribSet.getSize(); i++) {
                DBRec attribs = attribSet.getRec(i);
                String name = attribs.getAttribVal(customer_contactObj.NAME);
                if (name == null) name = "";
                String fax = attribs.getAttribVal(customer_contactObj.FAX1);
                if (fax == null) fax = "";
                String phone = attribs.getAttribVal(customer_contactObj.PHONE1);
                if (phone == null) phone = "";
                String isPrime = attribs.getAttribVal(customer_contactObj.IS_PRIMARY);
                //Default to the first contact, if non are marked as primary.
                if (i == 0) {
                    custOrdObj.set_cust_contact(name);
                    custOrdObj.set_customer_phone(phone);
                    custOrdObj.set_customer_fax(fax);
                }
                if (isPrime != null && isPrime.equals("Y")) {
                    custOrdObj.set_cust_contact(name);
                    custOrdObj.set_customer_phone(phone);
                    custOrdObj.set_customer_fax(fax);
                }
            }
        } catch (Exception e) {
            LOGGER.logError(CONTAINER, "Could not get Customer Contact Info.\n" +
                                            e.getLocalizedMessage());
        }
        return ret;
    }

    private String getOurContact() {
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
            LOGGER.logError(CONTAINER, "Could not get Our Contact Info.\n" +
                                            e.getLocalizedMessage());
        }
        return ret;
    }


    void daiLabel_carrierId_daiActionEvent(daiActionEvent e)
    {
        carrierObj tempObj = new carrierObj();
        String id = daiTextField_carrierId.getText();
        String name = daiTextField_carrierName.getText();

        DBAttributes attrib1 = new DBAttributes(carrierObj.ID, id, "Carrier ID", 200);
        DBAttributes attrib2 = new DBAttributes(carrierObj.NAME, name, "Carrier Name", 200);
        DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
                                              new carrierObj(),
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, " order by priority, id ");
        chooser.show();
        carrierObj chosenObj = (carrierObj)chooser.getChosenObj();
        chooser.dispose();
        copyCarrierAttribs(chosenObj);
    }

    private void daiTextField_carrierId_daiActionEvent(daiActionEvent e) {
        copyCarrierAttribs((carrierObj)e.getSource());
    }

    private void copyCarrierAttribs(carrierObj obj) {
        boolean isObjNull = false;
        String id = daiTextField_carrierId.getText();
        if (obj == null) isObjNull = true;
        if (isObjNull && (id != null && id.trim().length() > 0))
        {
            //Do nothing
        } else
        {
            daiTextField_carrierId.setText(isObjNull ? null : obj.get_id());
            daiTextField_carrierName.setText(isObjNull ? null : obj.get_name());
        }
    }

    private void setFieldsDisabled(boolean flag) {
        fieldControl_tax.setDisabled(flag);
        fieldControl_shipping.setDisabled(flag);
        fieldControl_discount.setDisabled(flag);
        fieldControl_datePromised.setDisabled(flag);
        fieldControl_revisedDate.setDisabled(flag);
        fieldControl_dateNeeded.setDisabled(flag);
        fieldControl_paymentTerms.setDisabled(flag);
        daiTextField_custId.setDisabled(flag);
        daiTextField_custName.setDisabled(flag);
        daiTextField_carrierId.setDisabled(flag);
        daiTextField_carrierName.setDisabled(flag);
        daiTextField_custPO.setDisabled(flag);
        daiDateField_custPODate.setDisabled(flag);
        daiTextField_fob.setDisabled(flag);
        daiLabel_carrierId.setDisabled(flag);
        daiLabel_custId.setDisabled(flag);
    }
}
