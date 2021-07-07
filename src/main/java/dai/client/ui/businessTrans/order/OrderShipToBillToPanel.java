
//Title:        Item Package
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Iten Entry/Update

package dai.client.ui.businessTrans.order;

import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.VerticalFlowLayout;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.cust_orderObj;
import daiBeans.daiAddressPanel;
import daiBeans.daiCheckBox;
import daiBeans.daiLabel;
import daiBeans.daiTextField;

public class OrderShipToBillToPanel extends daiHeaderSubPanel
{
    //Entry Field Declarations
    daiAddressPanel billToAddrPanel = new daiAddressPanel("Bill To:");
    daiAddressPanel shipToAddrPanel = new daiAddressPanel("Ship To:");
    //Label Declarations

    //Other Declarations
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    GroupBox groupBox_contacts = new GroupBox();
    daiTextField daiTextField_to = new daiTextField();
    daiTextField daiTextField_phone = new daiTextField();
    daiTextField daiTextField_fax = new daiTextField();
    XYLayout xYLayout1 = new XYLayout();
    daiTextField daiTextField_from = new daiTextField();
    daiLabel daiLabel_to = new daiLabel();
    daiLabel daiLabel_phone = new daiLabel();
    daiLabel daiLabel_fax = new daiLabel();
    daiLabel daiLabel_from = new daiLabel();
    daiCheckBox checkBox_isDropShip
            = new daiCheckBox("Ship To is a Third Party Drop Ship?");
    public OrderShipToBillToPanel(JFrame container, daiFrame parentFrame, cust_orderObj obj)
    {
		super(container, parentFrame, obj);

        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        this.setLayout(verticalFlowLayout1);

        this.setBackground(daiColors.PanelColor);
        groupBox_contacts.setLabel("Contact Info:");
        groupBox_contacts.setLayout(xYLayout1);
        daiLabel_to.setText("To:");
        daiLabel_phone.setText("Phone:");
        daiLabel_fax.setText("Fax:");
        daiLabel_from.setText("From:");
        groupBox_contacts.add(daiTextField_to, new XYConstraints(13, 5, 200, -1));
        groupBox_contacts.add(daiTextField_from, new XYConstraints(295, 5, 249, -1));
        groupBox_contacts.add(daiTextField_phone, new XYConstraints(30, 32, 160, -1));
        groupBox_contacts.add(daiTextField_fax, new XYConstraints(30, 57, 160, -1));
        groupBox_contacts.add(daiLabel_from, new XYConstraints(266, 7, -1, -1));
        groupBox_contacts.add(daiLabel_to, new XYConstraints(-5, 7, -1, -1));
        groupBox_contacts.add(daiLabel_phone, new XYConstraints(-5, 32, -1, -1));
        groupBox_contacts.add(daiLabel_fax, new XYConstraints(-5, 57, -1, -1));
        this.add(groupBox_contacts, null);
        this.add(checkBox_isDropShip);
        this.add(shipToAddrPanel, null);
        this.add(billToAddrPanel, null);
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        cust_orderObj tempObj = (cust_orderObj)obj;

		tempObj.set_shipto_addr1(shipToAddrPanel.getAddr1());
		tempObj.set_shipto_addr2(shipToAddrPanel.getAddr2());
		tempObj.set_shipto_addr3(shipToAddrPanel.getAddr3());
		tempObj.set_shipto_attn(shipToAddrPanel.getAttn());
		tempObj.set_shipto_city(shipToAddrPanel.getCity());
		tempObj.set_shipto_state_code(shipToAddrPanel.getStateCode());
		tempObj.set_shipto_zip(shipToAddrPanel.getZip());
		tempObj.set_shipto_country_code(shipToAddrPanel.getCountryCode());
		tempObj.set_shipto_country_name("");
		tempObj.set_billto_addr1(billToAddrPanel.getAddr1());
		tempObj.set_billto_addr2(billToAddrPanel.getAddr2());
		tempObj.set_billto_addr3(billToAddrPanel.getAddr3());
		tempObj.set_billto_attn(billToAddrPanel.getAttn());
		tempObj.set_billto_city(billToAddrPanel.getCity());
		tempObj.set_billto_state_code(billToAddrPanel.getStateCode());
		tempObj.set_billto_zip(billToAddrPanel.getZip());
		tempObj.set_billto_country_code(billToAddrPanel.getCountryCode());
		tempObj.set_billto_country_name("");
        tempObj.set_cust_contact(daiTextField_to.getText());
        tempObj.set_customer_phone(daiTextField_phone.getText());
        tempObj.set_customer_fax(daiTextField_fax.getText());
        tempObj.set_our_contact(daiTextField_from.getText());
        tempObj.set_shipto_is_dropship(checkBox_isDropShip.getValue());
    }

	public void update_UI(BusinessObject obj)
    {
		cust_orderObj tempObj = (cust_orderObj)obj;

		shipToAddrPanel.setAddr1(tempObj.get_shipto_addr1());
		shipToAddrPanel.setAddr2(tempObj.get_shipto_addr2());
		shipToAddrPanel.setAddr3(tempObj.get_shipto_addr3());
		shipToAddrPanel.setAttn(tempObj.get_shipto_attn());
		shipToAddrPanel.setCity(tempObj.get_shipto_city());
		shipToAddrPanel.setStateCode(tempObj.get_shipto_state_code());
		shipToAddrPanel.setZip(tempObj.get_shipto_zip());
		shipToAddrPanel.setCountryCode(tempObj.get_shipto_country_code());
		//shipToAddrPanel.set(tempObj.get_shipto_country_name());

		billToAddrPanel.setAddr1(tempObj.get_billto_addr1());
		billToAddrPanel.setAddr2(tempObj.get_billto_addr2());
		billToAddrPanel.setAddr3(tempObj.get_billto_addr3());
		billToAddrPanel.setAttn(tempObj.get_billto_attn());
		billToAddrPanel.setCity(tempObj.get_billto_city());
		billToAddrPanel.setStateCode(tempObj.get_billto_state_code());
		billToAddrPanel.setZip(tempObj.get_billto_zip());
		billToAddrPanel.setCountryCode(tempObj.get_billto_country_code());
		//billToAddrPanel.set(tempObj.get_billto_country_name());

        daiTextField_to.setText(tempObj.get_cust_contact());
        daiTextField_phone.setText(tempObj.get_customer_phone());
        daiTextField_fax.setText(tempObj.get_customer_fax());
        daiTextField_from.setText(tempObj.get_our_contact());
        checkBox_isDropShip.setValue(tempObj.get_shipto_is_dropship());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
