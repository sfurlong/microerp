/*
 File:		CRSample.java
 Rights:		Copyright 2006, Altaprise, Inc.
 All Rights Reserved world-wide.
 Purpose:
 Demonstrate the Crystal Reports engine Java wrapper.
 Notes:
 For all functions except printPreview, the required order of clicking
 1.	click loadEngine button
 2.	click openReport button
 3.	press other buttons to test certain functionality
 4.	click closeReport
 5.	click unloadEngine

 Known defects:
 - external moveable modal dialogs that operate on the AWT dispatch
 thread will leave artifacts - cosmetic
 */
package com.altaprise.crystal;

import visi.crystal.CRException;
import visi.crystal.CRExportOptions;
import visi.crystal.CRMAPI;
import visi.crystal.CRParameterField;
import visi.crystal.CRinf;
import visi.crystal.DevMode;
import dai.shared.cmnSvcs.daiException;

public class CRJNI {
	protected short reportId;

	protected Object[] _printer;

	// change the following to match your DB
	protected static final String DB_DLL = "p2sodbc.dll"; // oracle 7

	protected static final String DB_HOST = "artifacts";

	protected static final String DB_USER = "SYSDBA";

	protected static final String DB_PASSWORD = "daimgr";

	// init
	{
		reportId = -1;
	}

	public static void main(String[] args) {
		CRJNI du = new CRJNI();
	}

	public CRJNI() {
	}

	/**
	 * doLoadEngine - load CR engine and default log on
	 * <P>
	 * 
	 * Log on the database after opening the CR engine
	 * <P>
	 * 
	 * Req #1, #2
	 */
	public void doLoadEngine() throws daiException {
		CRinf cr = CRinf.getInstance();
		if (cr != null) {
			System.out.println("CRinf.DLL loaded\n");
			try {
				// open engine
				cr.open();
				System.out.println("open ok\n");

				// log on to table req #2
				cr.logOnServer(DB_DLL, DB_HOST, // jdbc:oracle:thin:@mapapp1:1521:ORCL",
												// //host.getText(),
						"", DB_USER, // userName.getText(),
						DB_PASSWORD); // passWord.getPassword()
				System.out.println("logOnServer ok\n");

				// get default printer
				// printer = cr.pageSetup( false, null );
				// System.out.println( "pageSetup ok\n" );
			} catch (CRException cre) {
				String msg = this.getClass().getName() + "::doLoadEngine\n"
						+ cre.getClass().getName() + "\n"
						+ cre.getLocalizedMessage();
				System.out.println(msg);
				throw new daiException(msg, this);
			}
		} else {
			System.out.println("CRinf failed to load, did you\n");
			System.out
					.println(" java -Djava.library.path=\\path\\to\\dll CRSample?\n");
		}

	}

	/**
	 * doOpenReport - open the sample report
	 * <P>
	 * 
	 */
	public void doOpenReport(String rptName) throws daiException {
		CRinf cr = CRinf.getInstance();
		try {
			System.out.println("doOpenReport::report Name: " + rptName);
			reportId = cr.openReport(rptName);
			System.out.println("doOpenReport::report id: " + reportId);
		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doOpenReport\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}
	}

	/**
	 * doSetParameter - set a parameter
	 * <P>
	 * 
	 * Specifically, change the TO_NAME parameter on the form letter. Sometimes
	 * it is preferrable to use setCurrentValue.
	 * <P>
	 * 
	 * Req #4
	 */
	public void doSetParameter(int parmNum, String parmVal) throws daiException {
		CRinf cr = CRinf.getInstance();
		try {
			CRParameterField crpf = cr.getNthParameterField(reportId,
					(short) parmNum);
			System.out.println("Old: " + crpf.toString() + '\n');
			crpf.DefaultValueSet = 0;
			crpf.CurrentValueSet = 1;
			crpf.needsCurrentValue = 0;
			crpf.CurrentValue = parmVal;
			cr.setNthParameterField(reportId, (short) parmNum, crpf);
			System.out.println("setNthParameterField ok\n");
		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doSetParameter\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}
	}

