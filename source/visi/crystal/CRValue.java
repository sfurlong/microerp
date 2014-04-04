/*
	File:		CRValue.java
	Rights:		Copyright 2005-2006, Altaprise Inc.
				All Rights Reserved world-wide.
	Purpose:
				Crystal Reports Value wrapper.
*/
package visi.crystal;

import java.awt.Color;
import java.util.GregorianCalendar;

/**
	A Variant Type.
	
	This class represents the Crystal "ValueInfo" 
	variant type.
	
	@see CRinf
*/
public class CRValue
	{
    public short type; // a PE_VI_ constant
    public double vNumber;
    public double vCurrency;
    public boolean vBoolean;
    public String vString; // [PE_VI_STRING_LEN];
    // short[] viDate [3]; // year, month, day
    // short[] viDateTime [6]; // year, month, day, hour, minute, second
    // short[] viTime [3];  // hour, minute, second
    public short vYear;
    public short vMonth;
    public short vDay;
    public short vHour;
    public short vMinute;
    public short vSecond;
    public int vColor; // COLORREF 0x00bbggrr -> sRGB
    public short vInteger;
    public byte vChar; // char
	public int vLong;
	
	public short getType()
		{
		return type;
		}
		
	public String toString()
		{
		// what about CRWNULL?
		switch( type )
			{
			case  CRinf.NUMBER:        //=0;
				return "NUMBER["+vNumber+"]";
			case  CRinf.CURRENCY:      //=1;
				return "CURRENCY["+vCurrency+"]";
			case  CRinf.BOOLEAN:       //=2;
				return "BOOLEAN["+vBoolean+"]";
			case  CRinf.DATE:          //=3;
				return "DATE["+vYear+"/"+vMonth+"/"+vDay+"]";
			case  CRinf.STRING:        //=4;
				return "STRING["+vString+"]";
			case  CRinf.DATETIME:      //=5;
				return "DATETIME["+vYear+"/"+vMonth+"/"+vDay+" "+
					vHour+":"+vMinute+":"+vSecond+"]";
			case  CRinf.TIME:          //=6;
				return "TIME["+vHour+":"+vMinute+":"+vSecond+"]";
			case  CRinf.INTEGER:		 //=7;	
				return "INTEGER["+vInteger+"]";
			case  CRinf.COLOR:		 //=8;	// COLOREF -> sRGB
				return "COLOR["+Integer.toHexString(vColor)+"]";
			case  CRinf.CHAR:			 //=9;	// Char
				return "CHAR["+Integer.toHexString((int)vChar)+"]";
			case  CRinf.LONG:			//=10;	// Long
				return "LONG["+vLong+"]";
			case  CRinf.NOVALUE:		//=100;	//No Value
				return "NOVALUE[]";
			default:
				return "[]";
			}
		}
	
	/**
		This returns the Variant as a java object.
		Detailed type info. is lost.  Time is returned as
		the number of seconds.  Color has been translated from
		COLORREF to sRGB by DLL.
	*/
	public Object getValue()
		{
		// what about CRWNULL?
		switch( type )
			{
			case  CRinf.NUMBER:        //=0;
				return new Double( vNumber );
			case  CRinf.CURRENCY:      //=1;
				return new Double( vCurrency );
			case  CRinf.BOOLEAN:       //=2;
				return new Boolean( vBoolean );
			case  CRinf.DATE:          //=3;
				return new GregorianCalendar( vYear, vMonth, vDay );
			case  CRinf.STRING:        //=4;
				return vString;
			case  CRinf.DATETIME:      //=5;
				return new GregorianCalendar( vYear, vMonth, vDay,
					vHour, vMinute, vSecond );
			case  CRinf.TIME:          //=6;
				return new Integer( (vHour*60*60)+(vMinute*60)+vSecond );
			case  CRinf.INTEGER:		 //=7;	
				return new Short( vInteger );
			case  CRinf.COLOR:		 //=8;	// sRGB
				return new Color( vColor );
			case  CRinf.CHAR:			 //=9;	// Char
				return new Byte( vChar );
			case  CRinf.LONG:			//=10;	// Long
				return new Integer( vLong );
			case  CRinf.NOVALUE:		//=100;	//No Value
			default:
				return null;
			}
		}
	}