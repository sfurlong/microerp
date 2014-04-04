/*
	File:		CRSample.java
	Rights:		Copyright 2006, Altaprise, Inc.
				All Rights Reserved world-wide.
	Purpose:
				Demonstrate the Crystal Reports engine Java wrapper.
	Notes:
				For all functions except printPreview, the required order of clicking
				1.	click loadEngine button
				2.	click openReport button
				3.	press other buttons to test certain functionality
				4.	click closeReport
				5.	click unloadEngine

	Known defects:
				- external moveable modal dialogs that operate on the AWT dispatch
					thread will leave artifacts - cosmetic
*/
package com.altaprise.crystal;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class CRJNI_t extends JFrame
{
	protected static final int MIN_WIDTH = 600;
	protected static final int MIN_HEIGHT = 400;

	protected Dimension extent = new Dimension( MIN_WIDTH, MIN_HEIGHT );

	protected JButton loadEngine  = new JButton( "loadEngine" ) ;
	protected JButton openReport  = new JButton( "openReport" );
	protected JButton setParameter  = new JButton( "setParameter" );
	protected JButton setPrinter  = new JButton( "setPrinter" );
	protected JButton pageSetup  = new JButton( "pageSetup" );
	protected JButton setPaperTray  = new JButton( "setPaperTray" );
	protected JButton print  = new JButton( "print" );
	protected JButton discardData  = new JButton( "discardData" );
	protected JButton preview  = new JButton( "preview" );
	protected JButton exportToFile  = new JButton( "exportToFile" );
	protected JButton exportToMAPI  = new JButton( "exportToMapi" );
	protected JButton closeReport  = new JButton( "closeReport" );
	protected JButton unloadEngine  = new JButton( "unloadEngine" );

	protected JTextArea status;

	protected short reportId;
	protected Object[] printer;

	// change the following to match your DB
	protected static final String DB_DLL = "p2sodbc.dll";	// oracle 7
	protected static final String DB_HOST = "artifacts";
	protected static final String DB_USER = "SYSDBA";
	protected static final String DB_PASSWORD = "masterkey";
	JPanel p = new JPanel();
    XYLayout xYLayout1 = new XYLayout();
    JLabel jLabel_rptName = new JLabel();
    XYLayout xYLayout2 = new XYLayout();
    JTextField jTextField_rptName = new JTextField();
    JLabel jLabel_parmNum = new JLabel();
    JTextField jTextField_parmNum = new JTextField();
    JLabel jLabel_parmVal = new JLabel();
    JTextField jTextField_parmVal = new JTextField();

	static CRJNI crjni = new CRJNI();

    JLabel jLabel_prtDriverName = new JLabel();
    JTextField jTextField_prtrDriverName = new JTextField();
    JLabel jLabel_prtrName = new JLabel();
    JTextField jTextField_prtrName = new JTextField();
    JLabel jLabel_prtrPort = new JLabel();
    JTextField jTextField_prtrPort = new JTextField();
    JLabel jLabel_tray = new JLabel();
    JTextField jTextField_tray = new JTextField();

	// init
	{
		reportId = -1;
	}

	public static void main( String[] args )
	{
		CRJNI_t frame = new CRJNI_t();
	}

	public CRJNI_t()
	{
		super( "CRJNI Sample" );
        jbInit();
    }

    private void jbInit() {

//		JComponent root = (JComponent)getContentPane();
//		root.setLayout( new BorderLayout() );

		p.setLayout( xYLayout2 );
        xYLayout1.setHeight(433);
        xYLayout1.setWidth(658);
        jLabel_rptName.setText("Rpt Name:");
        jLabel_parmNum.setText("ParmNum:");
        jLabel_parmVal.setText("ParmVal:");
        jLabel_prtDriverName.setText("PrintDriverName:");
        jLabel_prtrName.setText("Printer Name:");
        jLabel_prtrPort.setText("Printer Port:");
        jLabel_tray.setText("Tray:");
        p.add( loadEngine, new XYConstraints(43, 5, -1, -1));
		p.add( openReport, new XYConstraints(145, 5, -1, -1));
		p.add( setParameter, new XYConstraints(249, 5, -1, -1));
		p.add( discardData, new XYConstraints(363, 5, -1, -1));
		p.add( pageSetup, new XYConstraints(31, 37, -1, -1));
		p.add( setPrinter, new XYConstraints(131, 37, -1, -1));
		p.add( setPaperTray, new XYConstraints(223, 37, -1, -1));
		p.add( print, new XYConstraints(335, 37, -1, -1));
		p.add( preview, new XYConstraints(399, 37, -1, -1));
		p.add( exportToMAPI, new XYConstraints(13, 69, -1, -1) );
		p.add( exportToFile, new XYConstraints(125, 69, -1, -1) );
		p.add( closeReport, new XYConstraints(231, 69, -1, -1) );
		p.add( unloadEngine, new XYConstraints(337, 69, -1, -1) );
        p.add(jLabel_rptName, new XYConstraints(27, 106, -1, -1));
        p.add(jTextField_rptName, new XYConstraints(89, 105, 191, -1));
        p.add(jLabel_parmNum, new XYConstraints(26, 131, -1, -1));
        p.add(jTextField_parmNum, new XYConstraints(89, 129, 47, -1));
        p.add(jLabel_parmVal, new XYConstraints(36, 153, -1, -1));
        p.add(jTextField_parmVal, new XYConstraints(89, 153, 191, -1));
        p.add(jLabel_prtDriverName, new XYConstraints(284, 107, -1, -1));
        p.add(jTextField_prtrDriverName, new XYConstraints(381, 104, 247, -1));
        p.add(jTextField_prtrName, new XYConstraints(381, 128, 247, -1));
        p.add(jLabel_prtrName, new XYConstraints(302, 132, -1, -1));
        p.add(jLabel_prtrPort, new XYConstraints(314, 154, -1, -1));
        p.add(jTextField_prtrPort, new XYConstraints(381, 153, 247, -1));
//        this.getContentPane().add(jsp, new XYConstraints(30, 227, 507, 161));

		loadEngine.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doLoadEngine();}} );
		openReport.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doOpenReport();}} );
		setParameter.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doSetParameter();}} );
		setPrinter.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doSetPrinter();}} );
		setPaperTray.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doSetPaperTray();}} );
		print.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doPrint();}} );
		discardData.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doDiscardData();}} );
		pageSetup.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doPageSetup();}} );
		preview.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doPreview();}} );
		exportToMAPI.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doExportToMAPI();}} );
		exportToFile.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doExportToFile();}} );
		closeReport.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doCloseReport();}} );
		unloadEngine.addActionListener( new ActionListener(){ public void actionPerformed( ActionEvent ae ){ doUnloadEngine();}} );

		status = new JTextArea();
		status.setEditable( false );
		JScrollPane jsp = new JScrollPane( status, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS  );
		jsp.setPreferredSize( new Dimension( 200, 200 ) );

		this.getContentPane().setLayout(xYLayout1);
        this.getContentPane().add(jLabel_tray, new XYConstraints(330, 178, -1, -1));
        this.getContentPane().add(jTextField_tray, new XYConstraints(377, 176, 79, -1));
		this.getContentPane().add( p, new XYConstraints(0, 0, 507, 180) );
		this.addWindowListener( new WindowListener(){
						   public void windowActivated( WindowEvent evt ){ repaint();}
						 public void windowClosed( WindowEvent evt ){}
						 public void windowClosing( WindowEvent evt ){ System.exit(0);}
						 public void windowDeactivated( WindowEvent evt ){}
						 public void windowDeiconified( WindowEvent evt ){ repaint();}
						 public void windowIconified( WindowEvent evt ){}
						 public void windowOpened( WindowEvent evt ){ CRJNI_t.this.setState( Frame.NORMAL );}} );

		pack();
        // always frontMost work-around
		setVisible( true );
		//((JComponent)getContentPane()).requestDefaultFocus();
	}

	public Dimension getPreferredSize()
	{
		return extent;
	}

	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}


	/**
		doLoadEngine - load CR engine and default log on<P>
		
		Log on the database after opening the CR engine<P>
		
		Req #1, #2
	*/
	public void doLoadEngine()
	{
        try {
		    crjni.doLoadEngine();
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doOpenReport - open the sample report<P>
		
	*/      
	public void doOpenReport()
	{
        try {
		    crjni.doOpenReport(jTextField_rptName.getText());
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doSetParameter - set a parameter<P>
		
		Specifically, change the TO_NAME parameter on the form letter.
		Sometimes it is preferrable to use setCurrentValue.<P>
		
		Req #4
	*/
	public void doSetParameter()
	{
        String parmNum = jTextField_parmNum.getText();
        String parmVal = jTextField_parmVal.getText();

        try {
    		crjni.doSetParameter(Integer.parseInt(parmNum), parmVal);
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doSetPrinter - override the reports printer - sets to default<P>
		
		Req #5
	*/
	public void doSetPrinter()
	{
        String driverName = jTextField_prtrDriverName.getText();
        String prtrName = jTextField_prtrName.getText();
        String prtrPort = jTextField_prtrPort.getText();
        try{
		    crjni.doSetPrinter(driverName, prtrName, prtrPort);
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doPageSetup - change the printer and some DevMode stuff<P>

		Basically, pop up a Windows printer dialog to select the printer,
		that extracts everything I need to set the printer.<P>
	*/
	public void doPageSetup()
	{
		crjni.doPageSetup();
	}

	/**
		doSetPaperTray - setPaperTray<P>
		
		This should wrap dmPaperSize known constants into a ComboBox...<P>

		Req #6
	*/
	public void doSetPaperTray()
	{
        try {
    		crjni.doSetPaperTray(Short.parseShort(jTextField_tray.getText()));
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doPrint - print the report<P>
		
		Req #7
	*/  
	public void doPrint()
	{
        try {
    		crjni.doPrint((short)1);
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doPreview - preview the report<P>
		
		This call illustrates how to use the crystal report engine
		such that the preview does not block the AWT Event Dispatch
		thread.<P>
		
		This call does not depend on the CRSample instance - as
		it is in its own thread it must open its own copy of the
		engine on the thread.  Do not mix engine calls accross threads.<P>
		
		Req #8
	*/  
	public void doPreview()
	{
		crjni.doPreview(jTextField_rptName.getText(), new String[]{"SUPER", jTextField_parmVal.getText()});
	}

	/**
		doExportToMAPI - export a report in RTF format via e-mail<P>
		
		Req #10
	*/      
	public void doExportToMAPI()
	{
        try {
    		crjni.doExportToMAPI();
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doExportToFile - print report to file<P>
		
		Req #9
		
	*/
	public void doExportToFile()
	{
        try {
    		crjni.doExportToFile();
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doCloseReport - close the report<P>
	*/
	public void doCloseReport()
	{
        try {
    		crjni.doCloseReport();
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}

	/**
		doUnloadEngine - close the engine<P>
	*/      
	public void doUnloadEngine()
	{
		crjni.doUnloadEngine();
	}

	/**
		doDiscardData - discard report saved data, forcing requery and prompting.
	*/
	public void doDiscardData()
	{
        try {
    		crjni.doDiscardData();
        } catch (Exception e) {
            //System.out.println(e.printStackTrace());
        }
	}
}