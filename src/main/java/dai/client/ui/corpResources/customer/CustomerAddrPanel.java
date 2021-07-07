
//Title:        Customer Package
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Customer Address Entry/Update

package dai.client.ui.corpResources.customer;

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
import dai.shared.businessObjs.customerObj;
import daiBeans.daiAddressPanel;
import daiBeans.daiCheckBox;

public class CustomerAddrPanel extends daiHeaderSubPanel
{
    //Entry Field Declarations
    daiAddressPanel shipToAddrPanel = new daiAddressPanel("Ship To Addr:");
    daiAddressPanel billToAddrPanel = new daiAddressPanel("Bill To Addr:");
    daiCheckBox checkBox_billToIsShipTo = new daiCheckBox("Bill To Same as Ship To");

    //Other Declarations
    BoxLayout2 boxLayout21 = new BoxLayout2();
    JPanel jPanel1 = new JPanel();
    XYLayout xYLayout1 = new XYLayout();

    public CustomerAddrPanel(JFrame container, daiFrame parentFrame, customerObj obj)
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
        jPanel1.setBackground(daiColors.PanelColor);
        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        jPanel1.setLayout(xYLayout1);
        jPanel1.add(checkBox_billToIsShipTo, new XYConstraints(12, 0, -1, -1));
        this.setLayout(boxLayout21);
        this.setBackground(daiColors.PanelColor);
        this.add(shipToAddrPanel, null);
        this.add(jPanel1, null);
        this.add(billToAddrPanel, null);
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        customerObj tempObj = (customerObj)obj;

		tempObj.set_billto_addr1(billToAddrPanel.getAddr1());
		tempObj.set_billto_addr2(billToAddrPanel.getAddr2());
		tempObj.set_billto_addr3(billToAddrPanel.getAddr3());
		tempObj.set_billto_attn(billToAddrPanel.getAttn());
		tempObj.set_billto_city(billToAddrPanel.getCity());
		tempObj.set_billto_state_code(billToAddrPanel.getStateCode());
		tempObj.set_billto_zip(billToAddrPanel.getZip());
        tempObj.set_billto_country_code(billToAddrPanel.getCountryCode());
        tempObj.set_billto_country_name(billToAddrPanel.getCountryName());

		tempObj.set_shipto_addr1(shipToAddrPanel.getAddr1());
		tempObj.set_shipto_addr2(shipToAddrPanel.getAddr2());
		tempObj.set_shipto_addr3(shipToAddrPanel.getAddr3());
		tempObj.set_shipto_attn(shipToAddrPanel.getAttn());
		tempObj.set_shipto_city(shipToAddrPanel.getCity());
		tempObj.set_shipto_state_code(shipToAddrPanel.getStateCode());
		tempObj.set_shipto_zip(shipToAddrPanel.getZip());
        tempObj.set_shipto_country_code(shipToAddrPanel.getCountryCode());
        tempObj.set_shipto_country_name(shipToAddrPanel.getCountryName());

        tempObj.set_billto_is_shipto(checkBox_billToIsShipTo.getValue());
    }

	public void update_UI(BusinessObject obj)
    {
		customerObj tempObj = (customerObj)obj;

		billToAddrPanel.setAddr1(tempObj.get_billto_addr1());
		billToAddrPanel.setAddr2(tempObj.get_billto_addr2());
		billToAddrPanel.setAddr3(tempObj.get_billto_addr3());
		billToAddrPanel.setAttn(tempObj.get_billto_attn());
		billToAddrPanel.setCity(tempObj.get_billto_city());
		billToAddrPanel.setStateCode(tempObj.get_billto_state_code());
		billToAddrPanel.setZip(tempObj.get_billto_zip());
        billToAddrPanel.setCountryCode(tempObj.get_billto_country_code());
        billToAddrPanel.setCountryName(tempObj.get_billto_country_name());

		shipToAddrPanel.setAddr1(tempObj.get_shipto_addr1());
		shipToAddrPanel.setAddr2(tempObj.get_shipto_addr2());
		shipToAddrPanel.setAddr3(tempObj.get_shipto_addr3());
		shipToAddrPanel.setAttn(tempObj.get_shipto_attn());
		shipToAddrPanel.setCity(tempObj.get_shipto_city());
		shipToAddrPanel.setStateCode(tempObj.get_shipto_state_code());
		shipToAddrPanel.setZip(tempObj.get_shipto_zip());
        shipToAddrPanel.setCountryCode(tempObj.get_shipto_country_code());
        shipToAddrPanel.setCountryName(tempObj.get_shipto_country_name());

        checkBox_billToIsShipTo.setValue(tempObj.get_billto_is_shipto());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
