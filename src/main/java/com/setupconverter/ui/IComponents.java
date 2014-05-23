/**
 * 
 */
package com.setupconverter.ui;

import java.awt.*;
import java.io.File;


/**
 * IConversionComponents - UI Interface for manipulating configuration files
 * @version 1.00.0
 * @category Interface
 * @author prwallace
 *
 */
public interface IComponents {

    /**
     * Enumeration constants representing the buttons within the UI.  Provides a
     * public getName method that returns the String value of this constant and
     * a getType( String name) method that iterates over all enumeration constants
     * for a match to the String argument and returns the matched UIButton.
     * 
     * @see
     *  getName() - returns String value of this UIButton.
     *  getType( String name ) - returns the UIButton whose String value matches the argument String
     *
     */
    public enum  UI {

        LOAD( "Load" ), RUN( "Run" ), SAVE( "Save" ), CLOSE( "Close" ), CHECKSUM( "Checksum" ), CONVERT( "Convert" ), DEFAULT( "Default" );

        private final String m_btnName;

        private UI( String name ) {
            m_btnName = name;
        }

        public String getName() {
            return m_btnName;
        }

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
     * Enumeration constants representing the available drive system types that a
     * setup file can be converted to operate.
     */
    public enum SYSTEM {
        BENCH( "6 Axis Yaskawa" ), HYPATH( "Hypath" );

        private final String m_system;

        private SYSTEM( String system ) {
            m_system = system;
        }

        public String getName() {
            return m_system;
        }
    }


    /**
     * Displays a message in the status JTextField of the UI.  The message represents
     * the state of the current process(Load, Run, or Save), and/or state of the
     * selected radio button (Checksum or Convert).
     * A tool tip is set to the status JTextField that provides the name of the current
     * File.
     * 
     * @param color - Black text used for status messages; Red text used for exceptions.
     * @param message - Reports the status of the process or reports an exception.
     * @param tip - Displays the loaded or saved File object to the user.
     */
    public void setStatus( Color color, String message, String tip );


    /**
     * Gets the desired file to load or save from the user.  Uses a JFileChooser and
     * a FileNameExtensionFilter to generate a Windows dialog to allow user to
     * browse to a file.  Files are filtered by the argument extension.  Gets/Returns
     * a File object from the selected file.
     * 
     * @param dialogType    - indicates file type; 0 = FileChooser.OPEN_DIALOG, 1 = FileChooser.SAVE_DIALOG
     * @param ext           - String; extension used to filter file dialog
     * @return File         - returns File object of selected file
     */
    public File getFile( int dialogType, String ext );
}

