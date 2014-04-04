
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.businessObjs.purch_order_itemObj;
import dai.shared.businessObjs.purch_order_item_rcv_histObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiCurrencyField;
import daiBeans.daiGrid;
import daiBeans.daiLabel;
import daiBeans.daiTextField;

public class InvoiceReceiptSelectionPanel extends daiWizardPanel
{
    SessionMetaData sessionMeta;
    purch_orderObj  _purchOrdObj;

    csDBAdapterFactory dbAdapterFactory = null;
    csDBAdapter dbAdapter = null;
    csPurchOrderAdapterFactory _poAdapterFactory = null;
    csPurchOrderAdapter _poAdapter = null;

    XYLayout xYLayout1 = new XYLayout();
    DefaultListModel listModel_right = new DefaultListModel();
    DefaultListModel listModel_left = new DefaultListModel();

    Logger _logger;
    daiTextField daiTextField_vendorName = new daiTextField();
    daiCurrencyField daiCurrency_amtDue = new daiCurrencyField();
    daiLabel daiLabel_vendorId = new daiLabel("Vendor Id:");
    daiLabel daiLabel_purchOrdId = new daiLabel("Purch Order#:");
    daiTextField daiTextField_purchOrdId = new daiTextField();
    daiTextField daiTextField_vendorId = new daiTextField();
    daiLabel daiLabel_amtDue = new daiLabel("Total Amt Due:");

    daiGrid _grid = new daiGrid();
    //Grid locations
    final int gridColCheck          = 0;
    final int gridColPOId           = 1;
    final int gridColItemId         = 2;
    final int gridColPurchPrice     = 3;
    final int gridColQtyOrdered     = 4;
	final int gridColQtyRcvd        = 5;
    final int gridColDateRcvd       = 6;

    InvoiceReceiptFrame _CONTAINER_FRAME = null;
    daiLabel daiLabel_itemsRcvNote = new daiLabel();
    JOptionPane _userDlg = new JOptionPane();
    daiLabel daiLabel_shipCharges = new daiLabel();
    daiCurrencyField daiField_shipCharges = new daiCurrencyField();
    daiCurrencyField daiField_subtot = new daiCurrencyField();
    daiLabel daiLabel_subtot = new daiLabel();
    JButton jButton_reCalcTot = new JButton();

    JFrame CONTAINER = null;

    public InvoiceReceiptSelectionPanel(JFrame container)
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

    public InvoiceReceiptSelectionPanel(JFrame container, InvoiceReceiptFrame containerFrame)
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
        _grid.createColumns(new int[]{daiGrid.CHECKBOX_COL_TYPE,
        		daiGrid.CHAR_COL_TYPE,
        		daiGrid.CHAR_COL_TYPE,
        		daiGrid.DOUBLE_COL_TYPE,
        		daiGrid.INTEGER_COL_TYPE,
        		daiGrid.INTEGER_COL_TYPE,
        		daiGrid.DATE_COL_TYPE});
        _grid.setHeaderNames(new String[]{" ", "PO Id", "Item Id",
                    "Purch Price", "Qty Ordered", "Qty Rcvd", "Date Rcvd"});
        //Disable most of the Grid entry fields
        _grid.setColEditable(gridColPOId, false);
        _grid.setColEditable(gridColItemId, false);
        _grid.setColEditable(gridColPurchPrice, false);
        _grid.setColEditable(gridColQtyOrdered, false);
        _grid.setColEditable(gridColQtyRcvd, false);
        _grid.setColEditable(gridColDateRcvd, false);
        //Resize some of the Grid fields
        _grid.setColumnSize(gridColCheck, 30);
        _grid.setColumnSize(gridColItemId, daiGrid.DEFAULT_ID_COL_WIDTH);
        _grid.setBackground(Color.lightGray);
        //Turn off row selection
        _grid.allowRowSelection(false);

        _logger = Logger.getInstance();
        sessionMeta = SessionMetaData.getInstance();
        dbAdapterFactory = csDBAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();
        _poAdapterFactory = csPurchOrderAdapterFactory.getInstance();
        _poAdapter = _poAdapterFactory.getPurchOrderAdapter();

