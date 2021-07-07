/*
 * Created on Mar 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dai.server.dbService;

import java.sql.ResultSet;

import dai.shared.cmnSvcs.daiException;

/**
 * @author sfurlong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HSqlDBSequenceGenerator implements DBSequenceGenerator {

    public int getNewSequenceNum(dbconnect dbconn, int seqId) throws daiException {
        String sqlStmt = "";

        if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_SHIPMENT) {
            sqlStmt = "select next value for shipment_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CUST_ORDER) {
            sqlStmt = "select next value for cust_order_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PURCH_ORDER) {
            sqlStmt = "select next value for purch_order_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PURCH_ORDER_EXPENSE) {
            sqlStmt = "select next value for purch_order_expense_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_GENERIC_DETAIL_ID) {
            sqlStmt = "select next value for generic_detail_id_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_VENDOR) {
            sqlStmt = "select next value for vendor_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CUSTOMER) {
            sqlStmt = "select next value for customer_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PAY_VOUCHER) {
            sqlStmt = "select next value for pay_voucher_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CASH_RECEIPT) {
            sqlStmt = "select next value for pay_voucher_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CREDIT_MEMO) {
            sqlStmt = "select next value for credit_memo_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PROSPECT) {
            sqlStmt = "select next value for prospect_seq from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_QUOTE) {
            sqlStmt = "select next value for quote_seq from seq_gen_helper_for_interbase";
        }

        int ret = -1;

		//Execute the SQL
		ResultSet rs = dbconn.executeSQL(sqlStmt);
		try
		{
			while (rs.next())
			{
   				ret = rs.getInt(1);
   				System.out.println("seq: "+ ret);
			}
		} catch (Exception e)
		{
			String msg = "Error getting new SEQ.\n" + e.getLocalizedMessage();
			throw new daiException(msg, this);
		}

		return ret;
    }
    
    public void setSequenceValue(dbconnect dbconn, int seqId, int seqValue) throws daiException
    {
    	String sqlStmt = " alter sequence " + DBSequenceFactory.getSequenceName(seqId) + " restart with " + seqValue;

    	//Execute the SQL
		dbconn.executeSQL(sqlStmt);
    }
}
