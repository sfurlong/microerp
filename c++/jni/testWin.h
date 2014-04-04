#if !defined(AFX_TESTWIN_H__1721E9C1_B1C3_11D3_98F6_0050DA228D02__INCLUDED_)
#define AFX_TESTWIN_H__1721E9C1_B1C3_11D3_98F6_0050DA228D02__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000
// testWin.h : header file
//
#include "dairptviewer.h"

/////////////////////////////////////////////////////////////////////////////
// testWin dialog

class testWin : public CDialog
{
// Construction
public:
	testWin(CWnd* pParent = NULL);   // standard constructor
	
	daiRptViewer m_rptViewer;
// Dialog Data
	//{{AFX_DATA(testWin)
	enum { IDD = TESTWIN_ID };
		// NOTE: the ClassWizard will add data members here
	//}}AFX_DATA


// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(testWin)
	protected:
	virtual void DoDataExchange(CDataExchange* pDX);    // DDX/DDV support
	//}}AFX_VIRTUAL

// Implementation
protected:

	// Generated message map functions
	//{{AFX_MSG(testWin)
	afx_msg void OnPreview();
	virtual void OnCancel();
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_TESTWIN_H__1721E9C1_B1C3_11D3_98F6_0050DA228D02__INCLUDED_)
