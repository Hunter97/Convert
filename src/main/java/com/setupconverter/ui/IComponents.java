/**
 * IComponents.java
 * Paul Wallace
 * June 2014
 * 
 * Main interface for ConvertUI.  Provides enumeration constants for the UI components
 * and the selectable system types.  Defines base features of the UI, which are
 * display status messages, allows user to select system types, and provide
 * access to the Windows file system.
 */
package com.setupconverter.ui;

import java.awt.*;
import java.io.File;

/**
 * IComponents is an interface the defines functionality of class ConvertUI and its
 * enumeration constants.
 * @author prwallace
 */
public interface IComponents {

    /**
     * Enumeration constants for the components of the UI.
     */
    public enum  UI {

        LOAD( "Load" ), RUN( "Run" ), SAVE( "Save" ), CLOSE( "Close" ), CHECKSUM( "Checksum" ), CONVERT( "Convert" ), DEFAULT( "Default" );

        private final String m_btnName;

        /**
         * Constructor for enum UI
         * @param name  - String value assigned to this enum type
         */
        private UI( String name ) {
            m_btnName = name;
        }

        /**
         * Get/return the String value of this UI component
         * @return  - String value of this UI component
         */
        public String getName() {
            return m_btnName;
        }

        /**
         * Get/return the enum type that matches the String argument.
         * @param name  - String representation of this UI component
         * @return      - This enum type     
         */
        public static UI getType( String name ) {
            UI temp = DEFAULT;

            for( UI s : UI.values() ) {
                if( s.m_btnName.equals( name ) ){
                    temp = s;
                    break;
                }
            }

            return temp;
        }
    }


    /**
     * Enumeration constants representing the available drive system types
     */
    public enum DriveType {
        YASKAWA( "6 Axis Yaskawa" ), DIAG_BRDS( "Diagnostic Boards" ), EDGETI( "Edge Pro Ti Stand" );

        private final String m_system;

        /**
         * Constructor for enum SYSTEM
         * @param system - String value of this SYSTEM type
         */
        private DriveType( String system ) {
            m_system = system;
        }

        /**
         * Get/return the String value of this SYSTEM type
         * @return - String value assigned to this SYSTEM type
         */
        public String getName() {
            return m_system;
        }

        /**
         * Get/return the enum type that matches the String argument.
         * @param name  - String representation of this SYSTEM type
         * @return      - This enum type     
         */
        public static DriveType getType( String name ) {
            DriveType temp = null;

            for( DriveType sys : DriveType.values() ) {
                if( sys.m_system.equals( name ) ){
                    temp = sys;
                    break;
                }
            }

            return temp;
        }
    }


    /**
     * Displays a message in the status area of the UI.  The message represents
     * the current process, like load or run, and any error messages.
     * @param color     - Black text = process; Red text = error
     * @param message   - The status message to be displayed
     * @param tip       - Tip about the current process or error
     */
    public void setStatus( Color color, String message, String tip );


    /**
     * Get/return a File object to the selected file.  Generates a Windows
     * dialog that allows user to browse the Windows File system to load or save 
     * a file.  Files are filtered by the argument extension.
     * @param dialogType    - Indicates dialog type; 0 = OPEN_DIALOG, 1 = SAVE_DIALOG
     * @param ext           - File extension of file
     * @return File         - File object of selected file
     */
    public File getFile( int dialogType, String ext );


    /**
     * Get/return the selected system type from the comboBox
     * @return  - The user selection from comboBox
     */
    public String getSelectedSystem();
}

