package com.altaprise.clientupdate;

import java.awt.Cursor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dai.shared.cmnSvcs.SessionMetaData;

public class ClientUpdate {

	String _updateFileName = "microERP_patch-";
	String _patchIdPattern = "microERP_patchId-*";
	String _ftpHostName = "altaprise.com";
	String _patchNum = null;
	
	/**
	 * 
	 * @param parentFrame can be null.
	 */
    public ClientUpdate(JFrame parentFrame) {
    	boolean newerVersionExists = false;

		//Check for newer version
    	try {
			newerVersionExists = checkForNewerVersion(_ftpHostName, _patchIdPattern);
			if (!newerVersionExists) {
				JOptionPane.showMessageDialog(parentFrame, "No new updates found.", "MicroERP", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		} catch (Exception e){
        	JOptionPane.showMessageDialog(parentFrame, "Unable to check for newer version.\n"+e.getLocalizedMessage(), "MicroERP", JOptionPane.ERROR_MESSAGE);
		}
		
		int retVal = JOptionPane.showOptionDialog(
				parentFrame,
            "A new version of microERP exists.  Would you like to update now?" +
            "\nNote1:  Ensure all work is saved first." +
            "\nNote2:  Retrieving the update from the Internet may take several minutes.",
            "microERP", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE, null, null, null);
		if (retVal == JOptionPane.NO_OPTION) {
			return;
		}
		
		//Get the Remote update File!
		try {
            parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        	//Get the remote file
			String remoteFileName =  _updateFileName + _patchNum + ".zip";
        	FTPUtil.getRemoteFile(_ftpHostName, remoteFileName);
            parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

            JOptionPane.showMessageDialog(
					parentFrame,
	            "The Update has been Retrieved.  Press OK to continue with the Update.",
	            "microERP", JOptionPane.INFORMATION_MESSAGE);

            //Now unzip it.
    		String targetDir = SessionMetaData.getInstance().getDaiHome() + "/";
    	    unzipAndSave(remoteFileName, targetDir);
		} catch (Exception e) {
        	JOptionPane.showMessageDialog(parentFrame, "Unable to get the remote update file.\n"+e.getLocalizedMessage(), "MicroERP", JOptionPane.ERROR_MESSAGE);
        	return;
		}

		//The System Has Been Updated!
		//Tell the user what happened.
	    JOptionPane.showMessageDialog(parentFrame, "microERP has been updated.  Please restart your software.", "MicroERP", JOptionPane.INFORMATION_MESSAGE);
	    System.exit(0);
    }

    private boolean checkForNewerVersion(String hostName, String versionFilePattern) throws Exception {

        boolean doUpdate = false;
        SessionMetaData sessionMeta = SessionMetaData.getInstance();
        try {
        	String updateVersFileName = FTPUtil.checkForRemoteVersionFile(hostName, versionFilePattern);
        	String currentVersFileName = sessionMeta.getPatchNum();
        	System.out.println("UpdateVersFileName: " + updateVersFileName);
        	System.out.println("CurrentVersFileName: " + currentVersFileName);
        	if (updateVersFileName != null) {
        		int updateVersPos = updateVersFileName.indexOf("-");
        		int currentVersPos = currentVersFileName.indexOf("-");
        		String updateVers = updateVersFileName.substring(updateVersPos+1, updateVersFileName.length());
        		String currentVers = currentVersFileName.substring(currentVersPos+1, currentVersFileName.length());
        		float f_updateVers = Float.parseFloat(updateVers);
        		float f_currentVers = Float.parseFloat(currentVers);
            	System.out.println("UpdateVers: " + updateVers);
            	System.out.println("CurrentVers: " + currentVers);
        		if (f_updateVers > f_currentVers) {
        			doUpdate = true;
        			_patchNum = updateVers;
        		}
        	}
        } catch (Exception e) {
            //do error checking.
        	throw e;
        }
        return doUpdate;
    }

    private void unzipAndSave(String fileName, String targetDir) throws Exception {

    	String currentPatchNum = SessionMetaData.getInstance().getPatchNum();
        int BUFFER = 2048;
        int numXMLDocs = 0;
        BufferedOutputStream dest = null;
        BufferedInputStream is = null;
        try {
            ZipFile zipfile = new ZipFile(fileName);
            System.out.println(zipfile.getName());
            Enumeration entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                if (!ze.isDirectory()) {
                    System.out.println(ze.getName());
                    is = new BufferedInputStream(zipfile.getInputStream(ze));
                    int count;
                    byte data[] = new byte[BUFFER];
                    FileOutputStream fos = null;
                    //If this system has never been patched, use the old zip dir structure.
                    if (currentPatchNum.equals("201.01")) {
                    	System.out.println(targetDir+"../" + ze.getName());
                    	fos = new FileOutputStream(targetDir+"../" + ze.getName());
                    } else {
                    	System.out.println(targetDir+"/" + ze.getName());
                    	fos = new FileOutputStream(targetDir+"/" + ze.getName());
                    }
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                    numXMLDocs++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
        }
    }

    public static void main(String[] args) {
        new ClientUpdate(null);
    }

}