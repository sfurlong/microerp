
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      DAI
//Description:  Beans

package daiBeans;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import dai.client.clientShared.daiFrameActions;

public class daiButtonBar extends JToolBar implements Serializable {

    private Vector  buttonVect = new Vector();
    private Vector actionListeners = new Vector();
    private String imgBase = "";

    public static int NEW = 0;
    public static int RESET = 1;
    public static int SAVE = 2;
    public static int PRINT = 3;
    public static int PREVIEW = 4;
    public static int BACKWARD = 5;
    public static int FORWARD = 6;
    public static int HELP = 7;

    public daiButtonBar() {
        try  {
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public daiButtonBar(String imgBase) {
        try  {
            this.imgBase = imgBase;
            jbInit();

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        this.insertButton(daiFrameActions.NEW, imgBase+"Newid16.gif", "New Id");
        this.insertButton(daiFrameActions.RESET, imgBase+"New16.gif", "Reset");
        this.insertButton(daiFrameActions.SAVE, imgBase+"Save16.gif", "Save");
        this.insertButton(daiFrameActions.PRINT, imgBase+"print16.gif", "Print");
        this.insertButton(daiFrameActions.PREVIEW, imgBase+"printpreview16.gif", "Preview");
        this.insertButton(daiFrameActions.BACKWARD, imgBase+"Back16.gif", "Backward");
        this.insertButton(daiFrameActions.FORWARD, imgBase+"Forward16.gif", "Forward");
        this.insertSpacer();
        this.insertButton(daiFrameActions.HELP_ABOUT, imgBase+"Help16.gif", "Help");
    }

    public void insertButton(String label, String icon, String toolTip)
    {
        ToolButton butt = new ToolButton(label, new ImageIcon(icon));
        butt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ToolButton toolButt = (ToolButton)e.getSource();
                ActionEvent ae = new ActionEvent(e.getSource(), 0, toolButt.getActionId());
                fireActionPerformed(ae);
            }
        });
        buttonVect.addElement(butt);
        this.add(butt);
    }

    public void insertSpacer()
    {
        addSeparator();
    }

    public void clearAllButtons()
    {
    }

    public void buttonIsHidden(int pos, boolean isHidden)
    {
        ToolButton butt = (ToolButton)buttonVect.elementAt(pos);
        if (isHidden) {
            butt.setEnabled(false);
        } else {
            butt.setEnabled(true);
        }
    }

    public void removeButton(int pos)
    {
        this.remove(pos);
    }

    public void setButtonDisabled(int pos, boolean disable)
    {
        ToolButton butt = (ToolButton)buttonVect.elementAt(pos);
        if (disable) {
            butt.setEnabled(false);
        } else {
            butt.setEnabled(true);
        }
    }

    public synchronized void removeActionListener(ActionListener l) {
        if(actionListeners != null && actionListeners.contains(l)) {
            Vector v = (Vector) actionListeners.clone();
            v.removeElement(l);
            actionListeners = v;
        }
    }
    public synchronized void removeAllActionListeners() {
        if(actionListeners != null) {
            Vector v = (Vector) actionListeners.clone();
            v.removeAllElements();
            actionListeners = v;
        }
    }
    public synchronized void addActionListener(ActionListener l) {
        if(!actionListeners.contains(l)) {
            actionListeners.addElement(l);
        }
    }

    protected void fireActionPerformed(ActionEvent e) {
        if(actionListeners != null) {
            Vector listeners = actionListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((ActionListener) listeners.elementAt(i)).actionPerformed(e);
            }
        }
    }

    class ToolButton extends JButton {
        String _actionId;

        public ToolButton(String actionId, Icon icon) {
            super(icon);
            _actionId = actionId;
            setToolTipText(actionId);
        }

        public String getActionId() {
            return _actionId;
        }
    }
}

