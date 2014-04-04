package dai.shared.csAdapters;

import dai.shared.cmnSvcs.*;
import dai.shared.businessObjs.*;

abstract public class csInventoryAdapter
{
    abstract public void postCustOrderItemsToInventory(csSecurity security, cust_order_itemObj[] ordItemObjs)
	throws daiRemoteServiceException;

    abstract public void postShipmentItemsToInventory(csSecurity security, shipment_itemObj[] shipItemObjs)
	throws daiRemoteServiceException;

    abstract public void postPurchOrderItemsToInventory(csSecurity security, purch_order_itemObj[] purchItemObjs)
	throws daiRemoteServiceException;

	abstract public void postReceivedItemsToInventory(csSecurity security, DBRecSet poItemsData)
	throws daiRemoteServiceException;

	abstract public void postInventoryAdjustment(csSecurity security, DBRecSet itemData)
    throws daiRemoteServiceException;
}

