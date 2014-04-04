// daiRptViewer_win32_exeDlg.h : header file
//

#if !defined(AFX_DAIRPTVIEWER_WIN32_EXEDLG_H__2B475FB7_AD0F_11D3_98F3_0050DA228D02__INCLUDED_)
#define AFX_DAIRPTVIEWER_WIN32_EXEDLG_H__2B475FB7_AD0F_11D3_98F3_0050DA228D02__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "dairptviewer.h"

/////////////////////////////////////////////////////////////////////////////
// CDaiRptViewerDlg dialog

class CDaiRptViewerDlg : public CDialog
{
// Construction
public:
	CDaiRptViewerDlg(CWnd* pParent = NULL);	// standard constructor

	//This will store the commandline
	char * PROG_CMD_LINE;
	daiRptViewer m_rptViewer;
	void doPreview();

// Dialog Data
	//{{AFX_DATA(CDaiRptViewerDlg)
	enum { IDD = IDD_DAIRPTVIEWER_WIN32_EXE_DIALOG };
	CEdit	m_copies;
	CEdit	m_statusText;
	CComboBox	m_comboBox_printers;
	//}}AFX_DATA

	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDaiRptViewerDlg)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	//}}AFX_VIRTUAL


// Implementation
protected:
	HICON m_hIcon;

	// Generated message map functions
	//{{AFX_MSG(CDaiRptViewerDlg)
	virtual BOOL OnInitDialog();
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	afx_msg void OnPreview();
	virtual void OnCancel();
	afx_msg void OnButtonPrint();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DAIRPTVIEWER_WIN32_EXEDLG_H__2B475FB7_AD0F_11D3_98F3_0050DA228D02__INCLUDED_)
