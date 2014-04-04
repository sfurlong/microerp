#include <jni.h>

#include <windows.h>
#include <string.h>
#include <stdio.h>
#include <math.h>


/*
	File:		CRinf.c
	Rights:		Copyright 1999,2000, Neles Automation Inc.
				All Rights Reserved world-wide.
	Purpose:
				Crystal Reports wrapper code.  This is the glue
				between Java and the DLL.
	Notes:
				
	Known defects:
	
	History:
	see VCS log
*/
#include "crpe.h"
//#include <crdates.h>

#include <uxddisk.h>
#include <uxfhtml.h>
#include <uxfsepv.h>
#include <uxftext.h>
#include <uxfwordw.h>
#include <uxfrtf.h>
#include <uxfxls.h>

#include <uxdmapi.h>

#define ASSERT(XXX)

#if defined(WIN32)
#define _DEVMODE	DEVMODEA
#else
#define _DEVMODE	DEVMODE
#endif

// the later is obtained by
//  javah -cp polaris.jar visi.crystal.CRinf
#include "visi_crystal_CRinf.h"

/*
	Preprocessor magic for packge naming.
*/
#define PACKAGE(XXX) "visi/crystal/"##XXX

/*
	Preprocessor magic for class naming.  
	(This was so SAE guys could easily change package names.)
*/
#define PREFIX(XXX) Java_visi_crystal_CRinf_##XXX

#ifdef __cplusplus
extern "C"
	{
#endif
	
BOOL WINAPI DllMain(
	HINSTANCE hinstDLL,  // handle to DLL module
	DWORD fdwReason,     // reason for calling function
	LPVOID lpvReserved   // reserved
	);
	 
/* don't use
BOOL CALLBACK EventCallback (
	short eventID,
	void *param, 
	void *userData);
*/

// begin - these have been deprecated	
short CRPE_API PEGetNParams (short printJob);

BOOL CRPE_API PEGetNthParam (short printJob,
                                 short parameterN,
                                 HANDLE FAR *qh,
                                 short  FAR *ql );

BOOL CRPE_API PESetNthParam (short printJob,
                                  short parameterN,
                                  const char FAR *szq );	
// end deprecated
                                   
#ifdef __cplusplus
	}
#endif			
	
// ODBC Driver
//static char* dbDriver = "PDSODBC.DLL";
// Oracle Driver
//static char* dbDriver = "P2SORA7.DLL";

static PELogOnInfo logOnInfo;

static enum exportFormats { HTML=1,XLS,CSV,TEXT,RTF,WORDWIN };
static char* fileExtensions[] = {"",".html",".xls",".csv",".txt",".rtf",".doc" };

static char* szCRException = PACKAGE("CRException");
static char* szCRParameterField = PACKAGE("CRParameterField");
static char* szCRValueInfo = PACKAGE("CRValue");
static char* szCRExportOptions = PACKAGE("CRExportOptions");
static char* szCRMAPI = PACKAGE("CRMAPI");
static char* szDevMode = PACKAGE("DevMode");
static _DEVMODE* global_devMode = NULL;
	
static int _throwCRException( JNIEnv* env, jstring jsError )
	{
	jint rv;
	jclass clazz;
	jmethodID methodid;
	jobject exptn;

	clazz = (*env)->FindClass( env, szCRException ); 
	if( clazz == 0 )
		{
		fprintf( stderr, "class %s not found\n", szCRException );
		return -1;
		}
		
	methodid = (*env)->GetMethodID ( //(JNIEnv *env, jclass clazz, const char *name, const char *sig); 
			env, clazz, "<init>", "(Ljava/lang/String;)V" );
	if( methodid == 0 )
		{
		fprintf( stderr, "constructor for (%s) not found\n", szCRException );
		return -2;
		}
	
	exptn = (*env)->NewObject( env, clazz, methodid, jsError );
	//if( exptn == 0 )
	
	rv = (*env)->Throw( env, exptn );
	return rv;
	}
	
static int throwCRException( JNIEnv* env, short printJob )
	{
	BOOL		rv;
	jstring		jString;
	short		errorCode;
	HANDLE		errorHandle;
	short		errorLength;
	char*		errorString;

	errorCode = PEGetErrorCode( printJob ); // 0 for no job
	
	rv = PEGetErrorText( printJob, &errorHandle, &errorLength );
	// if( !rv )
	
	errorString = malloc( errorLength+1 );
	// if( errorString == 0 )

	rv = PEGetHandleString(errorHandle, errorString, errorLength);
	// if( !rv )
	
	errorString[ errorLength ] = 0;
	
	jString = (*env)->NewStringUTF(env, errorString );
	// if( jString == 0 )
	
	free( errorString );
	
	_throwCRException( env, jString );
	
	return (int)errorCode;
	}

JNIEXPORT jshort JNICALL PREFIX(open) (JNIEnv *env,jobject obj)
	{
	if( FALSE == PEOpenEngine() )
		{
		return throwCRException( env, 0 );
		}
	return 0;
	}
	
JNIEXPORT jboolean JNICALL PREFIX(canClose) (JNIEnv * env, jobject obj)
	{
	return PECanCloseEngine();
	}

JNIEXPORT void JNICALL PREFIX(close) (JNIEnv *env, jobject obj)
	{
	PECloseEngine();
	}

static	char dbDriver[64];

/*
 * Logon to the server
 */
JNIEXPORT jshort JNICALL PREFIX(logOnServer) (JNIEnv *env, jobject obj, jstring jdriver, jstring jserver, jstring jdatabase, jstring juserid, jstring jpassword)
	{
	const char* driver	 = (*env)->GetStringUTFChars( env, jdriver, 0);
	const char* server	 = (*env)->GetStringUTFChars( env, jserver, 0);
	const char* database = (*env)->GetStringUTFChars( env, jdatabase, 0);
	const char* userid	 = (*env)->GetStringUTFChars( env, juserid, 0);
	const char* password = (*env)->GetStringUTFChars( env, jpassword, 0);
	
	lstrcpyn( dbDriver, driver, 64);
	lstrcpyn( logOnInfo.ServerName, server, PE_SERVERNAME_LEN);
	lstrcpyn( logOnInfo.DatabaseName, database, PE_DATABASENAME_LEN);
	lstrcpyn( logOnInfo.UserID, userid, PE_USERID_LEN);
	lstrcpyn( logOnInfo.Password, password, PE_PASSWORD_LEN);

	(*env)->ReleaseStringUTFChars(env,jserver, server );
	(*env)->ReleaseStringUTFChars(env,jdatabase, database);
	(*env)->ReleaseStringUTFChars(env,juserid, userid );
	(*env)->ReleaseStringUTFChars(env,jpassword, password);
	
	// Connect to the server
	if( FALSE == PELogOnServer( dbDriver, &logOnInfo) )
		{
		return throwCRException( env, 0 ); // 0 means most recent
		}

	return 0;
	}
	
JNIEXPORT jshort JNICALL PREFIX(setNthTableLogOnInfo) (JNIEnv *env, jobject obj, 
		jshort jPrintJob, jshort jTableN, jboolean jPropagate,
		jstring jserver, jstring jdatabase, jstring juserid, jstring jpassword)
	{
	const char* server	 = (*env)->GetStringUTFChars( env, jserver, 0);
	const char* database = (*env)->GetStringUTFChars( env, jdatabase, 0);
	const char* userid	 = (*env)->GetStringUTFChars( env, juserid, 0);
	const char* password = (*env)->GetStringUTFChars( env, jpassword, 0);
	
	lstrcpyn( logOnInfo.ServerName, server, PE_SERVERNAME_LEN);
	lstrcpyn( logOnInfo.DatabaseName, database, PE_DATABASENAME_LEN);
	lstrcpyn( logOnInfo.UserID, userid, PE_USERID_LEN);
	lstrcpyn( logOnInfo.Password, password, PE_PASSWORD_LEN);

	(*env)->ReleaseStringUTFChars(env,jserver, server );
	(*env)->ReleaseStringUTFChars(env,jdatabase, database);
	(*env)->ReleaseStringUTFChars(env,juserid, userid );
	(*env)->ReleaseStringUTFChars(env,jpassword, password);

	if( FALSE == PESetNthTableLogOnInfo( jPrintJob, jTableN, &logOnInfo, jPropagate) )
		{
		return throwCRException( env, jPrintJob );
		}

	return 0;
	}
	
/*
 * Logoff the server
 */
JNIEXPORT jshort JNICALL PREFIX(logOffServer) (JNIEnv *env, jobject obj)
	{
	if( FALSE == PELogOffServer( dbDriver, &logOnInfo ) )
		{
		return throwCRException( env, 0 );
		}
	return 0;
	}
	
JNIEXPORT jshort JNICALL PREFIX(getSPParamCount) (
		JNIEnv * env, jobject obj, jshort jreportJob )
	{
	jshort retcode  = PEGetNParams( jreportJob );
	return ( -1 == retcode ) ? throwCRException( env, jreportJob ) : retcode ;
	}

JNIEXPORT jstring JNICALL PREFIX(getSPParam) (
		JNIEnv * env, jobject obj, jshort jreportJob, jshort jparamNum )
	{
	BOOL rv;
	HANDLE param;
	short paramLength;
	char* szparam;
	jstring jparam;
	
	if( FALSE == PEGetNthParam( jreportJob, jparamNum, &param, &paramLength ) )
		{
		int rv = throwCRException( env, jreportJob );
		return NULL;
		}
	szparam = malloc( paramLength+1 );
	// if szparam == 0
	rv = PEGetHandleString( param, szparam, paramLength );
	// if rv == FALSE
	szparam[ paramLength ] = 0; // just in case
	jparam = (*env)->NewStringUTF( env, szparam );
	// if jparam == 0
	free( szparam );
	return jparam;
	}

