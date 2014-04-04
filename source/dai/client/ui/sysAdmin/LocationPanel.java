
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.sysAdmin;


import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.locationObj;
import daiBeans.daiAddressPanel;
import daiBeans.daiLabel;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;


public class LocationPanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();
	daiTextField fieldControl_name = new daiTextField();
    daiTextArea textAreaControl_note1 = new daiTextArea();

	daiLabel daiLabel_id = new daiLabel("Id:");
	daiLabel daiLabel_name = new daiLabel("Description:");
    daiLabel daiLabel_note1 = new daiBeans.daiLabel("Note1:");

    JCheckBox jCheckBox_primaryLoc = new JCheckBox();

    daiAddressPanel shipToAddrPanel = new daiAddressPanel("Ship To");
    daiAddressPanel billToAddrPanel = new daiAddressPanel("Bill To");

	public LocationPanel(JFrame container, daiFrame parentFrame, locationObj obj)
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
		xYLayout2.setHeight(568);
		xYLayout2.setWidth(578);

        ID_TEXT_FIELD.addKeyListener(new customerMainPanel_ID_TEXT_FIELD_keyAdapter(this));

        jCheckBox_primaryLoc.setHorizontalTextPosition(SwingConstants.LEFT);
        jCheckBox_primaryLoc.setOpaque(false);
        jCheckBox_primaryLoc.setText("Is Primary Location");
        jCheckBox_primaryLoc.setFont(new java.awt.Font("Dialog", 0, 11));
        daiLabel_name.setText("Name:");
        daiLabel_note1.setText("Note:");

        this.setBackground(daiColors.PanelColor);
        this.add(daiLabel_id, new XYConstraints(82, 22, -1, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(97, 23, 123, -1));
        this.add(fieldControl_name, new XYConstraints(97, 47, 288, -1));
        this.add(daiLabel_name, new XYConstraints(63, 49, -1, -1));
        this.add(jCheckBox_primaryLoc, new XYConstraints(45, 71, -1, -1));
        this.add(shipToAddrPanel, new XYConstraints(44, 97, 467, 183));
        this.add(billToAddrPanel, new XYConstraints(44, 286, 467, 183));
        this.add(textAreaControl_note1, new XYConstraints(44, 474, 440, 67));
        this.add(daiLabel_note1, new XYConstraints(8, 472, -1, -1));
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        locationObj obj = new locationObj();
        locationObj tempObj = (locationObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
        obj.set_locality(locationObj.getObjLocality());

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
		locationObj obj = (locationObj)bobj;

		ID_TEXT_FIELD.setText(obj.get_id());
		fieldControl_name.setText(obj.get_description());
		shipToAddrPanel.setAddr1(obj.get_shipto_addr1());
		shipToAddrPanel.setAddr2(obj.get_shipto_addr2());
		shipToAddrPanel.setAddr3(obj.get_shipto_addr3());
		shipToAddrPanel.setAttn(obj.get_shipto_attn());
		shipToAddrPanel.setCity(obj.get_shipto_city());
		shipToAddrPanel.setStateCode(obj.get_shipto_state_code());
		shipToAddrPanel.setZip(obj.get_shipto_zip());
        shipToAddrPanel.setCountryCode(obj.get_shipto_country_code());

   		billToAddrPanel.setAddr1(obj.get_shipto_addr1());
		billToAddrPanel.setAddr2(obj.get_shipto_addr2());
		billToAddrPanel.setAddr3(obj.get_shipto_addr3());
		billToAddrPanel.setAttn(obj.get_shipto_attn());
		billToAddrPanel.setCity(obj.get_shipto_city());
		billToAddrPanel.setStateCode(obj.get_shipto_state_code());
		billToAddrPanel.setZip(obj.get_shipto_zip());
        billToAddrPanel.setCountryCode(obj.get_shipto_country_code());

        textAreaControl_note1.setText(obj.get_note());
        if (obj.get_is_primary_loc() == null || !obj.get_is_primary_loc().equals("Y")) {
            jCheckBox_primaryLoc.setSelected(false);
        } else {
            jCheckBox_primaryLoc.setSelected(true);
        }

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		locationObj obj = (locationObj)BUSINESS_OBJ;

		obj.set_id(ID_TEXT_FIELD.getText());
		obj.set_description(fieldControl_name.getText());
		obj.set_shipto_addr1(shipToAddrPanel.getAddr1());
		obj.set_shipto_addr2(shipToAddrPanel.getAddr2());
		obj.set_shipto_addr3(shipToAddrPanel.getAddr3());
		obj.set_shipto_attn(shipToAddrPanel.getAttn());
		obj.set_shipto_city(shipToAddrPanel.getCity());
		obj.set_shipto_state_code(shipToAddrPanel.getStateCode());
		obj.set_shipto_zip(shipToAddrPanel.getZip());
        obj.set_shipto_country_code(shipToAddrPanel.getCountryCode());

		obj.set_billto_addr1(billToAddrPanel.getAddr1());
		obj.set_billto_addr2(billToAddrPanel.getAddr2());
		obj.set_billto_addr3(billToAddrPanel.getAddr3());
		obj.set_billto_attn(billToAddrPanel.getAttn());
		obj.set_billto_city(billToAddrPanel.getCity());
		obj.set_billto_state_code(billToAddrPanel.getStateCode());
		obj.set_billto_zip(billToAddrPanel.getZip());
        obj.set_billto_country_code(billToAddrPanel.getCountryCode());

        obj.set_note(textAreaControl_note1.getText());
        if (jCheckBox_primaryLoc.isSelected()) {
            obj.set_is_primary_loc("Y");
        } else {
            obj.set_is_primary_loc("N");
        }

		BUSINESS_OBJ = obj;
	}

    void ID_TEXT_FIELD_keyPressed(KeyEvent e)
    {
		String id = ID_TEXT_FIELD.getText();
		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
	    	CONTAINER_FRAME.callBackInsertNewId(id);
    		//Disable the Trans ID text field.
	   		ID_TEXT_FIELD.setDisabled(true);
		}
    }
}

class customerMainPanel_ID_TEXT_FIELD_keyAdapter extends java.awt.event.KeyAdapter
{
    LocationPanel adaptee;

    customerMainPanel_ID_TEXT_FIELD_keyAdapter(LocationPanel adaptee)
    {
        this.adaptee = adaptee;
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
            adaptee.ID_TEXT_FIELD_keyPressed(e);
        }
    }
}


