package com.altaprise.plaf;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.*;
import java.awt.*;

public class AltapriseTabbedPaneUI extends BasicTabbedPaneUI {

    /**
     * This method contructs a altaprise style tabbed pane for the
     * component "c" that is requesting it.
     *
     * @param c The base component asking for the tabbed pane.
     * @return The ComponentUI delegate to paint the tabbed pane.
     */
    public static ComponentUI createUI (JComponent c) {
        return new AltapriseTabbedPaneUI();
    }

    /**
     * This method installs the UI delegate into the PLaF to render the
     * scrollbar component.
     *
     */
    protected void installDefaults() {
        super.installDefaults();
    }

    /**
     * Paint the text (label) that appears on the tab panel.
     *
     * @param g The graphics area to drawn within.
     * @param tabPlacement Horizontal or Vertical placement indicator.
     * @param font The font to use for the tab text.
     * @param metrics The font metrics to use for the tab text.
     * @param tabIndex The tab to be drawn based on a 0 based index.
     * @param title The text to render on the tab.
     * @param textRect The text rectangle area (to show focus).
     * @param isSelected Indicates if the current tab has focus.
     */
    protected void paintText(Graphics g, int tabPlacement,
                             Font font, FontMetrics metrics, int tabIndex,
                             String title, Rectangle textRect,
                             boolean isSelected) {

        Font tabFont = (!isSelected ? AltapriseUtilities.MEDIUM_FONT :
                AltapriseUtilities.MEDIUM_BOLD_FONT);

    	g.setFont(tabFont);
	    if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
	    	g.setColor(AltapriseUtilities.WHITE_COLOR);
	    	g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
	    } else { // tab disabled
	    	g.setColor(AltapriseUtilities.BUTTON_DISABLED_COLOR);
	    	g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
	    }
    }


    /**
     * This method overrides the base tab background painting algorithm so
     * that all of the tabs in the background (e.g. not selected/focused)
     * are in a lighter shade of blue than the one that is forward.
     *
     * @param g The graphical canvas on which to paint the tab.
     * @param tabPlacement  The place of the tab to be painted (N, S, E, W).
     * @param tabIndex The index of this tab within the set of tabs.
     * @param x The X position of the top left corner of the tab.
     * @param y The Y position of the top left corner of the tab.
     * @param w The width of the tab.
     * @param h The height of the tab.
     * @param isSelected Is this tab foward/selected in the set.
     */
    protected void paintTabBackground(Graphics g, int tabPlacement,
                                      int tabIndex,
                                      int x, int y, int w, int h,
                                      boolean isSelected ) {

        Color oldColor = g.getColor();

        // Obtain the appropriate color.  If the tab is selected, then the
        // color should be a dark blue, otherwise it is light blue.
        Color paintColor = (!isSelected ?
                UIManager.getColor("TabbedPane.tabNotSelected") :
                UIManager.getColor("TabbedPane.tabSelected"));

        // Set the appropriate color for the paint brush.
        g.setColor(paintColor);

        // Paint the tab background based on its placement.
        switch(tabPlacement) {
            case LEFT:
                g.fillRect(x+1, y+1, w-2, h-3);
                break;
            case RIGHT:
                  g.fillRect(x, y+1, w-2, h-3);
                break;
            case BOTTOM:
                  g.fillRect(x+1, y, w-3, h-1);
                  break;
            case TOP:
            default:
                g.fillRect(x+1, y+1, w-3, h-1);
                break;
        }

        g.setColor(oldColor);
    }

    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
                                         int selectedIndex,
                                         int x, int y, int w, int h) {

        if (tabPlacement == TOP)
            g.setColor(UIManager.getColor("TabbedPane.tabSelected"));

        if (tabPlacement != TOP || selectedIndex < 0 ||
            (rects[selectedIndex].y + rects[selectedIndex].height + 1 < y)) {
            g.drawLine(x, y, x+w-2, y);
        } else {
            Rectangle selRect = rects[selectedIndex];

            g.drawLine(x, y, selRect.x - 1, y);
            if (selRect.x + selRect.width < x + w - 2) {
                g.drawLine(selRect.x + selRect.width, y,
                           x+w-2, y);
            } else {
                g.setColor(shadow);
                g.drawLine(x+w-2, y, x+w-2, y);
            }
        }
    }

}