JNIEXPORT jshort JNICALL PREFIX(setSPParam) (
		JNIEnv * env, jobject obj, jshort jreportJob, jshort jparamNum, jstring jparamValue )
	{
	BOOL retcode;
	const char* paramValue = (*env)->GetStringUTFChars(env,jparamValue,0);
	retcode = PESetNthParam( jreportJob, jparamNum, paramValue );
	(*env)->ReleaseStringUTFChars(env,jparamValue,paramValue);
	if( FALSE == retcode )
		{
		return throwCRException( env, jreportJob );
		}
	return 0;
	}

JNIEXPORT jshort JNICALL PREFIX(setSelectionFormula) (JNIEnv *env, jobject obj, jshort jreportJob, jstring sel)
	{
	int retcode = 0;
	const char* selection = (*env)->GetStringUTFChars(env,sel,0);
	if( FALSE == PESetSelectionFormula( jreportJob, selection ) )
		{
		retcode = throwCRException( env, jreportJob );
		}
	(*env)->ReleaseStringUTFChars(env,sel, selection);
	return retcode;
	}

JNIEXPORT jstring JNICALL PREFIX(getSelectionFormula) (JNIEnv *env, jobject obj, jshort reportJob)
	{
	BOOL rv;
	HANDLE		textHandle;
	short		textLength;
	char*		szText;
	jstring		jText;

	if( FALSE == PEGetSelectionFormula(reportJob, &textHandle, &textLength) )
		{
		int errCode = throwCRException( env, reportJob );
		return NULL;
		}
	
	szText = malloc( textLength + 1 );
	// if szText == 0
	
	rv = PEGetHandleString( textHandle, szText, textLength );
	// if( rv == FALSE )
	szText[ textLength ] = 0;
	
	jText = (*env)->NewStringUTF( env, szText );
	// if jText == 0
	
	return jText;
	}

/*
 * Open the print job
 */
JNIEXPORT jshort JNICALL PREFIX(openReport) (
		JNIEnv *env, jobject obj, jstring jreport_file)
	{
	short printJob;
	const char* report_file = (*env)->GetStringUTFChars(env, jreport_file, 0);
	printJob = PEOpenPrintJob(report_file);
	(*env)->ReleaseStringUTFChars(env,jreport_file,report_file);
	return ( printJob == 0 ) ? throwCRException( env, 0 ) : printJob;
	}

/*
 * Open the sub report print job
 */
JNIEXPORT jshort JNICALL PREFIX(openSubReport) (
		JNIEnv *env, jobject obj, jshort printJob, jstring jreport_file)
	{
	int subreportprintJob;
	const char* sub_report_file = (*env)->GetStringUTFChars(env,jreport_file,0);	
	subreportprintJob = PEOpenSubreport( printJob, sub_report_file );
	(*env)->ReleaseStringUTFChars(env, jreport_file, sub_report_file);
	return (subreportprintJob == 0) ? throwCRException( env, printJob ) : subreportprintJob;
	}


/*
 * Close the print job
 */
JNIEXPORT jshort JNICALL PREFIX(closeReport) (JNIEnv *env, jobject obj, jshort printJob)
	{
	if( FALSE == PEClosePrintJob( printJob ) )
		{
		return throwCRException( env, printJob );
		}
	return 0;
	}

/*
 * Close the subreport print job
 */
JNIEXPORT jshort JNICALL PREFIX(closeSubReport) (JNIEnv *env, jobject obj, jshort printJob)
	{
	if( FALSE == PECloseSubreport( printJob ) )
		{
		return throwCRException( env, printJob );
		}
	return 0;
	}

JNIEXPORT jstring JNICALL PREFIX(getSQLQuery) (JNIEnv *env, jobject obj, jshort printJob )
	{
	BOOL rv;
	HANDLE	textHandle;
	short	textLength;
	char	*szText;
	jstring jString;

	if( FALSE == PEGetSQLQuery(printJob, &textHandle, &textLength) )
		{
		int rv = throwCRException( env, printJob );
		return NULL;
		}
	szText = malloc( textLength + 1 );
	// if szText == 0
	rv = PEGetHandleString( textHandle, szText, textLength );
	// if !rv
	
	jString = (*env)->NewStringUTF( env, szText );
	// if( jString == 0 )
	
	free( szText );
	
	return jString;
	}
	
JNIEXPORT jshort JNICALL PREFIX(setSQLQuery) (JNIEnv *env, jobject obj, jshort printJob, jstring query )
	{
	BOOL rv;
	HANDLE	textHandle;
	short	textLength;
	const char	*szQuery;
	jstring jString;

	szQuery = (*env)->GetStringUTFChars(env,query,0);
	// if( szQuery == 0 )	
	if( FALSE == PESetSQLQuery( printJob, szQuery ) )
		{
		(*env)->ReleaseStringUTFChars(env,query,szQuery);
		return throwCRException( env, printJob );
		}	
	(*env)->ReleaseStringUTFChars(env,query,szQuery);	
	return 0;
	}


/*
 * Run the print job
 */
JNIEXPORT jshort JNICALL PREFIX(outputToFile) (JNIEnv *env, jobject obj, jshort printJob, jstring jdestination, jint jexportformat)
{
	int retcode;
	char outputFile[FILENAME_MAX];

	UXFCommaTabSeparatedOptions commaSepOptions;
	UXDDiskOptions diskOptions;
	PEExportOptions exportOptions;
	UXFHTML3Options html3Options;
	const char* dest = (*env)->GetStringUTFChars(env,jdestination,0);
	// if dest == 0
	strcpy( outputFile, dest );

	// Initialize members of diskOptions
	diskOptions.structSize = UXDDiskOptionsSize;
	if( strchr( outputFile,'.' ) == NULL )
		strcat(outputFile,fileExtensions[jexportformat] );
	diskOptions.fileName = outputFile;
	//printf("\nDestination file = %s", outputFile );

	// Initialize members of exportOptions
	exportOptions.StructSize = PE_SIZEOF_EXPORT_OPTIONS;
	strcpy(exportOptions.destinationDLLName,"u2ddisk.dll");
	exportOptions.destinationType = UXDDiskType;
	exportOptions.destinationOptions = &diskOptions;
	
	switch( jexportformat )
	{
		case HTML:
			html3Options.structSize = UXFHTML3OptionsSize;
			html3Options.fileName = (char*)outputFile;
			strcpy(exportOptions.formatDLLName,"u2fhtml.dll");
			exportOptions.formatType = UXFHTML32StdType;	// UXFHTML3Type;
			exportOptions.formatOptions = &html3Options;
			break;

		case XLS:
			exportOptions.formatType = UXFXls5Type;
			strcpy(exportOptions.formatDLLName,"u2fxls.dll");
			break;

		case WORDWIN:
			exportOptions.formatType = UXFWordWinType;
			strcpy(exportOptions.formatDLLName,"u2fwordw.dll");
			break;

		case RTF:
			exportOptions.formatType = UXFRichTextFormatType;
			strcpy(exportOptions.formatDLLName,"u2frtf.dll");
			break;

		case CSV:
			commaSepOptions.structSize = UXFCommaTabSeparatedOptionsSize;
			commaSepOptions.useReportNumberFormat = TRUE;	// Use number format saved in report
			commaSepOptions.useReportDateFormat = TRUE;		// Do not use date format saved in report

			exportOptions.formatType = UXFCommaSeparatedType;
			exportOptions.formatOptions = &commaSepOptions;
			strcpy(exportOptions.formatDLLName,"u2fsepv.dll");
			break;

		case TEXT:
			exportOptions.formatType = UXFTextType;
			strcpy(exportOptions.formatDLLName,"u2ftext.dll");
			break;
	}

	if( FALSE == PEExportTo(printJob, &exportOptions) )
		{
		(*env)->ReleaseStringUTFChars(env,jdestination,dest);
		return throwCRException( env, printJob );
		}
		
	if( FALSE == PEStartPrintJob(printJob,TRUE) )
		{
		(*env)->ReleaseStringUTFChars(env,jdestination,dest);
		return throwCRException( env, printJob );
		}

	(*env)->ReleaseStringUTFChars(env,jdestination,dest);
	return 0;
}


JNIEXPORT jshort JNICALL PREFIX(outputToPrinter
		) (JNIEnv *env, jobject obj, jshort printJob, jshort copies)
	{
	if( FALSE == PEOutputToPrinter( printJob, copies ) )
		return throwCRException( env, printJob );
	if( FALSE == PEStartPrintJob( printJob, TRUE ) )
		return throwCRException( env, printJob );
	return 0;
	}

