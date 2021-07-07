
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      DAI
//Description:  Beans

package daiBeans;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;

public class daiContactsPanel extends JPanel
{
    daiLabel daiLabel_name = new daiLabel("Name:");
    daiLabel daiLabel_title = new daiLabel("Title:");
    daiLabel daiLabel_officePhone = new daiLabel("Office Phone:");
    daiLabel daiLabel_otherPhone = new daiLabel("Other Phone:");
    daiLabel daiLabel_officeFax = new daiLabel("Office Fax:");
    daiLabel daiLabel_otherFax = new daiLabel("Other Fax:");
    daiLabel daiLabel_pager = new daiLabel("Pager:");
    daiLabel daiLabel_mobilePhone = new daiLabel("Mobile Phone:");
    daiLabel daiLabel_officeEmail = new daiLabel("Office email:");
    daiLabel daiLabel_otherEmail = new daiLabel("Other email:");
    daiLabel daiLabel_web = new daiLabel("Web:");
    daiLabel daiLabel_note = new daiLabel("Note:");

    daiTextField daiTextField_name = new daiTextField();
    daiTextField daiTextField_title = new daiTextField();
    daiMaskField daiMaskField_officePhone = new daiMaskField("(###) ###-#### Ext. ####");
    daiMaskField daiMaskField_officeFax = new daiMaskField("(###) ###-####");
    daiMaskField daiMaskField_mobilePhone = new daiMaskField("(###) ###-####");
    daiMaskField daiMaskField_pager = new daiMaskField("(###) ###-####");
    daiTextField daiTextField_officeEmail = new daiTextField();
    daiTextField daiTextField_web = new daiTextField();
    daiMaskField daiMaskField_otherPhone = new daiMaskField("(###) ###-#### Ext. ####");
    daiMaskField daiMaskField_otherFax = new daiMaskField("(###) ###-####");
    daiTextField daiTextField_otherEmail = new daiTextField();
    daiTextArea  daiTextArea_note = new daiTextArea();
    daiCheckBox  checkBox_isPrimary = new daiCheckBox("Is Primary Contact");

    GroupBox groupBox = new GroupBox();
    BorderLayout borderLayout1 = new BorderLayout();
    XYLayout xYLayout1 = new XYLayout();
    daiGridController _gridController;

    public daiContactsPanel()
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

    public void setGridController(daiGridController gc)
    {
        _gridController = gc;
        groupBox.add(_gridController, new XYConstraints(0, 0, 354, 40));
    }

    public String getName() {
        return daiTextField_name.getText();
    }
    public void setName(String t) {
        daiTextField_name.setText(t);
    }

    public String getTitle() {
        return daiTextField_title.getText();
    }
    public void setTitle(String t) {
        daiTextField_title.setText(t);
    }

    public String getOfficePhone() {
        return daiMaskField_officePhone.getText();
    }
    public void setOfficePhone(String t) {
        daiMaskField_officePhone.setText(t);
    }

    public String getOtherPhone() {
        return daiMaskField_otherPhone.getText();
    }
    public void setOtherPhone(String t) {
        daiMaskField_otherPhone.setText(t);
    }

    public String getOfficeFax() {
        return daiMaskField_officeFax.getText();
    }
    public void setOfficeFax(String t) {
        daiMaskField_officeFax.setText(t);
    }

    public String getOtherFax() {
        return daiMaskField_otherFax.getText();
    }
    public void setOtherFax(String t) {
        daiMaskField_otherFax.setText(t);
    }

    public String getPager() {
        return daiMaskField_pager.getText();
    }
    public void setPager(String t) {
        daiMaskField_pager.setText(t);
    }

    public String getMobilePhone() {
        return daiMaskField_mobilePhone.getText();
    }
    public void setMobilePhone(String t) {
        daiMaskField_mobilePhone.setText(t);
    }

    public String getOfficeEmail() {
        return daiTextField_officeEmail.getText();
    }
    public void setOfficeEmail(String t) {
        daiTextField_officeEmail.setText(t);
    }

    public String getOtherEmail() {
        return daiTextField_otherEmail.getText();
    }
    public void setOtherEmail(String t) {
        daiTextField_otherEmail.setText(t);
    }

    public String getWeb() {
        return daiTextField_web.getText();
    }
    public String getIsPrimary() {
        return checkBox_isPrimary.getValue();
    }
    public void setWeb(String t) {
        daiTextField_web.setText(t);
    }

