
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.ClientDataCache;
import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.payment_voucherObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;
import daiBeans.daiGrid;
import daiBeans.daiGroupBox;
import daiBeans.daiLabel;
import daiBeans.daiNumField;

public class PayBillsSelectPanel extends daiWizardPanel
{

    daiGrid _grid = new daiGrid();
    SessionMetaData sessionMeta;
    csPurchOrderAdapterFactory purchOrdAdapterFactory;
    csPurchOrderAdapter purchOrdAdapter;
    ClientDataCache _clientDataCache;
    ButtonGroup radioGroup  = new ButtonGroup();

    Logger LOGGER;

    //Grid locations
    final int gridColPay            = 0;
   	final int gridColVendor         = 1;
    final int gridColDateDue        = 2;
    final int gridColVoucherNum     = 3;
    final int gridColRefNo          = 4;
	final int gridColAmtAlreadyPaid = 5;
    final int gridColAmtDue         = 6;
	final int gridColAmtToPay       = 7;
    final int gridColCheckNum       = 8;

    XYLayout xYLayout1 = new XYLayout();

    PayBillsFrame _containerFrame;
    JFrame CONTAINER = null;
    JButton jButton_toggleSelected = new JButton();
    JButton jButton_genCheckNums = new JButton();
    daiNumField daiNumField_startCheckNum = new daiNumField();
    JButton jButton_renumChecks = new JButton();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jLabel3 = new JLabel();
    daiLabel daiLabel_numBills = new daiLabel();

