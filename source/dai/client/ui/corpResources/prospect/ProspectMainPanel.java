//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.prospect;


import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.prospectObj;
import dai.shared.businessObjs.prospect_refered_by_idsObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;
import daiBeans.daiCheckBox;
import daiBeans.daiDBComboBox;
import daiBeans.daiDataModifiedEvent;
import daiBeans.daiDataModifiedListener;
import daiBeans.daiLabel;
import daiBeans.daiMaskField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;


public class ProspectMainPanel extends daiHeaderPanel
{
	XYLayout xYLayout2 = new XYLayout();

	daiDBComboBox dbCombo_refBy
    	= new daiDBComboBox(new prospect_refered_by_idsObj(),
						" locality = '" + prospect_refered_by_idsObj.getObjLocality()+"'");
	daiTextArea textAreaControl_note1 = new daiTextArea();
	daiLabel daiLabel_id = new daiLabel("Id:");
	daiLabel daiLabel_note1 = new daiBeans.daiLabel("Note1:");

	XYLayout xYLayout1 = new XYLayout();
	XYLayout xYLayout3 = new XYLayout();

	SessionMetaData sessionMeta;
    daiTextField textField_custId = new daiTextField();
	daiUserIdDateCreatedPanel userIdDateCreatedPanel = new daiUserIdDateCreatedPanel();
	XYLayout xYLayout4 = new XYLayout();
	XYLayout xYLayout5 = new XYLayout();
	GroupBox groupBox1 = new GroupBox();
	daiLabel daiLabel_firstName = new daiLabel();
	XYLayout xYLayout6 = new XYLayout();
	daiTextField daiTextField_firstName = new daiTextField();
	daiLabel daiLabel_lastName = new daiLabel();
	daiTextField daiTextField_lastName = new daiTextField();

	daiMaskField daiTextField_phone = new daiMaskField("(###) ###-#### Ext. ####");
	daiLabel daiLabel_phone = new daiLabel();
	daiMaskField daiTextField_fax = new daiMaskField("(###) ###-####");
	daiLabel daiLabel_fax = new daiLabel();
	daiTextField daiTextField_email = new daiTextField();
	daiLabel daiLabel_email = new daiLabel();
	daiTextField daiTextField_web = new daiTextField();
	daiLabel daiLabel_web = new daiLabel();
	daiLabel daiLabel_refBy = new daiLabel();
	daiCheckBox checkBox_sendPrint = new daiCheckBox("Send Catalog:");
	daiCheckBox checkBox_sendCD = new daiCheckBox("Send CD:");
	daiCheckBox checkBox_isCust = new daiCheckBox("Is Customer:");
    CompNamePopup compNamePopup = null;
    daiLabel daiLabel_custId = new daiLabel();
    GroupBox groupBox_cust = new GroupBox();
    XYLayout xYLayout7 = new XYLayout();

	public ProspectMainPanel(JFrame container, daiFrame parentFrame, prospectObj obj)
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
		sessionMeta = sessionMeta.getInstance();

        compNamePopup = new CompNamePopup(CONTAINER);

		setLayout(xYLayout2);
		xYLayout2.setHeight(357);
		xYLayout2.setWidth(578);
		xYLayout3.setHeight(468);
		xYLayout3.setWidth(685);

		this.setBackground(daiColors.PanelColor);
		daiLabel_note1.setText("Note:");
		ID_TEXT_FIELD.addFocusListener(new java.awt.event.FocusAdapter(){
									   public void focusLost(FocusEvent e){
									   ID_TEXT_FIELD_focusLost(e);}});
		groupBox1.setLabel("Contact Info");
		groupBox1.setLayout(xYLayout6);
		daiLabel_firstName.setText("First Name:");
		daiLabel_lastName.setText("Last Name:");
		daiLabel_phone.setText("Phone#:");
		daiLabel_fax.setText("Fax:");
		daiLabel_email.setText("eMail:");
		daiLabel_web.setText("Web:");

