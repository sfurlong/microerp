
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.accountObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiLabel;
import daiBeans.daiTextField;

public class BillReceiptAccountsPanel extends daiWizardPanel
{
    XYLayout xYLayout1 = new XYLayout();
    Logger _logger;
    SessionMetaData _sessionMeta;

    default_accountsObj _defaultAcctsObj;

	daiTextField fieldControl_acctsPayableId = new daiTextField();
	daiTextField fieldControl_acctsPayableName = new daiTextField();
	daiTextField fieldControl_expenseAcctId = new daiTextField();
	daiTextField fieldControl_expenseAcctName = new daiTextField();

	daiLabel daiLabel_acctsPayableId = new daiLabel("Accounts Payable:");
	daiLabel daiLabel_expenseAcctId = new daiLabel("Expense Account:");

    boolean firstTimeThrough = false;
    daiLabel daiLabel_acctSourceMsg1 = new daiLabel();
    daiLabel daiLabel_acctSourceMsg2 = new daiLabel();

    public BillReceiptAccountsPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            _logger.logError(null, this.getClass().getName()+": Could not initialize\n"+e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        _logger = _logger.getInstance();
        _sessionMeta = _sessionMeta.getInstance();

        xYLayout1.setHeight(265);
        xYLayout1.setWidth(567);

        this.setLayout(xYLayout1);
	    fieldControl_acctsPayableName.setDisabled(true);
	    fieldControl_expenseAcctName.setDisabled(true);
	    fieldControl_acctsPayableId.setDisabled(true);
	    fieldControl_expenseAcctId.setDisabled(true);
        daiLabel_acctsPayableId.setHREFstyle(true);
        daiLabel_acctsPayableId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_acctsPayableId_daiAction(e);
            }
        });
        daiLabel_expenseAcctId.setHREFstyle(true);
        daiLabel_expenseAcctId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_expenseAcctId_daiAction(e);
            }
        });
        daiLabel_acctSourceMsg1.setText("* This Acct Defaults to the value specified in the Financials Module.");
        daiLabel_acctSourceMsg1.setHorizontalAlignment(SwingConstants.LEFT);
        daiLabel_acctSourceMsg2.setText("* This Acct Defaults to the Primary Acct specified in the Vendor Module." +
    "Entry/Update Window");
        daiLabel_acctSourceMsg2.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(daiLabel_expenseAcctId, new XYConstraints(53, 130, -1, -1));
        this.add(daiLabel_acctsPayableId, new XYConstraints(51, 70, -1, -1));
        this.add(fieldControl_acctsPayableId, new XYConstraints(151, 68, -1, -1));
        this.add(fieldControl_expenseAcctId, new XYConstraints(150, 129, -1, -1));
        this.add(fieldControl_acctsPayableName, new XYConstraints(256, 68, 251, -1));
        this.add(fieldControl_expenseAcctName, new XYConstraints(256, 129, 251, -1));
        this.add(daiLabel_acctSourceMsg1, new XYConstraints(50, 93, 329, -1));
        this.add(daiLabel_acctSourceMsg2, new XYConstraints(50, 155, 420, -1));
    }

    public boolean panelDataIsValid()
    {
        String expenseAcct = fieldControl_expenseAcctId.getText();
        String payableAcct = fieldControl_acctsPayableId.getText();

        if (payableAcct == null || payableAcct.length() == 0){
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(this  ,
                                      "Please enter a valid Accounts Payable.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        if (expenseAcct == null || expenseAcct.length() == 0){
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(this  ,
                                      "Please enter a valid Expense Account.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        return true;
    }

    public void setExpenseAcct(String acctNum, String acctName)
    {
        fieldControl_expenseAcctId.setText(acctNum);
   		fieldControl_expenseAcctName.setText(acctName);
    }

    public String getPayableAcctId() {
        return fieldControl_acctsPayableId.getText();
    }
    public String getPayableAcctName() {
        return fieldControl_acctsPayableName.getText();
    }
    public String getExpenseAcctId() {
        return fieldControl_expenseAcctId.getText();
    }
    public String getExpenseAcctName() {
        return fieldControl_expenseAcctName.getText();
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        if (!firstTimeThrough) {
            getDefaultAccountData();
        }
        return true;
    }

    private void getDefaultAccountData()
    {
        csDBAdapterFactory dbAdapterFactory = null;
        csDBAdapter      dbAdapter = null;
        dbAdapterFactory = dbAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();

        String exp = "id = '" + default_accountsObj.SINGLETON_ID + "'" +
                    " and locality = '" + default_accountsObj.getObjLocality() + "'";

        try {
            Vector vect = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                                                    new default_accountsObj(),
                                                    exp);
            _defaultAcctsObj = (default_accountsObj)vect.elementAt(0);

            //Fill in the fields
    		fieldControl_acctsPayableId.setText(_defaultAcctsObj.get_accts_payable_id());
	    	fieldControl_acctsPayableName.setText(_defaultAcctsObj.get_accts_payable_name());
        } catch (Exception e) {
            _logger.logError(null, this.getClass().getName()+"::getDefaultAccountData failure\n"+e.getLocalizedMessage());
        }
    }

    private void daiLabel_acctsPayableId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_acctsPayableId.getText();
        String name = fieldControl_acctsPayableName.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, id, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, name, "Acct Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_acctsPayableId.setText(chosenObj.get_id());
            fieldControl_acctsPayableName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    private void daiLabel_expenseAcctId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_expenseAcctId.getText();
        String name = fieldControl_expenseAcctName.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, id, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, name, "Acct Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_expenseAcctId.setText(chosenObj.get_id());
            fieldControl_expenseAcctName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }
}
