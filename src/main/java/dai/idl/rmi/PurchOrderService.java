//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package dai.idl.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

public interface PurchOrderService extends Remote
{
    static String SERVER_NAME = "PurchOrderService";

    /**
      *  Returns the following attributes in the Collection:
      *      purch_orderObj.ID
      *      purch_orderObj.DATE_CREATED
      *      purch_order_itemObj.ITEM_ID
      *      purch_order_itemObj.DETAIL_ID
      *      purch_order_itemObj.QTY_ORDERED
      *      purch_order_itemObj.QTY_RECEIVED
      * @param  security A security context that has already been validated
      * @param  poId Used to check for all Purch Line Items.
      * @return     void
      * @exception  daiRemoteServiceException
      *             Will contain text describing specifics of the error.
      */
    public DBRecSet getInventoryReceivablePOItems(csSecurity security, String poId)
    throws RemoteException, daiRemoteServiceException;

      //* The following attributes are supplied int he collection.
      //*      purch_orderObj.ID
      //*      purch_orderObj.DATE_CREATED
      //*      purch_order_itemObj.ITEM_ID
      //*      purch_order_itemObj.QTY_ORDERED
      //*      purch_order_itemObj.QTY_RECEIVED
      //*      purch_order_itemObj._QTY_TO_RECEIVE
    public void receiveInventory(csSecurity security, DBRecSet poItemsData)
    throws RemoteException, daiRemoteServiceException;

    //Returns the following attributes in a collection.
    //  purch_order_item_rcv_histObj.ID
    //  purch_order_itemObj.ITEM_ID
    //  purch_order_itemObj.QTY_ORDERED
    //  purch_order_itemObj.DESCRIPTION1
    //  purch_order_item_rcv_histObj.QTY_RECEIVED
    //  purch_order_item_Obj.PURCH_PRICE
    //  purch_order_item_rcv_histObj.DATE_RECEIVED
    public DBRecSet getInvoicablePOItems(csSecurity security, String poId)
    throws RemoteException, daiRemoteServiceException;

    //Returns the following attributes in a collection.
    // payment_voucherObj.ID
    // payment_voucherObj.PAYMENT_DUE_DATE
    // payment_voucherObj.VENDOR_NAME
    // payment_voucherObj.INVOICE_NUM
    // payment_voucherObj.INVOICE_DATE
    // payment_voucherObj.TOTAL_PAYMENTS_POSTED
    // payment_voucherObj.TOTAL_VALUE
    public DBRecSet getPayablePurchases(csSecurity security, String filterDate)
    throws RemoteException, daiRemoteServiceException;

    //*     payment_voucherObj.PURCH_ORDER_ID
    //*     payment_voucherObj.VENDOR_NAME
    //*     payment_voucherObj.ACCTID
    //*     payment_voucherObj.ACCTNAME
    //*     payment_voucherObj.PAYMENT_TERMS
    //*     payment_voucherObj.PAYMENT_DUE_DATE
    //*     payment_voucherObj.SUBTOTAL_AMT
    //*     payment_voucherObj.TOTAL_SHIPPING_CHARGES
    //*     payment_voucherObj.TOTAL_VALUE
    //*     payment_voucherObj.INVOICE_NUM
    //*     payment_voucherObj.INVOICE_DATE
    //*     payment_voucherObj._SHIP_CHARGES_ACCT_ID
    //*     payment_voucherObj._SHIP_CHARGES_ACCT_NAME
    //*     payment_voucherObj._NUM_PAYMENTS
    //  * @return     Returns generated new generated Payment Voucher ID.
	public String[] postInvoiceReceipt(csSecurity security, DBRecSet poData)
    throws RemoteException, daiRemoteServiceException;

    //*     payment_voucherObj.PURCH_ORDER_ID
    //*     payment_voucherObj.VENDOR_NAME
    //*     payment_voucherObj.ACCTID
    //*     payment_voucherObj.ACCTNAME
    //*     payment_voucherObj.PAYMENT_TERMS
    //*     payment_voucherObj.PAYMENT_DUE_DATE
    //*     payment_voucherObj.SUBTOTAL_AMT
    //*     payment_voucherObj.TOTAL_SHIPPING_CHARGES
    //*     payment_voucherObj.TOTAL_VALUE
    //*     payment_voucherObj.INVOICE_NUM
    //*     payment_voucherObj.INVOICE_DATE
    //*     payment_voucherObj._SHIP_CHARGES_ACCT_ID
    //*     payment_voucherObj._SHIP_CHARGES_ACCT_NAME
    //*     payment_voucherObj._NUM_PAYMENTS
    //  * @return     Returns generated new generated Payment Voucher ID.
	public String[] postBillReceipt(csSecurity security, DBRecSet poData)
    throws RemoteException, daiRemoteServiceException;

    //Expects the following attributes.
    // payment_voucherObj.PAYMENT_AMT
    // payment_voucherObj.CHECK_NUM
    // payment_voucherObj.PAYMENT_METHOD
    // payment_voucherObj.PRINT_CHECK
    // payment_voucherObj.ACCTID
    // payment_voucherObj.DATE_PAID
    // payment_voucherObj.PURCH_ORDER_ID
    // payment_voucherObj.ID
    // payment_voucherObj.VENDOR_NAME
	public void postPurchOrderPayments(csSecurity security, DBRecSet payVouchers)
	throws RemoteException, daiRemoteServiceException;


    //Expects the following attributes.
    // payment_voucherObj.PAYMENT_AMT
    // payment_voucherObj.CHECK_NUM
    // payment_voucherObj.PAY_FROM_ACCT_ID
    // payment_voucherObj.PURCH_ORDER_ID
    // payment_voucherObj.ID
    // payment_voucherObj.VENDOR_NAME
	public void voidPurchOrderPayment(csSecurity security, DBRec payVouchAttribs)
	throws RemoteException, daiRemoteServiceException;

	public void cancelPurchaseOrder(csSecurity security, String poId)
	throws RemoteException, daiRemoteServiceException;

    //////////////////////////////////////////////////////////////////////
    //Name: Get Checks To Print
    //Description:
    //Returns the following attributes in the Collection:
    //  payment_voucherObj.PAYMENT_DUE_DATE
    //  payment_voucherObj.VENDOR_NAME
    //  payment_voucherObj.ID
    //  payment_voucherObj.TOTAL_VALUE
    //  payment_voucherObj.CHECK_NUM
    //  payment_voucherObj.PAYMENT_AMT
    public DBRecSet getChecksToPrint(csSecurity secuirty)
    throws RemoteException, daiRemoteServiceException;

    public void createCheckPrintScratchData(csSecurity security, String beginCheckNum, String endCheckNum)
    throws RemoteException, daiRemoteServiceException;

 }
