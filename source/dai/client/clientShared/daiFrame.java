// Title: Business Artifacts
//Version:
//Copyright: Copyright (c) 1998
//Author: Stephen P. Furlong
//Company: Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.KeyStroke;

import com.altaprise.clientupdate.ClientUpdate;
import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.OverlayLayout2;

import dai.client.clientAppRoot.daiAboutBox;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiGenericEvent;
import daiBeans.daiGenericEventListener;
import daiBeans.daiSelectionPanel;


abstract public class daiFrame extends JInternalFrame implements
		daiInternalFrameInterface {

	//********************************************************************//
	//                  CONSTRUCTORS //
	//********************************************************************//
	public daiFrame(JFrame container, BusinessObject obj) {
		CONTAINER = container;
		BUSINESS_OBJ = obj;
		//Let the selection box know which type of
		//business object we will be searching on.

		try {
			jbInit();
			selectionBox.setBusinessObj(obj);
			pack();

		} catch (Exception ex) {
			LOGGER.logError(container, "Could not initialize daiFrame.\n"
					+ ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception {
		this.setBorder(null);
		LOGGER = Logger.getInstance();

		buttonBarPanel.setMaximumSize(new Dimension(32767, 37));
		buttonBarPanel.setMinimumSize(new Dimension(28, 37));
		buttonBarPanel.setPreferredSize(new Dimension(304, 37));
		buttonBarPanel.setLayout(overlayLayout21);

		daiMenuBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				daiInternalFrame_actionPerformed(e);
			}
		});

		daiMenuBar.setPrintMenusActive(false);
	
		selectionBox.adddaiGenericEventListener(new daiGenericEventListener() {
			public void genericEventAction(daiGenericEvent e) {
				selectionBox_itemSelected(e);
			}
		});

		_tabbedPane.setBorder(BorderFactory.createLineBorder(Color.black));
		_tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				TabbedPane_stateChanged(e);
			}
		});

		_tabbedPane.addTab("Search", selectionBox);
		_tabbedPane.setEnabledAt(0, true);

		_tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F1"), "pressedF1");
		_tabbedPane.getActionMap().put("pressedF1", doF1);
		_tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "pressedF2");
		_tabbedPane.getActionMap().put("pressedF2", doF2);
		_tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F3"), "pressedF3");
		_tabbedPane.getActionMap().put("pressedF3", doF3);
		_tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F4"), "pressedF4");
		_tabbedPane.getActionMap().put("pressedF4", doF4);
		_tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F5"), "pressedF5");
		_tabbedPane.getActionMap().put("pressedF5", doF5);
		_tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F6"), "pressedF6");
		_tabbedPane.getActionMap().put("pressedF6", doF6);
		_tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F7"), "pressedF7");
		_tabbedPane.getActionMap().put("pressedF7", doF7);
		_tabbedPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F8"), "pressedF8");
		_tabbedPane.getActionMap().put("pressedF8", doF8);
		
		//This panel contains the Banner and the Tab Pane.
		//It will reside on the righ side of the split pane.
		boxLayout21.setAxis(BoxLayout.Y_AXIS);
		contentPanel.setLayout(boxLayout21);
		contentPanel.add(bannerPanel);
		contentPanel.add(_tabbedPane);

		this.getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setRequestFocusEnabled(true);
		javax.swing.plaf.InternalFrameUI ui = this.getUI();
		((javax.swing.plaf.basic.BasicInternalFrameUI) ui).setNorthPane(null);
		centerFrame();
		this.setVisible(true);
		this.requestFocusInWindow();
	}

	//***************************************************************//
	//              PUBLIC METHODS
	//***************************************************************//
	public daiStatusBar statusBar = new daiStatusBar();

	public void setTabsEnabled(boolean flag) {

		//Loop through all the TabPanels and disable them.
		for (int i = 1; i < TAB_PANELS.size(); i++) {
			_tabbedPane.setEnabledAt(i + 1, flag);
		}
	}

	public void addTabPanel(String label, daiPanel panel) {
		TAB_PANELS.addElement(panel);
		_tabbedPane.addTab(label, new JScrollPane(panel));

		//Activate event listeners for checking wether this panel
		//is dirty.
		panel.activateEntryChangeListeners();
	}
	public JMenuBar getDaiMenuBar() {
		return daiMenuBar;
	}
	
	public void setMenuBar(daiFrameMenuBar menu) {
	    
	}
	
	public daiSelectionPanel getDaiSelectionBox() {
		return selectionBox;
	}

	public JPanel getDaiStatusBar() {
		return statusBar;
	}

	public void setDaiStatusBar(JPanel _daiStatusBar) {
		statusBar = (daiStatusBar) _daiStatusBar;
	}

	//CALL BACK from main Panel Contined by this Frame.
	//Called when the user enters a new ID in the main tab.
	//This routine will return 0 if the record did not previously
	//exist, and 0 > if it does exist.
	public int callBackInsertNewId(String id) {
		BusinessObject obj = ((daiPanel) TAB_PANELS.elementAt(0))
				.getActiveBusinessObj();
		Vector ret = null;

		csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
		SessionMetaData sessionMeta = SessionMetaData.getInstance();

		//Check to see if the id already exists.
		//If exists, retreive and populated.
		//If NOT exists, treat as new record.
		String exp = " id = '" + id + "' and locality = '"
				+ sessionMeta.getLocality() + "'";

		try {
			ret = dbAdapter.getAllIds(sessionMeta.getClientServerSecurity(),
					obj.getTableName(), exp);

			//If the ID does NOT exist...
			if (ret.size() == 0) {

				//Loop through all the TabPanels and do the refresh.
				for (int i = 0; i < TAB_PANELS.size(); i++) {
					daiPanel panel = (daiPanel) TAB_PANELS.elementAt(i);

					//Insert the Tab Panel data to the DB.
					panel.BUSINESS_OBJ.set_id(id);

					//Set the defaults because this business object
					//may have been refreshed.
					panel.BUSINESS_OBJ.setDefaults();

					//Mark the Panel as dirty
					panel._panelIsDirty = true;

					//Indicate on Status Panel that this is NEW record
					statusBar.setLeftStatus("New");
				}

				//Make sure we update the the header panel UI to reflect any
				//fields that were updated as a result of inserting a new ID.
				daiHeaderPanel headerPanel = (daiHeaderPanel) TAB_PANELS
						.firstElement();
				headerPanel.update_UI(headerPanel.BUSINESS_OBJ);

				headerPanel.ID_TEXT_FIELD.grabFocus();

			} else { //It already exists...

				queryAllPanels(id);
			}

			IS_ACTIVE = true;

			//Set the banner panel to the new id
			bannerPanel.setBannerRightText(": " + id);
			_tabbedPane.setSelectedIndex(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret.size();
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

	public daiPanel getTabPanel(int index) {
		daiPanel panel = (daiPanel) TAB_PANELS.elementAt(index);
		return panel;
	}

	public void setStatusRight(String s) {
		statusBar.setRightStatus(s);
	}

	//***************************************************************//
	//              PROTECTED
	//***************************************************************//
	protected Vector TAB_PANELS = new Vector();

	protected daiSelectionPanel selectionBox = new daiSelectionPanel();

	protected BusinessObject BUSINESS_OBJ;

	protected Logger LOGGER;

	protected JFrame CONTAINER;

	protected JTabbedPane _tabbedPane = new JTabbedPane();

	protected daiFrameMenuBar daiMenuBar = new daiFrameMenuBar();

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
			fileClose();
		}
	}

	protected void setFrameDefaultFocus() {
		_tabbedPane.requestFocus();
		daiPanel panel = (daiPanel) TAB_PANELS.elementAt(0);
		panel.resetTabEntrySeq();
	}

	protected int fileNew() {
		int ret = checkForUncommittedChanges();
		selectionBox.reset();
		if (ret == JOptionPane.OK_OPTION) {
			// Yes, please save changes, then carry on.
			fileSave();
		} else if (ret == JOptionPane.NO_OPTION) {
			//Don't bother saving, then carry on.
		} else {
			//User Canceled, don't do anything.
			return ret;
		}

		_tabbedPane.setSelectedIndex(1);
		setFrameDefaultFocus();

		//Loop through all the TabPanels and do the refresh.
		for (int i = 0; i < TAB_PANELS.size(); i++) {
			daiPanel panel = (daiPanel) TAB_PANELS.elementAt(i);

			//Turn off the Dirty Panel Checking
			panel.disableDirtyFlagChecking();

			panel.refresh();
		}

		//Clear the banner panel
		bannerPanel.setBannerRightText("");

		//Clear the statusbar
		statusBar.setLeftStatus("");

		IS_ACTIVE = false;

		return ret;
	}

	protected void fileSave() {
		boolean hasUncommittedChanges = false;
		//Check to see if any of the panels has any changes to commit. If one
		//panel has a change, we have to commit all of them.
		for (int i = 0; i < TAB_PANELS.size(); i++) {
			daiPanel panel = (daiPanel) TAB_PANELS.elementAt(i);
			if (panel.hasUncommittedChanges()) {
				hasUncommittedChanges = true;
				break;
			}
		}
		if (!hasUncommittedChanges) {
			//Nothing to do let's get out of here.
			return;
		}

		//Loop through all the TabPanels and do the update.
		for (int i = 0; i < TAB_PANELS.size(); i++) {
			daiPanel panel = (daiPanel) TAB_PANELS.elementAt(i);

			panel.persistPanelData();

			//Reset the Dirty Panel Checking
			panel.enableDirtyFlagChecking();
		}

		//Update the statusbar
		statusBar.setLeftStatus("Saved");
	}

	protected void fileSaveAs() {
		String newId = JOptionPane.showInputDialog(this,
				"Please Enter The New Id.");
		if (newId == null)
			return;

		BusinessObject obj = ((daiPanel) TAB_PANELS.firstElement())
				.getActiveBusinessObj();

		csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
		SessionMetaData sessionMeta = SessionMetaData.getInstance();

		//Check to see if the id already exists.
		String exp = " id = '" + newId + "' and locality = '"
				+ sessionMeta.getLocality() + "'";

		try {
			Vector ret = dbAdapter.getAllIds(sessionMeta
					.getClientServerSecurity(), obj.getTableName(), exp);

			//If the ID DOES exists show an error
			if (ret.size() != 0) {
				JOptionPane.showMessageDialog(this, "The Id you have chosen: "
						+ newId
						+ " already exists.  Please choose a unique Id.");
				return;
			}
		} catch (Exception e) {
			String msg = "Could not perform SaveAs opperation.\n"
					+ e.getLocalizedMessage();
			LOGGER.logError(CONTAINER, msg);
			e.printStackTrace();
			return;
		}

		//Loop through all the TabPanels and update to the new Id.
		for (int i = 0; i < TAB_PANELS.size(); i++) {
			daiPanel panel = (daiPanel) TAB_PANELS.elementAt(i);

			panel.prepareSaveAs(newId);

			panel.persistPanelData();

			//Reset the Dirty Panel Checking
			panel.enableDirtyFlagChecking();
		}

		callBackInsertNewId(newId);

		//Update the statusbar
		statusBar.setLeftStatus("Saved");
	}

	protected void fileDelete() {
		int ret = JOptionPane.showConfirmDialog(this,
				"Are your sure you want to permanently remove this Record?",
				"Warning", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE, null);

		if (ret != JOptionPane.OK_OPTION) {
			return;
		}

		//Loop through all the TabPanels and do the delete.
		for (int i = 0; i < TAB_PANELS.size(); i++) {
			daiPanel panel = (daiPanel) TAB_PANELS.elementAt(i);

			//The DB should cascade delete.
			panel.delete();

			//Refresh all other tabs.
			panel.refresh();

			//Deactivate Dirty Panel Checking
			panel.disableDirtyFlagChecking();
		}

		IS_ACTIVE = false;

		//Clear the banner panel
		bannerPanel.setBannerRightText("");

		//Update the statusbar
		statusBar.setLeftStatus("Deleted");
	}

	protected void fileClose() {
		int ret = checkForUncommittedChanges();

		if (ret == JOptionPane.OK_OPTION) {
			// Yes, please save changes.
			fileSave();
			//Close the window.
			System.out.println("Ending Application");
			int retVal = JOptionPane.showOptionDialog(this,
					"Closing this window will close all windows",
					"Closing Application", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (retVal == JOptionPane.YES_OPTION)
				System.exit(0);
		} else if (ret == JOptionPane.NO_OPTION) {
			//Close with out saving.
			System.out.println("Ending Application");
			int retVal = JOptionPane.showOptionDialog(this,
					"Closing this window will close all windows",
					"Closing Application", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, null, null);

			if (retVal == JOptionPane.YES_OPTION)
				System.exit(0);

		} else {
			//Do nothing.
		}
	}

	//***************************************************************//
	//                      PRIVATE
	//***************************************************************//
	private daiBannerPanel bannerPanel = new daiBannerPanel();

	private JPanel contentPanel = new JPanel();

	private BoxLayout2 boxLayout21 = new BoxLayout2();


	private JPanel buttonBarPanel = new JPanel();

	private OverlayLayout2 overlayLayout21 = new OverlayLayout2();

	private void centerFrame() {
		//Center the window
		this.pack();
	}

	private void queryAllPanels(String id) {
		try {
			//Loop through all the TabPanels and do the query.
			for (int i = 0; i < TAB_PANELS.size(); i++) {
				daiPanel panel = (daiPanel) TAB_PANELS.elementAt(i);

				//Deactivate Dirty Panel Checking
				panel.disableDirtyFlagChecking();

				//Do a refresh first to make sure nothing from the previos
				//record is hanging around.
				panel.refresh();
				panel.query(id);

				//Activate Dirty Panel Checking
				panel.enableDirtyFlagChecking();
			}

			IS_ACTIVE = true;

		} catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.logError(CONTAINER,
					"daiFrame::Query Could not populate tab.\n" + ex);
		}
	}

	//Selection Box Entry Was Double Clicked.
	//Call query for all the panels.
	private void selectionBox_itemSelected(daiGenericEvent e) {
		int ret = checkForUncommittedChanges();

		if (ret == JOptionPane.OK_OPTION) {
			// Yes, please save changes.
			fileSave();
			//then carry on..
		} else if (ret == JOptionPane.NO_OPTION) {
			//No save just carry on.
		} else {
			//Cancel, just return.
			return;
		}

		String id = selectionBox.getSelectedItem();
		queryAllPanels(id);
		_tabbedPane.setSelectedIndex(1);
		//Set the banner panel to the new id
		bannerPanel.setBannerRightText(": " + id);
		//Update the statusbar
		statusBar.setLeftStatus("");
	}

	public void daiInternalFrame_actionPerformed(java.awt.event.ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals(daiFrameActions.RESET)) {
			fileNew();
		} else if (actionCommand.equals(daiFrameActions.NEW)) {
			int isCancel = fileNew();
			if (isCancel != JOptionPane.CANCEL_OPTION) {
				genNewIdAndPopulatePanels();
			}
		} else if (actionCommand.equals(daiFrameActions.SAVE)) {
			fileSave();
		} else if (actionCommand.equals(daiFrameActions.SAVEAS)) {
			fileSaveAs();
		} else if (actionCommand.equals(daiFrameActions.DELETE)) {
			fileDelete();
		} else if (actionCommand.equals(daiFrameActions.HELP_ABOUT)) {
			new daiAboutBox(CONTAINER);
		} else if (actionCommand.equals(daiFrameActions.HELP_UPDATES)) {
			//Run the Client Update Utility.
	    	ClientUpdate clientUpdates = new ClientUpdate(CONTAINER);			
		} else if (actionCommand.equals(daiFrameActions.EXIT)) {
			fileClose();
		} else if (actionCommand.equals(daiFrameActions.PRINT)) {
			//			doPrint();
		} else if (actionCommand.equals(daiFrameActions.PREVIEW)) {
			//			doPreview();
		} else if (actionCommand.equals(daiFrameActions.DISABLE)) {

		} else if (actionCommand.equals(daiFrameActions.FIND)) {
			_tabbedPane.setSelectedIndex(0);
		} else if (actionCommand.equals(daiFrameActions.WINDOW)) {
			new dai.client.clientAppRoot.daiExplorerFrame();
		}
	}

	private void genNewIdAndPopulatePanels() {
		callBackInsertNewId(generateNewUniqueId());
	}

	private int checkForUncommittedChanges() {
		//Check to see if any of the tabs have uncommitted changes
		for (int i = 0; i < TAB_PANELS.size(); i++) {
			daiPanel panel = (daiPanel) TAB_PANELS.elementAt(i);

			//Insert the Tab Panel data to the DB.
			if (panel.hasUncommittedChanges()) {
				int ret = JOptionPane.showConfirmDialog(this,
						"Save current record before exiting?", "Warning",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.WARNING_MESSAGE, null);
				//choiceMessage.
				return ret;
			}
		}
		//No uncommitted changes.
		return JOptionPane.NO_OPTION;
	}

	/**
	 * 
	 * @param F_key_index starts counting at 0
	 */
	private void do_F_keys(int F_key_index) {
		if (F_key_index < _tabbedPane.getTabCount() && _tabbedPane.isEnabledAt(F_key_index)) {
			_tabbedPane.setSelectedIndex(F_key_index);
			_tabbedPane.setSelectedIndex(F_key_index);
		}
	}

    javax.swing.Action doF1 = new javax.swing.AbstractAction() {
    	public void actionPerformed(ActionEvent e) {
    		do_F_keys(0);
    	}
	};
    javax.swing.Action doF2 = new javax.swing.AbstractAction() {
    	public void actionPerformed(ActionEvent e) {
    		do_F_keys(1);
    	}
	};
    javax.swing.Action doF3 = new javax.swing.AbstractAction() {
    	public void actionPerformed(ActionEvent e) {
    		do_F_keys(2);
    	}
	};
    javax.swing.Action doF4 = new javax.swing.AbstractAction() {
    	public void actionPerformed(ActionEvent e) {
    		do_F_keys(3);
    	}
	};
    javax.swing.Action doF5 = new javax.swing.AbstractAction() {
    	public void actionPerformed(ActionEvent e) {
    		do_F_keys(4);
    	}
	};
    javax.swing.Action doF6 = new javax.swing.AbstractAction() {
    	public void actionPerformed(ActionEvent e) {
    		do_F_keys(5);
    	}
	};
    javax.swing.Action doF7 = new javax.swing.AbstractAction() {
    	public void actionPerformed(ActionEvent e) {
    		do_F_keys(6);
    	}
	};
    javax.swing.Action doF8 = new javax.swing.AbstractAction() {
    	public void actionPerformed(ActionEvent e) {
    		do_F_keys(7);
    	}
	};
}