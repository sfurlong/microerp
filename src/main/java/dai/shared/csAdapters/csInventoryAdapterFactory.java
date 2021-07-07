//Title:        Client Server Adapter Factory
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:
//      Singleton for Creating/Supplying Adapters

package dai.shared.csAdapters;

import dai.shared.cmnSvcs.*;

public class csInventoryAdapterFactory {

    public static csInventoryAdapterFactory getInstance() {
        return _inventoryAdapterFactory;
    }

    public csInventoryAdapter getInventoryAdapter()
    {
        return _csInventoryAdapter;
    }

    /////////////////// Private Members/Methods ////////////////////////

    //Private constructor//
    private csInventoryAdapterFactory()
    {
        //Get the session data singleton instance.
        _sessionMetaData = SessionMetaData.getInstance();

        if (_sessionMetaData.getcsPlumbingType().equals("RMI")) {
            _csInventoryAdapter        = new csInventoryRMIAdapter(_sessionMetaData.getHostname());
        } else if (_sessionMetaData.getcsPlumbingType().equals("STUB")) {
            _csInventoryAdapter        = new csInventoryRMIAdapter();
        }
    }

    private static csInventoryAdapterFactory _inventoryAdapterFactory = new csInventoryAdapterFactory();

    private SessionMetaData             _sessionMetaData;
    private csInventoryAdapter          _csInventoryAdapter;
}
