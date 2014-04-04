
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiDetailPanel;
import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.itemObj;
import dai.shared.businessObjs.purch_order_itemObj;
import dai.shared.csAdapters.csInventoryAdapter;
import dai.shared.csAdapters.csInventoryAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiAcctIdNamePanel;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCheckBox;
import daiBeans.daiComboBox;
import daiBeans.daiCurrencyField;
import daiBeans.daiDateField;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiQueryTextField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;


public class PurchOrderItemPanel extends daiDetailPanel
{
	daiQueryTextField fieldControl_itemId = new daiQueryTextField(new itemObj());
	daiCurrencyField fieldControl_subtotal = new daiCurrencyField();
	daiCurrencyField fieldControl_unitPrice = new daiCurrencyField();
	daiNumField fieldControl_qtyOrdered = new daiNumField();
	daiCurrencyField fieldControl_extndPrice = new daiCurrencyField();
	daiLabel labelControl_subtotal = new daiLabel("Subtotal:");
	daiLabel jLabel_itemId = new daiLabel("Item Id:");
	daiLabel jLabel_itemDesc = new daiLabel("Description:");
	daiLabel jLabel_unitPrice = new daiLabel("List Price:");
	daiLabel jLabel_qtyOrdered = new daiLabel("Qty Ordered:");
	daiLabel jLabel_extndPrice = new daiLabel("Extended Price:");
    daiLabel jLabel_qtyStock = new daiLabel("Qty For Stock:");
    daiLabel jLabel_qtyCust = new daiLabel("Qty For Cust:");

    daiAcctIdNamePanel acctPanel = new daiAcctIdNamePanel();
	daiTextArea textAreaControl_itemDesc = new daiTextArea();
    daiBeans.daiNumField daiNumField_qtyReceived = new daiBeans.daiNumField();
    daiBeans.daiLabel daiLabel_qtyReceived = new daiBeans.daiLabel();
    daiComboBox daiComboBox_discountPct;
    daiCurrencyField fieldControl_purchPrice = new daiCurrencyField();
    JPanel subtotalPanel = new JPanel();
    daiCheckBox isRepairFlag = new daiCheckBox("Is Repair");

    GroupBox groupBox_itemEntry = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();

    //Ids for grid column positions.
    int gridColItemId       = 2;
	int gridColitemDesc     = 3;
	int gridColUnitPrice    = 4;
	int gridColqtyOrdered   = 5;
	int gridColExtndPrice   = 6;
    int gridColqtyReceived  = 7;
    int gridColPurchPrice   = 8;
    int gridColDiscountPct  = 9;
    int gridColAcctId       = 10;
    int gridColAcctName     = 11;
    int gridColIsRpr        = 12;
    int gridColVenModel     = 13;
    int gridColVenPart      = 14;
    int gridColQtyCust      = 15;
    int gridColQtyStock     = 16;
    int gridColDateExpct    = 17;
    daiLabel daiLabel_discountPct = new daiLabel("Discount%");
    daiLabel daiLabel_x = new daiLabel("X");
    daiLabel daiLabel_purchPrice = new daiLabel("Purchase Price:");
    daiLabel daiLabel_eq = new daiLabel("=");
    BoxLayout2 boxLayout21 = new BoxLayout2();
    daiLabel daiLabel_venModel = new daiLabel();
    daiTextField daiTextField_venModel = new daiTextField();
    daiLabel daiLabel_venPart = new daiLabel();
    daiTextField daiTextField_venPart = new daiTextField();
    daiNumField daiNumField_qtyCust = new daiNumField();
    daiNumField daiNumField_qtyStock = new daiNumField();
    daiDateField daiDateField_dateExpected = new daiDateField();
    daiLabel daiLabel_dateExpected = new daiLabel("Date Expected:");