/*
	BSW:	This must be done this way so AWT gets its time and events,
			and we get our time and events.  We must NOT run on the
			AWT dispatch thread as we'll block.
*/
JNIEXPORT jshort JNICALL PREFIX(outputToWindow) (
		JNIEnv *env, 
		jobject obj, 
		jshort printJob, 
		//jint hwnd,
		jstring windowTitle, 
		jint left, jint top, jint width, jint height)
	{
	HWND hwnd;
	MSG msg;
	jclass clazz;
	jmethodID mid;
	DWORD		style;
	// int dieNow;
	int errorCode = 0;

	const char* title = (*env)->GetStringUTFChars(env,windowTitle,0);
	// if title == 0
	
	style = WS_OVERLAPPEDWINDOW ;

	//dieNow = FALSE;

	if( TRUE == PEOutputToWindow ( printJob, title, (int)left, (int)top, (int)width, (int)height, style, 0 /* (HWND)hwnd */ )  )
		{
		if( FALSE == PEStartPrintJob( printJob, TRUE ) )
			{
			errorCode = throwCRException( env, printJob );
			goto EXIT;
			}
		clazz = (*env)->FindClass( env, "java/lang/Thread" ); 
		if( clazz == 0 )
			{
			errorCode = -1;
			goto EXIT;
			}
		mid = (*env)->GetStaticMethodID ( //(JNIEnv *env, jclass clazz, const char *name, const char *sig); 
			env, clazz, "yield", "()V" );
		if( mid == 0 )
			{
			errorCode = -2;
			goto EXIT;
			}
		// just to be safe
		hwnd = PEGetWindowHandle( printJob );
		// if null, very wierd
		while( GetMessage( &msg, NULL, 0, 0 ) > 0 )
			{ 		
			// all events are for my Window			
	        TranslateMessage(&msg);         
	        DispatchMessage(&msg);	        
	        
			// if( !( IsWindow( msg.hwnd ) && !IsDialogMessage( (HWND)0, &msg ) ) )
			if( msg.hwnd == hwnd )
				{
				(*env)->CallStaticVoidMethod( env, clazz, mid ); // leave, in case of Green Threads
				
				// for some wierd reason I never get WM_DESTROYED
				//fprintf( stderr, "%08X %08X %08X\n", msg.message, msg.wParam, msg.lParam );
				if( msg.message == 0xA1 /* WM_NCLBUTTONDOWN, wingdi.h */ 
						&& msg.wParam == 0x14 ) /* HTCLOSE, winuser.h */
					{
					break;
					}
				
				}
	        }
		}
	else
		{
		errorCode = throwCRException( env, printJob );
		}
EXIT: ;
	(*env)->ReleaseStringUTFChars( env, windowTitle, title);
	return errorCode;
	}

JNIEXPORT jstring JNICALL PREFIX(getErrorText) (JNIEnv *env, jobject obj, jshort printJob)
	{
	BOOL		rv;
	jstring		jString;
	short		errorCode;
	HANDLE		errorHandle;
	short		errorLength;
	char*		errorString;

	rv = PEGetErrorText(printJob, &errorHandle, &errorLength);
	// if( !rv )
	
	errorString = malloc( errorLength+1 );
	// if( errorString == 0 )

	rv = PEGetHandleString(errorHandle, errorString, errorLength);
	// if( !rv )
	errorString[ errorLength ] = 0;
	
	jString = (*env)->NewStringUTF(env, errorString );
	// if( jString == 0 )
	
	free( errorString );
	
	return jString;
	}
	
JNIEXPORT jshort JNICALL PREFIX(getErrorCode) (JNIEnv *env, jobject obj, jshort printJob)
	{
	return PEGetErrorCode( printJob );
	}
	
JNIEXPORT jshort JNICALL PREFIX(testReport) (JNIEnv *env, jobject obj)
	{
	_throwCRException( 
		env,
		(*env)->NewStringUTF( env, "Who lies in Grant's Tomb?" ) 
		);
	return -1;
	}

static short extractField( JNIEnv* env, jobject jfield, jfieldID id, char* szStr, int iLen )
	{
	const char* szBuf;
	jstring jStr = (*env)->GetObjectField( env, jfield, id );
	// if szStr == 0
	if( jStr )
		{
		szBuf = (*env)->GetStringUTFChars( env, jStr, 0 );
		// if szBuf == 0
		
		lstrcpyn( szStr, szBuf, iLen );
		
		(*env)->ReleaseStringUTFChars( env, jStr, szBuf );
		}
	else
		{
		szStr[0] = (char)0;
		}
	
	return 0;
	}
	
/*
BOOL CALLBACK EventCallback (short eventID,
					void *param, void *userData)
{

if(eventID == PE_CLOSE_PRINT_WINDOW_EVENT)
		{
			PEGeneralPrintWindowEventInfo * eventInfo =
				(PEGeneralPrintWindowEventInfo *) param;
			//ASSERT(eventInfo != 0 && eventInfo->StructSize ==
			//	PE_SIZEOF_GENERAL_PRINT_WINDOW_EVENT_INFO);
		int* dieNow = (int*)userData;
		*dieNow = TRUE;	
		}
    return TRUE; // this means call default functionality...
}
*/

static _DEVMODE * fillDevMode( JNIEnv* env, jobject jfield, _DEVMODE * mode )
	{
  	jclass clazz;
  	jmethodID methodid;
  	jfieldID deviceNameId, specVersionId, driverVersionId, sizeId, driverExtraId,
  		fieldsId, orientationId, paperSizeId, paperLengthId, paperWidthId, positionId,
  		scaleId, copiesId, defaultSourceId, printQualityId, colorId, duplexId, yResolutionId,
  		ttOptionId, collateId, formNameId, logPixelsId, bitsPerPixelId, pelsWidthId, pelsHeightId,
  		displayFlagsId, displayFrequencyId;
  		
	if( jfield == NULL || mode == NULL )
		return NULL;

	memset(mode,0,sizeof(_DEVMODE));
		
  	clazz = (*env)->FindClass( env, szDevMode ); 
	if( clazz == 0 )
		{
		jstring jString;
		char str[256];
		sprintf( str, "class %s not found\n", szDevMode );
		jString = (*env)->NewStringUTF( env, str );
		// if jString == 0
		_throwCRException( env, jString );
		return NULL;
		}
		
	//if( jfield == 0 )
	deviceNameId = (*env)->GetFieldID( env, clazz, "deviceName", "Ljava/lang/String;" );
	specVersionId = (*env)->GetFieldID( env, clazz, "specVersion", "S" );
	driverVersionId = (*env)->GetFieldID( env, clazz, "driverVersion", "S" );	
	sizeId = (*env)->GetFieldID( env, clazz, "size", "S" );
	driverExtraId = (*env)->GetFieldID( env, clazz, "driverExtra", "S" );
	fieldsId = (*env)->GetFieldID( env, clazz, "fields", "I" );
	orientationId = (*env)->GetFieldID( env, clazz, "orientation", "S" );
	paperSizeId = (*env)->GetFieldID( env, clazz, "paperSize", "S" );
	paperLengthId = (*env)->GetFieldID( env, clazz, "paperLength", "S" );
	paperWidthId = (*env)->GetFieldID( env, clazz, "paperWidth", "S" );
	//positionId = (*env)->GetFieldID( env, clazz, "positionId", "point" );
	scaleId = (*env)->GetFieldID( env, clazz, "scale", "S" );
	copiesId = (*env)->GetFieldID( env, clazz, "copies", "S" );
	defaultSourceId = (*env)->GetFieldID( env, clazz, "defaultSource", "S" );
	printQualityId = (*env)->GetFieldID( env, clazz, "printQuality", "S" );
	colorId  = (*env)->GetFieldID( env, clazz, "color", "S" );
	duplexId = (*env)->GetFieldID( env, clazz, "duplex", "S" );
	yResolutionId = (*env)->GetFieldID( env, clazz, "yResolution", "S" );
	ttOptionId = (*env)->GetFieldID( env, clazz, "ttOption", "S" );
	collateId  = (*env)->GetFieldID( env, clazz, "collate", "S" );	
	formNameId  = (*env)->GetFieldID( env, clazz, "formName", "Ljava/lang/String;" );	
	logPixelsId = (*env)->GetFieldID( env, clazz, "logPixels", "S" );
	bitsPerPixelId = (*env)->GetFieldID( env, clazz, "bitsPerPel", "I" );
	pelsWidthId = (*env)->GetFieldID( env, clazz, "pelsWidth", "I" );
	pelsHeightId = (*env)->GetFieldID( env, clazz, "pelsHeight", "I" );
	displayFlagsId = (*env)->GetFieldID( env, clazz, "displayFlags", "I" );	
	displayFrequencyId = (*env)->GetFieldID( env, clazz, "displayFrequency", "I" );
	
	extractField( env, jfield, deviceNameId, (char*)mode->dmDeviceName, CCHDEVICENAME );
	mode->dmSpecVersion=(*env)->GetShortField( env, jfield, specVersionId );
	mode->dmDriverVersion=(*env)->GetShortField( env, jfield, driverVersionId );
	mode->dmSize=(*env)->GetShortField( env, jfield, sizeId );
	mode->dmDriverExtra =(*env)->GetShortField( env, jfield, driverExtraId );
	mode->dmFields=(*env)->GetIntField( env, jfield, fieldsId );
	mode->dmOrientation =(*env)->GetShortField( env, jfield, orientationId );
	mode->dmPaperSize =(*env)->GetShortField( env, jfield, paperSizeId);
	mode->dmPaperLength=(*env)->GetShortField( env, jfield, paperLengthId );
	mode->dmPaperWidth =(*env)->GetShortField( env, jfield, paperWidthId );
	mode->dmScale=(*env)->GetShortField( env, jfield, scaleId );
	mode->dmCopies=(*env)->GetShortField( env, jfield, copiesId );
	mode->dmDefaultSource=(*env)->GetShortField( env, jfield, defaultSourceId);
	mode->dmPrintQuality =(*env)->GetShortField( env, jfield, printQualityId);
	mode->dmColor =(*env)->GetShortField( env, jfield, colorId );
	mode->dmDuplex=(*env)->GetShortField( env, jfield, duplexId );
	mode->dmYResolution =(*env)->GetShortField( env, jfield, yResolutionId);
	mode->dmTTOption=(*env)->GetShortField( env, jfield, ttOptionId );
	mode->dmCollate=(*env)->GetShortField( env, jfield, collateId);
	extractField( env, jfield, formNameId, (char*)mode->dmFormName, CCHFORMNAME );
	mode->dmLogPixels=(*env)->GetShortField( env, jfield, logPixelsId  );
	mode->dmBitsPerPel=(*env)->GetIntField( env, jfield, bitsPerPixelId  );
	mode->dmPelsWidth =(*env)->GetIntField( env, jfield, pelsWidthId );
	mode->dmPelsHeight = (*env)->GetIntField( env, jfield, pelsHeightId );
	mode->dmDisplayFlags = (*env)->GetIntField( env, jfield, displayFlagsId );
	mode->dmDisplayFrequency = (*env)->GetIntField( env, jfield, displayFrequencyId);

	return mode;
	}

