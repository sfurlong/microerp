/*
	File:		CRPrintOptions.java
	Purpose:
				Crystal Reports PrintOptions.
	Notes:
				This is un-implemented, as its
				all represented in DevMode.  
*/
package visi.crystal;

public class CRPrintOptions
	{
	public static final int UNCOLLATED = 0;
	public static final int COLLATED = 1;
	public static final int DEFAULTCOLLATION = 2;
	
	public int startPageN; // unsigned short
	public int stopPageN; // unsigned short
	public int nReportCopies; // unsigned short
	public int collation; // unsigned short
	public String outputFileName; // [PE_FILE_PATH_LEN=512]
	}
