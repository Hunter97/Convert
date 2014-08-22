/**
 * IProcessParameters.java
 * Paul Wallace
 * June 2014
 * 
 * IProcessParameters interface defines the methods used to manipulate a configuration
 * file.  The interface allows the client to read/write an entire configuration file,
 * set/get parameter values, add a block of parameters to a Map, set/get the checksum,
 * and replace an entire block of parameters.
 *  
 * Main attributes:
 *      * Set/Get the file checksum
 *      * Read/write the parameter file
 *      * Set/Get parameter values
 *      * Replace a block of parameters
 * 
 */
package com.setupconverter.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


/**
 * IProcessParameters interface; provides a set of methods to manipulate a configuration
 * file.
 * @author prwallace
 */
public interface IProcessParameters {


    /**
     * Reads a configuration file, line by line, and stores each line into an
     * ArrayList.
     * @param file          - File object of the configuration file
     * @throws IOException  - Thrown when BufferedReader fails to read in the file.
     */
    public void read( File file ) throws IOException;


    /**
     * Writes the contents of the parameter list to a configuration file.  Opens
     * a output stream using a BufferedWriter wrapped with an OutputStreamWriter
     * and passes each element of the parameter list into the file, one element
     * as a time.  The end result is a new configuration file with a new checksum.
     * @param file          - File object of new configuration file
     * @throws IOException  - Thrown when BufferedWriter fails to write to file
     */
    public void write( File file ) throws IOException;


    /**
     * Adds a block of parameters from the parameter list into a Map.
     * @param blockTitle    - Title of parameter block (i.e. [Machine]) to add into the Map
     * @param map           - Map to hold the block of parameters from the parameter list
     */
    public void add( String blockTitle, Map< String, Integer > map  );


    /**
     * Set the value of the specified parameter in the parameter list.  Parses the
     * parameter list, starting after the block title, for a match to the argument
     * parameter and set the value with this value.
     * @param blockTitle    - Title of parameter block (i.e. [Machine]) 
     * @param paramName     - Parameter whose value is to be set
     * @param value         - Value set to parameter
     */
    public void setValue( String blockTitle, String paramName, int value );


    /**
     * Calculates the checksum of a configuration file.  Each character in the
     * parameter list, beginning at the 2nd index, is summed together.
     * @throws IOException - Thrown if unable to calculate checksum
     */
    public void setChecksum() throws IOException;


    /**
     * Get/return the value of the specified parameter from the parameter list.
     * Parses the parameter list, starting after the block title, for a match to
     * the argument parameter.  Split the parameter and return its value.  Return
     * a -1 if the parameter is not found or its value is invalid.
     * @param blockTitle    - Title of parameter block (i.e. [Machine])
     * @param paramName     - Parameter whose value is to be returned
     * @return              - Value of the parameter if found or -1
     */
    public int getValue( String blockTitle, String paramName );


    /**
     * Get/return the checksum of the configuration file.
     * @return - an integer equal to sum of all characters
     */
    public int getChecksum();

    /**
     * Get/return the parameter list.
     * @return - The list of parameters
     */
    public ArrayList getAllParameters();


    /**
     * Replaces a set of parameters within the parameter list.  Parses the parameter
     * list, starting after the block title, for matches to parameters contained
     * within the argument Map, and replaces those parameters within the list.
     * @param blockTitle    - Title of the parameter block (i.e. [Machine])
     * @param map           - Map containing the set of parameters to replace
     */
    public void replace( String blockTitle, Map< String, Integer > map );
}
