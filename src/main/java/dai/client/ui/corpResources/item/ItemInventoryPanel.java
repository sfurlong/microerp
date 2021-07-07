
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.item;

import java.awt.Dimension;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiDetailPanel;
import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.item_inventoryObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.daiCurrencyField;
import daiBeans.daiGrid;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;

public class ItemInventoryPanel extends daiDetailPanel
{
    GroupBox groupBox_inventoryEntry = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();

    //Constants for Grid column positions
    final int gridColDateCreated    = 2;
    final int gridColCreatedBy      = 3;
    final int gridColForStock       = 4;
    final int gridColForCust        = 5;
    final int gridColAdjType        = 6;
    final int gridColAdjReason      = 7;
    final int gridColAdjQty         = 8;
    final int gridColOnHand         = 9;
    final int gridColCustBack       = 10;
    final int gridColVendBack       = 11;
    final int gridColPurchPrice     = 12;
    final int gridColPriceQtyUsed   = 13;
    final int gridColNote           = 14;

    //Entry Field Declarations
    daiNumField daiNumField_qtyReceived = new daiNumField();
    daiNumField daiNumField_qtyOrdered = new daiNumField();
    daiTextField daiTextField_adjustmentType = new daiTextField();
    daiTextField daiTextField_adjustmentReason = new daiTextField();
    daiNumField daiNumField_adjustmentQty = new daiNumField();
	daiTextArea textAreaControl_note = new daiTextArea();
    daiCurrencyField currencyField_purchPrice = new daiCurrencyField();
    daiNumField numField_purchPriceQtyUsed = new daiNumField();
    //Label Declarations
    daiBeans.daiLabel daiLabel_adjustmentType = new daiBeans.daiLabel("Adjustment Type:");
    daiBeans.daiLabel daiLabel_adjustmentReason = new daiBeans.daiLabel("Adjustment Reason:");
    daiBeans.daiLabel daiLabel_adjustmentQty = new daiBeans.daiLabel("Adjustment Qty:");
    daiBeans.daiLabel daiLabel_note = new daiBeans.daiLabel("Note:");
    daiLabel daiLabel_purchPrice = new daiLabel("Purchase Price:");
    daiLabel daiLabel_purchPriceQtyUsed = new daiLabel("Purchase Price Qty Used For FIFI Inventory Accounting:");
    daiUserIdDateCreatedPanel daiUserIdDateCreated = new daiUserIdDateCreatedPanel();
    BoxLayout2 boxLayout21 = new BoxLayout2();
    ButtonGroup buttonGroup = new ButtonGroup();
    JRadioButton jRadioButton_na = new JRadioButton();
    JRadioButton jRadioButton_forStock = new JRadioButton();
    JRadioButton jRadioButton_forCust = new JRadioButton();

    daiLabel labelControl_qtyOnHand = new daiLabel("Qty On Hand:");
	daiLabel labelControl_qtyCustBackOrd = new daiLabel("Qty Cust Backord:");
	daiLabel labelControl_qtyVendorBackOrd = new daiLabel("Qty Vendor Backord:");
	daiNumField fieldControl_qtyOnHand = new daiNumField();
	daiNumField fieldControl_qtyCustBackOrd = new daiNumField();
	daiNumField fieldControl_qtyVendBackOrd = new daiNumField();
    GroupBox groupBox_acctg = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();

