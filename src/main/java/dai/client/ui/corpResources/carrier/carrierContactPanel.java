
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.carrier;

import java.util.Vector;

import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiDetailPanel;
import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.customer_contactObj;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiLabel;
import daiBeans.daiMaskField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;

public class carrierContactPanel extends daiDetailPanel
{
	XYLayout    xYLayout1 = new XYLayout();
    GroupBox groupBox1 = new GroupBox();

    //Constants for Grid column positions
    final int gridColObj            = 0;
    final int gridColDetailId       = 1;
    final int gridColName           = 2;
    final int gridColTitle          = 3;
    final int gridColOfficePhone    = 4;
    final int gridColOtherPhone     = 5;
    final int gridColOfficeFax      = 6;
    final int gridColOtherFax       = 7;
    final int gridColOfficeEmail    = 8;
    final int gridColOtherEmail     = 9;
    final int gridColPager          = 10;
    final int gridColMoblePhone     = 11;
    final int gridColWeb            = 12;
    final int gridColNote           = 13;

    XYLayout xYLayout2 = new XYLayout();

    //Label Declarations
    daiLabel daiLabel_notel = new daiLabel("Note:");
    daiLabel daiLabel_name = new daiLabel("Name:");
    daiLabel daiLabel_title = new daiLabel("Title");
    daiLabel daiLabel_officePhone = new daiLabel("Office Phone:");
    daiLabel daiLabel_otherPhone = new daiLabel("Other Phone:");
    daiLabel daiLabel_officeFax = new daiLabel("Office Fax:");
    daiLabel daiLabel_otherFax = new daiLabel("Other Fax:");
    daiLabel daiLabel_officeEmail = new daiLabel("Office Email:");
    daiLabel daiLabel_otherEmail = new daiLabel("Other Email:");
    daiLabel daiLabel_pager = new daiLabel("Pager:");
    daiLabel daiLabel_mobil = new daiLabel("Mobile Phone:");
    daiLabel daiLabel_web = new daiLabel("Web:");
    //Entry Field Declarations
    daiTextField daiTextField_name = new daiTextField();
    daiMaskField daiTextField_officePhone = new daiMaskField("(###) ###-####");
    daiMaskField daiTextField_officeFax = new daiMaskField("(###) ###-####");
    daiMaskField daiTextField_pager = new daiMaskField("(###) ###-####");
    daiTextField daiTextField_officeEmail = new daiTextField();
    daiTextField daiTextField_web = new daiTextField();
    daiTextField daiTextField_title = new daiTextField();
    daiMaskField daiTextField_otherPhone = new daiMaskField("(###) ###-####");
    daiMaskField daiTextField_otherFax = new daiMaskField("(###) ###-####");
    daiMaskField daiTextField_mobilePhone = new daiMaskField("(###) ###-####");
    daiTextField daiTextField_otherEmail = new daiTextField();
	daiTextArea  textAreaControl_note = new daiTextArea();

