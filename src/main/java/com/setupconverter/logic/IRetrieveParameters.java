/**
 * IRetrieveParameters.java
 * Paul Wallace
 * June 2014
 * 
 * IRetrieveParameters provides implementation for the enum's in SetupConverter.
 *  
 * Main attributes:
 *      * Provides get methods for an enum's name and value parameters.
 *
 */
package com.setupconverter.logic;


/**
 * IRetrieveParameters:  Defines basic methods for the enum's in SetupConverter.
 * @author prwallace
 */
public interface IRetrieveParameters {

    /**
     * Get/return the integer value of this enum
     * @return  - The ordinal value of this enum type
     */
    public int getValue();

    /**
     * Get/return the String for this enum type
     * @return  - String value assigned to this enum type
     */
    public String getName();
}
