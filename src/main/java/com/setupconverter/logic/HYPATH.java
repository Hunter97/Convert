/**
 * SetupConverter.java Program
 * enum HYPATH.java
 */
package com.setupconverter.logic;


/**
 * Default Axes settings for a CNC using the Hypath Diagnostic boards.
 * @author prwallace
 */
public enum HYPATH {
    PGAIN(2000), IGAIN(0), DGAIN(0), FGAIN(0), ENCODER_CNTS_EN(1000), ENCODER_CNTS_M(39), DRIVE_TYPE(0),
    DAC(0), ENCODER_POL(0), ENCODER_MODE(2), HW_OT(1), HOME_SW(1), HOME_OT(1);

    private final int m_param;


    /**
     * Constructor for enum HYPATH
     * @param param - default value for parameters in the axes screen
     */
    private HYPATH( int param ) {
        this.m_param = param;
    }


    /**
     * Returns the default value of this HYPATH axis parameter
     * @return  - the value for this HYPATH parameter
     */
    public int getValue() {
        return m_param;
    }
}
