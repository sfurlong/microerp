
//Title:        Client Server DB Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.


package dai.server.purchOrderService;

import java.math.BigDecimal;
import java.util.Vector;

import dai.server.serverShared.ServerUtils;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.account_detailObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.businessObjs.itemObj;
import dai.shared.businessObjs.payment_voucherObj;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.businessObjs.purch_order_itemObj;
import dai.shared.businessObjs.purch_order_item_rcv_histObj;
import dai.shared.businessObjs.vendorObj;
import dai.shared.cmnSvcs.FinanceAcctsDataCache;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiException;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csInventoryAdapter;
import dai.shared.csAdapters.csInventoryAdapterFactory;
import dai.shared.csAdapters.csLoggerAdapter;
import dai.shared.csAdapters.csLoggerAdapterFactory;

public class PurchOrderServiceImpl
{
	csLoggerAdapter _logger;
	SessionMetaData     _sessionMeta;
	csDBAdapterFactory  _dbAdapterFactory;
	csDBAdapter         _dbAdapter;
	FinanceAcctsDataCache _financeAcctsCache;
	ServerUtils _serverUtils = new ServerUtils();

	public PurchOrderServiceImpl()
	{
		_logger = csLoggerAdapterFactory.getInstance().getLoggerAdapter();
		_sessionMeta = SessionMetaData.getInstance();
	}

	// purch_orderObj.ID
	// purch_orderObj.DATE_CREATED
	// purch_order_itemObj.DETAIL_ID
	// purch_order_itemObj.PURCH_PRICE
	// purch_order_itemObj.ITEM_ID
	// purch_order_itemObj.QTY_ORDERED
	// purch_order_itemObj.QTY_RECEIVED
	// purch_order_itemObj.IS_ITEM_REPAIR
	public DBRecSet getInventoryReceivablePOItems(String poId)
	throws daiException
	{
		_dbAdapterFactory    = csDBAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();

		DBRecSet collection;

		try
		{
			//This basically says give me all purchase orders items where
			//total of inventory received is less than the total qty ordered
			//for the specified po Id.
			String sqlStmt = "select " +
							 purch_orderObj.ID + ", " +
							 purch_orderObj.DATE_CREATED + ", " +
							 purch_order_itemObj.DETAIL_ID + ", " +
							 purch_order_itemObj.PURCH_PRICE + ", " +
							 purch_order_itemObj.ITEM_ID + ", " +
							 purch_order_itemObj.QTY_ORDERED + ", " +
							 purch_order_itemObj.QTY_RECEIVED + ", " +
							 purch_order_itemObj.IS_ITEM_REPAIR + 
							 " from " +
							 purch_orderObj.TABLE_NAME + ", " +
							 purch_order_itemObj.TABLE_NAME +
							 " where " +
							 purch_orderObj.ID + " = " + purch_order_itemObj.ID +
							 " and " +purch_orderObj.LOCALITY + " = " + purch_order_itemObj.LOCALITY +
							 " and (" + purch_order_itemObj.QTY_RECEIVED +
							 " < " + purch_order_itemObj.QTY_ORDERED + " or " +
							 purch_order_itemObj.QTY_RECEIVED + " is null ) " +
							 " and " + purch_orderObj.LOCALITY + " = '" +
							 purch_orderObj.getObjLocality() + "'" + " and " +
							 purch_orderObj.ID + " = '" + poId + "'";

			collection = _dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
												   sqlStmt);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::getInventoryReceivablePOItems failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}

