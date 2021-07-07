
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.item;

import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.itemObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCheckBox;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;
import daiBeans.daiUserIdDateCreatedPanel;

public class InventoryAdjustSelectPanel extends daiWizardPanel
{
    SessionMetaData sessionMeta;
    csDBAdapterFactory dbAdapterFactory = null;
    csDBAdapter dbAdapter = null;

    XYLayout xYLayout1 = new XYLayout();
    daiTextField daiTextField_itemId = new daiTextField();
    daiTextArea daiTextArea_itemDesc = new daiTextArea();

    Logger _logger;
    daiNumField daiNumField_onHand = new daiNumField();
    daiNumField daiNumField_vendBack = new daiNumField();
    daiNumField daiNumField_custBack = new daiNumField();
    daiLabel daiLabel_itemDesc = new daiLabel("Customer Id:");
    daiLabel daiLabel_itemId = new daiLabel("Invoice#:");
    daiLabel daiLabel_onHand = new daiLabel("Original Inv. Amt:");
    daiLabel daiLabel_custBack = new daiLabel("Total Amt Due:");
    daiLabel daiLabel_vendBack = new daiLabel("Total Amt Paid:");
    daiLabel daiLabel_note = new daiLabel("Note:");

    InventoryAdjustFrame _CONTAINER_FRAME = null;
    daiNumField daiNumField_newVendBack = new daiNumField();
    daiNumField daiNumField_newOnHand = new daiNumField();
    daiNumField daiNumField_newCustBack = new daiNumField();

    daiCheckBox custCheckBox = new daiCheckBox();
    daiCheckBox vendCheckBox = new daiCheckBox();
    daiCheckBox onHandCheckBox = new daiCheckBox();
    daiTextArea textArea_note = new daiTextArea();
    daiLabel daiLabel_guidance = new daiLabel();
    daiUserIdDateCreatedPanel uIdDatePanel = new daiUserIdDateCreatedPanel();
    GroupBox groupBox_entry = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();
    daiLabel daiLabel1 = new daiLabel();

    JFrame CONTAINER;

