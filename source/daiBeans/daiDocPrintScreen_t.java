package daiBeans;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.UIManager;

import dai.client.ui.docGen.DefaultDocPrintPanel;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;


public class daiDocPrintScreen_t
{
    csSessionAdapterFactory     sessionAdapterFactory;
    csSessionAdapter            sessionAdapter;
    SessionMetaData             sessionMeta;

    String rptFileName = "purchaseOrder.rpt";
    DefaultDocPrintPanel panel = null;

	public daiDocPrintScreen_t() {

		appInit();
	    panel = new DefaultDocPrintPanel(rptFileName);
        Vector vecRpts = new Vector();
        vecRpts.addElement(rptFileName);
        JFrame frame = new JFrame();

        daiPrintScreen dialog = new daiPrintScreen(frame,"Print Purchase Order", new purch_orderObj(),
                                                    vecRpts,
                                                    panel,"08001");
        frame.pack();
        frame.show();

	}

	private void appInit()
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
		new daiDocPrintScreen_t();
	}
}
