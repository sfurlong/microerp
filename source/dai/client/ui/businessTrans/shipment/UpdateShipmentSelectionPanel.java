
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCurrencyField;
import daiBeans.daiLabel;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;

public class UpdateShipmentSelectionPanel extends daiWizardPanel
{
    SessionMetaData sessionMeta;
    csDBAdapterFactory dbAdapterFactory = null;
    csDBAdapter dbAdapter = null;

    XYLayout xYLayout1 = new XYLayout();
    daiTextField daiTextField_custId = new daiTextField();
    DefaultListModel listModel_right = new DefaultListModel();
    DefaultListModel listModel_left = new DefaultListModel();

    Logger _logger;
    daiTextField daiTextField_custName = new daiTextField();
    daiCurrencyField daiCurrency_subtotal = new daiCurrencyField();
    daiCurrencyField daiCurrency_amtDue = new daiCurrencyField();
    daiCurrencyField daiCurrency_taxes = new daiCurrencyField();
    daiLabel daiLabel_custId = new daiLabel("Customer Id:");
    daiLabel daiLabel_shipmentId = new daiLabel("Invoice#:");
    daiTextField daiTextField_shipmentId = new daiTextField();
    daiLabel daiLabel_origAmt = new daiLabel("Original Inv. Amt:");
    daiLabel daiLabel_amtDue = new daiLabel("Total Amt Due:");
    daiLabel daiLabel_taxes = new daiLabel("Total Amt Paid:");
    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();

    JFrame CONTAINER;
    UpdateShipmentFrame _CONTAINER_FRAME = null;
    daiLabel daiLabel_shipCharges = new daiLabel();
    daiCurrencyField daiCurrency_shipCharges = new daiCurrencyField();
    GroupBox groupBox1 = new GroupBox();
    daiLabel daiLabel_newShipping = new daiLabel();
    XYLayout xYLayout2 = new XYLayout();
    daiLabel daiLabel_newTaxes = new daiLabel();
    daiCurrencyField daiCurrency_newTaxes = new daiCurrencyField();
    daiCurrencyField daiCurrency_newShipping = new daiCurrencyField();
    daiLabel daiLabel_trackingNum = new daiLabel();
    daiTextField daiTextField_trackingNum = new daiTextField();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();

    public UpdateShipmentSelectionPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public UpdateShipmentSelectionPanel(JFrame container, UpdateShipmentFrame containerFrame)
    {
        CONTAINER = container;
        _CONTAINER_FRAME = containerFrame;

        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        _logger = Logger.getInstance();
        sessionMeta = SessionMetaData.getInstance();
        dbAdapterFactory = csDBAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();

        xYLayout1.setHeight(265);
        xYLayout1.setWidth(567);

        daiTextField_custId.setDisabled(true);
        daiTextField_custName.setDisabled(true);
        daiCurrency_amtDue.setDisabled(true);
        daiCurrency_shipCharges.setDisabled(true);
        daiCurrency_taxes.setDisabled(true);
        daiCurrency_subtotal.setDisabled(true);
        daiLabel_shipmentId.setHREFstyle(true);
        daiLabel_shipmentId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_shipmentId_daiAction(e);
            }
        });

        userIdDateCreatedPanel.setDateCreated();
        userIdDateCreatedPanel.setUserId(sessionMeta.getUserId());

        daiLabel_amtDue.setFont(new java.awt.Font("Dialog", 1, 11));
        this.setLayout(xYLayout1);
        daiTextField_shipmentId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e)
            {
                daiTextField_shipmentId_keyPressed(e);
            }
            });        
        daiLabel_custId.setText("Customer:");
        daiLabel_origAmt.setText("Subtotal:");
        daiLabel_shipCharges.setText("Shipping Charges:");
        daiLabel_taxes.setText("Total Taxes:");
        groupBox1.setLabel("Shipment Updates");
        groupBox1.setLayout(xYLayout2);
        daiLabel_newShipping.setText("Addl. Shipping Charges:");
        daiLabel_newTaxes.setText("Addl. Taxes:");
        daiLabel_trackingNum.setText("Tracking Num:");
        jLabel1.setFont(new java.awt.Font("Dialog", 2, 11));
        jLabel1.setText("Note: Negative amounts can be used to reduce total");
        jLabel2.setFont(new java.awt.Font("Dialog", 2, 11));
        jLabel2.setText("Shipping Charges or total Taxes.");
        this.add(userIdDateCreatedPanel, new XYConstraints(376, 9, -1, -1));
        this.add(daiLabel_shipmentId, new XYConstraints(84, 50, -1, -1));
        this.add(daiTextField_shipmentId, new XYConstraints(134, 48, -1, -1));
        this.add(daiTextField_custName, new XYConstraints(236, 77, 275, -1));
        this.add(daiTextField_custId, new XYConstraints(134, 77, -1, -1));
        this.add(daiLabel_custId, new XYConstraints(78, 79, -1, -1));
        this.add(daiLabel_origAmt, new XYConstraints(85, 111, -1, -1));
        this.add(daiCurrency_subtotal, new XYConstraints(134, 106, -1, -1));
        this.add(daiCurrency_taxes, new XYConstraints(134, 135, -1, -1));
        this.add(daiLabel_taxes, new XYConstraints(68, 139, -1, -1));
        this.add(daiLabel_shipCharges, new XYConstraints(32, 164, 95, -1));
        this.add(daiCurrency_shipCharges, new XYConstraints(134, 164, -1, -1));
        this.add(daiLabel_amtDue, new XYConstraints(47, 193, -1, -1));
        this.add(daiCurrency_amtDue, new XYConstraints(134, 193, -1, -1));
        this.add(groupBox1, new XYConstraints(238, 104, 328, 134));
        groupBox1.add(daiCurrency_newTaxes, new XYConstraints(114, 0, -1, -1));
        groupBox1.add(daiCurrency_newShipping, new XYConstraints(114, 24, -1, -1));
        groupBox1.add(daiTextField_trackingNum, new XYConstraints(114, 47, 186, -1));
        groupBox1.add(daiLabel_trackingNum, new XYConstraints(41, 49, -1, -1));
        groupBox1.add(daiLabel_newTaxes, new XYConstraints(48, 0, -1, -1));
        groupBox1.add(daiLabel_newShipping, new XYConstraints(-7, 26, -1, -1));
        groupBox1.add(jLabel1, new XYConstraints(-4, 70, 269, 21));
        groupBox1.add(jLabel2, new XYConstraints(25, 85, -1, -1));

        daiTextField_shipmentId.requestFocus();
    }

    public void resetPanel() {
        daiCurrency_amtDue.setText(null);
        daiCurrency_newShipping.setText(null);
        daiCurrency_newTaxes.setText(null);
        daiCurrency_shipCharges.setText(null);
        daiCurrency_subtotal.setText(null);
        daiCurrency_taxes.setText(null);
        daiTextField_custId.setText(null);
        daiTextField_custName.setText(null);
        daiTextField_shipmentId.setText(null);
        daiTextField_trackingNum.setText(null);
        userIdDateCreatedPanel.setDateCreated();
        userIdDateCreatedPanel.setUserId(sessionMeta.getUserId());
        daiTextField_shipmentId.requestFocus();
    }

    public boolean panelDataIsValid()
    {
        //Must enter a valid Shipment Id.
        if (daiTextField_shipmentId.getText() == null) {
            //Let the user know that the Shipment Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Must enter an Invoice#.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        return true;
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }

    public String getSelectedShipment() {
        return daiTextField_shipmentId.getText();
    }

    public String getNewShipCharges() {
        return daiCurrency_newShipping.getText();
    }

    public String getNewTaxCharges() {
        return daiCurrency_newTaxes.getText();
    }

    public String getTrackingNum() {
        return daiTextField_trackingNum.getText();
    }

    public String getCustName() {
        return daiTextField_custName.getText();
    }

    void daiLabel_shipmentId_daiAction(daiActionEvent e)
    {
		shipmentObj tempObj = new shipmentObj();
        String id = daiTextField_shipmentId.getText();

        DBAttributes attrib1 = new DBAttributes(shipmentObj.ID, id, "Shipment Id", 100);
        DBAttributes attrib2 = new DBAttributes(shipmentObj.CUSTOMER_NAME, "", "Customer Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        shipmentObj chosenObj = (shipmentObj)chooser.getChosenObj();
        if (chosenObj != null) {
            populatePanel(chosenObj);
        }
    	chooser.dispose();
    }

    void daiTextField_shipmentId_keyPressed(FocusEvent e)
    {
        if (e.isTemporary()) {
            return;
        }
		String id = daiTextField_shipmentId.getText();

		if (id != null)
		{
    		String exp = " locality = " + "'" + shipmentObj.getObjLocality() + "'" +
                        " and id = '" + id + "' ";
            try {
                Vector shipObjsVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                                                new shipmentObj(),
                                                                exp);
                //Does the input Invoice exist?
                if (shipObjsVect.size() == 0) {
                    //Let the user know that the Shipment Does not exist.
                    JOptionPane.showMessageDialog(this  ,
                                        "Selected Shipment Does Not Exist.  Please try another.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);

                    //Clear out the entry field.
                    daiTextField_shipmentId.setText(null);
                } else {
                    shipmentObj shipObj = (shipmentObj)shipObjsVect.firstElement();
                    populatePanel(shipObj);
                }
            } catch (Exception ex) {
                _logger.logError(CONTAINER, "Can't Get Shipment Data.\n" + ex.getLocalizedMessage());
            }
		}
    }

    private void populatePanel(shipmentObj obj) {
        daiTextField_shipmentId.setText(obj.get_id());
        daiTextField_custId.setText(obj.get_customer_id());
        daiTextField_custName.setText(obj.get_customer_name());
        daiTextField_trackingNum.setText(obj.get_air_bill_num());

        daiCurrency_subtotal.setText(obj.get_subtotal());
        daiCurrency_taxes.setText(obj.get_total_tax());
        daiCurrency_shipCharges.setText(obj.get_total_shipping());
        daiCurrency_amtDue.setText(obj.get_total_value());

        String s_subtot = daiCurrency_subtotal.getText();
        String s_taxes = daiCurrency_taxes.getText();
        String s_shipping = daiCurrency_shipCharges.getText();
        if (s_subtot == null) s_subtot = "0";
        if (s_taxes == null) s_taxes = "0";
        if (s_shipping == null) s_shipping = "0";

         //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText(": " + obj.get_id());
        }
    }
}
