
//Title:        Client Server Shipment Service Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:


package dai.shared.csAdapters;

import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

abstract public class csPurchOrderAdapter
{

    //////////////////////////////////////////////////////////////////////
    //Name: Get Inventory Receivable Purch Order Items
    //Description:
    //  Returns the following attributes in the Collection:
    //      purch_orderObj.ID
    //      purch_orderObj.DATE_CREATED
    //      purch_order_itemObj.PURCH_PRICE
    //      purch_order_itemObj.ITEM_ID
    //      purch_order_itemObj.QTY_ORDERED
    //      purch_order_itemObj.QTY_RECEIVED
    abstract public DBRecSet getInventoryReceivablePOItems(csSecurity security, String vendorId)
    throws daiRemoteServiceException;

    abstract public DBRecSet getPayablePurchases(csSecurity security, String filterDate)
    throws daiRemoteServiceException;

    //*     payment_voucherObj.PURCH_ORDER_ID
    //*     payment_voucherObj.VENDOR_NAME
    //*     payment_voucherObj.ACCTID
    //*     payment_voucherObj.ACCTNAME
    //*     payment_voucherObj.PAYMENT_TERMS
    //*     payment_voucherObj.PAYMENT_DUE_DATE
    //*     payment_voucherObj.TOTAL_VALUE
    //*     payment_voucherObj.INVOICE_NUM
    //*     payment_voucherObj.INVOICE_DATE
    //* @return Returns the new generated Pay Voucher Id.
	abstract public String[] postInvoiceReceipt(csSecurity security,
                                            DBRecSet poData)
    throws daiRemoteServiceException;


    //*     payment_voucherObj.PURCH_ORDER_ID
    //*     payment_voucherObj.VENDOR_NAME
    //*     payment_voucherObj.ACCTID
    //*     payment_voucherObj.ACCTNAME
    //*     payment_voucherObj.PAYMENT_TERMS
    //*     payment_voucherObj.PAYMENT_DUE_DATE
    //*     payment_voucherObj.TOTAL_VALUE
    //*     payment_voucherObj.INVOICE_NUM
    //*     payment_voucherObj.INVOICE_DATE
    //* @return Returns the new generated Pay Voucher Id.
	abstract public String[] postBillReceipt(csSecurity security,
                                            DBRecSet poData)
    throws daiRemoteServiceException;

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
	abstract public void postPurchOrderPayments(csSecurity security, DBRecSet payVouchers)
	throws daiRemoteServiceException;


    //Expects the following attributes.
    // payment_voucherObj.PAYMENT_AMT
    // payment_voucherObj.CHECK_NUM
    // payment_voucherObj.PAY_FROM_ACCT_ID
    // payment_voucherObj.PURCH_ORDER_ID
    // payment_voucherObj.ID
    // payment_voucherObj.VENDOR_NAME
	abstract public void voidPurchOrderPayment(csSecurity security, DBRec payVouchAttribs)
	throws daiRemoteServiceException;


    //* The following attributes are supplied int he collection.
    //*      purch_orderObj.ID
    //*      purch_orderObj.DATE_CREATED
    //*      purch_order_itemObj.ITEM_ID
    //*      purch_order_itemObj.QTY_ORDERED
    //*      purch_order_itemObj.QTY_RECEIVED
    //*      purch_order_itemObj._QTY_TO_RECEIVE
    abstract public void receiveInventory(csSecurity security, DBRecSet poItemsData)
    throws daiRemoteServiceException;

    //Returns the following attributes in a collection.
    //  purch_order_item_rcv_histObj.ID
    //  purch_order_itemObj.ITEM_ID
    //  purch_order_itemObj.DESCRIPTION1
    //  purch_order_item_rcv_histObj.QTY_RECEIVED
    //  purch_order_item_Obj.PURCH_PRICE
    //  purch_order_item_rcv_histObj.DATE_RECEIVED
    abstract public DBRecSet getInvoicablePOItems(csSecurity security, String poId)
    throws daiRemoteServiceException;

    abstract public void cancelPurchaseOrder(csSecurity security, String poId)
    throws daiRemoteServiceException;

    //////////////////////////////////////////////////////////////////////
    //Name: Get Checks To Print
    //Description:
    //  Returns the following attributes in the Collection:
    //  payment_voucherObj.PAYMENT_DUE_DATE
    //  payment_voucherObj.VENDOR_NAME
    //  payment_voucherObj.ID
    //  payment_voucherObj.TOTAL_VALUE
    //  payment_voucherObj.CHECK_NUM
    //  payment_voucherObj.PAYMENT_AMT
    abstract public DBRecSet getChecksToPrint(csSecurity security)
    throws daiRemoteServiceException;

    abstract public void createCheckPrintScratchData(csSecurity security,
                                                    String beginCheckNum,
                                                    String endCheckNum)
    throws daiRemoteServiceException;
}

