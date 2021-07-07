
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package dai.server.inventoryService;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import dai.idl.rmi.InventoryService;
import dai.server.serverShared.ServerUtils;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cust_order_itemObj;
import dai.shared.businessObjs.purch_order_itemObj;
import dai.shared.businessObjs.shipment_itemObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class InventoryRMIServiceImpl extends UnicastRemoteObject implements InventoryService
{
    InventoryServiceImpl inventoryServiceImpl = new InventoryServiceImpl();
    SessionMetaData _sessionMeta;
    ServerUtils     _serverUtils = new ServerUtils();
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter _dbAdapter = dbAdapterFactory.getDBAdapter();

    // constructor //
    public InventoryRMIServiceImpl()
    throws RemoteException, daiRemoteServiceException
    {
        super();
        _sessionMeta = _sessionMeta.getInstance();
    }

    // Post Cust Order Items To Inventory //
	public void postCustOrderItemsToInventory(csSecurity security, cust_order_itemObj[] ordItemObjs)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
    		inventoryServiceImpl.postCustOrderItemsToInventory(ordItemObjs);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::postCustOrderItemsToInventory failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    // Post Shipment Items To Inventory //
	public void postShipmentItemsToInventory(csSecurity security, shipment_itemObj[] shipItemObjs)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
    		inventoryServiceImpl.postShipmentItemsToInventory(shipItemObjs);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::postShipmentItemsToInventory failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    // Post Purchase Order Items To Inventory //
	public void postPurchOrderItemsToInventory(csSecurity security, purch_order_itemObj[] purchItemObjs)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
    		inventoryServiceImpl.postPurchOrderItemsToInventory(purchItemObjs);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::postPurchOrderItemsToInventory failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    // Post Received Items To Inventory //
	public void postReceivedItemsToInventory(csSecurity security, DBRecSet poItemsData)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
    		inventoryServiceImpl.postReceivedItemsToInventory(poItemsData);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::postReceivedItemsToInventory failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	public void postInventoryAdjustment(csSecurity security, DBRecSet itemData)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
    		inventoryServiceImpl.postInventoryAdjustment(itemData);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::postInventoryAdjustment failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    public static void main (String[] args)
    {
        System.setSecurityManager (new RMISecurityManager());
        String serviceName = ""; //For the out of scope catch block.
        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        try
        {
            //Create a new instance of the service.
            InventoryRMIServiceImpl service = new InventoryRMIServiceImpl();
            serviceName = service.SERVER_NAME;

            //Bind service to local registry on default port.
            Naming.rebind("//"+sessionMeta.getHostname()+"/"+serviceName, service);

            //Some status.
            System.out.println(serviceName + " Started.");

        } catch (Exception e) {
            System.err.println(serviceName + "\n" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}

