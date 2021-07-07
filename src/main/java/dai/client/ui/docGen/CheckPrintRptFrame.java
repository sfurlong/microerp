
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.docGen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import com.borland.jbcl.layout.BoxLayout2;

import dai.client.clientShared.daiBannerPanel;
import dai.client.clientShared.daiFrameActions;
import dai.client.clientShared.daiFrameMenuBar;
import dai.client.clientShared.daiInternalFrameInterface;
import dai.client.clientShared.daiStatusBar;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.UserInputDialog;
import daiBeans.daiButtonBar;

public class CheckPrintRptFrame extends JInternalFrame implements daiInternalFrameInterface
{
    private TitledBorder titledBorder1;
    private daiStatusBar statusBar = new daiStatusBar();
    private daiBannerPanel  bannerPanel = new daiBannerPanel();
    private CheckPrintRptPanel _checkPrintRptPanel = null;
    private JPanel contentPanel = new JPanel();
    private BorderLayout borderLayout1 = new BorderLayout();
    private JPanel buttonPanel = new JPanel();
    private BoxLayout2 boxLayout21 = new BoxLayout2();
    private daiFrameMenuBar daiMenuBar = new daiFrameMenuBar();
	private daiButtonBar buttonBar = new daiButtonBar();
    SessionMetaData _sessionMeta = SessionMetaData.getInstance();
    private boolean isChecksPrinted = false;
    JFrame CONTAINER = null;

//*************************************************************************
//              CONSTRUCTORS
//*************************************************************************
	public CheckPrintRptFrame(JFrame container)
	{
		try
		{
                        CONTAINER = container;
                        _checkPrintRptPanel = new CheckPrintRptPanel(container, this);
			jbInit();

		} catch (Exception ex)
		{
			LOGGER.logError(container, "Could not initialize daiFrame.\n" + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}


	void jbInit() throws Exception
	{
        this.setBorder(null);
        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        contentPanel.setLayout(boxLayout21);
        titledBorder1 = new TitledBorder("");
        //Set the banner and title
        this.setTitle("Check Printing");
        this.setBannerLeftText("Check Printing");

        LOGGER = LOGGER.getInstance();

        String imgBase = _sessionMeta.getImagesHome();
        buttonBar = new daiButtonBar(imgBase);
        buttonBar.buttonIsHidden(0, true);
        //Setup the default button bar.
//        buttonBar.insertButton(daiFrameActions.PRINT, imgBase+"print24.gif", "Print");
//        buttonBar.insertButton(daiFrameActions.PREVIEW, imgBase+"printpreview24.gif", "Preview");

		buttonBar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                daiInternalFrame_actionPerformed(e);
            }
        });
        daiMenuBar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                daiInternalFrame_actionPerformed(e);
            }
        });

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

        //Decorate the frame
        daiMenuBar.setFileMenusActive(false);
        daiMenuBar.setNewIdMenuActive(false);
        daiMenuBar.setSearchMenusActive(false);
        contentPanel.add(bannerPanel);
        contentPanel.add(_checkPrintRptPanel);
//        this.getContentPane().add(buttonBar, BorderLayout.NORTH);
        this.getContentPane().add(contentPanel, BorderLayout.CENTER);
//        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
          javax.swing.plaf.InternalFrameUI ui = this.getUI();
        ((javax.swing.plaf.basic.BasicInternalFrameUI)ui).setNorthPane(null);
		centerFrame();
        this.pack();
        this.show();
	}

