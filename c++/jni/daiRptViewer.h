#if !defined(_DAIRPTVIEWER_INCLUDED_)
#define _DAIRPTVIEWER_INCLUDED_

#include "stdafx.h"
#include "peplus.h"
#include "crpe.h"


class daiRptViewer {

public:

	//Constructor
	daiRptViewer() :m_crpeEngine(true), m_crpeJob(NULL){};

	~daiRptViewer();

	BOOL logonPrintEngine();

	BOOL openPrintJob(char* rptName);

	BOOL openPreviewWindow();

	BOOL closePrintJob();

	BOOL parseCmdLine(char* cmdLine, char* parms[]);

	BOOL setNthDateParmField (short parmN, char* parmVal);

	BOOL startJob ();

private:
	CRPEngine	m_crpeEngine; //The Report Engine
	CRPEJob*	m_crpeJob; // job object that will be referenced.
	CRPEParameterFieldInfo m_parmInfo;

};


#endif