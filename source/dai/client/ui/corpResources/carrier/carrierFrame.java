
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.corpResources.carrier;

import javax.swing.JFrame;

import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.carrierObj;

public class carrierFrame extends daiFrame
{
   carrierMainPanel    mainTab;
   carrierJournalPanel journalTab;
   carrierContactPanel contactTab;

   carrierObj          carrObj;
//   carrier_journalObj  cust_journalObj;
//   carrier_contactObj  cust_contactObj;

   public carrierFrame(JFrame container)
   {
      super (container, new carrierObj());

      //Let's get our own local copy of the customerObj.
      carrObj           = new carrierObj();
//      cust_journalObj   = new customer_journalObj();
//      cust_contactObj   = new customer_contactObj();

      //Set the title for this Window.
      this.setTitle("Carrier Entry/Update");
      this.setBannerLeftText("Carrier Entry/Update");

      try
      {
         jbInit();
         pack();

      } catch (Exception ex)
      {
         System.out.println("resourceDialog.constructor");
         ex.printStackTrace();
      }
   }

   public carrierFrame(JFrame container, String carrierId)
   {
      super (container, new carrierObj());

      //Let's get our own local copy of the customerObj.
      carrObj           = new carrierObj();
//      cust_journalObj   = new customer_journalObj();
//      cust_contactObj   = new customer_contactObj();

      //Set the title for this Window.
      this.setTitle("Carrier Entry/Update");
      this.setBannerLeftText("Carrier Entry/Update");

      try
      {
         jbInit();
         pack();

         if (carrierId != null) {
            callBackInsertNewId(carrierId);
         }
      } catch (Exception ex)
      {
         System.out.println("resourceDialog.constructor");
         ex.printStackTrace();
      }
   }

   void jbInit() throws Exception
   {
      mainTab       = new carrierMainPanel(CONTAINER, this, carrObj);
//      journalTab    = new carrierJournalPanel(this, cust_journalObj);
//      contactTab    = new carrierContactPanel(this, cust_contactObj);

      //Let our Ansestor know about the Panel.
      addTabPanel("Main", mainTab);
//      addTabPanel("Contacts", contactTab);
//      addTabPanel("Journal", journalTab);

      //make ID the default search value
//      selectionBox.setComboBoxText("ID");
//      selectionBox.addSearchFilters(new String[]{});
      selectionBox.addSearchFilters(new DBAttributes[]{});
   }
}


