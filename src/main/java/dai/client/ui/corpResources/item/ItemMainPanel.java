
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.item;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.accountObj;
import dai.shared.businessObjs.itemObj;
import dai.shared.businessObjs.vendorObj;
import dai.shared.cmnSvcs.FinanceAcctsDataCache;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiComboBox;
import daiBeans.daiCurrencyField;
import daiBeans.daiDataModifiedEvent;
import daiBeans.daiDataModifiedListener;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiQueryTextField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedModifiedPanel;


public class ItemMainPanel extends daiHeaderPanel
{
    //Labels
	daiLabel labelControl_id = new daiLabel();
	daiLabel labelControl_standardDesc = new daiLabel();
	daiLabel labelControl_vendorId = new daiLabel("Vendor Id:");
	daiLabel labelControl_purchPrice = new daiLabel("Purchase Price:");
	daiLabel labelControl_salesPrice = new daiLabel("Sales Price:");
	daiLabel labelControl_qtyOnHand = new daiLabel("Qty On Hand:");
	daiLabel labelControl_qtyCustBackOrd = new daiLabel("Qty Cust Backorder:");
	daiLabel labelControl_qtyVendorBackOrd = new daiLabel("Qty Vendor Backorder:");
    daiLabel daiLabel_vendorRetailPrice = new daiLabel("Vendor Retail Price:");
    daiLabel daiLabel_purchAcctId = new daiLabel("Purch Acct Id:");
    daiLabel daiLabel_salesAcctId = new daiLabel("Sales Acct Id:");
    daiLabel daiLabel_inventoryAcctId = new daiLabel("Inventory Acct Id:");
    daiUserIdDateCreatedModifiedPanel daiUserIdDateCreatedPanel = new daiUserIdDateCreatedModifiedPanel();
    daiLabel daiLabel_note = new daiLabel("Note:");

    //Edit Controls
	daiTextField fieldControl_standardDesc = new daiTextField();
	daiQueryTextField fieldControl_vendorId = new daiQueryTextField(new vendorObj());
	daiCurrencyField fieldControl_purchPrice = new daiCurrencyField();
	daiCurrencyField fieldControl_salesPrice = new daiCurrencyField();
	daiNumField fieldControl_qtyOnHand = new daiNumField();
	daiNumField fieldControl_qtyCustBackOrd = new daiNumField();
	daiNumField fieldControl_qtyVendorBackOrd = new daiNumField();
    daiCurrencyField daiNumField_vendorRetailPrice = new daiCurrencyField();
    daiTextField daiTextField_vendorName = new daiTextField();

    daiComboBox daiTextField_purchAcctId = new daiComboBox();
    daiTextField daiTextField_purchAcctName = new daiTextField();
    daiComboBox daiTextField_salesAcctId = new daiComboBox();
    daiTextField daiTextField_salesAcctName = new daiTextField();
    daiComboBox daiTextField_inventoryAcctId = new daiComboBox();
    daiTextField daiTextField_inventoryAcctName = new daiTextField();
    daiTextArea daiTextArea_note = new daiTextArea();

	XYLayout xYLayout2 = new XYLayout();
	XYLayout xYLayout1 = new XYLayout();
	XYLayout xYLayout3 = new XYLayout();

	SessionMetaData sessionMeta;

    GroupBox groupBox_inventory = new GroupBox();
    XYLayout xYLayout6 = new XYLayout();
    GroupBox groupBox_accts = new GroupBox();
    XYLayout xYLayout8 = new XYLayout();
    daiLabel daiLabel_equal = new daiLabel();
    daiLabel daiLabel_mult = new daiLabel();
    daiComboBox daiComboBox_discountPct = new daiComboBox();
    daiLabel daiLabel_vendorDiscount = new daiLabel();
    FinanceAcctsDataCache _acctsDataCache;

	public ItemMainPanel(JFrame container, daiFrame parentFrame, itemObj obj)
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
        Vector vect = new Vector();
        for (int i=0; i<101; i++)
        {
            vect.addElement(Integer.toString(i));
        }
        daiComboBox_discountPct = new daiComboBox(vect);
        daiComboBox_discountPct.setSelectedIndex(0);

