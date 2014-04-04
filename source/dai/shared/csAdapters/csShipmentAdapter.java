package dai.shared.csAdapters;

import java.util.Vector;

import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cash_receiptObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

/**
  *Title:        Client Server Shipment Service Adapter<br>
  *Copyright:    Copyright (c) 1999-2000<br>
  *Author:       sfurlong<br>
  *Company:      Digital Artifacts Inc.<br>
  *Description:  The client side proxy for the Shipment Service.
  */
abstract public class csShipmentAdapter
{
    abstract public DBRecSet getShipableOrders(csSecurity security, String locality, DBAttributes[] filters)
    throws daiRemoteServiceException;

    abstract public Vector getShipableOrderItems(csSecurity security, String orderId, String locality)
    throws daiRemoteServiceException;

	abstract public String createShipment(csSecurity security, DBRec headerData, Vector custOrdItemObjs, default_accountsObj defaultAcctsObj)
	throws daiRemoteServiceException;

	abstract public void deleteShipment(csSecurity security, String shipmentId)
	throws daiRemoteServiceException;

	abstract public void deleteOrder(csSecurity security, String orderId)
	throws daiRemoteServiceException;

	abstract public void updateShipmentCharges(csSecurity security, DBRecSet shipmentData)
    throws daiRemoteServiceException;

    abstract public shipmentObj[] getPayableShipments(csSecurity security, String locality)
    throws daiRemoteServiceException;

	abstract public String createCashReceipt(csSecurity security, DBRecSet cashRcptData)
	throws daiRemoteServiceException;

        abstract public String updateCashReceipts_totalPaid(csSecurity security, cash_receiptObj _cash_receiptObj)
	throws daiRemoteServiceException;

	abstract public void deleteCashReceipt_updateTotalPaid(csSecurity security, String _cash_receiptID)
        throws daiRemoteServiceException;

        abstract public String createCreditMemo(csSecurity security, DBRecSet shipItemData)
    throws daiRemoteServiceException;

	abstract public String createCustFromProspect(csSecurity security, DBRec pspectData)
    throws daiRemoteServiceException;
}

