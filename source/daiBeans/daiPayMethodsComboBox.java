package daiBeans;

import java.util.HashMap;
import java.util.Vector;

import dai.shared.businessObjs.global_settings_pay_methodsObj;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class daiPayMethodsComboBox extends daiComboBox
{
    private HashMap _payMethodMap = new HashMap();

	public daiPayMethodsComboBox()
	{
		super();
        try {
    		jbInit();
        } catch (Exception e) {
            System.out.println(e);
        }
	}

    public String getPayMethodType (String payMethodId) {
        String ret = "";

        global_settings_pay_methodsObj obj = (global_settings_pay_methodsObj)_payMethodMap.get(payMethodId);
        if (obj != null) {
            ret = obj.get_pay_method_type();
        }
        return ret;
    }

	private void jbInit() throws Exception
	{
        this.setEditable(false);
        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
        csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();

        Vector vect = dbAdapter.queryByExpression(sessionMeta.getClientServerSecurity(),
                                    new global_settings_pay_methodsObj(),
                                    null );
        for (int i=0; i<vect.size(); i++) {
            global_settings_pay_methodsObj obj = (global_settings_pay_methodsObj)vect.elementAt(i);
            this.addItem(obj.get_pay_method_id());
            _payMethodMap.put(obj.get_pay_method_id(), obj);
        }
	}
}
