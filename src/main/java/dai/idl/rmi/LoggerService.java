//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package dai.idl.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

public interface LoggerService extends Remote
{
    static String SERVER_NAME = "LoggerService";

	public void logError(csSecurity security, String msg)
    throws RemoteException, daiRemoteServiceException;

	public void logSQL(csSecurity security, String msg)
    throws RemoteException, daiRemoteServiceException;
}
