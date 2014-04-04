
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiTextField;

public abstract class daiHeaderPanel extends daiPanel
{
	BorderLayout borderLayout1 = new BorderLayout();
    protected csDBAdapterFactory    _dbAdapterFactory;
	protected csDBAdapter           _dbAdapter;
    private Vector m_subPanelVect = new Vector();

    protected SessionMetaData _sessionMeta;
    protected daiTextField ID_TEXT_FIELD = new daiTextField();

	abstract protected void update_BusinessObj();
	abstract protected void update_UI(BusinessObject obj);

	public daiHeaderPanel(JFrame container, daiFrame containerFrame, BusinessObject obj)
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
	public daiHeaderPanel(JFrame containerFrame, BusinessObject obj)
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
		this.setLayout(borderLayout1);
        _dbAdapterFactory = _dbAdapterFactory.getInstance();
        _dbAdapter = _dbAdapterFactory.getDBAdapter();
        _sessionMeta = _sessionMeta.getInstance();
	}

	public int query(String id)
	{
		Vector objVect = new Vector();
		String exp = "";
		DBAttributes attribs[] = BUSINESS_OBJ.getImmutableAttribs();

		for (int i=0; i < attribs.length; i++)
		{
			exp = exp + " and " + attribs[i].getName() +
				  " = " +
				  "'" + attribs[i].getValue().trim() + "'";
		}

		//change the value in the parameter row and refresh the display
		String queryExpression = "id = " + "'" + id + "'" + exp;

		try
		{
			objVect = _dbAdapter.queryByExpression( _sessionMeta.getClientServerSecurity(),
                                                    BUSINESS_OBJ,
                                                    queryExpression);
			if (objVect.size() != 0)
			{
				//for Header type DBObjs, there should only be one element in the vector.
                BusinessObject obj = (BusinessObject)objVect.firstElement();

                //Populate the Panel controls
				update_UI(obj);

                //Populate subPanel controls.
                update_subPanel_UI(obj);

                //Indicate that the retrieved business object has been
                //persisted before so that we update instead of insert.
                obj.EXISTS_IN_DB = true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::query failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
		}

		return 0;
	}

	public int refresh()
	{
		BUSINESS_OBJ.clear(false);
        BUSINESS_OBJ.EXISTS_IN_DB = false;
		update_UI(BUSINESS_OBJ);
        //Update subPanel UI.
        update_subPanel_UI(BUSINESS_OBJ);
		return 0;
	}

    public int prepareSaveAs(String newId) {
        ID_TEXT_FIELD.setDisabled(false);
        ID_TEXT_FIELD.setText(newId);
        BUSINESS_OBJ.EXISTS_IN_DB = false;
        BUSINESS_OBJ.set_id(newId);
        BUSINESS_OBJ.set_created_by(_sessionMeta.getUserId());
        BUSINESS_OBJ.set_date_created(daiFormatUtil.getCurrentDate());

        this.update_UI(BUSINESS_OBJ);

        return 0;
    }

	public int persistPanelData()
	{
		String exp = "";

		update_BusinessObj();

        //Get any business obj updates from subpanels.
        //Loop throught the list of subpanels.
        for (int i=0; i<m_subPanelVect.size(); i++)
        {
            daiHeaderSubPanel subPanel = (daiHeaderSubPanel)m_subPanelVect.elementAt(i);
            //Have the subpanel fill in the business obj fields from it's
            //user interface.
            subPanel.update_BusinessObj(BUSINESS_OBJ);
        }

        //Get the list of imutables so that they can be used
        //as the where clause in the SQL update.
		DBAttributes attribs[] = BUSINESS_OBJ.getImmutableAttribs();

		for (int i=0; i < attribs.length; i++)
		{
			exp = exp + " and " +
				  attribs[i].getName() +
				  " = " +
				  "'" + attribs[i].getValue().trim() + "'";
		}

		exp = " id = " +
			  "'" + BUSINESS_OBJ.get_id() + "'" +
			  exp;

		try
		{
            if (BUSINESS_OBJ.EXISTS_IN_DB) {
    			_dbAdapter.update(  _sessionMeta.getClientServerSecurity(),
                                    BUSINESS_OBJ,
                                    exp);
            } else {
			    _dbAdapter.insert(  _sessionMeta.getClientServerSecurity(),
                                    BUSINESS_OBJ);
            }

            //This will always be true after we persist to the DB.
            BUSINESS_OBJ.EXISTS_IN_DB = true;
		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::update failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
		}

		return 0;
	}

	public int delete()
	{
		String exp = "";

		//Select all the IDs from the
		DBAttributes attribs[] = BUSINESS_OBJ.getImmutableAttribs();

		for (int i=0; i < attribs.length; i++)
		{
			exp = exp + " and " +
				  attribs[i].getName() +
				  " = " +
				  "'" + attribs[i].getValue().trim() + "'";
		}

		exp = " id = " +
			  "'" + BUSINESS_OBJ.get_id() + "'" +
			  exp;

		try
		{
			_dbAdapter.delete(  _sessionMeta.getClientServerSecurity(),
                                BUSINESS_OBJ,
                                exp);
		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::delete failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
		}

		BUSINESS_OBJ.clear(false); //Don't clear the immutables.
		update_UI(BUSINESS_OBJ);
        //Update subPanel UI.
        update_subPanel_UI(BUSINESS_OBJ);

		return 0;
	}

    public void addSubPanel(daiHeaderSubPanel subPanel)
    {
        m_subPanelVect.addElement(subPanel);
    }

	public BusinessObject getActiveBusinessObj()
	{
		return BUSINESS_OBJ;
	}

    private void update_subPanel_UI(BusinessObject obj)
    {
        //Loop throught the list of subpanels.
        for (int i=0; i<m_subPanelVect.size(); i++)
        {
            daiHeaderSubPanel subPanel = (daiHeaderSubPanel)m_subPanelVect.elementAt(i);
            //Have the subpanel fill in the entry fields using the business obj
            //from the master panel.
            subPanel.update_UI(obj);
        }
    }
}

