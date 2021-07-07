
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package dai.shared.csAdapters;

import java.rmi.Naming;
import java.rmi.RemoteException;

import dai.idl.rmi.SessionService;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

public class csSessionRMIAdapter extends csSessionAdapter
{
    //Connection to the Session Service
    static SessionService sessionService;

    private Logger _logger;

    // constructor //
    csSessionRMIAdapter(String host)
    {
        System.setSecurityManager (new daiSecurityManager());
        String lookup = "rmi://"+host+"/"+SessionService.SERVER_NAME;

        _logger = _logger.getInstance();

        try {
            sessionService = (SessionService)Naming.lookup(lookup);
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
    }

	// Constructor for Fat Client//
	public csSessionRMIAdapter()
	{
        _logger = _logger.getInstance();

        try {
            Class c = Class.forName("dai.server.sessionService.SessionRMIServiceImpl");
            sessionService = (SessionService)c.newInstance();
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

    // connect to server //
	public csSecurity connect(String url, String uid, String pwd)
	throws daiRemoteServiceException
    {
        csSecurity ret = null;
        try {
            ret = sessionService.connect(url, uid, pwd);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::connect failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
    }

    // close the server connection //
	public void close(csSecurity security)
	throws daiRemoteServiceException
    {
        try {
            sessionService.close(security);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::close failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }
}
