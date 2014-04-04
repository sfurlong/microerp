package com.altaprise.plaf;

import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class AltapriseSplitPaneDivider extends BasicSplitPaneDivider {

    /**
     * Constructor.
     *
     * @param ui Create a divider for the supplied splitter pane parameter.
     */
    public AltapriseSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }


    /**
     * Creates and return an instance of JButton that can be used to
     * collapse the left component in the metal split pane.
     */
    protected JButton createLeftOneTouchButton() {


        JButton b = new JButton() {

            public void setBorder(Border b) {
            }

            public void paint(Graphics g) {
                if (splitPane != null) {
                    int[]   xs = new int[3];
                    int[]   ys = new int[3];
                    int     blockSize = 4;

                    // Fill the background first ...
                    g.setColor(AltapriseUtilities.LIGHT_GRAY_COLOR);
                    g.fillRect(0, 0, this.getWidth(),
                               this.getHeight());

                    // ... then draw the arrow.
                    g.setColor(AltapriseUtilities.DARK_BLUE_COLOR);
                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        xs[0] = blockSize;
                        xs[1] = 0;
                        xs[2] = blockSize << 1;
                        ys[0] = 0;
                        ys[1] = ys[2] = blockSize;
                        g.drawPolygon(xs, ys, 3); // Little trick to make the
                                                  // arrows of equal size
                    } else {
                        xs[0] = xs[2] = blockSize;
                        xs[1] = 0;
                        ys[0] = 0;
                        ys[1] = blockSize;
                        ys[2] = blockSize << 1;
                    }
                    g.fillPolygon(xs, ys, 3);
                }
            }

    	    // Don't want the button to participate in focus traversable.
	        public boolean isFocusable() {
    		    return false;
	        }
        };

        b.setMinimumSize(new Dimension(ONE_TOUCH_SIZE, ONE_TOUCH_SIZE));
        b.setRequestFocusEnabled(false);
	    b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);

        return b;
    }

    /**
     * Creates and return an instance of JButton that can be used to
     * collapse the right component in the metal split pane.
     */
    protected JButton createRightOneTouchButton() {

        JButton b = new JButton() {

            public void setBorder(Border border) {
            }

            public void paint(Graphics g) {
                if (splitPane != null) {
                    int[]          xs = new int[3];
                    int[]          ys = new int[3];
                    int            blockSize = 4;

                    // Fill the background first ...
                    g.setColor(AltapriseUtilities.LIGHT_GRAY_COLOR);
                    g.fillRect(0, 0, this.getWidth(), this.getHeight());

                    // ... then draw the arrow.
                    if (orientation == JSplitPane.VERTICAL_SPLIT) {
                        xs[0] = blockSize;
                        xs[1] = blockSize << 1;
                        xs[2] = 0;
                        ys[0] = blockSize;
                        ys[1] = ys[2] = 0;
                    } else {
                        xs[0] = xs[2] = 0;
                        xs[1] = blockSize;
                        ys[0] = 0;
                        ys[1] = blockSize;
                        ys[2] = blockSize << 1;
                    }
                    g.setColor(AltapriseUtilities.DARK_BLUE_COLOR);
                    g.fillPolygon(xs, ys, 3);
                }
            }

	        // Don't want the button to participate in focus traversable.
	        public boolean isFocusable() {
	        	return false;
    	    }
        };

        b.setMinimumSize(new Dimension(ONE_TOUCH_SIZE, ONE_TOUCH_SIZE));
    	b.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setRequestFocusEnabled(false);

        return b;
    }


    public Dimension minimumLayoutSize(Container c) {
        return new Dimension(0,0);
    }

    public Dimension preferredLayoutSize(Container c) {
        return new Dimension(0, 0);
    }

    public void removeLayoutComponent(Component c) {}

    public void addLayoutComponent(String string, Component c) {}
}
