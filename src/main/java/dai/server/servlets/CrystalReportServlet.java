package dai.server.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.altaprise.crystal.CrystalCmdLineUtil;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class CrystalReportServlet extends HttpServlet
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

	//
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		String rptName = request.getParameter("rptName");
		String userId = request.getParameter("userid");
		String[] rptParams =  XMLRptServlet.getReplacementParms(request);

		//Generate the output
        javax.servlet.ServletOutputStream out = response.getOutputStream();

        String outFileName = CrystalCmdLineUtil.exportReportToPDF(rptName, rptParams);
        if (outFileName == null) {
        	//Get error screen
        	this.getErrorScreen(out);
        	return;
        }
        
        java.io.File f = new java.io.File( outFileName );


        int filesize = (int) f.length();        //figure out the file size
        byte[] data = new byte[filesize];       //create an array that is big enough

		response.setContentType("application/pdf");
		response.setContentLength(filesize);

        //create a stream to read the file:
        java.io.DataInputStream in = new java.io.DataInputStream( new java.io.FileInputStream( f ));
        in.readFully(data);
        in.close();
        out.write(data);
        out.flush();
        out.close();
        
        //Delete the temp file.
        f.delete();
	}


	//Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}//end doPost

	//Get Servlet information
	public String getServletInfo()
	{
		return "dai.server.Servlets.CrystalReportServlet Information";
	}

	private String getErrorScreen(javax.servlet.ServletOutputStream out) {
		String ret = "";
		ret += " <html> " +
		" <head> " +
		" <title>Document Print Error</title> " +
		" </head> " +
		" <body> " +
		" <h1>Printing of Doc failed.</h1> " +
		" Failed to print document.  Please try again." +
		" </body> " +
		" </html> ";
		try {
			out.println(ret);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
