package dai.client.ui.docGen;

import javax.swing.JFrame;

import dai.client.clientShared.daiDocPrintFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.purch_orderObj;

public class PrintPurchOrderDoc extends daiDocPrintFrame {

    public PrintPurchOrderDoc(JFrame container) {

        super(container, new purch_orderObj(),
                                                "purchaseOrder.rpt",
                                                new DefaultDocPrintPanel("purchaseOrder.rpt"),
                                                "Print Purchase Order");
        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("cust_name","Customer Name",100),
                                                          new DBAttributes("cust_id","Customer ID",75),
                                                          null,
                                                          new DBAttributes("vendor_id","Vendor ID",75),
                                                          new DBAttributes("vendor_name","Vendor Name",100),
                                                          null,
                                                          new DBAttributes("shipto_id","Ship To ID",75),
                                                          new DBAttributes("shipto_name","Ship To Name",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",75),
                                                          new DBAttributes("created_by","Created By",100)});
//      selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("vendor_id","Vendor ID",150),
//                                                          new DBAttributes("vendor_name","Vendor Name",450)});
    }

    public PrintPurchOrderDoc(JFrame container, String transId) {

        super(container, new purch_orderObj(),
                                                "purchaseOrder.rpt",
                                                new DefaultDocPrintPanel("purchaseOrder.rpt"),
                                                "Print Purchase Order",
                                                transId);
          selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("cust_name","Customer Name",100),
                                                          new DBAttributes("cust_id","Customer ID",75),
                                                          null,
                                                          new DBAttributes("vendor_id","Vendor ID",75),
                                                          new DBAttributes("vendor_name","Vendor Name",100),
                                                          null,
                                                          new DBAttributes("shipto_id","Ship To ID",75),
                                                          new DBAttributes("shipto_name","Ship To Name",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",75),
                                                          new DBAttributes("created_by","Created By",100)});
//      selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450)});
    }
}
