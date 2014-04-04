package dai.client.ui.docGen;

import javax.swing.JFrame;

import dai.client.clientShared.daiDocPrintFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.prospectObj;

public class PrintProspectLabelDoc extends daiDocPrintFrame {

    public PrintProspectLabelDoc(JFrame container) {

        super(container, new prospectObj(), "prospectLabel.rpt",
                new ProspectDocPrintPanel("prospectLabel.rpt"),
                "Print Propect Label");
        selectionBox.addSearchFilters(new DBAttributes[] {
                null,
                new DBAttributes("company_name", "Company Name", 150),
                new DBAttributes("customer_id", "Customer ID", 75),
                null,
                new DBAttributes("last_name", "Last Name", 100),
                new DBAttributes("comp_name+last_name",
                        "Company Name+Last Name", 125), null,
                new DBAttributes("zip", "ZIP Code", 75), null,
                new DBAttributes("date_created", "Date Created On", 75), null,
                new DBAttributes("< date_created", "Date Created Before", 75),
                new DBAttributes("> date_created", "Date Created After", 75),
                null, new DBAttributes("created_by", "Created By", 100) });
        //This report will not use the export report servlet, it will be printed
        //with the CRinf dlls.
        this.DO_WEB_PRINT = false;
    }

    public PrintProspectLabelDoc(JFrame container, String transId) {

        super(container, new prospectObj(), "prospectLabel.rpt",
                new ProspectDocPrintPanel("prospectLabel.rpt"), "Print Label",
                transId);
        selectionBox.addSearchFilters(new DBAttributes[] {
                null,
                new DBAttributes("company_name", "Company Name", 150),
                new DBAttributes("customer_id", "Customer ID", 75),
                null,
                new DBAttributes("last_name", "Last Name", 100),
                new DBAttributes("comp_name+last_name",
                        "Company Name+Last Name", 125), null,
                new DBAttributes("zip", "ZIP Code", 75), null,
                new DBAttributes("date_created", "Date Created On", 75), null,
                new DBAttributes("< date_created", "Date Created Before", 75),
                new DBAttributes("> date_created", "Date Created After", 75),
                null, new DBAttributes("created_by", "Created By", 100) });
    }
}