JNIEXPORT jshort JNICALL PREFIX(selectPrinter) (
		JNIEnv *env, 
		jobject obj, 
		jshort printJob, 
		jstring jDriverName, 
		jstring jPrinterName, 
		jstring jPortName,
		jobject jDevMode
		)
	{
	_DEVMODE mode;

	int errorCode = 0;
	
	const char* driverName = (*env)->GetStringUTFChars(env,jDriverName,0);
	const char* printerName = (*env)->GetStringUTFChars(env,jPrinterName,0);
	const char* portName = (*env)->GetStringUTFChars(env,jPortName,0);
	
	_DEVMODE* du = fillDevMode(env, jDevMode, &mode);


	if (du->dmDefaultSource == 1) {
		global_devMode->dmDefaultSource = 1;
		global_devMode->dmFields = 512;
	}
	if (du->dmDefaultSource == 2) {
		global_devMode->dmDefaultSource = 2;
		global_devMode->dmFields = 512;
	}
	if (du->dmDefaultSource == 3) {
		global_devMode->dmDefaultSource = 3;
		global_devMode->dmFields = 512;
	}


	if( FALSE == PESelectPrinter ( printJob,
                           driverName,
                           printerName,
                           portName,
                           global_devMode ) )
		{
		errorCode = throwCRException( env, printJob );
		}
	
	
	(*env)->ReleaseStringUTFChars( env, jDriverName, driverName );
	(*env)->ReleaseStringUTFChars( env, jPrinterName, printerName );
	(*env)->ReleaseStringUTFChars( env, jPortName, portName );
	
	return errorCode;
	}

static jobject makeDevMode( JNIEnv* env, _DEVMODE * mode )
	{
	jobject jfield;
  	jclass clazz;
  	jmethodID methodid;
  	jfieldID deviceNameId, specVersionId, driverVersionId, sizeId, driverExtraId,
  		fieldsId, orientationId, paperSizeId, paperLengthId, paperWidthId, positionId,
  		scaleId, copiesId, defaultSourceId, printQualityId, colorId, duplexId, yResolutionId,
  		ttOptionId, collateId, formNameId, logPixelsId, bitsPerPixelId, pelsWidthId, pelsHeightId,
  		displayFlagsId, displayFrequencyId;
		
	if( mode == NULL )
		return NULL;
	
  	clazz = (*env)->FindClass( env, szDevMode ); 
	if( clazz == 0 )
		{
		jstring jString;
		char str[256];
		sprintf( str, "class %s not found\n", szDevMode );
		jString = (*env)->NewStringUTF( env, str );
		// if jString == 0
		_throwCRException( env, jString );
		return NULL;
		}
		
  	jfield = (*env)->AllocObject( env, clazz );
	//if( jfield == 0 )
	deviceNameId = (*env)->GetFieldID( env, clazz, "deviceName", "Ljava/lang/String;" );
	(*env)->SetObjectField( env, jfield, deviceNameId, (*env)->NewStringUTF( env, (char*)mode->dmDeviceName ) );
	
	specVersionId = (*env)->GetFieldID( env, clazz, "specVersion", "S" );
	(*env)->SetShortField( env, jfield, specVersionId, mode->dmSpecVersion );
	
	driverVersionId = (*env)->GetFieldID( env, clazz, "driverVersion", "S" );
	(*env)->SetShortField( env, jfield, driverVersionId, mode->dmDriverVersion );
	
	sizeId = (*env)->GetFieldID( env, clazz, "size", "S" );
	(*env)->SetShortField( env, jfield, sizeId, mode->dmSize );
	
	driverExtraId = (*env)->GetFieldID( env, clazz, "driverExtra", "S" );
	(*env)->SetShortField( env, jfield, driverExtraId, mode->dmDriverExtra );
	
	fieldsId = (*env)->GetFieldID( env, clazz, "fields", "I" );
	(*env)->SetIntField( env, jfield, fieldsId, mode->dmFields );
	
	orientationId = (*env)->GetFieldID( env, clazz, "orientation", "S" );
	(*env)->SetShortField( env, jfield, orientationId, mode->dmOrientation );
	
	paperSizeId = (*env)->GetFieldID( env, clazz, "paperSize", "S" );
	(*env)->SetShortField( env, jfield, paperSizeId, mode->dmPaperSize );
	
	paperLengthId = (*env)->GetFieldID( env, clazz, "paperLength", "S" );
	(*env)->SetShortField( env, jfield, paperLengthId, mode->dmPaperLength );
	
	paperWidthId = (*env)->GetFieldID( env, clazz, "paperWidth", "S" );
	(*env)->SetShortField( env, jfield, paperWidthId, mode->dmPaperWidth );
	
	//positionId = (*env)->GetFieldID( env, clazz, "positionId", "point" );
	
	scaleId = (*env)->GetFieldID( env, clazz, "scale", "S" );
	(*env)->SetShortField( env, jfield, scaleId, mode->dmScale );
	
	copiesId = (*env)->GetFieldID( env, clazz, "copies", "S" );
	(*env)->SetShortField( env, jfield, copiesId, mode->dmCopies );
	
	defaultSourceId = (*env)->GetFieldID( env, clazz, "defaultSource", "S" );
	(*env)->SetShortField( env, jfield, defaultSourceId, mode->dmDefaultSource );
	
	printQualityId = (*env)->GetFieldID( env, clazz, "printQuality", "S" );
	(*env)->SetShortField( env, jfield, printQualityId, mode->dmPrintQuality );
	
	colorId  = (*env)->GetFieldID( env, clazz, "color", "S" );
	(*env)->SetShortField( env, jfield, colorId, mode->dmColor );
	
	duplexId = (*env)->GetFieldID( env, clazz, "duplex", "S" );
	(*env)->SetShortField( env, jfield, duplexId, mode->dmDuplex );
	
	yResolutionId = (*env)->GetFieldID( env, clazz, "yResolution", "S" );
	(*env)->SetShortField( env, jfield, yResolutionId, mode->dmYResolution );
	
	ttOptionId = (*env)->GetFieldID( env, clazz, "ttOption", "S" );
	(*env)->SetShortField( env, jfield, ttOptionId, mode->dmTTOption );
	
	collateId  = (*env)->GetFieldID( env, clazz, "collate", "S" );
	(*env)->SetShortField( env, jfield, collateId, mode->dmCollate );
	
	formNameId  = (*env)->GetFieldID( env, clazz, "formName", "Ljava/lang/String;" );
	(*env)->SetObjectField( env, jfield, formNameId, (*env)->NewStringUTF( env, (char*)mode->dmFormName ) );
	
	logPixelsId = (*env)->GetFieldID( env, clazz, "logPixels", "S" );
	(*env)->SetShortField( env, jfield, logPixelsId, mode->dmLogPixels );
	
	bitsPerPixelId = (*env)->GetFieldID( env, clazz, "bitsPerPel", "I" );
	(*env)->SetIntField( env, jfield, bitsPerPixelId, mode->dmBitsPerPel );
	
	pelsWidthId = (*env)->GetFieldID( env, clazz, "pelsWidth", "I" );
	(*env)->SetIntField( env, jfield, pelsWidthId, mode->dmPelsWidth );
	
	pelsHeightId = (*env)->GetFieldID( env, clazz, "pelsHeight", "I" );
	(*env)->SetIntField( env, jfield, pelsHeightId, mode->dmPelsHeight );
	
	displayFlagsId = (*env)->GetFieldID( env, clazz, "displayFlags", "I" );
	(*env)->SetIntField( env, jfield, displayFlagsId, mode->dmDisplayFlags );
	
	displayFrequencyId = (*env)->GetFieldID( env, clazz, "displayFrequency", "I" );
	(*env)->SetIntField( env, jfield, displayFrequencyId, mode->dmDisplayFrequency );
	
	return jfield;
	}

