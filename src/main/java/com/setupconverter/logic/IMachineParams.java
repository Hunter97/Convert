/**
 * IMachineParams.java
 * Paul Wallace
 * June 2014
 * 
 * IMachineParams provides the behavior for accessing and setting Machine parameters.
 * Parameters for Machine, Speeds, and I/O blocks within a setup file.  
 *  
 * Main attributes:
 *      * enum's for several Machine parameters 
 *      * Methods to be used to gain access to or set Machine parameters
 *  
 */

package com.setupconverter.logic;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Interface IMachineParams; Interface for machine setup parameters. 
 * @author prwallace
 */
public interface IMachineParams {


    /**
     * Enumeration constants containing titles to several blocks within a setup file.
     */
    public enum BlockTitle {
        MACHINE( "[Machine]\r\n" ), SPEEDS( "[Speeds]\r\n" ), AIC( "[AnalogInputCard]\r\n" ), LINK( "[Link]\r\n" ), IO( "[I/O]\r\n" ), ROTATE( "[Rotate]\r\n" ),
        TILT( "[Tilt]\r\n" ), DUAL_ROTATE( "[DualRotate]\r\n" ), DUAL_TILT( "[DualTilt]\r\n" ), DUAL_GANTRY( "[DualGantry]\r\n" ), THC_1( "[THC1]\r\n" ),
        THC_2( "[THC2]\r\n" ), THC_3( "[THC3]\r\n" ), THC_4( "[THC4]\r\n" ), AXIS_1( "[Axis0]\r\n" ), AXIS_2( "[Axis1]\r\n" ), AXIS_7( "[Axis6]\r\n" ),
        CBH( "[CBH]\r\n" );

        private final String m_name;

        /**
         * Constructor for enum BLOCK
         * @param name  - String value assigned to this enum type
         */
        private BlockTitle( String name ) {
            this.m_name = name;
        }

        /**
         * Get/return the String for this enum type
         * @return  - String value assigned to this enum type
         */
        public String getName() {
            return m_name;
        }
    }


