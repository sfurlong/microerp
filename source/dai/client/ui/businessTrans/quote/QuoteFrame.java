//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.quote;

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
import dai.shared.businessObjs.quoteObj;
import dai.shared.businessObjs.quote_itemObj;
import dai.shared.businessObjs.quote_journalObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class QuoteFrame extends daiFrame
{
	QuoteHeaderPanel        headerTab;
	QuoteItemPanel          itemTab;
	QuoteShipToBillToPanel  customerTab;
	QuoteDocNotesPanel      docNotesTab;
    QuoteJournalPanel       journalTab;

	quoteObj            headerObj;
	quote_itemObj       itemObj;
    quote_journalObj    journalObj;

	SessionMetaData sessionMeta;

	JMenu jMenu_documents = new JMenu();
	JMenuItem jMenuItem_invoiceDoc = new JMenuItem();

    MenuActionListener menuListener = new MenuActionListener();

	public QuoteFrame(JFrame container)
	{
		super (container, new quoteObj());

		headerObj = new quoteObj();
		itemObj   = new quote_itemObj();
        journalObj= new quote_journalObj();

		//Set the title.
		this.setTitle("Quotation Entry/Update");
        this.setBannerLeftText("Quotation Entry/Update");

		try
		{
			jbInit();
			pack();

		} catch (Exception ex)
		{
    		LOGGER.logError(CONTAINER, "Could not initialize Frame.\n" + ex.getLocalizedMessage());
    		ex.printStackTrace();
		}
	}

	public QuoteFrame(JFrame container, String quoteId)
	{
		super (container, new quoteObj());

		headerObj = new quoteObj();
		itemObj   = new quote_itemObj();
        journalObj= new quote_journalObj();

		//Set the title.
		this.setTitle("Quotation Entry/Update");
        this.setBannerLeftText("Quotation Entry/Update");

		try
		{
			jbInit();
			pack();

            if (quoteId != null) {
    			callBackInsertNewId(quoteId);
            }

		} catch (Exception ex)
		{
    		LOGGER.logError(container, "Could not initialize Frame.\n" + ex.getLocalizedMessage());
    		ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
        sessionMeta = SessionMetaData.getInstance();

		headerTab   = new QuoteHeaderPanel(CONTAINER, this, headerObj);
		itemTab     = new QuoteItemPanel(CONTAINER, this, itemObj);
        customerTab = new QuoteShipToBillToPanel(CONTAINER, this, headerObj);
        docNotesTab = new QuoteDocNotesPanel(CONTAINER, this, headerObj);
        journalTab  = new QuoteJournalPanel(CONTAINER, this, journalObj);

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
                                                          new DBAttributes("customer_name","Customer Name",250),
                                                          new DBAttributes("customer_id","Customer ID",100),
                                                          null,
                                                          new DBAttributes("item_id","Item ID",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created",100),
                                                          new DBAttributes("created_by","Created By",150)});

        JMenuItem custMenuItem = new JMenuItem("Customer...");
        custMenuItem.setMnemonic('C');
        custMenuItem.addActionListener(menuListener);

        JMenuItem pspectMenuItem = new JMenuItem("Prospect...");
        pspectMenuItem.setMnemonic('P');
        pspectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
        pspectMenuItem.addActionListener(menuListener);

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
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(pspectMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(carrMenuItem);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(itemMenuItem);

        JMenuItem ordAckMenuItem = new JMenuItem("Quotation...");
        ordAckMenuItem.addActionListener(menuListener);

        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).add(ordAckMenuItem);


        setTabsEnabled(false);
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
        QuoteHeaderPanel panel = (QuoteHeaderPanel)getTabPanel(0);
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
                                    csDBAdapter.SEQUENCE_QUOTE);
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
                QuoteHeaderPanel panel = (QuoteHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.customer.customerFrame", panel.getCustId());
            } else if (menuAction.equals("Prospect...")) {
                QuoteHeaderPanel panel = (QuoteHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.prospect.ProspectFrame", panel.getCustId());
            } else if (menuAction.equals("Item...")) {
                QuoteItemPanel panel = (QuoteItemPanel)getTabPanel(1);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.item.ItemFrame", panel.getItemId());
            } else if (menuAction.equals("Carrier...")) {
                QuoteHeaderPanel panel = (QuoteHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.carrier.carrierFrame", panel.getCarrierId());
            } else if (menuAction.equals("Quotation...")) {
                QuoteHeaderPanel panel = (QuoteHeaderPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.docGen.PrintQuoteDoc",panel.getTransId());
            }
        }
    }
}

