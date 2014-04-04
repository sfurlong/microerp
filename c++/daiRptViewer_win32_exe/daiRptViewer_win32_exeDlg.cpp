// daiRptViewer_win32_exeDlg.cpp : implementation file
//

#include "stdafx.h"
#include "daiRptViewer_win32_exe.h"
#include "daiRptViewer_win32_exeDlg.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// CDaiRptViewerDlg dialog

CDaiRptViewerDlg::CDaiRptViewerDlg(CWnd* pParent /*=NULL*/)
	: CDialog(CDaiRptViewerDlg::IDD, pParent)
{
	//{{AFX_DATA_INIT(CDaiRptViewerDlg)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
	// Note that LoadIcon does not require a subsequent DestroyIcon in Win32
	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
}

void CDaiRptViewerDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(CDaiRptViewerDlg)
	DDX_Control(pDX, IDC_COPIES, m_copies);
	DDX_Control(pDX, IDC_STATUS_TEXT, m_statusText);
	DDX_Control(pDX, IDC_COMBO_PRINTERS, m_comboBox_printers);
	//}}AFX_DATA_MAP
}

BEGIN_MESSAGE_MAP(CDaiRptViewerDlg, CDialog)
	//{{AFX_MSG_MAP(CDaiRptViewerDlg)
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_BN_CLICKED(IDC_PREVIEW, OnPreview)
	ON_BN_CLICKED(IDC_BUTTON_PRINT, OnButtonPrint)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDaiRptViewerDlg message handlers

BOOL CDaiRptViewerDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon
	
	// TODO: Add extra initialization here
	CString* printerNames;
	int numPrinterNames;
	m_rptViewer.getPrinterNames(printerNames, numPrinterNames);

	for (int i=0; i<numPrinterNames; i++) {
		m_comboBox_printers.AddString (printerNames[i]);
	}
	
	m_comboBox_printers.SetCurSel(0);
	m_copies.SetWindowText("1");

	return TRUE;  // return TRUE  unless you set the focus to a control
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CDaiRptViewerDlg::OnPaint() 
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, (WPARAM) dc.GetSafeHdc(), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CDaiRptViewerDlg::OnQueryDragIcon()
{
	return (HCURSOR) m_hIcon;
}


void CDaiRptViewerDlg::OnPreview() 
{
	CString selectedPrinterName;
	m_comboBox_printers.GetWindowText(selectedPrinterName);

	char* parms[5] = {NULL, NULL, NULL, NULL, NULL};

	m_rptViewer.parseCmdLine(PROG_CMD_LINE, parms);

	if (parms[0]) {

		if (!m_rptViewer.openPrintJob(parms[0])) {
			//Failure.. Just Return.
			return;
		}
	}

	m_rptViewer.setPrinter(selectedPrinterName.GetBuffer(0));

	if (!m_rptViewer.openPreviewWindow()) {
		//Failure.. Just Return.
		return;
	}

	if (parms[1]) {
		m_rptViewer.setNthParmField(0, parms[1]);
	}

	if (parms[2]) {
		m_rptViewer.setNthParmField(1, parms[2]);
	}

	if (parms[3]) {
		m_rptViewer.setNthParmField(2, parms[3]);
	}

	m_rptViewer.startJob();
}

void CDaiRptViewerDlg::OnButtonPrint() 
{
	CString selectedPrinterName;
	CString nCopiesToPrint;
	m_comboBox_printers.GetWindowText(selectedPrinterName);
	m_copies.GetWindowText(nCopiesToPrint);

	int nCopies = atoi(nCopiesToPrint);

	// TODO: Add your control notification handler code here
	char* parms[5] = {NULL, NULL, NULL, NULL, NULL};

	m_rptViewer.parseCmdLine(PROG_CMD_LINE, parms);

	if (parms[0]) {

		if (!m_rptViewer.openPrintJob(parms[0])) {
			//Failure.. Just Return.
			return;
		}
	}

	m_rptViewer.setPrinter(selectedPrinterName.GetBuffer(0));

	if (!m_rptViewer.outputToPrinter(nCopies)) {
		//Failure.. Just Return.
		return;
	}

	if (parms[1]) {
		m_rptViewer.setNthParmField(0, parms[1]);
	}

	if (parms[2]) {
		m_rptViewer.setNthParmField(1, parms[2]);
	}

	if (parms[3]) {
		m_rptViewer.setNthParmField(2, parms[3]);
	}

	m_rptViewer.startJob();	
}

void CDaiRptViewerDlg::OnCancel() 
{
	// TODO: Add extra cleanup here
	m_rptViewer.closePrintJob();

	CDialog::OnCancel();
}
