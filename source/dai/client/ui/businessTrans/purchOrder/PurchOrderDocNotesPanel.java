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
import daiBeans.daiLabel;
import daiBeans.daiTextArea;

public class PurchOrderDocNotesPanel extends daiHeaderSubPanel
{
    XYLayout xYLayout1 = new XYLayout();
	daiTextArea jTextArea_headerNote = new daiTextArea();
	daiTextArea jTextArea_footerNote = new daiTextArea();
 	daiLabel daiLabel_headerNote = new daiLabel("Document Header:");
	daiLabel daiLabel_footerNote = new daiLabel("Document Footer:");

    public PurchOrderDocNotesPanel(JFrame container, daiFrame parentFrame, purch_orderObj obj)
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
        xYLayout1.setHeight(261);
        xYLayout1.setWidth(528);
        daiLabel_headerNote.setText("Document Header Note:");
        this.add(jTextArea_headerNote, new XYConstraints(129, 24, 320, 77));
        this.add(daiLabel_headerNote, new XYConstraints(6, 19, 119, 26));
        this.add(daiLabel_footerNote, new XYConstraints(37, 121, 88, 25));
        this.add(jTextArea_footerNote, new XYConstraints(129, 128, 320, 77));

    }

   	public void update_BusinessObj(BusinessObject obj)
    {
        purch_orderObj tempObj = (purch_orderObj)obj;

		tempObj.set_note1(jTextArea_headerNote.getText());
		tempObj.set_note2(jTextArea_footerNote.getText());

    }

	public void update_UI(BusinessObject obj)
    {
		purch_orderObj tempObj = (purch_orderObj)obj;

		jTextArea_headerNote.setText(tempObj.get_note1());
		jTextArea_footerNote.setText(tempObj.get_note2());

		BUSINESS_OBJ = tempObj;
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        //!!Should never be called.
        return BUSINESS_OBJ ;
    }
}