/*
static UINT CALLBACK PageSetupHook(  
		HWND hdlg,      // handle to the dialog box window
		UINT uiMsg,     // message identifier  
		WPARAM wParam,  // message parameter
		LPARAM lParam   ) // message parameter)
	{
	MSG msg;
	
	msg.hwnd = hdlg;    
	msg.message = uiMsg;
	msg.wParam = wParam; 
    msg.lParam = lParam;    
    msg.time = 0;  
    msg.pt.x = 0; 
    msg.pt.y = 0; 
	if( IsWindow( msg.hwnd ) && !IsDialogMessage( (HWND)0, &msg ) )
		{
		// call thread yield
		}
		
	return 0; // let dialog process message
	}
*/
HGLOBAL girt = NULL; // should release on DLL terminate...

JNIEXPORT jobjectArray JNICALL PREFIX(pageSetup) (
		JNIEnv *env, 
		jobject obj, jboolean showDialog, jobject jDevMode )
	{
	_DEVMODE * mode;
	DEVNAMES* names;
	BOOL r;
	PAGESETUPDLG psd; // I really shoulda figured out a way to map this into java too - should have a dictionary for all printer setup
	jclass clazz;
	jobjectArray jstrings;
	jstring jdriver, jprinter, jport;
	
	// zap
	memset(&psd,0,sizeof(PAGESETUPDLG));
	psd.lStructSize = sizeof(PAGESETUPDLG);	
	psd.Flags = (showDialog?0:PSD_RETURNDEFAULT)+PSD_DISABLEMARGINS; // +PSD_ENABLEPAGESETUPHOOK;
	//psd.lpfnPageSetupHook = &PageSetupHook;
	if( girt == NULL )
		{
		girt = GlobalAlloc( GHND, sizeof(_DEVMODE) );
		}
	if( jDevMode != NULL )
		{
		mode = (_DEVMODE*)GlobalLock( girt );
		fillDevMode( env, jDevMode, mode );
		GlobalUnlock( girt );
		psd.hDevMode = girt;
		}
	
	if( FALSE == PageSetupDlg( &psd ) )
		return NULL;
		
	
	names = (DEVNAMES*)GlobalLock( psd.hDevNames );
	mode = ( NULL != psd.hDevMode ) ? (_DEVMODE *)GlobalLock( psd.hDevMode ) : NULL;
	
	//
	
	clazz = (*env)->FindClass( env, "java/lang/Object" ); 
	// if clazz == 0
	
	jstrings = (*env)->NewObjectArray( env, 4, clazz, NULL ); 
	// if( jstrings == 0 )
	jdriver = (*env)->NewStringUTF( env, (char*)names+names->wDriverOffset );
	// if( jdriver == 0 )
	jprinter = (*env)->NewStringUTF( env, (char*)names+names->wDeviceOffset );
	// if( jprinter == 0 )
	jport = (*env)->NewStringUTF( env, (char*)names+names->wOutputOffset );
	// if( jport == 0 )
	
	(*env)->SetObjectArrayElement( env, jstrings, 0, jdriver );
	(*env)->SetObjectArrayElement( env, jstrings, 1, jprinter );
	(*env)->SetObjectArrayElement( env, jstrings, 2, jport );
	(*env)->SetObjectArrayElement( env, jstrings, 3, makeDevMode( env, mode ) );
	
	global_devMode = makeDevMode(env, mode);

	GlobalUnlock( psd.hDevNames );
	if( 0 != mode ) GlobalUnlock( psd.hDevMode );
	
	return jstrings;
	}
	

JNIEXPORT jobjectArray JNICALL PREFIX(getSelectedPrinter) (
		JNIEnv *env, 
		jobject obj, 
		jshort printJob )
	{
	BOOL rv;
	jclass clazz;
	jobjectArray jstrings;
	jstring jdriver, jprinter, jport;
	short driverLength, printerLength, portLength;
	HANDLE hdriver, hprinter, hport;
	char* szdriver=0;
	char* szprinter=0;
	char* szport=0;
	_DEVMODE * mode;
	
	if( FALSE == PEGetSelectedPrinter ( printJob,
                                    &hdriver,
                                    &driverLength,
                                    &hprinter,
                                    &printerLength,
                                    &hport,
                                    &portLength,
									&mode
                                    ) )
    	{
    	int errCode = throwCRException( env, printJob );
		return NULL;
		}
	
	szdriver = malloc( driverLength+1 );
	// if( szdriver == 0 )
	szprinter = malloc( printerLength+1 );
	// if( szprinter == 0 )
	szport = malloc( portLength+1 );
	// if( szport == 0 )
	
	// these will release the handles
	rv = PEGetHandleString( hdriver, szdriver, driverLength );
	//if( rv == FALSE )

	rv = PEGetHandleString( hprinter, szprinter, printerLength );
	//if( rv == FALSE )
	
	rv = PEGetHandleString( hport, szport, portLength );
	//if( rv == FALSE )
	
	szdriver[driverLength] = 0;
	szprinter[printerLength] = 0;
	szport[portLength] = 0;
	
	// what about freeing DEVMODE?

	clazz = (*env)->FindClass( env, "java/lang/Object" ); 
	// if clazz == 0
	
	jstrings = (*env)->NewObjectArray( env, 4, clazz, NULL ); 
	// if( jstrings == 0 )
	jdriver = (*env)->NewStringUTF( env, szdriver );
	// if( jdriver == 0 )
	jprinter = (*env)->NewStringUTF( env, szprinter );
	// if( jprinter == 0 )
	jport = (*env)->NewStringUTF( env, szport );
	// if( jport == 0 )
	
	global_devMode = makeDevMode(env, mode);

	(*env)->SetObjectArrayElement( env, jstrings, 0, jdriver );
	(*env)->SetObjectArrayElement( env, jstrings, 1, jprinter );
	(*env)->SetObjectArrayElement( env, jstrings, 2, jport );
	(*env)->SetObjectArrayElement( env, jstrings, 3, global_devMode );
	
	free( szdriver );
	free( szprinter );
	free( szport );
	
	return jstrings;
	}

/*
 */
JNIEXPORT jshort JNICALL PREFIX(getParameterFieldCount
  ) (JNIEnv * env, jobject obj, jshort jobid )
	{
	short rv = PEGetNParameterFields( jobid );
	if( -1 == rv )
		{
		return throwCRException( env, jobid );
		}
	return rv;
	}
	
/*
 */
JNIEXPORT jshort JNICALL PREFIX(setNthParameterField
  ) (JNIEnv * env, jobject obj, jshort jobid, jshort idx, jobject jfield )
  	{
  	PEParameterFieldInfo fieldInfo;
  	
  	jclass clazz;
  	jfieldID valueTypeId, defaultValueSetId , currentValueSetId,
				nameId, promptId,defaultValueId,currentValueId,
				reportNameId, needsCurrentValueId, isLimitedId, minSizeId,
				maxSizeId, editMaskId, isHiddenId;
  	
  	clazz = (*env)->FindClass( env, szCRParameterField ); 
	if( clazz == 0 )
		{
		jstring jString;
		char str[256];
		sprintf( str, "class %s not found\n", szCRParameterField );
		jString = (*env)->NewStringUTF( env, str );
		_throwCRException( env, jString );
		return -1;
		}
		
	valueTypeId = (*env)->GetFieldID( env, clazz, "ValueType", "S" );
	defaultValueSetId = (*env)->GetFieldID( env, clazz, "DefaultValueSet", "S" );
	currentValueSetId = (*env)->GetFieldID( env, clazz, "CurrentValueSet", "S" );
	nameId = (*env)->GetFieldID( env, clazz, "Name", "Ljava/lang/String;" );
	promptId = (*env)->GetFieldID( env, clazz, "Prompt", "Ljava/lang/String;" );
	defaultValueId = (*env)->GetFieldID( env, clazz, "DefaultValue", "Ljava/lang/String;" );
	currentValueId = (*env)->GetFieldID( env, clazz, "CurrentValue", "Ljava/lang/String;" );
	reportNameId = (*env)->GetFieldID( env, clazz, "ReportName", "Ljava/lang/String;" );
	needsCurrentValueId = (*env)->GetFieldID( env, clazz, "needsCurrentValue", "S" );
	isLimitedId = (*env)->GetFieldID( env, clazz, "isLimited", "S" );
	minSizeId = (*env)->GetFieldID( env, clazz, "MinSize", "D" );
	maxSizeId = (*env)->GetFieldID( env, clazz, "MaxSize", "D" );
	editMaskId = (*env)->GetFieldID( env, clazz, "EditMask","Ljava/lang/String;" );
	isHiddenId = (*env)->GetFieldID( env, clazz, "isHidden", "S" );
  	
	fieldInfo.StructSize = PE_SIZEOF_PARAMETER_FIELD_INFO;
	fieldInfo.ValueType = (*env)->GetShortField( env, jfield, valueTypeId );
	fieldInfo.DefaultValueSet = (*env)->GetShortField( env, jfield, defaultValueSetId );
	fieldInfo.CurrentValueSet = (*env)->GetShortField( env, jfield, currentValueId );
	extractField( env, jfield, nameId, fieldInfo.Name, PE_PF_NAME_LEN);
	extractField( env, jfield, promptId, fieldInfo.Prompt, PE_PF_PROMPT_LEN);
	extractField( env, jfield, defaultValueId, fieldInfo.DefaultValue, PE_PF_VALUE_LEN);
	extractField( env, jfield, currentValueId, fieldInfo.CurrentValue, PE_PF_VALUE_LEN);
	extractField( env, jfield, reportNameId, fieldInfo.ReportName, PE_PF_REPORT_NAME_LEN);
	fieldInfo.needsCurrentValue = (*env)->GetShortField( env, jfield, needsCurrentValueId );
	fieldInfo.isLimited = (*env)->GetShortField( env, jfield, isLimitedId );
	fieldInfo.MinSize = (*env)->GetDoubleField( env, jfield, minSizeId );
	fieldInfo.MaxSize = (*env)->GetDoubleField( env, jfield, maxSizeId );
	extractField( env, jfield, editMaskId, fieldInfo.EditMask, PE_PF_EDITMASK_LEN);
	fieldInfo.isHidden = (*env)->GetShortField( env, jfield, isHiddenId );
	
	if( FALSE == PESetNthParameterField( jobid, idx, &fieldInfo ) )
		{
		return throwCRException( env, jobid );
		}
	
	return 0;
  	}

