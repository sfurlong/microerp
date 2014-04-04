
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package daiBeans;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Vector;

import pv.jfcx.JPVNumeric;

public class daiNumField extends JPVNumeric
{
    private transient Vector daiDataModifiedListeners;
    static final long serialVersionUID = -1L; 

	public daiNumField()
	{
        super();
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
        this.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(KeyEvent e)
            {
                fireDaiDataModified(new daiDataModifiedEvent(this));
            }
        });
    }

    //Were overriding this so that we can capture programatic changes
    //to values in this EditField.
    public void setText(String text)
    {
        super.setText(text);
        if (this.getEditable()) {
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

	public void setDisabled(boolean disable)
	{
		if (disable)
		{
			this.setBackground(Color.lightGray);
			this.setEditable(false);
		} else
		{
			this.setBackground(Color.white);
			this.setEditable(true);
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