    public PayBillsSelectPanel(JFrame container, PayBillsFrame containerFrame)
    {
        CONTAINER = container;
        _containerFrame = containerFrame;

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
        purchOrdAdapterFactory = purchOrdAdapterFactory.getInstance();
        purchOrdAdapter = purchOrdAdapterFactory.getPurchOrderAdapter();
        sessionMeta = SessionMetaData.getInstance();
        LOGGER = Logger.getInstance();
        _clientDataCache = _clientDataCache.getInstance();


        _grid.createColumns(new int[]{_grid.CHECKBOX_COL_TYPE,
                                     _grid.CHAR_COL_TYPE,
                                     _grid.DATE_COL_TYPE,
                                     _grid.CHAR_COL_TYPE,
                                     _grid.CHAR_COL_TYPE,
                                     _grid.DOUBLE_COL_TYPE,
                                     _grid.DOUBLE_COL_TYPE,
                                     _grid.DOUBLE_COL_TYPE,
                                     _grid.CHAR_COL_TYPE}); //Check num needs to be
                                                            //char col type for formating
                                                            //purposes.
        _grid.setHeaderNames(new String[]{"Pay", "Vendor", "Date Due", "Voucher#",
                                          "PO/Bill Id.", "Amt Paid", "Amt. Due",
                                          "Amt. To Pay", "Check#"});
        //Disable most of the Grid entry fields
        _grid.setColEditable(gridColVoucherNum, false);
        _grid.setColEditable(gridColDateDue, false);
        _grid.setColEditable(gridColVendor, false);
        _grid.setColEditable(gridColRefNo, false);
        _grid.setColEditable(gridColAmtAlreadyPaid, false);
        _grid.setColEditable(gridColAmtDue, false);
        //Resize some of the Grid fields
        _grid.setColumnSize(gridColPay, 50);
        //Turn off row selection
        _grid.allowRowSelection(true);


        this.setLayout(xYLayout1);
        xYLayout1.setHeight(345);
        xYLayout1.setWidth(702);

        jButton_toggleSelected.setMargin(new Insets(2, 2, 2, 2));
        jButton_toggleSelected.setText("Toggle Selected");
        jButton_toggleSelected.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton_toggleSelected_actionPerformed(e);
            }
        });
        jButton_genCheckNums.setMargin(new Insets(2, 2, 2, 2));
        jButton_genCheckNums.setText("Gen Check Nums");
        jButton_genCheckNums.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton_genCheckNums_actionPerformed(e);
            }
        });
        jButton_renumChecks.setMargin(new Insets(2, 2, 2, 2));
        jButton_renumChecks.setText("Renum Checks");
        jButton_renumChecks.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                jButton_renumChecks_actionPerformed(e);
            }
        });
        jLabel1.setText("*Check/Uncheck Selected Rows");
        jLabel2.setText("*Generate Check#s Starting With This Check#:");
        jLabel3.setText("*Renumber Checks Starting With the Selected Row");
        daiLabel_numBills.setText("Total Number of Bills ");
        daiLabel_numBills.setFont(new java.awt.Font("Dialog", 2, 11));
        daiGroupBox optionPanel = new daiGroupBox("Check Printing Options");
        optionPanel.setLayout(new XYLayout());
        BoxLayout2 boxLayout = new BoxLayout2();
        boxLayout.setAxis(BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        _grid.setPreferredSize(new Dimension(589, 200));
        _grid.setMinimumSize(new Dimension(589, 200));
        optionPanel.setPreferredSize(new Dimension(580, 100));
        optionPanel.add(jButton_toggleSelected, new XYConstraints(5, 0, 109, -1));
        optionPanel.add(jButton_genCheckNums, new XYConstraints(5, 30, 109, -1));
        optionPanel.add(jButton_renumChecks, new XYConstraints(5, 57, 109, -1));
        optionPanel.add(jLabel1, new XYConstraints(119, 3, -1, -1));
        optionPanel.add(jLabel2, new XYConstraints(119, 35, -1, -1));
        optionPanel.add(jLabel3, new XYConstraints(119, 61, -1, -1));
        optionPanel.add(daiNumField_startCheckNum, new XYConstraints(350, 32, 50, -1));
        optionPanel.add(daiLabel_numBills, new XYConstraints(5, 80, -1, -1));
        this.add(_grid,null);
        this.add(optionPanel,null);
    }

    public boolean panelDataIsValid ()
    {
        boolean ret = false;
        Double  d_amtToPay = null;

        //Make sure the user selected at least one row to ship.
        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColPay).equals(Boolean.TRUE))
            {
            	_grid.stopCellEditing(i, gridColAmtToPay);
            	_grid.stopCellEditing(i, gridColCheckNum);
                d_amtToPay = (Double)_grid.get(i, gridColAmtToPay);
                if (d_amtToPay == null || d_amtToPay.doubleValue() == 0) {
                    JOptionPane.showMessageDialog(  this,
                                        "Item number " + i +" has an invalid Payment Amount.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);
                    return false;
                }

                //we have at least one line item is selected.
                ret = true;
            }
        }

        if (!ret) {
            //No Items were selected to Pay.
            JOptionPane.showMessageDialog(  this,
                                      "Please select at least one Payment to Pay.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
        }

        return ret;
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        updateItemGrid(_containerFrame.dateEntryPanel.getDueDate());

        return true;
    }

	private void updateItemGrid(String dueDate)
	{
        _grid.removeAllRows();
        DBRecSet attribSets;
        try {
            //Assume the following will be returned.
            // payment_voucherObj.ID
            // payment_voucherObj.PAYMENT_DUE_DATE
            // payment_voucherObj.VENDOR_NAME
            // payment_voucherObj.INVOICE_NUM
            // payment_voucherObj.INVOICE_DATE
            // payment_voucherObj.TOTAL_PAYMENTS_POSTED
            // payment_voucherObj.TOTAL_VALUE
            attribSets = purchOrdAdapter.getPayablePurchases(sessionMeta.getClientServerSecurity(),
                                                        dueDate);

            if (attribSets == null || attribSets.getSize() == 0)
            {
                JOptionPane.showMessageDialog(  this,
                                        "No Payments Scheduled on or before specified Date.",
                                        "Message",
                                        JOptionPane.INFORMATION_MESSAGE, null);
                return;
            }

            for (int i=0; i<attribSets.getSize(); i++)
            {
                _grid.addRow();

                //_grid.setComboColData(i, _clientDataCache.getAcctsComboBox());

                DBRec attribs = attribSets.getRec(i);

                //Populate the Grid Columns.
                //Make sure any columns needed for calculations are not null.
                String s_origAmtDue = attribs.getAttrib(payment_voucherObj.TOTAL_VALUE).getValue();
                String s_amtPaid = attribs.getAttrib(payment_voucherObj.TOTAL_PAYMENTS_POSTED).getValue();
                if (s_origAmtDue == null || s_origAmtDue.length() == 0) s_origAmtDue = "0.00";
                if (s_amtPaid == null || s_amtPaid.length() == 0) s_amtPaid = "0.00";

                Double amtDue = new Double(Double.parseDouble(s_origAmtDue) - Double.parseDouble(s_amtPaid));

                _grid.set(i, gridColVoucherNum, attribs.getAttrib(payment_voucherObj.ID).getValue());
                _grid.set(i, gridColDateDue, attribs.getAttrib(payment_voucherObj.PAYMENT_DUE_DATE).getValue());
                _grid.set(i, gridColVendor, attribs.getAttrib(payment_voucherObj.VENDOR_NAME).getValue());
                _grid.set(i, gridColRefNo, attribs.getAttrib(payment_voucherObj.INVOICE_NUM).getValue());
                _grid.set(i, gridColAmtAlreadyPaid, new Double(s_amtPaid));
                _grid.set(i, gridColAmtDue, amtDue);
                _grid.set(i, gridColAmtToPay, amtDue);
            }

            _grid.setRowFocus(0);

            //Display the number of bills that can be paid.
            daiLabel_numBills.setText("Total Number of Bills: " +_grid.getRowCount());

        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, ex.getLocalizedMessage());
        }
	}

	public DBRecSet getPayVoucherObjs()
	{
		DBRecSet attribSets = new DBRecSet();

        for (int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColPay).equals(Boolean.TRUE))
            {
                DBRec attribs = new DBRec();
                attribs.addAttrib(new DBAttributes(payment_voucherObj.PAYMENT_AMT, ((Double)_grid.get(i, gridColAmtToPay)).toString()));
                attribs.addAttrib(new DBAttributes(payment_voucherObj.ID, (String)_grid.get(i, gridColVoucherNum)));
                attribs.addAttrib(new DBAttributes(payment_voucherObj.CHECK_NUM, (String)_grid.get(i, gridColCheckNum)));
                attribs.addAttrib(new DBAttributes(payment_voucherObj.VENDOR_NAME, (String)_grid.get(i, gridColVendor)));
                attribSets.addRec(attribs);
            }
        }

        return attribSets;
	}

    void jButton_toggleSelected_actionPerformed(ActionEvent e) {
        for (int i=0; i< _grid.getRowCount(); i++) {

            if (_grid.isRowSelected(i)) {
                if (_grid.get(i, gridColPay).equals(Boolean.TRUE)) {
                    _grid.set(i, gridColPay, Boolean.FALSE);
                    _grid.set(i, gridColCheckNum, null);
                } else {
                    _grid.set(i, gridColPay, Boolean.TRUE);
                }
            }
        }
    }

    void jButton_genCheckNums_actionPerformed(ActionEvent e) {
        String s_startCheckNum = daiNumField_startCheckNum.getText();
        if (s_startCheckNum == null) {
            return;
        }
        int i_startCheckNum = Integer.parseInt(s_startCheckNum);
        for (int i=0; i< _grid.getRowCount(); i++) {

            if (_grid.get(i, gridColPay).equals(Boolean.TRUE)) {
                _grid.set(i, gridColCheckNum, Integer.toString(i_startCheckNum));
                i_startCheckNum++;
            }
        }
    }

    void jButton_renumChecks_actionPerformed(ActionEvent e) {
        boolean isRowSelected = false;
        String firstCheckNum = null;
        int i_startCheckNum = 0;
        for (int i=0; i< _grid.getRowCount(); i++) {
            if (_grid.get(i, gridColPay).equals(Boolean.TRUE))
            {
                if (_grid.isRowSelected(i) && !isRowSelected) {
                    isRowSelected = true;
                    firstCheckNum = (String)_grid.get(i, gridColCheckNum);
                    if (firstCheckNum == null) return;
                    i_startCheckNum = Integer.parseInt(firstCheckNum);
                }
                if (isRowSelected) {
                    _grid.set(i, gridColCheckNum, Integer.toString(i_startCheckNum));
                    i_startCheckNum++;
                }
            }
        }
    }
}