/*
 */
JNIEXPORT jobject JNICALL PREFIX(getNthParameterField
  ) (JNIEnv * env, jobject obj, jshort jobid, jshort idx )
  	{
  	PEParameterFieldInfo fieldInfo;
  	jobject jfield;
  	jclass clazz;
  	jmethodID methodid;
  	jfieldID valueTypeId, defaultValueSetId , currentValueSetId,
				nameId, promptId,defaultValueId,currentValueId,
				reportNameId, needsCurrentValueId, isLimitedId, minSizeId,
				maxSizeId, editMaskId, isHiddenId;
  	
  	
	fieldInfo.StructSize = PE_SIZEOF_PARAMETER_FIELD_INFO;
	if( FALSE == PEGetNthParameterField( jobid, idx, &fieldInfo ) )
		{
		short errorCode = throwCRException( env, jobid );
		return NULL;
		}
		
  	clazz = (*env)->FindClass( env, szCRParameterField ); 
	if( clazz == 0 )
		{
		jstring jString;
		char str[256];
		sprintf( str, "class %s not found\n", szCRParameterField );
		jString = (*env)->NewStringUTF( env, str );
		_throwCRException( env, jString );
		return NULL;
		}
		
	valueTypeId = (*env)->GetFieldID( env, clazz, "ValueType", "S" );
	defaultValueSetId = (*env)->GetFieldID( env, clazz, "DefaultValueSet", "S" );
	currentValueSetId = (*env)->GetFieldID( env, clazz, "CurrentValueSet", "S" );
	nameId = (*env)->GetFieldID( env, clazz, "Name", "Ljava/lang/String;" );
	promptId = (*env)->GetFieldID( env, clazz, "Prompt", "Ljava/lang/String;" );
	defaultValueId = (*env)->GetFieldID( env, clazz, "DefaultValue", "Ljava/lang/String;" );
	currentValueId = (*env)->GetFieldID( env, clazz, "CurrentValue", "Ljava/lang/String;" );
	reportNameId = (*env)->GetFieldID( env, clazz, "ReportName", "Ljava/lang/String;" );
	needsCurrentValueId = (*env)->GetFieldID( env, clazz, "needsCurrentValue", "S" );
	isLimitedId = (*env)->GetFieldID( env, clazz, "isLimited", "S" );
	minSizeId = (*env)->GetFieldID( env, clazz, "MinSize", "D" );
	maxSizeId = (*env)->GetFieldID( env, clazz, "MaxSize", "D" );
	editMaskId = (*env)->GetFieldID( env, clazz, "EditMask","Ljava/lang/String;" );
	isHiddenId = (*env)->GetFieldID( env, clazz, "isHidden", "S" );
  	
  	jfield = (*env)->AllocObject( env, clazz );
  	//methodid = (*env)->GetMethodID ( env, clazz, "<init>", "(Ljava/lang/String;)V" );
	//if( methodid == 0 )
	//	{
	//	fprintf( stderr, "constructor for (%s) not found\n", szCRParameterField );
	//	return NULL;
	//	}
	//
	//jfield = (*env)->NewObject( env, clazz, methodid );
	//if( jfield == 0 )
	
	(*env)->SetShortField( env, jfield, valueTypeId, fieldInfo.ValueType );
	(*env)->SetShortField( env, jfield, defaultValueSetId, fieldInfo.DefaultValueSet );
	(*env)->SetShortField( env, jfield, currentValueId, fieldInfo.CurrentValueSet );
	(*env)->SetObjectField( env, jfield, nameId, (*env)->NewStringUTF( env, fieldInfo.Name ) );
	(*env)->SetObjectField( env, jfield, promptId, (*env)->NewStringUTF( env, fieldInfo.Prompt ) );
	(*env)->SetObjectField( env, jfield, defaultValueId, (*env)->NewStringUTF( env, fieldInfo.DefaultValue ) );
	(*env)->SetObjectField( env, jfield, currentValueId, (*env)->NewStringUTF( env, fieldInfo.CurrentValue ) );
	(*env)->SetObjectField( env, jfield, reportNameId, (*env)->NewStringUTF( env, fieldInfo.ReportName ) );
	(*env)->SetShortField( env, jfield, needsCurrentValueId, fieldInfo.needsCurrentValue );
	(*env)->SetShortField( env, jfield, isLimitedId, fieldInfo.isLimited );
	(*env)->SetDoubleField( env, jfield, minSizeId, fieldInfo.MinSize );
	(*env)->SetDoubleField( env, jfield, maxSizeId, fieldInfo.MaxSize );
	(*env)->SetObjectField( env, jfield, editMaskId, (*env)->NewStringUTF( env, fieldInfo.EditMask ) ); 
	(*env)->SetShortField( env, jfield, isHiddenId, fieldInfo.isHidden );
	
	return jfield;
  	}

JNIEXPORT jboolean JNICALL PREFIX(hasSavedData
  ) (JNIEnv * env, jobject obj, jshort rId )
 	{
 	BOOL rv;
 	if( FALSE == PEHasSavedData( rId, &rv ) )
 		{
 		int errorCode = throwCRException( env, rId );
 		return FALSE;
 		}
 	return rv;
 	}

JNIEXPORT jshort JNICALL PREFIX(discardSavedData
  ) (JNIEnv * env, jobject obj, jshort rId )
  	{
  	return ( FALSE == PEDiscardSavedData( rId ) )
  		? throwCRException( env, rId ) : 0 ;
  	}

/**

 */
JNIEXPORT jboolean JNICALL PREFIX(isPrintJobFinished
  ) (JNIEnv * env, jobject obj, jshort rId)
	{
	return PEIsPrintJobFinished( rId );
	}

/**

 */
JNIEXPORT jshort JNICALL PREFIX(getPageCount
  ) (JNIEnv * env, jobject obj, jshort rId)
  	{
  	jshort rv = PEGetNPages( rId );
  	return ( rv < 0 )
  		? throwCRException( env, rId ) : rv;
  	}

/**

 */
JNIEXPORT jshort JNICALL PREFIX(getFormulaCount
  ) (JNIEnv * env, jobject obj, jshort rId)
  	{
  	jshort rv = PEGetNFormulas( rId );
  	return ( rv < 0 )
  		? throwCRException( env, rId ) : rv;
  	}

/**

 */
JNIEXPORT jobjectArray JNICALL PREFIX(getFormula__SS
  ) (JNIEnv * env, jobject obj, jshort rId, jshort rIdx)
  	{	
  	BOOL rv;
	jclass clazz;
	jobjectArray jstrings;
	jstring jName, jFormula;
	short nameLength, formulaLength;
	HANDLE hName, hFormula;
	char* szName=0;
	char* szFormula=0;
	
	if( FALSE == PEGetNthFormula ( rId, rIdx,
                                    &hName,
                                    &nameLength,
                                    &hFormula,
                                    &formulaLength
                                    ) )
    	{
    	int errCode = throwCRException( env, rId );
		return NULL;
		}
	
	szName = malloc( nameLength+1 );
	// if( szdriver == 0 )
	szFormula = malloc( formulaLength+1 );
	// if( szprinter == 0 )
	
	// these will release the handles
	rv = PEGetHandleString( hName, szName, nameLength );
	//if( rv == FALSE )

	rv = PEGetHandleString( hFormula, szFormula, formulaLength );
	//if( rv == FALSE )
	
	szName[nameLength] = 0;
	szFormula[formulaLength] = 0;

	clazz = (*env)->FindClass( env, "java/lang/String" ); 
	// if clazz == 0
	
	jstrings = (*env)->NewObjectArray( env, 2, clazz, NULL ); 
	// if( jstrings == 0 )
	jName = (*env)->NewStringUTF( env, szName );
	// if( jName == 0 )
	jFormula = (*env)->NewStringUTF( env, szFormula );
	// if( jFormula == 0 )
	
	(*env)->SetObjectArrayElement( env, jstrings, 0, jName );
	(*env)->SetObjectArrayElement( env, jstrings, 1, jFormula );
	
	free( szName );
	free( szFormula );
	
	return jstrings;
  	}

/**

 */
JNIEXPORT jstring JNICALL PREFIX(getFormula__SLjava_lang_String_2
  ) (JNIEnv * env, jobject obj, jshort rId, jstring jName)
  	{
  	BOOL b;
  	jstring rv;
  	short iLen;
  	HANDLE hFormula;
  	char* szFormula;
  	const char* szName = (*env)->GetStringUTFChars( env, jName, 0 );
	// if szName == 0
	
  	b = PEGetFormula( rId, szName, &hFormula, &iLen );
	(*env)->ReleaseStringUTFChars( env, jName, szName );
	if( FALSE == b )
		{
		int errCode = throwCRException( env, rId );
		return NULL;
		}
	
	szFormula = malloc( iLen+1 );
	// if( szFormula == 0 )

	b = PEGetHandleString(hFormula, szFormula, iLen);
	// if( !b )
	szFormula[ iLen ] = 0;
	
	rv = (*env)->NewStringUTF( env, szFormula );
	// if( rv == 0 )
	
	free( szFormula );
	return rv;
  	}

