
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.accountObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiPayMethodsComboBox;
import daiBeans.daiRadioButton;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;

public class PayBillsDateEntryPanel extends daiWizardPanel
{
    SessionMetaData sessionMeta;
    csDBAdapterFactory dbAdapterFactory = null;
    csDBAdapter dbAdapter = null;

    XYLayout xYLayout1 = new XYLayout();
    DefaultListModel listModel_right = new DefaultListModel();
    DefaultListModel listModel_left = new DefaultListModel();
    ButtonGroup billSelectGroup = new ButtonGroup();
    daiRadioButton radioButton_payAll = new daiRadioButton("Show All Bills");
    daiRadioButton radioButton_payDates = new daiRadioButton("Show Bills Due On or Before");
    daiPayMethodsComboBox comboBox_payMethod = new daiPayMethodsComboBox();
    daiTextField payFromAcctId = new daiTextField();
    daiTextField payFromAcctName = new daiTextField();

    Logger _logger;
    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();

    PayBillsFrame _CONTAINER_FRAME = null;
    JFrame CONTAINER = null;

    daiDateField daiDateField_dueDate = new daiDateField();
    daiLabel daiLabel_dueDate = new daiLabel();
    daiLabel daiLabel_payMethod = new daiLabel();
    daiLabel daiLabel_payAcct = new daiLabel();

    public PayBillsDateEntryPanel(JFrame container, PayBillsFrame containerFrame)
    {
        CONTAINER = container;
        _CONTAINER_FRAME = containerFrame;

        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        _logger = _logger.getInstance();
        sessionMeta = sessionMeta.getInstance();
        dbAdapterFactory = dbAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();

        xYLayout1.setHeight(274);
        xYLayout1.setWidth(567);

        userIdDateCreatedPanel.setDateCreated();
        userIdDateCreatedPanel.setUserId(sessionMeta.getUserId());

        payFromAcctId.setDisabled(true);
        payFromAcctName.setDisabled(true);

        daiDateField_dueDate.setDisabled(true);

        radioButton_payAll.setSelected(true);
        radioButton_payAll.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                radioButton_payAll_actionPerformed(e);
            }
        });
        radioButton_payDates.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                radioButton_payDates_actionPerformed(e);
            }
        });
        daiLabel_payMethod.setText("Pay Method:");
        daiLabel_payAcct.setHREFstyle(true);
        daiLabel_payAcct.setText("Payment Acct:");
        daiLabel_payAcct.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_payAcct_daiAction(e);
            }
        });

        billSelectGroup.add(radioButton_payAll);
        billSelectGroup.add(radioButton_payDates);

        this.setLayout(xYLayout1);
        this.add(userIdDateCreatedPanel, new XYConstraints(376, 9, -1, -1));
        this.add(daiLabel_dueDate, new XYConstraints(126, 163, -1, -1));
        this.add(radioButton_payAll, new XYConstraints(115, 63, 101, 27));
        this.add(radioButton_payDates, new XYConstraints(115, 90, 157, 27));
        this.add(daiDateField_dueDate, new XYConstraints(275, 96, -1, -1));
        this.add(daiLabel_payMethod, new XYConstraints(116, 140, -1, -1));
        this.add(comboBox_payMethod, new XYConstraints(178, 137, 139, 22));
        this.add(payFromAcctId, new XYConstraints(178, 172, 131, -1));
        this.add(payFromAcctName, new XYConstraints(313, 172, 214, -1));
        this.add(daiLabel_payAcct, new XYConstraints(105, 174, -1, -1));
    }
     public void resetPanel() {
        daiDateField_dueDate.setText(null);
        comboBox_payMethod.setText(null);
        payFromAcctId.setText(null);
        payFromAcctName.setText(null);

    }
    public boolean panelDataIsValid()
    {
        if (radioButton_payDates.isSelected() && daiDateField_dueDate.isNull()) {
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(this  ,
                                      "Please enter a valid Date Due.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        String payMethod = comboBox_payMethod.getText();
        if (payMethod == null || payMethod.trim().length() == 0) {
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(this  ,
                                      "Please Select a Payment Method.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        if (payFromAcctId.getText() == null || payFromAcctId.getText().trim().length() == 0) {
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(this  ,
                                      "Please Select Valid Payment Account.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        return true;
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }

    //Getter's
    public String getDueDate()
    {
        if (isPayAllSelected()) {
            return null;
        } else {
            return daiDateField_dueDate.getText();
        }
    }

    public String getPayMethod() {
        return comboBox_payMethod.getText();
    }

    public String getPayAcctId() {
        return payFromAcctId.getText();
    }

    public String getPayAcctName() {
        return payFromAcctName.getText();
    }

    public boolean isPayAllSelected() {
        if (radioButton_payAll.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    void radioButton_payAll_actionPerformed(ActionEvent e) {
        daiDateField_dueDate.setDisabled(true);
    }

    void radioButton_payDates_actionPerformed(ActionEvent e) {
        daiDateField_dueDate.setDisabled(false);
    }

    private void daiLabel_payAcct_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = payFromAcctId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct ID", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            payFromAcctId.setText(chosenObj.get_id());
            payFromAcctName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }
}


