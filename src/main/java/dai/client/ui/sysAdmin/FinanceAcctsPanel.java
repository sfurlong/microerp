
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.sysAdmin;


import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.FocusEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.accountObj;
import daiBeans.daiAcctIdNamePanel;
import daiBeans.daiCheckBox;
import daiBeans.daiComboBox;
import daiBeans.daiLabel;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;


public class FinanceAcctsPanel extends daiHeaderPanel
{
	XYLayout    xYLayout1   = new XYLayout();
    JPanel      entryPanel  = new JPanel();

    //Data Entry Field Controls
	daiComboBox fieldControl_type = new daiComboBox();
	daiTextField fieldControl_desc = new daiTextField();
	daiTextField fieldControl_taxCat = new daiTextField();
	daiTextArea fieldControl_note = new daiTextArea();
    daiCheckBox checkBox_isNotActive = new daiCheckBox("Is NOT Active");
    daiCheckBox checkBox_isSubAcct = new daiCheckBox("Is SubAccount");
    daiAcctIdNamePanel subAcctPanel = new daiAcctIdNamePanel();

    //Label Controls
	daiLabel label_id = new daiLabel("Id:");
	daiLabel label_type = new daiLabel("Type:");
	daiLabel label_desc = new daiLabel("Description:");
	daiLabel label_isSubAcct = new daiLabel("Is Sub Account:");
	daiLabel label_taxCat = new daiLabel("Related Tax Form:");
	daiLabel label_note = new daiLabel("Note:");

    XYLayout xYLayout2 = new XYLayout();
    BoxLayout2 boxLayout21 = new BoxLayout2();
    GroupBox groupBox = new GroupBox();
    XYLayout xYLayout3 = new XYLayout();


	public FinanceAcctsPanel(JFrame container, daiFrame parentFrame, accountObj obj)
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
		xYLayout1.setHeight(360);
		xYLayout1.setWidth(619);
        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e) {
                textID_TEXT_FIELD_focusLost(e);
            }
        });


        //Populate combobox.
        fieldControl_type.addItems(new String[] {accountObj.ACCT_TYPE_ACCT_PAY,
                                                accountObj.ACCT_TYPE_ACCT_REC,
                                                accountObj.ACCT_TYPE_BANK,
                                                accountObj.ACCT_TYPE_COGS,
                                                accountObj.ACCT_TYPE_CREDIT_CRD,
                                                accountObj.ACCT_TYPE_EQUITY,
                                                accountObj.ACCT_TYPE_EXPENSE,
                                                accountObj.ACCT_TYPE_ASSET,
                                                accountObj.ACCT_TYPE_INCOME,
                                                accountObj.ACCT_TYPE_LIABILITY,
                                                accountObj.ACCT_TYPE_NON_POST});
        fieldControl_type.setEditable(false);

        //Add Controls to Entry Panel
		this.setLayout(xYLayout1);
        this.setBackground(daiColors.PanelColor);
        this.setMinimumSize(new Dimension(619, 350));
        this.setPreferredSize(new Dimension(619, 350));
        checkBox_isSubAcct.setMargin(new Insets(2, 0, 2, 0));
        checkBox_isSubAcct.setText("Is SubAccount Of:");
        groupBox.setLayout(xYLayout3);
        checkBox_isNotActive.setText("Acct Is NOT Active");
        groupBox.add(checkBox_isSubAcct, new XYConstraints(4, 0, 109, -1));
        groupBox.add(subAcctPanel, new XYConstraints(-8, 23, 370, 24));

        this.add(label_id, new XYConstraints(75, 29, 54, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(139, 28, 164, -1));
        this.add(fieldControl_type, new XYConstraints(139, 53, 164, -1));
        this.add(label_type, new XYConstraints(102, 56, -1, -1));
        this.add(label_desc, new XYConstraints(72, 84, -1, -1));
        this.add(fieldControl_desc, new XYConstraints(139, 82, 384, -1));
        this.add(fieldControl_taxCat, new XYConstraints(139, 108, 164, -1));
        this.add(label_taxCat, new XYConstraints(42, 110, -1, -1));
        this.add(groupBox, new XYConstraints(132, 130, -1, 77));
        this.add(label_note, new XYConstraints(104, 215, -1, -1));
        this.add(fieldControl_note, new XYConstraints(139, 214, 384, 121));
        this.add(checkBox_isNotActive, new XYConstraints(310, 24, -1, -1));

        ID_TEXT_FIELD.requestFocus();
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        accountObj obj = new accountObj();
        accountObj tempObj = (accountObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
        obj.set_locality(accountObj.getObjLocality());

        return obj;
    }

	public int refresh()
	{

		//Call the base class method first.
		super.refresh();

		//Enable the ID text field.
        ID_TEXT_FIELD.setText(null);
		ID_TEXT_FIELD.setDisabled(false);
        ID_TEXT_FIELD.requestFocus();
		return 0;
	}

	public int query(String id)
	{
		//Call the base class query then do our extended logic.
		super.query(id);

		//Disable the Trans ID text field.
		ID_TEXT_FIELD.setDisabled(true);
        ID_TEXT_FIELD.requestFocus();

		return 0;
	}

	protected void update_UI(BusinessObject bobj)
	{
		accountObj obj = (accountObj)bobj;

		ID_TEXT_FIELD.setText(obj.get_id());
		fieldControl_desc.setText(obj.get_description());
		fieldControl_type.setSelectedItem(obj.get_account_type());
		fieldControl_taxCat.setText(obj.get_tax_category());
		fieldControl_note.setText(obj.get_note());
        checkBox_isSubAcct.setValue(obj.get_is_subaccount());
		checkBox_isNotActive.setValue(obj.get_is_not_active());
        subAcctPanel.setAcctId(obj.get_subaccount());

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		accountObj obj = (accountObj)BUSINESS_OBJ;

		obj.set_id(ID_TEXT_FIELD.getText());
		obj.set_description(fieldControl_desc.getText());
		obj.set_account_type((String)fieldControl_type.getSelectedItem());
		obj.set_is_subaccount(checkBox_isSubAcct.getValue());
		obj.set_tax_category(fieldControl_taxCat.getText());
		obj.set_is_not_active(checkBox_isNotActive.getValue());
		obj.set_note(fieldControl_note.getText());
        obj.set_subaccount(subAcctPanel.getAcctId());

		BUSINESS_OBJ = obj;
	}

    private void textID_TEXT_FIELD_focusLost(FocusEvent e)
    {
        if (!e.isTemporary()) {
    		String id = ID_TEXT_FIELD.getText();
	    	if (id != null && !ID_TEXT_FIELD.isDisabled())
		    {
	    	    CONTAINER_FRAME.callBackInsertNewId(id);
        		//Disable the Trans ID text field.
	       		ID_TEXT_FIELD.setDisabled(true);
		    }
        }
    }
}
