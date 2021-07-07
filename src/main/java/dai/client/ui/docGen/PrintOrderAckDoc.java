package dai.client.ui.docGen;

import javax.swing.JFrame;

import dai.client.clientShared.daiDocPrintFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.cust_orderObj;

public class PrintOrderAckDoc extends daiDocPrintFrame {

    public PrintOrderAckDoc(JFrame container) {

        super(container, new cust_orderObj(),
                                                "orderAck.rpt",
                                                new DefaultDocPrintPanel("orderAck.rpt"),
                                                "Print Order Acknowledgement");
//        selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450),});
          selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("customer_name","Customer Name",200),
                                                          new DBAttributes("customer_id","Customer ID",100),
                                                          null,
                                                          new DBAttributes("item_id","Item ID",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",100),
                                                          new DBAttributes("created_by","Created By",100),
                                                          null,
                                                          new DBAttributes("po_num","PO Number",100)});
    }

    public PrintOrderAckDoc(JFrame container, String transId) {

        super(container, new cust_orderObj(),
                                                "orderAck.rpt",
                                                new DefaultDocPrintPanel("orderAck.rpt"),
                                                "Print Order Ack.",
                                                transId);
//        selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450),});
        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("customer_name","Customer Name",200),
                                                          new DBAttributes("customer_id","Customer ID",100),
                                                          null,
                                                          new DBAttributes("item_id","Item ID",100),
                                                          null,
                                                          new DBAttributes("date_created","Date Created On",100),
                                                          new DBAttributes("created_by","Created By",100),
                                                          null,
                                                          new DBAttributes("po_num","PO Number",100)});
    }
}