    public InventoryAdjustSelectPanel(JFrame container)
    {
        CONTAINER = container;
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public InventoryAdjustSelectPanel(JFrame container, InventoryAdjustFrame containerFrame)
    {
        CONTAINER = container;
        _CONTAINER_FRAME = containerFrame;

        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        _logger = Logger.getInstance();
        sessionMeta = SessionMetaData.getInstance();
        dbAdapterFactory = csDBAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();

        xYLayout1.setHeight(329);
        xYLayout1.setWidth(540);

        daiTextArea_itemDesc.setDisabled(true);
        daiNumField_vendBack.setDisabled(true);
        daiNumField_custBack.setDisabled(true);
        daiNumField_onHand.setDisabled(true);
        daiLabel_itemId.setHREFstyle(true);
        daiLabel_itemId.setText("Item Id:");
        daiLabel_itemId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_itemId_daiAction(e);
            }
        });

        this.setLayout(xYLayout1);
        daiLabel_itemDesc.setText("Description:");
        daiLabel_onHand.setText("Current On Hand");
        daiLabel_custBack.setText("Current Cust Back Ord");
        daiLabel_vendBack.setText("Current Vend Back Ord");
        onHandCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        custCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        vendCheckBox.setHorizontalAlignment(SwingConstants.RIGHT);
        daiLabel_guidance.setText("*Click the above check box for each inventory value to update.");

        groupBox_entry.setLabel("Inventory Update");
        groupBox_entry.setLayout(xYLayout2);

        daiTextField_itemId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e)
            {
                if (!e.isTemporary()) {
                    daiTextField_id_keyPressed(e);
                }
            }
            });        
        
        daiLabel1.setText("*Use positive values to increase inventory and negative values to " +
    "reduce inventory");
        groupBox_entry.add(daiLabel_onHand, new XYConstraints(26, 2, -1, -1));
        groupBox_entry.add(daiNumField_onHand, new XYConstraints(26, 18, -1, -1));
        groupBox_entry.add(daiNumField_custBack, new XYConstraints(148, 18, -1, -1));
        groupBox_entry.add(daiLabel_custBack, new XYConstraints(148, 2, -1, -1));
        groupBox_entry.add(daiNumField_vendBack, new XYConstraints(280, 18, -1, -1));
        groupBox_entry.add(daiLabel_vendBack, new XYConstraints(280, 2, -1, -1));
        groupBox_entry.add(onHandCheckBox, new XYConstraints(4, 44, -1, -1));
        groupBox_entry.add(daiNumField_newOnHand, new XYConstraints(26, 44, -1, -1));
        groupBox_entry.add(custCheckBox, new XYConstraints(126, 44, -1, -1));
        groupBox_entry.add(daiNumField_newCustBack, new XYConstraints(148, 44, -1, -1));
        groupBox_entry.add(vendCheckBox, new XYConstraints(257, 44, -1, -1));
        groupBox_entry.add(daiNumField_newVendBack, new XYConstraints(280, 43, -1, -1));
        groupBox_entry.add(daiLabel_guidance, new XYConstraints(5, 67, -1, -1));
        groupBox_entry.add(daiLabel1, new XYConstraints(5, 81, -1, -1));
        groupBox_entry.add(textArea_note, new XYConstraints(28, 104, 377, 57));
        groupBox_entry.add(daiLabel_note, new XYConstraints(2, 105, -1, -1));

        this.add(uIdDatePanel, new XYConstraints(327, 8, -1, -1));
        this.add(daiLabel_itemId, new XYConstraints(62, 44, -1, -1));
        this.add(daiTextField_itemId, new XYConstraints(104, 45, -1, -1));
        this.add(daiTextArea_itemDesc, new XYConstraints(104, 70, 382, 55));
        this.add(daiLabel_itemDesc, new XYConstraints(38, 70, -1, -1));
        this.add(groupBox_entry, new XYConstraints(71, 133, 437, 192));

        uIdDatePanel.setDateCreated();
        uIdDatePanel.setUserId(sessionMeta.getUserId());
        daiTextField_itemId.requestFocus();
    }

    public boolean panelDataIsValid()
    {
        //Must enter a valid Shipment Id.
        if (daiTextField_itemId.getText() == null) {
            //Let the user know that the item Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Must enter an Item Id.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        if (onHandCheckBox.getValue().equals("N") &&
            custCheckBox.getValue().equals("N") &&
            vendCheckBox.getValue().equals("N")) {
            //Let the user know that the item Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Must select at least one check box.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        if (onHandCheckBox.getValue().equals("Y") && daiNumField_newOnHand.getText() == null) {
            JOptionPane.showMessageDialog(this  ,
                                      "Invalid Qty On Hand.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        if (custCheckBox.getValue().equals("Y") && daiNumField_newCustBack.getText() == null) {
            JOptionPane.showMessageDialog(this  ,
                                      "Invalid Qty Customer Back Order.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        if (vendCheckBox.getValue().equals("Y") && daiNumField_newVendBack.getText() == null) {
            JOptionPane.showMessageDialog(this  ,
                                      "Invalid Qty Vendor Back Order.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        return true;
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }

    public String getSelectedItemId()
    {
        return daiTextField_itemId.getText();
    }

    public String getQtyCustBack() {
        return daiNumField_newCustBack.getText();
    }

    public String getQtyVendBack() {
        return daiNumField_newVendBack.getText();
    }

    public String getQtyOnHand() {
        return daiNumField_newOnHand.getText();
    }

    public String getDoCustBack() {
        return custCheckBox.getValue();
    }

    public String getDoVendBack() {
        return vendCheckBox.getValue();
    }

    public String getDoOnHand() {
        return onHandCheckBox.getValue();
    }

    public String getNote() {
        return textArea_note.getText();
    }

    public void resetPanel() {
        daiTextArea_itemDesc.setText(null);
        daiTextField_itemId.setText(null);
        daiNumField_vendBack.setText(null);
        daiNumField_custBack.setText(null);
        daiNumField_onHand.setText(null);
        daiNumField_newOnHand.setText(null);
        daiNumField_newCustBack.setText(null);
        daiNumField_newVendBack.setText(null);
        textArea_note.setText(null);
        onHandCheckBox.setValue("N");
        vendCheckBox.setValue("N");
        custCheckBox.setValue("N");
        daiTextField_itemId.requestFocus();
    }

    void daiLabel_itemId_daiAction(daiActionEvent e)
    {
		itemObj tempObj = new itemObj();
        String id = daiTextField_itemId.getText();

        DBAttributes attribs1 = new DBAttributes(itemObj.ID, id, "Item Id", 200);
        DBAttributes attribs2 = new DBAttributes(itemObj.STANDARD_DESC, "", "Item Description", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
		chooser.show();
        itemObj chosenObj = (itemObj)chooser.getChosenObj();
        if (chosenObj != null) {
            populatePanel(chosenObj);
        }
    	chooser.dispose();
    }

    void daiTextField_id_keyPressed(FocusEvent e)
    {
		String id = daiTextField_itemId.getText();

		if (id != null)
		{
    		String exp = " locality = " + "'" + itemObj.getObjLocality() + "'" +
                        " and id = '" + id + "' ";
            try {
                Vector itemObjsVect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                                                new itemObj(),
                                                                exp);
                //Does the input Item exist?
                if (itemObjsVect.size() == 0) {
                    //Let the user know that the Item Does not exist.
                    JOptionPane.showMessageDialog(this  ,
                                        "Selected Item Does Not Exist.  Please try another.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);

                    resetPanel();
                } else {
                    itemObj obj = (itemObj)itemObjsVect.firstElement();
                    populatePanel(obj);
                }
            } catch (Exception ex) {
                _logger.logError(CONTAINER, "Can't Get Item Data.\n" + ex.getLocalizedMessage());
            }
		}
    }

    private void populatePanel(itemObj obj) {
        String s_onHand = obj.get_onhand_qty();
        String s_custBack = obj.get_backorder_to_cust_qty();
        String s_vendBack = obj.get_backorder_to_vendor_qty();
        if (s_onHand == null) s_onHand = "0";
        if (s_custBack == null) s_custBack = "0";
        if (s_vendBack == null) s_vendBack = "0";

        daiTextField_itemId.setText(obj.get_id());
        daiTextArea_itemDesc.setText(obj.get_standard_desc());
        daiNumField_vendBack.setText(s_vendBack);
        daiNumField_custBack.setText(s_custBack);
        daiNumField_onHand.setText(s_onHand);

        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText(": " + obj.get_id());
        }
        daiNumField_newOnHand.requestFocus();
    }
}
