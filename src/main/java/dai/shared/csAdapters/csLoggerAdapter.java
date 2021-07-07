
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Base class for 


package dai.shared.csAdapters;

import dai.shared.cmnSvcs.*;

abstract public class csLoggerAdapter
{
	abstract public void logError(csSecurity security, String msg);

	abstract public void logSQL(csSecurity security, String msg);
}