    /**
     * Enumeration constant containing several input assignments.  In a setup file;
     * Input#Number refers to a specific input device and is equal to its input
     * location.
     * i.e. Input47Number=1 => input device 47 is assigned to Input 1
     */
    public enum Input {
        CUT_MARK_SENSE( "Input4Number=", 4 ), DRIVE_DISABLED( "Input10Number=", 10 ), X_HOME_NEG_OT( "Input11Number=", 11 ), X_POS_OT( "Input12Number=", 12 ),
        Y_HOME_NEG_OT( "Input13Number=", 13 ), Y_POS_OT( "Input14Number=", 14 ), CBH_HOME( "Input17Number=", 17 ), X_NEG_OT( "Input19Number=", 19 ), Y_NEG_OT( "Input20Number=", 20 ),
        TILT_POS_OT( "Input35Number=", 35 ), TILT_NEG_OT( "Input36Number=", 36 ), ROTATE_HOME( "Input37Number=", 37 ), THC_AUTO_1( "Input46Number=", 46 ), NCS_1( "Input47Number=", 47 ),
        THC_AUTO_2( "Input51Number=", 51 ), NCS_2( "Input52Number=", 52 ), THC_AUTO_3( "Input56Number=", 56 ), NCS_3( "Input57Number=", 57 ), 
        THC_AUTO_4( "Input61Number=", 61 ), NCS_4( "Input62Number=", 62 ), FUME_SELECT( "Input83Number=", 83 ), TORCH_COLLISION( "Input100Number=", 100 ),
        TEST_LIFTER( "Input102Number=", 102 ), TILT2_POS_OT( "Input103Number=", 103 ), TILT2_NEG_OT( "Input104Number=", 104 ), ROT_2_HOME( "Input105Number=", 105 ),
        CUT_SENSE_1( "Input106Number=", 106 ), CUT_SENSE_2( "Input107Number=", 107 ), CUT_SENSE_3( "Input108Number=", 108 ), CUT_SENSE_4( "Input109Number=", 109 ),
        PARK_HEAD_1( "Input126Number=", 126 ), PARK_HEAD_2( "Input127Number=", 127 ), DUAL_HEAD_COLLISION( "Input130Number=", 130 ), FUME_SENSE( "Input131Number=", 131 ),
        JOYSTICK_UP( "Input133Number=", 133 ), JOYSTICK_DOWN( "Input134Number=", 134 ), JOYSTICK_LEFT( "Input135Number=", 135 ), JOYSTICK_RIGHT( "Input136Number=", 136 ),
        TILT_PLUS( "Input137Number=", 137 ), TILT_MINUS( "Input138Number=", 138 ), ROTATE_PLUS( "Input139Number=", 139 ), ROTATE_MINUS( "Input140Number=", 140 ),
        RDY_TO_FIRE_1( "Input169Number=", 169 ), RDY_TO_FIRE_2( "Input170Number=", 170), RDY_TO_FIRE_3( "Input171Number=", 171 ), RDY_TO_FIRE_4( "Input172Number=", 172 ),
        TILT3_POS_OT( "Input178Number=", 178 ), TILT3_NEG_OT( "Input179Number=", 179 ), TILT4_POS_OT( "Input178Number=", 180 ), TILT4_NEG_OT( "Input179Number=", 181 ), 
        AUTO_SELECT_1( "Input182Number=", 182 ), AUTO_SELECT_2( "Input183Number=", 183 ), AUTO_SELECT_4( "Input185Number=", 185 ), MANUAL_SELECT_1( "Input202Number=", 202 ),
        MANUAL_SELECT_2( "Input203Number=", 203 ), MANUAL_SELECT_3( "Input204Number=", 204), MANUAL_SELECT_4( "Input205Number=", 205 ), RAISE_ALL_TORCHES( "Input282Number=", 282 ),
        AUTO_SELECT_ALL( "Input283Number=", 283 ), MANUAL_IGNITION_SEL( "Input284Number=", 284 ), HIGH_PREHEAT_SEL( "Input285Number=", 285 ), LOW_PREHEAT_SEL( "Input286Number=", 286 ),
        PIERCE_SELECT( "Input287Number=", 287 ), RAISE_TORCH_1( "Input288Number=", 288 ), RAISE_TORCH_2( "Input289Number=", 289 ), RAISE_TORCH_3( "Input290Number=", 290 ),
        RAISE_TORCH_4( "Input292Number=", 292 ), LOWER_TORCH_1( "Input308Number=", 308 ), LOWER_TORCH_2( "Input309Number=", 309 ), LOWER_TORCH_3( "Input310Number=", 310 ),
        LOWER_TORCH_4( "Input311Number=", 311 ), RAISE_TORCH_1A( "Input337Number=", 337 ), LOWER_TORCH_1A( "Input338Number=", 338 ), FP_STOP( "Input356Number=", 356 ),
        FP_START( "Input3579Number=", 357 ), FP_MANUAL( "Input358Number=", 358 ), CUT_SELECT( "Input361Number=", 361 ), LOWER_ALL_TORCHES( "Input362Number=", 362 ),
        FP_BACK_ON_PATH( "Input377Number=", 377 ), FP_FORWARD_ON_PATH( "Input378Number=", 378 ), TOOL_CYCLE_ACTIVE( "Input383Number=", 383 );

        private final int m_value;
        private final String m_name;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum Input
         * @param name  - String value assigned to this enum type
         * @param value - Ordinal value assigned to this enum type
         */
        private Input( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }

        /**
         * Get/return the integer value of this enum
         * @return  - The ordinal value of this enum type
         */
        public int getValue() {
            return m_value;
        }

        /**
         * Get/return the String for this enum type
         * @return  - String value assigned to this enum type
         */
        public String getName() {
            return m_name;
        }

        /**
         * Add all enum constants to a Map< String, Integer > and return the Map.
         * @return  - enum constants as a Map
         */
        public static Map<String, Integer> toMap() {
            for( Input param : Input.values() ) {
                map.put( param.getName(), param.getValue() );
            }
            return map;
        }
    }


