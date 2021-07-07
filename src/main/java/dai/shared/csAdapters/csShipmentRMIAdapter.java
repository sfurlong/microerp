
//Title:        Client Server Shipment Service Stub Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:


package dai.shared.csAdapters;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Vector;

import dai.idl.rmi.ShipmentService;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cash_receiptObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

public class csShipmentRMIAdapter extends csShipmentAdapter
{
    static ShipmentService shipmentService;

    private Logger _logger;

	public csShipmentRMIAdapter(String host)
	{
        System.setSecurityManager (new daiSecurityManager());
        String lookup = "rmi://"+host+"/"+ShipmentService.SERVER_NAME;

        _logger = _logger.getInstance();

        try {
            shipmentService = (ShipmentService)Naming.lookup(lookup);
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

	// Constructor for purposes of Fat Client//
	public csShipmentRMIAdapter()
	{
        _logger = _logger.getInstance();

        try {
            Class c = Class.forName("dai.server.shipmentService.ShipmentRMIServiceImpl");
            shipmentService = (ShipmentService)c.newInstance();
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

    public DBRecSet getShipableOrders(csSecurity security, String locality, DBAttributes[] attribs)
    throws daiRemoteServiceException
    {
        DBRecSet ret = null;
        try {
            ret =  shipmentService.getShipableOrders(security, locality, attribs);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getShipableOrders failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

    public Vector getShipableOrderItems(csSecurity security, String orderId, String locality)
    throws daiRemoteServiceException
    {
        Vector ret = null;
        try {
            ret =  shipmentService.getShipableOrderItems(security, orderId, locality);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getShipableOrderItems failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

	public String createShipment(csSecurity security, DBRec headerData, Vector custOrdItemObjs, default_accountsObj defaultAcctsObj)
	throws daiRemoteServiceException
	{
        String ret = "";
        try {
            ret = shipmentService.createShipment(security, headerData, custOrdItemObjs, defaultAcctsObj);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::createShipment failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
	}

	public void deleteShipment(csSecurity security, String shipmentId)
	throws daiRemoteServiceException
	{
        try {
            shipmentService.deleteShipment(security, shipmentId);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::deleteShipment failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	public void deleteOrder(csSecurity security, String orderId)
	throws daiRemoteServiceException
	{
        try {
            shipmentService.deleteOrder(security, orderId);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::deleteOrder failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	public void updateShipmentCharges(csSecurity security, DBRecSet shipmentData)
	throws daiRemoteServiceException
	{
        try {
            shipmentService.updateShipmentCharges(security, shipmentData);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::updateShipmentCharges failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    public shipmentObj[] getPayableShipments(csSecurity security, String locality)
    throws daiRemoteServiceException
    {
        shipmentObj[] ret = null;
        try {
            ret =  shipmentService.getPayableShipments(security, locality);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getPayableShipments failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

	public String createCashReceipt(csSecurity security, DBRecSet cashRcptData)
	throws daiRemoteServiceException
	{
        try {

            String ret = shipmentService.createCashReceipt(security, cashRcptData);
            return ret;

        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::createCashReceipt failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

        public String updateCashReceipts_totalPaid(csSecurity security, cash_receiptObj _cash_receiptObj)
	throws daiRemoteServiceException
	{
        try {

            String ret = shipmentService.updateCashReceipts_totalPaid(security, _cash_receiptObj);
            return ret;

        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::updateCashReceipt failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

        public void deleteCashReceipt_updateTotalPaid(csSecurity security, String _cash_receiptID)
	throws daiRemoteServiceException
	{
        try {

            shipmentService.deleteCashReceipt_updateTotalPaid(security, _cash_receiptID);


        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::deleteCashReceipt failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	public String createCreditMemo(csSecurity security, DBRecSet shipItemData)
	throws daiRemoteServiceException
	{
        try {

            String ret = shipmentService.createCreditMemo(security, shipItemData);
            return ret;

        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::createCreditMemo failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	public String createCustFromProspect(csSecurity security, DBRec pspectData)
	throws daiRemoteServiceException
	{
        try {

            String ret = shipmentService.createCustFromProspect(security, pspectData);
            return ret;

        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::createCustFromProspect failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}
}

