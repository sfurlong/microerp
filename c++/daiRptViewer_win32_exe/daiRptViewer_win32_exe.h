// daiRptViewer_win32_exe.h : main header file for the DAIRPTVIEWER_WIN32_EXE application
//

#if !defined(AFX_DAIRPTVIEWER_WIN32_EXE_H__2B475FB5_AD0F_11D3_98F3_0050DA228D02__INCLUDED_)
#define AFX_DAIRPTVIEWER_WIN32_EXE_H__2B475FB5_AD0F_11D3_98F3_0050DA228D02__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CDaiRptViewerApp:
// See daiRptViewer_win32_exe.cpp for the implementation of this class
//

class CDaiRptViewerApp : public CWinApp
{
public:
	CDaiRptViewerApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDaiRptViewerApp)
	public:
	virtual BOOL InitInstance();
	//}}AFX_VIRTUAL

// Implementation

	//{{AFX_MSG(CDaiRptViewerApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DAIRPTVIEWER_WIN32_EXE_H__2B475FB5_AD0F_11D3_98F3_0050DA228D02__INCLUDED_)
