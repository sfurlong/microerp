package dai.shared.cmnSvcs;

import java.util.Vector;

import dai.shared.businessObjs.user_saved_reportsObj;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;

public class UserSavedReportsManager
{
	csDBAdapterFactory  dbAdapterFactory = csDBAdapterFactory.getInstance();
	csDBAdapter         dbAdapter = dbAdapterFactory.getDBAdapter();
	SessionMetaData     sessionMeta = SessionMetaData.getInstance();
	Logger              logger = Logger.getInstance();
	user_saved_reportsObj   userSavedRptObj = new user_saved_reportsObj();


	public static UserSavedReportsManager getInstance()
	{
		return _userSavedRptMgr;
	}

	public String saveRpt(String userID, String title, String rptSQL)
	{
		String sqlStatement="insert into USER_SAVED_REPORTS values ('"
							+ userID + "', 'SUPER', '" + title + "', '" + rptSQL + "')";
		try
		{
			dbAdapter.execDynamicSQL(sessionMeta.getClientServerSecurity(), sqlStatement);
			return "Report saved as <b>"+title+"</b>.";
		} catch (Exception e)
		{
			e.printStackTrace();
			return "Report could not be saved.";
		}
	}

	public Vector getTitles(String userID)
	{
		String sqlStatement="select REPORT_TITLE from USER_SAVED_REPORTS where "
							+ "ID = '" + userID + "'";
		Vector results = new Vector();

		try
		{
			results=dbAdapter.getDynamicSQLResults(sessionMeta.getClientServerSecurity(), sqlStatement);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return results;
	}

	public String getSQLStatement(String userID, String title)
	{
		String temp="";
		String sqlStatement = "select SQL_STATEMENT from USER_SAVED_REPORTS where "
							  + "ID='" + userID + "' and REPORT_TITLE='" + title + "'";
		Vector results = new Vector();

		try
		{
			results=dbAdapter.getDynamicSQLResults(sessionMeta.getClientServerSecurity(), sqlStatement);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		if (results.size() > 0)
		{
			temp=results.get(0).toString();
			temp=temp.substring(1, temp.indexOf("]"));
		}

		return temp;
	}

	private UserSavedReportsManager()
	{
	}

	private static UserSavedReportsManager _userSavedRptMgr = new UserSavedReportsManager();
}
