package dai.server.dbService;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;

import dai.shared.businessObjs.DBAttributes;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiException;
import dai.shared.csAdapters.csLoggerAdapter;
import dai.shared.csAdapters.csLoggerAdapterFactory;

public class dbconnect
{

	private Connection        _conn     = null;

	csLoggerAdapter _logger;

	// Constructor //
	public dbconnect()
	{
        csLoggerAdapterFactory loggerAdapterFactory = csLoggerAdapterFactory.getInstance();
		_logger = loggerAdapterFactory.getLoggerAdapter();
	}

	// connectToDB //
	public boolean connectToDB(String url, String driver, String uid, String pwd)
	throws daiException
	{

		//Incase It's already open;
		closeDB();

		try
		{
            Class.forName (driver);

            java.sql.DriverManager.getDriver (url);

			//Open the bad boy.
			_conn = DriverManager.getConnection(url, uid, pwd);

            //!!AUTOCOMMIT OFF!!
            _conn.setAutoCommit(false);

            DatabaseMetaData dbMeta = _conn.getMetaData();
            
			String result = "Connected to: " + dbMeta.getURL() + "\n" +
							"Driver: " + dbMeta.getDriverName() + "\n" +
							"Version: " + dbMeta.getDriverVersion();

			//Let everyone know what happended
			System.out.println(result);
			System.out.println("Successfully connected to database.");

			return true;

		} catch (Exception e)
		{
			//Log: dialog, system.out, disk
			String msg = "Error connecting to database.\n" +
                            "url: " + url + "\n" +
                            "uid: " + uid + "\n" +
                            "pwd: " + pwd + "\n" +
                            e.getLocalizedMessage();
			_logger.logError(SessionMetaData.getInstance().getClientServerSecurity(), msg);
			throw new daiException(msg, this);
		}
	}


	// closeDB //
	public boolean closeDB()
	throws daiException
	{
		try
		{
			if (_conn != null)
			{
				_conn.close();
			}
		} catch (Exception e)
		{
			//Log: dialog, system.out, disk
			String msg = "Error closing the database connection\n" + e.getLocalizedMessage();
			_logger.logError(SessionMetaData.getInstance().getClientServerSecurity(), msg);
			throw new daiException(msg, this);
		}

		return true;
	}


	// executeSQL //
	public ResultSet executeSQL(String SQLStmt)
	throws daiException
	{
		ResultSet results = null;

		if (_conn == null)
		{
			//Log: dialog, system.out, disk
			String msg = "Error executing the current SQL statement.  "+
                         "Not Connected to the Database\n";
			_logger.logError(SessionMetaData.getInstance().getClientServerSecurity(), msg);
			throw new daiException(msg, this);
		}
		try
		{
            _logger.logSQL(SessionMetaData.getInstance().getClientServerSecurity(), SQLStmt);

            //Perform auditing on update and insert statements.
            String sqlCmd = SQLStmt.trim().substring(0, 6);
			if (sqlCmd.equalsIgnoreCase("update") || sqlCmd.equalsIgnoreCase("insert")) {
				insertSQLAudit(sqlCmd, SQLStmt);
			}
			
            Statement dbStmt = _conn.createStatement();

			dbStmt.execute(SQLStmt);

			results = dbStmt.getResultSet();

		} catch (Exception e)
		{
			String msg = "Error executing the current statement.\n" +
						 e.getLocalizedMessage() +
						 "\n" + SQLStmt;
			_logger.logError(SessionMetaData.getInstance().getClientServerSecurity(), msg);
			throw new daiException(msg, this);
		}

		return results;
	}


	private void insertSQLAudit(String sqlCmd, String sqlToAudit) throws Exception {
		
		String sqlStmt = "insert into sql_audit (" +
				" created_by, date_created, operation, sql_stmt) values (?, ?, ?, ?)";

		PreparedStatement dbStmt = _conn.prepareStatement(sqlStmt);
		dbStmt.setString(1, SessionMetaData.getInstance().getUserId());
		dbStmt.setTimestamp(2, new Timestamp(Calendar.getInstance().getTimeInMillis()));
		dbStmt.setString(3, sqlCmd);
		if (sqlToAudit.length() > 2000) {
			dbStmt.setString(4, sqlToAudit.substring(0, 1999));
		} else {
			dbStmt.setString(4, sqlToAudit);
		}
		dbStmt.executeUpdate();
	}

    //Get all tables in the database.
	public String[] getTables() throws Exception {
		String[] tableArray = {"",""};
		int nTables = 0;
		int nColumns = 0;
		int i = 0;

        DatabaseMetaData dbMeta = _conn.getMetaData();
        
		ResultSet resultSet = dbMeta.getTables( null, null, "%", new String[] {"TABLE"});
		while (resultSet.next())
			nTables++;

		resultSet = dbMeta.getTables( null, null, "%", new String[] {"TABLE"});
		ResultSetMetaData rsmd = resultSet.getMetaData();
		nColumns = rsmd.getColumnCount();

		tableArray = new String[nTables];
		while (resultSet.next())
		{
			tableArray[i] = resultSet.getString(3);
			i++;
		}
		return tableArray;
	}


    //Get all column attributes for a given table.
	public DBAttributes[] getColumns(String tableName) throws Exception {
		String[] colArray = {"",""};
		int nColumns = 0;
		int i = 0;

        DatabaseMetaData dbMeta = _conn.getMetaData();
		
        ResultSet resultSet = dbMeta.getColumns(null, null, tableName, "%");
		while (resultSet.next())
		{
			nColumns++;
		}

		resultSet = dbMeta.getColumns(null, null, tableName, "%");
		DBAttributes colData[] = new DBAttributes[nColumns];
		while (resultSet.next())
		{
			colData[i] = new DBAttributes(resultSet.getString(4),
										  "",
										  resultSet.getString(6),
										  resultSet.getInt(7),
										  false);
			i++;
		}

		return colData;
	}

    public void commit() throws daiException
    {
        try {
            _conn.commit();
		} catch (Exception e)
		{
			String msg = "Error commiting transaction.\n" +
						 e.getLocalizedMessage();
			_logger.logError(SessionMetaData.getInstance().getClientServerSecurity(), msg);
			throw new daiException(msg, this);
		}
    }

    public void rollback() throws daiException
    {
        try {
            _conn.rollback();
		} catch (Exception e)
		{
			String msg = "Error rolling back transaction.\n" +
						 e.getLocalizedMessage();
			_logger.logError(SessionMetaData.getInstance().getClientServerSecurity(), msg);
			throw new daiException(msg, this);
		}
    }
    
    public static void main(String args[]) {
      dbconnect dbconn = new dbconnect();
    }
}
