package dai.client.ui.docGen;

import javax.swing.JFrame;

import dai.client.clientShared.daiDocPrintFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.customerObj;

public class PrintCustStmtDoc extends daiDocPrintFrame {

    public PrintCustStmtDoc(JFrame container) {

        super(container, new customerObj(),
                                                "custStatement.rpt",
                                                new DefaultDocPrintPanel("custStatement.rpt"),
                                                "Print Customer Statement");
//                selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("name","Customer Name",600),});
          selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("name","Name",300),
                                                          null,
                                                          new DBAttributes("date_created","Date Created",200),
                                                          new DBAttributes("created_by","Created By",200)});
    }

    public PrintCustStmtDoc(JFrame container, String transId) {

        super(container, new customerObj(),
                                                "custStatement.rpt",
                                                new DefaultDocPrintPanel("custStatement.rpt"),
                                                "Print Customer Statement",
                                                transId);
//        selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("name","Customer Name",600),});
          selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("name","Name",300),
                                                          null,
                                                          new DBAttributes("date_created","Date Created",200),
                                                          new DBAttributes("created_by","Created By",200)});
    }
}
