
//Title:        clientShared
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Shared Code for Client Software

package dai.client.clientShared;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import daiBeans.daiLabel;

public class daiStatusBar extends JPanel {
    JPanel jPanel_leftStatus = new JPanel();
    JPanel jPanel_centerStatus = new JPanel();
    GridLayout gridLayout1 = new GridLayout();
    JPanel jPanel_rightStatus = new JPanel();
    XYLayout xYLayout1 = new XYLayout();
    daiLabel daiLabel_leftStatus = new daiLabel();
    daiLabel daiLabel_centerStatus = new daiLabel();
    XYLayout xYLayout2 = new XYLayout();
    XYLayout xYLayout3 = new XYLayout();
    daiLabel daiLabel_rightStatus = new daiLabel();

    public daiStatusBar() {
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
		setMaximumSize(new Dimension(32767, 25));
		setMinimumSize(new Dimension(10, 25));
		setPreferredSize(new Dimension(10, 25));
        this.setLayout(gridLayout1);
        jPanel_leftStatus.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel_leftStatus.setMaximumSize(new Dimension(50, 25));
        jPanel_leftStatus.setMinimumSize(new Dimension(50, 25));
        jPanel_leftStatus.setPreferredSize(new Dimension(50, 25));
        jPanel_leftStatus.setLayout(xYLayout1);
        jPanel_centerStatus.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel_centerStatus.setMaximumSize(new Dimension(32767, 25));
        jPanel_centerStatus.setMinimumSize(new Dimension(10, 25));
        jPanel_centerStatus.setPreferredSize(new Dimension(10, 25));
        jPanel_centerStatus.setLayout(xYLayout2);
        jPanel_rightStatus.setBorder(BorderFactory.createLoweredBevelBorder());
        jPanel_rightStatus.setMaximumSize(new Dimension(50, 25));
        jPanel_rightStatus.setMinimumSize(new Dimension(50, 25));
        jPanel_rightStatus.setPreferredSize(new Dimension(50, 25));
        jPanel_rightStatus.setLayout(xYLayout3);
        daiLabel_leftStatus.setHorizontalAlignment(SwingConstants.LEFT);
        daiLabel_leftStatus.setFont(new java.awt.Font("Dialog", 1, 11));
        daiLabel_rightStatus.setHorizontalAlignment(SwingConstants.LEFT);
        daiLabel_rightStatus.setFont(new java.awt.Font("Dialog", 1, 11));
        daiLabel_centerStatus.setHorizontalAlignment(SwingConstants.LEFT);
        daiLabel_centerStatus.setFont(new java.awt.Font("Dialog", 1, 11));
        this.add(jPanel_leftStatus, null);
        jPanel_leftStatus.add(daiLabel_leftStatus, new XYConstraints(1, 2, 139, 22));
        this.add(jPanel_centerStatus, null);
        jPanel_centerStatus.add(daiLabel_centerStatus, new XYConstraints(1, 2, 139, 22));
        this.add(jPanel_rightStatus, null);
        jPanel_rightStatus.add(daiLabel_rightStatus, new XYConstraints(1, 2, 139, 22));
    }

    public void setLeftStatus(String s)
    {
        daiLabel_leftStatus.setText(s);
    }

    public void setCenterStatus(String s)
    {
        daiLabel_centerStatus.setText(s);
    }

    public void setRightStatus(String s)
    {
        daiLabel_rightStatus.setText(s);
    }
}