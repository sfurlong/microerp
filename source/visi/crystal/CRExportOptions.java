/*
	File:		CRExportOptions.java
	Rights:		Copyright 2005-2006, Altaprise Inc.
				All Rights Reserved world-wide.
	Purpose:
				Crystal Reports ExportOptions wrapper.
	Notes:
				Only export of RTF to MAPI is supported.
				Use outputToFile for a few others.
*/
package visi.crystal;

/**

	Type information is specific to the type of DLL
	being used.
*/
public class CRExportOptions
	{
	public static final int UXFRichTextFormatType = 0;
	public static final int UXDMAPIType =0;
	
	public String formatDLLName; // [PE_DLL_NAME_LEN=64]
	public int formatType;
	public Object formatOptions;
	public String destinationDLLName; // [PE_DLL_NAME_LEN=64]
	public int destinationType;
	public Object destinationOptions;
	public short nFormatOptionsBytes;
	public short nDestinationOptionsBytes;
	}