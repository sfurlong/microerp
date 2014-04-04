//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.order;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;

import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiFrameMenuBar;
import dai.client.clientShared.daiPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.cust_orderObj;
import dai.shared.businessObjs.cust_order_itemObj;
import dai.shared.businessObjs.cust_order_journalObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class OrderFrame extends daiFrame
{
	OrderHeaderPanel        headerTab;
	OrderItemPanel          itemTab;
	OrderShipToBillToPanel  customerTab;
	OrderDocNotesPanel      docNotesTab;
        OrderJournalPanel       journalTab;
        OrderReportPanel       reportsTab;

	cust_orderObj       headerObj;
	cust_order_itemObj  itemObj;
    cust_order_journalObj journalObj;

	SessionMetaData sessionMeta;

	JMenu jMenu_documents = new JMenu();
	JMenuItem jMenuItem_invoiceDoc = new JMenuItem();

    MenuActionListener menuListener = new MenuActionListener();

	public OrderFrame(JFrame container)
	{
		super (container, new cust_orderObj());

		headerObj = new cust_orderObj();
		itemObj   = new cust_order_itemObj();
        journalObj= new cust_order_journalObj();

		//Set the title.
		this.setTitle("Order Entry/Update");
        this.setBannerLeftText("Order Entry/Update");

		try
		{
			jbInit();
			pack();

		} catch (Exception ex)
		{
                System.out.println("Could not initialize Frame.\n" + ex.getLocalizedMessage());
    		LOGGER.logError(CONTAINER, "Could not initialize Frame.\n" + ex.getLocalizedMessage());
    		ex.printStackTrace();
		}
	}

	public OrderFrame(JFrame container, String orderId)
	{
		super (container, new cust_orderObj());

		headerObj = new cust_orderObj();
		itemObj   = new cust_order_itemObj();
        journalObj= new cust_order_journalObj();

		//Set the title.
		this.setTitle("Order Entry/Update");
        this.setBannerLeftText("Order Entry/Update");

		try
		{
			jbInit();
			pack();

            if (orderId != null) {
    			callBackInsertNewId(orderId);
            }

		} catch (Exception ex)
		{
    		LOGGER.logError(CONTAINER, "Could not initialize Frame.\n" + ex.getLocalizedMessage());
    		ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
        sessionMeta = SessionMetaData.getInstance();

		headerTab   = new OrderHeaderPanel(CONTAINER, this, headerObj);
		itemTab     = new OrderItemPanel(CONTAINER, this,  itemObj);
        customerTab = new OrderShipToBillToPanel(CONTAINER, this,  headerObj);
        docNotesTab = new OrderDocNotesPanel(CONTAINER, this,  headerObj);
        journalTab  = new OrderJournalPanel(CONTAINER, this,  journalObj);
        reportsTab = new OrderReportPanel(CONTAINER, this,  headerObj);

        //Add the customer panel as a subpanel to the Header panel
        headerTab.addSubPanel(customerTab);
        headerTab.addSubPanel(docNotesTab);
        headerTab.addSubPanel(reportsTab);

		//Let our Ansestor know about the Panels.
		addTabPanel("Summary", headerTab);
		addTabPanel("Items", itemTab);
		addTabPanel("Customer", customerTab);
        addTabPanel("Doc Notes", docNotesTab);
        addTabPanel("Journal", journalTab);
        addTabPanel("Reports",reportsTab);

        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("customer_name","Customer Name",200),
                                                          new DBAttributes("customer_id","Customer ID",100),
                                                          null,
                                                          new DBAttributes("item_id","Item ID",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",100),
                                                          new DBAttributes("created_by","Created By",100),
                                                          null,
                                                          new DBAttributes("po_num","PO Number",100)});

        //make customer_name the default search-by value
        selectionBox.setComboBoxText("customer_name");

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
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(custMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(carrMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(itemMenuItem);

        JMenuItem ordAckMenuItem = new JMenuItem("Order Acknowledgement...");
        ordAckMenuItem.addActionListener(menuListener);

        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).add(ordAckMenuItem);


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
    //GL and inventory for this specific Shipment.
	protected void fileDelete()
	{
        int ret = JOptionPane.showConfirmDialog(this,"Are your sure you want to permanently remove this Record?\n"+
                                                "Note:  This will re-allocate inventory.",
                                                "Warning", JOptionPane.YES_NO_CANCEL_OPTION,
                                                JOptionPane.WARNING_MESSAGE, null);

        if (ret != JOptionPane.OK_OPTION) {
            return;
    	}

        //Get the Order ID
        OrderHeaderPanel panel = (OrderHeaderPanel)getTabPanel(0);
        String orderId = panel.getTransId();

        csShipmentAdapterFactory shipAdapterFactory = csShipmentAdapterFactory.getInstance();
        csShipmentAdapter shipAdapter = shipAdapterFactory.getShipmentAdapter();
        try {
            shipAdapter.deleteOrder(sessionMeta.getClientServerSecurity(), orderId);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = this.getClass().getName() + "::fileDelete failure\n" +
                        e.getClass().getName() + "\n" +
                        e.getLocalizedMessage();
			LOGGER.logError(CONTAINER, msg);
            return;
        }

		//Loop through all the TabPanels and do the delete.
		for (int i = 0;i < TAB_PANELS.size(); i++)
		{
			daiPanel p = (daiPanel)TAB_PANELS.elementAt(i);

			//The DB should cascade delete.
			p.delete();

			//Refresh all other tabs.
			p.refresh();

            //Deactivate Dirty Panel Checking
            p.disableDirtyFlagChecking();
		}

		IS_ACTIVE = false;

        //Clear the banner panel
        setBannerRightText("");

        //Update the statusbar
        statusBar.setLeftStatus("Deleted");
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
                                    csDBAdapter.SEQUENCE_CUST_ORDER);
        } catch (Exception e) {
			e.printStackTrace();
            String msg = this.getClass().getName() + "::generateNewUniqueId failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
        }
        return daiFormatUtil.padIntLeft(newSeq, 5);
    }

    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String menuAction = e.getActionCommand();
            if (menuAction.equals("Customer...")) {
                OrderHeaderPanel panel = (OrderHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.customer.customerFrame",panel.getCustId());
            } else if (menuAction.equals("Item...")) {
                OrderItemPanel panel = (OrderItemPanel)getTabPanel(1);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.item.ItemFrame",panel.getItemId());
            } else if (menuAction.equals("Carrier...")) {
                OrderHeaderPanel panel = (OrderHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.carrier.carrierFrame", panel.getCarrierId());
            } else if (menuAction.equals("Order Acknowledgement...")) {
                OrderHeaderPanel panel = (OrderHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.docGen.PrintOrderAckDoc", panel.getTransId());
            }
        }
    }
}