	/**
	 * doSetPrinter - override the reports printer - sets to default
	 * <P>
	 * 
	 * Req #5
	 */
	public void doSetPrinter(String driverName, String prtrName, String prtrPort)
			throws daiException {
		CRinf cr = CRinf.getInstance();
		try {
			cr.selectPrinter(reportId, driverName, prtrName, prtrPort, (DevMode)_printer[3]);
			_printer = cr.pageSetup(false, (DevMode)_printer[3]);
			System.out.println("selectPrinter ok");
		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doSetPrinter\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}
	}

	/**
	 * doPageSetup - change the printer and some DevMode stuff
	 * <P>
	 * Basically, pop up a Windows printer dialog to select the printer, that
	 * extracts everything I need to set the printer.
	 * <P>
	 * @return Object[4] with the following fields
	 * Object[0] - of type String representing the Driver Name
	 * Object[1] - of type String representing the Printer Name
	 * Object[2] = of type String representing the Printer Port
	 * Object[3] - not used.
	 */
	public Object[] doPageSetup() {
		//Should try to do this with treads.  Giving up for now...
		//We want the caller thread to block until this one is done.
		//		Thread myThread = new Thread(new Runnable() {
		//	public void run() {
				CRinf cr = CRinf.getInstance();
				Object[] currentPrinter = _printer;
				DevMode currentDevMode = null;
				if (_printer != null) {
					System.out.println("Current papersource: " + currentDevMode.defaultSource);
					System.out.println("Current drv,prn,prt: " + currentPrinter[0]
                  							+ ',' + currentPrinter[1] + ',' + currentPrinter[2] + '\n');
					currentDevMode = (DevMode) currentPrinter[3];
				}
				Object[] newPrinter = cr.pageSetup(true, currentDevMode);

				if (newPrinter != null) {
					_printer = newPrinter;
					System.out.println("new papersource: " + ((DevMode)_printer[3]).defaultSource);

					System.out.println("New drv,prn,prt: " + _printer[0]
							+ ',' + newPrinter[1] + ',' + _printer[2] + '\n');
					System.out.println("fields" + ((DevMode)_printer[3]).fields);
				} else {
					System.out.println("pageSetup cancelled\n");
				}
//			}
//		});
//		myThread.start();
		// should wait...
		return _printer;
	}

	/**
	 * doPageSetup - change the printer and some DevMode stuff
	 * <P>
	 * 
	 * No window will be popued up
	 * <P>.
	 */
	public void doPageSetup(boolean showDialog, short paperTray)
			throws daiException {
		CRinf cr = CRinf.getInstance();
		Object[] oldPrinter = _printer;
		Object[] newPrinter = cr.pageSetup(false, (DevMode) oldPrinter[3]);
		try {
			if (newPrinter != null) {
				_printer = newPrinter;
				DevMode dv = (DevMode) newPrinter[3];

				System.out.println("New PaperSource: " + dv.defaultSource);

				// Change the paper tray.
				dv.fields = 0x0200; // don't forget, you can change tray and
									// imagable area...
				dv.defaultSource = paperTray;

				cr.selectPrinter(reportId, (String) newPrinter[0],
						(String) newPrinter[1], (String) newPrinter[2],
						(DevMode) newPrinter[3]);
			} else {
				System.out.println("pageSetup cancelled\n");
			}

		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doPageSetup\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}
	}

