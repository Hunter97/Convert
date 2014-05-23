/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.setupconverter.logic;


/**
 *
 * @author Hunter97
 */
public interface ICNCTypes {
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


    /**
     *
     */
    public enum HY_INPUTS {
        INPUT1( INPUT.NCS_1 ), INPUT2( INPUT.X_NEG_OT ), INPUT3( INPUT.Y_NEG_OT );
        
        private final INPUT m_inputs;
        
        /**
         * 
         * @param inputs 
         */
        private HY_INPUTS( INPUT inputs ) {
            this.m_inputs = inputs;
        }
    }
}
