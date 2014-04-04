
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.awt.Cursor;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

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

public class CreateCreditMemoSelectPanel extends daiWizardPanel
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
    daiCurrencyField daiCurrency_amtPaid = new daiCurrencyField();
    daiLabel daiLabel_custId = new daiLabel("Customer Id:");
    daiLabel daiLabel_shipmentId = new daiLabel("Invoice#:");
    daiTextField daiTextField_shipmentId = new daiTextField();
    daiLabel daiLabel_origAmt = new daiLabel("Original Inv. Amt:");
    daiLabel daiLabel_amtPaid = new daiLabel("Total Amt Paid:");
    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();

    CashReceiptFrame _CONTAINER_FRAME = null;
    JFrame CONTAINER = null;
    public CreateCreditMemoSelectPanel(JFrame container)
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

    public CreateCreditMemoSelectPanel(JFrame container, CashReceiptFrame containerFrame)
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

        xYLayout1.setHeight(194);
        xYLayout1.setWidth(500);

        daiTextField_custId.setDisabled(true);
        daiTextField_custName.setDisabled(true);
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

        userIdDateCreatedPanel.setDateCreated();
        userIdDateCreatedPanel.setUserId(sessionMeta.getUserId());

        this.setLayout(xYLayout1);
        daiTextField_shipmentId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e)
            {
                daiTextField_shipmentId_keyPressed(e);
            }
            });        
        this.add(userIdDateCreatedPanel, new XYConstraints(329, 11, -1, -1));
        this.add(daiTextField_custId, new XYConstraints(123, 86, -1, -1));
        this.add(daiLabel_shipmentId, new XYConstraints(73, 58, -1, -1));
        this.add(daiTextField_shipmentId, new XYConstraints(123, 56, -1, -1));
        this.add(daiLabel_custId, new XYConstraints(56, 87, -1, -1));
        this.add(daiTextField_custName, new XYConstraints(225, 86, 267, -1));
        this.add(daiCurrency_origAmt, new XYConstraints(123, 117, -1, -1));
        this.add(daiLabel_origAmt, new XYConstraints(35, 118, -1, -1));
        this.add(daiLabel_amtPaid, new XYConstraints(45, 147, -1, -1));
        this.add(daiCurrency_amtPaid, new XYConstraints(123, 147, -1, -1));
    }
    public void resetPanel() {

    	//Enable the Trans ID text field.
    	daiTextField_shipmentId.setEnabled(true);
    	
        daiTextField_custId.setText(null);
        daiTextField_shipmentId.setText(null);
        daiTextField_custName.setText(null);
        daiCurrency_origAmt.setText(null);
        daiCurrency_amtPaid.setText(null);

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

    void daiLabel_shipmentId_daiAction(daiActionEvent e)
    {
		shipmentObj tempObj = new shipmentObj();
        String id = daiTextField_shipmentId.getText();

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DBAttributes attribs1 = new DBAttributes(shipmentObj.ID, id, "Shipment Id", 100);
        DBAttributes attribs2 = new DBAttributes(shipmentObj.CUSTOMER_NAME, "", "Customer Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		chooser.setVisible(true);
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
        			//Disable the Trans ID text field.
		        	daiTextField_shipmentId.setEnabled(false);
                    shipmentObj shipObj = (shipmentObj)shipObjsVect.firstElement();
                    populatePanel(shipObj);
                }
            } catch (Exception ex) {
                _logger.logError(CONTAINER, "Can't Get Shipment Data.\n" + ex.getLocalizedMessage());
            }
		}
    }

    private void populatePanel(shipmentObj obj) {
        String s_origAmt = obj.get_total_value();
        String s_amtPaid = obj.get_total_cash_received();
        if (s_origAmt == null) s_origAmt = "0";
        if (s_amtPaid == null) s_amtPaid = "0";
        double d_origAmt = Double.parseDouble(s_origAmt);
        double d_amtPaid = Double.parseDouble(s_amtPaid);

        daiTextField_shipmentId.setText(obj.get_id());
        daiTextField_custId.setText(obj.get_customer_id());
        daiTextField_custName.setText(obj.get_customer_name());
        daiCurrency_amtPaid.setText(s_amtPaid);
        daiCurrency_origAmt.setText(s_origAmt);

        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText(": " + obj.get_id());
        }
    }
}
