
//Title:        Client Server DB Adapter
//Copyright:    Copyright (c) 1999
//Author:       sfurlong
//Company:      Digital Artifacts Inc.
//Description:


package dai.server;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import dai.idl.rmi.DBService;
import dai.idl.rmi.InventoryService;
import dai.idl.rmi.LoggerService;
import dai.idl.rmi.PurchOrderService;
import dai.idl.rmi.SessionService;
import dai.idl.rmi.ShipmentService;
import dai.shared.cmnSvcs.SessionMetaData;

public class StartRMIServices 
{
	dai.server.dbService.DBRMIServiceImpl dbService = null;
	dai.server.inventoryService.InventoryRMIServiceImpl inventoryService = null;
	dai.server.loggerService.LoggerRMIServiceImpl loggerService = null;
	dai.server.purchOrderService.PurchOrderRMIServiceImpl purchOrderService = null;
	dai.server.sessionService.SessionRMIServiceImpl sessionService = null;
	dai.server.shipmentService.ShipmentRMIServiceImpl shipmentService = null;


	public StartRMIServices() {

		try
		{
            System.setSecurityManager (new RMISecurityManager());

            loggerService = new dai.server.loggerService.LoggerRMIServiceImpl();
            Naming.rebind("//"+SessionMetaData.getInstance().getHostname()+"/"+LoggerService.SERVER_NAME, loggerService);
			
			dbService = new dai.server.dbService.DBRMIServiceImpl();
            Naming.rebind("//"+SessionMetaData.getInstance().getHostname()+"/"+DBService.SERVER_NAME, dbService);

			inventoryService = new dai.server.inventoryService.InventoryRMIServiceImpl();
            Naming.rebind("//"+SessionMetaData.getInstance().getHostname()+"/"+InventoryService.SERVER_NAME, inventoryService);
			
            purchOrderService = new dai.server.purchOrderService.PurchOrderRMIServiceImpl();
            Naming.rebind("//"+SessionMetaData.getInstance().getHostname()+"/"+PurchOrderService.SERVER_NAME, purchOrderService);
			
            sessionService = new dai.server.sessionService.SessionRMIServiceImpl();
            Naming.rebind("//"+SessionMetaData.getInstance().getHostname()+"/"+SessionService.SERVER_NAME, sessionService);
			
            shipmentService = new dai.server.shipmentService.ShipmentRMIServiceImpl();
            Naming.rebind("//"+SessionMetaData.getInstance().getHostname()+"/"+ShipmentService.SERVER_NAME, shipmentService);

            String[] names = Naming.list(SessionMetaData.getInstance().getHostname());

            for (int i=0; i<names.length; i++) {
                System.out.println(names[i] + " Started");
            }
		
        } catch (Exception e)
		{
			System.err.println("StartRMIServices exception: " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public static void main (String[] args)
	{
		new StartRMIServices();
	}
}
