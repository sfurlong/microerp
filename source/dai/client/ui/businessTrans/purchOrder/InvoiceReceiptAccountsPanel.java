
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

public class InvoiceReceiptAccountsPanel extends daiWizardPanel
{
    XYLayout xYLayout1 = new XYLayout();
    Logger _logger;
    SessionMetaData _sessionMeta;

    default_accountsObj _defaultAcctsObj;

	daiTextField fieldControl_acctsPayableId = new daiTextField();
	daiTextField fieldControl_acctsPayableName = new daiTextField();
	daiTextField fieldControl_shipAcctId = new daiTextField();
	daiTextField fieldControl_shipAcctName = new daiTextField();

	daiLabel daiLabel_acctsPayableId = new daiLabel("Accounts Payable:");
	daiLabel daiLabel_shipAcctId = new daiLabel("Shipping Account:");

    boolean firstTimeThrough = false;
    daiLabel daiLabel_acctSourceMsg = new daiLabel();
    daiLabel daiLabel1 = new daiLabel();
    daiTextField daiTextField_cogsAcctName = new daiTextField();
    daiLabel daiLabel_cogs = new daiLabel();
    daiTextField daiTextField_cogsAcctId = new daiTextField();
    daiLabel daiLabel3 = new daiLabel();
    daiLabel daiLabel2 = new daiLabel();

    public InvoiceReceiptAccountsPanel()
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
	    fieldControl_shipAcctName.setDisabled(true);
	    fieldControl_acctsPayableName.setDisabled(true);
	    fieldControl_acctsPayableId.setDisabled(true);
	    fieldControl_shipAcctId.setDisabled(true);
        daiTextField_cogsAcctId.setDisabled(true);
        daiTextField_cogsAcctName.setDisabled(true);

        daiLabel_acctsPayableId.setHREFstyle(true);
        daiLabel_acctsPayableId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_acctsPayableId_daiAction(e);
            }
        });
        daiLabel_shipAcctId.setHREFstyle(true);
        daiLabel_shipAcctId.setText("Shipping In Account:");
        daiLabel_shipAcctId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_shipAcctId_daiAction(e);
            }
        });

        daiLabel_cogs.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_cogsAcctId_daiAction(e);
            }
        });

        daiLabel_acctSourceMsg.setText("* Defaults from the Financial Acct Defaults Module");
        daiLabel_acctSourceMsg.setHorizontalAlignment(SwingConstants.LEFT);
        daiLabel1.setText("* Defaults from the Financial Acct Defaults Module");
        daiLabel1.setHorizontalAlignment(SwingConstants.LEFT);
        daiTextField_cogsAcctName.setText("");
        daiLabel_cogs.setHREFstyle(true);
        daiLabel_cogs.setText("Cost Of Goods Sold:");
        daiTextField_cogsAcctId.setText("");
        daiLabel3.setText("* Defaults from the Financial Acct Defaults Module");
        daiLabel3.setHorizontalAlignment(SwingConstants.LEFT);
        daiLabel2.setText("** This acct is ONLY used to update the Expense Acct in the Payment " +
    "Voucher");
        daiLabel2.setHorizontalAlignment(SwingConstants.LEFT);
        this.add(daiLabel_acctsPayableId, new XYConstraints(53, 70, -1, -1));
        this.add(fieldControl_acctsPayableId, new XYConstraints(151, 68, -1, -1));
        this.add(fieldControl_acctsPayableName, new XYConstraints(256, 68, 251, -1));
        this.add(daiLabel_shipAcctId, new XYConstraints(45, 128, -1, -1));
        this.add(fieldControl_shipAcctId, new XYConstraints(151, 126, -1, -1));
        this.add(fieldControl_shipAcctName, new XYConstraints(256, 126, 251, -1));
        this.add(daiLabel_cogs, new XYConstraints(45, 186, -1, -1));
        this.add(daiTextField_cogsAcctId, new XYConstraints(151, 184, -1, -1));
        this.add(daiTextField_cogsAcctName, new XYConstraints(256, 184, 251, -1));
        this.add(daiLabel1, new XYConstraints(52, 146, 409, -1));
        this.add(daiLabel_acctSourceMsg, new XYConstraints(52, 88, 442, -1));
        this.add(daiLabel3, new XYConstraints(52, 205, 329, -1));
        this.add(daiLabel2, new XYConstraints(52, 220, -1, -1));
    }

    public boolean panelDataIsValid()
    {
        String payableAcct = fieldControl_acctsPayableId.getText();

        if (payableAcct == null || payableAcct.length() == 0){
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(this  ,
                                      "Please enter a valid Accounts Payable.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        return true;
    }

    public void setExpenseAcct(String acctNum, String acctName)
    {
        _defaultAcctsObj.set_accts_payable_id(fieldControl_acctsPayableId.getText());
        _defaultAcctsObj.set_accts_payable_name(fieldControl_acctsPayableName.getText());
        _defaultAcctsObj.set_shipping_in_id(fieldControl_shipAcctId.getText());
        _defaultAcctsObj.set_shipping_in_name(fieldControl_shipAcctName.getText());
    }

    public String getPayableAcctId() {
        return fieldControl_acctsPayableId.getText();
    }
    public String getPayableAcctName() {
        return fieldControl_acctsPayableName.getText();
    }
    public String getShipAcctId() {
        return fieldControl_shipAcctId.getText();
    }
    public String getShipAcctName() {
        return fieldControl_shipAcctName.getText();
    }
    public String getCOGSAcctId() {
        return daiTextField_cogsAcctId.getText();
    }
    public String getCOGSAcctName() {
        return daiTextField_cogsAcctName.getText();
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
    		fieldControl_shipAcctId.setText(_defaultAcctsObj.get_shipping_in_id());
	    	fieldControl_shipAcctName.setText(_defaultAcctsObj.get_shipping_in_name());
            daiTextField_cogsAcctId.setText(_defaultAcctsObj.get_cost_goods_sold_id());
            daiTextField_cogsAcctName.setText(_defaultAcctsObj.get_cost_goods_sold_name());
        } catch (Exception e) {
            _logger.logError(null, this.getClass().getName()+"::getDefaultAccountData failure\n"+e.getLocalizedMessage());
        }
    }

    private void daiLabel_acctsPayableId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_acctsPayableId.getText();
		String exp = " locality = " + "'" + accountObj.getObjLocality() + "'";

        //Filter to only get PAYABLES accounts.
        exp = exp + " and " + accountObj.ACCOUNT_TYPE+"='"+accountObj.ACCT_TYPE_ACCT_PAY+"'";

        DBAttributes attrib1 = new DBAttributes(accountObj.DESCRIPTION, "Acct Id", 200);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Description", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              exp, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_acctsPayableId.setText(chosenObj.get_id());
            fieldControl_acctsPayableName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    private void daiLabel_shipAcctId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_shipAcctId.getText();
		String exp = " locality = " + "'" + accountObj.getObjLocality() + "'";

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Description", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_shipAcctId.setText(chosenObj.get_id());
            fieldControl_shipAcctName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    private void daiLabel_cogsAcctId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_shipAcctId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Description", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            daiTextField_cogsAcctId.setText(chosenObj.get_id());
            daiTextField_cogsAcctName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }
}
