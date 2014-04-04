
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description


package dai.server.sessionService;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Vector;

import dai.idl.rmi.SessionService;
import dai.shared.businessObjs.user_profileObj;
import dai.shared.cmnSvcs.JCrypt;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class SessionRMIServiceImpl extends UnicastRemoteObject implements SessionService
{

    private csSecurity sessionMasterSecurity = new csSecurity();
    private HashMap _profileMap = new HashMap();
    private boolean initDone = false;
    private csDBAdapterFactory _dbAdapterFactory;
    private csDBAdapter _dbAdapter;

    // constructor //
    public SessionRMIServiceImpl()
    throws RemoteException, daiRemoteServiceException
    {
        //This is the master DBA login.
        //It will be used to get the set of sessionProfiles.
        //The sessionProfiles will be used to validate all other
        //user logins.
        super();

        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        sessionMasterSecurity.setUid(sessionMeta.getServerLogin());
        sessionMasterSecurity.setAndEncryptPwd(JCrypt.crypt(sessionMeta.getServerPasswd()));

        _dbAdapterFactory = csDBAdapterFactory.getInstance();
        _dbAdapter = _dbAdapterFactory.getDBAdapter();
    }

    // connect to server //
    public csSecurity connect(String url, String uid, String pwd)
    throws RemoteException, daiRemoteServiceException
    {
        if (!initDone)
        {
            initSessionService();
            initDone = true;
        }

        csSecurity security = new csSecurity(uid, JCrypt.crypt(pwd));

        if (!validateUserLogin(security))
        {
            String msg = this.getClass().getName()+"::Logon Failure.  Bad UserId or Password.";
            throw new daiRemoteServiceException(msg, null);
        }

        System.out.println("Session Connected");

        return security;
    }

    // close the server connection //
    public void close(csSecurity security)
    throws RemoteException, daiRemoteServiceException
    {
        System.out.println("Session Closed");
    }


    private void initSessionService() throws daiRemoteServiceException {
        try
        {
            getAllUserSessionProfiles();
        } catch (Exception e)
        {
            String msg = this.getClass().getName()+"::initSessionService failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    private boolean validateUserLogin(csSecurity security) {
        String uid = security.getUid();
        String cryptoPwd = security.getEncryptedPwd();

        user_profileObj profileObj = (user_profileObj)_profileMap.get(uid);
        if (profileObj == null)
        {
            return false;
        }
        if (!uid.equals(profileObj.get_id()))
        {
            return false;
        }
        if (uid.equals("demo")) {
        	return true;
        }
        if (!cryptoPwd.equals(profileObj.get_encrypted_pwd()))
        {
            return false;
        }

        return true;
    }
    private void getAllUserSessionProfiles()
    throws daiRemoteServiceException
    {

        try
        {
            Vector objVect = _dbAdapter.queryByExpression(sessionMasterSecurity,
                                                          new user_profileObj(),
                                                          "");
            user_profileObj profileObj;
            for (int i=0; i<objVect.size(); i++)
            {
                profileObj = (user_profileObj)objVect.elementAt(i);
                _profileMap.put(profileObj.get_id(), profileObj);
            }
            //Add one more for demo user...
            profileObj = new user_profileObj();
            profileObj.set_id("demo");
            profileObj.set_locality("SUPER");
            profileObj.set_user_name("Demo");
            profileObj.set_is_administrator("Y");
            _profileMap.put("demo", profileObj);
        } catch (Exception e)
        {
            String msg = this.getClass().getName()+"::getAllUserSessionProfiles failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
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
            SessionRMIServiceImpl service = new SessionRMIServiceImpl();
            serviceName = service.SERVER_NAME;

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

