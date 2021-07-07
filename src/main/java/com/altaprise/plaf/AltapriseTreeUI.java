package com.altaprise.plaf;

import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.*;
import java.awt.*;

public class AltapriseTreeUI extends BasicTreeUI {


    /**
     * This method contructs a altaprise style tree for the
     * component "c" that is requesting it.
     *
     * @param c The base component asking for the tree.
     * @return The ComponentUI delegate to paint the tree.
     */
    public static ComponentUI createUI (JComponent c) {
        return new AltapriseTreeUI();
    }

    /**
     * This method installs the UI delegate into the PLaF to render the
     * scrollbar component.
     */
    protected void installDefaults() {
        super.installDefaults();
    }

    /**
     * This method overrides the tree controls horizontal line painting
     * so that the line is hashed by the Tree.hashStep in the Tree.hash
     * Color definition for the PLaF.
     *
     * @param g The graphical canvas on which to paint the tree line.
     * @param c The component that contains the tree control.
     * @param y The Y position of the line to be drawn.
     * @param left The far left side of the line to be drawn.
     * @param right The far right side of the line to be drawn.
     */
    protected void paintHorizontalLine(Graphics g,
                                       JComponent c,
                                       int y,
                                       int left,
                                       int right) {

        // Get the color to paint the line with.
        Color dashColor = UIManager.getColor("Tree.hash");

        // Get the number of white pixels that should be painted between the
        // hash colors.
        int hashStep = UIManager.getInt("Tree.hashStep");

        // Process the entire length of the line that needs to be drawn.
        for (int index=left; index < right; index++) {

            // If we are processing the very first pixel, make it the dash
            // color.
            if (index == left) {
                g.setColor(dashColor);

            // If the index is "mod" hashstep, then it is time to paint
            // another piece of the line in dash color.
            } else if (Math.IEEEremainder((double)index,(double)hashStep) == 0) {
                g.setColor(dashColor);

            // Otherwise, it is all tree background color.
            } else {
                g.setColor(c.getBackground());
            }

            // Draw a single pixel in the appropriate color.
            g.drawLine(index, y, index+1, y);
        }

    }

    /**
     * This method overrides the tree controls vertical line painting
     * so that the line is hashed by the Tree.hashStep in the Tree.hash
     * Color definition for the PLaF.
     *
     * @param g The graphical canvas on which to paint the tree line.
     * @param c The component that contains the tree control.
     * @param x The X position of the line to be drawn.
     * @param top The top coordinate of the line to be drawn.
     * @param bottom The bottom coordinate of the line to be drawn.
     */
    protected void paintVerticalLine(Graphics g,
                                     JComponent c,
                                     int x,
                                     int top,
                                     int bottom) {

        // Get the color to paint the line with.
        Color dashColor = UIManager.getColor("Tree.hash");

        // Get the number of white pixels that should be painted between the
        // hash colors.
        int hashStep = UIManager.getInt("Tree.hashStep");

        // Process the entire length of the line that needs to be drawn.
        for (int index=top; index < bottom; index++) {

            // If we are processing the very first pixel, make it the dash
            // color.
            if (index == top) {
                g.setColor(dashColor);

            // If the index is "mod" hashstep, then it is time to paint
            // another piece of the line in dash color.
            } else if (Math.IEEEremainder((double)index,(double)hashStep) == 0) {
                g.setColor(dashColor);

                // Otherwise, it is all tree background color.
            } else {
                g.setColor(c.getBackground());
            }

            // Draw a single pixel in the appropriate color.
            g.drawLine(x, index, x, index+1);
        }
    }
}
