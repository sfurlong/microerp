// daiRptViewer.cpp : Defines the initialization routines for the DLL.
//

#include "stdafx.h"
#include "dairptviewer.h"

#include "crpe.h"
#include "peplus.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#undef THIS_FILE
static char THIS_FILE[] = __FILE__;
#endif

//
//	Note!
//
//		If this DLL is dynamically linked against the MFC
//		DLLs, any functions exported from this DLL which
//		call into MFC must have the AFX_MANAGE_STATE macro
//		added at the very beginning of the function.
//
//		For example:
//
//		extern "C" BOOL PASCAL EXPORT ExportedFunction()
//		{
//			AFX_MANAGE_STATE(AfxGetStaticModuleState());
//			// normal function body here
//		}
//
//		It is very important that this macro appear in each
//		function, prior to any calls into MFC.  This means that
//		it must appear as the first statement within the 
//		function, even before any object variable declarations
//		as their constructors may generate calls into the MFC
//		DLL.
//
//		Please see MFC Technical Notes 33 and 58 for additional
//		details.
//

/////////////////////////////////////////////////////////////////////////////
// CDaiRptViewerApp

BEGIN_MESSAGE_MAP(CDaiRptViewerApp, CWinApp)
	//{{AFX_MSG_MAP(CDaiRptViewerApp)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CDaiRptViewerApp construction

CDaiRptViewerApp::CDaiRptViewerApp()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
}

/////////////////////////////////////////////////////////////////////////////
// The one and only CDaiRptViewerApp object

CDaiRptViewerApp theApp;


CRPEngine m_crpeEngine(true); //The Report Engine
CRPEJob* m_crpeJob; // job object that will be referenced.

extern "C" __declspec(dllexport) BOOL openPrintJob(char* rptName)
{
	AFX_MANAGE_STATE(AfxGetStaticModuleState());

	BOOL	ret = true;

	char* szDaiHome = getenv( "DAI_HOME" );


	if ((m_crpeJob = m_crpeEngine.OpenJob(rptName)) == NULL)
	{
		MessageBox(NULL, "Error Opening Print Job.", "Error", MB_OK);
		ret = false;
	}

	return ret;
}

extern "C" __declspec(dllexport) BOOL openPreviewWindow()
{
	AFX_MANAGE_STATE(AfxGetStaticModuleState());

	BOOL ret = true;

	if (!m_crpeJob) {
		MessageBox(NULL, "Can't open Priview Window.  Null Print Job.", "Error", MB_OK);
		return false;
	}
	
	m_crpeJob->OutputToWindow("Preview Window Event Logging", 0,0,400,400,0, (CWnd *) 0);

	if (!m_crpeJob->Start())
	{
		MessageBox(NULL, "Errors Opening Preview Window", "Error", MB_OK);
		ret = false;
	}

	return ret;
}

extern "C" __declspec(dllexport) BOOL closePrintJob()
{
	AFX_MANAGE_STATE(AfxGetStaticModuleState());

	return true;
}