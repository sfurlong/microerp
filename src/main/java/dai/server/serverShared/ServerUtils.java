package dai.server.serverShared;

import dai.shared.cmnSvcs.*;
import dai.shared.businessObjs.*;
import dai.shared.csAdapters.*;

public class ServerUtils {

    public ServerUtils() {
    }

    static public void postNewLedgerEntry(String acctNum,
                            String transRef,
                            String transDate,
                            String transType,
                            String debitAmt,
                            String creditAmt,
                            String note)
	throws daiException
    {

		csDBAdapter         dbAdapter          = csDBAdapterFactory.getInstance().getDBAdapter();
        csLoggerAdapter     logger             = csLoggerAdapterFactory.getInstance().getLoggerAdapter();
        SessionMetaData     sessionMeta        = SessionMetaData.getInstance();

        if (debitAmt != null && Double.parseDouble(debitAmt) < 0) {
            String tmpAmt = Double.toString(Double.parseDouble(debitAmt) * -1);
            debitAmt = creditAmt;
            creditAmt = tmpAmt;
        }

        if (creditAmt != null && Double.parseDouble(creditAmt) < 0) {
            String tmpAmt = Double.toString(Double.parseDouble(creditAmt) * -1);
            creditAmt = debitAmt;
            debitAmt = tmpAmt;
        }

        //Get the new datail Id
        try {
            long newDetailId = dbAdapter.getNewSequenceNum(sessionMeta.getClientServerSecurity(),
                                                    dbAdapter.SEQUENCE_GENERIC_DETAIL_ID);

            //Add Expense amt to the Acct Detail table
	    	account_detailObj acctDetailObj = new account_detailObj();
		    acctDetailObj.set_id(acctNum);
    		acctDetailObj.set_locality(accountObj.getObjLocality());
	    	acctDetailObj.set_detail_id(new Long(newDetailId).toString());
		    acctDetailObj.set_trans_type(transType);
    		acctDetailObj.set_trans_date(transDate);
	    	acctDetailObj.set_trans_ref(transRef);
		    acctDetailObj.set_note1(note);
    		acctDetailObj.set_debit(debitAmt);
	    	acctDetailObj.set_credit(creditAmt);

		    dbAdapter.insert(sessionMeta.getClientServerSecurity(),
							 acctDetailObj);
        } catch (Exception e) {
			e.printStackTrace();
			String msg = "ServerUtils::postNewLedgerEntry failure"+
						 "\n"+e.getLocalizedMessage();
			logger.logError(sessionMeta.getClientServerSecurity(), msg);
			throw new daiException(msg, null);
        }
    }

    static public String addQuotes(String s) {
        if (s != null) {
            s = "'" + s + "'";
        }
        return s;
    }
}
