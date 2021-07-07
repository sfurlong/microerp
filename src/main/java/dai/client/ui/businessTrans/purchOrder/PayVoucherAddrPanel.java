
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import javax.swing.JFrame;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.payment_voucherObj;
import daiBeans.daiAddressPanel;
import daiBeans.daiLabel;
import daiBeans.daiTextField;


public class PayVoucherAddrPanel extends daiHeaderSubPanel
{
    daiAddressPanel addrPanel = new daiAddressPanel("Payee Address");
    XYLayout xYLayout1 = new XYLayout();
    daiLabel daiLabel_ourAcctWithVendor = new daiLabel();
    daiTextField daiTextField_ourAcctNumWithVendor = new daiTextField();

    public PayVoucherAddrPanel(JFrame container, daiFrame parentFrame, payment_voucherObj obj)
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
        xYLayout1.setHeight(258);
        xYLayout1.setWidth(467);

        //Hide the Attention field on the address panel
        addrPanel.hideAttnField();

        this.setLayout(xYLayout1);
        this.setBackground(daiColors.PanelColor);
        daiLabel_ourAcctWithVendor.setText("Our Acct Num With Vendor:");
        daiTextField_ourAcctNumWithVendor.setText("");
        this.add(addrPanel, new XYConstraints(10, 4, 400, 194));
        this.add(daiLabel_ourAcctWithVendor, new XYConstraints(15, 206, -1, -1));
        this.add(daiTextField_ourAcctNumWithVendor, new XYConstraints(152, 204, 242, -1));
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        payment_voucherObj tempObj = (payment_voucherObj)obj;

		tempObj.set_vendor_addr1(addrPanel.getAddr1());
		tempObj.set_vendor_addr2(addrPanel.getAddr2());
		tempObj.set_vendor_addr3(addrPanel.getAddr3());
		tempObj.set_vendor_city(addrPanel.getCity());
		tempObj.set_vendor_state_code(addrPanel.getStateCode());
		tempObj.set_vendor_zip(addrPanel.getZip());
		tempObj.set_vendor_country_code(addrPanel.getCountryCode());
		tempObj.set_vendor_country_name(addrPanel.getCountryName());
        tempObj.set_our_acct_no_with_vendor(daiTextField_ourAcctNumWithVendor.getText());
    }

	public void update_UI(BusinessObject obj)
    {
		payment_voucherObj tempObj = (payment_voucherObj)obj;

		addrPanel.setAddr1(tempObj.get_vendor_addr1());
		addrPanel.setAddr2(tempObj.get_vendor_addr2());
		addrPanel.setAddr3(tempObj.get_vendor_addr3());
		addrPanel.setCity(tempObj.get_vendor_city());
		addrPanel.setStateCode(tempObj.get_vendor_state_code());
		addrPanel.setZip(tempObj.get_vendor_zip());
		addrPanel.setCountryCode(tempObj.get_vendor_country_code());
		addrPanel.setCountryName(tempObj.get_vendor_country_name());
        daiTextField_ourAcctNumWithVendor.setText(tempObj.get_our_acct_no_with_vendor());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}