	public ItemInventoryPanel(JFrame container, daiFrame parentFrame, item_inventoryObj obj)
	{
		super(container, parentFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			//Log to dialog, system.out, disk.
			LOGGER.logError(CONTAINER, "Error Initializing Partner Detail panel.\n" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		this.setLayout(boxLayout21);

        //Grid Setup Stuff
        //These local grid components are only necessary so that they
        //will show up in the IDE designer for this panel.
        daiGrid grid = GRID;
        GRID.createColumns(15);
        GRID.setHeaderNames(new String[]{" ", "OBJ", "Date Created", "Created By",
            "For Stock", "For Cust",
            "Adj Type", "Adj Reason", "Adj Qty", "Qty OnHand",
            "Qty CustBack", "Qty VendBack", "Purch Price", "Price Qty Used", "Note"});
        GRID.hideColumn(1);  //Hide the OBJ grid column.
        GRID.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);

        labelControl_qtyCustBackOrd.setText("Qty Cust Backord");
        labelControl_qtyOnHand.setText("Qty On Hand");
        labelControl_qtyVendorBackOrd.setText("Qty Vendor Backord");
        jRadioButton_forStock.setFont(new java.awt.Font("Dialog", 0, 11));
        jRadioButton_forCust.setFont(new java.awt.Font("Dialog", 0, 11));
        jRadioButton_na.setFont(new java.awt.Font("Dialog", 0, 11));
        grid.setPreferredSize(new Dimension(526, 100));
        grid.setMinimumSize(new Dimension(526, 100));
        daiLabel_purchPriceQtyUsed.setText("Purchase Qty Avail for FIFO:");
        groupBox_acctg.setLabel("Inventory Accounting");
        groupBox_acctg.setLayout(xYLayout1);
        buttonGroup.add(jRadioButton_forCust);
        buttonGroup.add(jRadioButton_forStock);
        buttonGroup.add(jRadioButton_na);
        jRadioButton_na.setSelected(true);

        //GroupBox:Inventory Entry setup
        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        daiLabel_adjustmentReason.setText("Adjustment Ref Id:");
        jRadioButton_na.setOpaque(false);
        jRadioButton_na.setText("N/A");
        jRadioButton_forStock.setOpaque(false);
        jRadioButton_forStock.setText("For Stock");
        jRadioButton_forCust.setOpaque(false);
        jRadioButton_forCust.setText("For Customer");

        groupBox_inventoryEntry.setBackground(daiColors.PanelColor);
        groupBox_inventoryEntry.setLabel("Inventory Entry");
        groupBox_inventoryEntry.setLayout(xYLayout2);

        this.add(groupBox_inventoryEntry, null);
        groupBox_inventoryEntry.add(fieldControl_qtyOnHand, new XYConstraints(17, 40, -1, -1));
        groupBox_inventoryEntry.add(labelControl_qtyCustBackOrd, new XYConstraints(130, 23, -1, -1));
        groupBox_inventoryEntry.add(fieldControl_qtyCustBackOrd, new XYConstraints(130, 40, -1, -1));
        groupBox_inventoryEntry.add(labelControl_qtyVendorBackOrd, new XYConstraints(243, 23, -1, -1));
        groupBox_inventoryEntry.add(fieldControl_qtyVendBackOrd, new XYConstraints(243, 40, -1, -1));
        groupBox_inventoryEntry.add(labelControl_qtyOnHand, new XYConstraints(18, 23, -1, -1));
        groupBox_inventoryEntry.add(daiUserIdDateCreated, new XYConstraints(369, 2, -1, -1));
        groupBox_inventoryEntry.add(daiLabel_adjustmentQty, new XYConstraints(16, 72, -1, -1));
        groupBox_inventoryEntry.add(daiLabel_adjustmentType, new XYConstraints(9, 98, -1, -1));
        groupBox_inventoryEntry.add(daiLabel_adjustmentReason, new XYConstraints(5, 124, -1, -1));
        groupBox_inventoryEntry.add(daiTextField_adjustmentReason, new XYConstraints(100, 122, -1, -1));
        groupBox_inventoryEntry.add(daiNumField_adjustmentQty, new XYConstraints(100, 70, -1, -1));
        groupBox_inventoryEntry.add(daiTextField_adjustmentType, new XYConstraints(100, 96, 203, -1));
        groupBox_inventoryEntry.add(jRadioButton_forStock, new XYConstraints(347, 70, -1, -1));
        groupBox_inventoryEntry.add(jRadioButton_forCust, new XYConstraints(347, 95, -1, -1));
        groupBox_inventoryEntry.add(jRadioButton_na, new XYConstraints(347, 120, -1, -1));
        groupBox_inventoryEntry.add(textAreaControl_note, new XYConstraints(99, 148, 425, 53));
        groupBox_inventoryEntry.add(daiLabel_note, new XYConstraints(67, 149, -1, -1));
        groupBox_inventoryEntry.add(groupBox_acctg, new XYConstraints(70, 200, 461, 50));
        groupBox_acctg.add(daiLabel_purchPrice, new XYConstraints(-1, 2, -1, -1));
        groupBox_acctg.add(daiLabel_purchPriceQtyUsed, new XYConstraints(186, 2, -1, -1));
        groupBox_acctg.add(numField_purchPriceQtyUsed, new XYConstraints(325, 0, -1, -1));
        groupBox_acctg.add(currencyField_purchPrice, new XYConstraints(78, 0, -1, -1));
        this.add(grid, null);

        //Clear out the GRID
		GRID.removeAllRows();

		//Clear out all the rows and entry fields.
        disableEntryFields(true);
	}

	//Override the base class method.  We don't want to persist 
	//inventory tab.  Any changes to the data on this tab should be 
	//done via wizards.
	public int persistPanelData(){
		return 0;
	}
	
    protected BusinessObject getNewBusinessObjInstance()
    {
        item_inventoryObj obj = new item_inventoryObj();
        item_inventoryObj tempObj = (item_inventoryObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

        return obj;
    }

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		item_inventoryObj itemObj;

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
			itemObj = (item_inventoryObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, gridColDateCreated, itemObj.get_date_created());
			GRID.set(i, gridColCreatedBy, itemObj.get_created_by());
			GRID.set(i, gridColForStock, itemObj.get_for_stock());
			GRID.set(i, gridColForCust, itemObj.get_for_customer());
			GRID.set(i, gridColAdjType, itemObj.get_adjustment_type());
            GRID.set(i, gridColAdjReason, itemObj.get_adjustment_reason());
            GRID.set(i, gridColAdjQty, itemObj.get_adjustment_qty());
			GRID.set(i, gridColOnHand, itemObj.get_qty_onhand());
			GRID.set(i, gridColCustBack, itemObj.get_qty_cust_back_ord());
			GRID.set(i, gridColVendBack, itemObj.get_qty_vend_back_ord());
			GRID.set(i, gridColNote, itemObj.get_note());
            GRID.set(i, gridColPurchPrice, itemObj.get_item_purch_price());
            GRID.set(i, gridColPriceQtyUsed, itemObj.get_qty_avail_for_inventory_accting());

			GRID.set(i, OBJ_COL_POS, itemObj);
		}

		//Clear out the entry fields.
		clearEntryFields();
	}

