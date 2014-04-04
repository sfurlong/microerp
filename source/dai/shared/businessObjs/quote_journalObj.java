//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Your Name
//Company:      Your Company
//Description:  Your description

//!!WARNING:  THIS IS A GENERATED CLASS
//!!WARNING:  ONLY MODIFY SECTION BETWEEN BEGIN AND END OF USER SECTION

package dai.shared.businessObjs;

import java.util.Calendar;

import dai.shared.cmnSvcs.SessionMetaData;

public class quote_journalObj extends BusinessObject
{


	//!!!!!!BEGIN USER SECTION!!!!!!!

	public void clear(boolean clearImmutables)
	{
		String locality = get_locality();

		for (int i=0; i < MAX_COLS; i++)
		{
			m_dbAttribs[i].setValue(null);
		}

		if (!clearImmutables)
		{
			set_locality(locality);
		}
	}

	public DBAttributes[] getImmutableAttribs()
	{
		DBAttributes imutables[] = new DBAttributes[2];
		imutables[0] = new DBAttributes("id", get_id(), "CHAR", true);
		imutables[1] = new DBAttributes("locality", get_locality(), "CHAR", true);
		return imutables;
	}

	public void setDefaults()
	{
		//Initialize some of the attributes that should have default values.

		//Add the current date to the Date Created Field.
		Calendar now = Calendar.getInstance();
		SessionMetaData sessionMeta = null;
		sessionMeta = sessionMeta.getInstance();

		set_date_created(now.get(now.MONTH)+1 + "/" +
						 now.get(now.DAY_OF_MONTH) + "/" +
						 now.get(now.YEAR));
		set_created_by(sessionMeta.getUserId());
		set_locality(sessionMeta.getLocality());
	}

	//!!!!!! END USER SECTION !!!!!!!

	//STATIC DATA //
	static public final String TABLE_NAME = "QUOTE_JOURNAL";
	static public final String ID = "QUOTE_JOURNAL.ID";
	static public final String LOCALITY = "QUOTE_JOURNAL.LOCALITY";
	static public final String DETAIL_ID = "QUOTE_JOURNAL.DETAIL_ID";
	static public final String CREATED_BY = "QUOTE_JOURNAL.CREATED_BY";
	static public final String DATE_CREATED = "QUOTE_JOURNAL.DATE_CREATED";
	static public final String SUBJECT = "QUOTE_JOURNAL.SUBJECT";
	static public final String NOTE = "QUOTE_JOURNAL.NOTE";
	static public final String PRIORITY = "QUOTE_JOURNAL.PRIORITY";
	static public final String USER1 = "QUOTE_JOURNAL.USER1";
	static public final String USER2 = "QUOTE_JOURNAL.USER2";
	static public final String USER3 = "QUOTE_JOURNAL.USER3";
	static public final String USER4 = "QUOTE_JOURNAL.USER4";


	//Constructor
	public quote_journalObj()
	{
		//Private Field Members.
		MAX_COLS = 12;
		TAB_NAME = "QUOTE_JOURNAL";
		m_dbAttribs = new DBAttributes[MAX_COLS];
		Init();
		setDefaults();
	}

	public void Init()
	{
		m_dbAttribs[0] = new DBAttributes("ID", null, "VARCHAR", 30 ,true);
		m_dbAttribs[1] = new DBAttributes("LOCALITY", null, "VARCHAR", 20 ,true);
		m_dbAttribs[2] = new DBAttributes("DETAIL_ID", null, "INTEGER", 10 ,true);
		m_dbAttribs[3] = new DBAttributes("CREATED_BY", null, "VARCHAR", 20 ,true);
		m_dbAttribs[4] = new DBAttributes("DATE_CREATED", null, "DATE", 19 ,true);
		m_dbAttribs[5] = new DBAttributes("SUBJECT", null, "VARCHAR", 50 ,true);
		m_dbAttribs[6] = new DBAttributes("NOTE", null, "VARCHAR", 2000 ,true);
		m_dbAttribs[7] = new DBAttributes("PRIORITY", null, "VARCHAR", 20 ,true);
		m_dbAttribs[8] = new DBAttributes("USER1", null, "VARCHAR", 30 ,true);
		m_dbAttribs[9] = new DBAttributes("USER2", null, "VARCHAR", 30 ,true);
		m_dbAttribs[10] = new DBAttributes("USER3", null, "DATE", 19 ,true);
		m_dbAttribs[11] = new DBAttributes("USER4", null, "DATE", 19 ,true);
	}
	public String get_id()
	{return m_dbAttribs[0].getValue();}
	public String get_locality()
	{return m_dbAttribs[1].getValue();}
	public String get_detail_id()
	{return m_dbAttribs[2].getValue();}
	public String get_created_by()
	{return m_dbAttribs[3].getValue();}
	public String get_date_created()
	{return m_dbAttribs[4].getValue();}
	public String get_subject()
	{return m_dbAttribs[5].getValue();}
	public String get_note()
	{return m_dbAttribs[6].getValue();}
	public String get_priority()
	{return m_dbAttribs[7].getValue();}
	public String get_user1()
	{return m_dbAttribs[8].getValue();}
	public String get_user2()
	{return m_dbAttribs[9].getValue();}
	public String get_user3()
	{return m_dbAttribs[10].getValue();}
	public String get_user4()
	{return m_dbAttribs[11].getValue();}
	public void set_id(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[0].setValue(null);
		else
			m_dbAttribs[0].setValue(val);
	}

	public void set_locality(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[1].setValue(null);
		else
			m_dbAttribs[1].setValue(val);
	}

	public void set_detail_id(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[2].setValue(null);
		else
			m_dbAttribs[2].setValue(val);
	}

	public void set_created_by(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[3].setValue(null);
		else
			m_dbAttribs[3].setValue(val);
	}

	public void set_date_created(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[4].setValue(null);
		else
			m_dbAttribs[4].setValue(val);
	}

	public void set_subject(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[5].setValue(null);
		else
			m_dbAttribs[5].setValue(val);
	}

	public void set_note(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[6].setValue(null);
		else
			m_dbAttribs[6].setValue(val);
	}

	public void set_priority(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[7].setValue(null);
		else
			m_dbAttribs[7].setValue(val);
	}

	public void set_user1(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[8].setValue(null);
		else
			m_dbAttribs[8].setValue(val);
	}

	public void set_user2(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[9].setValue(null);
		else
			m_dbAttribs[9].setValue(val);
	}

	public void set_user3(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[10].setValue(null);
		else
			m_dbAttribs[10].setValue(val);
	}

	public void set_user4(String val)
	{
		if (val == null || val.length() == 0)
			m_dbAttribs[11].setValue(null);
		else
			m_dbAttribs[11].setValue(val);
	}

	public BusinessObject getNewInstance()
	{
		return new quote_journalObj();
	}


}
