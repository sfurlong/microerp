package dai.client.clientAppRoot;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.altaprise.plaf.AltapriseLookAndFeel;

import dai.shared.businessObjs.global_settingsObj;
import dai.shared.cmnSvcs.DetailInfoDialog;
import dai.shared.cmnSvcs.SessionMetaData;
import dai.shared.cmnSvcs.csSecurity;
import dai.shared.csAdapters.csDBAdapter;
import dai.shared.csAdapters.csDBAdapterFactory;
import dai.shared.csAdapters.csSessionAdapter;
import dai.shared.csAdapters.csSessionAdapterFactory;

public class AppRoot {
    boolean packFrame = false;

    csSessionAdapterFactory sessionAdapterFactory = null;

    SessionMetaData sessionMeta = null;

    //Construct the application
    public AppRoot(String uid, String pwd) {
        boolean sucess = false;

        //Crank up the adapter factories.
        sessionAdapterFactory = csSessionAdapterFactory.getInstance();
        if (uid != null && pwd != null) {
            sucess = connect(uid, pwd);
        } else {
            sucess = userLogin();
        }

        if (sucess) {
            //Start the application
            daiExplorerFrame frame = new daiExplorerFrame();
        } else {
            System.exit(0);
        }
    }

    private boolean userLogin() {

        boolean ret = false;
        boolean tryAgain = true;

        JFrame tempFrame = new JFrame();
        PasswordDialog passwordDialog;

        while (tryAgain) {
            passwordDialog = new PasswordDialog(tempFrame, "microERP Login", true);
            if (passwordDialog.getUserAction().equals("OK")) {
                ret = connect(passwordDialog.getUserId(), passwordDialog
                        .getPassword());
                if (ret) {
                    tryAgain = false;
                }
            } else if (passwordDialog.getUserAction().equals("CANCEL")) {
                System.out.println("User pressed Cancel");
                tryAgain = false;
            }
        }

        return ret;
    }

    private boolean connect(String uid, String pwd) {

        boolean ret = false;

        csSessionAdapter sessionAdapter = null;
        sessionAdapter = sessionAdapterFactory.getSessionAdapter();
        sessionMeta = SessionMetaData.getInstance();
        try {
            csSecurity security = sessionAdapter.connect(sessionMeta
                    .getServerDBURL(), uid, pwd);
            ret = true;
            sessionMeta.setClientServerSecurity(security);
        } catch (Exception e) {
            DetailInfoDialog dialog = new DetailInfoDialog(null, "Error", true,
                    e.getLocalizedMessage());
        }

        return ret;
    }

    //Main method
    public static void main(String[] args) {
        String uid = null;
        String pwd = null;
        try {
            //UIManager.setLookAndFeel(new
            // com.sun.java.swing.plaf.windows.WindowsLookAndFeel());
            //UIManager.setLookAndFeel(new
            // com.sun.java.swing.plaf.motif.MotifLookAndFeel());
            //UIManager
            //		.setLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
            UIManager.setLookAndFeel(new AltapriseLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
        if (args.length == 2) {
            uid = args[0];
            pwd = args[1];
        }
        new AppRoot(uid, pwd);
    }
}