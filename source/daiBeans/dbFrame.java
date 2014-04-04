
//Title:      Your Product Name
//Version:
//Copyright:  Copyright (c) 1998
//Author:     Stephen P. Furlong
//Company:    DAI
//Description:Beans
package daiBeans;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import pv.jfcx.JPVButton;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.shared.businessObjs.carrierObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.prospect_company_name_idsObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class dbFrame extends JFrame
{
	XYLayout xYLayout1 = new XYLayout();
    JButton jButton1 = new JButton();
    daiListBox  listBox = new daiListBox();

    TitledBorder titledBorder1;
    ButtonGroup buttonGroup = new ButtonGroup();

    JPVButton jPVButton2 = new JPVButton();

    JToolBar javaToolBar = new JToolBar();
    JPopupMenu jPopupMenu1 = new JPopupMenu();
    JMenuItem jMenuItem3 = new JMenuItem();
    JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem();
    JCheckBoxMenuItem jCheckBoxMenuItem1 = new JCheckBoxMenuItem();

    daiDBComboBox dbComboBox = new daiDBComboBox(new carrierObj(), " locality = 'SUPER'");
    daiDBIdPopupField idPopupField = new daiDBIdPopupField(this,
                                                        new customerObj(),
                                                        customerObj.NAME,
                                                        "Customer Id:");

	//Construct the frame
	public dbFrame()
	{
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	//Component initialization
	private void jbInit() throws Exception
	{
        jPVButton2.setOpaque(false);
        jPVButton2.setFocusDotRect(false);
        jPVButton2.setFocusRect(false);
        jPVButton2.setBorderStyle(jPVButton2.NONE);
        jMenuItem3.setText("next");
        jRadioButtonMenuItem1.setText("Serial Nums");
        jCheckBoxMenuItem1.setText("Receipt");
        jPopupMenu1.setInvoker(jButton1);
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                jButton1_actionPerformed(e);
            }
        });
        jButton1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                jButton1_mouseClicked(e);
            }
        });

		titledBorder1 = new TitledBorder("");
        this.getContentPane().setLayout(xYLayout1);
		xYLayout1.setHeight(609);
		xYLayout1.setWidth(728);
		jButton1.setText("jButton1");

        this.getContentPane().add(jButton1, new XYConstraints(22, 509, -1, -1));
        this.getContentPane().add(listBox, new XYConstraints(577, 39, -1, 124));
		this.setTitle("Frame Title");

        javaToolBar.add(new JButton(new ImageIcon("f:/release/images/toolbarButtonGraphics/general/Copy16.gif")));
        this.getContentPane().add(javaToolBar);
        this.getContentPane().add(dbComboBox, new XYConstraints(142, 155, 170, 24));
        this.getContentPane().add(idPopupField, new XYConstraints(34, 189, 414, 37));

        jPopupMenu1.add(jRadioButtonMenuItem1);
        jPopupMenu1.add(jCheckBoxMenuItem1);
        jPopupMenu1.add(jMenuItem3);

		pack();
	}

	//Overridden so we can exit on System Close
	protected void processWindowEvent(WindowEvent e)
	{
		super.processWindowEvent(e);
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			System.exit(0);
		}
	}


    void jButton1_actionPerformed(ActionEvent e)
    {
        csDBAdapter dbAdapter = csDBAdapterFactory.getInstance().getDBAdapter();
        Vector vect = null;
        try {
            vect = dbAdapter.getAllIds(SessionMetaData.getInstance().getClientServerSecurity(),
                            prospect_company_name_idsObj.TABLE_NAME, "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(vect.size());
        System.out.println(dbComboBox.getItemCount());
    }

    void jButton1_mouseClicked(MouseEvent e)
    {
    }
}
