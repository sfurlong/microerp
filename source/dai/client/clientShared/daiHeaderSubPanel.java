//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import javax.swing.JFrame;

import dai.shared.businessObjs.BusinessObject;

public abstract class daiHeaderSubPanel extends daiPanel
{
    //Called by the master header panel.
	abstract public void update_UI(BusinessObject obj);
    //Called by the master header panel.
    abstract public void update_BusinessObj(BusinessObject obj);

	public daiHeaderSubPanel(JFrame container, daiFrame containerFrame, BusinessObject obj)
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
	}

	public int query(String id)
	{
        //Don't need to do anything here.  The
        //main header panel will take care of all the
        //query operations.
		return 0;
	}

	public int refresh()
	{
		update_UI(BUSINESS_OBJ);
		return 0;
	}


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
		update_UI(BUSINESS_OBJ);

		return 0;
	}

	protected int insert(String id)
	{
        //Just update the entry fields on the panel.  the
        //main header panel will take care of the rest.
        update_UI(BUSINESS_OBJ);

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

    //Override of abstract method.  Typically won't be needed by decendants
    //of this class.
    public int persistPanelData() {
        return 0;
    }
}

