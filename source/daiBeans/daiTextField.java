
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.TextListener;
import java.io.Serializable;
import java.util.Vector;

import pv.jfcx.JPVEdit;

public class daiTextField extends JPVEdit implements Serializable{
    BorderLayout borderLayout1 = new BorderLayout();
    private transient Vector daiDataModifiedListeners;
    private boolean _isDisabled = false;
    protected boolean _isModified = false;

    public daiTextField()
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
        this.setLayout(borderLayout1);
        //If alt key is pressed (i.e. a mnemonic is presson on a button while
        //focus is on this field), consume the key char.
        this.addKeyListener(new KeyAdapter() {
        	public void keyTyped(KeyEvent keyevent) {
        		if (keyevent.isAltDown()) {
        			keyevent.consume();
        		}
        	}
        });
        this.addTextListener(new TextListener()
            {
            public void textValueChanged(TextEvent evt)
            {
                _isModified = true;
                fireDaiDataModified(new daiDataModifiedEvent(this));
            }});
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

    public boolean isDisabled() {
        return _isDisabled;
    }

    public void setDisabled(boolean disable)
    {
        if (disable)
        {
            this.setBackground(Color.lightGray);
            this.setEditable(false);
            _isDisabled = true;
        } else {
            this.setBackground(Color.white);
            this.setEditable(true);
            _isDisabled = false;
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

