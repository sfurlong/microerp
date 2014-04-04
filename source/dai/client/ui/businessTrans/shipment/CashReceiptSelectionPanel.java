
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.cust_orderObj;
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
import daiBeans.daiRadioButton;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;

public class CashReceiptSelectionPanel extends daiWizardPanel
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
    daiCurrencyField daiCurrency_origAmt = new daiCurrencyField();
    daiCurrencyField daiCurrency_amtDue = new daiCurrencyField();
    daiCurrencyField daiCurrency_amtPaid = new daiCurrencyField();
    daiLabel daiLabel_custId = new daiLabel("Customer Id:");
    daiLabel daiLabel_shipmentId = new daiLabel("Trans Id#:");
    daiTextField daiTextField_shipmentId = new daiTextField();
    daiLabel daiLabel_origAmt = new daiLabel("Original Inv. Amt:");
    daiLabel daiLabel_amtDue = new daiLabel("Total Amt Due:");
    daiLabel daiLabel_amtPaid = new daiLabel("Total Amt Paid:");
    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();
    daiRadioButton      orderRadioButton = new daiRadioButton("Pre-paid Order");
    daiRadioButton      invoiceRadioButton = new daiRadioButton("Invoice Payment");
    ButtonGroup         payTypeButtonGroup = new ButtonGroup();

    boolean _isCreditMemo = false;
    JFrame CONTAINER;
    CashReceiptFrame _CONTAINER_FRAME = null;
    GroupBox groupBox1 = new GroupBox();

    public CashReceiptSelectionPanel(JFrame container)
    {
        CONTAINER = container;
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public CashReceiptSelectionPanel(JFrame container, CashReceiptFrame containerFrame)
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

        xYLayout1.setHeight(271);
        xYLayout1.setWidth(567);

        daiTextField_custId.setDisabled(true);
        daiTextField_custName.setDisabled(true);
        daiCurrency_amtDue.setDisabled(true);
        daiCurrency_amtPaid.setDisabled(true);
        daiCurrency_origAmt.setDisabled(true);
        daiLabel_shipmentId.setHREFstyle(true);
        daiLabel_shipmentId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_shipmentId_daiAction(e);
            }
        });

        groupBox1.setLabel("Payment Type");
        invoiceRadioButton.setSelected(true);
        payTypeButtonGroup.add(invoiceRadioButton);
        payTypeButtonGroup.add(orderRadioButton);

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
        this.add(userIdDateCreatedPanel, new XYConstraints(376, 9, -1, -1));
        this.add(daiLabel_amtDue, new XYConstraints(48, 192, -1, -1));
        this.add(daiLabel_shipmentId, new XYConstraints(85, 73, -1, -1));
        this.add(daiLabel_custId, new XYConstraints(68, 102, -1, -1));
        this.add(daiLabel_origAmt, new XYConstraints(47, 133, -1, -1));
        this.add(daiLabel_amtPaid, new XYConstraints(57, 162, -1, -1));
        this.add(daiTextField_shipmentId, new XYConstraints(135, 71, -1, -1));
        this.add(daiTextField_custId, new XYConstraints(135, 101, -1, -1));
        this.add(daiCurrency_origAmt, new XYConstraints(135, 132, -1, -1));
        this.add(daiCurrency_amtPaid, new XYConstraints(135, 162, -1, -1));
        this.add(daiCurrency_amtDue, new XYConstraints(135, 192, -1, -1));
        this.add(groupBox1, new XYConstraints(23, 8, -1, 57));
        groupBox1.add(invoiceRadioButton, null);
        groupBox1.add(orderRadioButton, null);
        this.add(daiTextField_custName, new XYConstraints(237, 101, 280, -1));

        daiTextField_shipmentId.requestFocus();
    }

    public boolean panelDataIsValid()
    {
        //Must enter a valid Shipment Id.
        if (daiTextField_shipmentId.getText().trim().length() == 0) {
            //Let the user know that the Shipment Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Must enter an Invoice#.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        //Make sure the amount due is correct.
        double amtDue = Double.parseDouble(daiCurrency_amtDue.getText());
        if (amtDue == 0) {
            //Let the user know that the Shipment Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Can't have an Amount Due of 0.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        if (amtDue < 0 && !_isCreditMemo) {
            //Let the user know that the Shipment Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Only Credit Memo's can have a negative Amount Due.",
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

    public boolean isOrderPrePayment() {
        return orderRadioButton.isSelected();
    }

    public String getSelectedShipment()
    {
        return daiTextField_shipmentId.getText();
    }

    public String getCustId() {
        return daiTextField_custId.getText();
    }

    public String getCustName() {
        return daiTextField_custName.getText();
    }

    public void resetPanel() {
        daiCurrency_amtDue.setText(null);
        daiCurrency_amtPaid.setText(null);
        daiCurrency_origAmt.setText(null);
        daiTextField_custId.setText(null);
        daiTextField_shipmentId.setText(null);
        daiTextField_custName.setText(null);

        daiTextField_custId.requestFocus();
    }

    void daiLabel_shipmentId_daiAction(daiActionEvent e)
    {
        if (invoiceRadioButton.isSelected()) {
    		shipmentObj tempObj = new shipmentObj();
            String id = daiTextField_shipmentId.getText();

            DBAttributes attribs1 = new DBAttributes(shipmentObj.ID, id, "Shipment Id", 100);
            DBAttributes attribs2 = new DBAttributes(shipmentObj.CUSTOMER_NAME, "", "Customer Name", 200);
	    	DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
		    chooser.show();
            shipmentObj chosenObj = (shipmentObj)chooser.getChosenObj();
            if (chosenObj != null) {
                populatePanelShipment(chosenObj);
            }
    	    chooser.dispose();
        } else {
    		cust_orderObj tempObj = new cust_orderObj();
            String id = daiTextField_shipmentId.getText();

            DBAttributes attribs1 = new DBAttributes(cust_orderObj.ID, id, "Order Id", 100);
            DBAttributes attribs2 = new DBAttributes(cust_orderObj.CUSTOMER_NAME, "", "Customer Name", 200);
	    	DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
		    chooser.show();
            cust_orderObj chosenObj = (cust_orderObj)chooser.getChosenObj();
            if (chosenObj != null) {
                populatePanelOrder(chosenObj);
            }
    	    chooser.dispose();
        }
    }

    void daiTextField_shipmentId_keyPressed(FocusEvent e)
    {
        if (e.isTemporary())
        {
            return;
        }

		String id = daiTextField_shipmentId.getText();

		if (id != null && id.trim().length() > 0)
		{
            try {
                if (invoiceRadioButton.isSelected()) {
        		    String exp = " locality = " + "'" + shipmentObj.getObjLocality() + "'" +
                        " and id = '" + id + "' ";
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
                        populatePanelShipment(shipObj);
                    }
                } else {
        		    String exp = " locality = " + "'" + cust_orderObj.getObjLocality() + "'" +
                        " and id = '" + id + "' ";
                    Vector orderObjsVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                                                new cust_orderObj(),
                                                                exp);
                    //Does the input Invoice exist?
                    if (orderObjsVect.size() == 0) {
                        //Let the user know that the Shipment Does not exist.
                        JOptionPane.showMessageDialog(this  ,
                                        "Selected Order Does Not Exist.  Please try another.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);

                        //Clear out the entry field.
                        daiTextField_shipmentId.setText(null);
                    } else {
                        cust_orderObj orderObj = (cust_orderObj)orderObjsVect.firstElement();
                        populatePanelOrder(orderObj);
                    }
                }
            } catch (Exception ex) {
                _logger.logError(CONTAINER, "Can't Get Shipment Data.\n" + ex.getLocalizedMessage());
            }
		}
    }

    private void populatePanelShipment(shipmentObj obj) {
        if (obj.get_is_credit_memo() != null && obj.get_is_credit_memo().equals("Y")) {
            _isCreditMemo = true;
        }

        String s_origAmt = obj.get_total_value();
        String s_amtPaid = obj.get_total_cash_received();
        if (s_origAmt == null) s_origAmt = "0";
        if (s_amtPaid == null) s_amtPaid = "0";
        double d_origAmt = Double.parseDouble(s_origAmt);
        double d_amtPaid = Double.parseDouble(s_amtPaid);
        double d_amtDue = d_origAmt - d_amtPaid;

        daiTextField_shipmentId.setText(obj.get_id());
        daiTextField_custId.setText(obj.get_customer_id());
        daiTextField_custName.setText(obj.get_customer_name());
        daiCurrency_amtDue.setText(new Double(d_amtDue).toString());
        daiCurrency_amtPaid.setText(s_amtPaid);
        daiCurrency_origAmt.setText(s_origAmt);

        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText(": " + obj.get_id());
        }
    }

    private void populatePanelOrder(cust_orderObj obj) {
        String s_origAmt = obj.get_total_value();
        String s_amtPaid = null;//obj.get_total_cash_received();
        if (s_origAmt == null) s_origAmt = "0";
        if (s_amtPaid == null) s_amtPaid = "0";
        double d_origAmt = Double.parseDouble(s_origAmt);
        double d_amtPaid = Double.parseDouble(s_amtPaid);
        double d_amtDue = d_origAmt - d_amtPaid;

        daiTextField_shipmentId.setText(obj.get_id());
        daiTextField_custId.setText(obj.get_customer_id());
        daiTextField_custName.setText(obj.get_customer_name());
        daiCurrency_amtDue.setText(new Double(d_amtDue).toString());
        daiCurrency_amtPaid.setText(s_amtPaid);
        daiCurrency_origAmt.setText(s_origAmt);

        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText(": " + obj.get_id());
        }
    }
}
