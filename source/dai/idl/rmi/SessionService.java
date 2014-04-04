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

/**
  *Title:        Session Service Interface<br>
  *Copyright:    Copyright (c) 1999<br>
  *Company:      Altaprise Inc.<br>
  *Description:  The Interface for client/server Logging on/off to the backend services.
  */
public interface SessionService extends Remote
{
    static String SERVER_NAME = "SessionService";

    /**
      *Verifies the userId and password of the client and returns the
      *connection object.
      * @param  url Not used at this time.
      * @param  uid User Id.
      * @param  pwd Password.
      * @return csSecurity A connection object which is used to validate all subsequent API calls.
      * @exception  daiRemoteServiceException
      *             Will contain text describing specifics of the error.
      */
    public csSecurity connect(String url, String uid, String pwd)
    throws RemoteException, daiRemoteServiceException;


    /**
      *Closes the connection to the Altaprise servers.
      * @param  security A valid connection object.
      * @return none.
      * @exception  daiRemoteServiceException
      *             Will contain text describing specifics of the error.
      */
	public void close(csSecurity security)
    throws RemoteException, daiRemoteServiceException;
}
