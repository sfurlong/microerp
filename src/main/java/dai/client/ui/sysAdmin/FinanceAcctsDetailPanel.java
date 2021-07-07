
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.sysAdmin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Calendar;
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
import dai.shared.businessObjs.account_detailObj;
import dai.shared.cmnSvcs.daiFormatUtil;
import daiBeans.daiButton;
import daiBeans.daiCheckBox;
import daiBeans.daiCurrencyField;
import daiBeans.daiDateField;
import daiBeans.daiGrid;
import daiBeans.daiGridController;
import daiBeans.daiLabel;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;

public class FinanceAcctsDetailPanel extends daiDetailPanel
{
	XYLayout    xYLayout1   = new XYLayout();
    JPanel      entryPanel  = new JPanel();

    //Data Entry Field Controls
	daiDateField fieldControl_date = new daiDateField();
	daiTextField fieldControl_ref = new daiTextField();
	daiTextField fieldControl_type = new daiTextField();
	daiCurrencyField fieldControl_debit = new daiCurrencyField();
	daiCurrencyField fieldControl_credit = new daiCurrencyField();
	daiTextArea  fieldControl_note = new daiTextArea();
    daiCheckBox  isReconciled = new daiCheckBox("Is Reconciled");
    daiCheckBox  isManualEntry = new daiCheckBox("Is Manual Entry");
    GroupBox    entryBox    = new GroupBox("Journal Entry");

    //Label Controls
	daiLabel label_date     = new daiLabel("Date:");
	daiLabel label_ref      = new daiLabel("Reference#:");
	daiLabel label_type     = new daiLabel("Type:");
	daiLabel label_debit    = new daiLabel("Debit:");
	daiLabel label_credit   = new daiLabel("Credit:");
	daiLabel label_note    = new daiLabel("Note:");

    //Grid positions
    int gridColDate         = 2;
    int gridColRef          = 3;
    int gridColType         = 4;
    int gridColRefAcct      = 5;
    int gridColDebit        = 6;
    int gridColCredit       = 7;
    int gridColNote         = 8;
    int gridColIsRecon      = 9;
    int gridColIsManual     = 10;

    XYLayout xYLayout2 = new XYLayout();
    BoxLayout2 boxLayout21 = new BoxLayout2();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel dateSelectPanel = new JPanel();
    daiDateField beginDate = new daiDateField();
    daiDateField endDate = new daiDateField();
    daiButton refreshGridButton = new daiButton();

