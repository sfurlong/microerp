
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.sysAdmin;

import javax.swing.JFrame;

import dai.client.clientShared.daiFrame;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.default_accountsObj;

public class DefaultAccountsFrame extends daiFrame
{
   DefaultAccountsPanel    mainTab;
   default_accountsObj      defaultAccountsObj;

   public DefaultAccountsFrame(JFrame container)
   {
      super (container, new default_accountsObj());

      //Let's get our own local copy of the customerObj.
      defaultAccountsObj   =  new default_accountsObj();

      //Set the title for this Window.
      this.setTitle("Default Accounts Entry/Update");
      this.setBannerLeftText("Default Accounts Entry/Update");

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
      mainTab       = new DefaultAccountsPanel(CONTAINER, this, defaultAccountsObj);

      //Let our Ansestor know about the Panel.
      addTabPanel("Main", mainTab);

      //make ID the default search value
//      selectionBox.setComboBoxText("ID");
//      selectionBox.addSearchFilters(new String[]{});
      selectionBox.addSearchFilters(new DBAttributes[]{});
   }
}


