// Title: Business Artifacts
//Version:
//Copyright: Copyright (c) 1998
//Author: Stephen P. Furlong
//Company: Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.item;

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
import dai.shared.businessObjs.itemObj;
import dai.shared.businessObjs.webrpts_rpt_settingsObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiLabel;

public class ItemReportPanel extends daiHeaderSubPanel {
    GroupBox reportBox = new GroupBox();

    //Adapter Factory for getting handles to the Client Server Adapters
    csDBAdapterFactory dbAdapterFactory;

    csDBAdapter dbAdapter;

    SessionMetaData sessionMeta;

    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

    VerticalFlowLayout verticalFlowLayout2 = new VerticalFlowLayout();

    daiLabel daiLabel_prodAval = new daiLabel();

    daiLabel daiLabel_vendBackByVend = new daiLabel();

    daiLabel daiLabel_vendBackByModel = new daiLabel();

    private static String PROD_AVAIL_RPT_ID = "401";
    //Constructor.
    public ItemReportPanel(JFrame container, daiFrame parentFrame, itemObj obj) {

        super(container, parentFrame, obj);

        try {
            jbInit();
        } catch (Exception ex) {
            LOGGER.logError(CONTAINER, "Could not initialize Report Panel.\n"
                    + ex);
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

        DBRecSet userRpts = this.getReportsUserHasAccessTo();
        initReportListing(PROD_AVAIL_RPT_ID, userRpts, daiLabel_prodAval);
        daiLabel_prodAval.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                try {
                    itemObj item = (itemObj) BUSINESS_OBJ;
                    String stURL = "http://"
                            + sessionMeta.getWebRptsHost()
                            + "/"
                            + daiLabel_prodAval.getName()
                            + (item.get_id() != null ? "?ITEM_ID="
                                    + URLEncoder.encode(item.get_id(), "UTF-8")
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
        reportBox.add(daiLabel_prodAval, null);
    }

    protected BusinessObject getNewBusinessObjInstance() {
        itemObj obj = new itemObj();
        itemObj tempObj = (itemObj) BUSINESS_OBJ;

        //Set the Primary Keys for the new Item Object.
        obj.set_id(tempObj.get_id());
        obj.set_locality(tempObj.get_locality());

        return obj;
    }

    public void update_UI(BusinessObject bobj) {
        itemObj obj = (itemObj) bobj;
        BUSINESS_OBJ = obj;
    }

    public void update_BusinessObj(BusinessObject bobj) {
        itemObj obj = (itemObj) bobj;

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


    private void initReportListing(String rptId, DBRecSet userRpts,
            daiLabel reportListing) {
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