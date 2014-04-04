package dai.client.ui.docGen;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.jbcl.layout.BoxLayout2;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import dai.client.clientShared.daiColors;
import dai.shared.businessObjs.DBRec;
import dai.shared.businessObjs.DBRecSet;
import dai.shared.businessObjs.payment_voucherObj;
import dai.shared.businessObjs.purch_orderObj;
import dai.shared.cmnSvcs.Logger;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csPurchOrderAdapter;
import dai.shared.csAdapters.csPurchOrderAdapterFactory;
import daiBeans.daiCurrencyField;
import daiBeans.daiGrid;
import daiBeans.daiLabel;
import daiBeans.daiNumField;

class CheckPrintRptPanel extends JPanel
{

    daiGrid _grid = new daiGrid();
    payment_voucherObj[]  _payVoucherObjs;
    SessionMetaData sessionMeta;
    csPurchOrderAdapter purchOrdAdapter;
    protected JFrame  CONTAINER_FRAME;

    CheckPrintRptFrame _containerFrame;

    Logger LOGGER;

    //Grid locations
    final int gridColCheckNum       = 0;
    final int gridColDateDue        = 1;
   	final int gridColVendor         = 2;
    final int gridColRefNo          = 3;
	final int gridColCheckAmt       = 4;
    JPanel jPanel_checkNums = new JPanel();
    daiNumField daiNumField_beginCheckNum = new daiNumField();
    daiNumField daiNumField_endCheckNum = new daiNumField();
    BoxLayout2 boxLayout21 = new BoxLayout2();
    XYLayout xYLayout1 = new XYLayout();
    daiLabel daiLabel_startCheck = new daiLabel();
    daiLabel daiLabel_endingCheck = new daiLabel();

    public CheckPrintRptPanel(JFrame container, CheckPrintRptFrame containerFrame)
    {
        CONTAINER_FRAME = container;
        _containerFrame = containerFrame;

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
        this.setBackground(daiColors.PanelColor);
        this.setMinimumSize(new Dimension(520, 255));
        this.setPreferredSize(new Dimension(520, 255));
        csPurchOrderAdapterFactory purchOrdAdapterFactory = csPurchOrderAdapterFactory.getInstance();
        purchOrdAdapter = purchOrdAdapterFactory.getPurchOrderAdapter();
        sessionMeta = SessionMetaData.getInstance();
        LOGGER = Logger.getInstance();

        _grid.setPreferredSize(new Dimension(600, 200));
        _grid.setMinimumSize(new Dimension(600, 200));

        _grid.createColumns(new int[]{daiGrid.CHAR_COL_TYPE,
                                     daiGrid.DATE_COL_TYPE,
                                     daiGrid.CHAR_COL_TYPE,
                                     daiGrid.CHAR_COL_TYPE,
                                     daiGrid.DOUBLE_COL_TYPE});
        _grid.setHeaderNames(new String[]{"Check#", "Date Due", "Vendor", "PO/Bill Id.",
                                        "Check Amt"});
        //Disable most of the Grid entry fields
        _grid.setColEditable(gridColCheckNum, false);
        _grid.setColEditable(gridColDateDue, false);
        _grid.setColEditable(gridColVendor, false);
        _grid.setColEditable(gridColRefNo, false);
        _grid.setColEditable(gridColCheckAmt, false);
        //Resize some columns
        _grid.setColumnSize(2, 200);
        //Turn off row selection
        _grid.allowRowSelection(false);
        //Set the font to something smaller

        //Populate the grid
    	updateItemGrid();

        boxLayout21.setAxis(BoxLayout.Y_AXIS);

        jPanel_checkNums.setBackground(daiColors.PanelColor);
        jPanel_checkNums.setMaximumSize(new Dimension(2147483647, 55));
        jPanel_checkNums.setMinimumSize(new Dimension(98, 55));
        jPanel_checkNums.setPreferredSize(new Dimension(197, 55));
        jPanel_checkNums.setLayout(xYLayout1);
        daiLabel_startCheck.setText("Starting Check#:");
        daiLabel_endingCheck.setText("Ending Check#:");
        jPanel_checkNums.add(daiNumField_beginCheckNum, new XYConstraints(97, 5, -1, -1));
        jPanel_checkNums.add(daiNumField_endCheckNum, new XYConstraints(97, 28, -1, -1));
        jPanel_checkNums.add(daiLabel_startCheck, new XYConstraints(5, 7, -1, -1));
        jPanel_checkNums.add(daiLabel_endingCheck, new XYConstraints(10, 28, -1, -1));

        this.setLayout(boxLayout21);
        this.add(jPanel_checkNums, null);
        this.add(_grid, null);
    }

    public boolean panelDataIsValid ()
    {
        boolean ret = false;
        String  acct = null;
        Double  d_amtToPay = null;

        /*
        if (!ret) {
            //No Items were selected to Pay.
            JOptionPane userDlg = new JOptionPane();
            userDlg.showMessageDialog(  this,
                                      "Please select at least one Payment to Pay.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
        }
        */
        return ret;
    }