	public carrierContactPanel(JFrame container, daiFrame parentFrame, customer_contactObj obj)
	{
		super(container, parentFrame, obj);

        COMPONENT_ID = "TAB1";

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			//Log to dialog, system.out, disk.
			LOGGER.logError(CONTAINER, "Error Initializing customer Detail panel.\n" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
        //Grid Setup Stuff
        //These local grid components are only necessary so that they
        //will show up in the IDE designer for this panel.
        daiGrid grid = GRID;
        daiGridController gridController = GRID_CONTROLLER;
        groupBox1.setLayout(xYLayout2);
        GRID.createColumns(14);
		GRID.setHeaderNames(new String[]{"OBJ", "Detail Id", "Name", "Title",
            "Office Phone", "Other Phone", "Office Fax", "Other Fax",
            "Office Email", "Other Email", "Pager", "Mobile Phone", "Web", "Note"});
        GRID.hideColumn(0);  //Hide OBJ col
        GRID.hideColumn(0);  //Hide detail_ID col
        //Clear out the GridControl
		GRID.removeAllRows();

		xYLayout1.setHeight(492);
		xYLayout1.setWidth(619);
		this.setLayout(xYLayout1);
        groupBox1.setBackground(daiColors.PanelColor);
        groupBox1.setLabel("Contact Entry");
        daiLabel_title.setText("Title:");
        this.add(grid, new XYConstraints(1, 389, 597, 92));
        this.add(gridController, new XYConstraints(18, 22, 368, 42));
        this.add(groupBox1, new XYConstraints(-3, 0, 608, 364));

        //Populate the entry Group Box
        groupBox1.add(daiLabel_notel, new XYConstraints(41, 233, -1, -1));
        groupBox1.add(daiLabel_web, new XYConstraints(41, 209, -1, -1));
        groupBox1.add(daiLabel_officeEmail, new XYConstraints(6, 178, -1, -1));
        groupBox1.add(daiLabel_pager, new XYConstraints(35, 148, -1, -1));
        groupBox1.add(daiLabel_officeFax, new XYConstraints(12, 117, -1, -1));
        groupBox1.add(daiLabel_officePhone, new XYConstraints(0, 86, -1, -1));
        groupBox1.add(daiLabel_otherEmail, new XYConstraints(321, 178, -1, -1));
        groupBox1.add(daiLabel_mobil, new XYConstraints(312, 148, -1, -1));
        groupBox1.add(daiLabel_otherFax, new XYConstraints(327, 117, -1, -1));
        groupBox1.add(daiLabel_otherPhone, new XYConstraints(315, 86, -1, -1));
        groupBox1.add(daiLabel_title, new XYConstraints(356, 56, -1, -1));
        groupBox1.add(daiTextField_name, new XYConstraints(78, 53, 213, -1));
        groupBox1.add(daiTextField_title, new XYConstraints(385, 53, 189, -1));
        groupBox1.add(daiTextField_officePhone, new XYConstraints(78, 84, 159, -1));
        groupBox1.add(daiTextField_otherPhone, new XYConstraints(386, 84, 159, -1));
        groupBox1.add(daiTextField_officeFax, new XYConstraints(78, 115, 159, -1));
        groupBox1.add(daiTextField_otherFax, new XYConstraints(388, 115, 159, -1));
        groupBox1.add(daiTextField_pager, new XYConstraints(78, 145, 159, -1));
        groupBox1.add(daiTextField_mobilePhone, new XYConstraints(389, 145, 159, -1));
        groupBox1.add(daiTextField_officeEmail, new XYConstraints(78, 176, 228, -1));
        groupBox1.add(daiTextField_otherEmail, new XYConstraints(389, 176, 189, -1));
        groupBox1.add(daiTextField_web, new XYConstraints(78, 207, 327, -1));
        groupBox1.add(textAreaControl_note, new XYConstraints(78, 235, 465, 94));
        groupBox1.add(daiLabel_name, new XYConstraints(36, 54, -1, -1));

        disableEntryFields(true);
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        customer_contactObj obj = new customer_contactObj();
        customer_contactObj tempObj = (customer_contactObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

        return obj;
    }

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		customer_contactObj itemObj;

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
			itemObj = (customer_contactObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, gridColObj, itemObj);
			GRID.set(i, gridColDetailId, itemObj.get_detail_id());
			GRID.set(i, gridColName, itemObj.get_name());
			GRID.set(i, gridColOfficePhone, itemObj.get_phone1());
			GRID.set(i, gridColOfficeFax, itemObj.get_fax1());
			GRID.set(i, gridColPager, itemObj.get_pager());
			GRID.set(i, gridColOfficeEmail, itemObj.get_email1());
			GRID.set(i, gridColWeb, itemObj.get_web());
			GRID.set(i, gridColTitle, itemObj.get_title());
			GRID.set(i, gridColOtherPhone, itemObj.get_phone2());
			GRID.set(i, gridColOtherFax, itemObj.get_fax2());
			GRID.set(i, gridColMoblePhone, itemObj.get_moble_phone());
			GRID.set(i, gridColOtherEmail, itemObj.get_email2());
			GRID.set(i, gridColNote, itemObj.get_note());
		}

		//Clear out the entry fields.
        clearEntryFields();
	}

