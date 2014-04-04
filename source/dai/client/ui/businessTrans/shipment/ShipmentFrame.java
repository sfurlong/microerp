//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;


import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiFrameMenuBar;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.businessObjs.shipment_itemObj;
import dai.shared.businessObjs.shipment_journalObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class ShipmentFrame extends daiFrame
{
    ShipmentHeaderPanel         headerTab;
    ShipmentItemPanel           itemTab;
    ShipmentShipToBillToPanel   customerTab;
    ShipmentJournalPanel        journalTab;
    ShipmentDocNotesPanel       docNotesTab;

    shipmentObj       headerObj;
    shipment_itemObj  itemObj;
    shipment_journalObj journalObj;

    SessionMetaData sessionMeta;

    MenuActionListener menuListener = new MenuActionListener();

    public ShipmentFrame(JFrame container)
    {
        super (container, new shipmentObj());

        //Let's get our own local copy of the business objects.
        headerObj = new shipmentObj();
        itemObj   = new shipment_itemObj();
        journalObj= new shipment_journalObj();

        //Set the title.
        this.setTitle("Shipment Entry/Update");
        this.setBannerLeftText("Shipment Entry/Update");

        try
        {
            jbInit();

            //Initialize the query parameter for the listbox.
            pack();

        } catch (Exception ex)
        {
            LOGGER.logError(CONTAINER, "Could not initialize Frame.\n" + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    void jbInit() throws Exception
    {
        sessionMeta = SessionMetaData.getInstance();

        headerTab   = new ShipmentHeaderPanel(CONTAINER, this, headerObj);
        itemTab     = new ShipmentItemPanel(CONTAINER, this, itemObj);
        customerTab = new ShipmentShipToBillToPanel(CONTAINER, this, headerObj);
        journalTab  = new ShipmentJournalPanel(CONTAINER, this, journalObj);
        docNotesTab = new ShipmentDocNotesPanel(CONTAINER, this, headerObj);

        //Add the customer panel as a subpanel to the Header panel
        headerTab.addSubPanel(customerTab);
        headerTab.addSubPanel(docNotesTab);

        //Let our Ansestor know about the Panels.
        addTabPanel("Summary", headerTab);
        addTabPanel("Items", itemTab);
        addTabPanel("Customer", customerTab);
        addTabPanel("Doc Notes", docNotesTab);
        addTabPanel("Journal", journalTab);

        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                          new DBAttributes("customer_id","Customer ID",100),
                                          new DBAttributes("customer_name","Customer Name",200),
                                          null,
                                          new DBAttributes("item_id","Item ID",100),
                                          null,
                                          new DBAttributes("date_created","Date Created On",100),
                                          new DBAttributes("created_by","Created By",100),
                                          null,
                                          new DBAttributes("po_num","PO Number",100),
                                          new DBAttributes("order_num", "Order Number", 100)});


        JMenuItem custMenuItem = new JMenuItem("Customer...");
        custMenuItem.setMnemonic('C');
        custMenuItem.addActionListener(menuListener);

        JMenuItem carrMenuItem = new JMenuItem("Carrier...");
        carrMenuItem.setMnemonic('A');
        carrMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
        carrMenuItem.addActionListener(menuListener);

        JMenuItem itemMenuItem = new JMenuItem("Item...");
        itemMenuItem.setMnemonic('I');
        itemMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, Event.CTRL_MASK));
        itemMenuItem.addActionListener(menuListener);

        JMenuItem ordMenuItem = new JMenuItem("Order...");
        ordMenuItem.setMnemonic('O');
        ordMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
        ordMenuItem.addActionListener(menuListener);

        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(custMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(carrMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(itemMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(ordMenuItem);

        JMenuItem salesInvMenuItem = new JMenuItem("Sales Invoice...");
        salesInvMenuItem.addActionListener(menuListener);
        JMenuItem packSlipMenuItem = new JMenuItem("Packing Slip...");
        packSlipMenuItem.addActionListener(menuListener);
        JMenuItem credMemoMenuItem = new JMenuItem("Credit Memo...");
        credMemoMenuItem.addActionListener(menuListener);

        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).add(salesInvMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).add(packSlipMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).add(credMemoMenuItem);
    }

    //Do this so other tabs can find out if the shipment is
    //actually a credit memo.  For example the item tab
    //needs to know this so it dosn't try to adjust inventory.
    public boolean isCreditMemo() {
        return headerTab.isCreditMemo();
    }

    //Override from base class so we can tell when tabs change.
    protected void TabbedPane_stateChanged(ChangeEvent e)
    {
        if (IS_ACTIVE)
        {
            headerTab.setTransSubtotal(itemTab.getItemSubtotal());
        }
    }

    //Override from base class so we can extend the logic.
    //Specificaly update the Dollar totals for the order
    //based on the value of the subtotal from the item panel.
    protected void fileSave()
    {
        headerTab.setTransSubtotal(itemTab.getItemSubtotal());
        //Call the same method in the base class to do the
        //rest of the regular save logic.
        super.fileSave();
    }

    //Override from base class so we can extend the logic.
    //Specificaly delete all the updates that were made the the
    //GL and inventory for this specific Shipment.
    protected void fileDelete()
    {
        int ret = JOptionPane.showConfirmDialog(this,"Are your sure you want to permanently remove this Record?\n"+
                                                  "Note:  This will remove all related GL entries and re-allocate inventory.",
                                                  "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
                                                  JOptionPane.WARNING_MESSAGE, null);

        if (ret != JOptionPane.OK_OPTION)
        {
            return;
        }

        //Get the shipment ID
        ShipmentHeaderPanel panel = (ShipmentHeaderPanel)getTabPanel(0);
        String shipmentId = panel.getTransId();

        csShipmentAdapterFactory shipAdapterFactory = csShipmentAdapterFactory.getInstance();
        csShipmentAdapter shipAdapter = shipAdapterFactory.getShipmentAdapter();
        try
        {
            shipAdapter.deleteShipment(sessionMeta.getClientServerSecurity(), shipmentId);
        } catch (Exception e)
        {
            e.printStackTrace();
            String msg = this.getClass().getName() + "::fileDelete failure\n" +
                         e.getClass().getName() + "\n" +
                         e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
            return;
        }

        headerTab.setIsCanceled();
        fileSave();

        //Update the statusbar
        statusBar.setLeftStatus("Deleted");
    }

    class MenuActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            String menuAction = e.getActionCommand();
            if (menuAction.equals("Customer..."))
            {
                ShipmentHeaderPanel panel = (ShipmentHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.customer.customerFrame", panel.getCustId());
            } else if (menuAction.equals("Item..."))
            {
                ShipmentItemPanel panel = (ShipmentItemPanel)getTabPanel(1);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.item.ItemFrame", panel.getItemId());
            } else if (menuAction.equals("Order..."))
            {
                ShipmentHeaderPanel panel = (ShipmentHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.businessTrans.order.OrderFrame", panel.getSourceOrdId());
            } else if (menuAction.equals("Carrier..."))
            {
                ShipmentHeaderPanel panel = (ShipmentHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.carrier.carrierFrame", panel.getCarrierId());
            } else if (menuAction.equals("Sales Invoice..."))
            {
                ShipmentHeaderPanel panel = (ShipmentHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.docGen.PrintSalesInvoiceDoc",panel.getTransId());
            } else if (menuAction.equals("Packing Slip..."))
            {
                ShipmentHeaderPanel panel = (ShipmentHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.docGen.PrintPackSlipDoc",panel.getTransId());
            } else if (menuAction.equals("Credit Memo..."))
            {
                ShipmentHeaderPanel panel = (ShipmentHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.docGen.PrintCreditMemoDoc",panel.getTransId());
            }
        }
    }
}

