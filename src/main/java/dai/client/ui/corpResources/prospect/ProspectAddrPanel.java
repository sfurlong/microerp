
//Title:        Customer Package
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Customer Address Entry/Update

package dai.client.ui.corpResources.prospect;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.prospectObj;
import daiBeans.daiAddressPanel;

public class ProspectAddrPanel extends daiHeaderSubPanel
{
    //Entry Field Declarations
    daiAddressPanel addrPanel = new daiAddressPanel("Prospect Address:");

    //Other Declarations
    BoxLayout2 boxLayout21 = new BoxLayout2();
    JPanel jPanel1 = new JPanel();
    XYLayout xYLayout1 = new XYLayout();

    public ProspectAddrPanel(JFrame container, daiFrame parentFrame, prospectObj obj)
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
        addrPanel.hideAttnField();
        jPanel1.setBackground(daiColors.PanelColor);
        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        jPanel1.setLayout(xYLayout1);
        this.setLayout(boxLayout21);
        this.setBackground(daiColors.PanelColor);
        this.add(addrPanel, null);
        this.add(jPanel1, null);
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        prospectObj tempObj = (prospectObj)obj;

		tempObj.set_addr1(addrPanel.getAddr1());
		tempObj.set_addr2(addrPanel.getAddr2());
		tempObj.set_addr3(addrPanel.getAddr3());
		tempObj.set_city(addrPanel.getCity());
		tempObj.set_state_code(addrPanel.getStateCode());
		tempObj.set_zip(addrPanel.getZip());
        tempObj.set_country_code(addrPanel.getCountryCode());
        tempObj.set_country_name(addrPanel.getCountryName());

    }

	public void update_UI(BusinessObject obj)
    {
		prospectObj tempObj = (prospectObj)obj;

		addrPanel.setAddr1(tempObj.get_addr1());
		addrPanel.setAddr2(tempObj.get_addr2());
		addrPanel.setAddr3(tempObj.get_addr3());
		addrPanel.setCity(tempObj.get_city());
		addrPanel.setStateCode(tempObj.get_state_code());
		addrPanel.setZip(tempObj.get_zip());
        addrPanel.setCountryCode(tempObj.get_country_code());
        addrPanel.setCountryName(tempObj.get_country_name());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
