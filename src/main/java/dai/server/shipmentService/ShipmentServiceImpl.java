
//Title:        Client Server DB Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.


package dai.server.shipmentService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import dai.server.serverShared.ServerUtils;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.account_detailObj;
import dai.shared.businessObjs.cash_receiptObj;
import dai.shared.businessObjs.cust_orderObj;
import dai.shared.businessObjs.cust_order_itemObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.customer_contactObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.global_settingsObj;
import dai.shared.businessObjs.itemObj;
import dai.shared.businessObjs.item_inventoryObj;
import dai.shared.businessObjs.prospectObj;
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

public class ShipmentServiceImpl
{
	csLoggerAdapter     _logger;
	CreateShipmentSQL   shipmentSQL;
	SessionMetaData     _sessionMeta;

	public ShipmentServiceImpl()
	{
		//This is just used to house the sql.  A little neater this way.
		shipmentSQL = new CreateShipmentSQL();
		_logger = csLoggerAdapterFactory.getInstance().getLoggerAdapter();
		_sessionMeta = _sessionMeta.getInstance();
	}

	public DBRecSet getShipableOrders(String locality, DBAttributes[] filters)
	throws daiException
	{
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

		//This will select all the Orders which have not been shipped.
		//qty_shipped values that are null have not been shipped yet.
		String sqlStmt = shipmentSQL.getShipableOrdersSQL(locality, filters);
        DBRecSet dbRecSet = null;
		try
		{
			dbRecSet = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
					 							 sqlStmt);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::getShipableOrders failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}

