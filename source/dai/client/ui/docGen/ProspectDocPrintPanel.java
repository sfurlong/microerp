
//Title:        clientShared
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Shared Code for Client Software

package dai.client.ui.docGen;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiDocPrintPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.prospectObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiDateField;
import daiBeans.daiLabel;
import daiBeans.daiRadioButton;
import daiBeans.daiTextField;

public class ProspectDocPrintPanel extends daiDocPrintPanel {
    JPanel jPanel1 = new JPanel();
    XYLayout xYLayout2 = new XYLayout();
    daiTextField startId = new daiTextField();
    daiTextField endId = new daiTextField();
    daiLabel daiLabel_id1 = new daiLabel("Start Id:");
    daiLabel daiLabel_id2 = new daiLabel("End Id:");
    ButtonGroup buttonGroup_range = new ButtonGroup();
    daiRadioButton radioButton_idRange = new daiRadioButton("Use Id Range");
    daiRadioButton radioButton_dateRange = new daiRadioButton("Use Date Range");
    daiLabel daiLabel_startDate = new daiLabel("Start Date:");
    daiLabel daiLabel_endDate = new daiLabel("End Date:");
    daiDateField startDate = new daiDateField();
    daiDateField endDate = new daiDateField();

    public ProspectDocPrintPanel(String rptFileName) {
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

        buttonGroup_range.add(radioButton_idRange);
        buttonGroup_range.add(radioButton_dateRange);
        radioButton_idRange.setSelected(true);
        disableIdRangeFields(false);
        radioButton_idRange.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                disableIdRangeFields(false);
                //NOTE: SPECIAL HARDCODING.
                _containerFrame.setReportFileName("prospectLabel.rpt");
        	}
        });
        radioButton_dateRange.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                disableIdRangeFields(true);
                //NOTE: SPECIAL HARDCODING.
                _containerFrame.setReportFileName("prospectLabel-bydate.rpt");
        	}
        });
        
        jPanel1.setLayout(xYLayout2);
        jPanel1.setBackground(daiColors.PanelColor);
        jPanel1.add(radioButton_idRange, new XYConstraints(31, 50, -1, -1));
        jPanel1.add(daiLabel_id1, new XYConstraints(51, 85, -1, -1));
        jPanel1.add(startId, new XYConstraints(103, 85, -1, -1));
        jPanel1.add(daiLabel_id2, new XYConstraints(212, 85, -1, -1));
        jPanel1.add(endId, new XYConstraints(258, 85, -1, -1));
        jPanel1.add(radioButton_dateRange, new XYConstraints(31, 120, -1, -1));
        jPanel1.add(daiLabel_startDate, new XYConstraints(51, 155, -1, -1));
        jPanel1.add(startDate, new XYConstraints(103, 155, -1, -1));
        jPanel1.add(daiLabel_endDate, new XYConstraints(212, 155, -1, -1));
        jPanel1.add(endDate, new XYConstraints(258, 155, -1, -1));
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
        if (obj.getTableName().equals(prospectObj.TABLE_NAME)) {
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
        }

        startId.setText(id);

        } catch (Exception e) {
            Logger.getInstance().logError(this._containerFrame.CONTAINER, e.getLocalizedMessage());
        }
        return ret;
    }

    public void refresh() {
        startId.setText(null);
        endId.setText(null);
        startDate.setText(null);
        endDate.setText(null);
        disableIdRangeFields(false);
        startId.requestFocus();
    }

    private void disableIdRangeFields(boolean b) {
        startId.setDisabled(b);
        endId.setDisabled(b);
        startDate.setDisabled(!b);
        endDate.setDisabled(!b);
    }
    
    public String getId() {
        if (radioButton_idRange.isSelected()) {
            return startId.getText();
        } else {
            return startDate.getText();
        }
    }
    
    public String[] getRptParms() {
        String[] ret = new String[2];
        String start = null;
        String end   = null;
        if (radioButton_idRange.isSelected()) {
            start = startId.getText();
            end   = endId.getText();
        } else {
            start = startDate.getText();
            end   = endDate.getText();
        }
        ret[0] = start;
        if (end == null) {
            ret[1] = start;
        } else {
            ret[1] = end;
        }
        return ret;
    }
}
