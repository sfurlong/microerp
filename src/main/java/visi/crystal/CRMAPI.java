/*
	File:		CRMAPI.java
	Rights:		Copyright 2005-2006, Altaprise Inc.
				All Rights Reserved world-wide.
	Purpose:
				Crystal Reports MAPI wrapper.
	Notes:
				semi-colon delimiter for to and cc
*/
package visi.crystal;

public class CRMAPI
	{
    // WORD structSize;

    //char FAR *toList;           // 'toList' and 'ccList' are ignored
    public String toList;
    //char FAR *ccList;           // if specifying recipients below.
	public String ccList;
	
    //char FAR *subject;
    public String subject;
    //char FAR *message;
    public String message;

    //WORD nRecipients;           // 'nRecipients' must be 0 if specifying
    //lpMapiRecipDesc recipients; // To and CC lists above.
	
	}