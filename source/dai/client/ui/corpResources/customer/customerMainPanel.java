
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.customer;


import java.awt.event.FocusEvent;

import javax.swing.JFrame;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.carrierObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.cmnSvcs.SessionMetaData;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiComboBox;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiMaskField;
import daiBeans.daiPayMethodsComboBox;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;


public class customerMainPanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();
	daiTextField fieldControl_name = new daiTextField();
    daiTextArea textAreaControl_note1 = new daiTextArea();
    daiTextField daiTextField_priority = new daiTextField();
	daiLabel daiLabel_id = new daiLabel("Id:");
	daiLabel daiLabel_name = new daiLabel("Name:");
    daiLabel daiLabel_aka = new daiLabel("A.K.A.:");
    daiLabel daiLabel_ccType = new daiLabel("CC Type:");
    daiLabel daiLabel_ccExpDate = new daiLabel("CC Exp Date:");
    daiLabel daiLabel_ccAcct = new daiLabel("CC Acct:");
    daiLabel daiLabel_payStatus = new daiLabel("Pay Status:");
    daiLabel daiLabel_codStatus = new daiLabel("COD Status:");
    daiLabel daiLabel_preferedCarrier = new daiLabel("Prfrd. Carrier:");
    daiLabel daiLabel_carrierAcct = new daiLabel("Carrier Acct:");
    daiLabel daiLabel_note1 = new daiBeans.daiLabel("Note1:");
    daiLabel daiLabel_priority = new daiBeans.daiLabel("Priority:");
    daiLabel daiLabel_firstOrdDate = new daiLabel("First Order Date:");
    daiLabel daiLabel_lastOrdDate = new daiLabel("Last Order Date:");
    daiLabel daiLabel_user1 = new daiLabel("SIC1:");
    daiLabel daiLabel_user2 = new daiLabel("SIC2:");
    daiLabel daiLabel_user3 = new daiLabel("SIC3:");

	XYLayout xYLayout1 = new XYLayout();
	XYLayout xYLayout3 = new XYLayout();

	SessionMetaData sessionMeta;

    daiUserIdDateCreatedPanel daiUserIdDateCreatedPanel = new daiUserIdDateCreatedPanel();
    XYLayout xYLayout4 = new XYLayout();
    XYLayout xYLayout5 = new XYLayout();
    daiTextField daiTextField_aka = new daiTextField();
    daiTextField daiTextField_preferedCarrier = new daiTextField();
    daiTextField daiTextField_carrierAcct = new daiTextField();
    daiComboBox daiComboBox_payStatus = new daiComboBox();
    daiComboBox  daiComboBox_codStatus = new daiComboBox();
    daiPayMethodsComboBox daiComboBox_ccType = new daiPayMethodsComboBox();
    daiTextField daiTextField_ccAcct = new daiTextField();
    daiMaskField daiMaskField_ccExpDate = new daiMaskField("##/##");
    daiDateField daiDateField_firstOrdDate = new daiDateField();
    daiDateField daiDateField_lastOrdDate = new daiDateField();
    daiTextField daiTextField_SIC1 = new daiTextField();
    daiTextField daiTextField_SIC2 = new daiTextField();
    daiTextField daiTextField_SIC3 = new daiTextField();

	public customerMainPanel(JFrame container, daiFrame parentFrame, customerObj obj)
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
        sessionMeta = SessionMetaData.getInstance();

		setLayout(xYLayout2);
		xYLayout2.setHeight(352);
		xYLayout2.setWidth(578);
		xYLayout3.setHeight(468);
		xYLayout3.setWidth(685);


        daiTextField_preferedCarrier.setToolTipText("Prefered Carrier");
        daiTextField_ccAcct.setToolTipText("Credit Card Account Num");

        daiLabel_preferedCarrier.setHREFstyle(true);
        daiLabel_preferedCarrier.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_preferedCarrier_daiAction(e);
            }
        });
        daiLabel_priority.setText("Cust Priority:");


        //Populate the paystatus combobox
        daiComboBox_payStatus.addItems(new String[]{"NO STATUS", "EXCELLENT", "GOOD", "AVERAGE", "SLOW", "POOR"});

        //Populate the CODstatus combobox
        daiComboBox_codStatus.addItems(new String[]{"N", "Y"});

        this.setBackground(daiColors.PanelColor);
        daiLabel_note1.setText("Note:");
        ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter()
        {

            public void focusLost(FocusEvent e)
            {
                ID_TEXT_FIELD_focusLost(e);
            }
        });
        this.add(daiLabel_name, new XYConstraints(51, 61, -1, -1));
        this.add(daiLabel_aka, new XYConstraints(46, 87, 35, -1));
        this.add(daiUserIdDateCreatedPanel, new XYConstraints(409, 6, -1, -1));
        this.add(daiLabel_id, new XYConstraints(70, 37, -1, -1));

        this.add(daiLabel_carrierAcct, new XYConstraints(252, 123, -1, -1));
        this.add(daiLabel_ccAcct, new XYConstraints(190, 191, -1, -1));
        this.add(daiLabel_payStatus, new XYConstraints(215, 154, -1, -1));
        this.add(daiLabel_ccExpDate, new XYConstraints(406, 190, -1, -1));
        this.add(daiLabel_codStatus, new XYConstraints(410, 154, -1, -1));
        this.add(daiLabel_preferedCarrier, new XYConstraints(13, 123, 68, -1));
        this.add(daiLabel_ccType, new XYConstraints(37, 190, -1, -1));
        this.add(daiLabel_priority, new XYConstraints(20, 154, -1, -1));
        this.add(ID_TEXT_FIELD, new XYConstraints(84, 36, 175, -1));
        this.add(fieldControl_name, new XYConstraints(84, 62, 280, -1));
        this.add(daiTextField_aka, new XYConstraints(84, 87, 280, -1));
        this.add(daiTextField_preferedCarrier, new XYConstraints(84, 122, 161, -1));
        this.add(daiTextField_carrierAcct, new XYConstraints(315, 122, 200, -1));
        this.add(daiTextField_priority, new XYConstraints(84, 153, -1, -1));
        this.add(daiComboBox_payStatus, new XYConstraints(271, 153, 102, -1));
        this.add(daiComboBox_codStatus, new XYConstraints(473, 153, 39, -1));
        this.add(daiComboBox_ccType, new XYConstraints(84, 187, 97, -1));
        this.add(daiTextField_ccAcct, new XYConstraints(239, 187, 157, -1));
        this.add(daiMaskField_ccExpDate, new XYConstraints(474, 187, 38, -1));
        this.add(daiDateField_firstOrdDate, new XYConstraints(84, 218, -1, -1));
        this.add(daiLabel_lastOrdDate, new XYConstraints(190, 220, -1, -1));
        this.add(daiLabel_firstOrdDate, new XYConstraints(1, 220, -1, -1));
        this.add(daiDateField_lastOrdDate, new XYConstraints(273, 220, -1, -1));
        this.add(daiLabel_user1, new XYConstraints(56, 247, -1, -1));
        this.add(daiTextField_SIC1, new XYConstraints(84, 246, 86, -1));
        this.add(daiTextField_SIC2, new XYConstraints(273, 246, 86, -1));
        this.add(daiLabel_user2, new XYConstraints(247, 247, -1, -1));
        this.add(daiLabel_user3, new XYConstraints(395, 247, -1, -1));
        this.add(daiTextField_SIC3, new XYConstraints(427, 246, 86, -1));
        this.add(textAreaControl_note1, new XYConstraints(82, 277, 432, 67));
        this.add(daiLabel_note1, new XYConstraints(56, 274, -1, -1));
	}

    public String getTransId() {
        return ID_TEXT_FIELD.getText();
    }

    protected BusinessObject getNewBusinessObjInstance()
    {
        customerObj obj = new customerObj();
        customerObj tempObj = (customerObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

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

	protected void update_UI(BusinessObject bobj)
	{
		customerObj obj = (customerObj)bobj;

		daiUserIdDateCreatedPanel.setUserId(obj.get_created_by());
		daiUserIdDateCreatedPanel.setDateCreated(obj.get_date_created());
		ID_TEXT_FIELD.setText(obj.get_id());
		fieldControl_name.setText(obj.get_name());
        daiTextField_preferedCarrier.setText(obj.get_prefered_carrier());
        daiTextField_carrierAcct.setText(obj.get_carrier_acct());
        daiTextField_priority.setText(obj.get_priority());
        daiComboBox_payStatus.setSelectedItem(obj.get_payment_status());
        daiComboBox_codStatus.setSelectedItem(obj.get_cod_status());
        daiComboBox_ccType.setSelectedItem(obj.get_credit_card_type());
        daiTextField_ccAcct.setText(obj.get_credit_car_num());
        daiMaskField_ccExpDate.setText(obj.get_credit_card_exp_date());
        textAreaControl_note1.setText(obj.get_note1());
        daiTextField_priority.setText(obj.get_priority());
        daiDateField_firstOrdDate.setText(obj.get_date_of_first_order());
        daiDateField_lastOrdDate.setText(obj.get_date_of_last_order());
        daiTextField_SIC1.setText(obj.get_user1());
        daiTextField_SIC2.setText(obj.get_user2());
        daiTextField_SIC3.setText(obj.get_user3());
        daiTextField_aka.setText(obj.get_also_known_as());

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		customerObj obj = (customerObj)BUSINESS_OBJ;

		obj.set_id(ID_TEXT_FIELD.getText());
		obj.set_name(fieldControl_name.getText());
        obj.set_prefered_carrier(daiTextField_preferedCarrier.getText());
        obj.set_carrier_acct(daiTextField_carrierAcct.getText());
        obj.set_priority(daiTextField_priority.getText());
        obj.set_payment_status((String)daiComboBox_payStatus.getSelectedItem());
        obj.set_cod_status((String)daiComboBox_codStatus.getSelectedItem());
        obj.set_credit_card_type((String)daiComboBox_ccType.getSelectedItem());
        obj.set_credit_car_num(daiTextField_ccAcct.getText());
        obj.set_credit_card_exp_date(daiMaskField_ccExpDate.getText());
        obj.set_note1(textAreaControl_note1.getText());
        obj.set_priority(daiTextField_priority.getText());
        obj.set_date_of_first_order(daiDateField_firstOrdDate.getText());
        obj.set_date_of_last_order(daiDateField_lastOrdDate.getText());
        obj.set_user1(daiTextField_SIC1.getText());
        obj.set_user2(daiTextField_SIC2.getText());
        obj.set_user3(daiTextField_SIC3.getText());
        obj.set_also_known_as(daiTextField_aka.getText());

		BUSINESS_OBJ = obj;
	}

    void ID_TEXT_FIELD_focusLost(FocusEvent e)
    {
        if (e.isTemporary()) return;
		String id = ID_TEXT_FIELD.getText();
		if (id != null && !ID_TEXT_FIELD.isDisabled())
		{
	    	CONTAINER_FRAME.callBackInsertNewId(id);
    		//Disable the Trans ID text field.
	   		ID_TEXT_FIELD.setDisabled(true);

            fieldControl_name.requestFocus();
		}
    }

    void daiLabel_preferedCarrier_daiAction(daiActionEvent e)
    {
		carrierObj tempObj = new carrierObj();
        String id = daiTextField_preferedCarrier.getText();

        DBAttributes attrib1 = new DBAttributes(carrierObj.ID, "Carrier Id", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1},
                                              null, " order by priority, id ");
		chooser.show();
        carrierObj chosenObj = (carrierObj)chooser.getChosenObj();
        if (chosenObj != null) {
            daiTextField_preferedCarrier.setText(chosenObj.get_id());
        }
    	chooser.dispose();

    }
}

