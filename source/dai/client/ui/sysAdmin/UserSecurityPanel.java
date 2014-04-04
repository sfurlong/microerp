
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.sysAdmin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;

import dai.client.clientShared.daiDetailPanel;
import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.user_securityObj;
import dai.shared.cmnSvcs.daiSessionSecurity;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiGrid;

public class UserSecurityPanel extends daiDetailPanel
{
    //Grid positions
    int gridColCompId       = 2;
    int gridColDescrip      = 3;
    int gridColRead         = 4;

    BorderLayout borderLayout1 = new BorderLayout();

	public UserSecurityPanel(JFrame container, daiFrame parentFrame, user_securityObj obj)
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

	public UserSecurityPanel(JFrame parentFrame, user_securityObj obj)
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
        GRID.setPreferredSize(new Dimension(200, 150));
        GRID.setMinimumSize(new Dimension(200, 150));

        //Grid Setup Stuff
        GRID.createColumns(new int[]{daiGrid.CHAR_COL_TYPE,
                                     daiGrid.CHAR_COL_TYPE,
                                     daiGrid.CHAR_COL_TYPE,
                                     daiGrid.CHAR_COL_TYPE,
                                     daiGrid.CHECKBOX_COL_TYPE});
		GRID.setHeaderNames(new String[]
                {" ", "Obj", "Component Id", "Description", "Allowed"});
        GRID.hideColumn(1); //Obj
        GRID.setColumnSize(0, daiGrid.DEFAULT_ITEM_NUM_COL_WIDTH);
        GRID.setColumnSize(1, daiGrid.DEFAULT_DESC_COL_WIDTH);
        GRID.setColumnSize(2, daiGrid.DEFAULT_DESC_COL_WIDTH);

        this.setLayout(borderLayout1);
        this.add(grid, BorderLayout.CENTER);
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        user_securityObj obj = new user_securityObj();
        user_securityObj tempObj = (user_securityObj)BUSINESS_OBJ;

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
		user_securityObj tempObj;

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
			tempObj = (user_securityObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, gridColCompId, tempObj.get_component_id());
			GRID.set(i, gridColDescrip, tempObj.get_description());
            String permission = tempObj.get_read_permission();
            if (permission != null && permission.equals("Y")) {
                GRID.set(i, gridColRead, Boolean.TRUE);
            }
			GRID.set(i, OBJ_COL_POS, tempObj);
		}

        syncSecurityComponents();
	}

	protected void updateBusinessObjFromGrid()
	{
		user_securityObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (user_securityObj)GRID.get(i, OBJ_COL_POS);

            if (GRID.get(i, gridColRead).equals(Boolean.TRUE)) {
                tempObj.set_read_permission("Y");
            } else {
                tempObj.set_read_permission("N");
            }

			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}

	protected void clearEntryFields()
	{
		//Clear out the data entry fields.
    }

	protected void copyEntryFieldsToGrid()
	{
        //Nothing to do.
	}

	protected void copyGridToEntryFields()
	{
        //Nothing to do.
	}

    protected void disableEntryFields(boolean flag)
    {
    }

    private void syncSecurityComponents()
    {
        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

        daiSessionSecurity sessionSecurity = new daiSessionSecurity();
        Vector vect = sessionSecurity.getAllSecurityComponents();
        boolean found = false;

        for (int i=0; i<vect.size(); i++) {
            String compId = (String)vect.elementAt(i);
            for (int j=0; j<GRID.getRowCount(); j++) {
                if (compId.equals(GRID.get(j, gridColCompId))){
                    found = true;
                }
            }

            if (!found) {
            try {
                int detailId = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
                                            csDBAdapter.SEQUENCE_GENERIC_DETAIL_ID);
                //Add to grid
                String sqlStmt = " insert into user_security " +
                    "(id, locality, detail_id, component_id) " +
                    " values ('" + BUSINESS_OBJ.get_id() + "', " +
                    "'SUPER'" + ", " +
                    Integer.toString(detailId) + ", " +
                    "'" + compId + "')";
                dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
                e.printStackTrace();
            }
            }
            found = false;
        }
    }

    //This is necessary to allow the frame to set this protected method
    //because the grid does not fire change events.
    public void setPanelIsDirty() {
        this._panelIsDirty = true;
    }
}
