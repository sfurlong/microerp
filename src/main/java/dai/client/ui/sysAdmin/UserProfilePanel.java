
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.sysAdmin;


import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.user_profileObj;
import dai.shared.cmnSvcs.JCrypt;
import daiBeans.daiCheckBox;
import daiBeans.daiLabel;
import daiBeans.daiTextField;

public class UserProfilePanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();
	daiTextField fieldControl_name = new daiTextField();

	daiLabel daiLabel_id = new daiLabel("Id:");
	daiLabel daiLabel_name = new daiLabel("Description:");
    GroupBox groupBox_pwd = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();
    daiLabel daiLabel_pwd = new daiLabel();
    daiLabel daiLabel_pwdConfirm = new daiLabel();
    JPasswordField passwordField_pwd = new JPasswordField();
    JPasswordField passwordField_pwdConfirm = new JPasswordField();
    JButton button_setPwd = new JButton();
    daiLabel daiLabel_title = new daiLabel();
    daiTextField daiTextField_title = new daiTextField();
    daiTextField daiTextField_email = new daiTextField();
    daiLabel daiLabel_email = new daiLabel();
    daiCheckBox checkBox_isAdmin = new daiCheckBox("Is System Admin?");

	public UserProfilePanel(JFrame container, daiFrame parentFrame, user_profileObj obj)
	{
		super(container, parentFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	void jbInit() throws Exception
	{
		setLayout(xYLayout2);
		xYLayout2.setHeight(313);
		xYLayout2.setWidth(578);

        daiLabel_name.setText("Name:");

        this.setBackground(daiColors.PanelColor);
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(FocusEvent e) {
                ID_TEXT_FIELD_focusLost(e);
            }
        });
        groupBox_pwd.setLabel("Reset User Password");
        groupBox_pwd.setLayout(xYLayout1);
        daiLabel_pwd.setText("New Password:");
        daiLabel_pwdConfirm.setText("Password Confirm:");
        button_setPwd.setText("Set Password");
        button_setPwd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_setPwd_actionPerformed(e);
            }
        });

        passwordField_pwd.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e)
            {
                pwdField_keyTyped();
            }
            public void keyReleased(KeyEvent e)
            {
                //_isModified = true;
                //fireDaiDataModified(new daiDataModifiedEvent(this));
            }
            public void keyPressed(KeyEvent e)
            {
                //_isModified = true;
                //fireDaiDataModified(new daiDataModifiedEvent(this));
            }
        });

        daiLabel_title.setText("Title:");
        daiLabel_email.setText("Email:");
        this.add(daiLabel_id, new XYConstraints(82, 22, -1, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(97, 23, 123, -1));
        this.add(checkBox_isAdmin, new XYConstraints(275, 20, -1, -1));
        this.add(fieldControl_name, new XYConstraints(97, 47, 288, -1));
        this.add(daiLabel_name, new XYConstraints(63, 49, -1, -1));
        this.add(groupBox_pwd, new XYConstraints(65, 225, 415, 72));
        groupBox_pwd.add(passwordField_pwd, new XYConstraints(114, 0, 131, -1));
        groupBox_pwd.add(passwordField_pwdConfirm, new XYConstraints(115, 24, 131, -1));
        groupBox_pwd.add(daiLabel_pwdConfirm, new XYConstraints(19, 26, -1, -1));
        groupBox_pwd.add(daiLabel_pwd, new XYConstraints(32, 4, -1, -1));
        groupBox_pwd.add(button_setPwd, new XYConstraints(259, 0, -1, -1));
        this.add(daiLabel_title, new XYConstraints(63, 78, 30, -1));
        this.add(daiTextField_title, new XYConstraints(97, 74, 288, -1));
        this.add(daiTextField_email, new XYConstraints(97, 100, 288, -1));
        this.add(daiLabel_email, new XYConstraints(63, 104, 30, -1));
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        user_profileObj obj = new user_profileObj();
        user_profileObj tempObj = (user_profileObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
        obj.set_locality(user_profileObj.getObjLocality());

        return obj;
    }

	public int refresh()
	{
		//Call the base class method first.
		super.refresh();

		//Enable the ID text field.
		ID_TEXT_FIELD.setDisabled(false);

		return 0;
	}

	public int query(String id)
	{
		//Call the base class query then do our extended logic.
		super.query(id);

		//Disable the Trans ID text field.
		ID_TEXT_FIELD.setDisabled(true);
		return 0;
	}

	protected void update_UI(BusinessObject bobj)
	{
		user_profileObj obj = (user_profileObj)bobj;

		ID_TEXT_FIELD.setText(obj.get_id());
		fieldControl_name.setText(obj.get_user_name());
        daiTextField_title.setText(obj.get_user_title());
        daiTextField_email.setText(obj.get_user_email());
        checkBox_isAdmin.setValue(obj.get_is_administrator());
        passwordField_pwd.setText(null);
        passwordField_pwdConfirm.setText(null);

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		user_profileObj obj = (user_profileObj)BUSINESS_OBJ;

		obj.set_id(ID_TEXT_FIELD.getText());
		obj.set_user_name(fieldControl_name.getText());
        obj.set_user_title(daiTextField_title.getText());
        obj.set_user_email(daiTextField_email.getText());
        obj.set_is_administrator(checkBox_isAdmin.getValue());

		BUSINESS_OBJ = obj;
	}

    private void ID_TEXT_FIELD_focusLost(FocusEvent e) {
        if (e.isTemporary()) return;

		String id = ID_TEXT_FIELD.getText();
		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
	    	CONTAINER_FRAME.callBackInsertNewId(id);
    		//Disable the Trans ID text field.
	   		ID_TEXT_FIELD.setDisabled(true);
		}
    }

    private void button_setPwd_actionPerformed(ActionEvent e) {
        String pwd = new String(passwordField_pwd.getPassword());
        String pwdConfirm = new String(passwordField_pwdConfirm.getPassword());

        if (pwd.equals(pwdConfirm)) {
            String encrypedPwd = JCrypt.crypt(pwd);
            //Update the business object
    		user_profileObj obj = (user_profileObj)BUSINESS_OBJ;
	    	obj.set_encrypted_pwd(encrypedPwd);
		    BUSINESS_OBJ = obj;
        } else {
            //the passwords did not confirm.
            //Dispay error message.
            JOptionPane dlg = new JOptionPane();
            dlg.showMessageDialog(this, "Passwords do not match.",
                                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void pwdField_keyTyped() {
        _panelIsDirty = true;
        CONTAINER_FRAME.statusBar.setLeftStatus("Modified");
    }
}

