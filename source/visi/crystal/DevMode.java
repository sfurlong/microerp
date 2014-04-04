/*
	File:		DevMode.java
	Rights:		Copyright 2005-2006, Altaprise Inc.
				All Rights Reserved world-wide.
	Purpose:
				Wrapper for Windows DevMode structure.
	Notes:
				For setting parameters, OR your DM_ fields
				that you set.  You do not have to do all of
				them, and re-using DevMode can be dangerous
				(i.e. you change the paper tray but not
				the imageable extent).
*/

package visi.crystal;

import java.awt.Point;

public class DevMode
	{
	public DevMode()
		{
		}
	
	public String deviceName; // [CCHDEVICENAME]
	public short specVersion;
	public short driverVersion;
	public short size;
	public short driverExtra;
	public int fields;
	public short orientation;
	public short paperSize;
	public short paperLength;
	public short paperWidth;
	public Point position;
	public short scale;
	public short copies;
	public short defaultSource;
	public short printQuality;
	public short color;
	public short duplex;
	public short yResolution;
	public short ttOption;
	public short collate;
	public String formName; // [CCFORMNAME]
	public short logPixels;
	public int bitsPerPel;
	public int pelsWidth;
	public int pelsHeight;
	public int displayFlags;
	public int displayFrequency;
	 // ignore the rest
	}