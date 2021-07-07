
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.item;

import javax.swing.JFrame;

import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.itemObj;
import dai.shared.businessObjs.item_bomObj;
import dai.shared.businessObjs.item_inventoryObj;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class ItemFrame extends daiFrame
{
	ItemMainPanel mainTab;
	ItemInventoryPanel inventoryTab;
	ItemSubComponentsPanel subItemsTab;
	ItemInventoryGraphPanel inventoryGraphTab;
    ItemMeasPanel           dimsTab;
    ItemVendorPanel           vendorTab;
    ItemReportPanel           reportsTab;

	//Create the BusinessObjects that will be used in the tabPanels
	itemObj              item_Obj;
	item_inventoryObj    itemInventoryObj;
	item_bomObj          itemBOMObj;

	//Adapter Factory for getting handles to the Client Server Adapters
	csDBAdapterFactory  dbAdapterFactory;
	csDBAdapter         dbAdapter;

	public ItemFrame(JFrame container)
	{
		super (container, new itemObj());

		//Let's get our own local copy of the itemObj.
		item_Obj          = new itemObj();
		itemInventoryObj  = new item_inventoryObj();
		itemBOMObj        = new item_bomObj();

		//Set the window title
		this.setTitle("Item Entry/Update");
		this.setBannerLeftText("Item Entry/Update");

		try
		{
			jbInit();

			//Initialize the query parameter for the listbox.
			pack();

		} catch (Exception ex)
		{
			System.out.println("resourceDialog.constructor");
			ex.printStackTrace();
		}
	}

	public ItemFrame(JFrame container, String itemId)
	{
		super (container, new itemObj());

		//Let's get our own local copy of the itemObj.
		item_Obj          = new itemObj();
		itemInventoryObj  = new item_inventoryObj();
		itemBOMObj        = new item_bomObj();

		//Set the window title
		this.setTitle("Item Entry/Update");
		this.setBannerLeftText("Item Entry/Update");

		try
		{
			jbInit();
			pack();

            if (itemId != null) {
    			callBackInsertNewId(itemId);
            }

		} catch (Exception ex)
		{
			System.out.println("resourceDialog.constructor");
			ex.printStackTrace();
		}
	}

	void jbInit() throws Exception
	{
		mainTab = new ItemMainPanel(CONTAINER, this, item_Obj);
		inventoryTab = new ItemInventoryPanel(CONTAINER, this, itemInventoryObj);
		subItemsTab = new ItemSubComponentsPanel(CONTAINER, this, itemBOMObj);
        dimsTab = new ItemMeasPanel(CONTAINER, this, item_Obj);
        vendorTab = new ItemVendorPanel(CONTAINER, this, item_Obj);
        reportsTab = new ItemReportPanel(CONTAINER, this, item_Obj);

        //Add the Vendor and Dims panels as a subpanel to the Header panel
        mainTab.addSubPanel(vendorTab);
        mainTab.addSubPanel(dimsTab);
        mainTab.addSubPanel(reportsTab);

		//Let our Ancestor know about the Panel.
		addTabPanel("Summary", mainTab);
		addTabPanel("Vendor/Mfg.", vendorTab);
		addTabPanel("Dimensions", dimsTab);
		addTabPanel("SubComponents", subItemsTab);
		addTabPanel("Inventory Detail", inventoryTab);
		//addTabPanel("Inventory Graph", inventoryGraphTab);
                addTabPanel("Reports", reportsTab);


		//Adapter Factory for getting handles to the Client Server Adapters
		dbAdapterFactory = dbAdapterFactory.getInstance();
		dbAdapter = dbAdapterFactory.getDBAdapter();

//        selectionBox.addSearchFilters(new String[]{"vendor_id",
//                                                    "vendor_name",
//                                                    "vendor_item_model",
//                                                    "date_created",
//                                                    "created_by"});
          selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("vendor_id","Vendor ID",100),
                                                          new DBAttributes("vendor_name","Vendor Name",200),
                                                          null,
                                                          new DBAttributes("vendor_item_model","Vendor Model Number",150),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",100),
                                                          new DBAttributes("created_by","Created By",150)});
	}
                //Overided from the base tab so that the report tab is enabled always
        public void setTabsEnabled(boolean flag) {

            //Loop through all the TabPanels and disable them.
            for (int i = 1;i < TAB_PANELS.size()-1; i++)
                {
                _tabbedPane.setEnabledAt(i+1, flag);
            }
        }

}