	public PurchOrderItemPanel(JFrame container, daiFrame parentFrame, purch_order_itemObj obj) {

		super(container, parentFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			LOGGER.logError(CONTAINER, "Could not initialize Item Panel.\n" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {

        Vector vect = new Vector();
        for (int i=0; i<101; i++)
        {
            vect.addElement(Integer.toString(i));
        }
        daiComboBox_discountPct = new daiComboBox(vect);
        daiComboBox_discountPct.setSelectedIndex(0);

        //Grid Setup Stuff
        //These local grid components are only necessary so that they
        //will show up in the IDE designer for this panel.
        daiGrid grid = GRID;
        daiGridController gridController = GRID_CONTROLLER;
        GRID.createColumns(18);
		GRID.setHeaderNames(new String[]{" ", "OBJ", "Item Id", "Description",
                    "Unit Price", "Qty Ordered", "Extd. Price", "Qty Received",
                    "Purch Price", "Discount%", "AcctId", "AcctName", "IsRpr",
                    "VenModel", "VenPart", "QtyCust", "QtyStock", "DateExpect"});
        groupBox_itemEntry.setBackground(daiColors.PanelColor);
        groupBox_itemEntry.setLabel("Item Entry");
        groupBox_itemEntry.setLayout(xYLayout1);
        //Clear out the Grid control
		GRID.removeAllRows();
        GRID.hideColumn(1); //Hide the OBJ col.
        GRID.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);
        GRID.setColumnSize(1, daiGrid.DEFAULT_ID_COL_WIDTH+75);
        GRID.setColumnSize(2, daiGrid.DEFAULT_DESC_COL_WIDTH+100);

		fieldControl_qtyOrdered.addFocusListener(new java.awt.event.FocusAdapter(){
												 public void focusLost(FocusEvent e){
												 fieldControl_qtyOrdered_focusLost(e);}});
		fieldControl_unitPrice.addFocusListener(new java.awt.event.FocusAdapter(){
												public void focusLost(FocusEvent e){
                                                updatePurchasePrice();
												calcExtendedPrice();}});
		fieldControl_purchPrice.addFocusListener(new java.awt.event.FocusAdapter(){
												public void focusLost(FocusEvent e){
                                                updatePurchasePrice();
												calcExtendedPrice();}});
		jLabel_itemId.setHREFstyle(true);
        jLabel_itemId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                jLabel_itemId_daiAction(e);
            }
        });

