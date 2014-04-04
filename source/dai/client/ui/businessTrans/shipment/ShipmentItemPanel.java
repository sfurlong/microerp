
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
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
import dai.shared.businessObjs.shipment_itemObj;
import dai.shared.csAdapters.csInventoryAdapter;
import dai.shared.csAdapters.csInventoryAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiAcctIdNamePanel;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCheckBox;
import daiBeans.daiCurrencyField;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;

public class ShipmentItemPanel extends daiDetailPanel
{

	daiTextField fieldControl_itemId = new daiTextField();
	daiCurrencyField fieldControl_subtotal = new daiCurrencyField();
	daiCurrencyField fieldControl_unitPrice = new daiCurrencyField();
	daiCurrencyField fieldControl_extndPrice = new daiCurrencyField();
	daiNumField fieldControl_qtyOrdered = new daiNumField();
	daiLabel labelControl_subtotal = new daiLabel();
	daiLabel jLabel_itemId = new daiLabel();
	daiLabel jLabel_itemDesc = new daiLabel();
	daiLabel jLabel_unitPrice = new daiLabel();
	daiLabel jLabel_qtyOrdered = new daiLabel();
	daiLabel jLabel_extndPrice = new daiLabel();
	daiTextArea textAreaControl_itemDesc = new daiTextArea();
    daiBeans.daiNumField daiNumField_qtyShipped = new daiBeans.daiNumField();
    daiBeans.daiLabel daiLabel_qtyShipped = new daiBeans.daiLabel();
    daiCheckBox checkBox_insideRepair = new daiCheckBox("Is Inside Repair");
    daiCheckBox checkBox_outsideRepair = new daiCheckBox("Is Outside Repair");
    daiAcctIdNamePanel acctPanel = new daiAcctIdNamePanel();

    GroupBox groupBox_itemEntry = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();

    //Ids for grid column positions.
    int gridColItemId       = 2;
	int gridColitemDesc     = 3;
	int gridColunitPrice    = 4;
	int gridColqtyOrdered   = 5;
	int gridColextndPrice   = 6;
    int gridColqtyShipped   = 7;
    int gridColInRepair     = 8;
    int gridColOutRepair    = 9;
    int gridColAcctId       = 10;
    int gridColAcctName     = 11;
    int gridColRepairCost   = 12;

    JPanel jPanel_subtotals = new JPanel();
    BoxLayout2 boxLayout21 = new BoxLayout2();
    daiLabel daiLabel_repairCost = new daiLabel();
    daiCurrencyField daiCurrencyField_repairCost = new daiCurrencyField();

