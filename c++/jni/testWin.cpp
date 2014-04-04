// testWin.cpp : implementation file
//

#include "stdafx.h"
#include "jni2.h"
#include "testWin.h"
#include "dairptviewer.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

/////////////////////////////////////////////////////////////////////////////
// testWin dialog


testWin::testWin(CWnd* pParent /*=NULL*/)
	: CDialog(testWin::IDD, pParent)
{
	//{{AFX_DATA_INIT(testWin)
		// NOTE: the ClassWizard will add member initialization here
	//}}AFX_DATA_INIT
}


void testWin::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);
	//{{AFX_DATA_MAP(testWin)
		// NOTE: the ClassWizard will add DDX and DDV calls here
	//}}AFX_DATA_MAP
}


BEGIN_MESSAGE_MAP(testWin, CDialog)
	//{{AFX_MSG_MAP(testWin)
	ON_BN_CLICKED(IDC_PREVIEW, OnPreview)
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// testWin message handlers

void testWin::OnPreview() 
{
	// TODO: Add your control notification handler code here
	m_rptViewer.openPrintJob("c:\\gl.rpt");
	m_rptViewer.openPreviewWindow();
	m_rptViewer.startJob();
}

void testWin::OnCancel() 
{
	// TODO: Add extra cleanup here
	//m_rptViewer.closePrintJob();
	
	CDialog::OnCancel();
}
