
//Title:        Business Artifacts
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package daiBeans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.Serializable;
import java.util.Vector;

import pv.jfcx.JPVDatePlus;

public class daiDateField extends JPVDatePlus implements Serializable{
    BorderLayout borderLayout1 = new BorderLayout();
    private String setMask;
    private transient Vector daiDataModifiedListeners;


    public daiDateField()
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

    private void jbInit() throws Exception
    {
        //This sets month and day format to use leading zeros.
        this.setLeadingZero(true);
        this.setLayout(borderLayout1);
        this.addTextListener(new TextListener()
            {
            public void textValueChanged(TextEvent evt)
            {
                fireDaiDataModified(new daiDataModifiedEvent(this));
            }});
    }

    //Were overriding this so that we can capture programatic changes
    //to values in this EditField.
    public void setText(String text)
    {
        super.setText(text);
        if (this.isEnabled()) {
            fireDaiDataModified(new daiDataModifiedEvent(this));
        }
    }

    //Were overriding this so that we can capture programatic changes
    //to values in this EditField.
    public String getText()
    {
        String ret = super.getText();
        if (ret != null && ret.trim().length() == 0) {
            ret = null;
        }
        return ret;
    }

    public void setSetMask(String newSetMask) {
        setMask = newSetMask;
    }

    public String getSetMask() {
        return setMask;
    }

    public void setDisabled(boolean disable)
    {
        if (disable)
        {
            this.setBackground(Color.lightGray);
            this.setEnabled(false);
        } else {
            this.setBackground(Color.white);
            this.setEnabled(true);
        }
    }

    public synchronized void removedaiDataModifiedListener(daiDataModifiedListener l)
    {
        if(daiDataModifiedListeners != null && daiDataModifiedListeners.contains(l))
        {
            Vector v = (Vector) daiDataModifiedListeners.clone();
            v.removeElement(l);
            daiDataModifiedListeners = v;
        }
    }

    public synchronized void adddaiDataModifiedListener(daiDataModifiedListener l)
    {
        Vector v = daiDataModifiedListeners == null ? new Vector(2) : (Vector) daiDataModifiedListeners.clone();
        if(!v.contains(l))
        {
            v.addElement(l);
            daiDataModifiedListeners = v;
        }
    }

    protected void fireDaiDataModified(daiDataModifiedEvent e)
    {
        if(daiDataModifiedListeners != null)
        {
            Vector listeners = daiDataModifiedListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++)
            {
                ((daiDataModifiedListener) listeners.elementAt(i)).daiDataModified(e);
            }
        }
    }
}