	/**
	 * doSetPaperTray - setPaperTray
	 * <P>
	 * 
	 * This should wrap dmPaperSize known constants into a ComboBox...
	 * <P>
	 * 
	 * Req #6
	 */
	public void doSetPaperTray(short trayNum) {
		CRinf cr = CRinf.getInstance();
		DevMode devMode = (DevMode) _printer[3];
		System.out.println("old paperSource = " + devMode.defaultSource
				+ '\n');
		System.out.println("devmode.fields" + devMode.fields);
		devMode.fields = 0x0200; // don't forget, you can change tray and
									// imagable area...
		System.out.println("devmode.fields" + devMode.fields);
		devMode.defaultSource = trayNum;
		try {
			cr.selectPrinter(reportId, (String) _printer[0],
					(String) _printer[1], (String) _printer[2],
					(DevMode) devMode);
			System.out.println("new paperSource = " + trayNum + " ok\n");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * doGetSelectedPrinter -
	 * <P>
	 * 
	 */
	public Object[] doGetSelectedPrinter() throws daiException {
		CRinf cr = CRinf.getInstance();
		Object[] ret = null;

		try {
			ret = cr.getSelectedPrinter(reportId);
			if (ret != null) {
				_printer = ret;
				_printer[0] = (String) ret[0];
				_printer[1] = (String) ret[1];
				_printer[2] = (String) ret[2];
				_printer[3] = (DevMode) ret[3];
			} else {
				String msg = this.getClass().getName()
						+ "::doGetSelectedPrinter\n"
						+ "Report template does not have a default printer specified";
				System.out.println(msg);
				throw new daiException(msg, this);
			}

		} catch (Exception e) {
			String msg = this.getClass().getName() + "::doGetSelectedPrinter\n"
					+ e.getClass().getName() + "\n" + e.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}

		return ret;
	}

	/**
	 * doPrint - print the report
	 * <P>
	 * 
	 * Req #7
	 */
	public void doPrint(short nCopies) throws daiException {
		CRinf cr = CRinf.getInstance();
		try {
			if (nCopies == 0)
				nCopies = 1;
			System.out.println("rptId: " + reportId + " copies: " + nCopies);
			cr.outputToPrinter(reportId, nCopies);
			System.out.println("outputToPrinter " + nCopies + " copies ok");

			cr.closeReport(reportId);
			System.out.println("closeReport ok");
			if (cr.canClose()) {
				cr.close();
				System.out.println("close ok");
			} else {
				System.out.println("close failed - busy");
			}

		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doPrint\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			try {
				cr.closeReport(reportId);
				System.out.println("closeReport ok");
			} catch (Exception e) {
				throw new daiException(e.getLocalizedMessage(), this);
			}
			if (cr.canClose()) {
				cr.close();
				System.out.println("close ok");
			} else {
				System.out.println("close failed - busy");
			}
			throw new daiException(msg, this);
		}
	}

	/**
	 * doPreview - preview the report
	 * <P>
	 * 
	 * This call illustrates how to use the crystal report engine such that the
	 * preview does not block the AWT Event Dispatch thread.
	 * <P>
	 * 
	 * This call does not depend on the CRSample instance - as it is in its own
	 * thread it must open its own copy of the engine on the thread. Do not mix
	 * engine calls accross threads.
	 * <P>
	 * 
	 * Req #8
	 */
	public void doPreview(final String rptName, final String[] rptParms) {
		Thread myThread = new Thread(new Runnable() {

			public void run() {
				CRinf cr = CRinf.getInstance();
				try {
					cr.open();
					System.out.println("open ok\n");

					short reportId = reportId = cr.openReport(rptName);

					cr.setNthTableLogOnInfo(reportId, DB_HOST, "", // host.getText(),
																	// "",
							DB_USER, // userName.getText(),
							DB_PASSWORD); // password.getPassword()

					for (int i = 0; i < rptParms.length; i++) {
						System.out.println(rptParms[i]);
						doSetParameter(reportId, i, rptParms[i]);
					}

					System.out.println("calling outputToWindow...\n");
					// this call is blocking
					cr.outputToWindow(reportId, "Title", 1, 1, 600, 400);
					System.out.println("outputToWindow ok\n");
					cr.closeReport(reportId);
					System.out.println("closeReport ok\n");
					if (cr.canClose()) {
						cr.close();
						System.out.println("close ok\n");
					} else
						System.out.println("close failed - busy\n");
				} catch (Throwable cre) {
					String msg = this.getClass().getName() + "::doPreview\n"
							+ cre.getClass().getName() + "\n"
							+ cre.getLocalizedMessage();
					System.out.println(msg);
					// throw new daiException(msg, this);
				}
			}

			public void doSetParameter(short rptId, int parmNum, String parmVal)
					throws daiException {
				CRinf cr = CRinf.getInstance();
				try {
					CRParameterField crpf = cr.getNthParameterField(rptId,
							(short) parmNum);
					System.out.println("Old: " + crpf.toString() + '\n');
					crpf.DefaultValueSet = 0;
					crpf.CurrentValueSet = 1;
					crpf.needsCurrentValue = 0;
					crpf.CurrentValue = parmVal;
					cr.setNthParameterField(rptId, (short) parmNum, crpf);
					System.out.println("setNthParameterField ok\n");
				} catch (CRException cre) {
					String msg = this.getClass().getName()
							+ "::doSetParamter\n" + cre.getClass().getName()
							+ "\n" + cre.getLocalizedMessage();
					System.out.println(msg);
					throw new daiException(msg, this);
				}
			}
		});

		myThread.start();
		// should watch if thread lives
	}

	/**
	 * doExportToMAPI - export a report in RTF format via e-mail
	 * <P>
	 * 
	 * Req #10
	 */
	public void doExportToMAPI() throws daiException {

		CRinf cr = CRinf.getInstance();
		try {
			CRMAPI mapi = new CRMAPI();
			mapi.toList = "stevefur@erols.com;sfurlong@digitalartifacts.com";
			mapi.subject = "The Report";
			mapi.message = "Included is the report I promised.";

			CRExportOptions options = new CRExportOptions();
			options.formatDLLName = "u2frtf.dll";
			options.formatType = options.UXFRichTextFormatType; // only format
																// type
																// currently
																// supported
			options.formatOptions = null;
			options.destinationDLLName = "u2dmapi.dll";
			options.destinationType = options.UXDMAPIType; // only destination
															// type currently
															// supported
			options.destinationOptions = mapi;

			cr.exportTo(reportId, options);
			System.out.println("RTF exportTo MAPI ok\n");
		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doExportToMapi\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}
	}

	/**
	 * doExportToFile - print report to file
	 * <P>
	 * 
	 * Req #9
	 * 
	 */
	public void doExportToFile() throws daiException {
		CRinf cr = CRinf.getInstance();
		try {
			cr.outputToFile(reportId, "turfMe.htm", cr.HTML);
			System.out.println("outputToFile ok\n");
		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doExportToFile\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}
	}

	/**
	 * doCloseReport - close the report
	 * <P>
	 */
	public void doCloseReport() throws daiException {
		CRinf cr = CRinf.getInstance();
		try {
			cr.closeReport(reportId);
			System.out.println("closeReport ok\n");
			reportId = -1;
		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doCloseReport\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}
	}

	/**
	 * doUnloadEngine - close the engine
	 * <P>
	 */
	public void doUnloadEngine() {
		CRinf cr = CRinf.getInstance();
		if (cr.canClose()) {
			cr.close();
			System.out.println("close ok\n");
		} else
			System.out.println("close failed - busy\n");
	}

	/**
	 * doDiscardData - discard report saved data, forcing requery and prompting.
	 */
	public void doDiscardData() throws daiException {
		CRinf cr = CRinf.getInstance();
		try {
			if (cr.hasSavedData(reportId)) {
				cr.discardSavedData(reportId);
				System.out.println("discardSavedData ok\n");
			} else
				System.out.println("no saved data\n");
		} catch (CRException cre) {
			String msg = this.getClass().getName() + "::doDiscardData\n"
					+ cre.getClass().getName() + "\n"
					+ cre.getLocalizedMessage();
			System.out.println(msg);
			throw new daiException(msg, this);
		}
	}
}