
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package dai.shared.csAdapters;

import java.rmi.*;
import dai.idl.rmi.*;
import dai.shared.cmnSvcs.*;
import dai.shared.businessObjs.*;

public class csInventoryRMIAdapter extends csInventoryAdapter
{
    //Connection to the Inventory Service
    static InventoryService inventoryService;

    private Logger _logger;

    // constructor //
    csInventoryRMIAdapter(String host)
    {
        System.setSecurityManager (new daiSecurityManager());
        String lookup = "rmi://"+host+"/"+InventoryService.SERVER_NAME;

        _logger = _logger.getInstance();

        try {
            inventoryService = (InventoryService)Naming.lookup(lookup);
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
    }

	// Constructor for Fat Client//
	public csInventoryRMIAdapter()
	{
        _logger = Logger.getInstance();

        try {
            Class c = Class.forName("dai.server.inventoryService.InventoryRMIServiceImpl");
            inventoryService = (InventoryService)c.newInstance();
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

    // Post Cust Order Items To Inventory //
    public void postCustOrderItemsToInventory(csSecurity security, cust_order_itemObj[] ordItemObjs)
	throws daiRemoteServiceException
	{
        try {
            inventoryService.postCustOrderItemsToInventory(security, ordItemObjs);
        } catch (RemoteException e) {
            String msg = "csInventoryRMIAdapter::postCustOrderItemsToInventory failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    // Post Shipment Items To Inventory //
    public void postShipmentItemsToInventory(csSecurity security, shipment_itemObj[] shipItemObjs)
	throws daiRemoteServiceException
	{
        try {
            inventoryService.postShipmentItemsToInventory(security, shipItemObjs);
        } catch (RemoteException e) {
            String msg = "csInventoryRMIAdapter::postShipmentItemsToInventory failure" +
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    // Post Purch Order Items To Inventory //
    public void postPurchOrderItemsToInventory(csSecurity security, purch_order_itemObj[] purchItemObjs)
	throws daiRemoteServiceException
	{
        try {
            inventoryService.postPurchOrderItemsToInventory(security, purchItemObjs);
        } catch (RemoteException e) {
            String msg = "csInventoryRMIAdapter::postPurchOrderItemsToInventory failure" +
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	public void postReceivedItemsToInventory(csSecurity security, DBRecSet poItemsData)
	throws daiRemoteServiceException
    {
        try {
            inventoryService.postReceivedItemsToInventory(security, poItemsData);
        } catch (RemoteException e) {
            String msg = "csInventoryRMIAdapter::postReceivedItemsToInventory failure" +
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

	public void postInventoryAdjustment(csSecurity security, DBRecSet itemData)
    throws daiRemoteServiceException
    {
        try {
            inventoryService.postInventoryAdjustment(security, itemData);
        } catch (RemoteException e) {
            String msg = "csInventoryRMIAdapter::postInventoryAdjustment failure" +
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }
}
