
//Title:        Client Server Shipment Service Stub Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:


package dai.shared.csAdapters;

import java.rmi.Naming;
import java.rmi.RemoteException;

import dai.idl.rmi.PurchOrderService;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

public class csPurchOrderRMIAdapter extends csPurchOrderAdapter
{
    static PurchOrderService purchOrderService;

    private Logger _logger;

	public csPurchOrderRMIAdapter(String host)
	{
        System.setSecurityManager (new daiSecurityManager());
        String lookup = "rmi://"+host+"/"+PurchOrderService.SERVER_NAME;

        _logger = _logger.getInstance();

        try {
            purchOrderService = (PurchOrderService)Naming.lookup(lookup);
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

	// Constructor for purposes of Fat Client//
	public csPurchOrderRMIAdapter()
	{
        _logger = _logger.getInstance();

        try {
            Class c = Class.forName("dai.server.purchOrderService.PurchOrderRMIServiceImpl");
            purchOrderService = (PurchOrderService)c.newInstance();
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

    public DBRecSet getInventoryReceivablePOItems(csSecurity security, String vendorId)
    throws daiRemoteServiceException
    {
        DBRecSet ret = null;
        try {
            ret =  purchOrderService.getInventoryReceivablePOItems(security, vendorId);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getInventoryReceivablePOItems failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

    public DBRecSet getPayablePurchases(csSecurity security, String dateFilter)
    throws daiRemoteServiceException
    {
        DBRecSet ret = null;
        try {
            ret =  purchOrderService.getPayablePurchases(security, dateFilter);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getPayablePurchases failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

	public String[] postInvoiceReceipt(csSecurity security,
                                    DBRecSet poData)
    throws daiRemoteServiceException
	{
        try {
            String[] ret = purchOrderService.postInvoiceReceipt(security, poData);
            return ret;
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::postInvoiceReceipt failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	public String[] postBillReceipt(csSecurity security,
                                    DBRecSet poData)
    throws daiRemoteServiceException
	{
        try {
            String ret[] = purchOrderService.postBillReceipt(security, poData);
            return ret;
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::postBillReceipt failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	public void postPurchOrderPayments(csSecurity security, DBRecSet payVouchers)
	throws daiRemoteServiceException
    {
        try {
            purchOrderService.postPurchOrderPayments(security, payVouchers);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::postPurchOrderPayments failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

	public void voidPurchOrderPayment(csSecurity security, DBRec payVouchAttribs)
	throws daiRemoteServiceException
    {
        try {
            purchOrderService.voidPurchOrderPayment(security, payVouchAttribs);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::voidPurchOrderPayment failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public void receiveInventory(csSecurity security, DBRecSet poItemsData)
    throws daiRemoteServiceException
    {
        try {
            purchOrderService.receiveInventory(security, poItemsData);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::receiveInventory failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public DBRecSet getInvoicablePOItems(csSecurity security, String poId)
    throws daiRemoteServiceException
    {
        DBRecSet ret;
        try {
            ret = purchOrderService.getInvoicablePOItems(security, poId);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getinvoicablePOItems failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
    }

    public DBRecSet getChecksToPrint(csSecurity security)
    throws daiRemoteServiceException
    {
        DBRecSet ret;
        try {
            ret = purchOrderService.getChecksToPrint(security);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getChecksToPrint failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
    }

    public void cancelPurchaseOrder(csSecurity security, String poId)
    throws daiRemoteServiceException
    {
        try {
            purchOrderService.cancelPurchaseOrder(security, poId);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::calcelPurchaseOrder failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public void createCheckPrintScratchData(csSecurity security,
                                            String beginCheckNum,
                                            String endCheckNum)
    throws daiRemoteServiceException
    {
        try {
            purchOrderService.createCheckPrintScratchData(security, beginCheckNum, endCheckNum);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::createCheckPrintScratchData failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }
}

