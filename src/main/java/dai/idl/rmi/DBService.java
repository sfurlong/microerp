package dai.idl.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

/**
  *Title:        Database Service Interface<br>
  *Copyright:    Copyright (c) 1999<br>
  *Company:      Digital Artifacts Inc.<br>
  *Description:  The Interface for client/server access to DB Services
  *@author:      sfurlong
  */
public interface DBService extends Remote
{
    static String SERVER_NAME = "DBService";

	//Execute Dynamic SQL
	public DBRecSet execDynamicSQL(csSecurity security, String sqlStmt)
    throws RemoteException, daiRemoteServiceException;

	public Vector getDynamicSQLResults(csSecurity security, String sqlStmt)
    throws RemoteException, daiRemoteServiceException;

	//Insert
	public int insert(csSecurity security, BusinessObject obj)
    throws RemoteException, daiRemoteServiceException;

	//Update
	public int update(csSecurity security, BusinessObject obj, String exp)
    throws RemoteException, daiRemoteServiceException;

	//Delete
	public int delete(csSecurity security, BusinessObject obj, String exp)
    throws RemoteException, daiRemoteServiceException;

	//Get MAX Detail ID
	public long getMaxDetailId(csSecurity security, BusinessObject obj, String exp)
    throws RemoteException, daiRemoteServiceException;

	//Get All IDs
	public Vector getAllIds(csSecurity security, String tableName, String exp)
    throws RemoteException, daiRemoteServiceException;

	//Query By Expression.
	//NOTE:The obj is just needed to get the field names.  Kind of kludge but,
	//hey...
	public Vector queryByExpression(csSecurity security, BusinessObject obj, String exp)
    throws RemoteException, daiRemoteServiceException;

	//Get TableNames.
	public String[] getTableNames(csSecurity security)
    throws RemoteException, daiRemoteServiceException;

	//Get ColumnNames.
	public DBAttributes[] getColumnNames(csSecurity security, String tableName)
    throws RemoteException, daiRemoteServiceException;

    //Get New Sequence Number
    public int getNewSequenceNum(csSecurity security, int seqId)
    throws RemoteException, daiRemoteServiceException;

    //Set New Sequence Number
    public void setSequenceValue(csSecurity security, int seqId, int seqValue)
    throws RemoteException, daiRemoteServiceException;

    //Begin trans
    public void beginTrans(csSecurity security)
    throws RemoteException, daiRemoteServiceException;

    //End trans
    public void endTrans(csSecurity security)
    throws RemoteException, daiRemoteServiceException;

    //Rollback
    public void rollback(csSecurity security)
    throws RemoteException, daiRemoteServiceException;
}
