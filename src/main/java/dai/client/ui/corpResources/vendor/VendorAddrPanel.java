
//Title:        Vendor Package
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Vendor Address Entry/Update

package dai.client.ui.corpResources.vendor;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.vendorObj;
import daiBeans.daiAddressPanel;
import daiBeans.daiCheckBox;

public class VendorAddrPanel extends daiHeaderSubPanel
{
    //Entry Field Declarations
    daiAddressPanel shipToAddrPanel = new daiAddressPanel("Ship To Addr:");
    daiAddressPanel remitToAddrPanel = new daiAddressPanel("Remit To Addr:");
    JPanel          checkBoxPanel   = new JPanel();
    daiCheckBox checkBox_isRemitToSameAsShipTo = new daiCheckBox("Remit To Same As Ship To");

    //Other Declarations
    BoxLayout2 boxLayout21 = new BoxLayout2();
    XYLayout xYLayout1 = new XYLayout();

    public VendorAddrPanel(JFrame container, daiFrame parentFrame, vendorObj obj)
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
        this.setLayout(boxLayout21);

        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        checkBoxPanel.setLayout(xYLayout1);
        checkBoxPanel.setBackground(daiColors.PanelColor);

        this.setBackground(daiColors.PanelColor);
        this.add(shipToAddrPanel, null);
        this.add(checkBoxPanel, null);
        checkBoxPanel.add(checkBox_isRemitToSameAsShipTo, new XYConstraints(10, 0, -1, 21));
        this.add(remitToAddrPanel, null);
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        vendorObj tempObj = (vendorObj)obj;

		tempObj.set_remit_addr1(remitToAddrPanel.getAddr1());
		tempObj.set_remit_addr2(remitToAddrPanel.getAddr2());
		tempObj.set_remit_addr3(remitToAddrPanel.getAddr3());
		tempObj.set_remitto_attn(remitToAddrPanel.getAttn());
		tempObj.set_remit_city(remitToAddrPanel.getCity());
		tempObj.set_remit_state_code(remitToAddrPanel.getStateCode());
		tempObj.set_remit_zip(remitToAddrPanel.getZip());
        tempObj.set_remit_country_code(remitToAddrPanel.getCountryCode());
        tempObj.set_remit_country_name(remitToAddrPanel.getCountryName());

		tempObj.set_shipto_addr1(shipToAddrPanel.getAddr1());
		tempObj.set_shipto_addr2(shipToAddrPanel.getAddr2());
		tempObj.set_shipto_addr3(shipToAddrPanel.getAddr3());
		tempObj.set_shipto_attn(shipToAddrPanel.getAttn());
		tempObj.set_shipto_city(shipToAddrPanel.getCity());
		tempObj.set_shipto_state_code(shipToAddrPanel.getStateCode());
		tempObj.set_shipto_zip(shipToAddrPanel.getZip());
        tempObj.set_shipto_country_code(shipToAddrPanel.getCountryCode());
        tempObj.set_shipto_country_name(shipToAddrPanel.getCountryName());

        tempObj.set_is_remitto_sameas_shipto(checkBox_isRemitToSameAsShipTo.getValue());
    }

	public void update_UI(BusinessObject obj)
    {
		vendorObj tempObj = (vendorObj)obj;

		remitToAddrPanel.setAddr1(tempObj.get_remit_addr1());
		remitToAddrPanel.setAddr2(tempObj.get_remit_addr2());
		remitToAddrPanel.setAddr3(tempObj.get_remit_addr3());
		remitToAddrPanel.setAttn(tempObj.get_remitto_attn());
		remitToAddrPanel.setCity(tempObj.get_remit_city());
		remitToAddrPanel.setStateCode(tempObj.get_remit_state_code());
		remitToAddrPanel.setZip(tempObj.get_remit_zip());
        remitToAddrPanel.setCountryCode(tempObj.get_remit_country_code());
        remitToAddrPanel.setCountryName(tempObj.get_remit_country_name());

		shipToAddrPanel.setAddr1(tempObj.get_shipto_addr1());
		shipToAddrPanel.setAddr2(tempObj.get_shipto_addr2());
		shipToAddrPanel.setAddr3(tempObj.get_shipto_addr3());
		shipToAddrPanel.setAttn(tempObj.get_shipto_attn());
		shipToAddrPanel.setCity(tempObj.get_shipto_city());
		shipToAddrPanel.setStateCode(tempObj.get_shipto_state_code());
		shipToAddrPanel.setZip(tempObj.get_shipto_zip());
        shipToAddrPanel.setCountryCode(tempObj.get_shipto_country_code());
        shipToAddrPanel.setCountryName(tempObj.get_shipto_country_name());

        checkBox_isRemitToSameAsShipTo.setValue(tempObj.get_is_remitto_sameas_shipto());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
