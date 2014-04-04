
//Title:        Item Package
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI For Iten Entry/Update

package dai.client.ui.corpResources.item;

import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.VerticalFlowLayout;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.itemObj;
import daiBeans.daiLabel;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;

public class ItemVendorPanel extends daiHeaderSubPanel
{
    //Entry Field Declarations
    daiTextField daiTextField_vendorItemId = new daiTextField();
    daiTextField daiTextField_vendorModel = new daiTextField();
    daiTextArea daiTextField_vendorItemDesc = new daiTextArea();
    daiTextField daiTextField_vendorLot = new daiTextField();
	daiTextField fieldControl_mfgId = new daiTextField();
    daiTextField daiTextField_mfgItemId = new daiTextField();
    daiTextField daiTextField_mfgModel = new daiTextField();
    daiTextField daiTextField_mfgName = new daiTextField();
    daiTextField daiTextField_mfgItemDesc = new daiTextField();
    daiTextField daiTextField_mfgLot = new daiTextField();
	daiTextField fieldControl_countryOfMfg = new daiTextField();

    //Label Declarations
    daiLabel daiLabel_vendorItemId = new daiLabel("Item Id:");
    daiLabel daiLabel_vendorItemDesc = new daiLabel("Item Desc:");
    daiLabel daiLabel_vendorModel = new daiLabel("Model:");
    daiLabel daiLabel_vendorLot = new daiLabel("Lot:");
	daiLabel labelControl_mfgId = new daiLabel("Mfg Id:");
    daiLabel daiLabel_mfgName = new daiLabel("Mfg Name:");
    daiLabel daiLabel_mfgItemId = new daiLabel("Mfg Item Id:");
    daiLabel daiLabel_mfgItemDesc = new daiLabel("Mfg Desc:");
    daiLabel daiLabel_mfgModel = new daiLabel("Model:");
    daiLabel daiLabel_mfgLot = new daiLabel("Lot:");
	daiLabel labelControl_countryOfMfg = new daiLabel("Country Of Mfg:");


    //Other Declarations
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    GroupBox groupBox_vendor = new GroupBox();
    GroupBox groupBox_mfg = new GroupBox();
    XYLayout xYLayout4 = new XYLayout();
    XYLayout xYLayout5 = new XYLayout();

    public ItemVendorPanel(JFrame container, daiFrame parentFrame, itemObj obj)
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

        groupBox_vendor.setLabel("Vendor Data");
        groupBox_vendor.setLayout(xYLayout4);
        labelControl_mfgId.setHREFstyle(true);
        groupBox_mfg.setLabel("Mfg Data");
        groupBox_mfg.setLayout(xYLayout5);
        daiLabel_vendorItemId.setText("Part#:");
        daiLabel_mfgItemId.setText("Mfg Part#:");
        daiLabel_mfgModel.setText("Mfg Model:");
        daiLabel_vendorItemDesc.setText("Model Desc:");

        groupBox_mfg.add(daiLabel_mfgItemId, new XYConstraints(0, 66, 60, -1));
        groupBox_mfg.add(daiLabel_mfgModel, new XYConstraints(8, 39, -1, -1));
        groupBox_mfg.add(daiLabel_mfgLot, new XYConstraints(264, 66, -1, -1));
        groupBox_mfg.add(labelControl_mfgId, new XYConstraints(26, 13, 34, 15));
        groupBox_mfg.add(daiLabel_mfgName, new XYConstraints(231, 13, -1, -1));
        groupBox_mfg.add(daiLabel_mfgItemDesc, new XYConstraints(233, 39, -1, -1));
        groupBox_mfg.add(labelControl_countryOfMfg, new XYConstraints(198, 95, 83, 15));
        groupBox_mfg.add(fieldControl_mfgId, new XYConstraints(67, 10, 99, -1));
        groupBox_mfg.add(daiTextField_mfgName, new XYConstraints(287, 10, 221, -1));
        groupBox_mfg.add(daiTextField_mfgModel, new XYConstraints(67, 39, 155, -1));
        groupBox_mfg.add(daiTextField_mfgItemDesc, new XYConstraints(287, 38, 222, -1));
        groupBox_mfg.add(daiTextField_mfgItemId, new XYConstraints(67, 66, 155, -1));
        groupBox_mfg.add(daiTextField_mfgLot, new XYConstraints(287, 66, 155, -1));
        groupBox_mfg.add(fieldControl_countryOfMfg, new XYConstraints(286, 93, -1, -1));

        groupBox_vendor.add(daiLabel_vendorModel, new XYConstraints(33, 11, -1, -1));
        groupBox_vendor.add(daiTextField_vendorModel, new XYConstraints(68, 9, 143, -1));
        groupBox_vendor.add(daiLabel_vendorItemId, new XYConstraints(36, 40, -1, -1));
        groupBox_vendor.add(daiTextField_vendorItemId, new XYConstraints(68, 37, 143, -1));
        groupBox_vendor.add(daiLabel_vendorLot, new XYConstraints(45, 68, -1, -1));
        groupBox_vendor.add(daiTextField_vendorLot, new XYConstraints(68, 65, 143, -1));
        groupBox_vendor.add(daiLabel_vendorItemDesc, new XYConstraints(217, 11, -1, -1));
        groupBox_vendor.add(daiTextField_vendorItemDesc, new XYConstraints(283, 9, 231, 71));

        this.setBackground(daiColors.PanelColor);
        this.add(groupBox_vendor, new XYConstraints(19, 456, 587, 121));
        this.add(groupBox_mfg, new XYConstraints(19, 584, 587, -1));
    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        itemObj tempObj = (itemObj)obj;

		tempObj.set_vendor_item_id(daiTextField_vendorItemId.getText());
		tempObj.set_vendor_item_desc(daiTextField_vendorItemDesc.getText());
		tempObj.set_vendor_item_model(daiTextField_vendorModel.getText());
        tempObj.set_vendor_item_lot(daiTextField_vendorLot.getText());

        tempObj.set_mfg_id(fieldControl_mfgId.getText());
        tempObj.set_mfg_name(daiTextField_mfgName.getText());
        tempObj.set_mfg_item_id(daiTextField_mfgItemId.getText());
        tempObj.set_mfg_item_desc(daiTextField_mfgItemDesc.getText());
        tempObj.set_mfg_item_lot(daiTextField_mfgLot.getText());
        tempObj.set_mfg_item_model(daiTextField_mfgModel.getText());
		tempObj.set_country_of_mfg(fieldControl_countryOfMfg.getText());
    }

	public void update_UI(BusinessObject obj)
    {
		itemObj tempObj = (itemObj)obj;

        daiTextField_vendorItemId.setText(tempObj.get_vendor_item_id());
        daiTextField_vendorItemDesc.setText(tempObj.get_vendor_item_desc());
        daiTextField_vendorModel.setText(tempObj.get_vendor_item_model());
        daiTextField_vendorLot.setText(tempObj.get_vendor_item_lot());

        fieldControl_mfgId.setText(tempObj.get_mfg_id());
        daiTextField_mfgName.setText(tempObj.get_mfg_name());
        daiTextField_mfgItemId.setText(tempObj.get_mfg_item_id());
        daiTextField_mfgItemDesc.setText(tempObj.get_mfg_item_desc());
        daiTextField_mfgLot.setText(tempObj.get_mfg_item_lot());
        daiTextField_mfgModel.setText(tempObj.get_mfg_item_model());
        fieldControl_countryOfMfg.setText(tempObj.get_country_of_mfg());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
