
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

public class PurchOrderVendorPanel extends daiHeaderSubPanel
{
    daiAddressPanel addrPanel = new daiAddressPanel("Vendor Address:");
    GroupBox groupBox1 = new GroupBox();
    daiLabel daiLabel_contact = new daiLabel();
    daiLabel daiLabel_fax = new daiLabel();
    XYLayout xYLayout2 = new XYLayout();
    daiTextField daiTextField_contact = new daiTextField();
    daiTextField daiTextField_fax = new daiTextField();
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

    public PurchOrderVendorPanel(JFrame container, daiFrame parentFrame, purch_orderObj obj)
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
        groupBox1.setLabel("Vendor Contact Info:");
        groupBox1.setLayout(xYLayout2);
        daiLabel_contact.setText("Contact:");
        daiTextField_contact.setText("");
        daiLabel_fax.setText("Fax:");
        daiTextField_fax.setText("");
        this.add(groupBox1, null);
        groupBox1.add(daiLabel_contact, new XYConstraints(-3, 2, -1, -1));
        groupBox1.add(daiTextField_contact, new XYConstraints(40, 1, 331, -1));
        groupBox1.add(daiLabel_fax, new XYConstraints(390, 2, -1, -1));
        groupBox1.add(daiTextField_fax, new XYConstraints(415, 1, 117, -1));
        this.add(addrPanel, null);
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        purch_orderObj tempObj = (purch_orderObj)obj;

		tempObj.set_vendor_addr1(addrPanel.getAddr1());
		tempObj.set_vendor_addr2(addrPanel.getAddr2());
		tempObj.set_vendor_addr3(addrPanel.getAddr3());
		tempObj.set_vendor_attn(addrPanel.getAttn());
		tempObj.set_vendor_city(addrPanel.getCity());
		tempObj.set_vendor_state_code(addrPanel.getStateCode());
		tempObj.set_vendor_zip(addrPanel.getZip());
		tempObj.set_vendor_country_code(addrPanel.getCountryCode());
		tempObj.set_vendor_country_name(addrPanel.getCountryName());
        tempObj.set_vendor_contact(daiTextField_contact.getText());
        tempObj.set_vendor_fax(daiTextField_fax.getText());
    }

	public void update_UI(BusinessObject obj)
    {
		purch_orderObj tempObj = (purch_orderObj)obj;

		addrPanel.setAddr1(tempObj.get_vendor_addr1());
		addrPanel.setAddr2(tempObj.get_vendor_addr2());
		addrPanel.setAddr3(tempObj.get_vendor_addr3());
		addrPanel.setAttn(tempObj.get_vendor_attn());
		addrPanel.setCity(tempObj.get_vendor_city());
		addrPanel.setStateCode(tempObj.get_vendor_state_code());
		addrPanel.setZip(tempObj.get_vendor_zip());
		addrPanel.setCountryCode(tempObj.get_vendor_country_code());
		addrPanel.setCountryName(tempObj.get_vendor_country_name());
        daiTextField_contact.setText(tempObj.get_vendor_contact());
        daiTextField_fax.setText(tempObj.get_vendor_fax());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
