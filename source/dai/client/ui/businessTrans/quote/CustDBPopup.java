package dai.client.ui.businessTrans.quote;

import javax.swing.JFrame;

import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.DBAttributes;
import dai.shared.businessObjs.customerObj;
import dai.shared.businessObjs.prospectObj;
import daiBeans.DataChooser;
import daiBeans.daiDBIdPopupField;

public class CustDBPopup  extends daiDBIdPopupField
{

    public CustDBPopup(JFrame parentFrame, BusinessObject bizObj,
                       String bizObjField2Name, String labelText)
    {
        super(parentFrame, bizObj, bizObjField2Name, labelText);
    }

    protected void openDataChooser()
    {
        String idFieldText = getId();
        String nameFieldText = getName();
        DataChooser chooser = null;

        DBAttributes attrib1 = new DBAttributes(_bizObj.getTableName()+".ID", idFieldText, "Id", 100);
        DBAttributes attrib2 = new DBAttributes(_bizObjField2Name, nameFieldText, "Name", 200);
        if (_bizObj.getTableName().equals(prospectObj.TABLE_NAME)) {
            DBAttributes attrib3 = new DBAttributes(prospectObj.LAST_NAME, "Prospect Last Name", 200);
    		chooser = new DataChooser(_parentFrame, "Data Chooser",
								   	  _bizObj,
                                      new DBAttributes[]{attrib1, attrib2, attrib3},
                                      null,
                                      " order by COMPANY_NAME, Last_name");
        } else {
    		chooser = new DataChooser(_parentFrame, "Data Chooser",
								      _bizObj,
                                      new DBAttributes[]{attrib1, attrib2},
                                      null,
                                      " order by " + customerObj.NAME);
        }

		chooser.show();
        BusinessObject chosenObj = chooser.getChosenObj();
        populateFields(chosenObj);
    	chooser.dispose();
    }

} 