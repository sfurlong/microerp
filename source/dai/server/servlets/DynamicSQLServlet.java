package dai.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class DynamicSQLServlet extends HttpServlet
{
	csDBAdapterFactory      _dbAdapterFactory;
	csSessionAdapterFactory _sessionAdapterFactory;
	csDBAdapter         _dbAdapter;
	csSessionAdapter    _sessionAdapter;
	SessionMetaData     _sessionMeta;
	csSecurity          _security;
	Logger              _logger;

	//Initialize global variables
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("Started Init");
		_dbAdapterFactory  = csDBAdapterFactory.getInstance();
		_sessionAdapterFactory  = csSessionAdapterFactory.getInstance();
		_dbAdapter       = _dbAdapterFactory.getDBAdapter();
		_sessionAdapter  = _sessionAdapterFactory.getSessionAdapter();
		_sessionMeta     = SessionMetaData.getInstance();
		_logger         = Logger.getInstance();
	}

	//Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		String userValid = "" + (String) session.getAttribute("userValid");
		if (userValid.equals("true"))
		{

			//The SQL Statement
			String sqlStmt = "";
			try
			{
				sqlStmt = request.getParameter("sqlStmt");
				System.out.println(sqlStmt);
			} catch (Exception e)
			{
				e.printStackTrace();
			}

			session.setAttribute("sqlStmt", sqlStmt);
			String componentName="DynamicReport";
			boolean rptValid=false;
			csSecurity security  = (csSecurity) session.getAttribute("security");
			String userId = (String) session.getAttribute("userId");
			String rightsSql = "select READ_PERMISSION from USER_WEBREPORTS_SECURITY where " +
							   "COMPONENT_ID='"+componentName+"' and ID='"+userId+"'";
			try
			{
				//rptValid = _userReportsSecurityManager.getComponentSecurity(componentName);
				Vector rights = _dbAdapter.getDynamicSQLResults(security, rightsSql);
				if (rights.get(0).toString().equals("[Y]"))
					rptValid=true;
			} catch (Exception e)
			{
				rptValid=false;
				e.printStackTrace();
			}
			System.out.println("rptValid= " + rptValid);

			rptValid = true;

			if (rptValid)
			{
				//Generate the output
				String excel = request.getParameter("excel");
				if (excel != null && excel.equals("true"))
				{
					response.setContentType("application/x-msexcel");
				} else
				{
					response.setContentType("text/html");
				}
				System.out.println(request.getQueryString());

				PrintWriter out = new PrintWriter (response.getOutputStream());
				out.println("<html>");
				out.println("</head>");
				out.println("<center><H1>Dynamic Report Results<br><hr></H1></center>");
				String file = "http://" + _sessionMeta.getWebRptsHost() + "/saveRpt.html";
				out.println("<body  onUnload=\"window.open('" + file + "', 'new',"
							+"'resizable,width=400,height=220,top=115,left=195')\";>");
				//The body of the report
				out.println(createDataTable(sqlStmt));
				out.println("</body></html>");
				out.close();
			} else
			{
				System.out.println("Report Blocked");
				response.sendRedirect("http://" + _sessionMeta.getWebRptsHost() + "/error401.html");
			}
		} else
		{
			System.out.println("Send to error");
			response.sendRedirect("http://" + _sessionMeta.getWebRptsHost() + "/error401.html");
		}
	}//end doPost

	//Get Servlet information
	public String getServletInfo()
	{
		return "dai.server.Servlets.DynamiSQLServlet Information";
	}

	private String createDataTable(String sqlStmt)
	{
		Vector recs = null;
		String tableData = "";
		String headings[] = null;

		try
		{
			recs = _dbAdapter.getDynamicSQLResults(_sessionMeta.getClientServerSecurity(),
												   sqlStmt);
			HTMLGenerator gen = new HTMLGenerator();
			headings = gen.getColumnHeadings(_sessionMeta.getClientServerSecurity(), sqlStmt);
			tableData = gen.createSimpleTableFromVect(recs, headings); //new String[]{"Head1", "Head2", "Head3"});
		} catch (Exception e)
		{
			e.printStackTrace();
			return e.getLocalizedMessage();
		}

		return tableData;
	}
}

