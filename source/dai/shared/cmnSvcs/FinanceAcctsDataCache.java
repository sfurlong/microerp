
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998-2000
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:
//  This class maintains data related to a users session.
//  It has been implemented as a Singleton pattern.
//  NOTE:  This class should not be instantiated directly.
//          New instances should be initialized to null and
//          the getInstance() method should be used to get a
//          handle to this class.

package dai.shared.cmnSvcs;

import java.util.Vector;

import dai.shared.businessObjs.accountObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class FinanceAcctsDataCache
{
    //Get our static instance data.
    public static FinanceAcctsDataCache getInstance()
    {
        return _financeAcctsData;
    }

	public Vector getAcctNames()
	{
		return _acctNames;
	}

	public String getAcctName(int acctNumIndex)
	{
		return (String)_acctNames.elementAt(acctNumIndex);
	}

	public Vector getAcctNums()
	{
		return _acctNums;
	}

	public Vector getBankAcctNums()
	{
		return _bankAcctNums;
	}

	public Vector getBankAcctNames()
	{
		return _bankAcctNames;
	}

	public String getBankAcctName(int acctNumIndex)
	{
		return (String)_bankAcctNames.elementAt(acctNumIndex);
	}

    public String getDefaultCGSAcctId() {
        return _defaultAcctsObj.get_cost_goods_sold_id();
    }

    public String getDefaultInventoryAcctId() {
        return _defaultAcctsObj.get_inventory_id();
    }

    public String getPayableAcctNum()
    {
        if (_acctPayable == null || _acctPayable.length() == 0) {
            _logger.logError(this.getClass().getName() + " Dose not hava a valid Payable Acct."+
                                                " this will cause severe problems with the accounting features."+
                                                " Check the Chart Of Accounts for a Payables Account Type.");
        }
        return _acctPayable;
    }

    public String getReceivableAcctNum()
    {
        if (_acctReceivable == null || _acctReceivable.length() == 0) {
            _logger.logError(this.getClass().getName() + " Dose not hava a valid Receivable Acct."+
                                                " this will cause severe problems with the accounting features."+
                                                " Check the Chart Of Accounts for a Receivable Account Type.");
        }
        return _acctReceivable;
    }

    /////////////////  Private Methods/Data //////////////////////////
    Logger  _logger = null;

    //Private Constructor.  Enforces Singleton.
    private FinanceAcctsDataCache() {

        //Get out client server adapter.
        SessionMetaData     sessionMeta = null;
        csDBAdapterFactory  dbAdapterFactory = null;
		csDBAdapter         dbAdapter = null;
        dbAdapterFactory = dbAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();
        _logger = _logger.getInstance();
        sessionMeta = sessionMeta.getInstance();
        String acctType;

		try
		{
    		accountObj obj;
			Vector objVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                                        new accountObj(),
                                                        " id like '%' order by id");

			for (int i=0; i < objVect.size(); i++)
			{
				obj = (accountObj)objVect.elementAt(i);

				_acctNums.addElement(obj.get_id());
				_acctNames.addElement(obj.get_description());

                acctType = obj.get_account_type();

                if (acctType == null) {
                    _logger.logError(this.getClass().getName()+" Warning: All Finance Accts should have Account_Type set.");
                    continue;
                }

                if (obj.get_account_type().equals(obj.ACCT_TYPE_ACCT_PAY)) {
                    _acctPayable = obj.get_id();
                }
                if (obj.get_account_type().equals(obj.ACCT_TYPE_ACCT_REC)) {
                    _acctReceivable = obj.get_id();
                }
                //Get the list of all bank accts (i.e. checking accts)
                if (obj.get_account_type().equals(obj.ACCT_TYPE_BANK)) {
                    _bankAcctNums.addElement(obj.get_id());
                    _bankAcctNames.addElement(obj.get_description());
                }
			}

            //Get the default Accounts as well
            Vector defAcctsVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                        new default_accountsObj(),
                                        "locality='"+sessionMeta.getLocality()+"'");
            if (defAcctsVect.size() > 0) {
                _defaultAcctsObj = (default_accountsObj)defAcctsVect.elementAt(0);
            } else {
                _logger.logError(this.getClass().getName() + " No Default Accounting Accounts could be found.");
            }

		} catch (Exception e)
		{
            _logger.logError(this.getClass().getName() + " Could not Load Finance Account Cache\n"+e.getLocalizedMessage());
		}
	}

    //Declaration of the Private Static metaData.
    //Since it's static, only one copy will exist of the instance data
    //a'la the Singleton pattern.
    private static FinanceAcctsDataCache _financeAcctsData = new FinanceAcctsDataCache();
	private Vector    _acctNames = new Vector();
	private Vector    _acctNums = new Vector();
    private Vector      _bankAcctNums = new Vector();  //i.e. checking accts.
    private Vector      _bankAcctNames = new Vector();
    private String      _acctPayable;
    private String      _acctReceivable;
    private default_accountsObj _defaultAcctsObj;
}

