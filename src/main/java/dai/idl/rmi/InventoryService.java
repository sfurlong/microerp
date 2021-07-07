package dai.idl.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cust_order_itemObj;
import dai.shared.businessObjs.purch_order_itemObj;
import dai.shared.businessObjs.shipment_itemObj;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

/**
  *Title:        Inventory Service Interface<br>
  *Copyright:    Copyright (c) 1999-2000<br>
  *Author:       sfurlong<br>
  *Company:      Digital Artifacts Inc.<br>
  *Description:  The client/server Interface for the Inventory Service.
  */
public interface InventoryService extends Remote
{
    static String SERVER_NAME = "InventoryService";

    /**
      *Updates the Item Inventory based on the array of cust_order_itemObj Objects.
      *This routine will not double post inventory if called twice for the same
      *cust_order_itemObj.  This is possible because it keeps track of how many
      *items have already been posted for each line item.
      *<I><BR>Logic:<BR>
      * For each custOrdItemObj Do <br>
      * Increment the Cust Backorder Qty for the Item<BR>
      * Insert a new detail record into the item_inventory table<BR>
      * end Do.
      *</I>
      * @param  security A security context that has already been validated
      * @param  orderItemObjs An array of Cust Order Item objects
      * @return     void
      * @exception  daiRemoteServiceException
      *             Will contain text describing specifics of the error.
      */
    public void postCustOrderItemsToInventory(csSecurity security, cust_order_itemObj[] ordItemObjs)
    throws RemoteException, daiRemoteServiceException;

    /**
      *Updates the Item Inventory based on the array of shipment_itemObj Objects.
      *This routine will not double post inventory if called twice for the same
      *shipment_itemObj.  This is possible because it keeps track of how many
      *items have already been posted for each line item.
      *<I><BR>Logic:<BR>
      * For each custOrdItemObj Do <br>
      * Decrement the Cust Backorder Qty for the Item<BR>
      * Decrement the Total Onhand Qty<BR>
      * Insert a new detail record into the item_inventory table<BR>
      * end Do.
      * </I>
      * @param  security A security context that has already been validated
      * @param  ShipmentItemObjs An array of Shipment Item objects
      * @return     void
      * @exception  daiRemoteServiceException
      *             Will contain text describing specifics of the error.
      */
    public void postShipmentItemsToInventory(csSecurity security, shipment_itemObj[] shipItemObjs)
    throws RemoteException, daiRemoteServiceException;

    /**
      *Updates the Item Inventory based on the array of purch_order_itemObj Objects.
      *This routine will not double post inventory if called twice for the same
      *purch_order_itemObj.  This is possible because it keeps track of how many
      *items have already been posted for each line item.
      *<I><BR>Logic:<BR>
      * For each custOrdItemObj Do <br>
      * Increment the Vendor Backorder Qty for the Item<BR>
      * Insert a new detail record into the item_inventory table<BR>
      * end Do.
      *</I>
      * @param  security A security context that has already been validated
      * @param  PurchItemObjs An array of Purchase Order Item objects
      * @return     void
      * @exception  daiRemoteServiceException
      *             Will contain text describing specifics of the error.
      */
    public void postPurchOrderItemsToInventory(csSecurity security, purch_order_itemObj[] purchItemObjs)
    throws RemoteException, daiRemoteServiceException;

    /**
      *Updates the Item Inventory based on a collection of purch_order_itemObj data.
      *<I><BR>Logic:<BR>
      * For each Purch Ord Item Record Do <br>
      * Decrement the Vendor Backorder Qty for the Item<BR>
      * Increment the Qty On Hand for the Item<BR>
      * Insert a new detail record into the item_inventory table<BR>
      * end Do.<BR>
      * The following attributes are expected in the collection:<BR>
	  *      purch_orderObj.ID
	  *      purch_orderObj.DATE_CREATED
	  *      purch_order_itemObj.ITEM_ID
	  *      purch_order_itemObj.QTY_ORDERED
	  *      purch_order_itemObj.QTY_RECEIVED
	  *      purch_order_itemObj._QTY_TO_RECEIVE
      *</I>
      * @param  security A security context that has already been validated
      * @param  poItemsData a collection of Purchase Order Item Data
      * @return     void
      * @exception  daiRemoteServiceException
      *             Will contain text describing specifics of the error.
      */
	public void postReceivedItemsToInventory(csSecurity security, DBRecSet poItemsData)
    throws RemoteException, daiRemoteServiceException;

	public void postInventoryAdjustment(csSecurity security, DBRecSet itemData)
    throws RemoteException, daiRemoteServiceException;
}


