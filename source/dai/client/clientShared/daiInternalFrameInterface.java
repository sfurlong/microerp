package dai.client.clientShared;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Lattice Group Inc.
 * @author
 * @version 1.0
 */

public interface daiInternalFrameInterface {
  JMenuBar getDaiMenuBar();
  JPanel getDaiStatusBar();
  void setDaiStatusBar(JPanel _daiStatusBar);
  void daiInternalFrame_actionPerformed(java.awt.event.ActionEvent e);
}