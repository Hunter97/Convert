/**
 * CNCType.java
 * 
 *  Container class for ConvertLogic.java
 * 
 *  The Class provides a class that:
 *      *   Holds standard parameters in an EnumMap
 *      *   Holds specific CNC type parameters in an EnumMap
 *      *   Provides access to EnumMap's through get calls
 * 
 *  Implements:  ICNCTypes
 */
package com.setupconverter.logic;

import com.setupconverter.ui.IComponents.SYSTEM;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author prwallace
 */
public class DataAccessObj implements IDataAcess {
    private final Map< String, Integer > m_speedDefaults;
    private final Map< String, Integer > m_thcAxisDefaults;
    private final Map< String, Integer > m_thcMachineDefaults;
    private final Map< String, Integer > m_thcAnalogDefaults;
    private Map< String, Integer > m_axesDefaults;


    /**
     * Default constructor.  Initializes all Map's
     */
    public DataAccessObj() {
        this.m_thcMachineDefaults = new LinkedHashMap<>();
        this.m_thcAxisDefaults = new LinkedHashMap<>();
        this.m_speedDefaults = new LinkedHashMap<>();
        this.m_axesDefaults = new LinkedHashMap<>();
        this.m_thcAnalogDefaults = new LinkedHashMap<>();
    }


    /**
     * Single argument constructor.  Initializes all Maps and add's parameters 
     * to Map's based on the system type (Hypath, Pico-path, and so on).
     * @param type
     */
    public DataAccessObj( String type ) {
        this.m_thcMachineDefaults = new LinkedHashMap<>();
        this.m_thcAxisDefaults = new LinkedHashMap<>();
        this.m_speedDefaults = new LinkedHashMap<>();
        this.m_axesDefaults = new LinkedHashMap<>();
        this.m_thcAnalogDefaults = new LinkedHashMap<>();

        addAxesDefaults( type );
        addSpeedDefaults();
    }


