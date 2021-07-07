//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998-2000
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:
//  This class maintains data related to a users session.
//  It has been implemented as a Singleton pattern.
//  NOTE:  This class should not be instantiated directly.
//          New instances should be initialized to null and
//          the getInstance() method should be used to get a
//          handle to this class.

package dai.shared.cmnSvcs;

import java.io.FileInputStream;
import java.util.Properties;

import dai.server.dbService.FireBirdDBSequenceGenerator;
import dai.server.dbService.HSqlDBSequenceGenerator;

public class SessionMetaData
{
    //Get our static instance data.
    public static SessionMetaData getInstance()
    {
        return _metaData;
    }

	public String getLocality()
	{
		return _locality;
	}

	public String getUserId()
	{
		return _csSecurity.getUid();
	}

    public String getcsPlumbingType()
    {
        return _csPlumbingType;
    }

    public String getDaiHome()
    {
        return System.getProperty("DAI_HOME");
    }

    public String getDaiLibHome()
    {
        return System.getProperty("DAI_HOME")+"/lib/";
    }

    public String getDaiRptHome()
    {
        return System.getProperty("DAI_HOME")+"/rpt/";
    }

    public String getImagesHome()
    {
        return System.getProperty("DAI_HOME")+"/images/";
    }

    public String getHostname()
    {
        return _csHostName;
    }

    public String getJDBCDriverName()
    {
        return _dbDriverName;
    }

    /**
     * Returns the Date Format that is used by the database engine
     * for database sql opperations.
     * @return
     */
    public String getDBDateFormat() {
    	if (getDatabaseType().equals("firebird") || getDatabaseType().equals("interbase")) {
    		return "MM/dd/yy";
        } else if (getDatabaseType().equals("hsqldb")) {
        	return "yyyy-MM-dd";
        } else {
            return null;
        }
    	
    }
    
    public String getDatabaseType() {
        if (_dbDriverName.indexOf("firebird") >= 0) {
        	return "firebird";
        } else if (_dbDriverName.indexOf("interbase") >= 0) {
            return "interbase";
        } else if (_dbDriverName.indexOf("hsqldb") >= 0) {
            return "hsqldb";
        } else {
            return null;
        }
    }
    
    public String getServerLogin()
    {
        return _serverLogin;
    }

    public String getServerPasswd()
    {
        return _serverPasswd;
    }

    public String getServerDBURL()
    {
        return _serverDBURL;
    }

    public int getTraceLevel()
    {
        return _traceLevel;
    }

    public int getMaxDBSelectROws()
    {
        return _maxDBSelectRows;
    }

    public void setClientServerSecurity(csSecurity security)
    {
        _csSecurity = security;
    }

    public csSecurity getClientServerSecurity()
    {
        return _csSecurity;
    }

    public void setTransCallLevel(int level) {
        transCallLevel = level;
    }

    public int getTransCallLevel(){
        return transCallLevel;
    }

    public String getWebRptsHost() {
        return _webRptsHost;
    }
    
    public String getVersionInfo() {
        return _version;
    }
    
    public String getBuildInfo() {
        return _build;
    }

    public String getPatchNum() {
        return _patchNum;
    }
    
    /////////////////  Private Methods/Data //////////////////////////

    //Private Constructor.  Enforces Singleton.
    private SessionMetaData() {
        PropertyFileData reader = PropertyFileData.getInstance();

        _csPlumbingType = reader.getProperty(daiProperties.PLUMBING_TYPE);
        _csHostName     = reader.getProperty(daiProperties.HOST_NAME);
        _webRptsHost    = reader.getProperty(daiProperties.WEB_RPTS_HOST);
        _dbDriverName   = reader.getProperty(daiProperties.JDBC_DRIVER);
        _serverLogin    = "SYSDBA";
        _serverPasswd   = "daimgr";
        _serverDBURL    = reader.getProperty(daiProperties.SERVER_DB_URL);
        _traceLevel     = Integer.parseInt(reader.getProperty(daiProperties.TRACE_LEVEL));
        _maxDBSelectRows= Integer.parseInt(reader.getProperty(daiProperties.MAX_DB_SELECT_ROWS));
        loadVersionInfo();
    }

	private void loadVersionInfo() {
	       try {
	          String filePath = System.getProperty("DAI_HOME") + "/version.txt";
	          Properties prop = new Properties();
	          prop.load(new FileInputStream(filePath));
	          _build = prop.getProperty("buildDate");
	          _version = prop.getProperty("versionNumber");
	          _patchNum = prop.getProperty("patchNumber");
	       } catch (Exception e) {
	          Logger.getInstance().logError("Could not find client version file.");
	       } finally {
	          if (_build == null) {
	             _build = "??";
	          }
	          if (_version == null) {
	             _version = "??";
	          }
	          if (_patchNum == null) {
	        	  _patchNum = "??";
		      }
	       }
	    }

	//Declaration of the Private Static metaData.
    //Since it's static, only one copy will exist of the instance data
    //a'la the Singleton pattern.
    private static SessionMetaData _metaData = new SessionMetaData();
	private String _csPlumbingType;
	private String _csHostName;
	private String _webRptsHost;
    private String _dbDriverName;
    private String _serverLogin;
    private String _serverPasswd;
    private String _serverDBURL;
    private int    _traceLevel;
    private int    _maxDBSelectRows;
    private csSecurity _csSecurity;
    private int transCallLevel = 0;
	private String _locality           = "SUPER";
	private String _build = null;
	private String _version = null;
	private String _patchNum = null;
}
