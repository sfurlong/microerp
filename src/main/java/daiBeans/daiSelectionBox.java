
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      DAI
//Description:  Beans

package daiBeans;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;

import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class daiSelectionBox extends JPanel implements Serializable{

    JTextField jTextField_searchString      = new JTextField();
    Vector _listData             = new Vector();
    JScrollPane jScrollPane1    = new JScrollPane();
    JList jList                 = new JList();
    Logger m_logger;

    //Adapter Factory for getting handles to the Client Server Adapters
    csDBAdapterFactory  dbAdapterFactory;
    csDBAdapter         dbAdapter;
    SessionMetaData     sessionMeta;

    private BusinessObject m_businessObj;
    BoxLayout2 boxLayout21 = new BoxLayout2();
    JPanel controlPanel = new JPanel();
    private transient Vector daiGenericEventListeners;
    daiComboBox comboBox_searchFilter = new daiComboBox();
    daiLabel daiLabel_numRecs = new daiLabel();
    daiLabel daiLabel_more = new daiLabel();
    daiLabel daiLabel_searchFilter = new daiLabel();
    JPanel jPanel_status = new JPanel();
    JPanel jPanel_button = new JPanel();
    String _moreWhereClause = "";
    boolean _doMore = false;
    daiLabel daiLabel_search = new daiLabel();
    BoxLayout2 boxLayout22 = new BoxLayout2();
    XYLayout xYLayout1 = new XYLayout();
    daiLabel daiLabel_todays = new daiLabel();

    public daiSelectionBox() {
        try  {
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        comboBox_searchFilter.setDisabled(false);
        comboBox_searchFilter.setBackground(Color.white);
        comboBox_searchFilter.setFont(new java.awt.Font("Dialog", 0, 10));
        comboBox_searchFilter.addItem("ID");
        jList.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                jList_mouseClicked(e);
            }
        });
        jScrollPane1.setMinimumSize(new Dimension(24, 25));
        jScrollPane1.setPreferredSize(new Dimension(260, 300));
        jTextField_searchString.setMaximumSize(new Dimension(2147483647, 22));
        jTextField_searchString.setMinimumSize(new Dimension(0, 22));
        jTextField_searchString.setPreferredSize(new Dimension(0, 22));

        daiLabel_more.setHREFstyle(true);
        daiLabel_more.setVerticalTextPosition(SwingConstants.TOP);
        daiLabel_more.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_more_actionEvent();
            }
        });
        daiLabel_more.setText("more>>");
        daiLabel_more.setForeground(Color.white);
        daiLabel_more.setVerticalAlignment(SwingConstants.TOP);
        daiLabel_numRecs.setVerticalTextPosition(SwingConstants.TOP);
        daiLabel_numRecs.setText("Found");
        daiLabel_numRecs.setForeground(Color.white);
        daiLabel_numRecs.setVerticalAlignment(SwingConstants.TOP);
        daiLabel_more.setVisible(false);
        daiLabel_numRecs.setVisible(false);

        controlPanel.setBackground(daiColors.BannerGrey);
        controlPanel.setLayout(boxLayout22);
        comboBox_searchFilter.setText("ID");
        daiLabel_searchFilter.setHorizontalTextPosition(SwingConstants.LEFT);
        daiLabel_searchFilter.setPreferredSize(new Dimension(35, 15));
        daiLabel_searchFilter.setMaximumSize(new Dimension(35, 15));
        daiLabel_searchFilter.setMinimumSize(new Dimension(35, 15));
        daiLabel_searchFilter.setText("Filters:");
        daiLabel_searchFilter.setForeground(Color.white);
        daiLabel_searchFilter.setHorizontalAlignment(SwingConstants.LEFT);
        daiLabel_searchFilter.setFont(new java.awt.Font("Dialog", 0, 10));
        jPanel_status.setBackground(daiColors.BannerGrey);
        jPanel_status.setBorder(BorderFactory.createEtchedBorder());
        jPanel_status.setMaximumSize(new Dimension(32767, 25));
        jPanel_status.setMinimumSize(new Dimension(85, 25));
        jPanel_status.setPreferredSize(new Dimension(85, 25));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel_button.setLayout(xYLayout1);
        jPanel_button.setBackground(daiColors.BannerGrey);
        jPanel_button.setMaximumSize(new Dimension(2147483647, 38));
        jPanel_button.setMinimumSize(new Dimension(50, 38));
        jPanel_button.setPreferredSize(new Dimension(50, 38));
        daiLabel_search.setHREFstyle(true);
        daiLabel_search.setHorizontalTextPosition(SwingConstants.LEFT);
        daiLabel_search.setText("Search");
        daiLabel_search.setForeground(Color.white);
        daiLabel_search.setHorizontalAlignment(SwingConstants.LEFT);
        daiLabel_search.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiButton_search_actionPerformed();
            }
        });
        boxLayout22.setAxis(BoxLayout.Y_AXIS);
        daiLabel_todays.setHREFstyle(true);
        daiLabel_todays.setText("Get Today\'s");
        daiLabel_todays.setToolTipText("Get all Ids created Today.");
        daiLabel_todays.setForeground(Color.white);
        daiLabel_todays.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiButton_todays_actionPerformed();
            }
        });
        jScrollPane1.getViewport().add(jList, null);

        //Adapter Factory for getting handles to the Client Server Adapters
        dbAdapterFactory = dbAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();
        m_logger = m_logger.getInstance();
        sessionMeta = sessionMeta.getInstance();

        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        jPanel_status.add(daiLabel_numRecs, null);
        jPanel_status.add(daiLabel_more, null);

        this.setLayout(boxLayout21);
        this.add(controlPanel, null);

        controlPanel.add(jPanel_button, null);
        jPanel_button.add(comboBox_searchFilter, new XYConstraints(33, 20, 101, 17));
        jPanel_button.add(daiLabel_search, new XYConstraints(5, 1, -1, -1));
        jPanel_button.add(daiLabel_todays, new XYConstraints(62, 1, -1, 16));
        jPanel_button.add(daiLabel_searchFilter, new XYConstraints(0, 19, 33, 21));
        controlPanel.add(jTextField_searchString, null);
        this.add(jScrollPane1);
        this.add(jPanel_status, null);

    }

    //allows the default value to be set elsewhere
    public void setComboBoxText(String s)
    {
       comboBox_searchFilter.setText(s);
    }

    public void addSearchFilters(String[] s)
    {
        comboBox_searchFilter.addItems(s);
    }

    public BusinessObject getBusinessObj() {
        return m_businessObj;
    }

    public void setBusinessObj(BusinessObject obj) {
        m_businessObj = obj;
    }

    public String getSelectedItem()
    {
        return (String) jList.getSelectedValue();
    }

    public synchronized void removedaiGenericEventListener(daiGenericEventListener l)
    {
        if(daiGenericEventListeners != null && daiGenericEventListeners.contains(l))
        {
            Vector v = (Vector) daiGenericEventListeners.clone();
            v.removeElement(l);
            daiGenericEventListeners = v;
        }
    }

    public synchronized void adddaiGenericEventListener(daiGenericEventListener l)
    {
        Vector v = daiGenericEventListeners == null ? new Vector(2) : (Vector) daiGenericEventListeners.clone();
        if(!v.contains(l))
        {
            v.addElement(l);
            daiGenericEventListeners = v;
        }
    }

    protected void fireGenericEventAction(daiGenericEvent e)
    {
        if(daiGenericEventListeners != null)
        {
            Vector listeners = daiGenericEventListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++)
            {
                ((daiGenericEventListener) listeners.elementAt(i)).genericEventAction(e);
            }
        }
    }

    //This method tells all the listeners of this control when a mouse
    //event occurs.
    private void jList_mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2 && _listData.size() > 0)
        {
            fireGenericEventAction(new daiGenericEvent(e));
        }
    }

    protected void daiButton_search_actionPerformed()
    {
        //Check for null Business Objects
        if (m_businessObj == null)
        {
            m_logger.logError(null, "Table Name not specified");
            return;
        }
        String tableName = m_businessObj.getTableName();
        String searchString = jTextField_searchString.getText();
        if (searchString == null || searchString.length() == 0) {
            jTextField_searchString.setText("");
        }
        try {
            String filterType = comboBox_searchFilter.getText();
            String sqlWhere = "";
            if (filterType.toUpperCase().equals("DATE_CREATED")) {
                sqlWhere = comboBox_searchFilter.getText()+" = '" +
                            jTextField_searchString.getText() + "'" +
                                _moreWhereClause;
            } else if (filterType.toUpperCase().equals("< DATE_CREATED")) {
                sqlWhere = "DATE_CREATED < '" + jTextField_searchString.getText() + "'" +
                                _moreWhereClause;
            } else if (filterType.toUpperCase().equals("> DATE_CREATED")) {
                sqlWhere = "DATE_CREATED > '" + jTextField_searchString.getText() + "'" +
                                _moreWhereClause;
            } else if (filterType.toUpperCase().equals("COMP_NAME+LAST_NAME")) {
                //This option is special purpose for the Prospect Module.
                String pspectSearch = getProspectSearchVals();
                if (pspectSearch != null && pspectSearch.length() > 0) {
                    sqlWhere = getProspectSearchVals() +
                                _moreWhereClause;
                } else {
                    return;
                }
            } else if (filterType.toUpperCase().equals("ITEM_ID")) {
               //Set tableName explicitly here because item_id is not in mzin table
               tableName += "_ITEM";
               sqlWhere = "ITEM_ID like '" + "%" + jTextField_searchString.getText() + "%" + "'" +
                           _moreWhereClause;
            } else {
                sqlWhere = "UPPER(" + comboBox_searchFilter.getText() + ") like '" + "%" + jTextField_searchString.getText().toUpperCase() + "%" + "'" +
                                _moreWhereClause;
            }

            System.out.println(sqlWhere);
            Vector newListData = dbAdapter.getAllIds(sessionMeta.getClientServerSecurity(),
                                        tableName,
                                        sqlWhere);

            if (_doMore) {
                _listData.addAll(_listData.size(), newListData);
                _doMore = false;
            } else {
                _listData = newListData;
            }
            jList.setListData(_listData);
            jList.setCellRenderer( new CustomCellRenderer());
            jList.repaint();

            daiLabel_numRecs.setText(_listData.size() + " Found");
            daiLabel_numRecs.setVisible(true);

            if (newListData.size() > sessionMeta.getMaxDBSelectROws()) {
                daiLabel_more.setVisible(true);
            } else {
                daiLabel_more.setVisible(false);
            }

            _moreWhereClause = "";
        } catch (Exception ex) {
            m_logger.logError(null, ex.getLocalizedMessage());
        }
    }

    private void daiButton_todays_actionPerformed() {
        //Check for null Business Objects
        if (m_businessObj == null)
        {
            m_logger.logError(null, "Table Name not specified");
            return;
        }
        String tableName = m_businessObj.getTableName();
		Calendar now = Calendar.getInstance();
		String today = now.get(now.MONTH)+1 + "/" +
					   now.get(now.DAY_OF_MONTH) + "/" +
					   now.get(now.YEAR);

        try {
            String sqlWhere = "date_created = '" + today + "' ";

            Vector newListData = dbAdapter.getAllIds(sessionMeta.getClientServerSecurity(),
                                        tableName,
                                        sqlWhere);

            _listData = newListData;

            jList.setListData(_listData);
            jList.setCellRenderer( new CustomCellRenderer());
            jList.repaint();

            daiLabel_numRecs.setText(_listData.size() + " Found");
            daiLabel_numRecs.setVisible(true);

            _moreWhereClause = "";
        } catch (Exception ex) {
            m_logger.logError(null, ex.getLocalizedMessage());
        }
    }

    private void daiLabel_more_actionEvent() {
        _doMore = true;
        _moreWhereClause = " and id > '" + (String)_listData.lastElement() + "' ";
        daiButton_search_actionPerformed();
    }

    //This method is used by the prospect module only
    private String getProspectSearchVals() {
        String searchString = jTextField_searchString.getText();
        String ret = "";

        //Check to see if values are seperated by a "+" sign.
        int pos = searchString.indexOf("+");
        if (pos > 0) {
            String compName = searchString.substring(0, pos).trim();
            String lastName = searchString.substring(pos+1, searchString.length()).trim();
            ret = "UPPER(company_name) like '%" + compName.toUpperCase() + "%' and UPPER(last_name like) '%" + lastName.toUpperCase() + "%'";
        }
        return ret;
    }
}

class CustomCellRenderer extends JLabel implements ListCellRenderer
{
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                boolean isSelected, boolean cellHasFocus)
    {
        setText(value.toString());

        if (isSelected) {
            setBackground(Color.white);
            setForeground(Color.blue);
        } else {
            setBackground(Color.white);
            setForeground(Color.black);
        }

        return this;
    }
}
