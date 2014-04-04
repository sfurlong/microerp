package com.altaprise.plaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.View;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicGraphicsUtils;

public class AltapriseCheckBoxUI extends AltapriseRadioButtonUI {

    // Setup the icons that are used by the checkbox.
    Icon selectedIcon = UIManager.getIcon("CheckBox.selectedIcon");
    Icon disabledSelectedIcon = UIManager.getIcon("CheckBox.disabledSelectedIcon");
    Icon unselectedIcon = UIManager.getIcon("CheckBox.unselectedIcon");
    Icon disabledUnselectedIcon = UIManager.getIcon("CheckBox.disabledUnselectedIcon");
    Icon pressedIcon = UIManager.getIcon("CheckBox.pressedIcon");
    Icon rolloverUnselectedIcon = UIManager.getIcon("CheckBox.rolloverUnSelectedIcon");
    Icon rolloverSelectedIcon = UIManager.getIcon("CheckBox.rolloverSelectedIcon");

    /* These Dimensions/Rectangles are allocated once for all
     * RadioButtonUI.paint() calls.  Re-using rectangles
     * rather than allocating them in each paint call substantially
     * reduced the time it took paint to run.  Obviously, this
     * method can't be re-entered.
     */
    private static Dimension size = new Dimension();
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();

    /**
     * This method contructs an Altaprise style checkbox for the
     * component "c" that is requesting it.
     *
     * @param c The base component asking for the checkbox.
     * @return The ComponentUI delegate to paint the checkbox.
     */
    public static ComponentUI createUI (JComponent c) {
        return new AltapriseCheckBoxUI();
    }

    /**
     * This method paints the actual checkbox icons on the UI.  The icon
     * that isused will depend on the state of the checkbox (e.g. selected,
     * unselected, mouseover, mouse down/armed, etc).
     *
     * @param g The graphics canvas to paint within.
     * @param c The actual checkbox to render.
     */
    public synchronized void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();

        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();

        size = b.getSize(size);
        viewRect.x = viewRect.y = 0;
        viewRect.width = size.width;
        viewRect.height = size.height;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
        textRect.x = textRect.y = textRect.width = textRect.height = 0;

        Icon icon = null;

        if(!model.isEnabled()) {
            if(model.isSelected()) {
               icon = disabledSelectedIcon;
            } else {
               icon = disabledUnselectedIcon;
            }
        } else if(model.isPressed() && model.isArmed()) {
            icon = pressedIcon;
        } else if(model.isSelected()) {
            if(b.isRolloverEnabled() && model.isRollover()) {
                    icon = rolloverSelectedIcon;
                    if (icon == null) {
                        icon = selectedIcon;
                    }
            } else {
                icon = selectedIcon;
            }
        } else if(b.isRolloverEnabled() && model.isRollover()) {
            icon = rolloverUnselectedIcon;
        } else {
            icon = unselectedIcon;
        }

        String text = SwingUtilities.layoutCompoundLabel(
            c, fm, b.getText(), icon,
            b.getVerticalAlignment(), b.getHorizontalAlignment(),
            b.getVerticalTextPosition(), b.getHorizontalTextPosition(),
            viewRect, iconRect, textRect, getDefaultTextIconGap(b));

        // fill background
        if(c.isOpaque()) {
            g.setColor(b.getBackground());
            g.fillRect(0,0, size.width, size.height);
        }

        icon.paintIcon(c, g, iconRect.x, iconRect.y);

        // Draw the Text
        if(text != null) {
            View v = (View) c.getClientProperty(BasicHTML.propertyKey);
            if (v != null) {
               v.paint(g, textRect);
            } else {
               if(model.isEnabled()) {
                   // *** paint the text normally
                   g.setColor(b.getForeground());
                   BasicGraphicsUtils.drawString(g,text,model.getMnemonic(),
                                                 textRect.x + 3,
                                                 textRect.y + fm.getAscent());
               } else {
                   // *** paint the text disabled
                   g.setColor(b.getBackground().brighter());
                   BasicGraphicsUtils.drawString(g,text,model.getMnemonic(),
                                                 textRect.x + 3,
                                                 textRect.y + fm.getAscent() + 1);
                   g.setColor(b.getBackground().darker());
                   BasicGraphicsUtils.drawString(g,text,model.getMnemonic(),
                                                 textRect.x + 3,
                                                 textRect.y + fm.getAscent());
               }
               if(b.hasFocus() && b.isFocusPainted() &&
                  textRect.width > 0 && textRect.height > 0 ) {
                   paintFocus(g, textRect, size);
               }
           }
        }
    }
}
