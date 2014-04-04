// Title:
//Version:
//Copyright:
//Author:
//Company:
//Description:

package dai.client.clientShared;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class daiFrameMenuBar extends JMenuBar {
	public static int FILE_MENU = 0;

	public static int DOC_MENU = 4;

	public static int RESOURCE_MENU = 3;

	public static int HELP_MENU = 5;

	public static int SEARCH_MENU = 1;

	public static int ACTION_MENU = 2;

	private JMenu fileMenu = new JMenu();

	private JMenu helpMenu = new JMenu();

	private JMenu docMenu = new JMenu();

	private JMenu resourceMenu = new JMenu();

	private JMenu searchMenu = new JMenu();

	private JMenu actionMenu = new JMenu();

	private JMenuItem menuItemWindow = new JMenuItem();

	private JMenuItem menuItemNew = new JMenuItem();

	private JMenuItem menuItemReset = new JMenuItem();

	private JMenuItem menuItemSave = new JMenuItem();

	private JMenuItem menuItemSaveAs = new JMenuItem();

	private JMenuItem menuItemDel = new JMenuItem();

	private JMenuItem menuItemExit = new JMenuItem();


	private JMenuItem menuItemHelpUpdates = new JMenuItem();

	private JMenuItem menuItemHelpAbout = new JMenuItem();

	private JMenuItem menuItemFind = new JMenuItem();

	private JMenuItem menuItemPrint = new JMenuItem();

	private JMenuItem menuItemPreview = new JMenuItem();

	private JMenuItem menuItemConfig = new JMenuItem();

	private transient Vector actionListeners;

	private MenuItemListener menuListener = new MenuItemListener();

	public daiFrameMenuBar() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		fileMenu.setText("File");
		helpMenu.setText("Help");
		docMenu.setText("Documents");
		resourceMenu.setText("Resources");
		searchMenu.setText("Search");
		actionMenu.setText("Actions");

		fileMenu.setMnemonic('F');
		helpMenu.setMnemonic('H');
		docMenu.setMnemonic('D');
		resourceMenu.setMnemonic('R');
		searchMenu.setMnemonic('S');
		actionMenu.setMnemonic('A');

		menuItemWindow.setText(daiFrameActions.WINDOW);
		menuItemWindow.setMnemonic('W');
		menuItemWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				Event.CTRL_MASK));
		menuItemWindow.addActionListener(menuListener);
		//menuItemWindow.setEnabled(true);

		menuItemNew.setText(daiFrameActions.NEW);
		menuItemNew.setMnemonic('N');
		menuItemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Event.CTRL_MASK));
		menuItemNew.addActionListener(menuListener);
		//menuItemNew.setEnabled(false); //Default to inactive.

		menuItemReset.setText(daiFrameActions.RESET);
		menuItemReset.setMnemonic('R');
		menuItemReset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				Event.CTRL_MASK));
		menuItemReset.addActionListener(menuListener);

		menuItemSave.setText(daiFrameActions.SAVE);
		menuItemSave.setMnemonic('S');
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Event.CTRL_MASK));
		menuItemSave.addActionListener(menuListener);

		menuItemSaveAs.setText(daiFrameActions.SAVEAS);
		menuItemSaveAs.setMnemonic('A');
		menuItemSaveAs.addActionListener(menuListener);

		menuItemDel.setText(daiFrameActions.DELETE);
		menuItemDel.setMnemonic('D');
		menuItemDel.addActionListener(menuListener);

		menuItemExit.setText(daiFrameActions.EXIT);
		menuItemExit.setMnemonic('x');
		menuItemExit.addActionListener(menuListener);

		menuItemFind.setText(daiFrameActions.FIND);
		menuItemFind.setMnemonic('F');
		menuItemFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				Event.CTRL_MASK));
		menuItemFind.addActionListener(menuListener);

		menuItemPrint.setText(daiFrameActions.PRINT);
		menuItemPrint.setMnemonic('P');
		menuItemPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				Event.CTRL_MASK));
		menuItemPrint.addActionListener(menuListener);

		menuItemPreview.setText(daiFrameActions.PREVIEW);
		menuItemPreview.setMnemonic('V');
		menuItemPreview.addActionListener(menuListener);

		menuItemConfig.setText(daiFrameActions.CONFIG_PRINTER);
		menuItemConfig.addActionListener(menuListener);

		menuItemHelpUpdates.setText(daiFrameActions.HELP_UPDATES);
		menuItemHelpUpdates.addActionListener(menuListener);
		menuItemHelpAbout.setText(daiFrameActions.HELP_ABOUT);
		menuItemHelpAbout.addActionListener(menuListener);

		fileMenu.add(menuItemNew);
		fileMenu.add(menuItemReset);
		fileMenu.add(menuItemSave);
		fileMenu.add(menuItemSaveAs);
		fileMenu.addSeparator();
		fileMenu.add(menuItemDel);
		fileMenu.addSeparator();
		fileMenu.add(menuItemPrint);
		fileMenu.add(menuItemPreview);
		fileMenu.add(menuItemConfig);
		fileMenu.addSeparator();
		fileMenu.add(menuItemWindow);
		fileMenu.addSeparator();
		fileMenu.add(menuItemExit);

		helpMenu.add(menuItemHelpUpdates);
		helpMenu.addSeparator();
		helpMenu.add(menuItemHelpAbout);

		searchMenu.add(menuItemFind);

		this.add(fileMenu);
		this.add(searchMenu);
		this.add(actionMenu);
		this.add(resourceMenu);
		this.add(docMenu);
		this.add(helpMenu);

		this.actionMenu.setEnabled(false);
		this.resourceMenu.setEnabled(false);
		this.docMenu.setEnabled(false);
	}

	public void setNewIdMenuActive(boolean flag) {
		menuItemNew.setEnabled(flag);
	}

	public void setPrintMenusActive(boolean flag) {
		menuItemPrint.setEnabled(flag);
		menuItemPreview.setEnabled(flag);
		menuItemConfig.setEnabled(flag);
	}

	public void setFileMenusActive(boolean flag) {
		menuItemReset.setEnabled(flag);
		menuItemSave.setEnabled(flag);
		menuItemSaveAs.setEnabled(flag);
		menuItemDel.setEnabled(flag);
	}

	public void setSearchMenusActive(boolean flag) {
		menuItemFind.setEnabled(flag);
	}

	public synchronized void removeActionListener(ActionListener l) {
		if (actionListeners != null && actionListeners.contains(l)) {
			Vector v = (Vector) actionListeners.clone();
			v.removeElement(l);
			actionListeners = v;
		}
	}

	public synchronized void addActionListener(ActionListener l) {
		Vector v = actionListeners == null ? new Vector(2)
				: (Vector) actionListeners.clone();
		if (!v.contains(l)) {
			v.addElement(l);
			actionListeners = v;
		}
	}

	protected void fireActionPerformed(ActionEvent e) {
		if (actionListeners != null) {
			Vector listeners = actionListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++) {
				((ActionListener) listeners.elementAt(i)).actionPerformed(e);
			}
		}
	}

	class MenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fireActionPerformed(e);
		}
	}
}