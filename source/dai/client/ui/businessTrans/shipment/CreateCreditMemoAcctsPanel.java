
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.util.Vector;

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

public class CreateCreditMemoAcctsPanel extends daiWizardPanel
{
    XYLayout xYLayout1 = new XYLayout();
    Logger _logger;
    SessionMetaData _sessionMeta;

    default_accountsObj _defaultAcctsObj;

	daiTextField fieldControl_acctsReceivableId = new daiTextField();
	daiTextField fieldControl_acctsReceivableName = new daiTextField();
	daiTextField fieldControl_salesTaxId = new daiTextField();
	daiTextField fieldControl_salesTaxName = new daiTextField();
	daiTextField fieldControl_shippingId = new daiTextField();
	daiTextField fieldControl_shippingName = new daiTextField();

	daiLabel daiLabel_acctsReceivableId = new daiLabel("Accounts Receivable:");
	daiLabel daiLabel_salesTaxId = new daiLabel("Sales Tax Acct:");
	daiLabel daiLabel_shippingId = new daiLabel("Shipping(Out) Acct:");

    boolean firstTimeThrough = false;

    public CreateCreditMemoAcctsPanel()
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

        this.setLayout(xYLayout1);
	    fieldControl_acctsReceivableName.setDisabled(true);
	    fieldControl_salesTaxName.setDisabled(true);
	    fieldControl_shippingName.setDisabled(true);
	    fieldControl_acctsReceivableId.setDisabled(true);
	    fieldControl_salesTaxId.setDisabled(true);
	    fieldControl_shippingId.setDisabled(true);
        daiLabel_acctsReceivableId.setHREFstyle(true);
        daiLabel_acctsReceivableId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_acctsReceivableId_daiAction(e);
            }
        });
        daiLabel_salesTaxId.setHREFstyle(true);
        daiLabel_salesTaxId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_salesTaxId_daiAction(e);
            }
        });
        daiLabel_shippingId.setHREFstyle(true);
        daiLabel_shippingId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_shippingId_daiAction(e);
            }
        });
        this.add(daiLabel_acctsReceivableId, new XYConstraints(55, 70, -1, -1));
        this.add(daiLabel_salesTaxId, new XYConstraints(84, 130, -1, -1));
        this.add(daiLabel_shippingId, new XYConstraints(66, 189, -1, -1));
        this.add(fieldControl_salesTaxId, new XYConstraints(164, 129, 132, -1));
        this.add(fieldControl_shippingId, new XYConstraints(164, 189, 132, -1));
        this.add(fieldControl_acctsReceivableId, new XYConstraints(164, 69, 132, -1));
        this.add(fieldControl_acctsReceivableName, new XYConstraints(299, 69, 251, -1));
        this.add(fieldControl_shippingName, new XYConstraints(299, 189, 251, -1));
        this.add(fieldControl_salesTaxName, new XYConstraints(299, 128, 251, -1));
    }

    public boolean panelDataIsValid()
    {
        return false;
    }

    public String getAR_acctId() {
        return fieldControl_acctsReceivableId.getText();
    }

    public String getTax_acctId() {
        return fieldControl_salesTaxId.getText();
    }

    public String getShipping_acctId() {
        return fieldControl_shippingId.getText();
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
    		fieldControl_acctsReceivableId.setText(_defaultAcctsObj.get_accts_receivable_id());
	    	fieldControl_acctsReceivableName.setText(_defaultAcctsObj.get_accts_receivable_name());
		    fieldControl_salesTaxId.setText(_defaultAcctsObj.get_sales_tax_payable_id());
    		fieldControl_salesTaxName.setText(_defaultAcctsObj.get_sales_tax_payable_name());
	    	fieldControl_shippingId.setText(_defaultAcctsObj.get_shipping_out_id());
		    fieldControl_shippingName.setText(_defaultAcctsObj.get_shipping_out_name());
        } catch (Exception e) {
            _logger.logError(null, this.getClass().getName()+"::getDefaultAccountData failure\n"+e.getLocalizedMessage());
        }
    }

    void daiLabel_acctsReceivableId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_acctsReceivableId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Account Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_acctsReceivableId.setText(chosenObj.get_id());
            fieldControl_acctsReceivableName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    void daiLabel_salesTaxId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_salesTaxId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Account Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_salesTaxId.setText(chosenObj.get_id());
            fieldControl_salesTaxName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    void daiLabel_shippingId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_shippingId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Account Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_shippingId.setText(chosenObj.get_id());
            fieldControl_shippingName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

}
