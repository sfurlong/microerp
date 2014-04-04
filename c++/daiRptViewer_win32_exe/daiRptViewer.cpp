#include "dairptviewer.h"
#include <winspool.h>

//Destructor//
daiRptViewer::~daiRptViewer()
{
	try {
		//m_crpeJob->Close();
		m_crpeEngine.Close();

		//	delete m_crpeJob;
	} catch (...) {
	}
}


BOOL daiRptViewer::openPrintJob(char* rptName)
{
	char* szDaiHome = getenv( "DAI_HOME" );

	CRPELogOnInfo LogOnInfo("artifacts", "", "SYSDBA", "daimgr");

	if (!CRPEngine::GetEngine()->LogOnServer("p2sodbc.dll", &LogOnInfo))
	{
		CString strerror(CRPEngine::GetEngine()->GetErrorText());
		AfxMessageBox(strerror);
		return false;
	}

	m_crpeJob = CRPEngine::GetEngine()->OpenJob(rptName);
	if (m_crpeJob == NULL)
	{
		CString strerror(CRPEngine::GetEngine()->GetErrorText());
		AfxMessageBox(strerror);
		return false;
	}

	return true;
}

BOOL daiRptViewer::outputToPrinter(short nCopies) {
	return m_crpeJob->OutputToPrinter (nCopies);
}

