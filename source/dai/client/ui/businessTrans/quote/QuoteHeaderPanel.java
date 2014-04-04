
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.quote;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.carrierObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.customer_contactObj;
import dai.shared.businessObjs.prospectObj;
import dai.shared.businessObjs.quoteObj;
import dai.shared.businessObjs.user_profileObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCurrencyField;
import daiBeans.daiDBIdPopupField;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiPayTermsComboBox;
import daiBeans.daiRadioButton;
import daiBeans.daiTextField;


public class QuoteHeaderPanel extends daiHeaderPanel
{
    //Layout Controls
    XYLayout xYLayout1 = new XYLayout();

    //Field Controls
    daiCurrencyField fieldControl_totalValue = new daiCurrencyField();
    daiCurrencyField fieldControl_tax = new daiCurrencyField();
    daiCurrencyField fieldControl_shipping = new daiCurrencyField();
    daiCurrencyField fieldControl_discount = new daiCurrencyField();
    daiCurrencyField fieldControl_subtotal = new daiCurrencyField();
    daiTextField fieldControl_datePromised = new daiBeans.daiTextField();
    daiDateField fieldControl_dateNeeded = new daiBeans.daiDateField();
    daiPayTermsComboBox fieldControl_paymentTerms = new daiPayTermsComboBox();
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
    daiLabel daiLabel_fob = new daiLabel("FOB:");

    //Misc Controls
    CustDBPopup custPopup = null;
    daiDBIdPopupField carrierPopup = null;
    daiRadioButton      custRadioButton = new daiRadioButton("Customer");
    daiRadioButton      pspectRadioButton = new daiRadioButton("Prospect");
    ButtonGroup         custTypeButtonGroup = new ButtonGroup();

    //Customer Classes
    String TRANS_ID;
    String TRANS_TYPE;
    SessionMetaData sessionMeta;
    daiLabel daiLabel_id = new daiLabel();
    daiBeans.daiUserIdDateCreatedPanel daiUserIdDateCreatedPanel = new daiBeans.daiUserIdDateCreatedPanel();
    GroupBox groupBox_custData = new GroupBox();
    GroupBox groupBox_termsData = new GroupBox();
    GroupBox groupBox_totalsData = new GroupBox();
    XYLayout xYLayout4 = new XYLayout();
    GridLayout gridLayout1 = new GridLayout();
    XYLayout xYLayout2 = new XYLayout();

