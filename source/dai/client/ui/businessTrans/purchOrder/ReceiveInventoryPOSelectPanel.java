
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiWizardPanel;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiFormatUtil;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiTextArea;
import daiBeans.daiTextField;

public class ReceiveInventoryPOSelectPanel extends daiWizardPanel
{
    SessionMetaData sessionMeta;
    csDBAdapterFactory dbAdapterFactory = null;
    csDBAdapter dbAdapter = null;

    XYLayout xYLayout1 = new XYLayout();
    DefaultListModel listModel_right = new DefaultListModel();
    DefaultListModel listModel_left = new DefaultListModel();

    Logger _logger;
    daiTextField daiTextField_vendorName = new daiTextField();
    daiDateField daiDate_dateReceived = new daiDateField();
    daiLabel daiLabel_vendor = new daiLabel("Vendor Id:");
    daiTextField daiTextField_vendorId = new daiTextField();
    daiLabel daiLabel_dateReceived = new daiLabel("Date Received:");

    ReceiveInventoryFrame _CONTAINER_FRAME = null;
    daiLabel daiLabel_refNum = new daiLabel("Ref Num:");
    daiTextField daiTextField_refNum = new daiTextField();
    daiTextArea textArea_note   = new daiTextArea();
    daiLabel daiLabel_note = new daiLabel();

    String expenseAcctId = "";
    String expenseAcctName = "";
    daiLabel daiLabel_poId = new daiLabel();
    daiTextField daiTextField_poId = new daiTextField();
    daiTextField daiTextField_custName = new daiTextField();
    daiLabel daiLabel_cust = new daiLabel();
    daiTextField daiTextField_custId = new daiTextField();

    JFrame CONTAINER = null;

    public ReceiveInventoryPOSelectPanel(JFrame container, ReceiveInventoryFrame containerFrame)
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

        xYLayout1.setHeight(266);
        xYLayout1.setWidth(667);

        this.setLayout(xYLayout1);
        daiTextField_vendorId.setDisabled(true);
        daiTextField_vendorName.setDisabled(true);
        daiTextField_custId.setDisabled(true);
        daiTextField_custName.setDisabled(true);

