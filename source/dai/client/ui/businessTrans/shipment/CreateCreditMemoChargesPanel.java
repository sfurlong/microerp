           
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.awt.event.FocusEvent;

import javax.swing.JLabel;

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
import daiBeans.daiLabel;

public class CreateCreditMemoChargesPanel extends daiWizardPanel
{
    Logger _logger;
    SessionMetaData _sessionMeta;

    default_accountsObj _defaultAcctsObj;

	daiCurrencyField daiCurrencyField_shippingCredits = new daiCurrencyField();

	daiLabel daiLabel_shippingCredits = new daiLabel("Shipping Charges:");

    boolean firstTimeThrough = false;
    GroupBox groupBox = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();
    daiLabel daiLabel_subtot = new daiLabel();
    XYLayout xYLayout1 = new XYLayout();
    daiCurrencyField daiCurrencyField_subtot = new daiCurrencyField();
    daiCurrencyField daiCurrencyField_taxCredits = new daiCurrencyField();
    daiLabel daiLabel_taxCredits = new daiLabel();
    daiCurrencyField daiCurrencyField_totCredit = new daiCurrencyField();
    daiLabel daiLabel_totCredit = new daiLabel();
    JLabel jLabel_note = new JLabel();

    public CreateCreditMemoChargesPanel()
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

        daiCurrencyField_subtot.setDisabled(true);
        daiCurrencyField_totCredit.setDisabled(true);
        this.setLayout(xYLayout1);
        groupBox.setLabel("Credit Totals");
        groupBox.setLayout(xYLayout2);
        daiLabel_subtot.setText("Credit Subtotal:");
        xYLayout1.setHeight(238);
        xYLayout1.setWidth(519);
        daiLabel_shippingCredits.setText("Shipping Credits:");
        daiLabel_taxCredits.setText("Tax Credits:");
        daiLabel_totCredit.setText("Total Credit Amt:");

        daiCurrencyField_shippingCredits.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                daiCurrencyField_shippingCredits_focusLost(e);
            }
        });
        daiCurrencyField_taxCredits.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                daiCurrencyField_taxCredits_focusLost(e);
            }
        });
        jLabel_note.setFont(new java.awt.Font("Dialog", 2, 11));
        jLabel_note.setText("*Shipping & Tax credits should be entered as a positive number");
        this.add(groupBox, new XYConstraints(88, 19, 358, 189));
        groupBox.add(jLabel_note, new XYConstraints(11, 147, -1, -1));
        groupBox.add(daiLabel_taxCredits, new XYConstraints(68, 78, 81, -1));
        groupBox.add(daiLabel_subtot, new XYConstraints(76, 25, -1, -1));
        groupBox.add(daiCurrencyField_subtot, new XYConstraints(154, 23, -1, -1));
        groupBox.add(daiLabel_shippingCredits, new XYConstraints(68, 52, -1, -1));
        groupBox.add(daiCurrencyField_shippingCredits, new XYConstraints(154, 50, -1, -1));
        groupBox.add(daiCurrencyField_taxCredits, new XYConstraints(154, 76, -1, -1));
        groupBox.add(daiCurrencyField_totCredit, new XYConstraints(154, 100, -1, -1));
        groupBox.add(daiLabel_totCredit, new XYConstraints(68, 102, 81, -1));

    }

    public boolean panelDataIsValid()
    {
        return true;
    }

    public void setSubTotal(double subtot) {
        daiCurrencyField_subtot.setText(Double.toString(subtot * -1));
        calcTotals();
    }

    public DBRec getChargesData()
    {
        DBAttributes shipChargeAttribs = new DBAttributes(shipmentObj.TOTAL_SHIPPING,
                                                daiCurrencyField_shippingCredits.getText(),
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

    public String getSubTot() {
        return daiCurrencyField_subtot.getText();
    }

    public String getShipCredits() {
        return daiCurrencyField_shippingCredits.getText();
    }

    public String getTaxCredits() {
        return daiCurrencyField_taxCredits.getText();
    }

    public String getTotCreditAmt() {
        return daiCurrencyField_totCredit.getText();
    }

    private void calcTotals() {

        String s_subtot = daiCurrencyField_subtot.getText();
        if (s_subtot == null) s_subtot = "0.0";
        String s_shippingCredits = daiCurrencyField_shippingCredits.getText();
        if (s_shippingCredits == null) s_shippingCredits = "0.0";
        String s_taxCredits = daiCurrencyField_taxCredits.getText();
        if (s_taxCredits == null) s_taxCredits = "0.0";

        double tot = Double.parseDouble(s_subtot) +
                     Double.parseDouble(s_shippingCredits) +
                     Double.parseDouble(s_taxCredits);

        daiCurrencyField_totCredit.setText(Double.toString(tot));
    }


    void daiCurrencyField_shippingCredits_focusLost(FocusEvent e)
    {
        String s_shipCredits = daiCurrencyField_shippingCredits.getText();
        if (s_shipCredits != null && Double.parseDouble(s_shipCredits) > 0) {
            double d_shipCredits = Double.parseDouble(s_shipCredits);
            d_shipCredits = d_shipCredits * -1;
            daiCurrencyField_shippingCredits.setText(Double.toString(d_shipCredits));
            calcTotals();
        }
    }

    void daiCurrencyField_taxCredits_focusLost(FocusEvent e)
    {
        String s_taxCredits = daiCurrencyField_taxCredits.getText();
        if (s_taxCredits != null && Double.parseDouble(s_taxCredits) > 0) {
            double d_taxCredits = Double.parseDouble(s_taxCredits);
            d_taxCredits = d_taxCredits * -1;
            daiCurrencyField_taxCredits.setText(Double.toString(d_taxCredits));
            calcTotals();
        }
    }
}
