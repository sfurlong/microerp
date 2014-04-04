package com.altaprise.plaf;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalSplitPaneUI;
import javax.swing.*;

public class AltapriseSplitPaneUI extends MetalSplitPaneUI {

    /**
     * Creates a new splitter pane instance.
     *
     * @param x The parent component that is requested the allocation of
     *          a splitter pane.
     * @return The new splitter pane component.
     */
    public static ComponentUI createUI(JComponent x) {
        return new AltapriseSplitPaneUI();
    }

    /**
     * Creates the default divider.
     */
    public BasicSplitPaneDivider createDefaultDivider() {
	    return new AltapriseSplitPaneDivider(this);
    }

}
