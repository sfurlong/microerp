package dai.client.clientShared;

import java.io.IOException;

public class BrowserLauncher {
   private static boolean windows;

   static {
      String os = System.getProperty("os.name");
      windows = os != null && os.startsWith("Windows");
   }

   /**
    * Opens URL in a browser window
    * @param url
    * @return
    * @throws IOException
    * @throws InterruptedException
    */
   public static Process openURL(String url) throws IOException, InterruptedException {
      Process res = null;
      if (windows) {
         res = openWinFile(url);
      } else {
         //TODO: for unix the following code always trying to get netscape
         // Under Unix, Netscape has to be running for the "-remote"
         // command to work.  So, we try sending the command and
         // check for an exit value.  If the exit command is 0,
         // it worked, otherwise we need to start the browser.
         // cmd = 'netscape -remote openURL(http://www.javaworld.com)'
         String cmd = "netscape -remote (" + url + ")";
         res = Runtime.getRuntime().exec(cmd);
         int exitCode = res.waitFor();
         if (exitCode != 0) {
            // Command failed, start up the browser
            cmd = "netscape " + url;
            res = Runtime.getRuntime().exec(cmd);
         }
      }
      return res;
   }

   /**
    * In windows, opens a file provided with associated app.
    * In others OS does nothing and returns null.
    * @param filePath
    * @return
    * @throws IOException
    */
   public static Process openWinFile(String filePath) throws IOException {
      Process res = null;
      if (windows) {
         res = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filePath);
      }
      return res;
   }
}

