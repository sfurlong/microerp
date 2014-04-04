/*
	File:		CRinf.java
	Rights:		Copyright 2005-2006, Altaprise Inc.
				All Rights Reserved world-wide.
	Purpose:
				Crystal Reports interface (inf).  This is the
				wrapper you run javah on.
				
	Notes:
				javah -cp altaprise.jar visi.crystal.CRinf
*/
package visi.crystal;

/**
	This is the wrapper on the crystal reporting DLL.
	
	See CRinf.c for implementation and make the
	header file via "javah -classpath polaris.jar visi.mav.util.CRinf".<P>
	
	Most functions return an error code.  As these functions also
	throw exceptions, you can safely ignore them!  They are here for
	historical reasons.<P>
 */

public final class CRinf
{
	// these are for informational purposes - the DLL will enforce constraints
	//public static final int PE_WORD_LEN            =2;
	public static final int  REPORT_NAME_LEN =128;
	public static final int  NAME_LEN        =256;
	public static final int  PROMPT_LEN      =256;
	public static final int  VALUE_LEN       =256;
	public static final int  EDITMASK_LEN    =256;
	public static final int  STRING_LEN      =256;

	// these are ValueType PE_VI_ or PE_PF_
	public static final short  NUMBER        =0;
	public static final short  CURRENCY      =1;
	public static final short  BOOLEAN       =2;
	public static final short  DATE          =3;
	public static final short  STRING        =4;
	public static final short  DATETIME      =5;
	public static final short  TIME          =6;
	public static final short  INTEGER       =7;    
	public static final short  COLOR         =8;	// COLOREF
	public static final short  CHAR          =9;	// Char
	public static final short  LONG         =10;	// Long
	public static final short  NOVALUE      =100;	//No Value

	// not using booleans 'cause sometimes tristate
	public static final short  UNCHANGED    =-1;
	public static final short  FALSE    = 0;
	public static final short  TRUE    = 1;

	// CRWNULL
	public static final String CRWNULL = "CRWNULL";

	// Supported file export formats
	public static final int HTML  =1;
	public static final int XLS   =2;
	public static final int CSV   =3;
	public static final int TEXT  =4;
	public static final int RTF   =5;
	public static final int WORDWIN  =6;

	// I am Singleton (stupid stupid term for global static instance)
	protected static CRinf instance = null;

	/**
		Get instance.
		
		Return myself - there can only be one.
		@return CRinf instance
	*/
	public static CRinf getInstance()
	{
		if ( instance == null )
		{
			instance = new CRinf();
		}
		return instance;
	}

	/**
		<init>
		
		Load the DLL automagically.<P>
		
		Make this static for clinit.
	*/
	/*<init>*/  
    {
		//System.out.println( "JNI_Loading: CRINF.DLL" );
		try
		{
			System.loadLibrary("CRinf"); // anywhere in the path - .DLL is added for Windows
		} catch ( Throwable e )	// security exceptions...
		{
			System.err.println( e.toString() );
		}
	}

	/**
		Open the CR engine.
		
		This MUST be done once per thread.  All CR engine usage
		should be in a single procedural block on their own thread
		(*not the AWT Event Dispatcher a la actionPerformed *).
		
		@return error code or zero for no error
	*/  
	public native short open() throws CRException;

	/**
		Close the CR engine.
		
		All open calls must be bounded by a close call.
		
		@see canClose
		@see isPrintJobFinished
	*/
	public native void close();

	/**
		Set log on information for a table.
		
		Note the server and database names are not the JDBC specifiers
		(i.e. driver:host:port:instance whatever)
		
		@param reportId the report id
		@param server the host name (i.e. "MAPAPP1")
		@param database the database name (i.e. "" for Oracle - *not the SID*)
		@param userid the user name
		@param password the user's password
		@return error code or zero for no error
	*/
	public native short setNthTableLogOnInfo(short reportId, short tableN, boolean propagate, String server, String database, String userid, String password) throws CRException;

	// for compatability with whomever did it this way
	public short setNthTableLogOnInfo(short reportId, String server, String database, String userid, String password) throws CRException
	{
		return setNthTableLogOnInfo( reportId, (short)0, false, server, database, userid, password );
	}

	public native short logOnServer( String driver, String server, String database, String userid, String password) throws CRException;

	public native short logOffServer() throws CRException;

	/**
		Open the report file.
		
		Note report file names are currently platform dependant!
		Eventually they should be generated in a platform neutral manner.
		
		@param reportFile path and file name
		@return the report id (<= zero is bad, but should not happen)
	*/
	public native short openReport( String reportFile ) throws CRException;

	public native short closeReport( short reportId ) throws CRException;

	public native short openSubReport(short reportId, String reportFile ) throws CRException;

	public native short closeSubReport( short reportId ) throws CRException;

	public native short outputToFile( short reportId, String destination, int exportFormat ) throws CRException;

	public native short outputToWindow( short reportId, String title, int left, int top, int width, int height ) throws CRException;

	public native short outputToPrinter( short reportId, short copies) throws CRException;

	public native short getParameterFieldCount( short reportId ) throws CRException;

	public native short setNthParameterField( short reportId, short parameterId,  CRParameterField field ) throws CRException;

	public native CRParameterField getNthParameterField( short reportId, short parameterIdx ) throws CRException;

	public native short setSelectionFormula( short reportId, String selection ) throws CRException;

	public native short getErrorCode( short reportId );

	public native String getErrorText( short reportId ) throws CRException;

	/**
		Test - throws an exception.
		
		@return a number you will never see
	*/
	public native short testReport();

	/**
		Return state of CR engine.
		
		If you cannot close the engine, you should probably wait until you can.
		
		@return true if safe to call close
	*/
	public native boolean canClose();      

