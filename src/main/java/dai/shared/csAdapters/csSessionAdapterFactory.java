//Title:        Client Server Adapter Factory
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:
//      Singleton for Creating/Supplying Adapters

package dai.shared.csAdapters;

import dai.shared.cmnSvcs.SessionMetaData;

public class csSessionAdapterFactory {

    public static csSessionAdapterFactory getInstance() {
        return _sessionAdapterFactory;
    }

    public csSessionAdapter getSessionAdapter()
    {
        return _csSessionAdapter;
    }

    /////////////////// Private Members/Methods ////////////////////////

    //Private constructor//
    private csSessionAdapterFactory()
    {
        //Get the session data singleton instance.
        _sessionMetaData = _sessionMetaData.getInstance();

        if (_sessionMetaData.getcsPlumbingType().equals("RMI")) {
            _csSessionAdapter        = new csSessionRMIAdapter(_sessionMetaData.getHostname());
        } else if (_sessionMetaData.getcsPlumbingType().equals("STUB")) {
            _csSessionAdapter        = new csSessionRMIAdapter();
        }
    }

    private static csSessionAdapterFactory _sessionAdapterFactory = new csSessionAdapterFactory();

    private SessionMetaData             _sessionMetaData;
    private csSessionAdapter            _csSessionAdapter;
}
