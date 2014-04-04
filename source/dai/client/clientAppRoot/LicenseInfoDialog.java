//Title:
//Version:
//Copyright:
//Author:
//Company:
//Description:

package  dai.client.clientAppRoot;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LicenseInfoDialog extends JDialog
{
    JPanel rootPanel = new JPanel();
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();
	JButton button_accept = new JButton();
	JButton button_decline = new JButton();
	JTextArea JTextArea_msg = new JTextArea();
    JScrollPane jScrollPane1 = new JScrollPane();

    boolean _isAccepted = false;
    FlowLayout flowLayout1 = new FlowLayout();
    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout2 = new BorderLayout();

	public static void main (String[] args) {
		new LicenseInfoDialog(null);
	}

	public LicenseInfoDialog(JFrame frame) {
		super(frame, "License Acceptance", true);
		try
		{
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	private void jbInit() throws Exception {
        String licenseText = loadLicenseText();
        JTextArea_msg.setText(licenseText);

		button_accept.setText("I Accept");
		button_decline.setText("Decline");
        button_accept.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                button_accept_actionPerformed(e);
            }
        });
        button_decline.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                button_decline_actionPerformed(e);
            }
        });

		JTextArea_msg.setLineWrap(true);
        buttonPanel.setLayout(flowLayout1);
        rootPanel.setLayout(borderLayout2);
		JTextArea_msg.setEditable(false);

        //Decorate the Window

        textPanel.setLayout(borderLayout1);
        buttonPanel.setMaximumSize(new Dimension(32767, 37));
        textPanel.setMinimumSize(new Dimension(600, 400));
        textPanel.setPreferredSize(new Dimension(600, 400));
        rootPanel.setMinimumSize(new Dimension(600, 400));
        rootPanel.setPreferredSize(new Dimension(600, 400));
        jScrollPane1.setMinimumSize(new Dimension(600, 400));
        jScrollPane1.setPreferredSize(new Dimension(600, 400));
        textPanel.add(jScrollPane1, BorderLayout.CENTER);
        jScrollPane1.getViewport().add(JTextArea_msg, null);
        buttonPanel.add(button_accept);
        buttonPanel.add(button_decline);
		this.getContentPane().add(rootPanel);

        rootPanel.add(textPanel, BorderLayout.CENTER);
        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

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


    void button_accept_actionPerformed(ActionEvent e)
    {
        _isAccepted = true;
        this.setVisible(false);
    }

    public boolean getResponse() {
        return _isAccepted;
    }

    void button_decline_actionPerformed(ActionEvent e)
    {
        this.setVisible(false);
    }

    private String loadLicenseText() {
        BufferedReader _fileReader;

        String filePath = System.getProperty("DAI_HOME") + "/license.txt";

        String licenseText = "";
        try {
            _fileReader = new BufferedReader(new FileReader(filePath));

            //Priming read
            String t = _fileReader.readLine();
            while (t != null) {
                licenseText += t + "\n";
                //Read the next record
                t = _fileReader.readLine();
            }

            _fileReader.close();

        } catch (Exception e) {
            System.out.println("LicenseInfoDialog::readData\n"+e.getLocalizedMessage());
            e.printStackTrace();
        }
        return licenseText;
    }

}

