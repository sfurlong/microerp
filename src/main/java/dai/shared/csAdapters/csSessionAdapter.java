
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Base class for 


package dai.shared.csAdapters;

import dai.shared.cmnSvcs.csSecurity;
import dai.shared.cmnSvcs.daiRemoteServiceException;

abstract public class csSessionAdapter
{
	// connect to server //
	abstract public csSecurity connect(String url, String uid, String pwd)
	throws daiRemoteServiceException;

	// closeDB //
	abstract public void close(csSecurity security)
	throws daiRemoteServiceException;
}
