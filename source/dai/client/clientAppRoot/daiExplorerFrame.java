package dai.client.clientAppRoot;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.altaprise.clientupdate.ClientUpdate;

import dai.client.clientShared.daiFrameActions;
import dai.client.clientShared.daiFrameMenuBar;
import dai.client.clientShared.daiInternalFrameInterface;
import dai.client.clientShared.daiStatusBar;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.UserSecurityManager;
import daiBeans.daiButtonBar;

public class daiExplorerFrame extends JFrame {

	static final long serialVersionUID = -1L; 
	
    //Construct the frame
    private daiFrameMenuBar frameMenuBar = new daiFrameMenuBar();

    private daiButtonBar buttonBar;
    private FrameCache _frameCache = new FrameCache();
    
    Logger logger;

    Hashtable _hashDaiIFrames = new Hashtable();

    SessionMetaData sessionMeta;

    JSplitPane _splitPane = new JSplitPane();

    daiExplorerTree _tree = new daiExplorerTree();

    JTabbedPane _tabbedPane = new JTabbedPane();

    JDesktopPane _desktopPane = new JDesktopPane();

    daiStatusBar _statusBar = new daiStatusBar();

    JScrollPane jScrollPane1 = new JScrollPane();

    public daiExplorerFrame() {
        try {
            sessionMeta = SessionMetaData.getInstance();
            logger = Logger.getInstance();
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public daiExplorerFrame(String title) {
        super(title);
        try {

            sessionMeta = SessionMetaData.getInstance();
            logger = Logger.getInstance();
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        frameMenuBar.setNewIdMenuActive(false);
        frameMenuBar.setFileMenusActive(false);
        frameMenuBar.setPrintMenusActive(false);
        frameMenuBar.setSearchMenusActive(false);
        frameMenuBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processActionEvent(e);
            }
        });

        _splitPane.setPreferredSize(new Dimension(750, 450));
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                this_windowClosed(e);
            }

