package com.altaprise.clientupdate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public final class FTPUtil {
    private static String _userId = "daimgr";
    private static String _passwd = "daimgr1";

    public static void getRemoteFile(String hostName, String remoteFileName) throws Exception {
        FTPClient ftp = new FTPClient();

        //Try to connect to the remote host
        try {
            ftp.connect(hostName);
            System.out.println("Connected to " + hostName + ".");

            // After connection attempt, you should check the reply code to
            // verify success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
            	throw new Exception ("FTP server refused connection.");
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            throw new Exception ("Could not connect to server.\n" + e.getLocalizedMessage());
        }

        //Login and get the file.
        try {
            if (!ftp.login(_userId, _passwd)) {
                ftp.logout();
                System.err.println("Bad Login.");
            	throw new Exception ("Bad Login.");
            }

            System.out.println("Remote system is " + ftp.getSystemName());

            //We are doing a binary transfer.
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            ftp.enterLocalPassiveMode();

            OutputStream output = new FileOutputStream(remoteFileName);
            System.out.println("Getting Remote File: " + remoteFileName);
            ftp.retrieveFile(remoteFileName, output);

            ftp.logout();
        } catch (FTPConnectionClosedException e) {
            System.err.println("Server closed connection.");
         	throw new Exception ("Server closed connection.\n" + e.getLocalizedMessage());
        } catch (IOException e) {
            System.err.println("I/O Exception.");
         	throw new Exception ("I/O Exception.\n" + e.getLocalizedMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }
    }
    
    public static String checkForRemoteVersionFile(String hostName, String filePattern) throws Exception {
        FTPClient ftp = new FTPClient();
        String ret = null;
        
        try {
            ftp.connect(hostName);
            System.out.println("Connected to " + hostName + ".");

            // After connection attempt, you should check the reply code to
            // verify success.
            int reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
            	throw new Exception ("FTP server refused connection.");
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            throw new Exception ("Could not connect to server.\n" + e.getLocalizedMessage());
        }

        try {
            if (!ftp.login(_userId, _passwd)) {
                ftp.logout();
                System.err.println("Bad Login.");
            	throw new Exception ("Bad Login.");
            }

            System.out.println("Remote system is " + ftp.getSystemName());

            FTPFile[] files = ftp.listFiles(filePattern);

            if (files != null && files.length > 0) {
            	ret = files[0].getName();
            }

            ftp.logout();
        } catch (FTPConnectionClosedException e) {
            System.err.println("Server closed connection.");
         	throw new Exception ("Server closed connection.\n" + e.getLocalizedMessage());
        } catch (IOException e) {
            System.err.println("I/O Exception.");
         	throw new Exception ("I/O Exception.\n" + e.getLocalizedMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
        }
        return ret;
    }
}
