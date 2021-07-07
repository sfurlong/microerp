
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Your description

package dai.server.shipmentService;

import java.util.*;
import dai.shared.csAdapters.*;
import dai.shared.cmnSvcs.*;
import dai.shared.businessObjs.*;
import dai.server.serverShared.*;

public class CreateShipmentSQL
{

	//Get the correct Client Server Adapter.
	csDBAdapterFactory dbAdapterFactory = null;
	csInventoryAdapterFactory inventoryAdapterFactory = null;

	//Get access to the DB.
	csDBAdapter dbAdapter = null;

	//Get access to the Inventory Service
	csInventoryAdapter inventoryAdapter = null;

	//Get access to the Session Data
	SessionMetaData sessionMeta = null;

	csLoggerAdapter  _logger = null;

	String className;
	public CreateShipmentSQL()
	{
		//Get instances of singletons.
		sessionMeta     = sessionMeta.getInstance();
		_logger         = csLoggerAdapterFactory.getInstance().getLoggerAdapter();
		className       = this.getClass().getName();
	}


	public String getShipableOrdersSQL (String locality, DBAttributes[] filters)
	{
		String sqlStmt;
		sqlStmt = "select distinct cust_order.id, is_prepaid from cust_order, cust_order_item ";
		sqlStmt = sqlStmt + " where cust_order.locality = cust_order_item.locality " +
				  " and cust_order.id = cust_order_item.id " +
				  " and cust_order.locality = '" + locality + "'" +
				  " and (qty_ordered > qty_shipped " +
				  " or qty_shipped is null)";
		//Add the filtering Data
		for (int i=0; i<filters.length; i++)
		{
			if (filters[i] != null)
			{
				sqlStmt = sqlStmt + " and " +
						  filters[i].getName() + " = " +
						  "'" + filters[i].getValue() + "'";
			}
		}

		return sqlStmt;
	}

