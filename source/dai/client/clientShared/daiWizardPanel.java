
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:

package dai.client.clientShared;

import javax.swing.JPanel;

import dai.shared.cmnSvcs.Logger;

abstract public class daiWizardPanel extends JPanel
{

    protected Logger _logger;

    public daiWizardPanel()
    {
        try
        {
            jbInit();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception
    {
        //Set the standard background color for DAI Entry Panels
        this.setBackground(daiColors.PanelColor);

        _logger = Logger.getInstance();
    }

    abstract public boolean panelDataIsValid();
    abstract public boolean doPreDisplayProcessing(Object[] data);
    //abstract public void    resetPanel();
}

