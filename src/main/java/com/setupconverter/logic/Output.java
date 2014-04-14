/**
 * 
 */
package com.setupconverter.logic;


/**
 * @author prwallace
 *
 */
public enum Output {
    THD( 4 ), DRIVE_ENABLE( 55 ), NCE1( 57 ), HOLD_IGN1( 58 ), CUT_CONTROL1( 197 ), CUT_CONTROL2( 198 ), STATION_ENABLE_LED1( 217 ), STATION_ENABLE_LED2( 218 ),
    VENT1( 472 ), VENT2( 473 ), VENT3( 474 ), VENT4( 475 ), VENT5( 476 );

    private final int value;

    private Output( int value ) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