	public void updateItemGrid()
	{
        _grid.removeAllRows();
        daiCurrencyField currencyField = new daiCurrencyField();
        DBRecSet collection;

        try {
            //The following attributes are expected from the service call
            //  payment_voucherObj.PAYMENT_DUE_DATE
            //  payment_voucherObj.VENDOR_NAME
            //  payment_voucherObj.ID
            //  payment_voucherObj.TOTAL_VALUE
            //  payment_voucherObj.CHECK_NUM
            //  payment_voucherObj.PAYMENT_AMT
            collection = purchOrdAdapter.getChecksToPrint(sessionMeta.getClientServerSecurity());

            if (collection.getSize() == 0)
            {
                JOptionPane.showMessageDialog(  this,
                                        "No Checks Scheduled to Print.",
                                        "Message",
                                        JOptionPane.INFORMATION_MESSAGE, null);
                return;
            }

            for (int i=0; i<collection.getSize(); i++)
            {
                _grid.addRow();

                DBRec attribSet = collection.getRec(i);
                _grid.set(i, gridColDateDue, attribSet.getAttribVal(payment_voucherObj.PAYMENT_DUE_DATE));
                _grid.set(i, gridColVendor, attribSet.getAttribVal(payment_voucherObj.VENDOR_NAME));
                _grid.set(i, gridColRefNo, attribSet.getAttribVal(payment_voucherObj.ID));
                _grid.set(i, gridColCheckAmt, attribSet.getAttribVal(payment_voucherObj.PAYMENT_AMT));
                _grid.set(i, gridColCheckNum, attribSet.getAttribVal(payment_voucherObj.CHECK_NUM));
            }

            _grid.setRowFocus(0);

        } catch (Exception ex) {
            LOGGER.logError(CONTAINER_FRAME, ex.getLocalizedMessage());
        }
	}

    public void previewReport()
    {
        if (!doPrePrinting()) return ;

        String execCmd      = "";
        String reportParams = "";
        reportParams        = "," + purch_orderObj.getObjLocality() + "," + daiNumField_beginCheckNum.getText() +
                                "," + daiNumField_endCheckNum.getText();
        try {
            execCmd = sessionMeta.getDaiLibHome()+ "daiRptViewer.exe "
                            + sessionMeta.getDaiRptHome() + "checkPrint.rpt"
                            + reportParams;

            System.out.println(execCmd);
			Process p = Runtime.getRuntime().exec(execCmd);

		} catch (Exception e)
		{
            String msg = "Unable to Launch Report Viewer."+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage()+
                        "\n"+execCmd;
            LOGGER.logError(null, msg);
		}
    }

    public void printReport()
    {
        if (!doPrePrinting()) return ;

        String execCmd      = "";
        String reportParams = "";
        reportParams        = "," + purch_orderObj.getObjLocality() + "," + daiNumField_beginCheckNum.getText() +
                                "," + daiNumField_endCheckNum.getText();
        try {
            execCmd = sessionMeta.getDaiLibHome()+ "daiRptViewer.exe "
                            + sessionMeta.getDaiRptHome() + "checkPrint.rpt"
                            + reportParams;

            System.out.println(execCmd);
			Process p = Runtime.getRuntime().exec(execCmd);

		} catch (Exception e)
		{
            String msg = "Unable to Launch Report Viewer."+
                        "\n"+e.toString()+"\n"+e.getLocalizedMessage()+
                        "\n"+execCmd;
            LOGGER.logError(null, msg);
		}
    }

    public int getStartingCheckNum() {
        String s_beginCheckNum = daiNumField_beginCheckNum.getText();
        if (s_beginCheckNum == null) s_beginCheckNum = "0";
        return Integer.parseInt(s_beginCheckNum);
    }

    public int getEndingCheckNum() {
        String s_endCheckNum = daiNumField_endCheckNum.getText();
        if (s_endCheckNum == null) s_endCheckNum = "0";
        return Integer.parseInt(s_endCheckNum);
    }
    public void setEndingCheckNum(String stEndNum)  {
        if(stEndNum == null) stEndNum = "0";
        if(stEndNum.length() == 0) stEndNum = "0";
        daiNumField_endCheckNum.setText(stEndNum);
    }
    public void updatePayVoucherCheckPrintFlag() {
        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
        int startCheckNum = getStartingCheckNum();
        int endCheckNum = getEndingCheckNum();

        String sqlStmt = " update payment_voucher set " + payment_voucherObj.PRINT_CHECK +
                        " = 'N' where locality = '" + payment_voucherObj.getObjLocality() + "'" +
                        " and " + payment_voucherObj.CHECK_NUM + " = ";

        for (int i=0; i< _grid.getRowCount(); i++) {
            String checkNum = (String)_grid.get(i, gridColCheckNum);
            if (checkNum != null && checkNum.length() > 0 && Integer.parseInt(checkNum) >= startCheckNum &&
                Integer.parseInt(checkNum) <= endCheckNum) {
                try {
                    dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(),
                                    sqlStmt + "'" + checkNum + "'");
                } catch (Exception e) {
                    String msg = this.getClass().getName() + "::updatePayVoucherCheckPrintingFlag failure\n" +
                                e.getClass().getName() + "\n" +
                                e.getLocalizedMessage();
                    LOGGER.logError(CONTAINER_FRAME, msg);
                }
            }
        }
    }

    private boolean doPrePrinting()
    {
        csPurchOrderAdapterFactory poAdapterFactory = csPurchOrderAdapterFactory.getInstance();
        csPurchOrderAdapter poAdapter = poAdapterFactory.getPurchOrderAdapter();

        String beginCheckNum = daiNumField_beginCheckNum.getText();
        String endCheckNum = daiNumField_endCheckNum.getText();
        boolean ret = false;

        if (beginCheckNum == null || endCheckNum == null) {
            //Let the user know that the Purchase Order Does not exist.
            JOptionPane.showMessageDialog(this  ,
                                      "Please enter a valid begin and end check number.",
                                      "Warning",
                                      JOptionPane.WARNING_MESSAGE, null);
            return false;
        }

        try {
            poAdapter.createCheckPrintScratchData(sessionMeta.getClientServerSecurity(),
                                                    beginCheckNum, endCheckNum);
            ret =  true;
        } catch (Exception e) {
            LOGGER.logError(CONTAINER_FRAME, "Can't create check scratch data.\n" + e.getLocalizedMessage());
            ret = false;
        }
        return ret;
    }
}