    /**
     * Add the default Hypath gain settings from enum HYPATH to a map
     * @return - The Map containing the gains setting for a Hypath interface
     */
    private Map< String, Integer > addHypathDefaults() {
        final Map< String, Integer > hypathDefaults = new LinkedHashMap<>();

        hypathDefaults.put( HYPATH.PGAIN.getName(), HYPATH.PGAIN.getValue() );
        hypathDefaults.put( HYPATH.IGAIN.getName(), HYPATH.IGAIN.getValue() );
        hypathDefaults.put( HYPATH.DGAIN.getName(), HYPATH.DGAIN.getValue() );
        hypathDefaults.put( HYPATH.FGAIN.getName(), HYPATH.FGAIN.getValue() );
        hypathDefaults.put( HYPATH.VGAIN.getName(), HYPATH.VGAIN.getValue() );
        hypathDefaults.put( HYPATH.PGAIN_2.getName(), HYPATH.PGAIN_2.getValue() );
        hypathDefaults.put( HYPATH.IGAIN_2.getName(), HYPATH.IGAIN_2.getValue() );
        hypathDefaults.put( HYPATH.DGAIN_2.getName(), HYPATH.DGAIN_2.getValue() );
        hypathDefaults.put( HYPATH.FGAIN_2.getName(), HYPATH.FGAIN_2.getValue() );
        hypathDefaults.put( HYPATH.VGAIN_2.getName(), HYPATH.VGAIN_2.getValue() );
        hypathDefaults.put( HYPATH.PGAIN_3.getName(), HYPATH.PGAIN_3.getValue() );
        hypathDefaults.put( HYPATH.IGAIN_3.getName(), HYPATH.IGAIN_3.getValue() );
        hypathDefaults.put( HYPATH.DGAIN_3.getName(), HYPATH.DGAIN_3.getValue() );
        hypathDefaults.put( HYPATH.FGAIN_3.getName(), HYPATH.FGAIN_3.getValue() );
        hypathDefaults.put( HYPATH.VGAIN_3.getName(), HYPATH.VGAIN_3.getValue() );
        hypathDefaults.put( HYPATH.PGAIN_4.getName(), HYPATH.PGAIN_4.getValue() );
        hypathDefaults.put( HYPATH.IGAIN_4.getName(), HYPATH.IGAIN_4.getValue() );
        hypathDefaults.put( HYPATH.DGAIN_4.getName(), HYPATH.DGAIN_4.getValue() );
        hypathDefaults.put( HYPATH.FGAIN_4.getName(), HYPATH.FGAIN_4.getValue() );
        hypathDefaults.put( HYPATH.VGAIN_4.getName(), HYPATH.VGAIN_4.getValue() );
        hypathDefaults.put( HYPATH.PGAIN_5.getName(), HYPATH.PGAIN_5.getValue() );
        hypathDefaults.put( HYPATH.IGAIN_5.getName(), HYPATH.IGAIN_5.getValue() );
        hypathDefaults.put( HYPATH.DGAIN_5.getName(), HYPATH.DGAIN_5.getValue() );
        hypathDefaults.put( HYPATH.FGAIN_5.getName(), HYPATH.FGAIN_5.getValue() );
        hypathDefaults.put( HYPATH.VGAIN_5.getName(), HYPATH.VGAIN_5.getValue() );
        hypathDefaults.put( HYPATH.DAC.getName(), HYPATH.DAC.getValue() );
        hypathDefaults.put( HYPATH.DRIVE_TYPE.getName(), HYPATH.DRIVE_TYPE.getValue() );
        hypathDefaults.put( HYPATH.ENCODER_CNTS_EN.getName(), HYPATH.ENCODER_CNTS_EN.getValue() );
        hypathDefaults.put( HYPATH.ENCODER_CNTS_M.getName(), HYPATH.ENCODER_CNTS_M.getValue() );
        hypathDefaults.put( HYPATH.ENCODER_MODE.getName(), HYPATH.ENCODER_MODE.getValue() );
        hypathDefaults.put( HYPATH.ENCODER_POL.getName(), HYPATH.ENCODER_POL.getValue() );
        hypathDefaults.put( HYPATH.HOME_OT.getName(), HYPATH.HOME_OT.getValue() );
        hypathDefaults.put( HYPATH.HOME_SW.getName(), HYPATH.HOME_SW.getValue() );
        hypathDefaults.put( HYPATH.HW_OT.getName(), HYPATH.HW_OT.getValue() );
        hypathDefaults.put( HYPATH.SERVO_ERROR_EN.getName(), HYPATH.SERVO_ERROR_EN.getValue() );
        hypathDefaults.put( HYPATH.SERVO_ERROR_M.getName(), HYPATH.SERVO_ERROR_M.getValue() );

        return hypathDefaults;
    }


