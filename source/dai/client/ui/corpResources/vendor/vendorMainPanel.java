
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.vendor;


import java.awt.event.FocusEvent;

import javax.swing.JFrame;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.accountObj;
import dai.shared.businessObjs.vendorObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;


public class vendorMainPanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();
	XYLayout xYLayout1 = new XYLayout();
	XYLayout xYLayout3 = new XYLayout();
	SessionMetaData sessionMeta;

    //Label Declarations.
	daiLabel daiLabel_id = new daiLabel("Id:");
	daiLabel daiLabel_name = new daiLabel("Name:");
    daiLabel daiLabel_aka = new daiLabel("A.K.A.");
    daiLabel daiLabel_ssNum = new daiLabel("SS#:");
    daiLabel daiLabel_vendorType = new daiLabel("Vendor Type:");
    daiLabel daiLabel_paymentDays = new daiLabel("Payment Days:");
    daiLabel daiLabel_discount = new daiLabel("Discount %:");
    daiLabel daiLabel_primaryAcct = new daiLabel("Primary Acct:");
    daiLabel daiLabel_note = new daiLabel("Note:");
    daiLabel daiLabel_acctRefNum = new daiLabel("Account/Ref#:");
    //Entry Field delcarations
    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();
	daiTextField fieldControl_name = new daiTextField();
    daiTextField daiTextField_aka = new daiTextField();
	daiTextField fieldControl_ssNum = new daiTextField();
	daiTextField fieldControl_vendorType = new daiTextField();
	daiNumField fieldControl_paymentDays = new daiNumField();
	daiNumField fieldControl_discount = new daiNumField();
	daiTextField fieldControl_primaryAcct = new daiTextField();
    daiTextArea textAreaControl_note = new daiTextArea();
    daiTextField daiTextField_primAcctName = new daiTextField();
    daiLabel daiLabel_primAcctNote = new daiLabel();
    daiTextField daiTextField_acctRefNum = new daiTextField();

	public vendorMainPanel(JFrame container, daiFrame parentFrame, vendorObj obj)
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
        sessionMeta = sessionMeta.getInstance();

		setLayout(xYLayout2);
		xYLayout2.setHeight(361);
		xYLayout2.setWidth(578);
		xYLayout3.setHeight(468);
		xYLayout3.setWidth(685);
        this.setBackground(daiColors.PanelColor);
        daiLabel_primaryAcct.setHREFstyle(true);
        daiLabel_primaryAcct.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_primaryAcct_daiAction(e);
            }
        });

        daiTextField_primAcctName.setDisabled(true);

        daiLabel_primAcctNote.setText("* Used as the default expense acct in the Receive Expese Bill task.");
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
        {

            public void focusLost(FocusEvent e)
            {
                ID_TEXT_FIELD_focusLost(e);
            }
        });
        this.add(userIdDateCreatedPanel, new XYConstraints(391, 5, -1, -1));
        this.add(daiLabel_id, new XYConstraints(74, 44, -1, -1));
        this.add(daiLabel_name, new XYConstraints(55, 68, -1, -1));
        this.add(daiLabel_aka, new XYConstraints(50, 94, 35, -1));
        this.add(daiLabel_acctRefNum, new XYConstraints(12, 131, -1, -1));
        this.add(daiLabel_note, new XYConstraints(60, 266, -1, -1));
        this.add(daiLabel_primAcctNote, new XYConstraints(91, 235, -1, -1));
        this.add(daiLabel_vendorType, new XYConstraints(17, 158, -1, -1));
        this.add(daiLabel_ssNum, new XYConstraints(242, 158, -1, -1));
        this.add(daiLabel_paymentDays, new XYConstraints(11, 186, -1, -1));
        this.add(daiLabel_primaryAcct, new XYConstraints(18, 216, -1, -1));
        this.add(daiLabel_discount, new XYConstraints(208, 186, -1, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(92, 45, 163, -1));
        this.add(fieldControl_name, new XYConstraints(92, 69, 280, -1));
        this.add(daiTextField_aka, new XYConstraints(92, 94, 280, -1));
        this.add(daiTextField_acctRefNum, new XYConstraints(89, 129, 180, -1));
        this.add(fieldControl_vendorType, new XYConstraints(90, 155, -1, -1));
        this.add(fieldControl_ssNum, new XYConstraints(273, 155, -1, -1));
        this.add(fieldControl_paymentDays, new XYConstraints(90, 185, -1, -1));
        this.add(fieldControl_discount, new XYConstraints(274, 185, -1, -1));
        this.add(fieldControl_primaryAcct, new XYConstraints(90, 214, -1, -1));
        this.add(daiTextField_primAcctName, new XYConstraints(197, 214, 250, -1));
        this.add(textAreaControl_note, new XYConstraints(92, 265, 370, 67));
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        vendorObj obj = new vendorObj();
        vendorObj tempObj = (vendorObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

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

	//Override base class query because we want to do some extra work.
	public int persistPanelData()
    {
  		//Call the base class insert then do our extended logic.
		int ret = super.persistPanelData();

        //The extended logic.
        ID_TEXT_FIELD.setDisabled(true);

        return ret;
    }


	protected void update_UI(BusinessObject bobj)
	{
		vendorObj obj = (vendorObj)bobj;

		userIdDateCreatedPanel.setUserId(obj.get_created_by());
		userIdDateCreatedPanel.setDateCreated(obj.get_date_created());
		ID_TEXT_FIELD.setText(obj.get_id());
		fieldControl_name.setText(obj.get_name());
		fieldControl_ssNum.setText(obj.get_ss_num());
		fieldControl_vendorType.setText(obj.get_vendor_type());
		fieldControl_paymentDays.setText(obj.get_payment_days());
		fieldControl_discount.setText(obj.get_discount_percent());
		fieldControl_primaryAcct.setText(obj.get_primary_acct());
        daiTextField_primAcctName.setText(obj.get_primary_acct_name());
        daiTextField_acctRefNum.setText(obj.get_our_acct_no_with_vendor());
        textAreaControl_note.setText(obj.get_note1());
        daiTextField_aka.setText(obj.get_also_known_as());

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		vendorObj obj = (vendorObj)BUSINESS_OBJ;

		obj.set_id(ID_TEXT_FIELD.getText());
		obj.set_name(fieldControl_name.getText());
		obj.set_ss_num(fieldControl_ssNum.getText());
		obj.set_vendor_type(fieldControl_vendorType.getText());
		obj.set_payment_days(fieldControl_paymentDays.getText());
		obj.set_discount_percent(fieldControl_discount.getText());
		obj.set_primary_acct(fieldControl_primaryAcct.getText());
        obj.set_primary_acct_name(daiTextField_primAcctName.getText());
        obj.set_note1(textAreaControl_note.getText());
        obj.set_our_acct_no_with_vendor(daiTextField_acctRefNum.getText());
        obj.set_also_known_as(daiTextField_aka.getText());

		BUSINESS_OBJ = obj;
	}

    void ID_TEXT_FIELD_focusLost(FocusEvent e)
    {
        if (e.isTemporary()) return;

		String id = ID_TEXT_FIELD.getText();

		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
			//Disable the Trans ID text field.
			ID_TEXT_FIELD.setDisabled(true);

			CONTAINER_FRAME.callBackInsertNewId(id);

            fieldControl_name.requestFocus();
		}
    }

    void daiLabel_primaryAcct_daiAction(daiActionEvent e)
    {
   		accountObj tempObj = new accountObj();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_primaryAcct.setText(chosenObj.get_id());
            daiTextField_primAcctName.setText(chosenObj.get_description());
        }
        chooser.dispose();
    }
}

