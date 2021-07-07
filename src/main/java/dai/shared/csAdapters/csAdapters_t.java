
//Title:        Shared Files
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Client Server shared files

package dai.shared.csAdapters;

import dai.shared.cmnSvcs.*;

public class csAdapters_t {

    public csAdapters_t() {
        csSessionAdapterFactory sessionAdapterFactory = null;
        csDBAdapterFactory dbAdapterFactory = null;
        SessionMetaData sessionMeta = null;

        sessionMeta = sessionMeta.getInstance();
        sessionAdapterFactory = sessionAdapterFactory.getInstance();
        dbAdapterFactory = dbAdapterFactory.getInstance();

        csSessionAdapter sessionAdapter = sessionAdapterFactory.getSessionAdapter();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

        try {
            csSecurity security = sessionAdapter.connect(sessionMeta.getServerDBURL(),
                                                    "STEVE",
                                                    "fur");

            //Use the dbService to get some data from the DB.
            String tabNames[] = dbAdapter.getTableNames(security);
            for (int i=0; i<tabNames.length; i++)
            {
                System.out.println(tabNames[i]);
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        csAdapters_t vCsAdapters_t = new csAdapters_t();
        vCsAdapters_t.invokedStandalone = true;
    }
    private boolean invokedStandalone = false;
} 