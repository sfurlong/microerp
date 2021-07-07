
//Title:        eCorp
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Your description

package dai.client.clientShared;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class daiBannerPanel extends JPanel {
    JLabel jLabel_leftLabel     = new JLabel();
    JLabel jLabel_rightLabel    = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();

    public daiBannerPanel() {
        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public daiBannerPanel(String leftText) {
        try  {
            jbInit();
            jLabel_leftLabel.setText(leftText);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setBannerLeftText(String t)
    {
        jLabel_leftLabel.setText("  "+t);
    }

    public String getBannerLeftText()
    {
        return jLabel_leftLabel.getText();
    }

    public void setBannerRightText(String t)
    {
        jLabel_rightLabel.setText(t);
    }

    public String getBannerRightText()
    {
        return jLabel_rightLabel.getText();
    }

    private void jbInit() throws Exception {
        this.setBackground(Color.darkGray);
        this.setMaximumSize(new Dimension(2147483647, 38));
        this.setMinimumSize(new Dimension(0, 23));
        this.setPreferredSize(new Dimension(0, 23));
        this.setLayout(borderLayout1);

//        jLabel_leftLabel.setText("left");
//        jLabel_rightLabel.setText("right");
        jLabel_leftLabel.setFont(new java.awt.Font("Dialog", 0, 16));
        jLabel_leftLabel.setForeground(Color.white);
    jLabel_leftLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        jLabel_rightLabel.setFont(new java.awt.Font("Dialog", 0, 16));
        jLabel_rightLabel.setForeground(Color.white);
    jLabel_rightLabel.setHorizontalAlignment(SwingConstants.LEFT);


    this.add(jLabel_leftLabel, BorderLayout.WEST);
    this.add(jLabel_rightLabel, BorderLayout.CENTER);
    }
}