        _acctsDataCache = FinanceAcctsDataCache.getInstance();
        daiTextField_purchAcctId = new daiComboBox(_acctsDataCache.getAcctNums());
        daiTextField_inventoryAcctId = new daiComboBox(_acctsDataCache.getAcctNums());
        daiTextField_salesAcctId = new daiComboBox(_acctsDataCache.getAcctNums());

		setLayout(xYLayout2);
		xYLayout2.setHeight(455);
		xYLayout2.setWidth(613);
		xYLayout3.setHeight(468);
		xYLayout3.setWidth(685);
        labelControl_id.setText("Id:");
        labelControl_standardDesc.setText("Description:");
        fieldControl_vendorId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                fieldControl_vendorId_daiActionEvent(e);
            }
        });
        labelControl_vendorId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                labelControl_vendorId_daiAction(e);
            }
        });
        labelControl_vendorId.setHREFstyle(true);
    	fieldControl_qtyOnHand.setDisabled(true);
	    fieldControl_qtyCustBackOrd.setDisabled(true);
    	fieldControl_qtyVendorBackOrd.setDisabled(true);

        groupBox_inventory.setLabel("Inventory Totals");
        groupBox_inventory.setLayout(xYLayout6);

        sessionMeta = sessionMeta.getInstance();

        groupBox_accts.setLabel("Financial Accounts");
        groupBox_accts.setLayout(xYLayout8);
        daiLabel_purchAcctId.setHREFstyle(true);
        daiLabel_purchAcctId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_purchAcctId_daiAction(e);
            }
        });
        daiLabel_salesAcctId.setHREFstyle(true);
        daiLabel_salesAcctId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_salesAcctId_daiAction(e);
            }
        });
        daiLabel_inventoryAcctId.setHREFstyle(true);
        daiLabel_inventoryAcctId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_inventoryAcctId_daiAction(e);
            }
        });
        daiTextField_purchAcctName.setDisabled(true);
        daiTextField_salesAcctName.setDisabled(true);
        daiTextField_inventoryAcctName.setDisabled(true);

        daiTextField_purchAcctId.adddaiDataModifiedListener(new daiDataModifiedListener()
            {
                public void daiDataModified(daiDataModifiedEvent e)
                {
                    String t = daiTextField_purchAcctId.getText();
                    int    selectedIndex = daiTextField_purchAcctId.getSelectedIndex();
                    if (t == null || t.length() == 0 || selectedIndex == -1) {
                        daiTextField_purchAcctName.setText(null);
                    } else {
                        daiTextField_purchAcctName.setText(_acctsDataCache.getAcctName(selectedIndex));
                    }
                }
            });
        daiTextField_salesAcctId.adddaiDataModifiedListener(new daiDataModifiedListener()
            {
                public void daiDataModified(daiDataModifiedEvent e)
                {
                    String t = daiTextField_salesAcctId.getText();
                    int selectedIndex = daiTextField_salesAcctId.getSelectedIndex();
                    if (t == null || t.length() == 0 || selectedIndex == -1) {
                        daiTextField_salesAcctName.setText(null);
                    } else {
                        daiTextField_salesAcctName.setText(_acctsDataCache.getAcctName(daiTextField_salesAcctId.getSelectedIndex()));
                    }
                }
            });
        daiTextField_inventoryAcctId.adddaiDataModifiedListener(new daiDataModifiedListener()
            {
                public void daiDataModified(daiDataModifiedEvent e)
                {
                    String t = daiTextField_inventoryAcctId.getText();
                    int selectedIndex = daiTextField_inventoryAcctId.getSelectedIndex();
                    if (t == null || t.length() == 0 || selectedIndex == -1) {
                        daiTextField_inventoryAcctName.setText(null);
                    } else {
                        daiTextField_inventoryAcctName.setText(_acctsDataCache.getAcctName(daiTextField_inventoryAcctId.getSelectedIndex()));
                    }
                }
            });


        daiLabel_equal.setText("=");
        daiLabel_mult.setText("X");
        daiLabel_vendorDiscount.setText("Discount %");
        daiNumField_vendorRetailPrice.addTextListener(new TextListener(){
            public void textValueChanged(TextEvent evt)
            {
                updatePurchasePrice();
            }
        });
        daiComboBox_discountPct.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                updatePurchasePrice();
            }
        });

        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                ID_TEXT_FIELD_focusLost(e);
            }
        });
        groupBox_inventory.add(labelControl_qtyCustBackOrd, new XYConstraints(154, 8, -1, -1));
        groupBox_inventory.add(labelControl_qtyOnHand, new XYConstraints(-3, 8, -1, -1));
        groupBox_inventory.add(labelControl_qtyVendorBackOrd, new XYConstraints(347, 8, -1, -1));
        groupBox_inventory.add(fieldControl_qtyOnHand, new XYConstraints(67, 6, 75, -1));
        groupBox_inventory.add(fieldControl_qtyCustBackOrd, new XYConstraints(257, 6, 75, -1));
        groupBox_inventory.add(fieldControl_qtyVendorBackOrd, new XYConstraints(463, 6, 75, -1));
        groupBox_accts.add(daiLabel_inventoryAcctId, new XYConstraints(5, 57, -1, -1));
        groupBox_accts.add(daiLabel_purchAcctId, new XYConstraints(22, 6, -1, -1));
        groupBox_accts.add(daiLabel_salesAcctId, new XYConstraints(23, 32, -1, -1));
        groupBox_accts.add(daiTextField_purchAcctId, new XYConstraints(97, 4, 133, -1));
        groupBox_accts.add(daiTextField_purchAcctName, new XYConstraints(233, 4, 210, -1));
        groupBox_accts.add(daiTextField_salesAcctId, new XYConstraints(97, 30, 133, -1));
        groupBox_accts.add(daiTextField_salesAcctName, new XYConstraints(233, 29, 210, -1));
        groupBox_accts.add(daiTextField_inventoryAcctId, new XYConstraints(97, 56, 133, -1));
        groupBox_accts.add(daiTextField_inventoryAcctName, new XYConstraints(233, 55, 210, -1));

        //Decorate the panel
        this.setBackground(daiColors.PanelColor);
        this.add(labelControl_standardDesc, new XYConstraints(11, 66, 70, 15));
        this.add(labelControl_id, new XYConstraints(70, 40, -1, 15));
        this.add(labelControl_vendorId, new XYConstraints(31, 90, -1, 15));
        this.add(labelControl_salesPrice, new XYConstraints(10, 124, 71, 15));
        this.add(labelControl_purchPrice, new XYConstraints(1, 154, 80, 15));
        this.add(daiLabel_note, new XYConstraints(56, 353, -1, -1));
        this.add(daiUserIdDateCreatedPanel, new XYConstraints(410, 0, -1, -1));
        this.add(daiLabel_vendorRetailPrice, new XYConstraints(206, 136, -1, -1));
        this.add(daiLabel_equal, new XYConstraints(182, 154, 14, -1));
        this.add(daiLabel_mult, new XYConstraints(313, 154, 11, -1));
        this.add(daiLabel_vendorDiscount, new XYConstraints(337, 135, -1, -1));

        this.add(ID_TEXT_FIELD, new XYConstraints(83, 38, 179, -1));
        this.add(fieldControl_standardDesc, new XYConstraints(83, 64, 338, -1));
        this.add(fieldControl_vendorId, new XYConstraints(83, 88, 138, -1));
        this.add(daiTextField_vendorName, new XYConstraints(225, 88, 290, -1));
        this.add(fieldControl_salesPrice, new XYConstraints(83, 123, -1, -1));
        this.add(fieldControl_purchPrice, new XYConstraints(83, 153, -1, -1));
        this.add(daiNumField_vendorRetailPrice, new XYConstraints(204, 153, -1, -1));
        this.add(daiComboBox_discountPct, new XYConstraints(341, 151, 45, -1));
        this.add(groupBox_inventory, new XYConstraints(17, 182, 569, 59));
        this.add(groupBox_accts, new XYConstraints(17, 245, 565, 106));
        this.add(daiTextArea_note, new XYConstraints(83, 355, 406, 76));
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        itemObj obj = new itemObj();
        itemObj tempObj = (itemObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_item_type(tempObj.get_item_type());
		obj.set_locality(tempObj.get_locality());

                Calendar now = Calendar.getInstance();
                String _dateModified = now.get(now.MONTH)+1 + "/" +
						 now.get(now.DAY_OF_MONTH) + "/" +
						 now.get(now.YEAR);
                obj.set_date_modified(_dateModified);
        return obj;
    }

	public int refresh()
	{
		//Call the base class method first.
		super.refresh();

		//Enable the ID text field.
        ID_TEXT_FIELD.setText(null);
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

	protected void update_UI(BusinessObject bobj)
	{
		itemObj obj = (itemObj)bobj;

		daiUserIdDateCreatedPanel.setUserId(obj.get_created_by());
		daiUserIdDateCreatedPanel.setDateCreated(obj.get_date_created());
                daiUserIdDateCreatedPanel.setDateModified(obj.get_date_modified());

		ID_TEXT_FIELD.setText(obj.get_id());
		fieldControl_salesPrice.setText(obj.get_sales_price());
		fieldControl_standardDesc.setText(obj.get_standard_desc());
		fieldControl_vendorId.setText(obj.get_vendor_id());
        daiTextField_vendorName.setText(obj.get_vendor_name());
		fieldControl_qtyOnHand.setText(obj.get_onhand_qty());
		fieldControl_qtyCustBackOrd.setText(obj.get_backorder_to_cust_qty());
		fieldControl_qtyVendorBackOrd.setText(obj.get_backorder_to_vendor_qty());
        daiTextField_salesAcctId.setText(obj.get_sales_acct_id());
        daiTextField_salesAcctName.setText(obj.get_sales_acct_name());
        daiTextField_purchAcctId.setText(obj.get_purch_acct_id());
        daiTextField_purchAcctName.setText(obj.get_purch_acct_name());
        daiTextField_inventoryAcctId.setText(obj.get_inventory_acct_id());
        daiTextField_inventoryAcctName.setText(obj.get_inventory_acct_name());
        daiComboBox_discountPct.setText(obj.get_vendor_discount_pct());
        daiTextArea_note.setText(obj.get_note1());
        daiNumField_vendorRetailPrice.setText(obj.get_vendor_retail_price());
        //!!purchPrice must be last because it is calculated when adding other
        //!!fields.  See the updatePurchPrice() method.
		fieldControl_purchPrice.setText(obj.get_purchase_price());

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
                daiUserIdDateCreatedPanel.setDateModified();
		itemObj obj = (itemObj)BUSINESS_OBJ;

		obj.set_id(ID_TEXT_FIELD.getText());
                obj.set_date_modified(daiUserIdDateCreatedPanel.getDateModified());
		obj.set_sales_price(fieldControl_salesPrice.getText());
		obj.set_standard_desc(fieldControl_standardDesc.getText());
		obj.set_purchase_price(fieldControl_purchPrice.getText());
		obj.set_vendor_id(fieldControl_vendorId.getText());
        obj.set_vendor_name(daiTextField_vendorName.getText());
		obj.set_onhand_qty(fieldControl_qtyOnHand.getText());
		obj.set_backorder_to_cust_qty(fieldControl_qtyCustBackOrd.getText());
		obj.set_backorder_to_vendor_qty(fieldControl_qtyVendorBackOrd.getText());
		obj.set_sales_acct_id(daiTextField_salesAcctId.getText());
		obj.set_sales_acct_name(daiTextField_salesAcctName.getText());
		obj.set_purch_acct_id(daiTextField_purchAcctId.getText());
		obj.set_purch_acct_name(daiTextField_purchAcctName.getText());
		obj.set_inventory_acct_id(daiTextField_inventoryAcctId.getText());
		obj.set_inventory_acct_name(daiTextField_inventoryAcctName.getText());
        obj.set_vendor_discount_pct(daiComboBox_discountPct.getText());
        obj.set_note1(daiTextArea_note.getText());
        obj.set_vendor_retail_price(daiNumField_vendorRetailPrice.getText());

		BUSINESS_OBJ = obj;
	}

    private void fieldControl_vendorId_daiActionEvent(daiActionEvent e) {
        copyVendorAttribs((vendorObj)e.getSource());
    }

    private void labelControl_vendorId_daiAction(daiActionEvent e)
    {
		vendorObj tempObj = new vendorObj();
        String id = fieldControl_vendorId.getText();
        String name = daiTextField_vendorName.getText();

        DBAttributes attrib1 = new DBAttributes(vendorObj.ID, "Vendor Id", 100);
        DBAttributes attrib2 = new DBAttributes(vendorObj.NAME, name, "Vendor Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, " order by Name ");
        chooser.show();
        vendorObj chosenObj = (vendorObj)chooser.getChosenObj();
		chooser.dispose();
        copyVendorAttribs(chosenObj);
    }

    private void copyVendorAttribs(vendorObj obj) {
		boolean isObjNull = false;
		String id = fieldControl_vendorId.getText();
		if (obj == null) isObjNull = true;
		if (isObjNull && (id != null && id.trim().length() > 0)) {
			//Do nothing
		} else {
    		fieldControl_vendorId.setText(isObjNull ? null : obj.get_id());
	    	daiTextField_vendorName.setText(isObjNull ? null : obj.get_name());
		}
    }

    private void ID_TEXT_FIELD_focusLost(FocusEvent e)
    {
        if (e.isTemporary()) return;

   		String id = ID_TEXT_FIELD.getText();

		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
			//Disable the Trans ID text field.
			ID_TEXT_FIELD.setDisabled(true);

			if (CONTAINER_FRAME.callBackInsertNewId(id) == 0)
         {
            //set account defaults
            daiTextField_purchAcctId.setText("50110.00");
            daiTextField_salesAcctId.setText("40000.01");
            daiTextField_inventoryAcctId.setText("14000.00");
         }
		}
    }

    void daiLabel_purchAcctId_daiAction(daiActionEvent e)
    {
		accountObj obj = new accountObj();
        String id = daiTextField_purchAcctId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  obj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);

		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            daiTextField_purchAcctId.setText(chosenObj.get_id());
            daiTextField_purchAcctName.setText(chosenObj.get_description());
        }
		chooser.dispose();
    }

    void daiLabel_salesAcctId_daiAction(daiActionEvent e)
    {
		accountObj obj = new accountObj();
        String id = daiTextField_salesAcctId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  obj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);

		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            daiTextField_salesAcctId.setText(chosenObj.get_id());
            daiTextField_salesAcctName.setText(chosenObj.get_description());
        }
		chooser.dispose();
    }

    void daiLabel_inventoryAcctId_daiAction(daiActionEvent e)
    {
		accountObj obj = new accountObj();
        String id = daiTextField_inventoryAcctId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  obj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);

		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            daiTextField_inventoryAcctId.setText(chosenObj.get_id());
            daiTextField_inventoryAcctName.setText(chosenObj.get_description());
        }
		chooser.dispose();
    }

    private void updatePurchasePrice()
    {
        double d_purchPrice = 0.0;
        String s_discountPct = daiComboBox_discountPct.getText();
        if (s_discountPct == null || s_discountPct.length() == 0) s_discountPct = "0";
        String s_retailPrice = daiNumField_vendorRetailPrice.getText();
        if (s_retailPrice == null) s_retailPrice = "0.0";
        String s_purchPrice = fieldControl_purchPrice.getText();
        if (s_purchPrice == null) s_purchPrice = "0.0";

        if (s_discountPct == "0") {
            d_purchPrice = Double.parseDouble(s_purchPrice);
        } else {
            double discountPct = Double.parseDouble(s_discountPct) / 100;
            double retailPrice = Double.parseDouble(s_retailPrice);
            double discountAmt = retailPrice * discountPct;
            d_purchPrice = retailPrice - discountAmt;
        }

        fieldControl_purchPrice.setText(new Double(d_purchPrice).toString());
    }
}
