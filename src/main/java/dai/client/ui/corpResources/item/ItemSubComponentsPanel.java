
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.item;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

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
import dai.shared.businessObjs.item_bomObj;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiNumField;
import daiBeans.daiQueryTextField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;

public class ItemSubComponentsPanel extends daiDetailPanel
{
    GroupBox groupBox_subItems = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();

    //Constants for Grid column positions
    final int gridColSubItemId      = 2;
    final int gridColSubItemDesc    = 3;
    final int gridColBOMLevel       = 4;
    final int gridColQtyUsed        = 5;
    final int gridColUOM            = 6;
    final int gridColNote           = 7;

    //Entry Field Declarations
    daiNumField daiNumField_bomLevel = new daiNumField();
    daiQueryTextField daiTextField_subItemId = new daiQueryTextField(new itemObj());
    daiTextField daiTextField_subItemDesc = new daiTextField();
    daiNumField daiNumField_qtyUsed = new daiNumField();
    daiTextField daiTextField_uom = new daiTextField();
	daiTextArea  textAreaControl_note = new daiTextArea();
    //Label Declarations
    daiBeans.daiLabel daiLabel_bomLevel = new daiBeans.daiLabel("BOM Level:");
    daiBeans.daiLabel daiLabel_subItemId = new daiBeans.daiLabel("Sub Item Id:");
    daiBeans.daiLabel daiLabel_subItemDesc = new daiBeans.daiLabel("Sub Item Desc:");
    daiBeans.daiLabel daiLabel_qtyUsed = new daiBeans.daiLabel("Qty Consumed:");
    daiBeans.daiLabel daiLabel_uom = new daiBeans.daiLabel("UOM:");
    daiBeans.daiLabel daiLabel_note = new daiBeans.daiLabel("Note:");
    BoxLayout2 boxLayout21 = new BoxLayout2();

