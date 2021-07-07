/**
 * Title:        Your Product Name<p>
 * Description:  Beans<p>
 * Copyright:    Copyright (c) 1998<p>
 * Company:      DAI<p>
 * @author Stephen P. Furlong
 * @version
 */
package dai.client.ui.corpResources.prospect;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.prospect_company_name_idsObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.DataChooser;
import daiBeans.daiActionEvent;
import daiBeans.daiActionListener;
import daiBeans.daiLabel;

public class CompNamePopup extends JPanel
{
    JTextField  textField_id    = new JTextField();
    daiLabel    idLabel         = new daiLabel("Company Name:");
    FlowLayout  flowLayout1     = new FlowLayout();
    BusinessObject _bizObj = null;
    JFrame      _parentFrame = null;
    String      _bizObjField2Name = null;
    JPopupMenu _popupMenu = new JPopupMenu();

    public CompNamePopup(JFrame parentFrame)
    {
        _bizObj = new prospect_company_name_idsObj();
        _parentFrame = parentFrame;

        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void jbInit() throws Exception
    {
        JMenuItem popupMenuItem = _popupMenu.add("Delete Item?");
        popupMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                doDeleteItemFromTable();
            }
        });

        textField_id.setMaximumSize(new Dimension(325, 2147483647));
        textField_id.setMinimumSize(new Dimension(130, 21));
        textField_id.setPreferredSize(new Dimension(325, 21));
        textField_id.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if (e.isMetaDown()){
                    doRightMouseClick(e);
                }
            }
        });
        textField_id.addFocusListener(new java.awt.event.FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                textField_id_focusLost(e);
            }
        });
        textField_id.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                textField_id_keyReleased(e);
            }
        });
        idLabel.setHREFstyle(true);

        idLabel.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                openDataChooser();
            }
        });

        this.setLayout(flowLayout1);
        this.setMaximumSize(new Dimension(425, 32767));
        this.setMinimumSize(new Dimension(425, 31));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(425, 31));
        this.setToolTipText("");
        this.add(idLabel, null);
        this.add(textField_id, null);
    }

    public void setCompName(String name) {
        textField_id.setText(name);
    }

    public String getCompName() {
        return textField_id.getText();
    }

    public void requestFocus() {
        textField_id.requestFocus();
    }

    private void openDataChooser()
    {
        String idFieldText = textField_id.getText();
        String temp = "" + _bizObj.getTableName()+".ID";
        DataChooser chooser = null;

        DBAttributes attrib1 = new DBAttributes(temp, idFieldText, "Id", 200);

        if (idFieldText == null || idFieldText.length()==0) {
  		    chooser = new DataChooser(_parentFrame, "Data Chooser",
			     			   	      _bizObj,
                                      new DBAttributes[]{attrib1},
                                      null, null);
        } else {
           //get a list of names similar to what has been entered
           chooser = new DataChooser(_parentFrame, "Data Choose",
                                  _bizObj,
                                      new DBAttributes[]{attrib1},
                                      temp + " like '" + idFieldText + "%'", null);
        }

		chooser.show();
        BusinessObject chosenObj = chooser.getChosenObj();
        if (chosenObj != null) {
            textField_id.setText(chosenObj.get_id());
        }
    	chooser.dispose();
    }

    private void textField_id_keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_U && e.getModifiers() == KeyEvent.CTRL_MASK) {
            openDataChooser();
        }
    }

    void textField_id_focusLost(FocusEvent e)
    {
        String compNameId = textField_id.getText();
        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        csDBAdapter dbAdapter = csDBAdapterFactory.getInstance().getDBAdapter();

        if (compNameId == null) return;
        if (e.isTemporary() || compNameId.trim().length() == 0) return;


        //Check to see if that field is in the db
        try {
            Vector vect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                    new prospect_company_name_idsObj(),
                                    " id = '" + compNameId + "'");
            if (vect.size() == 0) {
                int answ = JOptionPane.showConfirmDialog(this,
                                                        "The Company Name must exist in the Company Name master file.  Add it now?",
                                                        "Company Name",
                                                        JOptionPane.YES_NO_OPTION);
                if (answ == JOptionPane.YES_OPTION) {
                    prospect_company_name_idsObj obj = new prospect_company_name_idsObj();
                    obj.setDefaults();
                    obj.set_id(compNameId);
                    dbAdapter.insert(sessionMeta.getClientServerSecurity(), obj);
                } else {
                    textField_id.setText(compNameId);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    private void doRightMouseClick(MouseEvent e) {
        if (textField_id.getText() != null) {
            _popupMenu.show(textField_id, e.getX(), e.getY());
        }
    }

    private void doDeleteItemFromTable() {
        String idToDelete = textField_id.getText();
        csDBAdapter dbAdapter = csDBAdapterFactory.getInstance().getDBAdapter();

        int userChoice = JOptionPane.showConfirmDialog(this,
                            "Really Delete This Id: " + idToDelete,
                            "Delete?",
                            JOptionPane.YES_NO_OPTION);
        if (userChoice != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            dbAdapter.delete(SessionMetaData.getInstance().getClientServerSecurity(),
                              _bizObj,
                              " id = '" + idToDelete + "' and locality='" +_bizObj.getObjLocality()+"'");
            textField_id.setText(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
