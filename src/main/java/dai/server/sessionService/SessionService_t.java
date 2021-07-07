package dai.server.sessionService;

import javax.swing.UIManager;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class SessionService_t
{
    csSessionAdapterFactory  sessionAdapterFactory;
    csSessionAdapter sessionAdapter;
    SessionMetaData sessionMeta;

	public SessionService_t() {

		appInit();
	}

	public void appInit()
	{
        sessionAdapterFactory = csSessionAdapterFactory.getInstance();
		sessionAdapter = sessionAdapterFactory.getSessionAdapter();
        sessionMeta = SessionMetaData.getInstance();

		try
		{
			sessionAdapter.connect( sessionMeta.getServerDBURL(),
                                    sessionMeta.getServerLogin(),
                                    sessionMeta.getServerPasswd());
		} catch (Exception e)
		{
			System.out.println("Error Logging On!!!\n" +e.getLocalizedMessage());
			System.exit(0);
		}
	}


	//Main method
	public static void main(String[] args) {
		try
		{
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
			UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
		} catch (Exception e)
		{
		}
		new SessionService_t();
	}
}
