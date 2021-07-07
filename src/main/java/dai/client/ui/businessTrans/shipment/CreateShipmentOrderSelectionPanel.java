
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cust_orderObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csShipmentAdapter;
import dai.shared.csAdapters.csShipmentAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiButton;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiTextField;

public class CreateShipmentOrderSelectionPanel extends daiWizardPanel
{
    JCheckBox jCheckBox_custId = new JCheckBox();
    JCheckBox jCheckBox_shipDate = new JCheckBox();
    XYLayout xYLayout1 = new XYLayout();
    daiTextField daiTextField_custId = new daiTextField();
    daiDateField daiDateField1 = new daiDateField();
    daiDateField daiDateField2 = new daiDateField();
    JList jList_left = new JList();
    DefaultListModel listModel_right = new DefaultListModel();
    DefaultListModel listModel_left = new DefaultListModel();
    JList jList_right = new JList();
    daiButton button_moveRight = new daiButton();
    daiButton button_moveLeft = new daiButton();
    daiButton button_getOrdIds = new daiButton();
    JScrollPane jScrollPane_left = new JScrollPane();
    JScrollPane jScrollPane_right = new JScrollPane();

    Logger _logger;
    daiTextField daiTextField_custName = new daiTextField();
    daiLabel daiLabel_dateFrom = new daiLabel();
    daiLabel daiLabel_dateTo = new daiLabel();
    GroupBox groupBox_filterCriteria = new GroupBox();
    XYLayout xYLayout2 = new XYLayout();
    daiLabel daiLabel_custId = new daiLabel();
    JLabel jLabel_shipableOrds = new JLabel();
    JLabel jLabel_selectedOrds = new JLabel();

    DBRecSet _shipableOrders = null;
    int _selectedOrder = 0;