		return dbRecSet;
	}

	public Vector getShipableOrderItems(String orderId, String locality)
	throws daiException
	{
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

		try
		{
			//This will select all the items which have not been shipped.
			//qty_shipped values that are null have not been shipped yet.
			return dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
											   new cust_order_itemObj(),
											   "id = '" +orderId +"'"+
											   " and locality = '" + locality +"'" +
											   " and (qty_ordered > qty_shipped " +
											   " or qty_shipped is null)");
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::getShipableOrderItems failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	public String createShipment(Vector custOrdItemObjs, DBRec headerAttribs, default_accountsObj defaultAcctsObj)
	throws daiException
	{
		//Let's make sure there are some order items first.  If not, throw
		//an exception.
		if (custOrdItemObjs == null)
		{
			throw new daiException("No Order Items to create shipment.", this);
		}

		//Generate the new Shipment Id
		String shipmentId = generateShipmentId();

		try
		{
			//Do a select insert for Header data.  Cust_order --> cust_shipment
			shipmentSQL.executeHeaderSQL(shipmentId, headerAttribs, custOrdItemObjs);

			//Update the Order's qtyShipped and qtyBackord
			//This needs to be done before we copy the order
			//items to the shipment items.
			shipmentSQL.executeOrderUpdateSQL(shipmentId, custOrdItemObjs);

			//Copy the Order Items to the new Shipment.
			shipmentSQL.executeItemSQL(shipmentId, custOrdItemObjs);

			//Update the Shipment header totals.
			shipmentSQL.executeShipmentHeaderUpdateSQL(shipmentId, custOrdItemObjs);

			//Update the Inventory for each detail item.
			shipmentSQL.updateItemInventory(shipmentId);

			//Update the Financial Accounts accordingly
			ShipmentServiceAccountUpdates acctUpdates = new ShipmentServiceAccountUpdates();
			acctUpdates.updateCreateShipmentAccounts(shipmentId, defaultAcctsObj);

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::createShipment failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}

		return shipmentId;
	}

	public void deleteShipment(String shipmentId)
	throws daiException
	{
        try {
    		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
	    	csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();
            csInventoryAdapterFactory inventoryAdapterFactory = csInventoryAdapterFactory.getInstance();
            csInventoryAdapter inventoryAdapter = inventoryAdapterFactory.getInventoryAdapter();

            //Delete all the entries in the GL for this Shipment.
            String sqlStmt = " delete from " + account_detailObj.TABLE_NAME +
                            " where " + account_detailObj.TRANS_REF + " = '" + shipmentId + "'" +
                            " and locality = '" + account_detailObj.getObjLocality() + "'";
            dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

            //Adjust Inventory
            sqlStmt = " select " + shipmentObj.ORDER_NUM + " , " +
                                shipment_itemObj.ITEM_ID + " , " +
                                shipment_itemObj.ORDER_ITEM_DETAIL_ID + " , " +
                                shipment_itemObj.QTY_ORDERED + " , " +
                                shipment_itemObj.QTY_SHIPPED +
                                " from " + shipment_itemObj.TABLE_NAME +
                                " , " + shipmentObj.TABLE_NAME +
                                " where shipment.id = shipment_item.id and " +
                                " shipment.locality = shipment_item.locality and " +
                                " shipment.id = '" + shipmentId + "'" +
                                " and shipment.locality = '" + shipment_itemObj.getObjLocality() + "'";
            DBRecSet retAttribs = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

            for (int i=0; i<retAttribs.getSize(); i++) {
                String ordId = retAttribs.getRec(i).getAttribVal(shipmentObj.ORDER_NUM);
                String itemId = retAttribs.getRec(i).getAttribVal(shipment_itemObj.ITEM_ID);
                String qtyShipped = retAttribs.getRec(i).getAttribVal(shipment_itemObj.QTY_SHIPPED);
                String qtyOrdered = retAttribs.getRec(i).getAttribVal(shipment_itemObj.QTY_ORDERED);
                String ordItemDetailId = retAttribs.getRec(i).getAttribVal(shipment_itemObj.ORDER_ITEM_DETAIL_ID);

                //Add back the qty shipped.  This
                //will re-allocate the inventory by this amount.
                int i_qtyShipped = Integer.parseInt(qtyShipped);
                qtyShipped = Integer.toString(i_qtyShipped);

                DBRecSet itemData = new DBRecSet();
                DBRec attribs = new DBRec();
                attribs.addAttrib(new DBAttributes(itemObj.ID, itemId));
                attribs.addAttrib(new DBAttributes(itemObj.BACKORDER_TO_CUST_QTY, qtyShipped));
                attribs.addAttrib(new DBAttributes(itemObj.ONHAND_QTY, qtyShipped));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.ADJUSTMENT_REASON, shipmentId));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.ADJUSTMENT_TYPE, item_inventoryObj.ADJUST_TYPE_DELETE_SHIPMENT));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.DO_MANUAL_ADJUST_CUST_BACK, "Y"));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.DO_MANUAL_ADJUST_ONHAND, "Y"));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.NOTE, "Deleted Shipment "+shipmentId));
                itemData.addRec(attribs);

                inventoryAdapter.postInventoryAdjustment(_sessionMeta.getClientServerSecurity(),
                                                        itemData);

                //Update the qty shipped in the order.
                int i_qtyShip = Integer.parseInt(qtyShipped);
                int i_qtyOrd  = Integer.parseInt(qtyOrdered);
                int i_qtyBack = 0;
                if (i_qtyOrd - i_qtyShip > 0) {
                    i_qtyBack = i_qtyOrd - i_qtyShip;
                }
                sqlStmt = " update cust_order_item " +
                            " set " + cust_order_itemObj.QTY_SHIPPED + " = " +
                            cust_order_itemObj.QTY_SHIPPED + " - " + qtyShipped + " , " +
                            " qty_backorder = qty_backorder + " + qtyShipped +                             " where id = '" + ordId + "' and " +
                            " locality = '" + cust_order_itemObj.getObjLocality() + "' and " +
                            " detail_id = " + ordItemDetailId;
                dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);
            }

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::deleteShipment failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	public void deleteOrder(String orderId)
	throws daiException
	{
        try {
    		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
	    	csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();
            csInventoryAdapterFactory inventoryAdapterFactory = csInventoryAdapterFactory.getInstance();
            csInventoryAdapter inventoryAdapter = inventoryAdapterFactory.getInventoryAdapter();

            //Adjust Inventory
            String sqlStmt = " select " + cust_order_itemObj.ITEM_ID + " , " +
                                cust_order_itemObj.QTY_ORDERED +
                                " from " + cust_order_itemObj.TABLE_NAME +
                                " where id = '" + orderId + "'" +
                                " and locality = '" + cust_order_itemObj.getObjLocality() + "'";
            DBRecSet retAttribs = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

            for (int i=0; i<retAttribs.getSize(); i++) {
                String itemId = retAttribs.getRec(i).getAttribVal(cust_order_itemObj.ITEM_ID);
                String qtyOrder = retAttribs.getRec(i).getAttribVal(cust_order_itemObj.QTY_ORDERED);
                //Reverse the sign of the qty ordered.  This
                //will reduce the inventory by this amount.
                if (qtyOrder == null) qtyOrder = "0";
                int i_qtyOrder = Integer.parseInt(qtyOrder) * -1;
                qtyOrder = Integer.toString(i_qtyOrder);

                DBRecSet itemData = new DBRecSet();
                DBRec attribs = new DBRec();
                attribs.addAttrib(new DBAttributes(itemObj.ID, itemId));
                attribs.addAttrib(new DBAttributes(itemObj.BACKORDER_TO_CUST_QTY, qtyOrder));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.ADJUSTMENT_REASON, orderId));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.ADJUSTMENT_TYPE, item_inventoryObj.ADJUST_TYPE_DELETE_ORDER));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.DO_MANUAL_ADJUST_CUST_BACK, "Y"));
                attribs.addAttrib(new DBAttributes(item_inventoryObj.NOTE, "Deleted Order "+orderId));
                itemData.addRec(attribs);

                //Don't Adjust Inventory if we don't have a good Item Id
                if (itemId != null) {
                    inventoryAdapter.postInventoryAdjustment(_sessionMeta.getClientServerSecurity(),
                                                            itemData);
                }
            }

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::deleteShipment failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

    //The service will exept the following attributes
    //shipmentObj.SHIPMENT_ID
    //shipmentObj.TOTAL_TAX
    //shipmentObj.TOTAL_SHIPPING
    //shipmentObj.AIR_BILL_NUM
    //shipmentObj.CUSTOMER_NAME
    //default_accountsObj.SHIPPING_OUT_ID
    //default_accountsObj.ACCTS_RECEIVABLE_ID
    //default_accountsObj.SALES_TAX_PAYABLE_ID
	public void updateShipmentCharges(DBRecSet shipmentData)
	throws daiException
	{
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

		//Let's make sure the shipment Obj is valid first.  If not, throw
		//an exception.
		if (shipmentData == null || shipmentData.getSize() == 0)
		{
			throw new daiException("Invalid Shipment Data.", this);
		} else if (shipmentData.getSize() > 1) {
			throw new daiException("Expected only one Shipment object.  Found > 1.", this);
        }

        String shipmentId = shipmentData.getRec(0).getAttribVal(shipmentObj.ID);
        String s_newTaxes = shipmentData.getRec(0).getAttribVal(shipmentObj.TOTAL_TAX);
        String s_newShipping = shipmentData.getRec(0).getAttribVal(shipmentObj.TOTAL_SHIPPING);
        String trackingNum = shipmentData.getRec(0).getAttribVal(shipmentObj.AIR_BILL_NUM);
        String receivableAcctId = shipmentData.getRec(0).getAttribVal(default_accountsObj.ACCTS_RECEIVABLE_ID);
        String shippingAcctId = shipmentData.getRec(0).getAttribVal(default_accountsObj.SHIPPING_OUT_ID);
        String taxAcctId = shipmentData.getRec(0).getAttribVal(default_accountsObj.SALES_TAX_PAYABLE_ID);
        String custName = shipmentData.getRec(0).getAttribVal(shipmentObj.CUSTOMER_NAME);

        if (s_newTaxes == null) s_newTaxes = "0.0";
        if (s_newShipping == null) s_newShipping = "0.0";

		try
		{
			//Get the Old Shipment Header Total Values
			shipmentObj shipObj = new shipmentObj();
			String sqlStmt = " select " + shipmentObj.TOTAL_SHIPPING +
                            ", " + shipmentObj.TOTAL_TAX + ", " +
                            shipmentObj.SUBTOTAL + ", " +
                            shipmentObj.TOTAL_DISCOUNT +
							 " from " + shipmentObj.TABLE_NAME +
                            " where id = '" + shipmentId + "' and " +
                            " locality = '" + shipmentObj.getObjLocality()+"'";
			DBRecSet oldShipmentData = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
															 sqlStmt);

    		if (oldShipmentData == null || oldShipmentData.getSize() == 0)
	    	{
		    	throw new daiException("Invalid Shipment Object.", this);
    		} else if (oldShipmentData.getSize() > 1) {
	    		throw new daiException("Expected only one Shipment object.  Found > 1.", this);
            }

            String s_oldTaxes = oldShipmentData.getRec(0).getAttribVal(shipmentObj.TOTAL_TAX);
            String s_oldShipping = oldShipmentData.getRec(0).getAttribVal(shipmentObj.TOTAL_SHIPPING);
            String s_subtotal = oldShipmentData.getRec(0).getAttribVal(shipmentObj.SUBTOTAL);
            String s_discount = oldShipmentData.getRec(0).getAttribVal(shipmentObj.TOTAL_DISCOUNT);
            if (s_oldTaxes == null) s_oldTaxes = "0.0";
            if (s_oldShipping == null) s_oldShipping = "0.0";
            if (s_subtotal == null) s_subtotal = "0.0";
            if (s_discount == null) s_discount = "0.0";

            double d_totTaxes = Double.parseDouble(s_oldTaxes) + Double.parseDouble(s_newTaxes);
            double d_totShipping = Double.parseDouble(s_oldShipping) + Double.parseDouble(s_newShipping);
            double d_totShipment = (Double.parseDouble(s_subtotal) - Double.parseDouble(s_discount)) +
                                    d_totTaxes + d_totShipping;
          //
          //Update Cash Receipt running-balances and running-total-payments
          //
            cash_receiptObj _cash_receiptObj = new cash_receiptObj();
            Vector objVect = new Vector();
            String exp = cash_receiptObj.SHIPMENT_ID + " = " +
                    "'" + shipmentId + "'";

            //Add order by clause for sorting.
            exp = exp + " order by " + cash_receiptObj.DATE_RECEIVED + ", " + cash_receiptObj.ID;
            try  {
              objVect = dbAdapter.queryByExpression( _sessionMeta.getClientServerSecurity(),
                                        _cash_receiptObj,
                                        exp);
              objVect.trimToSize();
              //Loop through all the retreived business objects and indicate
              //that they already exist in the database.  This is how we know
              //to update instead of insert.
              for (int i=0; i<objVect.size(); i++) {
                  BusinessObject obj = (BusinessObject)objVect.elementAt(i);
                  obj.EXISTS_IN_DB = true;
                  objVect.setElementAt(obj, i);
              }
            }
            catch (Exception e)  {
              //Log to dialog, system.out, disk.
              e.printStackTrace();
              String msg = this.getClass().getName() + "::query failure." +
                          "\n"+e.toString()+"\n"+e.getLocalizedMessage();
              throw new daiException(msg, null);
            }
            //Update vector
            if(objVect != null && objVect.size() != 0){
            double runningTotalPaid = 0;
            double runningBalance = d_totShipment;
            for(int i=0; i<objVect.size(); i++)  {
             cash_receiptObj temp_cash_receiptObj = (cash_receiptObj)objVect.elementAt(i);


             //calculate running total paid
             temp_cash_receiptObj.set_total_due((new Double(runningBalance)).toString());
             runningTotalPaid += Double.parseDouble(temp_cash_receiptObj.get_payment_amt());
             temp_cash_receiptObj.set_total_paid((new Double(runningTotalPaid)).toString());
             runningBalance -= Double.parseDouble(temp_cash_receiptObj.get_payment_amt());
             objVect.setElementAt(temp_cash_receiptObj, i);
            }//end for
          }//end if

          //form expression for the where part of the query
          DBAttributes attribs[] = _cash_receiptObj.getImmutableAttribs();
          exp = "";
          for (int i=0; i < attribs.length; i++)  {
            if (i > 0)  {
                exp =  exp + " and ";
            }
            exp = exp + attribs[i].getName() +
                    " = " +
                    "'" + attribs[i].getValue().trim() + "'";
          }
          //Update Cash Receipts with the updated Vector
          for(int i=0; i<objVect.size(); i++)  {
            cash_receiptObj temp_cash_receiptObj = (cash_receiptObj)objVect.elementAt(i);
            if (temp_cash_receiptObj.EXISTS_IN_DB) {
                String fullExp = exp + " and " + temp_cash_receiptObj.ID + " = '" + temp_cash_receiptObj.get_id() + "'";
                dbAdapter.update(  _sessionMeta.getClientServerSecurity(),
                            temp_cash_receiptObj,
                            fullExp);
            } else {
                throw new daiException("Cash Receipt cannot be updated, not present in table", null);
            }
          }
        //
        //End Update for Cash Receipts
        //

            //Update The shipment header total values with the new values.
			sqlStmt = " update shipment set " +
                            shipmentObj.TOTAL_TAX + " = " + Double.toString(d_totTaxes) + ", " +
                            shipmentObj.TOTAL_SHIPPING + " = " + Double.toString(d_totShipping) + ", " +
                            shipmentObj.TOTAL_VALUE + " = " + Double.toString(d_totShipment) + ", " +
                            shipmentObj.AIR_BILL_NUM + " = " + ServerUtils.addQuotes(trackingNum) +
                            " where id = '" + shipmentId + "' and " +
                            " locality = '" + shipmentObj.getObjLocality()+"'";
			dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
									 sqlStmt);

			//Add Shipping Acct amt to the Acct Detail table
            if (Double.parseDouble(s_newShipping) != 0 && shippingAcctId != null) {
                String shipDebit = null;
                String shipCredit = s_newShipping;
                //Incase we have a negative value...
                if (Double.parseDouble(s_newShipping) < 0) {
                    shipDebit = Double.toString(Double.parseDouble(shipCredit)*-1);;
                    shipCredit = null;
                }
                ServerUtils.postNewLedgerEntry(shippingAcctId,  //Acct ID
                            shipmentId,         //Ref
                            daiFormatUtil.getCurrentDate(),
                            account_detailObj.TRANS_TYPE_UPDATE_SHIP, //transType
                            shipDebit,    //debit
                            shipCredit,             //Credit
                            custName); //note
                String acDebit = s_newShipping;
                String acCredit = null;
                //Incase we have a negative value...
                if (Double.parseDouble(s_newShipping) < 0) {
                    acCredit = Double.toString(Double.parseDouble(acDebit)*-1);;
                    acDebit = null;
                }
                ServerUtils.postNewLedgerEntry(receivableAcctId,  //Acct ID
                            shipmentId,         //Ref
                            daiFormatUtil.getCurrentDate(),
                            account_detailObj.TRANS_TYPE_UPDATE_SHIP, //transType
                            acDebit,    //debit
                            acCredit,             //Credit
                            custName); //note
            }

			//Add Taxes Acct amt to the Acct Detail table
            if (Double.parseDouble(s_newTaxes) != 0 && taxAcctId != null) {
                String taxDebit = null;
                String taxCredit = s_newTaxes;
                //Incase we have a negative value...
                if (Double.parseDouble(s_newTaxes) < 0) {
                    taxDebit = Double.toString(Double.parseDouble(taxCredit)*-1);
                    taxCredit = null;
                }
                ServerUtils.postNewLedgerEntry(taxAcctId,  //Acct ID
                            shipmentId,         //Ref
                            daiFormatUtil.getCurrentDate(),
                            account_detailObj.TRANS_TYPE_UPDATE_SHIP, //transType
                            taxDebit,    //debit
                            taxCredit,             //Credit
                            custName); //note
                String acDebit = s_newTaxes;
                String acCredit = null;
                //Incase we have a negative value...
                if (Double.parseDouble(s_newTaxes) < 0) {
                    acCredit = Double.toString(Double.parseDouble(acDebit)*-1);;
                    acDebit = null;
                }
                ServerUtils.postNewLedgerEntry(receivableAcctId,  //Acct ID
                            shipmentId,         //Ref
                            daiFormatUtil.getCurrentDate(),
                            account_detailObj.TRANS_TYPE_UPDATE_SHIP, //transType
                            acDebit,    //debit
                            acCredit,             //Credit
                            custName); //note
            }
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::updateShipmentCharges failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	public shipmentObj[] getPayableShipments(String locality)
	throws daiException
	{
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();
		shipmentObj[]       shipObjs = null;

		try
		{
			//This will select all the shipments in which the Total_Value has
			//not been received yet (i.e. total_cash_received < total_value)
			//total_cash_received values that are null means we have not received
			//any payment yet at all.
			Vector shipObjsVect = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															  new shipmentObj(),
															  " locality = '" + locality +"'" +
															  " and (total_value > total_cash_received " +
															  " or total_cash_received is null)");
			//Convert from vector to array
			shipObjs = (shipmentObj[])shipObjsVect.toArray(new shipmentObj[]{});
			shipObjsVect = null; //Take out the Garbage.

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::getPayableShipments failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
		return shipObjs;
	}

    // cash_receiptObj.SHIPMENT_ID
    // cash_receiptObj.PAYMENT_METHOD
    // cash_receiptObj.CHECK_NUM
    // cash_receiptObj.PAYMENT_AMT
    // cash_receiptObj.CC_NUM
    // cash_receiptObj.CC_EXP_DATE
    // cash_receiptObj.DATE_RECEIVED
    // cash_receiptObj.RECEIVABLE_ACCT_ID
    // cash_receiptObj.RECEIVABLE_ACCT_NAME
    // cash_receiptObj.DEPOSIT_ACCT_ID
    // cash_receiptObj.DEPOSIT_ACCT_NAME
    // cash_receiptObj.CUST_ID
    // cash_receiptObj.CUST_NAME
    // cash_receiptObj.NOTE
    // cash_receiptObj.IS_PREPAID_ORDER
    //Returns the Newly generated Receipt ID
	public String createCashReceipt(DBRecSet cashRcptData)
	throws daiException
	{
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

		//Let's make sure the Cash Rcpt Obj is valid first.  If not, throw
		//an exception.
		if (cashRcptData == null || cashRcptData.getSize() == 0)
		{
			throw new daiException("Invalid Cash Receipt Object.", this);
		} else if (cashRcptData.getSize() > 1) {
			throw new daiException("Expected only one Cash Receipt object.  Found > 1.", this);
        }

        String payMethod = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.PAYMENT_METHOD);
        String payAmt = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.PAYMENT_AMT);
        String shipmentId = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.SHIPMENT_ID);
        String dateReceived = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.DATE_RECEIVED);
        String custId = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CUST_ID);
        String custName = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CUST_NAME);
        String receivableAcctId = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.RECEIVABLE_ACCT_ID);
        String receivableAcctName = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.RECEIVABLE_ACCT_NAME);
        String depositAcctId = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.DEPOSIT_ACCT_ID);
        String depositAcctName = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.DEPOSIT_ACCT_NAME);
        String checkNum = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CHECK_NUM);
        String ccNum = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CC_NUM);
        String ccExp = cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CC_EXP_DATE);
        String isPrePaidOrd = cashRcptData.getRec(0).getAttribVal(cash_receiptObj._IS_PREPAID_ORDER);

        if (checkNum != null) checkNum = "'" + checkNum + "'";
        if (ccNum != null) ccNum = "'" + ccNum + "'";
        if (ccExp != null) ccExp = "'" + ccExp + "'";
        String cashRcptNote = null;
        if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
            cashRcptNote = "Source Transaction was a Pre-Paid Order";
        }

		try
		{
            //Insert to the new Cash Receipt
            int cashRcptSEQ = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
													dbAdapter.SEQUENCE_CASH_RECEIPT);
            String cashRcptId = "REC"+ daiFormatUtil.padIntLeft(cashRcptSEQ, 5);
            String cashRcptSQL = " insert into " + cash_receiptObj.TABLE_NAME + " (" +
                cash_receiptObj.ID + ", " +
                cash_receiptObj.LOCALITY + ", " +
                cash_receiptObj.CREATED_BY + ", " +
                cash_receiptObj.DATE_CREATED + ", " +
                cash_receiptObj.PAYMENT_METHOD + ", " +
                cash_receiptObj.PAYMENT_AMT + ", " +
                cash_receiptObj.SHIPMENT_ID + ", " +
                cash_receiptObj.DATE_RECEIVED + ", " +
                cash_receiptObj.CHECK_NUM + ", " +
                cash_receiptObj.CC_NUM + ", " +
                cash_receiptObj.CC_EXP_DATE + ", " +
                cash_receiptObj.RECEIVABLE_ACCT_ID + ", " +
                cash_receiptObj.RECEIVABLE_ACCT_NAME + ", " +
                cash_receiptObj.DEPOSIT_ACCT_ID + ", " +
                cash_receiptObj.DEPOSIT_ACCT_NAME + ", " +
                cash_receiptObj.CUST_ID + ", " +
                cash_receiptObj.CUST_NAME + ", " +
                cash_receiptObj.NOTE + " " +
            ") values (" +
                "'" + cashRcptId + "', " +
                "'"+ cash_receiptObj.getObjLocality() + "', " +
                "'"+ _sessionMeta.getUserId() + "', " +
                "'"+ daiFormatUtil.getCurrentDate() + "', " +
                ServerUtils.addQuotes(payMethod) + ", " +
                ""+ payAmt + ", " +
                "'"+ shipmentId + "', " +
                ServerUtils.addQuotes(dateReceived) + ", " +
                checkNum + ", " +
                ccNum + ", " +
                ccExp + ", " +
                ServerUtils.addQuotes(receivableAcctId) + ", " +
                ServerUtils.addQuotes(receivableAcctName) + ", " +
                ServerUtils.addQuotes(depositAcctId) + ", " +
                ServerUtils.addQuotes(depositAcctName) + ", " +
                ServerUtils.addQuotes(custId) + ", " +
                ServerUtils.addQuotes(custName) + ", " +
                ServerUtils.addQuotes(cashRcptNote) + ")";
            dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
                                        cashRcptSQL);

    	    //Get the tot cash received so far from the Shipment/Order.
            String sqlStmt = null;
            if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
			    sqlStmt = " select " + cust_orderObj.TOTAL_CASH_RECEIVED +
							 " from " + cust_orderObj.TABLE_NAME +
                            " where id = '" + shipmentId + "' and " +
                            " locality = '" + cust_orderObj.getObjLocality()+"'";
            } else {
		    	sqlStmt = " select " + shipmentObj.TOTAL_CASH_RECEIVED +
							 " from " + shipmentObj.TABLE_NAME +
                            " where id = '" + shipmentId + "' and " +
                            " locality = '" + shipmentObj.getObjLocality()+"'";
            }
			DBRecSet transData = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
															 sqlStmt);

    		if (transData == null || transData.getSize() == 0)
	    	{
		    	throw new daiException("Invalid Shipment/Order Object.", this);
            }

            String s_totReceived = null;
            if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
    			s_totReceived = transData.getRec(0).getAttribVal(cust_orderObj.TOTAL_CASH_RECEIVED);
            } else {
    			s_totReceived = transData.getRec(0).getAttribVal(shipmentObj.TOTAL_CASH_RECEIVED);
            }
			if (payAmt == null) payAmt = "0";
			if (s_totReceived == null) s_totReceived = "0";

			double d_totReceived = Double.parseDouble(s_totReceived);
			d_totReceived = d_totReceived + Double.parseDouble(payAmt);

            //Update the tot cash received so far.
            if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
    			sqlStmt = " update cust_order set total_cash_received = " +
							 Double.toString(d_totReceived) +   ", " +
                            " is_prepaid = 'Y' " +
                            " where id = '" + shipmentId + "' and " +
                            " locality = '" + cust_orderObj.getObjLocality()+"'";
            } else {
    			sqlStmt = " update shipment set total_cash_received = " +
							 Double.toString(d_totReceived) +
                            " where id = '" + shipmentId + "' and " +
                            " locality = '" + shipmentObj.getObjLocality()+"'";
            }
			dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
									 sqlStmt);


			//Add Accts Receivable amt to the Acct Detail table
            ServerUtils.postNewLedgerEntry(receivableAcctId,  //Acct ID
                            shipmentId,         //Ref
                            dateReceived,
                            account_detailObj.TRANS_TYPE_RECEIVE_PAY, //transType
                            null,    //debit
                            payAmt,             //Credit
                            custName); //note
            //Add Deposit Acct amt to the Acct Detail table
            ServerUtils.postNewLedgerEntry(depositAcctId,  //Acct ID
                            shipmentId,         //Ref
                            dateReceived,
                            account_detailObj.TRANS_TYPE_RECEIVE_PAY, //transType
                            payAmt,    //debit
                            null,             //Credit
                            custName); //note

            return cashRcptId;

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::createCashReceipt failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

    public String createCashReceipt_updateTotalPaid(DBRecSet cashRcptData) throws daiException  {
      csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
      csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

      //Let's make sure the Cash Rcpt Obj is valid first.  If not, throw
      //an exception.
      if (cashRcptData == null || cashRcptData.getSize() == 0)  {
        throw new daiException("Invalid Cash Receipt Object.", this);
      }
      else if (cashRcptData.getSize() > 1) {
        throw new daiException("Expected only one Cash Receipt object.  Found > 1.", this);
      }

      cash_receiptObj _cash_receiptObj = new cash_receiptObj();
      //Generate a new ID
      int cashRcptSEQ = 0;
      try  {
        cashRcptSEQ = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
                                                                                                     dbAdapter.SEQUENCE_CASH_RECEIPT);
      }
      catch (Exception e)  {
            //Log to dialog, system.out, disk.
            e.printStackTrace();
            String msg = this.getClass().getName() + "::query failure." +
                          "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiException(msg, null);
      }
      String cashRcptId = "REC"+ daiFormatUtil.padIntLeft(cashRcptSEQ, 5);
      _cash_receiptObj.set_id(cashRcptId);
      _cash_receiptObj.set_payment_method(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.PAYMENT_METHOD));
      _cash_receiptObj.set_payment_amt(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.PAYMENT_AMT));
      _cash_receiptObj.set_shipment_id(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.SHIPMENT_ID));
      _cash_receiptObj.set_date_received(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.DATE_RECEIVED));
      _cash_receiptObj.set_cust_id(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CUST_ID));
      _cash_receiptObj.set_cust_name(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CUST_NAME));
      _cash_receiptObj.set_receivable_acct_id(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.RECEIVABLE_ACCT_ID));
      _cash_receiptObj.set_receivable_acct_name(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.RECEIVABLE_ACCT_NAME));
      _cash_receiptObj.set_deposit_acct_id(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.DEPOSIT_ACCT_ID));
      _cash_receiptObj.set_deposit_acct_name(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.DEPOSIT_ACCT_NAME));
      _cash_receiptObj.set_check_num(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CHECK_NUM));
      _cash_receiptObj.set_cc_num(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CC_NUM));
      _cash_receiptObj.set_cc_exp_date(cashRcptData.getRec(0).getAttribVal(cash_receiptObj.CC_EXP_DATE));
      String isPrePaidOrd = cashRcptData.getRec(0).getAttribVal(cash_receiptObj._IS_PREPAID_ORDER);
      //To determine whether an insert or update is needed
      _cash_receiptObj.EXISTS_IN_DB = false;


