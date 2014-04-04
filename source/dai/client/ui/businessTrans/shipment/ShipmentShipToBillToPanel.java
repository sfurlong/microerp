
//Title:        Shipment Entry
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Shipment Entry/Update

package dai.client.ui.businessTrans.shipment;

import javax.swing.JFrame;

import com.borland.jbcl.layout.VerticalFlowLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.shipmentObj;
import daiBeans.daiAddressPanel;
import daiBeans.daiCheckBox;

public class ShipmentShipToBillToPanel extends daiHeaderSubPanel
{
    //Entry Field Declarations
    daiAddressPanel billToAddrPanel = new daiAddressPanel("Bill To:");
    daiAddressPanel shipToAddrPanel = new daiAddressPanel("Ship To:");
    //Label Declarations

    //Other Declarations
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    daiCheckBox checkBox_isDropShip
            = new daiCheckBox("Ship To is a Third Party Drop Ship?");

    public ShipmentShipToBillToPanel(JFrame container, daiFrame parentFrame, shipmentObj obj)
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
        this.add(shipToAddrPanel, null);
        this.add(checkBox_isDropShip);
        this.add(billToAddrPanel, null);
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        shipmentObj tempObj = (shipmentObj)obj;

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
        tempObj.set_shipto_is_dropship(checkBox_isDropShip.getValue());
    }

	public void update_UI(BusinessObject obj)
    {
		shipmentObj tempObj = (shipmentObj)obj;

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
        checkBox_isDropShip.setValue(tempObj.get_shipto_is_dropship());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
