package com.altaprise.plaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class AltapriseComboBoxUI extends BasicComboBoxUI {

    // A simple border definition.  A single boxed line in dark gray that is
    // one pixel wide.
    private static final Border SIMPLE_BORDER =
            BorderFactory.createLineBorder(AltapriseUtilities.DARK_GRAY_COLOR, 1);

    /**
     * This method contructs a Altaprise style combobox for the
     * component "c" that is requesting it.
     *
     * @param c The base component asking for the combobox.
     * @return The ComponentUI delegate to paint the combobox.
     */
    public static ComponentUI createUI (JComponent c) {
        return new AltapriseComboBoxUI();
    }

    /**
     * This method contructs a Altaprise style arrow button
     * for use by the combo box and returns it as a JButton.
     *
     * @return  The JButton drawn for the Altaprise look and feel for use
     *          by the combobox.
     */
    protected JButton createArrowButton() {

        // Construct a AltapriseArrow that points south (down for those that
        // are compass impaired) and returns it for rendering.
        return new AltapriseArrowButton (AltapriseArrowButton.SOUTH);
    }

   /**
     * This method installs the UI delegate into the PLaF to render the
     * combobox component.
     *
     */
    protected void installDefaults() {
        super.installDefaults();
    }

    /**
     * This method performs the basic look and feel painting algorithms, then
     * adds the SIMPLE_BORDER decoration.
     *
     * @param g The graphics area in which to draw the combobox.
     * @param c The component in which the combobox is being rendered.
     */
    public void paint(Graphics g, JComponent c) {

        // Peform the basic paint operation.
        super.paint(g, c);

        // Add our border decoration to the combobox.
        c.setBorder(SIMPLE_BORDER);
    }
}
