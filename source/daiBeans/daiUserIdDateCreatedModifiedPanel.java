package daiBeans;

import java.util.Calendar;

import com.borland.jbcl.layout.XYConstraints;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Lattice Group Inc.
 * @author
 * @version 1.0
 */

public class daiUserIdDateCreatedModifiedPanel extends daiUserIdDateCreatedPanel {
  daiTextField textField_dateModified = new daiTextField();
  daiLabel label_dateModified = new daiLabel();
  private String _dateModified;

  public daiUserIdDateCreatedModifiedPanel() {
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  void jbInit() throws Exception {
    label_dateModified.setText("Date Modified:");
    this.add(textField_dateModified, new XYConstraints(83, 45, 70, -1));
    this.add(label_dateModified, new XYConstraints(13, 47, -1, -1));
    textField_dateModified.setDisabled(true);
  }
  public void setDateModified(String newDateModified)
    {
        _dateModified = newDateModified;
        textField_dateModified.setText(_dateModified);
    }

    public void setDateModified()
    {
    	Calendar now = Calendar.getInstance();

        _dateModified = now.get(now.MONTH)+1 + "/" +
						 now.get(now.DAY_OF_MONTH) + "/" +
						 now.get(now.YEAR);
        textField_dateModified.setText(_dateModified);
    }

    public String getDateModified()
    {
        return _dateModified;
    }
}