//        if (_cash_receiptObj.get_check_num() != null) checkNum = "'" + checkNum + "'";
//        if (ccNum != null) ccNum = "'" + ccNum + "'";
//        if (ccExp != null) ccExp = "'" + ccExp + "'";
      String cashRcptNote = null;
      if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
          cashRcptNote = "Source Transaction was a Pre-Paid Order";
          _cash_receiptObj.set_note(cashRcptNote);
      }

//Get all same invoices to calculate balances
      Vector objVect = new Vector();
      String exp = cash_receiptObj.SHIPMENT_ID + " = " +
                    "'" + cashRcptData.getRec(0).getAttribVal(cash_receiptObj.SHIPMENT_ID) + "'";

      //Add order by clause for sorting.
      exp = exp + " order by " + cash_receiptObj.DATE_RECEIVED + ", " + cash_receiptObj.ID;
      try  {
            objVect = dbAdapter.queryByExpression( _sessionMeta.getClientServerSecurity(),
                                        _cash_receiptObj,
                                        exp);
            objVect.trimToSize();
            //Loop through all the retreived business objects and indicate
            //that they already exist in the database.  This is how we know
            //to update instead of insert.
            for (int i=0; i<objVect.size(); i++) {
                BusinessObject obj = (BusinessObject)objVect.elementAt(i);
                obj.EXISTS_IN_DB = true;
                objVect.setElementAt(obj, i);
            }

      }
      catch (Exception e)  {
            //Log to dialog, system.out, disk.
            e.printStackTrace();
            String msg = this.getClass().getName() + "::query failure." +
                          "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiException(msg, null);
      }
      //
      //
      //
      //
      try  {
        //Get the tot cash received so far from the Shipment/Order.
        String sqlStmt = null;
        if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
            sqlStmt = " select " + cust_orderObj.TOTAL_CASH_RECEIVED + " , "
                                      + cust_orderObj.TOTAL_VALUE +
                                         " from " + cust_orderObj.TABLE_NAME +
            " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
            " locality = '" + cust_orderObj.getObjLocality()+"'";
        }
        else {
            sqlStmt = " select " + shipmentObj.TOTAL_CASH_RECEIVED  + ", "
                                     + shipmentObj.TOTAL_VALUE +
                                             " from " + shipmentObj.TABLE_NAME +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + shipmentObj.getObjLocality()+"'";
        }
        DBRecSet transData = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

        boolean isReceiptInsertedIntoVector = false;
        //Check to make sure there is a valid Shipment object
        if (transData == null || transData.getSize() == 0)  {
          throw new daiException("Invalid Shipment/Order Object.", this);
        }
        String s_totReceived = null;
        String s_totValue = null;
        if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
          s_totReceived = transData.getRec(0).getAttribVal(cust_orderObj.TOTAL_CASH_RECEIVED);
          s_totValue = transData.getRec(0).getAttribVal(cust_orderObj.TOTAL_VALUE);
        }
        else {
          s_totReceived = transData.getRec(0).getAttribVal(shipmentObj.TOTAL_CASH_RECEIVED);
          s_totValue = transData.getRec(0).getAttribVal(shipmentObj.TOTAL_VALUE);
        }
        if (_cash_receiptObj.get_payment_amt() == null) _cash_receiptObj.set_payment_amt("0");
        if (s_totReceived == null) s_totReceived = "0";
        if (s_totValue == null) s_totReceived = "0";
        double d_totDue = Double.parseDouble(s_totValue);
        if(objVect == null || objVect.size() == 0){
          _cash_receiptObj.set_total_paid(_cash_receiptObj.get_payment_amt());
          _cash_receiptObj.set_total_due((new Double(d_totDue)).toString());
          objVect.addElement(_cash_receiptObj);
          isReceiptInsertedIntoVector = true;
        }
        double d_totReceived = Double.parseDouble(s_totReceived);
        double d_totalPaid = Double.parseDouble(((cash_receiptObj)objVect.lastElement()).get_total_paid());
        d_totReceived = d_totReceived + Double.parseDouble(_cash_receiptObj.get_payment_amt());

      //
      //
      ///
      //
      //
      //
      //
      //
      //
      double runningBalance = d_totDue;
      //If there are no duplicate payments for a particular invoice
      //If duplicate invoices for a payment, then insert new payment into the
      //appropriate spot and recalculate balances
      //then insert/update payments
      if( objVect != null && objVect.size() != 0 && !isReceiptInsertedIntoVector){
        try  {
          SimpleDateFormat _simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
          java.util.Date dateReceived = _simpleDateFormat.parse(
                _cash_receiptObj.get_date_received());
          double runningTotalPaid = 0;
          for(int i=0; i<objVect.size(); i++)  {
             //special condition if the element is to be the first payment
             if(i==0)  {
                java.util.Date beforeDateReceived = _simpleDateFormat.parse(
                  ((cash_receiptObj)objVect.elementAt(i) ).get_date_received());
                if(dateReceived.before(beforeDateReceived))  {
                  objVect.insertElementAt(_cash_receiptObj,0);
                  isReceiptInsertedIntoVector = true;
                }
                else if(objVect.size() == 1)  {
                  objVect.addElement(_cash_receiptObj);
                  isReceiptInsertedIntoVector = true;
                }
                runningTotalPaid = Double.parseDouble(((cash_receiptObj)objVect.elementAt(0)).get_payment_amt());
                ((cash_receiptObj)objVect.elementAt(i) ).set_total_paid((new Double(runningTotalPaid)).toString());
                ((cash_receiptObj)objVect.elementAt(i) ).set_total_due((new Double(runningBalance)).toString());
                runningBalance -= Double.parseDouble(((cash_receiptObj)objVect.elementAt(0)).get_payment_amt());
             }
             else  {
               //calculate running total paid
               runningTotalPaid += Double.parseDouble(((cash_receiptObj)objVect.elementAt(i)).get_payment_amt());
               ((cash_receiptObj)objVect.elementAt(i) ).set_total_paid((new Double(runningTotalPaid)).toString());
               ((cash_receiptObj)objVect.elementAt(i) ).set_total_due((new Double(runningBalance)).toString());
               runningBalance -= Double.parseDouble(((cash_receiptObj)objVect.elementAt(i)).get_payment_amt());

               if(!isReceiptInsertedIntoVector)  {
                 java.util.Date beforeDateReceived = _simpleDateFormat.parse(
                    ((cash_receiptObj)objVect.elementAt(i) ).get_date_received());
                 if(i+1 < objVect.size())  {
                   java.util.Date afterDateReceived = _simpleDateFormat.parse(
                      ((cash_receiptObj)objVect.elementAt(i+1) ).get_date_received());
                   if( (dateReceived.after(beforeDateReceived) && dateReceived.before(afterDateReceived)) ||
                    (dateReceived.equals(beforeDateReceived) && dateReceived.before(afterDateReceived)) )  {
                      objVect.insertElementAt(_cash_receiptObj,i+1);
                      isReceiptInsertedIntoVector = true;
                   }
                 }
                 else { //make a new spot at the end of the vector and add receipt
                  objVect.insertElementAt(_cash_receiptObj,objVect.size());
                  isReceiptInsertedIntoVector = true;
                 }
               }//end if(!isReceiptInsertedIntoVector)
             }//end else
          }//end for
        }
        catch (Exception e)  {
            //Log to dialog, system.out, disk.
            e.printStackTrace();
            String msg = this.getClass().getName() + "::date format incorrect." +
                          "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiException(msg, null);
        }
      }//end else
      //New Cash Receipt added to the Vector in the appropriate spot
