
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.borland.jbcl.model.MatrixLocation;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiGrid;
import daiBeans.daiGridController;


public abstract class daiHeaderGridPanel extends daiPanel
{
    protected csDBAdapterFactory  _dbAdapterFactory;
	protected csDBAdapter         _dbAdapter;

	//All Detail Panels will have GridControls.
	protected daiGrid GRID = new daiGrid();
	protected daiGridController GRID_CONTROLLER = new daiGridController();

	protected int OBJ_COL_POS = 0;
	protected int ID_COL_POS = 1;

    //This is the currently active grid row in which changes are being made.
    private MatrixLocation currentRow;
  	JOptionPane choiceMessage = new JOptionPane();

    //This is used to determine wether we should do an intert or update
    //when the user presses the ENTER button on the grid controller.
    //If SAVE_IS_UPDATE is false we want to Insert the new record.
    //If SAVE_IS_UPDATE is true we want to Update the new record.
    private boolean SAVE_IS_UPDATE = false;

//********************************************************************//
//                  CONSTRUCTORS                                      //
//********************************************************************//
	public daiHeaderGridPanel(JFrame container, daiFrame containerFrame, BusinessObject obj)
	{
		super(container, containerFrame, obj);

		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::constructor failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(container, msg);
		}
	}

    //Used for panels that are not intended to work in conjunction with
    //daiFrame.
	public daiHeaderGridPanel(JFrame containerFrame, BusinessObject obj)
	{
   		super(containerFrame, obj);

		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::constructor failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(containerFrame, msg);
		}
	}

	private void jbInit() throws Exception
	{
		GRID_CONTROLLER.addActionListener(new java.awt.event.ActionListener(){
									public void actionPerformed(ActionEvent e){
									gridController_actionPerformed(e);}});

        GRID = GRID_CONTROLLER.getGridInstance();
		GRID.setFont(new Font("Dialog", 0, 10));
        GRID.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e)
            {
                GRID_rowSelected(e);
            }
        });

        //Clear out all the rows and entry fields.
		GRID.removeAllRows();

        _dbAdapterFactory = _dbAdapterFactory.getInstance();
        _dbAdapter      = _dbAdapterFactory.getDBAdapter();
        _sessionMeta    = _sessionMeta.getInstance();
	}

//********************************************************************//
//                  PUBLIC                                            //
//********************************************************************//
	public int refresh()
	{
        //Clear out all the rows and entry fields.
		GRID.removeAllRows();
        clearEntryFields();
		return 0;
	}

	//Called by base Frame class to set the ID when a new TransID is inserted
	//We know the rest of the immutables from when we were instantiated.
	public void setId(String id)
	{
		BUSINESS_OBJ.set_id(id);
	}


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

		try
		{
			objVect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                                                BUSINESS_OBJ,
                                                exp);

			//Update the UI even if objVect size is zero.  We don't want the
			//previous id's rows to be hanging around.
			updateGridFromBusinessObj(objVect);

		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::query failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
		}

		return 0;
	}

	public int insert(String id)
	{
		//This is called when the header does inserts.
        //Don't need to do much here, but make sure the base business
        //object has the new trans id.
        BUSINESS_OBJ.set_id(id);
		return 0;
	}

	public int delete()
	{
		//No need to do anything here.  This is called when the header does
		//a delete.  DB should do a cascade delete.
		return 0;
	}

	public int update()
	{
        //Incase update was called by mistake do this check.  This
        //may be necessary when this panel is used with daiChoicesFrame.
        if (!SAVE_IS_UPDATE) {
            saveDetailRow();
            return 0;
        }

		String exp = "";

        //If there's nothing in the grid, let's get out of here.
        if (GRID.getRowCount() <= 0) return 0;

		copyEntryFieldsToGrid();
   		updateBusinessObjFromGrid();

		//Select all the IDs from the
		DBAttributes attribs[] = BUSINESS_OBJ.getImmutableAttribs();

		//The ID will always be in GRID column 1.
        int row = GRID.getSelectedRow();
        exp = " id = '" + (String)GRID.get(row, ID_COL_POS) + "'";

		for (int i=0; i < attribs.length; i++)
		{
            exp =  exp + " and ";
			exp = exp + attribs[i].getName() +
				  " = " +
				  "'" + attribs[i].getValue().trim() + "'";
		}

		try
		{
			//The Business Object for each row in the grid is
			//always stored in column 0.
			BusinessObject tempObj = (BusinessObject)GRID.get(row, OBJ_COL_POS);

			_dbAdapter.update(_sessionMeta.getClientServerSecurity(),
                                tempObj,
                                exp);
    	} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::update failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
		}
		return 0;
	}

	public Vector getActiveBusinessObjVector()
	{
		Vector objVect = new Vector();

		for (int i=0; i < GRID.getRowCount(); i++)
		{
			objVect.addElement(GRID.get(i, OBJ_COL_POS));
		}

		return objVect;
	}

