
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.purchOrder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiFrameMenuBar;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.payment_voucherObj;

public class PayVoucherFrame extends daiFrame
{
   PayVoucherPanel      mainTab;
   PayVoucherAddrPanel  addrTab;
   payment_voucherObj   payVouchObj;
   MenuActionListener menuListener = new MenuActionListener();

   public PayVoucherFrame(JFrame container)
   {
      super (container, new payment_voucherObj());

      //Let's get our own local copy of the BusinessObject.
      payVouchObj           = new payment_voucherObj();

      //Set the title for this Window.
      this.setTitle("Payment Voucher Entry/Update");
      this.setBannerLeftText("Payment Voucher Entry/Update");

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
      mainTab       = new PayVoucherPanel(CONTAINER, this, payVouchObj);
      addrTab       = new PayVoucherAddrPanel(CONTAINER, this, payVouchObj);

      //Add subpanel
      mainTab.addSubPanel(addrTab);

      //Let our Ansestor know about the Panel.
      addTabPanel("Main", mainTab);
      addTabPanel("Payee Info", addrTab);

        selectionBox.addSearchFilters(new DBAttributes[]{null,
                                                          new DBAttributes("vendor_id","Vendor ID",100),
                                                          new DBAttributes("vendor_name","Vendor Name",200),
                                                          null,
                                                          new DBAttributes("purch_order_id","Purchase Order ID",150),
                                                          null,
                                                          new DBAttributes("invoice_num","Invoice Number",100),
                                                          new DBAttributes("check_num","Check Number",150),
                                                          null,
                                                          new DBAttributes("date_created","Date Created",100),
                                                          new DBAttributes("created_by","Created By",100)});

        JMenuItem actionMenuItem = new JMenuItem("Void Voucher");
        actionMenuItem.setMnemonic('V');
        actionMenuItem.addActionListener(menuListener);


        daiMenuBar.getMenu(daiFrameMenuBar.ACTION_MENU).setEnabled(true);
        daiMenuBar.getMenu(daiFrameMenuBar.ACTION_MENU).add(actionMenuItem);
   }

	//Override from base class so we can extend the logic.
    protected void fileDelete()
	{
        JOptionPane.showMessageDialog(CONTAINER, "Can't delete Payment Vouchers.  If necessary, use Void Voucher instead",
                              "Warning",
                              JOptionPane.WARNING_MESSAGE, null);
    }

    class MenuActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String menuAction = e.getActionCommand();
            if (menuAction.equals("Void Voucher")) {

               int ret = JOptionPane.showConfirmDialog(null,"Are You Sure?\n" + "This action cannot be reversed.\n" +
                                                "Previously posted journal entries will be reversed.",
                                                "Question", JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE);
                if (ret == JOptionPane.YES_OPTION) {
                    PayVoucherPanel panel = (PayVoucherPanel)getTabPanel(0);
                    boolean bret = panel.voidPayment();
                    if (bret) {
                        fileSave();
                       JOptionPane.showMessageDialog(null,"Payment Voucher Successfuly Voided.",
                                                "Confirm",
                                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }
    }
}


