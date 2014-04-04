//Title:        Item Package
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Iten Entry/Update

package dai.client.ui.businessTrans.purchOrder;

import javax.swing.JFrame;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.purch_orderObj;
import daiBeans.daiAddressPanel;

public class PurchOrderShipToPanel extends daiHeaderSubPanel
{
    daiAddressPanel addrPanel = new daiAddressPanel("Ship To Address:");
    XYLayout xYLayout1 = new XYLayout();

    public PurchOrderShipToPanel(JFrame container, daiFrame parentFrame, purch_orderObj obj)
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
        this.setLayout(xYLayout1);
        this.setBackground(daiColors.PanelColor);
        xYLayout1.setHeight(300);
        xYLayout1.setWidth(467);
        this.add(addrPanel, new XYConstraints(10, 13, 452, 260));
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        purch_orderObj tempObj = (purch_orderObj)obj;

		tempObj.set_shipto_addr1(addrPanel.getAddr1());
		tempObj.set_shipto_addr2(addrPanel.getAddr2());
		tempObj.set_shipto_addr3(addrPanel.getAddr3());
		tempObj.set_shipto_attn(addrPanel.getAttn());
		tempObj.set_shipto_city(addrPanel.getCity());
		tempObj.set_shipto_state_code(addrPanel.getStateCode());
		tempObj.set_shipto_zip(addrPanel.getZip());
		tempObj.set_shipto_country_code(addrPanel.getCountryCode());
		tempObj.set_shipto_country_name(addrPanel.getCountryName());
    }

	public void update_UI(BusinessObject obj)
    {
		purch_orderObj tempObj = (purch_orderObj)obj;

		addrPanel.setAddr1(tempObj.get_shipto_addr1());
		addrPanel.setAddr2(tempObj.get_shipto_addr2());
		addrPanel.setAddr3(tempObj.get_shipto_addr3());
		addrPanel.setAttn(tempObj.get_shipto_attn());
		addrPanel.setCity(tempObj.get_shipto_city());
		addrPanel.setStateCode(tempObj.get_shipto_state_code());
		addrPanel.setZip(tempObj.get_shipto_zip());
		addrPanel.setCountryCode(tempObj.get_shipto_country_code());
		addrPanel.setCountryName(tempObj.get_shipto_country_name());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
