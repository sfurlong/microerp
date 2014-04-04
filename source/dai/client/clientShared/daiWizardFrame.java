
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.shared.cmnSvcs.Logger;
import daiBeans.daiButton;
import daiBeans.daiButtonBar;

abstract public class daiWizardFrame extends JInternalFrame implements daiInternalFrameInterface
{

//********************************************************************//
//                  CONSTRUCTORS                                      //
//********************************************************************//
	public daiWizardFrame(JFrame container)
	{
          CONTAINER = container;
		try
		{
			jbInit();
			pack();

		} catch (Exception ex)
		{
			LOGGER.logError(container, "Could not initialize daiWizardFrame.\n" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}


	void jbInit() throws Exception
	{
        this.setBorder(null);
        LOGGER = Logger.getInstance();

        this.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                this_keyPressed(e);
            }

            public void keyTyped(KeyEvent e) {
                this_keyTyped(e);
            }

            public void keyReleased(KeyEvent e) {
                this_keyReleased(e);
            }
        });
        daiMenuBar.setNewIdMenuActive(false);
        daiMenuBar.setFileMenusActive(false);
        daiMenuBar.setPrintMenusActive(false);
        daiMenuBar.setSearchMenusActive(false);

        daiMenuBar.addActionListener(new ActionListener()  {
          public void actionPerformed(ActionEvent e)  {
            daiInternalFrame_actionPerformed(e);
          }
        });

        buttonPanel.setBorder(BorderFactory.createEtchedBorder());
        buttonPanel.setMaximumSize(new Dimension(32767, 37));
        buttonPanel.setMinimumSize(new Dimension(600, 37));
        buttonPanel.setPreferredSize(new Dimension(600, 37));
        buttonPanel.setLayout(xYLayout1);

        button_next.setLength(80);
        button_next.setMnemonic(KeyEvent.VK_N);
        button_next.setText("Next >>");
        button_next.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                button_next_actionPerformed();
            }
        });

        button_prev.setLength(80);
        button_prev.setMnemonic(KeyEvent.VK_P);
        button_prev.setText("<< Prev");
        button_prev.setEnabled(false);
        button_prev.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                button_prev_actionPerformed();
            }
        });

        button_cancel.setLength(80);
        button_cancel.setMnemonic(KeyEvent.VK_C);
        button_cancel.setText("Cancel");
        button_cancel.addActionListener(new java.awt.event.ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                button_cancel_actionPerformed();
            }
        });

        button_post.setLength(80);
        button_post.setMnemonic(KeyEvent.VK_O);
        button_post.setText("Post");
        button_post.setEnabled(false);
        button_post.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                button_post_actionPerformed();
            }
        });

        buttonPanel.add(button_cancel, new XYConstraints(120, 3, -1, -1));
        buttonPanel.add(button_prev, new XYConstraints(213, 3, -1, -1));
        buttonPanel.add(button_next, new XYConstraints(300, 3, -1, -1));
        buttonPanel.add(button_post, new XYConstraints(395, 3, -1, -1));

        //Decorate the frame
        _jscrollpane.setViewportView(CONTENT_PANEL);
        this.getContentPane().add(bannerPanel, BorderLayout.NORTH);
        this.getContentPane().add(_jscrollpane, BorderLayout.CENTER);
        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        CONTENT_PANEL.setLayout(cardLayout1);
        javax.swing.plaf.InternalFrameUI ui = this.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI)ui).setNorthPane(null);
		centerFrame();
	}

//***************************************************************//
//              PUBLIC METHODS
//***************************************************************//
    public daiStatusBar statusBar = new daiStatusBar();

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
//              PROTECTED
//***************************************************************//
    protected Logger LOGGER;
    protected JFrame CONTAINER;
    protected JScrollPane _jscrollpane = new JScrollPane();
    protected JPanel CONTENT_PANEL = new JPanel();
    protected daiBannerPanel  bannerPanel = new daiBannerPanel();
    protected daiButton button_cancel = new daiButton();
    protected daiButton button_prev = new daiButton();
    protected daiButton button_next = new daiButton();
    protected daiButton button_post = new daiButton();
    protected WizardPanel[]    wizardPanels;
    private daiFrameMenuBar daiMenuBar = new daiFrameMenuBar();

    abstract protected void button_next_actionPerformed();
    abstract protected void button_prev_actionPerformed();
    abstract protected void button_cancel_actionPerformed();
    abstract protected void button_post_actionPerformed();

    protected class WizardPanel {
        public daiWizardPanel  panelRef;
        public String  panelName;

        public WizardPanel(daiWizardPanel panel, String name) {
            panelRef = panel;
            panelName = name;
        }
    }

    protected void setActivePanel(int panelNum, String panelName) {
        cardLayout1.show(CONTENT_PANEL, wizardPanels[panelNum].panelName);
    }

	protected void centerFrame()
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
//***************************************************************//
//                      PRIVATE
//***************************************************************//
    BoxLayout2 boxLayout21 = new BoxLayout2();
    JPanel buttonPanel = new JPanel();
    XYLayout xYLayout1 = new XYLayout();
    CardLayout cardLayout1 = new CardLayout();

    void this_keyPressed(KeyEvent e) {
    }

    void this_keyTyped(KeyEvent e) {
    }

    void this_keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    public JMenuBar getDaiMenuBar(){
      return daiMenuBar;
    }
    public void setDaiMenuBar(JMenuBar _daiMenuBar){}
    public JToolBar getDaiButtonBar(){
      return new daiButtonBar();
    }
    public void setDaiButtonBar(JToolBar _daiButtonBar){}

    public JPanel getDaiStatusBar(){
      return new daiStatusBar();
    }
    public void setDaiStatusBar(JPanel _daiStatusBar){}
    public void daiInternalFrame_actionPerformed(java.awt.event.ActionEvent e){
      String actionCommand = e.getActionCommand();
      if (actionCommand.equals(daiFrameActions.EXIT)) {
                      System.out.println("Ending Application");
                      int retVal = JOptionPane.showOptionDialog(this,"Closing this window will close all windows",
                        "Closing Application",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);

                      if(retVal == JOptionPane.YES_OPTION)
                        System.exit(0);
      } else if (actionCommand.equals(daiFrameActions.WINDOW)) {
                        new dai.client.clientAppRoot.daiExplorerFrame();
      }
    }
    public void daiInternalFrame_keyEvent(KeyEvent e) {}
}
