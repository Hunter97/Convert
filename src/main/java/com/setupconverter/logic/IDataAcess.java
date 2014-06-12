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
     * Input number enum; represents all the inputs that can possible be used with this API.
     */
    public enum INPUT_NUM {
        DRIVE_DISABLED( "Input10Number=", 10 ), X_NEG_OT( "Input11Number=", 11 ), X_POS_OT( "Input12Number=", 12 ), Y_NEG_OT( "Input13Number=", 13 ), Y_POS_OT( "Input14Number=", 14 ),
        TILT_POS_OT( "Input35Number=", 35 ), TILT_NEG_OT( "Input36Number=", 36 ), THC_AUTO_1( "Input46Number=", 46 ), NCS_1( "Input47Number=", 47 ), THC_AUTO_2( "Input51Number=", 51 ),
        NCS_2( "Input52Number=", 52 ), THC_AUTO_3( "Input56Number=", 56 ), NCS_3( "Input57Number=", 57 ), THC_AUTO_4( "Input61Number=", 61 ), NCS_4( "Input62Number=", 62 ),
        FUME_SELECT( "Input83Number=", 83 ), TORCH_COLLISION( "Input100Number=", 100 ), TEST_LIFTER( "Input102Number=", 102 ), TILT2_POS_OT( "Input103Number=", 103 ),
        TILT2_NEG_OT( "Input104Number=", 104 ), ROT_2_HOME( "Input105Number=", 105 ), CUT_SENSE_1( "Input106Number=", 106 ), CUT_SENSE_2( "Input107Number=", 107 ), 
        CUT_SENSE_3( "Input108Number=", 108 ), CUT_SENSE_4( "Input109Number=", 109 ), PARK_HEAD_1( "Input126Number=", 126 ), PARK_HEAD_2( "Input127Number=", 127 ),
        DUAL_HEAD_COLLISION( "Input130Number=", 130 ), FUME_SENSE( "Input131Number=", 131 ), JOYSTICK_UP( "Input133Number=", 133 ), JOYSTICK_DOWN( "Input134Number=", 134 ),
        JOYSTICK_LEFT( "Input135Number=", 135 ), JOYSTICK_RIGHT( "Input136Number=", 136 ), TILT_PLUS( "Input137Number=", 137 ), TILT_MINUS( "Input138Number=", 138 ),
        ROTATE_PLUS( "Input139Number=", 139 ), ROTATE_MINUS( "Input140Number=", 140 ), RDY_TO_FIRE_1( "Input169Number=", 169 ), RDY_TO_FIRE_2( "Input170Number=", 170),
        RDY_TO_FIRE_3( "Input171Number=", 171 ), RDY_TO_FIRE_4( "Input172Number=", 172 ), AUTO_SELECT_1( "Input182Number=", 182 ), AUTO_SELECT_2( "Input183Number=", 183 ),
        AUTO_SELECT_4( "Input185Number=", 185 ), MANUAL_SELECT_1( "Input202Number=", 202 ), MANUAL_SELECT_2( "Input203Number=", 203 ), MANUAL_SELECT_3( "Input204Number=", 204),
        MANUAL_SELECT_4( "Input205Number=", 205 ), RAISE_ALL_TORCHES( "Input282Number=", 282 ), AUTO_SELECT_ALL( "Input283Number=", 283 ), MANUAL_IGNITION_SEL( "Input284Number=", 284 ),
        HIGH_PREHEAT_SEL( "Input285Number=", 285 ), LOW_PREHEAT_SEL( "Input286Number=", 286 ), PIERCE_SELECT( "Input287Number=", 287 ), RAISE_TORCH_1( "Input288Number=", 288 ),
        RAISE_TORCH_2( "Input289Number=", 289 ), RAISE_TORCH_3( "Input290Number=", 290 ), RAISE_TORCH_4( "Input292Number=", 292 ), LOWER_TORCH_1( "Input308Number=", 308 ),
        LOWER_TORCH_2( "Input309Number=", 309 ), LOWER_TORCH_3( "Input310Number=", 310 ), LOWER_TORCH_4( "Input311Number=", 311 ), RAISE_TORCH_1A( "Input337Number=", 337 ),
        LOWER_TORCH_1A( "Input338Number=", 338 ), FP_STOP( "Input356Number=", 356 ), FP_START( "Input3579Number=", 357 ), FP_MANUAL( "Input358Number=", 358 ),
        CUT_SELECT( "Input361Number=", 361 ), LOWER_ALL_TORCHES( "Input362Number=", 362 ), FP_BACK_ON_PATH( "Input377Number=", 377 ), FP_FORWARD_ON_PATH( "Input378Number=", 378 ),
        TOOL_CYCLE_ACTIVE( "Input383Number=", 383 );

        private final int m_value;
        private final String m_name;

        private INPUT_NUM( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }

        /**
         * Get/return the input number, the integer value represented by the input
         * @return  - Value of the input
         */
        public int getValue() {
            return m_value;
        }

        /**
         * Get/return the String representation of the input
         * @return  - String representation of the input (i.e. "Input10Number="
         */
        public String getName() {
            return m_name;
        }
    }


    /**
     * Input assignment enum; represent physical input the INPUT_NUM was assigned
     * too (DriveDisabled assigned to input 1 is equivalent to: Input1Type=10)
     */
    public enum INPUT_TYPE {
        INPUT_1( "Input1Type=" ), INPUT_2( "Input2Type=" ), INPUT_3( "Input3Type=" ), INPUT_4( "Input4Type=" ), INPUT_5( "Input5Type=" ), INPUT_6( "Input6Type=" ), INPUT_7( "Input7Type=" ),
        INPUT_8( "Input8Type=" ), INPUT_9( "Input9Type=" ), INPUT_10( "Input10Type=" ), INPUT_11( "Input11Type=" ), INPUT_12( "Input12Type=" ), INPUT_13( "Input13Type=" ),
        INPUT_14( "Input14Type=" ), INPUT_15( "Input15Type=" ), INPUT_16( "Input16Type=" ), INPUT_17( "Input17Type=" ), INPUT_18( "Input18Type=" ), INPUT_19( "Input19Type=" ),
        INPUT_20( "Input20Type=" ), INPUT_21( "Input21Type=" ), INPUT_22( "Input22Type=" ), INPUT_40( "Input40Type=" ), INPUT_41( "Input41Type=" ), INPUT_42( "Input42Type=" ),
        INPUT_43( "Input43Type=" ), INPUT_44( "Input44Type=" ), INPUT_129( "Input129Type=" ), INPUT_130( "Input130Type=" ), INPUT_131( "Input131Type=" ), INPUT_132( "Input132Type=" ),
        INPUIT_133( "Input133Type=" ), INPUT_134( "Input134Type=" ), INPUT_135( "Input135Type=" ), INPUT_136( "Input136Type=" ), INPUT_137( "Input137Type=" ), INPUT_138( "Input138Type=" ),
        INPUT_139( "Input139Type=" ), INPUT_140( "Input140Type=" ), INPUT_141( "Input141Type=" ), INPUT_142( "Input142Type=" ), INPUT_143( "Input143Type=" ), INPUT_144( "Input144Type=" ),
        INPUT_145( "Input145Type=" );

        private final String m_name;

        private INPUT_TYPE( String name ) {
            this.m_name = name;
        }

        /**
         * Get/return the String associated with the enum
         * @return  String equivalent of the enum
         */
        public String getName() {
            return m_name;
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