	public void executeHeaderSQL(String shipmentId, DBRec shipHeaderAttribs, Vector itemObjs)
	throws daiException
	{
		dbAdapterFactory  = dbAdapterFactory.getInstance();
		dbAdapter       = dbAdapterFactory.getDBAdapter();

		if (itemObjs == null) return;

		//Get the Order Info from the Order Item object vector.
		//Since the stuff we need is the same on all the items, we
		//just get the first element in the vector.
		cust_order_itemObj itemObj = (cust_order_itemObj) itemObjs.elementAt(0);

		try
		{
			dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
									 getHeaderSQL(shipmentId, shipHeaderAttribs, itemObj));

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::executeHeaderSQL failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	//Copy the Order Items to the new Shipment.
	public void executeItemSQL(String shipmentId, Vector itemObjs)
	throws daiException
	{
		dbAdapterFactory  = dbAdapterFactory.getInstance();
		dbAdapter       = dbAdapterFactory.getDBAdapter();

		if (itemObjs == null) return;

		//Local vars.
		int shipmentDetailIdIndex = 0;
		String sqlStmt;
		boolean isInsideRepair  = false;
		boolean isOutsideRepair = false;

		//Loop through all the Order Items in the vector and
		//create equivelent records in the shipment table.
		for (int i=0; i<itemObjs.size(); i++)
		{
			//Get the next Order Item object from the vector.
			cust_order_itemObj ordItemObj = (cust_order_itemObj) itemObjs.elementAt(i);
			if (ordItemObj.get_is_external_repair() != null && ordItemObj.get_is_external_repair().equals("Y"))
			{
				isOutsideRepair = true;
			}
			if (ordItemObj.get_is_internal_repair() != null && ordItemObj.get_is_internal_repair().equals("Y"))
			{
				isInsideRepair = true;
			}

			shipmentDetailIdIndex++;

			sqlStmt = getItemSQL(shipmentId, shipmentDetailIdIndex, ordItemObj);

			try
			{
				//Copy the Selected Order Items to the Shipment
				dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
										 sqlStmt);

				//Updat the Account Detail for the sales account
				String acctId = ordItemObj.get_account_id();
				int i_qtyShip = ordItemObj._get_qty_to_ship();
				String s_unitPrice = ordItemObj.get_unit_price();
				double d_exdPrice = i_qtyShip * Double.parseDouble(s_unitPrice);
				ServerUtils.postNewLedgerEntry(ordItemObj.get_account_id(),
											   shipmentId, //Trans Ref,
											   daiFormatUtil.getCurrentDate(),
											   account_detailObj.TRANS_TYPE_CREATE_SHIP, //TransType
											   null, //debit
											   Double.toString(d_exdPrice),	//credit,
											   ordItemObj.get_description1()); //note

				double d_exdPurchPrice = 0.0;
    			String purchPrice = null;
				if (isInsideRepair) {
                    //Don't update the CGS or the Inventory Accts.
                    //Just skip to the next line item.
					continue;
				} else if (isOutsideRepair) {
                    //Use the outside repair cost for the CGS and Inventory Accts.
                    purchPrice = ordItemObj.get_outside_repair_cost();
                } else {
					//Update the account detail for the CGS & Inventory
					//First must get the cost of the item using the FIFO
					//accounting method.
					sqlStmt = " select item_purch_price, detail_id, qty_avail_for_inventory_accting from item_inventory where " +
							  " id = '" + ordItemObj.get_item_id() + "' and " +
							  item_inventoryObj.ADJUSTMENT_TYPE + "='" + item_inventoryObj.ADJUST_TYPE_PURCH_ORDER + "' and " +
							  " (qty_avail_for_inventory_accting > 0) and " +
							  " locality = '" + sessionMeta.getLocality() + "'" +
							  " order by date_created asc";

					DBRecSet attribSet = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);

					if (attribSet.getSize() > 0)
					{
						//Take the first one
						purchPrice = attribSet.getRec(0).getAttribVal(item_inventoryObj.ITEM_PURCH_PRICE);

						//Qty Avail for accting field in the item inventory table
						String sQtyAvailForAccting = attribSet.getRec(0).getAttribVal(item_inventoryObj.QTY_AVAIL_FOR_INVENTORY_ACCTING);

						int i_qtyAvailForAccting = Integer.parseInt(sQtyAvailForAccting) - i_qtyShip;

						sqlStmt = "update item_inventory set "+ item_inventoryObj.QTY_AVAIL_FOR_INVENTORY_ACCTING + "=" + Integer.toString(i_qtyAvailForAccting) +
								  " where id = '" + ordItemObj.get_item_id() + "' and " +
								  " detail_id = " + attribSet.getRec(0).getAttribVal(item_inventoryObj.DETAIL_ID) + " and " +
								  " locality = '" + sessionMeta.getLocality() + "'";
						dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);

					} else
					{
						//Use the purchase price from the item table
						sqlStmt = "select " + itemObj.PURCHASE_PRICE + " from item where " +
								  " id = '" + ordItemObj.get_item_id() + "' and " +
								  " locality = '" + sessionMeta.getLocality() + "'";
						DBRecSet purchPriceAttribs = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
						if (purchPriceAttribs.getSize() > 0)
						{
							purchPrice = purchPriceAttribs.getRec(0).getAttribVal(itemObj.PURCHASE_PRICE);
						}
					}
				}

                if (purchPrice == null) purchPrice = "0.0";
    			d_exdPurchPrice = i_qtyShip * Double.parseDouble(purchPrice);

				//Add a journal entry for CGS
				FinanceAcctsDataCache finAcctDefaults = FinanceAcctsDataCache.getInstance();
				ServerUtils.postNewLedgerEntry(finAcctDefaults.getDefaultCGSAcctId(),
											   shipmentId, //Trans Ref,
											   daiFormatUtil.getCurrentDate(),
											   account_detailObj.TRANS_TYPE_CREATE_SHIP, //TransType
											   Double.toString(d_exdPurchPrice), //debit
											   null, //credit,
											   ordItemObj.get_description1()); //note

				//Add a journal entry for Inventory
				ServerUtils.postNewLedgerEntry(finAcctDefaults.getDefaultInventoryAcctId(),
											   shipmentId, //Trans Ref,
											   daiFormatUtil.getCurrentDate(),
											   account_detailObj.TRANS_TYPE_CREATE_SHIP, //TransType
											   null, //debit
											   Double.toString(d_exdPurchPrice), //credit,
											   ordItemObj.get_description1()); //note

			} catch (Exception e)
			{
				e.printStackTrace();
				String msg = this.getClass().getName()+"::executeItemSQL failure"+
							 "\n"+e.getLocalizedMessage();
				_logger.logError(sessionMeta.getClientServerSecurity(), msg);
				throw new daiException(msg, null);
			}
		}
	}

	//This will update the total value field in the shipment header.
	public void executeShipmentHeaderUpdateSQL(String shipmentId, Vector itemObjs)
	throws daiException
	{
		dbAdapterFactory  = dbAdapterFactory.getInstance();
		dbAdapter       = dbAdapterFactory.getDBAdapter();

		if (itemObjs == null) return;

		//Local vars.
		String subtotSqlStmt, totValueSqlStmt, exp;
		//Get the next Order Item object from the vector.
		cust_order_itemObj itemObj = (cust_order_itemObj) itemObjs.elementAt(0);

		exp = " shipment.id = " + "'" + shipmentId + "'" +
			  " and shipment.locality = '" + itemObj.get_locality() + "'";

		subtotSqlStmt = " update shipment set subtotal = " +
						" (select sum(extended_price) from shipment_item " +
						" where shipment_item.id = '" + shipmentId + "'" +
						" and shipment_item.locality = '" + itemObj.get_locality() + "')" +
						" where " + exp;

		try
		{
			//Updat ethe database with the new subtotal.
			dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
									 subtotSqlStmt);


			//Get the new shipment Header object so
			//we can calculate the totals.
			Vector vect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
													  new shipmentObj(), exp);
			shipmentObj shipObj = (shipmentObj)vect.firstElement();

			//Get all the fields we need for the calculation
			String s_subtot = shipObj.get_subtotal();
			String s_totTax = shipObj.get_total_tax();
			String s_totShipping = shipObj.get_total_shipping();
			String s_totDiscount = shipObj.get_total_discount();
			if (s_subtot == null) s_subtot = "0";
			if (s_totTax == null) s_totTax = "0";
			if (s_totShipping == null) s_totShipping = "0";
			if (s_totDiscount == null) s_totDiscount = "0";
			double d_subtot = Double.parseDouble(s_subtot);
			double d_totTax = Double.parseDouble(s_totTax);
			double d_totShipping = Double.parseDouble(s_totShipping);
			double d_totDiscount = Double.parseDouble(s_totDiscount);

			//Calculate the total.
			double totShipmentVal = d_subtot + d_totTax + d_totShipping - d_totDiscount;

			//The sql for updating the total shipment value
			totValueSqlStmt = " update shipment set total_value = " +
							  new Double(totShipmentVal).toString() +
							  " where " + exp;

			dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
									 totValueSqlStmt);
                        //If necessary, update the total cash received
                        if ( Double.parseDouble(shipObj.get_total_cash_received()) > totShipmentVal ) {
                          String totCashReceivedSqlStmt = " update shipment set total_cash_received = " +
							  new Double(totShipmentVal).toString() +
							  " where " + exp;
                          dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
									 totCashReceivedSqlStmt);
                        }

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::executeShipmentHeaderUpdateSQL failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	public void executeOrderUpdateSQL(String shipmentId, Vector itemObjs)
	throws daiException
	{
		dbAdapterFactory  = dbAdapterFactory.getInstance();
		dbAdapter       = dbAdapterFactory.getDBAdapter();

		if (itemObjs == null) return;

		//Local vars.
		String exp;

		//Loop through all the Order Items in the vector and
		//update the qty shipped and the qty back ordered.
		for (int i=0; i<itemObjs.size(); i++)
		{
			//Get the next Order Item object from the vector.
			cust_order_itemObj ordItemObj = (cust_order_itemObj) itemObjs.elementAt(i);

			//Update the QtyOrd and QtyBackOrd in the BusinessObj.
			String  s_qtyOrdered = ordItemObj.get_qty_ordered();
			String  s_qtyShipped = ordItemObj.get_qty_shipped();
			String  s_qtyBackOrder = ordItemObj.get_qty_backorder();
			if (s_qtyOrdered == null) s_qtyOrdered = "0";
			if (s_qtyShipped == null) s_qtyShipped = "0";
			if (s_qtyBackOrder == null)	s_qtyBackOrder = "0";

			int qtyOrdered = Integer.parseInt(s_qtyOrdered);
			int qtyShipped = Integer.parseInt(s_qtyShipped);
			int qtyBackOrder = Integer.parseInt(s_qtyBackOrder);
			int  qtyToShip = ordItemObj._get_qty_to_ship();	//Note Transient (non-persistent)

			//More error checking
			if ((qtyToShip + qtyShipped) > qtyOrdered)
			{
				throw new daiException("Invalid Value For Qty To Ship", this);
			}
			qtyShipped = qtyShipped + qtyToShip;
			qtyBackOrder = qtyOrdered - qtyShipped;

			//Set Business Object with the new values.
			ordItemObj.set_qty_shipped(new Integer(qtyShipped).toString());
			ordItemObj.set_qty_backorder(new Integer(qtyBackOrder).toString());

			exp = " id = " + "'" + ordItemObj.get_id() + "'" +
				  " and locality = '" + ordItemObj.get_locality() + "'" +
				  " and detail_id = " + ordItemObj.get_detail_id();

			try
			{
				dbAdapter.update(sessionMeta.getClientServerSecurity(),
								 ordItemObj,
								 exp);
			} catch (Exception e)
			{
				e.printStackTrace();
				String msg = this.getClass().getName()+"::executeOrderUpdateSQL failure"+
							 "\n"+e.getLocalizedMessage();
				_logger.logError(sessionMeta.getClientServerSecurity(), msg);
				throw new daiException(msg, null);
			}
		}
	}

	public void updateItemInventory(String shipmentId)
	throws daiException
	{
		String exp = "";
		dbAdapterFactory      = dbAdapterFactory.getInstance();
		dbAdapter           = dbAdapterFactory.getDBAdapter();
		inventoryAdapterFactory = inventoryAdapterFactory.getInstance();
		inventoryAdapter    = inventoryAdapterFactory.getInventoryAdapter();

		try
		{
			exp = " id = '"+shipmentId+"' and locality = '"+sessionMeta.getLocality()+"'";
			Vector shipItemVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
															  new shipment_itemObj(),
															  exp);

			//Convert from vector to array
			shipment_itemObj[] shipItemObjs = (shipment_itemObj[])shipItemVect.toArray(new shipment_itemObj[]{});
			shipItemVect = null; //Take out the Garbage.

			//Post the Shipment Items to Inventory
			inventoryAdapter.postShipmentItemsToInventory(sessionMeta.getClientServerSecurity(),
														  shipItemObjs);

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::updateItemInventory failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}


	//---------------------------------------------//
	//--------  PRIVATE METHODS -------------------//
	//---------------------------------------------//

	private String getHeaderSQL(String shipmentId, DBRec shipHeaderAttribs, cust_order_itemObj itemObj)
	{
		DBAttributes totShipChargeAttr = shipHeaderAttribs.getAttrib(shipmentObj.TOTAL_SHIPPING);
		String totShipCharge = totShipChargeAttr.getValue();
		if (totShipCharge == null || totShipCharge.trim() == "") totShipCharge = "0.00";

		//Check to see if this is a prepaid order
		String isPrepaid = shipHeaderAttribs.getAttribVal(cust_orderObj.IS_PREPAID);
                String prepaid_amt = "0.0";
                if ( isPrepaid != null && isPrepaid.equals("Y") ) {
                    cash_receiptObj _cash_receiptObj = new cash_receiptObj();

                    String sqlStmt = "SELECT sum("+shipmentObj.TOTAL_CASH_RECEIVED+") FROM shipment "+
                                     "WHERE "+shipmentObj.ORDER_NUM+"='"+itemObj.get_id()+"' and ("+shipmentObj.IS_CANCELED+" is null or "+shipmentObj.IS_CANCELED+"='N')";

                    String sqlStmt2 = "SELECT sum("+_cash_receiptObj.PAYMENT_AMT+") FROM cash_receipt "+
                                     "WHERE "+_cash_receiptObj.SHIPMENT_ID+"='"+itemObj.get_id()+"'";

                    String sqlStmt3 = "SELECT sum("+_cash_receiptObj.PAYMENT_AMT+") FROM cash_receipt "+
                                     "WHERE "+_cash_receiptObj.SHIPMENT_ID+" in "+
                                     "(select "+shipmentObj.ID+" FROM shipment WHERE "+shipmentObj.ORDER_NUM+"='"+itemObj.get_id()+"')";

                    double d_shipment_received = 0;
                    double d_payment_shipment = 0;
                    double d_payment_prepaid = 0;

                    try {
                      String s_shipment_received = "0";
                      String s_payment_shipment = "0";
                      String s_payment_prepaid =  "0";

                      DBRecSet attribSet = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
                      DBRecSet attribSet2 = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt2);
                      DBRecSet attribSet3 = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt3);

                      if ( attribSet.getSize() > 0 )
                        s_shipment_received = attribSet.getRec(0).getAttribVal(".SUM");
                      if ( attribSet2.getSize() > 0 )
                        s_payment_prepaid = attribSet2.getRec(0).getAttribVal(".SUM");
                      if ( attribSet3.getSize() > 0 )
                        s_payment_shipment =  attribSet3.getRec(0).getAttribVal(".SUM");

                      if ( s_shipment_received != null && !s_shipment_received.equals("") )
                        d_shipment_received = Double.parseDouble( s_shipment_received );
                      if ( s_payment_prepaid != null && !s_payment_prepaid.equals("") )
                        d_payment_prepaid = Double.parseDouble(s_payment_prepaid);
                      if ( s_payment_shipment != null && !s_payment_shipment.equals("") )
                        d_payment_shipment = Double.parseDouble(s_payment_shipment);
                    } catch (Exception e) {
                      d_shipment_received = 0;
                      d_payment_prepaid = 0;
                      d_payment_shipment = 0;
                      System.out.println("dai.server.shipmentService.CreateShipmentSQL:"+e);
                    }
                    double d_prepaid_amt = d_payment_prepaid - (d_shipment_received - d_payment_shipment);
                    prepaid_amt = Double.toString(d_prepaid_amt);
                }

