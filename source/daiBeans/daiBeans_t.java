
//Title:      Your Product Name
//Version:    
//Copyright:  Copyright (c) 1998
//Author:     Stephen P. Furlong
//Company:    DAI
//Description:Beans
package daiBeans;

import java.awt.AWTPermission;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicLookAndFeel;

import com.altaprise.plaf.AltapriseLookAndFeel;

public class daiBeans_t
{
  boolean packFrame = false;

  AWTPermission awtPermission = new AWTPermission("accessEventQueue");

  //Construct the application
  public daiBeans_t()
  {
    Frame frame = new Frame();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame)
      frame.pack();
    else
      frame.validate();
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height)
      frameSize.height = screenSize.height;
    if (frameSize.width > screenSize.width)
      frameSize.width = screenSize.width;
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    frame.setVisible(true);
  }

  //Main method
  public static void main(String[] args)
  {
    try
    {
      //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      UIManager.setLookAndFeel(new AltapriseLookAndFeel());
//     UIManager.setLookAndFeel(new com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
//      UIManager.setLookAndFeel(new
//       com.sun.java.swing.plaf.motif.MotifLookAndFeel());
//      UIManager
//      		.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());

    }
    catch(Exception e)
    {
    }
    new daiBeans_t();
  }
}