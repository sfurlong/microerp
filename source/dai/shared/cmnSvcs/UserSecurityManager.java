package dai.shared.cmnSvcs;

import java.util.HashMap;
import java.util.Vector;

import dai.shared.businessObjs.user_securityObj;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class UserSecurityManager {

    Logger logger;

    public static UserSecurityManager getInstance()
    {
        return _userSecurityMgr;
    }

    public boolean getComponentSecurity(String componentId) throws daiException
    {
        boolean ret = false;

        user_securityObj obj = (user_securityObj)_securityMap.get(componentId);
        if (obj == null) {
            System.out.println("Security Component Not Found: " + componentId);
        } else {
            String flag = obj.get_read_permission();
            if (flag != null && flag.equals("Y")) {
                ret = true;
            }
        }
        return ret;
    }


/////////////////  Private Methods/Data //////////////////////////

    //Private Constructor.  Enforces Singleton.
    private UserSecurityManager()
    {
        //The constructor should load the list of security
        //components from the database.
        csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();
        SessionMetaData     sessionMeta = SessionMetaData.getInstance();
        logger          = logger.getInstance();

        String exp = " id = '" + sessionMeta.getUserId() +
                    "' and locality='" + user_securityObj.getObjLocality() + "'";
        try {
            //Get all the security objects from the database.
            Vector v = dbAdapter.queryByExpression( sessionMeta.getClientServerSecurity(),
                                                    new user_securityObj(),
                                                    exp);
            //Fill up the hash map with all the security objects.
            for (int i=0; i<v.size(); i++)
            {
                user_securityObj obj = (user_securityObj)v.elementAt(i);
                String key = obj.get_component_id();
                _securityMap.put(key, obj);
            }
        } catch (Exception e) {
            String msg = "UserSecurityManager::constructor\n"+e.getLocalizedMessage();
            logger.logError(null, msg);
            e.printStackTrace();
        }
    }

    //Declaration of the Private Static metaData.
    //Since it's static, only one copy will exist of the instance data,
    //a la Singleton pattern.
    private static UserSecurityManager _userSecurityMgr = new UserSecurityManager();

    //Hashmap of security components.
    private HashMap _securityMap = new HashMap();

}
