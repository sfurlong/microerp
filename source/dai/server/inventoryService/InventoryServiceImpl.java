package dai.server.inventoryService;

import java.util.Vector;

import dai.server.serverShared.ServerUtils;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cust_order_itemObj;
import dai.shared.businessObjs.itemObj;
import dai.shared.businessObjs.item_inventoryObj;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.businessObjs.purch_order_itemObj;
import dai.shared.businessObjs.shipment_itemObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiException;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csLoggerAdapter;
import dai.shared.csAdapters.csLoggerAdapterFactory;

public class InventoryServiceImpl
{
	csDBAdapterFactory  dbAdapterFactory;
	csDBAdapter         dbAdapter;
	csLoggerAdapter     _logger;
	SessionMetaData     _sessionMeta;

	public InventoryServiceImpl()
	{
		_logger         = csLoggerAdapterFactory.getInstance().getLoggerAdapter();
		_sessionMeta    = SessionMetaData.getInstance();
	}


	public void postCustOrderItemsToInventory(cust_order_itemObj[] ordItemObjs)
	throws daiException
	{
		dbAdapterFactory    = dbAdapterFactory.getInstance();
		dbAdapter           = dbAdapterFactory.getDBAdapter();

		if (ordItemObjs == null) return;

		//Local vars.
		String exp = "";
		String ordItemId = "";
		String ordItemLocality = "";
		String s_ordItemQtyOrdered  = "0";
		String s_ordItemQtyPosted   = "0";
		int ordItemQtyOrdered  = 0;
		int ordItemQtyPosted   = 0;
		int qtyCustBackOrd = 0;

		//Loop through all the Order Items in the vector.
		for (int i=0; i<ordItemObjs.length; i++)
		{
			ordItemId = ordItemObjs[i].get_item_id();
			ordItemLocality = ordItemObjs[i].get_locality();

			//Get the values or Qty Ordered and Qty Ordered Posted
			s_ordItemQtyOrdered = ordItemObjs[i].get_qty_ordered();
			s_ordItemQtyPosted = ordItemObjs[i].get_qty_inventory_posted();
			if (s_ordItemQtyOrdered == null) s_ordItemQtyOrdered = "0";
			if (s_ordItemQtyPosted == null)	s_ordItemQtyPosted = "0";
			ordItemQtyOrdered = new Double(s_ordItemQtyOrdered).intValue();
			ordItemQtyPosted = new Double(s_ordItemQtyPosted).intValue();

			int ordItemQtyPostDiff = ordItemQtyOrdered - ordItemQtyPosted;

			//Dont adjust inventory for line items that are repair
			String isInRepair = ordItemObjs[i].get_is_internal_repair();
			if (isInRepair == null)	isInRepair = "N";
			String isOutRepair = ordItemObjs[i].get_is_external_repair();
			if (isOutRepair == null) isOutRepair = "N";

			if (ordItemQtyPostDiff != 0 && !isInRepair.equals("Y") && !isOutRepair.equals("Y"))
			{
				//Adjust the qtyPosted value
				ordItemQtyPosted = ordItemQtyPosted + ordItemQtyPostDiff;

				try
				{
					//Update the Order Item Business Object and DB to reflect
					//the new total item amount posted.
					ordItemObjs[i].set_qty_inventory_posted((new Integer(ordItemQtyPosted)).toString());
					exp = "id = '"+ordItemObjs[i].get_id()+"' and locality='"+
						  ordItemObjs[i].get_locality()+"' and detail_id="+
						  ordItemObjs[i].get_detail_id();
					dbAdapter.update(_sessionMeta.getClientServerSecurity(),
									 ordItemObjs[i],
									 exp);

					//Get the Current Onhand Qty and Cust BackOrd Qty
					exp = " id = '" + ordItemId + "' and locality = '" + ordItemLocality + "'";
					Vector objs = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															  new itemObj(),
															  exp);
					if (objs.size() == 0)
					{
						//The item needs to exist to perform this operation
						throw new daiException("Can't Update Inventory, Item does not exist: "+exp, this);
					}

					itemObj item_obj = (itemObj)objs.firstElement();
					String s_qtyCustBackOrd = item_obj.get_backorder_to_cust_qty();
					if (s_qtyCustBackOrd == null) s_qtyCustBackOrd = "0";

					//Calculte new QtyCustBackOrd value
					qtyCustBackOrd = (new Double(s_qtyCustBackOrd).intValue()) + ordItemQtyPostDiff;

					//Update the DB with the new Inventory Qty
					item_obj.set_backorder_to_cust_qty(Integer.toString(qtyCustBackOrd));
					dbAdapter.update(_sessionMeta.getClientServerSecurity(),
									 item_obj,
									 exp);

					//Add a detail entry to the Item Inventory table
					item_inventoryObj itemInvObj = new item_inventoryObj();
					itemInvObj.set_id(item_obj.get_id());
					itemInvObj.setDefaults(); //Sets date_created, created_by, locality
					int newSeqNum = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
																dbAdapter.SEQUENCE_GENERIC_DETAIL_ID);
					itemInvObj.set_detail_id(Integer.toString(newSeqNum));
					itemInvObj.set_adjustment_type(item_inventoryObj.ADJUST_TYPE_CUST_ORDER);
					itemInvObj.set_adjustment_reason(ordItemObjs[i].get_id());
					itemInvObj.set_adjustment_qty(Integer.toString(ordItemQtyPostDiff));
					itemInvObj.set_qty_cust_back_ord(Integer.toString(qtyCustBackOrd));
					itemInvObj.set_qty_vend_back_ord(item_obj.get_backorder_to_vendor_qty());
					itemInvObj.set_qty_onhand(item_obj.get_onhand_qty());
					itemInvObj.set_note("Adjustment for Cust Order: "+ordItemObjs[i].get_id());
					dbAdapter.insert(_sessionMeta.getClientServerSecurity(),
									 itemInvObj);

				} catch (Exception e)
				{
					e.printStackTrace();
					String msg = this.getClass().getName()+"::postCustOrderItemsToInventory failure"+
								 "\n"+e.getLocalizedMessage();
					_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
					throw new daiException(msg, null);
				}
			}
		}
	}

	public void postShipmentItemsToInventory(shipment_itemObj[] shipItemObjs)
	throws daiException
	{
		dbAdapterFactory    = dbAdapterFactory.getInstance();
		dbAdapter           = dbAdapterFactory.getDBAdapter();

		if (shipItemObjs == null) return;

		//Local vars.
		String exp = "";
		String shipItemId = "";
		String shipItemLocality = "";
		String s_shipItemQtyShipped  = "0";
		String s_shipItemQtyPosted   = "0";
		int shipItemQtyShipped  = 0;
		int shipItemQtyPosted   = 0;
		int qtyCustBackOrd = 0;
		int qtyOnHand = 0;

		//Loop through all the shipment Items in the vector
		for (int i=0; i<shipItemObjs.length; i++)
		{
			shipItemId = shipItemObjs[i].get_item_id();
			shipItemLocality = shipItemObjs[i].get_locality();

			//Get the values or Qty Shipped and Qty Shipped Posted
			s_shipItemQtyShipped = shipItemObjs[i].get_qty_shipped();
			s_shipItemQtyPosted = shipItemObjs[i].get_qty_inventory_posted();
			if (s_shipItemQtyShipped == null) s_shipItemQtyShipped = "0";
			if (s_shipItemQtyPosted == null) s_shipItemQtyPosted = "0";
			shipItemQtyShipped = new Double(s_shipItemQtyShipped).intValue();
			shipItemQtyPosted = new Double(s_shipItemQtyPosted).intValue();

			int shipItemQtyPostDiff = shipItemQtyShipped - shipItemQtyPosted;

			//Dont adjust inventory for line items that are repair
			String isInRepair = shipItemObjs[i].get_is_internal_repair();
			if (isInRepair == null)	isInRepair = "N";
			String isOutRepair = shipItemObjs[i].get_is_external_repair();
			if (isOutRepair == null) isOutRepair = "N";

			if (shipItemQtyPostDiff != 0 && !isInRepair.equals("Y") && !isOutRepair.equals("Y"))
			{
				//Adjust the qtyPosted value
				shipItemQtyPosted = shipItemQtyPosted + shipItemQtyPostDiff;

				//Updat the Inventory
				try
				{
					//Update the Shipment Item Business Object and DB to reflect
					//the new total item amount posted.
					shipItemObjs[i].set_qty_inventory_posted((new Integer(shipItemQtyPosted)).toString());
					exp = "id = '"+shipItemObjs[i].get_id()+"' and locality='"+
						  shipItemObjs[i].get_locality()+"' and detail_id="+
						  shipItemObjs[i].get_detail_id();
					dbAdapter.update(_sessionMeta.getClientServerSecurity(),
									 shipItemObjs[i],
									 exp);

					//Get the Current Onhand Qty and Cust Backship Qty
					exp = " id = '" + shipItemId + "' and locality = '" + shipItemLocality + "'";
					Vector objs = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															  new itemObj(),
															  exp);
					if (objs.size() == 0)
					{
						//The item needs to exist to perform this operation
						throw new daiException("Can't Update Inventory, Item does not exist: "+exp, this);
					}

					itemObj item_obj = (itemObj)objs.firstElement();
					String s_qtyOnHand = item_obj.get_onhand_qty();
					String s_qtyCustBackOrd = item_obj.get_backorder_to_cust_qty();
					String s_qtyVendorBackOrd = item_obj.get_backorder_to_vendor_qty();
					if (s_qtyCustBackOrd == null) s_qtyCustBackOrd = "0";
					if (s_qtyVendorBackOrd == null) s_qtyVendorBackOrd = "0";
					if (s_qtyOnHand == null) s_qtyOnHand = "0";

					//Calculte new QtyOnHand and QtyCustBackOrd values
					qtyCustBackOrd = (new Double(s_qtyCustBackOrd).intValue()) - shipItemQtyPostDiff;
					qtyCustBackOrd = (new Double(s_qtyCustBackOrd).intValue()) - shipItemQtyPostDiff;
					int qtyVendorBackOrd = (new Double(s_qtyVendorBackOrd).intValue());
					qtyOnHand = (new Double(s_qtyOnHand).intValue()) - shipItemQtyPostDiff;

					//Update the DB with the new Inventory Qty
					item_obj.set_backorder_to_cust_qty(Integer.toString(qtyCustBackOrd));
					item_obj.set_onhand_qty(Integer.toString(qtyOnHand));
					dbAdapter.update(_sessionMeta.getClientServerSecurity(),
									 item_obj,
									 exp);

					//Add a detail entry to the Item Inventory table
					item_inventoryObj itemInvObj = new item_inventoryObj();
					itemInvObj.set_id(item_obj.get_id());
					itemInvObj.setDefaults(); //Sets date_created, created_by, locality
					int newSeqNum = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
																dbAdapter.SEQUENCE_GENERIC_DETAIL_ID);
					itemInvObj.set_detail_id(Integer.toString(newSeqNum));
					itemInvObj.set_adjustment_type(item_inventoryObj.ADJUST_TYPE_SHIPMENT);
					itemInvObj.set_adjustment_reason(shipItemObjs[i].get_id());
					itemInvObj.set_qty_cust_back_ord(Integer.toString(qtyCustBackOrd));
					itemInvObj.set_qty_vend_back_ord(Integer.toString(qtyVendorBackOrd));
					itemInvObj.set_qty_onhand(Integer.toString(qtyOnHand));
					itemInvObj.set_adjustment_qty(Integer.toString(shipItemQtyPostDiff));
					itemInvObj.set_note("Adjustment for Cust Shipment: "+shipItemObjs[i].get_id());
					dbAdapter.insert(_sessionMeta.getClientServerSecurity(),
									 itemInvObj);
				} catch (Exception e)
				{
					e.printStackTrace();
					String msg = this.getClass().getName()+"::postShipmentItemsToInventory failure"+
								 "\n"+e.getLocalizedMessage();
					_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
					throw new daiException(msg, null);
				}
			}
		}
	}

	public void postPurchOrderItemsToInventory(purch_order_itemObj[] purchItemObjs)
	throws daiException
	{
		dbAdapterFactory    = dbAdapterFactory.getInstance();
		dbAdapter           = dbAdapterFactory.getDBAdapter();

		if (purchItemObjs == null) return;

		//Local vars.
		String exp = "";
		String purchItemId = "";
		String purchItemLocality = "";
		String s_purchItemQtyOrdered  = "0";
		String s_purchItemQtyPosted   = "0";
		int purchItemQtyOrdered  = 0;
		int purchItemQtyPosted   = 0;
		int qtyVendorBackOrd = 0;

		//Loop through all the Order Items in the vector.
		for (int i=0; i<purchItemObjs.length; i++)
		{
			purchItemId = purchItemObjs[i].get_item_id();
			purchItemLocality = purchItemObjs[i].get_locality();

			//Get the values or Qty Ordered and Qty Ordered Posted
			s_purchItemQtyOrdered = purchItemObjs[i].get_qty_ordered();
			s_purchItemQtyPosted = purchItemObjs[i].get_qty_inventory_posted();
			if (s_purchItemQtyOrdered == null) s_purchItemQtyOrdered = "0";
			if (s_purchItemQtyPosted == null) s_purchItemQtyPosted = "0";
			purchItemQtyOrdered = new Double(s_purchItemQtyOrdered).intValue();
			purchItemQtyPosted = new Double(s_purchItemQtyPosted).intValue();

			int purchItemQtyPostDiff = purchItemQtyOrdered - purchItemQtyPosted;

			//Do not adjust Inventor If this is a Repair item
			String isRepair = purchItemObjs[i].get_is_item_repair();
			if (isRepair == null) isRepair = "N";

			if (purchItemQtyPostDiff != 0 && !isRepair.equals("Y"))
			{
				//Adjust the qtyPosted value
				purchItemQtyPosted = purchItemQtyPosted + purchItemQtyPostDiff;

				try
				{
					//Update the Purch Order Item Business Object and DB to reflect
					//the new total item amount posted.
					purchItemObjs[i].set_qty_inventory_posted((new Integer(purchItemQtyPosted)).toString());
					exp = "id = '"+purchItemObjs[i].get_id()+"' and locality='"+
						  purchItemObjs[i].get_locality()+"' and detail_id="+
						  purchItemObjs[i].get_detail_id();
					dbAdapter.update(_sessionMeta.getClientServerSecurity(),
									 purchItemObjs[i],
									 exp);

					//Get the Vendor BackOrd Qty
					exp = " id = '" + purchItemId + "' and locality = '" + purchItemLocality + "'";
					Vector objs = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
															  new itemObj(),
															  exp);
					if (objs.size() == 0)
					{
						//The item needs to exist to perform this operation
						throw new daiException("Can't Update Inventory, Item does not exist in Item Master File: "+exp, this);
					}

					itemObj item_obj = (itemObj)objs.firstElement();
					String s_qtyVendorBackOrd = item_obj.get_backorder_to_vendor_qty();
					if (s_qtyVendorBackOrd == null)	s_qtyVendorBackOrd = "0";

					//Calculte new QtyVendorBackOrd value
					qtyVendorBackOrd = (new Double(s_qtyVendorBackOrd).intValue()) + purchItemQtyPostDiff;

					//Update the DB with the new Inventory Qty
					item_obj.set_backorder_to_vendor_qty(Integer.toString(qtyVendorBackOrd));
					dbAdapter.update(_sessionMeta.getClientServerSecurity(),
									 item_obj,
									 exp);

					//Add a detail entry to the Item Inventory table
					item_inventoryObj itemInvObj = new item_inventoryObj();
					itemInvObj.set_id(item_obj.get_id());
					itemInvObj.setDefaults(); //Sets date_created, created_by, locality
					int newSeqNum = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
																dbAdapter.SEQUENCE_GENERIC_DETAIL_ID);
					itemInvObj.set_detail_id(Integer.toString(newSeqNum));
					itemInvObj.set_adjustment_type(item_inventoryObj.ADJUST_TYPE_PURCH_ORDER);
					itemInvObj.set_adjustment_reason(purchItemObjs[i].get_id());
					itemInvObj.set_qty_cust_back_ord(item_obj.get_backorder_to_cust_qty());
					itemInvObj.set_qty_vend_back_ord(Integer.toString(qtyVendorBackOrd));
					itemInvObj.set_qty_onhand(item_obj.get_onhand_qty());
					itemInvObj.set_adjustment_qty(Integer.toString(purchItemQtyPostDiff));
					itemInvObj.set_note("Adjustment for Purchase Order: "+purchItemObjs[i].get_id());
					//Set the cost for this inventory item.
					itemInvObj.set_item_purch_price(item_obj.get_purchase_price());
					itemInvObj.set_qty_avail_for_inventory_accting(Integer.toString(purchItemQtyPostDiff));
					dbAdapter.insert(_sessionMeta.getClientServerSecurity(),
									 itemInvObj);
				} catch (Exception e)
				{
					e.printStackTrace();
					String msg = this.getClass().getName()+"::postPurchOrderItemsToInventory failure"+
								 "\n"+e.getLocalizedMessage();
					_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
					throw new daiException(msg, null);
				}
			}
		}
	}

	// Post Received Items To Inventory //
	//* The following attributes are supplied int he collection.
	//*      purch_orderObj.ID
	//*      purch_order_itemObj.ITEM_ID
	//*      purch_order_itemObj.QTY_ORDERED
	//*      purch_order_itemObj.QTY_RECEIVED
	//*      purch_order_itemObj.IS_ITEM_REPAIR
	//*      purch_order_itemObj._QTY_TO_RECEIVE
	public void postReceivedItemsToInventory(DBRecSet poItemsData)
	throws daiException
	{
		dbAdapterFactory    = dbAdapterFactory.getInstance();
		dbAdapter           = dbAdapterFactory.getDBAdapter();

		//Local vars.
		String poId;
		String exp = "";
		String poItemId = "";
		String poItemLocality = "";
		String s_qtyToReceive;
		String s_qtyReceived;
		int qtyToReceive;
		int qtyReceived;
		double qtyVendorBackOrd;
		double qtyOnHand;
		String sqlStmt;
		String isRepair = "";

		DBRec poItemData;

		//Loop through all the Purch Order Items in the vector
		for (int i=0; i<poItemsData.getSize(); i++)
		{
			poItemData = poItemsData.getRec(i);

			isRepair = poItemData.getAttribVal(purch_order_itemObj.IS_ITEM_REPAIR);

			//SKIP THIS LINE ITEM IF IT IS A REPAIR.
			if (isRepair != null && isRepair.equals("Y")) continue;

			poId = poItemData.getAttribVal(purch_orderObj.ID);
			poItemId = poItemData.getAttribVal(purch_order_itemObj.ITEM_ID);
			s_qtyToReceive = poItemData.getAttribVal(purch_order_itemObj._QTY_TO_RECEIVE);
			if (s_qtyToReceive == null)	s_qtyToReceive = "0";
			poItemLocality = purch_order_itemObj.getObjLocality();

			//Update the Inventory
			try
			{
				//Get the Current Onhand Qty and Vendor BackOrder Qty
				exp = " id = '" + poItemId + "' and locality = '" + poItemLocality + "'";
				Vector objs = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
														  new itemObj(),
														  exp);
				if (objs.size() == 0)
				{
					//The item needs to exist to perform this operation
					throw new daiException("Can't Update Inventory, Item does not exist: "+exp, this);
				}

				itemObj item_obj = (itemObj)objs.firstElement();
				String s_qtyOnHand = item_obj.get_onhand_qty();
				String s_qtyVendorBackOrd = item_obj.get_backorder_to_vendor_qty();
				if (s_qtyVendorBackOrd == null)	s_qtyVendorBackOrd = "0";
				if (s_qtyOnHand == null) s_qtyOnHand = "0";

				//Calculte new QtyOnHand and QtyVendorBackOrd values
				qtyVendorBackOrd = Double.parseDouble(s_qtyVendorBackOrd) - Double.parseDouble(s_qtyToReceive);
				qtyOnHand = Double.parseDouble(s_qtyOnHand) + Double.parseDouble(s_qtyToReceive);

				//Update the DB with the new Inventory Qty
				item_obj.set_backorder_to_vendor_qty(Double.toString(qtyVendorBackOrd));
				item_obj.set_onhand_qty(Double.toString(qtyOnHand));
				dbAdapter.update(_sessionMeta.getClientServerSecurity(),
								 item_obj, exp);

				//Add a detail entry to the Item Inventory table
				item_inventoryObj itemInvObj = new item_inventoryObj();
				itemInvObj.set_id(item_obj.get_id());
				itemInvObj.setDefaults(); //Sets date_created, created_by, locality

				int newSeqNum = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
															dbAdapter.SEQUENCE_GENERIC_DETAIL_ID);
				itemInvObj.set_detail_id(Integer.toString(newSeqNum));
				itemInvObj.set_adjustment_type(item_inventoryObj.ADJUST_TYPE_RECEIVE_INVENTORY);
				itemInvObj.set_adjustment_reason(poId);
				itemInvObj.set_qty_cust_back_ord(item_obj.get_backorder_to_cust_qty());
				itemInvObj.set_qty_vend_back_ord(Double.toString(qtyVendorBackOrd));
				itemInvObj.set_qty_onhand(Double.toString(qtyOnHand));
				itemInvObj.set_adjustment_qty(s_qtyToReceive);
				itemInvObj.set_note("Adjustment for Purch Order: " + poId);
				dbAdapter.insert(_sessionMeta.getClientServerSecurity(),
								 itemInvObj);
			} catch (Exception e)
			{
				e.printStackTrace();
				String msg = this.getClass().getName()+"::receiveInventory failure"+
							 "\n"+e.getLocalizedMessage();
				_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
				throw new daiException(msg, null);
			}
		}
	}

	// itemObj.id
	// itemObj.BACKORDER_TO_CUST_QTY - this will represent qty to add/subtract
	// itemObj.BACKORDER_TO_VENDOR_QTY - this will represent qty to add/subtract
	// itemObj.ONHAND_QTY - this will represent the qty to add/subtract
	// item_inventoryObj.ADJUSTMENT_REASON
	// item_inventoryObj.ADJUSTMENT_TYPE
	// item_inventoryObj.DO_MANUAL_ADJUST_CUST_BACK
	// item_inventoryObj.DO_MANUAL_ADJUST_VEND_BACK
	// item_inventoryObj.DO_MANUAL_ADJUST_ONHAND
	// item_inventoryObj.note;
	public void postInventoryAdjustment(DBRecSet itemData)
	throws daiException
	{
		dbAdapterFactory    = dbAdapterFactory.getInstance();
		dbAdapter           = dbAdapterFactory.getDBAdapter();
		String itemId = itemData.getRec(0).getAttribVal(itemObj.ID);
		String adjQtyCustBack = itemData.getRec(0).getAttribVal(itemObj.BACKORDER_TO_CUST_QTY);
		String adjQtyVendBack = itemData.getRec(0).getAttribVal(itemObj.BACKORDER_TO_VENDOR_QTY);
		String adjQtyOnHand = itemData.getRec(0).getAttribVal(itemObj.ONHAND_QTY);
		String adjNote = itemData.getRec(0).getAttribVal(item_inventoryObj.NOTE);
		String adjType = itemData.getRec(0).getAttribVal(item_inventoryObj.ADJUSTMENT_TYPE);
		String adjReason = itemData.getRec(0).getAttribVal(item_inventoryObj.ADJUSTMENT_REASON);
		String doCustBack = itemData.getRec(0).getAttribVal(item_inventoryObj.DO_MANUAL_ADJUST_CUST_BACK);
		String doVendBack = itemData.getRec(0).getAttribVal(item_inventoryObj.DO_MANUAL_ADJUST_VEND_BACK);
		String doOnHand = itemData.getRec(0).getAttribVal(item_inventoryObj.DO_MANUAL_ADJUST_ONHAND);
		if (adjQtyCustBack == null)	adjQtyCustBack = "0";
		if (adjQtyVendBack == null)	adjQtyVendBack = "0";
		if (adjQtyOnHand == null) adjQtyOnHand = "0";

		try
		{

			//Get the Current ItemObj inventory values
			String exp = " id = " + ServerUtils.addQuotes(itemId) +
						 " and locality = " + ServerUtils.addQuotes(itemObj.getObjLocality());
			Vector objs = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
													  new itemObj(),
													  exp);
			if (objs.size() == 0)
			{
				//The item needs to exist to perform this operation
				throw new daiException("Can't Update Inventory, Item does not exist: "+exp, this);
			}

			itemObj item_obj = (itemObj)objs.firstElement();
			String s_qtyOnHand = item_obj.get_onhand_qty();
			String s_qtyVendBack = item_obj.get_backorder_to_vendor_qty();
			String s_qtyCustBack = item_obj.get_backorder_to_cust_qty();
			if (s_qtyVendBack == null)	s_qtyVendBack = "0";
			if (s_qtyCustBack == null)	s_qtyCustBack = "0";
			if (s_qtyOnHand == null) s_qtyOnHand = "0";

			//Update the Item Header with the new Inventory Summaries.
			if (doCustBack != null && doCustBack.equals("Y"))
			{
				int i_adjQtyCustBack = new Double(s_qtyCustBack).intValue() + new Double(adjQtyCustBack).intValue();
				s_qtyCustBack = Integer.toString(i_adjQtyCustBack);
				item_obj.set_backorder_to_cust_qty(s_qtyCustBack);
			}
			if (doVendBack != null && doVendBack.equals("Y"))
			{
				int i_adjQtyVendBack = new Double(s_qtyVendBack).intValue() + new Double(adjQtyVendBack).intValue();
				s_qtyVendBack = Integer.toString(i_adjQtyVendBack);
				item_obj.set_backorder_to_vendor_qty(s_qtyVendBack);
			}
			if (doOnHand != null && doOnHand.equals("Y"))
			{
				int i_newQtyOnHand = new Double(s_qtyOnHand).intValue() + new Double(adjQtyOnHand).intValue();
				s_qtyOnHand = Integer.toString(i_newQtyOnHand);
				item_obj.set_onhand_qty(s_qtyOnHand);
			}
			dbAdapter.update(_sessionMeta.getClientServerSecurity(),
							 item_obj,
							 exp);

			//Insert a new row in the Item Inventory Table
			item_inventoryObj invObj = new item_inventoryObj();
			invObj.setDefaults();
			invObj.set_id(itemId);
			int newSeqNum = dbAdapter.getNewSequenceNum(_sessionMeta.getClientServerSecurity(),
														dbAdapter.SEQUENCE_GENERIC_DETAIL_ID);
			invObj.set_detail_id(Integer.toString(newSeqNum));
			invObj.set_qty_cust_back_ord(s_qtyCustBack);
			invObj.set_qty_vend_back_ord(s_qtyVendBack);
			invObj.set_qty_onhand(s_qtyOnHand);
			invObj.set_adjustment_type(adjType);
			invObj.set_adjustment_reason(adjReason);
			invObj.set_note(adjNote);
			dbAdapter.insert(_sessionMeta.getClientServerSecurity(), invObj);

		} catch (Exception e)
		{
			e.printStackTrace();
			String msg = this.getClass().getName()+"::postInventoryAdjustment failure"+
						 "\n"+e.getLocalizedMessage();
			_logger.logError(_sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
		}
	}
}
