/**
 * DataAcessObj.java
 * 
 *  Data access object for the SetupConverter application
 * 
 *  The class provides access to data contained in the IDataAccess interface:
 *      *   Adds/Initializes Maps for each container type present in IDataAcess
 *      *   Provides access to the Map variables
 * 
 *  Implements:  IDataAccess
 */
package com.setupconverter.logic;

import com.setupconverter.ui.IComponents.SYSTEM;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DataAccessObj provides data access to the data contained in IDataAccess in the 
 * form of Map's.
 * @author prwallace
 */
public class DataAccessObj implements IDataAcess {
    private final Map< String, Integer > m_speedParamMap;
    private final Map< String, Integer > m_thcParamMap;
    private final Map< String, Integer > m_thcMachineParamMap;
    private final Map< String, Integer > m_thcAnalogParamMap;
    private final Map< String, Integer > m_dualGantryParamMap;
    private Map< String, Integer > m_axesParamMap;


    /**
     * Default constructor.  Initializes all Map's
     */
    public DataAccessObj() {
        this.m_thcMachineParamMap = new LinkedHashMap<>();
        this.m_thcParamMap = new LinkedHashMap<>();
        this.m_speedParamMap = new LinkedHashMap<>();
        this.m_axesParamMap = new LinkedHashMap<>();
        this.m_thcAnalogParamMap = new LinkedHashMap<>();
        this.m_dualGantryParamMap = new LinkedHashMap<>();
    }


    /**
     * Single argument constructor.  Initializes all Maps and add's parameters 
     * to Map's based on the system type (Hypath, Pico-path, and so on).
     * @param type  - The selected system type
     */
    public DataAccessObj( String type ) {
        this.m_thcMachineParamMap = new LinkedHashMap<>();
        this.m_thcParamMap = new LinkedHashMap<>();
        this.m_speedParamMap = new LinkedHashMap<>();
        this.m_axesParamMap = new LinkedHashMap<>();
        this.m_thcAnalogParamMap = new LinkedHashMap<>();
        this.m_dualGantryParamMap = new LinkedHashMap<>();

        addAxesDefaults( type );
        addSpeedDefaults();
        addDualGantryDefaults();
    }


    @Override
    public final void addSpeedDefaults() {
        // Add typical acceleration settings
        m_speedParamMap.put( SPEED.ACCEL_BREAK_1.getName(), SPEED.ACCEL_BREAK_1.getValue() );
        m_speedParamMap.put( SPEED.ACCEL_BREAK_2.getName(), SPEED.ACCEL_BREAK_2.getValue() );
        m_speedParamMap.put( SPEED.ACCEL_BREAK_3.getName(), SPEED.ACCEL_BREAK_3.getValue() );
        m_speedParamMap.put( SPEED.ACCEL_BREAK_4.getName(), SPEED.ACCEL_BREAK_4.getValue() );
        m_speedParamMap.put( SPEED.ACCEL_BREAK_5.getName(), SPEED.ACCEL_BREAK_5.getValue() );

        // Add typical max speed settings
        m_speedParamMap.put( SPEED.MAX_SPEED_EN.getName(), SPEED.MAX_SPEED_EN.getValue() );
        m_speedParamMap.put( SPEED.MAX_SPEED_M.getName(), SPEED.MAX_SPEED_M.getValue() );

        // Add typical speed breaks ranges
        m_speedParamMap.put( SPEED.SPEED_RANGE_1_EN.getName(), SPEED.SPEED_RANGE_1_EN.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_1_M.getName(), SPEED.SPEED_RANGE_1_M.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_2_EN.getName(), SPEED.SPEED_RANGE_2_EN.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_2_M.getName(), SPEED.SPEED_RANGE_2_M.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_3_EN.getName(), SPEED.SPEED_RANGE_3_EN.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_3_M.getName(), SPEED.SPEED_RANGE_3_M.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_4_EN.getName(), SPEED.SPEED_RANGE_4_EN.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_4_M.getName(), SPEED.SPEED_RANGE_4_M.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_5_EN.getName(), SPEED.SPEED_RANGE_5_EN.getValue() );
        m_speedParamMap.put( SPEED.SPEED_RANGE_5_M.getName(), SPEED.SPEED_RANGE_5_M.getValue() );
    }


    @Override
    public void addTHCDefaults() {
        m_thcMachineParamMap.put( THC.ANALOG_1.getName(), THC.ANALOG_1.getValue() );
        m_thcMachineParamMap.put( THC.ANALOG_2.getName(), THC.ANALOG_2.getValue() );

        m_thcParamMap.put( THC.HARD_STOP.getName(), THC.HARD_STOP.getValue() );
        m_thcParamMap.put( THC.HOME_SWITCH.getName(), THC.HOME_SWITCH.getValue() );
        m_thcParamMap.put( THC.SLIDE_EN.getName(), THC.SLIDE_EN.getValue() );
        m_thcParamMap.put( THC.SLIDE_M.getName(), THC.SLIDE_M.getValue() );

        m_thcAnalogParamMap.put( THC.SPEEDPOT_1_INSTALLED.getName(), THC.SPEEDPOT_1_INSTALLED.getValue() );
        m_thcAnalogParamMap.put( THC.SPEEDPOT_1_ANALOG_1.getName(), THC.SPEEDPOT_1_ANALOG_1.getValue() );
        m_thcAnalogParamMap.put( THC.SPEEDPOT_1_ANALOG_2.getName(), THC.SPEEDPOT_1_ANALOG_2.getValue() );
    }


    @Override
    public final void addAxesDefaults( String type ) {      
        if( SYSTEM.BENCH.getName().equals( type )) {
             m_axesParamMap = PICO_PATH.toMap();
        }
        else if( SYSTEM.HYPATH.getName().equals( type )) {
            m_axesParamMap = HYPATH.toMap();
        }   
    }


    @Override
    public final void addDualGantryDefaults() {
        m_dualGantryParamMap.put( DUAL_GANTRY.SKEW_ERROR_EN.getName(), 1 );
        m_dualGantryParamMap.put( DUAL_GANTRY.SKEW_ERROR_M.getName(), 25 );
    }


    @Override
    public Map< String, Integer > getTHCAxisParams() {
        return m_thcParamMap;
    }


    @Override
    public Map< String, Integer > getTHCMachineParams() {
        return m_thcMachineParamMap;
    }


    @Override
    public Map< String, Integer > getTHCAnalogParams() {
        return m_thcAnalogParamMap;
    }


    @Override
    public Map<String, Integer> getAxesParams() {
        return m_axesParamMap;
    }


    @Override
    public Map< String, Integer > getSpeedParams() {
        return m_speedParamMap;
    }


    @Override
    public Map< String, Integer > getDualGantryParams() {
        return m_dualGantryParamMap;
    }
}
