// jni2.cpp : Defines the initialization routines for the DLL.
//

#include "stdafx.h"
#include "jni2.h"
#include "testwin.h"
#include "rpteng.h"
#include "dairptviewer.h"

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
// CJni2App

BEGIN_MESSAGE_MAP(CJni2App, CWinApp)
	//{{AFX_MSG_MAP(CJni2App)
		// NOTE - the ClassWizard will add and remove mapping macros here.
		//    DO NOT EDIT what you see in these blocks of generated code!
	//}}AFX_MSG_MAP
END_MESSAGE_MAP()

/////////////////////////////////////////////////////////////////////////////
// CJni2App construction

CJni2App::CJni2App()
{
	// TODO: add construction code here,
	// Place all significant initialization in InitInstance
}

/////////////////////////////////////////////////////////////////////////////
// The one and only CJni2App object

CJni2App theApp;
testWin testDlg;
//daiRptViewer m_rptViewer;


/*
 * Class:     RptEng
 * Method:    closePrintJob
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_closePrintJob
  (JNIEnv* env, jobject thisObj)
{
	return true;
}

/*
 * Class:     RptEng
 * Method:    openPreviewWindow
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_openPreviewWindow
  (JNIEnv* env, jobject thisObj)
{
	//m_rptViewer.openPreviewWindow();

	return true;
}

/*
 * Class:     RptEng
 * Method:    openPrintJob
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_openPrintJob
  (JNIEnv* env, jobject thisObj, jstring rptName)
{
	const char* cStr = env->GetStringUTFChars(rptName, false);
  
	testDlg.DoModal();

	//m_rptViewer.openPrintJob((char*)cStr);

	return true;
}

/*
 * Class:     RptEng
 * Method:    setNthParmField
 * Signature: (ILjava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_setNthParmField
  (JNIEnv* env, jobject thisObj, jint jInt, jstring parmVal)
{
	const char* cStr = env->GetStringUTFChars(parmVal, false);
  
	return true;
}

/*
 * Class:     RptEng
 * Method:    startJob
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_RptEng_startJob
  (JNIEnv* env, jobject thisObj)
{
	//m_rptViewer.startJob();

	return true;
}

