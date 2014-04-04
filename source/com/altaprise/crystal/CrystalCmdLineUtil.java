package com.altaprise.crystal;

import java.util.Calendar;
import java.util.Date;

import dai.shared.cmnSvcs.PropertyFileData;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.daiProperties;

public class CrystalCmdLineUtil {
	SessionMetaData smd = null;

	/**
	 *
	 * @param userId The User Id that is printing the doc.
	 * @param rptName The Report Name to print.  Path should not be included.
	 * @param rptParams The parameters to the report.
	 * @return The full Path and File name of the exported report.  Returns null is export failed.
	 * @throws Exception
	 */
	public static String exportReportToPDF(String rptName, String[] rptParams)  {
		String ret = null;
		SessionMetaData sessionMeta = SessionMetaData.getInstance();
		String RPT_ENGINE_EXE = PropertyFileData.getInstance().getProperty(daiProperties.RPT_ENGINE_EXE);
		String RPT_DB_DSN = PropertyFileData.getInstance().getProperty(daiProperties.RPT_DB_DSN);
		String RPT_DB_PATH = PropertyFileData.getInstance().getProperty(daiProperties.RPT_DB_PATH);
		String RPT_TEMP_DIR = PropertyFileData.getInstance().getProperty(daiProperties.RPT_TEMP_DIR);
		String rptPathAndName = PropertyFileData.getInstance().getProperty(daiProperties.RPT_DOCS_DIR) + "\\"+  rptName;
		String dbConn = "/L1 " + RPT_DB_DSN+ "," + RPT_DB_PATH + ","+sessionMeta.getServerLogin()+","+sessionMeta.getServerPasswd();
		String rptParms = getRptParamsString(rptParams);
		String exportParams = "/X 31 /D 1 /FN ";
		String exportFileName = RPT_TEMP_DIR + "\\" + Calendar.getInstance().getTimeInMillis() + ".pdf";
		String space = " ";
		String cmd = RPT_ENGINE_EXE + space + rptPathAndName + space + dbConn + space + rptParms + space + exportParams + exportFileName ;
		System.out.println(cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			boolean exportFound = sleepWaitForRpt(exportFileName);
			if (exportFound) {
				ret = exportFileName;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return ret;
	}

	private static String getRptParamsString(String[] params) {
		String ret = "";
		for (int i=0; i<params.length; i++) {
			ret += " /P" + i + " " + params[i];
		}
		return ret;
	}
	private static boolean sleepWaitForRpt(String rptName) {
		boolean ret = false;
		for (int i=0; i<20; i++) {
	        java.io.File f = new java.io.File( rptName );
	        if (f.exists()) {
	        	ret = true;
	    		System.out.println("File Found!");
	        	break;
	        } else {
	        	try {
	        		Thread.sleep(1000);
	        	} catch (Exception e) {
	        		e.printStackTrace();
	        		break;
	        	}
	        }
		}
		return ret;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//10572
			exportReportToPDF("orderAck.rpt", new String[]{"SUPER", args[0]});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
