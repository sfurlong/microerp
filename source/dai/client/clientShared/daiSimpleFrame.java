
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.borland.jbcl.layout.PaneLayout;

import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.UserSecurityManager;

public class daiSimpleFrame extends JFrame
{

//***************************************************************//
//              CONSTRUCTORS
//***************************************************************//
	public daiSimpleFrame()
	{
		try
		{
			jbInit();
			pack();

		} catch (Exception ex)
		{
			LOGGER.logError(this, "Could not initialize daiFrame.\n" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}


	void jbInit() throws Exception
	{
        LOGGER = LOGGER.getInstance();

        contentPanel.setBackground(daiColors.PanelColor);
        contentPanel.setLayout(paneLayout1);

        //Decorate the frame
        this.getContentPane().add(bannerPanel, BorderLayout.NORTH);
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);

		centerFrame();
	}

//***************************************************************//
//              PUBLIC METHODS
//***************************************************************//
    public void setContentPane(JPanel panel)
    {
        contentPanel.add(panel);
        //For some reason this is needed for the new panel to get painted.
        centerFrame();
        this.show();
    }

    public void setComponentId(String compId) {
        COMPONENT_ID = compId;
    }

    public String getComponentId() {
        return COMPONENT_ID;
    }

    //This method should be called by any Frame that needs to implement
    //security.
    public void setComponentSecurity(String compId)
    {
        UserSecurityManager compSecurityMgr = null;
        compSecurityMgr = compSecurityMgr.getInstance();
        SessionMetaData sessionMeta = null;
        sessionMeta = sessionMeta.getInstance();

        /*
        user_securityObj compSecurity;
        compSecurity = compSecurityMgr.getComponentSecurity(compId);

        //Set the permissions for the New menu and button bar options
        if (compSecurity.get_create_permission().equals("0")) {
            //TBD_LOGIC
        }
            //Set the permissions for the Save menu and button bar options
        if (compSecurity.get_modify_permission().equals("0")) {
            //TBD_LOGIC
        }
        //Set the permissions for the Delete menu and button bar options
        if (compSecurity.get_delete_permission().equals("0")) {
            //TBD_LOGIC
        }
        //Set the permissions for the Delete menu and button bar options
        if (compSecurity.get_execute_permission().equals("0")) {
            //TBD_LOGIC
        }
        */
    }

    public void setBannerLeftText(String t)
    {
        bannerPanel.setBannerLeftText(t);
    }

    public String getBannerLeftText()
    {
        return bannerPanel.getBannerLeftText();
    }

    public void setBannerRightText(String t)
    {
        bannerPanel.setBannerRightText(t);
    }

    public String getBannerRightText()
    {
        return bannerPanel.getBannerRightText();
    }

//***************************************************************//
//              PROTECTED METHODS
//***************************************************************//
	protected Logger LOGGER;

	protected void processWindowEvent(WindowEvent e)
	{
		//Is the user closing the window?
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
            super.processWindowEvent(e);
		}
	}

//***************************************************************//
//              PRIVATE METHODS
//***************************************************************//
    private String COMPONENT_ID = "";
    private daiStatusBar statusBar = new daiStatusBar();
    private daiBannerPanel  bannerPanel = new daiBannerPanel();
    private JPanel contentPanel = new JPanel();
    PaneLayout paneLayout1 = new PaneLayout();

	private void centerFrame()
	{
		//Center the window
        this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);
	}
}