    //Constructor.
    public QuoteHeaderPanel(JFrame container, daiFrame parentFrame, quoteObj obj) {

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

        custPopup = new CustDBPopup(CONTAINER, new customerObj(),
                                    customerObj.NAME, "Cust/Prospect Id:");

        carrierPopup = new daiDBIdPopupField(CONTAINER, new carrierObj(),
                                        carrierObj.NAME, "Carrier Id:",
                                        null, " order by priority, id ");

        custPopup.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e) {
                custPopup_daiActionEvent(e);
            }
        });

        custRadioButton.setSelected(true);
        custRadioButton.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                custRadioButton_itemStateChanged(e);
            }
        });
        pspectRadioButton.addItemListener(new java.awt.event.ItemListener()
        {

            public void itemStateChanged(ItemEvent e)
            {
                pspectRadioButton_itemStateChanged(e);
            }
        });
        custTypeButtonGroup.add(custRadioButton);
        custTypeButtonGroup.add(pspectRadioButton);

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
        daiLabel_datePromised.setText("Est Ship Date Note:");
        daiLabel_dateNeeded.setText("Date Needed:");
        fieldControl_subtotal.setDisabled(true);
        fieldControl_totalValue.setDisabled(true);
        xYLayout1.setHeight(339);
        xYLayout1.setWidth(591);
        daiLabel_id.setText("Quote Id:");

        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e){
                ID_TEXT_FIELD_focusLost(e);
            }
        });
        groupBox_custData.setLayout(xYLayout2);
        groupBox_termsData.setLayout(gridLayout1);
        groupBox_totalsData.setLayout(xYLayout4);

        //Entry Controls
        groupBox_custData.setLabel("Quote For");
        groupBox_termsData.setLabel("Pay Terms/ Dates");
        groupBox_totalsData.setLabel("Totals");
        daiTextField_fob.setMaximumSize(new Dimension(2147483647, 24));
        gridLayout1.setColumns(2);
        gridLayout1.setHgap(5);
        gridLayout1.setRows(4);
        gridLayout1.setVgap(5);

        //Set some Defaults
        daiTextField_fob.setText("ORIGIN");
        carrierPopup.setId("BEST WAY");
        carrierPopup.setName("BEST WAY");


        groupBox_totalsData.add(daiLabel_subtotal, new XYConstraints(11, 2, -1, -1));
        groupBox_totalsData.add(fieldControl_subtotal, new XYConstraints(59, 2, -1, -1));
        groupBox_totalsData.add(daiLabel_tax, new XYConstraints(32, 25, -1, -1));
        groupBox_totalsData.add(daiLabel_shipping, new XYConstraints(9, 47, -1, -1));
        groupBox_totalsData.add(daiLabel_discount, new XYConstraints(8, 70, -1, -1));
        groupBox_totalsData.add(daiLabel_totalValue, new XYConstraints(-4, 92, -1, -1));
        groupBox_totalsData.add(fieldControl_totalValue, new XYConstraints(59, 91, -1, -1));
        groupBox_totalsData.add(fieldControl_tax, new XYConstraints(59, 24, -1, -1));
        groupBox_totalsData.add(fieldControl_shipping, new XYConstraints(59, 46, -1, -1));
        groupBox_totalsData.add(fieldControl_discount, new XYConstraints(59, 68, -1, -1));
        groupBox_custData.add(custRadioButton, new XYConstraints(-1, 0, 82, -1));
        groupBox_custData.add(pspectRadioButton, new XYConstraints(88, 0, 81, -1));
        groupBox_custData.add(custPopup, new XYConstraints(-9, 20, 440, -1));
        groupBox_custData.add(carrierPopup, new XYConstraints(29, 46, -1, -1));
        groupBox_termsData.add(daiLabel_paymentTerms, null);
        groupBox_termsData.add(fieldControl_paymentTerms, null);
        groupBox_termsData.add(daiLabel_fob, null);
        groupBox_termsData.add(daiTextField_fob, null);
        groupBox_termsData.add(daiLabel_datePromised, null);
        groupBox_termsData.add(fieldControl_datePromised, null);
        groupBox_termsData.add(daiLabel_dateNeeded, null);
        groupBox_termsData.add(fieldControl_dateNeeded, null);

        //Decorate the frame
        this.setLayout(xYLayout1);
        //this.setPreferredSize(new Dimension(591, 500));
        //this.setMinimumSize(new Dimension(591, 400));
        this.setBackground(daiColors.PanelColor);
        this.add(ID_TEXT_FIELD, new XYConstraints(100, 29, -1, -1));
        this.add(daiLabel_id, new XYConstraints(44, 31, 52, -1));
        this.add(daiUserIdDateCreatedPanel, new XYConstraints(334, 0, 155, -1));
        this.add(groupBox_custData, new XYConstraints(17, 63, 466, 104));
        this.add(groupBox_termsData, new XYConstraints(24, 179, -1, 133));
        this.add(groupBox_totalsData, new XYConstraints(266, 178, 197, 141));

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
        custRadioButton.setSelected(true);
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
            custPopup.requestFocus();
        }
    }

    public String getCustId() {
        return custPopup.getId();
    }

    public String getCarrierId() {
        return carrierPopup.getId();
    }

    public String getTransId() {
        return ID_TEXT_FIELD.getText();
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        quoteObj obj = new quoteObj();
        quoteObj tempObj = (quoteObj)BUSINESS_OBJ;

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
        quoteObj obj = (quoteObj)bobj;

        ID_TEXT_FIELD.setText(obj.get_id());
        daiUserIdDateCreatedPanel.setUserId(obj.get_created_by());
        daiUserIdDateCreatedPanel.setDateCreated(obj.get_date_created());
        fieldControl_discount.setText(obj.get_total_discount());
        fieldControl_shipping.setText(obj.get_total_shipping());
        fieldControl_subtotal.setText(obj.get_subtotal());
        fieldControl_tax.setText(obj.get_total_tax());
        fieldControl_totalValue.setText(obj.get_total_value());
        fieldControl_dateNeeded.setText(obj.get_date_needed());
        fieldControl_datePromised.setText(obj.get_quote_deliv_note());
        fieldControl_paymentTerms.setSelectedItem(obj.get_payment_terms());
        custPopup.setId(obj.get_customer_id());
        custPopup.setName(obj.get_customer_name());
        carrierPopup.setId(obj.get_carrier_id());
        carrierPopup.setName(obj.get_carrier_name());
        daiTextField_fob.setText(obj.get_fob());
        if (obj.get_customer_is_prospect() != null && obj.get_customer_is_prospect().equals("Y")) {
            pspectRadioButton.setSelected(true);
        } else {
            pspectRadioButton.setSelected(false);
        }
        BUSINESS_OBJ = obj;
        ID_TEXT_FIELD.requestFocus();
    }

    public void update_BusinessObj()
    {
        quoteObj obj = (quoteObj)BUSINESS_OBJ;

        obj.set_created_by(daiUserIdDateCreatedPanel.getUserId());
        obj.set_total_discount(fieldControl_discount.getText());
        obj.set_total_shipping(fieldControl_shipping.getText());
        obj.set_subtotal(fieldControl_subtotal.getText());
        obj.set_total_tax(fieldControl_tax.getText());
        obj.set_total_value(fieldControl_totalValue.getText());
        obj.set_date_needed(fieldControl_dateNeeded.getText());
        obj.set_quote_deliv_note(fieldControl_datePromised.getText());
        obj.set_payment_terms((String)fieldControl_paymentTerms.getSelectedItem());
        obj.set_customer_id(custPopup.getId());
        obj.set_customer_name(custPopup.getName());
        obj.set_carrier_id(carrierPopup.getId());
        obj.set_carrier_name(carrierPopup.getName());
        obj.set_fob(daiTextField_fob.getText());
        if (pspectRadioButton.isSelected()) {
            obj.set_customer_is_prospect("Y");
        } else {
            obj.set_customer_is_prospect("N");
        }

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

    void ID_TEXT_FIELD_focusLost(FocusEvent e)
    {
        if (!e.isTemporary())
        {
            String id = ID_TEXT_FIELD.getText();
            int exists=0;
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
                  //select the Prospect radio button
                  pspectRadioButton.setSelected(true);
                }
            }
        }
    }

    void custPopup_daiActionEvent(daiActionEvent e)
    {
        boolean isObjNull = false;

            if (custRadioButton.isSelected()) {
                customerObj obj = (customerObj)e.getSource();
                if (custPopup.getId() == null || obj == null) isObjNull = true;
                quoteObj custQuoteObj = (quoteObj)BUSINESS_OBJ;
                custQuoteObj.set_shipto_addr1(isObjNull ? null : obj.get_shipto_addr1());
                custQuoteObj.set_shipto_addr2(isObjNull ? null : obj.get_shipto_addr2());
                custQuoteObj.set_shipto_addr3(isObjNull ? null : obj.get_shipto_addr3());
                custQuoteObj.set_shipto_addr4(isObjNull ? null : obj.get_shipto_addr4());
                custQuoteObj.set_shipto_city(isObjNull ? null : obj.get_shipto_city());
                custQuoteObj.set_shipto_state_code(isObjNull ? null : obj.get_shipto_state_code());
                custQuoteObj.set_shipto_zip(isObjNull ? null : obj.get_shipto_zip());
                custQuoteObj.set_shipto_country_code(isObjNull ? null : obj.get_shipto_country_code());
                custQuoteObj.set_shipto_country_name(isObjNull ? null : obj.get_shipto_country_name());

                String[] custInfo = getCustContact(custPopup.getId());
                custQuoteObj.set_cust_contact(custInfo[0]);
                custQuoteObj.set_cust_phone(custInfo[1]);
                custQuoteObj.set_cust_fax(custInfo[2]);
                String[] ourInfo  = getOurContact();
                custQuoteObj.set_our_contact(ourInfo[0]);
                custQuoteObj.set_our_email(ourInfo[1]);

                BUSINESS_OBJ = custQuoteObj;
            } else {
                prospectObj obj = (prospectObj)e.getSource();
                if (custPopup.getId() == null || obj == null) isObjNull = true;
                quoteObj custQuoteObj = (quoteObj)BUSINESS_OBJ;
                custQuoteObj.set_shipto_addr1(isObjNull ? null : obj.get_addr1());
                custQuoteObj.set_shipto_addr2(isObjNull ? null : obj.get_addr2());
                custQuoteObj.set_shipto_addr3(isObjNull ? null : obj.get_addr3());
                custQuoteObj.set_shipto_addr4(isObjNull ? null : obj.get_addr4());
                custQuoteObj.set_shipto_city(isObjNull ? null : obj.get_city());
                custQuoteObj.set_shipto_state_code(isObjNull ? null : obj.get_state_code());
                custQuoteObj.set_shipto_zip(isObjNull ? null : obj.get_zip());
                custQuoteObj.set_shipto_country_code(isObjNull ? null : obj.get_country_code());
                custQuoteObj.set_shipto_country_name(isObjNull ? null : obj.get_country_name());

                String[] custInfo = getCustContact(custPopup.getId());
                custQuoteObj.set_cust_contact(custInfo[0]);
                custQuoteObj.set_cust_phone(custInfo[1]);
                custQuoteObj.set_cust_fax(custInfo[2]);
                String[] ourInfo  = getOurContact();
                custQuoteObj.set_our_contact(ourInfo[0]);
                custQuoteObj.set_our_email(ourInfo[1]);

                BUSINESS_OBJ = custQuoteObj;
            }

            daiHeaderSubPanel shiptoBilltoPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(2);
            shiptoBilltoPanel.update_UI(BUSINESS_OBJ);;
    }

    private String[] getCustContact(String custId) {
        String ret[] = new String[3];
        String name = null;
        String fax = null;
        String phone = null;
        try {
            if (custRadioButton.isSelected()) {
                String sqlStmt = " select " + customer_contactObj.NAME + "," +
                        customer_contactObj.FAX1 + ", " +
                        customer_contactObj.PHONE1 + ", " +
                        customer_contactObj.IS_PRIMARY +
                        " from " + customer_contactObj.TABLE_NAME + " where " +
                        " id = '" + custId + "' and locality = '" +
                        customer_contactObj.getObjLocality() + "'" +
                        " order by detail_id ";
                DBRecSet attribSet = _dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
                                                                    sqlStmt);
                for (int i=0; i<attribSet.getSize(); i++) {
                    DBRec attribs = attribSet.getRec(i);
                    String isPrime = attribs.getAttribVal(customer_contactObj.IS_PRIMARY);
                    //Default to the first contact, if none are marked as primary.
                    if (i == 0) {
                        name = attribs.getAttribVal(customer_contactObj.NAME);
                        fax = attribs.getAttribVal(customer_contactObj.FAX1);
                        phone = attribs.getAttribVal(customer_contactObj.PHONE1);
                    }
                    if (isPrime != null && isPrime.equals("Y")) {
                        name = attribs.getAttribVal(customer_contactObj.NAME);
                        fax = attribs.getAttribVal(customer_contactObj.FAX1);
                        phone = attribs.getAttribVal(customer_contactObj.PHONE1);
                    }
                }
            } else {
                String sqlStmt = " select " + prospectObj.FIRST_NAME + "," +
                        prospectObj.LAST_NAME + ", " +
                        prospectObj.PHONE + ", " +
                        prospectObj.FAX  +
                        " from " + prospectObj.TABLE_NAME + " where " +
                        " id = '" + custId + "' and locality = '" +
                        prospectObj.getObjLocality() + "'";
                DBRecSet attribSet = _dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
                                                                    sqlStmt);
                if (attribSet.getSize() > 0) {
                    DBRec attribs = attribSet.getRec(0);
                    name = attribs.getAttribVal(prospectObj.FIRST_NAME);
                    name += " " + attribs.getAttribVal(prospectObj.LAST_NAME);
                    fax = attribs.getAttribVal(prospectObj.FAX);
                    phone = attribs.getAttribVal(prospectObj.PHONE);
                }
            }
            ret[0] = name;
            ret[1] = phone;
            ret[2] = fax;
        } catch (Exception e) {
            LOGGER.logError(CONTAINER, "Could not get Customer Contact Info.\n" +
                                            e.getLocalizedMessage());
        }
        return ret;
    }

    private String[] getOurContact() {
        String ret[] = new String[2];
        String name = null;
        String email = null;
        String sqlStmt = " select " + user_profileObj.USER_NAME +  ", " +
                        user_profileObj.USER_EMAIL +
                        " from " + user_profileObj.TABLE_NAME + " where " +
                        " id = '" + sessionMeta.getUserId() + "'";

        try {
            DBRecSet attribSet = _dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
                                                                    sqlStmt);
            if (attribSet.getSize() > 0) {
                name = attribSet.getRec(0).getAttribVal(user_profileObj.USER_NAME);
                email = attribSet.getRec(0).getAttribVal(user_profileObj.USER_EMAIL);
            }
            ret[0] = name;
            ret[1] = email;
        } catch (Exception e) {
            LOGGER.logError(CONTAINER, "Could not get Our Contact Info.\n" +
                                            e.getLocalizedMessage());
        }
        return ret;
    }

    private void setFieldsDisabled(boolean flag) {
        fieldControl_tax.setDisabled(flag);
        fieldControl_shipping.setDisabled(flag);
        fieldControl_discount.setDisabled(flag);
        fieldControl_datePromised.setDisabled(flag);
        fieldControl_dateNeeded.setDisabled(flag);
        fieldControl_paymentTerms.setDisabled(flag);
        daiTextField_fob.setDisabled(flag);
        custPopup.setDisabled(flag);
        carrierPopup.setDisabled(flag);
    }

    void custRadioButton_itemStateChanged(ItemEvent e)
    {
        if (custRadioButton.isSelected()) {
            custPopup.setPopupSource(new customerObj(), customerObj.NAME, "Cust/Prospect Id:");
        }
    }

    void pspectRadioButton_itemStateChanged(ItemEvent e)
    {
        if (pspectRadioButton.isSelected()) {
            custPopup.setPopupSource(new prospectObj(), prospectObj.COMPANY_NAME, "Cust/Prospect Id:");
        }
    }
}
