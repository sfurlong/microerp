
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package daiBeans;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dai.client.clientShared.daiBannerPanel;
import dai.client.clientShared.daiDocPrintPanel;
import com.altaprise.crystal.CRJNI;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;

public class daiPrintScreen extends JDialog
{
    String RPT_FILE_NAME;
    daiDocPrintPanel _docPrintPanel;
    Logger _logger = Logger.getInstance();
    JPanel buttonPanel = new JPanel();
    FlowLayout flowLayout1 = new FlowLayout();
    daiButton button_print = new daiButton();
    daiButton button_preview = new daiButton();
    private String COMPONENT_ID = "";
    private daiBannerPanel  bannerPanel = new daiBannerPanel();
    private SessionMetaData sessionMeta = null;

//********************************************************************//
//                  CONSTRUCTORS                                      //
//********************************************************************//

	public daiPrintScreen(JFrame frame, String title, BusinessObject obj,
                            Vector vecRptFileName,
                            daiDocPrintPanel docPrintPanel,
                            String transId)
	{
        super(frame, title, true);
		BUSINESS_OBJ = obj;
        RPT_FILE_NAME = (String)vecRptFileName.firstElement();

        this.setBannerLeftText("Print Document");
        this.setTitle("Print Document");



        _docPrintPanel = docPrintPanel;
//        _docPrintPanel.setContainerFrame(frame);

		try
		{
            sessionMeta = SessionMetaData.getInstance();
			jbInit();
			pack();

            //populate the panel
            if (transId != null) {
                query(transId);
                setPrintActionsDisabled(false);
            }
		} catch (Exception ex)
		{
			LOGGER.logError(frame, "Could not initialize daiPrintScreen.\n" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
        LOGGER = Logger.getInstance();
        buttonPanel.setMaximumSize(new Dimension(32767, 40));
        buttonPanel.setMinimumSize(new Dimension(160, 40));
        buttonPanel.setPreferredSize(new Dimension(160, 40));
        buttonPanel.setLayout(flowLayout1);
        buttonPanel.add(button_print, null);
        buttonPanel.add(button_preview, null);

        button_print.setLength(80);
        button_print.setMnemonic(KeyEvent.VK_P);
        button_print.setText("Print");
        button_print.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doPrint();
            }
        });
        button_preview.setLength(80);
        button_preview.setMnemonic(KeyEvent.VK_V);
        button_preview.setText("Preview");
        button_preview.addActionListener(new java.awt.event.ActionListener(){
           public void actionPerformed(ActionEvent e){
            doPreview();
           }
        });



        //Setup the default button bar.
//        String imgBase = sessionMeta.getImagesHome();
//        buttonBar.insertButton(daiFrameActions.PRINT, imgBase+"print24.gif", "Print");
//        buttonBar.insertButton(daiFrameActions.PREVIEW, imgBase+"printpreview24.gif", "Preview");
        setPrintActionsDisabled(true);

        this.getContentPane().add(_docPrintPanel, BorderLayout.NORTH);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        centerFrame();
        this.setVisible(true);
	}

//***************************************************************//
//              PUBLIC METHODS
//***************************************************************//





    public void setComponentId(String compId) {
        COMPONENT_ID = compId;
    }

    public String getComponentId() {
        return COMPONENT_ID;
    }

    public void setBannerLeftText(String t)
    {
        bannerPanel.setBannerLeftText(t);
    }

    public String getBannerLeftText()
    {
        return bannerPanel.getBannerLeftText();
    }

    public void setBannerRightText(String t)
    {
        bannerPanel.setBannerRightText(t);
    }

    public String getBannerRightText()
    {
        return bannerPanel.getBannerRightText();
    }



    public void setPrintActionsDisabled(boolean flag) {
        //button_print.setEnabled(!flag);
        //button_preview.setEnabled(!flag);
    }

//***************************************************************//
//              PROTECTED
//***************************************************************//

	protected BusinessObject BUSINESS_OBJ;
	protected Logger LOGGER;
	//True if a DB record has populated this frame.
	//false if no record has populated this frame yet.
	protected boolean IS_ACTIVE = false;


	//Intended for the Decendants to override.
        //This should be used to generate a new unique Id for
        //the transaction managed by this frame
	protected String generateNewUniqueId()
	{
        LOGGER.logError((JFrame)this.getOwner(), "This option is not valid for this Frame.");
        return "0";
	}

	protected void processWindowEvent(WindowEvent e)
	{
		//Is the user closing the window?
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
		    doClose();
		}
	}

        protected void doClose()
        {
          //Check to see if the user printer setting have changed.
          //If so see if the user want's to same them
          if (_docPrintPanel.isUserSettingsChanged()) {
              int ret = JOptionPane.showConfirmDialog(this, "Save your printer setting changes?",
                                                  "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
                                                  JOptionPane.WARNING_MESSAGE, null);

              if (ret == JOptionPane.YES_OPTION) {
                  //Save the print options
                  _docPrintPanel.savePrinterOptionChanges();
                  this.dispose();
              } else if (ret == JOptionPane.NO_OPTION) {
                  this.dispose();
              }
          } else {
              this.dispose();
          }
        }

