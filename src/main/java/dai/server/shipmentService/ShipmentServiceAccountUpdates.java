package dai.server.shipmentService;

import java.util.Vector;

import dai.server.serverShared.ServerUtils;
import dai.shared.businessObjs.account_detailObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.businessObjs.shipment_itemObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiException;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csInventoryAdapter;
import dai.shared.csAdapters.csInventoryAdapterFactory;
import dai.shared.csAdapters.csLoggerAdapter;
import dai.shared.csAdapters.csLoggerAdapterFactory;

public class ShipmentServiceAccountUpdates
{

	//Declare singletons
	csDBAdapterFactory dbAdapterFactory = null;
	csInventoryAdapterFactory inventoryAdapterFactory = null;

	csDBAdapter dbAdapter = null;
	csInventoryAdapter inventoryAdapter = null;
	SessionMetaData sessionMeta = null;
	csLoggerAdapter  _logger = null;

	public ShipmentServiceAccountUpdates() {
		//Get instances of singletons.
		sessionMeta     = sessionMeta.getInstance();
		_logger         = csLoggerAdapterFactory.getInstance().getLoggerAdapter();
	}

	public void updateCreateShipmentAccounts(String shipId, default_accountsObj defaultAcctsObj)
	throws daiException
	{
		String exp = "";
		dbAdapterFactory        = dbAdapterFactory.getInstance();
		inventoryAdapterFactory = inventoryAdapterFactory.getInstance();
		dbAdapter               = dbAdapterFactory.getDBAdapter();
		inventoryAdapter        = inventoryAdapterFactory.getInventoryAdapter();

		try
		{
			exp = " id = '"+shipId+"' and locality = '"+shipment_itemObj.getObjLocality()+"'";
			Vector shipVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
														  new shipmentObj(),
														  exp);

			if (shipVect.size() == 0) return;

			//Convert from vector to array
			shipmentObj shipObj = (shipmentObj)shipVect.firstElement();
			shipVect = null; //Take out the Garbage.

			//Local vars.
			String acctExp = "";
			account_detailObj acctDetailObj;

			String s_totShipping = shipObj.get_total_shipping();
			String s_totTax = shipObj.get_total_tax();
			String s_totValue = shipObj.get_total_value();
			if (s_totShipping == null) s_totShipping = "0.0";
			if (s_totTax == null) s_totTax = "0.0";
			if (s_totValue == null)	s_totValue = "0.0";

			//Add Accts Receivable amt to the Acct Detail table
            ServerUtils.postNewLedgerEntry(defaultAcctsObj.get_accts_receivable_id(),
                                                shipId, //Trans Ref,
                                                daiFormatUtil.getCurrentDate(),
                                                account_detailObj.TRANS_TYPE_CREATE_SHIP, //TransType
                                                s_totValue, //debit
                                                null, //credit,
                                                shipObj.get_customer_name()); //note

			//Add Shipping Acct amt to the Acct Detail table
            if (Double.parseDouble(s_totShipping) != 0 && defaultAcctsObj.get_shipping_out_id() != null) {
                ServerUtils.postNewLedgerEntry(defaultAcctsObj.get_shipping_out_id(),
                                                shipId, //Trans Ref,
                                                daiFormatUtil.getCurrentDate(),
                                                account_detailObj.TRANS_TYPE_CREATE_SHIP, //TransType
                                                null, //debit
                                                s_totShipping, //credit,
                                                shipObj.get_customer_name()); //note
            }

			//Add Tax Amt Payable amt to the Acct Detail table
            if (Double.parseDouble(s_totTax) != 0 && defaultAcctsObj.get_sales_tax_payable_id() != null) {
                ServerUtils.postNewLedgerEntry(defaultAcctsObj.get_sales_tax_payable_id(),
                                                shipId, //Trans Ref,
                                                daiFormatUtil.getCurrentDate(),
                                                account_detailObj.TRANS_TYPE_CREATE_SHIP, //TransType
                                                null, //debit
                                                s_totTax, //credit,
                                                shipObj.get_customer_name()); //note
            }
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::updateHeaderAccounts failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}
}
