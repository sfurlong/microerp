
//Title:        Item Package
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Iten Entry/Update

package dai.client.ui.businessTrans.quote;

import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.VerticalFlowLayout;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.quoteObj;
import daiBeans.daiAddressPanel;
import daiBeans.daiLabel;
import daiBeans.daiMaskField;
import daiBeans.daiTextField;

public class QuoteShipToBillToPanel extends daiHeaderSubPanel
{
    //Entry Field Declarations
    daiAddressPanel shipToAddrPanel = new daiAddressPanel("Ship To:");

    //Other Declarations
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    GroupBox groupBox_ourContact = new GroupBox();
    daiTextField daiTextField_custName = new daiTextField();
    XYLayout xYLayout1 = new XYLayout();
    daiTextField daiTextField_name = new daiTextField();
    daiLabel daiLabel_custName = new daiLabel();
    daiLabel daiLabel_name = new daiLabel();
    GroupBox groupBox_custContact = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();
    daiTextField daiTextField_email = new daiTextField();
    daiLabel daiLabel_email = new daiLabel();
    daiMaskField daiTextField_custPhone = new daiMaskField("(###) ###-#### Ext. ####");
    daiLabel daiLabel_custPhone = new daiLabel();
    daiMaskField daiTextField_custFax = new daiMaskField("(###) ###-####");
    daiLabel daiLabel_fax = new daiLabel();

    public QuoteShipToBillToPanel(JFrame container, daiFrame parentFrame, quoteObj obj)
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

        shipToAddrPanel.hideAttnField();
        this.setBackground(daiColors.PanelColor);
        groupBox_ourContact.setLabel("Our Contact Info:");
        groupBox_ourContact.setLayout(xYLayout1);
        daiLabel_custName.setText("Name:");
        daiLabel_name.setText("Name:");
        groupBox_custContact.setLabel("Customer Contact Info:");
        groupBox_custContact.setLayout(xYLayout2);
        daiLabel_email.setText("Email:");
        daiLabel_custPhone.setText("Phone:");
        daiLabel_fax.setText("Fax:");
        groupBox_ourContact.add(daiLabel_name, new XYConstraints(1, 5, -1, -1));
        groupBox_ourContact.add(daiLabel_email, new XYConstraints(4, 27, 27, -1));
        groupBox_ourContact.add(daiTextField_name, new XYConstraints(51, 3, 271, -1));
        groupBox_ourContact.add(daiTextField_email, new XYConstraints(51, 25, 271, -1));
        this.add(groupBox_custContact, null);
        groupBox_custContact.add(daiLabel_custName, new XYConstraints(0, 1, -1, -1));
        groupBox_custContact.add(daiLabel_custPhone, new XYConstraints(-8, 24, 38, -1));
        groupBox_custContact.add(daiLabel_fax, new XYConstraints(0, 49, 30, -1));
        groupBox_custContact.add(daiTextField_custName, new XYConstraints(54, 2, 270, -1));
        groupBox_custContact.add(daiTextField_custPhone, new XYConstraints(54, 25, 270, -1));
        groupBox_custContact.add(daiTextField_custFax, new XYConstraints(54, 50, 270, -1));
        this.add(groupBox_ourContact, null);
        this.add(shipToAddrPanel, null);
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        quoteObj tempObj = (quoteObj)obj;

		tempObj.set_shipto_addr1(shipToAddrPanel.getAddr1());
		tempObj.set_shipto_addr2(shipToAddrPanel.getAddr2());
		tempObj.set_shipto_addr3(shipToAddrPanel.getAddr3());
		tempObj.set_shipto_attn(shipToAddrPanel.getAttn());
		tempObj.set_shipto_city(shipToAddrPanel.getCity());
		tempObj.set_shipto_state_code(shipToAddrPanel.getStateCode());
		tempObj.set_shipto_zip(shipToAddrPanel.getZip());
		tempObj.set_shipto_country_code(shipToAddrPanel.getCountryCode());
		tempObj.set_shipto_country_name("");
        tempObj.set_cust_contact(daiTextField_custName.getText());
        tempObj.set_cust_fax(daiTextField_custFax.getText());
        tempObj.set_cust_phone(daiTextField_custPhone.getText());
        tempObj.set_our_contact(daiTextField_name.getText());
        tempObj.set_our_email(daiTextField_email.getText());
    }

	public void update_UI(BusinessObject obj)
    {
		quoteObj tempObj = (quoteObj)obj;

		shipToAddrPanel.setAddr1(tempObj.get_shipto_addr1());
		shipToAddrPanel.setAddr2(tempObj.get_shipto_addr2());
		shipToAddrPanel.setAddr3(tempObj.get_shipto_addr3());
		shipToAddrPanel.setAttn(tempObj.get_shipto_attn());
		shipToAddrPanel.setCity(tempObj.get_shipto_city());
		shipToAddrPanel.setStateCode(tempObj.get_shipto_state_code());
		shipToAddrPanel.setZip(tempObj.get_shipto_zip());
		shipToAddrPanel.setCountryCode(tempObj.get_shipto_country_code());
		//shipToAddrPanel.set(tempObj.get_shipto_country_name());

        daiTextField_custName.setText(tempObj.get_cust_contact());
        daiTextField_custPhone.setText(tempObj.get_cust_phone());
        daiTextField_custFax.setText(tempObj.get_cust_fax());
        daiTextField_name.setText(tempObj.get_our_contact());
        daiTextField_email.setText(tempObj.get_our_email());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