//********************************************************************//
//                  PROTECTED                                         //
//********************************************************************//
   	abstract protected void copyEntryFieldsToGrid();
    abstract protected void copyGridToEntryFields();
	abstract protected void updateBusinessObjFromGrid();
	abstract protected void updateGridFromBusinessObj(Vector objVect);
    abstract protected void clearEntryFields();
    abstract protected void disableEntryFields(boolean disable);

//********************************************************************//
//                  PRIVATE                                           //
//********************************************************************//
    private SessionMetaData _sessionMeta;

	//Navigator Event Handler.
	void gridController_actionPerformed(ActionEvent e)
	{
		if (e.getActionCommand() == "NEW")
        {
            SAVE_IS_UPDATE = false;
          	newDetailRow();
        }
		if (e.getActionCommand() == "ENTER")
        {
            if (SAVE_IS_UPDATE) update();
            else saveDetailRow();
        }
		if (e.getActionCommand() == "EDIT")
        {
            SAVE_IS_UPDATE = true;
			disableEntryFields(false);
        }
		if (e.getActionCommand() == "CANCEL")
        {
            clearEntryFields();
            disableEntryFields(true);
        }
		if (e.getActionCommand() == "DELETE")
        {
           	deleteDetailRow();
        }
	}

  	void newDetailRow()
	{
        //Assume the Grid Controller already added the new row
        //to the grid....

		//Clear out the data entry fields.
        clearEntryFields();

        disableEntryFields(false);
	}

	void deleteDetailRow()
	{
        int row = GRID.getSelectedRow();
		String exp = "";

		BusinessObject tempObj = (BusinessObject)GRID.get(row, OBJ_COL_POS);

        //The row may not have been entered yet.  Thus not business obj
        //will exist.
        if (tempObj == null)
        {
			GRID.deleteRow(row);
            return;
        }

		//Select all the IDs from the
		DBAttributes attribs[] = tempObj.getImmutableAttribs();

		for (int i=0; i < attribs.length; i++)
		{
			exp = exp + " and " +
				  attribs[i].getName() +
				  " = " +
				  "'" + attribs[i].getValue().trim() + "'";
		}

		exp = " id = " +
			  "'" + tempObj.get_id() + "'" +
			  exp;

		try
		{
			//Delete from the DB.
			_dbAdapter.delete(_sessionMeta.getClientServerSecurity(),
                            tempObj,
                            exp);

			//Deletes the current row from the UI
			GRID.deleteRow(row);

		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::deleteDetailRow failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
		}
	}

	void saveDetailRow()
	{
        int row = GRID.getSelectedRow();

        BusinessObject itemObj = getNewBusinessObjInstance();
    	//Add the new Item Object to the grid.
		GRID.set(row, OBJ_COL_POS, itemObj);

        //These routines will update the itemObj.
		copyEntryFieldsToGrid();
		updateBusinessObjFromGrid();

   		try
    	{
	    	//Add the new item to the DB.
		    _dbAdapter.insert(_sessionMeta.getClientServerSecurity(),
                                itemObj);
   		} catch (Exception e)
    	{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::saveDetailRow failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
   		}

        disableEntryFields(true);
	}

   	void GRID_rowSelected(ListSelectionEvent e)
	{
		if (GRID.getRowCount() > 0)
		{

//        	if (!IS_TRANS_COMMITTED)
            if (true)
            {
                int ret = choiceMessage.showConfirmDialog(this,"The current record has unsaved changes.  " +
                                                                "Please save or delete this record\n before continuing.",
                                                "Warning", JOptionPane.DEFAULT_OPTION,
                                                JOptionPane.WARNING_MESSAGE, null);
            } else  {
    			//Update the Panel with Grid data
                copyGridToEntryFields();
                disableEntryFields(true);
            }

		}

	}
}




