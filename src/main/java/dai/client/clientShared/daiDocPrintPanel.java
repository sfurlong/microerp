
//Title:        clientShared
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Shared Code for Client Software

package dai.client.clientShared;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JPanel;

import com.borland.jbcl.control.GroupBox;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.user_doc_print_optionsObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import daiBeans.daiDataModifiedEvent;
import daiBeans.daiDataModifiedListener;
import daiBeans.daiLabel;
import daiBeans.daiNumField;
import daiBeans.daiTextField;

abstract public class daiDocPrintPanel extends JPanel {
    BorderLayout borderLayout1 = new BorderLayout();
    GroupBox groupBox_printOptions = new GroupBox();
    XYLayout xYLayout1 = new XYLayout();
    daiLabel daiLabel_printerDriver = new daiLabel();
    daiTextField daiTextField_printerDriver = new daiTextField();
    daiLabel daiLabel_printerName = new daiLabel();
    daiTextField daiTextField_printerName = new daiTextField();
    daiLabel daiLabel_printerPort = new daiLabel();
    daiTextField daiTextField_printerPort = new daiTextField();
    daiLabel daiLabel_tray = new daiLabel();
    daiNumField daiNumField_tray = new daiNumField();
    daiLabel daiLabel_copies = new daiLabel();
    daiNumField daiNumField_numCopies = new daiNumField();
    user_doc_print_optionsObj _printOptionsDbObj;
    boolean _settingsChanged = false;
	daiBeans.daiButton printerSetupButton = new daiBeans.daiButton();
    protected JPanel jPanel1 = new JPanel();
    protected String _rptFileName;
    protected daiDocPrintFrame _containerFrame;


