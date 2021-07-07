
//Title:        Client Server DB Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:


package dai.shared.csAdapters;

import java.util.*;
import dai.shared.cmnSvcs.*;
import dai.shared.businessObjs.*;
import java.rmi.*;
import dai.idl.rmi.*;

public class csDBRMIAdapter extends csDBAdapter
{
    //Connection to the Session Service
    static DBService dbService;

    private Logger _logger;

    //For use when using RMI
	public csDBRMIAdapter(String host)
	{
        System.setSecurityManager (new daiSecurityManager());
        String lookup = "rmi://"+host+"/"+DBService.SERVER_NAME;

        _logger = Logger.getInstance();

        try {
            //Find the service using the Naming service.
            dbService = (DBService)Naming.lookup(lookup);
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

    //For use when using in Stub Mode (i.e.  Fat Client).
	public csDBRMIAdapter()
	{
        _logger = Logger.getInstance();

        try {
            //Instantiate the Server Directly..StubMode Only (Fat Client!!)
            Class c = Class.forName("dai.server.dbService.DBRMIServiceImpl");
            dbService = (DBService)c.newInstance();
        } catch (Exception e) {
            String msg = this.getClass().getName()+"::Constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            //Can't do anything but log.  Don't want to log to message window
            //bacause server side uses this as well.
            _logger.logError(msg);
        }
	}

    //Connect To the Database
    public void connectToDB(String uid, String pwd, String connectString)
    throws daiRemoteServiceException
    {
        //No need to do anything here.  The server side process does
        //it's own login to the DB using connection pools.
    }

	//Execute Dynamic SQL
	public DBRecSet execDynamicSQL(csSecurity security, String sqlStmt)
	throws daiRemoteServiceException
	{
        DBRecSet ret = new DBRecSet();
        try {
            ret = dbService.execDynamicSQL(security, sqlStmt);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::execDynamicSQL failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
	}

	//Get Dynamic SQL Results
	public Vector getDynamicSQLResults(csSecurity security, String sqlStmt)
	throws daiRemoteServiceException
	{
        Vector ret = new Vector();
        try {
            ret = dbService.getDynamicSQLResults(security, sqlStmt);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getDynamicSQLResults failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
	}

	//Insert
	public int insert(csSecurity security, BusinessObject obj)
	throws daiRemoteServiceException
	{
        try {
    		return dbService.insert(security, obj);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::insert failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	//Update
	public int update(csSecurity security, BusinessObject obj, String exp)
	throws daiRemoteServiceException
	{
        try {
    		return dbService.update(security, obj, exp);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::update failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	//Delete
	public int delete(csSecurity security, BusinessObject obj, String exp)
	throws daiRemoteServiceException
	{
        try {
    		return dbService.delete(security, obj, exp);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::delete failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	//Get MAX Detail ID
	public long getMaxDetailId(csSecurity security, BusinessObject obj, String exp)
	throws daiRemoteServiceException
	{
        try {
    		return dbService.getMaxDetailId(security, obj, exp);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getMaxDetailId failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	//Get All IDs
	public Vector getAllIds(csSecurity security, String tableName, String exp)
	throws daiRemoteServiceException
	{
        try {
    		return dbService.getAllIds(security, tableName, exp);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getAllIds failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	//Query By Expression.
	//NOTE:The obj is just needed to get the field names.  Kind of kludee but,
	//hey...
	public Vector queryByExpression(csSecurity security, BusinessObject obj, String exp)
	throws daiRemoteServiceException
	{
        try {
            Vector ret = dbService.queryByExpression(security, obj, exp);
            return ret;
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::queryByExpression failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	//Get TableNames.
	public String[] getTableNames(csSecurity security)
	throws daiRemoteServiceException
	{
        try {
    		return dbService.getTableNames(security);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getTableNames failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

	//Get ColumnNames.
	public DBAttributes[] getColumnNames(csSecurity security, String tableName)
	throws daiRemoteServiceException
	{
        try {
    		return dbService.getColumnNames(security, tableName);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getColumnNames failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    //Get New Sequence Number
    public int getNewSequenceNum(csSecurity security, int seqId)
    throws daiRemoteServiceException
	{
        try {
    		return dbService.getNewSequenceNum(security, seqId);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::getNewSequenceNum failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    //Set Sequence Number Value
    public void setSequenceValue(csSecurity security, int seqId, int seqVal)
    throws daiRemoteServiceException
	{
        try {
    		dbService.setSequenceValue(security, seqId, seqVal);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::setSequenceValue failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    //Begin trans
    public void beginTrans(csSecurity security)
    throws daiRemoteServiceException
	{
        try {
    		dbService.beginTrans(security);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::beginTrans failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    //End trans
    public void endTrans(csSecurity security)
    throws daiRemoteServiceException
	{
        try {
    		dbService.endTrans(security);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::endTrans failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}

    //Rollback
    public void rollback(csSecurity security)
    throws daiRemoteServiceException
	{
        try {
    		dbService.rollback(security);
        } catch (RemoteException e) {
            String msg = this.getClass().getName()+"::rollback failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
	}
}

