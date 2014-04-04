package dai.server.servlets;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class XMLRptServlet_t
{
    csDBAdapterFactory      _dbAdapterFactory;
    csSessionAdapterFactory _sessionAdapterFactory;
    csDBAdapter         _dbAdapter;
    csSessionAdapter    _sessionAdapter;
    SessionMetaData     _sessionMeta;
    csSecurity          _security;
    Logger              _logger;
    ParseXMLRptData     _xmlRptData;

    public static void main (String argv [])
    {
        new XMLRptServlet_t();
    }

	//Initialize global variables
	public XMLRptServlet_t()
    {
        System.out.println("Started Init");
        _dbAdapterFactory  = csDBAdapterFactory.getInstance();
        _sessionAdapterFactory  = csSessionAdapterFactory.getInstance();
        _dbAdapter       = _dbAdapterFactory.getDBAdapter();
        _sessionAdapter  = _sessionAdapterFactory.getSessionAdapter();
        _sessionMeta     = SessionMetaData.getInstance();
        _logger         = Logger.getInstance();
        init();
	}

	//Process the HTTP Post request
	public void init() {
		//valid eCorp user Id
		String userId = "";
		try {
            userId = "STEVE";
        } catch (Exception e) {
            e.printStackTrace();
        }
		//valid eCorp user password
		String passwd = "";
		try {
            passwd = "fur";
        } catch (Exception e) {
            e.printStackTrace();
        }
		//The SQL Statement
		String rptName = "";
		try {
            rptName = "cashReceipts.xml";
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Validate the user's login.
        boolean userValid = validateSecurity(userId, passwd);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter (os);
		out.println("<html>");
		out.println("<head><title>Dynamic SQL Results</title>");
        out.println("<H1>Dynamic SQL Results</H1>");
        out.println("</head>");

        //Generate the content.
        if (userValid) {
//            _xmlRptData = new ParseXMLRptData(rptName);
            out.println(createDataTable(_xmlRptData.getSQLStmt(), _xmlRptData.getColHeadings()));
        } else {
            out.println(generateLoginErrorText());
        }

		out.println("<body>");
		out.println("</body></html>");
		out.close();
	}

    private boolean validateSecurity(String uid, String pwd) {
        try {
            _security = _sessionAdapter.connect(null, //not used at this time
                                                    uid,
                                                    pwd);

        } catch (Exception e) {
            return false;
        }
        _sessionMeta.setClientServerSecurity(_security);
        return true;
    }

   	private String createDataTable(String sqlStmt, String[] tableHeadings)
	{
        Vector recs = null;
        String tableData = "";

        try {
            System.out.println(sqlStmt);
            recs = _dbAdapter.getDynamicSQLResults(_sessionMeta.getClientServerSecurity(),
                                                    sqlStmt);
            HTMLGenerator gen = new HTMLGenerator();
            tableData = gen.createTable(recs, _xmlRptData);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

        return tableData;
	}

    private String generateLoginErrorText() {
        String ret = "<p>" +
            "Unable to Login Please Check User Id and Password." +
            "<p><I>(Note:  User Id and Password are Case sensitive)<I>";
        return ret;
    }
}
