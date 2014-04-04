//-------------------------------------------------------------------------------
// This win32 c++ program will start a java program via a ".exe" program.
//
// For VC++ compile at the command line, use the following:  cl -MT jexe.cpp
//
//
//-------------------------------------------------------------------------------
//#include <windows.h>
#include "stdafx.h"

#include <stdio.h>
#include <iostream.h>

#include "dairptviewer.h"

int APIENTRY WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, LPSTR lpCmdLine, int nCmdShow)
{
	openPrintJob("c:\\gl.rpt");

	openPreviewWindow();

	MessageBox(NULL, "1", "1", MB_OK);

	closePrintJob();

	return 0;
}

