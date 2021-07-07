package dai.client.clientAppRoot;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JTabbedPane;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Lattice Group Inc.
 * @author
 * @version 1.0
 */

public class daiTabbedPane extends JTabbedPane {
  BorderLayout borderLayout1 = new BorderLayout();
  Vector vecDaiFrames = new Vector();

  public daiTabbedPane() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
  //  this.setLayout(borderLayout1);
  }

/*
  public void addTab(String title, Component component)  {
    JDesktopPane desk = new JDesktopPane();
    desk.add(component);
    super.addTab(title, desk);
    vecDaiFrames.addElement(component);
  }
  public void addTab(String title, Icon icon, Component component)  {
    JDesktopPane desk = new JDesktopPane();
    desk.add(component);
    super.addTab(title, icon, desk);
    vecDaiFrames.addElement(component);
  }
  public void addTab(String title, Icon icon, Component component, String tip)  {
    JDesktopPane desk = new JDesktopPane();
    desk.add(component);
    super.addTab(title, icon, desk, tip);
    vecDaiFrames.addElement(component);
  }
*/
}