/////
        //form expression for the where part of the query
        DBAttributes attribs[] = _cash_receiptObj.getImmutableAttribs();
        exp = "";
        for (int i=0; i < attribs.length; i++)  {
          if (i > 0)  {
              exp =  exp + " and ";
          }
          exp = exp + attribs[i].getName() +
                  " = " +
                  "'" + attribs[i].getValue().trim() + "'";
        }
        //Insert/update Cash Receipts with the updated Vector
        for(int i=0; i<objVect.size(); i++)  {
          cash_receiptObj temp_cash_receiptObj = (cash_receiptObj)objVect.elementAt(i);
          if (temp_cash_receiptObj.EXISTS_IN_DB) {
              String fullExp = exp + " and " + temp_cash_receiptObj.ID + " = '" + temp_cash_receiptObj.get_id() + "'";
              dbAdapter.update(  _sessionMeta.getClientServerSecurity(),
                          temp_cash_receiptObj,
                          fullExp);
          } else {
              dbAdapter.insert(  _sessionMeta.getClientServerSecurity(),
                      temp_cash_receiptObj);
          }
        }


//        if(d_totReceived != d_totalPaid)
//          throw new daiException("shipping total paid does not correspond with the receipt payment total" , null);
        //Update the tot cash received so far.
        if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
            sqlStmt = " update cust_order set total_cash_received = " +
                                             Double.toString(d_totReceived) +   ", " +
                " is_prepaid = 'Y' " +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + cust_orderObj.getObjLocality()+"'";
        }
        else {
            sqlStmt = " update shipment set total_cash_received = " +
                                             Double.toString(d_totReceived) +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + shipmentObj.getObjLocality()+"'";
        }
        dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

        //Add Accts Receivable amt to the Acct Detail table
        ServerUtils.postNewLedgerEntry(_cash_receiptObj.get_receivable_acct_id(),        //Acct ID
                      _cash_receiptObj.get_shipment_id(),                               //Ref
                      _cash_receiptObj.get_date_received(),
                      account_detailObj.TRANS_TYPE_RECEIVE_PAY, //transType
                      null,                                     //debit
                      _cash_receiptObj.get_payment_amt(),                                   //Credit
                      _cash_receiptObj.get_cust_name());                                //note
        //Add Deposit Acct amt to the Acct Detail table
        ServerUtils.postNewLedgerEntry(_cash_receiptObj.get_deposit_acct_id(),           //Acct ID
                      _cash_receiptObj.get_shipment_id(),                               //Ref
                      _cash_receiptObj.get_date_received(),
                      account_detailObj.TRANS_TYPE_RECEIVE_PAY, //transType
                      _cash_receiptObj.get_payment_amt(),                                   //debit
                      null,                                     //Credit
                      _cash_receiptObj.get_cust_name());                                //note

        return cashRcptId;

      }
      catch (Exception e2)  {
        e2.printStackTrace();
        String msg = this.getClass().getName()+"::createCashReceipt failure"+
                                 "\n"+e2.getLocalizedMessage();
        _logger.logError(_sessionMeta.getClientServerSecurity(), msg);
        throw new daiException(msg, null);
      }
    }

    public String updateCashReceipts_totalPaid(cash_receiptObj _cash_receiptObj) throws daiException  {
      csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
      csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

      //Let's make sure the Cash Rcpt Obj is valid first.  If not, throw
      //an exception.
      if (_cash_receiptObj == null)  {
        throw new daiException("Invalid Cash Receipt Object.", this);
      }
      double d_old_amtPaid = 0;
      _cash_receiptObj.EXISTS_IN_DB = true;


//Get all same invoices to calculate balances
      Vector objVect = new Vector();
      String exp = cash_receiptObj.SHIPMENT_ID + " = " +
                    "'" + _cash_receiptObj.get_shipment_id() + "'";

      //Add order by clause for sorting.
      exp = exp + " order by " + cash_receiptObj.DATE_RECEIVED + ", " + cash_receiptObj.ID;
      try  {
            objVect = dbAdapter.queryByExpression( _sessionMeta.getClientServerSecurity(),
                                        _cash_receiptObj,
                                        exp);
            objVect.trimToSize();
            //Loop through all the retreived business objects and indicate
            //that they already exist in the database.  This is how we know
            //to update instead of insert.
            for (int i=0; i<objVect.size(); i++) {
                BusinessObject obj = (BusinessObject)objVect.elementAt(i);
                obj.EXISTS_IN_DB = true;
                objVect.setElementAt(obj, i);
            }

      }
      catch (Exception e)  {
            //Log to dialog, system.out, disk.
            e.printStackTrace();
            String msg = this.getClass().getName() + "::query failure." +
                          "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiException(msg, null);
      }
      //
      //
      //
      //Get initial balance/Amount Due from a shipment or order
      //Get the tot cash received so far from the Shipment/Order.
      try  {
        String sqlStmtOrder = null;
        String sqlStmtShip = null;
        String sqlStmt = null;
            sqlStmtOrder = " select " + cust_orderObj.TOTAL_CASH_RECEIVED + " , "
                                      + cust_orderObj.TOTAL_VALUE +
                                         " from " + cust_orderObj.TABLE_NAME +
            " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
            " locality = '" + cust_orderObj.getObjLocality()+"'";
            sqlStmtShip = " select " + shipmentObj.TOTAL_CASH_RECEIVED + ", "
                                     + shipmentObj.TOTAL_VALUE +
                                             " from " + shipmentObj.TABLE_NAME +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + shipmentObj.getObjLocality()+"'";

        DBRecSet transDataOrder = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmtOrder);
        DBRecSet transDataShip = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmtShip);

        DBRecSet transData = null;
        String isPrePaidOrd = null;
        if(transDataOrder != null && transDataOrder.getSize() != 0)  {
          transData = transDataOrder;
          isPrePaidOrd = "Y";
        }
        else if(transDataShip != null && transDataShip.getSize() != 0)  {
          transData = transDataShip;
          isPrePaidOrd = "N";
        }
        if (transData == null || transData.getSize() == 0)  {
          throw new daiException("Invalid Shipment/Order Object.", this);
        }
        String s_totReceived = null;
        String s_totValue = null;
        if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
          s_totReceived = transData.getRec(0).getAttribVal(cust_orderObj.TOTAL_CASH_RECEIVED);
          s_totValue = transData.getRec(0).getAttribVal(cust_orderObj.TOTAL_VALUE);
        }
        else {
          s_totReceived = transData.getRec(0).getAttribVal(shipmentObj.TOTAL_CASH_RECEIVED);
          s_totValue = transData.getRec(0).getAttribVal(shipmentObj.TOTAL_VALUE);
        }
        if (_cash_receiptObj.get_payment_amt() == null) _cash_receiptObj.set_payment_amt("0");
        if (s_totReceived == null) s_totReceived = "0";
        if (s_totValue == null) s_totReceived = "0";
        double d_totReceived = Double.parseDouble(s_totReceived);
        double d_totDue = Double.parseDouble(s_totValue);



      //
      //
      //



      //update payments
      if(objVect != null || objVect.size() != 0){
        double runningTotalPaid = 0;
        double runningBalance = d_totDue;
        for(int i=0; i<objVect.size(); i++)  {
             cash_receiptObj temp_cash_receiptObj = (cash_receiptObj)objVect.elementAt(i);

             //calculate running total paid
             if(temp_cash_receiptObj.get_id().equals(_cash_receiptObj.get_id())) {
               d_old_amtPaid = Double.parseDouble(temp_cash_receiptObj.get_payment_amt());
               temp_cash_receiptObj = _cash_receiptObj;
             }
             temp_cash_receiptObj.set_total_due((new Double(runningBalance)).toString());
             runningTotalPaid += Double.parseDouble(temp_cash_receiptObj.get_payment_amt());
             temp_cash_receiptObj.set_total_paid((new Double(runningTotalPaid)).toString());
             runningBalance -= Double.parseDouble(temp_cash_receiptObj.get_payment_amt());
             objVect.setElementAt(temp_cash_receiptObj, i);
          }//end for

      }//end if
      //Cash Receipt-Total Paid updated throughout vector
