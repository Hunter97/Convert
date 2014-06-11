/**
 * 
 */
package com.setupconverter.logic;

import java.io.File;
import java.io.IOException;


/**
 * @author prwallace
 *
 */
public interface IProcess {

    /**
     * Enumeration constants containing the titles to each block (or group) of parameters
     * within the parameter List.  Each constant represents a string in the form of
     * "[Title]\r\n".
     */
    public enum BLOCK {
        MACHINE( "[Machine]\r\n" ), SPEEDS( "[Speeds]\r\n" ), AIC( "[AnalogInputCard]\r\n" ), LINK( "[Link]\r\n" ), IO( "[I/O]\r\n" ), ROTATE( "[Rotate]\r\n" ), TILT( "[Tilt]\r\n" ),
        DUAL_ROTATE( "[DualRotate]\r\n" ), DUAL_TILT( "[DualTilt]\r\n" ), DUAL_GANTRY( "[DualGantry]\r\n" ), THC1( "[THC1]\r\n" ), THC2( "[THC2]\r\n" ), THC3( "[THC3]\r\n" ),
        THC4( "[THC4]\r\n" ), AXIS1( "[Axis0]\r\n" ), AXIS2( "[Axis1]\r\n" ), AXIS7( "[Axis6]\r\n" );

        private final String m_name;

        private BLOCK( String name ) {
            this.m_name = name;
        }

        /**
         * Get/return the String value of the enum type
         * @return  String of enum type
         */
        public String getName() {
            return m_name;
        }
    }

    public void loadParams( File file ) throws IOException;

    public void setChecksum() throws IOException;

    public int getChecksum();

    public void convertFile() throws IOException;

    public void writeParam( File file ) throws IOException;
}
