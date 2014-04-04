//Title:
//Version:
//Copyright:
//Author:
//Company:
//Description:

package  dai.shared.cmnSvcs;

import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.borland.jbcl.control.BevelPanel;
import com.borland.jbcl.layout.PaneConstraints;
import com.borland.jbcl.layout.PaneLayout;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import daiBeans.daiButton;

public class DetailInfoDialog extends JDialog
{
	Panel panel = new Panel();
	XYLayout xYLayout1 = new XYLayout();
	BevelPanel bevelPanel = new BevelPanel();
	daiButton button_ok = new daiButton();
	JTextArea JTextArea_msg = new JTextArea();
	String dlgMsg;
    JScrollPane jScrollPane1 = new JScrollPane();
    PaneLayout paneLayout1 = new PaneLayout();

	public DetailInfoDialog(JFrame frame, String title, boolean modal, String msg) {
		super(frame, title, modal);
		dlgMsg = msg;
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public DetailInfoDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public DetailInfoDialog(JFrame frame, String title) {
		this(frame, title, false);
	}

	public DetailInfoDialog(JFrame frame) {
		this(frame, "", false);
	}

	private void jbInit() throws Exception {
		xYLayout1.setWidth(564);
		xYLayout1.setHeight(257);
		button_ok.setMnemonic(KeyEvent.VK_O);
        button_ok.setSelected(true);
        button_ok.setText("OK");
        button_ok.setEnabled(true);
		button_ok.addActionListener(new StandardDialog1_button_ok_actionAdapter(this));

		panel.setLayout(xYLayout1);
		bevelPanel.setLayout(paneLayout1);
        panel.add(bevelPanel, new XYConstraints(9, 10, 544, 191));
        bevelPanel.add(jScrollPane1, new PaneConstraints("jScrollPane1", "jScrollPane1", PaneConstraints.ROOT, 1.0f));
        panel.add(button_ok, new XYConstraints(227, 211, 74, 25));
        jScrollPane1.getViewport().add(JTextArea_msg, null);
		JTextArea_msg.setText(dlgMsg);
		JTextArea_msg.setEditable(false);

        //Decorate the Window
		this.getContentPane().add(panel);
		this.addWindowListener(new StandardDialog1_this_windowAdapter(this));

        showDlg();
	}

	public void showDlg()
	{
		//Center the window
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		show();
	}


	// OK
	void button_ok_actionPerformed(ActionEvent e) {
		dispose();
	}

	// Cancel
	void button_cancel_actionPerformed(ActionEvent e) {
		dispose();
	}

	void this_windowClosing(WindowEvent e) {
		dispose();
	}
}

class StandardDialog1_button_ok_actionAdapter implements ActionListener
{
	DetailInfoDialog adaptee;

	StandardDialog1_button_ok_actionAdapter(DetailInfoDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.button_ok_actionPerformed(e);
	}
}

class StandardDialog1_button_cancel_actionAdapter implements ActionListener
{
	DetailInfoDialog adaptee;

	StandardDialog1_button_cancel_actionAdapter(DetailInfoDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent e) {
		adaptee.button_cancel_actionPerformed(e);
	}
}

class StandardDialog1_this_windowAdapter extends WindowAdapter
{
	DetailInfoDialog adaptee;

	StandardDialog1_this_windowAdapter(DetailInfoDialog adaptee) {
		this.adaptee = adaptee;
	}

	public void windowClosing(WindowEvent e) {
		adaptee.this_windowClosing(e);
	}
}
