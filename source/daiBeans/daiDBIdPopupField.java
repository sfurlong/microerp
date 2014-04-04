
/**
 * Title:        Your Product Name<p>
 * Description:  Beans<p>
 * Copyright:    Copyright (c) 1998<p>
 * Company:      DAI<p>
 * @author Stephen P. Furlong
 * @version
 */
package daiBeans;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class daiDBIdPopupField extends JPanel
{
    daiTextField  textField_id    = new daiTextField();
    daiLabel    idLabel         = new daiLabel("Popup Id");
    FlowLayout  flowLayout1     = new FlowLayout();
    daiTextField  textField_name  = new daiTextField();
    protected BusinessObject _bizObj = null;
    protected JFrame      _parentFrame = null;
    protected String      _bizObjField2Name = null;
    boolean     _isDisabled = false;
    private transient Vector daiActionListeners;
    protected BusinessObject _chosenBizObj = null;
    private String _sqlWhereExp = null;
    private String _sqlOrderByExp = null;

    public daiDBIdPopupField(JFrame parentFrame, BusinessObject bizObj, String labelText)
    {
        _bizObj = bizObj;
        _parentFrame = parentFrame;
        idLabel.setText(labelText);

        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public daiDBIdPopupField(JFrame parentFrame, BusinessObject bizObj,
                            String bizObjField2Name, String labelText)
    {
        _bizObj = bizObj;
        _parentFrame = parentFrame;
        idLabel.setText(labelText);
        _bizObjField2Name = bizObjField2Name;

        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public daiDBIdPopupField(JFrame parentFrame, BusinessObject bizObj,
                            String bizObjField2Name, String labelText,
                            String sqlWhereExp, String sqlOrderByExp)
    {
        _bizObj = bizObj;
        _parentFrame = parentFrame;
        idLabel.setText(labelText);
        _bizObjField2Name = bizObjField2Name;
        _sqlWhereExp = sqlWhereExp;
        _sqlOrderByExp = sqlOrderByExp;

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
        textField_id.setMaximumSize(new Dimension(130, 2147483647));
        textField_id.setMinimumSize(new Dimension(130, 21));
        textField_id.setPreferredSize(new Dimension(130, 21));
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
        textField_name.setMaximumSize(new Dimension(200, 2147483647));
        textField_name.setMinimumSize(new Dimension(200, 21));
        textField_name.setPreferredSize(new Dimension(200, 21));

        textField_name.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyReleased(KeyEvent e)
            {
                textField_name_keyReleased(e);
            }
        });

        idLabel.adddaiActionListener(new daiActionListener()
        {
            public void daiActionEvent(daiActionEvent e)
            {
                openDataChooser();
            }
        });

        this.setLayout(flowLayout1);
        this.setMaximumSize(new Dimension(400, 32767));
        this.setMinimumSize(new Dimension(400, 31));
        this.setOpaque(false);
        this.setPreferredSize(new Dimension(400, 31));
        this.setToolTipText("");
        this.add(idLabel, null);
        this.add(textField_id, null);
        this.add(textField_name, null);
    }


    public void hideField2(boolean flag) {
        textField_name.setVisible(flag);
    }

    public void setName(String name) {
        textField_name.setText(name);
    }
    public String getName() {
        String ret = textField_name.getText();
        if (ret != null && ret.trim().length() == 0) {
            ret = null;
        }
        return ret;
    }
    public void setId(String id) {
        textField_id.setText(id);
    }
    public String getId() {
        String ret = textField_id.getText();
        if (ret != null && ret.trim().length() == 0) {
            ret = null;
        }
        return ret;
    }

    public void requestFocus() {
        textField_id.requestFocus();
    }

    public void setPopupSource(BusinessObject bizObj, String bizObjField2Name, String labelText) {
        _bizObj = bizObj;
        idLabel.setText(labelText);
        _bizObjField2Name = bizObjField2Name;
    }

    public boolean isDisabled() {
        return _isDisabled;
    }

    public void setDisabled(boolean flag)
    {
        idLabel.setDisabled(flag);
        textField_id.setEditable(!flag);
        textField_name.setEditable(!flag);
        _isDisabled = flag;
        if (flag) {
            textField_id.setBackground(Color.lightGray);
            textField_name.setBackground(Color.lightGray);
        } else {
            textField_id.setBackground(Color.white);
            textField_name.setBackground(Color.white);
        }
    }

    protected void openDataChooser()
    {
        String idFieldText = textField_id.getText();
        String nameFieldText = textField_name.getText();
        DataChooser chooser = null;

        DBAttributes attrib1 = new DBAttributes(_bizObj.getTableName()+".ID", idFieldText, "Id", 100);
        DBAttributes attrib2 = new DBAttributes(_bizObjField2Name, nameFieldText, "Name", 200);
        if (_bizObjField2Name == null) {
    		chooser = new DataChooser(_parentFrame, "Data Chooser",
								   	  _bizObj,
                                      new DBAttributes[]{attrib1},
                                      _sqlWhereExp, _sqlOrderByExp);
        } else {
    		chooser = new DataChooser(_parentFrame, "Data Chooser",
								      _bizObj,
                                      new DBAttributes[]{attrib1, attrib2},
                                      _sqlWhereExp, _sqlOrderByExp);
        }

		chooser.show();
        BusinessObject chosenObj = chooser.getChosenObj();
        populateFields(chosenObj);
    	chooser.dispose();
    }

    private void textField_id_focusLost(FocusEvent e)
    {
        if (!e.isTemporary() && !textField_id.isDisabled() && textField_id._isModified) {
            queryBusinessObject();
            textField_id._isModified = false;
        }
    }

    private void queryBusinessObject() {
        csDBAdapter dbAdapter = csDBAdapterFactory.getInstance().getDBAdapter();
        SessionMetaData sessionMeta = SessionMetaData.getInstance();

        String id = textField_id.getText();
        String exp = "";

        if (id != null && id.trim().length() > 0) {
            exp = exp + " id = '" + id + "' " +
                " and locality = " + "'" + _bizObj.getObjLocality() + "'";
        } else {
            _bizObj = null;
            textField_id.setText(null);
            return;
        }

        try {
            Vector vect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                    _bizObj, exp);

            BusinessObject obj = (BusinessObject)vect.firstElement();
            populateFields(obj);

        } catch (Exception e) {
            System.out.println(exp);
            e.printStackTrace();;
        }
    }

    protected void populateFields(BusinessObject obj) {
        if (obj != null) {
            textField_id.setText(obj.get_id());
            DBAttributes[] attribs = obj.getAttribList();
            //Need to determin if the _bizObjField2Name was supplied with
            //the table name prefix.  If so strip it...
            int endOfTablePos = _bizObjField2Name.indexOf(".");
            String field2 = _bizObjField2Name;
            if (endOfTablePos > 0) {
                field2 = _bizObjField2Name.substring(endOfTablePos+1, _bizObjField2Name.length());
            }
            //Find the other field name in the attrib list and use
            //it to populate field2.
            if (_bizObjField2Name != null) {
                for (int i=0; i<attribs.length; i++) {
                    if (attribs[i].getName().equals(field2)) {
                        textField_name.setText(attribs[i].getValue());
                        break;
                    }
                }
            }
            fireDaiActionEvent(new daiActionEvent(obj));
        }
    }

    private void textField_name_keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_U && e.getModifiers() == KeyEvent.CTRL_MASK) {
            openDataChooser();
        }
    }

    private void textField_id_keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_U && e.getModifiers() == KeyEvent.CTRL_MASK) {
            openDataChooser();
        }
    }

    public synchronized void removedaiActionListener(daiActionListener l)
    {
        if(daiActionListeners != null && daiActionListeners.contains(l))
        {
            Vector v = (Vector) daiActionListeners.clone();
            v.removeElement(l);
            daiActionListeners = v;
        }
    }

    public synchronized void adddaiActionListener(daiActionListener l)
    {
        Vector v = daiActionListeners == null ? new Vector(2) : (Vector) daiActionListeners.clone();
        if(!v.contains(l))
        {
            v.addElement(l);
            daiActionListeners = v;
        }
    }

    protected void fireDaiActionEvent(daiActionEvent e)
    {
        if(daiActionListeners != null)
        {
            Vector listeners = daiActionListeners;
            int count = listeners.size();
            for (int i = 0; i < count; i++)
            {
                ((daiActionListener) listeners.elementAt(i)).daiActionEvent(e);
            }
        }
    }
}
