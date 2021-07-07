package dai.idl.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
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
  *Title:        Shipment Service Interface<br>
  *Copyright:    Copyright (c) 1999-2000<br>
  *Author:       sfurlong<br>
  *Company:      Digital Artifacts Inc.<br>
  *Description:  The client/server Interface for the Shipment Service.
  */
public interface ShipmentService extends Remote
{
    static String SERVER_NAME = "ShipmentService";

    // Get Shipable Orders //
    public DBRecSet getShipableOrders(csSecurity security, String locality,  DBAttributes[] filters)
    throws RemoteException, daiRemoteServiceException;

    // Get Shipable Order items //
    public Vector getShipableOrderItems(csSecurity security, String orderId, String locality)
    throws RemoteException, daiRemoteServiceException;

    /**
      *Creates a new Shipment transaction based on the specified Order transaction.
      *<I><BR>Logic:<BR>
      * Copy cust_orderObj record to shipmentObj<br>
      * Update cust_order_itemObj qtyShipped and qtyBackorder to reflected qtys chosen to ship<br>
      * Copy the cust_order_itemObjs to the shipment_itemObjs<br>
      * Update the header totals on the shipmentObj<br>
      * Update the item inventory for each new shipment_itemObj<br>
      *</I>
      * @param  security A security context that has already been validated
      * @param  headerData Attributes to update on the ShipmentObj.  Expected attribs are: total_shipping, FOB.
      * @param  defaultAcctsObj The financial accounts which will be updated
      * @return     void
      * @exception  daiRemoteServiceException
      *             Will contain text describing specifics of the error.
      */
	public String createShipment(csSecurity security, DBRec headerData, Vector custOrdItemObjs, default_accountsObj defaultAcctsObj)
    throws RemoteException, daiRemoteServiceException;

	public void deleteShipment(csSecurity security, String shipmentId)
    throws RemoteException, daiRemoteServiceException;

	public void deleteOrder(csSecurity security, String orderId)
    throws RemoteException, daiRemoteServiceException;

	public void updateShipmentCharges(csSecurity security, DBRecSet shipmentData)
    throws RemoteException, daiRemoteServiceException;

    //cash_receiptObj.PAYMENT_METHOD
    //cash_receiptObj.CHECK_NUM
    //cash_receiptObj.CHECK_AMT
    //cash_receiptObj.CC_NUM
    //cash_receiptObj.CC_EXP_DATE
    //cash_receiptObj.DATE_RECEIVED
    //cash_receiptObj.RECEIVABLE_ACCT_ID
    //cash_receiptObj.RECEIVABLE_ACCT_NAME
    //cash_receiptObj.DEPOSIT_ACCT_ID
    //cash_receiptObj.DEPOSIT_ACCT_NAME
    //cash_receiptObj.NOTE
	public String createCashReceipt(csSecurity security, DBRecSet cashRcptData)
    throws RemoteException, daiRemoteServiceException;

    public String updateCashReceipts_totalPaid(csSecurity security, cash_receiptObj _cash_receiptObj)
    throws RemoteException, daiRemoteServiceException;

    public void deleteCashReceipt_updateTotalPaid(csSecurity security, String _cash_receiptID)
    throws RemoteException, daiRemoteServiceException;


	public String createCreditMemo(csSecurity security, DBRecSet shipItemData)
    throws RemoteException, daiRemoteServiceException;

    //prospectObj.CUSTOMER_ID
    //prospectObj.COMPANY_NAME
	public String createCustFromProspect(csSecurity security, DBRec pspectData)
    throws RemoteException, daiRemoteServiceException;

    // Get Payable Shipments //
    public shipmentObj[] getPayableShipments(csSecurity security, String locality)
    throws RemoteException, daiRemoteServiceException;
}
