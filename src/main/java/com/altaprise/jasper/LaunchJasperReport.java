package com.altaprise.jasper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import dai.shared.cmnSvcs.SessionMetaData;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 * @author Stephen Furlong
 * @version $Id$
 */
public class LaunchJasperReport {

	/**
	 * 
	 */
	private static final String TASK_COMPILE = "compile";

	private static final String TASK_FILL = "fill";

	private static final String TASK_PRINT = "print";

	private static final String TASK_PDF = "pdf";

	private static final String TASK_RTF = "rtf";

	private static final String TASK_XML = "xml";

	private static final String TASK_XML_EMBED = "xmlEmbed";

	private static final String TASK_HTML = "html";

	private static final String TASK_XLS = "xls";

	private static final String TASK_JXL = "jxl";

	private static final String TASK_CSV = "csv";

	private static final String TASK_RUN = "run";

	private static final String TASK_FUR = "fur";

	private static final String TASK_FILL_IGNORE_PAGINATION = "fillIgnorePagination";

	public LaunchJasperReport(String taskName, String fileName) {
		this.printReport(TASK_FUR, fileName, new String[]{"120-1-2005"});
	}

	/**
	 * 
	 */
	public static void main(String[] args) {
		String fileName = "microerp-invoice";
		String taskName = null;
		
		new LaunchJasperReport(taskName, fileName);

	}

	private void printReport(String taskName, String fileName, String[] parmVals) {
		try {
			long start = System.currentTimeMillis();
			if (TASK_COMPILE.equals(taskName)) {
				JasperCompileManager.compileReportToFile(fileName);
				System.err.println("Compile time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			}
			if (TASK_FUR.equals(taskName)) {
				String PATH = SessionMetaData.getInstance().getDaiRptHome()+"/jasper/";
				JasperCompileManager.compileReportToFile(PATH + fileName
						+ ".jrxml");
				System.err.println("Compile time : "
						+ (System.currentTimeMillis() - start));

				// Preparing parameters
				Map parameters = new HashMap();
				parameters.put("shipmnet_id", parmVals[0]);

				if (TASK_FILL_IGNORE_PAGINATION.equals(taskName)) {
					parameters.put(JRParameter.IS_IGNORE_PAGINATION,
							Boolean.TRUE);
				}
				JasperFillManager.fillReportToFile(PATH + fileName + ".jasper",
						parameters, getConnection());
				System.err.println("Filling time : "
						+ (System.currentTimeMillis() - start));

				net.sf.jasperreports.view.JasperViewer.viewReport(PATH
						+ fileName + ".jrprint", false);

			} else if (TASK_FILL.equals(taskName)
					|| TASK_FILL_IGNORE_PAGINATION.equals(taskName)) {
				// Preparing parameters
				Map parameters = new HashMap();
				parameters.put("ReportTitle", "Address Report");
				parameters.put("FilterClause", "'Boston', 'Chicago', 'Oslo'");
				parameters.put("OrderClause", "City");

				if (TASK_FILL_IGNORE_PAGINATION.equals(taskName)) {
					parameters.put(JRParameter.IS_IGNORE_PAGINATION,
							Boolean.TRUE);
				}

				JasperFillManager.fillReportToFile(fileName, parameters,
						getConnection());
				System.err.println("Filling time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_PRINT.equals(taskName)) {
				JasperPrintManager.printReport(fileName, true);
				System.err.println("Printing time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_PDF.equals(taskName)) {
				JasperExportManager.exportReportToPdfFile(fileName);
				System.err.println("PDF creation time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_RTF.equals(taskName)) {
				File sourceFile = new File(fileName);

				JasperPrint jasperPrint = (JasperPrint) JRLoader
						.loadObject(sourceFile);

				File destFile = new File(sourceFile.getParent(), jasperPrint
						.getName()
						+ ".rtf");

				JRRtfExporter exporter = new JRRtfExporter();

				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
						destFile.toString());

				exporter.exportReport();

				System.err.println("RTF creation time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_XML.equals(taskName)) {
				JasperExportManager.exportReportToXmlFile(fileName, false);
				System.err.println("XML creation time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_XML_EMBED.equals(taskName)) {
				JasperExportManager.exportReportToXmlFile(fileName, true);
				System.err.println("XML creation time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_HTML.equals(taskName)) {
				JasperExportManager.exportReportToHtmlFile(fileName);
				System.err.println("HTML creation time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_XLS.equals(taskName)) {
				File sourceFile = new File(fileName);

				JasperPrint jasperPrint = (JasperPrint) JRLoader
						.loadObject(sourceFile);

				File destFile = new File(sourceFile.getParent(), jasperPrint
						.getName()
						+ ".xls");

				JRXlsExporter exporter = new JRXlsExporter();

				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
						destFile.toString());
				exporter.setParameter(
						JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
						Boolean.FALSE);

				exporter.exportReport();

				System.err.println("XLS creation time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_JXL.equals(taskName)) {
				File sourceFile = new File(fileName);

				JasperPrint jasperPrint = (JasperPrint) JRLoader
						.loadObject(sourceFile);

				File destFile = new File(sourceFile.getParent(), jasperPrint
						.getName()
						+ ".jxl.xls");

				JExcelApiExporter exporter = new JExcelApiExporter();

				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
						destFile.toString());
				exporter.setParameter(
						JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
						Boolean.TRUE);

				exporter.exportReport();

				System.err.println("XLS creation time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_CSV.equals(taskName)) {
				File sourceFile = new File(fileName);

				JasperPrint jasperPrint = (JasperPrint) JRLoader
						.loadObject(sourceFile);

				File destFile = new File(sourceFile.getParent(), jasperPrint
						.getName()
						+ ".csv");

				JRCsvExporter exporter = new JRCsvExporter();

				exporter.setParameter(JRExporterParameter.JASPER_PRINT,
						jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
						destFile.toString());

				exporter.exportReport();

				System.err.println("CSV creation time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else if (TASK_RUN.equals(taskName)) {
				// Preparing parameters
				Map parameters = new HashMap();
				parameters.put("ReportTitle", "Address Report");
				parameters.put("FilterClause", "'Boston', 'Chicago'");
				parameters.put("OrderClause", "City");

				JasperRunManager.runReportToPdfFile(fileName, parameters,
						getConnection());
				System.err.println("PDF running time : "
						+ (System.currentTimeMillis() - start));
				System.exit(0);
			} else {
				usage();
				System.exit(0);
			}
		} catch (JRException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 
	 */
	private static void usage() {
		System.out.println("QueryApp usage:");
		System.out.println("\tjava QueryApp -Ttask -Ffile");
		System.out
				.println("\tTasks : compile | fill | fillIgnorePagination | print | pdf | xml | xmlEmbed | html | rtf | xls | jxl | csv | run");
	}

	/**
	 * 
	 */
	private static Connection getConnection() throws ClassNotFoundException,
			SQLException {
		// Change these settings according to your local configuration
		String driver = "org.hsqldb.jdbcDriver";
		String connectString = "jdbc:hsqldb:hsql://localhost";
		String user = "SYSDBA";
		String password = "daimgr";

		Class.forName(driver);
		Connection conn = DriverManager.getConnection(connectString, user,
				password);
		return conn;
	}

}