		dbCombo_refBy.setText("");
		daiLabel_refBy.setText("Refered By:");
		checkBox_isCust.setText("Is a Customer");
        checkBox_isCust.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e) {
                checkBox_isCust_dataModified(e);
            }
        });

		checkBox_isCust.setActionCommand("Is Customer");
		checkBox_sendPrint.setText("Send Catalog");
		checkBox_sendPrint.setActionCommand("Send Catalog");
		checkBox_sendCD.setText("Send CD");
		checkBox_sendCD.setActionCommand("Send CD");


        daiLabel_custId.setText("Customer Id:");
        groupBox_cust.setLabel("Customer Info");
        groupBox_cust.setLayout(xYLayout7);
        this.add(userIdDateCreatedPanel, new XYConstraints(409, 6, -1, -1));
		this.add(daiLabel_id, new XYConstraints(90, 27, -1, -1));
		this.add(ID_TEXT_FIELD, new XYConstraints(105, 26, 175, -1));
        this.add(compNamePopup, new XYConstraints(15, 53, -1, -1));
        this.add(groupBox1, new XYConstraints(39, 83, 499, 103));
        groupBox1.add(daiLabel_firstName, new XYConstraints(-4, 0, -1, -1));
        groupBox1.add(daiTextField_firstName, new XYConstraints(51, 0, 174, -1));
        groupBox1.add(daiLabel_lastName, new XYConstraints(229, 0, -1, -1));
        groupBox1.add(daiTextField_lastName, new XYConstraints(285, 0, 174, -1));
        groupBox1.add(daiTextField_phone, new XYConstraints(51, 25, 174, -1));
        groupBox1.add(daiLabel_phone, new XYConstraints(-4, 25, 54, -1));
        groupBox1.add(daiLabel_fax, new XYConstraints(247, 25, 36, -1));
        groupBox1.add(daiTextField_fax, new XYConstraints(285, 25, 174, -1));
        groupBox1.add(daiTextField_email, new XYConstraints(51, 50, 174, -1));
        groupBox1.add(daiLabel_email, new XYConstraints(-4, 52, 54, -1));
        groupBox1.add(daiTextField_web, new XYConstraints(285, 50, 174, -1));
        groupBox1.add(daiLabel_web, new XYConstraints(248, 52, 35, -1));
        this.add(groupBox_cust, new XYConstraints(42, 186, 500, 51));
        groupBox_cust.add(checkBox_isCust, new XYConstraints(97, 0, -1, -1));
        groupBox_cust.add(daiLabel_custId, new XYConstraints(194, 4, -1, -1));
        groupBox_cust.add(textField_custId, new XYConstraints(255, 2, -1, -1));
        this.add(dbCombo_refBy, new XYConstraints(145, 241, 121, -1));
        this.add(checkBox_sendPrint, new XYConstraints(357, 239, 97, -1));
        this.add(checkBox_sendCD, new XYConstraints(274, 239, 77, -1));
        this.add(daiLabel_refBy, new XYConstraints(66, 242, 73, -1));
        this.add(textAreaControl_note1, new XYConstraints(108, 274, 424, 67));
        this.add(daiLabel_note1, new XYConstraints(80, 271, -1, -1));
	}

	public String getTransId()
	{
		return ID_TEXT_FIELD.getText();
	}
    public String getFirstName() {
        return daiTextField_firstName.getText();
    }
    public String getLastName() {
        return daiTextField_lastName.getText();
    }
    public String getCompName() {
        return compNamePopup.getCompName();
    }
    public String getCustId() {
        return textField_custId.getText();
    }

    public void updateUserIdDateCreated() {
        userIdDateCreatedPanel.setUserId(sessionMeta.getUserId());
        userIdDateCreatedPanel.setDateCreated();
    }

	protected BusinessObject getNewBusinessObjInstance()
	{
		prospectObj obj = new prospectObj();
		prospectObj tempObj = (prospectObj)BUSINESS_OBJ;

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
		prospectObj obj = (prospectObj)bobj;

		userIdDateCreatedPanel.setUserId(obj.get_created_by());
		userIdDateCreatedPanel.setDateCreated(obj.get_date_created());
		ID_TEXT_FIELD.setText(obj.get_id());
		compNamePopup.setCompName(obj.get_company_name());
		daiTextField_fax.setText(obj.get_fax());
		daiTextField_firstName.setText(obj.get_first_name());
		daiTextField_lastName.setText(obj.get_last_name());
		daiTextField_phone.setText(obj.get_phone());
		dbCombo_refBy.setText(obj.get_refered_by());
		daiTextField_web.setText(obj.get_web());
		daiTextField_email.setText(obj.get_email());
        checkBox_isCust.setDisabled(true);
		checkBox_isCust.setValue(obj.get_is_customer());
        checkBox_isCust.setDisabled(false);
		checkBox_sendCD.setValue(obj.get_send_cd());
		checkBox_sendPrint.setValue(obj.get_send_print());
        textField_custId.setText(obj.get_customer_id());
		textAreaControl_note1.setText(obj.get_note1());

		BUSINESS_OBJ = obj;
	}

	protected void update_BusinessObj()
	{
		prospectObj obj = (prospectObj)BUSINESS_OBJ;

		obj.set_id(ID_TEXT_FIELD.getText());
		obj.set_company_name(compNamePopup.getCompName());
		obj.set_fax(daiTextField_fax.getText());
		obj.set_first_name(daiTextField_firstName.getText());
		obj.set_last_name(daiTextField_lastName.getText());
		obj.set_phone(daiTextField_phone.getText());
		obj.set_refered_by(dbCombo_refBy.getText());
		obj.set_web(daiTextField_web.getText());
		obj.set_email(daiTextField_email.getText());
		obj.set_note1(textAreaControl_note1.getText());
		obj.set_is_customer(checkBox_isCust.getValue());
		obj.set_send_cd(checkBox_sendCD.getValue());
		obj.set_send_print(checkBox_sendPrint.getValue());
        obj.set_customer_id(textField_custId.getText());
        obj.set_created_by(userIdDateCreatedPanel.getUserId());
        obj.set_date_created(userIdDateCreatedPanel.getDateCreated());

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

			compNamePopup.requestFocus();
		}
	}

    private void checkBox_isCust_dataModified(daiDataModifiedEvent e) {
        if (checkBox_isCust.isSelected()) {
            if (ID_TEXT_FIELD.getText() != null) {

                //Ask if we should create a new Cust Id.
                int userAction = JOptionPane.showConfirmDialog(this,
                    "Would you like to create a new entry in the Cusomter Master File for this Prospect?",
                    "Create Customer?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (userAction == JOptionPane.YES_OPTION) {
                    try {
                        if (textField_custId.getText() != null) {
                            //Make sure that if a custId was entered, that the Cust Id exists
                            //in the customer file.
                            Vector custIdVect = _dbAdapter.getAllIds(_sessionMeta.getClientServerSecurity(),
                                            customerObj.TABLE_NAME,
                                            customerObj.ID + "='" + textField_custId.getText() + "'");
                            if (custIdVect.size() ==0) {
                                JOptionPane.showMessageDialog(this, "The Customer ID: " +
                                    textField_custId.getText() + " does not exist.\n" +
                                    "Please enter a valid Customer Id or leave blank to generate a new Customer Entry.",
                                    "Bad Customer Id",
                                    JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                        csShipmentAdapter shipmentAdapter = csShipmentAdapterFactory.getInstance().getShipmentAdapter();
                        String pspectId = getTransId();
                        String custId = textField_custId.getText();
                        DBRec pspectData = new DBRec();
                        pspectData.addAttrib(new DBAttributes(prospectObj.ID, pspectId));
                        pspectData.addAttrib(new DBAttributes(prospectObj.CUSTOMER_ID, custId));
                        custId = shipmentAdapter.createCustFromProspect(sessionMeta.getClientServerSecurity(),
                                                            pspectData);
                        textField_custId.setText(custId);
                    } catch (Exception ex) {
            			ex.printStackTrace();
			            String msg = this.getClass().getName() + "::checkBox_isCust_dataModified failure." +
						    "\n"+ex.toString()+"\n"+ex.getLocalizedMessage();
            			LOGGER.logError(CONTAINER, msg);
                    }
                } else {
                    checkBox_isCust.setValue("N");
                }
            }
        }
    }
}