//*************************************************************************
//              PUBLIC
//*************************************************************************
    public void daiInternalFrame_actionPerformed(java.awt.event.ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (actionCommand.equals(daiFrameActions.PRINT)) {
                  _checkPrintRptPanel.printReport();
                   isChecksPrinted = true;
                   verifyChecksPrintedWindow();
		} else if (actionCommand.equals(daiFrameActions.PREVIEW)) {
                   _checkPrintRptPanel.printReport();
                   isChecksPrinted = true;
                   verifyChecksPrintedWindow();
                } else if (actionCommand.equals(daiFrameActions.EXIT))  {
                   System.out.println("Ending Application");
                      JOptionPane windowCloseOption = new JOptionPane();
                      int retVal = windowCloseOption.showOptionDialog(this,"Closing this window will close all windows",
                        "Closing Application",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,null,null);

                      if(retVal == JOptionPane.YES_OPTION)  {
                        verifyChecksPrintedWindow();
                        System.exit(0);
                      }
                } else if (actionCommand.equals(daiFrameActions.WINDOW)) {
                        new dai.client.clientAppRoot.daiExplorerFrame();
                }

    }
    public JPanel getDaiStatusBar()  {
      return statusBar;
    }
    public void setDaiStatusBar(JPanel _daiStatusBar)  {
      statusBar = (daiStatusBar)_daiStatusBar;
    }
    public JToolBar getDaiButtonBar()  {
      return buttonBar;
    }
    public void setDaiButtonBar(JToolBar _daiButtonBar)  {
      buttonBar = (daiButtonBar)_daiButtonBar;
    }
    public JMenuBar getDaiMenuBar()  {
      return daiMenuBar;
    }
    public void setDaiMenuBar(JMenuBar _daiMenuBar)  {
      daiMenuBar = (daiFrameMenuBar)_daiMenuBar;
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

//*************************************************************************
//              PROTECTED
//*************************************************************************
	protected Logger LOGGER;

	protected void processWindowEvent(WindowEvent e)
	{
		//Is the user closing the window?
		if (e.getID() == WindowEvent.WINDOW_CLOSING)
		{
//            doCloseWindow();
            //super.processWindowEvent(e);

		}
	}

//*************************************************************************
//              PRIVATE
//*************************************************************************
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

    void this_keyPressed(KeyEvent e) {
        //TBD
    }

    void this_keyTyped(KeyEvent e) {
        //TBD
    }

    void this_keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
//            doCloseWindow();
        }
    }

//    void doCloseWindow() {
//        if (isChecksPrinted) {
//            JOptionPane q1 = new JOptionPane();
//            //Did all the checks print correctly?
//            int q1Ret = q1.showConfirmDialog(this, "Did all checks print correctly?");
//
//            //All checks printed OK.
//            if (q1Ret == JOptionPane.YES_OPTION) {
//                //All Checks printed great.  Set the Flags in the PayVoucher
//                //so we don't try to print the checks again.
//                _checkPrintRptPanel.updatePayVoucherCheckPrintFlag();
//
//            //Some checks had problems.
//            } else if (q1Ret == JOptionPane.NO_OPTION) {
//                //What was the last good check number printed?
//                UserInputDialog q2 = new UserInputDialog(CONTAINER, "Enter Value",
//                                        "Please enter the last good check number printed.",
//                                        UserInputDialog.INT_INPUT_TYPE);
//                String lastGoodCheckNum = q2.getValueEntered();
//                System.out.println(lastGoodCheckNum);
//
//                if (q2.getUserButtonChoice() == UserInputDialog.OK_OPTION) {
//                } else {
//                }
//
//            //User pressed Cancel
//            } else {
//                //Do nothing and leave the window here.
//                JOptionPane q3 = new JOptionPane();
//
//                return;
//            }
//        }
//
//        this.dispose();
//    }
    void verifyChecksPrintedWindow() {
        if (isChecksPrinted) {
            JOptionPane q1 = new JOptionPane();
            //Did all the checks print correctly?
            int q1Ret = q1.showConfirmDialog(this, "Did all checks print correctly?");

            //All checks printed OK.
            if (q1Ret == JOptionPane.YES_OPTION) {
                //All Checks printed great.  Set the Flags in the PayVoucher
                //so we don't try to print the checks again.
                _checkPrintRptPanel.updatePayVoucherCheckPrintFlag();
                _checkPrintRptPanel.updateItemGrid();

            //Some checks had problems.
            } else if (q1Ret == JOptionPane.NO_OPTION) {
                //What was the last good check number printed?
                UserInputDialog q2 = new UserInputDialog(CONTAINER, "Enter Value",
                                        "Please enter the last good check number printed.",
                                        UserInputDialog.INT_INPUT_TYPE);
                String lastGoodCheckNum = q2.getValueEntered();
                System.out.println(lastGoodCheckNum);

                if (q2.getUserButtonChoice() == UserInputDialog.OK_OPTION) {
                  _checkPrintRptPanel.setEndingCheckNum(lastGoodCheckNum);
                  _checkPrintRptPanel.updatePayVoucherCheckPrintFlag();
                  _checkPrintRptPanel.updateItemGrid();

                } else {
                }

            //User pressed Cancel
            } else {
                //Do nothing and leave the window here.
                JOptionPane q3 = new JOptionPane();

                return;
            }
        }

//        this.dispose();
    }
    public int refresh()
	{
	        _checkPrintRptPanel.updateItemGrid();
		return 0;
	}

  public void daiInternalFrame_keyEvent(KeyEvent e) {}
}
