
//Title:        Business Artifacts
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:  

package daiBeans;

import java.awt.Color;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.io.Serializable;
import java.util.Vector;

import pv.jfcx.JPVCurrency;

public class daiCurrencyField extends JPVCurrency implements Serializable{
    public static final String US_DOLLAR = "$";
    private transient Vector daiDataModifiedListeners;

    public daiCurrencyField()
    {
        super();
        try
        {
            jbInit();
            this.setSymbol("");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public daiCurrencyField(String symbol)
    {
        super();
        try
        {
            jbInit();
            this.setSymbol(symbol);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        this.setThousandSeparators(false);
        this.setFormatNegative(1);  //sets format of neg values to use "-"

        this.addTextListener(new TextListener()
            {
            public void textValueChanged(TextEvent evt)
            {
                fireDaiDataModified(new daiDataModifiedEvent(this));
            }});
    }

    public void setDisabled(boolean disable)
    {
        if (disable)
        {
            this.setBackground(Color.lightGray);
            this.setEditable(false);
        } else {
            this.setBackground(Color.white);
            this.setEditable(true);
        }
    }

    //Were overriding this so that we can capture programatic changes
    //to values in this EditField.
    public void setText(String text)
    {
        //Check to see if incomming text is same as current text.  If so,
        //Don't fire data modified event.
        String oldText = this.getText();
        super.setText(text);
        if (this.getEditable() && !isSame(oldText, this.getText())) {
            fireDaiDataModified(new daiDataModifiedEvent(this));
        }
    }
    //We need this helper just incase the text is null.  We don't want to
    //throw an exception.
    private boolean isSame(String s1, String s2) {
        boolean ret = false;
        if (s1 == null) {
            if (s2 == null) ret = true;
        } else {
            ret = s1.equals(s2);
        }
        return ret;
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

