
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.carrier;

import java.util.Calendar;
import java.util.Vector;

import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiDetailPanel;
import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.customer_journalObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiLabel;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;

public class carrierJournalPanel extends daiDetailPanel
{
	XYLayout    xYLayout1 = new XYLayout();

	daiTextArea textAreaControl_note = new daiTextArea();
	daiTextField fieldControl_subject = new daiTextField();

    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();
    daiLabel daiLabel_notel = new daiLabel();
    daiLabel daiLabel_subject = new daiLabel();
    GroupBox groupBox1 = new GroupBox();

    int gridColObj          = 0;
    int gridColId           = 1;
    int gridColDateCreated  = 2;
    int gridColCreatedBy    = 3;
    int gridColSubject      = 4;
    int gridColNote         = 5;
    XYLayout xYLayout2 = new XYLayout();

	public carrierJournalPanel(JFrame container, daiFrame parentFrame, customer_journalObj obj)
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
        groupBox1.setLayout(xYLayout2);
        GRID.createColumns(6);
		GRID.setHeaderNames(new String[]{"OBJ", "Id#", "Date Created", "Created By", "Subject", "Note"});
        //Clear out the GridControl
		GRID.removeAllRows();

		xYLayout1.setHeight(492);
		xYLayout1.setWidth(619);
		this.setLayout(xYLayout1);
        daiLabel_notel.setText("Note:");
        daiLabel_subject.setText("Subject:");
        groupBox1.setBackground(daiColors.PanelColor);
        groupBox1.setLabel("Journal Entry");
        this.add(textAreaControl_note, new XYConstraints(80, 136, 502, 94));
        this.add(daiLabel_subject, new XYConstraints(18, 95, 54, 21));
        this.add(fieldControl_subject, new XYConstraints(80, 96, 299, -1));
        this.add(userIdDateCreatedPanel, new XYConstraints(426, 70, -1, -1));
        this.add(daiLabel_notel, new XYConstraints(43, 134, -1, -1));
        this.add(grid, new XYConstraints(24, 277, 563, 204));
        this.add(gridController, new XYConstraints(20, 27, 351, 42));
        this.add(groupBox1, new XYConstraints(8, 6, 604, 260));

        //Hide grid columns used for hidden data.
        GRID.hideColumn(0);
        GRID.hideColumn(0); //This is the new zero column.

        disableEntryFields(true);
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        customer_journalObj obj = new customer_journalObj();
        customer_journalObj tempObj = (customer_journalObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

        return obj;
    }

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		customer_journalObj itemObj;

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
			itemObj = (customer_journalObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, 2, itemObj.get_date_created());
			GRID.set(i, 3, itemObj.get_created_by());
            GRID.set(i, 4, itemObj.get_subject());
			GRID.set(i, 5, itemObj.get_note());
			GRID.set(i, OBJ_COL_POS, itemObj);
		}

		//Clear out the entry fields.
		clearEntryFields();
	}

	protected void updateBusinessObjFromGrid()
	{
		customer_journalObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (customer_journalObj)GRID.get(i, OBJ_COL_POS);

			tempObj.set_date_created((String)GRID.get(i, 2));
			tempObj.set_created_by((String)GRID.get(i, 3));
			tempObj.set_subject((String)GRID.get(i, 4));
			tempObj.set_note((String)GRID.get(i, 5));

			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

	protected void clearEntryFields()
	{
		//Clear out the data entry fields.
		fieldControl_subject.setText(null);
		textAreaControl_note.setText(null);
        userIdDateCreatedPanel.setDateCreated(null);
        userIdDateCreatedPanel.setUserId(null);

        fieldControl_subject.requestFocus();
    }

	protected void copyEntryFieldsToGrid()
	{
		int row = GRID.getActiveRow();
        SessionMetaData sessionMeta = null;
        sessionMeta = sessionMeta.getInstance();

        Calendar now = Calendar.getInstance();
        String nowDate =    now.get(now.MONTH)+1 + "/" +
                            now.get(now.DAY_OF_MONTH) + "/" +
                            now.get(now.YEAR);

        GRID.set(row, 2, nowDate);
        GRID.set(row, 3, sessionMeta.getUserId());
		GRID.set(row, 4, fieldControl_subject.getText());
		GRID.set(row, 5, textAreaControl_note.getText());
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
		fieldControl_subject.setText((String)GRID.get(row, gridColSubject));
		textAreaControl_note.setText((String)GRID.get(row, gridColNote));

        userIdDateCreatedPanel.setDateCreated((String)GRID.get(row, gridColDateCreated));
        userIdDateCreatedPanel.setUserId((String)GRID.get(row, gridColCreatedBy));
	}

    protected void disableEntryFields(boolean disable)
    {
        if (disable)
        {
		    fieldControl_subject.setDisabled(true);
		    textAreaControl_note.setDisabled(true);
        } else {
		    fieldControl_subject.setDisabled(false);
		    textAreaControl_note.setDisabled(false);
        }
    }

}





