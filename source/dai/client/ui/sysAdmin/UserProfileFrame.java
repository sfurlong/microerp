
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
import dai.shared.businessObjs.user_profileObj;
import dai.shared.businessObjs.user_securityObj;

public class UserProfileFrame extends daiFrame
{
   UserProfilePanel    mainTab;
   UserSecurityPanel   securityTab;
   user_profileObj     profObj;
   user_securityObj    securityObj;

   public UserProfileFrame(JFrame container)
   {
      super (container, new user_profileObj());

      //Let's get our own local copy of the customerObj.
      profObj      = new user_profileObj();
      securityObj  = new user_securityObj();

      //Set the title for this Window.
      this.setTitle("User Profile Entry/Update");
      this.setBannerLeftText("User Profile Entry/Update");

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
      mainTab       = new UserProfilePanel(CONTAINER, this, profObj);
      securityTab   = new UserSecurityPanel(CONTAINER, this, securityObj);

      //Let our Ansestor know about the Panel.
      addTabPanel("Main", mainTab);
      addTabPanel("Security", securityTab);

      //make ID the default search value
      selectionBox.setComboBoxText("ID");
      selectionBox.addSearchFilters(new DBAttributes[]{});

   }

   //Override the base class so that we can force a save to happen
   //when the user hits save.  This is necessary because the security
   //grid does not fire change events.
   //To get around this we will always save the security settings when
   //the user hits save, regardless if any changes where made.
   protected void fileSave() {
      //Ignore the save if there is no ID
      if (mainTab.getActiveBusinessObj().get_id() != null &&
            mainTab.getActiveBusinessObj().get_id().length() > 0) {
          securityTab.setPanelIsDirty();;
          super.fileSave();
      }
   }
}


