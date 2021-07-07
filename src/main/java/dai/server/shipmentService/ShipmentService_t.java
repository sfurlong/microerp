package dai.server.shipmentService;

import javax.swing.UIManager;

import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class ShipmentService_t
{
    csSessionAdapterFactory    sessionAdapterFactory;
    csShipmentAdapterFactory   shipmentAdapterFactory;
    csShipmentAdapter   shipmentAdapter;
    csSessionAdapter    sessionAdapter;

	public ShipmentService_t() {

		appInit();

        try {

        } catch (Exception e) {
            System.out.println(e);
        }
	}

	public void appInit()
	{
		//Instantiate static state data to be used for the
		//entire session.
        sessionAdapterFactory  = sessionAdapterFactory.getInstance();
        sessionAdapter  = sessionAdapterFactory.getSessionAdapter();
		shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();

		try
		{
			sessionAdapter.connect("jdbc:interbase://nt_server/d:/release/v1.2/artifactsdb/artifacts.gdb", "SYSDBA", "masterkey");
			//dbConn.connectToDB("jdbc:interbase://localhost/c:/dai/artifactsdb/artifacts.gdb", "SYSDBA", "masterkey");
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
			UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
			//UIManager.setLookAndFeel(new com.sun.java.swing.plaf.motif.MotifLookAndFeel());
			//UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
		} catch (Exception e)
		{
		}
		new ShipmentService_t();
	}
}
