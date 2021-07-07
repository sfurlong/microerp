//  This class has been implemented as a Singleton pattern.
//  NOTE:  This class should not be instantiated directly.
//          New instances should be initialized to null and
//          the getInstance() method should be used to get a
//          handle to this class.
package dai.shared.cmnSvcs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class PropertyFileData {

    //Get our static instance data.
    public static PropertyFileData getInstance()
    {
        return _propFileData;
    }

    public String getProperty(int prop)
    {
        return (String)_propertyMap.get(new Integer(prop));
    }

    //The private constructor
    private PropertyFileData() {
        try {
            String filePath = System.getProperty("DAI_HOME") + "/altaprise.conf";
            _fileReader = new BufferedReader(new FileReader(filePath));

            readData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readData()
    {
        String rec = "";
        try {
            //Priming read
            rec = _fileReader.readLine();
            while (rec != null) {

                rec = rec.trim();

                //Skip comment lines.
                if (rec.length() > 0) {
                    parseProperty(rec);
                }

                //Read the next record
                rec = _fileReader.readLine();
            }

            _fileReader.close();

        } catch (Exception e) {
            System.out.println("PropertyFileReader::readData\n"+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void parseProperty(String rec)
    {
        //Check to see if this is a comment record.
        if (rec.charAt(0) == '#') {
            return;
        }

        int pos = rec.indexOf("=");

        if (pos > 0) {
            String propName  = rec.substring(0, pos).trim().toLowerCase();
            String propValue = rec.substring(pos + 1, rec.length()).trim();

            if (propName.equals(_PLUMBING_TYPE)) {
                _propertyMap.put(new Integer(daiProperties.PLUMBING_TYPE), propValue);
            } else if (propName.equals(_HOST_NAME)) {
                _propertyMap.put(new Integer(daiProperties.HOST_NAME), propValue);
            } else if (propName.equals(_JDBC_DRIVER)) {
                _propertyMap.put(new Integer(daiProperties.JDBC_DRIVER), propValue);
            } else if (propName.equals(_SERVER_LOGIN)) {
                _propertyMap.put(new Integer(daiProperties.SERVER_LOGIN), propValue);
            } else if (propName.equals(_SERVER_PASSWD)) {
                _propertyMap.put(new Integer(daiProperties.SERVER_PASSWD), propValue);
            } else if (propName.equals(_SERVER_DB_URL)) {
                _propertyMap.put(new Integer(daiProperties.SERVER_DB_URL), propValue);
            } else if (propName.equals(_TRACE_LEVEL)) {
                _propertyMap.put(new Integer(daiProperties.TRACE_LEVEL), propValue);
            } else if (propName.equals(_MAX_DB_SELECT_ROWS)) {
                _propertyMap.put(new Integer(daiProperties.MAX_DB_SELECT_ROWS), propValue);
            } else if (propName.equals(_WEB_RPTS_HOST)) {
                _propertyMap.put(new Integer(daiProperties.WEB_RPTS_HOST), propValue);
            } else if (propName.equals(_ERR_LOGGING)) {
                _propertyMap.put(new Integer(daiProperties.ERR_LOGGING), propValue);
            } else if (propName.equals(_SQL_LOGGING)) {
                _propertyMap.put(new Integer(daiProperties.SQL_LOGGING), propValue);
            } else if (propName.equals(_RPT_ENGINE_EXE)) {
                _propertyMap.put(new Integer(daiProperties.RPT_ENGINE_EXE), propValue);
            } else if (propName.equals(_RPT_DB_DSN)) {
                _propertyMap.put(new Integer(daiProperties.RPT_DB_DSN), propValue);
            } else if (propName.equals(_RPT_DB_PATH)) {
                _propertyMap.put(new Integer(daiProperties.RPT_DB_PATH), propValue);
            } else if (propName.equals(_RPT_TEMP_DIR)) {
                _propertyMap.put(new Integer(daiProperties.RPT_TEMP_DIR), propValue);
            } else if (propName.equals(_RPT_DOCS_DIR)) {
                _propertyMap.put(new Integer(daiProperties.RPT_DOCS_DIR), propValue);
            } else if (propName.equals(_RPT_WEBPRINT_URL)) {
                _propertyMap.put(new Integer(daiProperties.RPT_WEBPRINT_URL), propValue);
            } else if (propName.equals(_RPT_USE_WEB)) {
                _propertyMap.put(new Integer(daiProperties.RPT_USE_WEB), propValue);
            }
        }
    }

    //The private instantiation.
    private static PropertyFileData _propFileData = new PropertyFileData();

    //These values should match the string values in the property file: ecorp.conf.
    private String _PLUMBING_TYPE   = "plumbingtype";
    private String _HOST_NAME       = "hostname";
    private String _JDBC_DRIVER     = "jdbcdriver";
    private String _SERVER_LOGIN    = "serverlogin";
    private String _SERVER_PASSWD   = "serverpasswd";
    private String _SERVER_DB_URL   = "serverdburl";
    private String _TRACE_LEVEL     = "tracelevel";
    private String _MAX_DB_SELECT_ROWS = "maxdbselectrows";
    private String _WEB_RPTS_HOST = "webrptshost";
    private String _ERR_LOGGING = "errlogging";
    private String _SQL_LOGGING = "sqllogging";
    private String _RPT_USE_WEB = "reportuseweb";
    private String _RPT_ENGINE_EXE = "reportengine";
    private String _RPT_DB_DSN = "reportdbdsn";
    private String _RPT_DB_PATH = "reportdbpath";
    private String _RPT_TEMP_DIR = "reporttempdir";
    private String _RPT_DOCS_DIR = "reportdocsdir";
    private String _RPT_WEBPRINT_URL = "reportwebprinturl";
    private BufferedReader _fileReader;
    private HashMap _propertyMap = new HashMap();
}


