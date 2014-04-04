
//Title:        Your Product Name
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Your description

package dai.client.ui.docGen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.BrowserPanel;

public class daiWebRptFrame extends JFrame {

    BrowserPanel daiBrowser = new BrowserPanel();

    SessionMetaData _sessionMeta;
    String _hostName;

    public daiWebRptFrame() {
        _sessionMeta = SessionMetaData.getInstance();
        try  {
            dai.client.clientShared.BrowserLauncher.openURL("http://"+_sessionMeta.getWebRptsHost());
            System.out.println(_sessionMeta.getWebRptsHost());
            //BrowserPanel not used right now.
            //Just leaving implementation for later reference.
            //jbInit();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        _hostName = _sessionMeta.getHostname();

        this.getContentPane().add(daiBrowser, BorderLayout.CENTER);

        daiBrowser.hideNavBar(true);
        pack();
        centerFrame();
        setVisible(true);

        //daiBrowser.setURL("http://"+_hostName+":8080/CreateSessionServelet.html?userId="+_sessionMeta.getUserId(), false);
        daiBrowser.setHTMLText(getWebRptsHTML());
    }

    public static void main (String[] args) {
        new daiWebRptFrame();
    }

    private void centerFrame()
    {
		//Center the window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    void daiLabel_home_mouseClicked(MouseEvent e) {
        daiBrowser.setURL("http://"+_hostName+":8080/demo.html", false);
    }

    void daiLabel_invRpt_mouseClicked(MouseEvent e) {
        daiBrowser.setURL("http://"+_hostName+":8080/ItemInventoryServlet.html", false);
    }

    void daiLabel_xmlRptDemo_mouseClicked(MouseEvent e) {
        daiBrowser.setURL("http://"+_hostName+":8080/XMLRptServlet.html", false);
    }

    void daiLabel_glRpt_mouseClicked(MouseEvent e) {
        String html = daiWebRptDynHTML.getRptHTML("gl.xml");
        daiBrowser.setHTMLText(html);
    }

    private String getWebRptsHTML() {
        String html = "<HTML>"+
                        "<HEAD><TITLE>Digital Artifacts, Inc. Web Reports Login</TITLE></HEAD>"+
                        "<CENTER>"+
                        "<FORM action=http://localhost:8080/servlet/dai.server.servlets.CreateSessionServlet METHOD=POST>"+
                        "<INPUT TYPE=hidden name=appCall value=true"+
                        "<INPUT TYPE=hidden name=userId value=\""+_sessionMeta.getUserId()+"\""+
                        "<INPUT TYPE=submit VALUE=\"Start Web Reports\"></TD></TR></TABLE>"+
                        "</FORM></CENTER></BODY></HTML>";
        return html;
    }
}