	protected void updateBusinessObjFromGrid()
	{
		customer_contactObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (customer_contactObj)GRID.get(i, gridColObj);

			tempObj.set_detail_id((String)GRID.get(i, gridColDetailId));
			tempObj.set_name((String)GRID.get(i, gridColName));
			tempObj.set_phone1((String)GRID.get(i, gridColOfficePhone));
			tempObj.set_fax1((String)GRID.get(i, gridColOfficeFax));
			tempObj.set_pager((String)GRID.get(i, gridColPager));
			tempObj.set_email1((String)GRID.get(i, gridColOfficeEmail));
			tempObj.set_web((String)GRID.get(i, gridColWeb));
			tempObj.set_title((String)GRID.get(i, gridColTitle));
			tempObj.set_phone2((String)GRID.get(i, gridColOtherPhone));
			tempObj.set_fax2((String)GRID.get(i, gridColOtherFax));
			tempObj.set_moble_phone((String)GRID.get(i, gridColMoblePhone));
			tempObj.set_email2((String)GRID.get(i, gridColOtherEmail));
			tempObj.set_note((String)GRID.get(i, gridColNote));

			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

	protected void clearEntryFields()
	{
		//Clear out the data entry fields.
        daiTextField_name.setText(null);
        daiTextField_officePhone.setText(null);
        daiTextField_officeFax.setText(null);
        daiTextField_pager.setText(null);
        daiTextField_officeEmail.setText(null);
        daiTextField_web.setText(null);
        daiTextField_title.setText(null);
        daiTextField_otherPhone.setText(null);
        daiTextField_otherFax.setText(null);
        daiTextField_mobilePhone.setText(null);
        daiTextField_otherEmail.setText(null);
		textAreaControl_note.setText(null);
    }

	protected void copyEntryFieldsToGrid()
	{
		int row = GRID.getActiveRow();
        GRID.set(row, gridColName, daiTextField_name.getText());
        GRID.set(row, gridColOfficePhone, daiTextField_officePhone.getText());
        GRID.set(row, gridColOfficeFax, daiTextField_officeFax.getText());
        GRID.set(row, gridColPager, daiTextField_pager.getText());
        GRID.set(row, gridColOfficeEmail, daiTextField_officeEmail.getText());
        GRID.set(row, gridColWeb, daiTextField_web.getText());
        GRID.set(row, gridColTitle, daiTextField_title.getText());
        GRID.set(row, gridColOtherPhone, daiTextField_otherPhone.getText());
        GRID.set(row, gridColOtherFax, daiTextField_otherFax.getText());
        GRID.set(row, gridColMoblePhone, daiTextField_mobilePhone.getText());
        GRID.set(row, gridColOtherEmail, daiTextField_otherEmail.getText());
		GRID.set(row, gridColNote, textAreaControl_note.getText());
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
        daiTextField_name.setText((String)GRID.get(row, gridColName));
        daiTextField_officePhone.setText((String)GRID.get(row, gridColOfficePhone));
        daiTextField_officeFax.setText((String)GRID.get(row, gridColOfficeFax));
        daiTextField_pager.setText((String)GRID.get(row, gridColPager));
        daiTextField_officeEmail.setText((String)GRID.get(row, gridColOfficeEmail));
        daiTextField_web.setText((String)GRID.get(row, gridColWeb));
        daiTextField_title.setText((String)GRID.get(row, gridColTitle));
        daiTextField_otherPhone.setText((String)GRID.get(row, gridColOtherPhone));
        daiTextField_otherFax.setText((String)GRID.get(row, gridColOtherFax));
        daiTextField_mobilePhone.setText((String)GRID.get(row, gridColMoblePhone));
        daiTextField_otherEmail.setText((String)GRID.get(row, gridColOtherEmail));
		textAreaControl_note.setText((String)GRID.get(row, gridColNote));
	}

    protected void disableEntryFields(boolean disable)
    {
        if (disable)
        {
            daiTextField_name.setDisabled(true);
            daiTextField_officePhone.setDisabled(true);
            daiTextField_officeFax.setDisabled(true);
            daiTextField_pager.setDisabled(true);
            daiTextField_officeEmail.setDisabled(true);
            daiTextField_web.setDisabled(true);
            daiTextField_title.setDisabled(true);
            daiTextField_otherPhone.setDisabled(true);
            daiTextField_otherFax.setDisabled(true);
            daiTextField_mobilePhone.setDisabled(true);
            daiTextField_otherEmail.setDisabled(true);
	        textAreaControl_note.setDisabled(true);
        } else {
            daiTextField_name.setDisabled(false);
            daiTextField_officePhone.setDisabled(false);
            daiTextField_officeFax.setDisabled(false);
            daiTextField_pager.setDisabled(false);
            daiTextField_officeEmail.setDisabled(false);
            daiTextField_web.setDisabled(false);
            daiTextField_title.setDisabled(false);
            daiTextField_otherPhone.setDisabled(false);
            daiTextField_otherFax.setDisabled(false);
            daiTextField_mobilePhone.setDisabled(false);
            daiTextField_otherEmail.setDisabled(false);
            textAreaControl_note.setDisabled(false);
        }
    }
}
