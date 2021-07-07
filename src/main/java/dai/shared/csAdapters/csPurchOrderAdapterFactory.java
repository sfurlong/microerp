//Title:        Client Server Adapter Factory
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:
//      Singleton for Creating/Supplying Adapters

package dai.shared.csAdapters;

import dai.shared.cmnSvcs.SessionMetaData;

public class csPurchOrderAdapterFactory {

    public static csPurchOrderAdapterFactory getInstance() {
        return _purchOrderAdapterFactory;
    }

    public csPurchOrderAdapter getPurchOrderAdapter()
    {
        return _purchOrderAdapter;
    }

    /////////////////// Private Members/Methods ////////////////////////

    //Private constructor//
    private csPurchOrderAdapterFactory()
    {
        //Get the session data singleton instance.
        _sessionMetaData = _sessionMetaData.getInstance();

        //Instantiate the correct Client/Server Adapters.
        //Note:  ShipmentAdapter must be the first Adapter instantiated.
        if (_sessionMetaData.getcsPlumbingType().equals("RMI")) {
            _purchOrderAdapter        = new csPurchOrderRMIAdapter(_sessionMetaData.getHostname());
        } else if (_sessionMetaData.getcsPlumbingType().equals("STUB")) {
            _purchOrderAdapter        = new csPurchOrderRMIAdapter();
        }
    }

    private static csPurchOrderAdapterFactory _purchOrderAdapterFactory = new csPurchOrderAdapterFactory();

    private SessionMetaData             _sessionMetaData;
    private csPurchOrderAdapter         _purchOrderAdapter;
}