/////
        double d_diff_amtPaid = Double.parseDouble(_cash_receiptObj.get_payment_amt()) - d_old_amtPaid;

        d_totReceived = d_totReceived + d_diff_amtPaid;
        //form expression for the where part of the query
        DBAttributes attribs[] = _cash_receiptObj.getImmutableAttribs();
        exp = "";
        for (int i=0; i < attribs.length; i++)  {
          if (i > 0)  {
              exp =  exp + " and ";
          }
          exp = exp + attribs[i].getName() +
                  " = " +
                  "'" + attribs[i].getValue().trim() + "'";
        }
        //Insert/update Cash Receipts with the updated Vector
        for(int i=0; i<objVect.size(); i++)  {
          cash_receiptObj temp_cash_receiptObj = (cash_receiptObj)objVect.elementAt(i);
          if (temp_cash_receiptObj.EXISTS_IN_DB) {
              String fullExp = exp + " and " + temp_cash_receiptObj.ID + " = '" + temp_cash_receiptObj.get_id() + "'";
              dbAdapter.update(  _sessionMeta.getClientServerSecurity(),
                          temp_cash_receiptObj,
                          fullExp);
          } else {
              throw new daiException("Cash Receipt cannot be updated, not present in table", null);
          }
        }


        //Update the tot cash received so far.
        if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
            sqlStmt = " update cust_order set total_cash_received = " +
                                             Double.toString(d_totReceived) +   ", " +
                " is_prepaid = 'Y' " +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + cust_orderObj.getObjLocality()+"'";
        }
        else {
            sqlStmt = " update shipment set total_cash_received = " +
                                             Double.toString(d_totReceived) +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + shipmentObj.getObjLocality()+"'";
        }
        dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

        //Update the Financial Accounts accordingly
        Calendar now = Calendar.getInstance();
        String today = now.get(now.MONTH)+1 + "/" +
                       now.get(now.DAY_OF_MONTH) + "/" +
                       now.get(now.YEAR);
        //Add Accts Receivable amt to the Acct Detail table
        ServerUtils.postNewLedgerEntry(_cash_receiptObj.get_receivable_acct_id(),        //Acct ID
                      _cash_receiptObj.get_shipment_id(),                               //Ref
                      today,
                      account_detailObj.TRANS_TYPE_UPDATE_RECEIPT, //transType
                      null,                                     //debit
                      ((new Double(d_diff_amtPaid)).toString()),                                   //Credit
                      _cash_receiptObj.get_cust_name());                                //note
        //Add Deposit Acct amt to the Acct Detail table
        ServerUtils.postNewLedgerEntry(_cash_receiptObj.get_deposit_acct_id(),           //Acct ID
                      _cash_receiptObj.get_shipment_id(),                               //Ref
                      today,
                      account_detailObj.TRANS_TYPE_UPDATE_RECEIPT, //transType
                      ((new Double(d_diff_amtPaid)).toString()),                                   //debit
                      null,                                     //Credit
                      _cash_receiptObj.get_cust_name());                                //note

        return _cash_receiptObj.get_id();

      }
      catch (Exception e2)  {
        e2.printStackTrace();
        String msg = this.getClass().getName()+"::updateCashReceipt failure"+
                                 "\n"+e2.getLocalizedMessage();
        _logger.logError(_sessionMeta.getClientServerSecurity(), msg);
        throw new daiException(msg, null);
      }
    }

    public void deleteCashReceipt_updateTotalPaid(String _cash_receiptID) throws daiException  {
      csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
      csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

      //Let's make sure the Cash Rcpt Obj is valid first.  If not, throw
      //an exception.
      if (_cash_receiptID == null || _cash_receiptID.equals("") )  {
        throw new daiException("Invalid Cash Receipt ID.", this);
      }
      double d_old_amtPaid = 0;
      cash_receiptObj _cash_receiptObj = new cash_receiptObj();
      _cash_receiptObj.EXISTS_IN_DB = false;


//Get all same invoices to calculate balances
      Vector objVect = new Vector();
      String exp = cash_receiptObj.ID + " = " +
                    "'" + _cash_receiptID + "'";
      //Add order by clause for sorting.
      exp = exp + " order by " + cash_receiptObj.DATE_RECEIVED;
      try  {
            objVect = dbAdapter.queryByExpression( _sessionMeta.getClientServerSecurity(),
                                        _cash_receiptObj,
                                        exp);
            objVect.trimToSize();

            if(objVect == null || objVect.size() == 0)
              throw new daiException("Cash Receipt-" + _cash_receiptID + " not found", null);
            else  {
              _cash_receiptObj = ((cash_receiptObj)(objVect.firstElement()));
              exp = cash_receiptObj.SHIPMENT_ID + " = " +
                    "'" + _cash_receiptObj.get_shipment_id() + "'";
              exp = exp + " order by " + cash_receiptObj.DATE_RECEIVED + ", " + cash_receiptObj.ID;
              objVect = dbAdapter.queryByExpression( _sessionMeta.getClientServerSecurity(),
                                        _cash_receiptObj,
                                        exp);
              objVect.trimToSize();
            }

            //Loop through all the retreived business objects and indicate
            //that they already exist in the database.  This is how we know
            //to update instead of insert.
            _cash_receiptObj = null;
            for (int i=0; i<objVect.size(); i++) {
                if(((cash_receiptObj)objVect.elementAt(i)).get_id().equals(_cash_receiptID)) {
                  _cash_receiptObj = ((cash_receiptObj)objVect.elementAt(i));
                  objVect.removeElementAt(i);
                objVect.trimToSize();
                }
                if(objVect.size()!=0 && i != objVect.size())  {
                  BusinessObject obj = (BusinessObject)objVect.elementAt(i);
                  obj.EXISTS_IN_DB = true;
                  objVect.setElementAt(obj, i);
                }
            }
            if(_cash_receiptObj == null)
              throw new daiException("Cash Receipt-" + _cash_receiptID + " not found", null);

      }
      catch (Exception e)  {
            //Log to dialog, system.out, disk.
            e.printStackTrace();
            String msg = this.getClass().getName() + "::query failure." +
                          "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiException(msg, null);
      }
      try  {

//////////////////////////////////////////////////////
        //Get the tot cash received so far from the Shipment/Order.
        String sqlStmtOrder = null;
        String sqlStmtShip = null;
        String sqlStmt = null;
            sqlStmtOrder = " select " + cust_orderObj.TOTAL_CASH_RECEIVED + " , "
                                      + cust_orderObj.TOTAL_VALUE +
                                         " from " + cust_orderObj.TABLE_NAME +
            " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
            " locality = '" + cust_orderObj.getObjLocality()+"'";
            sqlStmtShip = " select " + shipmentObj.TOTAL_CASH_RECEIVED + ", "
                                     + shipmentObj.TOTAL_VALUE +
                                             " from " + shipmentObj.TABLE_NAME +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + shipmentObj.getObjLocality()+"'";

        DBRecSet transDataOrder = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmtOrder);
        DBRecSet transDataShip = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmtShip);

        DBRecSet transData = null;
        String isPrePaidOrd = null;
        if(transDataOrder != null && transDataOrder.getSize() != 0)  {
          transData = transDataOrder;
          isPrePaidOrd = "Y";
        }
        else if(transDataShip != null && transDataShip.getSize() != 0)  {
          transData = transDataShip;
          isPrePaidOrd = "N";
        }
        if (transData == null || transData.getSize() == 0)  {
          throw new daiException("Invalid Shipment/Order Object."+_cash_receiptObj.get_shipment_id(), this);
        }
        String s_totReceived = null;
        String s_totValue = null;
        if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
          s_totReceived = transData.getRec(0).getAttribVal(cust_orderObj.TOTAL_CASH_RECEIVED);
          s_totValue = transData.getRec(0).getAttribVal(cust_orderObj.TOTAL_VALUE);
        }
        else {
          s_totReceived = transData.getRec(0).getAttribVal(shipmentObj.TOTAL_CASH_RECEIVED);
          s_totValue = transData.getRec(0).getAttribVal(shipmentObj.TOTAL_VALUE);
        }
        if (_cash_receiptObj.get_payment_amt() == null) _cash_receiptObj.set_payment_amt("0");
        if (s_totReceived == null) s_totReceived = "0";
        if (s_totValue == null) s_totReceived = "0";
        double d_diff_amtPaid = -(Double.parseDouble(_cash_receiptObj.get_payment_amt()));
        double d_totReceived = (Double.parseDouble(s_totReceived)) + d_diff_amtPaid;
        double d_totDue = Double.parseDouble(s_totValue);

        //update payments
        if(objVect != null && objVect.size() != 0){
          double runningTotalPaid = 0;
          double runningBalance = d_totDue;
          for(int i=0; i<objVect.size(); i++)  {
               //calculate running total paid
              if(objVect.size()!=0)  {
                cash_receiptObj temp_cash_receiptObj = (cash_receiptObj)objVect.elementAt(i);

               //calculate running total paid
               temp_cash_receiptObj.set_total_due((new Double(runningBalance)).toString());
               runningTotalPaid += Double.parseDouble(temp_cash_receiptObj.get_payment_amt());
               temp_cash_receiptObj.set_total_paid((new Double(runningTotalPaid)).toString());
               runningBalance -= Double.parseDouble(temp_cash_receiptObj.get_payment_amt());
               objVect.setElementAt(temp_cash_receiptObj, i);
             }
          }//end for
        }//end if

      //Cash Receipt-Total Paid updated throughout vector
