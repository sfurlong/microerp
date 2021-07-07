/*
 * Created on Mar 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dai.server.dbService;

import dai.shared.cmnSvcs.SessionMetaData;

/**
 * @author sfurlong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DBSequenceFactory {

    public static DBSequenceGenerator getSequenceGenerator() throws Exception {

        String dbType = SessionMetaData.getInstance().getDatabaseType();
        DBSequenceGenerator seqGenerator = null;

        if (dbType.equals("firebird") || dbType.equals("interbase")) {
            seqGenerator = new FireBirdDBSequenceGenerator();

        } else if (dbType.equals("hsqldb")) {
            return new HSqlDBSequenceGenerator();

        } else {
            throw new Exception("No compatible sequence generator found for database: "+dbType);
        }

        return seqGenerator;
    }
    
    /**
     * Utility Method to return the sequence name.
     * @param seqId
     * @return
     */
    static public String getSequenceName(int seqId) {
    	String ret = null;
	    if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_SHIPMENT) {
	    	ret = "shipment_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CUST_ORDER) {
	    	ret = "cust_order_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PURCH_ORDER) {
	    	ret = "purch_order_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PURCH_ORDER_EXPENSE) {
	    	ret = "purch_order_expense_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_GENERIC_DETAIL_ID) {
	    	ret = "generic_detail_id_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_VENDOR) {
	    	ret = "vendor_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CUSTOMER) {
	    	ret = "customer_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PAY_VOUCHER) {
	    	ret = "pay_voucher_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CASH_RECEIPT) {
	    	ret = "pay_voucher_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CREDIT_MEMO) {
	    	ret = "credit_memo_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PROSPECT) {
	    	ret = "prospect_seq";
	    } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_QUOTE) {
	    	ret = "quote_seq";
	    }
	    return ret;
    }
    
}