    public CreateShipmentOrderSelectionPanel()
    {
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
        _logger = _logger.getInstance();

        jCheckBox_custId.setOpaque(false);
        jCheckBox_custId.setText("Customer Id");
        jCheckBox_custId.setFont(new java.awt.Font("Dialog", 0, 11));
        jCheckBox_shipDate.setOpaque(false);
        jCheckBox_shipDate.setText("Ship Date");
        jCheckBox_shipDate.setFont(new java.awt.Font("Dialog", 0, 11));

        button_moveRight.setMnemonic(KeyEvent.VK_GREATER);
        button_moveRight.setText(">>>");
        button_moveRight.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                button_moveRight_actionPerformed(e);
            }
        });

        button_moveLeft.setMnemonic(KeyEvent.VK_LESS);
        button_moveLeft.setText("<<<");
        button_moveLeft.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                button_moveLeft_actionPerformed(e);
            }
        });

        button_getOrdIds.setMnemonic(KeyEvent.VK_G);
        button_getOrdIds.setHorizontalAlignment(SwingConstants.LEFT);
        button_getOrdIds.setHorizontalTextPosition(SwingConstants.LEFT);
        button_getOrdIds.setText("Get Orders");
        button_getOrdIds.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                button_getOrdIds_actionPerformed(e);
            }
        });

        jList_right.setModel(listModel_right);
        jList_left.setModel(listModel_left);

        daiLabel_dateFrom.setText("From:");
        daiLabel_dateTo.setText("To:");
        groupBox_filterCriteria.setLabel("Filtering Options");
        groupBox_filterCriteria.setLayout(xYLayout2);
        daiLabel_custId.setHREFstyle(true);
        daiLabel_custId.setText("Cust Id:");
        daiLabel_custId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_custId_daiAction(e);
            }
        });

        jLabel_shipableOrds.setText("Shippable Orders");
        jLabel_selectedOrds.setText("Orders To Ship");
        this.setLayout(xYLayout1);
        xYLayout1.setHeight(333);
        xYLayout1.setWidth(576);

        jScrollPane_right.getViewport().add(jList_right, null);
        jScrollPane_left.getViewport().add(jList_left, null);

        groupBox_filterCriteria.add(jCheckBox_shipDate, new XYConstraints(-3, 7, -1, -1));
        groupBox_filterCriteria.add(daiDateField1, new XYConstraints(125, 12, -1, -1));
        groupBox_filterCriteria.add(daiDateField2, new XYConstraints(251, 12, -1, -1));
        groupBox_filterCriteria.add(jCheckBox_custId, new XYConstraints(-4, 46, -1, -1));
        groupBox_filterCriteria.add(daiLabel_custId, new XYConstraints(84, 51, -1, -1));
        groupBox_filterCriteria.add(daiLabel_dateFrom, new XYConstraints(93, 14, -1, -1));
        groupBox_filterCriteria.add(daiTextField_custId, new XYConstraints(125, 47, -1, -1));
        groupBox_filterCriteria.add(daiTextField_custName, new XYConstraints(230, 47, 207, -1));
        groupBox_filterCriteria.add(daiLabel_dateTo, new XYConstraints(232, 14, -1, -1));

        this.add(jLabel_selectedOrds, new XYConstraints(391, 111, -1, -1));
        this.add(jLabel_shipableOrds, new XYConstraints(120, 111, -1, -1));
        this.add(button_getOrdIds, new XYConstraints(11, 4, -1, -1));
        this.add(groupBox_filterCriteria, new XYConstraints(112, 5, 466, 100));
        this.add(jScrollPane_left, new XYConstraints(100, 131, 153, 192));
        this.add(jScrollPane_right, new XYConstraints(356, 129, 153, 192));
        this.add(button_moveLeft, new XYConstraints(278, 209, -1, -1));
        this.add(button_moveRight, new XYConstraints(278, 182, -1, -1));
    }

    public boolean panelDataIsValid()
    {
        if (listModel_right.size() > 0) {
            return true;
        } else {
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(  this,
                                        "Please select an Order to Ship before continuing.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }

    public String[] getSelectedOrderIds()
    {
        String[] ret = new String[listModel_right.size()];

        for(int i=0; i<listModel_right.size(); i++) {
            ret[i] = (String)listModel_right.getElementAt(i);
        }

        return ret;
    }

    public void resetPanel()
    {
        jCheckBox_shipDate.setSelected(false);
        jCheckBox_custId.setSelected(false);
        daiDateField1.setText(null);
        daiTextField_custId.setText(null);
        daiTextField_custName.setText(null);
        daiDateField2.setText(null);
        listModel_left.clear();
        listModel_right.clear();
    }

    public boolean isSelectedOrderPrepaid() {
        String test = _shipableOrders.getRec(_selectedOrder).getAttribVal(cust_orderObj.IS_PREPAID);
        if (test != null && test.equals("Y")) {
            return true;
        } else {
            return false;
        }
    }

    private DBAttributes[] createFilterData()
    {
        DBAttributes[] filters = {null, null, null};

        if (jCheckBox_custId.isSelected()) {
            String txt = daiTextField_custId.getText();
            if (txt != null && txt.length() != 0) {
                DBAttributes attrib = new DBAttributes();
                attrib.setName("cust_order.customer_id");
                attrib.setValue(txt);
                filters[0] = attrib;
            }
        }
        return filters;
    }

    void daiLabel_custId_daiAction(daiActionEvent e)
    {
		customerObj tempObj = new customerObj();
        String id = daiTextField_custId.getText();
        String name = daiTextField_custName.getText();

        DBAttributes attribs1 = new DBAttributes(customerObj.ID, id, "Cust Id", 100);
        DBAttributes attribs2 = new DBAttributes(customerObj.NAME, name, "Customer Name", 200);
		DataChooser chooser = new DataChooser(null, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attribs1, attribs2},
                                              null, null);
		chooser.show();
        customerObj chosenObj = (customerObj)chooser.getChosenObj();
        if (chosenObj != null) {
            daiTextField_custId.setText(chosenObj.get_id());
            daiTextField_custName.setText(chosenObj.get_name());
        }
    	chooser.dispose();
    }

    void button_getOrdIds_actionPerformed(ActionEvent e)
    {
        //Clear the lists incase they have been previously populated.
        listModel_left.clear();;
        listModel_right.clear();

        //Adapter Factory for getting handles to the Client Server Adapters
        csDBAdapterFactory          dbAdapterFactory = null;
        csShipmentAdapterFactory    shipmentAdapterFactory = null;
        dbAdapterFactory        = dbAdapterFactory.getInstance();
        shipmentAdapterFactory  = shipmentAdapterFactory.getInstance();

        csDBAdapter         dbAdapter = null;
        csShipmentAdapter   shipmentAdapter = null;
        SessionMetaData     sessionMeta = null;

        dbAdapter = dbAdapterFactory.getDBAdapter();
        shipmentAdapter = shipmentAdapterFactory.getShipmentAdapter();
        sessionMeta = sessionMeta.getInstance();

        String tableName = new cust_orderObj().getTableName();
        try {
            _shipableOrders = shipmentAdapter.getShipableOrders(sessionMeta.getClientServerSecurity(),
                                                                cust_orderObj.getObjLocality(),
                                                                createFilterData());
            for (int i=0; i<_shipableOrders.getSize(); i++)
            {
                listModel_left.addElement(_shipableOrders.getRec(i).getAttribVal(cust_orderObj.ID));
            }

            if (_shipableOrders.getSize() == 0) {
                JOptionPane userDlg = new JOptionPane();
                userDlg.showMessageDialog(this  ,
                                        "No Shipable Orders For The Selected Criteria.",
                                        "Information",
                                        JOptionPane.INFORMATION_MESSAGE, null);
            }
        } catch (Exception ex) {
            _logger.logError(ex.getLocalizedMessage());
        }
    }

    void button_moveRight_actionPerformed(ActionEvent e)
    {
        if (jList_left.getSelectedValue() != null) {
            listModel_right.addElement(jList_left.getSelectedValue());
            _selectedOrder = jList_left.getSelectedIndex();
            listModel_left.removeElementAt(jList_left.getSelectedIndex());
        }
    }

    void button_moveLeft_actionPerformed(ActionEvent e)
    {
        listModel_left.insertElementAt(jList_right.getSelectedValue(),0);
        listModel_right.removeElementAt(jList_right.getSelectedIndex());
    }
}

