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

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author prwallace
 */
public class DataAcessObj implements IDataAcess {
    private final EnumMap< SPEED, SPEED > m_speedDefault = new EnumMap<>( SPEED.class );


    @Override
    public void addSpeedDefaults() {
        // Add typica acceleration settings
        m_speedDefault.put( SPEED.ACCEL1, SPEED.ACCEL1 );
        m_speedDefault.put( SPEED.ACCEL2, SPEED.ACCEL2 );
        m_speedDefault.put( SPEED.ACCEL3, SPEED.ACCEL3 );
        m_speedDefault.put( SPEED.ACCEL4, SPEED.ACCEL4 );
        m_speedDefault.put( SPEED.ACCEL5, SPEED.ACCEL5 );

        // Add typical max speed settings
        m_speedDefault.put( SPEED.MAX_EN, SPEED.MAX_EN );
        m_speedDefault.put( SPEED.MAX_M, SPEED.MAX_M );

        // Add typical speed breaks ranges
        m_speedDefault.put( SPEED.SPEED1_EN, SPEED.SPEED1_EN );
        m_speedDefault.put( SPEED.SPEED1_M, SPEED.SPEED1_M );
        m_speedDefault.put( SPEED.SPEED2_EN, SPEED.SPEED2_EN );
        m_speedDefault.put( SPEED.SPEED2_M, SPEED.SPEED2_M );
        m_speedDefault.put( SPEED.SPEED3_EN, SPEED.SPEED3_EN );
        m_speedDefault.put( SPEED.SPEED3_M, SPEED.SPEED3_M );
        m_speedDefault.put( SPEED.SPEED4_EN, SPEED.SPEED4_EN );
        m_speedDefault.put( SPEED.SPEED4_M, SPEED.SPEED4_M );
        m_speedDefault.put( SPEED.SPEED5_EN, SPEED.SPEED5_EN );
        m_speedDefault.put( SPEED.SPEED5_M, SPEED.SPEED5_M );
    }

    @Override
    public void addAnalogDefaults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addMachineDefaults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public void addTHCDefaults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addBevelAxesDefaults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addAxesDefaults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    
}
