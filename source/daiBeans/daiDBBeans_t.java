
//Title:      Your Product Name
//Version:
//Copyright:  Copyright (c) 1998
//Author:     Stephen P. Furlong
//Company:    DAI
//Description:Beans
package daiBeans;

import java.awt.AWTPermission;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.UIManager;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class daiDBBeans_t
{
    boolean packFrame = false;
    csSessionAdapterFactory    sessionAdapterFactory;
    csSessionAdapter    sessionAdapter;
    SessionMetaData     sessionMeta;
    AWTPermission awtPermission = new AWTPermission("accessEventQueue");

    public static void main(String[] args)
    {
        try
        {
			UIManager.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
        }
        new daiDBBeans_t();
    }

    //Construct the application
    public daiDBBeans_t()
    {
    appInit();

    dbFrame frame = new dbFrame();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame)
      frame.pack();
    else
      frame.validate();
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
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
}