    public daiDocPrintPanel(String rptFileName) {
        _rptFileName = rptFileName;

        try  {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
    	
        printerSetupButton.addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        com.altaprise.crystal.CRJNI crjni = new com.altaprise.crystal.CRJNI();
                        Object[] printerSetup = crjni.doPageSetup();
                        if (printerSetup != null) {
	                        daiTextField_printerDriver.setText((String)printerSetup[0]);
	                        daiTextField_printerName.setText((String)printerSetup[1]);
	                        daiTextField_printerPort.setText((String)printerSetup[2]);
                        }
                    }
                });

        groupBox_printOptions.setLabel("Printer Options");
        groupBox_printOptions.setLayout(xYLayout1);
        this.setBackground(daiColors.PanelColor);
        this.setLayout(borderLayout1);
        daiLabel_printerDriver.setText("Printer Driver:");
        daiLabel_printerName.setText("Printer Name:");
        daiLabel_printerPort.setText("Port:");
        daiLabel_tray.setText("Paper Tray:");
        daiLabel_copies.setText("Copies:");
        printerSetupButton.setText("PrinterSetup");
        daiTextField_printerName.setDisabled(true);
        daiTextField_printerDriver.setDisabled(true);
        daiTextField_printerPort.setDisabled(true);
        groupBox_printOptions.add(daiLabel_printerDriver, new XYConstraints(-5, 1, -1, -1));
        groupBox_printOptions.add(daiLabel_printerName, new XYConstraints(-5, 23, -1, -1));
        groupBox_printOptions.add(daiLabel_printerPort, new XYConstraints(37, 46, -1, -1));
        groupBox_printOptions.add(daiLabel_copies, new XYConstraints(280, 1, -1, -1));
        groupBox_printOptions.add(daiTextField_printerDriver, new XYConstraints(62, 1, 193, -1));
        groupBox_printOptions.add(daiTextField_printerName, new XYConstraints(62, 22, 193, -1));
        groupBox_printOptions.add(daiTextField_printerPort, new XYConstraints(62, 44, 193, -1));
        groupBox_printOptions.add(daiNumField_numCopies, new XYConstraints(318, 1, 51, -1));
        groupBox_printOptions.add(printerSetupButton, new XYConstraints(62, 66, 193, -1));

        //We are no longer supporting printer options
        //All printer options are now specified in the CR report.
        this.add(groupBox_printOptions, BorderLayout.SOUTH);

        populatePrinterOptions();
    }

    protected void setContainerFrame(daiDocPrintFrame frame) {
        _containerFrame = frame;
    }

    abstract public int query(String id, BusinessObject obj);

    abstract public void refresh();

    abstract public String getId();

    abstract public String[] getRptParms();

    public String getPrinterDriver() {
        return daiTextField_printerDriver.getText();
    }

    public String getPrinterName() {
        return daiTextField_printerName.getText();
    }

    public String getPort() {
        return daiTextField_printerPort.getText();
    }

    public short getNumCopies() {
        String s_nCopies = daiNumField_numCopies.getText();
        if (s_nCopies == null) s_nCopies = "0";

        short nCopies = Short.parseShort(s_nCopies);

        return nCopies;
    }

    public short getPaperTray() {
        String s_paperTray = daiNumField_tray.getText();
        if (s_paperTray == null) s_paperTray = "0";

        short sh_paperTray = Short.parseShort(s_paperTray);

        return sh_paperTray;
    }

    public boolean isUserSettingsChanged() {
        return _settingsChanged;
    }

    public void savePrinterOptionChanges() {
        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
        SessionMetaData sessionMeta = SessionMetaData.getInstance();

        String exp = " id = '" + sessionMeta.getUserId() + "'" +
                     " and locality = '" + user_doc_print_optionsObj.getObjLocality() + "'" +
                        " and " + user_doc_print_optionsObj.REPORT_NAME + " = '" + _rptFileName + "' ";
        try {
            _printOptionsDbObj.set_printer_name(daiTextField_printerName.getText());
            _printOptionsDbObj.set_printer_port(daiTextField_printerPort.getText());
            _printOptionsDbObj.set_printer_tray(daiNumField_tray.getText());
            _printOptionsDbObj.set_printer_num_copies(daiNumField_numCopies.getText());
            dbAdapter.update(sessionMeta.getClientServerSecurity(), _printOptionsDbObj, exp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populatePrinterOptions() {
        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
        SessionMetaData sessionMeta = SessionMetaData.getInstance();

        String exp = " id = '" + sessionMeta.getUserId() + "'" +
                     " and locality = '" + user_doc_print_optionsObj.getObjLocality() + "'" +
                        " and " + user_doc_print_optionsObj.REPORT_NAME + " = '" + _rptFileName + "' ";

        try {
            Vector vect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                    new user_doc_print_optionsObj(),
                                    exp);
            //This report's user profile does not exist yet, let's add it.
            if (vect.size() == 0) {
                _printOptionsDbObj = new user_doc_print_optionsObj();
                _printOptionsDbObj.set_id(sessionMeta.getUserId());
                _printOptionsDbObj.set_locality(user_doc_print_optionsObj.getObjLocality());
                _printOptionsDbObj.set_report_name(_rptFileName);
                dbAdapter.insert(sessionMeta.getClientServerSecurity(), _printOptionsDbObj);
            } else {
            //Populate the Panel
                _printOptionsDbObj = (user_doc_print_optionsObj)vect.firstElement();

                daiTextField_printerName.setText(_printOptionsDbObj.get_printer_name());
                daiTextField_printerPort.setText(_printOptionsDbObj.get_printer_port());
                daiNumField_tray.setText(_printOptionsDbObj.get_printer_tray());
                daiNumField_numCopies.setText(_printOptionsDbObj.get_printer_num_copies());
        }

        //Add data modified listener only after we populate from DB.
        //That way only user changes will be captured.
        daiTextField_printerName.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e)
            {
                _settingsChanged = true;
            }
        });
        daiTextField_printerPort.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e)
            {
                _settingsChanged = true;
            }
        });
        daiNumField_tray.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e)
            {
                _settingsChanged = true;
            }
        });
        daiNumField_numCopies.adddaiDataModifiedListener(new daiDataModifiedListener()
        {
            public void daiDataModified(daiDataModifiedEvent e)
            {
                _settingsChanged = true;
            }
        });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resetTabEntrySeq() {
        //Intended for the decendants to override.
    }
}
