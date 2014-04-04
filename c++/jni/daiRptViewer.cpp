#include "dairptviewer.h"

//Destructor//
daiRptViewer::~daiRptViewer()
{
//	m_crpeJob->Close();
//	m_crpeEngine.Close();

//	delete m_crpeJob;
}

BOOL daiRptViewer::logonPrintEngine()
{
	char* szDaiHome = getenv( "DAI_HOME" );

	CRPELogOnInfo LogOnInfo("artifacts", "", "SYSDBA", "masterkey");

	if (m_crpeEngine.GetEngineStatus() == CRPEngine::engineClosed) {
	if (!CRPEngine::GetEngine()->LogOnServer("p2sodbc.dll", &LogOnInfo))
	{
		CString strerror(CRPEngine::GetEngine()->GetErrorText());
		AfxMessageBox(strerror);
		return false;
	}
	}
}

BOOL daiRptViewer::openPrintJob(char* rptName)
{
	logonPrintEngine();

	m_crpeJob = CRPEngine::GetEngine()->OpenJob(rptName);
	if (m_crpeJob == NULL) 
	{
		CString strerror(CRPEngine::GetEngine()->GetErrorText());
		AfxMessageBox(strerror);
		return false;
	}

	return true;
}


BOOL daiRptViewer::openPreviewWindow()
{
	BOOL ret = true;

	if (!m_crpeJob) {
		MessageBox(NULL, "Can't open Priview Window.  Null Print Job.", "Error", MB_OK);
		return false;
	}
	
	//m_hasGroupTree, m_canDrillDown, m_hasNavigationControls, 
	//m_hasCancelButton, m_hasPrintButton, m_hasExportButton, 
	//m_hasZoomControl, m_hasCloseButton, m_hasProgressControls,
	//m_hasSearchButton, m_hasPrintSetupButton, m_hasRefreshButton;
	CRPEWindowOptions* windowOptions = new CRPEWindowOptions;
	windowOptions->m_hasCloseButton = true;
	windowOptions->m_hasNavigationControls = true;
	windowOptions->m_hasPrintButton = true;
	windowOptions->m_hasExportButton = true;
	windowOptions->m_hasZoomControl = true;
	windowOptions->m_hasSearchButton = true;


	m_crpeJob->SetWindowOptions(windowOptions);

	if (!m_crpeJob->OutputToWindow("Preview Window Event Logging", 0,0,400,400, WS_MAXIMIZE, (CWnd *) 0)) {
		MessageBox(NULL, "Can't open Priview Window.", "Error", MB_OK);
		return false;
	}

	return ret;
}

BOOL daiRptViewer::startJob()
{
	if (!m_crpeJob->Start())
	{
		MessageBox(NULL, "Error Opening Preview Window", "Error", MB_OK);
		return false;
	}

	return true;
}

BOOL daiRptViewer::closePrintJob()
{
	if (m_crpeJob != NULL) {
		m_crpeJob->Close();
	}
	m_crpeEngine.Close();

	return true;
}


BOOL daiRptViewer::setNthDateParmField (short parmN, char* parmVal)
{
	BOOL ret = false;

 	if (m_crpeJob->GetNthParameterField (parmN, &m_parmInfo)) {

		m_parmInfo.m_CurrentValueSet = true;
		strcpy(m_parmInfo.m_CurrentValue, parmVal);

		if (!m_crpeJob->SetNthParameterField (parmN, &m_parmInfo)) {
			MessageBox(NULL, "Could Not set Parameter to Report Engine.", "Error", MB_OK);
		}

	} else {
		MessageBox(NULL, "Could Not get Parameter from Report Engine.", "Error", MB_OK);
	}

	return ret;
}


BOOL daiRptViewer::parseCmdLine(char* cmdLine, char* parms[])
{
	int tokCnt = 0;

	parms[tokCnt] = strtok(cmdLine, ",");
	
	while (parms[tokCnt]) {
		
		tokCnt++;

		parms[tokCnt] = strtok(0, ",");
	}

	return true;
}

