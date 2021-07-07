package dai.server.servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import dai.shared.csAdapters.*;
import dai.shared.businessObjs.*;
import dai.shared.cmnSvcs.*;

public class OrderStatusServlet extends HttpServlet
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
		String ordId = "";
		try {
            ordId = request.getParameter("orderId");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Validate the user's login.
        boolean userValid = validateSecurity(userId, passwd);

        //Generate the output
		response.setContentType("text/html");

		PrintWriter out = new PrintWriter (response.getOutputStream());
		out.println("<html>");
		out.println("<head><title>Order Payment History</title>");
        out.println("<H1>Order Payment History</H1>");
        out.println("</head>");

        //Generate the content.
        if (userValid) {
            out.println(createOrderDataTable(ordId));
        } else {
            out.println(generateLoginErrorText());
        }

		out.println("<body>");
		out.println("</body></html>");
		out.close();
	}

	//Get Servlet information
	public String getServletInfo() {
		return "dai.server.Servlets.OrderStatusServlet Information";
	}

	private boolean validateSecurity(String uid, String pwd) {
		try
		{
			csSecurity _security = _sessionAdapter.connect(null, //not used at this time
														   uid,
														   pwd);

		} catch (Exception e)
		{
			return false;
		}
		_sessionMeta.setClientServerSecurity(_security);
		return true;
	}

	private String createOrderDataTable(String orderId)
	{
		System.out.println("Started createOrderData");
		Vector recs = null;
		try
		{
			recs = _dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
											  new cust_order_itemObj(),
											  " id = '"+orderId+"'" + " order by detail_id ");
		} catch (Exception e)
		{
			e.printStackTrace();
			return e.getLocalizedMessage();
		}

		cust_order_itemObj obj;
		String orderTableHtml = "<TABLE border=\"1\">";

		//Write out the table Header
		orderTableHtml =    orderTableHtml +
							"<TR><TD></TD>" +
							"<TD><B>Item Id</B></TD>" +
							"<TD><B>Description</B></TD>" +
							"<TD><B>Qty Order</B></TD>" +
							"<TD><B>Unit Price</B></TD>" +
							"<TD><B>Extnd Price</B></TD>" +
							"</TR>";
		for (int i=0; i<recs.size(); i++)
		{
			//Begin of table row.
			orderTableHtml = orderTableHtml + "<TR>";

			obj = (cust_order_itemObj)recs.elementAt(i);

			//Write table columns for this row.
			orderTableHtml = orderTableHtml +
							 "<TD>" + obj.get_detail_id() + "</TD>" +
							 "<TD>" + obj.get_item_id() + "</TD>" +
							 "<TD>" + obj.get_description1() + "</TD>" +
							 "<TD>" + obj.get_qty_ordered() + "</TD>" +
							 "<TD>" + obj.get_unit_price() + "</TD>" +
							 "<TD>" + obj.get_extended_price() + "</TD>";

			//End of table row.
			orderTableHtml = orderTableHtml + "</TR>";
		}

		orderTableHtml = orderTableHtml + "</TABLE>";

		return orderTableHtml;
	}

	private String generateLoginErrorText() {
		String ret = "<p>" +
					 "Unable to Login Please Check User Id and Password." +
					 "<p><I>(Note:  User Id and Password are Case sensitive)<I>";
		return ret;
	}
}