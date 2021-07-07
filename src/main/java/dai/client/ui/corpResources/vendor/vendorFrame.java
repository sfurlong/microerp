
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.vendor;

import javax.swing.JFrame;

import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.vendorObj;
import dai.shared.businessObjs.vendor_contactObj;
import dai.shared.businessObjs.vendor_journalObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class vendorFrame extends daiFrame
{
	vendorMainPanel    mainTab;
	vendorJournalPanel journalTab;
	vendorContactPanel contactTab;
	VendorAddrPanel    addrTab;

	vendorObj          custObj;
	vendor_journalObj  cust_journalObj;
	vendor_contactObj  cust_contactObj;

	public vendorFrame(JFrame container)
	{
		super (container, new vendorObj());

		//Let's get our own local copy of the vendorObj.
		custObj           = new vendorObj();
		cust_journalObj   = new vendor_journalObj();
		cust_contactObj   = new vendor_contactObj();

		//Set the title
		this.setTitle("Vendor Entry/Update");
		this.setBannerLeftText("Vendor Entry/Update");

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

	public vendorFrame(JFrame container, String vendorId)
	{
		super (container, new vendorObj());

		//Let's get our own local copy of the vendorObj.
		custObj           = new vendorObj();
		cust_journalObj   = new vendor_journalObj();
		cust_contactObj   = new vendor_contactObj();

		//Set the title
		this.setTitle("Vendor Entry/Update");
		this.setBannerLeftText("Vendor Entry/Update");

		try
		{
			jbInit();
			pack();

            if (vendorId != null) {
    			callBackInsertNewId(vendorId);
            }

		} catch (Exception ex)
		{
			System.out.println("resourceDialog.constructor");
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		mainTab       = new vendorMainPanel(CONTAINER, this, custObj);
		journalTab    = new vendorJournalPanel(CONTAINER, this, cust_journalObj);
		contactTab    = new vendorContactPanel(CONTAINER, this, cust_contactObj);
		addrTab       = new VendorAddrPanel(CONTAINER, this, custObj);

		//Add the Address subpanel to the header panel
		mainTab.addSubPanel(addrTab);

		//Let our Ansestor know about the Panel.
		addTabPanel("Main", mainTab);
		addTabPanel("Addresses", addrTab);
		addTabPanel("Contacts", contactTab);
		addTabPanel("Journal", journalTab);

        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                         new DBAttributes("name","Name",300),
                                                         new DBAttributes("vendor_type","Vendor Type",200),
                                                         null,
                                                         new DBAttributes("date_created","Date Created",100),
                                                         new DBAttributes("created_by","Created By",100)});
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
												 dbAdapter.SEQUENCE_VENDOR);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName() + "::generateNewUniqueId failure." +
						 "\n"+e.toString()+"\n"+e.getLocalizedMessage();
			LOGGER.logError(CONTAINER, msg);
		}

		ret = "V" + daiFormatUtil.padIntLeft(newSeq, 3);

		return ret;
	}

}
