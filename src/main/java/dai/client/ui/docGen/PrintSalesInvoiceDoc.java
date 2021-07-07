package dai.client.ui.docGen;

import javax.swing.JFrame;

import dai.client.clientShared.daiDocPrintFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.shipmentObj;

public class PrintSalesInvoiceDoc extends daiDocPrintFrame {

    public PrintSalesInvoiceDoc(JFrame container) {

        super(container, new shipmentObj(),
                                                "salesinvoice.rpt",
                                                new DefaultDocPrintPanel("salesinvoice.rpt"),
                                                "Print Sales Invoice");
          selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("customer_id","Customer ID",100),
                                                          new DBAttributes("customer_name","Customer Name",200),
                                                          null,
                                                          new DBAttributes("item_id","Item ID",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",100),
                                                          new DBAttributes("created_by","Created By",100),
                                                          null,
                                                          new DBAttributes("po_num","PO Number",100)});
//                selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450)});

    }

    public PrintSalesInvoiceDoc(JFrame container, String transId) {

        super(container, new shipmentObj(),
                                                "salesinvoice.rpt",
                                                new DefaultDocPrintPanel("salesinvoice.rpt"),
                                                "Print Sales Invoice",
                                                transId);
          selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("customer_id","Customer ID",100),
                                                          new DBAttributes("customer_name","Customer Name",200),
                                                          null,
                                                          new DBAttributes("item_id","Item ID",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",100),
                                                          new DBAttributes("created_by","Created By",100),
                                                          null,
                                                          new DBAttributes("po_num","PO Number",100)});
//                selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450)});

    }
}
