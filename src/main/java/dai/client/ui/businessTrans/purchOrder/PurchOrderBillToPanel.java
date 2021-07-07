
//Title:        Item Package
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Iten Entry/Update

package dai.client.ui.businessTrans.purchOrder;

import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.VerticalFlowLayout;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.purch_orderObj;
import daiBeans.daiAddressPanel;
import daiBeans.daiLabel;
import daiBeans.daiTextField;

public class PurchOrderBillToPanel extends daiHeaderSubPanel
{
    daiAddressPanel addrPanel = new daiAddressPanel("Bill To Address:");
    GroupBox groupBox_contacts = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();
    daiLabel daiLabel_contacts = new daiLabel();
    daiTextField daiTextField_contact = new daiTextField();
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

    public PurchOrderBillToPanel(JFrame container, daiFrame parentFrame, purch_orderObj obj)
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
        groupBox_contacts.setLabel("Our Contact Info");
        groupBox_contacts.setLayout(xYLayout2);
        daiLabel_contacts.setText("Our Contact:");
        daiTextField_contact.setText("");
        this.add(groupBox_contacts, null);
        groupBox_contacts.add(daiLabel_contacts, new XYConstraints(-2, 4, -1, -1));
        groupBox_contacts.add(daiTextField_contact, new XYConstraints(60, 2, 322, -1));
        this.add(addrPanel, null);
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        purch_orderObj tempObj = (purch_orderObj)obj;

		tempObj.set_billto_addr1(addrPanel.getAddr1());
		tempObj.set_billto_addr2(addrPanel.getAddr2());
		tempObj.set_billto_addr3(addrPanel.getAddr3());
		tempObj.set_billto_attn(addrPanel.getAttn());
		tempObj.set_billto_city(addrPanel.getCity());
		tempObj.set_billto_state_code(addrPanel.getStateCode());
		tempObj.set_billto_zip(addrPanel.getZip());
		tempObj.set_billto_country_code(addrPanel.getCountryCode());
		tempObj.set_billto_country_name(addrPanel.getCountryName());
        tempObj.set_our_contact(daiTextField_contact.getText());
    }

	public void update_UI(BusinessObject obj)
    {
		purch_orderObj tempObj = (purch_orderObj)obj;

		addrPanel.setAddr1(tempObj.get_billto_addr1());
		addrPanel.setAddr2(tempObj.get_billto_addr2());
		addrPanel.setAddr3(tempObj.get_billto_addr3());
		addrPanel.setAttn(tempObj.get_billto_attn());
		addrPanel.setCity(tempObj.get_billto_city());
		addrPanel.setStateCode(tempObj.get_billto_state_code());
		addrPanel.setZip(tempObj.get_billto_zip());
		addrPanel.setCountryCode(tempObj.get_billto_country_code());
		addrPanel.setCountryName(tempObj.get_billto_country_name());
        daiTextField_contact.setText(tempObj.get_our_contact());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
