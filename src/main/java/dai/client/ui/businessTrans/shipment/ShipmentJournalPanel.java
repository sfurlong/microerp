
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.ui.businessTrans.shipment;


import java.util.Vector;

import javax.swing.JFrame;

import dai.client.clientShared.daiFrame;
import dai.client.clientShared.daiJournalPanel;
import dai.shared.businessObjs.BusinessObject;
import dai.shared.businessObjs.shipment_journalObj;

public class ShipmentJournalPanel extends daiJournalPanel
{
	public ShipmentJournalPanel(JFrame container, daiFrame parentFrame, shipment_journalObj obj)
	{
		super(container, parentFrame, obj);
	}


    protected BusinessObject getNewBusinessObjInstance()
    {
        shipment_journalObj obj = new shipment_journalObj();
        shipment_journalObj tempObj = (shipment_journalObj)BUSINESS_OBJ;

		//Set the Primary Keys for the new Item Object.
		obj.set_id(tempObj.get_id());
		obj.set_locality(tempObj.get_locality());

        return obj;
    }

	protected void updateGridFromBusinessObj(Vector objVect)
	{
		shipment_journalObj itemObj;

		//Delete all the detail rows.
		try
		{
			GRID.removeAllRows();
		} catch (Exception ex)
		{
			LOGGER.logError(CONTAINER, "Couldn't delete all detail rows.\n" +ex.getLocalizedMessage());
		}

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < objVect.size(); i++)
		{
			itemObj = (shipment_journalObj)objVect.elementAt(i);
			GRID.addRow();

			GRID.set(i, COL_DATE_CREATED, itemObj.get_date_created());
			GRID.set(i, COL_CREATED_BY, itemObj.get_created_by());
            GRID.set(i, COL_SUBJECT, itemObj.get_subject());
			GRID.set(i, COL_NOTE, itemObj.get_note());
			GRID.set(i, OBJ_COL_POS, itemObj);
		}

		//Clear out the entry fields.
		clearEntryFields();
	}

	protected void updateBusinessObjFromGrid()
	{
		shipment_journalObj tempObj;

		//Refresh the all rows of the grid from the Item Vector.
		for (int i=0; i < GRID.getRowCount(); i++)
		{
			tempObj = (shipment_journalObj)GRID.get(i, OBJ_COL_POS);

			tempObj.set_date_created((String)GRID.get(i, COL_DATE_CREATED));
			tempObj.set_created_by((String)GRID.get(i, COL_CREATED_BY));
			tempObj.set_subject((String)GRID.get(i, COL_SUBJECT));
			tempObj.set_note((String)GRID.get(i, COL_NOTE));

			GRID.set(i, OBJ_COL_POS, tempObj);
		}
	}
}
