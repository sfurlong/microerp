package daiBeans;

import javax.swing.JFrame;
import javax.swing.UIManager;

import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.customerObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class DataChooser_t
{
    csSessionAdapterFactory     sessionAdapterFactory;
    csSessionAdapter            sessionAdapter;
    SessionMetaData             sessionMeta;

	public DataChooser_t() {

		appInit();

        JFrame frame = new JFrame();

		customerObj tempObj = new customerObj();

        DBAttributes attrib = new DBAttributes(tempObj.ID, "Cust Id", 200);
		DataChooser chooser = new DataChooser(frame, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib},
                                              null, null);
		chooser.setVisible(true);
        customerObj chosenObj = (customerObj)chooser.getChosenObj();
        if (chosenObj != null) {
        }
    	chooser.dispose();
	}

	public void appInit()
	{
		//Instantiate static state data to be used for the
		//entire session.
        sessionAdapterFactory  = sessionAdapterFactory.getInstance();
        sessionAdapter  = sessionAdapterFactory.getSessionAdapter();
        sessionMeta     = sessionMeta.getInstance();
		try
		{
			csSecurity security = sessionAdapter.connect(sessionMeta.getServerDBURL(),
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
		new DataChooser_t();
	}
}