    /**
     * Add the default Pico-path gain settings from enum PICO_PATH to a map
     * @return - The Map containing the gains setting for a Pico-path interface
     */
    private Map< String, Integer > addPicoPathDefaults() {
        final Map< String, Integer > picoDefaults = new LinkedHashMap<>();

        picoDefaults.put( PICO_PATH.PGAIN.getName(), PICO_PATH.PGAIN.getValue() );
        picoDefaults.put( PICO_PATH.IGAIN.getName(), PICO_PATH.IGAIN.getValue() );
        picoDefaults.put( PICO_PATH.DGAIN.getName(), PICO_PATH.DGAIN.getValue() );
        picoDefaults.put( PICO_PATH.FGAIN.getName(), PICO_PATH.FGAIN.getValue() );
        picoDefaults.put( PICO_PATH.VGAIN.getName(), PICO_PATH.VGAIN.getValue() );
        picoDefaults.put( PICO_PATH.PGAIN_2.getName(), PICO_PATH.PGAIN_2.getValue() );
        picoDefaults.put( PICO_PATH.IGAIN_2.getName(), PICO_PATH.IGAIN_2.getValue() );
        picoDefaults.put( PICO_PATH.DGAIN_2.getName(), PICO_PATH.DGAIN_2.getValue() );
        picoDefaults.put( PICO_PATH.FGAIN_2.getName(), PICO_PATH.FGAIN_2.getValue() );
        picoDefaults.put( PICO_PATH.VGAIN_2.getName(), PICO_PATH.VGAIN_2.getValue() );
        picoDefaults.put( PICO_PATH.PGAIN_3.getName(), PICO_PATH.PGAIN_3.getValue() );
        picoDefaults.put( PICO_PATH.IGAIN_3.getName(), PICO_PATH.IGAIN_3.getValue() );
        picoDefaults.put( PICO_PATH.DGAIN_3.getName(), PICO_PATH.DGAIN_3.getValue() );
        picoDefaults.put( PICO_PATH.FGAIN_3.getName(), PICO_PATH.FGAIN_3.getValue() );
        picoDefaults.put( PICO_PATH.VGAIN_3.getName(), PICO_PATH.VGAIN_3.getValue() );
        picoDefaults.put( PICO_PATH.PGAIN_4.getName(), PICO_PATH.PGAIN_4.getValue() );
        picoDefaults.put( PICO_PATH.IGAIN_4.getName(), PICO_PATH.IGAIN_4.getValue() );
        picoDefaults.put( PICO_PATH.DGAIN_4.getName(), PICO_PATH.DGAIN_4.getValue() );
        picoDefaults.put( PICO_PATH.FGAIN_4.getName(), PICO_PATH.FGAIN_4.getValue() );
        picoDefaults.put( PICO_PATH.VGAIN_4.getName(), PICO_PATH.VGAIN_4.getValue() );
        picoDefaults.put( PICO_PATH.PGAIN_5.getName(), PICO_PATH.PGAIN_5.getValue() );
        picoDefaults.put( PICO_PATH.IGAIN_5.getName(), PICO_PATH.IGAIN_5.getValue() );
        picoDefaults.put( PICO_PATH.DGAIN_5.getName(), PICO_PATH.DGAIN_5.getValue() );
        picoDefaults.put( PICO_PATH.FGAIN_5.getName(), PICO_PATH.FGAIN_5.getValue() );
        picoDefaults.put( PICO_PATH.VGAIN_5.getName(), PICO_PATH.VGAIN_5.getValue() );
        picoDefaults.put( PICO_PATH.DAC.getName(), PICO_PATH.DAC.getValue() );
        picoDefaults.put( PICO_PATH.DRIVE_TYPE.getName(), PICO_PATH.DRIVE_TYPE.getValue() );
        picoDefaults.put( PICO_PATH.ENCODER_CNTS_EN.getName(), PICO_PATH.ENCODER_CNTS_EN.getValue() );
        picoDefaults.put( PICO_PATH.ENCODER_CNTS_M.getName(), PICO_PATH.ENCODER_CNTS_M.getValue() );
        picoDefaults.put( PICO_PATH.ENCODER_MODE.getName(), PICO_PATH.ENCODER_MODE.getValue() );
        picoDefaults.put( PICO_PATH.ENCODER_POL.getName(), PICO_PATH.ENCODER_POL.getValue() );
        picoDefaults.put( PICO_PATH.HOME_OT.getName(), PICO_PATH.HOME_OT.getValue() );
        picoDefaults.put( PICO_PATH.HOME_SW.getName(), PICO_PATH.HOME_SW.getValue() );
        picoDefaults.put( PICO_PATH.HW_OT.getName(), PICO_PATH.HW_OT.getValue() );
        picoDefaults.put( PICO_PATH.SERVO_ERROR_EN.getName(), PICO_PATH.SERVO_ERROR_EN.getValue() );
        picoDefaults.put( PICO_PATH.SERVO_ERROR_M.getName(), PICO_PATH.SERVO_ERROR_M.getValue() );

        return picoDefaults;
    }


