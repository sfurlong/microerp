//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;


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
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.businessObjs.purch_order_itemObj;
import dai.shared.businessObjs.purch_order_journalObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;

public class PurchOrderFrame extends daiFrame
{
	PurchOrderHeaderPanel   headerTab;
	PurchOrderItemPanel     itemTab;
	PurchOrderVendorPanel   vendorTab;
	PurchOrderShipToPanel   shiptoTab;
	PurchOrderBillToPanel   billtoTab;
    PurchOrderJournalPanel  journalTab;
    PurchOrderDocNotesPanel docNotesTab;
    PurchOrderReportsPanel reportsTab;

	purch_orderObj       headerObj;
	purch_order_itemObj  itemObj;
    purch_order_journalObj journalObj;

	SessionMetaData sessionMeta;

    MenuActionListener menuListener = new MenuActionListener();

	public PurchOrderFrame(JFrame container)
	{
		super (container, new purch_orderObj());

		//Let's get our own local copy of the partnerObj.
		headerObj = new purch_orderObj();
		itemObj   = new purch_order_itemObj();
        journalObj= new purch_order_journalObj();

		//Set the title.
		this.setTitle("Purchase Order Entry/Update");
        this.setBannerLeftText("Purchase Order Entry/Update");

		try
		{
			jbInit();
			pack();

            //Default focus to the first tab and the first entry field
            setFrameDefaultFocus();

		} catch (Exception ex)
		{
    		LOGGER.logError(container, "Could not initialize Frame.\n" + ex.getLocalizedMessage());
    		ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
        sessionMeta = SessionMetaData.getInstance();

		headerTab   = new PurchOrderHeaderPanel(CONTAINER, this, headerObj);
		itemTab     = new PurchOrderItemPanel(CONTAINER, this, itemObj);
        vendorTab   = new PurchOrderVendorPanel(CONTAINER, this, headerObj);
        shiptoTab   = new PurchOrderShipToPanel(CONTAINER, this, headerObj);
        billtoTab   = new PurchOrderBillToPanel(CONTAINER, this, headerObj);
        docNotesTab = new PurchOrderDocNotesPanel(CONTAINER, this, headerObj);
        journalTab  = new PurchOrderJournalPanel(CONTAINER, this, journalObj);
        reportsTab  = new PurchOrderReportsPanel(CONTAINER, this, headerObj);

        //Add the customer panel as a subpanel to the Header panel
        headerTab.addSubPanel(vendorTab);
        headerTab.addSubPanel(shiptoTab);
        headerTab.addSubPanel(billtoTab);
        headerTab.addSubPanel(docNotesTab);
        headerTab.addSubPanel(reportsTab);

		//Let our Ansestor know about the Panels.
		addTabPanel("Summary", headerTab);
		addTabPanel("Items", itemTab);
		addTabPanel("Vendor", vendorTab);
		addTabPanel("Ship To", shiptoTab);
		addTabPanel("Bill To", billtoTab);
		addTabPanel("Doc Notes", docNotesTab);
        addTabPanel("Journal", journalTab);
        addTabPanel("Reports",reportsTab);

        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("cust_name","Customer Name",100),
                                                          new DBAttributes("cust_id","Customer ID",75),
                                                          null,
                                                          new DBAttributes("vendor_id","Vendor ID",75),
                                                          new DBAttributes("vendor_name","Vendor Name",100),
                                                          null,
                                                          new DBAttributes("shipto_id","Ship To ID",75),
                                                          new DBAttributes("shipto_name","Ship To Name",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",75),
                                                          new DBAttributes("created_by","Created By",100)});

        JMenuItem vendorMenuItem = new JMenuItem("Vendor...");
        vendorMenuItem.setMnemonic('V');
        vendorMenuItem.addActionListener(menuListener);

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

        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(vendorMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(custMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(carrMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(itemMenuItem);

        JMenuItem purchOrdMenuItem = new JMenuItem("Purch Order...");
        purchOrdMenuItem.addActionListener(menuListener);

        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).add(purchOrdMenuItem);

        setTabsEnabled(false);

	}
        //Overided from the base tab so that the report tab is enabled always
        public void setTabsEnabled(boolean flag) {

            //Loop through all the TabPanels and disable them.
            for (int i = 1;i < TAB_PANELS.size()-1; i++)
                {
                _tabbedPane.setEnabledAt(i+1, flag);
            }
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
    //GL and inventory for this specific purchase order.
	protected void fileDelete()
	{
        int ret = JOptionPane.showConfirmDialog(this,"Are your sure you want to permanently cancel this Purchase Order?\n"+
                                                "Note:  This will remove all related GL entries and re-allocate inventory.",
                                                "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
                                                JOptionPane.WARNING_MESSAGE, null);

        if (ret != JOptionPane.OK_OPTION) {
            return;
    	}

        //Get the shipment ID
        PurchOrderHeaderPanel panel = (PurchOrderHeaderPanel)getTabPanel(0);
        String purchOrdId = panel.getTransId();

        csPurchOrderAdapter poAdapter = csPurchOrderAdapterFactory.getInstance().getPurchOrderAdapter();
        try {
            poAdapter.cancelPurchaseOrder(sessionMeta.getClientServerSecurity(), purchOrdId);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = this.getClass().getName() + "::fileDelete failure\n" +
                        e.getClass().getName() + "\n" +
                        e.getLocalizedMessage();
			LOGGER.logError(CONTAINER, msg);
            return;
        }

        fileSave();

        //Update the statusbar
        statusBar.setLeftStatus("Canceled");
    }

	//Overriden from base class so we can extend the logic.
    //This is used to generate a new unique Id for
    //the transaction managed by this frame
    protected String generateNewUniqueId()
    {
        String  ret;
        int     newSeq = 0;

        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
        SessionMetaData sessionMeta = SessionMetaData.getInstance();

        try {
            newSeq = dbAdapter.getNewSequenceNum(sessionMeta.getClientServerSecurity(),
                                    csDBAdapter.SEQUENCE_PURCH_ORDER);
        } catch (Exception e) {
			e.printStackTrace();
            String msg = this.getClass().getName() + "::generateNewUniqueId failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
        }

        ret = daiFormatUtil.padIntLeft(newSeq, 5);

        return ret;
    }

    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String menuAction = e.getActionCommand();
            if (menuAction.equals("Vendor...")) {
                PurchOrderHeaderPanel panel = (PurchOrderHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.vendor.vendorFrame", panel.getVendorId());
            } else if (menuAction.equals("Customer...")) {
                PurchOrderHeaderPanel panel = (PurchOrderHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.customer.customerFrame", panel.getCustId());
            } else if (menuAction.equals("Item...")) {
                PurchOrderItemPanel panel = (PurchOrderItemPanel)getTabPanel(1);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.item.ItemFrame", panel.getItemId());
            } else if (menuAction.equals("Carrier...")) {
                PurchOrderHeaderPanel panel = (PurchOrderHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.carrier.carrierFrame", panel.getCarrierId());
            } else if (menuAction.equals("Purch Order...")) {
                PurchOrderHeaderPanel panel = (PurchOrderHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.docGen.PrintPurchOrderDoc",panel.getTransId());
            }
        }
    }
}