	public FinanceAcctsDetailPanel(JFrame container, daiFrame parentFrame, account_detailObj obj)
	{
		super(container, parentFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			//Log to dialog, system.out, disk.
			LOGGER.logError(CONTAINER, "Error Initializing Account Detail panel.\n" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	public FinanceAcctsDetailPanel(JFrame parentFrame, account_detailObj obj)
	{
		super(parentFrame, obj);

		try
		{
			jbInit();

            //Get all the detail records for this Account.
            query(obj.get_id());

		} catch (Exception ex)
		{
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

        //Grid Setup Stuff
        GRID.createColumns(11);
		GRID.setHeaderNames(new String[]
                {" ", "Obj", "Date", "Ref", "Type", "Ref Acct", "Debit",
                "Credit", "Note", "Reconciled", "Manual"});
        GRID.hideColumn(1); //Obj
        GRID.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);
		xYLayout1.setHeight(482);
		xYLayout1.setWidth(619);
		entryBox.setLayout(xYLayout1);

        boxLayout21.setAxis(BoxLayout.Y_AXIS);

        //Add Controls to Entry Panel
        entryBox.setMinimumSize(new Dimension(500, 180));
        entryBox.setPreferredSize(new Dimension(500, 180));

        //Set up the date select panel.
        dateSelectPanel.setBackground(daiColors.PanelColor);
        refreshGridButton.setText("Refresh");
        refreshGridButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshGridButton_actionPerformed(e);
            }
        });
        dateSelectPanel.add(new daiLabel("Begin Date:"));
        dateSelectPanel.add(beginDate);
        dateSelectPanel.add(new daiLabel("End Date:"));
        dateSelectPanel.add(endDate);
        dateSelectPanel.add(refreshGridButton);

        grid.setPreferredSize(new Dimension(550, 105));
        grid.setMinimumSize(new Dimension(550, 105));

        entryBox.add(label_date, new XYConstraints(43, 1, -1, -1));
        entryBox.add(fieldControl_date, new XYConstraints(72, 1, -1, -1));
        entryBox.add(fieldControl_type, new XYConstraints(72, 22, 209, -1));
        entryBox.add(fieldControl_ref, new XYConstraints(72, 45, 209, -1));
        entryBox.add(fieldControl_debit, new XYConstraints(72, 67, -1, -1));
        entryBox.add(fieldControl_credit, new XYConstraints(72, 89, -1, -1));
        entryBox.add(fieldControl_note, new XYConstraints(72, 111, 376, 43));
        entryBox.add(label_type, new XYConstraints(14, 22, 54, 21));
        entryBox.add(label_ref, new XYConstraints(8, 48, -1, -1));
        entryBox.add(label_debit, new XYConstraints(41, 68, -1, -1));
        entryBox.add(label_credit, new XYConstraints(37, 89, -1, -1));
        entryBox.add(label_note, new XYConstraints(43, 110, -1, -1));
        entryBox.add(isReconciled, new XYConstraints(403, 0, 97, -1));
        entryBox.add(isManualEntry, new XYConstraints(403, 22, 117, -1));

        disableEntryFields(true);

        this.setLayout(boxLayout21);
        this.add(gridController, null);
        this.add(entryBox, null);
        this.add(dateSelectPanel, null);
        this.add(grid, null);
	}

    //Overridden from the base class so that we can filter by date.
	public int query(String id)
	{
		Vector objVect = new Vector();
		String exp = "";

		//Set the ID in BUSINESS_OBJ so that other operations
        //will know what the current ID we are working with is.
        setId(id);

		DBAttributes attribs[] = BUSINESS_OBJ.getImmutableAttribs();

		for (int i=0; i < attribs.length; i++)
		{
			if (i != 0) exp = exp + " and ";
			exp = exp + attribs[i].getName() +
				  " = " +
				  "'" + attribs[i].getValue().trim() + "'";
		}

        exp += " and trans_date between '" + beginDate.getText() +
                "' and '" + endDate.getText() + "'";

        //Add order by clause for sorting.
        exp = exp + " order by trans_date ";

		try
		{
			objVect = _dbAdapter.queryByExpression( _sessionMeta.getClientServerSecurity(),
                                                    BUSINESS_OBJ,
                                                    exp);

            //Loop through all the retreived business objects and indicate
            //that they already exist in the database.  This is how we know
            //to update instead of insert.
            for (int i=0; i<objVect.size(); i++) {
                BusinessObject obj = (BusinessObject)objVect.elementAt(i);
                obj.EXISTS_IN_DB = true;
                objVect.setElementAt(obj, i);
            }

			//Update the UI even if objVect size is zero.  We don't want the
			//previous id's rows to be hanging around.
			updateGridFromBusinessObj(objVect);

            GRID.clearAllRowSelections();
            GRID.setRowFocus(0);

		} catch (Exception e)
		{
			//Log to dialog, system.out, disk.
			e.printStackTrace();
            String msg = this.getClass().getName() + "::query failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
		}

		return 0;
	}

    //Override of method from base class.
   	public int refresh() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        endDate.setText(daiFormatUtil.getCurrentDate());
        beginDate.setCalendar(cal);
        super.refresh();
        return 0;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        account_detailObj obj = new account_detailObj();
        account_detailObj tempObj = (account_detailObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

        return obj;
    }

    protected void update_UI(BusinessObject obj)
    {
    }

