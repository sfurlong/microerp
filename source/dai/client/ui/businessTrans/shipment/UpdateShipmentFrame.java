
//Title:        order
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI for Entry/Update of Orders

package dai.client.ui.businessTrans.shipment;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dai.client.clientShared.daiWizardFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class UpdateShipmentFrame extends daiWizardFrame
{
    UpdateShipmentSelectionPanel shipSelectPanel;
    UpdateShipmentAcctsPanel acctsPanel = new UpdateShipmentAcctsPanel();

    Logger LOGGER;
    SessionMetaData _sessionMeta;
    int _currentPanelNum = 0;
    boolean _isStandAloneApp = false;

    public UpdateShipmentFrame(JFrame container)
    {
        super(container);
        try
        {
            shipSelectPanel = new UpdateShipmentSelectionPanel(container,this);
            jbInit();
        }
        catch(Exception ex)
        {
            LOGGER.logError(container, ex.getLocalizedMessage());
        }
    }

    private void jbInit() throws Exception
    {
        LOGGER = LOGGER.getInstance();
        _sessionMeta = _sessionMeta.getInstance();

//        this.addWindowListener(new java.awt.event.WindowAdapter()
//        {
//            public void windowClosed(WindowEvent e)
//            {
//                //Exit only if this is running as a stand alone application.
//                if (_isStandAloneApp) {
//                    System.exit(0);
//                }
//            }
//
//            public void windowClosing(WindowEvent e)
//            {
//                //Exit only if this is running as a stand alone application.
//                if (_isStandAloneApp) {
//                    System.exit(0);
//                }
//            }
//        });

        //Update the Banner Panel
        bannerPanel.setBannerLeftText("Update Shipment Charges Wizard");
        this.setTitle("Update Shipment Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(shipSelectPanel, "shipSelectPanel"),
                                        new WizardPanel(acctsPanel, "acctsPanel"),
                                        };
        CONTENT_PANEL.add(wizardPanels[0].panelRef, wizardPanels[0].panelName);
        CONTENT_PANEL.add(wizardPanels[1].panelRef, wizardPanels[1].panelName);

        centerFrame();
        show();
    }

    //This is used so that we will know wether this frame is called as
    //a standalone application or as a module in the main app.
    public void setIsStandAloneApp(boolean flag) {
        _isStandAloneApp = flag;
    }

    protected void button_cancel_actionPerformed()
    {
//        this.dispose();
          //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            shipSelectPanel.resetPanel();
    }

    protected void button_prev_actionPerformed()
    {
        if (_currentPanelNum > 0) {
            _currentPanelNum--;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        } else {
            button_prev.setEnabled(false);
        }
    }

    protected void button_next_actionPerformed()
    {
        //If the data on the current panel is valid we can move to the
        //next panel.
        if (wizardPanels[_currentPanelNum].panelRef.panelDataIsValid())
        {
            button_prev.setEnabled(true);
            _currentPanelNum++;
            if (_currentPanelNum == 1) {
                button_post.setEnabled(true);
                button_next.setEnabled(false);
            }
            wizardPanels[_currentPanelNum].panelRef.doPreDisplayProcessing(null);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        }
    }

    protected void button_post_actionPerformed()
    {
        try {
            csShipmentAdapterFactory shipmentAdapterFactory = csShipmentAdapterFactory.getInstance();
            csShipmentAdapter shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();

            DBRecSet updateData = new DBRecSet();
            DBRec attribs = new DBRec();

            attribs.addAttrib(new DBAttributes(shipmentObj.ID, shipSelectPanel.getSelectedShipment()));
            attribs.addAttrib(new DBAttributes(shipmentObj.TOTAL_TAX, shipSelectPanel.getNewTaxCharges()));
            attribs.addAttrib(new DBAttributes(shipmentObj.TOTAL_SHIPPING, shipSelectPanel.getNewShipCharges()));
            attribs.addAttrib(new DBAttributes(shipmentObj.AIR_BILL_NUM, shipSelectPanel.getTrackingNum()));
            attribs.addAttrib(new DBAttributes(shipmentObj.CUSTOMER_NAME, shipSelectPanel.getCustName()));
            attribs.addAttrib(new DBAttributes(default_accountsObj.SHIPPING_OUT_ID, acctsPanel.getShippingAcctId()));
            attribs.addAttrib(new DBAttributes(default_accountsObj.ACCTS_RECEIVABLE_ID, acctsPanel.getReceivableAcctId()));
            attribs.addAttrib(new DBAttributes(default_accountsObj.SALES_TAX_PAYABLE_ID, acctsPanel.getTaxAcctId()));

            updateData.addRec(attribs);

            //The service will exept the following attributes
            //shipmentObj.SHIPMENT_ID
            //shipmentObj.TOTAL_TAX
            //shipmentObj.TOTAL_SHIPPING
            //shipmentObj.AIR_BILL_NUM
            //shipmentObj.CUSTOMER_NAME
            //default_accountsObj.SHIPPING_OUT_ID
            //default_accountsObj.ACCTS_RECEIVABLE_ID
            //default_accountsObj.SALES_TAX_PAYABLE_ID
            shipmentAdapter.updateShipmentCharges(_sessionMeta.getClientServerSecurity(),
                                                updateData);

            //Let the user know that the Shipment was created.
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(  this,
                                        "Shipment Updates Posted",
                                        "Shipment Updates Posted",
                                        JOptionPane.INFORMATION_MESSAGE, null);

            //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            shipSelectPanel.resetPanel();

        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not post shipment updates.\n"+ex.getLocalizedMessage());
        }
    }
}

