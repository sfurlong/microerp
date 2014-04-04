package dai.client.ui.docGen;

import javax.swing.JFrame;

import dai.client.clientShared.daiDocPrintFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.shipmentObj;

public class PrintPackSlipDoc extends daiDocPrintFrame {



    public PrintPackSlipDoc(JFrame container) {

        super(container, new shipmentObj(),
                                                "packSlip.rpt",
                                                new DefaultDocPrintPanel("packSlip.rpt"),
                                                "Print Packing Slip");
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
//      selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450)});
    }

    public PrintPackSlipDoc(JFrame container, String transId) {

        super(container,new shipmentObj(),
                                                "packSlip.rpt",
                                                new DefaultDocPrintPanel("packSlip.rpt"),
                                                "Print Packing Slip",
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
//      selectionBox.addSearchFilters(new DBAttributes[]{null,
//                                                          new DBAttributes("customer_id","Customer ID",150),
//                                                          new DBAttributes("customer_name","Customer Name",450)});
    }
}
