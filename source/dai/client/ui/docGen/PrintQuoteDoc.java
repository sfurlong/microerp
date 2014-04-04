package dai.client.ui.docGen;

import javax.swing.JFrame;

import dai.client.clientShared.daiDocPrintFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.quoteObj;

public class PrintQuoteDoc extends daiDocPrintFrame {

    public PrintQuoteDoc(JFrame container) {
        super(container, new quoteObj(),
                                                "salesQuote.rpt",
                                                new DefaultDocPrintPanel("salesQuote.rpt"),
                                                "Print Quotation");
//                selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450),});
            selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("customer_name","Customer Name",250),
                                                          new DBAttributes("customer_id","Customer ID",100),
                                                          null,
                                                          new DBAttributes("item_id","Item ID",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created",100),
                                                          new DBAttributes("created_by","Created By",150)});

    }

    public PrintQuoteDoc(JFrame container, String transId) {
        super(container, new quoteObj(),
                                                "salesQuote.rpt",
                                                new DefaultDocPrintPanel("salesQuote.rpt"),
                                                "Print Quotation",
                                                transId);
//    selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450),});
          selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("customer_name","Customer Name",250),
                                                          new DBAttributes("customer_id","Customer ID",100),
                                                          null,
                                                          new DBAttributes("item_id","Item ID",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created",100),
                                                          new DBAttributes("created_by","Created By",150)});

    }
}
