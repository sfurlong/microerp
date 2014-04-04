//Title:        Client Server Adapter Factory
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:
//      Singleton for Creating/Supplying Adapters

package dai.shared.csAdapters;

import dai.shared.cmnSvcs.SessionMetaData;

public class csShipmentAdapterFactory {

    public static csShipmentAdapterFactory getInstance() {
        return _ShipmentAdapterFactory;
    }

    public csShipmentAdapter getShipmentAdapter()
    {
        return _csShipmentAdapter;
    }

    /////////////////// Private Members/Methods ////////////////////////

    //Private constructor//
    private csShipmentAdapterFactory()
    {
        //Get the session data singleton instance.
        _sessionMetaData = _sessionMetaData.getInstance();

        //Instantiate the correct Client/Server Adapters.
        //Note:  ShipmentAdapter must be the first Adapter instantiated.
        if (_sessionMetaData.getcsPlumbingType().equals("RMI")) {
            _csShipmentAdapter        = new csShipmentRMIAdapter(_sessionMetaData.getHostname());
        } else if (_sessionMetaData.getcsPlumbingType().equals("STUB")) {
            _csShipmentAdapter        = new csShipmentRMIAdapter();
        }
    }

    private static csShipmentAdapterFactory _ShipmentAdapterFactory = new csShipmentAdapterFactory();

    private SessionMetaData             _sessionMetaData;
    private csShipmentAdapter           _csShipmentAdapter;
}
