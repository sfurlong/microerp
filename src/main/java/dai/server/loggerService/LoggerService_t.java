package dai.server.loggerService;

import java.rmi.Naming;

import javax.swing.UIManager;
//import java.rmi.server.*;
//import dai.shared.cmnSvcs.*;

public class LoggerService_t
{
    //Connection to the Logger Service
    static dai.idl.rmi.LoggerService loggerService;

	public LoggerService_t() {

        System.out.println("Befor App Init");
		appInit();
	}

	public void appInit()
	{
        System.setSecurityManager (new daiSecurityManager());
        String lookup = "//204.254.179.169/LoggerService";

        try {
            loggerService = (dai.idl.rmi.LoggerService)Naming.lookup(lookup);
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            System.out.println(msg);
            return;
        }
		try
		{
            System.out.println("About to Log Error");
            loggerService.logError(null, "hello");
            System.out.println("Logged Error");
		} catch (Exception e)
		{
			System.out.println("Error Logging!!!\n" +e.getLocalizedMessage());
            e.printStackTrace();
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
		new LoggerService_t();
	}

    public class daiSecurityManager extends SecurityManager {
        public boolean checkTopLevelWindow(Object window) {
            //super.checkTopLevelWindow(window);
            return true;
        }
    }


}
