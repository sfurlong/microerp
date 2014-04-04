package com.altaprise.plaf;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class AltapriseScrollBarUI extends BasicScrollBarUI {

    /**
     * This method contructs a altaprise style scrollbar for the
     * component "c" that is requesting it.
     *
     * @param c The base component asking for the scrollbar.
     * @return The ComponentUI delegate to paint the scrollbar.
     */
    public static ComponentUI createUI (JComponent c) {
        return new AltapriseScrollBarUI();
    }

    /**
     * This method contructs a altaprise style button with an arrow
     * on it for use by the scrollbar.
     *
     * @param orientation   Specifics the direction of the arrow that is
     *                      rendered on the button (N, S, E, W).
     * @return The decrease JButton for the scrollbar.
     */
    protected JButton createDecreaseButton(int orientation) {
        return new AltapriseArrowButton (orientation);
    }

    /**
     * This method contructs a altaprise style button with an arrow
     * on it for use by the scrollbar.
     *
     * @param orientation   Specifics the direction of the arrow that is
     *                      rendered on the button (N, S, E, W).
     * @return The increase JButton for the scrollbar.
     */
    protected JButton createIncreaseButton(int orientation) {
        return new AltapriseArrowButton (orientation);
    }

    /**
     * This method installs the UI delegate into the PLaF to render the
     * scrollbar component.
     *
     */
    protected void installDefaults() {
        super.installDefaults();
    }

}

