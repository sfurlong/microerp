
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.quote;


import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
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
import dai.shared.businessObjs.quote_itemObj;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCurrencyField;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiQueryTextField;
import daiBeans.daiRadioButton;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;


public class QuoteItemPanel extends daiDetailPanel
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
    daiLabel label_delivNote = new daiLabel("Delivery Note:");
    daiTextField textField_delivNote = new daiTextField();
    daiRadioButton radioButton_isPrime = new daiRadioButton("Is Primary");
    daiRadioButton radioButton_isOpt = new daiRadioButton("Is Optional");
    daiRadioButton radioButton_isAlt = new daiRadioButton("Is Alternate");
    ButtonGroup itemTypeButtonGroup = new ButtonGroup();

    GroupBox groupBox_itemEntry = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();

    //Ids for grid column positions.
    int gridColItemId       = 2;
	int gridColItemDesc     = 3;
	int gridColUnitPrice    = 4;
	int gridColQtyOrdered   = 5;
	int gridColExtndPrice   = 6;
	int gridColDelivNote    = 7;
    int gridColItemType     = 8;
    JPanel jPanel_subtotals = new JPanel();
    BoxLayout2 boxLayout21 = new BoxLayout2();

	public QuoteItemPanel(JFrame container, daiFrame parentFrame, quote_itemObj obj) {

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
        GRID.createColumns(9);
		GRID.setHeaderNames(new String[]{" ", "OBJ", "Item Id", "Description",
                    "Unit Price", "Qty Ordered", "Extd. Price", "Deliv Note",
                    "Item Type"});
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

        jLabel_qtyOrdered.setText("Qty:");
        itemTypeButtonGroup.add(radioButton_isPrime);
        itemTypeButtonGroup.add(radioButton_isOpt);
        itemTypeButtonGroup.add(radioButton_isAlt);

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

        radioButton_isPrime.setSelected(true);

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
        groupBox_itemEntry.add(jLabel_itemId, new XYConstraints(21, 50, -1, -1));
        groupBox_itemEntry.add(jLabel_itemDesc, new XYConstraints(-3, 78, -1, -1));
        groupBox_itemEntry.add(jLabel_unitPrice, new XYConstraints(398, 53, -1, -1));
        groupBox_itemEntry.add(jLabel_qtyOrdered, new XYConstraints(426, 82, -1, -1));
        groupBox_itemEntry.add(jLabel_extndPrice, new XYConstraints(371, 109, -1, -1));
        groupBox_itemEntry.add(fieldControl_itemId, new XYConstraints(61, 50, 183, -1));
        groupBox_itemEntry.add(fieldControl_unitPrice, new XYConstraints(452, 51, -1, -1));
        groupBox_itemEntry.add(fieldControl_qtyOrdered, new XYConstraints(452, 80, -1, -1));
        groupBox_itemEntry.add(fieldControl_extndPrice, new XYConstraints(452, 108, -1, -1));
        groupBox_itemEntry.add(textField_delivNote, new XYConstraints(452, 134, -1, -1));
        groupBox_itemEntry.add(label_delivNote, new XYConstraints(375, 135, 71, -1));
        groupBox_itemEntry.add(textAreaControl_itemDesc, new XYConstraints(61, 79, 299, 77));
        groupBox_itemEntry.add(radioButton_isAlt, new XYConstraints(477, 22, 79, -1));
        groupBox_itemEntry.add(radioButton_isPrime, new XYConstraints(339, 22, 65, -1));
        groupBox_itemEntry.add(radioButton_isOpt, new XYConstraints(406, 22, 69, -1));

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

	protected BusinessObject getNewBusinessObjInstance()
	{
		quote_itemObj tempObj = (quote_itemObj)BUSINESS_OBJ;
		quote_itemObj obj = new quote_itemObj();

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
        textField_delivNote.setText(null);
        radioButton_isPrime.setSelected(true);
        radioButton_isOpt.setSelected(false);
        radioButton_isAlt.setSelected(false);

		//Set the focus to the item ID.
		fieldControl_itemId.requestFocus();
	}

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		quote_itemObj itemObj;

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
			itemObj = (quote_itemObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, gridColItemId, itemObj.get_item_id());
			GRID.set(i, gridColItemDesc, itemObj.get_description1());
			GRID.set(i, gridColUnitPrice, itemObj.get_unit_price());
			GRID.set(i, gridColQtyOrdered, itemObj.get_qty_ordered());
			GRID.set(i, gridColExtndPrice, itemObj.get_extended_price());
			GRID.set(i, gridColDelivNote, itemObj.get_est_deliv_note());
            GRID.set(i, gridColItemType, itemObj.get_item_type_ind());

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
		GRID.set(row, gridColDelivNote, textField_delivNote.getText());

        if (radioButton_isPrime.isSelected()) {
            GRID.set(row, gridColItemType, "P");
        } else if (radioButton_isOpt.isSelected()) {
            GRID.set(row, gridColItemType, "O");
        } else if (radioButton_isAlt.isSelected()) {
            GRID.set(row, gridColItemType, "A");
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
		textField_delivNote.setText((String)GRID.get(row, gridColDelivNote));
        String itemType = (String)GRID.get(row, gridColItemType);
        if (itemType != null && itemType.equals("O")) {
            radioButton_isOpt.setSelected(true);
        } else if (itemType != null && itemType.equals("A")) {
            radioButton_isAlt.setSelected(true);
        } else {
            radioButton_isPrime.setSelected(true);
        }


	}

	protected void updateBusinessObjFromGrid()
	{
		quote_itemObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (quote_itemObj)GRID.get(i, OBJ_COL_POS);

			tempObj.set_item_id((String)GRID.get(i, gridColItemId));
			tempObj.set_description1((String)GRID.get(i, gridColItemDesc));
			tempObj.set_unit_price((String)GRID.get(i, gridColUnitPrice));
			tempObj.set_qty_ordered((String)GRID.get(i, gridColQtyOrdered));
			tempObj.set_extended_price((String)GRID.get(i, gridColExtndPrice));
            tempObj.set_est_deliv_note((String)GRID.get(i, gridColDelivNote));
            tempObj.set_item_type_ind((String)GRID.get(i, gridColItemType));

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
        textField_delivNote.setDisabled(flag);
        radioButton_isPrime.setDisabled(flag);
        radioButton_isAlt.setDisabled(flag);
        radioButton_isOpt.setDisabled(flag);
        jLabel_itemId.setDisabled(flag);
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
        }
	}

	private void jLabel_itemId_daiActionEvent(daiActionEvent e)
	{
		itemObj itemObj = new itemObj();
        String itemId = fieldControl_itemId.getText();

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DBAttributes attrib1 = new DBAttributes(itemObj.ID, itemId, "Id", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
                                                new itemObj(),
											    new DBAttributes[]{attrib1},
                                                null, null);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		chooser.setVisible(true);
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
		}
    }
}

