
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.prospect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiFrameMenuBar;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.prospectObj;
import dai.shared.businessObjs.prospect_journalObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class ProspectFrame extends daiFrame
{
	ProspectMainPanel    mainTab;
	ProspectJournalPanel journalTab;
    ProspectAddrPanel    addrTab;

	prospectObj          pspectObj;
	prospect_journalObj  pspect_journalObj;

    MenuActionListener menuListener = new MenuActionListener();

	public ProspectFrame(JFrame container)
	{
		super (container, new prospectObj());

		pspectObj         = new prospectObj();
		pspect_journalObj = new prospect_journalObj();

		//Set the title for this Window.
		this.setTitle("Prospect Entry/Update");
		this.setBannerLeftText("Prospect Entry/Update");

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

	public ProspectFrame(JFrame container, String custId)
	{
		super (container, new prospectObj());

		pspectObj           = new prospectObj();
		pspect_journalObj   = new prospect_journalObj();

		//Set the title for this Window.
		this.setTitle("Prospect Entry/Update");
		this.setBannerLeftText("Prospect Entry/Update");

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
		mainTab       = new ProspectMainPanel(CONTAINER, this, pspectObj);
		journalTab    = new ProspectJournalPanel(CONTAINER, this, pspect_journalObj);
        addrTab       = new ProspectAddrPanel(CONTAINER, this, pspectObj);

        //Add the customer panel as a subpanel to the Header panel
        mainTab.addSubPanel(addrTab);

		//Let our Ansestor know about the Panel.
		addTabPanel("Summary", mainTab);
        addTabPanel("Address", addrTab);
		addTabPanel("Journal", journalTab);

        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("company_name","Company Name",150),
                                                          new DBAttributes("customer_id","Customer ID",75),
                                                          null,
                                                          new DBAttributes("last_name","Last Name",100),
                                                          new DBAttributes("comp_name+last_name","Company Name+Last Name",125),
                                                          null,
                                                          new DBAttributes("city","City",125),
                                                          new DBAttributes("state_code","State",50),
                                                          null,
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",75),
                                                          null,
                                                          new DBAttributes("< date_created","Date Created Before",75),
                                                          new DBAttributes("> date_created","Date Created After",75),
                                                          null,
                                                          new DBAttributes("created_by","Created By",75)});

        JMenuItem custMenuItem = new JMenuItem("Customer...");
        custMenuItem.setMnemonic('C');
        custMenuItem.addActionListener(menuListener);

        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.RESOURCE_MENU).add(custMenuItem);

        JMenuItem labelMenuItem = new JMenuItem("Print Prospect Label...");
        labelMenuItem.addActionListener(menuListener);

        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.DOC_MENU).add(labelMenuItem);
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
												 csDBAdapter.SEQUENCE_PROSPECT);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName() + "::generateNewUniqueId failure." +
						 "\n"+e.toString()+"\n"+e.getLocalizedMessage();
			LOGGER.logError(CONTAINER, msg);
		}
		return daiFormatUtil.padIntLeft(newSeq, 5);
	}

	//Override from base class so we can extend the logic.
    //Specificaly: ensure that the Prospect company name and
    //contact name do not match others already in the database.
    protected void fileSave()
    {
        String id = mainTab.getTransId();
        String firstName = mainTab.getFirstName();
        String lastName = mainTab.getLastName();
        String compName = mainTab.getCompName();

        System.out.println(id + "     " + firstName + "   " + lastName + "    " + compName);

        String sqlStmt = " select " + prospectObj.ID + ", " +
                                        prospectObj.FIRST_NAME + ", " +
                                        prospectObj.LAST_NAME + ", " +
                                        prospectObj.COMPANY_NAME +
                        " from " + prospectObj.TABLE_NAME +
                        " where " + prospectObj.FIRST_NAME + " = '" + firstName + "' and " +
                                prospectObj.LAST_NAME + " = '" + lastName + " ' and " +
                                prospectObj.COMPANY_NAME + " = '" + compName + "'";

        csDBAdapter dbAdapter = csDBAdapterFactory.getInstance().getDBAdapter();

        try {
            SessionMetaData sessionMeta = SessionMetaData.getInstance();
            DBRecSet attribs = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
                                                                    sqlStmt);
            boolean duplicateFound = false;
            String duplicateId = null;
            if (attribs.getSize() > 0) {
                for (int i=0; i<attribs.getSize(); i++) {
                    String pspectId = attribs.getRec(i).getAttribVal(prospectObj.ID);
                    String pspectFirstName = attribs.getRec(i).getAttribVal(prospectObj.FIRST_NAME);
                    String pspectLastName = attribs.getRec(i).getAttribVal(prospectObj.LAST_NAME);
                    String pspectCompName = attribs.getRec(i).getAttribVal(prospectObj.COMPANY_NAME);
                    if (!pspectId.equals(id)){
                        duplicateFound = true;
                        duplicateId = pspectId;
                    }
                    System.out.println(pspectId + "     " + pspectFirstName + "   " + pspectLastName + "    " + pspectCompName);
                }
            }

            if (duplicateFound) {
                JOptionPane.showMessageDialog(this, "A prospect already exists that has the same " +
                        "Company Name, First Name and Last Name.\nPlease enter a distinct prospect." +
                        "  The duplicate ID is: " + duplicateId + ".",
                        "Unable To Save",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                //Update the userId, date created fields.
                mainTab.updateUserIdDateCreated();

                //Call the same method in the base class to do the
                //rest of the regular save logic.
                super.fileSave();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String menuAction = e.getActionCommand();
            if (menuAction.equals("Customer...")) {
                ProspectMainPanel panel = (ProspectMainPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.corpResources.customer.customerFrame", panel.getCustId());
            } else if (menuAction.equals("Print Prospect Label...")) {
                ProspectMainPanel panel = (ProspectMainPanel)getTabPanel(0);
                ((dai.client.clientAppRoot.daiExplorerFrame)CONTAINER).launchNodeComponent("dai.client.ui.docGen.PrintProspectLabelDoc",panel.getTransId());
            }
        }
    }
}