	public ShipmentItemPanel(JFrame container, daiFrame parentFrame, shipment_itemObj obj) {

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

        //Grid Setup Stuff
        //These local grid components are only necessary so that they
        //will show up in the IDE designer for this panel.
        daiGrid grid = GRID;
        daiGridController gridController = GRID_CONTROLLER;
        GRID.createColumns(13);
		GRID.setHeaderNames(new String[]{" ", "OBJ", "Item Id", "Description", "Unit Price",
                    "Qty Ordered", "Extd. Price", "Qty Shipped", "inRpr", "outRpr",
                    "AcctId", "AcctName", "Repair Cost"});
        groupBox_itemEntry.setBackground(daiColors.PanelColor);
        groupBox_itemEntry.setLabel("Item Entry");
        groupBox_itemEntry.setLayout(xYLayout1);
        //Clear out the Grid control
		GRID.removeAllRows();
        GRID.hideColumn(1);
        GRID.setColumnSize(0, GRID.DEFAULT_ITEM_NUM_COL_WIDTH);
        GRID.setColumnSize(1, GRID.DEFAULT_ID_COL_WIDTH);
        GRID.setColumnSize(2, GRID.DEFAULT_DESC_COL_WIDTH);

		this.setLayout(boxLayout21);

		fieldControl_subtotal.setEditable(false);

		labelControl_subtotal.setText("Subtotal:");
		fieldControl_qtyOrdered.addFocusListener(new java.awt.event.FocusAdapter(){
												 public void focusLost(FocusEvent e){
												 fieldControl_qtyOrdered_focusLost(e);}});
		daiNumField_qtyShipped.addFocusListener(new java.awt.event.FocusAdapter(){
												 public void focusLost(FocusEvent e){
												 daiNumField_qtyShipped_focusLost(e);}});
		fieldControl_unitPrice.addFocusListener(new java.awt.event.FocusAdapter(){
												public void focusLost(FocusEvent e){
												fieldControl_unitPrice_focusLost(e);}});
		jLabel_itemId.setText("Item Id:");
		jLabel_itemId.setHREFstyle(true);
        jLabel_itemId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                jLabel_itemId_daiAction(e);
            }
        });

        jLabel_itemDesc.setText("Description:");
        fieldControl_qtyOrdered.setDisabled(true);
        daiLabel_qtyShipped.setText("Qty Shipped:");
		jLabel_unitPrice.setText("Unit Price:");
		jLabel_qtyOrdered.setText("Qty Ordered:");
		jLabel_extndPrice.setText("Extended Price:");

        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        jPanel_subtotals.setBackground(daiColors.PanelColor);

        grid.setMinimumSize(new Dimension(594, 100));
        grid.setPreferredSize(new Dimension(594, 100));

        daiLabel_repairCost.setText("Repair Cost:");
        checkBox_outsideRepair.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                checkBox_outsideRepair_actionPerformed(e);
            }
        });
        groupBox_itemEntry.add(gridController, new XYConstraints(11, 8, 337, -1));
        groupBox_itemEntry.add(jLabel_itemId, new XYConstraints(24, 54, -1, -1));
        groupBox_itemEntry.add(fieldControl_itemId, new XYConstraints(66, 55, 286, -1));
        groupBox_itemEntry.add(jLabel_unitPrice, new XYConstraints(390, 61, -1, -1));
        groupBox_itemEntry.add(fieldControl_unitPrice, new XYConstraints(458, 59, -1, -1));
        groupBox_itemEntry.add(jLabel_qtyOrdered, new XYConstraints(375, 87, -1, -1));
        groupBox_itemEntry.add(fieldControl_qtyOrdered, new XYConstraints(458, 88, -1, -1));
        groupBox_itemEntry.add(daiNumField_qtyShipped, new XYConstraints(458, 118, -1, -1));
        groupBox_itemEntry.add(daiLabel_qtyShipped, new XYConstraints(376, 118, -1, -1));
        groupBox_itemEntry.add(jLabel_extndPrice, new XYConstraints(363, 144, -1, -1));
        groupBox_itemEntry.add(fieldControl_extndPrice, new XYConstraints(458, 147, -1, -1));
        groupBox_itemEntry.add(jLabel_itemDesc, new XYConstraints(0, 82, -1, -1));
        groupBox_itemEntry.add(textAreaControl_itemDesc, new XYConstraints(66, 84, 287, 88));
        groupBox_itemEntry.add(checkBox_insideRepair, new XYConstraints(66, 173, -1, -1));
        groupBox_itemEntry.add(daiCurrencyField_repairCost, new XYConstraints(458, 175, -1, -1));
        groupBox_itemEntry.add(daiLabel_repairCost, new XYConstraints(362, 177, 75, -1));
        groupBox_itemEntry.add(acctPanel, new XYConstraints(16, 200, 438, 25));
        groupBox_itemEntry.add(checkBox_outsideRepair, new XYConstraints(255, 173, 108, -1));

        jPanel_subtotals.add(labelControl_subtotal, null);
        jPanel_subtotals.add(fieldControl_subtotal, null);

        this.setBackground(daiColors.PanelColor);
        this.add(groupBox_itemEntry, null);
        this.add(jPanel_subtotals, null);
        this.add(grid, null);

		//Clear out all the rows and entry fields.
        disableEntryFields(true);
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

    //The is an override from a base class.  This is necessary
    //so we can add some special Inventory Update logic after this
    //save has been done.
    public int persistPanelData()
    {
        //Call the base class method.
        super.persistPanelData();

        //Update the inventory for these line items. But only if this
        //shipment is NOT actually a credit memo.
        if (!((ShipmentFrame)CONTAINER_FRAME).isCreditMemo()) {
            csInventoryAdapterFactory inventoryAdapterFactory = null;
            inventoryAdapterFactory = inventoryAdapterFactory.getInstance();

            csInventoryAdapter invoiceAdapter = null;
            invoiceAdapter = inventoryAdapterFactory.getInventoryAdapter();

            //Get the vector of shipment_itemObj
            Vector vect = getActiveBusinessObjVector();
            //convert the vector to an array
            shipment_itemObj[] objs = (shipment_itemObj[])vect.toArray(new shipment_itemObj[]{});
            //Update the inventory
            try {
                invoiceAdapter.postShipmentItemsToInventory(_sessionMeta.getClientServerSecurity(),
                                                        objs);
            } catch (Exception e) {
                LOGGER.logError(CONTAINER, "Could not update Inventory\n"+e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        return 0;
    }

    public String getItemId() {
        return fieldControl_itemId.getText();
    }

	protected BusinessObject getNewBusinessObjInstance()
	{
		shipment_itemObj tempObj = (shipment_itemObj)BUSINESS_OBJ;
		shipment_itemObj obj = new shipment_itemObj();

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
		fieldControl_subtotal.setText(null);
        checkBox_insideRepair.setSelected(false);
        checkBox_outsideRepair.setSelected(false);
        acctPanel.setAcctId(null);
        acctPanel.setAcctName(null);
        daiNumField_qtyShipped.setText(null);
        daiCurrencyField_repairCost.setText(null);

		//Set the focus to the item ID.
		fieldControl_itemId.requestFocus();
	}

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		shipment_itemObj itemObj;

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
			itemObj = (shipment_itemObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, 2, itemObj.get_item_id());
			GRID.set(i, 3, itemObj.get_description1());
			GRID.set(i, 4, itemObj.get_unit_price());
			GRID.set(i, 5, itemObj.get_qty_ordered());
			GRID.set(i, 6, itemObj.get_extended_price());
			GRID.set(i, 7, itemObj.get_qty_shipped());
            GRID.set(i, gridColInRepair, itemObj.get_is_internal_repair());
            GRID.set(i, gridColOutRepair, itemObj.get_is_external_repair());
            GRID.set(i, gridColAcctId, itemObj.get_account_id());
            GRID.set(i, gridColAcctName, itemObj.get_account_name());
            GRID.set(i, gridColRepairCost, itemObj.get_outside_repair_cost());

			GRID.set(i, OBJ_COL_POS, itemObj);
		}
		//Update the Subtotals
		calcSubtotal();
	}

	protected void copyEntryFieldsToGrid()
	{
		int row = GRID.getActiveRow();

		GRID.set(row, 2, fieldControl_itemId.getText());
		GRID.set(row, 3, textAreaControl_itemDesc.getText());
		GRID.set(row, 4, fieldControl_unitPrice.getText());
		GRID.set(row, 5, fieldControl_qtyOrdered.getText());
		GRID.set(row, 6, fieldControl_extndPrice.getText());
		GRID.set(row, 7, daiNumField_qtyShipped.getText());
		GRID.set(row, gridColAcctId, acctPanel.getAcctId());
		GRID.set(row, gridColAcctName, acctPanel.getAcctName());
        if (checkBox_insideRepair.isSelected()) {
            GRID.set(row, gridColInRepair, "Y");
        } else {
            GRID.set(row, gridColInRepair, "N");
        }
        if (checkBox_outsideRepair.isSelected()) {
            GRID.set(row, gridColOutRepair, "Y");
        } else {
            GRID.set(row, gridColOutRepair, "N");
        }

		calcSubtotal();
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
		fieldControl_itemId.setText((String)GRID.get(row, 2));
		textAreaControl_itemDesc.setText((String)GRID.get(row, 3));
		fieldControl_unitPrice.setText((String)GRID.get(row, 4));
		fieldControl_qtyOrdered.setText((String)GRID.get(row, 5));
		fieldControl_extndPrice.setText((String)GRID.get(row, 6));
		daiNumField_qtyShipped.setText((String)GRID.get(row, 7));
		acctPanel.setAcctId((String)GRID.get(row, gridColAcctId));
		acctPanel.setAcctName((String)GRID.get(row, gridColAcctName));
        daiCurrencyField_repairCost.setText((String)GRID.get(row, gridColRepairCost));
        String inRpr = (String)GRID.get(row, gridColInRepair);
        if (inRpr != null && inRpr.equals("Y")) {
            checkBox_insideRepair.setSelected(true);
        } else {
            checkBox_insideRepair.setSelected(false);
        }
        String outRpr = (String)GRID.get(row, gridColOutRepair);
        if (outRpr != null && outRpr.equals("Y")) {
            checkBox_outsideRepair.setSelected(true);
        } else {
            checkBox_outsideRepair.setSelected(false);
        }
	}

	protected void updateBusinessObjFromGrid()
	{
		shipment_itemObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (shipment_itemObj)GRID.get(i, OBJ_COL_POS);

			tempObj.set_item_id((String)GRID.get(i, 2));
			tempObj.set_description1((String)GRID.get(i, 3));
			tempObj.set_unit_price((String)GRID.get(i, 4));
			tempObj.set_qty_ordered((String)GRID.get(i, 5));
			tempObj.set_extended_price((String)GRID.get(i, 6));
			tempObj.set_qty_shipped((String)GRID.get(i, 7));
            tempObj.set_is_internal_repair((String)GRID.get(i, gridColInRepair));
            tempObj.set_is_external_repair((String)GRID.get(i, gridColOutRepair));
            tempObj.set_account_id((String)GRID.get(i, gridColAcctId));
            tempObj.set_account_name((String)GRID.get(i, gridColAcctName));
            tempObj.set_outside_repair_cost((String)GRID.get(i, gridColRepairCost));

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
			gridVal = (String)GRID.get(i, 6);
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
        String s_unitPrice = fieldControl_unitPrice.getText();
        String s_qtyShipped = daiNumField_qtyShipped.getText();
        if (s_qtyShipped == null) s_qtyShipped = "0";
        if (s_unitPrice == null) s_unitPrice = "0.0";

		double extendedPrice = Double.parseDouble(s_unitPrice) * Integer.parseInt(s_qtyShipped);

		fieldControl_extndPrice.setText(Double.toString(extendedPrice));
	}

	void fieldControl_unitPrice_focusLost(FocusEvent e)
	{
		calcExtendedPrice();
	}

	void fieldControl_qtyOrdered_focusLost(FocusEvent e)
	{
		calcExtendedPrice();
	}

	void daiNumField_qtyShipped_focusLost(FocusEvent e)
	{
		calcExtendedPrice();
	}

	void jLabel_itemId_daiAction(daiActionEvent e)
	{
		itemObj tempObj = new itemObj();
        String id = fieldControl_itemId.getText();

        DBAttributes attrib = new DBAttributes(itemObj.ID, "Item Id", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib},
                                              null, null);
		chooser.show();
        itemObj chosenObj = (itemObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_itemId.setText(chosenObj.get_id());
            textAreaControl_itemDesc.setText(chosenObj.get_standard_desc());
            fieldControl_unitPrice.setText(chosenObj.get_sales_price());
            acctPanel.setAcctId(chosenObj.get_sales_acct_id());
            acctPanel.setAcctName(chosenObj.get_sales_acct_name());
        }
    	chooser.dispose();
	}

    protected void disableEntryFields(boolean flag)
    {
        fieldControl_itemId.setDisabled(flag);
    	textAreaControl_itemDesc.setDisabled(flag);
		fieldControl_unitPrice.setDisabled(flag);
    	fieldControl_extndPrice.setDisabled(flag);
	    fieldControl_subtotal.setDisabled(flag);
        daiNumField_qtyShipped.setDisabled(flag);
        jLabel_itemId.setDisabled(flag);
        checkBox_insideRepair.setDisabled(flag);
        checkBox_outsideRepair.setDisabled(flag);
        acctPanel.setDisabled(flag);
        if (flag == false && checkBox_outsideRepair.isSelected()) {
            daiCurrencyField_repairCost.setDisabled(false);
        } else {
            daiCurrencyField_repairCost.setDisabled(true);
        }

    }

    void checkBox_outsideRepair_actionPerformed(ActionEvent e)
    {
        if (checkBox_outsideRepair.isSelected()) {
            daiCurrencyField_repairCost.setDisabled(false);
        } else {
            daiCurrencyField_repairCost.setText(null);
            daiCurrencyField_repairCost.setDisabled(true);
        }
    }
}

