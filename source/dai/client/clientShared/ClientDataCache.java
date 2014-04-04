//  This class is used to log runtime data.
//  It has been implemented as a Singleton pattern.
//  NOTE:  This class should not be instantiated directly.
//          New instances should be initialized to null and
//          the getInstance() method should be used to get a
//          handle to this class.

package dai.client.clientShared;

import dai.shared.cmnSvcs.FinanceAcctsDataCache;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.StateCountryDataCache;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiComboBox;

public class ClientDataCache
{

    //Get our static instance data.
    public static ClientDataCache getInstance()
    {
        return _dataCache;
    }

	public daiComboBox getStateComboBox()
	{
		return new daiComboBox(_stateComboBox);
	}

	public daiComboBox getCountryComboBox()
	{
		return new daiComboBox(_countryComboBox);
	}

	public daiComboBox getAcctNumComboBox()
	{
		return _acctsComboBox;
	}

	public daiComboBox getBankAcctNumComboBox()
	{
		return _bankAcctsComboBox;
	}
/////////////////  Private Methods/Data //////////////////////////

    //Private Constructor.  Enforces Singleton.
    private ClientDataCache()
    {
        _stateCountryData = _stateCountryData.getInstance();
        _financeAcctsData = _financeAcctsData.getInstance();
        _stateComboBox = new daiComboBox(_stateCountryData.getStateNames());
        _countryComboBox = new daiComboBox(_stateCountryData.getCountryNames());

        csDBAdapter dbAdapter = null;
        csDBAdapterFactory dbAdapterFactory = null;
        SessionMetaData sessionMeta = null;
        sessionMeta = sessionMeta.getInstance();
        dbAdapterFactory = dbAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();

        _acctsComboBox = new daiComboBox(_financeAcctsData.getAcctNums());
        _bankAcctsComboBox = new daiComboBox(_financeAcctsData.getBankAcctNums());
    }

    //Declaration of the Private Static metaData.
    //Since it's static, only one copy will exist of the instance data
    //a'la the Singleton pattern.
    private static ClientDataCache _dataCache = new ClientDataCache();
	private daiComboBox _stateComboBox;
	private daiComboBox _countryComboBox;
    private StateCountryDataCache _stateCountryData;
    private FinanceAcctsDataCache _financeAcctsData;
    private daiComboBox _acctsComboBox;
    private daiComboBox _bankAcctsComboBox;
}

