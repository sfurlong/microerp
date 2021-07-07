
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.awt.event.FocusEvent;

import javax.swing.JFrame;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.cash_receiptObj;
import dai.shared.businessObjs.locationObj;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiPayMethodsComboBox;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;


public class ReceiptPanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();
    daiTextArea textAreaControl_note1 = new daiTextArea();

	daiLabel daiLabel_id = new daiLabel("Id:");
    daiLabel daiLabel_note1 = new daiBeans.daiLabel("Note1:");

    daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();
    GroupBox groupBox_billInfo = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();
    daiTextField daiTextField_invoiceNum = new daiTextField();
    daiLabel daiLabel_invoiceNum = new daiLabel();
    daiTextField daiTextField_checkNum = new daiTextField();
    daiTextField daiTextField_acctRcvId = new daiTextField();
    daiTextField daiTextField_acctRcvName = new daiTextField();
    daiLabel daiLabel_checkNum = new daiLabel();
    daiLabel daiLabel_depositAcctId = new daiLabel();
    daiPayMethodsComboBox daiComboBox_payMethod = new daiPayMethodsComboBox();
    daiLabel daiLabel_payMethod = new daiLabel();
    GroupBox groupBox_payRelated = new GroupBox();
    XYLayout xYLayout3 = new XYLayout();
    daiLabel daiLabel_payAmt = new daiLabel();
    daiLabel daiLabel_custId = new daiLabel();
    daiTextField daiTextField_payAmt = new daiTextField();
    daiTextField daiTextField_custId = new daiTextField();
    daiTextField daiTextField_custName = new daiTextField();
    daiDateField daiDateField_dateRcvd = new daiDateField();
    daiLabel daiLabel_dateRcvd = new daiLabel();
    daiLabel daiLabel_acctRcvId = new daiLabel();
    daiTextField daiTextField_depositAcctId = new daiTextField();
    daiTextField daiTextField_depositAcctName = new daiTextField();


	public ReceiptPanel(JFrame container, daiFrame parentFrame, cash_receiptObj obj)
	{
		super(container, parentFrame, obj);

		try
		{
			jbInit();
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}


	void jbInit() throws Exception
	{
		setLayout(xYLayout2);
		xYLayout2.setHeight(368);
		xYLayout2.setWidth(578);

        daiLabel_note1.setText("Note:");

        this.setBackground(daiColors.PanelColor);
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                ID_TEXT_FIELD_focusLost(e);
            }
        });
        daiLabel_invoiceNum.setText("Invoice/Ref Num:");
        daiLabel_checkNum.setText("Check Num:");
        daiLabel_depositAcctId.setText("Deposit Account:");
        daiLabel_payMethod.setText("Pay Method:");
        daiLabel_payAmt.setText("Payment Amt:");
        daiLabel_custId.setText("Cust Id:");
        daiLabel_id.setText("Receipt Id:");
        daiDateField_dateRcvd.setText("daiDateField1");
        daiLabel_dateRcvd.setText("Date Received:");
        daiLabel_acctRcvId.setText("Accts Receivable:");

        groupBox_billInfo.setLayout(xYLayout1);
        groupBox_billInfo.setLabel("Payee Info");
        groupBox_billInfo.add(daiTextField_invoiceNum, new XYConstraints(84, 5, 122, -1));
        groupBox_billInfo.add(daiLabel_invoiceNum, new XYConstraints(-3, 5, -1, -1));
        groupBox_billInfo.add(daiTextField_custId, new XYConstraints(84, 34, 122, -1));
        groupBox_billInfo.add(daiLabel_custId, new XYConstraints(42, 35, -1, -1));
        groupBox_billInfo.add(daiTextField_custName, new XYConstraints(209, 34, 224, -1));
        groupBox_billInfo.add(daiLabel_note1, new XYConstraints(53, 62, -1, -1));
        groupBox_billInfo.add(textAreaControl_note1, new XYConstraints(84, 60, 349, 67));

        groupBox_payRelated.setLayout(xYLayout3);
        groupBox_payRelated.setLabel("Payment Info");
        groupBox_payRelated.add(daiLabel_payAmt, new XYConstraints(17, 1, -1, -1));
        groupBox_payRelated.add(daiLabel_payMethod, new XYConstraints(24, 28, -1, -1));
        groupBox_payRelated.add(daiLabel_acctRcvId, new XYConstraints(-5, 58, -1, -1));
        groupBox_payRelated.add(daiTextField_payAmt, new XYConstraints(84, 0, -1, -1));
        groupBox_payRelated.add(daiDateField_dateRcvd, new XYConstraints(273, 0, -1, -1));
        groupBox_payRelated.add(daiComboBox_payMethod, new XYConstraints(84, 26, 114, -1));
        groupBox_payRelated.add(daiLabel_checkNum, new XYConstraints(210, 28, -1, -1));
        groupBox_payRelated.add(daiTextField_checkNum, new XYConstraints(273, 26, -1, -1));
        groupBox_payRelated.add(daiLabel_depositAcctId, new XYConstraints(-6, 82, 89, -1));
        groupBox_payRelated.add(daiTextField_acctRcvId, new XYConstraints(84, 57, -1, -1));
        groupBox_payRelated.add(daiTextField_acctRcvName, new XYConstraints(187, 57, 207, -1));
        groupBox_payRelated.add(daiTextField_depositAcctId, new XYConstraints(84, 81, -1, -1));
        groupBox_payRelated.add(daiTextField_depositAcctName, new XYConstraints(187, 80, 207, -1));
        groupBox_payRelated.add(daiLabel_dateRcvd, new XYConstraints(194, 1, -1, -1));

        this.add(userIdDateCreatedPanel, new XYConstraints(333, 7, -1, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(102, 24, 104, -1));
        this.add(groupBox_billInfo, new XYConstraints(34, 62, -1, 160));
        this.add(daiLabel_id, new XYConstraints(30, 25, -1, -1));
        this.add(groupBox_payRelated, new XYConstraints(34, 222, 467, 137));
	}

    protected BusinessObject getNewBusinessObjInstance()
    {
        locationObj obj = new locationObj();
        locationObj tempObj = (locationObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
        obj.set_locality(locationObj.getObjLocality());

        return obj;
    }

	public int refresh()
	{

		//Call the base class method first.
		super.refresh();

		//Enable the ID text field.
		ID_TEXT_FIELD.setDisabled(false);

		return 0;
	}

	public int query(String id)
	{
		//Call the base class query then do our extended logic.
		super.query(id);

		//Disable the Trans ID text field.
		ID_TEXT_FIELD.setDisabled(true);
		return 0;
	}

        //override the base class method, no insert for this panel
        public int persistPanelData()
	{

                try  {
                  update_BusinessObj();
                  csShipmentAdapterFactory  shipmentAdapterFactory = csShipmentAdapterFactory.getInstance();
                  csShipmentAdapter         shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();

                  shipmentAdapter.updateCashReceipts_totalPaid(_sessionMeta.getClientServerSecurity(),
                                                                    (cash_receiptObj)BUSINESS_OBJ);
                }
                catch(Exception e)  {
                  LOGGER.logError(CONTAINER, "Could not save Cash Receipt.\n" + e.getLocalizedMessage());
			e.printStackTrace();
                }

                super.persistPanelData();
		return 0;
	}

        //override the base class method
	public int delete()
	{
		try  {
                  update_BusinessObj();
                  csShipmentAdapterFactory  shipmentAdapterFactory = csShipmentAdapterFactory.getInstance();
                  csShipmentAdapter         shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();

                  shipmentAdapter.deleteCashReceipt_updateTotalPaid(_sessionMeta.getClientServerSecurity(),
                                                                    ((cash_receiptObj)BUSINESS_OBJ).get_id());
                }
                catch(Exception e)  {
                  LOGGER.logError(CONTAINER, "Could not delete Cash Receipt.\n" + e.getLocalizedMessage());
			e.printStackTrace();
                }

                super.delete();
		return 0;
	}

	protected void update_UI(BusinessObject bobj)
	{
		cash_receiptObj obj = (cash_receiptObj)bobj;

		ID_TEXT_FIELD.setText(obj.get_id());
        userIdDateCreatedPanel.setUserId(obj.get_created_by());
        userIdDateCreatedPanel.setDateCreated(obj.get_date_created());

        daiDateField_dateRcvd.setText(obj.get_date_received());
        daiTextField_checkNum.setText(obj.get_check_num());
        daiTextField_custId.setText(obj.get_cust_id());
        daiTextField_custName.setText(obj.get_cust_name());
        daiTextField_invoiceNum.setText(obj.get_shipment_id());
        daiTextField_payAmt.setText(obj.get_payment_amt());
        textAreaControl_note1.setText(obj.get_note());
        daiTextField_acctRcvId.setText(obj.get_receivable_acct_id());
        daiTextField_acctRcvName.setText(obj.get_receivable_acct_name());
        daiTextField_depositAcctId.setText(obj.get_deposit_acct_id());
        daiTextField_depositAcctName.setText(obj.get_deposit_acct_name());
        daiComboBox_payMethod.setText(obj.get_payment_method());

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		cash_receiptObj obj = (cash_receiptObj)BUSINESS_OBJ;

		ID_TEXT_FIELD.setText(obj.get_id());
        obj.set_created_by(userIdDateCreatedPanel.getUserId());
        obj.set_date_created(userIdDateCreatedPanel.getDateCreated());

        obj.set_date_received(daiDateField_dateRcvd.getText());
        obj.set_check_num(daiTextField_checkNum.getText());
        obj.set_cust_id(daiTextField_custId.getText());
        obj.set_cust_name(daiTextField_custName.getText());
        obj.set_shipment_id(daiTextField_invoiceNum.getText());
        obj.set_payment_amt(daiTextField_payAmt.getText());
        obj.set_note(textAreaControl_note1.getText());
        obj.set_receivable_acct_id(daiTextField_acctRcvId.getText());
        obj.set_receivable_acct_name(daiTextField_acctRcvName.getText());
        obj.set_deposit_acct_id(daiTextField_depositAcctId.getText());
        obj.set_deposit_acct_name(daiTextField_depositAcctName.getText());
        obj.set_payment_method(daiComboBox_payMethod.getText());

		BUSINESS_OBJ = obj;
	}

    void ID_TEXT_FIELD_focusLost(FocusEvent e) {
        if (e.isTemporary()) return;

		String id = ID_TEXT_FIELD.getText();
		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
	    	CONTAINER_FRAME.callBackInsertNewId(id);
    		//Disable the Trans ID text field.
	   		ID_TEXT_FIELD.setDisabled(true);
		}
    }
}

