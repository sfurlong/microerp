
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
import dai.shared.businessObjs.accountObj;
import dai.shared.businessObjs.account_detailObj;
import dai.shared.cmnSvcs.Logger;

public class FinanceAcctsFrame extends daiFrame
{
   FinanceAcctsPanel    mainTab;
   FinanceAcctsDetailPanel   detailTab;
   accountObj           acctObj;
   account_detailObj    detailObj;
   Logger               _logger;

   public FinanceAcctsFrame(JFrame container)
   {
      super (container, new accountObj());

      acctObj           = new accountObj();
      detailObj         = new account_detailObj();

      _logger = _logger.getInstance();

      //Set the title for this Window.
      this.setTitle("Financial Accounts Entry/Update");
      this.setBannerLeftText("Financial Accounts Entry/Update");

      try
      {
            jbInit();

            //Initialize the query parameter for the listbox.
            pack();

      } catch (Exception e)
      {
            String msg = this.getClass().getName()+"::constructor failure"+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            _logger.logError(CONTAINER, msg);
      }
   }

   void jbInit() throws Exception
   {
      mainTab       = new FinanceAcctsPanel(CONTAINER, this, acctObj);
      detailTab     = new FinanceAcctsDetailPanel(CONTAINER, this, detailObj);

      //Let our Ansestor know about the Panel.
      addTabPanel("Main", mainTab);
      addTabPanel("Detail", detailTab);

//      selectionBox.addSearchFilters(new String[]{"description",
//                                                  "account_type"});
      //make ID the default search value
//      selectionBox.setComboBoxText("ID");
        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("description","Description",400),
                                                          new DBAttributes("account_type","Account Type",200)});
   }
}


