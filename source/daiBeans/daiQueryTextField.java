//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package daiBeans;

import java.awt.event.FocusEvent;
import java.util.Vector;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class daiQueryTextField extends daiTextField {

    private BusinessObject _queriedBusinessObj = null;
    private BusinessObject _businessObjToQuery = null;
    private transient Vector daiActionListeners;

    public daiQueryTextField()
    {
        try
        {
            jbInit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public daiQueryTextField(BusinessObject obj)
    {
        try
        {
            jbInit();
            _businessObjToQuery = obj;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        this.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                if (!e.isTemporary()) {
                    this_focusLost(e);
                }
            }
        });
    }

    public void setbusinessObjToQuery(BusinessObject obj)
    {
        _businessObjToQuery = obj;
    }

    public BusinessObject getQueriedObj() {
        return _queriedBusinessObj;
    }

    private void this_focusLost(FocusEvent e)
    {
        if (!e.isTemporary() && !this.isDisabled() && _isModified) {
            queryBusinessObject();
            fireDaiActionEvent(new daiActionEvent(_queriedBusinessObj));
            _isModified = false;
        }
    }

    public synchronized void removedaiActionListener(daiActionListener l)
    {
        if(daiActionListeners != null && daiActionListeners.contains(l))
        {
            Vector v = (Vector) daiActionListeners.clone();
            v.removeElement(l);
            daiActionListeners = v;
        }
    }

    public synchronized void adddaiActionListener(daiActionListener l)
    {
        Vector v = daiActionListeners == null ? new Vector(2) : (Vector) daiActionListeners.clone();
        if(!v.contains(l))
        {
            v.addElement(l);
            daiActionListeners = v;
        }
    }

    protected void fireDaiActionEvent(daiActionEvent e)
    {
        if(daiActionListeners != null)
        {
            Vector listeners = daiActionListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++)
            {
                ((daiActionListener) listeners.elementAt(i)).daiActionEvent(e);
            }
        }
    }

    private void queryBusinessObject() {
        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
        SessionMetaData sessionMeta = SessionMetaData.getInstance();

        String id = this.getText();
        String exp = "";

        if (id != null && id.trim().length() > 0) {
            exp = exp + " id = '" + id + "' " +
                " and locality = " + "'" + _businessObjToQuery.getObjLocality() + "'";
        } else {
            _queriedBusinessObj = null;
            this.setText(null);
            return;
        }

        try {
            Vector vect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                    _businessObjToQuery, exp);

            if (vect.size() > 0) {
                _queriedBusinessObj = (BusinessObject)vect.firstElement();
            } else {
                _queriedBusinessObj = null;
            }
        } catch (Exception e) {
            System.out.println(exp);
            e.printStackTrace();;
        }
    }
}


