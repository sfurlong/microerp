//Title:        Client Server DB Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.

package dai.server.purchOrderService;

import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import dai.idl.rmi.*;
import java.sql.*;
import java.util.*;
import java.lang.*;
import dai.shared.cmnSvcs.*;
import dai.shared.csAdapters.*;
import dai.shared.businessObjs.*;
import dai.server.dbService.*;
import dai.server.serverShared.*;

public class PurchOrderRMIServiceImpl extends UnicastRemoteObject implements PurchOrderService
{

    PurchOrderServiceImpl purchOrderService;
    SessionMetaData     _sessionMeta;
    ServerUtils         _serverUtils = new ServerUtils();
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter _dbAdapter = dbAdapterFactory.getDBAdapter();

	public PurchOrderRMIServiceImpl() throws RemoteException
	{
        super();
        purchOrderService = new PurchOrderServiceImpl();
        _sessionMeta = _sessionMeta.getInstance();
	}

    public DBRecSet getInventoryReceivablePOItems(csSecurity security, String poId)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        DBRecSet ret = null;

        try {
            _dbAdapter.beginTrans(security);
            ret = purchOrderService.getInventoryReceivablePOItems(poId);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::getInventoryReceivablePOItems failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

      //*      purch_orderObj.ID
      //*      purch_orderObj.DATE_CREATED
      //*      purch_order_itemObj.ITEM_ID
      //*      purch_order_itemObj.QTY_ORDERED
      //*      purch_order_itemObj.QTY_RECEIVED
    public void receiveInventory(csSecurity security, DBRecSet poItemsData)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        try {
            _dbAdapter.beginTrans(security);
            purchOrderService.receiveInventory(poItemsData);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::getInventoryReceivablePOItems failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public DBRecSet getInvoicablePOItems(csSecurity security, String poId)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        DBRecSet ret = null;

        try {
            _dbAdapter.beginTrans(security);
            ret = purchOrderService.getInvoicablePOItems(poId);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::getInvoicablePOItems failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }


    public DBRecSet getPayablePurchases(csSecurity security, String filterDate)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        DBRecSet ret = null;

        try {
            _dbAdapter.beginTrans(security);
            ret = purchOrderService.getPayablePurchases(filterDate);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::getPayablePurchases failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
    }

    //*     purch_orderObj.id
    //*     purch_orderObj.VENDOR_NAME
    //*     purch_orderObj.account_num
    //*     purch_orderObj.account_name
    //*     purch_orderObj.payment_terms
    //*     purch_orderObj.payment_due_date
    //*     purch_orderObj._bill_payment_amt
	public String[] postInvoiceReceipt(csSecurity security, DBRecSet poData)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
            String[] ret = purchOrderService.postInvoiceReceipt(poData);
            _dbAdapter.endTrans(security);
            return ret;
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::postInvoiceReceipt failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //*     purch_orderObj.id
    //*     purch_orderObj.VENDOR_NAME
    //*     purch_orderObj.account_num
    //*     purch_orderObj.account_name
    //*     purch_orderObj.payment_terms
    //*     purch_orderObj.payment_due_date
    //*     purch_orderObj._bill_payment_amt
	public String[] postBillReceipt(csSecurity security, DBRecSet poData)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
            String[] ret = purchOrderService.postBillReceipt(poData);
            _dbAdapter.endTrans(security);
            return ret;
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::postBillReceipt failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

	public void postPurchOrderPayments(csSecurity security, DBRecSet payVouchers)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);

        try {
            _dbAdapter.beginTrans(security);
            purchOrderService.postPurchOrderPayments(payVouchers);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::postPurchOrderPayments failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public void voidPurchOrderPayment(csSecurity security, DBRec payVouchAttribs)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);

        try {
            _dbAdapter.beginTrans(security);
            purchOrderService.voidPurchOrderPayment(payVouchAttribs);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::voidPurchOrderPayment failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public DBRecSet getChecksToPrint(csSecurity security)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        DBRecSet ret;
        try {
            _dbAdapter.beginTrans(security);
            ret = purchOrderService.getChecksToPrint();
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::getChecksToPrint failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
    }

    public void cancelPurchaseOrder(csSecurity security, String poId)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        try {
            _dbAdapter.beginTrans(security);
            purchOrderService.cancelPurchaseOrder(poId);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::cancelPurchaseOrder failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public void createCheckPrintScratchData(csSecurity security, String beginCheckNum, String endCheckNum)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        DBRecSet ret;
        try {
            _dbAdapter.beginTrans(security);
            purchOrderService.createCheckPrintScratchData(beginCheckNum, endCheckNum);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::createCheckPrintScratchData failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public static void main (String[] args)
    {
        System.setSecurityManager (new RMISecurityManager());
        String serviceName = ""; //For the out of scorpe catch block.
        SessionMetaData sessionMeta = SessionMetaData.getInstance();

        try
        {
            //Create a new instance of the service.
            PurchOrderRMIServiceImpl service = new PurchOrderRMIServiceImpl();
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