    @Override
    public final void addSpeedDefaults() {
        // Add typical acceleration settings
        m_speedDefaults.put( SPEED.ACCEL1.getName(), SPEED.ACCEL1.getValue() );
        m_speedDefaults.put( SPEED.ACCEL2.getName(), SPEED.ACCEL2.getValue() );
        m_speedDefaults.put( SPEED.ACCEL3.getName(), SPEED.ACCEL3.getValue() );
        m_speedDefaults.put( SPEED.ACCEL4.getName(), SPEED.ACCEL4.getValue() );
        m_speedDefaults.put( SPEED.ACCEL5.getName(), SPEED.ACCEL5.getValue() );

        // Add typical max speed settings
        m_speedDefaults.put( SPEED.MAX_EN.getName(), SPEED.MAX_EN.getValue() );
        m_speedDefaults.put( SPEED.MAX_M.getName(), SPEED.MAX_M.getValue() );

        // Add typical speed breaks ranges
        m_speedDefaults.put( SPEED.SPEED1_EN.getName(), SPEED.SPEED1_EN.getValue() );
        m_speedDefaults.put( SPEED.SPEED1_M.getName(), SPEED.SPEED1_M.getValue() );
        m_speedDefaults.put( SPEED.SPEED2_EN.getName(), SPEED.SPEED2_EN.getValue() );
        m_speedDefaults.put( SPEED.SPEED2_M.getName(), SPEED.SPEED2_M.getValue() );
        m_speedDefaults.put( SPEED.SPEED3_EN.getName(), SPEED.SPEED3_EN.getValue() );
        m_speedDefaults.put( SPEED.SPEED3_M.getName(), SPEED.SPEED3_M.getValue() );
        m_speedDefaults.put( SPEED.SPEED4_EN.getName(), SPEED.SPEED4_EN.getValue() );
        m_speedDefaults.put( SPEED.SPEED4_M.getName(), SPEED.SPEED4_M.getValue() );
        m_speedDefaults.put( SPEED.SPEED5_EN.getName(), SPEED.SPEED5_EN.getValue() );
        m_speedDefaults.put( SPEED.SPEED5_M.getName(), SPEED.SPEED5_M.getValue() );
    }


    @Override
    public void addTHCDefaults() {
        m_thcMachineDefaults.put( THC.ANALOG1.getName(), THC.ANALOG1.getValue() );
        m_thcMachineDefaults.put( THC.ANALOG2.getName(), THC.ANALOG2.getValue() );

        m_thcAxisDefaults.put( THC.HARD_STOP.getName(), THC.HARD_STOP.getValue() );
        m_thcAxisDefaults.put( THC.HOMING.getName(), THC.HOMING.getValue() );
        m_thcAxisDefaults.put( THC.SLIDE_EN.getName(), THC.SLIDE_EN.getValue() );
        m_thcAxisDefaults.put( THC.SLIDE_M.getName(), THC.SLIDE_M.getValue() );

        m_thcAnalogDefaults.put( THC.SPEEDPOT1_INSTALLED.getName(), THC.SPEEDPOT1_INSTALLED.getValue() );
        m_thcAnalogDefaults.put( THC.SPEEDPOT1_ANALOG1.getName(), THC.SPEEDPOT1_ANALOG1.getValue() );
        m_thcAnalogDefaults.put( THC.SPEEDPOT1_ANALOG2.getName(), THC.SPEEDPOT1_ANALOG2.getValue() );
    }


    @Override
    public void addInputDefaults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void addOutputDefaults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void addBevelAxesDefaults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public final void addAxesDefaults( String type ) {      
        if( SYSTEM.BENCH.getName().equals( type )) {
            m_axesDefaults = addPicoPathDefaults();
        }
        else if( SYSTEM.HYPATH.getName().equals( type )) {
            m_axesDefaults = addHypathDefaults();
        }   
    }


    @Override
    public Map< String, Integer > getTHCAxisParams() {
        return m_thcAxisDefaults;
    }


    @Override
    public Map< String, Integer > getTHCMachineParams() {
        return m_thcMachineDefaults;
    }


    @Override
    public Map< String, Integer > getTHCAnalogParams() {
        return m_thcAnalogDefaults;
    }


    @Override
    public Map<String, Integer> getAxesParams() {
        return m_axesDefaults;
    }


    @Override
    public Map< String, Integer > getSpeedParams() {
        return m_speedDefaults;
    }
}
