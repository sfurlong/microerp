package dai.client.clientAppRoot;

import java.awt.AWTEvent;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.shared.cmnSvcs.SessionMetaData;

public class daiAboutBox extends Dialog implements ActionListener
{
	JPanel panel_root = new JPanel();
	JPanel panel_text = new JPanel();
	JButton button1 = new JButton();
	ImageIcon imageControl1 = new ImageIcon();
	FlowLayout flowLayout2 = new FlowLayout();
	JLabel _company = new JLabel("Altaprise, Inc.");
	JLabel _product = new JLabel("microERP");
	JLabel _copyright = new JLabel("Copyright (c) 1998-2006");
	JLabel _version = new JLabel();
	JLabel _build = new JLabel();
	JLabel _patch = new JLabel();
    SessionMetaData sessionMeta;
    GridLayout gridLayout1 = new GridLayout();
    XYLayout xYLayout2 = new XYLayout();

	public daiAboutBox(Frame parent) {
		super(parent, true);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
        Dimension dlgSize = this.getPreferredSize();
        Dimension frmSize = parent.getSize();
        Point loc = parent.getLocation();
        this.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                (frmSize.height - dlgSize.height) / 2 + loc.y);
		pack();
		this.setVisible(true);
	}

	private void jbInit() throws Exception  {
        sessionMeta = SessionMetaData.getInstance();
		this.setTitle("About");
		setResizable(false);
		panel_root.setLayout(xYLayout2);
		panel_text.setLayout(gridLayout1);
		_product.setText("microERP");
		_product.setFont(new Font("Dialog", 3, 14));
		_version.setText("Version: " + sessionMeta.getVersionInfo());
		_build.setText("Build: " + sessionMeta.getBuildInfo());
		_build.setText("Patch: " + sessionMeta.getPatchNum());
		button1.setText("OK");
		button1.addActionListener(this);
		panel_text.setBorder(BorderFactory.createLoweredBevelBorder());
        gridLayout1.setRows(5);

        xYLayout2.setHeight(152);
        xYLayout2.setWidth(236);

        panel_root.add(panel_text, new XYConstraints(8, 3, 221, -1));
        panel_text.add(_product, null);
        panel_text.add(_company, null);
        panel_text.add(_copyright, null);
        panel_text.add(_version, null);
        panel_text.add(_build, null);
        panel_root.add(button1, new XYConstraints(86, 114, 70, 26));

        this.add(panel_root, null);
        
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			cancel();
		}
		super.processWindowEvent(e);
	}

	void cancel() {
		dispose();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1)
		{
			cancel();
		}
	}
}