/////

        //form expression for the where part of the query
        DBAttributes attribs[] = _cash_receiptObj.getImmutableAttribs();
        exp = "";
        for (int i=0; i < attribs.length; i++)  {
          if (i > 0)  {
              exp =  exp + " and ";
          }
          exp = exp + attribs[i].getName() +
                  " = " +
                  "'" + attribs[i].getValue().trim() + "'";
        }
        //Insert/update Cash Receipts with the updated Vector
        for(int i=0; i<objVect.size(); i++)  {
          cash_receiptObj temp_cash_receiptObj = (cash_receiptObj)objVect.elementAt(i);
          if (temp_cash_receiptObj.EXISTS_IN_DB) {
              String fullExp = exp + " and " + temp_cash_receiptObj.ID + " = '" + temp_cash_receiptObj.get_id() + "'";
              dbAdapter.update(  _sessionMeta.getClientServerSecurity(),
                          temp_cash_receiptObj,
                          fullExp);
          } else {
              throw new daiException("Cash Receipt cannot be updated, not present in table", null);
          }
        }
        exp = "id = " + "'" + _cash_receiptObj.get_id() + "'";
        dbAdapter.delete(_sessionMeta.getClientServerSecurity(),
                                _cash_receiptObj,
                                exp);
        //Update the tot cash received so far.
        if (isPrePaidOrd != null && isPrePaidOrd.equals("Y")) {
            sqlStmt = " update cust_order set total_cash_received = " +
                                             Double.toString(d_totReceived) +   ", " +
                " is_prepaid = 'Y' " +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + cust_orderObj.getObjLocality()+"'";
        }
        else {
            sqlStmt = " update shipment set total_cash_received = " +
                                             Double.toString(d_totReceived) +
                " where id = '" + _cash_receiptObj.get_shipment_id() + "' and " +
                " locality = '" + shipmentObj.getObjLocality()+"'";
        }
        dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

        //Update the Financial Accounts accordingly
        Calendar now = Calendar.getInstance();
        String today = now.get(now.MONTH)+1 + "/" +
                       now.get(now.DAY_OF_MONTH) + "/" +
                       now.get(now.YEAR);
        //Add Accts Receivable amt to the Acct Detail table
        ServerUtils.postNewLedgerEntry(_cash_receiptObj.get_receivable_acct_id(),        //Acct ID
                      _cash_receiptObj.get_shipment_id(),                               //Ref
                      today,
                      account_detailObj.TRANS_TYPE_DELETE_RECEIPT, //transType
                      null,                                     //debit
                      ((new Double(d_diff_amtPaid)).toString()),                                   //Credit
                      _cash_receiptObj.get_cust_name());                                //note
        //Add Deposit Acct amt to the Acct Detail table
        ServerUtils.postNewLedgerEntry(_cash_receiptObj.get_deposit_acct_id(),           //Acct ID
                      _cash_receiptObj.get_shipment_id(),                               //Ref
                      today,
                      account_detailObj.TRANS_TYPE_DELETE_RECEIPT, //transType
                      ((new Double(d_diff_amtPaid)).toString()),                                   //debit
                      null,                                     //Credit
                      _cash_receiptObj.get_cust_name());                                //note


      }
      catch (Exception e2)  {
        e2.printStackTrace();
        String msg = this.getClass().getName()+"::deleteCashReceipt failure"+
                                 "\n"+e2.getLocalizedMessage();
        _logger.logError(_sessionMeta.getClientServerSecurity(), msg);
        throw new daiException(msg, null);
      }
    }
    //shipmentObj.SUBTOTAL -- will be negative amt
    //shipmentObj.TOTAL_TAX -- will be negative amt
    //shipmentObj.TOTAL_SHIPPING -- will be negative amt
    //shipmentObj.TOTAL_VALUE  -- will be negative amt
    //shipment_itemObj.ID
    //shipment_itemObj.DETAIL_ID
    //shipment_itemObj.QTY_ORDERED -- holds qtyShipped from orig shipment
    //shipment_itemObj.QTY_SHIPPED -- holds qtyToCredit
    //shipment_itemObj.UNIT_PRICE -- Holds Unit Price Amt to credit *Negative value*
    //shipment_itemObj.EXTENDED_PRICE -- Holds Extended Amt to credit *Negative value*
    //default_accountsObj.SHIPPING_OUT_ID
    //default_accountsObj.ACCTS_RECEIVABLE_ID
    //default_accountsObj.SALES_TAX_PAYABLE_ID
    //Returns the Newly generated Credit Memo Id
	public String createCreditMemo(DBRecSet shipItemData)
	throws daiException
	{
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();
        Vector dbObjVect;
        String creditMemoId = generateCreditMemoId();
        String shipItemDetailId;

        String shipId = shipItemData.getRec(0).getAttribVal(shipment_itemObj.ID);
        String s_subtot = shipItemData.getRec(0).getAttribVal(shipmentObj.SUBTOTAL);
        String s_totTax = shipItemData.getRec(0).getAttribVal(shipmentObj.TOTAL_TAX);
        String s_totShipping = shipItemData.getRec(0).getAttribVal(shipmentObj.TOTAL_SHIPPING);
        String s_totValue = shipItemData.getRec(0).getAttribVal(shipmentObj.TOTAL_VALUE);

        //Get the Shipment Header, Clone it and save it with the new
        //credit memo id.
        try {
            dbObjVect = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                                    new shipmentObj(),
                                    "id = '" + shipId + "' and locality ='"+ shipment_itemObj.getObjLocality() + "'");
            shipmentObj shipObj = (shipmentObj)dbObjVect.elementAt(0);
            shipmentObj credMemoObj = (shipmentObj)shipObj.Clone();
            credMemoObj.set_id(creditMemoId);
            credMemoObj.set_date_created(daiFormatUtil.getCurrentDate());
            credMemoObj.set_created_by(_sessionMeta.getUserId());
            credMemoObj.set_is_credit_memo("Y");
            credMemoObj.set_subtotal(s_subtot);
            credMemoObj.set_total_tax(s_totTax);
            credMemoObj.set_total_shipping(s_totShipping);
            credMemoObj.set_total_value(s_totValue);
            credMemoObj.set_total_cash_received("0.00");
            credMemoObj.set_total_discount(null);
            //Clear out the old document notes from the shipment.
            credMemoObj.set_note1("");
            credMemoObj.set_note2("");
            dbAdapter.insert(_sessionMeta.getClientServerSecurity(), credMemoObj);

			//Add Shipping Acct amt to the Acct Detail table
            if (s_totShipping == null) s_totShipping = "0";
            if (Double.parseDouble(s_totShipping) != 0) {
                //Need to reverse the sign since this is a credit memo
                double d_totShipping = Double.parseDouble(s_totShipping) * -1;
                String shippingAcctId = shipItemData.getRec(0).getAttribVal(default_accountsObj.SHIPPING_OUT_ID);
                ServerUtils.postNewLedgerEntry(shippingAcctId,
                                            creditMemoId, //Trans Ref,
                                            daiFormatUtil.getCurrentDate(),
                                            account_detailObj.TRANS_TYPE_CREATE_CREDIT_MEMO, //TransType
                                            Double.toString(d_totShipping), //debit
                                            null, //credit,
                                            credMemoObj.get_customer_name()); //note
            }

			//Add Tax Amt Payable amt to the Acct Detail table
            if (s_totTax == null) s_totTax = "0";
            if (Double.parseDouble(s_totTax) != 0) {
                double d_totTax = Double.parseDouble(s_totTax) * -1;
                String taxAcctId = shipItemData.getRec(0).getAttribVal(default_accountsObj.SALES_TAX_PAYABLE_ID);
                ServerUtils.postNewLedgerEntry(taxAcctId,
                                            creditMemoId, //Trans Ref,
                                            daiFormatUtil.getCurrentDate(),
                                            account_detailObj.TRANS_TYPE_CREATE_CREDIT_MEMO, //TransType
                                            Double.toString(d_totTax), //debit
                                            null, //credit,
                                            credMemoObj.get_customer_name()); //note
            }
            //Update the A/R Financial Account
            String arAcctId = shipItemData.getRec(0).getAttribVal(default_accountsObj.ACCTS_RECEIVABLE_ID);
            double d_totValue = Double.parseDouble(s_totValue) * -1;
            ServerUtils.postNewLedgerEntry(arAcctId,
                                            creditMemoId, //Trans Ref,
                                            daiFormatUtil.getCurrentDate(),
                                            account_detailObj.TRANS_TYPE_CREATE_CREDIT_MEMO, //TransType
                                            null, //debit
                                            Double.toString(d_totValue), //credit,
                                            credMemoObj.get_customer_name()); //note

            for (int i=0; i<shipItemData.getSize(); i++) {
                String s_extdPrice = shipItemData.getRec(i).getAttribVal(shipment_itemObj.EXTENDED_PRICE);
                //We may not have any line item that needs to be credited.
                //Use the check below to determine if no lines were credited.
                if (s_extdPrice != null) {
                    shipItemDetailId = shipItemData.getRec(i).getAttribVal(shipment_itemObj.DETAIL_ID);
                    dbObjVect = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                                    new shipment_itemObj(),
                                    "id = '" + shipId + "' and locality ='"+ shipment_itemObj.getObjLocality() + "'" +
                                    " and detail_id = " + shipItemDetailId);
                    shipment_itemObj shipItemObj = (shipment_itemObj)dbObjVect.elementAt(0);
                    shipment_itemObj credMemoItemObj = (shipment_itemObj)shipItemObj.Clone();
                    credMemoItemObj.set_id(creditMemoId);
                    credMemoItemObj.set_detail_id(Integer.toString(i+1));
                    credMemoItemObj.set_qty_ordered(shipItemData.getRec(i).getAttribVal(shipment_itemObj.QTY_ORDERED));
                    credMemoItemObj.set_qty_shipped(shipItemData.getRec(i).getAttribVal(shipment_itemObj.QTY_SHIPPED));
                    credMemoItemObj.set_unit_price(shipItemData.getRec(i).getAttribVal(shipment_itemObj.UNIT_PRICE));
                    credMemoItemObj.set_extended_price(s_extdPrice);
                    credMemoItemObj.set_qty_backorder("0");
                    credMemoItemObj.set_qty_inventory_posted("0");
                    dbAdapter.insert(_sessionMeta.getClientServerSecurity(), credMemoItemObj);

                    //Update the Item Financial Accounts accordingly
                    double d_extdPrice = Double.parseDouble(s_extdPrice) * -1;
                    ServerUtils.postNewLedgerEntry(credMemoItemObj.get_account_id(),
                                                creditMemoId, //Trans Ref,
                                                daiFormatUtil.getCurrentDate(),
                                                account_detailObj.TRANS_TYPE_CREATE_CREDIT_MEMO, //TransType
                                                Double.toString(d_extdPrice), //debit
                                                null, //credit,
                                                credMemoItemObj.get_description1()); //note
                }
            }

        } catch (Exception e) {
			e.printStackTrace();
			String msg = this.getClass().getName()+"::createCreditMemo failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
        }

        return creditMemoId;
	}

    public String createCustFromProspect(DBRec pspectData) throws daiException {

        String ret = null;
        String custId = pspectData.getAttribVal(prospectObj.CUSTOMER_ID);
        String pspectId = pspectData.getAttribVal(prospectObj.ID);
        String sqlStmt = null;
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

		try
		{
            //If the custId is null or blank that means we want to generate
            //a new entry.  Otherwise well just reuse the cust entry and only
            //add the contact info.
            if (custId == null) {

                //Insert to the new Cash Receipt
                int custIdSEQ = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
													dbAdapter.SEQUENCE_CUSTOMER);
                custId = daiFormatUtil.padIntLeft(custIdSEQ, 5);
                sqlStmt = " insert into " + customerObj.TABLE_NAME + " (" +
                customerObj.ID + ", " +
                customerObj.LOCALITY + ", " +
                customerObj.CREATED_BY + ", " +
                customerObj.DATE_CREATED + ", " +
                customerObj.NAME + ", " +
                customerObj.SHIPTO_ADDR1 + ", " +
                customerObj.SHIPTO_ADDR2 + ", " +
                customerObj.SHIPTO_ADDR3 + ", " +
                customerObj.SHIPTO_ADDR4 + ", " +
                customerObj.SHIPTO_CITY + ", " +
                customerObj.SHIPTO_STATE_CODE + ", " +
                customerObj.SHIPTO_ZIP + ", " +
                customerObj.SHIPTO_COUNTRY_CODE + ", " +
                customerObj.SHIPTO_COUNTRY_NAME + ", " +
                customerObj.REFERED_BY + ", " +
                customerObj.BILLTO_IS_SHIPTO +
                ") "+
                "select " +
                "'" + custId + "', " +
                "'"+ customerObj.getObjLocality() + "', " +
                "'"+ _sessionMeta.getUserId() + "', " +
                "'"+ daiFormatUtil.getCurrentDate() + "', " +
                prospectObj.COMPANY_NAME + ", " +
                prospectObj.ADDR1 + ", " +
                prospectObj.ADDR2 + ", " +
                prospectObj.ADDR3 + ", " +
                prospectObj.ADDR4 + ", " +
                prospectObj.CITY + ", " +
                prospectObj.STATE_CODE + ", " +
                prospectObj.ZIP + ", " +
                prospectObj.COUNTRY_CODE + ", " +
                prospectObj.COUNTRY_NAME + ", " +
                prospectObj.REFERED_BY + ", " +
                " 'Y' " +
                " from prospect " +
                " where ID = '" + pspectId + "' and " +
                " locality = '" + prospectObj.getObjLocality() + "'";
                dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
                                      sqlStmt);
            }

            //Add the prospect info to the customer contact
            int detailSEQ = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
													dbAdapter.SEQUENCE_GENERIC_DETAIL_ID);
            sqlStmt = " insert into " + customer_contactObj.TABLE_NAME + " (" +
                customer_contactObj.ID + ", " +
                customer_contactObj.LOCALITY + ", " +
                customer_contactObj.DETAIL_ID + ", " +
                customer_contactObj.NAME + ", " +
                customer_contactObj.EMAIL1 + ", " +
                customer_contactObj.FAX1 + ", " +
                customer_contactObj.PHONE1 + ", " +
                customer_contactObj.WEB + ", " +
                customer_contactObj.IS_PRIMARY + ", " +
                customer_contactObj.ADDR1 +  ", " +
                customer_contactObj.ADDR2 +  ", " +
                customer_contactObj.ADDR3 +  ", " +
                customer_contactObj.CITY +  ", " +
                customer_contactObj.STATE_CODE +  ", " +
                customer_contactObj.ZIP +  ", " +
                customer_contactObj.COUNTRY_CODE +  ", " +
                customer_contactObj.COUNTRY_NAME +
            ") " +
                "select " +
                "'" + custId + "', " +
                "'"+ customerObj.getObjLocality() + "', " +
                 Integer.toString(detailSEQ) + ", " +
                prospectObj.FIRST_NAME + " || ' ' || " + prospectObj.LAST_NAME + ", " +
                prospectObj.EMAIL + ", " +
                prospectObj.FAX + ", " +
                prospectObj.PHONE + ", " +
                prospectObj.WEB + ", " +
                " 'Y' " + ", " +
                prospectObj.ADDR1 +  ", " +
                prospectObj.ADDR2 +  ", " +
                prospectObj.ADDR3 +  ", " +
                prospectObj.CITY +  ", " +
                prospectObj.STATE_CODE +  ", " +
                prospectObj.ZIP +  ", " +
                prospectObj.COUNTRY_CODE +  ", " +
                prospectObj.COUNTRY_NAME +
                " from prospect " +
                " where ID = '" + pspectId + "' and " +
                " locality = '" + prospectObj.getObjLocality() + "'";
            dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
                                      sqlStmt);

            ret = custId;
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::createCustFromProspect failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}

        return ret;
    }

	private String generateShipmentId()
	throws daiException
	{
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

		String  sRet = "";
		int     iRet = -1;
        String  lastInvMonth = "";
        boolean createSetting = false;
        global_settingsObj gsObj = null;
		Calendar now = Calendar.getInstance();
        String MM = Integer.toString(now.get(now.MONTH)+1);

		try
		{
            //Check for the last invoice month used
            Vector vect = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                                        new global_settingsObj(),
                                        " id = '" +global_settingsObj.SETTING_LAST_INVOICE_MO+"'"+
                                        " and locality = '"+global_settingsObj.getObjLocality()+"'");
            if (vect.size() > 0) {
                gsObj = (global_settingsObj)vect.firstElement();
                lastInvMonth = gsObj.get_varchar_setting1();
            } else {
                createSetting = true;
                gsObj = new global_settingsObj();
                gsObj.set_id(global_settingsObj.SETTING_LAST_INVOICE_MO);
                gsObj.set_locality(global_settingsObj.getObjLocality());
            }

            //Check to see wether we need to reset the invoice Id counter.
            if (!lastInvMonth.equals(MM)) {
                //Reset the sequence counter.
                dbAdapter.setSequenceValue(_sessionMeta.getClientServerSecurity(),
                        csDBAdapter.SEQUENCE_SHIPMENT, 0);
            }

            iRet = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
                                               csDBAdapter.SEQUENCE_SHIPMENT);

		    sRet = daiFormatUtil.padIntLeft(now.get(now.MONTH)+1, 2) +
                        daiFormatUtil.padIntLeft(iRet, 3) + "-"+now.get(now.YEAR);

            gsObj.set_varchar_setting1(MM);
            if (createSetting) {
                dbAdapter.insert(_sessionMeta.getClientServerSecurity(), gsObj);
            } else {
                dbAdapter.update(_sessionMeta.getClientServerSecurity(), gsObj,
                            " id = '" + gsObj.SETTING_LAST_INVOICE_MO + "'");
            }

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::generateShipmentId failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
		return sRet;
	}

	private String generateCreditMemoId()
	throws daiException
	{
		csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();

		String  sRet = "";
		int     iRet = -1;

		Calendar now = Calendar.getInstance();
		try
		{
			iRet = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
                                                dbAdapter.SEQUENCE_CREDIT_MEMO);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::generateShipmentId failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}

		sRet = daiFormatUtil.padIntLeft(now.get(now.MONTH)+1, 2) + daiFormatUtil.padIntLeft(iRet, 3) + "-"+now.get(now.YEAR);

		return "CR-" + sRet;
	}
}

