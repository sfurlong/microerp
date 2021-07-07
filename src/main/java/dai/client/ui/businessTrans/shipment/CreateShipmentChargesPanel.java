
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.daiCurrencyField;
import daiBeans.daiDateField;
import daiBeans.daiLabel;

public class CreateShipmentChargesPanel extends daiWizardPanel
{
    Logger _logger;
    SessionMetaData _sessionMeta;

    default_accountsObj _defaultAcctsObj;

	daiCurrencyField fieldControl_shippingCharges = new daiCurrencyField();

	daiLabel daiLabel_shippingCharges = new daiLabel("Shipping Charges:");

    boolean firstTimeThrough = false;
    GroupBox groupBox = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();
    daiDateField daiDateField_dateShipped = new daiDateField();
    daiLabel daiLabel_dateShipped = new daiLabel();
    XYLayout xYLayout1 = new XYLayout();

    public CreateShipmentChargesPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            _logger.logError(null, this.getClass().getName()+": Could not initialize\n"+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        _logger = _logger.getInstance();
        _sessionMeta = _sessionMeta.getInstance();

        this.setLayout(xYLayout1);
        groupBox.setLabel("New Shipment Values");
        groupBox.setLayout(xYLayout2);
        daiDateField_dateShipped.setCurrentDate(true);
        daiLabel_dateShipped.setText("Date Shipped:");
        this.add(groupBox, new XYConstraints(157, 56, 330, -1));
        groupBox.add(daiLabel_dateShipped, new XYConstraints(37, 20, -1, -1));
        groupBox.add(daiDateField_dateShipped, new XYConstraints(109, 20, -1, -1));
        groupBox.add(daiLabel_shippingCharges, new XYConstraints(16, 48, -1, -1));
        groupBox.add(fieldControl_shippingCharges, new XYConstraints(109, 46, -1, -1));
    }

    public boolean panelDataIsValid()
    {
        return true;
    }

    public DBRec getChargesData()
    {
        DBAttributes shipChargeAttribs = new DBAttributes(shipmentObj.TOTAL_SHIPPING,
                                                fieldControl_shippingCharges.getText(),
                                                BusinessObject.DATA_TYPE_NUMERIC,
                                                false);
        DBRec ret = new DBRec();
        ret.addAttrib(shipChargeAttribs);
        return ret;
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }
}