	protected void updateBusinessObjFromGrid()
	{
		item_inventoryObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (item_inventoryObj)GRID.get(i, OBJ_COL_POS);

			tempObj.set_date_created((String)GRID.get(i, gridColDateCreated));
			tempObj.set_for_stock((String)GRID.get(i, gridColForStock));
			tempObj.set_for_customer((String)GRID.get(i, gridColForCust));
			tempObj.set_adjustment_type((String)GRID.get(i, gridColAdjType));
			tempObj.set_adjustment_reason((String)GRID.get(i, gridColAdjReason));
			tempObj.set_adjustment_qty((String)GRID.get(i, gridColAdjQty));
			tempObj.set_qty_onhand((String)GRID.get(i, gridColOnHand));
			tempObj.set_qty_cust_back_ord((String)GRID.get(i, gridColCustBack));
			tempObj.set_qty_vend_back_ord((String)GRID.get(i, gridColVendBack));
			tempObj.set_note((String)GRID.get(i, gridColNote));
            tempObj.set_item_purch_price((String)GRID.get(i, gridColPurchPrice));
            tempObj.set_qty_avail_for_inventory_accting((String)GRID.get(i, gridColPriceQtyUsed));
			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

	protected void clearEntryFields()
	{
		//Clear out the data entry fields.
        jRadioButton_forStock.setSelected(false);
        jRadioButton_forCust.setSelected(false);
        jRadioButton_na.setSelected(true);
        daiTextField_adjustmentType.setText(null);
        daiTextField_adjustmentReason.setText(null);
        daiNumField_adjustmentQty.setText(null);
        textAreaControl_note.setText(null);
        fieldControl_qtyOnHand.setText(null);
        fieldControl_qtyCustBackOrd.setText(null);
        fieldControl_qtyVendBackOrd.setText(null);
        currencyField_purchPrice.setText(null);
        numField_purchPriceQtyUsed.setText(null);
    }

	protected void copyEntryFieldsToGrid()
	{
		int row = GRID.getActiveRow();

        SessionMetaData sessionMeta = null;
        sessionMeta = SessionMetaData.getInstance();

        Calendar now = Calendar.getInstance();
        String nowDate =    now.get(Calendar.MONTH)+1 + "/" +
                            now.get(Calendar.DAY_OF_MONTH) + "/" +
                            now.get(Calendar.YEAR);

        GRID.set(row, gridColDateCreated, nowDate);
        GRID.set(row, gridColCreatedBy, sessionMeta.getUserId());
        if (jRadioButton_forStock.isSelected()) {
            GRID.set(row, gridColForStock, "Y");
        } else {
            GRID.set(row, gridColForStock, "");
        }
        if (jRadioButton_forCust.isSelected()) {
            GRID.set(row, gridColForCust, "Y");
        } else {
            GRID.set(row, gridColForCust, "");
        }
        GRID.set(row, gridColAdjType, daiTextField_adjustmentType.getText());
        GRID.set(row, gridColAdjReason, daiTextField_adjustmentReason.getText());
        GRID.set(row, gridColAdjQty, daiNumField_adjustmentQty.getText());
		GRID.set(row, gridColNote, textAreaControl_note.getText());
        GRID.set(row, gridColOnHand, fieldControl_qtyOnHand.getText());
        GRID.set(row, gridColCustBack, fieldControl_qtyCustBackOrd.getText());
        GRID.set(row, gridColVendBack, fieldControl_qtyVendBackOrd.getText());
        GRID.set(row, gridColPurchPrice, currencyField_purchPrice.getText());
        GRID.set(row, gridColPriceQtyUsed, numField_purchPriceQtyUsed.getText());
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
        daiUserIdDateCreated.setDateCreated((String)GRID.get(row, gridColDateCreated));
        daiUserIdDateCreated.setUserId((String)GRID.get(row, gridColCreatedBy));
        String forStock = (String)GRID.get(row, gridColForStock);
        String forCust = (String)GRID.get(row, gridColForCust);
        if (forStock != null && forStock.equals("Y")) {
            jRadioButton_forStock.setSelected(true);
        }else if (forCust != null && forCust.equals("Y")) {
            jRadioButton_forCust.setSelected(true);
        } else {
            jRadioButton_na.setSelected(true);
        }
        daiTextField_adjustmentType.setText((String)GRID.get(row, gridColAdjType));
        daiTextField_adjustmentReason.setText((String)GRID.get(row, gridColAdjReason));
        daiNumField_adjustmentQty.setText((String)GRID.get(row, gridColAdjQty));
		textAreaControl_note.setText((String)GRID.get(row, gridColNote));
        fieldControl_qtyOnHand.setText((String)GRID.get(row, gridColOnHand));
        fieldControl_qtyCustBackOrd.setText((String)GRID.get(row, gridColCustBack));
        fieldControl_qtyVendBackOrd.setText((String)GRID.get(row, gridColVendBack));
        currencyField_purchPrice.setText((String)GRID.get(row, gridColPurchPrice));
        numField_purchPriceQtyUsed.setText((String)GRID.get(row, gridColPriceQtyUsed));
	}

    protected void disableEntryFields (boolean flag)
    {
        daiTextField_adjustmentType.setDisabled(flag);
        daiTextField_adjustmentReason.setDisabled(flag);
        daiNumField_adjustmentQty.setDisabled(flag);
        textAreaControl_note.setDisabled(flag);
        fieldControl_qtyOnHand.setDisabled(flag);
        fieldControl_qtyCustBackOrd.setDisabled(flag);
        fieldControl_qtyVendBackOrd.setDisabled(flag);
        currencyField_purchPrice.setDisabled(flag);
        numField_purchPriceQtyUsed.setDisabled(flag);
    }
}

