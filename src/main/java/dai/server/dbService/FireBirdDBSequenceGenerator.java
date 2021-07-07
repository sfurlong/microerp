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
public class FireBirdDBSequenceGenerator implements DBSequenceGenerator {

    public int getNewSequenceNum(dbconnect dbconn, int seqId) throws daiException
    {
        String sqlStmt = "";

        if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_SHIPMENT) {
            sqlStmt = "select gen_id(shipment_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CUST_ORDER) {
            sqlStmt = "select gen_id(cust_order_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PURCH_ORDER) {
            sqlStmt = "select gen_id(purch_order_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PURCH_ORDER_EXPENSE) {
            sqlStmt = "select gen_id(purch_order_expense_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_GENERIC_DETAIL_ID) {
            sqlStmt = "select gen_id(generic_detail_id_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_VENDOR) {
            sqlStmt = "select gen_id(vendor_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CUSTOMER) {
            sqlStmt = "select gen_id(customer_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PAY_VOUCHER) {
            sqlStmt = "select gen_id(pay_voucher_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CASH_RECEIPT) {
            sqlStmt = "select gen_id(pay_voucher_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_CREDIT_MEMO) {
            sqlStmt = "select gen_id(credit_memo_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_PROSPECT) {
            sqlStmt = "select gen_id(prospect_seq,1) from seq_gen_helper_for_interbase";
        } else if (seqId == dai.shared.csAdapters.csDBAdapter.SEQUENCE_QUOTE) {
            sqlStmt = "select gen_id(quote_seq,1) from seq_gen_helper_for_interbase";
        }

        int ret = -1;

		//Execute the SQL
		ResultSet rs = dbconn.executeSQL(sqlStmt);
		try
		{
			while (rs.next())
			{
   				ret = rs.getInt(rs.findColumn("GEN_ID"));
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
    	String sqlStmt = " set generator " + DBSequenceFactory.getSequenceName(seqId) + " to " + seqValue;

    	//Execute the SQL
		dbconn.executeSQL(sqlStmt);
    }

}