    /**
     * Enumeration constant containing several output assignments.  In a setup file;
     * Output#Number refers to a specific output device and is equal to its output
     * location.
     * i.e. Output47Number=1 => output device 47 is assigned to Output 1
     */
    public enum Output {
        THD( "Output4Number=", 4 ), CUT_CONTROL( "Output8Number=", 8 ), DRIVE_ENABLE( "Output55Number=", 55 ), NCE_1( "Output57Number=", 57 ), HOLD_IGN1( "Output58Number=", 58 ),
        CUT_CONTROL_1( "Output197Number=", 197 ), CUT_CONTROL_2( "Output198Number=", 198 ), CUT_CONTROL_3( "Output198Number=", 198 ), CUT_CONTROL_4( "Output198Number=", 198 ),
        STATION_ENABLE_LED_1( "Output217Number=", 217 ), STATION_ENABLE_LED_2( "Output218Number=", 218 ), VENT_1( "Output472Number=", 472 ), VENT_2( "Output473Number=", 473 ),
        VENT_3( "Output474Number=", 474 ), VENT_4( "Output475Number=", 475 ), VENT_5( "Output476Number=", 476 ), THC_TorqueLimit( "Output523Number=", 523 );

        private final int m_value;
        private final String m_name;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum OUTPUT_NUM
         * @param name  - String value assigned to this enum type
         * @param value - Ordinal value assigned to this enum type
         */
        private Output( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }

        /**
         * Get/return the integer value of this enum
         * @return  - The ordinal value of this enum type
         */
        public int getValue() {
            return m_value;
        }

        /**
         * Get/return the String for this enum type
         * @return - String value assigned to this enum type
         */
        public String getName() {
            return m_name;
        }

        /**
         * Add all enum constants to a Map< String, Integer > and return the Map.
         * @return  - enum constants as a Map
         */
        public Map<String, Integer> toMap() {
            for( Output param : Output.values() ) {
                map.put( param.getName(), param.getValue() );
            }

            return map;
        }
    }


    /**
     * Enumeration constants containing default Speed settings for all drive types
     */
    public enum Speed {
        MAX_SPEED_EN( "MaxSpeed(english)=", 1000 ), MAX_SPEED_M( "MaxSpeed(metric)=", 25400 ), SPEED_RANGE_1_EN( "GainSpeed1(english)=", 100 ),
        SPEED_RANGE_1_M( "GainSpeed1(metric)=", 2540 ), SPEED_RANGE_2_EN( "GainSpeed2(english)=", 300 ), SPEED_RANGE_2_M( "GainSpeed2(metric)=", 7620 ),
        SPEED_RANGE_3_EN( "GainSpeed3(english)=", 500 ), SPEED_RANGE_3_M( "GainSpeed3(metric)=", 12700 ), SPEED_RANGE_4_EN( "GainSpeed4(english)=", 1000 ),
        SPEED_RANGE_4_M( "GainSpeed4(metric)=", 25400 ), SPEED_RANGE_5_EN( "GainSpeed5(english)=", 1000 ), SPEED_RANGE_5_M( "GainSpeed5(metric)=", 25400 ),
        ACCEL_BREAK_1( "AccelRate=", 20 ), ACCEL_BREAK_2( "AccelRate2=", 40 ), ACCEL_BREAK_3( "AccelRate3=", 50 ), ACCEL_BREAK_4( "AccelRate4=", 60 ),
        ACCEL_BREAK_5( "AccelRate5=", 60 );

        private final int m_value;
        private final String m_name;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum SPEED
         * @param name  - String value assigned to this enum type
         * @param value - Ordinal value assigned to this enum type
         */
        private Speed( String name, int value ) {
            this.m_value = value;
            this.m_name = name;
        }



        public int getValue() {
            return m_value;
        }


        public String getName() {
            return m_name;
        }

        /**
         * Add all enum constants to a Map< String, Integer > and return the Map.
         * @return  - enum constants as a Map
         */
        public static Map< String, Integer > toMap() {
            for( Speed param : Speed.values() ) {
                map.put( param.getName(), param.getValue() );
            }

            return map;
        }
    }


