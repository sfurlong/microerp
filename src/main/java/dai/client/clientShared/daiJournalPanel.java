
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;


import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiLabel;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;

public abstract class daiJournalPanel extends daiDetailPanel
{

	daiTextArea textAreaControl_note = new daiTextArea();
	daiTextField fieldControl_subject = new daiTextField();

    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();
    daiLabel daiLabel_notel = new daiLabel("Note:");
    daiLabel daiLabel_subject = new daiLabel("Subject:");
    GroupBox groupBox = new GroupBox();

    protected static int COL_DATE_CREATED   = 2;
    protected static int COL_CREATED_BY     = 3;
    protected static int COL_SUBJECT        = 4;
    protected static int COL_NOTE           = 5;

    XYLayout xYLayout2 = new XYLayout();
    BoxLayout2 boxLayout21 = new BoxLayout2();

	public daiJournalPanel(JFrame container, daiFrame parentFrame, BusinessObject obj)
	{
		super(container, parentFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			//Log to dialog, system.out, disk.
			LOGGER.logError(CONTAINER, "Error Initializing Order Journal Detail panel.\n" + ex.getLocalizedMessage());
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
        GRID.createColumns(6);
		GRID.setHeaderNames(new String[]{" ", "OBJ", "Date Created", "Created By", "Subject", "Note"});
        //Clear out the GridControl
		GRID.removeAllRows();
        //Hide grid columns used for hidden data.
        GRID.hideColumn(1);  //Hide the Obj Column
        GRID.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);
        GRID.setColumnSize(3, 200);
        GRID.setColumnSize(4, 200);

        grid.setPreferredSize(new Dimension(589, 100));
        grid.setMinimumSize(new Dimension(589, 100));

        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        groupBox.setLayout(xYLayout2);
        groupBox.setBackground(daiColors.PanelColor);
        groupBox.setLabel("Journal Entry");
        groupBox.add(gridController, new XYConstraints(-3, 0, 351, 42));
        groupBox.add(userIdDateCreatedPanel, new XYConstraints(352, 34, -1, 49));
        groupBox.add(daiLabel_subject, new XYConstraints(-4, 54, 44, 21));
        groupBox.add(daiLabel_notel, new XYConstraints(15, 85, -1, -1));
        groupBox.add(fieldControl_subject, new XYConstraints(48, 55, 304, -1));
        groupBox.add(textAreaControl_note, new XYConstraints(48, 87, 457, 94));

		this.setLayout(boxLayout21);
        this.add(groupBox, null);
        this.add(grid, null);

        disableEntryFields(true);
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

        GRID.set(row, COL_DATE_CREATED, nowDate);
        GRID.set(row, COL_CREATED_BY, sessionMeta.getUserId());
		GRID.set(row, COL_SUBJECT, fieldControl_subject.getText());
		GRID.set(row, COL_NOTE, textAreaControl_note.getText());
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
		fieldControl_subject.setText((String)GRID.get(row, COL_SUBJECT));
		textAreaControl_note.setText((String)GRID.get(row, COL_NOTE));

        userIdDateCreatedPanel.setDateCreated((String)GRID.get(row, COL_DATE_CREATED));
        userIdDateCreatedPanel.setUserId((String)GRID.get(row, COL_CREATED_BY));
	}

    protected void disableEntryFields(boolean disable)
    {
        fieldControl_subject.setDisabled(disable);
		textAreaControl_note.setDisabled(disable);
    }
}