BOOL daiRptViewer::openPreviewWindow()
{
	BOOL ret = true;

	if (!m_crpeJob)
	{
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

	if (!m_crpeJob->OutputToWindow("Preview Window Event Logging", 0,0,400,400, WS_MAXIMIZE, (CWnd *) 0))
	{
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
	if (m_crpeJob != NULL)
	{
		m_crpeJob->Close();
	}
	m_crpeEngine.Close();

	return true;
}


BOOL daiRptViewer::setNthParmField (short parmN, char* parmVal)
{
	BOOL ret = false;

	if (m_crpeJob->GetNthParameterField (parmN, &m_parmInfo))
	{

		m_parmInfo.m_CurrentValueSet = true;
		strcpy(m_parmInfo.m_CurrentValue, parmVal);

		if (!m_crpeJob->SetNthParameterField (parmN, &m_parmInfo))
		{
			MessageBox(NULL, "Could Not set Parameter to Report Engine.", "Error", MB_OK);
		}

	} else
	{
		MessageBox(NULL, "Could Not get Parameter from Report Engine.", "Error", MB_OK);
	}

	return ret;
}


BOOL daiRptViewer::parseCmdLine(char* cmdLine, char* parms[])
{
	int tokCnt = 0;

	parms[tokCnt] = strtok(cmdLine, ",");

	while (parms[tokCnt])
	{

		tokCnt++;

		parms[tokCnt] = strtok(0, ",");
	}

	return true;
}

LPDEVMODE GetLandscapeDevMode(HWND hWnd, char *pDevice)
{
	HANDLE      hPrinter = NULL;
	LPDEVMODE   pDevMode;
	DWORD       dwNeeded, dwRet;
	PRINTER_DEFAULTS pd;

	ZeroMemory(&pd, sizeof(pd));
	pd.DesiredAccess = PRINTER_ALL_ACCESS; 

	/* Start by opening the printer */
	if (!OpenPrinter(pDevice, &hPrinter, &pd))
		return NULL;

	/*
	 * Step 1:
	 * Allocate a buffer of the correct size.
	 */
	dwNeeded = DocumentProperties(hWnd,
								  hPrinter,		  /* handle to our printer */ 
								  pDevice,		  /* Name of the printer */ 
								  NULL,			  /* Asking for size so */ 
								  NULL,			  /* these are not used. */ 
								  0); /* Zero returns buffer size. */
	pDevMode = (LPDEVMODE)malloc(dwNeeded); 

	/*
	 * Step 2:
	 * Get the default DevMode for the printer and
	 * modify it for our needs.
	 */
	dwRet = DocumentProperties(hWnd,
							   hPrinter,
							   pDevice,
							   pDevMode,	   /* The address of the buffer to fill. */ 
							   NULL,		   /* Not using the input buffer. */ 
							   DM_OUT_BUFFER); /* Have the output buffer filled. */
	if (dwRet != IDOK)
	{ 
		/* if failure, cleanup and return failure */
		free(pDevMode);
		ClosePrinter(hPrinter);
		return NULL;
	}

	/*
	 * Make changes to the DevMode which are supported.
	 */
	if (pDevMode->dmFields & DM_ORIENTATION)
	{ 
		/* if the printer supports paper orientation, set it*/
		pDevMode->dmOrientation = DMORIENT_LANDSCAPE;
	}

	if (pDevMode->dmFields & DM_DUPLEX)
	{ 
		/* if it supports duplex printing, use it */

		pDevMode->dmDuplex = DMDUP_HORIZONTAL;
	}

	/*
	 * Step 3:
	 * Merge the new settings with the old.
	 * This gives the driver a chance to update any private
	 * portions of the DevMode structure.
	 */
	dwRet = DocumentProperties(hWnd,
							   hPrinter,
							   pDevice,
							   pDevMode,	   /* Reuse our buffer for output. */ 
							   pDevMode,	   /* Pass the driver our changes. */ 
							   DM_IN_BUFFER |  /* Commands to Merge our changes and */ 
							   DM_OUT_BUFFER);  /* write the result. */

	/* Done with the printer */
	ClosePrinter(hPrinter);

	if (dwRet != IDOK)
	{ 
		/* if failure, cleanup and return failure */
		free(pDevMode);
		return NULL;
	}

	/* return the modified DevMode structure */
	return pDevMode;

}

BOOL daiRptViewer::setPrinter(char* driverName)
{
	DEVMODE *pDevMode = NULL;
	CString dName;
	CString pName;
	CString portName;
	//	mode.dmCopies
	//	mode.dmDefaultSource = //Which Paper Tray

//	pDevMode = GetLandscapeDevMode(NULL, driverName);
	//	if (!m_crpeJob->GetSelectedPrinter(dName, pName, portName, &pDevMode)) {
	//		printf("this sucks");
	//	}
	//	pDevMode->dmOrientation  = DMORIENT_LANDSCAPE; //Landscape ord Portait
//	return m_crpeJob->SelectPrinter(driverName, driverName, "LPT1", pDevMode);
	return m_crpeJob->SelectPrinter(driverName, driverName, "LPT1");

	/*
	
			// report printer attributes
			BOOL GetSelectedPrinter (CString &driverName,
									 CString &printerName,
									 CString &portName,
									 DEVMODE **mode);
	
			BOOL SelectPrinter (const _TCHAR *driverName,
								const _TCHAR *printerName,
								const _TCHAR *portName,
								const DEVMODE *mode = 0);
	*/
}

void daiRptViewer::getPrinterNames(CString*& names, int& numNames)
{
	DWORD dwSize, dwPrinters;
	::EnumPrinters (PRINTER_ENUM_LOCAL, NULL, 5, NULL, 0, &dwSize, &dwPrinters); 

	BYTE* pBuffer = new BYTE[dwSize];

	::EnumPrinters (PRINTER_ENUM_LOCAL, NULL, 5, pBuffer, dwSize, &dwSize, &dwPrinters); 

	names = new CString[dwPrinters];
	numNames = dwPrinters;

	if (dwPrinters != 0)
	{
		//!!NOTE PRINTER_INFO_4 should be used for NT!!//
		PRINTER_INFO_5* pPrnInfo = (PRINTER_INFO_5*) pBuffer;
		for (UINT i=0; i<dwPrinters; i++)
		{
			names[i] = pPrnInfo->pPrinterName;
			pPrnInfo++;
		}
	}

	delete[] pBuffer;
}