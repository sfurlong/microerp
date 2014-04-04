// daiRptViewer.h : main header file for the daiRptViewer DLL
//

#if !defined(AFX_DLL5_H__DBE16620_A5C3_11D3_98F0_0050DA228D02__INCLUDED_)
#define AFX_DLL5_H__DBE16620_A5C3_11D3_98F0_0050DA228D02__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

//#include "resource.h"		// main symbols

/////////////////////////////////////////////////////////////////////////////
// CDaiRptViewerApp
// See daiRptViewer.cpp for the implementation of this class
//
extern "C" __declspec(dllexport) BOOL openPrintJob(char* rptName);
extern "C" __declspec(dllexport) BOOL openPreviewWindow();
extern "C" __declspec(dllexport) BOOL closePrintJob();

class CDaiRptViewerApp : public CWinApp
{
public:
	CDaiRptViewerApp();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CDaiRptViewerApp)
	//}}AFX_VIRTUAL

	//{{AFX_MSG(CDaiRptViewerApp)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_DLL5_H__DBE16620_A5C3_11D3_98F0_0050DA228D02__INCLUDED_)
