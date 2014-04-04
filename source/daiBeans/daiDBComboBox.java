
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      DAI
//Description:  Beans

package daiBeans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class daiDBComboBox extends JComboBox implements Serializable, Cloneable {

    Vector _dataVect;
    DefaultComboBoxModel _model;
    private transient Vector daiDataModifiedListeners;
    private boolean _isDisabled = false;
    public static final String ADD_NEW_ELEMENT = "<<Add New>>";
    csDBAdapter _dbAdapter = csDBAdapterFactory.getInstance().getDBAdapter();
    BusinessObject _bizObj = null;
    String _sqlExp = null;
    boolean _doCheckForUserChanges = true;
    JPopupMenu _popupMenu = new JPopupMenu();

    public daiDBComboBox(BusinessObject obj, String exp) {
        try  {
            _bizObj = obj;
            _sqlExp = exp;
            populateComboBox();
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        JMenuItem popupMenuItem = _popupMenu.add("Delete Item?");

        popupMenuItem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                doDeleteItemFromTable();
            }
        });

        this.getComponent(0).addMouseListener(new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if (e.isMetaDown()){
                    doRightMouseClick(e);
                }
            }
        });

        this.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
               fireDaiDataModified(new daiDataModifiedEvent(e));
            }
        });
        this.setEnabled(true);
        this.setFont(new Font("Dialog", 0, 11));
        this.setMaximumSize(new Dimension(32767, 21));
        this.setMinimumSize(new Dimension(0, 21));
        this.setPreferredSize(new Dimension(35, 21));
        this.setSelectedIndex(-1);
    }

    public String getText() {
        return (String)this.getSelectedItem();
    }

    public void setText(String t) {
        this.setEditable(true);
        _doCheckForUserChanges = false;
        this.setSelectedItem(t);
        if (!_isDisabled) {
            fireDaiDataModified(new daiDataModifiedEvent(this));
        }
        _doCheckForUserChanges = true;
        this.setEditable(false);
    }

    public void setDisabled(boolean disable)
    {
        if (disable)
        {
            this.setBackground(Color.lightGray);
            this.setEditable(false);
            _isDisabled = true;
        } else {
            this.setBackground(Color.white);
            this.setEditable(true);
            _isDisabled = false;
        }
    }

    public synchronized void removedaiDataModifiedListener(daiDataModifiedListener l)
    {
        if(daiDataModifiedListeners != null && daiDataModifiedListeners.contains(l))
        {
            Vector v = (Vector) daiDataModifiedListeners.clone();
            v.removeElement(l);
            daiDataModifiedListeners = v;
        }
    }

    public synchronized void adddaiDataModifiedListener(daiDataModifiedListener l)
    {
        Vector v = daiDataModifiedListeners == null ? new Vector(2) : (Vector) daiDataModifiedListeners.clone();
        if(!v.contains(l))
        {
            v.addElement(l);
            daiDataModifiedListeners = v;
        }
    }

    protected void fireDaiDataModified(daiDataModifiedEvent e)
    {
        String selectedItem = (String)this.getSelectedItem();
        if(_doCheckForUserChanges && selectedItem != null && selectedItem.equals(ADD_NEW_ELEMENT)) {
            addNewIdToTable();
        }
        if(daiDataModifiedListeners != null)
        {
            Vector listeners = daiDataModifiedListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++)
            {
                ((daiDataModifiedListener) listeners.elementAt(i)).daiDataModified(e);
            }
        }
    }

    protected void addNewIdToTable() {
        String newId = null;

        UserInputDialog inputDialog = new UserInputDialog(null,
                                                        "Add New Value",
                                                        "Enter Value To Add.",
                                                        UserInputDialog.TEXT_INPUT_TYPE);
        int userChoice = inputDialog.getUserButtonChoice();
        if (userChoice == UserInputDialog.OK_OPTION) {
            newId = inputDialog.getValueEntered();
        } else {
            _model = new DefaultComboBoxModel(_dataVect);
            setModel(_model);
            setSelectedIndex(-1);
            return;
        }

        _bizObj.clear(true);
        _bizObj.setDefaults();
        _bizObj.set_id(newId);
        try {
            _dbAdapter.insert(SessionMetaData.getInstance().getClientServerSecurity(),
                                _bizObj);
            populateComboBox();
            this.setSelectedItem(newId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDeleteItemFromTable() {
        String idToDelete = getText();

        int userChoice = JOptionPane.showConfirmDialog(this,
                            "Really Delete This Id: " + idToDelete,
                            "Delete?",
                            JOptionPane.YES_NO_OPTION);
        if (userChoice != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            _dbAdapter.delete(SessionMetaData.getInstance().getClientServerSecurity(),
                                _bizObj,
                                " id = '" + idToDelete + "' and locality='" +_bizObj.getObjLocality()+"'");
            populateComboBox();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateComboBox() {
        //Get the table contents and populate the comboBox.
        _doCheckForUserChanges = false;
        try {
            _dataVect = _dbAdapter.getAllIds(SessionMetaData.getInstance().getClientServerSecurity(),
                                            _bizObj.getTableName(),
                                            _sqlExp);
            _dataVect.add(0, ADD_NEW_ELEMENT);
            _model = new DefaultComboBoxModel(_dataVect);
            setModel(_model);
            setSelectedIndex(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        _doCheckForUserChanges = true;
    }

    private void doRightMouseClick(MouseEvent e) {
        if (getText() != null) {
            _popupMenu.show(this, e.getX(), e.getY());
        }
    }
}

