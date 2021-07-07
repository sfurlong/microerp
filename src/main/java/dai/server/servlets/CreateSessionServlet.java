package dai.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class CreateSessionServlet extends HttpServlet
{
    csSessionAdapterFactory _sessionAdapterFactory;
    csSessionAdapter    _sessionAdapter;
    SessionMetaData     _sessionMeta;
    csSecurity          _security;

	//Initialize global variables
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
        System.out.println("Started Init");
        _sessionAdapterFactory  = csSessionAdapterFactory.getInstance();
        _sessionAdapter  = _sessionAdapterFactory.getSessionAdapter();
        _sessionMeta     = SessionMetaData.getInstance();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException {

		HttpSession session = request.getSession(true);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		Enumeration attribNames = session.getAttributeNames();
		while(attribNames.hasMoreElements()) {
			String key = (String)attribNames.nextElement();
			out.println(key+": "+session.getAttribute(key)+"<br>");
		}
		out.println("</body></html>");
		out.close();
	}
	
	//Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//valid eCorp user Id
		String userId = "";
		try {
            userId = request.getParameter("userId");
        } catch (Exception e) {
            e.printStackTrace();
        }
		//valid eCorp user password
		String passwd = "";
		try {
            passwd = request.getParameter("passwd");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", userId);
        session.setAttribute("passwd", passwd);

        //Validate the user's login.
        boolean userValid = validateSecurity(userId, passwd);
        session.setAttribute("csSecurity", _security);
        session.setAttribute("userValid", ""+userValid);

        if (userValid) {
            System.out.println("CreateSessionServlet: " + userId);
            System.out.println("Redirecting To: "+_sessionMeta.getWebRptsHost());
            response.sendRedirect("http://"+_sessionMeta.getWebRptsHost()+"/webrptsmain.jsp?TABID=FINANCIAL");
        } else {
            //Generate the output
    		response.setContentType("text/html");
    		PrintWriter out = new PrintWriter (response.getOutputStream());
    		out.println("<html>");
    		out.println("<head><title>Exec Report</title>");
            out.println("<H1>Exec Report</H1>");
            out.println("</head>");
            //Generate the content.
    		out.println("<body>");
            out.println(generateLoginErrorText());
            out.println("<P>"+Long.toString(session.getLastAccessedTime()));
    		out.println("</body></html>");
    		out.close();
        }
	}

	//Get Servlet information
	public String getServletInfo() {
		return "dai.server.Servlets.ItemInventoryServlet Information";
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

    private String generateLoginErrorText() {
        String ret = "<p>" +
            "Unable to Login Please Check User Id and Password." +
            "<p><I>(Note:  User Id and Password are Case sensitive)<I>";
        return ret;
    }
}
