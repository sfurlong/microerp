
//Title:      Your Product Name
//Version:
//Copyright:  Copyright (c) 1998
//Author:     Stephen P. Furlong
//Company:    DAI
//Description:Beans
package daiBeans;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import pv.jfcx.JPVButton;
import pv.jfcx.JPVImageButton;
import pv.jfcx.JPVRoundButton;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class Frame extends JFrame
{
	daiGridController buts = new daiGridController();
	XYLayout xYLayout1 = new XYLayout();
    JButton jButton1 = new JButton();
//    daiSelectionBox selectionBox = new daiSelectionBox();
    daiListBox  listBox = new daiListBox();
    BrowserPanel    browser = new BrowserPanel();

    TitledBorder titledBorder1;
    //javax.swing.JComboBox daiComboBox1 = new javax.swing.JComboBox();
    daiComboBox daiComboBox1 = new daiComboBox();
    daiDateField daiDateField1 = new daiDateField();
    JPVButton jPVButton1 = new JPVButton();
    JPVRoundButton jPVRoundButton1 = new JPVRoundButton();
    JPVImageButton jPVImageButton1 = new JPVImageButton();
    daiButtonBar daiButtonBar1 = new daiButtonBar();
    daiLabel daiLabel1 = new daiLabel();
    JPanel jPanel1 = new JPanel();
    XYLayout xYLayout2 = new XYLayout();
    daiTextArea textArea = new daiTextArea();
    daiRadioButton radioButton1 = new daiRadioButton();
    daiRadioButton radioButton2 = new daiRadioButton();
    ButtonGroup buttonGroup = new ButtonGroup();

    daiCheckBox checkBox = new daiCheckBox();
    JTextArea jTextArea1 = new JTextArea();
    JPVButton jPVButton2 = new JPVButton();

//    csAdapterFactory    _adapterFactory;
//    csDBAdapter         _dbAdapter;

    JToolBar javaToolBar = new JToolBar();
    JPopupMenu jPopupMenu1 = new JPopupMenu();
    JMenuItem jMenuItem3 = new JMenuItem();
    JRadioButtonMenuItem jRadioButtonMenuItem1 = new JRadioButtonMenuItem();
    JCheckBoxMenuItem jCheckBoxMenuItem1 = new JCheckBoxMenuItem();

	//Construct the frame
	public Frame()
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
        jTextArea1.setText("jTextArea1");
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
        buttonGroup.add(radioButton1);
        buttonGroup.add(radioButton2);

		titledBorder1 = new TitledBorder("");
        this.getContentPane().setLayout(xYLayout1);
		xYLayout1.setHeight(609);
		xYLayout1.setWidth(728);
		jButton1.setText("jButton1");


/*
        selectionBox.addMouseListener(new java.awt.event.MouseAdapter()
        {

            public void mouseClicked(MouseEvent e)
            {
                selectionBox_mouseClicked(e);
            }
        });
*/
		daiDateField1.setText("daiDateField1");
        jPVImageButton1.setText("<u>jPVImageButton1</u>");
        jPVImageButton1.setFileImageAt("f:/release/images/open.gif", 0);
        jPVRoundButton1.setShadowWidth(1);
        jPVRoundButton1.setColorsDepth(1);
        jPVRoundButton1.setCenterArea(5);
        jPVRoundButton1.setShape(1);
        daiLabel1.setText("Vendor Id:");
//        daiLabel1.setHREFstyle(true);
        jPanel1.setBorder(BorderFactory.createEtchedBorder());
        jPanel1.setLayout(xYLayout2);
        browser.setPreferredSize(new Dimension(100, 100));
        jPVButton1.addMouseListener(new java.awt.event.MouseAdapter()
        {

            public void mouseClicked(MouseEvent e)
            {
                jPVButton1_mouseClicked(e);
            }
        });
/*        
        daiComboBox1.addItemListener(new java.awt.event.ItemListener()
        {

            public void itemStateChanged(ItemEvent e)
            {
                daiComboBox1_itemStateChanged(e);
            }
        });
                daiComboBox1.adddaiDataModifiedListener(new daiDataModifiedListener()
                    {
                        public void daiDataModified(daiDataModifiedEvent e)
                        {
                            System.out.println("++++++++++++++\n"+e);
                        }
                    });
*/
//        this.getContentPane().add(selectionBox, new XYConstraints(533, 63, 137, 338));
        this.getContentPane().add(browser, new XYConstraints(92, 238, 606, 144));
        this.getContentPane().add(jButton1, new XYConstraints(22, 509, -1, -1));
        this.getContentPane().add(daiButtonBar1, new XYConstraints(20, 388, 406, 33));
        this.getContentPane().add(jPVButton1, new XYConstraints(7, 208, -1, -1));
        this.getContentPane().add(listBox, new XYConstraints(577, 39, -1, 124));
        this.getContentPane().add(jPVImageButton1, new XYConstraints(13, 278, -1, -1));
        this.getContentPane().add(jPanel1, new XYConstraints(148, 483, 325, 95));
        jPanel1.add(daiDateField1, new XYConstraints(19, 16, -1, -1));
        jPanel1.add(daiComboBox1, new XYConstraints(167, 16, -1, -1));
        jPanel1.add(daiLabel1, new XYConstraints(161, 53, -1, -1));
        jPanel1.add(jPVRoundButton1, new XYConstraints(238, 15, -1, -1));
        this.getContentPane().add(buts, new XYConstraints(12, 447, 360, 36));
        this.getContentPane().add(textArea, new XYConstraints(503, 460, 176, 98));
        this.getContentPane().add(checkBox, new XYConstraints(19, 424, 158, 21));
        this.getContentPane().add(radioButton1, new XYConstraints(461, 407, -1, -1));
        this.getContentPane().add(radioButton2, new XYConstraints(501, 408, -1, -1));
        this.getContentPane().add(jTextArea1, new XYConstraints(154, 55, 377, 86));
		this.setTitle("Frame Title");

        javaToolBar.add(new JButton(new ImageIcon("f:/release/images/toolbarButtonGraphics/general/Copy16.gif")));
        this.getContentPane().add(javaToolBar);
        jPopupMenu1.add(jRadioButtonMenuItem1);
        jPopupMenu1.add(jCheckBoxMenuItem1);
        jPopupMenu1.add(jMenuItem3);

        //grid.createColumns(new int[]{grid.CHECKBOX_COL_TYPE,
        //                                grid.CHAR_COL_TYPE,
        //                                grid.INTEGER_COL_TYPE,
        //                                grid.DOUBLE_COL_TYPE});

        String[] du = {"head1", "head2", "head3", "head4"};

//        selectionBox.setBusinessObj(new itemObj());
		pack();


        //Logon to db.
        //_dbAdapter.connect("jdbc:odbc:artifacts", "SYSDBA", "masterkey");


//        _adapterFactory = _adapterFactory.getInstance();
//        _dbAdapter      = _adapterFactory.getDBAdapter();

        listBox.addItem("Item1");
        listBox.addItem("Item2");

        daiComboBox1.addItem("1");
        daiComboBox1.addItem("2");

        checkBox.setDisabled(true);

        //daiComboBox1.setEditable(false);
        //daiComboBox1.setDisabled(true);

        daiComboBox1.setSelectedItem("3.00");
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

    void jPVButton1_mouseClicked(MouseEvent e)
    {
        try {
            String execCmd = "c:/progra~1/plus!/micros~1/iexplore.exe http://www.yahoo.com";
            System.out.println(execCmd);
            Process p = Runtime.getRuntime().exec(execCmd);
        }catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }

        String html = "<HTML>"+
                "<HEAD>"+
//                "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=windows-1252\">"+
                "<TITLE>XML Report Servlet</TITLE>"+
                "<H1>XML Report Servlet</H1>"+
                "</HEAD>"+
                "<BODY>"+
                "<FORM  action=\"http://nt_server:8080/servlet/dai.server.servlets.XMLRptServlet?parm1=1+parm2=2\" method=\"POST\">"+
                "<BR><BR>valid eCorp user  <input size=25 type=text name=userId>"+
                "<BR><BR>valid eCorp user password  <input type=\"password\" size=25 name=passwd>"+
                "<BR><BR>XML Rpt File  <input type=text size=25 name=rptName>"+
                "<BR><BR> press Submit to launch servlet ItemInventoryServlet"+
                "<BR><BR><input type=submit value=\"Submit\"><input type=reset value=\"Reset\"></form>"+
                "</BODY>"+
                "</HTML>";

                browser.setHTMLText(html);
    }

    void daiComboBox1_itemStateChanged(ItemEvent e)
    {
        System.out.println("========================================\n"+e);
    }

    void jButton1_actionPerformed(ActionEvent e)
    {
    }

    void jButton1_mouseClicked(MouseEvent e)
    {
        Point p = e.getPoint();

        //jPopupMenu1.show(jPanel1, e.getX(), e.getY());
        jPopupMenu1.show(jPanel1, p.x, p.y);
    }



/*
    void selectionBox_mouseClicked(MouseEvent e)
    {
        daiTextField1.setText(selectionBox.getSelectedItem());
    }
*/
}