    /**
     * Enumeration constant containing default Machine parameters.  Can be used to
     * determine the application type(i.e. Bevel on Pipe) as well as available
     * features (i.e. Homes to over-travels) of the cutting machine.
     */
    public enum Machine {
        DUAL_GANTRY( "DualGantryInstalled=" ), FP( "FrontPanelInstalled=" ), SERCOS( "SercosSensorUtility=" ), ARC_GLIDE( "ArcGlideTHCInstalled=" ),
        PSCOMM_HYPERNET( "PSCommOverHypNetEn" ), STHC( "SensorTHCInstalled=" ), BEVEL_AXES( "SkewRotatorInstalled=" ), DUAL_BEVEL( "DualSkewRotatorInstalled=" ),
        AUTO_HOME( "AutoHome=" ), DUAL_TRANS( "DualTransverseInstalled=" ), NO_ROTATE_TILT( "NoRotateTilt=" ), ONE_ROTATE_TILT( "OneRotateTilt=" ),
        ROTATING_TRANS( "RotatingTransverse=" ), X_AXIS_ORIENTATION( "XAxisOrientation=" ), DUAL_TILTING( "DualTiltMode=" ), CHECK_SUM( "Checksum=" ),
        SERVO_ERROR_EN( "ServoErrTolerance(english)=" ), SERVO_ERROR_M( "ServoErrTolerance(metric)=" ), ENCODER_CNTS_EN( "EncoderCounts(english)=" ),
        ENCODER_CNTS_M( "EncoderCounts(metric)=" ), CBH( "CBHInstalled=" ), HOME_DIRECTION( "HomeDirection=" ), HOME_TO_OT( "HomeToOvertravel=" );

        private final String m_name;

        /**
         * Constructor for enum PARAMETER
         * @param name  - String value assigned to this enum type
         */
        private Machine( String name ) {
            this.m_name = name;
        }

        /**
         * Get/return the String for this enum type
         * @return  - String representation of this enum type
         */
        public String getName() {
            return m_name;
        }
    }


    /**
     * Enumeration constant containing Axes parameters
     */
    public enum Axes implements IParametersEnum {
        PGAIN( "PGain=" ), IGAIN( "IGain=" ), DGAIN( "DGain=" ), FGAIN( "FGain=" ), VGAIN( "VGain=" ), PGAIN_2( "PGain2=" ), IGAIN_2( "IGain2=" ),
        DGAIN_2( "DGain2=" ), FGAIN_2( "FGain2=" ), VGAIN_2( "VGain2=" ), PGAIN_3( "PGain3=" ), IGAIN_3( "IGain3=" ), DGAIN_3( "DGain3=" ),
        FGAIN_3( "FGain3=" ), VGAIN_3( "VGain3=" ), PGAIN_4( "PGain4=" ), IGAIN_4( "IGain4=" ), DGAIN_4( "DGain4=" ), FGAIN_4( "FGain4=" ),
        VGAIN_4( "VGain4=" ), PGAIN_5( "PGain5=" ), IGAIN_5( "IGain5=" ), DGAIN_5( "DGain5=" ), FGAIN_5( "FGain5=" ), VGAIN_5( "VGain5=" ),
        SERVO_ERROR_EN( "ServoErrTolerance(english)=" ), SERVO_ERROR_M( "ServoErrTolerance(metric)=" ), ENCODER_CNTS_EN( "EncoderCounts(english)=" ),
        ENCODER_CNTS_M( "EncoderCounts(metric)=" ), DRIVE_TYPE( "DriveType=" ), DAC( "DACPolarity=" ), ENCODER_POLARITY( "EncoderPolarity=" ),
        ENCODER_MODE( "EncoderMode=" ), USE_HW_OT( "UseHWOvertravelSwitches=" ), HOME_SW( "UseHomeSwitch=" ), HOME_OT( "HomeToOvertravel=" ),
        MARKER_PULSE( "UseMarker=" );

        private final String m_name;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum HYPATH
         * @param name  - String value assigned to this enum type
         */
        private Axes( String name ) {
            this.m_name = name;
        }


        @Override
        public int getValue() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getName() {
            return m_name;
        }
    }


