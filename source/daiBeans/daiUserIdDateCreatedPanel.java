
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package daiBeans;

import java.awt.Color;
import java.util.Calendar;

import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class daiUserIdDateCreatedPanel extends JPanel
{
    XYLayout xYLayout1 = new XYLayout();
    daiTextField textField_userId = new daiTextField();
    daiTextField textField_dateCreated = new daiTextField();
    daiLabel label_dateCreated = new daiLabel();
    daiLabel label_createdBy = new daiLabel();
    private String _userId;
    private String _dateCreated;



    public daiUserIdDateCreatedPanel()
    {
        try
        {
            jbInit();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        this.setLayout(xYLayout1);
        xYLayout1.setHeight(78);
        xYLayout1.setWidth(164);
        label_dateCreated.setText("Date Created:");
        label_createdBy.setText("Created By:");
        this.setOpaque(false);
        this.add(textField_userId, new XYConstraints(83, 5, 70, -1));
        this.add(textField_dateCreated, new XYConstraints(83, 25, 70, -1));
        this.add(label_createdBy, new XYConstraints(22, 7, -1, -1));
        this.add(label_dateCreated, new XYConstraints(13, 27, -1, -1));
        this.setBackground(Color.lightGray);

        textField_userId.setDisabled(true);
        textField_dateCreated.setDisabled(true);
    }



    public void setUserId(String newUserId)
    {
        _userId = newUserId;
        textField_userId.setText(_userId);
    }

    public String getUserId()
    {
        return _userId;
    }

    public void setDateCreated(String newDateCreated)
    {
        _dateCreated = newDateCreated;
        textField_dateCreated.setText(_dateCreated);
    }

    public void setDateCreated()
    {
    	Calendar now = Calendar.getInstance();

        _dateCreated = now.get(now.MONTH)+1 + "/" +
						 now.get(now.DAY_OF_MONTH) + "/" +
						 now.get(now.YEAR);
        textField_dateCreated.setText(_dateCreated);
    }

    public String getDateCreated()
    {
        return _dateCreated;
    }

}


