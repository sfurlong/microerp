
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.sysAdmin;


import javax.swing.JFrame;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.accountObj;
import dai.shared.businessObjs.default_accountsObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiLabel;
import daiBeans.daiTextField;


public class DefaultAccountsPanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();
	daiTextField fieldControl_acctsReceivableId = new daiTextField();
	daiTextField fieldControl_acctsReceivableName = new daiTextField();
	daiTextField fieldControl_salesTaxId = new daiTextField();
	daiTextField fieldControl_salesTaxName = new daiTextField();
	daiTextField fieldControl_shippingInId = new daiTextField();
	daiTextField fieldControl_shippingInName = new daiTextField();
	daiTextField fieldControl_shippingOutId = new daiTextField();
	daiTextField fieldControl_shippingOutName = new daiTextField();
	daiTextField fieldControl_checkingId = new daiTextField();
	daiTextField fieldControl_checkingName = new daiTextField();
	daiTextField fieldControl_acctsPayableId = new daiTextField();
	daiTextField fieldControl_acctsPayableName = new daiTextField();
	daiTextField fieldControl_costGoodsSoldId = new daiTextField();
	daiTextField fieldControl_costGoodsSoldName = new daiTextField();
	daiTextField fieldControl_inventoryId = new daiTextField();
	daiTextField fieldControl_inventoryName = new daiTextField();

	daiLabel daiLabel_acctsReceivableId = new daiLabel("Accounts Receivable:");
	daiLabel daiLabel_salesTaxId = new daiLabel("Sales Tax Acct:");
	daiLabel daiLabel_shippingInId = new daiLabel("Shipping In Acct:");
	daiLabel daiLabel_shippingOutId = new daiLabel("Shipping Out Acct:");
    daiLabel daiLabel_checkingId = new daiLabel("Checking Acct:");
    daiLabel daiLabel_acctsPayableId = new daiLabel("Accounts Payable:");
    daiLabel daiLabel_costGoodsSoldId = new daiLabel("Cost of Goods Sold:");
    daiLabel daiLabel_inventory = new daiLabel("Inventory:");

	XYLayout xYLayout1 = new XYLayout();
	XYLayout xYLayout3 = new XYLayout();

	SessionMetaData sessionMeta;

    String _ID = "SINGLETON";

	public DefaultAccountsPanel(JFrame container, daiFrame parentFrame, default_accountsObj obj)
	{
		super(container, parentFrame, obj);

		try
		{
			jbInit();

            //There should only be one record in this table.
            //Lets get it now.
            query(_ID);

		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	void jbInit() throws Exception
	{
        sessionMeta = sessionMeta.getInstance();

		setLayout(xYLayout2);
		xYLayout2.setHeight(297);
		xYLayout2.setWidth(578);
		xYLayout3.setHeight(468);
		xYLayout3.setWidth(685);

        this.setBackground(daiColors.PanelColor);

	    fieldControl_acctsReceivableName.setDisabled(true);
	    fieldControl_salesTaxName.setDisabled(true);
	    fieldControl_shippingInName.setDisabled(true);
	    fieldControl_shippingOutName.setDisabled(true);
	    fieldControl_checkingName.setDisabled(true);
	    fieldControl_acctsPayableName.setDisabled(true);
	    fieldControl_costGoodsSoldName.setDisabled(true);
	    fieldControl_inventoryName.setDisabled(true);
        daiLabel_acctsReceivableId.setHREFstyle(true);
        daiLabel_acctsReceivableId.adddaiActionListener(new daiActionListener() {

            public void daiActionEvent(daiActionEvent e) {
                daiLabel_acctsReceivableId_daiAction(e);
            }
        });
        daiLabel_checkingId.setHREFstyle(true);
        daiLabel_checkingId.adddaiActionListener(new daiActionListener() {

            public void daiActionEvent(daiActionEvent e) {
                daiLabel_checkingId_daiAction(e);
            }
        });
        daiLabel_salesTaxId.setHREFstyle(true);
        daiLabel_salesTaxId.adddaiActionListener(new daiActionListener() {

            public void daiActionEvent(daiActionEvent e) {
                daiLabel_salesTaxId_daiAction(e);
            }
        });
        daiLabel_acctsPayableId.setHREFstyle(true);
        daiLabel_acctsPayableId.adddaiActionListener(new daiActionListener() {

            public void daiActionEvent(daiActionEvent e) {
                daiLabel_acctsPayableId_daiAction(e);
            }
        });
        daiLabel_shippingInId.setHREFstyle(true);
        daiLabel_shippingInId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_shippingInId_daiAction(e);
            }
        });
        daiLabel_shippingOutId.setHREFstyle(true);
        daiLabel_shippingOutId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_shippingOutId_daiAction(e);
            }
        });
        daiLabel_costGoodsSoldId.setHREFstyle(true);
        daiLabel_costGoodsSoldId.adddaiActionListener(new daiActionListener() {

            public void daiActionEvent(daiActionEvent e) {
                daiLabel_costGoodsSoldId_daiAction(e);
            }
        });
        daiLabel_inventory.setHREFstyle(true);
        daiLabel_inventory.adddaiActionListener(new daiActionListener() {

            public void daiActionEvent(daiActionEvent e) {
                daiLabel_inventory_daiAction(e);
            }
        });
        this.add(daiLabel_acctsReceivableId, new XYConstraints(55, 70, -1, -1));
        this.add(daiLabel_checkingId, new XYConstraints(85, 98, 76, -1));
        this.add(daiLabel_salesTaxId, new XYConstraints(84, 126, -1, -1));
        this.add(daiLabel_acctsPayableId, new XYConstraints(70, 154, -1, -1));
        this.add(daiLabel_shippingInId, new XYConstraints(80, 182, -1, -1));
        this.add(daiLabel_shippingOutId, new XYConstraints(71, 210, -1, -1));
        this.add(daiLabel_costGoodsSoldId, new XYConstraints(64, 238, -1, -1));
        this.add(daiLabel_inventory, new XYConstraints(113, 266, -1, -1));
        this.add(fieldControl_salesTaxId, new XYConstraints(164, 124, 132, -1));
        this.add(fieldControl_checkingId, new XYConstraints(164, 97, 132, -1));
        this.add(fieldControl_acctsPayableId, new XYConstraints(164, 152, 132, -1));
        this.add(fieldControl_acctsReceivableId, new XYConstraints(164, 69, 132, -1));
        this.add(fieldControl_acctsReceivableName, new XYConstraints(299, 69, 251, -1));
        this.add(fieldControl_checkingName, new XYConstraints(299, 97, 251, -1));
        this.add(fieldControl_salesTaxName, new XYConstraints(299, 124, 251, -1));
        this.add(fieldControl_acctsPayableName, new XYConstraints(299, 152, 251, -1));
        this.add(fieldControl_shippingInId, new XYConstraints(164, 180, 132, -1));
        this.add(fieldControl_shippingInName, new XYConstraints(299, 180, 251, -1));
        this.add(fieldControl_shippingOutId, new XYConstraints(164, 207, 132, -1));
        this.add(fieldControl_shippingOutName, new XYConstraints(299, 207, 251, -1));
        this.add(fieldControl_costGoodsSoldId, new XYConstraints(164, 235, 132, -1));
        this.add(fieldControl_costGoodsSoldName, new XYConstraints(299, 235, 251, -1));
        this.add(fieldControl_inventoryId, new XYConstraints(164, 263, 132, -1));
        this.add(fieldControl_inventoryName, new XYConstraints(299, 263, 251, -1));
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        default_accountsObj obj = new default_accountsObj();
        default_accountsObj tempObj = (default_accountsObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

        return obj;
    }

	public int refresh()
	{

		//Call the base class method first.
		super.refresh();

		return 0;
	}

	public int query(String id)
	{
		//Call the base class query then do our extended logic.
		super.query(id);

		return 0;
	}

	protected void update_UI(BusinessObject bobj)
	{
		default_accountsObj obj = (default_accountsObj)bobj;

		fieldControl_acctsReceivableId.setText(obj.get_accts_receivable_id());
		fieldControl_acctsReceivableName.setText(obj.get_accts_receivable_name());
		fieldControl_salesTaxId.setText(obj.get_sales_tax_payable_id());
		fieldControl_salesTaxName.setText(obj.get_sales_tax_payable_name());
		fieldControl_shippingInId.setText(obj.get_shipping_in_id());
		fieldControl_shippingInName.setText(obj.get_shipping_in_name());
		fieldControl_shippingOutId.setText(obj.get_shipping_out_id());
		fieldControl_shippingOutName.setText(obj.get_shipping_out_name());
		fieldControl_checkingId.setText(obj.get_checking_id());
		fieldControl_checkingName.setText(obj.get_checking_name());
		fieldControl_acctsPayableId.setText(obj.get_accts_payable_id());
		fieldControl_acctsPayableName.setText(obj.get_accts_payable_name());
		fieldControl_costGoodsSoldId.setText(obj.get_cost_goods_sold_id());
		fieldControl_costGoodsSoldName.setText(obj.get_cost_goods_sold_name());
		fieldControl_inventoryId.setText(obj.get_inventory_id());
		fieldControl_inventoryName.setText(obj.get_inventory_name());

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		default_accountsObj obj = (default_accountsObj)BUSINESS_OBJ;

        obj.set_accts_receivable_id(fieldControl_acctsReceivableId.getText());
        obj.set_accts_receivable_name(fieldControl_acctsReceivableName.getText());
        obj.set_sales_tax_payable_id(fieldControl_salesTaxId.getText());
        obj.set_sales_tax_payable_name(fieldControl_salesTaxName.getText());
        obj.set_shipping_in_id(fieldControl_shippingInId.getText());
        obj.set_shipping_in_name(fieldControl_shippingInName.getText());
        obj.set_shipping_out_id(fieldControl_shippingOutId.getText());
        obj.set_shipping_out_name(fieldControl_shippingOutName.getText());
        obj.set_checking_id(fieldControl_checkingId.getText());
        obj.set_checking_name(fieldControl_checkingName.getText());
        obj.set_accts_payable_id(fieldControl_acctsPayableId.getText());
        obj.set_accts_payable_name(fieldControl_acctsPayableName.getText());
        obj.set_cost_goods_sold_id(fieldControl_costGoodsSoldId.getText());
        obj.set_cost_goods_sold_name(fieldControl_costGoodsSoldName.getText());
        obj.set_inventory_id(fieldControl_inventoryId.getText());
        obj.set_inventory_name(fieldControl_inventoryName.getText());

        obj.set_id(_ID);

		BUSINESS_OBJ = obj;
	}

    void daiLabel_acctsReceivableId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_acctsReceivableId.getText();

        //Filter to only get the desired accounts.
        String exp = accountObj.ACCOUNT_TYPE+"='"+accountObj.ACCT_TYPE_ACCT_REC+"'";

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
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

    void daiLabel_checkingId_daiAction(daiActionEvent e) {
		accountObj tempObj = new accountObj();
        String id = fieldControl_checkingId.getText();

        //Filter to only get the desired accounts.
        String exp = accountObj.ACCOUNT_TYPE+"='"+accountObj.ACCT_TYPE_BANK+"'";

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              exp, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_checkingId.setText(chosenObj.get_id());
            fieldControl_checkingName.setText(chosenObj.get_description());
        }
    	chooser.dispose();

    }

    void daiLabel_salesTaxId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_salesTaxId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
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

    void daiLabel_acctsPayableId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_acctsPayableId.getText();

        //Filter to only get the desired accounts.
        String exp = accountObj.ACCOUNT_TYPE+"='"+accountObj.ACCT_TYPE_ACCT_PAY+"'";

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
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

    void daiLabel_shippingInId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_shippingInId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_shippingInId.setText(chosenObj.get_id());
            fieldControl_shippingInName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    void daiLabel_shippingOutId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_shippingOutId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_shippingOutId.setText(chosenObj.get_id());
            fieldControl_shippingOutName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    void daiLabel_costGoodsSoldId_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_acctsPayableId.getText();

        //Filter to only get the desired accounts.
        String exp = accountObj.ACCOUNT_TYPE+"='"+accountObj.ACCT_TYPE_COGS+"'";

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              exp, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_costGoodsSoldId.setText(chosenObj.get_id());
            fieldControl_costGoodsSoldName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }

    void daiLabel_inventory_daiAction(daiActionEvent e) {

		accountObj tempObj = new accountObj();
        String id = fieldControl_acctsPayableId.getText();

        DBAttributes attrib1 = new DBAttributes(accountObj.ID, "Acct Id", 100);
        DBAttributes attrib2 = new DBAttributes(accountObj.DESCRIPTION, "Acct Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        accountObj chosenObj = (accountObj)chooser.getChosenObj();
        if (chosenObj != null) {
            fieldControl_inventoryId.setText(chosenObj.get_id());
            fieldControl_inventoryName.setText(chosenObj.get_description());
        }
    	chooser.dispose();
    }
}


