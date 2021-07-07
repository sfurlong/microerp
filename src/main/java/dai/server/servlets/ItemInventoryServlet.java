package dai.server.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import dai.shared.csAdapters.*;
import dai.shared.businessObjs.*;
import dai.shared.cmnSvcs.*;

public class ItemInventoryServlet extends HttpServlet
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
		//id of the Order we are checking status on
		String itemId = "";
		try {
            itemId = request.getParameter("itemId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Validate the user's login.
        boolean userValid = validateSecurity(userId, passwd);

        //Generate the output
		response.setContentType("text/html");

		PrintWriter out = new PrintWriter (response.getOutputStream());
		out.println("<html>");
		out.println("<head><title>Item Inventory Servlet</title>");
        out.println("<H1>Inventory Onhand</H1>");
        out.println("</head>");

        //Generate the content.
        if (userValid) {
            out.println(createItemDataTable(itemId));
        } else {
            out.println(generateLoginErrorText());
        }

		out.println("<body>");
		out.println("</body></html>");
		out.close();
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

   	private String createItemDataTable(String itemId)
	{
        System.out.println("Started createInventoryData");
        Vector recs = null;
        try {
            recs = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                        new itemObj(),
                        " id like '"+itemId+"'" + " order by id ");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }

        itemObj obj;
        String tableHtml = "<TABLE border=\"1\">";

        //Write out the table Header
        tableHtml =    tableHtml +
                            "<TD><B>Item Id</B></TD>" +
                            "<TD><B>Description</B></TD>" +
                            "<TD><B>Qty OnHand</B></TD>" +
                            "<TD><B>BackOrd Customer</B></TD>" +
                            "<TD><B>BackOrd Vendor</B></TD>" +
                            "</TR>";
        for (int i=0; i<recs.size(); i++) {
            //Begin of table row.
            tableHtml = tableHtml + "<TR>";

            obj = (itemObj)recs.elementAt(i);

            //Write table columns for this row.
            tableHtml = tableHtml +
                "<TD>" + obj.get_id() + "</TD>" +
                "<TD>" + obj.get_standard_desc() + "</TD>" +
                "<TD>" + obj.get_onhand_qty() + "</TD>" +
                "<TD>" + obj.get_backorder_to_cust_qty() + "</TD>" +
                "<TD>" + obj.get_backorder_to_vendor_qty() + "</TD>";

            //End of table row.
            tableHtml = tableHtml + "</TR>";
        }

        tableHtml = tableHtml + "</TABLE>";

        return tableHtml;
	}

    private String generateLoginErrorText() {
        String ret = "<p>" +
            "Unable to Login Please Check User Id and Password." +
            "<p><I>(Note:  User Id and Password are Case sensitive)<I>";
        return ret;
    }
}
