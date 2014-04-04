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

	BOOL openPrintJob(char* rptName);

	BOOL daiRptViewer::setPrinter(char* driverName);

	BOOL outputToPrinter(short nCopies = 1);

	BOOL openPreviewWindow();

	BOOL closePrintJob();

	BOOL parseCmdLine(char* cmdLine, char* parms[]);

	BOOL setNthParmField (short parmN, char* parmVal);

	BOOL startJob ();

	void getPrinterNames(CString*& names, int& numNames);

private:
	CRPEngine	m_crpeEngine; //The Report Engine
	CRPEJob*	m_crpeJob; // job object that will be referenced.
	CRPEParameterFieldInfo m_parmInfo;

};


#endif