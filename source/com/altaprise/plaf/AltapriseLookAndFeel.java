package com.altaprise.plaf;

import javax.swing.*;
import javax.swing.plaf.basic.*;

public class AltapriseLookAndFeel extends
// com.sun.java.swing.plaf.windows.WindowsLookAndFeel {
		BasicLookAndFeel {

	/**
	 * This method returns the string name of the pluggable look and feel.
	 * 
	 * @return Returns the name of the pluggable look and feel.
	 */
	public String getName() {
		return AltapriseUtilities.PLAF_NAME;
	}

	/**
	 * This method returns a string description of the pluggable look and feel.
	 * 
	 * @return Returns the description of the pluggable look and feel.
	 */
	public String getDescription() {
		return AltapriseUtilities.PLAF_DESCRIPTION;
	}

	/**
	 * This method returns a string ID value for the pluggable look and feel.
	 * 
	 * @return Returns the ID of the pluggable look and feel.
	 */
	public String getID() {
		return AltapriseUtilities.PLAF_NAME;
	}

	/**
	 * This method returns a boolean indicating if the pluggable look and feel
	 * is native to the Operating System. A value of False is always returned
	 * since this is a custom look and feel.
	 * 
	 * @return A boolean value of False indicating that this is not a native
	 *         PLaF.
	 */
	public boolean isNativeLookAndFeel() {
		return false;
	}

	/**
	 * This method returns a boolean indicating if the look and feel is
	 * supported on the client Operating System. A value of True is always
	 * returned for this method since it is cross O/S supported.
	 * 
	 * @return A boolean value of True indicating that this is PLaF is always
	 *         supported regardless of client O/S.
	 */
	public boolean isSupportedLookAndFeel() {
		return true;
	}

	/**
	 * This method overrides the default UI delagets with the Altaprise UI
	 * delegates that handle the specific UI Look and Feel design.
	 * 
	 * @param uiDefaults
	 */
	protected void initClassDefaults(UIDefaults uiDefaults) {

		// Setup all default UI delegate classes.
		super.initClassDefaults(uiDefaults);

		UIManager.put("ScrollBarUI", "com.altaprise.plaf.AltapriseScrollBarUI");
		UIManager.put("ComboBoxUI", "com.altaprise.plaf.AltapriseComboBoxUI");
		UIManager.put("TabbedPaneUI",
				"com.altaprise.plaf.AltapriseTabbedPaneUI");
		UIManager.put("TreeUI", "com.altaprise.plaf.AltapriseTreeUI");
		UIManager.put("CheckBoxUI", "com.altaprise.plaf.AltapriseCheckBoxUI");
		UIManager.put("RadioButtonUI",
				"com.altaprise.plaf.AltapriseRadioButtonUI");
		UIManager.put("SplitPaneUI", "com.altaprise.plaf.AltapriseSplitPaneUI");
		UIManager.put("TextFieldUI", "javax.swing.plaf.metal.MetalTextFieldUI");
		UIManager.put("ButtonUI", "com.altaprise.plaf.AltapriseButtonUI");
		UIManager.put("FileChooserUI",
				"javax.swing.plaf.metal.MetalFileChooserUI");
		UIManager.put("ToggleButtonUI",
				"com.altaprise.plaf.AltapriseToggleButtonUI");
	}

	/**
	 * This method loads all of the default PLaF colors, then overrides them
	 * with the Altaprise PLaF pallette.
	 * 
	 * @param uiDefaults
	 *            The UI Default colors for the base PLaF.
	 */
	protected void initSystemColorDefaults(UIDefaults uiDefaults) {

		// Initialize the colors from the base PLaF that was extended.
		super.initClassDefaults(uiDefaults);

		// Create a name/value pair of color overrides for the
		// PLaF.
		String[] colors = { "desktop", "#86A2BE", "activeCaption", "#244E7A",
				"activeCaptionText", "#FFFFFF", "activeCaptionBorder",
				"#CCCCCC", "inactiveCaption", "#999999", "inactiveCaptionText",
				"#FFFFFF", "inactiveCaptionBorder", "#CCCCCC", "window",
				"#FFFFFF", "windowBorder", "#999999", "windowText", "#000000",
				"menu", "#CCCCCC", "menuText", "#000000", "text", "#FFFFFF",
				"textText", "#000000", "textHighlight", "#244E7A",
				"textHighlightText", "#FFFFFF", "textInactiveText", "#999999",
				"control", "#CCCCCC", "controlText", "#000000",
				"controlHighlight", "#E2E0E0", "controlLtHighlight", "#FFFFFF",
				"controlShadow", "#CCCCCC", "controlDkShadow", "#999999",
				"scrollbar", "#BED1E9", "info", "#CCCCCC", "infoText",
				"#006699" };

		// Update the PLaF with color scheme.
		this.loadSystemColors(uiDefaults, colors, false);

	}

	/**
	 * Setup the default component look and feels, then apply overrides for the
	 * sepcific Look and Feel attributes.
	 * 
	 * @param uiDefaults
	 *            The default UI settings for the bsae PLaF.
	 */
	protected void initComponentDefaults(UIDefaults uiDefaults) {

		// Create all default settings.
		super.initComponentDefaults(uiDefaults);

		// Create a name/value pair array for all of the look and feel color and
		// icon overrides that we want to create for the look and
		// feel.
		Object[] altaprise = {

				// Basic Font overrides
				"EditorPane.font",
				AltapriseUtilities.MEDIUM_FONT,
				"Label.font",
				AltapriseUtilities.MEDIUM_FONT,
				"List.font",
				AltapriseUtilities.MEDIUM_FONT,
				"Panel.font",
				AltapriseUtilities.MEDIUM_FONT,
				"PopupMenu.font",
				AltapriseUtilities.MEDIUM_FONT,
				"TextArea.font",
				AltapriseUtilities.MEDIUM_FONT,
				"TextField.font",
				AltapriseUtilities.MEDIUM_FONT,
				"TextPane.font",
				AltapriseUtilities.MEDIUM_FONT,
				"TitledBorder.font",
				AltapriseUtilities.MEDIUM_FONT,
				"ToggleButton.font",
				AltapriseUtilities.MEDIUM_FONT,
				"Viewport.font",
				AltapriseUtilities.MEDIUM_FONT,

				// Key Mappings
				"TextArea.focusInputMap",
				AltapriseUtilities.TEXTAREA_INPUT_MAP,
				"TextField.focusInputMap",
				AltapriseUtilities.FIELD_INPUT_MAP,
				"PasswordField.focusInputMap",
				AltapriseUtilities.FIELD_INPUT_MAP,
				"FormattedTextField.focusInputMap",
				AltapriseUtilities.FIELD_INPUT_MAP,

				// CheckBox overrides
				"CheckBox.selectedIcon",
				AltapriseUtilities.CHECKBOX_ON_ICON,
				"CheckBox.disabledSelectedIcon",
				AltapriseUtilities.CHECKBOX_ON_ICON,
				"CheckBox.unselectedIcon",
				AltapriseUtilities.CHECKBOX_OFF_ICON,
				"CheckBox.disabledUnselectedIcon",
				AltapriseUtilities.CHECKBOX_OFF_ICON,
				"CheckBox.pressedIcon",
				AltapriseUtilities.CHECKBOX_ON_ICON,
				"CheckBox.rolloverSelectedIcon",
				AltapriseUtilities.CHECKBOX_ON_ICON,
				"CheckBox.rolloverUnSelectedIcon",
				AltapriseUtilities.CHECKBOX_OFF_ICON,
				"CheckBox.font",
				AltapriseUtilities.MEDIUM_FONT,

				// ComboBox overrides
				"ComboBox.font",
				AltapriseUtilities.MEDIUM_FONT,
				"ComboBox.background",
				AltapriseUtilities.FOCUS_CELL_BACKGROUND_COLOR,
				"ComboBox.foreground",
				AltapriseUtilities.FOCUS_CELL_FOREGROUND_COLOR,
				// "ComboBox.buttonBackground",
				// AltapriseUtilities.BUTTON_BACKGROUND_COLOR,
				// "ComboBox.buttonShadow",
				// AltapriseUtilities.MEDIUM_GRAY_COLOR,
				// "ComboBox.buttonDarkShadow", AltapriseUtilities.BLACK_COLOR,
				// "ComboBox.buttonHighlight",
				// AltapriseUtilities.BUTTON_BACKGROUND_COLOR,
				"ComboBox.selectionBackground",
				AltapriseUtilities.TABLE_SELECTION_COLOR,
				"ComboBox.selectionForeground",
				AltapriseUtilities.BLACK_COLOR,
				"ComboBox.disabledBackground",
				AltapriseUtilities.READ_ONLY_BACKGROUND_COLOR,
				"ComboBox.disabledForeground",
				AltapriseUtilities.BLACK_COLOR,
				"ComboBox.ancestorInputMap",
				new UIDefaults.LazyInputMap(new Object[] { "ESCAPE",
						"hidePopup", "PAGE_UP", "pageUpPassThrough",
						"PAGE_DOWN", "pageDownPassThrough", "HOME",
						"homePassThrough", "END", "endPassThrough", "DOWN",
						"selectNext", "KP_DOWN", "selectNext", "UP",
						"selectPrevious", "KP_UP", "selectPrevious", "ENTER",
						"enterPressed", "F4", "togglePopup" }),

				// Chart overrides
				"Chart.backgroundColor", AltapriseUtilities.PALE_BLUE_COLOR,

				// RadioButton overrides
				"RadioButton.selectedIcon",
				AltapriseUtilities.RADIOBUTTON_ON_ICON,
				"RadioButton.disabledSelectedIcon",
				AltapriseUtilities.RADIOBUTTON_ON_ICON,
				"RadioButton.unselectedIcon",
				AltapriseUtilities.RADIOBUTTON_OFF_ICON,
				"RadioButton.disabledUnselectedIcon",
				AltapriseUtilities.RADIOBUTTON_OFF_ICON,
				"RadioButton.pressedIcon",
				AltapriseUtilities.RADIOBUTTON_ON_ICON,
				"RadioButton.rolloverSelectedIcon",
				AltapriseUtilities.RADIOBUTTON_ON_ICON,
				"RadioButton.rolloverUnSelectedIcon",
				AltapriseUtilities.RADIOBUTTON_OFF_ICON, "RadioButton.font",
				AltapriseUtilities.MEDIUM_FONT,

				// Internal frame overrides (used within an MDI)
				"InternalFrame.closeIcon",
				AltapriseUtilities.CLOSE_WINDOW_ICON,
				"InternalFrame.minimizeIcon",
				AltapriseUtilities.MINIMIZE_WINDOW_ICON,
				"InternalFrame.iconifyIcon",
				AltapriseUtilities.ICONIFY_WINDOW_ICON,
				"InternalFrame.maximizeIcon",
				AltapriseUtilities.MAXIMIZE_WINDOW_ICON,
				// "InternalFrame.icon", AltapriseUtilities.ALTAPRISE_ICON,
				"InternalFrame.titleFont", AltapriseUtilities.MEDIUM_FONT,

				// Menu component overrides
				"Menu.arrowIcon", AltapriseUtilities.MENU_ARROW_ICON,
				"Menu.arrowOverIcon", AltapriseUtilities.MENU_ARROW_OVER_ICON,
				"Menu.icon", AltapriseUtilities.CHECK_ICON, "MenuBar.font",
				AltapriseUtilities.MEDIUM_FONT, "Menu.font",
				AltapriseUtilities.MEDIUM_FONT, "MenuItem.font",
				AltapriseUtilities.MEDIUM_FONT,

				// Tree control overrides
				"Tree.collapsedIcon", AltapriseUtilities.COLLAPSE_ICON,
				"Tree.expandedIcon", AltapriseUtilities.EXPAND_ICON,
				"Tree.leafIcon", AltapriseUtilities.TREE_LEAF_ICON,
				"Tree.closedIcon", AltapriseUtilities.TREE_CLOSED_ICON,
				"Tree.openIcon", AltapriseUtilities.TREE_OPEN_ICON,
				"Tree.hash", AltapriseUtilities.TREE_COLOR, "Tree.hashStep",
				Integer.valueOf("2"), "Tree.selectionBackground",
				AltapriseUtilities.TREE_SELECTION_COLOR,
				"Tree.selectionForeground", AltapriseUtilities.BLACK_COLOR,
				"Tree.font", AltapriseUtilities.MEDIUM_FONT, "Tree.panelInset",
				AltapriseUtilities.TREE_AREA_INSET,

				// Option dialog overrides
				"OptionPane.errorIcon", AltapriseUtilities.MESSAGE_ERROR_ICON,
				"OptionPane.informationIcon",
				AltapriseUtilities.MESSAGE_INFORMATIONAL_ICON,
				"OptionPane.questionIcon",
				AltapriseUtilities.MESSAGE_QUESTION_ICON,
				"OptionPane.warningIcon",
				AltapriseUtilities.MESSAGE_WARNING_ICON, "OptionPane.font",
				AltapriseUtilities.MEDIUM_FONT,

				// Progress bar control overrides
				"ProgressBar.background", AltapriseUtilities.DARK_GRAY_COLOR, // Initial
																				// color
																				// of
																				// progess
																				// bar
				"ProgressBar.foreground", AltapriseUtilities.DARK_BLUE_COLOR, // The
																				// progress
																				// indicator
																				// color
				"ProgressBar.border", AltapriseUtilities.EMPTY_BORDER,

				// Table control overrides
				"Table.focusCellHighlightBorder",
				AltapriseUtilities.TABLE_SELECTION_COLOR,
				"Table.focusCellForeground", AltapriseUtilities.BLACK_COLOR,
				"Table.focusCellBackground",
				AltapriseUtilities.TABLE_SELECTION_COLOR,
				"Table.selectionBorderColor",
				AltapriseUtilities.TABLE_SELECTION_COLOR, "Table.rowHighlight",
				AltapriseUtilities.TABLE_SELECTION_COLOR, "Table.font",
				AltapriseUtilities.MEDIUM_FONT, "Table.headerFont",
				AltapriseUtilities.MEDIUM_BOLD_FONT, "Table.ancestorInputMap",
				AltapriseUtilities.TABLE_INPUT_MAP, "Table.foreground",
				AltapriseUtilities.BLACK_COLOR, "Table.panelBackgroundColor",
				AltapriseUtilities.DARK_GRAY_COLOR,
				"Table.readOnlyBackgroundColor",
				AltapriseUtilities.READ_ONLY_BACKGROUND_COLOR,
				"Table.readOnlyForegroundColor",
				AltapriseUtilities.READ_ONLY_FOREGROUND_COLOR,
				"Table.cellErrorBackgroundColor",
				AltapriseUtilities.CELL_ERROR_BACKGROUND_COLOR,
			    "Table.focusSelectedCellHighlightBorder", AltapriseUtilities.TABLE_CELL_FOCUS_SELECTED_HIGHLIGHT_BORDER,
			    "Table.focusCellHighlightBorder",AltapriseUtilities.TABLE_CELL_FOCUS_HIGHLIGHT_BORDER,
				

				// Tabbed panel/sheet overrides
				"TabbedPane.contentBorderInsets",
				AltapriseUtilities.TABBED_PANE_CONTENT_BORDER_INSET,
				"TabbedPane.tabNotSelected",
				AltapriseUtilities.MEDIUM_BLUE_COLOR, "TabbedPane.tabSelected",
				AltapriseUtilities.DARK_BLUE_COLOR, "TabbedPane.selected",
				AltapriseUtilities.DARK_BLUE_COLOR, "TabbedPane.focus",
				AltapriseUtilities.MEDIUM_BLUE_COLOR, "TabbedPane.darkShadow",
				AltapriseUtilities.LIGHT_GRAY_COLOR,
				"TabbedPane.lightHighlight",
				AltapriseUtilities.LIGHT_GRAY_COLOR, "TabbedPane.highlight",
				AltapriseUtilities.LIGHT_GRAY_COLOR, "TabbedPane.shadow",
				AltapriseUtilities.MEDIUM_DARK_BLUE_COLOR,
				"TabbedPane.tabAreaInsets", AltapriseUtilities.TAB_INSET,

				// Tooltip overrides
				"ToolTip.background",
				AltapriseUtilities.FOCUS_CELL_BACKGROUND_COLOR,
				"ToolTip.foreground", AltapriseUtilities.BLACK_COLOR,
				"ToolTip.font", AltapriseUtilities.SMALL_FONT,

				// Toolbar overrides
				"ToolBar.separatorSize", AltapriseUtilities.EMPTY_DIMENSION,

				// Button overrides
				// !Removed the lightblue below because it was causing the
				// toolbar
				// !to show light blue background.
				// !"Button.background",
				// AltapriseUtilities.BUTTON_BACKGROUND_COLOR,
				"Button.foreground", AltapriseUtilities.DARK_BLUE_COLOR,
				"Button.font", AltapriseUtilities.MEDIUM_BOLD_FONT,
				"Button.focus", AltapriseUtilities.BUTTON_BACKGROUND_COLOR,
				"Button.select", AltapriseUtilities.BUTTON_BACKGROUND_COLOR,
				"Button.disabledText",
				AltapriseUtilities.BUTTON_DISABLED_COLOR,

				// Hypertext link foreground color.
				"Hypertext.foreground",
				AltapriseUtilities.HYPERTEXT_FOREGROUND_COLOR,
				"Hypertext.font", AltapriseUtilities.MEDIUM_FONT,
				"Hypertext.selectedFont", AltapriseUtilities.MEDIUM_BOLD_FONT,

				// VPSLinkbar background color.
				"VPSLinkbar.background",
				AltapriseUtilities.VPSLINKBAR_BACKGROUND_COLOR,
				"VPSLinkbar.separatorLine", AltapriseUtilities.DARK_BLUE_COLOR, };

		// Update the UI settings for this Look and Feel definition.
		uiDefaults.putDefaults(altaprise);
	}
}