        xYLayout1.setHeight(322);
        xYLayout1.setWidth(544);

        daiTextField_vendorId.setDisabled(true);
        daiTextField_vendorName.setDisabled(true);
        daiCurrency_amtDue.setDisabled(true);
        daiLabel_purchOrdId.setHREFstyle(true);
        daiField_subtot.setDisabled(true);
        daiLabel_purchOrdId.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                daiLabel_purchOrdId_daiAction(e);
            }
        });

        this.setLayout(xYLayout1);

        daiLabel_amtDue.setFont(new java.awt.Font("Dialog", 1, 11));
        daiLabel_itemsRcvNote.setText("Items Received For This Purchase Order");
        daiTextField_purchOrdId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e)
            {
                if (!e.isTemporary() && !daiTextField_purchOrdId.isDisabled()) {
                    purchOrdId_focusLost(e);
                }
            }
            });        
        
        daiLabel_shipCharges.setText("Shipping Charges:");
        daiLabel_subtot.setText("Subtotal:");
        jButton_reCalcTot.setMnemonic(KeyEvent.VK_R);
        jButton_reCalcTot.setMargin(new Insets(2, 0, 2, 0));
        jButton_reCalcTot.setText("Recalc Total");
        jButton_reCalcTot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton_reCalcTot_actionPerformed();
            }
        });

        this.add(_grid, new XYConstraints(19, 173, 530, 130));
        this.add(daiLabel_itemsRcvNote, new XYConstraints(19, 157, -1, -1));
        this.add(daiLabel_amtDue, new XYConstraints(46, 125, -1, -1));
        this.add(daiCurrency_amtDue, new XYConstraints(132, 125, -1, -1));
        this.add(jButton_reCalcTot, new XYConstraints(233, 125, -1, 20));
        this.add(daiField_subtot, new XYConstraints(132, 73, -1, -1));
        this.add(daiTextField_vendorId, new XYConstraints(133, 47, -1, -1));
        this.add(daiTextField_vendorName, new XYConstraints(235, 47, 271, -1));
        this.add(daiTextField_purchOrdId, new XYConstraints(133, 17, -1, -1));
        this.add(daiLabel_purchOrdId, new XYConstraints(59, 19, -1, -1));
        this.add(daiLabel_vendorId, new XYConstraints(77, 48, -1, -1));
        this.add(daiLabel_subtot, new XYConstraints(84, 73, -1, -1));
        this.add(daiLabel_shipCharges, new XYConstraints(38, 99, -1, -1));
        this.add(daiField_shipCharges, new XYConstraints(132, 99, -1, -1));

        _grid.removeAllRows();
    }

    public boolean panelDataIsValid()
    {
        //Must enter a valid Purchase Order Id.
        if (daiTextField_purchOrdId.getText() == null) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(_CONTAINER_FRAME  ,
                                      "Must enter an Purchase Order#.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        //Make sure the amount due is correct.
        double amtDue = Double.parseDouble(daiCurrency_amtDue.getText());
        if (amtDue <= 0) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(_CONTAINER_FRAME  ,
                                      "No Payments are Due for this Invoice.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE);
            return false;
        }

        //Make sure at least one grid item is checked.
        boolean gridChecked = false;
        for (int i=0; i<_grid.getRowCount(); i++) {
            if (_grid.get(i, gridColCheck).equals(Boolean.TRUE)) {
                gridChecked = true;
            }
        }
        if (gridChecked == false) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(_CONTAINER_FRAME  ,
                                      "Please select at least one inventory item.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean doPreDisplayProcessing(Object[] data)
    {
        return true;
    }

    public purch_orderObj getSelectedPurchOrderObj()
    {
        return _purchOrdObj;
    }

    public String getPaymentAmt() {
        String ret = daiCurrency_amtDue.getText();
        if (ret == null) ret = "0.00";
        return ret;
    }

    public String getShipAmt() {
        String ret = daiField_shipCharges.getText();
        if (ret == null) ret = "0.00";
        return ret;
    }

    public String getSubtotal() {
        String ret = daiField_subtot.getText();
        if (ret == null) ret = "0.00";
        return ret;
    }

    public void resetPanel() {
        daiField_shipCharges.setText(null);
        daiField_subtot.setText(null);
        daiTextField_purchOrdId.setText(null);
        daiTextField_purchOrdId.setDisabled(false);
        daiTextField_vendorId.setText(null);
        daiTextField_vendorName.setText(null);
        daiCurrency_amtDue.setText(null);
        _grid.removeAllRows();

        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText("");
        }
        daiTextField_purchOrdId.requestFocus();
    }

    private void daiLabel_purchOrdId_daiAction(daiActionEvent e)
    {
		purch_orderObj tempObj = new purch_orderObj();
        String id = daiTextField_purchOrdId.getText();

		String exp = purch_orderObj.PURCH_ORDER_TYPE+"='"+purch_orderObj.PURCH_ORDER_TYPE_PURCHASE+"'";

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DBAttributes attrib1 = new DBAttributes(purch_orderObj.ID, id, "PO Id", 100);
        DBAttributes attrib2 = new DBAttributes(purch_orderObj.VENDOR_NAME, "Vendor Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              exp, null);
		chooser.setVisible(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        purch_orderObj chosenObj = (purch_orderObj)chooser.getChosenObj();
        if (chosenObj != null) {
            populatePanel(chosenObj);
            _purchOrdObj = chosenObj;
        }
    	chooser.dispose();
    }

    private void purchOrdId_focusLost(FocusEvent e) {
		String id = daiTextField_purchOrdId.getText();

		if (id != null && id.trim().length() > 0)
		{
    		String exp = " locality = " + "'" + purch_orderObj.getObjLocality() + "'" +
                        " and id = '" + id + "' ";
            try {
                Vector purchObjsVect = dbAdapter.queryByExpression( sessionMeta.getClientServerSecurity(),
                                                                    new purch_orderObj(),
                                                                    exp);
                //Does the input Invoice exist?
                if (purchObjsVect.size() == 0) {
                    //Let the user know that the Purch Order Does not exist.
                    JOptionPane.showMessageDialog(_CONTAINER_FRAME  ,
                                        "Selected Purchase Order Does Not Exist.  Please try another.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE);

                    //Clear out the entry field.
                    this.resetPanel();
                } else {
                    purch_orderObj purchObj = (purch_orderObj)purchObjsVect.firstElement();
                    populatePanel(purchObj);
                    _purchOrdObj = purchObj;
                }
            } catch (Exception ex) {
                _logger.logError(CONTAINER, "Can't Get Purchase Order Data.\n" + ex.getLocalizedMessage());
            }
		}
    }

    private void populatePanel(purch_orderObj obj) {

        daiTextField_purchOrdId.setText(obj.get_id());
        daiTextField_vendorId.setText(obj.get_vendor_id());
        daiTextField_vendorName.setText(obj.get_vendor_name());

        daiTextField_purchOrdId.setDisabled(true);
        
        calcAmtDue();

        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText(": " + obj.get_id());
        }

        int numItems = populateGrid();

        if (numItems <= 0)
        {
            JOptionPane.showMessageDialog(_CONTAINER_FRAME,
                                        "No Payments are Due for this Invoice.",
                                        "Message",
                                        JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private int populateGrid()
    {
        _grid.removeAllRows();

        String poId =  daiTextField_purchOrdId.getText();
        this.daiField_shipCharges.setText(null);
        double totAmtDue = 0.0;
        int ret = 0;

        try {
            //Get the the PO Line Items in which all Ordered Items have not
            //been invoiced.  We assume we are getting the following attributes
            //in the collection:
            //  purch_order_item_rcv_histObj.ID
            //  purch_order_itemObj.ITEM_ID
            //  purch_order_itemObj.DESCRIPTION1
            //  purch_order_item_rcv_histObj.QTY_RECEIVED
            //  purch_order_itemObj.PURCH_PRICE
            //  purch_order_item_rcv_histObj.DATE_RECEIVED
            DBRecSet poAttribsCollection = _poAdapter.getInvoicablePOItems(sessionMeta.getClientServerSecurity(),
                                                                     poId);
            ret = poAttribsCollection.getSize();

            for (int i=0; i<poAttribsCollection.getSize(); i++)
            {
                _grid.addRow();

                DBRec attribSet = poAttribsCollection.getRec(i);

                //Populate the Grid Columns.
                //Make sure any columns needed for calculations are not null.
                String s_qtyReceived = attribSet.getAttrib(purch_order_item_rcv_histObj.QTY_RECEIVED).getValue();
                if (s_qtyReceived == null || s_qtyReceived.length() == 0) s_qtyReceived = "0";
                String s_purchPrice = attribSet.getAttrib(purch_order_itemObj.PURCH_PRICE).getValue();
                if (s_purchPrice == null || s_purchPrice.length() == 0) s_purchPrice = "0.0";
                String s_qtyOrdered = attribSet.getAttrib(purch_order_itemObj.QTY_ORDERED).getValue();
                if (s_qtyOrdered == null || s_qtyOrdered.length() == 0) s_qtyOrdered = "0";

                _grid.set(i, gridColCheck, Boolean.TRUE);
                _grid.set(i, gridColPOId, attribSet.getAttrib(purch_order_item_rcv_histObj.ID).getValue());
                _grid.set(i, gridColItemId, attribSet.getAttrib(purch_order_itemObj.ITEM_ID).getValue());
                _grid.set(i, gridColDateRcvd, attribSet.getAttrib(purch_order_item_rcv_histObj.DATE_RECEIVED).getValue());
                Integer qtyReceived = new Integer(s_qtyReceived);
                Integer qtyOrdered = new Integer(s_qtyOrdered);
                Double purchPrice = new Double(s_purchPrice);
                _grid.set(i, gridColQtyRcvd, qtyReceived);
                _grid.set(i, gridColPurchPrice, purchPrice);
                _grid.set(i, gridColQtyOrdered, qtyOrdered);

                totAmtDue = totAmtDue + (qtyReceived.intValue() * purchPrice.doubleValue());
            }

            //Take care of any percision problems
            java.math.BigDecimal bd = new java.math.BigDecimal(totAmtDue);
            bd = bd.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);

            daiField_subtot.setText(bd.toString());
            daiCurrency_amtDue.setText(bd.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            _logger.logError(CONTAINER, ex.getLocalizedMessage());
        }

        return ret;
    }

    private void calcAmtDue() {
        //Calculate the subtotal
        double subtot = 0.00;
        Integer qtyReceived = new Integer(0);
        Double purchPrice = new Double(0.00);

        for(int i=0; i<_grid.getRowCount(); i++)
        {
            if (_grid.get(i, gridColCheck).equals(Boolean.TRUE)) {
                qtyReceived = (Integer)_grid.get(i, gridColQtyRcvd);
                purchPrice = (Double)_grid.get(i, gridColPurchPrice);
                subtot = subtot + (qtyReceived.intValue() * purchPrice.doubleValue());
            }
        }
        daiField_subtot.setText(Double.toString(subtot));

        String s_shipAmt = daiField_shipCharges.getText();
        String s_subtot  = daiField_subtot.getText();

        if (s_shipAmt == null) s_shipAmt = "0.00";
        if (s_subtot == null)  s_subtot  = "0.00";

        double d_shipAmt = Double.parseDouble(s_shipAmt);
        double d_subtot  = Double.parseDouble(s_subtot);

        double d_amtDue = d_subtot + d_shipAmt;


        //Take care of any percision problems
        java.math.BigDecimal bd_subtot = new java.math.BigDecimal(d_amtDue);
        bd_subtot = bd_subtot.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        java.math.BigDecimal bd_amtDue = new java.math.BigDecimal(d_amtDue);
        bd_amtDue = bd_amtDue.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);

        daiField_subtot.setText(bd_subtot.toString());
        daiCurrency_amtDue.setText(bd_amtDue.toString());
    }

    void jButton_reCalcTot_actionPerformed() {
        calcAmtDue();
    }
}
