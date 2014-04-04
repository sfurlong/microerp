package com.altaprise.plaf;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultEditorKit;

public class AltapriseUtilities {

    // The name of the Altaprise Pluggable Look and Feel (PLaF)
    protected static final String PLAF_NAME = "Altaprise";

    // The description of the PLaF
    protected static final String PLAF_DESCRIPTION = "Altaprise standard Pluggable Look and Feel";

    /**
     * No one should be able to instantiate this class.
     */
    protected AltapriseUtilities () {
    }

    // Icon definitions.  These are LazyValue's so that they are only created
    // if actually referenced.

    // Menu oriented icons.
    protected static Object CHECK_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/check.gif");
    protected static Object MENU_ARROW_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/menuArrow.gif");
    protected static Object MENU_ARROW_OVER_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/menuArrowOver.gif");

    // Tree oriented icons.
    protected static Object COLLAPSE_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/collapse.gif");
    protected static Object EXPAND_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/expand.gif");
    protected static Object TREE_LEAF_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/treeLeaf.gif");
    protected static Object TREE_CLOSED_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/treeClosed.gif");
    protected static Object TREE_OPEN_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/treeOpen.gif");

    // Window and Internal Frame oriented icons.
    protected static Object CLOSE_WINDOW_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/closeWindow.gif");
    protected static Object MAXIMIZE_WINDOW_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/maximizeWindow.gif");
    protected static Object MINIMIZE_WINDOW_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/restoreWindow.gif");
    protected static Object ICONIFY_WINDOW_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/minimizeWindow.gif");
    protected static Object MESSAGE_ERROR_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/messageError.gif");

    // Error message oriented icons.
    protected static Object MESSAGE_WARNING_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/messageWarning.gif");
    protected static Object MESSAGE_QUESTION_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/messageQuestion.gif");
    protected static Object MESSAGE_INFORMATIONAL_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/messageInformational.gif");

    // Radio button icons.
    protected static Object RADIOBUTTON_ON_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/radiobuttonOn.gif");
    protected static Object RADIOBUTTON_OFF_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/radiobuttonOff.gif");

    // Checkbox icons.
    protected static Object CHECKBOX_ON_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/checkboxOn.gif");
    protected static Object CHECKBOX_OFF_ICON =
            LookAndFeel.makeIcon(AltapriseUtilities.class, "icons/checkboxOff.gif");

    // Define colors that are used within the PLaF.
    protected static Color TREE_COLOR = new Color(187, 43, 69);                     // #BB2B45
    protected static Color FOCUS_CELL_HIGHLIGHT_COLOR = new Color(204, 204, 204);   // #CCCCCC
    protected static Color FOCUS_CELL_BACKGROUND_COLOR = new Color(255, 255, 225);  // #FFFFE1
    protected static Color FOCUS_CELL_FOREGROUND_COLOR = new Color(0, 0, 0);        // #000000
    protected static Color READ_ONLY_BACKGROUND_COLOR = new Color(225, 225, 225);   // #E1E1E1
    protected static Color READ_ONLY_FOREGROUND_COLOR = new Color(123, 123, 123);   // #7B7B7B
    protected static Color CELL_ERROR_BACKGROUND_COLOR = new Color(204, 82, 82);    // #CC5252
    protected static Color TREE_SELECTION_COLOR = new Color(190, 209, 233);         // #BED1E9
    protected static Color BLACK_COLOR = new Color(0, 0, 0);                        // #000000
    protected static Color WHITE_COLOR = new Color(255, 255, 255);                  // #FFFFFF
    protected static Color RED_COLOR = new Color(255, 0, 0);                        // #FF0000
    protected static Color DARK_BLUE_COLOR = new Color(36, 78, 122);                // #244E7A
    protected static Color MEDIUM_BLUE_COLOR = new Color(134, 162, 190);            // #86A2BE
    protected static Color DARK_GRAY_COLOR = new Color(153, 153, 153);              // #999999
    protected static Color MEDIUM_GRAY_COLOR = new Color(180, 180, 180);            // #B4B4B4
    protected static Color LIGHT_GRAY_COLOR = new Color(206, 207, 206);             // #CECFCE
    protected static Color MEDIUM_DARK_BLUE_COLOR = new Color(115, 145, 172);       // #7391AC
    protected static Color PALE_BLUE_COLOR = new Color(237, 243, 249);              // #EDF3F9
    protected static Color TABLE_SELECTION_COLOR = new Color(126, 134, 233);        // #BED1E9
    protected static Color BUTTON_BACKGROUND_COLOR = new Color(218, 232, 250);      // #DAE8FA
    protected static Color BUTTON_DISABLED_COLOR = new Color(181, 179, 180);        // #B5B3B4
    protected static Color BUTTON_FOCUS_COLOR = new Color(204, 51, 51);             // #CC3333
    protected static Color HYPERTEXT_FOREGROUND_COLOR = new Color(0, 102, 153);     // #006699
    protected static Color VPSLINKBAR_BACKGROUND_COLOR = new Color(237, 243, 249);  // #EDF3F9

