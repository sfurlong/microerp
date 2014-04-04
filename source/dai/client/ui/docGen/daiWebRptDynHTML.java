package dai.client.ui.docGen;

import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;

public class daiWebRptDynHTML
{

    public daiWebRptDynHTML()
    {
    }

    public static String getRptHTML(String rptName)
    {
        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        csSecurity      security = sessionMeta.getClientServerSecurity();

        String html = "<HTML>"+
                "<HEAD>"+
                "<TITLE>XML Report Servlet</TITLE>"+
                "<H1>XML Report Servlet</H1>"+
                "</HEAD>"+
                "<BODY>"+
                "<FORM  action=http://"+sessionMeta.getHostname()+":8080/servlet/dai.server.servlets.XMLRptServlet method=POST>"+
                "<table>"+
                "<tr>"+
                "<td>Start Date:</td>"+
                "<td><input type=text name=startDate size=15></td>"+
                "<td>End Date:</td>"+
                "<td><input type=text name=endDate size=15></td>"+
                "</tr>"+
                "<tr>"+
                "<td><input type=hidden name=userId value="+security.getUid()+" size=15></td>"+
                "<td><input type=hidden  name=passwd value="+security.getEncryptedPwd()+" size=15></td>"+
                "<td><input type=hidden  name=rptName value="+rptName+" size=15></td>"+
                "</tr>"+
                "</table>"+
                "<BR><BR><input type=submit value=\"Submit\"><input type=reset value=\"Reset\"></form>"+
                "</BODY>"+
                "</HTML>";

        return html;
    }
}
