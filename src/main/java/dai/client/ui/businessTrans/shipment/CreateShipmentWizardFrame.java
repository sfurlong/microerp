
//Title:        order
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  UI for Entry/Update of Orders

package dai.client.ui.businessTrans.shipment;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dai.client.clientShared.daiWizardFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.cust_orderObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class CreateShipmentWizardFrame extends daiWizardFrame
{
    CreateShipmentOrderSelectionPanel orderSelectionPanel = new CreateShipmentOrderSelectionPanel();
    CreateShipmentItemSelectionPanel  itemSelectionPanel = new CreateShipmentItemSelectionPanel();
    CreateShipmentChargesPanel  chargesPanel = new CreateShipmentChargesPanel();
    CreateShipmentAcctsPanel  acctsPanel = new CreateShipmentAcctsPanel();

    int _currentPanelNum = 0;
    Logger LOGGER;
    SessionMetaData _sessionMeta;

    public CreateShipmentWizardFrame(JFrame container)
    {
        super(container);
        try
        {
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

        //Update the Banner Panel
        bannerPanel.setBannerLeftText("Create Shipment Wizard");
        this.setTitle("Create Shipment Wizard");

        //Create the list of wizard panels
        wizardPanels = new WizardPanel[] {
                                        new WizardPanel(orderSelectionPanel, "orderSelectionPanel"),
                                        new WizardPanel(itemSelectionPanel, "itemSelectionPanel"),
                                        new WizardPanel(chargesPanel, "chargesPanel"),
                                        new WizardPanel(acctsPanel, "defaultAccountsPanel"),
                                        };
        CONTENT_PANEL.add(wizardPanels[0].panelRef, wizardPanels[0].panelName);
        CONTENT_PANEL.add(wizardPanels[1].panelRef, wizardPanels[1].panelName);
        CONTENT_PANEL.add(wizardPanels[2].panelRef, wizardPanels[2].panelName);
        CONTENT_PANEL.add(wizardPanels[3].panelRef, wizardPanels[3].panelName);

        CONTENT_PANEL.setPreferredSize(new Dimension(670, 350));
        CONTENT_PANEL.setMinimumSize(new Dimension(670, 350));

        centerFrame();
        show();
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
            orderSelectionPanel.resetPanel();
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
        if (wizardPanels[_currentPanelNum].panelRef.panelDataIsValid())
        {
            button_prev.setEnabled(true);
            _currentPanelNum++;
            if (_currentPanelNum == 3) {
                button_post.setEnabled(true);
                button_next.setEnabled(false);
            }
            if (wizardPanels[_currentPanelNum].panelName.equals("itemSelectionPanel")) {
                String[] ids = orderSelectionPanel.getSelectedOrderIds();
                itemSelectionPanel.updateItemGrid(ids);
            }
            wizardPanels[_currentPanelNum].panelRef.doPreDisplayProcessing(null);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
        }
    }

    protected void button_post_actionPerformed()
    {
        Vector selectedItemObjs = itemSelectionPanel.getSelectedItemsToShip();
        default_accountsObj defaultAcctsObj = acctsPanel.getDefaultAccountObj();

        try {
            csShipmentAdapterFactory shipmentAdapterFactory = csShipmentAdapterFactory.getInstance();
            csShipmentAdapter shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();

            DBRec headerAttribs = chargesPanel.getChargesData();
            if (orderSelectionPanel.isSelectedOrderPrepaid()) {
                headerAttribs.addAttrib(new DBAttributes(cust_orderObj.IS_PREPAID, "Y"));
                System.out.println("prepaid");
            } else {
                System.out.println("NOT prepaid");
                headerAttribs.addAttrib(new DBAttributes(cust_orderObj.IS_PREPAID, "N"));
            }

            //Call the Create Shipment Service.
            String shipmentId = shipmentAdapter.createShipment(_sessionMeta.getClientServerSecurity(),
                                                                headerAttribs,
                                                                selectedItemObjs,
                                                                defaultAcctsObj);

            //Let the user know that the Shipment was created.
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(  this,
                                        "Created new Shipment Id:  " + shipmentId,
                                        "New Shipment Created",
                                        JOptionPane.INFORMATION_MESSAGE, null);

            //Reset the Wizard.
            _currentPanelNum = 0;
            button_post.setEnabled(false);
            button_next.setEnabled(true);
            setActivePanel(_currentPanelNum, wizardPanels[_currentPanelNum].panelName);
            button_prev.setEnabled(false);
            orderSelectionPanel.resetPanel();

        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not create shipment.\n"+ex.getLocalizedMessage());
        }
    }
}