//***************************************************************//
//                      PRIVATE
//***************************************************************//
	private void centerFrame()
	{
		//Center the window
          this.pack();
          Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
          Dimension frameSize = this.getSize();
          if (frameSize.height > screenSize.height)
              frameSize.height = screenSize.height;
          if (frameSize.width > screenSize.width)
              frameSize.width = screenSize.width;
          setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
	}

    public void query(String id)
    {
		try
		{

                  //Do a refresh first to make sure nothing from the previos
                  //record is hanging around.
                  _docPrintPanel.refresh();
                  _docPrintPanel.query(id, BUSINESS_OBJ);


                  IS_ACTIVE = true;
                  setPrintActionsDisabled(false);

		} catch (Exception ex)
		{
			ex.printStackTrace();
			LOGGER.logError((JFrame)this.getOwner(), "daiDocPrintFrame::Query Could not populate tab.\n" + ex);
		}
    }




//	private void daiFrame_actionPerformed(java.awt.event.ActionEvent e) {
//		String actionCommand = e.getActionCommand();
//
//		if (actionCommand.equals(daiFrameActions.EXIT)) {
//			doClose();
//		} else if (actionCommand.equals(daiFrameActions.PRINT)) {
//            doPrint();
//		} else if (actionCommand.equals(daiFrameActions.PREVIEW)) {
//            doPreview();
//		} else if (actionCommand.equals(daiFrameActions.RESET)) {
//            _docPrintPanel.refresh();
//            setPrintActionsDisabled(true);
//		} else if (actionCommand.equals(daiFrameActions.CONFIG_PRINTER)) {
//            doConfigPrinter();
//        }
//	}

    private void doPrint() {
        String printer = _docPrintPanel.getPrinterName();
        String port = _docPrintPanel.getPort();
        short  nCopies = _docPrintPanel.getNumCopies();
        short  paperTray = _docPrintPanel.getPaperTray();

        if (printer == null) {
            JOptionPane.showMessageDialog(this, "Please enter a Printer Name in the Printer Options section of this window.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (port == null) {
            JOptionPane.showMessageDialog(this, "Please enter a Printer Port in the Printer Options section of this window.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
        CRJNI crjni = new CRJNI();
        crjni.doLoadEngine();

        crjni.doOpenReport(sessionMeta.getDaiRptHome() + RPT_FILE_NAME);

        crjni.doGetSelectedPrinter();
        //crjni.doSetPaperTray(paperTray);

        crjni.doSetParameter(0, BUSINESS_OBJ.getObjLocality());
        String[] parms = _docPrintPanel.getRptParms();
        for (int i=0; i<parms.length; i++) {
            crjni.doSetParameter(i+1, parms[i]);
        }

        crjni.doPrint(nCopies);

        } catch (Exception e) {
            String msg = e.getClass().getName() + "\n" + e.getLocalizedMessage();
            _logger.logError((JFrame)this.getOwner(), msg);
        }
    }

    private void doPreview() {
        try {
            //Get the parms
            String[] parms = _docPrintPanel.getRptParms();
            String[] parms2 = new String[parms.length+1];
            parms2[0] = BUSINESS_OBJ.getObjLocality();
            for (int i=0; i<parms.length; i++) {
                parms2[i+1] = parms[i];
            }

            CRJNI crjni = new CRJNI();
            crjni.doPreview(sessionMeta.getDaiRptHome() + RPT_FILE_NAME, parms2);
        } catch (Exception e) {
            String msg = e.getClass().getName() + "\n" + e.getLocalizedMessage();
            _logger.logError((JFrame)this.getOwner(), msg);
        }
    }

    private void doConfigPrinter() {
        String printer = _docPrintPanel.getPrinterName();
        String port = _docPrintPanel.getPort();
        short  nCopies = _docPrintPanel.getNumCopies();
        short  paperTray = _docPrintPanel.getPaperTray();

        if (printer == null) {
            JOptionPane.showMessageDialog(this, "Please enter a Printer Name in the Printer Options section of this window.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (port == null) {
        	JOptionPane.showMessageDialog(this, "Please enter a Printer Port in the Printer Options section of this window.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
        CRJNI crjni = new CRJNI();
        crjni.doLoadEngine();

        crjni.doOpenReport(sessionMeta.getDaiRptHome() + RPT_FILE_NAME);

        crjni.doGetSelectedPrinter();

        crjni.doSetPrinter(printer, printer, port);
        crjni.doPageSetup();


        } catch (Exception e) {
            String msg = e.getClass().getName() + "\n" + e.getLocalizedMessage();
            _logger.logError((JFrame)this.getOwner(), msg);
        }
    }


}

