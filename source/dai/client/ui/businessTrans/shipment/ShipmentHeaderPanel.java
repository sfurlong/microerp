//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;

import com.borland.jbcl.control.BevelPanel;
import com.borland.jbcl.control.ShapeControl;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.carrierObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.shipmentObj;
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

public class ShipmentHeaderPanel extends daiHeaderPanel
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
    daiBeans.daiDateField fieldControl_dateShipped = new daiBeans.daiDateField();
    daiBeans.daiDateField fieldControl_datePromised = new daiBeans.daiDateField();
    daiBeans.daiDateField fieldControl_dateNeeded = new daiBeans.daiDateField();
    daiPayTermsComboBox fieldControl_paymentTerms = new daiPayTermsComboBox();
    daiBeans.daiTextField fieldControl_airBillNum = new daiBeans.daiTextField();
    daiTextField daiTextField_carrierId = new daiTextField();
    daiTextField daiTextField_carrierName = new daiTextField();
    daiQueryTextField daiTextField_custId = new daiQueryTextField(new customerObj());
    daiTextField daiTextField_custName = new daiTextField();
    daiTextField daiTextField_fob = new daiTextField();
    daiCurrencyField daiCurrency_totAmtPaid = new daiCurrencyField();

    //Label Controls
    daiLabel daiLabel_totAmtPaid = new daiLabel("Tot Amt Paid:");
    daiLabel daiLabel_totalValue = new daiLabel();
    daiLabel daiLabel_tax = new daiLabel();
    daiLabel daiLabel_shipping = new daiLabel();
    daiLabel daiLabel_discount = new daiLabel();
    daiLabel daiLabel_subtotal = new daiLabel();
    daiLabel daiLabel_datePromised = new daiLabel("Date Promised:");
    daiLabel daiLabel_dateShipped = new daiLabel("Date Shipped:");
    daiLabel daiLabel_dateNeeded = new daiLabel("Date Needed:");
    daiLabel daiLabel_airBillNum = new daiLabel("Tracking#:");
    daiLabel daiLabel_paymentTerms = new daiLabel();
    daiLabel daiLabel_custId = new daiLabel("Customer Id:");
    daiLabel daiLabel_carrierId = new daiLabel("Carrier Id:");
    daiLabel daiLabel_id = new daiLabel("Shipment Id:");
    daiLabel daiLabel_fob = new daiLabel("FOB:");

    //Misc Controls
    ShapeControl shapeControl_line1 = new ShapeControl();

    //Customer Classes
    String TRANS_ID;
    String TRANS_TYPE;
    BevelPanel bevelPanel3 = new BevelPanel();
    SessionMetaData sessionMeta;
    XYLayout xYLayout2 = new XYLayout();
    XYLayout xYLayout3 = new XYLayout();
    daiBeans.daiUserIdDateCreatedPanel daiUserIdDateCreatedPanel = new daiBeans.daiUserIdDateCreatedPanel();
    daiLabel daiLabel_custPo = new daiLabel();
    daiLabel daiLabel_poDate = new daiLabel();
    daiDateField daiDateField_poDate = new daiDateField();
    daiTextField daiTextField_poId = new daiTextField();
    daiTextField daiTextField_sourceOrd = new daiTextField();
    daiLabel daiLabel_sourceOrd = new daiLabel();
    daiLabel daiLabel_credMemo = new daiLabel();
    JLabel jLabel_isCanceled = new JLabel();

    //Constructor.
    public ShipmentHeaderPanel(JFrame container, daiFrame parentFrame, shipmentObj obj) {

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
        daiLabel_totalValue.setText("Total Value:");
        daiLabel_tax.setText("Tax:");
        daiLabel_shipping.setText("Shipping:");
        daiLabel_subtotal.setText("Subtotal:");
        daiLabel_discount.setText("Discount:");
        daiLabel_paymentTerms.setText("Payment Terms:");
        fieldControl_subtotal.setDisabled(true);
        fieldControl_totalValue.setDisabled(true);
        daiTextField_sourceOrd.setDisabled(true);
        daiLabel_credMemo.setVisible(false);
        jLabel_isCanceled.setVisible(false);
        daiCurrency_totAmtPaid.setBackground(Color.lightGray);

        xYLayout1.setHeight(382);
        xYLayout1.setWidth(654);
        shapeControl_line1.setType(ShapeControl.HORZ_LINE);
        daiLabel_custId.setHREFstyle(true);
        daiLabel_custId.adddaiActionListener(new daiActionListener(){
                                                 public void daiActionEvent(daiActionEvent e){
                                                     daiLabel_custId_daiAction(e);}});
        daiTextField_custId.adddaiActionListener(new daiActionListener(){   public void daiActionEvent(daiActionEvent e){
                                                         daiTextField_custId_daiAction(e);}});

        //Decorate the frame
        this.setLayout(xYLayout1);
        this.setPreferredSize(new Dimension(591, 375));
        this.setMinimumSize(new Dimension(591, 375));
        this.setBackground(daiColors.PanelColor);
        daiLabel_carrierId.setHREFstyle(true);
        daiLabel_carrierId.adddaiActionListener(new daiActionListener() {
                                                    public void daiActionEvent(daiActionEvent e) {
                                                        daiLabel_carrierId_daiActionEvent();
                                                    }
                                                });
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter(){
                                           public void focusLost(FocusEvent e){
                                               ID_TEXT_FIELD_focusLost(e);}});
        daiLabel_custPo.setText("Cust. PO:");
        daiLabel_poDate.setText("Cust. PO Date:");
        daiLabel_sourceOrd.setText("Source Trans:");
        daiLabel_credMemo.setText("CREDIT MEMO");
        daiLabel_credMemo.setForeground(Color.blue);
        daiLabel_credMemo.setFont(new java.awt.Font("Dialog", 1, 14));
        jLabel_isCanceled.setFont(new java.awt.Font("Dialog", 1, 18));
        jLabel_isCanceled.setForeground(Color.red);
        jLabel_isCanceled.setText("CANCELED");
        //Entry Controls
        this.add(shapeControl_line1, new XYConstraints(1, 142, 581, 5));
        this.add(daiUserIdDateCreatedPanel, new XYConstraints(434, 1, 155, -1));
        this.add(daiLabel_custId, new XYConstraints(40, 87, -1, -1));
        this.add(daiLabel_id, new XYConstraints(37, 28, 67, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(108, 26, 118, -1));
        this.add(daiTextField_sourceOrd, new XYConstraints(108, 51, 118, -1));
        this.add(daiTextField_custId, new XYConstraints(108, 86, 153, -1));
        this.add(daiTextField_custName, new XYConstraints(264, 86, 218, -1));
        this.add(daiLabel_sourceOrd, new XYConstraints(35, 53, -1, -1));
        this.add(daiLabel_custPo, new XYConstraints(55, 115, -1, -1));
        this.add(daiTextField_poId, new XYConstraints(108, 113, 153, -1));
        this.add(daiDateField_poDate, new XYConstraints(338, 113, -1, -1));
        this.add(fieldControl_paymentTerms, new XYConstraints(109, 163, 135, -1));
        this.add(daiLabel_paymentTerms, new XYConstraints(24, 167, 81, -1));
        this.add(daiLabel_datePromised, new XYConstraints(24, 216, 81, -1));
        this.add(daiLabel_dateNeeded, new XYConstraints(24, 241, 81, -1));
        this.add(daiLabel_dateShipped, new XYConstraints(24, 263, 81, -1));
        this.add(daiLabel_fob, new XYConstraints(24, 189, 81, -1));
        this.add(daiLabel_credMemo, new XYConstraints(233, 26, -1, -1));
        this.add(jLabel_isCanceled, new XYConstraints(109, 0, 116, 26));
        this.add(daiLabel_carrierId, new XYConstraints(57, 329, -1, -1));
        this.add(daiLabel_airBillNum, new XYConstraints(51, 351, 53, 19));
        this.add(daiLabel_subtotal, new XYConstraints(307, 170, 78, -1));
        this.add(daiLabel_tax, new XYConstraints(307, 190, 78, -1));
        this.add(daiLabel_shipping, new XYConstraints(307, 212, 78, -1));
        this.add(daiLabel_discount, new XYConstraints(307, 235, 78, -1));
        this.add(daiLabel_totalValue, new XYConstraints(328, 257, -1, -1));
        this.add(daiLabel_totAmtPaid, new XYConstraints(322, 279, -1, 20));
        this.add(fieldControl_subtotal, new XYConstraints(388, 167, -1, -1));
        this.add(fieldControl_tax, new XYConstraints(388, 190, -1, -1));
        this.add(fieldControl_shipping, new XYConstraints(388, 213, -1, -1));
        this.add(fieldControl_discount, new XYConstraints(388, 236, -1, -1));
        this.add(fieldControl_totalValue, new XYConstraints(388, 259, -1, -1));
        this.add(daiCurrency_totAmtPaid, new XYConstraints(388, 281, -1, -1));
        this.add(daiTextField_fob, new XYConstraints(109, 187, -1, -1));
        this.add(daiLabel_poDate, new XYConstraints(267, 115, -1, -1));
        this.add(fieldControl_datePromised, new XYConstraints(109, 214, -1, -1));
        this.add(fieldControl_dateNeeded, new XYConstraints(109, 238, -1, -1));
        this.add(fieldControl_dateShipped, new XYConstraints(109, 262, -1, -1));
        this.add(daiTextField_carrierId, new XYConstraints(108, 327, 148, -1));
        this.add(daiTextField_carrierName, new XYConstraints(258, 327, 238, -1));
        this.add(fieldControl_airBillNum, new XYConstraints(108, 350, 181, -1));
    }

    //Override base class method so we can do some extra logic afterword.
    public int refresh()
    {
        //Call the base class method first.
        super.refresh();

        //Enable the Trans ID text field.
        ID_TEXT_FIELD.setText(null);
        ID_TEXT_FIELD.setDisabled(false);
        return 0;
    }

    public void resetTabEntrySeq() {
        if (!ID_TEXT_FIELD.isDisabled())
        {
            ID_TEXT_FIELD.requestFocus();
        } else
        {
            daiTextField_custId.requestFocus();
        }
    }

    public String getCustId() {
        return daiTextField_custId.getText();
    }

    public String getTransId() {
        return ID_TEXT_FIELD.getText();
    }

    public boolean isCreditMemo() {
        String cm = ((shipmentObj)BUSINESS_OBJ).get_is_credit_memo();
        if (cm != null && cm.equals("Y"))
        {
            return true;
        } else
        {
            return false;
        }
    }

    public boolean isCanceled() {
        String flag = ((shipmentObj)BUSINESS_OBJ).get_is_canceled();
        if (flag != null && flag.equals("Y"))
        {
            return true;
        } else
        {
            return false;
        }
    }

    public String getCarrierId() {
        return daiTextField_carrierId.getText();
    }

    public String getSourceOrdId() {
        return daiTextField_sourceOrd.getText();
    }

    public void setIsCanceled() {
        shipmentObj obj = (shipmentObj)BUSINESS_OBJ;
        obj.set_is_canceled("Y");
        BUSINESS_OBJ = obj;
        jLabel_isCanceled.setVisible(true);
        //Set the dirty flag so that this panel data will get saved
        this._panelIsDirty = true;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        shipmentObj obj = new shipmentObj();
        shipmentObj tempObj = (shipmentObj)BUSINESS_OBJ;

        //Set the Primary Keys for the new Item Object.
        obj.set_id(tempObj.get_id());
        obj.set_locality(tempObj.get_locality());

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

        return 0;
    }

    protected void update_UI(BusinessObject bobj)
    {
        shipmentObj obj = (shipmentObj)bobj;

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
        fieldControl_dateShipped.setText(obj.get_date_shipped());
        fieldControl_paymentTerms.setSelectedItem(obj.get_payment_terms());
        daiTextField_custId.setText(obj.get_customer_id());
        daiTextField_custName.setText(obj.get_customer_name());
        daiTextField_carrierId.setText(obj.get_carrier_id());
        daiTextField_carrierName.setText(obj.get_carrier_name());
        fieldControl_airBillNum.setText(obj.get_air_bill_num());
        daiTextField_fob.setText(obj.get_fob());
        daiTextField_poId.setText(obj.get_po_num());
        daiDateField_poDate.setText(obj.get_po_date());
        daiTextField_sourceOrd.setText(obj.get_order_num());
        daiCurrency_totAmtPaid.setText(obj.get_total_cash_received());

        if (obj.get_is_credit_memo() != null && obj.get_is_credit_memo().equals("Y"))
        {
            daiLabel_credMemo.setVisible(true);
        } else
        {
            daiLabel_credMemo.setVisible(false);
        }

        if (obj.get_is_canceled() != null && obj.get_is_canceled().equals("Y"))
        {
            jLabel_isCanceled.setVisible(true);
        } else
        {
            jLabel_isCanceled.setVisible(false);
        }

        BUSINESS_OBJ = obj;
    }

    public void update_BusinessObj()
    {
        shipmentObj obj = (shipmentObj)BUSINESS_OBJ;

        obj.set_created_by(daiUserIdDateCreatedPanel.getUserId());
        obj.set_total_discount(fieldControl_discount.getText());
        obj.set_total_shipping(fieldControl_shipping.getText());
        obj.set_subtotal(fieldControl_subtotal.getText());
        obj.set_total_tax(fieldControl_tax.getText());
        obj.set_total_value(fieldControl_totalValue.getText());
        obj.set_air_bill_num(fieldControl_airBillNum.getText());
        obj.set_date_needed(fieldControl_dateNeeded.getText());
        obj.set_date_promised(fieldControl_datePromised.getText());
        obj.set_date_shipped(fieldControl_dateShipped.getText());
        obj.set_payment_terms((String)fieldControl_paymentTerms.getSelectedItem());
        obj.set_customer_id(daiTextField_custId.getText());
        obj.set_customer_name(daiTextField_custName.getText());
        obj.set_carrier_id(daiTextField_carrierId.getText());
        obj.set_carrier_name(daiTextField_carrierName.getText());
        obj.set_air_bill_num(fieldControl_airBillNum.getText());
        obj.set_fob(daiTextField_fob.getText());
        obj.set_po_num(daiTextField_poId.getText());
        obj.set_po_date(daiDateField_poDate.getText());
        obj.set_order_num(daiTextField_sourceOrd.getText());
        obj.set_total_cash_received(daiCurrency_totAmtPaid.getText());

        BUSINESS_OBJ = obj;
    }

    void refreshTotalValue()
    {
        Float discount = new Float(0);
        Float shipping = new Float(0);
        Float subtotal = new Float(0);
        Float tax      = new Float(0);
        if (fieldControl_discount.getText() != null)
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
            if (id != null && !ID_TEXT_FIELD.isDisabled())
            {
                CONTAINER_FRAME.callBackInsertNewId(id);
                //Disable the Trans ID text field.
                ID_TEXT_FIELD.setDisabled(true);
            }
        }
    }

    private void daiLabel_carrierId_daiActionEvent() {
        carrierObj tempObj = new carrierObj();
        String id = daiTextField_carrierId.getText();
        String name = daiTextField_carrierName.getText();

        DBAttributes attrib = new DBAttributes(carrierObj.ID, "Carrier Id", 200);
        DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
                                              tempObj,
                                              new DBAttributes[]{attrib},
                                              null, " order by priority, id ");
        chooser.show();
        carrierObj chosenObj = (carrierObj)chooser.getChosenObj();
        chooser.dispose();
        copyCarrierAttribs(chosenObj);
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
    private void daiTextField_carrierId_daiAction(daiActionEvent e)
    {
        //Copy all the cust fields
        copyCarrierAttribs((carrierObj)e.getSource());
    }

    void daiLabel_custId_daiAction(daiActionEvent e)
    {
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

    private void copyCustAttribs(customerObj obj) {
        boolean isObjNull = false;
        String custId = daiTextField_custId.getText();
        if (obj == null) isObjNull = true;
        if (isObjNull && (custId != null && custId.trim().length() > 0))
        {
            //Do nothing
        } else
        {
            daiTextField_custId.setText(isObjNull ? null : obj.get_id());
            daiTextField_custName.setText(isObjNull ? null : obj.get_name());
            shipmentObj shipObj = (shipmentObj)BUSINESS_OBJ;
            shipObj.set_customer_id(isObjNull ? null : obj.get_id());
            shipObj.set_shipto_attn(isObjNull ? null : obj.get_shipto_attn());
            shipObj.set_shipto_addr1(isObjNull ? null : obj.get_shipto_addr1());
            shipObj.set_shipto_addr2(isObjNull ? null : obj.get_shipto_addr2());
            shipObj.set_shipto_addr3(isObjNull ? null : obj.get_shipto_addr3());
            shipObj.set_shipto_addr4(isObjNull ? null : obj.get_shipto_addr4());
            shipObj.set_shipto_city(isObjNull ? null : obj.get_shipto_city());
            shipObj.set_shipto_state_code(isObjNull ? null : obj.get_shipto_state_code());
            shipObj.set_shipto_zip(isObjNull ? null : obj.get_shipto_zip());
            shipObj.set_shipto_country_code(isObjNull ? null : obj.get_shipto_country_code());
            shipObj.set_shipto_country_name(isObjNull ? null : obj.get_shipto_country_name());
            shipObj.set_billto_attn(isObjNull ? null : obj.get_billto_attn());
            shipObj.set_billto_addr1(isObjNull ? null : obj.get_billto_addr1());
            shipObj.set_billto_addr2(isObjNull ? null : obj.get_billto_addr2());
            shipObj.set_billto_addr3(isObjNull ? null : obj.get_billto_addr3());
            shipObj.set_billto_addr4(isObjNull ? null : obj.get_billto_addr4());
            shipObj.set_billto_city(isObjNull ? null : obj.get_billto_city());
            shipObj.set_billto_state_code(isObjNull ? null : obj.get_billto_state_code());
            shipObj.set_billto_zip(isObjNull ? null : obj.get_billto_zip());
            shipObj.set_billto_country_code(isObjNull ? null : obj.get_billto_country_code());
            shipObj.set_billto_country_name(isObjNull ? null : obj.get_billto_country_name());
            daiHeaderSubPanel shiptoBilltoPanel = (daiHeaderSubPanel)CONTAINER_FRAME.getTabPanel(2);
            BUSINESS_OBJ = shipObj;
            shiptoBilltoPanel.update_UI(BUSINESS_OBJ);;
        }
    }

    void daiTextField_custId_daiAction(daiActionEvent e)
    {
        //Copy all the cust fields
        copyCustAttribs((customerObj)e.getSource());
    }
}
