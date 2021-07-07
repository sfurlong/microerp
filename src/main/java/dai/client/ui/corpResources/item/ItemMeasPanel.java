
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
import daiBeans.daiNumField;

public class ItemMeasPanel extends daiHeaderSubPanel
{
    //Entry Field Declarations
	daiNumField fieldControl_len = new daiNumField();
	daiNumField fieldControl_width = new daiNumField();
	daiNumField fieldControl_height = new daiNumField();
    daiNumField fieldControl_grWgtLb = new daiNumField();
    daiNumField fieldControl_grWgtKg = new daiNumField();
    daiNumField fieldControl_netWgtLb = new daiNumField();
    daiNumField fieldControl_netWgtKg = new daiNumField();


    //Label Declarations
	daiLabel labelControl_len = new daiLabel("Length:");
	daiLabel labelControl_width = new daiLabel("Width:");
	daiLabel labelControl_height = new daiLabel("Height:");
    daiLabel daiLabel_grWgtLB = new daiLabel("Gross Wgt LB:");
    daiLabel daiLabel_grWgtKG = new daiLabel("Gross Wgt KG:");
    daiLabel daiLabel_netWgtLB = new daiLabel("Net Wgt LB:");
    daiLabel daiLabel_netWgtKG = new daiLabel("Net Wgt KG:");

    //Other Declarations
    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();
    GroupBox groupBox_meas = new GroupBox();
    XYLayout xYLayout7 = new XYLayout();

    public ItemMeasPanel(JFrame container, daiFrame parentFrame, itemObj obj)
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
        groupBox_meas.setLabel("Dimensions");
        groupBox_meas.setLayout(xYLayout7);
        groupBox_meas.add(labelControl_len, new XYConstraints(42, 4, -1, -1));
        groupBox_meas.add(daiLabel_netWgtLB, new XYConstraints(21, 57, -1, -1));
        groupBox_meas.add(daiLabel_grWgtLB, new XYConstraints(7, 31, -1, -1));
        groupBox_meas.add(daiLabel_grWgtKG, new XYConstraints(168, 31, -1, -1));
        groupBox_meas.add(labelControl_width, new XYConstraints(203, 4, 38, -1));
        groupBox_meas.add(daiLabel_netWgtKG, new XYConstraints(182, 57, -1, -1));
        groupBox_meas.add(labelControl_height, new XYConstraints(368, 4, -1, -1));
        groupBox_meas.add(fieldControl_len, new XYConstraints(82, 2, 83, -1));
        groupBox_meas.add(fieldControl_width, new XYConstraints(247, 2, -1, -1));
        groupBox_meas.add(fieldControl_height, new XYConstraints(404, 2, -1, -1));
        groupBox_meas.add(fieldControl_grWgtLb, new XYConstraints(82, 28, 83, -1));
        groupBox_meas.add(fieldControl_grWgtKg, new XYConstraints(247, 26, -1, -1));
        groupBox_meas.add(fieldControl_netWgtLb, new XYConstraints(82, 55, 83, -1));
        groupBox_meas.add(fieldControl_netWgtKg, new XYConstraints(247, 53, -1, -1));
        this.add(groupBox_meas, new XYConstraints(19, 344, 587, 104));

    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        itemObj tempObj = (itemObj)obj;

		tempObj.set_height(fieldControl_height.getText());
		tempObj.set_len(fieldControl_len.getText());
		tempObj.set_width(fieldControl_width.getText());
        tempObj.set_gross_wgt_kg(fieldControl_grWgtKg.getText());
        tempObj.set_gross_wgt_lb(fieldControl_grWgtLb.getText());
        tempObj.set_net_wgt_kg(fieldControl_netWgtKg.getText());
        tempObj.set_net_wgt_lb(fieldControl_netWgtLb.getText());
    }

	public void update_UI(BusinessObject obj)
    {
		itemObj tempObj = (itemObj)obj;

		fieldControl_height.setText(tempObj.get_height());
		fieldControl_len.setText(tempObj.get_len());
		fieldControl_width.setText(tempObj.get_width());
        fieldControl_grWgtKg.setText(tempObj.get_gross_wgt_kg());
        fieldControl_grWgtLb.setText(tempObj.get_gross_wgt_lb());
        fieldControl_netWgtKg.setText(tempObj.get_net_wgt_kg());
        fieldControl_netWgtLb.setText(tempObj.get_net_wgt_lb());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
