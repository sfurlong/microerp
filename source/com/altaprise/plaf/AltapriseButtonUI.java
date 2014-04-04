package com.altaprise.plaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.View;
import java.awt.event.FocusEvent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicGraphicsUtils;

public class AltapriseButtonUI extends BasicButtonUI {

    // Local copy of the button.
    private final static AltapriseButtonUI altapriseButtonUI = new AltapriseButtonUI();

    // NOTE: These are not really needed, but at this point we can't pull
    // them. Their values are updated purely for historical reasons.
    protected Color focusColor;
    protected Color selectColor;
    protected Color disabledTextColor;

    /**
     * Create the PLaF for the Button.
     */
    public static ComponentUI createUI(JComponent c) {
        return altapriseButtonUI;
    }

    /**
     * Install all of the UI defaults for the Button.
     *
     * @param b The button for which the defaults are to be installed.
     */
    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
    }

    /**
     * Uninstall the UI defaults for the button.
     * @param b The button for which the defaults are to be uninstalled.
     */
    public void uninstallDefaults(AbstractButton b) {
	    super.uninstallDefaults(b);
    }

    /**
     * Create the button listener.
     *
     * @param b The button that is requesting the listener.
     * @return The basic button event listener.
     */
    protected BasicButtonListener createButtonListener(AbstractButton b) {
	    return new AltapriseButtonListener(b);
    }


    /**
     * Gets the select color Button.select for the PLaF.
     * @return The Button.select Color.
     */
    protected Color getSelectColor() {
        selectColor = UIManager.getColor(getPropertyPrefix() + "select");
	    return selectColor;
    }

    /**
     * Gets the disabled text Button.disabledText for the PLaF.
     * @return The Button.disabledText Color.
     */
    protected Color getDisabledTextColor() {
        disabledTextColor = UIManager.getColor(getPropertyPrefix() +
                                               "disabledText");
	    return disabledTextColor;
    }

    /**
     * Gets the focus border color Button.focus for the PLaF.
     * @return The Button.focus Color.
     */
    protected Color getFocusColor() {
        focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
	    return focusColor;
    }

    /**
     * Paint the button in the pressed down state.
     * @param g The graphics area to draw within.
     * @param b The button that is to be drawn.
     */
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if ( b.isContentAreaFilled() ) {
            Dimension size = b.getSize();
            g.setColor(getSelectColor());
	        g.fillRect(0, 0, size.width, size.height);
    	}
    }

    /* These rectangles/insets are allocated once for all
     * ButtonUI.paint() calls.  Re-using rectangles rather than
     * allocating them in each paint call substantially reduced the time
     * it took paint to run.  Obviously, this method can't be re-entered.
     */
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();

    /**
     * This is the base paint method for the button.  All of other required
     * paint methods are invoked from here (e.g. paint text, paint icon,
     * etc.).
     *
     * @param g The graphics are to draw the button in (e.g. the canvas).
     * @param c The actual component/button to draw with all of its state.
     */
    public void paint(Graphics g, JComponent c) {

        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        //Don't do the rest if this is a button with no text,
        //like a toolbar button.  This is so we get regular button
        //behavior on toolbars like rollover.
        if (b.getText() == null || b.getText().length() == 0) {
            super.paint(g, c);
            return;
        }
        FontMetrics fm = g.getFontMetrics();

        // Get the defined insets.
        Insets i = c.getInsets();

        // Set the appropriate border for the button depending on
        // if an icon is present or not.
        if (b.getIcon() == null) {
            if (b.hasFocus()) {
                c.setBorder(AltapriseUtilities.BUTTON_FOCUSED_TEXT_BORDER);
            } else {
                c.setBorder(AltapriseUtilities.BUTTON_TEXT_BORDER);
            }
        } else {
            c.setBorder(AltapriseUtilities.BUTTON_ICON_BORDER);
        }

        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = b.getWidth() - (i.right + viewRect.x);
        viewRect.height = b.getHeight() - (i.bottom + viewRect.y);

        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        Font f = c.getFont();
        g.setFont(f);

        // Layout the text and icon.
        String text = SwingUtilities.layoutCompoundLabel(c, fm, b.getText(),
                b.getIcon(), b.getVerticalAlignment(),
                b.getHorizontalAlignment(), b.getVerticalTextPosition(),
                b.getHorizontalTextPosition(), viewRect, iconRect, textRect,
                b.getText() == null ? 0 : b.getIconTextGap());

        clearTextShiftOffset();

        // Perform UI specific press action, e.g. Windows L&F shifts text
        if (model.isArmed() && model.isPressed()) {
            paintButtonPressed(g,b);
        }

        // Paint the Icon
        if(b.getIcon() != null) {
            paintIcon(g,c,iconRect);
        }

        // Paint the text portion of the button.
        if (text != null && !text.equals("")) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
                v.paint(g, textRect);
            } else {
                paintText(g, b, textRect, text);
            }
        }

        // Paint the button when it has focus.
        if (b.isFocusPainted() && b.hasFocus()) {
            // paint UI specific focus
            paintFocus(g, b, viewRect, textRect, iconRect);
        }
    }

    /**
     * This method paints the icon within the button component.
     *
     * @param g The graphics area (a.k.a. canvas) to draw within.
     * @param c The actual button to be drawn.
     * @param iconRect The rectangular area of the button.
     */
    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect){

        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        Icon icon = b.getIcon();
        Icon tmpIcon = null;

        // Since we are painting an icon, abort if there isn't one.  This
        // was verified earlier in the paint() method, but you never know,
        // the runtime could have lied.
	    if(icon == null) {
	       return;
	    }

        // Paint an appropriate icon based on the button state.
        if(!model.isEnabled()) {
    		if(model.isSelected()) {
               tmpIcon = b.getDisabledSelectedIcon();
	    	} else {
               tmpIcon = b.getDisabledIcon();
		    }
        } else if(model.isPressed() && model.isArmed()) {
            tmpIcon = b.getPressedIcon();
            if(tmpIcon != null) {
                // revert back to 0 offset
                clearTextShiftOffset();
            }
        } else if(b.isRolloverEnabled() && model.isRollover()) {
		    if(model.isSelected()) {
               tmpIcon = b.getRolloverSelectedIcon();
		    } else {
               tmpIcon = b.getRolloverIcon();
		    }
        } else if(model.isSelected()) {
            tmpIcon = b.getSelectedIcon();
	    }

	    if(tmpIcon != null) {
	        icon = tmpIcon;
	    }

        if (model.isPressed() && model.isArmed()) {
            icon.paintIcon(c, g, iconRect.x + getTextShiftOffset(),
                    iconRect.y + getTextShiftOffset());
        } else {
            icon.paintIcon(c, g, iconRect.x, iconRect.y);
        }

    }

    /**
     * This method handled painting the button when it has focus.
     *
     * @param g The graphics area (a.k.a. canvas) to draw within.
     * @param b The actual button to be drawn.
     * @param viewRect The complete button area.
     * @param textRect The area of the text.
     * @param iconRect The area of the icon.
     */
    protected void paintFocus(Graphics g, AbstractButton b,
			      Rectangle viewRect, Rectangle textRect, Rectangle iconRect){

        Rectangle focusRect = new Rectangle();
    	String text = b.getText();
	    boolean isIcon = b.getIcon() != null;

        //Don't do the rest if this is a button with no text,
        //like a toolbar button.  This is so we get regular button
        //behavior on toolbars like rollover.
        if (b.getText() == null || b.getText().length() == 0) {
            super.paintFocus(g, b, viewRect, textRect, iconRect);
            return;
        }
        // If there is text
        if ((text != null) && !text.equals("")) {
      	    if (!isIcon) {
	            focusRect.setBounds(textRect);
	        } else {
	            focusRect.setBounds(iconRect.union(textRect));
    	    }

        // If there is an icon and no text
        } else if (isIcon) {
  	        focusRect.setBounds( iconRect );
        }

        g.setColor(getFocusColor());
    	g.drawRect((focusRect.x-1), (focusRect.y-1), focusRect.width+1,
                focusRect.height+1);
    }


    /**
     * This method is responsible for painting the text within the button
     * if it has any.
     *
     * @param g The graphics area (a.k.a. canvas) to draw within.
     * @param c The actual button to be drawn.
     * @param textRect The area of the text.
     * @param text The actual text to be drawn.
     */
    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {

	    AbstractButton b = (AbstractButton) c;
	    ButtonModel model = b.getModel();
        b.setRolloverEnabled(true);
	    FontMetrics fm = g.getFontMetrics();
        int mnemIndex = b.getDisplayedMnemonicIndex();

    	// Draw the text in an enabled state.
	    if(model.isEnabled()) {

            // If the button is armed, is pressed, or is being rolled over,
            // then the BUTTON_FOCUS_COLOR should be used.
            if (model.isArmed() || model.isRollover() || model.isPressed()) {
                g.setColor(AltapriseUtilities.BUTTON_FOCUS_COLOR);

            // Otherwise use the default foreground color.
            } else {
                g.setColor(b.getForeground());
            }

            // Draw the shortcut underline.
            BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex,
                          textRect.x, textRect.y + fm.getAscent());

    	} else {

	        // Paint the text in a disabled state.
    	    g.setColor(getDisabledTextColor());
	        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, mnemIndex,
                    textRect.x, textRect.y + fm.getAscent());
    	}
    }


    /**
     * Paints the textual part of the button.
     *
     * @param g The graphics area (a.k.a. canvas) to draw within.
     * @param b The actual button to be drawn.
     * @param textRect The text area to be drawn in.
     * @param text The text to be drawn.
     */
    protected void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        paintText(g, (JComponent)b, textRect, text);
    }

    /**
     * This class is the base button listener.
     */
    class AltapriseButtonListener extends BasicButtonListener {

        /**
         * Constructs a new button listener.
         *
         * @param b The button for which the listener should be applied.
         */
        public AltapriseButtonListener(AbstractButton b) {
          super(b);
        }

        /**
         * Fired when the button gains focus.
         *
         * @param e The actual focus event.
         */
        public void focusGained(FocusEvent e) {
            Component c = (Component)e.getSource();
            c.repaint();
        }

        /**
         * Fired when the button loses focus.
         *
         * @param e The actual focus event.
         */
        public void focusLost(FocusEvent e) {
            AbstractButton b = (AbstractButton)e.getSource();
            b.getModel().setArmed(false);
            b.repaint();
        }
    }
}

//
