
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package daiBeans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.borland.jbcl.layout.BoxLayout2;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class DataChooser extends JDialog
{
	JPanel panel_root = new JPanel();
	daiButton button_ok = new daiButton();
	daiButton button_cancel = new daiButton();
	JPanel buttonPanel = new JPanel();

	BusinessObject OBJ;
    JFrame          PARENT_FRAME;

	String ID_CHOICE = "";
	daiLabel label_title = new daiLabel();
	Logger logger;
    BusinessObject _chosenObj;

	//Client Server Adapters.
	csDBAdapterFactory  dbAdapterFactory;
	csDBAdapter         dbAdapter;
    SessionMetaData     _sessionMeta;
    FlowLayout flowLayout1 = new FlowLayout();
    BoxLayout2 boxLayout21 = new BoxLayout2();
    JPanel jPanel_title = new JPanel();
    JPanel jPanel_listBox = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    daiGrid _daiGrid = new daiGrid();
    DBAttributes[]    _gridAttribs;
    daiLabel daiLabel_more = new daiLabel();
    String _sqlWhereExp = "";
    String _sqlOrderByExp = "";
    String _tableName = "";

    /**
      * @param  frame is the parent fram that called this control
      * @param  title will be used to display the title on the chooser window
      * @param  displayAttribs an array of attributes which will be used as
      *         the columns to display on the chooser window.
      * @param  whereExp is the where clause expression used to filter the
      *         rows which will be displayed in by the chooser.  The key word
      *         "WHERE" should NOT be used.  Also it is not necessary to provide
      *         the "locality" field in the whereExp (it is done by default).
      *         If null is provided, only the default where cluase will be used, such
      *         as locality.  Example: <i> name = 'DAI' and priority = 1 </i>
      * @param  orderByExp should contain the sql "order by" clause.  If this param is
      *         null, the rows will be ordered by ID.  An Example order by clause:
      *         <i> order by name, priority </i>
      * @return     void
      */
	public DataChooser(JFrame frame, String title,
					   BusinessObject obj,
                       DBAttributes[] displayAttribs,
                       String whereExp,
                       String orderByExp)
	{
		super(frame, title, true);

		OBJ = obj;
        PARENT_FRAME = frame;
        _sqlOrderByExp = orderByExp;

        _sqlWhereExp += obj.getTableName()+".locality = '" + obj.getObjLocality() + "'";

        if (whereExp != null) {
            _sqlWhereExp += " and " + whereExp;
        }

        _tableName = obj.getTableName();

		label_title.setText(title);

        _gridAttribs = new DBAttributes[displayAttribs.length];

        for (int i=0; i<displayAttribs.length; i++) {
            _gridAttribs[i] = new DBAttributes();
            _gridAttribs[i] = displayAttribs[i];
            String val = displayAttribs[i].getValue();
            //Add the attributes to the where clause only if it includes
            //a wild card.
            if (val != null && val.indexOf("%") > 0) {
                _sqlWhereExp = _sqlWhereExp + " and " +
                                displayAttribs[i].getName() + " like '" + val + "' ";
            }
        }

		try
		{
			jbInit();
			populateChooser(OBJ.getTableName(), _sqlWhereExp);
			pack();

		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::constructor failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            logger.logError(frame, msg);
		}
	}

	//Initialize Dialog Controls
	void jbInit() throws Exception
	{
        _daiGrid.createColumns(_gridAttribs.length);
        String[] headings = new String[_gridAttribs.length];
        for (int i=0; i<headings.length; i++) {
            if (_gridAttribs[i].getLabel() == null) {
                headings[i] = "";
            } else {
                headings[i] = _gridAttribs[i].getLabel();
            }
        }
        _daiGrid.setHeaderNames(headings);

        logger = Logger.getInstance();
        _sessionMeta = SessionMetaData.getInstance();

        _daiGrid.setTableEditable(false);
        //This section of code adds an action map to the grid.
        //So that when the enter key is hit, do the same as selecting with the mouse.
        //This is to enhance keyboard navigation.
        javax.swing.Action doEnter = new javax.swing.AbstractAction() {
        		public void actionPerformed(ActionEvent e) {
         			if (_daiGrid.getSelectedRow() >= 0){
         				button_ok_actionPerformed();
         			}
        		}
        	};
        _daiGrid.addKeyMapAction(javax.swing.KeyStroke.getKeyStroke("ENTER"), doEnter);

		panel_root.setLayout(boxLayout21);
        button_ok.setLength(80);
        button_ok.setMnemonic(KeyEvent.VK_O);
		button_ok.setText("OK");
        button_ok.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                button_ok_actionPerformed();
            }
        });
        button_cancel.setLength(80);
        button_cancel.setMnemonic(KeyEvent.VK_C);
		button_cancel.setText("Cancel");
		button_cancel.addActionListener(new java.awt.event.ActionListener(){
											   public void actionPerformed(ActionEvent e){
											   buttonControl_cancel_actionPerformed(e);}});
		buttonPanel.setLayout(flowLayout1);
		boxLayout21.setAxis(BoxLayout.Y_AXIS);
        buttonPanel.setMaximumSize(new Dimension(32767, 40));
        buttonPanel.setMinimumSize(new Dimension(160, 40));
        buttonPanel.setPreferredSize(new Dimension(160, 40));
        jPanel_listBox.setLayout(borderLayout1);

        panel_root.setMinimumSize(new Dimension(225, 400));
        panel_root.setPreferredSize(new Dimension(225, 400));

        daiLabel_more.setHREFstyle(true);
        daiLabel_more.setText("more>>");
        daiLabel_more.setForeground(Color.white);
        daiLabel_more.setVisible(false);
        daiLabel_more.adddaiActionListener(new daiActionListener() {
            public void daiActionEvent(daiActionEvent e) {
                getMoreRecords();
            }
        });

        jPanel_title.setBackground(Color.darkGray);
        label_title.setForeground(Color.white);
        getContentPane().add(panel_root);
        panel_root.add(jPanel_title, null);

        jPanel_title.add(label_title, null);
        jPanel_title.add(daiLabel_more, null);

        panel_root.add(jPanel_listBox, null);
        jPanel_listBox.add(_daiGrid, BorderLayout.CENTER);
		panel_root.add(buttonPanel, null);
		buttonPanel.add(button_ok, null);
		buttonPanel.add(button_cancel, null);
        _daiGrid.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                button_ok.setEnabled(true);
            }
        });

		//Instantiate client/server adapters
		dbAdapterFactory    = csDBAdapterFactory.getInstance();
		dbAdapter           = csDBAdapterFactory.getInstance().getDBAdapter();

        _daiGrid.removeAllRows();
	}

    public BusinessObject getChosenObj()
    {
        return _chosenObj;
    }

	private void populateChooser(String tableName, String expression)
	{
        String s_attribs = "";
        int     gridWidth = 0;

        //Set the number of cells and size of each cell
        //in the grid which will contain popup data.
        for (int i=0; i< _gridAttribs.length; i++) {
            s_attribs = s_attribs + _gridAttribs[i].getName();
            if (_gridAttribs[i].getAttribLength() == 0) {
                gridWidth = gridWidth + daiGrid.DEFAULT_ID_COL_WIDTH+20;
                _daiGrid.setColumnSize(i, daiGrid.DEFAULT_ID_COL_WIDTH+20);
            } else {
                gridWidth = gridWidth + _gridAttribs[i].getAttribLength()+20;
                _daiGrid.setColumnSize(i, _gridAttribs[i].getAttribLength()+20);
            }
            if (i < (_gridAttribs.length-1)) s_attribs = s_attribs + ", ";
        }
        //
        String sqlStmt = " select " + s_attribs + " from " + tableName;
        if (expression != null && expression.trim().length() > 0) {
            sqlStmt = sqlStmt + " where " + expression;
        }

        //Check to see if an order by clause has already been supplied.
        //If not, default to "order by id".
        if (_sqlOrderByExp != null) {
            sqlStmt += _sqlOrderByExp;
        } else {
            sqlStmt += " order by id ";
        }

        panel_root.setMinimumSize(new Dimension(gridWidth+20, 400));
        panel_root.setPreferredSize(new Dimension(gridWidth+20, 400));
        this.pack();

		try
		{
            _daiGrid.setRowSelectionEnabled(false);

			//Execute the Select Stmt.
			DBRecSet attribs = dbAdapter.execDynamicSQL(_sessionMeta.getClientServerSecurity(),
                                                sqlStmt);
            int collectionSize = attribs.getSize();

			//Populate the ID listbox.
			for (int i = 0; i<collectionSize; i++)
			{
                DBRec attribSet = attribs.getRec(i);
				_daiGrid.addRow();
                for (int j=0; j<_gridAttribs.length; j++) {
                    _daiGrid.set(_daiGrid.getRowCount()-1, j, attribSet.getAttrib(_gridAttribs[j].getName()).getValue());
                }
			}

            label_title.setText(_daiGrid.getRowCount() + " Found");
            _daiGrid.setRowFocus(0);

            if (collectionSize > _sessionMeta.getMaxDBSelectROws()) {
                daiLabel_more.setVisible(true);
            } else {
                daiLabel_more.setVisible(false);
            }

            _daiGrid.setRowSelectionEnabled(true);

            button_ok.setEnabled(false);

		} catch (Exception e)
		{
			e.printStackTrace();
            String msg = this.getClass().getName() + "::constructor failure." +
                            "\n"+e.toString()+"\n"+e.getLocalizedMessage();
            logger.logError(PARENT_FRAME, msg);
		}

	}

	void buttonControl_cancel_actionPerformed(ActionEvent e)
	{
        this.setVisible(false);
	}

    private void getMoreRecords()
    {
        String id = (String)_daiGrid.get(_daiGrid.getRowCount()-1, 0);
        String newExp;
        if (_sqlWhereExp == null || _sqlWhereExp.trim().length() > 0) {
            newExp = " id > '" + id + "'";
        } else {
            newExp = _sqlWhereExp + " and id > '" + id + "'";
        }
    	populateChooser(_tableName, newExp);
    }

    private void button_ok_actionPerformed() {
        String id = (String)_daiGrid.get(_daiGrid.getSelectedRow(), 0);
		String exp = OBJ.getTableName()+".id = " + "'" + id + "'" +
                    " and " + OBJ.getTableName() +".locality = '" + OBJ.getObjLocality() + "'";

		try
		{
			Vector objVect = dbAdapter.queryByExpression(_sessionMeta.getClientServerSecurity(),
                                                            OBJ,
                                                            exp);
			if (objVect.size() != 0)
			{
				_chosenObj = (BusinessObject)objVect.firstElement();
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
            String msg = this.getClass().getName() + "::constructor failure." +
                            "\n"+ex.toString()+"\n"+ex.getLocalizedMessage();
            logger.logError(PARENT_FRAME, msg);
		}

        this.setVisible(false);
    }
}
