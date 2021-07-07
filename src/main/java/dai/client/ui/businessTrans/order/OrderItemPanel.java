
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.order;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiDetailPanel;
import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.cust_order_itemObj;
import dai.shared.businessObjs.itemObj;
import dai.shared.csAdapters.csInventoryAdapter;
import dai.shared.csAdapters.csInventoryAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiAcctIdNamePanel;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCheckBox;
import daiBeans.daiCurrencyField;
import daiBeans.daiDateField;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiQueryTextField;
import daiBeans.daiTextArea;


public class OrderItemPanel extends daiDetailPanel
{
	daiQueryTextField fieldControl_itemId = new daiQueryTextField(new itemObj());
	daiCurrencyField fieldControl_subtotal = new daiCurrencyField();
	daiCurrencyField fieldControl_unitPrice = new daiCurrencyField();
	daiNumField fieldControl_qtyOrdered = new daiNumField();
	daiCurrencyField fieldControl_extndPrice = new daiCurrencyField();
	daiLabel labelControl_subtotal = new daiLabel("Subtotal:");
	daiLabel jLabel_itemId = new daiLabel("Item Id:");
	daiLabel jLabel_itemDesc = new daiLabel("Description:");
	daiLabel jLabel_unitPrice = new daiLabel("Unit Price:");
	daiLabel jLabel_qtyOrdered = new daiLabel("Qty Ordered:");
	daiLabel jLabel_extndPrice = new daiLabel("Extended Price:");
    daiTextArea textAreaControl_itemDesc = new daiTextArea();
    daiNumField daiNumField_qtyShipped = new daiBeans.daiNumField();
    daiLabel daiLabel_qtyShipped = new daiLabel("Qty Shipped:");
    daiCheckBox checkBox_insideRepair = new daiCheckBox("Is Inside Repair");
    daiCheckBox checkBox_outsideRepair = new daiCheckBox("Is Outside Repair");
    daiLabel daiLabel_shipDate = new daiLabel("Expected Date:");
    daiDateField daiDateField_shipDate = new daiDateField();

    GroupBox groupBox_itemEntry = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();

    JPopupMenu _popupMenu = new JPopupMenu();

    //Ids for grid column positions.
    int gridColItemId       = 2;
	int gridColItemDesc     = 3;
	int gridColUnitPrice    = 4;
	int gridColQtyOrdered   = 5;
	int gridColExtndPrice   = 6;
    int gridColQtyShipped   = 7;
    int gridColInRepair     = 8;
    int gridColOutRepair    = 9;
    int gridColAcctId       = 10;
    int gridColAcctName     = 11;
    int gridColShipDate     = 12;
    int gridColRepairCost   = 13;

    JPanel jPanel_subtotals = new JPanel();
    BoxLayout2 boxLayout21 = new BoxLayout2();
    daiAcctIdNamePanel acctPanel = new daiAcctIdNamePanel();
    daiLabel daiLabel_repairCost = new daiLabel("Repair Cost:");
    daiCurrencyField daiCurrencyField_repairCost = new daiCurrencyField();

	public OrderItemPanel(JFrame container, daiFrame parentFrame, cust_order_itemObj obj) {

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
        GRID.createColumns(14);
		GRID.setHeaderNames(new String[]{" ", "OBJ", "Item Id", "Description",
                    "Unit Price", "Qty Ordered", "Extd. Price", "Qty Shipped",
                    "inRpr", "outRpr", "AcctId", "AcctName", "ShipDate", "RepairCost"});
        groupBox_itemEntry.setBackground(daiColors.PanelColor);
        groupBox_itemEntry.setLabel("Item Entry");
        groupBox_itemEntry.setLayout(xYLayout1);
        //Clear out the Grid control
		GRID.removeAllRows();
        GRID.hideColumn(1);  //Hide the Obj column
        //Resize some of the columns.
        GRID.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);
        GRID.setColumnSize(1, daiGrid.DEFAULT_ID_COL_WIDTH);
        GRID.setColumnSize(2, daiGrid.DEFAULT_DESC_COL_WIDTH);

		fieldControl_subtotal.setDisabled(true);

