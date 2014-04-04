package dai.server.dbService;

import java.util.Vector;

import javax.swing.UIManager;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class DBService_t
{
    csSessionAdapterFactory sessionAdapterFactory;
    csDBAdapterFactory      dbAdapterFactory;
    csSessionAdapter        sessionAdapter;
    csDBAdapter             dbAdapter;
    SessionMetaData         sessionMeta;

	public DBService_t() {

		appInit();
	}

	public void appInit()
	{
        sessionMeta             = sessionMeta.getInstance();
        dbAdapterFactory        = dbAdapterFactory.getInstance();
        sessionAdapterFactory   = sessionAdapterFactory.getInstance();
        sessionAdapter          = sessionAdapterFactory.getSessionAdapter();
        dbAdapter               = dbAdapterFactory.getDBAdapter();

		try
		{
			csSecurity security = sessionAdapter.connect( sessionMeta.getServerDBURL(),
                                    "STEVE",
                                    "fur");
            sessionMeta.setClientServerSecurity(security);

            Vector vect = dbAdapter.getDynamicSQLResults(security, "select count(*) from user_security");
            
            for (int i=0; i<vect.size(); i++)
            {
                Vector colVect = (Vector) vect.elementAt(i);
                for (int j=0; j<colVect.size(); j++)
                {
                    System.out.print(i+"/"+j+": " +colVect.elementAt(j));
                }
                System.out.println();
            }
            
            dbAdapter.getNewSequenceNum(security, dai.shared.csAdapters.csDBAdapter.SEQUENCE_SHIPMENT);
		} catch (Exception e)
		{
			System.out.println("Error Logging On!!!\n" +e.getLocalizedMessage());
		}
			System.exit(0);
	}


	//Main method
	public static void main(String[] args) {
		try
		{
			UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
			//UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
		} catch (Exception e)
		{
		}
		new DBService_t();
	}
}