	/**
		Get the selected printer (for a report.)
		
		The return value is a packed array of three strings.<UL>
		<LI> the driver
		<LI> the printer 
		<LI> the port <LI> DevMode </UL>
		
		@param reportId the report id
		@return array of three strings
	*/
	public native Object[] getSelectedPrinter( short reportId ) throws CRException;

	public short selectPrinter( short reportId, String driver, String printer, String port ) throws CRException
	{
		return selectPrinter( reportId, driver, printer, port, null );
	}

	public native short selectPrinter( short reportId, String driver, String printer, String port, DevMode mode ) throws CRException;

	public Object[] pageSetup( boolean showDialog )
	{
		return pageSetup( showDialog, null );
	}

	/**
		Call Windows native page set up dialog.

		Allows set up of paper (not margins), orientation, and printer.
		The return value is a packed array of three strings.<UL>
		<LI> the driver
		<LI> the printer
		<LI> the port <LI> DevMode </UL>

		@param showDialog show the dialog (false to get defaults)
		@param devMode initial devmode or null
	*/
	public native Object[] pageSetup( boolean showDialog, DevMode devMode );

	/**
		Get count of stored procedures.

		@return the number of stored procedure parameters.
		@deprecated use getParameterFieldCount
	*/
	public native short getSPParamCount( short reportId ) throws CRException; // get (stored procedure) param count

	/**
		Get stored procedures parameter.
		
		@param reportId the report id
		@param idx the parameter index
		@return the stored procedure parameter.
		@deprecated use getNthParameterField
	*/
	public native String getSPParam( short reportId, short idx ) throws CRException;  // get Nth (stored procedure) param

	/**
		Set stored procedures parameter.
		
		@param reportId the report id
		@param idx the parameter index
		@param param the parameter
		@return error code or zero if no error
		@deprecated use setNthParameterField
	*/
	public native short setSPParam( short reportId, short idx, String param ) throws CRException; // set Nth (stored procedure) param

	public native short setSQLQuery( short reportId, String query ) throws CRException;

	public native String getSQLQuery( short reportId ) throws CRException;

	public native String getSelectionFormula( short reportId ) throws CRException;

	/**
		Ask if report has saved data.
		
		Parameters will be saved (by default.)
		
		@param reportId the report id
		@return true if the report has saved data
	*/
	public native boolean hasSavedData( short reportId ) throws CRException;  

	/**
		Throw away any saved data.
		
		Only call if the report has saved data and you want to get rid of it.
		
		@param reportId the report id
		@return true if the report has saved data
		@see hasSavedData
	*/
	public native short discardSavedData( short reportId ) throws CRException;

	/**
		Ask if printing (or previewing) is finished.
		
		@param reportId the report id
		@return true if the report has printed, or the last page has been viewed
	*/
	public native boolean isPrintJobFinished( short reportId );

	/**
		Count the number of pages in a print job.
		
		@param reportId the report id
		@return the number of pages
	*/
	public native short getPageCount( short reportId ) throws CRException;

	public native short getFormulaCount( short reportId ) throws CRException;

	/**
		Return the name and formula at index.
		
		This call returns an array of two Strings.  The first is the
		formula name.  The second is the formula itself.  Setting
		formulae is done by name.
		
		@param reportId the report id
		@param idx the formula index
		@return the two strings packed into an array
	*/
	public native Object[] getFormula( short reportId, short idx ) throws CRException;

	public native String getFormula( short reportId, String jName ) throws CRException;

	public native short setFormula( short reportId, String jName, String jFormula ) throws CRException;

	/**
		Set the print options (not implemented fully.)
		
		This call does NOT implement a standard print dialog, so you cannot choose the
		printer.
		
		@param reportId the report id
		@param prOptions print options, or null to be prompted
		@return error code
	*/
	public native short setPrintOptions( short reportId, CRPrintOptions prOptions ) throws CRException;

	/**
		Get the number of default values.
		
		@param reportId the report id
		@param fieldName the field name
		@param reportName the report name the field applies to (not always the report id)
		@return the count of default values
	*/
	public native short getDefaultValuesCount( short reportId, String fieldName, String reportName ) throws CRException;  

	/**
		Get the number of current values.
		
		@param reportId the report id
		@param fieldName the field name
		@param reportName the report name the field applies to (not always the report id)
		@return the count of current values
	*/
	public native short getCurrentValuesCount( short reportId, String fieldName, String reportName ) throws CRException;  // int is NOT a mistake

	/**
		Get a default value.  (getNthDefaultValueInfo)
		
		@param reportId the report id
		@param fieldName the field name
		@param reportName the report name the field applies to (not always the report id)
		@param index the parameter index
		@return the count of current values
	*/
	public native CRValue getDefaultValue( short reportId, String fieldName, String reportName, short index ) throws CRException;  

	/**
		Get a current value.  (getNthCurrentValueInfo)
		
		@param reportId the report id
		@param fieldName the field name
		@param reportName the report name the field applies to (not always the report id)
		@param index the parameter index
		@return the count of current values
	*/
	public native CRValue getCurrentValue( short reportId, String fieldName, String reportName, short index ) throws CRException;

	/**
		Get a parameter field's type.
		
		@param reportId the report id
		@param index the parameter field index
		@return the parameter type
		
		@see getNthParameterField
	*/
	public native short getParameterType( short reportId, short index ) throws CRException;

	/**
		Export the report with specific details.
		
		@param reportId the report id
		@param options the export options
		@return error code or zero for no error
	*/
	public native short exportTo( short reportId, CRExportOptions options ) throws CRException;
}

