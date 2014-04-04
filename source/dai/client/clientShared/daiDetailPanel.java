
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
import dai.shared.businessObjs.DBAttributes;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiGrid;
import daiBeans.daiGridController;


public abstract class daiDetailPanel extends daiPanel
{
    private Vector m_subPanelVect = new Vector();
//********************************************************************//
//                  CONSTRUCTORS                                      //
//********************************************************************//
    public daiDetailPanel(JFrame container, daiFrame containerFrame, BusinessObject obj)
    {
        super(container, containerFrame, obj);

        try
        {
            jbInit();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            String msg = this.getClass().getName() + "::constructor failure." +
                         "\n"+ex.toString()+"\n"+ex.getLocalizedMessage();
            LOGGER.logError(container, msg);
        }
    }

    //Used for panels that are not intended to work in conjunction with
    //daiFrame.
    public daiDetailPanel(JFrame containerFrame, BusinessObject obj)
    {
        super(containerFrame, obj);

        try
        {
            jbInit();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            String msg = this.getClass().getName() + "::constructor failure." +
                         "\n"+ex.toString()+"\n"+ex.getLocalizedMessage();
            LOGGER.logError(containerFrame, msg);
        }
    }

    private void jbInit() throws Exception
    {
        GRID_CONTROLLER.addActionListener(new java.awt.event.ActionListener(){
                                              public void actionPerformed(ActionEvent e)
                                              {
                                                  gridController_actionPerformed(e);
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

        _dbAdapterFactory   = csDBAdapterFactory.getInstance();
        _dbAdapter          = _dbAdapterFactory.getDBAdapter();
        _sessionMeta        = SessionMetaData.getInstance();
    }

//***************************************************************//
//              PUBLIC METHODS
//***************************************************************//
    public int refresh()
    {
        //Clear out all the rows and entry fields.
        GRID.removeAllRows();
        clearEntryFields();
        refresh_subPanel();
        //Cleare out the numItems indicator from the status bar.
        CONTAINER_FRAME.setStatusRight("");
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

        //Add order by clause for sorting.
        exp = exp + " order by detail_id ";

        try
        {
            objVect = _dbAdapter.queryByExpression( _sessionMeta.getClientServerSecurity(),
                                                    BUSINESS_OBJ,
                                                    exp);

            //Loop through all the retreived business objects and indicate
            //that they already exist in the database.  This is how we know
            //to update instead of insert.
            for (int i=0; i<objVect.size(); i++)
            {
                BusinessObject obj = (BusinessObject)objVect.elementAt(i);
                obj.EXISTS_IN_DB = true;
                objVect.setElementAt(obj, i);
            }

            //Update the UI even if objVect size is zero.  We don't want the
            //previous id's rows to be hanging around.
            updateGridFromBusinessObj(objVect);
            //Populate subPanel controls.
            update_subPanel_updateGridFromBusinessObj(objVect);

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

    public int delete()
    {
        //No need to do anything here.  This is called when the header does
        //a delete.  DB should do a cascade delete.
        return 0;
    }

    public int prepareSaveAs(String newId) {
        for (int i=0; i < GRID.getRowCount(); i++)
        {
            BusinessObject tempObj = (BusinessObject)GRID.get(i, OBJ_COL_POS);

            tempObj.EXISTS_IN_DB = false;
            tempObj.set_id(newId);
            BUSINESS_OBJ.set_id(newId);

            GRID.set(i, OBJ_COL_POS, tempObj);
        }

        return 0;
    }

    public int persistPanelData()
    {
        //In case a detail row is currently being edited,
        //do an enter on the detail row.  No effect, if nothing
        //is being edited.
        GRID_CONTROLLER.daiButton_enter_actionPerformed();

        String exp = "";
        String fullExp = "";

        //If there's nothing in the grid, let's get out of here.
        if (GRID.getRowCount() <= 0) return 0;

        updateBusinessObjFromGrid();

        for (int i=0; i<m_subPanelVect.size(); i++)
        {
            daiDetailSubPanel subPanel = (daiDetailSubPanel)m_subPanelVect.elementAt(i);
            //Have the subpanel fill in the business obj fields from it's
            //user interface.
            subPanel.updateBusinessObjFromGrid();
        }

        //Select all the IDs from the
        DBAttributes attribs[] = BUSINESS_OBJ.getImmutableAttribs();

        for (int i=0; i < attribs.length; i++)
        {
            if (i > 0)
            {
                exp =  exp + " and ";
            }
            exp = exp + attribs[i].getName() +
                  " = " +
                  "'" + attribs[i].getValue().trim() + "'";
        }

        for (int i=0; i < GRID.getRowCount(); i++)
        {
            try
            {
                //The Business Object for each row in the grid is
                //always stored in column 0.
                BusinessObject tempObj = (BusinessObject)GRID.get(i, OBJ_COL_POS);

                //Get the detail item Id.
                fullExp = exp + " and detail_id = " + tempObj.get_detail_id();

                if (tempObj.EXISTS_IN_DB)
                {
                    _dbAdapter.update(  _sessionMeta.getClientServerSecurity(),
                                        tempObj,
                                        fullExp);
                } else
                {
                    _dbAdapter.insert(_sessionMeta.getClientServerSecurity(),
                                      tempObj);
                }
                //This will always be true after we insert/update.
                tempObj.EXISTS_IN_DB = true;

            } catch (Exception e)
            {
                e.printStackTrace();
                String msg = this.getClass().getName() + "::update failure." +
                             "\n"+e.toString()+"\n"+e.getLocalizedMessage();
                LOGGER.logError(CONTAINER, msg);
            }
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

//***************************************************************//
//              PROTECTED METHODS
//***************************************************************//
    protected csDBAdapterFactory  _dbAdapterFactory;
    protected csDBAdapter         _dbAdapter;
    //All Detail Panels will have GridControls.
    protected daiGridController GRID_CONTROLLER = new daiGridController();
    protected daiGrid GRID;

    protected int OBJ_COL_POS = 1;
    protected SessionMetaData _sessionMeta;

    abstract protected void copyEntryFieldsToGrid();
    abstract protected void copyGridToEntryFields();
    abstract protected void updateBusinessObjFromGrid();
    abstract protected void updateGridFromBusinessObj(Vector objVect);
    abstract protected void clearEntryFields();
    abstract protected void disableEntryFields(boolean disable);

//***************************************************************//
//              PRIVATE METHODS
//***************************************************************//

    //Navigator Event Handler.
    private void gridController_actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand() == "NEW")
        {
            newDetailRow();
            //Update the status bar with the number of detail items
            CONTAINER_FRAME.setStatusRight("Editing Row: " + (GRID.getActiveRow()+1));
        }
        if (e.getActionCommand() == "ENTER")
        {
            saveDetailRow();
            GRID.clearAllRowSelections();

        }
        if (e.getActionCommand() == "EDIT")
        {
            disableEntryFields(false);
            //Update the status bar with the number of detail items
            CONTAINER_FRAME.setStatusRight("Editing Row: " + GRID.getActiveRow()+1);
        }
        if (e.getActionCommand() == "CANCEL")
        {
            disableEntryFields(true);
            clearEntryFields();
            GRID.clearAllRowSelections();
        }
        if (e.getActionCommand() == "DELETE")
        {
            this._panelIsDirty = true;
            CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
            deleteDetailRow();
            GRID.clearAllRowSelections();
            //Update the status bar with the number of detail items
            CONTAINER_FRAME.setStatusRight("Deleted Row: " + GRID.getActiveRow()+1);
        }
    }

    private void newDetailRow()
    {
        int newRowLoc = GRID.getRowCount() -1;
        BusinessObject itemObj = getNewBusinessObjInstance();

        //Clear out the data entry fields.
        clearEntryFields();

        try
        {
            //Find out what the Highest Detail ID is and add one for the new ID
            DBAttributes attribs[] = itemObj.getImmutableAttribs();
            String exp = "";

            for (int i=0; i < attribs.length; i++)
            {
                if (i > 0)
                {
                    exp = exp + " and ";
                }
                exp = exp + attribs[i].getName() +
                      " = " +
                      "'" + attribs[i].getValue().trim() + "'";
            }
            int newRowId = _dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
                                                        csDBAdapter.SEQUENCE_GENERIC_DETAIL_ID);

            //Set the Detail Primary Key for the new Item Object.
            itemObj.set_detail_id(Integer.toString(newRowId));
            itemObj.EXISTS_IN_DB = false;

            //Add the new Item Object to the grid.
            GRID.set(newRowLoc, OBJ_COL_POS, itemObj);

        } catch (Exception e)
        {
            e.printStackTrace();
            String msg = this.getClass().getName() + "::newDetailRow failure." +
                         "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
        }

        //Update the subpanel's grids
        update_subPanel_updateGridFromBusinessObj(getActiveBusinessObjVector());
        disableEntryFields(false);
    }

    private void deleteDetailRow()
    {
        int row = GRID.getSelectedRow();
        String exp = "";

        BusinessObject tempObj = (BusinessObject)GRID.get(row, OBJ_COL_POS);

        //Select all the IDs from the
        DBAttributes attribs[] = tempObj.getImmutableAttribs();

        for (int i=0; i < attribs.length; i++)
        {
            exp = exp + " and " +
                  attribs[i].getName() +
                  " = " +
                  "'" + attribs[i].getValue().trim() + "'";
        }

        exp = " detail_id = " +
              "'" + tempObj.get_detail_id() + "'" +
              exp;

        try
        {
            //Delete from the DB.
            _dbAdapter.delete(_sessionMeta.getClientServerSecurity(),
                              tempObj,
                              exp);

            //Deletes the current row from the UI
            GRID.deleteRow(row);

            //Update the sub Panel's grids
            update_subPanel_updateGridFromBusinessObj(getActiveBusinessObjVector());

        } catch (Exception e)
        {
            e.printStackTrace();
            String msg = this.getClass().getName() + "::deleteDetailRow failure." +
                         "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
        }
    }

    private void saveDetailRow()
    {
        copyEntryFieldsToGrid();
        updateBusinessObjFromGrid();

        //update sub panels' grids
        update_subPanel_updateGridFromBusinessObj(getActiveBusinessObjVector());
        disableEntryFields(true);
    }

    private void GRID_rowSelected(ListSelectionEvent e)
    {
        if (GRID.getRowCount() > 0)
        {
            if (GRID_CONTROLLER.isDetailCommitted())
            {
                //Update the Panel with Grid data
                copyGridToEntryFields();
                disableEntryFields(true);

                //Update the status bar with the number of detail items
                CONTAINER_FRAME.setStatusRight("");
            }

        }
    }
    public void addSubPanel(daiDetailSubPanel subPanel)
    {
        m_subPanelVect.addElement(subPanel);
    }



    private void update_subPanel_updateGridFromBusinessObj(Vector objVect)
    {
        //Loop throught the list of subpanels.
        for (int i=0; i<m_subPanelVect.size(); i++)
        {
            daiDetailSubPanel subPanel = (daiDetailSubPanel)m_subPanelVect.elementAt(i);
            //Have the subpanel fill in the entry fields using the business obj
            //from the master panel.
            subPanel.updateGridFromBusinessObj(objVect);
        }
    }
    private void refresh_subPanel()
    {
        //Loop throught the list of subpanels.
        for (int i=0; i<m_subPanelVect.size(); i++)
        {
            daiDetailSubPanel subPanel = (daiDetailSubPanel)m_subPanelVect.elementAt(i);
            //Have the subpanel fill in the entry fields using the business obj
            //from the master panel.
            subPanel.GRID_refresh();
        }
    }
}