    public String getNote() {
        return daiTextArea_note.getText();
    }
    public void setNote(String t) {
        daiTextArea_note.setText(t);
    }

    public void setIsPrimary(String t) {
        checkBox_isPrimary.setValue(t);
    }

    public void disableEntryFields(boolean flag)
    {
        daiTextField_name.setDisabled(flag);
        daiTextField_title.setDisabled(flag);
        daiMaskField_officePhone.setDisabled(flag);
        daiMaskField_officeFax.setDisabled(flag);
        daiMaskField_mobilePhone.setDisabled(flag);
        daiMaskField_pager.setDisabled(flag);
        daiTextField_officeEmail.setDisabled(flag);
        daiTextField_web.setDisabled(flag);
        daiMaskField_otherPhone.setDisabled(flag);
        daiMaskField_otherFax.setDisabled(flag);
        daiTextField_otherEmail.setDisabled(flag);
        daiTextArea_note.setDisabled(flag);
        checkBox_isPrimary.setDisabled(flag);
    }

    public void clearEntryFields() {
        //set all the entry fields to null
        daiTextField_name.setText(null);
        daiTextField_title.setText(null);
        daiMaskField_officePhone.setText(null);
        daiMaskField_officeFax.setText(null);
        daiMaskField_mobilePhone.setText(null);
        daiMaskField_pager.setText(null);
        daiTextField_officeEmail.setText(null);
        daiTextField_web.setText(null);
        daiMaskField_otherPhone.setText(null);
        daiMaskField_otherFax.setText(null);
        daiTextField_otherEmail.setText(null);
        daiTextArea_note.setText(null);
        checkBox_isPrimary.setValue(null);

        daiTextField_name.requestFocus();
    }

    private void jbInit() throws Exception
    {
        //Set the standard background color for DAI Entry Panels
        this.setBackground(daiColors.PanelColor);

        this.setLayout(borderLayout1);
        groupBox.setLabel("Contact Entry");
        groupBox.setLayout(xYLayout1);

        this.add(groupBox, BorderLayout.CENTER);

        groupBox.add(daiLabel_pager, new XYConstraints(281, 105, -1, -1));
        groupBox.add(daiLabel_officeFax, new XYConstraints(258, 81, -1, -1));
        groupBox.add(daiLabel_title, new XYConstraints(290, 56, -1, -1));
        groupBox.add(daiLabel_otherFax, new XYConstraints(261, 184, -1, -1));
        groupBox.add(daiLabel_name, new XYConstraints(30, 56, -1, -1));
        groupBox.add(daiLabel_officePhone, new XYConstraints(-6, 81, -1, -1));
        groupBox.add(daiLabel_mobilePhone, new XYConstraints(-6, 105, -1, -1));
        groupBox.add(daiLabel_officeEmail, new XYConstraints(0, 131, -1, -1));
        groupBox.add(daiLabel_web, new XYConstraints(27, 155, 33, -1));
        groupBox.add(daiLabel_otherPhone, new XYConstraints(-3, 184, -1, -1));
        groupBox.add(daiLabel_otherEmail, new XYConstraints(3, 209, -1, -1));
        groupBox.add(daiLabel_note, new XYConstraints(35, 236, -1, -1));

        groupBox.add(checkBox_isPrimary, new XYConstraints(315, 31, 165, -1));
        groupBox.add(daiTextField_name, new XYConstraints(67, 56, 173, -1));
        groupBox.add(daiTextField_title, new XYConstraints(315, 56, 173, -1));
        groupBox.add(daiMaskField_officePhone, new XYConstraints(67, 80, 173, -1));
        groupBox.add(daiMaskField_officeFax, new XYConstraints(315, 80, 110, -1));
        groupBox.add(daiMaskField_mobilePhone, new XYConstraints(67, 104, 110, -1));
        groupBox.add(daiMaskField_pager, new XYConstraints(315, 104, 110, -1));
        groupBox.add(daiTextField_officeEmail, new XYConstraints(67, 128, 250, -1));
        groupBox.add(daiTextField_web, new XYConstraints(67, 151, 250, -1));
        groupBox.add(daiMaskField_otherPhone, new XYConstraints(67, 183, 173, -1));
        groupBox.add(daiMaskField_otherFax, new XYConstraints(315, 183, 110, -1));
        groupBox.add(daiTextField_otherEmail, new XYConstraints(67, 207, 250, -1));
        groupBox.add(daiTextArea_note, new XYConstraints(67, 233, 383, 53));
    }
}