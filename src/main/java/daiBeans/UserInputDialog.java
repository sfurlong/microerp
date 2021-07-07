
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

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.borland.jbcl.layout.BoxLayout2;

public class UserInputDialog extends JDialog
{
    public static final int TEXT_INPUT_TYPE = 1;
    public static final int INT_INPUT_TYPE = 2;
    public static final int CURRENCY_INPUT_TYPE = 3;

    public static final int CANCEL_OPTION = 0;
    public static final int OK_OPTION = 1;

    JPanel entryPanel = new JPanel();
	JPanel panel_root = new JPanel();
	daiButton button_ok = new daiButton();
	daiButton button_cancel = new daiButton();
	JPanel buttonPanel = new JPanel();

	int _userButtonChoice = CANCEL_OPTION;

    FlowLayout flowLayout1 = new FlowLayout();
    BoxLayout2 boxLayout21 = new BoxLayout2();
    daiLabel daiLabel_entryText = new daiLabel();
    BorderLayout borderLayout1 = new BorderLayout();

    daiTextField textField_entry = new daiTextField();
    daiNumField intField_entry = new daiNumField();
    daiCurrencyField currencyField_entry = new daiCurrencyField();
    int _inputType = 0;

	public UserInputDialog(JFrame frame, String title, String promptText, int inputType)
	{
		super(frame, title, true);

		try
		{
            _inputType = inputType;
            daiLabel_entryText.setText(promptText);
			jbInit();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	//Initialize Dialog Controls
	void jbInit() throws Exception
	{
		panel_root.setLayout(boxLayout21);
		button_ok.setText("OK");
        button_ok.setLength(80);
        button_ok.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_ok_actionPerformed(e);
            }
        });
		button_cancel.setText("Cancel");
        button_cancel.setLength(80);
		button_cancel.addActionListener(new java.awt.event.ActionListener(){
											   public void actionPerformed(ActionEvent e){
											   buttonControl_cancel_actionPerformed(e);}});
		buttonPanel.setLayout(flowLayout1);
		boxLayout21.setAxis(BoxLayout.Y_AXIS);
        buttonPanel.setMaximumSize(new Dimension(32767, 34));
        buttonPanel.setMinimumSize(new Dimension(115, 34));
        buttonPanel.setPreferredSize(new Dimension(115, 34));
        entryPanel.setLayout(borderLayout1);
        daiLabel_entryText.setHorizontalAlignment(SwingConstants.CENTER);
        entryPanel.setMaximumSize(new Dimension(2147483647, 43));
        entryPanel.setMinimumSize(new Dimension(50, 43));
        entryPanel.setPreferredSize(new Dimension(50, 43));
        panel_root.setMaximumSize(new Dimension(2147483647, 77));
        buttonPanel.add(button_ok, null);
		buttonPanel.add(button_cancel, null);

        panel_root.setMinimumSize(new Dimension(325, 77));
        panel_root.setPreferredSize(new Dimension(325, 77));

        panel_root.add(entryPanel, null);

        intField_entry.setPreferredSize(new Dimension(50, 27));
        textField_entry.setMaximumSize(new Dimension(50, 27));
        currencyField_entry.setMinimumSize(new Dimension(50, 27));

        //Add the right entry field for the requested datatype
        if (_inputType == TEXT_INPUT_TYPE) {
            entryPanel.add(textField_entry, BorderLayout.CENTER);
        } else if (_inputType == INT_INPUT_TYPE) {
            entryPanel.add(intField_entry, BorderLayout.CENTER);
        } else if (_inputType == CURRENCY_INPUT_TYPE) {
            entryPanel.add(currencyField_entry, BorderLayout.CENTER);
        }

        entryPanel.add(daiLabel_entryText, BorderLayout.NORTH);
		panel_root.add(buttonPanel, null);


        this.getContentPane().add(panel_root);

        this.centerFrame();
        this.show();
	}

    public String getValueEntered()
    {
        String ret = null;

        //Return the right value for the requested datatype
        if (_inputType == TEXT_INPUT_TYPE) {
            ret = textField_entry.getText();
        } else if (_inputType == INT_INPUT_TYPE) {
            ret = intField_entry.getText();
        } else if (_inputType == CURRENCY_INPUT_TYPE) {
            ret = currencyField_entry.getText();
        }
        return ret;
    }

    public int getUserButtonChoice()
    {
        return _userButtonChoice;
    }

	void buttonControl_cancel_actionPerformed(ActionEvent e)
	{
        _userButtonChoice = CANCEL_OPTION;
        this.setVisible(false);
	}

    private void button_ok_actionPerformed(ActionEvent e) {
        if (getValueEntered() == null) {
            if (_inputType == TEXT_INPUT_TYPE) {
                textField_entry.requestFocus();
            } else if (_inputType == INT_INPUT_TYPE) {
                intField_entry.requestFocus();
            } else if (_inputType == CURRENCY_INPUT_TYPE) {
                currencyField_entry.requestFocus();
            }
            return;
        }

        _userButtonChoice = OK_OPTION;
        this.setVisible(false);
    }

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

	//Main method
	public static void main(String[] args) {
		try
		{
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
			UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
		} catch (Exception e)
		{
		}
		new UserInputDialog(null, "hello", "Yo, man put in some stuff", UserInputDialog.INT_INPUT_TYPE);
	}
}