            public void windowClosing(WindowEvent e) {
                this_windowClosing(e);
            }
        });
        _desktopPane.addContainerListener(new ContainerAdapter() {
            public void componentAdded(ContainerEvent e) {
                JInternalFrame iFrame = (JInternalFrame) e.getChild();
                setButtonActivations(iFrame);
            }
        });

        jScrollPane1.setAutoscrolls(false);
        jScrollPane1.setPreferredSize(new Dimension(150, 324));
        String imgBase = sessionMeta.getImagesHome();
        buttonBar = new daiButtonBar(imgBase);
        buttonBar.setButtonDisabled(daiButtonBar.NEW, true);
        buttonBar.setButtonDisabled(daiButtonBar.RESET, true);
        buttonBar.setButtonDisabled(daiButtonBar.PRINT, true);
        buttonBar.setButtonDisabled(daiButtonBar.PREVIEW, true);
        buttonBar.setButtonDisabled(daiButtonBar.SAVE, true);
        buttonBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processActionEvent(e);
            }
        });

        //Define the Tree Selection Listener.
        _tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 1) {
                    treeSelection_valueChanged(((daiExplorerTree) e.getSource())
                            .getSelectionModel());
                }
            }
        });
        
        javax.swing.Action doPressedEnter = new javax.swing.AbstractAction() {
        	static final long serialVersionUID = -1L; 

        	public void actionPerformed(ActionEvent e) {
        		TreePath path = _tree.getLeadSelectionPath();
                if (path == null)
                    return;
                if (path.getLastPathComponent() == null)
                    return;
                daiExplorerNode node = (daiExplorerNode) path.getLastPathComponent();
                if (node.isLeaf()) {
                    launchNodeComponent(node.getClassName(), "");
                } else {
                	_tree.expandPath(path);
                }
        	}
    	};
		_tree.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "pressedEnter");
		_tree.getActionMap().put("pressedEnter", doPressedEnter);
        
        javax.swing.Action doPressedESC = new javax.swing.AbstractAction() {
        	static final long serialVersionUID = -1L; 
        	public void actionPerformed(ActionEvent e) {
        		if (!_tree.hasFocus()) {
        			_tree.requestFocusInWindow();
        		} else {
        			_desktopPane.requestFocusInWindow();
        		}
        	}
    	};
		_splitPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F12"), "pressedESC");
		_splitPane.getActionMap().put("pressedESC", doPressedESC);
		
        _tree.addTreeExpansionListener(new TreeExpansionListener() {
            public void treeExpanded(TreeExpansionEvent e) {
            }

            public void treeCollapsed(TreeExpansionEvent e) {
                jScrollPane1.revalidate();
            }
        });
        //Decorate the split pane
        _splitPane.add(_desktopPane, JSplitPane.RIGHT);
        jScrollPane1.getViewport().add(_tree, null);
        jScrollPane1.getViewport().setSize(20, 20);
        _splitPane.setOneTouchExpandable(true);
        _splitPane.add(jScrollPane1, JSplitPane.LEFT);

        //Decorate the frame
        this.setSize(new Dimension(700, 450));
        this.setTitle("microERP Explorer");
        this.setJMenuBar(frameMenuBar);
        this.getContentPane().add(buttonBar, BorderLayout.NORTH);
        this.getContentPane().add(_splitPane, BorderLayout.CENTER);
        this.getContentPane().add(_statusBar, BorderLayout.SOUTH);

        this
                .setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        centerFrame();
        pack();
        this.setVisible(true);
        //Let the tree have default focus.
        this._tree.requestFocusInWindow();
    }

    private void centerFrame() {
        //Center the window
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        this.setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
    }

    private void treeSelection_valueChanged(TreeSelectionModel tm) {

        TreePath path = tm.getLeadSelectionPath();
        if (path == null)
            return;
        if (path.getLastPathComponent() == null)
            return;
        daiExplorerNode node = (daiExplorerNode) path.getLastPathComponent();
        if (node.isLeaf()) {
            launchNodeComponent(node.getClassName(), "");
        }
    }

    //File | Exit action performed
    private void fileExit_actionPerformed(ActionEvent e) {
        applicationFinalizer();
    }

    //Help | About action performed
    private void doHelpAbout() {
        daiAboutBox dlg = new daiAboutBox(this);
    }

    //Help | Check for Updates
    private void doHelpUpdates() {
    	//Run the Client Update Utility.
    	ClientUpdate clientUpdates = new ClientUpdate(this);
    }

    private void processActionEvent(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals(daiFrameActions.BACKWARD)) {
            daiInternalFrameInterface frame = _frameCache.getPrevFrame();
            if (frame != null ) {
                showInternalFrame(frame);
            }
        } else if (actionCommand.equals(daiFrameActions.FORWARD)) {
            daiInternalFrameInterface frame = _frameCache.getNextFrame();
            if (frame != null ) {
                showInternalFrame(frame);
            }
        } else if (actionCommand.equals(daiFrameActions.EXIT)) {
            applicationFinalizer();
        } else if (actionCommand.equals(daiFrameActions.WINDOW)) {
            new daiExplorerFrame("new");
        } else if (actionCommand.equals(daiFrameActions.HELP_UPDATES)) {
            doHelpUpdates();
        } else if (actionCommand.equals(daiFrameActions.HELP_ABOUT)) {
            doHelpAbout();
        } else {
            //Get the current window
            daiInternalFrameInterface internalFrame = (daiInternalFrameInterface) _desktopPane
                    .getSelectedFrame();
            if (internalFrame != null) {
            	internalFrame.daiInternalFrame_actionPerformed(e);
            }
        }
    }

    public void launchNodeComponent(String launchString, String ID) {
        //Add the existing frame to the stack before we load a new one.
        JInternalFrame currentFrame = _desktopPane.getSelectedFrame();

        if (launchString.equals("dai.client.ui.docGen.PrintCustStmtDoc")) {
            JOptionPane
            .showMessageDialog(
                    this,
                    "This feature has been temporarily disabled.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        
        	return;
        }
        
        daiInternalFrameInterface frameToLaunch = (daiInternalFrameInterface) _hashDaiIFrames
                .get(launchString);

        //If the frame to launch is the same as the current frame, just return.
        if (currentFrame != null && currentFrame == frameToLaunch) {
            return;
        } else {
            if (currentFrame != null) {
                _frameCache.addFrame(currentFrame);
            }
        }

        try {
            //Check security.
            if (!checkSecurity(launchString)) {
                JOptionPane
                        .showMessageDialog(
                                this,
                                "You do not have sufficient priveleges for this operation.",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            //Is the frame already open? Null says no.
            if (frameToLaunch == null) {
                Class c = Class.forName(launchString);
                Class[] cArr;
                Object[] initArgs;
                if (ID != null && !ID.equals("")) {
                    cArr = new Class[] { Class.forName("javax.swing.JFrame"),
                            Class.forName("java.lang.String") };
                    initArgs = new Object[] { this, ID };
                } else {
                    cArr = new Class[] { Class.forName("javax.swing.JFrame") };
                    initArgs = new Object[] { this };
                }
                Constructor formConstruct = c.getConstructor(cArr);

                frameToLaunch = (daiInternalFrameInterface) (formConstruct
                        .newInstance(initArgs));
                
                _desktopPane.add((JInternalFrame) frameToLaunch);
                _hashDaiIFrames.put(launchString, frameToLaunch);
            } else {
                if (ID != null && !ID.equals("")) {
                    ((JInternalFrame) frameToLaunch).dispose();
                    _hashDaiIFrames.remove(launchString);
                    launchNodeComponent(launchString, ID);

                } else {
                    frameToLaunch = (daiInternalFrameInterface) _hashDaiIFrames
                            .get(launchString);
                }
            }

            showInternalFrame(frameToLaunch);

        } catch (NoSuchMethodException nsme) {
            try {
                Class c = Class.forName(launchString);
                c.newInstance();
            } catch (Exception e) {
                Logger.getInstance().logError(
                        this,
                        "Could not initialize Form.\n"
                                + e.getLocalizedMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            Logger.getInstance().logError(this,
                    "Could not initialize Form.\n" + e.getLocalizedMessage());
            e.printStackTrace();
        }finally{
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private void showInternalFrame(daiInternalFrameInterface frameToLaunch) {
        if (_desktopPane.getSelectedFrame() != null) {
            ((JInternalFrame) frameToLaunch).setVisible(true);
        }

        this.setJMenuBar(frameToLaunch.getDaiMenuBar());
        frameToLaunch.setDaiStatusBar((JPanel) _statusBar);
        buttonBar.setButtonDisabled(daiButtonBar.BACKWARD, false);

        try {
            ((JInternalFrame) frameToLaunch).setVisible(true);
            ((JInternalFrame) frameToLaunch).setMaximum(true);
            ((JInternalFrame) frameToLaunch).moveToFront();
            ((JInternalFrame) frameToLaunch).setSelected(true);
            this.setButtonActivations((JInternalFrame)frameToLaunch);
        } catch (Exception e) {
            Logger.getInstance().logError(
                    this,
                    "Unexpected error launching frame.\n"
                            + e.getLocalizedMessage());
        }

        this.invalidate();
        this.validate();
    }

    private void this_windowClosed(WindowEvent e) {
        //System.exit(0);
    }

    void this_windowClosing(WindowEvent e) {
        applicationFinalizer();
    }

    private void applicationFinalizer() {
        System.out.println("Ending Application");
        int retVal = JOptionPane.showOptionDialog(this,
                "Closing this window will close all windows",
                "Closing Application", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);

        if (retVal == JOptionPane.YES_OPTION)
            System.exit(0);
    }

    private boolean checkSecurity(String launchString) {
        StringTokenizer st = new StringTokenizer(launchString, ".");
        String token = "";
        boolean ret = false;
        while (st.hasMoreTokens()) {
            token = st.nextToken();
        }
        try {
            UserSecurityManager securityMgr = UserSecurityManager.getInstance();
            ret = securityMgr.getComponentSecurity(token);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
        return ret;
    }

    private void loadWallPaper() {
        ImageIcon wallPaper_imageIcon = new ImageIcon(sessionMeta
                .getImagesHome()
                + "ECORP_WALLPAPER.JPG", "Ecorp WallPaper");
        JLabel wallPaper_JLabel = new JLabel(wallPaper_imageIcon);
        wallPaper_JLabel.setBounds(0, 0, wallPaper_imageIcon.getIconWidth(),
                wallPaper_imageIcon.getIconHeight());
        _desktopPane.add(wallPaper_JLabel, new Integer(Integer.MIN_VALUE));
    }

    class FrameCache {
        private Stack _frameStackFwd = new Stack();
        private Stack _frameStackBack = new Stack();

        public void addFrame(Object frame) {
            buttonBar.setButtonDisabled(daiButtonBar.BACKWARD, false);
            buttonBar.setButtonDisabled(daiButtonBar.FORWARD, true);
            _frameStackBack.push(frame);
            _frameStackFwd.clear();
        }

        public void dumpStack() {
            java.util.Iterator iter = _frameStackBack.iterator();
            System.out.println("-------StackDump-----------");
            while (iter.hasNext()) {
                System.out.println("StackDump: " + iter.next().getClass().getName());
            }
        }
        
        public daiInternalFrameInterface getPrevFrame() {
            Object ret = null;
            try {
                ret = _frameStackBack.pop();
                buttonBar.setButtonDisabled(daiButtonBar.FORWARD, false);
            } catch (java.util.EmptyStackException ese) {
                //Nothing left in the cache
                buttonBar.setButtonDisabled(daiButtonBar.BACKWARD, true);
                return null;
            }
            JInternalFrame currentFrame = _desktopPane.getSelectedFrame();
            if (currentFrame != null) {
                _frameStackFwd.push(currentFrame);
            }
            return ((daiInternalFrameInterface) ret);
        }
        
        public daiInternalFrameInterface getNextFrame() {
            Object ret = null;
            try {
                ret = _frameStackFwd.pop();
            } catch (java.util.EmptyStackException ese) {
                buttonBar.setButtonDisabled(daiButtonBar.FORWARD, true);
                return null;
            }
            JInternalFrame currentFrame = _desktopPane.getSelectedFrame();
            if (currentFrame != null) {
                _frameStackBack.push(currentFrame);
            }
            return ((daiInternalFrameInterface) ret);
        }
    }

    private void setButtonActivations(JInternalFrame iFrame) {
        if (iFrame instanceof dai.client.clientShared.daiFrame) {
            if (iFrame instanceof dai.client.ui.businessTrans.shipment.ShipmentFrame
                    || iFrame instanceof dai.client.ui.businessTrans.shipment.CashReceiptFrame
                    || iFrame instanceof dai.client.ui.businessTrans.purchOrder.PayVoucherFrame
                    || iFrame instanceof dai.client.ui.corpResources.carrier.carrierFrame
                    || iFrame instanceof dai.client.ui.corpResources.item.ItemFrame
                    || iFrame instanceof dai.client.ui.sysAdmin.UserProfileFrame
                    || iFrame instanceof dai.client.ui.sysAdmin.LocationFrame
                    || iFrame instanceof dai.client.ui.sysAdmin.DefaultAccountsFrame
                    || iFrame instanceof dai.client.ui.sysAdmin.FinanceAcctsFrame) {
                buttonBar.setButtonDisabled(daiButtonBar.NEW, true);
            } else {
                buttonBar.setButtonDisabled(daiButtonBar.NEW, false);
            }
            buttonBar.setButtonDisabled(daiButtonBar.RESET, false);
            buttonBar.setButtonDisabled(daiButtonBar.SAVE, false);
            buttonBar.setButtonDisabled(daiButtonBar.PRINT, true);
            buttonBar.setButtonDisabled(daiButtonBar.PREVIEW, true);
        } else if (iFrame instanceof dai.client.clientShared.daiDocPrintFrame) {
            buttonBar.setButtonDisabled(daiButtonBar.NEW, true);
            buttonBar.setButtonDisabled(daiButtonBar.RESET, true);
            buttonBar.setButtonDisabled(daiButtonBar.SAVE, true);
            buttonBar.setButtonDisabled(daiButtonBar.PRINT, false);
            buttonBar.setButtonDisabled(daiButtonBar.PREVIEW, false);
        } else if (iFrame instanceof dai.client.clientShared.daiWizardFrame) {
            buttonBar.setButtonDisabled(daiButtonBar.NEW, true);
            buttonBar.setButtonDisabled(daiButtonBar.RESET, true);
            buttonBar.setButtonDisabled(daiButtonBar.SAVE, true);
            buttonBar.setButtonDisabled(daiButtonBar.PRINT, true);
            buttonBar.setButtonDisabled(daiButtonBar.PREVIEW, true);
        } else if (iFrame instanceof dai.client.ui.docGen.CheckPrintRptFrame) {
            buttonBar.setButtonDisabled(daiButtonBar.NEW, true);
            buttonBar.setButtonDisabled(daiButtonBar.RESET, true);
            buttonBar.setButtonDisabled(daiButtonBar.SAVE, true);
            buttonBar.setButtonDisabled(daiButtonBar.PRINT, false);
            buttonBar.setButtonDisabled(daiButtonBar.PREVIEW, false);
            ((dai.client.ui.docGen.CheckPrintRptFrame) iFrame)
                    .refresh();
        }
    }

}

