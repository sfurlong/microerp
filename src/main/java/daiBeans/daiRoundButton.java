
//Title:        Your Product Name
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      DAI
//Description:  Beans

package daiBeans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;

import pv.jfcx.JPVRoundButton;

public class daiRoundButton extends JPVRoundButton implements MouseListener{
    BorderLayout borderLayout1 = new BorderLayout();


    public daiRoundButton() {
        try  {
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setShadowWidth(1);
        this.setColorsDepth(1);
        this.setCenterArea(5);
        this.setShape(1);
        this.addMouseListener(this);
        this.setFont(new Font("SansSerif", 0, 12));
        this.setLayout(borderLayout1);
        this.setSize(55, 22);
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.setMaximumSize(new Dimension(50, 23));
        this.setMinimumSize(new Dimension(50, 23));
        this.setPreferredSize(new Dimension(50, 23));
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
    }

    public void mouseEntered(MouseEvent e) {
        if (isEnabled()) {
            this.setForeground(Color.white);
        }
    }

    public void mouseExited(MouseEvent e) {
        if (isEnabled()) {
            this.setForeground(Color.black);
        }
    }
}