	public ItemSubComponentsPanel(JFrame container, daiFrame parentFrame, item_bomObj obj)
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
        daiGridController gridController = GRID_CONTROLLER;
        GRID.createColumns(8);
		GRID.setHeaderNames(new String[]{" ", "OBJ", "Sub Item Id", "Sub Item Desc",
            "BOM Level", "Qty Consumed", "UOM", "Note"});
        daiLabel_subItemId.setHREFstyle(true);
        GRID.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);
        GRID.setColumnSize(2, daiGrid.DEFAULT_ID_COL_WIDTH);
        GRID.setColumnSize(3, daiGrid.DEFAULT_DESC_COL_WIDTH);
        GRID.hideColumn(1); //Id field

        daiLabel_subItemId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_subItemId_daiAction(e);
            }
        });

        daiTextField_subItemId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e) {
                daiTextField_subItemId_daiActionEvent(e);
                }
            });

        //GroupBox:SubComponents Entry setup
        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        groupBox_subItems.add(gridController, new XYConstraints(3, 6, -1, -1));
        groupBox_subItems.setBackground(daiColors.PanelColor);
        groupBox_subItems.setLabel("SubComponent Entry");
        groupBox_subItems.setLayout(xYLayout2);
        //Add Labels and Entry Fields to the group box
        groupBox_subItems.add(daiTextField_subItemId, new XYConstraints(93, 57, 218, -1));
        groupBox_subItems.add(daiLabel_subItemId, new XYConstraints(31, 58, 56, -1));
        groupBox_subItems.add(daiLabel_subItemDesc, new XYConstraints(19, 82, -1, -1));
        groupBox_subItems.add(daiTextField_subItemDesc, new XYConstraints(93, 81, 372, -1));
        groupBox_subItems.add(daiNumField_bomLevel, new XYConstraints(93, 105, -1, -1));
        groupBox_subItems.add(daiLabel_bomLevel, new XYConstraints(21, 106, 66, -1));
        groupBox_subItems.add(daiLabel_qtyUsed, new XYConstraints(13, 131, -1, -1));
        groupBox_subItems.add(daiNumField_qtyUsed, new XYConstraints(93, 129, -1, -1));
        groupBox_subItems.add(daiTextField_uom, new XYConstraints(233, 129, -1, -1));
        groupBox_subItems.add(daiLabel_uom, new XYConstraints(197, 131, -1, -1));
        groupBox_subItems.add(textAreaControl_note, new XYConstraints(93, 155, 399, 42));
        groupBox_subItems.add(daiLabel_note, new XYConstraints(62, 154, -1, -1));

        grid.setPreferredSize(new Dimension(526, 100));
        grid.setMinimumSize(new Dimension(526, 100));

        this.add(groupBox_subItems, null);
        this.add(grid, null);

        //Clear out the GRID
		GRID.removeAllRows();

		//Clear out all the rows and entry fields.
        disableEntryFields(true);
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        item_bomObj obj = new item_bomObj();
        item_bomObj tempObj = (item_bomObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

        return obj;
    }

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		item_bomObj obj;

		//Delete all the detail rows.
        GRID.removeAllRows();

		//Refresh the all rows of the grid from the detail Object Vector.
		for (int i=0; i < objVect.size(); i++)
		{
			obj = (item_bomObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, gridColBOMLevel, obj.get_bom_level());
			GRID.set(i, gridColSubItemId, obj.get_subcomponent_id());
            GRID.set(i, gridColSubItemDesc, obj.get_subcomponent_desc());
			GRID.set(i, gridColQtyUsed, obj.get_qty_consumed());
			GRID.set(i, gridColUOM, obj.get_unit_of_meas());
			GRID.set(i, gridColNote, "");

			GRID.set(i, OBJ_COL_POS, obj);
		}

		//Clear out the entry fields.
        clearEntryFields();
	}

	protected void updateBusinessObjFromGrid()
	{
		item_bomObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
            //Get the DBObject from the grid
			tempObj = (item_bomObj)GRID.get(i, OBJ_COL_POS);

            //Update the DBobject with the entry fields
			tempObj.set_bom_level((String)GRID.get(i, gridColBOMLevel));
			tempObj.set_subcomponent_id((String)GRID.get(i, gridColSubItemId));
            tempObj.set_subcomponent_desc((String)GRID.get(i, gridColSubItemDesc));
            tempObj.set_qty_consumed((String)GRID.get(i, gridColQtyUsed));
            tempObj.set_unit_of_meas((String)GRID.get(i, gridColUOM));
			//tempObj.set_note((String)GRID.get(i, 7)); //!!Not in the obj yet.  Must ADD

			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

	protected void clearEntryFields()
	{
		//Clear out the data entry fields.
        daiNumField_bomLevel.setText(null);
        daiTextField_subItemId.setText(null);
        daiTextField_subItemDesc.setText(null);
        daiNumField_qtyUsed.setText(null);
        daiTextField_uom.setText(null);
	    textAreaControl_note.setText(null);
    }

	protected void copyEntryFieldsToGrid()
	{
		int row = GRID.getActiveRow();

        GRID.set(row, gridColBOMLevel, daiNumField_bomLevel.getText());
        GRID.set(row, gridColSubItemId, daiTextField_subItemId.getText());
        GRID.set(row, gridColSubItemDesc, daiTextField_subItemDesc.getText());
        GRID.set(row, gridColQtyUsed, daiNumField_qtyUsed.getText());
        GRID.set(row, gridColUOM, daiTextField_uom.getText());
        GRID.set(row, gridColNote, textAreaControl_note.getText());
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
        daiNumField_bomLevel.setText((String)GRID.get(row, gridColBOMLevel));
        daiTextField_subItemId.setText((String)GRID.get(row, gridColSubItemId));
        daiTextField_subItemDesc.setText((String)GRID.get(row, gridColSubItemDesc));
        daiNumField_qtyUsed.setText((String)GRID.get(row, gridColQtyUsed));
        daiTextField_uom.setText((String)GRID.get(row, gridColUOM));
        textAreaControl_note.setText((String)GRID.get(row, gridColNote));
	}

    protected void disableEntryFields (boolean disable)
    {
        if (disable)
        {
            daiNumField_bomLevel.setDisabled(true);
            daiTextField_subItemId.setDisabled(true);
            daiTextField_subItemDesc.setDisabled(true);
            daiNumField_qtyUsed.setDisabled(true);
            daiTextField_uom.setDisabled(true);
            textAreaControl_note.setDisabled(true);
        } else {
            daiNumField_bomLevel.setDisabled(false);
            daiTextField_subItemId.setDisabled(false);
            daiTextField_subItemDesc.setDisabled(false);
            daiNumField_qtyUsed.setDisabled(false);
            daiTextField_uom.setDisabled(false);
            textAreaControl_note.setDisabled(false);
        }
    }

    private void daiTextField_subItemId_daiActionEvent(daiActionEvent e)
    {
        copyItemAttribs((itemObj)e.getSource());
    }

    private void daiLabel_subItemId_daiAction(daiActionEvent e)
    {
            itemObj obj = new itemObj();
            String id = daiTextField_subItemId.getText();

            DBAttributes attrib = new DBAttributes(itemObj.ID, id, "Item Id", 200);
            DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
                                                  obj,
                                                  new DBAttributes[]{attrib},
                                                  null, null);
            chooser.show();
            itemObj chosenObj = (itemObj)chooser.getChosenObj();
            if (chosenObj != null) {
                copyItemAttribs(chosenObj);
            }
            chooser.dispose();
    }

    private void copyItemAttribs(itemObj obj) {
        daiTextField_subItemId.setText(obj.get_id());
        daiTextField_subItemDesc.setText(obj.get_standard_desc());
    }
}

