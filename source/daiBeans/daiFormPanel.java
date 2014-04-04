
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      DAI
//Description:  Beans

package daiBeans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.Serializable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import dai.client.clientShared.daiColors;
import dai.shared.businessObjs.DBAttributes;

public class daiFormPanel extends JPanel implements Serializable, Cloneable {

    Hashtable dataHash = new Hashtable();
    private transient Vector daiDataModifiedListeners;
    private boolean _isDisabled = false;
    BorderLayout borderLayout1 = new BorderLayout();
    Vector fieldPanel_vec = new Vector();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();

    public daiFormPanel() {
        try  {
            //dataHash = new Hashtable();
            jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void jbInit() throws Exception {
//        this.addActionListener(new java.awt.event.ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                fireDaiDataModified(new daiDataModifiedEvent(e));
//            }
//        });
      //this.setFont(new Font("Dialog", 0, 11));
      //GridBagConstraints gbc = new GridBagContraints();
      this.setLayout(gridBagLayout1);
      this.setBackground(daiColors.PanelColor);
      gbc.fill = GridBagConstraints.NONE;
      gbc.weightx = 0;
      gbc.weighty = 0;


//      constraint_names_panel.setBackground(dai.client.clientShared.daiColors.PanelColor);
//      constraint_names_panel.setLayout(verticalFlowLayout1);
//      constraint_values_panel.setBackground(dai.client.clientShared.daiColors.PanelColor);
//      constraint_values_panel.setLayout(verticalFlowLayout2);
//      this.add(constraint_names_panel, BorderLayout.WEST);
//      this.add(constraint_values_panel, BorderLayout.CENTER);
    }

//    public void setBackground(java.awt.Color _color)  {
//      //super.setBackground(_color);
//      constraint_values_panel.setBackground(_color);
//      constraint_names_panel.setBackground(_color);
//    }
    public void addItems(Object[] objs)
    {

        for (int i=0; i<objs.length; i++) {
            daiLabel fieldname = new daiLabel(objs[i].toString()+":");
            JComponent value;

            if(objs[i].toString().toUpperCase().indexOf("DATE") == -1)  {
              value = new daiTextField();
              ((daiTextField)value).setColumns(90);
            }
            else  {
              value = new daiDateField();
            }

            fieldname.setHorizontalAlignment(SwingConstants.RIGHT);
            value.setName(objs[i].toString());
            dataHash.put(value.getName(),value);
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            gbc.insets = new Insets(3,3,3,3);
            gbc.anchor = GridBagConstraints.EAST;
            gridBagLayout1.setConstraints(fieldname, gbc);
            this.add(fieldname,null);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(3,3,3,3);
            gbc.anchor = GridBagConstraints.WEST;
            gridBagLayout1.setConstraints(value, gbc);
            this.add(value, null);
        }
        this.invalidate();
        this.revalidate();
        fireDaiDataModified(new daiDataModifiedEvent(this));
    }
    public void addItems(DBAttributes[] _gridAttribs)
    {
        for (int i=0; i<_gridAttribs.length; i++) {
            if(_gridAttribs[i] != null)  {
              daiLabel fieldname = new daiLabel(_gridAttribs[i].getLabel()+":");
              JComponent value;
              if(_gridAttribs[i].getName().toUpperCase().indexOf("DATE") == -1)  {
                value = new daiTextField();
                ((daiTextField)value).setColumns(90);
              }
              else  {
                value = new daiDateField();
              }

              fieldname.setHorizontalAlignment(SwingConstants.RIGHT);
              value.setName(_gridAttribs[i].getName());
              dataHash.put(value.getName(),value);
              //gbc.gridwidth = GridBagConstraints.RELATIVE;
              gbc = new GridBagConstraints();
              gbc.insets = new Insets(3,3,3,3);
              gbc.ipadx = 20;
              gbc.anchor = GridBagConstraints.EAST;
              gridBagLayout1.setConstraints(fieldname, gbc);
              this.add(fieldname,null);
              //gbc.gridwidth = GridBagConstraints.RELATIVE;
              gbc = new GridBagConstraints();
              gbc.insets = new Insets(3,3,3,3);
              gbc.anchor = GridBagConstraints.WEST;
              gridBagLayout1.setConstraints(value, gbc);
              this.add(value, null);
           }
           else  {
              daiLabel fieldname = new daiLabel("");
              daiLabel value = new daiLabel("");
              gbc.gridwidth = GridBagConstraints.RELATIVE;
//              gbc = new GridBagConstraints();
              gbc.insets = new Insets(3,3,3,3);
              gbc.anchor = GridBagConstraints.EAST;
              gridBagLayout1.setConstraints(fieldname, gbc);
              this.add(fieldname,null);
              gbc.gridwidth = GridBagConstraints.REMAINDER;
//              gbc = new GridBagConstraints();
              gbc.insets = new Insets(3,3,3,3);
              gbc.anchor = GridBagConstraints.WEST;
              gridBagLayout1.setConstraints(value, gbc);
              this.add(value, null);
           }
        }
        this.invalidate();
        this.revalidate();
        fireDaiDataModified(new daiDataModifiedEvent(this));
    }
    public void addItem(DBAttributes _gridAttrib)
    {   try {
        if(_gridAttrib != null)  {
          daiLabel fieldname = new daiLabel(_gridAttrib.getLabel()+":");
          JComponent value;

              if(_gridAttrib.getName().toUpperCase().indexOf("DATE") == -1)  {
                value = new daiTextField();
                ((daiTextField)value).setColumns(90);
              }
              else  {
                value = new daiDateField();
              }

          fieldname.setHorizontalTextPosition(SwingConstants.RIGHT);
          value.setName(_gridAttrib.getName());
          dataHash.put(value.getName(),value);
//              gbc.gridwidth = GridBagConstraints.RELATIVE;
              gbc.insets = new Insets(3,3,3,3);
              gbc.anchor = GridBagConstraints.EAST;
              gridBagLayout1.setConstraints(fieldname, gbc);
              this.add(fieldname,null);
//              gbc.gridwidth = GridBagConstraints.REMAINDER;
              gbc.insets = new Insets(3,3,3,3);
              gbc.anchor = GridBagConstraints.WEST;
              gridBagLayout1.setConstraints(value, gbc);
              this.add(value, null);
        }
        else  {
              daiLabel fieldname = new daiLabel("");
              daiLabel value = new daiLabel("");
              gbc.gridwidth = GridBagConstraints.RELATIVE;
              gbc.insets = new Insets(3,3,3,3);
              gbc.anchor = GridBagConstraints.EAST;
              gridBagLayout1.setConstraints(fieldname, gbc);
              this.add(fieldname,null);
              gbc.gridwidth = GridBagConstraints.REMAINDER;
              gbc.insets = new Insets(3,3,3,3);
              gbc.anchor = GridBagConstraints.WEST;
              gridBagLayout1.setConstraints(value, gbc);
              this.add(value, null);


        }
        this.invalidate();
        this.revalidate();
        fireDaiDataModified(new daiDataModifiedEvent(this));
      }
      catch(Exception e)  {e.printStackTrace();System.out.println(e.getLocalizedMessage());}
    }
    public void addItem(Object obj)
    {   try {
        daiLabel fieldname = new daiLabel(obj.toString()+":");
        JComponent value;

            if(obj.toString().toUpperCase().indexOf("DATE") == -1)  {
              value = new daiTextField();
              ((daiTextField)value).setColumns(90);
            }
            else  {
              value = new daiDateField();
            }

        fieldname.setHorizontalTextPosition(SwingConstants.RIGHT);
        value.setName(obj.toString());
        dataHash.put(value.getName(),value);
            gbc.gridwidth = GridBagConstraints.RELATIVE;
            gbc.insets = new Insets(3,3,3,3);
            gbc.anchor = GridBagConstraints.EAST;
            gridBagLayout1.setConstraints(fieldname, gbc);
            this.add(fieldname,null);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.insets = new Insets(3,3,3,3);
            gbc.anchor = GridBagConstraints.WEST;
            gridBagLayout1.setConstraints(value, gbc);
            this.add(value, null);
        this.invalidate();
        this.revalidate();
        fireDaiDataModified(new daiDataModifiedEvent(this));
      }
      catch(Exception e)  {e.printStackTrace();}
    }

    public String getTextFieldValue(String fieldname) {
        if(dataHash.get(fieldname) == null)
          return null;
        if(dataHash.get(fieldname) instanceof daiTextField)
          return ((daiTextField)dataHash.get(fieldname)).getText();
        else
          return ((daiDateField)dataHash.get(fieldname)).getText();
    }
    public void setTextFieldValue(String fieldname, String fieldvalue)  {
        if(dataHash.get(fieldname) != null)  {
          if(dataHash.get(fieldname) instanceof daiTextField)
            ((daiTextField)dataHash.get(fieldname)).setText(fieldvalue);
          else
            ((daiDateField)dataHash.get(fieldname)).setText(fieldvalue);
        }
    }
    public Enumeration getAllFieldNames()  {
      return dataHash.keys();
    }

    public void disableTextField(String fieldname)
    {
        JComponent tempfield = (JComponent)dataHash.get(fieldname);
        if (tempfield != null)
        {
            tempfield.setBackground(Color.lightGray);
            tempfield.setEnabled(false);

        }
        fireDaiDataModified(new daiDataModifiedEvent(this));
    }
    public void enableTextField(String fieldname)
    {
        JComponent tempfield = (JComponent)dataHash.get(fieldname);
        if (tempfield != null)
        {
            tempfield.setBackground(Color.white);
            tempfield.setEnabled(true);
        }
        fireDaiDataModified(new daiDataModifiedEvent(this));
    }
    public void disableAllTextField()
    {
        Collection tempValues = dataHash.values();
        if(tempValues != null)  {
          for(int i=0; i<tempValues.size(); i++)
          {
            JComponent tempfield = (JComponent)(tempValues.toArray())[i];
            if (tempfield != null)
            {
              tempfield.setBackground(Color.lightGray);
              tempfield.setEnabled(false);
            }
          }
          fireDaiDataModified(new daiDataModifiedEvent(this));
        }
    }
    public void enableAllTextField()
    {
        Collection tempValues = dataHash.values();
        if(tempValues != null)  {
          for(int i=0; i<tempValues.size(); i++)
          {
            JComponent tempfield = (JComponent)(tempValues.toArray())[i];
            if (tempfield != null)
            {
              tempfield.setBackground(Color.white);
              tempfield.setEnabled(true);
            }
          }
          fireDaiDataModified(new daiDataModifiedEvent(this));
        }
    }
    public void resetAllTextField()
    {
        Collection tempValues = dataHash.values();
        if(tempValues != null)  {
          for(int i=0; i<tempValues.size(); i++)
          {
            JComponent tempfield = (JComponent)(tempValues.toArray())[i];
            if (tempfield != null)
            {
              if(tempfield instanceof daiTextField)
                ((daiTextField)tempfield).setText("");
              else
                ((daiDateField)tempfield).setText("");
            }
          }
          fireDaiDataModified(new daiDataModifiedEvent(this));
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
}