        fieldControl_itemId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                fieldControl_itemId_daiActionEvent(e);
            }
        });

        daiLabel_qtyReceived.setPreferredSize(new Dimension(100, 16));
        daiLabel_qtyReceived.setMinimumSize(new Dimension(47, 16));
        daiLabel_qtyReceived.setText("Qty Received:");
        daiComboBox_discountPct.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e) {
                if (daiComboBox_discountPct.isDisabled()) return;
                updatePurchasePrice();
                calcExtendedPrice();
            }
        });

        daiLabel_venModel.setText("Vendor Model:");
        daiTextField_venModel.setText("");
        daiLabel_venPart.setText("Vendor Part#:");
        daiTextField_venPart.setText("");
        jLabel_itemDesc.setText("Vendor Desc:");
        grid.setPreferredSize(new Dimension(604, 100));
        grid.setMinimumSize(new Dimension(604, 100));
        subtotalPanel.add(labelControl_subtotal);
        subtotalPanel.add(fieldControl_subtotal);
        subtotalPanel.add(daiLabel_qtyReceived, null);
        subtotalPanel.add(daiNumField_qtyReceived, null);
        subtotalPanel.setBackground(daiColors.PanelColor);
		fieldControl_subtotal.setDisabled(true);
        daiNumField_qtyReceived.setDisabled(true);
        fieldControl_extndPrice.setDisabled(true);

        boxLayout21.setAxis(BoxLayout.Y_AXIS);

		this.setLayout(boxLayout21);
        this.add(groupBox_itemEntry, null);
        groupBox_itemEntry.add(jLabel_qtyOrdered, new XYConstraints(406, 90, -1, -1));
        groupBox_itemEntry.add(jLabel_qtyCust, new XYConstraints(404, 115, -1, -1));
        groupBox_itemEntry.add(jLabel_qtyStock, new XYConstraints(401, 140, -1, -1));
        groupBox_itemEntry.add(jLabel_extndPrice, new XYConstraints(394, 165, -1, -1));
        groupBox_itemEntry.add(jLabel_unitPrice, new XYConstraints(322, 45, -1, -1));
        groupBox_itemEntry.add(daiLabel_purchPrice, new XYConstraints(481, 45, -1, -1));
        groupBox_itemEntry.add(daiLabel_discountPct, new XYConstraints(406, 45, -1, -1));
        groupBox_itemEntry.add(daiLabel_eq, new XYConstraints(462, 61, -1, -1));
        groupBox_itemEntry.add(daiLabel_x, new XYConstraints(400, 61, -1, -1));
        groupBox_itemEntry.add(gridController, new XYConstraints(-5, 0, 334, 34));
        groupBox_itemEntry.add(fieldControl_itemId, new XYConstraints(76, 34, 185, -1));
        groupBox_itemEntry.add(daiTextField_venModel, new XYConstraints(76, 59, 185, -1));
        groupBox_itemEntry.add(daiTextField_venPart, new XYConstraints(76, 82, 185, -1));
        groupBox_itemEntry.add(fieldControl_unitPrice, new XYConstraints(298, 60, -1, -1));
        groupBox_itemEntry.add(daiComboBox_discountPct, new XYConstraints(411, 60, 45, -1));
        groupBox_itemEntry.add(fieldControl_purchPrice, new XYConstraints(473, 60, -1, -1));
        groupBox_itemEntry.add(fieldControl_qtyOrdered, new XYConstraints(473, 88, -1, -1));
        groupBox_itemEntry.add(daiNumField_qtyCust, new XYConstraints(473, 113, -1, -1));
        groupBox_itemEntry.add(daiNumField_qtyStock, new XYConstraints(473, 141, -1, -1));
        groupBox_itemEntry.add(isRepairFlag, new XYConstraints(475, 1, -1, -1));
        groupBox_itemEntry.add(fieldControl_extndPrice, new XYConstraints(473, 166, -1, -1));
        groupBox_itemEntry.add(textAreaControl_itemDesc, new XYConstraints(76, 106, 305, 58));
        groupBox_itemEntry.add(daiDateField_dateExpected, new XYConstraints(76, 170, 117, -1));
        groupBox_itemEntry.add(acctPanel, new XYConstraints(-2, 190, -1, -1));
        groupBox_itemEntry.add(daiLabel_dateExpected, new XYConstraints(-4, 170, 74, -1));
        groupBox_itemEntry.add(jLabel_itemId, new XYConstraints(37, 36, -1, -1));
        groupBox_itemEntry.add(daiLabel_venModel, new XYConstraints(0, 60, -1, -1));
        groupBox_itemEntry.add(daiLabel_venPart, new XYConstraints(0, 81, 70, -1));
        groupBox_itemEntry.add(jLabel_itemDesc, new XYConstraints(3, 107, -1, -1));

        this.add(subtotalPanel, null);
        this.add(grid, null);

		//Clear out all the rows and entry fields.
		GRID.removeAllRows();
        disableEntryFields(true);
	}

    //The is an override from a base class.  This is necessary
    //so we can add some special Inventory Update logic after this
    //save has been done.
    public int persistPanelData()
    {
        //Call the base class method.
        super.persistPanelData();

        //Update the inventory for these line items.
        csInventoryAdapterFactory inventoryAdapterFactory = csInventoryAdapterFactory.getInstance();
        csInventoryAdapter inventoryAdapter = inventoryAdapterFactory.getInventoryAdapter();

        //Get the vector of purch_order_itemObj
        Vector vect = getActiveBusinessObjVector();
        //convert the vector to an array
        purch_order_itemObj[] objs = (purch_order_itemObj[])vect.toArray(new purch_order_itemObj[]{});
        //Update the inventory
        try {
            inventoryAdapter.postPurchOrderItemsToInventory(_sessionMeta.getClientServerSecurity(),
                                                            objs);
        } catch (Exception e) {
            LOGGER.logError(CONTAINER, "Could not update Inventory\n"+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return 0;
    }

	public Float getItemSubtotal()
	{
        calcSubtotal();
		String s_subtotal = fieldControl_subtotal.getText();
		Float f_subtotal;

		if (s_subtotal != null)
		{
			f_subtotal = new Float(s_subtotal);
		} else
		{
			f_subtotal = new Float(0);
		}

		return f_subtotal;
	}

    public String getItemId() {
        return fieldControl_itemId.getText();
    }

	protected BusinessObject getNewBusinessObjInstance()
	{
		purch_order_itemObj tempObj = (purch_order_itemObj)BUSINESS_OBJ;
		purch_order_itemObj obj = new purch_order_itemObj();

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

		return obj;
	}

	protected void clearEntryFields()
	{
		//Clear out the data entry fields.
		fieldControl_itemId.setText(null);
		textAreaControl_itemDesc.setText(null);
		fieldControl_qtyOrdered.setText(null);
		fieldControl_unitPrice.setText(null);
		fieldControl_extndPrice.setText(null);
		daiNumField_qtyReceived.setText(null);
        fieldControl_purchPrice.setText(null);
        daiComboBox_discountPct.setText(null);
        acctPanel.setAcctId(null);
        acctPanel.setAcctName(null);
        isRepairFlag.setSelected(false);;
        daiTextField_venModel.setText(null);
        daiTextField_venPart.setText(null);
        daiNumField_qtyCust.setText(null);
        daiNumField_qtyStock.setText(null);
        daiDateField_dateExpected.setText(null);

		//Set the focus to the item ID.
		fieldControl_itemId.requestFocus();
	}

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		purch_order_itemObj itemObj;

		//Delete all the detail rows.
		try
		{
			GRID.removeAllRows();
		} catch (Exception ex)
		{
			LOGGER.logError(CONTAINER, "Couldn't delete all detail rows.\n" +ex.getLocalizedMessage());
		}

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < objVect.size(); i++)
		{
			itemObj = (purch_order_itemObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, 2, itemObj.get_item_id());
			GRID.set(i, 3, itemObj.get_description1());
			GRID.set(i, gridColUnitPrice, itemObj.get_unit_price());
			GRID.set(i, 5, itemObj.get_qty_ordered());
			GRID.set(i, 6, itemObj.get_extended_price());
			GRID.set(i, 7, itemObj.get_qty_received());
			GRID.set(i, gridColPurchPrice, itemObj.get_purch_price());
			GRID.set(i, gridColDiscountPct, itemObj.get_discount_percent());
			GRID.set(i, gridColAcctId, itemObj.get_account_id());
			GRID.set(i, gridColAcctName, itemObj.get_account_name());
            GRID.set(i, gridColVenModel, itemObj.get_vendor_model());
            GRID.set(i, gridColVenPart, itemObj.get_vendor_item_id());
            GRID.set(i, gridColQtyStock, itemObj.get_qty_for_stock());
            GRID.set(i, gridColQtyCust, itemObj.get_qty_for_cust());
            GRID.set(i, gridColDateExpct, itemObj.get_date_item_expected());

            if (itemObj.get_is_item_repair() != null && itemObj.get_is_item_repair().equals("Y")) {
                GRID.set(i, gridColIsRpr, "Y");
            }
			GRID.set(i, OBJ_COL_POS, itemObj);
		}
		//Update the Subtotals
		calcSubtotal();
	}

	protected void copyEntryFieldsToGrid()
	{
        calcExtendedPrice();
        calcSubtotal();

		int row = GRID.getActiveRow();

		GRID.set(row, 2, fieldControl_itemId.getText());
		GRID.set(row, 3, textAreaControl_itemDesc.getText());
		GRID.set(row, gridColUnitPrice, fieldControl_unitPrice.getText());
		GRID.set(row, 5, fieldControl_qtyOrdered.getText());
		GRID.set(row, 6, fieldControl_extndPrice.getText());
		GRID.set(row, 7, daiNumField_qtyReceived.getText());
        GRID.set(row, gridColPurchPrice, fieldControl_purchPrice.getText());
        GRID.set(row, gridColDiscountPct, daiComboBox_discountPct.getText());
        GRID.set(row, gridColAcctId, acctPanel.getAcctId());
        GRID.set(row, gridColAcctName, acctPanel.getAcctName());
        GRID.set(row, gridColVenModel, daiTextField_venModel.getText());
        GRID.set(row, gridColVenPart, daiTextField_venPart.getText());
        GRID.set(row, gridColQtyStock, daiNumField_qtyStock.getText());
        GRID.set(row, gridColQtyCust, daiNumField_qtyCust.getText());
        GRID.set(row, gridColDateExpct, daiDateField_dateExpected.getText());
        if (isRepairFlag.isSelected()) {
            GRID.set(row, gridColIsRpr, "Y");
        }
		calcSubtotal();
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
		fieldControl_itemId.setText((String)GRID.get(row, 2));
		textAreaControl_itemDesc.setText((String)GRID.get(row, 3));
		fieldControl_unitPrice.setText((String)GRID.get(row, gridColUnitPrice));
		fieldControl_qtyOrdered.setText((String)GRID.get(row, 5));
		fieldControl_extndPrice.setText((String)GRID.get(row, 6));
		daiNumField_qtyReceived.setText((String)GRID.get(row, 7));
        String du = (String)GRID.get(row, gridColDiscountPct);
        if (du != null && du.trim().length() > 0) {
            double dDiscount = Double.parseDouble(du);
            daiComboBox_discountPct.setText(Integer.toString((int)dDiscount));
        } else {
            daiComboBox_discountPct.setText(null);
        }
        acctPanel.setAcctId((String)GRID.get(row, gridColAcctId));
        acctPanel.setAcctName((String)GRID.get(row, gridColAcctName));
        daiTextField_venModel.setText((String)GRID.get(row, gridColVenModel));
        daiTextField_venPart.setText((String)GRID.get(row, gridColVenPart));
        daiNumField_qtyStock.setText((String)GRID.get(row, gridColQtyStock));
        daiNumField_qtyCust.setText((String)GRID.get(row, gridColQtyCust));
        daiDateField_dateExpected.setText((String)GRID.get(row, gridColDateExpct));
        String rprFlag = (String)GRID.get(row, gridColIsRpr);
        if (rprFlag != null && rprFlag.equals("Y")) {
            isRepairFlag.setSelected(true);
        } else {
            isRepairFlag.setSelected(false);
        }
        //!!purchPrice must be last because it is calculated when adding other
        //!!fields.  See the updatePurchPrice() method.
        fieldControl_purchPrice.setText((String)GRID.get(row, gridColPurchPrice));
	}

	protected void updateBusinessObjFromGrid()
	{
		purch_order_itemObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (purch_order_itemObj)GRID.get(i, OBJ_COL_POS);

			tempObj.set_item_id((String)GRID.get(i, 2));
			tempObj.set_description1((String)GRID.get(i, 3));
			tempObj.set_unit_price((String)GRID.get(i, gridColUnitPrice));
			tempObj.set_qty_ordered((String)GRID.get(i, 5));
			tempObj.set_extended_price((String)GRID.get(i, 6));
			tempObj.set_qty_received((String)GRID.get(i, 7));
            tempObj.set_purch_price((String)GRID.get(i, gridColPurchPrice));
            tempObj.set_discount_percent((String)GRID.get(i, gridColDiscountPct));
            tempObj.set_account_id((String)GRID.get(i, gridColAcctId));
            tempObj.set_account_name((String)GRID.get(i, gridColAcctName));
            tempObj.set_is_item_repair((String)GRID.get(i, gridColIsRpr));
            tempObj.set_vendor_model((String)GRID.get(i, gridColVenModel));
            tempObj.set_vendor_item_id((String)GRID.get(i, gridColVenPart));
            tempObj.set_qty_for_cust((String)GRID.get(i, gridColQtyCust));
            tempObj.set_qty_for_stock((String)GRID.get(i, gridColQtyStock));
            tempObj.set_date_item_expected((String)GRID.get(i, gridColDateExpct));
			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

	void calcSubtotal()
	{
		Float extendedPrice;
		float subtotal = 0;
		String gridVal;

		for (int i=0; i < GRID.getRowCount(); i++)
		{
			gridVal = (String)GRID.get(i, gridColExtndPrice);
			if (gridVal != null && gridVal.length() > 0)
			{
				extendedPrice = new Float(gridVal);
				subtotal = subtotal + extendedPrice.floatValue();
			}
		}
		fieldControl_subtotal.setText(Float.toString(subtotal));
	}

	void calcExtendedPrice()
	{
		Float purchPrice;
		Long  qtyOrdered;
		if (fieldControl_purchPrice.getText() == null)
		{
			fieldControl_purchPrice.setText("0");
		}
		purchPrice = new Float (fieldControl_purchPrice.getText());

		if (fieldControl_qtyOrdered.getText() == null)
		{
			fieldControl_qtyOrdered.setText("0");
		}
		qtyOrdered = new Long(fieldControl_qtyOrdered.getText());

		float extendedPrice = (purchPrice.floatValue() * qtyOrdered.longValue());

		fieldControl_extndPrice.setText(Float.toString(extendedPrice));
	}

	void fieldControl_qtyOrdered_focusLost(FocusEvent e)
	{
        if (!e.isTemporary()) {
		    calcExtendedPrice();
            //We do this so that a calculation will work when
            //running the reports.
            if (daiNumField_qtyReceived.getText() == null) {
                daiNumField_qtyReceived.setText("0");
            }
            if (daiNumField_qtyCust.getText() == null) {
                daiNumField_qtyCust.setText("0");
            }
            if (daiNumField_qtyStock.getText() == null) {
                daiNumField_qtyStock.setText("0");
            }
        }
	}

	void jLabel_itemId_daiAction(daiActionEvent e)
	{
        String id = fieldControl_itemId.getText();

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DBAttributes attribs1 = new DBAttributes(itemObj.ID, id, "Item Id", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  new itemObj(),
                                              new DBAttributes[]{attribs1},
                                              null, null);

		chooser.setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		itemObj chosenObj = (itemObj)chooser.getChosenObj();
		chooser.dispose();
        copyItemAttribs(chosenObj);
	}

    private void fieldControl_itemId_daiActionEvent(daiActionEvent e) {
        copyItemAttribs((itemObj)e.getSource());
    }

    private void copyItemAttribs(itemObj obj) {
		boolean isObjNull = false;
		String id = fieldControl_itemId.getText();
		if (obj == null) isObjNull = true;
		if (isObjNull && id != null)
		{
			//Do nothing
		} else
		{
    		fieldControl_itemId.setText(isObjNull ? null : obj.get_id());
	    	textAreaControl_itemDesc.setText(isObjNull ? null : obj.get_vendor_item_desc());
		    fieldControl_unitPrice.setText(isObjNull ? null : obj.get_vendor_retail_price());
            fieldControl_purchPrice.setText(isObjNull ? null : obj.get_purchase_price());
            daiComboBox_discountPct.setText(isObjNull ? null : obj.get_vendor_discount_pct());
            acctPanel.setAcctId(isObjNull ? null : obj.get_purch_acct_id());
            acctPanel.setAcctName(isObjNull ? null : obj.get_purch_acct_name());
            daiTextField_venModel.setText(isObjNull ? null: obj.get_vendor_item_model());
            daiTextField_venPart.setText(isObjNull ? null: obj.get_vendor_item_id());
		}
    }

    protected void disableEntryFields(boolean disable)
    {
        fieldControl_itemId.setDisabled(disable);
    	textAreaControl_itemDesc.setDisabled(disable);
	    fieldControl_qtyOrdered.setDisabled(disable);
		fieldControl_unitPrice.setDisabled(disable);
	    fieldControl_purchPrice.setDisabled(disable);
        acctPanel.setDisabled(disable);
        daiComboBox_discountPct.setDisabled(disable);
        isRepairFlag.setDisabled(disable);
        jLabel_itemId.setDisabled(disable);
        daiTextField_venModel.setDisabled(disable);
        daiTextField_venPart.setDisabled(disable);
        daiNumField_qtyStock.setDisabled(disable);
        daiNumField_qtyCust.setDisabled(disable);
        daiDateField_dateExpected.setDisabled(disable);
    }

    private void updatePurchasePrice()
    {
        double d_purchPrice = 0.0;
        String s_discountPct = daiComboBox_discountPct.getText();
        if (s_discountPct == null || s_discountPct.length() == 0) s_discountPct = "0";
        String s_retailPrice = fieldControl_unitPrice.getText();
        if (s_retailPrice == null) s_retailPrice = "0.0";
        String s_purchPrice = fieldControl_purchPrice.getText();
        if (s_purchPrice == null) s_purchPrice = "0.0";

        if (s_discountPct == "0") {
            d_purchPrice  = Double.parseDouble(s_purchPrice);
        } else {
            double discountPct = Double.parseDouble(s_discountPct) / 100;
            double retailPrice = Double.parseDouble(s_retailPrice);
            double discountAmt = retailPrice * discountPct;
            d_purchPrice  = retailPrice - discountAmt;
        }

        fieldControl_purchPrice.setText(new Double(d_purchPrice).toString());
    }
}

