/**
 * DataAcessObj.java
 * 
 *  Data access object for the SetupConverter application
 * 
 *  The class provides data access to that data contained in IMachineType and
 *  IMachineParams interfaces:
 *      *   Initializes Maps of specific parameters based on the machine type
 *      *   Provides access to the individual Maps
 * 
 *  Implements:  IDataAccess
 */
package com.setupconverter.logic;

import com.setupconverter.ui.IComponents.DriveType;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * DataAccessObj provides data access to the data contained in IDataAccess in the 
 * form of Map's.
 * @author prwallace
 */
public class DataAccessObj implements IMachineParams {
    private Map< String, Integer > m_axesParamMap;
    private Map< String, Integer > m_thcAxesParamMap;
    private final Map< String, Integer > m_thcMachineParamMap;
    private final Map< String, Integer > m_thcAnalogParamMap;

    private int m_cutSenseLoc;
    private int m_cutControlLoc;
    private int m_thcTorqueLimitLoc = 19;

    private boolean m_isEDGETi = false;


    /**
     * Default constructor.  Initializes all Map's
     */
    public DataAccessObj() {
        this.m_thcMachineParamMap = new LinkedHashMap<>();
        this.m_thcAxesParamMap = new LinkedHashMap<>();
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
        this.m_thcAxesParamMap = new LinkedHashMap<>();
        this.m_thcAnalogParamMap = new LinkedHashMap<>();
        this.m_axesParamMap = new LinkedHashMap<>();
        addAxesDefaults( type );
    }


    @Override
    public void addTHCDefaults() {
        m_thcMachineParamMap.put( THC.ANALOG_1.getName(), THC.ANALOG_1.getValue() );
        m_thcMachineParamMap.put( THC.ANALOG_2.getName(), THC.ANALOG_2.getValue() );

        if( !m_isEDGETi ) {
            m_thcAxesParamMap.put( THC.HARD_STOP.getName(), THC.HARD_STOP.getValue() );
            m_thcAxesParamMap.put( THC.HOME_SWITCH.getName(), THC.HOME_SWITCH.getValue() );
            m_thcAxesParamMap.put( THC.SLIDE_EN.getName(), THC.SLIDE_EN.getValue() );
            m_thcAxesParamMap.put( THC.SLIDE_M.getName(), THC.SLIDE_M.getValue() );
        }

        m_thcAnalogParamMap.put( THC.SPEEDPOT_1_INSTALLED.getName(), THC.SPEEDPOT_1_INSTALLED.getValue() );
        m_thcAnalogParamMap.put( THC.SPEEDPOT_1_ANALOG_1.getName(), THC.SPEEDPOT_1_ANALOG_1.getValue() );
        m_thcAnalogParamMap.put( THC.SPEEDPOT_1_ANALOG_2.getName(), THC.SPEEDPOT_1_ANALOG_2.getValue() );
    }


    @Override
    public final void addAxesDefaults( String type ) {      
        if( DriveType.YASKAWA.getName().equals( type )) {
            m_axesParamMap = m_thcAxesParamMap = IMachineType.Bench.toMap();
            m_cutSenseLoc = 13;
            m_cutControlLoc = 18;
        }
        else if( DriveType.DIAG_BRDS.getName().equals( type )) {
            m_axesParamMap = m_thcAxesParamMap = IMachineType.DiagBrds1.toMap();
            m_cutSenseLoc = 40;
            m_cutControlLoc = 40;
        }
        else if( DriveType.EDGETI.getName().equals( type )) {
            m_axesParamMap = IMachineType.EdgeProTi.toMap();
            m_thcAxesParamMap = IMachineType.Ti_Lifter.toMap();
            m_cutSenseLoc = 1;
            m_isEDGETi = true;
        }
    }


    @Override
    public Map< String, Integer > getTHCAxisParams() {
        return m_thcAxesParamMap;
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

    public Map<String, Integer> getDualGantryParams() {
        return IMachineParams.DualGantry.toMap();
    }

    @Override
    public int getCutSenseLoc() {
        return m_cutSenseLoc;
    }

    @Override
    public int getCutControlLoc() {
        return m_cutControlLoc;
    }

    @Override
    public int getTHCTorqueLimitLoc() {
        return m_thcTorqueLimitLoc;
    }

    @Override
    public boolean isEDGETi() {
        return m_isEDGETi;
    }
}
