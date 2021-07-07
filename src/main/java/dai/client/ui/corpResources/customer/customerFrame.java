
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.customer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiFrameMenuBar;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.customer_contactObj;
import dai.shared.businessObjs.customer_journalObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class customerFrame extends daiFrame
{
	customerMainPanel    mainTab;
	customerJournalPanel journalTab;
	customerContactPanel contactTab;
    CustomerAddrPanel    addrTab;

	customerObj          custObj;
	customer_journalObj  cust_journalObj;
	customer_contactObj  cust_contactObj;

    MenuActionListener menuListener = new MenuActionListener();

	public customerFrame(JFrame container)
	{
		super (container, new customerObj());

		//Let's get our own local copy of the customerObj.
		custObj           = new customerObj();
		cust_journalObj   = new customer_journalObj();
		cust_contactObj   = new customer_contactObj();

		//Set the title for this Window.
		this.setTitle("Customer Entry/Update");
		this.setBannerLeftText("Customer Entry/Update");

		try
		{
			jbInit();
			pack();

		} catch (Exception ex)
		{
			System.out.println("resourceDialog.constructor");
			ex.printStackTrace();
		}
	}

	public customerFrame(JFrame container, String custId)
	{
		super (container, new customerObj());

		//Let's get our own local copy of the customerObj.
		custObj           = new customerObj();
		cust_journalObj   = new customer_journalObj();
		cust_contactObj   = new customer_contactObj();

		//Set the title for this Window.
		this.setTitle("Customer Entry/Update");
		this.setBannerLeftText("Customer Entry/Update");

		try
		{
			jbInit();
			pack();

            if (custId != null) {
    			callBackInsertNewId(custId);
            }
		} catch (Exception ex)
		{
			System.out.println("resourceDialog.constructor");
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		mainTab       = new customerMainPanel(CONTAINER, this, custObj);
		journalTab    = new customerJournalPanel(CONTAINER, this, cust_journalObj);
		contactTab    = new customerContactPanel(CONTAINER, this, cust_contactObj);
        addrTab       = new CustomerAddrPanel(CONTAINER, this, custObj);

        //Add the customer panel as a subpanel to the Header panel
        mainTab.addSubPanel(addrTab);

		//Let our Ansestor know about the Panel.
		addTabPanel("Summary", mainTab);
        addTabPanel("Address", addrTab);
		addTabPanel("Contacts", contactTab);
		addTabPanel("Journal", journalTab);

		selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("name","Name",300),
                                                          new DBAttributes("also_known_as","AKA",300),
                                                          null,
                                                          new DBAttributes("billto_city","Bill City",150),
                                                          new DBAttributes("billto_state_code","Bill State",50),
                                                          null,
                                                          new DBAttributes("shipto_city","Ship City",150),
                                                          new DBAttributes("shipto_state_code","Ship State",50),
                                                          null,
                                                          new DBAttributes("date_created","Date Created",100),
                                                          new DBAttributes("created_by","Created By",100)});

        //make name the default search value
        selectionBox.setComboBoxText("name");

        JMenuItem custStmtMenuItem = new JMenuItem("Customer Statement...");
        custStmtMenuItem.addActionListener(menuListener);

        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).add(custStmtMenuItem);
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

		try
		{
			newSeq = dbAdapter.getNewSequenceNum(sessionMeta.getClientServerSecurity(),
												 dbAdapter.SEQUENCE_CUSTOMER);
		} catch (Exception e)
		{
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
            if (menuAction.equals("Customer Statement...")) {
                customerMainPanel panel = (customerMainPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.docGen.PrintCustStmtDoc",panel.getTransId());
            }
        }
    }
}


