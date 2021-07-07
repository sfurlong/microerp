
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      DAI
//Description:  Beans

package daiBeans;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.accountObj;
import dai.shared.cmnSvcs.FinanceAcctsDataCache;

public class daiAcctIdNamePanel extends JPanel
{
    FinanceAcctsDataCache _acctsDataCache;

    BorderLayout borderLayout1 = new BorderLayout();

    daiTextField daiTextField_acctName = new daiTextField();
    daiComboBox  daiComboBox_acctId;
    daiLabel     daiLabel_acctId = new daiLabel("Acct Id:");
    XYLayout xYLayout1 = new XYLayout();

    public daiAcctIdNamePanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        //Set the standard background color for DAI Entry Panels
        this.setBackground(daiColors.PanelColor);
        this.setMaximumSize(new Dimension(390, 32));
        this.setMinimumSize(new Dimension(390, 32));
        this.setPreferredSize(new Dimension(390, 32));
        this.setLayout(xYLayout1);

        _acctsDataCache = FinanceAcctsDataCache.getInstance();
        daiComboBox_acctId = new daiComboBox(_acctsDataCache.getAcctNums());
        daiComboBox_acctId.insertItemAt(" ", 0);
        daiComboBox_acctId.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
                public void daiDataModified(daiDataModifiedEvent e)
                {
                    String t = daiComboBox_acctId.getText();
                    int selectedIndex = daiComboBox_acctId.getSelectedIndex();
                    if (t != null && t.trim().length() > 0 && selectedIndex != -1) {
                        String acctName = _acctsDataCache.getAcctName(selectedIndex-1);
                        daiTextField_acctName.setText(acctName);
                    } else {
                        daiTextField_acctName.setText(null);
                    }
                }
        });
        daiLabel_acctId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_acctId_daiAction(e);
            }
        });

        daiLabel_acctId.setHREFstyle(true);
        xYLayout1.setHeight(32);
        xYLayout1.setWidth(464);
        this.add(daiTextField_acctName, new XYConstraints(156, 5, 211, -1));
        this.add(daiLabel_acctId, new XYConstraints(6, 7, 40, -1));
        this.add(daiComboBox_acctId, new XYConstraints(51, 4, 101, -1));
    }

    public void setAcctIdLabel(String t) {
        daiLabel_acctId.setText(t);
    }

    public String getAcctId() {
        return daiComboBox_acctId.getText();
    }
    public String getAcctName() {
        return daiTextField_acctName.getText();
    }
    public void setAcctId(String t) {
        daiComboBox_acctId.setText(t);
    }
    public void setAcctName(String t) {
        daiTextField_acctName.setText(t);
    }

    public void setDisabled(boolean flag)
    {
        daiTextField_acctName.setDisabled(flag);
        daiComboBox_acctId.setDisabled(flag);
        daiLabel_acctId.setDisabled(flag);
    }

    void daiLabel_acctId_daiAction(daiActionEvent e)
    {
		accountObj obj = new accountObj();
        String id = daiComboBox_acctId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  obj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);

		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            daiComboBox_acctId.setText(chosenObj.get_id());
            daiTextField_acctName.setText(chosenObj.get_description());
        }
		chooser.dispose();
    }
}