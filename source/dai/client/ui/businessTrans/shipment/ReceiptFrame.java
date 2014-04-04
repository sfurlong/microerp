
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;

import javax.swing.JFrame;

import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.cash_receiptObj;

public class ReceiptFrame extends daiFrame
{
   ReceiptPanel      mainTab;
   cash_receiptObj   receiptObj;

   public ReceiptFrame(JFrame container)
   {
      super (container, new cash_receiptObj());

      //Let's get our own local copy of the BusinessObject.
      receiptObj           = new cash_receiptObj();

      //Set the title for this Window.
      this.setTitle("Receipt Entry/Update");
      this.setBannerLeftText("Receipt Entry/Update");

      try
      {
         jbInit();

         //Initialize the query parameter for the listbox.
         pack();

      } catch (Exception ex)
      {
         System.out.println("resourceDialog.constructor");
         ex.printStackTrace();
      }
   }

   void jbInit() throws Exception
   {
      mainTab       = new ReceiptPanel(CONTAINER, this, receiptObj);

      //Let our Ansestor know about the Panel.
      addTabPanel("Main", mainTab);

//      selectionBox.addSearchFilters(new String[]{"check_num",
//                                                    "shipment_id",
//                                                    "cust_id",
//                                                    "cust_name"});
//      //make ID the default search value
//      selectionBox.setComboBoxText("ID");
         selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("check_num","Check Number",100),
                                                          new DBAttributes("shipment_id","Shipment ID",100),
                                                          null,
                                                          new DBAttributes("cust_id","Customer ID",100),
                                                          new DBAttributes("cust_name","Customer Name",200),
                                                          null,
                                                          new DBAttributes("date_created","Date Created",100),
                                                          new DBAttributes("created_by","Created By",100)});
   }
}


