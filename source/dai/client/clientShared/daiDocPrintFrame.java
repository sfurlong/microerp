// Title: Business Artifacts
//Version:
//Copyright: Copyright (c) 1998
//Author: Stephen P. Furlong
//Company: Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.net.URLEncoder;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.borland.jbcl.layout.BoxLayout2;

import com.altaprise.crystal.CRJNI;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.PropertyFileData;
import dai.shared.cmnSvcs.daiProperties;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.daiButtonBar;
import daiBeans.daiGenericEvent;
import daiBeans.daiGenericEventListener;

public class daiDocPrintFrame extends JInternalFrame implements
        daiInternalFrameInterface {
    String RPT_FILE_NAME;

    daiDocPrintPanel _docPrintPanel;

    Logger _logger = Logger.getInstance();

    private Vector TAB_PANELS = new Vector();

    private String COMPONENT_ID = "";

    private daiBannerPanel bannerPanel = new daiBannerPanel();

    private JPanel contentPanel = new JPanel();

    private BoxLayout2 boxLayout21 = new BoxLayout2();

    private SessionMetaData sessionMeta = null;

    private daiFrameMenuBar daiMenuBar = new daiFrameMenuBar();
    
    //This flag indicates if we should print with the PDF export servlet or
    //the CRinf dlls.
    protected boolean DO_WEB_PRINT = false;

    //********************************************************************//
    //                  CONSTRUCTORS //
    //********************************************************************//
    public daiDocPrintFrame(JFrame container, BusinessObject obj,
            String rptFileName, daiDocPrintPanel docPrintPanel,
            String frameTitle) {
        CONTAINER = container;
        BUSINESS_OBJ = obj;
        RPT_FILE_NAME = rptFileName;

        this.setBannerLeftText(frameTitle);
        this.setTitle(frameTitle);

        //Let the selection box know which type of
        //business object we will be searching on.
        selectionBox.setBusinessObj(BUSINESS_OBJ);

        _docPrintPanel = docPrintPanel;
        _docPrintPanel.setContainerFrame(this);
        
        //Check the property file to see if we are configured to do web printing.
        String prop = PropertyFileData.getInstance().getProperty(daiProperties.RPT_USE_WEB);
        if (prop != null && prop.equalsIgnoreCase("true")) {
        	DO_WEB_PRINT = true; 
        }
        
        try {
            sessionMeta = SessionMetaData.getInstance();
            jbInit();
            pack();

        } catch (Exception ex) {
            LOGGER.logError(container, "Could not initialize daiFrame.\n"
                    + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    public daiDocPrintFrame(JFrame container, BusinessObject obj,
            String rptFileName, daiDocPrintPanel docPrintPanel,
            String frameTitle, String transId) {
        CONTAINER = container;
        BUSINESS_OBJ = obj;
        RPT_FILE_NAME = rptFileName;

        this.setBannerLeftText(frameTitle);
        this.setTitle(frameTitle);

        //Let the selection box know which type of
        //business object we will be searching on.
        selectionBox.setBusinessObj(BUSINESS_OBJ);

        _docPrintPanel = docPrintPanel;
        _docPrintPanel.setContainerFrame(this);

        try {
            sessionMeta = SessionMetaData.getInstance();
            jbInit();
            pack();

            //populate the panel
            if (transId != null) {
                queryAllPanels(transId);
                setPrintActionsDisabled(false);
            }
            TabbedPane.setSelectedIndex(1);
        } catch (Exception ex) {
            LOGGER.logError(container, "Could not initialize daiFrame.\n"
                    + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception {
        this.setBorder(null);
        LOGGER = Logger.getInstance();

        //Setup the button bar
        this.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                this_keyPressed(e);
            }

            public void keyTyped(KeyEvent e) {
                this_keyTyped(e);
            }

            public void keyReleased(KeyEvent e) {
                this_keyReleased(e);
            }
        });

        //Setup the default button bar.
        String imgBase = sessionMeta.getImagesHome();
        buttonBar = new daiButtonBar(imgBase);
        setPrintActionsDisabled(true);

        buttonBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                daiInternalFrame_actionPerformed(e);
            }
        });

        daiMenuBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                daiInternalFrame_actionPerformed(e);
            }
        });
        daiMenuBar.setFileMenusActive(false);
        daiMenuBar.setNewIdMenuActive(false);
        selectionBox.adddaiGenericEventListener(new daiGenericEventListener() {
            public void genericEventAction(daiGenericEvent e) {
                selectionBox_itemSelected(e);
            }
        });
        TabbedPane.setBorder(BorderFactory.createLineBorder(Color.black));
        TabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                TabbedPane_stateChanged(e);
            }
        });

        //This panel contains the Banner and the Tab Pane.
        //It will reside on the righ side of the split pane.
        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        contentPanel.setLayout(boxLayout21);
        contentPanel.add(bannerPanel);
        contentPanel.add(TabbedPane);
        TabbedPane.addTab("Search", selectionBox);
        TabbedPane.setEnabledAt(0, true);
        addTabPanel("Main", _docPrintPanel);
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);

        javax.swing.plaf.InternalFrameUI ui = this.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) ui).setNorthPane(null);

        show();
    }

    //***************************************************************//
    //              PUBLIC METHODS
    //***************************************************************//
    public daiStatusBar statusBar = new daiStatusBar();

    public JPanel getDaiStatusBar() {
        return statusBar;
    }

    public void setDaiStatusBar(JPanel _daiStatusBar) {
        statusBar = (daiStatusBar) _daiStatusBar;
    }

    public JToolBar getDaiButtonBar() {
        return buttonBar;
    }

    public void setDaiButtonBar(JToolBar _daiButtonBar) {
        buttonBar = (daiButtonBar) _daiButtonBar;
    }

    public JMenuBar getDaiMenuBar() {
        return daiMenuBar;
    }

    public void setDaiMenuBar(JMenuBar _daiMenuBar) {
        daiMenuBar = (daiFrameMenuBar) _daiMenuBar;
    }

    public void setTabsEnabled(boolean flag) {
        TabbedPane.setSelectedIndex(1);

        //Loop through all the TabPanels and disable them.
        for (int i = 1; i < TAB_PANELS.size(); i++) {
            TabbedPane.setEnabledAt(i + 1, flag);
        }
    }

    public void addTabPanel(String label, daiDocPrintPanel panel) {
        TAB_PANELS.addElement(panel);
        TabbedPane.addTab(label, new JScrollPane(panel));
    }

    public void setComponentId(String compId) {
        COMPONENT_ID = compId;
    }

    public String getComponentId() {
        return COMPONENT_ID;
    }

    public void setBannerLeftText(String t) {
        bannerPanel.setBannerLeftText(t);
    }

    public String getBannerLeftText() {
        return bannerPanel.getBannerLeftText();
    }

    public void setBannerRightText(String t) {
        bannerPanel.setBannerRightText(t);
    }

    public String getBannerRightText() {
        return bannerPanel.getBannerRightText();
    }

    public void setStatusRight(String s) {
        statusBar.setRightStatus(s);
    }

    public void setPrintActionsDisabled(boolean flag) {
        buttonBar.setButtonDisabled(daiButtonBar.PRINT, flag);
        buttonBar.setButtonDisabled(daiButtonBar.PREVIEW, flag);
        daiMenuBar.setPrintMenusActive(!flag);
    }

    public void setReportFileName(String rptName) {
        this.RPT_FILE_NAME = rptName;
    }

    //***************************************************************//
    //              PROTECTED
    //***************************************************************//
    protected daiBeans.daiSelectionPanel selectionBox = new daiBeans.daiSelectionPanel();

    protected daiButtonBar buttonBar;

    protected BusinessObject BUSINESS_OBJ;

    protected Logger LOGGER;

    public JFrame CONTAINER = null;

    protected JTabbedPane TabbedPane = new JTabbedPane();

    //True if a DB record has populated this frame.
    //false if no record has populated this frame yet.
    protected boolean IS_ACTIVE = false;

    //Intended for the Decendants to override.
    protected void TabbedPane_stateChanged(ChangeEvent e) {
    }

    //Intended for the Decendants to override.
    //This should be used to generate a new unique Id for
    //the transaction managed by this frame
    protected String generateNewUniqueId() {
        LOGGER.logError(CONTAINER, "This option is not valid for this Frame.");
        return "0";
    }

    protected void processWindowEvent(WindowEvent e) {
        //Is the user closing the window?
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            doClose();
        }
    }

    protected void doClose() {
        //Check to see if the user printer setting have changed.
        //If so see if the user want's to same them
        if (_docPrintPanel.isUserSettingsChanged()) {
            int ret = JOptionPane.showConfirmDialog(this,
                    "Save your printer setting changes?", "Warning",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE, null);

            if (ret == JOptionPane.YES_OPTION) {
                //Save the print options
                _docPrintPanel.savePrinterOptionChanges();
                System.out.println("Ending Application");
                int retVal = JOptionPane.showOptionDialog(this,
                        "Closing this window will close all windows",
                        "Closing Application", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (retVal == JOptionPane.YES_OPTION)
                    System.exit(0);
            } else if (ret == JOptionPane.NO_OPTION) {
                System.out.println("Ending Application");
                int retVal = JOptionPane.showOptionDialog(this,
                        "Closing this window will close all windows",
                        "Closing Application", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);

                if (retVal == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        } else {
            System.out.println("Ending Application");
            int retVal = JOptionPane.showOptionDialog(this,
                    "Closing this window will close all windows",
                    "Closing Application", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (retVal == JOptionPane.YES_OPTION)
                System.exit(0);
        }
    }

    //***************************************************************//
    //                      PRIVATE
    //***************************************************************//
    public void queryAllPanels(String id) {
        try {
            //Loop through all the TabPanels and do the query.
            for (int i = 0; i < TAB_PANELS.size(); i++) {
                daiDocPrintPanel panel = (daiDocPrintPanel) TAB_PANELS
                        .elementAt(i);

                //Do a refresh first to make sure nothing from the previos
                //record is hanging around.
                panel.refresh();
                panel.query(id, BUSINESS_OBJ);
            }

            IS_ACTIVE = true;
            setPrintActionsDisabled(false);

        } catch (Exception ex) {
            ex.printStackTrace();
            LOGGER.logError(CONTAINER,
                    "daiDocPrintFrame::Query Could not populate tab.\n" + ex);
        }
    }

    //Selection Box Entry Was Double Clicked.
    //Call query for all the panels.
    private void selectionBox_itemSelected(daiGenericEvent e) {
        String id = selectionBox.getSelectedItem();

        queryAllPanels(id);
        TabbedPane.setSelectedIndex(1);
        //Set the banner panel to the new id
        bannerPanel.setBannerRightText(": " + id);
        //Update the statusbar
        statusBar.setLeftStatus("");
        buttonBar.setButtonDisabled(daiButtonBar.PRINT, false);
        buttonBar.setButtonDisabled(daiButtonBar.PREVIEW, false);
    }

    public void daiInternalFrame_actionPerformed(java.awt.event.ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (actionCommand.equals(daiFrameActions.EXIT)) {
            doClose();
        } else if (actionCommand.equals(daiFrameActions.PRINT)) {
        	if (this.DO_WEB_PRINT) {
        		doWebPrint();
        	} else {
        		doPrint();
        	}
        } else if (actionCommand.equals(daiFrameActions.PREVIEW)) {
        	if (this.DO_WEB_PRINT) {
        		doWebPrint();
        	} else {
                doPreview();
        	}
        } else if (actionCommand.equals(daiFrameActions.RESET)) {
            _docPrintPanel.refresh();
            setPrintActionsDisabled(true);
        } else if (actionCommand.equals(daiFrameActions.CONFIG_PRINTER)) {
            doConfigPrinter();
        } else if (actionCommand.equals(daiFrameActions.FIND)) {
            TabbedPane.setSelectedIndex(0);
        } else if (actionCommand.equals(daiFrameActions.WINDOW)) {
            new dai.client.clientAppRoot.daiExplorerFrame();
        }
    }

    private void doPrint() {
        String printer = _docPrintPanel.getPrinterName();
        String port = _docPrintPanel.getPort();
        short nCopies = _docPrintPanel.getNumCopies();
        short paperTray = _docPrintPanel.getPaperTray();

        if (printer == null) {
            JOptionPane
                    .showMessageDialog(
                            this,
                            "Please enter a Printer Name in the Printer Options section of this window.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (port == null) {
            JOptionPane
                    .showMessageDialog(
                            this,
                            "Please enter a Printer Port in the Printer Options section of this window.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            CRJNI crjni = new CRJNI();
            crjni.doLoadEngine();

            System.out.println("Printing: " + RPT_FILE_NAME);
            crjni.doOpenReport(sessionMeta.getDaiRptHome() + RPT_FILE_NAME);

            crjni.doGetSelectedPrinter();
            //crjni.doSetPaperTray(paperTray);

            crjni.doSetParameter(0, BUSINESS_OBJ.getObjLocality());
            String[] parms = _docPrintPanel.getRptParms();
            for (int i = 0; i < parms.length; i++) {
                crjni.doSetParameter(i + 1, parms[i]);
            }

            crjni.doPrint(nCopies);
            statusBar.setLeftStatus("Document Printed");
        } catch (Exception e) {
            String msg = e.getClass().getName() + "\n"
                    + e.getLocalizedMessage();
            _logger.logError(CONTAINER, msg);
        }
    }

    private void doWebPrint() {
        try {
            String[] parms = _docPrintPanel.getRptParms();
        	String rptParams = "&parm0="+ BUSINESS_OBJ.getObjLocality();
            for (int i = 0; i < parms.length; i++) {
                rptParams +="&parm"+(i+1)+"="+parms[i];
            }
        	String rptFileName = URLEncoder.encode(RPT_FILE_NAME, "UTF-8");
        	String rptPrintURL = PropertyFileData.getInstance().getProperty(daiProperties.RPT_WEBPRINT_URL);
        	rptPrintURL += "?rptName="+rptFileName+"&userid="+sessionMeta.getUserId()+rptParams;
        	System.out.println(rptPrintURL);
        	BrowserLauncher.openURL(rptPrintURL);
        } catch (Exception e) {
            String msg = e.getClass().getName() + "\n"
            + e.getLocalizedMessage();
            _logger.logError(CONTAINER, msg);
        }
    }
    
    private void doPreview() {
        try {
            //Get the parms
            String[] parms = _docPrintPanel.getRptParms();
            String[] parms2 = new String[parms.length + 1];
            parms2[0] = BUSINESS_OBJ.getObjLocality();
            for (int i = 0; i < parms.length; i++) {
                parms2[i + 1] = parms[i];
            }

            CRJNI crjni = new CRJNI();
            System.out.println("Printing: " + RPT_FILE_NAME);
            crjni
                    .doPreview(sessionMeta.getDaiRptHome() + RPT_FILE_NAME,
                            parms2);
        } catch (Exception e) {
            String msg = e.getClass().getName() + "\n"
                    + e.getLocalizedMessage();
            _logger.logError(CONTAINER, msg);
        }
    }

    private void doConfigPrinter() {
        //Check to see if the user printer setting have changed.
        //If so see if the user want's to same them
        if (_docPrintPanel.isUserSettingsChanged()) {
            int ret = JOptionPane.showConfirmDialog(this,
                    "Save your printer setting changes?", "Warning",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE, null);

            if (ret == JOptionPane.YES_OPTION) {
                //Save the print options
                _docPrintPanel.savePrinterOptionChanges();
                statusBar.setLeftStatus("Saved");
            } else if (ret == JOptionPane.NO_OPTION) {
                statusBar.setLeftStatus("Canceled");
            }
        } else {
            statusBar.setLeftStatus("Saved");
        }
    }


    void this_keyPressed(KeyEvent e) {
        //System.out.println(e);
    }

    void this_keyTyped(KeyEvent e) {
        //System.out.println(e);
    }

    protected void setFrameDefaultFocus() {
        TabbedPane.requestFocus();
        daiDocPrintPanel panel = (daiDocPrintPanel) TAB_PANELS.elementAt(0);
        panel.resetTabEntrySeq();
    }

    void this_keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1 && TabbedPane.getTabCount() >= 1) {
            TabbedPane.setSelectedIndex(0);
            setFrameDefaultFocus();
        } else if (e.getKeyCode() == KeyEvent.VK_F2
                && TabbedPane.getTabCount() >= 2 && TabbedPane.isEnabledAt(1)) {
            TabbedPane.setSelectedIndex(1);
        } else if (e.getKeyCode() == KeyEvent.VK_F3
                && TabbedPane.getTabCount() >= 3 && TabbedPane.isEnabledAt(2)) {
            TabbedPane.setSelectedIndex(2);
        } else if (e.getKeyCode() == KeyEvent.VK_F4
                && TabbedPane.getTabCount() >= 4 && TabbedPane.isEnabledAt(3)) {
            TabbedPane.setSelectedIndex(3);
        } else if (e.getKeyCode() == KeyEvent.VK_F5
                && TabbedPane.getTabCount() >= 5 && TabbedPane.isEnabledAt(4)) {
            TabbedPane.setSelectedIndex(4);
        } else if (e.getKeyCode() == KeyEvent.VK_F6
                && TabbedPane.getTabCount() >= 6 && TabbedPane.isEnabledAt(5)) {
            TabbedPane.setSelectedIndex(5);
        } else if (e.getKeyCode() == KeyEvent.VK_F7
                && TabbedPane.getTabCount() >= 7 && TabbedPane.isEnabledAt(6)) {
            TabbedPane.setSelectedIndex(6);
        } else if (e.getKeyCode() == KeyEvent.VK_F8
                && TabbedPane.getTabCount() >= 8 && TabbedPane.isEnabledAt(7)) {
            TabbedPane.setSelectedIndex(7);
        } else if (e.getKeyCode() == KeyEvent.VK_F9
                && TabbedPane.getTabCount() >= 9 && TabbedPane.isEnabledAt(8)) {
            TabbedPane.setSelectedIndex(8);
        } else if (e.getKeyCode() == KeyEvent.VK_F10
                && TabbedPane.getTabCount() >= 10 && TabbedPane.isEnabledAt(9)) {
            TabbedPane.setSelectedIndex(9);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            doClose();
        }
    }

    public void daiInternalFrame_keyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1 && TabbedPane.getTabCount() >= 1) {
            TabbedPane.setSelectedIndex(0);
            setFrameDefaultFocus();
        } else if (e.getKeyCode() == KeyEvent.VK_F2
                && TabbedPane.getTabCount() >= 2 && TabbedPane.isEnabledAt(1)) {
            TabbedPane.setSelectedIndex(1);
        } else if (e.getKeyCode() == KeyEvent.VK_F3
                && TabbedPane.getTabCount() >= 3 && TabbedPane.isEnabledAt(2)) {
            TabbedPane.setSelectedIndex(2);
        } else if (e.getKeyCode() == KeyEvent.VK_F4
                && TabbedPane.getTabCount() >= 4 && TabbedPane.isEnabledAt(3)) {
            TabbedPane.setSelectedIndex(3);
        } else if (e.getKeyCode() == KeyEvent.VK_F5
                && TabbedPane.getTabCount() >= 5 && TabbedPane.isEnabledAt(4)) {
            TabbedPane.setSelectedIndex(4);
        } else if (e.getKeyCode() == KeyEvent.VK_F6
                && TabbedPane.getTabCount() >= 6 && TabbedPane.isEnabledAt(5)) {
            TabbedPane.setSelectedIndex(5);
        } else if (e.getKeyCode() == KeyEvent.VK_F7
                && TabbedPane.getTabCount() >= 7 && TabbedPane.isEnabledAt(6)) {
            TabbedPane.setSelectedIndex(6);
        } else if (e.getKeyCode() == KeyEvent.VK_F8
                && TabbedPane.getTabCount() >= 8 && TabbedPane.isEnabledAt(7)) {
            TabbedPane.setSelectedIndex(7);
        } else if (e.getKeyCode() == KeyEvent.VK_F9
                && TabbedPane.getTabCount() >= 9 && TabbedPane.isEnabledAt(8)) {
            TabbedPane.setSelectedIndex(8);
        } else if (e.getKeyCode() == KeyEvent.VK_F10
                && TabbedPane.getTabCount() >= 10 && TabbedPane.isEnabledAt(9)) {
            TabbedPane.setSelectedIndex(9);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            //fileClose();
        }
    }
}

