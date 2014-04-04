
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package dai.shared.csAdapters;

import java.rmi.*;
import java.rmi.server.*;

import dai.idl.rmi.*;
import dai.shared.cmnSvcs.*;

public class csLoggerRMIAdapter extends csLoggerAdapter
{
    //Connection to the Logger Service
    static LoggerService loggerService;

    private Logger _logger;

    // constructor //
    csLoggerRMIAdapter(String host)
    {
        System.setSecurityManager (new daiSecurityManager());
        String lookup = "rmi://"+host+"/"+LoggerService.SERVER_NAME;

        _logger = _logger.getInstance();

        try {
            loggerService = (LoggerService)Naming.lookup(lookup);
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
    }

	// Constructor for Fat Client//
	public csLoggerRMIAdapter()
	{
        _logger = _logger.getInstance();

        try {
            Class c = Class.forName("dai.server.loggerService.LoggerRMIServiceImpl");
            loggerService = (LoggerService)c.newInstance();
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

	public void logError(csSecurity security, String msg)
    {
        try {
            loggerService.logError(security, msg);
        } catch (Exception e) {
            String err = this.getClass().getName()+"::logError failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
        }
    }

	public void logSQL(csSecurity security, String msg)
    {
        try {
            loggerService.logSQL(security, msg);
        } catch (Exception e) {
            String err = this.getClass().getName()+"::logSQL failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
        }
    }
}
