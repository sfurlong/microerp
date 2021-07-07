//Title:
//Version:
//Copyright:
//Author:
//Company:
//Description:

package  dai.client.clientShared;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;


public class daiDocPrintMenuBar extends JMenuBar
{
	JMenu fileMenu = new JMenu();

	JMenuItem menuItemReset = new JMenuItem();
	JMenuItem menuItemPrint = new JMenuItem();
	JMenuItem menuItemPreview = new JMenuItem();
	JMenuItem menuItemExit = new JMenuItem();
    JMenuItem menuItemConfig = new JMenuItem();

    private transient Vector actionListeners;

    MenuItemListener menuListener = new MenuItemListener();

	public daiDocPrintMenuBar() {
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		fileMenu.setText("File");

		fileMenu.setMnemonic('F');

		menuItemReset.setText(daiFrameActions.RESET);
		menuItemReset.setMnemonic('R');
        menuItemReset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
        menuItemReset.addActionListener(menuListener);

		menuItemPrint.setText(daiFrameActions.PRINT);
		menuItemPrint.setMnemonic('P');
        menuItemPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
        menuItemPrint.addActionListener(menuListener);

		menuItemPreview.setText(daiFrameActions.PREVIEW);
		menuItemPreview.setMnemonic('V');
        menuItemPreview.addActionListener(menuListener);

		menuItemConfig.setText(daiFrameActions.CONFIG_PRINTER);
        menuItemConfig.addActionListener(menuListener);

		menuItemExit.setText(daiFrameActions.EXIT);
		menuItemExit.setMnemonic('X');
        menuItemExit.addActionListener(menuListener);

		fileMenu.add(menuItemReset);
		fileMenu.add(menuItemPrint);
		fileMenu.add(menuItemPreview);
        //fileMenu.add(menuItemConfig);
		fileMenu.addSeparator();
		fileMenu.add(menuItemExit);

		this.add(fileMenu);
	}

    public void setPrintMenusActive(boolean flag) {
        menuItemPrint.setEnabled(flag);
        menuItemPreview.setEnabled(flag);
    }

    public synchronized void removeActionListener(ActionListener l)
    {
        if(actionListeners != null && actionListeners.contains(l))
        {
            Vector v = (Vector) actionListeners.clone();
            v.removeElement(l);
            actionListeners = v;
        }
    }

    public synchronized void addActionListener(ActionListener l)
    {
        Vector v = actionListeners == null ? new Vector(2) : (Vector) actionListeners.clone();
        if(!v.contains(l))
        {
            v.addElement(l);
            actionListeners = v;
        }
    }

    protected void fireActionPerformed(ActionEvent e)
    {
        if(actionListeners != null)
        {
            Vector listeners = actionListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++)
            {
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
