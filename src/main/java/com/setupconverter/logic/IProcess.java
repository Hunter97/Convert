/**
 * IProcess.java
 * Paul Wallace
 * June 2014
 * 
 * IProcess interface defines the base functionality of the logic class.
 *  
 * Main attributes:
 *      * Set/Get the file checksum
 *      * Read/write the parameter file
 *      * Convert the file parameters
 * 
 */
package com.setupconverter.logic;

import java.io.File;
import java.io.IOException;


/**
 * IProcess defines the base functionality of the logic class.
 * @author prwallace
 */
public interface IProcess {

    /**
     * Reads each line of a configuration file and stores the line into a a 
     * List< String >.  The File argument is loaded into a BufferedReader that
     * is wrapped by a FileInputStream that uses the standard character set UTF_8.
     * @param file  - File reference to the configuration file
     * @throws IOException
     */
    public void read( File file ) throws IOException;

    /**
     * Calculates the checksum of a configuration file.  Each character in the
     * List< String >(), beginning at the 2nd index, is summed together.
     * @throws IOException
     */
    public void setChecksum() throws IOException;

    /**
     * Get/return the checksum of the configuration file.
     * @return an integer equal to sum of characters in List< String >
     */
    public int getChecksum();

    /**
     * Converts the original parameter file to control the user specified drive 
     * system.  Replaces Axes, I/O, Speed, and Machine settings based on the selected
     * drive type.  I/O is re-arranges to allow user to satisfy homing and simulate
     * cutting by use of a switch box and loop-back jumper CPC.  Instantiates the
     * DataAccessObj which provides access to the specific drive system type.
     * @throws IOException
     */
    public void convert() throws IOException;

    /**
     * Saves the contents of the parameter list to the argument File.  Adds the
     * new checksum back to the parameter list prior to saving the file.  Each
     * String in the parameter list is written to buffer via an OutputStreamWriter
     * whose location is referenced by the argument File object.
     * @param file  - File object containing reference to location of file
     * @throws IOException
     */
    public void write( File file ) throws IOException;
}
