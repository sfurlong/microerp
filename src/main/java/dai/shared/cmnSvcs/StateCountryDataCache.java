
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

import dai.shared.businessObjs.countryObj;
import dai.shared.businessObjs.state_provinceObj;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class StateCountryDataCache
{
    //Get our static instance data.
    public static StateCountryDataCache getInstance()
    {
        return _stateCountryData;
    }

	public Vector getStateNames()
	{
		return _stateNames;
	}

	public Vector getCountryNames()
	{
		return _countryNames;
	}

    /////////////////  Private Methods/Data //////////////////////////

    //Private Constructor.  Enforces Singleton.
    private StateCountryDataCache() {

        //Get the client server adapters.
        SessionMetaData     sessionMeta = SessionMetaData.getInstance();
        csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
		csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();
        Logger              logger = Logger.getInstance();

		try
		{
    		state_provinceObj obj;
			Vector objVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                                        new state_provinceObj(),
                                                        " id like '%' order by id");

			for (int i=0; i < objVect.size(); i++)
			{
				obj = (state_provinceObj)objVect.elementAt(i);

				_stateNames.addElement(obj.get_id());
			}
		} catch (Exception e)
		{
            logger.logError(null, this.getClass().getName() + "Could not Load State Codes\n"+e.getLocalizedMessage());
		}


		try
		{
    		countryObj obj;
			Vector objVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                                        new countryObj(),
                                                        " id like '%' order by id");

			for (int i=0; i < objVect.size(); i++)
			{
				obj = (countryObj)objVect.elementAt(i);

				_countryNames.addElement(obj.get_id());
			}
		} catch (Exception e)
		{
            logger.logError(this.getClass().getName() + "Could not Load Country Codes\n"+e.getLocalizedMessage());
		}
	}

    //Declaration of the Private Static metaData.
    //Since it's static, only one copy will exist of the instance data
    //a'la the Singleton pattern.
    private static StateCountryDataCache _stateCountryData = new StateCountryDataCache();
	private Vector _stateNames = new Vector();
	private Vector _countryNames = new Vector();
}

