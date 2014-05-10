/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.setupconverter.logic;


/**
 *
 * @author prwallace
 */
public enum DiagBrds {
    PGAIN(2000), SERROR_EN(1), SERROR_M(25), ENCODER_CNT_EN(1000), ENCODER_CNT_M(39), DRIVE_TYPE(0),
    DAC_POL(0), ENCODER_POL(0), ENCODER_MODE(2), HW_OT(1), HOME_SW(1), HOME_OT(1);


    private final int m_param;


    /**
     * Constructor for enum DiagBrds
     * @param param     - int value of argument
     */
    private DiagBrds( int param ) {
        this.m_param = param;
    }


    /**
     * Returns the value of this UI type as a String
     * @return  - the string value for this UI type
     */
    public int getValue() {
        return m_param;
    }

    /**
     * Returns the DiagBrds type verses its String value (i.e. PGAIN vs
     * 2000)
     * @param name  - String that is equal to one of the UI fields.
     * @return - returns the UI type that is equal to the argument String
     */
    /*public static DiagBrds getType(String name) {
        DiagBrds temp = null;

        for(DiagBrds s : DiagBrds.values()) {
            if(s.m_param.equals(name)) {
                temp = s;
                break;
            }
        }

        return temp;
    }*/
}