    /**
     * Enumeration constant containing default settings for a CNC containing one
     * or more SensorTHC's.
     */
    public enum THC implements IParametersEnum {
        SLIDE_EN( "SlideLength(english)=", 10 ), SLIDE_M( "SlideLength(metric)=", 254 ), HARD_STOP( "UseHardStop=", 0 ), HOME_SWITCH( "UseHomeSwitch=", 1 ),
        ANALOG_1( "THCAnalog1=", 0 ), ANALOG_2( "THCAnalog2=", 2 ), SPEEDPOT_1_INSTALLED( "SpeedPotInstalled=", 1 ), SPEEDPOT_1_ANALOG_1( "SpeedPotAnalog1=", 1 ),
        SPEEDPOT_1_ANALOG_2( "SpeedPotAnalog2=", 1 );

        private final int m_value;
        private final String m_name;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum THC
         * @param name  - String value of this enum type
         * @param value - Ordinal value of this enum type
         */
        private THC( String name, int value ) {
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
            for( THC param : THC.values() ) {
                map.put( param.getName(), param.getValue() );
            }
            
            return map;
        }
    }


    /**
     * Enumeration constant containing default settings for a CNC containing a 
     * Dual Gantry Axis.
     */
    public enum DualGantry implements IParametersEnum {
        SKEW_ERROR_EN( "SkewErrTolerance(english)=", 1 ), SKEW_ERROR_M( "SkewErrTolerance(metric)=", 25 );

        private final int m_value;
        private final String m_name;
        private static final Map< String, Integer > map = new LinkedHashMap<>();

        /**
         * Constructor for enum DUAL_GANTRY
         * @param name  - String value for this enum type
         * @param value - Ordinal value for this enum type
         */
        private DualGantry( String name, int value ) {
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
            for( DualGantry param : DualGantry.values() ) {
                map.put( param.getName(), param.getValue() );
            }
            
            return map;
        }
    }


    /**
     * Enumeration constant containing default settings for a Bevel axis
     */
    public enum Bevel implements IParametersEnum {
        SERVO_ERROR( "ServoErrTolerance(degrees)=", 90 ), ENCODER_CNTS( "EncoderCounts(revs)=", 6000 ), AUTO_HOME( "AutoHome=", 1 );

        private final int m_value;
        private final String m_name;

        /**
         * Constructor for the enum BEVEL
         * @param name  - String value of this enum type
         * @param value - Ordinal value of this enum type
         */
        private Bevel( String name, int value ) {
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
    }


    /**
     * Adds the enum THC into 3 separate Map's. One for the THC Axes parameters,
     * one for the THC Speed parameters, and one for the THC Machine parameters.
     */
    public void addTHCDefaults();


    /**
     * Instantiates a DataAccessObj based on the system type, adds the enum
     * parameters for the system type to a Map, and assigns the location of I/O,
     * like cut sense, that is specific to the system type.
     * @param type  - The system type to convert the configuration file
     */
    public void addAxesDefaults( String type );


    /**
     * Get/return the SensorTHC Axis Map
     * @return - The SensorTHC THC Axes parameter Map
     */
    public Map< String, Integer > getTHCAxisParams();


    /**
     * Get/return the SensorTHC Machine Map
     * @return  - The SensorTHC Machine parameter Map
     */
    public Map< String, Integer > getTHCMachineParams();


    /**
     * Get/return the SensorTHC Analog Map
     * @return  -   The SensorTHC Analog parameter Map
     */
    public Map< String, Integer > getTHCAnalogParams();


    /**
     * Get/return the Axes parameters Map
     * @return  - The Axes parameter Map
     */
    public Map< String, Integer > getAxesParams();


    /**
     * Get/return the input location of the first cut sense input
     * @return  - The location of cut sense 1 or cut/mark sense
     */
    public int getCutSenseLoc();


    /**
     * Get/return the output location of the first cut control output
     * @return  - The location of the cut control 1 or cut control output
     */
    public int getCutControlLoc();


    /**
     * Get/return the output location of the THC Torque Limit output
     * @return  - The location of the THC Torque Limit output
     */
    public int getTHCTorqueLimitLoc();
}
