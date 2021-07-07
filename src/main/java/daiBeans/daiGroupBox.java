
//Title:        Your Product Name
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      DAI
//Description:  Beans

package daiBeans;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import dai.client.clientShared.daiColors;

public class daiGroupBox extends JPanel
{
    BorderLayout borderLayout1 = new BorderLayout();
    String _title = null;
	EmptyBorder border5 = new EmptyBorder(5,5,5,5);

    public daiGroupBox(String title)
    {
        _title = title;

        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        this.setBackground(daiColors.PanelColor);
        this.setLayout(borderLayout1);
        this.setBorder(new CompoundBorder(new TitledBorder(null, _title,
                       TitledBorder.LEFT, TitledBorder.TOP), border5));

    }
}