// jni2.h : main header file for the JNI2 DLL
//

#if !defined(AFX_JNI2_H__AB98A6B8_A608_11D3_98F0_0050DA228D02__INCLUDED_)
#define AFX_JNI2_H__AB98A6B8_A608_11D3_98F0_0050DA228D02__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#ifndef __AFXWIN_H__
	#error include 'stdafx.h' before including this file for PCH
#endif

#include "resource.h"		// main symbols

#include "dairptviewer.h"

/////////////////////////////////////////////////////////////////////////////
// CJni2App
// See jni2.cpp for the implementation of this class
//

class CJni2App : public CWinApp
{
public:
	CJni2App();

// Overrides
	// ClassWizard generated virtual function overrides
	//{{AFX_VIRTUAL(CJni2App)
	//}}AFX_VIRTUAL

	//{{AFX_MSG(CJni2App)
		// NOTE - the ClassWizard will add and remove member functions here.
		//    DO NOT EDIT what you see in these blocks of generated code !
	//}}AFX_MSG
	DECLARE_MESSAGE_MAP()
};


/////////////////////////////////////////////////////////////////////////////

//{{AFX_INSERT_LOCATION}}
// Microsoft Visual C++ will insert additional declarations immediately before the previous line.

#endif // !defined(AFX_JNI2_H__AB98A6B8_A608_11D3_98F0_0050DA228D02__INCLUDED_)
