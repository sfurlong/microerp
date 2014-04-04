
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.cmnSvcs.Logger;
import daiBeans.daiCheckBox;
import daiBeans.daiComboBox;
import daiBeans.daiCurrencyField;
import daiBeans.daiDataModifiedEvent;
import daiBeans.daiDataModifiedListener;
import daiBeans.daiDateField;
import daiBeans.daiMaskField;
import daiBeans.daiNumField;
import daiBeans.daiRadioButton;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;

public abstract class daiPanel extends JPanel
{

//********************************************************************//
//                  CONSTRUCTORS                                      //
//********************************************************************//
	public daiPanel(JFrame container, daiFrame containerFrame, BusinessObject obj)
	{
		CONTAINER = container;
                CONTAINER_FRAME = containerFrame;
		BUSINESS_OBJ    = obj;

		LOGGER = LOGGER.getInstance();

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

    //Used for panels that are not intended to work in conjunction with
    //daiFrame.
	public daiPanel(JFrame containerFrame, BusinessObject obj)
	{
		BUSINESS_OBJ    = obj;
                CONTAINER = containerFrame;
		LOGGER = LOGGER.getInstance();

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
        //Set the standard background color for DAI Entry Panels
        this.setBackground(daiColors.PanelColor);
	}

//********************************************************************//
//                  PUBLIC                                            //
//********************************************************************//
	public abstract int query(String id);
	public abstract int refresh();
	public abstract int delete();
    public abstract int persistPanelData();
    public abstract int prepareSaveAs(String id);

    public String getComponentId()
    {
        return COMPONENT_ID;
    }

	public BusinessObject getActiveBusinessObj()
	{
		return BUSINESS_OBJ;
	}

    //Called by frames to see if the panel has changes to commit.
    public boolean hasUncommittedChanges()
    {
        return _panelIsDirty;
    }

    public void setComponentId(String compId) {
        COMPONENT_ID = compId;
    }

    public void setComponentSecurity(String compId) {
        //Nothing to do in the base class.  Subclasses may
        //override, but probably not necessary.  This must
        //be implemented here because of the daiSecurity interface.
    }

    public void activateEntryChangeListeners()
    {
        Component[] comps = this.getComponents();
        setControlChangeListeners(comps);
    }

    public void disableDirtyFlagChecking()
    {
        _dirtyFlagCheckingIsEnabled = false;
        _panelIsDirty = false;
    }
    public void enableDirtyFlagChecking()
    {
        _panelIsDirty = false;
        _dirtyFlagCheckingIsEnabled = true;
    }
    public boolean panelIsDirty()
    {
        return _panelIsDirty;
    }

    public void resetTabEntrySeq() {
        //Intended for the decendants to override.
    }

//********************************************************************//
//                  PROTECTED                                         //
//********************************************************************//
    //Objects
	protected Logger LOGGER;
        protected JFrame CONTAINER;
	protected daiFrame CONTAINER_FRAME;
	protected BusinessObject BUSINESS_OBJ;
    protected String COMPONENT_ID = "";

    //Methods
    protected abstract BusinessObject getNewBusinessObjInstance();

//********************************************************************//
//                  PRIVATE                                          //
//********************************************************************//
    protected boolean _panelIsDirty = false;
    private boolean _dirtyFlagCheckingIsEnabled = false;

    private void setControlChangeListeners(Component[] comps)
    {
        for (int i=0; i<comps.length; i++)
        {
            if (comps[i] instanceof com.borland.jbcl.control.GroupBox)
            {
                com.borland.jbcl.control.GroupBox t_comps = (com.borland.jbcl.control.GroupBox)comps[i];
                setControlChangeListeners(t_comps.getComponents());
            } else if (comps[i] instanceof daiBeans.daiAddressPanel)
            {
                daiBeans.daiAddressPanel t_comps = (daiBeans.daiAddressPanel)comps[i];
                setControlChangeListeners(t_comps.getComponents());
            } else if (comps[i] instanceof daiBeans.daiContactsPanel)
            {
                daiBeans.daiContactsPanel t_comps = (daiBeans.daiContactsPanel)comps[i];
                setControlChangeListeners(t_comps.getComponents());
            } else if (comps[i] instanceof daiBeans.daiAcctIdNamePanel) {
                daiBeans.daiAcctIdNamePanel t_comps = (daiBeans.daiAcctIdNamePanel)comps[i];
                setControlChangeListeners(t_comps.getComponents());
            }

            if (comps[i] instanceof daiTextField)
            {
                daiTextField temp = (daiTextField)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
            if (comps[i] instanceof daiCurrencyField)
            {
                daiCurrencyField temp = (daiCurrencyField)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
            if (comps[i] instanceof daiNumField)
            {
                daiNumField temp = (daiNumField)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
            if (comps[i] instanceof daiMaskField)
            {
                daiMaskField temp = (daiMaskField)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
            if (comps[i] instanceof daiDateField)
            {
                daiDateField temp = (daiDateField)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
            if (comps[i] instanceof daiTextArea)
            {
                daiTextArea temp = (daiTextArea)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
            if (comps[i] instanceof daiComboBox)
            {
                daiComboBox temp = (daiComboBox)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
            if (comps[i] instanceof daiCheckBox)
            {
                daiCheckBox temp = (daiCheckBox)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
            if (comps[i] instanceof daiRadioButton)
            {
                daiRadioButton temp = (daiRadioButton)comps[i];
                temp.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            if (_dirtyFlagCheckingIsEnabled) {
                                _panelIsDirty = true;
                                CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
                            }
                        }
                    });
            }
        }
    }
}


