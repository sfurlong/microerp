/*
	File:		CRParameterField.java
	Rights:		Copyright 2005-2006, Altaprise Inc.
				All Rights Reserved world-wide.
	Purpose:
				Crystal Reports ParameterField wrapper.
	Notes:
*/
package visi.crystal;

public class CRParameterField
	{

	
    // PE_PF_ constant
    public short ValueType;

    // Indicate the default value is set in PEParameterFieldInfo.
    public short DefaultValueSet;

    // Indicate the current value is set in PEParameterFieldInfo.
    public short CurrentValueSet;

    // All strings are null-terminated.
    public String  Name ; // [PE_PF_NAME_LEN];
    public String  Prompt ; // [PE_PF_PROMPT_LEN];

    // Could be Number, Currency, Boolean, Date, DateTime, Time, or String
    // NB: these ARE strings - currently ignore PF to VI conversion
    public String  DefaultValue ; // [PE_PF_VALUE_LEN];
    public String  CurrentValue ; // [PE_PF_VALUE_LEN];

    // name of report where the field belongs, only used in
    // PEGetNthParameterField and PENewParameterField
    public String  ReportName ; // [PE_PF_REPORT_NAME_LEN];

    // returns false if parameter is linked, not in use, or has current value set
    public short needsCurrentValue; 

    //for String values this will be TRUE if the string is limited on length, for 
	//other types it will be TRUE if the parameter is limited by a range
    public short isLimited; // this field is also known as MandatoryPrompt

    //For string fields, these are the minimum/maximum length of the string.
    //For numeric fields, they are the minimum/maximum numeric value.
    public double MinSize;
    public double MaxSize;

    //An edit mask that restricts what may be entered for string parameters.
    public String  EditMask ; // [PE_PF_EDITMASK_LEN];

    //  return true if it is essbase sub var
    public short isHidden; // true, false, or unchanged
    
    public String toString()
    	{
    	return new String(
    		"ValueType["+ValueType+
    		"],DefaultValueSet["+DefaultValueSet+
    		"],CurrentValueSet["+CurrentValueSet+
    		"],Name["+Name+
    		"],Prompt["+Prompt+
    		"],DefaultValue["+DefaultValue+
    		"],CurrentValue["+CurrentValue+
    		"],Reportname["+ReportName+
    		"],needsCurrentValue["+needsCurrentValue+
    		"],isLimited["+isLimited+
    		"],MinSize["+MinSize+
    		"],MaxSize["+MaxSize+
    		"],EditMask["+EditMask+
    		"],isHidden["+isHidden+
    		"]" );
    	}
	}