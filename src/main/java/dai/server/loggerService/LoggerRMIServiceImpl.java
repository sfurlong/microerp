
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package dai.server.loggerService;

import java.rmi.*;
import java.rmi.server.*;
import dai.idl.rmi.*;
import dai.shared.cmnSvcs.*;
import java.io.*;

public class LoggerRMIServiceImpl extends UnicastRemoteObject implements LoggerService
{
    private FileWriter fwp;
    private String _errFileName = SessionMetaData.getInstance().getDaiHome()+"/"+daiFormatUtil.DateTimeStamp()+"err.log";
    private String _sqlFileName = SessionMetaData.getInstance().getDaiHome()+"/"+daiFormatUtil.DateTimeStamp()+"sql.log";
    private String _errLoggingProp = PropertyFileData.getInstance().getProperty(daiProperties.ERR_LOGGING);
    private String _sqlLoggingProp = PropertyFileData.getInstance().getProperty(daiProperties.SQL_LOGGING);

    // constructor //
    public LoggerRMIServiceImpl()
    throws RemoteException, daiRemoteServiceException
    {
        super();
        try {
            System.out.println(_sqlLoggingProp + "Constructor");
            if (_errLoggingProp != null && _errLoggingProp.equals("YES")) {
                fwp = new FileWriter(_errFileName);
                String msg = daiFormatUtil.getCurrentDate() + ": BEGIN NEW ERR LOGGER SESSION ";
                fwp.write(msg+"\r");
                fwp.close();
            }
            if (_sqlLoggingProp != null && _sqlLoggingProp.equals("YES")) {
                fwp = new FileWriter(_sqlFileName);
                String msg = daiFormatUtil.getCurrentDate() + ": BEGIN NEW SQL LOGGER SESSION ";
                fwp.write(msg+"\r");
                fwp.close();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void logError(csSecurity security, String msg)
    throws RemoteException, daiRemoteServiceException
    {
        try {
            if (_errLoggingProp != null && _errLoggingProp.equals("YES")) {
                fwp = new FileWriter(_errFileName, true);
                fwp.write("begin msg+++++++++++++++++++++++++++++++++++++\r");
                fwp.write(daiFormatUtil.DateTimeStamp()+"::"+security.getUid()+": "+msg+"\r");
                fwp.write("end msg---------------------------------------\r");
                fwp.close();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
	}

    public void logSQL(csSecurity security, String msg)
    throws RemoteException, daiRemoteServiceException
    {
        try {
            if (_sqlLoggingProp != null && _sqlLoggingProp.equals("YES")) {
                fwp = new FileWriter(_sqlFileName, true);
                fwp.write("begin msg+++++++++++++++++++++++++++++++++++++\r");
                fwp.write(daiFormatUtil.DateTimeStamp()+"::"+security.getUid()+": "+ msg+"\r");
                fwp.write("end msg---------------------------------------\r");
                fwp.close();
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }
	}

    public static void main (String[] args)
    {
        System.setSecurityManager (new RMISecurityManager());
        String serviceName = ""; //For the out of scope catch block.
        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        try
        {
            //Create a new instance of the service.
            LoggerRMIServiceImpl service = new LoggerRMIServiceImpl();
            serviceName = LoggerRMIServiceImpl.SERVER_NAME;

            //Bind service to local registry on default port.
            Naming.rebind("//"+sessionMeta.getHostname()+"/"+serviceName, service);

            //Some status.
            System.out.println(serviceName + " Started.");

        } catch (Exception e)
        {
            System.err.println(serviceName + "\n" + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

}