//                cash_receiptObj _cash_receiptObj = new cash_receiptObj();
//                String sqlStmt = "SELECT "+_cash_receiptObj.PAYMENT_AMT+", "+ _cash_receiptObj.DATE_CREATED + " FROM cash_receipt "+
//                                 "WHERE "+_cash_receiptObj.SHIPMENT_ID+"='"+itemObj.get_id()+"' "+
//                                 "ORDER BY "+_cash_receiptObj.DATE_CREATED + " asc";
//                DBRecSet attribSet = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
//
//                String sqlStmt2 = "SELECT "+shipmentObj.TOTAL_VALUE+", "+ shipmentObj.DATE_CREATED+ " FROM shipment "+
//                                 "WHERE "+shipmentObj.ORDER_NUM+"='"+itemObj.get_id()+"' "+
//                                 "ORDER BY "+shipmentObj.DATE_CREATED + " asc";
//                DBRecSet attribSet2 = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt2);
//
//                double d_prepaid = 0;
//                Date asof = null;
//                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
//                for ( int i=0; i < attribSet.getSize(); i++) {
//                  try {
//                    String s_prepaid = attribSet.getRec(i).getAttribVal(_cash_receiptObj.PAYMENT_AMT);
//                    String s_asof = attribSet.getRec(i).getAttribVal(shipmentObj.DATE_CREATED);
//                    if ( s_prepaid != null && !s_prepaid.equals("") )
//                      d_prepaid = Double.parseDouble( s_prepaid );
//                    if ( s_asof != null && !s_asof.equals("") )
//                      asof = sdf.parse(s_asof);
//                  } catch (Exception e) { }
//                  for ( int j=0; j <attribSet2.getSize() && d_prepaid > 0; j++ ) {
//                    String s_total = attribSet2.getRec(j).getAttribVal(shipmentObj.TOTAL_VALUE);
//                    String s_date = attribSet2.getRec(j).getAttribVal(shipmentObj.DATE_CREATED);
//                    Date d_shipcreated = sdf.parse(s_date);
//
//                  }
//                }

        String currentDate = daiFormatUtil.cnvtToDBDateFormat(daiFormatUtil.getCurrentDate());
		//Create the SQL statement.  This will copy most fields from the
		//Order Header table to the Shipment Header Table.
		String sqlStmt;
		sqlStmt = "insert into SHIPMENT (" +
				  shipmentObj.ID+", "                                   +
				  shipmentObj.LOCALITY+", "                             +
				  shipmentObj.DATE_CREATED+", "                         +
				  shipmentObj.CREATED_BY  +", "                         +
				  shipmentObj.ORDER_NUM   +", "                         +
				  shipmentObj.SHIPMENT_TYPE+", "                        +
				  shipmentObj.PO_NUM      +", "                         +
				  shipmentObj.PO_DATE+", "                              +
				  shipmentObj.CUSTOMER_ID +", "                         +
				  shipmentObj.CUSTOMER_NAME +", "                         +
				  shipmentObj.SHIPTO_ATTN + ", " +
				  shipmentObj.SHIPTO_ADDR1+", "                         +
				  shipmentObj.SHIPTO_ADDR2+", "                         +
				  shipmentObj.SHIPTO_ADDR3+", "                         +
				  shipmentObj.SHIPTO_ADDR4+", "                         +
				  shipmentObj.SHIPTO_CITY +", "                         +
				  shipmentObj.SHIPTO_STATE_CODE+", "                    +
				  shipmentObj.SHIPTO_ZIP  +", "                         +
				  shipmentObj.SHIPTO_COUNTRY_CODE+", "                  +
				  shipmentObj.SHIPTO_COUNTRY_NAME+", "                  +
				  shipmentObj.BILLTO_ATTN + ", " +
				  shipmentObj.BILLTO_ADDR1+", "                         +
				  shipmentObj.BILLTO_ADDR2+", "                         +
				  shipmentObj.BILLTO_ADDR3+", "                         +
				  shipmentObj.BILLTO_ADDR4+", "                         +
				  shipmentObj.BILLTO_CITY +", "                         +
				  shipmentObj.BILLTO_STATE_CODE+", "                    +
				  shipmentObj.BILLTO_ZIP  +", "                         +
				  shipmentObj.BILLTO_COUNTRY_CODE+", "                  +
				  shipmentObj.BILLTO_COUNTRY_NAME+", "                  +
				  shipmentObj.SALES_REP   +", "                         +
				  shipmentObj.IS_REPAIR+", "                            +
				  shipmentObj.COMMISION_AMOUNT+", "                     +
				  shipmentObj.SHIPMENT_STATUS+", "                         +
				  shipmentObj.DATE_SHIPPED+", "                         +
				  shipmentObj.DATE_PROMISED+", "                        +
				  shipmentObj.DATE_NEEDED+", "                          +
				  shipmentObj.PAYMENT_TERMS+", "                        +
				  shipmentObj.FREIGHT_TERMS+", "                        +
				  shipmentObj.CARRIER_ID  +", "                         +
				  shipmentObj.CARRIER_NAME+", "                         +
				  shipmentObj.COD         +", "                         +
				  shipmentObj.FOB         +", "                         +
				  shipmentObj.TAX_RATE      +", "                       +
				  shipmentObj.BOL_NUM  +", "                            +
				  shipmentObj.AIR_BILL_NUM+", "                         +
				  shipmentObj.CURRENCY    +", "                         +
				  shipmentObj.EXCHANGE_RATE+", "                        +
				  shipmentObj.SHIPTO_IS_DROPSHIP+", "                   +
				  shipmentObj.TOTAL_CASH_RECEIVED+", "                  +
				  shipmentObj.SUBTOTAL      +", "                       +
				  shipmentObj.TOTAL_VALUE   +", "                       +
				  shipmentObj.TOTAL_DISCOUNT+", "                       +
				  shipmentObj.TOTAL_TAX     +", "                       +
				  shipmentObj.TOTAL_SHIPPING+") "                       +
				  "select "                               +
				  "'" + shipmentId + "', "                 +
				  "'" + sessionMeta.getLocality() + "', "  +  //Locality
				  "'" + currentDate + "' , "               +  //Date Created
				  "'" + sessionMeta.getUserId() + "' ,"    +  //Created By
				  "'" + itemObj.get_id()  + "' ,"          +  //Cust Ord ID
				  "'',    "                                +  //Shipment Type
				  cust_orderObj.PO_NUM             +", "                         +
				  cust_orderObj.PO_DATE            +", "                              +
				  cust_orderObj.CUSTOMER_ID +", "                         +
				  cust_orderObj.CUSTOMER_NAME +", "                         +
				  cust_orderObj.SHIPTO_ATTN+", "                         +
				  cust_orderObj.SHIPTO_ADDR1+", "                         +
				  cust_orderObj.SHIPTO_ADDR2+", "                         +
				  cust_orderObj.SHIPTO_ADDR3+", "                         +
				  cust_orderObj.SHIPTO_ADDR4+", "                         +
				  cust_orderObj.SHIPTO_CITY +", "                         +
				  cust_orderObj.SHIPTO_STATE_CODE+", "                    +
				  cust_orderObj.SHIPTO_ZIP  +", "                         +
				  cust_orderObj.SHIPTO_COUNTRY_CODE+", "                  +
				  cust_orderObj.SHIPTO_COUNTRY_NAME+", "                  +
				  cust_orderObj.BILLTO_ATTN+", "                         +
				  cust_orderObj.BILLTO_ADDR1+", "                         +
				  cust_orderObj.BILLTO_ADDR2+", "                         +
				  cust_orderObj.BILLTO_ADDR3+", "                         +
				  cust_orderObj.BILLTO_ADDR4+", "                         +
				  cust_orderObj.BILLTO_CITY +", "                         +
				  cust_orderObj.BILLTO_STATE_CODE+", "                    +
				  cust_orderObj.BILLTO_ZIP  +", "                         +
				  cust_orderObj.BILLTO_COUNTRY_CODE+", "                  +
				  cust_orderObj.BILLTO_COUNTRY_NAME+", "                  +
				  cust_orderObj.SALES_REP   +", "                         +
				  cust_orderObj.IS_REPAIR+", "                            +
				  cust_orderObj.COMMISION_AMOUNT+", "                     +
				  cust_orderObj.ORDER_STATUS+", "                         +
				  "'" + currentDate +"', "             +
				  cust_orderObj.DATE_PROMISED+", "                        +
				  cust_orderObj.DATE_NEEDED+", "                          +
				  cust_orderObj.PAYMENT_TERMS+", "                        +
				  cust_orderObj.FREIGHT_TERMS+", "                        +
				  cust_orderObj.CARRIER_ID  +", "                         +
				  cust_orderObj.CARRIER_NAME+", "                         +
				  cust_orderObj.COD         +", "                         +
				  cust_orderObj.FOB + ", "                           +
				  cust_orderObj.TAX_RATE      +", "                       +
				  cust_orderObj.BOL_NUM  +", "                            +
				  cust_orderObj.AIR_BILL_NUM+", "                         +
				  cust_orderObj.CURRENCY    +", "                         +
				  cust_orderObj.EXCHANGE_RATE+", "                        +
				  cust_orderObj.SHIPTO_IS_DROPSHIP+", "                    +
				  //Statement below will copy the total_cash_recieved if this
				  //is a prepaid order or just "0.0" otherwise.
				  //(isPrepaid != null && isPrepaid.equals("Y") ? cust_orderObj.TOTAL_CASH_RECEIVED : "0.0") +" , "                                                +
				  //Statement below will copy the calculated prepaid amount if this
				  //is a prepaid order or just "0.0" otherwise.
                                  (isPrepaid != null && isPrepaid.equals("Y") ? prepaid_amt : "0.0") +" , "                                                +
				  cust_orderObj.SUBTOTAL      +", "                       +
				  cust_orderObj.TOTAL_VALUE   +", "                       +
				  cust_orderObj.TOTAL_DISCOUNT+", "                       +
				  cust_orderObj.TOTAL_TAX     +", "                       +
				  totShipCharge                         +
				  " from cust_order where "                 +
				  "id='" + itemObj.get_id() + "' and "                +
				  "locality='" + itemObj.get_locality() + "'";

		return sqlStmt;
	}

	private String getItemSQL(  String shipmentId,
								int shipmentDetailId,
								cust_order_itemObj orderItemObj)
	{
		//Get today's date
		String dateCreated = daiFormatUtil.cnvtToDBDateFormat(daiFormatUtil.getCurrentDate());

		//Create the SQL statement.  This will copy most fields from the
		//Order Header table to the Shipment Header Table.
		String sqlStmt;
		sqlStmt =   "insert into SHIPMENT_ITEM ( "                  +
					shipment_itemObj.ID  +", "                                        +
					shipment_itemObj.LOCALITY+", "                                    +
					shipment_itemObj.DETAIL_ID+", "                                   +
					shipment_itemObj.DATE_CREATED+", "                                +
					shipment_itemObj.ITEM_ID     +", "                                +
					shipment_itemObj.DESCRIPTION1+", "                                +
					shipment_itemObj.DESCRIPTION2+", "                                +
					shipment_itemObj.ECCN        +", "                                +
					shipment_itemObj.HTS         +", "                                +
					shipment_itemObj.IS_HAZMAT   +", "                                +
					shipment_itemObj.HAZMAT_DESCRIPT+", "                             +
					shipment_itemObj.QTY_ORDERED    +", "                             +
					shipment_itemObj.QTY_AVAIL      +", "                             +
					shipment_itemObj.QTY_SHIPPED    +", "                             +
					shipment_itemObj.QTY_BACKORDER  +", "                             +
					shipment_itemObj.UNIT_PRICE     +", "                             +
					shipment_itemObj.EXTENDED_PRICE +", "                             +
					shipment_itemObj.IS_INTERNAL_REPAIR +", "                         +
					shipment_itemObj.IS_EXTERNAL_REPAIR +", "                         +
					shipment_itemObj.ACCOUNT_ID +", "                         +
					shipment_itemObj.ACCOUNT_NAME +", "                         +
					shipment_itemObj.ORDER_ITEM_DETAIL_ID + ", " +
					shipment_itemObj.OUTSIDE_REPAIR_COST + ", " +
					shipment_itemObj.NOTE1          +", "                             +
					shipment_itemObj.NOTE2          +") "                             +
					"select  "                                      +
					"'" + shipmentId + "', "                        +
					"'" + sessionMeta.getLocality() + "', "         +  //Locality
					shipmentDetailId + ", "                         +
					"'" + dateCreated + "' , "                      +  //Date Created
					cust_order_itemObj.ITEM_ID     +", "                                +
					cust_order_itemObj.DESCRIPTION1+", "                                +
					cust_order_itemObj.DESCRIPTION2+", "                                +
					cust_order_itemObj.ECCN        +", "                                +
					cust_order_itemObj.HTS         +", "                                +
					cust_order_itemObj.IS_HAZMAT   +", "                                +
					cust_order_itemObj.HAZMAT_DESCRIPT+", "                             +
					cust_order_itemObj.QTY_ORDERED    +", "                             +
					cust_order_itemObj.QTY_AVAIL      +", "                             +
					orderItemObj._get_qty_to_ship()   +","           +
					cust_order_itemObj.QTY_BACKORDER  +", "                             +
					cust_order_itemObj.UNIT_PRICE     +", "                             +
					cust_order_itemObj.UNIT_PRICE + " *  " + orderItemObj._get_qty_to_ship() + ", " +  //Calc the extd price                            +
					cust_order_itemObj.IS_INTERNAL_REPAIR          +", "                +
					cust_order_itemObj.IS_EXTERNAL_REPAIR          +", "                +
					cust_order_itemObj.ACCOUNT_ID +", "                         +
					cust_order_itemObj.ACCOUNT_NAME +", "                         +
					cust_order_itemObj.DETAIL_ID + ", " +
					cust_order_itemObj.OUTSIDE_REPAIR_COST + ", " +
					cust_order_itemObj.NOTE1          +", "                             +
					cust_order_itemObj.NOTE2                        +
					" from cust_order_item  where "                      +
					"id='" + orderItemObj.get_id() + "' and "       +
					"locality='" + orderItemObj.get_locality() + "'"  +
					"and detail_id=" + orderItemObj.get_detail_id();

		return sqlStmt;
	}
}