    protected void update_BusinessObj()
    {
    }

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		account_detailObj tempObj;

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
			tempObj = (account_detailObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, gridColDate, tempObj.get_trans_date());
			GRID.set(i, gridColRef, tempObj.get_trans_ref());
			GRID.set(i, gridColType, tempObj.get_trans_type());
			GRID.set(i, gridColRefAcct, tempObj.get_trans_ref_acct());
			GRID.set(i, gridColDebit, tempObj.get_debit());
			GRID.set(i, gridColCredit, tempObj.get_credit());
			GRID.set(i, gridColNote, tempObj.get_note1());
            GRID.set(i, gridColIsRecon, tempObj.get_reconcile_status());
            GRID.set(i, gridColIsManual, tempObj.get_user1());
			GRID.set(i, OBJ_COL_POS, tempObj);
		}

		//Clear out the entry fields.
		clearEntryFields();
	}

	protected void updateBusinessObjFromGrid()
	{
		account_detailObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (account_detailObj)GRID.get(i, OBJ_COL_POS);

			tempObj.set_trans_date((String)GRID.get(i, gridColDate));
			tempObj.set_trans_ref((String)GRID.get(i, gridColRef));
			tempObj.set_trans_type((String)GRID.get(i, gridColType));
			tempObj.set_trans_ref_acct((String)GRID.get(i, gridColRefAcct));
			tempObj.set_debit((String)GRID.get(i, gridColDebit));
			tempObj.set_credit((String)GRID.get(i, gridColCredit));
			tempObj.set_note1((String)GRID.get(i, gridColNote));
            tempObj.set_user1((String)GRID.get(i, gridColIsManual));

			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

	protected void clearEntryFields()
	{
		//Clear out the data entry fields.
		fieldControl_date.setText(null);
		fieldControl_ref.setText(null);
		fieldControl_type.setText(null);
		fieldControl_debit.setText(null);
		fieldControl_credit.setText(null);
		fieldControl_note.setText(null);
        isReconciled.setValue(null);
        isManualEntry.setValue(null);
        fieldControl_date.requestFocus();
    }

	protected void copyEntryFieldsToGrid()
	{
		int row = GRID.getActiveRow();

        GRID.set(row, gridColDate, fieldControl_date.getText());
        GRID.set(row, gridColRef, fieldControl_ref.getText());
        GRID.set(row, gridColType, fieldControl_type.getText());
        GRID.set(row, gridColDebit, fieldControl_debit.getText());
        GRID.set(row, gridColCredit, fieldControl_credit.getText());
        GRID.set(row, gridColNote, fieldControl_note.getText());
        GRID.set(row, gridColIsRecon, isReconciled.getValue());
        GRID.set(row, gridColIsManual, isManualEntry.getValue());
	}

	protected void copyGridToEntryFields()
	{
		int row = GRID.getActiveRow();

		//Update the Panel with Grid data
		fieldControl_date.setText((String)GRID.get(row, gridColDate));
		fieldControl_ref.setText((String)GRID.get(row, gridColRef));
		fieldControl_type.setText((String)GRID.get(row, gridColType));
		fieldControl_debit.setText((String)GRID.get(row, gridColDebit));
		fieldControl_credit.setText((String)GRID.get(row, gridColCredit));
		fieldControl_note.setText((String)GRID.get(row, gridColNote));
        isReconciled.setValue((String)GRID.get(row, gridColIsRecon));
        isManualEntry.setValue((String)GRID.get(row, gridColIsManual));
	}

    protected void disableEntryFields(boolean flag)
    {
		    fieldControl_date.setDisabled(flag);
		    fieldControl_ref.setDisabled(flag);
		    fieldControl_type.setDisabled(flag);
		    fieldControl_debit.setDisabled(flag);
		    fieldControl_credit.setDisabled(flag);
		    fieldControl_note.setDisabled(flag);
            isReconciled.setDisabled(flag);
            isManualEntry.setDisabled(flag);
    }

    void refreshGridButton_actionPerformed(ActionEvent e) {
        if (BUSINESS_OBJ.get_id() != null) {
            query(BUSINESS_OBJ.get_id());
        }
    }
}