    // Define all Font styles and sizes used by the altaprise PLaF
    protected static Font SMALL_FONT = new Font("Arial", Font.PLAIN, 10);
    protected static Font MEDIUM_FONT = new Font("Arial", Font.PLAIN, 11);
    protected static Font MEDIUM_BOLD_FONT = new Font("Arial", Font.BOLD, 11);
    protected static Font LARGE_FONT = new Font("Arial", Font.PLAIN, 12);

    // Define all of the Insets that are used within the altaprise PLaF.
    protected static Insets TABBED_PANE_CONTENT_BORDER_INSET = new Insets (3, 0, 0, 0);
    protected static Insets EMPTY_INSET = new Insets (0, 0, 0, 0);
    protected static Insets TAB_INSET = new Insets (5, 5, 0, 5);
    protected static Insets SELECTED_TAB_INSET = new Insets (5, 8, 5, 8);
    protected static Insets TREE_AREA_INSET = new Insets (0, 2, 0, 2);

    // Define static borders used within the PLaF.
    protected static Border EMPTY_BORDER = BorderFactory.createEmptyBorder();

    protected static Border TABLE_CELL_FOCUS_SELECTED_HIGHLIGHT_BORDER
    	= BorderFactory.createLineBorder(
            AltapriseUtilities.DARK_BLUE_COLOR, 1);

