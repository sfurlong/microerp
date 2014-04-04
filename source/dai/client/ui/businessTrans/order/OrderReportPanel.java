// Title: Business Artifacts
//Version:
//Copyright: Copyright (c) 1998
//Author: Stephen P. Furlong
//Company: Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.order;

import java.net.URLEncoder;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.VerticalFlowLayout;

import dai.client.clientShared.BrowserLauncher;
import dai.client.clientShared.daiColors;
import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiHeaderSubPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.cust_orderObj;
import dai.shared.businessObjs.webrpts_rpt_settingsObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiLabel;

public class OrderReportPanel extends daiHeaderSubPanel {
    GroupBox reportBox = new GroupBox();

    //Adapter Factory for getting handles to the Client Server Adapters
    csDBAdapterFactory dbAdapterFactory;

    csDBAdapter dbAdapter;

    SessionMetaData sessionMeta;

    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

    VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();

    daiLabel daiLabel_orderStatus = new daiLabel();

    daiLabel daiLabel_CustBackOrderByCust = new daiLabel();

    private final static String CHAR_ENCODING = "UTF-8";
    private final static String ORDER_STATUS_RPT_ID = "200";
    private final static String CUST_BACK_ORD_RPT_ID = "430";
    
    //Constructor.
    public OrderReportPanel(JFrame container, daiFrame parentFrame,
            cust_orderObj obj) {

        super(container, parentFrame, obj);

        try {
            jbInit();
        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not initialize headerPanel.\n"
                    + ex);
            ex.printStackTrace();
        }
    }

    //Initialize Controls
    void jbInit() throws Exception {

        //Adapter Factory for getting handles to the Client Server Adapters
        dbAdapterFactory = csDBAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();
        sessionMeta = SessionMetaData.getInstance();

        this.setBackground(daiColors.PanelColor);
        this.setLayout(verticalFlowLayout1);
        reportBox.setLabel("Reports:");
        reportBox.setLayout(verticalFlowLayout2);
        this.add(reportBox, null);

        DBRecSet userRpts = getReportsUserHasAccessTo();
        
        initReportListing(ORDER_STATUS_RPT_ID, userRpts, daiLabel_orderStatus);
        daiLabel_orderStatus.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                try {
                    cust_orderObj custOrd = (cust_orderObj) BUSINESS_OBJ;
                    String stURL = "http://"
                            + sessionMeta.getWebRptsHost()
                            + "/"
                            + daiLabel_orderStatus.getName()
                            + "?"
                            + (custOrd.get_customer_id() != null ? "CUST_ID="
                                    + URLEncoder.encode(custOrd
                                            .get_customer_id(), CHAR_ENCODING)
                                    + "\"&\"" : "")
                            + (custOrd.get_customer_name() != null ? "CUST_NAME="
                                    + URLEncoder
                                            .encode(
                                                    custOrd.get_customer_name(),
                                                    CHAR_ENCODING) + "\"&\""
                                    : "")
                            + (custOrd.get_id() != null ? "CUST_ORD_ID="
                                    + URLEncoder.encode(custOrd.get_id(),
                                            CHAR_ENCODING) + "\"&\"" : "")
                            + (custOrd.get_po_num() != null ? "CUST_PO_ID="
                                    + URLEncoder.encode(custOrd.get_po_num(),
                                            CHAR_ENCODING) : "");
                    BrowserLauncher.openURL(stURL);
                } catch (Exception eR) {
                    Logger.getInstance().logError(
                            CONTAINER,
                            "Unable to launch browser.\n"
                                    + eR.getLocalizedMessage());
                }
            }
        });
        reportBox.add(daiLabel_orderStatus, null);

        initReportListing(CUST_BACK_ORD_RPT_ID, userRpts, daiLabel_CustBackOrderByCust);
        daiLabel_CustBackOrderByCust
                .adddaiActionListener(new daiActionListener() {
                    public void daiActionEvent(daiActionEvent e) {
                        try {
                            cust_orderObj custOrd = (cust_orderObj) BUSINESS_OBJ;
                            String stURL = "http://"
                                    + sessionMeta.getWebRptsHost()
                                    + "/"
                                    + daiLabel_CustBackOrderByCust.getName()
                                    + (custOrd.get_customer_id() != null ? "?CUST_ID="
                                            + URLEncoder.encode(custOrd
                                                    .get_customer_id(),
                                                    CHAR_ENCODING)
                                            : "");
                            BrowserLauncher.openURL(stURL);
                        } catch (Exception eR) {
                            Logger.getInstance().logError(
                                    CONTAINER,
                                    "Unable to launch browser.\n"
                                            + eR.getLocalizedMessage());
                        }
                    }
                });
        reportBox.add(daiLabel_CustBackOrderByCust, null);
    }

    protected BusinessObject getNewBusinessObjInstance() {
        cust_orderObj obj = new cust_orderObj();
        cust_orderObj tempObj = (cust_orderObj) BUSINESS_OBJ;

        //Set the Primary Keys for the new Item Object.
        obj.set_id(tempObj.get_id());
        obj.set_locality(tempObj.get_locality());

        return obj;
    }

    public void update_UI(BusinessObject bobj) {
        cust_orderObj obj = (cust_orderObj) bobj;
        BUSINESS_OBJ = obj;
    }

    public void update_BusinessObj(BusinessObject bobj) {
        cust_orderObj obj = (cust_orderObj) bobj;

        BUSINESS_OBJ = obj;
    }

    private DBRecSet getReportsUserHasAccessTo() throws Exception {
        String sqlStmt;
        DBRecSet rptRecSet = new DBRecSet();
        //Now get all the reports on this tab that this user has permissions
        // for.
        sqlStmt = " select rpts.id, rpt_name, rpt_url, has_read_access "
                + " from webrpts_rpt_settings rpts, webrpts_security sec "
                + " where rpts.id = sec.id and " 
                + " sec.userid = '" + sessionMeta.getUserId() + "'"
                + " and "
                + " sec.has_read_access = 'Y'  order by rpt_name";
        try {
            return dbAdapter.execDynamicSQL(sessionMeta
                    .getClientServerSecurity(), sqlStmt);
        } catch (Exception e) {
            String msg = this.getClass().getName()
            + "::webreport listing failure."
            + "\n" + e.getLocalizedMessage();
            LOGGER.logError(CONTAINER, msg);
            throw e;
        }
    }

    private void initReportListing(String rptId, DBRecSet userRpts, daiLabel reportListing) {
        reportListing.setVisible(false);

        for (int i = 0; i < userRpts.getSize(); i++) {
            DBRec rptRec = userRpts.getRec(i);
            if (rptRec.getAttribVal(webrpts_rpt_settingsObj.ID).equals(rptId)) {
                reportListing.setHorizontalAlignment(SwingConstants.LEFT);
                reportListing.setHorizontalTextPosition(SwingConstants.LEFT);
                reportListing.setText(rptRec
                        .getAttribVal(webrpts_rpt_settingsObj.RPT_NAME));
                reportListing.setHREFstyle(true);
                reportListing.setVisible(true);
                reportListing.setName(rptRec
                        .getAttribVal(webrpts_rpt_settingsObj.RPT_URL));
            }
        }
    }
}