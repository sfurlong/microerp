// Title: Your Product Name
//Version:
//Copyright: Copyright (c) 1998
//Author: Stephen P. Furlong
//Company: DAI
//Description: Beans

package daiBeans;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.VerticalFlowLayout;

import dai.client.clientShared.daiColors;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class daiSelectionPanel extends JPanel implements Serializable {

    DBRecSet _gridRecSet = new DBRecSet();

    daiGrid _daiGrid = new daiGrid();

    Logger m_logger;

    //Adapter Factory for getting handles to the Client Server Adapters
    csDBAdapterFactory dbAdapterFactory;

    csDBAdapter dbAdapter;

    SessionMetaData sessionMeta;

    private ListSelectionListener _listSelectionListener;

    private BusinessObject m_businessObj;

    BoxLayout2 boxLayout21 = new BoxLayout2();

    JPanel controlPanel = new JPanel();

    private transient Vector daiGenericEventListeners;

    private Vector vecSearchFilters = new Vector();

    daiFormPanel formPanel_searchFilter = new daiFormPanel();

    GroupBox groupBox_searchfilters = new GroupBox();

    daiLabel daiLabel_numRecs = new daiLabel();

    daiLabel daiLabel_more = new daiLabel();

    daiLabel daiLabel_prev = new daiLabel();

    daiLabel daiLabel_next = new daiLabel();

    JPanel jPanel_status = new JPanel();

    String _moreWhereClause = "";

    boolean _doMore = false;

    boolean _doPrev = false;

    boolean _doNext = false;

    daiLabel daiLabel_search = new daiLabel();

    daiLabel daiLabel_todays = new daiLabel();

    VerticalFlowLayout verticalFlowLayout1 = new VerticalFlowLayout();

    VerticalFlowLayout verticalFlowLayout3 = new VerticalFlowLayout();

    JPanel jPanel_search = new JPanel();

    daiButton daiButton_search = new daiButton();

    daiButton daiButton_todays = new daiButton();

    BoxLayout2 boxLayout22 = new BoxLayout2();

    BoxLayout2 boxLayout23 = new BoxLayout2();

    public daiSelectionPanel() {
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
    	daiButton_search.setMnemonic(KeyEvent.VK_S);
    	daiButton_todays.setMnemonic(KeyEvent.VK_T);
        formPanel_searchFilter.addItem(new DBAttributes("ID", "ID", 100));
        boxLayout23.setAxis(BoxLayout.X_AXIS);
        verticalFlowLayout3.setAlignment(verticalFlowLayout3.CENTER);
        groupBox_searchfilters.setLabel("Search Filters");
        groupBox_searchfilters
                .addActionListener(new daiSelectionPanel_groupBox_searchfilters_actionAdapter(
                        this));
        groupBox_searchfilters.setLayout(verticalFlowLayout3);
        _daiGrid.setTableEditable(false);
        _listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                _daiGrid_rowSelected(e);
            }

        };
        _daiGrid.setMinimumSize(new Dimension(24, 25));
        _daiGrid.setPreferredSize(new Dimension(260, 23));

        daiLabel_prev.setHREFstyle(true);
        daiLabel_prev.setVerticalTextPosition(SwingConstants.TOP);
        daiLabel_prev.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_prev_actionEvent();
            }
        });
        daiLabel_prev.setText("prev");
        daiLabel_prev.setForeground(Color.blue);
        daiLabel_prev.setVerticalAlignment(SwingConstants.TOP);

        daiLabel_next.setHREFstyle(true);
        daiLabel_next.setVerticalTextPosition(SwingConstants.TOP);
        daiLabel_next.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                daiLabel_next_actionEvent();
            }
        });
        daiLabel_next.setText("next");
        daiLabel_next.setForeground(Color.blue);
        daiLabel_next.setVerticalAlignment(SwingConstants.TOP);

        daiLabel_numRecs.setVerticalTextPosition(SwingConstants.TOP);
        daiLabel_numRecs.setText("Found");
        daiLabel_numRecs.setForeground(Color.blue);
        daiLabel_numRecs.setVerticalAlignment(SwingConstants.TOP);
        daiLabel_prev.setVisible(false);
        daiLabel_next.setVisible(false);
        daiLabel_more.setVisible(false);
        daiLabel_numRecs.setVisible(false);

        controlPanel.setBackground(daiColors.PanelColor);
        controlPanel.setLayout(boxLayout22);

        jPanel_status.setBackground(daiColors.PanelColor);
        jPanel_status.setBorder(BorderFactory.createEtchedBorder());
        jPanel_status.setMaximumSize(new Dimension(32767, 23));
        jPanel_status.setMinimumSize(new Dimension(85, 23));
        jPanel_status.setPreferredSize(new Dimension(85, 23));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        daiButton_search.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                _daiGrid.removeListSelectionListener(_listSelectionListener);
                daiButton_search_actionPerformed(false, false, false);
                _daiGrid.addListSelectionListener(_listSelectionListener);
            }
        });
        daiButton_todays.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                _daiGrid.removeListSelectionListener(_listSelectionListener);
                daiButton_search_actionPerformed(true, false, false);
                _daiGrid.addListSelectionListener(_listSelectionListener);
            }
        });
        
        //This section of code adds an action map to the grid.
        //So that when the enter key is hit, do the same as selecting with the mouse.
        //This is to enhance keyboard navigation.
        javax.swing.Action doEnter = new javax.swing.AbstractAction() {
        		public void actionPerformed(ActionEvent e) {
         			if (_gridRecSet.getSize() > 0 && _daiGrid.getSelectedRow() >= 0){
         				fireGenericEventAction(new daiGenericEvent(e));
         			}
        		}
        	};
        _daiGrid.addKeyMapAction(javax.swing.KeyStroke.getKeyStroke("ENTER"), doEnter);

        daiButton_search.setText("Search");
        daiButton_todays.setText("Todays");
        _daiGrid.removeAllRows();

        //Adapter Factory for getting handles to the Client Server Adapters
        dbAdapterFactory = csDBAdapterFactory.getInstance();
        dbAdapter = dbAdapterFactory.getDBAdapter();
        m_logger = Logger.getInstance();
        sessionMeta = SessionMetaData.getInstance();

        boxLayout21.setAxis(BoxLayout.Y_AXIS);
        jPanel_search.setMaximumSize(new Dimension(32767, 30));
        jPanel_search.setMinimumSize(new Dimension(163, 30));
        jPanel_search.setPreferredSize(new Dimension(163, 30));
        jPanel_status.add(daiLabel_prev, null);
        jPanel_status.add(daiLabel_numRecs, null);
        jPanel_status.add(daiLabel_next, null);

        this.setLayout(boxLayout21);
        this.add(controlPanel, null);
        controlPanel.add(groupBox_searchfilters);

        groupBox_searchfilters.add(formPanel_searchFilter, null);
        groupBox_searchfilters.add(jPanel_search, null);
        jPanel_search.setBackground(daiColors.PanelColor);
        jPanel_search.add(daiButton_search, null);
        jPanel_search.add(daiButton_todays, null);
        this.add(_daiGrid, null);
        this.add(jPanel_status, null);

    }

    //allows the default value to be set elsewhere
    public void setComboBoxText(String s) {
    }

    public void addSearchFilters(String[] whereStrings) {
        formPanel_searchFilter.addItems(whereStrings);
        vecSearchFilters.addElement("ID");
        boolean noDateCreatedFound = true;
        for (int i = 0; i < whereStrings.length; i++) {
            if (!whereStrings[i].toUpperCase().equals("ITEM_ID")
                    && !whereStrings[i].toUpperCase().equals(
                            "COMP_NAME+LAST_NAME")
                    && !whereStrings[i].toUpperCase().equals("< ID")
                    && !whereStrings[i].toUpperCase().equals("> ID")) {
                if (whereStrings[i].toUpperCase().indexOf("DATE_CREATED") == -1) {
                    vecSearchFilters.addElement(whereStrings[i]);
                } else if (noDateCreatedFound) {
                    vecSearchFilters.addElement("date_created");
                    noDateCreatedFound = false;
                    daiButton_todays.setEnabled(true);
                }
            }
        }
        vecSearchFilters.trimToSize();
        _daiGrid.createColumns(vecSearchFilters.size());
        String[] tempArr = new String[vecSearchFilters.size()];
        for (int i = 0; i < vecSearchFilters.size(); i++) {
            tempArr[i] = vecSearchFilters.elementAt(i).toString().toUpperCase();
        }
        _daiGrid.setHeaderNames(tempArr);
    }

    public void addSearchFilters(DBAttributes[] _gridAttribs) {
        Vector vecSearchLabels = new Vector();
        Vector vecGridColSizes = new Vector();
        formPanel_searchFilter.addItems(_gridAttribs);
        vecSearchFilters.addElement("ID");
        vecSearchLabels.addElement("ID");
        vecGridColSizes.addElement(new Integer(100));
        boolean noDateCreatedFound = true;
        for (int i = 0; i < _gridAttribs.length; i++) {
            if (_gridAttribs[i] != null) {
                if (!_gridAttribs[i].getName().toUpperCase().equals("ITEM_ID")
                        && !_gridAttribs[i].getName().toUpperCase().equals(
                                "COMP_NAME+LAST_NAME")
                        && !_gridAttribs[i].getName().toUpperCase().equals(
                                "< ID")
                        && !_gridAttribs[i].getName().toUpperCase().equals(
                                "> ID")) {
                    if (_gridAttribs[i].getName().toUpperCase().indexOf(
                            "DATE_CREATED") == -1) {
                        vecSearchFilters.addElement(_gridAttribs[i].getName());
                        vecSearchLabels.addElement(_gridAttribs[i].getLabel());
                        vecGridColSizes.addElement(new Integer(_gridAttribs[i]
                                .getAttribLength()));
                    } else if (noDateCreatedFound) {
                        vecSearchFilters.addElement("DATE_CREATED");
                        vecSearchLabels.addElement(_gridAttribs[i].getLabel());
                        vecGridColSizes.addElement(new Integer(_gridAttribs[i]
                                .getAttribLength()));
                        noDateCreatedFound = false;
                        daiButton_todays.setEnabled(true);
                    }
                }
            }
        }
        vecSearchFilters.trimToSize();
        vecSearchLabels.trimToSize();
        _daiGrid.createColumns(vecSearchLabels.size());
        String[] tempArr = new String[vecSearchLabels.size()];
        for (int i = 0; i < vecSearchLabels.size(); i++) {
            tempArr[i] = vecSearchLabels.elementAt(i).toString();
            _daiGrid.setColumnSize(i, ((Integer) vecGridColSizes.elementAt(i))
                    .intValue());
        }
        _daiGrid.setHeaderNames(tempArr);
        if (noDateCreatedFound)
            daiButton_todays.setEnabled(false);
    }

    public BusinessObject getBusinessObj() {
        return m_businessObj;
    }

    public void setBusinessObj(BusinessObject obj) {
        m_businessObj = obj;
    }

    public String getSelectedItem() {
        return (String) _daiGrid.get(_daiGrid.getSelectedRow(), 0);
    }

    public void reset() {
        formPanel_searchFilter.resetAllTextField();
        _daiGrid.removeAllRows();
        daiLabel_numRecs.setVisible(false);
    }

    public synchronized void removedaiGenericEventListener(
            daiGenericEventListener l) {
        if (daiGenericEventListeners != null
                && daiGenericEventListeners.contains(l)) {
            Vector v = (Vector) daiGenericEventListeners.clone();
            v.removeElement(l);
            daiGenericEventListeners = v;
        }
    }

    public synchronized void adddaiGenericEventListener(
            daiGenericEventListener l) {
        Vector v = daiGenericEventListeners == null ? new Vector(2)
                : (Vector) daiGenericEventListeners.clone();
        if (!v.contains(l)) {
            v.addElement(l);
            daiGenericEventListeners = v;
        }
    }

    protected void fireGenericEventAction(daiGenericEvent e) {
        if (daiGenericEventListeners != null) {
            Vector listeners = daiGenericEventListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++) {
                ((daiGenericEventListener) listeners.elementAt(i))
                        .genericEventAction(e);
            }
        }
    }

    private void _daiGrid_rowSelected(ListSelectionEvent e) {
        if (_gridRecSet.getSize() > 0) {
            if (e.getValueIsAdjusting())
                fireGenericEventAction(new daiGenericEvent(e));
        }
    }

    protected void daiButton_search_actionPerformed(boolean enableSearchTodays,
            boolean enableSearchPrev, boolean enableSearchNext) {
        //Check for null Business Objects
        if (m_businessObj == null) {
            m_logger.logError(null, "Table Name not specified");
            return;
        }
        String tableName = m_businessObj.getTableName();

        try {
            String sqlSelect = "";
            String sqlFrom = "";
            String sqlWhere = " where ";
            String filterType = "";
            String itemTableName = "";
            Enumeration searchFilterNames = formPanel_searchFilter
                    .getAllFieldNames();
            if (searchFilterNames != null) {
                while (searchFilterNames.hasMoreElements()) {
                    filterType = (String) searchFilterNames.nextElement();
                    if (formPanel_searchFilter.getTextFieldValue(filterType) != null
                            && !(formPanel_searchFilter
                                    .getTextFieldValue(filterType).equals(""))) {
                        if (filterType.toUpperCase().equals("DATE_CREATED")) {
                            if (!enableSearchTodays) {
                                sqlWhere += tableName
                                        + "."
                                        + filterType
                                        + " = '"
                                        + formPanel_searchFilter
                                                .getTextFieldValue(filterType)
                                        + "'";
                            } else {
                                Calendar now = Calendar.getInstance();
                                String today = now.get(Calendar.MONTH) + 1 + "/"
                                        + now.get(Calendar.DAY_OF_MONTH) + "/"
                                        + now.get(Calendar.YEAR);
                                formPanel_searchFilter.setTextFieldValue(
                                        "date_created", today);
                                sqlWhere += tableName + "." + filterType
                                        + " = '" + today + "'";
                            }
                        } else if (filterType.toUpperCase().equals(
                                "< DATE_CREATED")) {
                            sqlWhere += tableName
                                    + "."
                                    + "DATE_CREATED < '"
                                    + formPanel_searchFilter
                                            .getTextFieldValue(filterType)
                                    + "'";
                        } else if (filterType.toUpperCase().equals(
                                "> DATE_CREATED")) {
                            sqlWhere += tableName
                                    + "."
                                    + "DATE_CREATED > '"
                                    + formPanel_searchFilter
                                            .getTextFieldValue(filterType)
                                    + "'";
                        } else if (filterType.toUpperCase().equals("< ID")) {
                            sqlWhere += tableName
                                    + "."
                                    + "ID > '"
                                    + formPanel_searchFilter
                                            .getTextFieldValue(filterType)
                                    + "'";
                        } else if (filterType.toUpperCase().equals("> ID")) {
                            sqlWhere += tableName
                                    + "."
                                    + "ID < '"
                                    + formPanel_searchFilter
                                            .getTextFieldValue(filterType)
                                    + "'";
                        } else if (filterType.toUpperCase().equals(
                                "COMP_NAME+LAST_NAME")) {
                            //This option is special purpose for the Prospect
                            // Module.
                            String pspectSearch = getProspectSearchVals(
                                    tableName, formPanel_searchFilter
                                            .getTextFieldValue(filterType));
                            if (pspectSearch != null
                                    && pspectSearch.length() > 0) {
                                sqlWhere += getProspectSearchVals(tableName,
                                        formPanel_searchFilter
                                                .getTextFieldValue(filterType));
                            } else {
                                return;
                            }
                        } else if (filterType.toUpperCase().equals("ITEM_ID")) {
                            //Set tableName explicitly here because item_id is
                            // not in mzin table
                            itemTableName = tableName + "_ITEM";
                            sqlWhere += tableName
                                    + "_ITEM"
                                    + ".ITEM_ID like '"
                                    + formPanel_searchFilter
                                            .getTextFieldValue(filterType)
                                    + "%" + "'" + " and " + tableName + "_ITEM"
                                    + ".ID=" + tableName + ".ID";
                        } else {
                            sqlWhere += "UPPER("
                                    + tableName
                                    + "."
                                    + filterType
                                    + ") like '"
                                    + formPanel_searchFilter.getTextFieldValue(
                                            filterType).toUpperCase() + "%"
                                    + "'";
                        }
                        sqlWhere += " and ";
                    } else if (filterType.toUpperCase().equals("DATE_CREATED")) {
                        if (enableSearchTodays) {
                            Calendar now = Calendar.getInstance();
                            String today = now.get(Calendar.MONTH) + 1 + "/"
                                    + now.get(Calendar.DAY_OF_MONTH) + "/"
                                    + now.get(Calendar.YEAR);
                            formPanel_searchFilter.setTextFieldValue(
                                    "date_created", today);
                            sqlWhere += tableName + "." + filterType + " = '"
                                    + today + "'";
                        }
                    }

                }
            }
            if (sqlWhere.endsWith("and "))
                sqlWhere = sqlWhere.substring(0, sqlWhere.length() - 4);
            if (sqlWhere.equals(" where ")) {
                if (!_moreWhereClause.equals("")) {
                    sqlWhere += _moreWhereClause;
                } else {
                    sqlWhere = "";
                }
            } else if (!_moreWhereClause.equals("")) {
                sqlWhere += " and " + _moreWhereClause;
            }
            sqlSelect = "select distinct "/*
                                           * + journalTableName + ".SUBJECT, " +
                                           * journalTableName + ".NOTE"
                                           */;
            if (vecSearchFilters != null) {
                for (int col = 0; col < vecSearchFilters.size(); col++) {
                    String selectField = (String) vecSearchFilters
                            .elementAt(col);
                    sqlSelect += tableName + "." + selectField.toUpperCase()
                            + ", ";
                }
            }
            if (sqlSelect.endsWith(", "))
                sqlSelect = sqlSelect.substring(0, sqlSelect.length() - 2)
                        + " ";

            sqlFrom = " from "
                    + (itemTableName.equals("") ? "" : itemTableName + ", ")
                    + tableName/*
                                * + " left outer join " + journalTableName + "
                                * on " + tableName+ ".ID=" + journalTableName +
                                * ".ID "
                                */;
            
            //No filters, should we search anyway?
            if (sqlWhere.trim().length() == 0 ) {
        		int retVal = JOptionPane.showOptionDialog(this,
        				"No Filters have been selected.  Search Anyway?",
        				"Search", JOptionPane.YES_NO_OPTION,
        				JOptionPane.QUESTION_MESSAGE, null, null, null);

        		if (retVal == JOptionPane.NO_OPTION) {
        		    return;
        		}
            }
            
            //System.out.println(sqlSelect + sqlFrom + sqlWhere);
            DBRecSet _newGridData = dbAdapter.execDynamicSQL(sessionMeta
                    .getClientServerSecurity(), sqlSelect + sqlFrom + sqlWhere);

            if (_doNext) {
                _doNext = false;
                _doPrev = true;
            }
            if (_doPrev) {
                _doPrev = false;
                _doNext = true;
            }
            _gridRecSet = _newGridData;

            _daiGrid.removeAllRows();
            if (_gridRecSet != null && _gridRecSet.getSize() > 0) {
                for (int row = 0; row < _gridRecSet.getSize(); row++) {
                    if (vecSearchFilters != null) {
                        _daiGrid.addRow();
                        for (int col = 0; col < vecSearchFilters.size(); col++) {
                            String selectField = (String) vecSearchFilters
                                    .elementAt(col);
                            String field = _gridRecSet
                                    .getRec(row)
                                    .getAttribVal(
                                            tableName + "."
                                                    + selectField.toUpperCase());
                            if (field == null)
                                field = "";
                            _daiGrid.set(row, col, field);
                        }
                    }
                }
            }
            _daiGrid.repaint();

            daiLabel_numRecs
                    .setText("   Displaying: "
                            + ((_gridRecSet != null && _gridRecSet.getSize() >= 1) ? _gridRecSet
                                    .getRec(0).getAttribVal(tableName + ".ID")
                                    : "")
                            + " - "
                            + ((_gridRecSet != null && _gridRecSet.getSize() >= 2) ? _gridRecSet
                                    .getRec(_gridRecSet.getSize() - 1)
                                    .getAttribVal(tableName + ".ID")
                                    : "") + "   ");
            daiLabel_numRecs.setVisible(true);

            if (_gridRecSet.getSize() > sessionMeta.getMaxDBSelectROws()) {
                if (!enableSearchPrev && !enableSearchNext) {
                    daiLabel_next.setVisible(true);
                } else {
                    daiLabel_next.setVisible(true);
                }
            } else {
                if (!enableSearchPrev && !enableSearchNext) {
                    daiLabel_next.setVisible(false);
                } else if (enableSearchPrev) {
                    daiLabel_next.setVisible(false);
                } else if (enableSearchNext) {
                    daiLabel_next.setVisible(true);
                }
            }

            _moreWhereClause = "";
        } catch (Exception ex) {
            m_logger.logError(null, ex.getLocalizedMessage() + ex.toString());
        }

    }

    private void daiLabel_prev_actionEvent() {
        //Check for null Business Objects
        if (m_businessObj == null) {
            m_logger.logError(null, "Table Name not specified");
            return;
        }
        String tableName = m_businessObj.getTableName();
        _doPrev = true;
        if (_gridRecSet != null && _gridRecSet.getSize() > 0) {
            _moreWhereClause = " "
                    + tableName
                    + ".ID < '"
                    + (String) _gridRecSet.getRec(0).getAttribVal(
                            tableName + ".ID") + "' ";
        }
        daiButton_search_actionPerformed(false, false, true);
    }

    private void daiLabel_next_actionEvent() {
        //Check for null Business Objects
        if (m_businessObj == null) {
            m_logger.logError(null, "Table Name not specified");
            return;
        }
        String tableName = m_businessObj.getTableName();

        _doNext = true;
        if (_gridRecSet != null && _gridRecSet.getSize() > 0) {
            _moreWhereClause = " "
                    + tableName
                    + ".ID > '"
                    + (String) _gridRecSet.getRec(_gridRecSet.getSize() - 1)
                            .getAttribVal(tableName + ".ID") + "' ";
        }
        daiButton_search_actionPerformed(false, true, false);
    }

    //This method is used by the prospect module only
    private String getProspectSearchVals(String tableName, String searchString) {
        String ret = "";

        //Check to see if values are seperated by a "+" sign.
        int pos = searchString.indexOf("+");
        if (pos > 0) {
            String compName = searchString.substring(0, pos).trim();
            String lastName = searchString.substring(pos + 1,
                    searchString.length()).trim();
            ret = "UPPER(" + tableName + "." + "company_name) like '%"
                    + compName.toUpperCase() + "%' and UPPER(" + tableName
                    + "." + "last_name) like '%" + lastName.toUpperCase()
                    + "%'";
        }
        return ret;
    }

    void groupBox_searchfilters_actionPerformed(ActionEvent e) {

    }
}

class CustomCellRenderer2 extends JLabel implements ListCellRenderer {
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
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

class daiSelectionPanel_groupBox_searchfilters_actionAdapter implements
        java.awt.event.ActionListener {
    daiSelectionPanel adaptee;

    daiSelectionPanel_groupBox_searchfilters_actionAdapter(
            daiSelectionPanel adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.groupBox_searchfilters_actionPerformed(e);
    }
}