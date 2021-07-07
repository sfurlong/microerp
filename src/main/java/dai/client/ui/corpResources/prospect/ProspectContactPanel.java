
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.prospect;


import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import com.borland.jbcl.layout.BoxLayout2;

import dai.client.clientShared.daiDetailPanel;
import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.customer_contactObj;
import daiBeans.daiContactsPanel;
import daiBeans.daiGrid;
import daiBeans.daiGridController;

public class ProspectContactPanel extends daiDetailPanel
{
    //Constants for Grid column positions
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
    final int gridColIsPrimary      = 14;

    //The panel containing the contacts
    daiContactsPanel contactPanel = new daiContactsPanel();
    BoxLayout2 boxLayout21 = new BoxLayout2();

	public ProspectContactPanel(JFrame container, daiFrame parentFrame, customer_contactObj obj)
	{
		super(container, parentFrame, obj);

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
        GRID.createColumns(15);
		GRID.setHeaderNames(new String[]{"", "OBJ", "Name", "Title",
            "Office Phone", "Other Phone", "Office Fax", "Other Fax",
            "Office Email", "Other Email", "Pager", "Mobile Phone", "Web", "Note", "Is Primary"});
        GRID.hideColumn(1);  //Hide OBJ col
        //Clear out the GridControl
		GRID.removeAllRows();
        GRID.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);
        contactPanel.setGridController(gridController);

		this.setLayout(boxLayout21);


        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        this.add(contactPanel, null);
        this.add(grid, null);

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

			GRID.set(i, OBJ_COL_POS, itemObj);
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
            GRID.set(i, gridColIsPrimary, itemObj.get_is_primary());
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
			tempObj = (customer_contactObj)GRID.get(i, OBJ_COL_POS);

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
            tempObj.set_is_primary((String)GRID.get(i, gridColIsPrimary));

			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

	protected void clearEntryFields()
	{
		//Clear out the data entry fields.
        contactPanel.clearEntryFields();
    }

	protected void copyEntryFieldsToGrid()
	{
		int row = GRID.getActiveRow();
        GRID.set(row, gridColName, contactPanel.getName());
        GRID.set(row, gridColOfficePhone, contactPanel.getOfficePhone());
        GRID.set(row, gridColOfficeFax, contactPanel.getOfficeFax());
        GRID.set(row, gridColPager, contactPanel.getPager());
        GRID.set(row, gridColOfficeEmail, contactPanel.getOfficeEmail());
        GRID.set(row, gridColWeb, contactPanel.getWeb());
        GRID.set(row, gridColTitle, contactPanel.getTitle());
        GRID.set(row, gridColOtherPhone, contactPanel.getOtherPhone());
        GRID.set(row, gridColOtherFax, contactPanel.getOtherFax());
        GRID.set(row, gridColMoblePhone, contactPanel.getMobilePhone());
        GRID.set(row, gridColOtherEmail, contactPanel.getOtherEmail());
		GRID.set(row, gridColNote, contactPanel.getNote());
        GRID.set(row, gridColIsPrimary, contactPanel.getIsPrimary());
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
        contactPanel.setName((String)GRID.get(row, gridColName));
        contactPanel.setOfficePhone((String)GRID.get(row, gridColOfficePhone));
        contactPanel.setOfficeFax((String)GRID.get(row, gridColOfficeFax));
        contactPanel.setPager((String)GRID.get(row, gridColPager));
        contactPanel.setOfficeEmail((String)GRID.get(row, gridColOfficeEmail));
        contactPanel.setWeb((String)GRID.get(row, gridColWeb));
        contactPanel.setTitle((String)GRID.get(row, gridColTitle));
        contactPanel.setOtherPhone((String)GRID.get(row, gridColOtherPhone));
        contactPanel.setOtherFax((String)GRID.get(row, gridColOtherFax));
        contactPanel.setMobilePhone((String)GRID.get(row, gridColMoblePhone));
        contactPanel.setOtherEmail((String)GRID.get(row, gridColOtherEmail));
		contactPanel.setNote((String)GRID.get(row, gridColNote));
        contactPanel.setIsPrimary((String)GRID.get(row, gridColIsPrimary));
	}

    protected void disableEntryFields(boolean disable)
    {
        contactPanel.disableEntryFields(disable);
    }
}
