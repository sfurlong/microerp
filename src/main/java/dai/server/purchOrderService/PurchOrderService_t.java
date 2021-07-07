package dai.server.purchOrderService;

import javax.swing.UIManager;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;

public class PurchOrderService_t
{
    csSessionAdapterFactory    sessionAdapterFactory;
    csShipmentAdapterFactory   shipmentAdapterFactory;
    csShipmentAdapter   shipmentAdapter;
    csSessionAdapter    sessionAdapter;

	public PurchOrderService_t() {

		appInit();


        try {
            PurchOrderServiceImpl poImpl = new PurchOrderServiceImpl();
            String[] ret = poImpl.calcMultiPayments(120973.13, 2);
            for (int i=0; i<ret.length; i++) {
                System.out.println(ret[i]);
            }
            

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
        SessionMetaData sessionMeta = SessionMetaData.getInstance();
		try
		{
			csSecurity security = sessionAdapter.connect( sessionMeta.getServerDBURL(),
                                    "STEVE",
                                    "fur");
            sessionMeta.setClientServerSecurity(security);
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
		new PurchOrderService_t();
	}
}
