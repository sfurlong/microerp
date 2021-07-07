
//Title:        clientShared
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Shared Code for Client Software

package dai.client.ui.docGen;

import java.awt.BorderLayout;
import java.awt.event.FocusEvent;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiDocPrintPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cust_orderObj;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.prospectObj;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.businessObjs.quoteObj;
import dai.shared.businessObjs.shipmentObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiLabel;
import daiBeans.daiTextField;

public class DefaultDocPrintPanel extends daiDocPrintPanel {
    daiLabel daiLabel_id = new daiLabel();
    XYLayout xYLayout2 = new XYLayout();
    daiTextField daiTextField_id = new daiTextField();
    daiLabel daiLabel_cust = new daiLabel();
    daiTextField daiTextField_cust = new daiTextField();
    daiTextField daiTextField_custName = new daiTextField();

    public DefaultDocPrintPanel(String rptFileName) {
        super(rptFileName);
        _rptFileName = rptFileName;

        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        daiLabel_id.setText("Id:");
        daiLabel_cust.setText("Cust/Vendor:");

        daiLabel_id.setDisabled(true);
        daiTextField_cust.setDisabled(true);
        daiTextField_custName.setDisabled(true);

        daiTextField_id.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(FocusEvent e) {
                daiTextField_id_focusLost(e);
            }
        });

        jPanel1.setLayout(xYLayout2);
        jPanel1.setBackground(daiColors.PanelColor);
        jPanel1.add(daiLabel_id, new XYConstraints(67, 43, -1, -1));
        jPanel1.add(daiTextField_id, new XYConstraints(82, 42, -1, -1));
        jPanel1.add(daiLabel_cust, new XYConstraints(14, 77, -1, -1));
        jPanel1.add(daiTextField_cust, new XYConstraints(82, 75, 100, -1));
        jPanel1.add(daiTextField_custName, new XYConstraints(182, 75, 220, -1));

        this.add(jPanel1, BorderLayout.CENTER);
    }

    public int query(String id, BusinessObject obj) {
        int ret = 0;

        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
        String sqlStmt = " select ";
        String venCustId = null;
        String venCustName = null;
        try {
        if (obj.getTableName().equals(cust_orderObj.TABLE_NAME)) {
            sqlStmt = " select id, " +
                    cust_orderObj.CUSTOMER_ID + ", " +
                    cust_orderObj.CUSTOMER_NAME +
                    " from " + cust_orderObj.TABLE_NAME +
                    " where id = '" + id + "' and locality = '" + cust_orderObj.getObjLocality() + "'";

            DBRecSet attribs = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
            ret = attribs.getSize();
            if (ret > 0) {
                venCustId = attribs.getRec(0).getAttribVal(cust_orderObj.CUSTOMER_ID);
                venCustName = attribs.getRec(0).getAttribVal(cust_orderObj.CUSTOMER_NAME);
            }
        } else if (obj.getTableName().equals(shipmentObj.TABLE_NAME)) {
            sqlStmt = " select id, " +
                    shipmentObj.CUSTOMER_ID + ", " +
                    shipmentObj.CUSTOMER_NAME +
                    " from " + shipmentObj.TABLE_NAME +
                    " where id = '" + id + "' and locality = '" + shipmentObj.getObjLocality() + "'";
            System.out.println(sqlStmt);
            DBRecSet attribs = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
            ret = attribs.getSize();
            if (ret > 0) {
                venCustId = attribs.getRec(0).getAttribVal(shipmentObj.CUSTOMER_ID);
                venCustName = attribs.getRec(0).getAttribVal(shipmentObj.CUSTOMER_NAME);
            }
        } else if (obj.getTableName().equals(purch_orderObj.TABLE_NAME)) {
            sqlStmt = " select id, " +
                    purch_orderObj.VENDOR_ID + ", " +
                    purch_orderObj.VENDOR_NAME +
                    " from " + purch_orderObj.TABLE_NAME +
                    " where id = '" + id + "' and locality = '" + purch_orderObj.getObjLocality() + "'";
            DBRecSet attribs = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
            ret = attribs.getSize();
            if (ret > 0) {
                venCustId = attribs.getRec(0).getAttribVal(purch_orderObj.VENDOR_ID);
                venCustName = attribs.getRec(0).getAttribVal(purch_orderObj.VENDOR_NAME);
            }
        } else if (obj.getTableName().equals(customerObj.TABLE_NAME)) {
            sqlStmt = " select id, " +
                    customerObj.ID + ", " +
                    customerObj.NAME +
                    " from " + customerObj.TABLE_NAME +
                    " where id = '" + id + "' and locality = '" + customerObj.getObjLocality() + "'";
            DBRecSet attribs = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
            ret = attribs.getSize();
            if (ret > 0) {
                venCustId = attribs.getRec(0).getAttribVal(customerObj.ID);
                venCustName = attribs.getRec(0).getAttribVal(customerObj.NAME);
            }
        } else if (obj.getTableName().equals(prospectObj.TABLE_NAME)) {
            sqlStmt = " select " +
                    prospectObj.ID + ", " +
                    prospectObj.COMPANY_NAME +
                    " from " + prospectObj.TABLE_NAME +
                    " where id = '" + id + "' and locality = '" + prospectObj.getObjLocality() + "'";
            DBRecSet attribs = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
            ret = attribs.getSize();
            if (ret > 0) {
                venCustId = attribs.getRec(0).getAttribVal(prospectObj.ID);
                venCustName = attribs.getRec(0).getAttribVal(prospectObj.COMPANY_NAME);
            }
        } else if (obj.getTableName().equals(quoteObj.TABLE_NAME)) {
            sqlStmt = " select " +
                    quoteObj.ID + ", " +
                    quoteObj.CUSTOMER_NAME +
                    " from " + quoteObj.TABLE_NAME +
                    " where id = '" + id + "' and locality = '" + quoteObj.getObjLocality() + "'";
            DBRecSet attribs = dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStmt);
            ret = attribs.getSize();
            if (ret > 0) {
                venCustId = attribs.getRec(0).getAttribVal(quoteObj.ID);
                venCustName = attribs.getRec(0).getAttribVal(quoteObj.CUSTOMER_NAME);
            }
        }

        daiTextField_id.setText(id);
        daiTextField_cust.setText(venCustId);
        daiTextField_custName.setText(venCustName);

        } catch (Exception e) {
        }
        return ret;
    }

    public void refresh() {
        daiTextField_id.setText(null);
        daiTextField_cust.setText(null);
        daiTextField_custName.setText(null);
        //daiTextField_id.requestFocus();
    }

    public String getId() {
        return daiTextField_id.getText();
    }

    public String[] getRptParms()
    {
        String[] ret = new String[1];
        ret[0] = daiTextField_id.getText();
        return ret;
    }

    void daiTextField_id_focusLost(FocusEvent e) {
        String id = daiTextField_id.getText();

        if (!e.isTemporary()) {
            if (id != null) {
                _containerFrame.queryAllPanels(id);
                _containerFrame.setPrintActionsDisabled(false);
            } else {
                refresh();
                _containerFrame.setPrintActionsDisabled(true);
            }
        }
    }
}
