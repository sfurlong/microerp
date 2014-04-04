//Title:        Client Server Adapter Factory
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:
//      Singleton for Creating/Supplying Adapters

package dai.shared.csAdapters;

import dai.shared.cmnSvcs.*;

public class csLoggerAdapterFactory {

    public static csLoggerAdapterFactory getInstance() {
        return _loggerAdapterFactory;
    }

    public csLoggerAdapter getLoggerAdapter()
    {
        return _csLoggerAdapter;
    }

    /////////////////// Private Members/Methods ////////////////////////

    //Private constructor//
    private csLoggerAdapterFactory()
    {
        //Get the session data singleton instance.
        _sessionMetaData = SessionMetaData.getInstance();

        if (_sessionMetaData.getcsPlumbingType().equals("RMI")) {
            _csLoggerAdapter        = new csLoggerRMIAdapter(_sessionMetaData.getHostname());
        } else if (_sessionMetaData.getcsPlumbingType().equals("STUB")) {
            _csLoggerAdapter        = new csLoggerRMIAdapter();
        }
    }

    private static csLoggerAdapterFactory _loggerAdapterFactory = new csLoggerAdapterFactory();

    private SessionMetaData             _sessionMetaData;
    private csLoggerAdapter            _csLoggerAdapter;
}
