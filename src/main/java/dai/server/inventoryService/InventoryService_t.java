package dai.server.inventoryService;

import javax.swing.UIManager;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class InventoryService_t
{
    csSessionAdapterFactory  sessionAdapterFactory;
    csSessionAdapter sessionAdapter;
    SessionMetaData sessionMeta;

	public InventoryService_t() {

		appInit();
	}

	public void appInit()
	{
        sessionAdapterFactory = sessionAdapterFactory.getInstance();
		sessionAdapter = sessionAdapterFactory.getSessionAdapter();
        sessionMeta = sessionMeta.getInstance();

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
			//UIManager.setLookAndFeel(new javax.swing.plaf.basic..getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
			UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
		} catch (Exception e)
		{
		}
		new InventoryService_t();
	}
}