        daiLabel_poId.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_poId_daiAction(e);
            }
        });
        daiLabel_note.setText("Note:");
        daiTextField_poId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(FocusEvent e)
            {
                if (!e.isTemporary()) {
                    daiTextField_poId_keyPressed(e);
                }
            }
            });        
        
        daiLabel_poId.setHREFstyle(true);
        daiLabel_poId.setText("Purch Ord Id:");
        daiDate_dateReceived.setCurrentDate(true);

        daiLabel_cust.setText("Customer:");
        daiLabel_vendor.setText("Vendor:");
        this.add(daiLabel_vendor, new XYConstraints(91, 81, -1, -1));
        this.add(daiLabel_poId, new XYConstraints(67, 48, -1, -1));
        this.add(daiTextField_poId, new XYConstraints(135, 49, 128, -1));
        this.add(daiLabel_dateReceived, new XYConstraints(57, 167, -1, -1));
        this.add(daiLabel_note, new XYConstraints(105, 197, -1, -1));
        this.add(daiLabel_refNum, new XYConstraints(86, 137, -1, -1));
        this.add(daiTextField_vendorName, new XYConstraints(265, 77, 255, -1));
        this.add(daiTextField_custName, new XYConstraints(265, 105, 255, -1));
        this.add(daiLabel_cust, new XYConstraints(80, 106, 50, -1));
        this.add(daiTextField_custId, new XYConstraints(135, 105, 128, -1));
        this.add(daiTextField_vendorId, new XYConstraints(135, 77, 128, -1));
        this.add(daiTextField_refNum, new XYConstraints(135, 135, 128, -1));
        this.add(daiDate_dateReceived, new XYConstraints(135, 165, -1, -1));
        this.add(textArea_note, new XYConstraints(135, 197, 386, 49));

        daiTextField_poId.requestFocus();
    }

    public boolean panelDataIsValid()
    {
        //Must enter a valid Purchase Order Id.
        if (daiTextField_poId.getText() == null) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a Purchase Order Id.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }
        //Make sure the Date Received is correct.
        String dateReceived = daiDate_dateReceived.getText();
        if (dateReceived == null || dateReceived.length() == 0) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a valid Date the Inventory was Received.",
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

    //Getter's
    public String getVendorId()
    {
        return daiTextField_vendorId.getText();
    }

    public String getVendorName()
    {
        return daiTextField_vendorName.getText();
    }

    public String getRefNum()
    {
        return daiTextField_refNum.getText();
    }

    public String getDateReceived()
    {
        return daiDate_dateReceived.getText();
    }

    public String getPOId()
    {
        return daiTextField_poId.getText();
    }

    public void resetPanel() {
        daiTextField_custId.setText(null);
        daiTextField_custName.setText(null);
        textArea_note.setText(null);
        daiTextField_poId.setText(null);
        daiTextField_vendorId.setText(null);
        daiTextField_vendorName.setText(null);
        daiTextField_refNum.setText(null);
        daiDate_dateReceived.setText(daiFormatUtil.getCurrentDate());
        //Update the Banner
        if (_CONTAINER_FRAME != null) {
            _CONTAINER_FRAME.setBannerRightText("");
        }
        daiTextField_poId.requestFocus();
    }

    private void daiLabel_poId_daiAction(daiActionEvent e)
    {
		purch_orderObj tempObj = new purch_orderObj();
        String id = daiTextField_poId.getText();

        DBAttributes attrib1 = new DBAttributes(purch_orderObj.ID, id, "PO Id", 100);
        DBAttributes attrib2 = new DBAttributes(purch_orderObj.VENDOR_NAME, id, "Vendor Name", 200);
		DataChooser chooser = new DataChooser(CONTAINER, "Data Chooser",
											  tempObj,
                                              new DBAttributes[]{attrib1, attrib2},
                                              null, null);
		chooser.show();
        purch_orderObj chosenObj = (purch_orderObj)chooser.getChosenObj();
        if (chosenObj != null) {
            populatePanel(chosenObj);
        }
    	chooser.dispose();
    }

    void daiTextField_poId_keyPressed(FocusEvent e) {
		String id = daiTextField_poId.getText();

		if (id != null)
		{
    		String exp = " locality = " + "'" + purch_orderObj.getObjLocality() + "'" +
                        " and id = '" + id + "' ";
            try {
                Vector objsVect = dbAdapter.queryByExpression( sessionMeta.getClientServerSecurity(),
                                                                    new purch_orderObj(),
                                                                    exp);
                //Does the input po exist?
                if (objsVect.size() == 0) {
                    //Let the user know that the PO Does not exist.
                    JOptionPane.showMessageDialog(this  ,
                                        "Selected Purchase Order Does Not Exist.  Please try another.",
                                        "Warning",
                                        JOptionPane.WARNING_MESSAGE, null);

                    this.resetPanel();
                } else {
                    purch_orderObj poObj = (purch_orderObj)objsVect.firstElement();
                    populatePanel(poObj);
                    daiTextField_refNum.requestFocus();
                }
            } catch (Exception ex) {
                _logger.logError(CONTAINER, "Can't Get Purch Order Data.\n" + ex.getLocalizedMessage());
            }
		}
    }

    private void populatePanel(purch_orderObj obj) {
        daiTextField_poId.setText(obj.get_id());
        daiTextField_vendorId.setText(obj.get_vendor_id());
        daiTextField_vendorName.setText(obj.get_vendor_name());
        if (obj.get_cust_id() != null && obj.get_cust_id().length() != 0) {
            daiTextField_custId.setText(obj.get_cust_id());
            daiTextField_custName.setText(obj.get_cust_name());
        } else {
            daiTextField_custId.setText("<<STOCK>>");
            daiTextField_custName.setText(null);
        }

        daiTextField_refNum.requestFocus();
    }
}
