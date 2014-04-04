
//Title:        Client Server DB Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.


package dai.server.shipmentService;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import dai.idl.rmi.ShipmentService;
import dai.server.serverShared.ServerUtils;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cash_receiptObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class ShipmentRMIServiceImpl extends UnicastRemoteObject implements ShipmentService
{

    ShipmentServiceImpl shipmentService;
    SessionMetaData     _sessionMeta;
    ServerUtils         _serverUtils = new ServerUtils();
    csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
    csDBAdapter _dbAdapter = dbAdapterFactory.getDBAdapter();

	public ShipmentRMIServiceImpl() throws RemoteException
	{
        super();
        shipmentService = new ShipmentServiceImpl();
        _sessionMeta = _sessionMeta.getInstance();
	}

    public Vector getShipableOrderItems(csSecurity security, String orderId, String locality)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        Vector ret = null;
        try {
            _dbAdapter.beginTrans(security);
            ret = shipmentService.getShipableOrderItems(orderId, locality);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::constructor failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

    public DBRecSet getShipableOrders(csSecurity security, String locality, DBAttributes[] filters)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        DBRecSet ret = null;
        try {
            _dbAdapter.beginTrans(security);
            ret = shipmentService.getShipableOrders(locality, filters);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::getShipableOrders failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

	public String createShipment(csSecurity security,
                                DBRec headerData,
                                Vector custOrdItemObjs,
                                default_accountsObj defaultAcctsObj)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);

        String ret = "";

        try {
            _dbAdapter.beginTrans(security);
            ret = shipmentService.createShipment(custOrdItemObjs, headerData, defaultAcctsObj);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::createShipment failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }

        return ret;
    }

	public void deleteShipment(csSecurity security, String shipmentId)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);

        String ret = "";

        try {
            _dbAdapter.beginTrans(security);
            shipmentService.deleteShipment(shipmentId);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::deleteShipment failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

	public void deleteOrder(csSecurity security, String orderId)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);

        String ret = "";

        try {
            _dbAdapter.beginTrans(security);
            shipmentService.deleteOrder(orderId);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::deleteOrder failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

	public void updateShipmentCharges(csSecurity security, DBRecSet shipmentData)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);

        try {
            _dbAdapter.beginTrans(security);
            shipmentService.updateShipmentCharges(shipmentData);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::updateShipmentCharges failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }


    public shipmentObj[] getPayableShipments(csSecurity security, String locality)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        shipmentObj[] ret = null;

        try {
            _dbAdapter.beginTrans(security);
            ret =  shipmentService.getPayableShipments(locality);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::getPayableShipments failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
    }

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
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
            //String ret = shipmentService.createCashReceipt(cashRcptData);
            String ret = shipmentService.createCashReceipt_updateTotalPaid(cashRcptData);
            _dbAdapter.endTrans(security);
            return ret;
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::createCashReceipt failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }
    public String updateCashReceipts_totalPaid(csSecurity security, cash_receiptObj _cash_receiptObj)
      throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
            String ret = shipmentService.updateCashReceipts_totalPaid(_cash_receiptObj);
            _dbAdapter.endTrans(security);
            return ret;
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::updateCashReceipt failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }
    public void deleteCashReceipt_updateTotalPaid(csSecurity security, String _cash_receiptID)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
            shipmentService.deleteCashReceipt_updateTotalPaid(_cash_receiptID);
            _dbAdapter.endTrans(security);
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::deleteCashReceipt failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }
	public String createCreditMemo(csSecurity security, DBRecSet shipItemData)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
            String ret = shipmentService.createCreditMemo(shipItemData);
            _dbAdapter.endTrans(security);
            return ret;
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::createCreditMemo failure"+
                        "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Returns the ID of the new customer record.
	public String createCustFromProspect(csSecurity security, DBRec pspectData)
    throws RemoteException, daiRemoteServiceException
	{
        _sessionMeta.setClientServerSecurity(security);
        try {
            _dbAdapter.beginTrans(security);
            String ret = shipmentService.createCustFromProspect(pspectData);
            _dbAdapter.endTrans(security);
            return ret;
        } catch (Exception e) {
            _dbAdapter.rollback(security);
            String msg = this.getClass().getName()+"::createCustFromProspect failure"+
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
            ShipmentRMIServiceImpl service = new ShipmentRMIServiceImpl();
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

