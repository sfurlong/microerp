
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.carrier;


import java.awt.event.FocusEvent;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.carrierObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.daiAddressPanel;
import daiBeans.daiComboBox;
import daiBeans.daiLabel;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;


public class carrierMainPanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();
	daiTextField fieldControl_name = new daiTextField();
    daiTextArea textAreaControl_note = new daiTextArea();
	daiLabel daiLabel_id = new daiLabel("Id:");
	daiLabel daiLabel_name = new daiLabel("Name:");
    JCheckBox jCheckBox_billToIsShipTo = new JCheckBox();
    daiTextField daiTextField_carrierAcct = new daiTextField();
    daiLabel daiLabel_carrierAcct = new daiLabel("Carrier Acct:");
    daiLabel daiLabel_note = new daiBeans.daiLabel("Note:");
    daiLabel daiLabel_dispPriority = new daiBeans.daiLabel("Popup Display Priority:");
    daiComboBox comboBox_priority = new daiComboBox();

    daiAddressPanel addrPanel = new daiAddressPanel("Carrier Address:");

	XYLayout xYLayout1 = new XYLayout();
	XYLayout xYLayout3 = new XYLayout();

	SessionMetaData sessionMeta;

    daiUserIdDateCreatedPanel userIdDatePanel = new daiUserIdDateCreatedPanel();
    XYLayout xYLayout4 = new XYLayout();
    XYLayout xYLayout5 = new XYLayout();

	public carrierMainPanel(JFrame container, daiFrame parentFrame, carrierObj obj)
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
        comboBox_priority.addItems(new String[]{"1","2","3","4","5","6","7","8","9","10"});

		setLayout(xYLayout2);
		xYLayout2.setHeight(339);
		xYLayout2.setWidth(578);
		xYLayout3.setHeight(468);
		xYLayout3.setWidth(685);

        this.setBackground(daiColors.PanelColor);
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
        {

            public void focusLost(FocusEvent e)
            {
                ID_TEXT_FIELD_focusLost(e);
            }
        });
        this.add(userIdDatePanel, new XYConstraints(370, 3, -1, -1));
        this.add(daiLabel_note, new XYConstraints(67, 269, -1, -1));
        this.add(daiLabel_id, new XYConstraints(81, 6, -1, -1));
        this.add(daiLabel_name, new XYConstraints(62, 30, -1, -1));
        this.add(daiLabel_carrierAcct, new XYConstraints(30, 56, -1, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(99, 7, 155, -1));
        this.add(fieldControl_name, new XYConstraints(99, 31, 256, -1));
        this.add(daiTextField_carrierAcct, new XYConstraints(99, 55, 155, -1));
        this.add(comboBox_priority, new XYConstraints(382, 55, 43, -1));
        this.add(addrPanel, new XYConstraints(32, 86, 442, 179));
        this.add(textAreaControl_note, new XYConstraints(99, 271, 341, 56));
        this.add(daiLabel_dispPriority, new XYConstraints(268, 56, 113, -1));
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        carrierObj obj = new carrierObj();
        carrierObj tempObj = (carrierObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

        return obj;
    }

    public String getCarrierId() {
        return ID_TEXT_FIELD.getText();
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
		carrierObj obj = (carrierObj)bobj;

		ID_TEXT_FIELD.setText(obj.get_id());
		fieldControl_name.setText(obj.get_name());
        daiTextField_carrierAcct.setText(obj.get_carrier_acct_num());
        userIdDatePanel.setDateCreated(obj.get_date_created());
        userIdDatePanel.setUserId(obj.get_created_by());
		addrPanel.setAddr1(obj.get_addr1());
		addrPanel.setAddr2(obj.get_addr2());
		addrPanel.setAddr3(obj.get_addr3());
		addrPanel.setAttn(obj.get_attn());
		addrPanel.setCity(obj.get_city());
		addrPanel.setStateCode(obj.get_state_code());
		addrPanel.setZip(obj.get_zip());
        addrPanel.setCountryCode(obj.get_country_code());
        textAreaControl_note.setText(obj.get_note());
        comboBox_priority.setText(obj.get_priority());
		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		carrierObj obj = (carrierObj)BUSINESS_OBJ;

		obj.set_id(ID_TEXT_FIELD.getText());
		obj.set_name(fieldControl_name.getText());
		obj.set_addr1(addrPanel.getAddr1());
		obj.set_addr2(addrPanel.getAddr2());
		obj.set_addr3(addrPanel.getAddr3());
		obj.set_attn(addrPanel.getAttn());
		obj.set_city(addrPanel.getCity());
		obj.set_state_code(addrPanel.getStateCode());
		obj.set_zip(addrPanel.getZip());
        obj.set_country_code(addrPanel.getCountryCode());
        obj.set_note(textAreaControl_note.getText());
        obj.set_created_by(userIdDatePanel.getUserId());
        obj.set_date_created(userIdDatePanel.getDateCreated());
        obj.set_carrier_acct_num(daiTextField_carrierAcct.getText());
        obj.set_priority(comboBox_priority.getText());
		BUSINESS_OBJ = obj;
	}

    void ID_TEXT_FIELD_focusLost(FocusEvent e)
    {
        if (e.isTemporary()) return;

		String id = ID_TEXT_FIELD.getText();
		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
	    	CONTAINER_FRAME.callBackInsertNewId(id);
    		//Disable the Trans ID text field.
	   		ID_TEXT_FIELD.setDisabled(true);

            fieldControl_name.requestFocus();
		}
    }
}

