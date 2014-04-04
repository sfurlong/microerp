package dai.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class XMLRptServlet extends HttpServlet
{
	csDBAdapterFactory      _dbAdapterFactory;
	csDBAdapter             _dbAdapter;
	SessionMetaData         _sessionMeta;
	csSecurity              _security;
	ParseXMLRptData         _xmlRptData;
	HttpSession session;

	//Initialize global variables
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("Started Init");
	}

	//Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		session = request.getSession(true);
		_sessionMeta = SessionMetaData.getInstance();
		_sessionMeta.setClientServerSecurity((csSecurity)session.getAttribute("csSecurity"));
		_dbAdapterFactory  = csDBAdapterFactory.getInstance();
		_dbAdapter       = _dbAdapterFactory.getDBAdapter();

		//The Name of the XML Report definition file.
		String rptName = "";
		try
		{
			rptName = request.getParameter("rptName");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		//Get the rest of the parameters.
		String[] repParms = getReplacementParms(request);


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

		//Generate the content.
		_xmlRptData = new ParseXMLRptData(rptName, repParms);
		out.println("<H1>"+_xmlRptData.getRptTitle()+"</H1>");
		out.println("<body>");
		out.println(createDataTable(_xmlRptData.getSQLStmt(), _xmlRptData.getColHeadings()));
		out.println("</body></html>");
		out.close();
	}//end doPost

	//Get Servlet information
	public String getServletInfo()
	{
		return "dai.server.Servlets.ItemInventoryServlet Information";
	}

	private String createDataTable(String sqlStmt, String[] tableHeadings)
	{
		Vector recs = null;
		String tableData = "";

		try
		{
			System.out.println(sqlStmt);
			recs = _dbAdapter.getDynamicSQLResults(_sessionMeta.getClientServerSecurity(),
												   sqlStmt);
			HTMLGenerator gen = new HTMLGenerator();
			tableData = gen.createTable(recs, _xmlRptData);
		} catch (Exception e)
		{
			e.printStackTrace();
			return e.getLocalizedMessage();
		}

		return tableData;
	}


	public static String[] getReplacementParms(HttpServletRequest request)
	{
		Enumeration parms = request.getParameterNames();
		String      currParm;
		String[]    tempParms = new String[9];
		int         parmCnt = 0;
		String[]    ret = null;

		while (parms.hasMoreElements())
		{
			currParm = (String)parms.nextElement();
			if (currParm.substring(0, 4).toLowerCase().equals("parm"))
			{
				tempParms[parmCnt] = request.getParameter(currParm.substring(0, 4)+Integer.toString(parmCnt));
				parmCnt++;
			}
		}
		if (parmCnt > 0)
		{
			ret = new String[parmCnt];
			for (int i=0; i< parmCnt; i++)
			{
				ret[i] = tempParms[i]+"";
				System.out.println(ret[i]);
			}
		}
                else
                {  ret = new String[1];
                   ret[0] = "";
                   System.out.println(ret[0]);
                }
		return ret;
	}
}