/**

 */
JNIEXPORT jshort JNICALL PREFIX(setFormula
  ) (JNIEnv * env, jobject obj, jshort rId, jstring jName, jstring jFormula)
  	{
  	BOOL rv;
  	short errCode = 0;
	const char* szName;
	const char* szFormula;
	
	szName = (*env)->GetStringUTFChars( env, jName, 0 );
	// if szName == 0
	
	szFormula = (*env)->GetStringUTFChars( env, jFormula, 0 );
	// if szFormula == 0
	
	rv = PESetFormula( rId, szName, szFormula );
	
	(*env)->ReleaseStringUTFChars( env, jName, szName );
	(*env)->ReleaseStringUTFChars( env, jFormula, szFormula );
	
	return (FALSE == rv) ? throwCRException( env, rId ) : 0;
  	}

JNIEXPORT jshort JNICALL PREFIX(setPrintOptions
  ) (JNIEnv * env, jobject obj, jshort rId, jobject prOptions )
  	{
  	if( FALSE == PESetPrintOptions( rId, NULL ) )
  		{
  		return throwCRException( env, rId );
  		}
  	return 0;
  	}
  
  
/*
 */
JNIEXPORT jshort JNICALL PREFIX(getDefaultValuesCount
  ) (JNIEnv * env, jobject obj, jshort rId, jstring jFieldName, jstring jReportName)
  	{
  	short rv;
	const char* szFieldName;
	const char* szReportName;
	
	szFieldName = (*env)->GetStringUTFChars( env, jFieldName, 0 );
	// if szFieldName == 0
	szReportName = (*env)->GetStringUTFChars( env, jReportName, 0 );
	// if szReportName == 0
	
	rv = PEGetNParameterDefaultValues( rId, szFieldName, szReportName );
	if( rv == (short)-1 )
		{
		int errCode = throwCRException( env, rId );
		}
	
	(*env)->ReleaseStringUTFChars( env, jFieldName, szFieldName );
	(*env)->ReleaseStringUTFChars( env, jReportName, szReportName );
	return rv;
  	}

/*
 */
JNIEXPORT jshort JNICALL PREFIX(getCurrentValuesCount
  ) (JNIEnv * env, jobject obj, jshort rId, jstring jFieldName, jstring jReportName)
  	{
	const char* szFieldName;
	const char* szReportName;
  	unsigned short rv; // why this one is unsigned and others are not I don't know
  	
	szFieldName = (*env)->GetStringUTFChars( env, jFieldName, 0 );
	// if szFieldName == 0
	szReportName = (*env)->GetStringUTFChars( env, jReportName, 0 );
	// if szReportName == 0
  	
  	rv = PEGetNParameterCurrentValues( rId, szFieldName, szReportName );	
  	if ( (unsigned short)-1 == rv )
  		{
  		int errCode = throwCRException( env, rId );
  		}
  	
	(*env)->ReleaseStringUTFChars( env, jFieldName, szFieldName );
	(*env)->ReleaseStringUTFChars( env, jReportName, szReportName );
	return rv;
  	}

static jobject makeValueInfo( JNIEnv* env, PEValueInfo* vi )
	{
  	jobject jfield;
  	jclass clazz;
  	jmethodID methodid;
  	jfieldID typeId, numberId, currencyId, booleanId,
  		stringId, yearId, monthId, dayId, hourId, minuteId,
  		secondId, colorId, integerId, charId, longId;
		
  	clazz = (*env)->FindClass( env, szCRValueInfo ); 
	if( clazz == 0 )
		{
		jstring jString;
		char str[256];
		sprintf( str, "class %s not found\n", szCRValueInfo );
		jString = (*env)->NewStringUTF( env, str );
		// if jString == 0
		_throwCRException( env, jString );
		return NULL;
		}
		
  	jfield = (*env)->AllocObject( env, clazz );
	//if( jfield == 0 )
  	
	typeId = (*env)->GetFieldID( env, clazz, "type", "S" );
	(*env)->SetShortField( env, jfield, typeId, vi->valueType );
  	
	switch(  vi->valueType )
		{
		case PE_VI_NUMBER:
			numberId = (*env)->GetFieldID( env, clazz, "vNumber", "D" );
			(*env)->SetDoubleField( env, jfield, numberId, vi->viNumber );
			break;
		case PE_VI_CURRENCY:
			currencyId = (*env)->GetFieldID( env, clazz, "vCurrency", "D" );
			(*env)->SetDoubleField( env, jfield, currencyId, vi->viCurrency );
			break;
		case PE_VI_BOOLEAN:
			booleanId = (*env)->GetFieldID( env, clazz, "vBoolean", "Z" );
			(*env)->SetBooleanField( env, jfield, booleanId, vi->viBoolean );
			break;
		case PE_VI_DATE:
			yearId = (*env)->GetFieldID( env, clazz, "vYear", "S" );
			monthId = (*env)->GetFieldID( env, clazz, "vMonth", "S" );
			dayId = (*env)->GetFieldID( env, clazz, "vDay", "S" );
			
			(*env)->SetShortField( env, jfield, yearId, vi->viDate[0] );
			(*env)->SetShortField( env, jfield, monthId, vi->viDate[1] );
			(*env)->SetShortField( env, jfield, dayId, vi->viDate[2] );
			break;
		case PE_VI_STRING:
			stringId = (*env)->GetFieldID( env, clazz, "vString", "Ljava/lang/String;" );
			(*env)->SetObjectField( env, jfield, stringId, (*env)->NewStringUTF( env, vi->viString ) );
			break;
		case PE_VI_DATETIME:
			yearId = (*env)->GetFieldID( env, clazz, "vYear", "S" );
			monthId = (*env)->GetFieldID( env, clazz, "vMonth", "S" );
			dayId = (*env)->GetFieldID( env, clazz, "vDay", "S" );
			hourId = (*env)->GetFieldID( env, clazz, "vHour", "S" );
			minuteId = (*env)->GetFieldID( env, clazz, "vMinute", "S" );
			secondId = (*env)->GetFieldID( env, clazz, "vSecond", "S" );
			
			(*env)->SetShortField( env, jfield, yearId, vi->viDateTime[0] );
			(*env)->SetShortField( env, jfield, monthId, vi->viDateTime[1] );
			(*env)->SetShortField( env, jfield, dayId, vi->viDateTime[2] );
			(*env)->SetShortField( env, jfield, hourId, vi->viDateTime[3] );
			(*env)->SetShortField( env, jfield, minuteId, vi->viDateTime[4] );
			(*env)->SetShortField( env, jfield, secondId, vi->viDateTime[5] );
			break;
		case PE_VI_TIME:
			hourId = (*env)->GetFieldID( env, clazz, "vHour", "S" );
			minuteId = (*env)->GetFieldID( env, clazz, "vMinute", "S" );
			secondId = (*env)->GetFieldID( env, clazz, "vSecond", "S" );
			
			(*env)->SetShortField( env, jfield, hourId, vi->viTime[0] );
			(*env)->SetShortField( env, jfield, minuteId, vi->viTime[1] );
			(*env)->SetShortField( env, jfield, secondId, vi->viTime[2] );
			break;
		case PE_VI_INTEGER:
			integerId = (*env)->GetFieldID( env, clazz, "vInteger","S" );
			(*env)->SetShortField( env, jfield, integerId, vi->viInteger );
			break;
		case PE_VI_COLOR:
			{
			jint viColor = ( GetRValue( vi->viColor ) << 16 ) +
							( GetGValue( vi->viColor ) << 8 ) +
							GetBValue( vi->viColor );
			
			colorId = (*env)->GetFieldID( env, clazz, "vColor", "I" );
			(*env)->SetIntField( env, jfield, colorId, viColor );
			}
			break;
		case PE_VI_CHAR:
			charId = (*env)->GetFieldID( env, clazz, "vChar", "B" );
			(*env)->SetByteField( env, jfield, charId, vi->viC );
			break;
		case PE_VI_LONG:
			longId = (*env)->GetFieldID( env, clazz, "vLong", "I" );
			(*env)->SetShortField( env, jfield, longId, vi->viLong );
			break;
		case PE_VI_NOVALUE:
			break;
		default:
			fprintf( stderr, "unknown type %02X\n", vi->valueType );
			break;
		}
		
	return jfield;
	}
	
/*
 */
JNIEXPORT jobject JNICALL PREFIX(getDefaultValue
  ) (JNIEnv * env, jobject obj, jshort rId, jstring jFieldName, jstring jReportName, jshort index)
  	{
  	jobject rv;
	const char* szFieldName;
	const char* szReportName;
	
	PEValueInfo vi;
	vi.StructSize = PE_SIZEOF_VALUE_INFO;
	
	szFieldName = (*env)->GetStringUTFChars( env, jFieldName, 0 );
	// if szFieldName == 0
	szReportName = (*env)->GetStringUTFChars( env, jReportName, 0 );
	// if szReportName == 0
	
	if( FALSE == PEGetNthParameterDefaultValue( rId, szFieldName, szReportName, index, &vi ) )
		{
		int errCode = throwCRException( env, rId );
		rv = NULL;
		}
	else
		{
		rv = makeValueInfo( env, &vi );
		}
	
	(*env)->ReleaseStringUTFChars( env, jFieldName, szFieldName );
	(*env)->ReleaseStringUTFChars( env, jReportName, szReportName );
	return rv;
  	}

