package dai.shared.cmnSvcs;

import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class cmnSvcs_t {

    csSessionAdapterFactory sessionAdapterFactory;
    csSessionAdapter sessionAdapter;
    SessionMetaData  sessionMeta;
    Logger           logger;

    public cmnSvcs_t() {

		appInit();
	}

	public void appInit()
	{
		//Instantiate static state data to be used for the
		//entire session.
        sessionAdapterFactory = sessionAdapterFactory.getInstance();
        sessionAdapter = sessionAdapterFactory.getSessionAdapter();
        sessionMeta     = sessionMeta.getInstance();
        logger          = logger.getInstance();

        FinanceAcctsDataCache acctsDataCache = null;
        acctsDataCache = acctsDataCache.getInstance();

		try
		{
			csSecurity security = sessionAdapter.connect(sessionMeta.getServerDBURL(),
                                    "STEVE",
                                    "fur");
            sessionMeta.setClientServerSecurity(security);
		} catch (Exception e)
		{
			System.out.println("Error Logging On!!!\n" +e.getLocalizedMessage());
            logger.logError("Error Logging On!!!\n" +e.getLocalizedMessage());
			System.exit(0);
		}
	}
    public static void main(String[] args) {
        cmnSvcs_t vCmnSvcs_t = new cmnSvcs_t();
    }
}
