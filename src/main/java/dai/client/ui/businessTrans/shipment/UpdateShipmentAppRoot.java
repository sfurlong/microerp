
//Title:        order
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI for Entry/Update of Orders

package dai.client.ui.businessTrans.shipment;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.shared.cmnSvcs.DetailInfoDialog;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;
import daiBeans.daiRoundButton;

public class UpdateShipmentAppRoot extends JFrame
{
	boolean packFrame = false;

    csSessionAdapterFactory sessionAdapterFactory = null;
    SessionMetaData         sessionMeta = null;

	//Construct the application
	public UpdateShipmentAppRoot() {

        //Crank client/server up the adapter factories.
        sessionAdapterFactory = csSessionAdapterFactory.getInstance();

        if (userLogin()) {
            //Start the application
    		UpdateShipmentFrame updateShipmentFrame = new UpdateShipmentFrame(this);
            updateShipmentFrame.setIsStandAloneApp(true);

        } else {
            System.exit(0);
        }
	}


    private boolean userLogin() {

        boolean ret = false;
        boolean tryAgain = true;

        JFrame  tempFrame = new JFrame();
        PasswordDialog passwordDialog;

        while (tryAgain) {
            passwordDialog = new PasswordDialog(tempFrame, "eCorp Login", true);
            if (passwordDialog.getUserAction().equals("OK"))
            {

                csSessionAdapter sessionAdapter = null;
                sessionAdapter = sessionAdapterFactory.getSessionAdapter();
                sessionMeta = SessionMetaData.getInstance();
                try {
                    csSecurity security = sessionAdapter.connect(sessionMeta.getServerDBURL(),
                                        passwordDialog.getUserId(),
                                        passwordDialog.getPassword());

                    ret = true;
                    tryAgain = false;
                    sessionMeta.setClientServerSecurity(security);
                } catch (Exception e) {
                    DetailInfoDialog dialog = new DetailInfoDialog(null, "Error", true, e.getLocalizedMessage() );
                }

            } else if (passwordDialog.getUserAction().equals("CANCEL")) {
                System.out.println("User pressed Cancel");
                tryAgain = false;
            }
        }

        return ret;
    }

	//Main method
	public static void main(String[] args) {

		try
		{
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
			UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
		} catch (Exception e)
		{
            e.printStackTrace();
            System.out.println(e);
		}
		new UpdateShipmentAppRoot();
	}
}

class PasswordDialog extends JDialog
{
	JPanel  buttonPanel = new JPanel();
	JPanel  entryPanel  = new JPanel();

    String  userAction  = "";

	daiRoundButton button_ok = new daiRoundButton();
	daiRoundButton button_cancel = new daiRoundButton();
	FlowLayout flowLayout1 = new FlowLayout();
	XYLayout xYLayout1 = new XYLayout();

	daiBeans.daiLabel daiLabel_passwd = new daiBeans.daiLabel("Password:");
	daiBeans.daiLabel daiLabel_uid = new daiBeans.daiLabel("User Id:");

	daiBeans.daiTextField daiTextfield_uid = new daiBeans.daiTextField();
	JPasswordField passwordField = new JPasswordField();
    XYLayout xYLayout2 = new XYLayout();
    FlowLayout flowLayout2 = new FlowLayout();

	public PasswordDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public PasswordDialog(JFrame frame, String title) {
		this(frame, title, false);
	}

	public PasswordDialog(JFrame frame) {
		this(frame, "", false);
	}

	private void jbInit() throws Exception {

        button_ok.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                button_ok_actionPerformed(e);
            }
        });
        button_ok.setText("OK");
        button_cancel.setText("Cancel");
        button_cancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                button_cancel_actionPerformed(e);
            }
        });

		buttonPanel.setLayout(flowLayout2);

        //Decorate the entry Panel
        entryPanel.setBorder(BorderFactory.createEtchedBorder());
        entryPanel.setMaximumSize(new Dimension(400, 150));
        entryPanel.setMinimumSize(new Dimension(400, 150));
        entryPanel.setPreferredSize(new Dimension(400, 150));
		entryPanel.setSize(new Dimension(392, 154));
        entryPanel.setLayout(xYLayout2);
        buttonPanel.setMaximumSize(new Dimension(400, 35));
        buttonPanel.setMinimumSize(new Dimension(88, 35));
        buttonPanel.setPreferredSize(new Dimension(88, 35));
        passwordField.setFont(new java.awt.Font("Monospaced", 0, 11));
        daiTextfield_uid.setFont(new java.awt.Font("SansSerif", 0, 11));

        buttonPanel.add(button_ok, null);
        buttonPanel.add(button_cancel, null);

        entryPanel.add(daiTextfield_uid, new XYConstraints(112, 33, 175, -1));
        entryPanel.add(passwordField, new XYConstraints(112, 72, 175, -1));
        entryPanel.add(daiLabel_uid, new XYConstraints(58, 36, -1, -1));
        entryPanel.add(daiLabel_passwd, new XYConstraints(42, 74, -1, -1));

        //Decorate the Dialog
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.getContentPane().add(entryPanel);
        this.getContentPane().add(buttonPanel);

        centerAndShowDlg();
	}

    private void centerAndShowDlg()
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

    public String getUserId()
    {
        return daiTextfield_uid.getText();
    }

    public String getPassword()
    {
    	char[] ch = passwordField.getPassword(); 
        return new String(ch);
    }

    public String getUserAction()
    {
        return userAction;
    }

    void button_cancel_actionPerformed(ActionEvent e)
    {
        userAction = "CANCEL";
        this.dispose();
    }

    void button_ok_actionPerformed(ActionEvent e)
    {
        userAction = "OK";
        System.out.println("OK HIT");
        this.dispose();
    }
}