/*
 */
JNIEXPORT jobject JNICALL PREFIX(getCurrentValue
  ) (JNIEnv * env, jobject obj, jshort rId, jstring jFieldName, jstring jReportName, jshort index)
  	{
  	jobject rv;
	const char* szFieldName;
	const char* szReportName;
	
	PEValueInfo vi;
	vi.StructSize = PE_SIZEOF_VALUE_INFO;
	
	szFieldName = (*env)->GetStringUTFChars( env, jFieldName, 0 );
	// if szFieldName == 0
	szReportName = (*env)->GetStringUTFChars( env, jReportName, 0 );
	// if szReportName == 0
	
	if( FALSE == PEGetNthParameterCurrentValue( rId, szFieldName, szReportName, index, &vi ) )
		{
		int errCode = throwCRException( env, rId );
		rv = NULL;
		}
	else
		{
		rv = makeValueInfo( env, &vi );
		}
	
	(*env)->ReleaseStringUTFChars( env, jFieldName, szFieldName );
	(*env)->ReleaseStringUTFChars( env, jReportName, szReportName );
	return rv;
  	}

/*
 */
JNIEXPORT jshort JNICALL PREFIX(getParameterType
  ) (JNIEnv * env, jobject obj, jshort rId, jshort index)
  	{
  	short rv = PEGetNthParameterType( rId, index );
  	return ( (short)-1 == rv ) ? throwCRException( env, rId ) : rv;
  	}

static char* getString( JNIEnv* env, jobject jfield, jfieldID jid )
	{
	int len;
	const char* szStr;
	jstring jStr;
	char* rv;
	jStr = (*env)->GetObjectField( env, jfield, jid );
	if( jStr )
		{
		szStr = (*env)->GetStringUTFChars( env, jStr, 0 );
		len = strlen( szStr );
		rv = malloc( len+1 );
		lstrcpy( rv, szStr );
		(*env)->ReleaseStringUTFChars( env, jfield, szStr );
		}
	else
		{
		rv = NULL;
		}
	return rv;
	}
	
/*
	I will only deal with the toList, ccList, subject, and message.
 */
static short allocMAPI( JNIEnv* env, jobject jMAPI, UXDMAPIOptions* options )
	{
  	jclass clazz;
  	jfieldID toListId, ccListId, subjectId, messageId;
  	
	// first fill in the important stuff
	options->structSize = UXDMAPIOptionsSize;
	options->nRecipients = 0;
	options->recipients = NULL;
	
	clazz = (*env)->FindClass( env, szCRMAPI ); 
	if( clazz == 0 )
		{
		jstring jString;
		char str[256];
		sprintf( str, "class %s not found\n", szCRMAPI );
		jString = (*env)->NewStringUTF( env, str );
		_throwCRException( env, jString );
		return -1;
		}
		
	toListId = (*env)->GetFieldID( env, clazz, "toList", "Ljava/lang/String;" );
	ccListId = (*env)->GetFieldID( env, clazz, "ccList", "Ljava/lang/String;" );
	subjectId = (*env)->GetFieldID( env, clazz, "subject", "Ljava/lang/String;" );
	messageId = (*env)->GetFieldID( env, clazz, "message", "Ljava/lang/String;" );

	options->toList = getString( env, jMAPI, toListId );
	options->ccList = getString( env, jMAPI, ccListId );
	options->subject = getString( env, jMAPI, subjectId );
	options->message = getString( env, jMAPI, messageId );
	
	return 0;
	}

/*
 */
static void freeMAPI( UXDMAPIOptions* options )
	{
	if( options == NULL )
		return;
	if( options->toList )
		free( options->toList );
	if( options->ccList )
		free( options->ccList );
	if( options->subject )
		free( options->subject );
	if( options->message )
		free( options->message );
	}
	
/*
 */
JNIEXPORT jshort JNICALL PREFIX(exportTo
  ) (JNIEnv * env, jobject obj, jshort jobid, jobject joptions )
  	{
  	short err;
  	
  	PEExportOptions options;
  	
  	jobject jFormatOptions, jDestinationOptions;
  	
  	jclass clazz;
  	jfieldID formatDLLNameId, formatTypeId, formatOptionsId,
  			destinationDLLNameId, destinationTypeId, destinationOptionsId;
  	
  	clazz = (*env)->FindClass( env, szCRExportOptions ); 
	if( clazz == 0 )
		{
		jstring jString;
		char str[256];
		sprintf( str, "class %s not found\n", szCRExportOptions );
		jString = (*env)->NewStringUTF( env, str );
		_throwCRException( env, jString );
		return -1;
		}
		
	formatDLLNameId = (*env)->GetFieldID( env, clazz, "formatDLLName", "Ljava/lang/String;" );	
	formatTypeId = (*env)->GetFieldID( env, clazz, "formatType", "I" );
	formatOptionsId = (*env)->GetFieldID( env, clazz, "formatOptions","Ljava/lang/Object;" );

	destinationDLLNameId = (*env)->GetFieldID( env, clazz, "destinationDLLName", "Ljava/lang/String;" );	
	destinationTypeId = (*env)->GetFieldID( env, clazz, "destinationType", "I" );
	destinationOptionsId = (*env)->GetFieldID( env, clazz, "destinationOptions","Ljava/lang/Object;" );
	
  	
	options.StructSize = PE_SIZEOF_EXPORT_OPTIONS;
	err = extractField( env, joptions, formatDLLNameId, options.formatDLLName, PE_DLL_NAME_LEN );
	//
	options.formatType = (*env)->GetIntField( env, joptions, formatTypeId );
	//
	options.formatOptions = NULL;
	jFormatOptions = (*env)->GetObjectField( env, joptions, formatOptionsId );
	err = extractField( env, joptions, destinationDLLNameId, options.destinationDLLName, PE_DLL_NAME_LEN );
	//
	options.destinationType = (*env)->GetIntField( env, joptions, destinationTypeId );
	//
	options.destinationOptions = NULL;
	jDestinationOptions = (*env)->GetObjectField( env, joptions, destinationOptionsId );
	
	/*	check the format options for special type, now, I assume RTF
		that has NO options
	 */
	// nada
	
	/*	check the destination options for something I understand, currently I ignore
		everything but MAPI however I probably should do disk - that is simply
		a file name.
	*/
	if( (*env)->IsInstanceOf( env, options.formatOptions, (*env)->FindClass( env, szCRMAPI ) ) )
		{
		UXDMAPIOptions mapiOptions;
		err = allocMAPI( env, jDestinationOptions, &mapiOptions );
		//
		options.destinationOptions = &mapiOptions;
		if( FALSE == PEExportTo( jobid, &options ) )
			{
			freeMAPI( &mapiOptions );
			return throwCRException( env, jobid );
			}
			
		if( FALSE == PEStartPrintJob( jobid, TRUE ) )
			{
			freeMAPI( &mapiOptions );
			return throwCRException( env, jobid );
			}
		freeMAPI( &mapiOptions );
		}
	
	return 0;
	}
	
/*
	BSW:	Must exist for 1.2
*/
jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved)
	{
	//fprintf( stdout, "JNI_OnLoad\n" );
	
	logOnInfo.StructSize = PE_SIZEOF_LOGON_INFO;
	
	/*
	if( FALSE == PEOpenEngine() )
		{
		HANDLE		errorHandle;
		int errorCode;
		short errorLength;
		BOOL rv;
		char* errorString;
		
		errorCode = PEGetErrorCode( 0 ); // 0 for no job
	
		rv = PEGetErrorText( 0, &errorHandle, &errorLength );
		// if( !rv )
		
		errorString = malloc( errorLength+1 );
		// if( errorString == 0 )
	
		rv = PEGetHandleString(errorHandle, errorString, errorLength);
		// if( !rv )
		errorString[ errorLength ] = 0;
		
		fprintf( stderr, "cannot open CR engine: (%d) %s\n", errorCode, errorString );
		if( errorString != 0 )
			free( errorString );
		return 0; // should cause an error
		}
	*/
	return JNI_VERSION_1_2 ;
	}

void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved)
	{
	fprintf( stdout, "JNI_OnUnload\n" );
	if( TRUE == PECanCloseEngine() )
		{
		PECloseEngine();
		}
	else
		{
		fprintf( stderr, "cannot close CR engine busy\n" );
		}
	} 

/*
	BSW:	Any special initialization here - probably better to put it in OnLoad
*/	
BOOL WINAPI DllMain(  
		HINSTANCE hinstDLL, 
		DWORD fdwReason,    
		LPVOID lpvReserved )
	{
	//static char* what[4] =
	//	{
	//	"PROC_DETACH", "PROC_ATTACH", "THRD_ATTACH", "THRD_DETACH"
	//	};
	
	// can set last error (Windows)
	switch( fdwReason )
		{
		case DLL_PROCESS_ATTACH:
			break;
		case DLL_THREAD_ATTACH:
			break;
		case DLL_THREAD_DETACH:
			break;
		case DLL_PROCESS_DETACH:
			break;
		}
	// fprintf( stdout, "DllMain HNST %08X MESG %s RESV %08X\n", hinstDLL, what[fdwReason], lpvReserved );
	return TRUE;
	}