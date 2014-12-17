/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.setupconverter.logic;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author prwallace
 */
public interface IMachineTypeData {
    /**
     * Default Axes settings for a CNC using the Hypath diagnostic boards
     */
    public enum DiagBrds1 implements IParametersEnum {
        PGAIN( 2000 ), IGAIN( 0 ), DGAIN( 0 ), FGAIN( 0 ), VGAIN( 0 ), SERVO_ERROR_EN( 1 ), SERVO_ERROR_M( 25 ), 
        ENCODER_CNTS_EN( 1000 ), ENCODER_CNTS_M( 39 ), DRIVE_TYPE( 0 ), DAC( 0 ), MARKER_PULSE( 0 );

        private final int m_value;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum HYPATH
         * @param name  - String value assigned to this enum type
         * @param value - Ordinal value assigned to this enum type
         */
        private DiagBrds1( int value ) {
            this.m_value = value;
        }


        @Override
        public int getValue() {
            return m_value;
        }

        /**
         * Add all enum constants to a Map< String, Integer > and return the Map.
         * @return  - enum constants as a Map
         */
        public static Map< String, Integer > toMap() {
            for( IBaseSetupData.DiagBrds param : IBaseSetupData.DiagBrds.values() ) {
                map.put( param.getName(), param.getValue() );
            }

            return map;
       }

        @Override
        public String getName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }


    /**
     * Default Axes settings for a CNC using the Picopath diagnostic boards.
     */
    public enum Bench implements IParametersEnum {
        PGAIN( 20 ), IGAIN( 0 ), DGAIN( "DGain=", 20 ), FGAIN( "FGain=", 100 ), VGAIN( "VGain=", 0 ), PGAIN_2( "PGain2=", 20 ), IGAIN_2( "IGain2=", 0 ),
        DGAIN_2( "DGain2=", 20 ), FGAIN_2( "FGain2=", 100 ), VGAIN_2( "VGain2=", 0 ), PGAIN_3( "PGain3=", 20 ), IGAIN_3( "IGain3=", 0 ), DGAIN_3( "DGain3=", 20 ),
        FGAIN_3( "FGain3=", 100 ), VGAIN_3( "VGain3=", 0 ), PGAIN_4( "PGain4=", 20 ), IGAIN_4( "IGain4=", 0 ), DGAIN_4( "DGain4=", 20 ), FGAIN_4( "FGain4=", 100 ),
        VGAIN_4( "VGain4=", 0 ), PGAIN_5( "PGain5=", 20 ), IGAIN_5( "IGain5=", 0 ), DGAIN_5( "DGain5=", 20 ), FGAIN_5( "FGain5=", 100 ), VGAIN_5( "VGain5=", 0 ),
        SERVO_ERROR_EN( "ServoErrTolerance(english)=", 1 ), SERVO_ERROR_M( "ServoErrTolerance(metric)=", 25 ), ENCODER_CNTS_EN( "EncoderCounts(english)=", 8192 ),
        ENCODER_CNTS_M( "EncoderCounts(metric)=", 322 ), DRIVE_TYPE( "DriveType=", 0 ), DAC( "DACPolarity=", 0 ), ENCODER_POLARITY( "EncoderPolarity=", 1 ),
        ENCODER_MODE( "EncoderMode=", 2 ), USE_HW_OT( "UseHWOvertravelSwitches=", 1 ), HOME_SW( "UseHomeSwitch=", 1 ), HOME_OT( "HomeToOvertravel=", 1 ),
        MARKER_PULSE( "UseMarker=", 0 );

        private final int m_value;
        private final String m_name;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum PICO_PATH
         * @param name  - String value of this enum type
         * @param value - Ordinal value of this enum type
         */
        private Bench( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }


        @Override
        public int getValue() {
            return m_value;
        }

        @Override
        public String getName() {
            return m_name;
        }

        /**
         * Add all enum constants to a Map< String, Integer > and return the Map.
         * @return  - enum constants as a Map
         */
        public static Map< String, Integer > toMap() {
            for( IBaseSetupData.Bench param : IBaseSetupData.Bench.values() ) {
                map.put( param.getName(), param.getValue() );
            }
            
            return map;
        }
    }


    /**
     * Default Axes settings for a EdgePro Ti on a test stand.
     */
    public enum EdgeProTi implements IParametersEnum {
        PGAIN( "PGain=", 100 ), IGAIN( "IGain=", 0 ), DGAIN( "DGain=", 150 ), FGAIN( "FGain=", 100 ), VGAIN( "VGain=", 300 ), PGAIN_2( "PGain2=", 100 ), IGAIN_2( "IGain2=", 0 ),
        DGAIN_2( "DGain2=", 150 ), FGAIN_2( "FGain2=", 100 ), VGAIN_2( "VGain2=", 100 ), PGAIN_3( "PGain3=", 100 ), IGAIN_3( "IGain3=", 0 ), DGAIN_3( "DGain3=", 150 ),
        FGAIN_3( "FGain3=", 100 ), VGAIN_3( "VGain3=", 300 ), PGAIN_4( "PGain4=", 100 ), IGAIN_4( "IGain4=", 0 ), DGAIN_4( "DGain4=", 150 ), FGAIN_4( "FGain4=", 100 ),
        VGAIN_4( "VGain4=", 300 ), PGAIN_5( "PGain5=", 100 ), IGAIN_5( "IGain5=", 0 ), DGAIN_5( "DGain5=", 150 ), FGAIN_5( "FGain5=", 100 ), VGAIN_5( "VGain5=", 300 ),
        SERVO_ERROR_EN( "ServoErrTolerance(english)=", 1 ), SERVO_ERROR_M( "ServoErrTolerance(metric)=", 25 ), ENCODER_CNTS_EN( "EncoderCounts(english)=", 8000 ),
        ENCODER_CNTS_M( "EncoderCounts(metric)=", 315 ), DRIVE_TYPE( "DriveType=", 1 ), DAC( "DACPolarity=", 0 ), ENCODER_POLARITY( "EncoderPolarity=", 1 ),
        ENCODER_MODE( "EncoderMode=", 2 ), USE_HW_OT( "UseHWOvertravelSwitches=", 1 ), HOME_SW( "UseHomeSwitch=", 1 ), HOME_OT( "HomeToOvertravel=", 1 ),
        MARKER_PULSE( "UseMarker=", 0 );

        private final int m_value;
        private final String m_name;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum PICO_PATH
         * @param name  - String value of this enum type
         * @param value - Ordinal value of this enum type
         */
        private EdgeProTi( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }


        @Override
        public int getValue() {
            return m_value;
        }

        @Override
        public String getName() {
            return m_name;
        }

        /**
         * Add all enum constants to a Map< String, Integer > and return the Map.
         * @return  - enum constants as a Map
         */
        public static Map< String, Integer > toMap() {
            for( IBaseSetupData.EdgeProTi param : IBaseSetupData.EdgeProTi.values() ) {
                map.put( param.getName(), param.getValue() );
            }
            
            return map;
        }
    }
}
