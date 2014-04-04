package com.altaprise.plaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;

public class AltapriseArrowButton extends BasicArrowButton {

    // The shadow color of the base button.
    private static Color shadowColor;

    // The highlight color of the base button.
    private static Color highlightColor;

    // The darker (3D) color of the base button.  _|
    private static Color darkShadowColor;

    public AltapriseArrowButton(int direction) {

        // Call supers contructor.
        super(direction);

        darkShadowColor = UIManager.getColor("controlDkShadow");
        shadowColor = UIManager.getColor("controlShadow");
        highlightColor = UIManager.getColor("controlHighlight");

        this.setMargin(AltapriseUtilities.EMPTY_INSET);
    }


    /**
     * This method actually paints (renders) the button along with the
     * directional arrow using the Altaprise look and feel color scheme.
     */
	public void paint(Graphics g) {

        // Saves off the current color so that it can be reset at the end
        // of this operation.
	    Color origColor;

        // Boolean that tracks if the button is pressed.
	    boolean isPressed;

        // Button dimensional information.
        int w, h, size;

        // Get the current size of the component.
        w = getSize().width;
        h = getSize().height;

        // Save off the current color.
	    origColor = g.getColor();

        // Determine if the button is currently in a down state.
	    isPressed = getModel().isPressed();

        // Set the color to the background of the container and fill in the
        // button area.
        g.setColor(getBackground());
        g.fillRect(1, 1, w-2, h-2);

        /// Draw the proper Border based on the pressed state.  If it is
        // pressed, then the button will be beveled in, otherwise it will
        // be protruding out.
        if (isPressed) {

            g.setColor(darkShadowColor);     // top left corner
            g.drawLine(0, 0, 0, h-1);
            g.drawLine(1, 0, w-2, 0);

            g.setColor(shadowColor);       // inner 3D border shadow
            g.drawLine(1, 1, 1, h-3);
            g.drawLine(2, 1, w-3, 1);

            g.setColor(highlightColor);    // bottom right corner
            g.drawLine(1, h-2, w-2, h-2);
            g.drawLine(w-2, 1, w-2, h-3);

            g.setColor(getBackground());
            g.drawLine(0, h-1, w-1, h-1);
            g.drawLine(w-1, h-1, w-1, 0);

        } else {  // Render the button as raised and un-pressed.

            // Using the background color set above
            g.drawLine(0, 0, 0, h-1);
            g.drawLine(1, 0, w-2, 0);

            g.setColor(highlightColor);    // top left corner
            g.drawLine(1, 1, 1, h-3);
            g.drawLine(2, 1, w-3, 1);

            g.setColor(shadowColor);       // inner 3D border shadow
            g.drawLine(1, h-2, w-2, h-2);
            g.drawLine(w-2, 1, w-2, h-3);

            g.setColor(darkShadowColor);     // bottom right corner
            g.drawLine(0, h-1, w-1, h-1);
            g.drawLine(w-1, h-1, w-1, 0);
        }

        // If there's no room to draw arrow, bail.
        if (h < 5 || w < 5)      {
            g.setColor(origColor);
            return;
        }

        if (isPressed) {
            g.translate(1, 1);
        }

        // Ok, calculate the size of the arrow based on the size of the
        // button and paint it.
        size = Math.min((h - 4) / 3, (w - 4) / 3);
        size = Math.max(size, 2);
	    paintTriangle(
                g,
                (w - size) / 2,
                (h - size) / 2,
                size,
                direction);

        // Reset the Graphics back to it's original settings
        if (isPressed) {
            g.translate(-1, -1);
	    }

        // Reset the color of the paintbrush.
	    g.setColor(origColor);
    }

    /**
     * This method paints the directional trianle on top of the button
     * component in the appropriate direction, using the Altaprise look
     * and feel color scheme.
     *
     * @param g The canvas in which to paint the masterpiece.
     * @param x The x-coordinate the arrow should be contained within.
     * @param y The y-coordinate the arrow should be contained within.
     * @param size  The size of the arrow to be drawn.
     * @param direction The direction it should be drawn in (N, S, E, W).
     */
    public void paintTriangle(Graphics g, int x, int y,
                              int size, int direction) {

        // Get the original color so it can be reset after this operation.
        Color oldColor = g.getColor();
        int mid, i, j;

        // Obtain the size of the arrow area, and its mid-point.
        j = 0;
        size = Math.max(size, 2);
        mid = (size / 2) - 1;

        // Get the x and y coorindates and then set the arrow color.
        g.translate(x, y);
        g.setColor(AltapriseUtilities.DARK_BLUE_COLOR);

        // Perform the painting of the arrow based on the direction.
        switch(direction)       {
            case NORTH:
                for(i = 0; i < size; i++) {
                    g.drawLine(mid-i, i, mid+i, i);
                }

                break;

            case SOUTH:

                j = 0;
                for(i = size-1; i >= 0; i--) {
                    g.drawLine(mid-i, j, mid+i, j);
                    j++;
                }
                break;

            case WEST:
                for(i = 0; i < size; i++)      {
                    g.drawLine(i, mid-i, i, mid+i);
                }
                break;

            case EAST:

                j = 0;
                for(i = size-1; i >= 0; i--)   {
                    g.drawLine(j, mid-i, j, mid+i);
                    j++;
                }
                break;
        }

        // Reset the graphics area.
        g.translate(-x, -y);

        // Reset the paintbrush color.
        g.setColor(oldColor);
    }

}

