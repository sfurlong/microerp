//Title:        Client Server Adapter Factory
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:
//      Singleton for Creating/Supplying Adapters

package dai.shared.csAdapters;

import dai.shared.cmnSvcs.*;

public class csDBAdapterFactory {

    public static csDBAdapterFactory getInstance() {
        return _dbAdapterFactory;
    }

    public csDBAdapter getDBAdapter()
    {
        return _csDBAdapter;
    }

    /////////////////// Private Members/Methods ////////////////////////

    //Private constructor//
    private csDBAdapterFactory()
    {
        //Get the session data singleton instance.
        _sessionMetaData = SessionMetaData.getInstance();

        if (_sessionMetaData.getcsPlumbingType().equals("RMI")) {
            _csDBAdapter        = new csDBRMIAdapter(_sessionMetaData.getHostname());
        } else if (_sessionMetaData.getcsPlumbingType().equals("STUB")) {
            _csDBAdapter        = new csDBRMIAdapter();
        }
    }

    private static csDBAdapterFactory _dbAdapterFactory = new csDBAdapterFactory();

    private SessionMetaData             _sessionMetaData;
    private csDBAdapter                 _csDBAdapter;
}
