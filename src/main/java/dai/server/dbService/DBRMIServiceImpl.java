
//Title:        Client Server DB Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:


package dai.server.dbService;


import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import dai.idl.rmi.DBService;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiException;
import dai.shared.cmnSvcs.daiRemoteServiceException;

public class DBRMIServiceImpl extends UnicastRemoteObject implements DBService
{
    SQLResolver sqlResolver = new SQLResolver();
    dbconnect   _dbConn;
    SessionMetaData _sessionMeta;

    public DBRMIServiceImpl()
    throws RemoteException, daiRemoteServiceException
    {
        super();
        _dbConn = new dbconnect();
        _sessionMeta = _sessionMeta.getInstance();
        try
        {
            _dbConn.connectToDB(_sessionMeta.getServerDBURL(), _sessionMeta.getJDBCDriverName(), _sessionMeta.getServerLogin(), _sessionMeta.getServerPasswd());
        } catch (daiException ex)
        {
            String msg = "DB Service Could not logon to DB." +
                         "\n"+ex.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        } catch (Exception e)
        {
            String msg = this.getClass().getName()+"::execDynamicSQL failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Execute Dynamic SQL
    public DBRecSet execDynamicSQL(csSecurity security, String sqlStmt)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        DBRecSet ret = new DBRecSet();
        try
        {
            sqlResolver.beginTrans(_dbConn);
            ret = sqlResolver.executeDynamicSQL(_dbConn, sqlStmt);
            sqlResolver.endTrans(_dbConn);
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::execDynamicSQL failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
    }

    //Execute Dynamic SQL
    public Vector getDynamicSQLResults(csSecurity security, String sqlStmt)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);

        Vector ret = new Vector();
        try
        {
            sqlResolver.beginTrans(_dbConn);
            ret = sqlResolver.getDynamicSQLResults(_dbConn, sqlStmt);
            sqlResolver.endTrans(_dbConn);
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::getDynamicSQLResults failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
        return ret;
    }

    //Insert
    public int insert(csSecurity security, BusinessObject obj)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            int ret = sqlResolver.insert(_dbConn, obj);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::insert failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Update
    public int update(csSecurity security, BusinessObject obj, String exp)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            int ret = sqlResolver.update(_dbConn, obj, exp);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::update failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Delete
    public int delete(csSecurity security, BusinessObject obj, String exp)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            int ret = sqlResolver.delete(_dbConn, obj, exp);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::delete failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Get MAX Detail ID
    public long getMaxDetailId(csSecurity security, BusinessObject obj, String exp)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            long ret = sqlResolver.getMaxDetailId(_dbConn, obj, exp);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::getMaxDetailId failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Get All IDs
    public Vector getAllIds(csSecurity security, String tableName, String exp)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            Vector ret = sqlResolver.getAllIds(_dbConn, tableName, exp);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::getAllIds failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Query By Expression.
    //NOTE:The obj is just needed to get the field names.  Kind of kludge but,
    //hey...
    public Vector queryByExpression(csSecurity security, BusinessObject obj, String exp)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            Vector ret = sqlResolver.queryByExpression(_dbConn, obj, exp);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::queryByExpression failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Get TableNames.
    public String[] getTableNames(csSecurity security)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            String[] ret =  sqlResolver.getTables(_dbConn);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::getTableNames failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    //Get ColumnNames.
    public DBAttributes[] getColumnNames(csSecurity security, String tableName)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            DBAttributes[] ret = sqlResolver.getColumns(_dbConn, tableName);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::getColumnNames failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public int getNewSequenceNum(csSecurity security, int seqId)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            int ret = sqlResolver.getNewSequenceNum(_dbConn, seqId);
            sqlResolver.endTrans(_dbConn);
            return ret;
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::getNewSequenceNum failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public void setSequenceValue(csSecurity security, int seqId, int seqVal)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
            sqlResolver.setSequenceValue(_dbConn, seqId, seqVal);
            sqlResolver.endTrans(_dbConn);
        } catch (Exception e)
        {
            sqlResolver.rollback(_dbConn);
            String msg = this.getClass().getName()+"::getNewSequenceNum failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public void beginTrans(csSecurity security)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.beginTrans(_dbConn);
        } catch (Exception e)
        {
            String msg = this.getClass().getName()+"::commit failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public void endTrans(csSecurity security)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.endTrans(_dbConn);
        } catch (Exception e)
        {
            String msg = this.getClass().getName()+"::commit failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public void rollback(csSecurity security)
    throws RemoteException, daiRemoteServiceException
    {
        _sessionMeta.setClientServerSecurity(security);
        try
        {
            sqlResolver.rollback(_dbConn);
        } catch (Exception e)
        {
            String msg = this.getClass().getName()+"::rollback failure"+
                         "\n"+e.getLocalizedMessage();
            throw new daiRemoteServiceException(msg, null);
        }
    }

    public static void main (String[] args)
    {
        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        if (!sessionMeta.getcsPlumbingType().equals("RMI")) {
            String msg = "Could not start RMI Service.\n" +
                        "PlumbingType not set to RMI in configuration file.";
            System.out.println(msg);
            return;
        }

        System.setSecurityManager (new RMISecurityManager());

        try
        {
            //Create a new instance of the service.
            DBRMIServiceImpl dbService = new DBRMIServiceImpl();

            //Bind service to local registry on default port.
            Naming.rebind("//"+sessionMeta.getHostname()+"/"+DBService.SERVER_NAME, dbService);

        } catch (Exception e)
        {
            System.err.println("DBService exception: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }
}