    protected static Border TABLE_CELL_FOCUS_HIGHLIGHT_BORDER
	= BorderFactory.createLineBorder(
        AltapriseUtilities.DARK_BLUE_COLOR, 2);

    
    // Define the button border for text and icon oriented buttons.
    protected static Border BUTTON_TEXT_BORDER =
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                            AltapriseUtilities.DARK_BLUE_COLOR, 1),
                    BorderFactory.createEmptyBorder(0, 2, 1, 2));
    protected static Border BUTTON_FOCUSED_TEXT_BORDER =
            BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(
                            AltapriseUtilities.DARK_BLUE_COLOR, 2),
                    BorderFactory.createEmptyBorder(0, 2, 1, 2));
    protected static Border BUTTON_ICON_BORDER = EMPTY_BORDER;

    // Define all dimensions that are used within the altaprise PLaF.
    protected static Dimension EMPTY_DIMENSION = new Dimension(0,0);

    // Define the input map for basic key actions and sequences.
    protected static UIDefaults.LazyInputMap FIELD_INPUT_MAP =
            new UIDefaults.LazyInputMap (new Object[] {
        "ctrl C", DefaultEditorKit.copyAction,
        "ctrl V", DefaultEditorKit.pasteAction,
        "ctrl X", DefaultEditorKit.cutAction,
        "COPY", DefaultEditorKit.copyAction,
        "PASTE", DefaultEditorKit.pasteAction,
        "CUT", DefaultEditorKit.cutAction,
        "shift LEFT", DefaultEditorKit.selectionBackwardAction,
        "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
        "shift RIGHT", DefaultEditorKit.selectionForwardAction,
        "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
        "ctrl LEFT", DefaultEditorKit.previousWordAction,
        "ctrl KP_LEFT", DefaultEditorKit.previousWordAction,
        "ctrl RIGHT", DefaultEditorKit.nextWordAction,
        "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,
        "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
        "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction,
        "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,
        "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
        "ctrl A", DefaultEditorKit.selectAllAction,
        "HOME", DefaultEditorKit.beginLineAction,
        "END", DefaultEditorKit.endLineAction,
        "shift HOME", DefaultEditorKit.selectionBeginLineAction,
        "shift END", DefaultEditorKit.selectionEndLineAction,
        "typed \010", DefaultEditorKit.deletePrevCharAction,
        "DELETE", DefaultEditorKit.deleteNextCharAction,
        "RIGHT", DefaultEditorKit.forwardAction,
        "LEFT", DefaultEditorKit.backwardAction,
        "KP_RIGHT", DefaultEditorKit.forwardAction,
        "KP_LEFT", DefaultEditorKit.backwardAction,
        "ENTER", JTextField.notifyAction,
        "ctrl BACK_SLASH", "unselect",
        "control shift O", "toggle-componentOrientation",
        "ESCAPE", "reset-field-edit",
        "UP", "increment",
        "KP_UP", "increment",
        "DOWN", "decrement",
        "KP_DOWN", "decrement",
    });

    /*
//Below are the the std laf attributes for jtextarea.
JTextArea (Java L&F)
Navigate in    |  Tab 
Navigate in    |  Alt+Char Accelerator Key (if defined)
Navigate out forward    |  Ctrl+Tab
Navigate out backward   |  Ctrl+Shift+Tab
Move up/down one line        |  Up, Down
Move left/right one char     |  Left, Right
Move to start/end of line    |  Home, End
Move to prev/next word       |  Ctrl+Left/Right
Move to start/end of text area    |  Ctrl+Home/End
Block move up/down    |  PgUp, PgDn
Block move left       |  Ctrl+PgUp
Block move right      |  Ctrl+PgDn
Block extend up       |  Shift+PgUp
Block extend down     |  Shift+PgDn
Block extend left     |  Ctrl+Shift+PgUp
Block extend right    |  Ctrl+Shift+PgDn
Select all      |  Ctrl+A
Deselect all    |  arrow keys
Extend selection    |  Shift+Up, Shift+Down
Extend selection left/right    |  Shift+Left, Shift+Right
Extend selection to start/end of line    |  Shift+Home, Shift+End
Extend selection to start/end of text area    |  Ctrl+Shift+Home, Ctrl+Shift+End
Extend selection to prev/next word    |  Ctrl+Shift+Left, Ctrl+Shift+Right
Copy selection         |  Ctrl+C
Cut selection          |  Ctrl+X
Paste Selected Text    |  Ctrl+V
Delete next character        |  Delete
Delete previous character    |  Backspace
Insert line break    |  Enter
Insert tab           |  Tab
     */
    protected static UIDefaults.LazyInputMap TEXTAREA_INPUT_MAP =
        new UIDefaults.LazyInputMap (new Object[] {
    "ctrl C", DefaultEditorKit.copyAction,
    "ctrl V", DefaultEditorKit.pasteAction,
    "ctrl X", DefaultEditorKit.cutAction,
    "COPY", DefaultEditorKit.copyAction,
    "PASTE", DefaultEditorKit.pasteAction,
    "CUT", DefaultEditorKit.cutAction,
    "shift LEFT", DefaultEditorKit.selectionBackwardAction,
    "shift KP_LEFT", DefaultEditorKit.selectionBackwardAction,
    "shift RIGHT", DefaultEditorKit.selectionForwardAction,
    "shift KP_RIGHT", DefaultEditorKit.selectionForwardAction,
    "ctrl LEFT", DefaultEditorKit.previousWordAction,
    "ctrl KP_LEFT", DefaultEditorKit.previousWordAction,
    "ctrl RIGHT", DefaultEditorKit.nextWordAction,
    "ctrl KP_RIGHT", DefaultEditorKit.nextWordAction,
    "ctrl shift LEFT", DefaultEditorKit.selectionPreviousWordAction,
    "ctrl shift KP_LEFT", DefaultEditorKit.selectionPreviousWordAction,
    "ctrl shift RIGHT", DefaultEditorKit.selectionNextWordAction,
    "ctrl shift KP_RIGHT", DefaultEditorKit.selectionNextWordAction,
    "ctrl A", DefaultEditorKit.selectAllAction,
    "HOME", DefaultEditorKit.beginLineAction,
    "END", DefaultEditorKit.endLineAction,
    "shift HOME", DefaultEditorKit.selectionBeginLineAction,
    "shift END", DefaultEditorKit.selectionEndLineAction,
    "typed \010", DefaultEditorKit.deletePrevCharAction,
    "DELETE", DefaultEditorKit.deleteNextCharAction,
    "RIGHT", DefaultEditorKit.forwardAction,
    "LEFT", DefaultEditorKit.backwardAction,
    "KP_RIGHT", DefaultEditorKit.forwardAction,
    "KP_LEFT", DefaultEditorKit.backwardAction,
    "ENTER", DefaultEditorKit.insertBreakAction,
    "ctrl BACK_SLASH", "unselect",
    "control shift O", "toggle-componentOrientation",
    "ESCAPE", "reset-field-edit",
    "UP", "increment",
    "KP_UP", "increment",
    "DOWN", "decrement",
    "KP_DOWN", "decrement",
});

    // These key maps are for table/grid navigation purposes only.
    protected static UIDefaults.LazyInputMap TABLE_INPUT_MAP =
            new UIDefaults.LazyInputMap(new Object[]{
        "ctrl C", "copy",
        "ctrl V", "paste",
        "ctrl X", "cut",
        "COPY", "copy",
        "PASTE", "paste",
        "CUT", "cut",
        "RIGHT", "selectNextColumn",
        "KP_RIGHT", "selectNextColumn",
        "LEFT", "selectPreviousColumn",
        "KP_LEFT", "selectPreviousColumn",
        "DOWN", "selectNextRow",
        "KP_DOWN", "selectNextRow",
        "UP", "selectPreviousRow",
        "KP_UP", "selectPreviousRow",
        "shift RIGHT", "selectNextColumnExtendSelection",
        "shift KP_RIGHT", "selectNextColumnExtendSelection",
        "shift LEFT", "selectPreviousColumnExtendSelection",
        "shift KP_LEFT", "selectPreviousColumnExtendSelection",
        "shift DOWN", "selectNextRowExtendSelection",
        "shift KP_DOWN", "selectNextRowExtendSelection",
        "shift UP", "selectPreviousRowExtendSelection",
        "shift KP_UP", "selectPreviousRowExtendSelection",
        "PAGE_UP", "scrollUpChangeSelection",
        "PAGE_DOWN", "scrollDownChangeSelection",
        "HOME", "selectFirstColumn",
        "END", "selectLastColumn",
        "shift PAGE_UP", "scrollUpExtendSelection",
        "shift PAGE_DOWN", "scrollDownExtendSelection",
        "shift HOME", "selectFirstColumnExtendSelection",
        "shift END", "selectLastColumnExtendSelection",
        "ctrl PAGE_UP", "scrollLeftChangeSelection",
        "ctrl PAGE_DOWN", "scrollRightChangeSelection",
        "ctrl HOME", "selectFirstRow",
        "ctrl END", "selectLastRow",
        "ctrl shift PAGE_UP", "scrollRightExtendSelection",
        "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection",
        "ctrl shift HOME", "selectFirstRowExtendSelection",
        "ctrl shift END", "selectLastRowExtendSelection",
        "TAB", "selectNextColumnCell",
        "shift TAB", "selectPreviousColumnCell",
        "ENTER", "selectNextRowCell",
        "shift ENTER", "selectPreviousRowCell",
        "ctrl A", "selectAll",
        "ESCAPE", "cancel",
        "F2", "startEditing"
    });

}

