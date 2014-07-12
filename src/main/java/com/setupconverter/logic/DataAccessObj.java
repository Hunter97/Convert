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
    private final Map< String, Integer > m_thcParamMap;
    private final Map< String, Integer > m_thcMachineParamMap;
    private final Map< String, Integer > m_thcAnalogParamMap;
    private Map< String, Integer > m_axesParamMap;


    /**
     * Default constructor.  Initializes all Map's
     */
    public DataAccessObj() {
        this.m_thcMachineParamMap = new LinkedHashMap<>();
        this.m_thcParamMap = new LinkedHashMap<>();
        this.m_thcAnalogParamMap = new LinkedHashMap<>();
        this.m_axesParamMap = new LinkedHashMap<>();
    }


    /**
     * Single argument constructor.  Initializes all Maps and add's parameters 
     * to Map's based on the system type (Hypath, Pico-path, and so on).
     * @param type  - The selected system type
     */
    public DataAccessObj( String type ) {
        this.m_thcMachineParamMap = new LinkedHashMap<>();
        this.m_thcParamMap = new LinkedHashMap<>();
        this.m_thcAnalogParamMap = new LinkedHashMap<>();
        this.m_axesParamMap = new LinkedHashMap<>();
        addAxesDefaults( type );
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
}
