//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dai.shared.businessObjs.BusinessObject;
import daiBeans.daiGrid;
import daiBeans.daiGridController;

public abstract class daiDetailSubPanel extends daiPanel
{
       //Called by the master header panel.
	abstract public void updateBusinessObjFromGrid();
      //Called by the master header panel.
       abstract public void updateGridFromBusinessObj(Vector objVect);
       abstract public int GRID_refresh();

        protected daiGridController GRID_CONTROLLER = new daiGridController();
	protected daiGrid GRID;
        protected int OBJ_COL_POS = 1;

	public daiDetailSubPanel(JFrame container, daiFrame containerFrame, BusinessObject obj)
	{
   		super(container, containerFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

        private void jbInit() throws Exception
	{

		GRID_CONTROLLER.addActionListener(new java.awt.event.ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                //gridController_actionPerformed(e);
            }
        });

        //Get the instance of the GRID so we can
        //extend it and let subclasses extend it also.
        GRID = GRID_CONTROLLER.getGridInstance();
        GRID.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                GRID_rowSelected(e);
            }
        });

        GRID.showRowNumbers();

        //Clear out all the rows and entry fields.
		GRID.removeAllRows();

	}

	public int query(String id)
	{
        //Don't need to do anything here.  The
        //main header panel will take care of all the
        //query operations.
		return 0;
	}



        //Called by base Frame class to set the ID when a new TransID is inserted
	//We know the rest of the immutables from when we were instantiated.
//	public void setId(String id)
//	{
//		BUSINESS_OBJ.set_id(id);
//	}

	public int update()
	{
        //Don't need to do anything here.  The
        //main header panel will take care of all the
        //DB operations.
		return 0;
	}

	public int delete()
	{
        //Just clear the entry fields on the panel.  the
        //main header panel will take care of the rest.
	updateBusinessObjFromGrid();
		return 0;
	}

	protected int insert(String id)
	{
        //Just update the entry fields on the panel.  the
        //main header panel will take care of the rest.
        updateBusinessObjFromGrid();

		return 0;
	}

	public BusinessObject getActiveBusinessObj()
	{
		return BUSINESS_OBJ;
	}

	public int prepareSaveAs(String newId)
	{
		//Descendents should override if needed.
		return 0;
	}

    //Allow Main Panel to handle refresh
    public int refresh()
    {
		return 0;
    }
    //Override of abstract method.  Typically won't be needed by decendants
    //of this class.
    public int persistPanelData() {
        return 0;
    }
    private void GRID_rowSelected(ListSelectionEvent e)
	{
		if (GRID.getRowCount() > 0)
		{
        	if (GRID_CONTROLLER.isDetailCommitted())
            {
                //Update the Panel with Grid data
                //copyGridToEntryFields();
                //disableEntryFields(true);

                //Update the status bar with the number of detail items
                CONTAINER_FRAME.setStatusRight("");
            }

		}
	}
}

