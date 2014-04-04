
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
import dai.shared.businessObjs.locationObj;

public class LocationFrame extends daiFrame
{
   LocationPanel    mainTab;
   locationObj          locObj;

   public LocationFrame(JFrame container)
   {
      super (container, new locationObj());

      //Let's get our own local copy of the customerObj.
      locObj           = new locationObj();

      //Set the title for this Window.
      this.setTitle("Corporate Locations Entry/Update");
      this.setBannerLeftText("Corporate Locations Entry/Update");

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
      mainTab       = new LocationPanel(CONTAINER, this, locObj);

      //Let our Ansestor know about the Panel.
      addTabPanel("Main", mainTab);

      //make ID the default search value
//      selectionBox.setComboBoxText("ID");
//      selectionBox.addSearchFilters(new String[]{});
      selectionBox.addSearchFilters(new DBAttributes[]{});
   }
}


