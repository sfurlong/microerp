package dai.shared.csAdapters;

import java.util.*;
import dai.shared.cmnSvcs.*;
import dai.shared.businessObjs.*;

abstract public class csDBAdapter
{
    static public final int SEQUENCE_SHIPMENT               = 1;
    static public final int SEQUENCE_PURCH_ORDER            = 2;
    static public final int SEQUENCE_PURCH_ORDER_EXPENSE    = 3;
    static public final int SEQUENCE_GENERIC_DETAIL_ID      = 4;
    static public final int SEQUENCE_CUST_ORDER             = 5;
    static public final int SEQUENCE_VENDOR                 = 6;
    static public final int SEQUENCE_CUSTOMER               = 7;
    static public final int SEQUENCE_PAY_VOUCHER            = 8;
    static public final int SEQUENCE_CASH_RECEIPT           = 9;
    static public final int SEQUENCE_CREDIT_MEMO            = 10;
    static public final int SEQUENCE_PROSPECT               = 11;
    static public final int SEQUENCE_QUOTE                  = 12;


    abstract public void connectToDB(String uid, String pwd, String connectString)
    throws daiRemoteServiceException;

	//Execute Dynamic SQL
	abstract public DBRecSet execDynamicSQL(csSecurity security, String sqlStmt)
	throws daiRemoteServiceException;

	//Get Dynamic SQL Results
	abstract public Vector getDynamicSQLResults(csSecurity security, String sqlStmt)
	throws daiRemoteServiceException;

	//Insert
	abstract public int insert(csSecurity security, BusinessObject obj)
	throws daiRemoteServiceException;

	//Update
	abstract public int update(csSecurity security, BusinessObject obj, String exp)
	throws daiRemoteServiceException;

	//Delete
	abstract public int delete(csSecurity security, BusinessObject obj, String exp)
	throws daiRemoteServiceException;

	//Get MAX Detail ID
	abstract public long getMaxDetailId(csSecurity security, BusinessObject obj, String exp)
	throws daiRemoteServiceException;

	//Get All IDs
	abstract public Vector getAllIds(csSecurity security, String tableName, String exp)
	throws daiRemoteServiceException;

	//Query By Expression.
	//NOTE:The obj is just needed to get the field names.  Kind of kludee but,
	//hey...
	abstract public Vector queryByExpression(csSecurity security, BusinessObject obj, String exp)
	throws daiRemoteServiceException;

	//Get TableNames.
	abstract public String[] getTableNames(csSecurity security)
	throws daiRemoteServiceException;

	//Get ColumnNames.
	abstract public DBAttributes[] getColumnNames(csSecurity security, String tableName)
	throws daiRemoteServiceException;

    //Get New Sequence Number
    abstract public int getNewSequenceNum(csSecurity security, int seqId)
    throws daiRemoteServiceException;

    //Set Sequence Value
    abstract public void setSequenceValue(csSecurity security, int seqId, int seqValue)
    throws daiRemoteServiceException;

    //Begin trans
    abstract public void beginTrans(csSecurity security)
    throws daiRemoteServiceException;

    //End trans
    abstract public void endTrans(csSecurity security)
    throws daiRemoteServiceException;

    //Rollback
    abstract public void rollback(csSecurity security)
    throws daiRemoteServiceException;
}