		fieldControl_qtyOrdered.addFocusListener(new java.awt.event.FocusAdapter(){
												 public void focusLost(FocusEvent e){
												 fieldControl_qtyOrdered_focusLost(e);}});
		fieldControl_unitPrice.addFocusListener(new java.awt.event.FocusAdapter(){
												public void focusLost(FocusEvent e){
												fieldControl_unitPrice_focusLost(e);}});
		jLabel_itemId.setHREFstyle(true);
        jLabel_itemId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e) {
                jLabel_itemId_daiActionEvent(e);
            }
        });
        fieldControl_itemId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                fieldControl_itemId_daiActionEvent(e);
            }
        });

        checkBox_outsideRepair.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                checkBox_outsideRepair_actionPerformed(e);
            }
        });

        daiNumField_qtyShipped.setDisabled(true);


		//Clear out all the rows and entry fields.
		GRID.removeAllRows();
        disableEntryFields(true);

        jPanel_subtotals.setBackground(daiColors.PanelColor);
        grid.setPreferredSize(new Dimension(589, 100));
        grid.setMinimumSize(new Dimension(589, 100));

        jPanel_subtotals.add(labelControl_subtotal, null);
        jPanel_subtotals.add(fieldControl_subtotal, null);

        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        groupBox_itemEntry.add(gridController, new XYConstraints(-4, 0, 337, -1));
        groupBox_itemEntry.add(jLabel_itemId, new XYConstraints(34, 50, -1, -1));
        groupBox_itemEntry.add(jLabel_itemDesc, new XYConstraints(10, 78, -1, -1));
        groupBox_itemEntry.add(jLabel_unitPrice, new XYConstraints(398, 52, -1, -1));
        groupBox_itemEntry.add(fieldControl_itemId, new XYConstraints(72, 50, 297, -1));
        groupBox_itemEntry.add(fieldControl_unitPrice, new XYConstraints(452, 50, -1, -1));
        groupBox_itemEntry.add(fieldControl_qtyOrdered, new XYConstraints(452, 75, -1, -1));
        groupBox_itemEntry.add(textAreaControl_itemDesc, new XYConstraints(72, 78, 297, 77));
        groupBox_itemEntry.add(daiNumField_qtyShipped, new XYConstraints(452, 101, -1, -1));
        groupBox_itemEntry.add(fieldControl_extndPrice, new XYConstraints(452, 129, -1, -1));
        groupBox_itemEntry.add(jLabel_extndPrice, new XYConstraints(371, 132, -1, -1));
        groupBox_itemEntry.add(jLabel_qtyOrdered, new XYConstraints(383, 79, -1, -1));
        groupBox_itemEntry.add(daiLabel_qtyShipped, new XYConstraints(384, 102, -1, -1));
        groupBox_itemEntry.add(checkBox_insideRepair, new XYConstraints(89, 159, -1, -1));
        groupBox_itemEntry.add(checkBox_outsideRepair, new XYConstraints(240, 159, 108, -1));
        groupBox_itemEntry.add(daiCurrencyField_repairCost, new XYConstraints(452, 159, -1, -1));
        groupBox_itemEntry.add(daiLabel_repairCost, new XYConstraints(371, 159, 75, -1));
        groupBox_itemEntry.add(daiLabel_shipDate, new XYConstraints(-9, 183, 76, 24));
        groupBox_itemEntry.add(daiDateField_shipDate, new XYConstraints(72, 187, -1, -1));
        groupBox_itemEntry.add(acctPanel, new XYConstraints(182, 182, 370, 24));

		this.setLayout(boxLayout21);
        this.add(groupBox_itemEntry, null);
        this.add(jPanel_subtotals, null);
        this.add(grid, null);
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

    //The is an override from a base class.  This is necessary
    //so we can add some special item BOM logic after this
    //query has been done.
  	public int query(String id) {
        //Call the base class method.
        super.query(id);
        return 0;
    }

    //The is an override from a base class.  This is necessary
    //so we can add some special Inventory Update logic after this
    //save has been done.
    public int persistPanelData()
    {
		//Call the base class method.
		super.persistPanelData();

        //Update the inventory for these line items.
        csInventoryAdapter inventoryAdapter =
                    csInventoryAdapterFactory.getInstance().getInventoryAdapter();

		//Get the vector of cust_order_itemObj
		Vector vect = getActiveBusinessObjVector();
		//convert the vector to an array
		cust_order_itemObj[] objs = (cust_order_itemObj[])vect.toArray(new cust_order_itemObj[]{});

		//Update the inventory
		try
		{
			inventoryAdapter.postCustOrderItemsToInventory(_sessionMeta.getClientServerSecurity(),
														 objs);
        } catch (Exception e) {
			e.printStackTrace();
            String msg = this.getClass().getName() + "::update failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
        }

        return 0;
    }

	protected BusinessObject getNewBusinessObjInstance()
	{
		cust_order_itemObj tempObj = (cust_order_itemObj)BUSINESS_OBJ;
		cust_order_itemObj obj = new cust_order_itemObj();

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
        daiDateField_shipDate.setText(null);
        daiCurrencyField_repairCost.setText(null);

		//Set the focus to the item ID.
		fieldControl_itemId.requestFocus();
	}

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		cust_order_itemObj itemObj;

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
			itemObj = (cust_order_itemObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, gridColItemId, itemObj.get_item_id());
			GRID.set(i, gridColItemDesc, itemObj.get_description1());
			GRID.set(i, gridColUnitPrice, itemObj.get_unit_price());
			GRID.set(i, gridColQtyOrdered, itemObj.get_qty_ordered());
			GRID.set(i, gridColExtndPrice, itemObj.get_extended_price());
			GRID.set(i, gridColQtyShipped, itemObj.get_qty_shipped());
            GRID.set(i, gridColInRepair, itemObj.get_is_internal_repair());
            GRID.set(i, gridColOutRepair, itemObj.get_is_external_repair());
            GRID.set(i, gridColAcctId, itemObj.get_account_id());
            GRID.set(i, gridColAcctName, itemObj.get_account_name());
            GRID.set(i, gridColShipDate, itemObj.get_expected_ship_date());
            GRID.set(i, gridColRepairCost, itemObj.get_outside_repair_cost());

			GRID.set(i, OBJ_COL_POS, itemObj);
		}
		//Update the Subtotals
		calcSubtotal();
	}

	protected void copyEntryFieldsToGrid()
	{
		int row = GRID.getActiveRow();

		GRID.set(row, gridColItemId, fieldControl_itemId.getText());
		GRID.set(row, gridColItemDesc, textAreaControl_itemDesc.getText());
		GRID.set(row, gridColUnitPrice, fieldControl_unitPrice.getText());
		GRID.set(row, gridColQtyOrdered, fieldControl_qtyOrdered.getText());
		GRID.set(row, gridColExtndPrice, fieldControl_extndPrice.getText());
		GRID.set(row, gridColQtyShipped, daiNumField_qtyShipped.getText());
		GRID.set(row, gridColAcctId, acctPanel.getAcctId());
		GRID.set(row, gridColAcctName, acctPanel.getAcctName());
        GRID.set(row, gridColShipDate, daiDateField_shipDate.getText());
        GRID.set(row, gridColRepairCost, daiCurrencyField_repairCost.getText());
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
		fieldControl_itemId.setText((String)GRID.get(row, gridColItemId));
		textAreaControl_itemDesc.setText((String)GRID.get(row, gridColItemDesc));
		fieldControl_unitPrice.setText((String)GRID.get(row, gridColUnitPrice));
		fieldControl_qtyOrdered.setText((String)GRID.get(row, gridColQtyOrdered));
		fieldControl_extndPrice.setText((String)GRID.get(row, gridColExtndPrice));
		daiNumField_qtyShipped.setText((String)GRID.get(row, gridColQtyShipped));
		acctPanel.setAcctId((String)GRID.get(row, gridColAcctId));
		acctPanel.setAcctName((String)GRID.get(row, gridColAcctName));
        daiDateField_shipDate.setText((String)GRID.get(row, gridColShipDate));
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
		cust_order_itemObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (cust_order_itemObj)GRID.get(i, OBJ_COL_POS);

			tempObj.set_item_id((String)GRID.get(i, gridColItemId));
			tempObj.set_description1((String)GRID.get(i, gridColItemDesc));
			tempObj.set_unit_price((String)GRID.get(i, gridColUnitPrice));
			tempObj.set_qty_ordered((String)GRID.get(i, gridColQtyOrdered));
			tempObj.set_extended_price((String)GRID.get(i, gridColExtndPrice));
			tempObj.set_qty_shipped((String)GRID.get(i, gridColQtyShipped));
            tempObj.set_is_internal_repair((String)GRID.get(i, gridColInRepair));
            tempObj.set_is_external_repair((String)GRID.get(i, gridColOutRepair));
            tempObj.set_account_id((String)GRID.get(i, gridColAcctId));
            tempObj.set_account_name((String)GRID.get(i, gridColAcctName));
            tempObj.set_expected_ship_date((String)GRID.get(i, gridColShipDate));
            tempObj.set_outside_repair_cost((String)GRID.get(i, gridColRepairCost));

			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

    protected void disableEntryFields(boolean flag)
    {
        fieldControl_itemId.setDisabled(flag);
    	textAreaControl_itemDesc.setDisabled(flag);
        fieldControl_qtyOrdered.setDisabled(flag);
		fieldControl_unitPrice.setDisabled(flag);
    	fieldControl_extndPrice.setDisabled(flag);
        jLabel_itemId.setDisabled(flag);
        checkBox_insideRepair.setDisabled(flag);
        checkBox_outsideRepair.setDisabled(flag);
        acctPanel.setDisabled(flag);
        daiDateField_shipDate.setDisabled(flag);;
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

	private void calcSubtotal()
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

	private void calcExtendedPrice()
	{
		Float unitPrice;
		Long  qtyOrdered;
		if (fieldControl_unitPrice.getText() == null)
		{
			fieldControl_unitPrice.setText("0");
		}
		unitPrice = new Float (fieldControl_unitPrice.getText());

		if (fieldControl_qtyOrdered.getText() == null)
		{
			fieldControl_qtyOrdered.setText("0");
		}
		qtyOrdered = new Long(fieldControl_qtyOrdered.getText());

		float extendedPrice = (unitPrice.floatValue() * qtyOrdered.longValue());

		fieldControl_extndPrice.setText(Float.toString(extendedPrice));
	}

	private void fieldControl_unitPrice_focusLost(FocusEvent e)
	{
        if (!e.isTemporary()) {
    		calcExtendedPrice();
        }
	}

	private void fieldControl_qtyOrdered_focusLost(FocusEvent e)
	{
        if (!e.isTemporary()) {
    		calcExtendedPrice();
            //We do this so that a calculation will work when
            //running the reports.
            if (daiNumField_qtyShipped.getText() == null) {
                daiNumField_qtyShipped.setText("0");
            }
        }
	}

	private void jLabel_itemId_daiActionEvent(daiActionEvent e)
	{
		itemObj itemObj = new itemObj();
        String itemId = fieldControl_itemId.getText();

        DBAttributes attrib1 = new DBAttributes(itemObj.ID, itemId, "Item Id", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  itemObj,
                                              new DBAttributes[] {attrib1},
											  null, null);
		chooser.show();
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
		if (isObjNull && (id != null && id.trim().length() > 0))
		{
			//Do nothing
		} else
		{
    		fieldControl_itemId.setText(isObjNull ? null : obj.get_id());
	    	textAreaControl_itemDesc.setText(isObjNull ? null : obj.get_standard_desc());
		    fieldControl_unitPrice.setText(isObjNull ? null : obj.get_sales_price());
            acctPanel.setAcctId(isObjNull ? null : obj.get_sales_acct_id());
            acctPanel.setAcctName(isObjNull ? null : obj.get_sales_acct_name());

            //Get the Item's subcomponents and add/remove from the model
   			cust_order_itemObj tempObj = (cust_order_itemObj)GRID.get(GRID.getActiveRow(), OBJ_COL_POS);
            GRID.set(GRID.getActiveRow(), OBJ_COL_POS, tempObj);
		}
    }
}

