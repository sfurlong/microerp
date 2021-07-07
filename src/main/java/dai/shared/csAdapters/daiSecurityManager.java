
//Title:        Shared Files
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Client Server shared files
package dai.shared.csAdapters;

//This gets rid of the non-secure applet warning (Yellow band
//at the bottom of all windows/dialogs) when using RMI.
//System.setSecurityManager(new daiSecurityManager());
public class daiSecurityManager extends SecurityManager {
    public boolean checkTopLevelWindow(Object window) {
        //super.checkTopLevelWindow(window);  //Deprecated
        return true;
    }
}

