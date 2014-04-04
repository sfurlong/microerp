
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

import dai.client.clientShared.ClientDataCache;
import dai.client.clientShared.daiColors;

public class daiAddressPanel extends JPanel
{
    BorderLayout borderLayout1 = new BorderLayout();
    GroupBox groupBox = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();

    daiLabel daiLabel_addr1 = new daiLabel("Addr1:");
    daiLabel daiLabel_addr2 = new daiLabel("Addr2:");
    daiLabel daiLabel_addr3 = new daiLabel("Addr3:");
    daiLabel daiLabel_city = new daiLabel("City:");
    daiLabel daiLabel_state = new daiLabel("State:");
    daiLabel daiLabel_zip = new daiLabel("Zip:");
    daiLabel daiLabel_country = new daiLabel("Country:");

    daiTextField daiTextField_addr1 = new daiTextField();
    daiTextField daiTextField_addr2 = new daiTextField();
    daiTextField daiTextField_addr3 = new daiTextField();
    daiTextField daiTextField_city = new daiTextField();
    daiTextField daiTextField_zip = new daiTextField();
    daiTextField daiTextField_countryName = new daiTextField();
    daiComboBox  daiComboBox_state;
    daiComboBox  daiComboBox_country;
    daiTextField daiTextField_attn = new daiTextField();
    daiLabel daiLabel_attn = new daiLabel();

    public daiAddressPanel()
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

    public daiAddressPanel(String groupBoxLabel)
    {
        groupBox.setLabel(groupBoxLabel);
        try
        {
            jbInit();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void hideAttnField() {
        daiLabel_attn.setVisible(false);
        daiTextField_attn.setVisible(false);
    }

    public String getAddr1() {
        return daiTextField_addr1.getText();
    }
    public void setAddr1(String t) {
        daiTextField_addr1.setText(t);
    }
    public String getAddr2() {
        return daiTextField_addr2.getText();
    }
    public void setAddr2(String t) {
        daiTextField_addr2.setText(t);
    }
    public String getAddr3() {
        return daiTextField_addr3.getText();
    }
    public void setAddr3(String t) {
        daiTextField_addr3.setText(t);
    }
    public String getAttn() {
        return daiTextField_attn.getText();
    }
    public void setAttn(String t) {
        daiTextField_attn.setText(t);
    }
    public String getCity() {
        return daiTextField_city.getText();
    }
    public void setCity(String t) {
        daiTextField_city.setText(t);
    }
    public String getStateCode() {
        return (String)daiComboBox_state.getSelectedItem();
    }
    public void setStateCode(String t) {
        daiComboBox_state.setSelectedItem(t);
    }
    public String getCountryCode() {
        return (String)daiComboBox_country.getSelectedItem();
    }
    public void setCountryCode(String t) {
        daiComboBox_country.setSelectedItem(t);
    }
    public String getCountryName() {
        return daiTextField_countryName.getText();
    }
    public void setCountryName(String t) {
        daiTextField_countryName.setText(t);
    }
    public String getZip() {
        return daiTextField_zip.getText();
    }
    public void setZip(String t) {
        daiTextField_zip.setText(t);
    }

    public void setGroupBoxTitle(String t)
    {
        groupBox.setLabel(t);
    }

    public void disableEntryFields(boolean flag)
    {
        daiTextField_addr1.setDisabled(flag);
        daiTextField_addr2.setDisabled(flag);
        daiTextField_addr3.setDisabled(flag);
        daiTextField_attn.setDisabled(flag);
        daiTextField_city.setDisabled(flag);
        daiTextField_countryName.setDisabled(flag);
        daiTextField_zip.setDisabled(flag);
        daiComboBox_country.setDisabled(flag);
        daiComboBox_state.setDisabled(flag);
    }

    public void clearEntryFields()
    {
        daiTextField_addr1.setText(null);
        daiTextField_addr2.setText(null);
        daiTextField_addr3.setText(null);
        daiTextField_attn.setText(null);
        daiTextField_city.setText(null);
        daiTextField_countryName.setText(null);
        daiTextField_zip.setText(null);
        daiComboBox_country.setText(null);
        daiComboBox_state.setText(null);
    }

    private void jbInit() throws Exception
    {
        //Set the standard background color for DAI Entry Panels
        this.setBackground(daiColors.PanelColor);
        
        groupBox.setLayout(xYLayout1);

        ClientDataCache clientDataCache = ClientDataCache.getInstance();

        //!!FIX Later
        //daiComboBox_state = clientDataCache.getStateComboBox();
        //daiComboBox_country = clientDataCache.getCountryComboBox();
        daiComboBox_state = new daiComboBox();
        daiComboBox_country = new daiComboBox();

        daiLabel_attn.setText("Attn:");
        groupBox.add(daiTextField_attn, new XYConstraints(45, 1, 234, -1));
        groupBox.add(daiLabel_attn, new XYConstraints(14, 2, -1, -1));
        groupBox.add(daiLabel_addr1, new XYConstraints(4, 27, -1, -1));
        groupBox.add(daiTextField_addr1, new XYConstraints(45, 26, 234, -1));
        groupBox.add(daiLabel_addr2, new XYConstraints(4, 52, -1, -1));
        groupBox.add(daiLabel_addr3, new XYConstraints(4, 77, -1, -1));
        groupBox.add(daiTextField_addr2, new XYConstraints(45, 52, 234, -1));
        groupBox.add(daiTextField_addr3, new XYConstraints(45, 77, 234, -1));
        groupBox.add(daiTextField_city, new XYConstraints(45, 102, 234, -1));
        groupBox.add(daiLabel_city, new XYConstraints(16, 102, -1, -1));
        groupBox.add(daiLabel_state, new XYConstraints(9, 130, -1, -1));
        groupBox.add(daiComboBox_state, new XYConstraints(45, 128, 47, -1));
        groupBox.add(daiLabel_zip, new XYConstraints(116, 131, -1, -1));
        groupBox.add(daiTextField_zip, new XYConstraints(139, 128, 87, -1));
        groupBox.add(daiComboBox_country, new XYConstraints(291, 128, 47, -1));
        groupBox.add(daiLabel_country, new XYConstraints(247, 130, -1, -1));

        this.setLayout(borderLayout1);
        this.add(groupBox, BorderLayout.CENTER);
    }
}