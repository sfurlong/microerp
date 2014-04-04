package com.altaprise.plaf;

// Import statements.
import javax.swing.*;
import java.awt.*;

public class ThemeUtilities {

    /**
     * This method returns the color that should be used for rendering
     * hypertext links.
     *
     * @return The hypertext link (foreground) color.
     */
    public static Color getHypertextForegroundColor () {
        return UIManager.getColor("Hypertext.foreground");
    }

    /**
     * Returns the normal font for hypertext navigation links.
     *
     * @return
     */
    public static Font getHypertextFont () {
        return UIManager.getFont("Hypertext.font");
    }

    /**
     * Returns the font for hypertext links that are currently active in
     * the UI (e.g. the selected page in Strategy Model Manager).
     *
     * @return
    */
    public static Font getHypertextSelectedFont () {
        return UIManager.getFont("Hypertext.selectedFont");
    }

    /**
     * This method returns the background color for the altaprise VPSLinkbar
     * component.
     *
     * @return The background color for the VPSLinkbar component.
     */
    public static Color getVPSLinkbarBackgroundColor () {
        return UIManager.getColor("VPSLinkbar.background");
    }


    /**
     * This method returns the color for the separator line within the
     * altaprise VPSLinkbar component.
     *
     * @return The separator line color.
     */
    public static Color getVPSLinkbarSeparatorColor () {
     return UIManager.getColor("VPSLinkbar.separatorLine");
    }


    /**
     * Returns the color that should be used as the cell background when
     * highlighting a row in a grid/table component.
     *
     * @return The row background highlight color for a table/grid.
     */
    public static Color getGridRowHighlightBackgroundColor () {
        return UIManager.getColor("Table.rowHighlight");
    }

    /**
     * Returns the color that should be used as the cell background when
     * highlighting a specific cell that has focus within in a grid/table
     * component.
     *
     * @return The row background highlight color for a table/grid.
     */
    public static Color getGridCellHighlightBackgroundColor () {
        return UIManager.getColor("Table.focusCellBackground");
    }

    /**
     * This method returns the font that should be used as the default for
     * rendering cell data in a grid/table control.
     *
     * @return The default font to be used for cell data rendering.
     */
    public static Font getGridFont () {
        return UIManager.getFont("Table.font");
    }

    /**
     * Returns the foreground color for the grid (this is the font color)
     * that should always be applied regardless of the cell state.
     *
     * @return The grid's foreground/font color.
     */
    public static Color getGridForeground () {
        return UIManager.getColor("Table.foreground");
    }

    /**
     * This method returns the font that should be used as the default for
     * rendering table headers (a.k.a. column headers).
     *
     * @return The default font to be used for column headers.
     */
    public static Font getGridHeaderFont () {
        return UIManager.getFont("Table.headerFont");
    }

    /**
     * This method returns the background color for read-only cells in
     * a table or grid component.
     *
     * @return The read-only table/grid cell color.
     */
    public static Color getReadOnlyBackgroundColor () {
        return UIManager.getColor("Table.readOnlyBackgroundColor");
    }

    /**
     * This method returns the foreground (font color) for read-only cells
     * in a table/grid component.
     *
     * @return The foreground color for read-only cells.
    */
    public static Color getReadOnlyForegroundColor () {
        return UIManager.getColor("Table.readOnlyForegroundColor");
    }

    /**
     * This method returns the background color for cells that contain an
     * error within a table/grid component.
     *
     * @return The cell error background color.
     */
    public static Color getErrorBackgroundColor () {
        return UIManager.getColor("Table.cellErrorBackgroundColor");
    }

    /**
     * This method returns the color for the panels on which a Table/Grid is
     * being displayed.  This will allow the area around the grid (that isn't
     * part of the grid) to fall behind and fade into oblivian.
     *
     * @return  The panel color to be used when the panel contains a
     *          grid/table.
     */
    public static Color getTablePanelBackgroundColor () {
        return UIManager.getColor("Table.panelBackgroundColor");
    }

    /**
     * This method returns the background color for chart dialog windows
     * so that they are similar in appearance to the web UI.
     *
     * @return  The panel background color to be used for dialogs that
     *          display charts/graphs.
     */
    public static Color getChartBackgroundColor () {
        return UIManager.getColor("Chart.backgroundColor");
    }

    /**
     * This method returns the insets that should be used for a JTree on a
     * JPanel.  This ensures that a margin on the left and right side of
     * the tree exists.
     *
     * @return The inset/margin for a tree control placed on a JPanel.
     */
    public static Insets getTreeInsets () {
        return UIManager.getInsets("Tree.panelInset");
    }
}

