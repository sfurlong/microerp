
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.util.Vector;

import javax.swing.JOptionPane;

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

public class CashReceiptDefaultAcctsPanel extends daiWizardPanel
{
    XYLayout xYLayout1 = new XYLayout();
    Logger _logger;
    SessionMetaData _sessionMeta;

    default_accountsObj _defaultAcctsObj;

	daiTextField fieldControl_acctsReceivableId = new daiTextField();
	daiTextField fieldControl_acctsReceivableName = new daiTextField();
	daiTextField fieldControl_depositAcctId = new daiTextField();
	daiTextField fieldControl_depositAcctName = new daiTextField();

	daiLabel daiLabel_acctsReceivableId = new daiLabel("Accounts Receivable:");
	daiLabel daiLabel_depositAcctId = new daiLabel("Deposit Account:");

    boolean firstTimeThrough = false;

    public CashReceiptDefaultAcctsPanel()
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
	    fieldControl_acctsReceivableName.setDisabled(true);
	    fieldControl_depositAcctName.setDisabled(true);
	    fieldControl_acctsReceivableId.setDisabled(true);
	    fieldControl_depositAcctId.setDisabled(true);
        daiLabel_acctsReceivableId.setHREFstyle(true);
        daiLabel_acctsReceivableId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_acctsReceivableId_daiAction(e);
            }
        });
        daiLabel_depositAcctId.setHREFstyle(true);
        daiLabel_depositAcctId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_depositAcctId_daiAction(e);
            }
        });
        this.add(fieldControl_acctsReceivableId, new XYConstraints(164, 69, 132, -1));
        this.add(fieldControl_acctsReceivableName, new XYConstraints(299, 69, 251, -1));
        this.add(fieldControl_depositAcctId, new XYConstraints(164, 129, 132, -1));
        this.add(fieldControl_depositAcctName, new XYConstraints(299, 128, 251, -1));
        this.add(daiLabel_depositAcctId, new XYConstraints(74, 130, -1, -1));
        this.add(daiLabel_acctsReceivableId, new XYConstraints(51, 70, -1, -1));
    }

    public boolean panelDataIsValid()
    {
        if (fieldControl_acctsReceivableId == null || fieldControl_depositAcctId == null) {
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(  this,
                                        "Must select a value for All Accounts.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        return true;
    }

    public default_accountsObj getDefaultAccountObj()
    {
        _defaultAcctsObj.set_accts_receivable_id(fieldControl_acctsReceivableId.getText());
        _defaultAcctsObj.set_accts_receivable_name(fieldControl_acctsReceivableName.getText());
        _defaultAcctsObj.set_undeposited_funds_id(fieldControl_depositAcctId.getText());
        _defaultAcctsObj.set_undeposited_funds_name(fieldControl_depositAcctName.getText());
        return _defaultAcctsObj;
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        if (!firstTimeThrough) {
            getDefaultAccountData();
        }
        return true;
    }

    public String getReceivableAcctId() {
        String ret = fieldControl_acctsReceivableId.getText();
        return ret;
    }

    public String getReceivableAcctName() {
        String ret = fieldControl_acctsReceivableName.getText();
        return ret;
    }

    public String getDepositAcctId() {
        String ret = fieldControl_depositAcctId.getText();
        return ret;
    }

    public String getDepositAcctName() {
        String ret = fieldControl_depositAcctName.getText();
        return ret;
    }

    private void getDefaultAccountData()
    {
        csDBAdapterFactory dbAdapterFactory = null;
        dbAdapterFactory = dbAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

        String exp = "id = '" + default_accountsObj.SINGLETON_ID + "'" +
                    " and locality = '" + default_accountsObj.getObjLocality() + "'";

        try {
            Vector vect = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                                                    new default_accountsObj(),
                                                    exp);
            _defaultAcctsObj = (default_accountsObj)vect.elementAt(0);

            //Fill in the fields
    		fieldControl_acctsReceivableId.setText(_defaultAcctsObj.get_accts_receivable_id());
	    	fieldControl_acctsReceivableName.setText(_defaultAcctsObj.get_accts_receivable_name());
		    fieldControl_depositAcctId.setText(_defaultAcctsObj.get_checking_id());
    		fieldControl_depositAcctName.setText(_defaultAcctsObj.get_checking_name());
        } catch (Exception e) {
			e.printStackTrace();
            String msg = this.getClass().getName() + "::getDefaultAccountData failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            _logger.logError(null, msg);
        }
    }

    void daiLabel_acctsReceivableId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_acctsReceivableId.getText();
		String exp = accountObj.ACCOUNT_TYPE+"='"+accountObj.ACCT_TYPE_ACCT_REC+"'";

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              exp, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_acctsReceivableId.setText(chosenObj.get_id());
            fieldControl_acctsReceivableName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    void daiLabel_depositAcctId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_depositAcctId.getText();
		String exp = accountObj.ACCOUNT_TYPE+"='"+accountObj.ACCT_TYPE_BANK+"'";


        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              exp, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_depositAcctId.setText(chosenObj.get_id());
            fieldControl_depositAcctName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }
}