		return collection;
	}

	//Returns the following attributes in a collection.
	//  purch_order_item_rcv_histObj.ID
	//  purch_order_itemObj.ITEM_ID
	//  purch_order_itemObj.DESCRIPTION1
	//  purch_order_itemObj.QTY_ORDERED
	//  purch_order_item_rcv_histObj.QTY_RECEIVED
	//  purch_order_item_Obj.PURCH_PRICE
	//  purch_order_item_rcv_histObj.DATE_RECEIVED
	//  purch_order_item_rcv_histObj.BILL_RECEIVED
	public DBRecSet getInvoicablePOItems(String poId)
	throws daiException
	{
		_dbAdapterFactory    = csDBAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();

		DBRecSet collection;

		try
		{
			//This basically says give me all purchase orders items where
			//total of inventory received is less than the total qty ordered
			//for the specified vendor Id.
			String sqlStmt = "select " +
							 purch_order_item_rcv_histObj.ID + ", " +
							 purch_order_itemObj.ITEM_ID + ", " +
							 purch_order_itemObj.QTY_ORDERED + ", " +
							 purch_order_itemObj.DESCRIPTION1 + ", " +
							 purch_order_item_rcv_histObj.QTY_RECEIVED + ", " +
							 purch_order_itemObj.PURCH_PRICE + ", " +
							 purch_order_item_rcv_histObj.DATE_RECEIVED + ", " +
							 purch_order_item_rcv_histObj.BILL_RECEIVED +
							 " from " +
							 purch_order_itemObj.TABLE_NAME + ", " +
							 purch_order_item_rcv_histObj.TABLE_NAME +
							 " where " +
							 purch_order_itemObj.ID + " = " + purch_order_item_rcv_histObj.ID +
							 " and " + purch_order_itemObj.LOCALITY + " = " + purch_order_item_rcv_histObj.LOCALITY +
							 " and " + purch_order_itemObj.DETAIL_ID + " = " + purch_order_item_rcv_histObj.DETAIL_ID +
							 " and (" + purch_order_item_rcv_histObj.BILL_RECEIVED + " = '" + "N'" +
							 "   or " + purch_order_item_rcv_histObj.BILL_RECEIVED + " is null)" +
							 " and " + purch_order_itemObj.LOCALITY+ " = '" + purch_orderObj.getObjLocality() + "'" + " and " +
							 purch_order_itemObj.ID + " = '" + poId + "'";

			collection = _dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
												   sqlStmt);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::getPayablePOItems failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}

		return collection;
	}

	//*      purch_orderObj.ID
	//*      purch_orderObj.VENDOR_NAME
	//*      purch_order_itemObj.ITEM_ID
	//*      purch_order_itemObj.PURCH_PRICE
	//*      purch_order_itemObj.QTY_ORDERED
	//*      purch_order_itemObj.QTY_RECEIVED
	//*      purch_order_itemObj._QTY_TO_RECEIVE
	//*      purch_order_itemObj.IS_ITEM_REPAIR
	//*      purch_order_item_rcv_histObj.DATE_RECEIVED   //This attrib is not the result of a query.
	//it is manually added from by the client.
	public void receiveInventory(DBRecSet poItemsData)
	throws daiException
	{
		_dbAdapterFactory    = _dbAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();
		_financeAcctsCache = _financeAcctsCache.getInstance();
		csInventoryAdapterFactory inventoryAdapterFactory = csInventoryAdapterFactory.getInstance();
		csInventoryAdapter inventoryAdapter = inventoryAdapterFactory.getInventoryAdapter();

		DBRec poItemData;
		String      sqlStmt = "";
		int         totQtyReceived = 0;
		String      poId;
		String      s_qtyReceived;
		String      s_qtyToReceive;
		String      s_poItemDetailId;
		String      s_purchPrice;
		String      itemId;
		String      inventoryAcctId;
		double      extendedPrice;
		String      dateReceived = null;

		try
		{
			for (int i=0; i<poItemsData.getSize(); i++)
			{
				poItemData = poItemsData.getRec(i);

				poId = poItemData.getAttribVal(purch_orderObj.ID);
				itemId = poItemData.getAttribVal(purch_order_itemObj.ITEM_ID);
				s_poItemDetailId = poItemData.getAttribVal(purch_order_itemObj.DETAIL_ID);
				s_qtyReceived = poItemData.getAttribVal(purch_order_itemObj.QTY_RECEIVED);
				s_qtyToReceive = poItemData.getAttribVal(purch_order_itemObj._QTY_TO_RECEIVE);
				s_purchPrice = poItemData.getAttribVal(purch_order_itemObj.PURCH_PRICE);
				dateReceived = poItemData.getAttribVal(purch_order_item_rcv_histObj.DATE_RECEIVED);
				if (s_qtyReceived == null) s_qtyReceived = "0";
				if (s_qtyToReceive == null)	s_qtyToReceive = "0";
				if (s_purchPrice == null) s_purchPrice = "0.0";

				totQtyReceived = Integer.parseInt(s_qtyReceived) + Integer.parseInt(s_qtyToReceive);

				//Create a new record in the Purch Order Item Receive History table
				purch_order_item_rcv_histObj poItemRcvHist = new purch_order_item_rcv_histObj();
				poItemRcvHist.set_id(poId);
				poItemRcvHist.set_locality(purch_orderObj.getObjLocality());
				poItemRcvHist.set_detail_id(s_poItemDetailId);
				int subDet = _dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
														  csDBAdapter.SEQUENCE_GENERIC_DETAIL_ID);
				poItemRcvHist.set_sub_detail_id(Integer.toString(subDet));
				poItemRcvHist.set_date_received(dateReceived);
				poItemRcvHist.set_qty_received(s_qtyToReceive);
				poItemRcvHist.set_bill_received("N");
				_dbAdapter.insert(_sessionMeta.getClientServerSecurity(), poItemRcvHist);

				//Updat the qty received on the Purch Order Item table
				sqlStmt = "update " + purch_order_itemObj.TABLE_NAME +
						  " set " + purch_order_itemObj.QTY_RECEIVED + " = " + Integer.toString(totQtyReceived) +
						  " where ID = '" + poId + "' and " +
						  "     locality = '" + purch_order_itemObj.getObjLocality() + "' and " +
						  "     detail_id = " + s_poItemDetailId;
				_dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
										  sqlStmt);

				//Calculate the extended price for the qty received.
				extendedPrice = Integer.parseInt(s_qtyToReceive) * Double.parseDouble(s_purchPrice);

				//Update the Accts Payable Acct for Each line item received
				ServerUtils.postNewLedgerEntry(_financeAcctsCache.getPayableAcctNum(),	//Acct Id
												poId,			 //Ref Num
												dateReceived,
												account_detailObj.TRANS_TYPE_RECEIVE_INVENTORY,
												null,							//Debit Amt
												Double.toString(extendedPrice),	//Credit Amt
												poItemData.getAttrib(purch_orderObj.VENDOR_NAME).getValue()); //Note

				//Update the Inventory Acct for Each line item received
				sqlStmt = " select " + itemObj.INVENTORY_ACCT_ID +
						  " from " + itemObj.TABLE_NAME +
						  " where " + itemObj.ID + " = '" + itemId + "'" +
						  " and  locality ='" + itemObj.getObjLocality() + "'";
				DBRecSet itemAttribs = _dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
																 sqlStmt);
				if (itemAttribs.getSize() > 0)
				{
					inventoryAcctId = itemAttribs.getRec(0).getAttribVal(itemObj.INVENTORY_ACCT_ID);
					if (inventoryAcctId != null)
					{
						ServerUtils.postNewLedgerEntry(inventoryAcctId, //Acct Id
														poId,			 //Ref Num
														dateReceived,
														account_detailObj.TRANS_TYPE_RECEIVE_INVENTORY,
														Double.toString(extendedPrice),	 //Debit Amt
														null,							//Credit Amt
														poItemData.getAttrib(purch_orderObj.VENDOR_NAME).getValue()); //Note
					} else
					{
						String msg = "No Inventory Acct Id exists for item " + itemId + "." +
									 "  Please set the Inventory Acct Id in the Item Master File " +
									 "before continuing.";
						throw new daiException(msg, null);
					}
				} else
				{
					String msg = "This item does not exist in the Item Master File: " + itemId + "." +
								 "  Please add it to the Item Master File " +
								 "before continuing.";
					throw new daiException(msg, null);
				}
			}

			//Update the Inventory for each item PO Item Collection
			inventoryAdapter.postReceivedItemsToInventory(_sessionMeta.getClientServerSecurity(),
														  poItemsData);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::receiveInventory failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	// payment_voucherObj.ID
	// payment_voucherObj.PAYMENT_DUE_DATE
	// payment_voucherObj.VENDOR_NAME
	// payment_voucherObj.INVOICE_NUM
	// payment_voucherObj.INVOICE_DATE
	// payment_voucherObj.TOTAL_PAYMENTS_POSTED
	// payment_voucherObj.TOTAL_VALUE
	public DBRecSet getPayablePurchases(String filterDate)
	throws daiException
	{
		_dbAdapterFactory    = _dbAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();

		try
		{
			//This basically says give me all purchase orders where
			//total of payments received is less than the total value
			//of the purchase order.
			String sqlStmt = "select " +
							 payment_voucherObj.ID + ", " +
							 payment_voucherObj.PAYMENT_DUE_DATE + ", " +
							 payment_voucherObj.VENDOR_NAME + ", " +
							 payment_voucherObj.INVOICE_NUM + ", " +
							 payment_voucherObj.INVOICE_DATE + ", " +
							 payment_voucherObj.TOTAL_PAYMENTS_POSTED + ", " +
							 payment_voucherObj.TOTAL_VALUE +
							 " from " +  payment_voucherObj.TABLE_NAME + " where ";

			String whereClause = "((" + payment_voucherObj.TOTAL_PAYMENTS_POSTED +
								 " < " + payment_voucherObj.TOTAL_VALUE + ") or (" +
								 payment_voucherObj.TOTAL_PAYMENTS_POSTED + " > " +
								 payment_voucherObj.TOTAL_VALUE + " ) or " +
								 payment_voucherObj.TOTAL_PAYMENTS_POSTED + " is null) " +
								 " and locality = '" +
								 payment_voucherObj.getObjLocality() + "' and (" +
                                 payment_voucherObj.IS_VOIDED + " is null or " +
                                 payment_voucherObj.IS_VOIDED + " = 'N') " ;

			//Add the filter date
			if (filterDate != null && filterDate.length() > 0)
			{
				whereClause = whereClause +
							  " and " + payment_voucherObj.PAYMENT_DUE_DATE + " <= " +
							  "'" + filterDate + "'";
			}

			//This sort order matches the sort order on the webRpts.
			String orderByClause = " order by " + payment_voucherObj.VENDOR_NAME +
								   " , " + payment_voucherObj.PAYMENT_DUE_DATE +
								   ", " + payment_voucherObj.ID;

			sqlStmt = sqlStmt + whereClause + orderByClause;

			//Get the Payment Voucher attributes that meet our critera.
			DBRecSet ret = _dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
													 sqlStmt);

			return ret;

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::getPayablePurchases failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

    //Expects the following attributes.
    // payment_voucherObj.PAYMENT_AMT
    // payment_voucherObj.CHECK_NUM
    // payment_voucherObj.PAYMENT_METHOD
    // payment_voucherObj.PRINT_CHECK
    // payment_voucherObj.PAY_FROM_ACCT_ID
    // payment_voucherObj.PAY_FROM_ACCT_NAME
    // payment_voucherObj.DATE_PAID
    // payment_voucherObj.PURCH_ORDER_ID
    // payment_voucherObj.ID
    // payment_voucherObj.VENDOR_NAME
    public void postPurchOrderPayments(DBRecSet payVouchers)
    throws daiException
    {
        _dbAdapterFactory    = _dbAdapterFactory.getInstance();
        _dbAdapter           = _dbAdapterFactory.getDBAdapter();
        _financeAcctsCache = _financeAcctsCache.getInstance();
        String transType = null;
        String purchOrdType = null;
        String sqlStmt;
        DBRec payVouchAttribs;
        String s_totPaymentsPosted = "0.00";

        try
        {
            for (int i=0; i<payVouchers.getSize(); i++)
            {

                payVouchAttribs = payVouchers.getRec(i);
                String payAcctId = payVouchAttribs.getAttribVal(payment_voucherObj.PAY_FROM_ACCT_ID);
                String payAcctName = payVouchAttribs.getAttribVal(payment_voucherObj.PAY_FROM_ACCT_NAME);
                String s_paymentAmt = payVouchAttribs.getAttribVal(payment_voucherObj.PAYMENT_AMT);
                String voucherId = payVouchAttribs.getAttribVal(payment_voucherObj.ID);
                String vendorName = payVouchAttribs.getAttribVal(payment_voucherObj.VENDOR_NAME);

                //Get the amount paid thus far in this voucher.
                sqlStmt = "select " + payment_voucherObj.TOTAL_PAYMENTS_POSTED +
                          " from " + payment_voucherObj.TABLE_NAME +
                          " where " + payment_voucherObj.ID + " = '" + voucherId + "'" +
                          "    and " + payment_voucherObj.LOCALITY + " = '" + payment_voucherObj.getObjLocality() + "'";
                DBRecSet attribSet = _dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);
                if (attribSet.getSize() > 0)
                {
                    //Expecting only one returned..
                    s_totPaymentsPosted = attribSet.getRec(0).getAttribVal(payment_voucherObj.TOTAL_PAYMENTS_POSTED);
                }
                if (s_totPaymentsPosted == null) s_totPaymentsPosted = "0.00";
                if (s_paymentAmt == null) s_paymentAmt = "0.00";
                double totPaymentsPosted = Double.parseDouble(s_totPaymentsPosted) +
                                           Double.parseDouble(s_paymentAmt);

                //Update the Payment Voucher with the payment Info
                sqlStmt = "update " + payment_voucherObj.TABLE_NAME + " set " +
                          payment_voucherObj.PAYMENT_AMT + " = " + s_paymentAmt + ", " +
                          payment_voucherObj.CHECK_NUM + " = " + ServerUtils.addQuotes(payVouchAttribs.getAttribVal(payment_voucherObj.CHECK_NUM)) + ", " +
                          payment_voucherObj.PAYMENT_METHOD + " = " + ServerUtils.addQuotes(payVouchAttribs.getAttribVal(payment_voucherObj.PAYMENT_METHOD)) + ", " +
                          payment_voucherObj.PRINT_CHECK + " = " + ServerUtils.addQuotes(payVouchAttribs.getAttribVal(payment_voucherObj.PRINT_CHECK)) + ", " +
                          payment_voucherObj.PAY_FROM_ACCT_ID + " = " + ServerUtils.addQuotes(payAcctId) + ", " +
                          payment_voucherObj.PAY_FROM_ACCT_NAME + " = " + ServerUtils.addQuotes(payAcctName) + ", " +
                          payment_voucherObj.DATE_PAID + " = " + ServerUtils.addQuotes(payVouchAttribs.getAttribVal(payment_voucherObj.DATE_PAID)) + ", " +
                          payment_voucherObj.TOTAL_PAYMENTS_POSTED + " = " + Double.toString(totPaymentsPosted) +
                          " where " + payment_voucherObj.ID + " = '" + voucherId + "'" +
                          "    and " + payment_voucherObj.LOCALITY + " = '" + payment_voucherObj.getObjLocality() + "'";
                _dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

                //Update the total amt paid in the PO Header
                //!!TBD_LOGIC

                //Update the Cash Acct
                ServerUtils.postNewLedgerEntry(payAcctId,
                                                voucherId,
                                                daiFormatUtil.getCurrentDate(),
                                                account_detailObj.TRANS_TYPE_PAY_BILL,
                                                null,           //Debit
                                                s_paymentAmt, //Credit
                                                vendorName);

                //Update the Accts Payable Acct
                ServerUtils.postNewLedgerEntry(_financeAcctsCache.getPayableAcctNum(), //Acct Id
                                                voucherId,            //Ref Num
                                                daiFormatUtil.getCurrentDate(),
                                                account_detailObj.TRANS_TYPE_PAY_BILL,
                                                s_paymentAmt,    //Debit Amt
                                                null,   //Credit Amt
                                                vendorName); //Note
            }

        } catch (Exception e)
        {
            e.printStackTrace();
            String msg = this.getClass().getName()+"::postPurchOrderPayments failure"+
                         "\n"+e.getLocalizedMessage();
            _logger.logError(_sessionMeta.getClientServerSecurity(), msg);
            throw new daiException(msg, null);
        }
    }


	//Expects the following attributes.
    // payment_voucherObj.PAYMENT_AMT  --This is the amount of the Bill
    // payment_voucherObj.TOTAL_PAYMENTS_POSTED --This is the amount that has been paid on the bill
    // payment_voucherObj.TOTAL_SHIPPING_CHARGES
    // payment_voucherObj.CHECK_NUM
    // payment_voucherObj.PAY_FROM_ACCT_ID
    // payment_voucherObj.ACCT_ID
    // payment_voucherObj.PURCH_ORDER_ID
    // payment_voucherObj.ID
    // payment_voucherObj.VENDOR_NAME
	public void voidPurchOrderPayment(DBRec payVouchAttribs)
	throws daiException
	{
		_dbAdapterFactory    = _dbAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();
		_financeAcctsCache = _financeAcctsCache.getInstance();

		try
		{
			String payAcctId = payVouchAttribs.getAttribVal(payment_voucherObj.PAY_FROM_ACCT_ID);
			String expnsAcctId = payVouchAttribs.getAttribVal(payment_voucherObj.ACCTID);
			String s_paymentAmt = payVouchAttribs.getAttribVal(payment_voucherObj.PAYMENT_AMT);
			String s_amtPaid = payVouchAttribs.getAttribVal(payment_voucherObj.TOTAL_PAYMENTS_POSTED);
			String s_shipChargeAmt = payVouchAttribs.getAttribVal(payment_voucherObj.TOTAL_SHIPPING_CHARGES);
			String voucherId = payVouchAttribs.getAttribVal(payment_voucherObj.ID);
			String vendorName = payVouchAttribs.getAttribVal(payment_voucherObj.VENDOR_NAME);
			String checkNum = payVouchAttribs.getAttribVal(payment_voucherObj.CHECK_NUM);
			String purchOrdId = payVouchAttribs.getAttribVal(payment_voucherObj.PURCH_ORDER_ID);

            //Reverse the Ledger entry if any payments have been made.
            if (s_amtPaid != null && Double.parseDouble(s_amtPaid) != 0) {
    			//Update the Cash Acct
	    		ServerUtils.postNewLedgerEntry(payAcctId,
											voucherId,
											daiFormatUtil.getCurrentDate(),
											account_detailObj.TRANS_TYPE_VOID_PAYMENT,
											s_amtPaid, //Debit
											null, //Credit
											vendorName + " Check Num: " + checkNum);

		    	//Update the Accts Payable Acct
			    ServerUtils.postNewLedgerEntry(_financeAcctsCache.getPayableAcctNum(),	//Acct Id
											voucherId,			  //Ref Num
											daiFormatUtil.getCurrentDate(),
											account_detailObj.TRANS_TYPE_VOID_PAYMENT,
											null,	 //Debit Amt
											s_amtPaid,	//Credit Amt
											vendorName + " Check Num: " + checkNum); //Note
            }

            //If the purchase order id field from the voucher is "N/A" that
            //is the indication that this is an expense bill.  If not we
            //assume it's an invoice.  The N/A is set in the postBillReceipt method.
            if (purchOrdId.equals("N/A")) {
                //This means it's an expense account Voucher

    			//Update the Expense Acct
	    		ServerUtils.postNewLedgerEntry(expnsAcctId,
											voucherId,
											daiFormatUtil.getCurrentDate(),
											account_detailObj.TRANS_TYPE_VOID_PAYMENT,
											null, //Debit
											s_paymentAmt, //Credit
											vendorName);

		    	//Update the Accts Payable Acct
			    ServerUtils.postNewLedgerEntry(_financeAcctsCache.getPayableAcctNum(),	//Acct Id
											voucherId,			  //Ref Num
											daiFormatUtil.getCurrentDate(),
											account_detailObj.TRANS_TYPE_VOID_PAYMENT,
											s_paymentAmt,	 //Debit Amt
											null,	//Credit Amt
											vendorName); //Note
            } else { //It's a PO Voucher
                if (s_shipChargeAmt != null && Double.parseDouble(s_shipChargeAmt) != 0) {

        			//Get the shipping-In acct Id so we can update the G/L
		        	default_accountsObj defAcctsObj = null;
        			String acctExp = " id = '" + default_accountsObj.SINGLETON_ID + "'" +
							 " and locality = '" + default_accountsObj.getObjLocality() + "'";
		        	Vector defAcctsVect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															   new default_accountsObj(),
															   acctExp);
        			if (defAcctsVect.size() > 0)
		        	{
				        defAcctsObj = (default_accountsObj)defAcctsVect.firstElement();
        			} else {
                        throw new Exception("No Default Acct for Shipping-In");
                    }

                    defAcctsObj.get_shipping_in_id();

        			//Update the Shipping In Acct Acct
	        		ServerUtils.postNewLedgerEntry(defAcctsObj.get_shipping_in_id(),
											voucherId,
											daiFormatUtil.getCurrentDate(),
											account_detailObj.TRANS_TYPE_VOID_PAYMENT,
											null, //Debit
											s_shipChargeAmt, //Credit
											vendorName);

		        	//Update the Accts Payable Acct
			        ServerUtils.postNewLedgerEntry(_financeAcctsCache.getPayableAcctNum(),	//Acct Id
											voucherId,			  //Ref Num
											daiFormatUtil.getCurrentDate(),
											account_detailObj.TRANS_TYPE_VOID_PAYMENT,
											s_shipChargeAmt,	 //Debit Amt
											null,	//Credit Amt
											vendorName); //Note
                }
            }

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::voidPurchOrderPayments failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}


	//This method returns there attributes:
	//  payment_voucherObj.PAYMENT_DUE_DATE
	//  payment_voucherObj.VENDOR_NAME
	//  payment_voucherObj.ID
	//  payment_voucherObj.TOTAL_VALUE
	//  payment_voucherObj.CHECK_NUM
	//  payment_voucherObj.PAYMENT_AMT
	public DBRecSet getChecksToPrint()
	throws daiException
	{
		_dbAdapterFactory    = _dbAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();
		DBRecSet collection;
		try
		{

			String sqlStmt = "select " +
							 payment_voucherObj.PAYMENT_DUE_DATE +","+
							 payment_voucherObj.VENDOR_NAME +","+
							 payment_voucherObj.ID +","+
							 payment_voucherObj.TOTAL_VALUE +","+
							 payment_voucherObj.CHECK_NUM + ", " +
							 payment_voucherObj.PAYMENT_AMT +
							 " from " +
							 payment_voucherObj.TABLE_NAME +
							 " where " +
							 payment_voucherObj.LOCALITY +"='"+ payment_voucherObj.getObjLocality()+"' and "+
							 payment_voucherObj.PRINT_CHECK + "='Y'" +
							 " order by " + payment_voucherObj.CHECK_NUM;

			collection = _dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
												   sqlStmt);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::getCheckToPrint failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}

		return collection;
	}

	public void deletePurchOrder(String poId)
	throws daiException
	{
		_dbAdapterFactory    = _dbAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();

		csInventoryAdapterFactory inventoryAdapterFactory = csInventoryAdapterFactory.getInstance();
		csInventoryAdapter inventoryAdapter = inventoryAdapterFactory.getInventoryAdapter();

		//!! TBD LOGIC !!
		try
		{
			/*
				//Delete all the entries in the GL for this Purch Order.
				String sqlStmt = " delete from " + account_detailObj.TABLE_NAME +
								" where " + account_detailObj.TRANS_REF + " = '" + Id + "'" +
								" and locality = '" + account_detailObj.getObjLocality() + "'";
				dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

				//Adjust Inventory
				sqlStmt = " select " + shipment_itemObj.ITEM_ID + " , " +
									shipment_itemObj.QTY_SHIPPED +
									" from " + shipment_itemObj.TABLE_NAME +
									" where id = '" + shipmentId + "'" +
									" and locality = '" + shipment_itemObj.getObjLocality() + "'";
				DBRecSet retAttribs = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlStmt);

				for (int i=0; i<retAttribs.getSize(); i++) {
					String itemId = retAttribs.getRec(i).getAttribVal(shipment_itemObj.ITEM_ID);
					String qtyShipped = retAttribs.getRec(i).getAttribVal(shipment_itemObj.QTY_SHIPPED);
					//Reverse the sign of the qty shipped.  This
					//will reduce the inventory by this amount.
					int i_qtyShipped = Integer.parseInt(qtyShipped) * -1;
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
					itemData.addAttribSet(attribs);

					inventoryAdapter.postInventoryAdjustment(_sessionMeta.getClientServerSecurity(),
															itemData);
				}
			*/
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::deletePurchOrder failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	public String[] calcMultiPayments(double totPayAmt, int numPayments) {
		String[] ret = new String[numPayments];

		//If only one payment, lets get out of here.
		if (numPayments == 1)
		{
			ret[0] = Double.toString(totPayAmt);
		}

		//Calc what each payment amount will be.
		double d_numPayments = Double.parseDouble(Integer.toString(numPayments));
		double eachPayAmt = totPayAmt / d_numPayments;

		//Convert to big decimal and do any necessary rounding.
		BigDecimal bd_eachPayAmt = new BigDecimal(eachPayAmt);
		bd_eachPayAmt = bd_eachPayAmt.setScale(2, BigDecimal.ROUND_HALF_UP);


		double d_lastEachPayAmt = 0.0;
		BigDecimal bd_origTotPayAmt = new BigDecimal(totPayAmt);
		bd_origTotPayAmt = bd_origTotPayAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal bd_newTotPayAmt = bd_eachPayAmt.multiply(new BigDecimal(d_numPayments));
		bd_newTotPayAmt = bd_newTotPayAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
		d_lastEachPayAmt = bd_eachPayAmt.doubleValue();
		if (!bd_origTotPayAmt.equals(bd_newTotPayAmt))
		{
			BigDecimal diff = bd_origTotPayAmt.subtract(bd_newTotPayAmt);
			diff = diff.setScale(2, BigDecimal.ROUND_HALF_UP);
			d_lastEachPayAmt = diff.doubleValue() + bd_eachPayAmt.doubleValue();
		}

		for (int i=0; i<numPayments; i++)
		{
			ret[i] = bd_eachPayAmt.toString();
			if (i == numPayments -1)
			{
				ret[i] = Double.toString(d_lastEachPayAmt);
			}
		}

		return ret;
	}

	//*     payment_voucherObj.PURCH_ORDER_ID
	//*     payment_voucherObj.VENDOR_ID
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
	public String[] postInvoiceReceipt(DBRecSet vouchData)
	throws daiException
	{
		_dbAdapterFactory    = _dbAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();

		//Let's make sure the Purchase Order Obj is valid first.  If not, throw
		//an exception.
		if (vouchData == null || vouchData.getSize() == 0)
		{
			throw new daiException("Invalid Purch Order Object.", this);
		} else if (vouchData.getSize() > 1)
		{
			throw new daiException("Expected only one Purch Order object.  Found > 1.", this);
		}

		String poId = vouchData.getRec(0).getAttribVal(payment_voucherObj.PURCH_ORDER_ID);
		String s_paymentAmt = vouchData.getRec(0).getAttribVal(payment_voucherObj.TOTAL_VALUE);
		String s_subtotalAmt = vouchData.getRec(0).getAttribVal(payment_voucherObj.SUBTOTAL_AMT);
		String s_shipChargeAmt = vouchData.getRec(0).getAttribVal(payment_voucherObj.TOTAL_SHIPPING_CHARGES);
		String vendorId = vouchData.getRec(0).getAttribVal(payment_voucherObj.VENDOR_ID);
		String vendorName = vouchData.getRec(0).getAttribVal(payment_voucherObj.VENDOR_NAME);
		String expenseAcctNum = vouchData.getRec(0).getAttribVal(payment_voucherObj.ACCTID);
		String expenseAcctName = vouchData.getRec(0).getAttribVal(payment_voucherObj.ACCTNAME);
		String payDueDate = vouchData.getRec(0).getAttribVal(payment_voucherObj.PAYMENT_DUE_DATE);
		String payTerms = vouchData.getRec(0).getAttribVal(payment_voucherObj.PAYMENT_TERMS);
		String invoiceNum = vouchData.getRec(0).getAttribVal(payment_voucherObj.INVOICE_NUM);
		String invoiceDate = vouchData.getRec(0).getAttribVal(payment_voucherObj.INVOICE_DATE);
		String shipAcctId = vouchData.getRec(0).getAttribVal(payment_voucherObj._SHIP_CHARGES_ACCT_ID);
		String shipAcctName = vouchData.getRec(0).getAttribVal(payment_voucherObj._SHIP_CHARGES_ACCT_NAME);
		String vendorAddr1 = "";
		String vendorAddr2 = "";
		String vendorAddr3 = "";
		String vendorCity = "";
		String vendorStCode = "";
		String vendorZip = "";
		String vendorCountryCode = "";
		String ourAcctNumWithVendor = "";
		int numPayments = Integer.parseInt(vouchData.getRec(0).getAttribVal(payment_voucherObj._NUM_PAYMENTS));
		String ret[] = new String[numPayments];

		try
		{
			//Update Purch Order header Fields.
			String sqlStmt = " update purch_order set " +
							 purch_orderObj.PAYMENT_TERMS + " = " + ServerUtils.addQuotes(payTerms) + ", " +
							 purch_orderObj.PAYMENT_DUE_DATE + " = " + ServerUtils.addQuotes(payDueDate) +
							 " where id = '" + poId + "' and " +
							 " locality = '" + purch_orderObj.getObjLocality()+"'";
			_dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
									  sqlStmt);
			//Get the vendor Address
			Vector vendorVect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															 new vendorObj(),
															 " id = " + ServerUtils.addQuotes(vendorId) +
															 " and locality = '" + vendorObj.getObjLocality() + "'");
			if (vendorVect.size() > 0)
			{
				vendorObj venObj = (vendorObj)vendorVect.firstElement();
				if (venObj.get_is_remitto_sameas_shipto() != null && venObj.get_is_remitto_sameas_shipto().equals("Y"))
				{
					vendorAddr1 = venObj.get_shipto_addr1();
					vendorAddr2 = venObj.get_shipto_addr2();
					vendorAddr3 = venObj.get_shipto_addr3();
					vendorCity = venObj.get_shipto_city();
					vendorStCode = venObj.get_shipto_state_code();
					vendorZip = venObj.get_shipto_zip();
					vendorCountryCode = venObj.get_shipto_country_code();
				} else
				{
					vendorAddr1 = venObj.get_remit_addr1();
					vendorAddr2 = venObj.get_remit_addr2();
					vendorAddr3 = venObj.get_remit_addr3();
					vendorCity = venObj.get_remit_city();
					vendorStCode = venObj.get_remit_state_code();
					vendorZip = venObj.get_remit_zip();
					vendorCountryCode = venObj.get_remit_country_code();
				}
				ourAcctNumWithVendor = venObj.get_our_acct_no_with_vendor();
			}

            //Use the subtotal amount to derive each payment.
			String[] s_paymentAmts = calcMultiPayments(Double.parseDouble(s_paymentAmt), numPayments);
			for (int i=0; i<numPayments; i++)
			{
				s_paymentAmt = s_paymentAmts[i];

                //Only include the shipcharge amt on the first payvoucher.
                //This is done so that if a multi-pay is done and the user
                //later decides to void the voucher.  We will have only included
                //the ship charges on one of the vouchers.
                String shipChargeAmt = s_shipChargeAmt;
                if (i > 0) shipChargeAmt = null;

				//Insert to the Payment Voucher
				int voucherSEQ = _dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
															  _dbAdapter.SEQUENCE_PAY_VOUCHER);
				String voucherId = "VR"+ daiFormatUtil.padIntLeft(voucherSEQ, 6);
				String payVoucherSQL = " insert into payment_voucher (" +
									   payment_voucherObj.ID + ", " +
									   payment_voucherObj.LOCALITY + ", " +
									   payment_voucherObj.CREATED_BY + ", " +
									   payment_voucherObj.DATE_CREATED + ", " +
									   payment_voucherObj.ACCTID + ", " +
									   payment_voucherObj.ACCTNAME + ", " +
									   payment_voucherObj.INVOICE_NUM + ", " +
									   payment_voucherObj.INVOICE_DATE + ", " +
									   payment_voucherObj.PURCH_ORDER_ID + ", " +
									   payment_voucherObj.VENDOR_ID + ", " +
									   payment_voucherObj.VENDOR_NAME + ", " +
									   payment_voucherObj.PAYMENT_DUE_DATE + ", " +
									   payment_voucherObj.TOTAL_VALUE + ", " +
                                       payment_voucherObj.TOTAL_SHIPPING_CHARGES + ", " +
									   payment_voucherObj.VENDOR_ADDR1 + ", " +
									   payment_voucherObj.VENDOR_ADDR2 + ", " +
									   payment_voucherObj.VENDOR_ADDR3 + ", " +
									   payment_voucherObj.VENDOR_CITY + ", " +
									   payment_voucherObj.VENDOR_STATE_CODE + ", " +
									   payment_voucherObj.VENDOR_ZIP + ", " +
									   payment_voucherObj.VENDOR_COUNTRY_CODE + ", " +
									   payment_voucherObj.OUR_ACCT_NO_WITH_VENDOR + ", " +
									   payment_voucherObj.TOTAL_PAYMENTS_POSTED +
									   ") values (" +
									   "'" + voucherId + "', " +
									   "'"+ payment_voucherObj.getObjLocality() + "', " +
									   "'"+ _sessionMeta.getUserId() + "', " +
									   "'"+ daiFormatUtil.getCurrentDate() + "', " +
									   ServerUtils.addQuotes(expenseAcctNum) + ", " +
									   ServerUtils.addQuotes(expenseAcctName) + ", " +
									   ServerUtils.addQuotes(invoiceNum) + ", " +
									   ServerUtils.addQuotes(invoiceDate) + ", " +
									   ServerUtils.addQuotes(poId) + ", " +
									   ServerUtils.addQuotes(vendorId) + ", " +
									   ServerUtils.addQuotes(vendorName) + ", " +
									   ServerUtils.addQuotes(payDueDate) + ", " +
									   s_paymentAmt + ", " +
                                       shipChargeAmt + ", " +
									   ServerUtils.addQuotes(vendorAddr1) + ", " +
									   ServerUtils.addQuotes(vendorAddr2) + ", " +
									   ServerUtils.addQuotes(vendorAddr3) + ", " +
									   ServerUtils.addQuotes(vendorCity) + ", " +
									   ServerUtils.addQuotes(vendorStCode) + ", " +
									   ServerUtils.addQuotes(vendorZip) + ", " +
									   ServerUtils.addQuotes(vendorCountryCode) + ", " +
									   ServerUtils.addQuotes(ourAcctNumWithVendor) +  ", " +
									   "0" + //Need a non-null value for this field because of reporting(acct payable).
									   ")";
				_dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
										  payVoucherSQL);

				ret[i] = voucherId;
			}

			//Add Shipping Amt to Shipping Acct Detail Table
			if (shipAcctId != null && s_shipChargeAmt != null && Double.parseDouble(s_shipChargeAmt) != 0)
			{
				ServerUtils.postNewLedgerEntry(shipAcctId,	 //Acct ID
												invoiceNum,			//Ref
												invoiceDate, //Trans Date
												account_detailObj.TRANS_TYPE_GET_PURCHORD_BILL,	//transType
												s_shipChargeAmt,	  //debit
												null,			  //Credit
												vendorName); //note
			}

			//Add Acct Payable amt to the Acct Detail table
			default_accountsObj defAcctsObj = null;
			String acctExp = " id = '" + default_accountsObj.SINGLETON_ID + "'" +
							 " and locality = '" + default_accountsObj.getObjLocality() + "'";
			Vector defAcctsVect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															   new default_accountsObj(),
															   acctExp);
			if (defAcctsVect.size() > 0)
			{
				defAcctsObj = (default_accountsObj)defAcctsVect.firstElement();
			}

			if (s_shipChargeAmt != null && Double.parseDouble(s_shipChargeAmt) != 0)
			{
				ServerUtils.postNewLedgerEntry(defAcctsObj.get_accts_payable_id(),	 //Acct ID
												invoiceNum,			//Ref
												invoiceDate,   //Trans Date
												account_detailObj.TRANS_TYPE_GET_PURCHORD_BILL,	//transType
												null,	 //debit
												s_shipChargeAmt,	//Credit
												vendorName); //note
			}

			return ret;

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::postInvoiceReceipt failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	//*     payment_voucherObj.PURCH_ORDER_ID
	//*     payment_voucherObj.VENDOR_ID
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
	public String[] postBillReceipt(DBRecSet vouchData)
	throws daiException
	{
		_dbAdapterFactory    = _dbAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();

		//Let's make sure the Purchase Order Obj is valid first.  If not, throw
		//an exception.
		if (vouchData == null || vouchData.getSize() == 0)
		{
			throw new daiException("Invalid Purch Order Object.", this);
		} else if (vouchData.getSize() > 1)
		{
			throw new daiException("Expected only one Purch Order object.  Found > 1.", this);
		}

		String poId = "N/A";  //This is a bill not from a PO.
		String s_totPayAmt = vouchData.getRec(0).getAttribVal(payment_voucherObj.TOTAL_VALUE);
		String s_subtotalAmt = vouchData.getRec(0).getAttribVal(payment_voucherObj.SUBTOTAL_AMT);
		String vendorId = vouchData.getRec(0).getAttribVal(payment_voucherObj.VENDOR_ID);
		String vendorName = vouchData.getRec(0).getAttribVal(payment_voucherObj.VENDOR_NAME);
		String expenseAcctNum = vouchData.getRec(0).getAttribVal(payment_voucherObj.ACCTID);
		String expenseAcctName = vouchData.getRec(0).getAttribVal(payment_voucherObj.ACCTNAME);
		String payDueDate = vouchData.getRec(0).getAttribVal(payment_voucherObj.PAYMENT_DUE_DATE);
		String payTerms = vouchData.getRec(0).getAttribVal(payment_voucherObj.PAYMENT_TERMS);
		String invoiceNum = vouchData.getRec(0).getAttribVal(payment_voucherObj.INVOICE_NUM);
		String invoiceDate = vouchData.getRec(0).getAttribVal(payment_voucherObj.INVOICE_DATE);
		String shipChargeAmt = vouchData.getRec(0).getAttribVal(payment_voucherObj.TOTAL_SHIPPING_CHARGES);
		String shipAcctId = vouchData.getRec(0).getAttribVal(payment_voucherObj._SHIP_CHARGES_ACCT_ID);
		String shipAcctName = vouchData.getRec(0).getAttribVal(payment_voucherObj._SHIP_CHARGES_ACCT_NAME);
		String vendorAddr1 = "";
		String vendorAddr2 = "";
		String vendorAddr3 = "";
		String vendorCity = "";
		String vendorStCode = "";
		String vendorZip = "";
		String vendorCountryCode = "";
		String ourAcctNumWithVendor = "";
		int numPayments = Integer.parseInt(vouchData.getRec(0).getAttribVal(payment_voucherObj._NUM_PAYMENTS));
		String ret[] = new String[numPayments];

		try
		{
			//Get the vendor Address
			Vector vendorVect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															 new vendorObj(),
															 " id = " + ServerUtils.addQuotes(vendorId) +
															 " and locality = '" + vendorObj.getObjLocality() + "'");
			if (vendorVect.size() > 0)
			{
				vendorObj venObj = (vendorObj)vendorVect.firstElement();
				if (venObj.get_is_remitto_sameas_shipto() != null && venObj.get_is_remitto_sameas_shipto().equals("Y"))
				{
					vendorAddr1 = venObj.get_shipto_addr1();
					vendorAddr2 = venObj.get_shipto_addr2();
					vendorAddr3 = venObj.get_shipto_addr3();
					vendorCity = venObj.get_shipto_city();
					vendorStCode = venObj.get_shipto_state_code();
					vendorZip = venObj.get_shipto_zip();
					vendorCountryCode = venObj.get_shipto_country_code();
				} else
				{
					vendorAddr1 = venObj.get_remit_addr1();
					vendorAddr2 = venObj.get_remit_addr2();
					vendorAddr3 = venObj.get_remit_addr3();
					vendorCity = venObj.get_remit_city();
					vendorStCode = venObj.get_remit_state_code();
					vendorZip = venObj.get_remit_zip();
					vendorCountryCode = venObj.get_remit_country_code();
				}
				ourAcctNumWithVendor = venObj.get_our_acct_no_with_vendor();
			}

			String[] s_paymentAmts = calcMultiPayments(Double.parseDouble(s_totPayAmt), numPayments);
			for (int i=0; i<numPayments; i++)
			{
				String s_paymentAmt = s_paymentAmts[i];

				//Insert to the Payment Voucher
				int voucherSEQ = _dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
															  _dbAdapter.SEQUENCE_PAY_VOUCHER);
				String voucherId = "VR"+ daiFormatUtil.padIntLeft(voucherSEQ, 6);
				String payVoucherSQL = " insert into payment_voucher (" +
									   payment_voucherObj.ID + ", " +
									   payment_voucherObj.LOCALITY + ", " +
									   payment_voucherObj.CREATED_BY + ", " +
									   payment_voucherObj.DATE_CREATED + ", " +
									   payment_voucherObj.ACCTID + ", " +
									   payment_voucherObj.ACCTNAME + ", " +
									   payment_voucherObj.INVOICE_NUM + ", " +
									   payment_voucherObj.INVOICE_DATE + ", " +
									   payment_voucherObj.PURCH_ORDER_ID + ", " +
									   payment_voucherObj.VENDOR_ID + ", " +
									   payment_voucherObj.VENDOR_NAME + ", " +
									   payment_voucherObj.PAYMENT_DUE_DATE + ", " +
									   payment_voucherObj.TOTAL_VALUE + ", " +
									   payment_voucherObj.VENDOR_ADDR1 + ", " +
									   payment_voucherObj.VENDOR_ADDR2 + ", " +
									   payment_voucherObj.VENDOR_ADDR3 + ", " +
									   payment_voucherObj.VENDOR_CITY + ", " +
									   payment_voucherObj.VENDOR_STATE_CODE + ", " +
									   payment_voucherObj.VENDOR_ZIP + ", " +
									   payment_voucherObj.VENDOR_COUNTRY_CODE + ", " +
									   payment_voucherObj.OUR_ACCT_NO_WITH_VENDOR + ", " +
									   payment_voucherObj.TOTAL_PAYMENTS_POSTED +
									   ") values (" +
									   "'" + voucherId + "', " +
									   "'"+ payment_voucherObj.getObjLocality() + "', " +
									   "'"+ _sessionMeta.getUserId() + "', " +
									   "'"+ daiFormatUtil.getCurrentDate() + "', " +
									   ServerUtils.addQuotes(expenseAcctNum) + ", " +
									   ServerUtils.addQuotes(expenseAcctName) + ", " +
									   ServerUtils.addQuotes(invoiceNum) + ", " +
									   ServerUtils.addQuotes(invoiceDate) + ", " +
									   ServerUtils.addQuotes(poId) + ", " +
									   ServerUtils.addQuotes(vendorId) + ", " +
									   ServerUtils.addQuotes(vendorName) + ", " +
									   ServerUtils.addQuotes(payDueDate) + ", " +
									   s_paymentAmt + ", " +
									   ServerUtils.addQuotes(vendorAddr1) + ", " +
									   ServerUtils.addQuotes(vendorAddr2) + ", " +
									   ServerUtils.addQuotes(vendorAddr3) + ", " +
									   ServerUtils.addQuotes(vendorCity) + ", " +
									   ServerUtils.addQuotes(vendorStCode) + ", " +
									   ServerUtils.addQuotes(vendorZip) + ", " +
									   ServerUtils.addQuotes(vendorCountryCode) + ", " +
									   ServerUtils.addQuotes(ourAcctNumWithVendor) +  ", " +
									   "0" + //Need a non-null value for this field because of reporting(acct payable).
									   ")";
				_dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
										  payVoucherSQL);

				ret[i] = voucherId;
			}

			//Only do this when we're doing an Expense Bill as opposed to an
			//invoice receipt.
			if (expenseAcctNum != null)
			{
				//Add Expense amt to the Acct Detail table
				ServerUtils.postNewLedgerEntry(expenseAcctNum,	 //Acct ID
												invoiceNum,			//Ref
												invoiceDate,  //Trans Date
												account_detailObj.TRANS_TYPE_GET_PURCHORD_BILL,	//transType
												s_totPayAmt,	 //debit
												null,			  //Credit
												vendorName); //note

			}

			//Add Acct Payable amt to the Acct Detail table
			default_accountsObj defAcctsObj = null;
			String acctExp = " id = '" + default_accountsObj.SINGLETON_ID + "'" +
							 " and locality = '" + default_accountsObj.getObjLocality() + "'";
			Vector defAcctsVect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															   new default_accountsObj(),
															   acctExp);
			if (defAcctsVect.size() > 0)
			{
				defAcctsObj = (default_accountsObj)defAcctsVect.firstElement();
			}

			if (s_totPayAmt != null && Double.parseDouble(s_totPayAmt) != 0)
			{
				ServerUtils.postNewLedgerEntry(defAcctsObj.get_accts_payable_id(),	 //Acct ID
												invoiceNum,			//Ref
												invoiceDate,   //Trans Date
												account_detailObj.TRANS_TYPE_GET_PURCHORD_BILL,	//transType
												null,	 //debit
												s_totPayAmt,	//Credit
												vendorName); //note
			}

			return ret;

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::postBillReceipt failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	public void createCheckPrintScratchData(String beginCheckNum, String endCheckNum)
	throws daiException
	{
		_dbAdapterFactory    = _dbAdapterFactory.getInstance();
		_dbAdapter           = _dbAdapterFactory.getDBAdapter();

		String exp = payment_voucherObj.PRINT_CHECK + " = 'Y' AND " +
					 payment_voucherObj.LOCALITY + " = 'SUPER' AND " +
					 payment_voucherObj.CHECK_NUM + ">= '" + beginCheckNum + "' AND " +
					 payment_voucherObj.CHECK_NUM + "<= '" + endCheckNum + "'" +
					 " order by " + payment_voucherObj.CHECK_NUM;

		payment_voucherObj vouchObj;
		String prevCheckNum = "";
		String currCheckNum = "";
		double totCheckAmt = 0;
		String sqlStmt;
		Vector totCheckAmts = new Vector();
		Vector checkNums = new Vector();
		Vector totCheckAmtsText = new Vector();

		try
		{
			//Get all the payment_vouchers that we need to pay
			Vector vouchVect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															new payment_voucherObj(),
															exp);

			//Create the master/detail scratch records
			for (int i=0; i<vouchVect.size(); i++)
			{
				vouchObj = (payment_voucherObj) vouchVect.elementAt(i);
				currCheckNum = vouchObj.get_check_num();

				if (!prevCheckNum.equals(currCheckNum) && i != 0)
				{
					//Insert into the header scratch table.
					totCheckAmts.addElement(Double.toString(totCheckAmt));
					checkNums.addElement(prevCheckNum);
					totCheckAmtsText.addElement(daiFormatUtil.convCurrencyToText(new Double(totCheckAmt)));

					totCheckAmt = 0;
					//Accumulate the check total amt
					String s_payAmt = vouchObj.get_total_payments_posted();
					if (s_payAmt == null) s_payAmt = "0.00";
					totCheckAmt = totCheckAmt + Double.parseDouble(s_payAmt);
				} else
				{
					//Accumulate the check total amt
					String s_payAmt = vouchObj.get_total_payments_posted();
					if (s_payAmt == null) s_payAmt = "0.00";
					totCheckAmt = totCheckAmt + Double.parseDouble(s_payAmt);
				}

				prevCheckNum = currCheckNum;
			}

			if (vouchVect.size() > 0)
			{
				totCheckAmts.addElement(Double.toString(totCheckAmt));
				checkNums.addElement(currCheckNum);
				totCheckAmtsText.addElement(daiFormatUtil.convCurrencyToText(new Double(totCheckAmt)));
			}

			for (int i=0; i<checkNums.size(); i++)
			{
				sqlStmt = "update " + payment_voucherObj.TABLE_NAME +
						  " set " + payment_voucherObj.CHECK_TOT_AMT_FOR_PRINTING + " = " + (String)totCheckAmts.elementAt(i) + ", " +                                    payment_voucherObj.CHECK_TOT_AMT_TEXT_FOR_PRINTING + " = '" + (String)totCheckAmtsText.elementAt(i) + "' " +
						  " where " + payment_voucherObj.CHECK_NUM + " = '" + (String)checkNums.elementAt(i) + "' and " +
						  payment_voucherObj.PRINT_CHECK + " = 'Y'";
				_dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
										  sqlStmt);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::createCheckPrintScratchData failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	public void cancelPurchaseOrder(String poId)
	throws daiException
	{
        try {
        /*
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
        */
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::cancelPurchaseOrder failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}

	private purch_orderObj updatePaymentPurchHeaderFields(payment_voucherObj payVoucherObj)
	throws daiException
	{
		purch_orderObj purchOrdObj;
		try
		{
			//Get the Purch Order Header obj we are interested in
			String exp = " id = '"+payVoucherObj.get_purch_order_id()+
						 "' and locality='"+purch_orderObj.getObjLocality()+"'";
			Vector purchOrdVect = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															   new purch_orderObj(),
															   exp);
			purchOrdObj = (purch_orderObj)purchOrdVect.firstElement();

			//Get the existing amout paid field so we can update it.
			String s_totPayPosted =purchOrdObj.get_total_payments_posted();
			if (s_totPayPosted == null)	s_totPayPosted = "0.0";

			//Update the abount paid field.
			String sqlUpdateStmt = "update purch_order set " + purch_orderObj.TOTAL_PAYMENTS_POSTED + " = " +
								   s_totPayPosted + "+" + payVoucherObj.get_payment_amt() +
								   " where " + exp;

			_dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(), sqlUpdateStmt);
		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::updatePaymentPurchHeaderFields failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
		return purchOrdObj;
	}
}

