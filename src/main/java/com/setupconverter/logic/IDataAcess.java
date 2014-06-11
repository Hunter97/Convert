/*
 *  ICNCTypes.java
 *  Paul Wallace
 *  May 2014
 * 
 *  ICNCTypes is an interface that acts like a data transfer object when implemented
 *  by a parent class.  The interface includes the default settings for a group of
 *  drive types, contained in individual enum's, one for each test stand or motion 
 *  diagnostic kit or force simulation.  ICNCTypes implementation, by a parent object,
 *  will add all the required default settings into individual EnumMap's, thus providing
 *  the API's logic access to these settings.
 * 
 *  Main attributes:
 *      *   To provides the minimum gain and I/O settings to control motion and I/O
 *          on each test stand and/or each motion diagnostic kit
 *      *   Different drive types are:
 *          * Hypath Diagnostic kits
 *          * Hypath Diagnostic kits
 *          * Sercos III 6 axis test stand
 *          * Pico-path (analog) 6 axis test stand
 *          * Simulation
 *
 */

package com.setupconverter.logic;

import java.util.Map;


/**
 * Interface ICNCTypes
 * @author Hunter97
 */
    public interface IDataAcess {

    /**
     * Input enum, contains all available inputs of the CNC.  Input integer values
     * equals the input number in Phoenix
     */
    public enum INPUT_NUM {
        DRIVE_DISABLED( 10 ), X_NEG_OT( 11 ), X_POS_OT( 12 ), Y_NEG_OT( 13 ), Y_POS_OT( 14 ), TILT_POS_OT( 35 ), TILT_NEG_OT( 36 ), THC_AUTO_1( 46 ),
        NCS_1( 47 ), THC_AUTO_2( 51 ), NCS_2( 52 ), THC_AUTO_3( 57 ), NCS_3( 57 ), THC_AUTO_4( 61 ), NCS_4( 62 ),FUME_SELECT( 83 ), TORCH_COLLISION( 100 ),
        TEST_LIFTER( 102 ), TILT2_POS_OT( 103 ), TILT2_NEG_OT( 104 ), ROT_2_HOME( 105 ), CUT_SENSE_1( 106 ), CUT_SENSE_2( 107 ), CUT_SENSE_3( 108 ),
        CUT_SENSE_4( 109 ), PARK_HEAD_1( 126 ), PARK_HEAD_2( 127 ), DUAL_HEAD_COLLISION( 130 ), FUME_SENSE( 131 ), JOYSTICK_UP( 133 ), JOYSTICK_DOWN( 134 ),
        JOYSTICK_LEFT( 135 ), JOYSTICK_RIGHT( 136 ), TILT_PLUS( 137 ), TILT_MINUS( 138 ), ROTATE_PLUS( 139 ), ROTATE_MINUS( 140 ), RDY_TO_FIRE_1( 169 ),
        RDY_TO_FIRE_2( 170), RDY_TO_FIRE_3( 171 ), RDY_TO_FIRE_4( 172 ), AUTO_SELECT_1( 182 ), AUTO_SELECT_2( 183 ), AUTO_SELECT_3( 184 ), AUTO_SELECT_4( 185 ),
        MANUAL_SELECT_1( 202 ), MANUAL_SELECT_2( 203 ), MANUAL_SELECT_3( 204), MANUAL_SELECT_4( 205 ), RAISE_ALL_TORCHES( 282 ), AUTO_SELECT_ALL( 283 ),
        MANUAL_IGNITION_SEL( 284 ), HIGH_PREHEAT_SEL( 285 ), LOW_PREHEAT_SEL( 286 ), PIERCE_SELECT( 287 ), RAISE_TORCH_1( 288 ), RAISE_TORCH_2( 289 ),
        RAISE_TORCH_3( 290 ), RAISE_TORCH_4( 292 ), LOWER_TORCH_1( 308 ), LOWER_TORCH_2( 309 ), LOWER_TORCH_3( 310 ), LOWER_TORCH_4( 311 ), RAISE_TORCH_1A( 337 ),
        LOWER_TORCH_1A( 338 ), FP_STOP( 356 ), FP_START( 357 ), FP_MANUAL( 358 ), CUT_SELECT( 361 ), LOWER_ALL_TORCHES( 362 ), FP_BACK_ON_PATH( 377 ),
        FP_FORWARD_ON_PATH( 378 ), TOOL_CYCLE_ACTIVE( 383 );

        private final int value;

        private INPUT_NUM( int value ) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * Output enum, contains all available inputs of the CNC.  Output integer values
     * equals the output number in Phoenix
     */
    public enum OUTPUT {
        THD( 4 ), DRIVE_ENABLE( 55 ), NCE1( 57 ), HOLD_IGN1( 58 ), CUT_CONTROL1( 197 ), CUT_CONTROL2( 198 ), STATION_ENABLE_LED1( 217 ), STATION_ENABLE_LED2( 218 ),
        VENT1( 472 ), VENT2( 473 ), VENT3( 474 ), VENT4( 475 ), VENT5( 476 );

        private final int value;

        private OUTPUT( int value ) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }


    /**
     * Contains default Speed settings for all drive types
     */
    public enum SPEED {
        MAX_EN( "MaxSpeed(english)=", 1000 ), MAX_M( "MaxSpeed(metric)=", 25400 ), SPEED1_EN( "GainSpeed1(english)=", 100 ), SPEED1_M( "GainSpeed1(metric)=", 2540 ), 
        SPEED2_EN( "GainSpeed2(english)=", 300 ), SPEED2_M( "GainSpeed2(metric)=", 7620 ), SPEED3_EN( "GainSpeed3(english)=", 500 ), SPEED3_M( "GainSpeed3(metric)=", 12700 ), 
        SPEED4_EN( "GainSpeed4(english)=", 1000 ), SPEED4_M( "GainSpeed4(metric)=", 25400 ), SPEED5_EN( "GainSpeed5(english)=", 1000 ), SPEED5_M( "GainSpeed5(metric)=", 25400 ),
        ACCEL1( "AccelRate=", 20 ), ACCEL2( "AccelRate2=", 40 ), ACCEL3( "AccelRate3=", 50 ), ACCEL4( "AccelRate4=", 60 ), ACCEL5( "AccelRate5=", 60 );

        private final int m_value;
        private final String m_name;

        private SPEED( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }

        /**
         * Get/return the integer value of the enum type
         * @return  value of enum type
         */
        public int getValue() {
            return m_value;
        }

        /**
         * Get/return the String value of the enum type
         * @return  String of enum type
         */
        public String getName() {
            return m_name;
        }
    }


    /**
     * Contain Machine parameters, used to determine machine configuration
     */
    public enum MACHINE {
        DUAL_GANTRY( "DualGantryInstalled=" ), FP( "FrontPanelInstalled=" ), SERCOS( "SercosSensorUtility=" ), AG( "ArcGlideTHCInstalled=" ),
        PSCOMM_HYPERNET( "PSCommOverHypNetEn" ), STHC( "SensorTHCInstalled=" ), BEVEL_AXES( "SkewRotatorInstalled=" ), DUAL_BEVEL( "DualSkewRotatorInstalled=" ),
        AUTO_HOME( "AutoHome=" ), DUAL_TRANS( "DualTransverseInstalled=" ), NO_ROTATE_TILT( "NoRotateTilt=" ), ONE_ROTATE_TILT( "OneRotateTilt=" ),
        ROTATING_TRANS( "RotatingTransverse=" );

        private final String m_name;

        private MACHINE( String name ) {
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


    /**
     * Default Axes settings for a CNC using the Hypath diagnostic boards.
     * @author prwallace
     */
    public enum HYPATH {
        PGAIN( "PGain=", 2000 ), IGAIN( "IGain=", 0 ), DGAIN( "DGain=", 0 ), FGAIN( "FGain=", 0 ), VGAIN( "VGain=", 0 ), PGAIN_2( "PGain2=", 2000 ), IGAIN_2( "IGain2=", 0 ),
        DGAIN_2( "DGain2=", 0 ), FGAIN_2( "FGain2=", 0 ), VGAIN_2( "VGain2=", 0 ), PGAIN_3( "PGain3=", 2000 ), IGAIN_3( "IGain3=", 0 ), DGAIN_3( "DGain3=", 0 ),FGAIN_3( "FGain3=", 0 ),
        VGAIN_3( "VGain3=", 0 ), PGAIN_4( "PGain4=", 2000 ), IGAIN_4( "IGain4=", 0 ), DGAIN_4( "DGain4=", 0 ), FGAIN_4( "FGain4=", 0 ), VGAIN_4( "VGain4=", 0 ), PGAIN_5( "PGain5=", 2000 ),
        IGAIN_5( "IGain5=", 0 ), DGAIN_5( "DGain5=", 0 ), FGAIN_5( "FGain5=", 0 ), VGAIN_5( "VGain5=", 0 ), SERVO_ERROR_EN( "ServoErrTolerance(english)=", 1 ), SERVO_ERROR_M( "ServoErrTolerance(metric)=", 25 ),
        ENCODER_CNTS_EN( "EncoderCounts(english)=", 1000 ), ENCODER_CNTS_M( "EncoderCounts(metric)=", 39 ), DRIVE_TYPE( "DriveType=", 0 ), DAC( "DACPolarity=", 0 ), ENCODER_POL( "EncoderPolarity=", 0 ),
        ENCODER_MODE( "EncoderMode=", 2 ), HW_OT( "UseHWOvertravelSwitches=", 1 ), HOME_SW( "UseHomeSwitch=", 1 ), HOME_OT( "HomeToOvertravel=", 1 );

        private final int m_value;
        private final String m_name;

        /**
         * Constructor for enum HYPATH
         * @param param default value for parameters in the axes screen
         */
        private HYPATH( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }

        /**
         * Get/return the integer value of the enum type
         * @return  value of enum
         */
        public int getValue() {
            return m_value;
        }
 
        /**
         * Get/return the String value of the enum type
         * @return  String of enum
         */
        public String getName() {
            return m_name;
        }
    }


    /**
     *
     */
    public enum PICO_PATH {
        PGAIN( "PGain=", 20 ), IGAIN( "IGain=", 0 ), DGAIN( "DGain=", 20 ), FGAIN( "FGain=", 100 ), VGAIN( "VGain=", 0 ), PGAIN_2( "PGain2=", 20 ), IGAIN_2( "IGain2=", 0 ),
        DGAIN_2( "DGain2=", 20 ), FGAIN_2( "FGain2=", 100 ), VGAIN_2( "VGain2=", 0 ), PGAIN_3( "PGain3=", 20 ), IGAIN_3( "IGain3=", 0 ), DGAIN_3( "DGain3=", 20 ),FGAIN_3( "FGain3=", 100 ),
        VGAIN_3( "VGain3=", 0 ), PGAIN_4( "PGain4=", 20 ), IGAIN_4( "IGain4=", 0 ), DGAIN_4( "DGain4=", 20 ), FGAIN_4( "FGain4=", 100 ), VGAIN_4( "VGain4=", 0 ), PGAIN_5( "PGain5=", 20 ),
        IGAIN_5( "IGain5=", 0 ), DGAIN_5( "DGain5=", 20 ), FGAIN_5( "FGain5=", 100 ), VGAIN_5( "VGain5=", 0 ), SERVO_ERROR_EN( "ServoErrTolerance(english)=", 1 ), SERVO_ERROR_M( "ServoErrTolerance(metric)=", 25 ),
        ENCODER_CNTS_EN( "EncoderCounts(english)=", 8192 ), ENCODER_CNTS_M( "EncoderCounts(metric)=", 323 ), DRIVE_TYPE( "DriveType=", 0 ), DAC( "DACPolarity=", 0 ), ENCODER_POL( "EncoderPolarity=", 1 ),
        ENCODER_MODE( "EncoderMode=", 2 ), HW_OT( "UseHWOvertravelSwitches=", 1 ), HOME_SW( "UseHomeSwitch=", 1 ), HOME_OT( "HomeToOvertravel=", 1 );

        private final int m_value;
        private final String m_name;

        /**
         * Constructor for enum HYPATH
         * @param param default value for parameters in the axes screen
         */
        private PICO_PATH( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }

        /**
         * Get/return the integer value of the enum type
         * @return  value of enum
         */
        public int getValue() {
            return m_value;
        }
 
        /**
         * Get/return the String value of the enum type
         * @return  String of enum
         */
        public String getName() {
            return m_name;
        }
    }


    /**
     * Default settings for a CNC containing one or more SensorTHC's
     */
    public enum THC {
        SLIDE_EN( "SlideLength(english)=", 10 ), SLIDE_M( "SlideLength(metric)=", 254 ), HARD_STOP( "UseHardStop=", 0 ), HOMING( "UseHomeSwitch=", 1 ), ANALOG1( "THCAnalog1=", 0 ), ANALOG2( "THCAnalog2=", 2 ),
        SPEEDPOT1_INSTALLED( "SpeedPotInstalled=", 1 ), SPEEDPOT1_ANALOG1( "SpeedPotAnalog1=", 1 ), SPEEDPOT1_ANALOG2( "SpeedPotAnalog2=", 1 );

        private final int m_value;
        private final String m_name;

        private THC( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }

        /**
         * Get/return the integer value of the enum type
         * @return  value of enum type
         */
        public int getValue() {
            return m_value;
        }

        /**
         * Get/return the String value of the enum type
         * @return  String of enum type
         */
        public String getName() {
            return m_name;
        }
    }


    /**
     * Default settings for a CNC containing one or more Bevel axis
     */
    public enum BEVEL {
        SERVO_ERROR( "ServoErrTolerance(degrees)=", 90 ), ENCODER_CNTS( "EncoderCounts(revs)=", 6000 ), AUTO_HOME( "AutoHome=", 1 );

        private final int m_value;
        private final String m_name;

        private BEVEL( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }

        /**
         * Get/return the integer value of the enum type
         * @return  value of enum type
         */
        public int getValue() {
            return m_value;
        }

        /**
         * Get/return the String value of the enum type
         * @return  String of enum type
         */
        public String getName() {
            return m_name;
        }
    }


    /**
     *  Default Inputs assignments required to operate a CNC with the Hypath diagnostic kit.
     */
    public enum SWBOX_INPUT {
        NCS( INPUT_NUM.NCS_1 ), INPUT2( INPUT_NUM.X_NEG_OT ), INPUT3( INPUT_NUM.Y_NEG_OT ), INPUT4( INPUT_NUM.NCS_2 ), DRIVE_DISABLED( INPUT_NUM.DRIVE_DISABLED ), 
        INPUT40( INPUT_NUM.CUT_SENSE_1 ), INPUT41( INPUT_NUM.CUT_SENSE_2 ), INPUT42( INPUT_NUM.CUT_SENSE_3 ), INPUT43( INPUT_NUM.CUT_SENSE_4 );
        
        private final INPUT_NUM m_inputs;
        
        /**
         * Basic inputs used with a Hypath CNC with diagnostic test kits to motion simulation
         * and test I/O.
         * @param inputs 
         */
        private SWBOX_INPUT( INPUT_NUM inputs ) {
            this.m_inputs = inputs;
        }
    }


    /**
     * Default Input assignments required to operate a CNC with the Pico-path diagnostic kit.
     */
    public enum SWBOX_OUTPUT {
        
    }


    /**
     * Adds the default speed settings from the enum SPEED into a EnumMap.
     */
    public void addSpeedDefaults();
    

    /**
     * Adds the default SensorTHC parameters from the enum THC into 3 separate Map
     * containers. One for the THC Axes parameters, one for the Speed parameters,
     * and one for the Machine parameters.
     */
    public void addTHCDefaults();


    /**
     * Adds the default Input settings from the enum INPUT into an EnumMap.
     */
    public void addInputDefaults();


    /**
     * Adds the default Output settings from the enum OUTPUT into an EnumMap.
     */
    public void addOutputDefaults();


    /**
     * Adds the default Bevel Axes settings from the enum BEV_AXES into an EnumMap.
     */
    public void addBevelAxesDefaults();


    /**
     * Adds the default Axis settings for Axis 1, 2, and 6 from the enum AXES into an EnumMap.
     */
    public void addAxesDefaults( String type );


    /**
     * Get/return the specific SensorTHC parameters for the THC Axes screen
     * @return - The SensorTHC THC Axes parameter map
     */
    public Map< String, Integer > getTHCAxisParams();


    /**
     * Get/return the specific SensorTHC parameters within the Machine screen
     * @return  - The SensorTHC Machine parameter Map
     */
    public Map< String, Integer > getTHCMachineParams();


    /**
     * Get/return the specific SensorTHC analog parameters within the Speed screen
     * @return  -   The SensorTHC Analog parameter Map
     */
    public Map< String, Integer > getTHCAnalogParams();


    /**
     * Get/return the specific Axes parameters within the Axes setup screens
     * @return  - The Axes parameter Map
     */
    public Map< String, Integer > getAxesParams();


    /**
     * Get/return the specific Speed parameters within the Speed's setup screen
     * @return  - The Speed parameter Map
     */
    public Map< String, Integer > getSpeedParams();

}
