package com.altaprise.plaf;

import javax.swing.plaf.ComponentUI;
import javax.swing.*;
import java.awt.*;

public class AltapriseToggleButtonUI extends AltapriseButtonUI {

    private final static AltapriseToggleButtonUI altapriseButtonUI = new AltapriseToggleButtonUI();

    /**
     * This method contructs the UI delgate for rendering.
     * @param c The component to be drawn.
     * @return The component UI delegate.
     */
    public static ComponentUI createUI(JComponent c) {
        return altapriseButtonUI;
    }

    /**
     * This method actually paints the button within the supplied
     * graphics area (defined by g), using the information and data from
     * the AbstractButton (b) parameter.
     *
     * @param g The graphics are in which to draw the UI component.
     * @param b The abstract button class with the associated model.
     */
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        super.paintButtonPressed(g,b);
        javax.swing.border.Border border=BorderFactory.createLoweredBevelBorder();
        border.paintBorder(b,g,1,1,b.getWidth()-2,b.getHeight()-2);
    }

}
