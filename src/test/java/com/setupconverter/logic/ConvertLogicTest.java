/**
 *  ConvertLogicTest.java
 *  Paul Wallace
 *  June 2014
 * 
 *  ConvertLogicTest tests the methods in interface IParameters as implemented 
 *  in class ConvertLogic.
 * 
 */

package com.setupconverter.logic;

import com.setupconverter.ui.ConvertUI.OperateConverter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
//import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author prwallace
 */
public class ConvertLogicTest {
    private static final String LOAD_FILEPATH = "./testFiles/PhoenixPass.ini";
    //private static final String SAVE_FILEPATH = "./testFiles/PhoenixSave.ini";
    private static final String LINE_RETURN = "\r\n";
    private static final String REGEX = "[=\\r\\n]";
    private static final String MACHINE = "[Machine]\r\n";
    private static final String PARAMETER = "FrontPanelInstalled=";
    private static final String INVALID_PARAM = "SensorTHCInstalled=-1";
    private final String[] m_params = { "SensorPlasma1=", "ScaleRotator=", "SensorPlasma2=", "KeyLogging=" };
    private OperateConverter m_operate;
    private ConvertLogic m_setup;
    private File m_loadFile;
    //private File m_saveFile;
   

    
    /**
     * Instantiates objects that are essential to all tests.
     */
    @Before
    public void setUp() {
        m_loadFile = new File( LOAD_FILEPATH );
    }


    /**
     * Test the load method of class ConvertLogic.  Uses a known configuration
     * file to verify the file can be properly read from and its contents loaded
     * to an ArrayList.
     */
    @Test
    public void testLoad() {
        ArrayList< String > tmpList = new ArrayList<>();
        String line;
        int checksum = 0;

        System.out.println( "testLoad..." );

        try {
            m_setup = new ConvertLogic( m_loadFile, m_operate );
        } catch (IOException e) {
            fail( new StringBuilder( "Instantiating ConvertLogic object: ").append( e.getMessage() ).toString() );
        }

        try ( BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( LOAD_FILEPATH ), StandardCharsets.UTF_8 ))) {
            while(( line = buffer.readLine() ) != null ) {
                tmpList.add( new StringBuilder( line ).append( LINE_RETURN ).toString() );
            }
        }
        catch( IOException ex ) {
            fail( new StringBuilder( "testLoad: " ).append( ex.getMessage() ).toString() );
        }

        for( int i = 1; i < m_setup.getParameterList().size(); i++ ) {
            for( char ch : m_setup.getParameterList().get( i ).toString().toCharArray() ) {
                checksum += ch;
            }
        }

        String[] splitChecksum = tmpList.get( 0 ).split( REGEX );


        assertEquals( "Checksums not equal:", checksum, Integer.parseInt( splitChecksum[ 1 ] ) );
        assertEquals( "File save date not equal:", m_setup.getParameterList().get( 17 ), tmpList.get( 17 ) );
        assertEquals( "File size not equal:", m_setup.getParameterList().size(), tmpList.size() );
    }


    /**
     * Tests the putParameters method of class ConvertLogic.  Uses a known
     * configuration file to tests that a block of parameters can be loaded into
     * a Map.
     */
    @Test
    public void testPutParameters() {
        ArrayList< String > machineParams = new ArrayList<>();
        ArrayList< String > analogParams = new ArrayList<>();
        Map< String, Integer > map = new LinkedHashMap<>();
        String line;
        boolean wrongSize = false;
        boolean notEqual = false;
        int index = 0;

        System.out.println( "testPutParameters..." );

        try {
            m_setup = new ConvertLogic( m_loadFile, m_operate );
        } catch ( IOException e ) {
            fail( new StringBuilder( "ConvertLogic object; failed to open file stream: " ).append( e.getMessage() ).toString() );
        }

        m_setup.putParameters( MACHINE, map );

        try ( BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( LOAD_FILEPATH ), StandardCharsets.UTF_8 ) )) {
            while(( line = buffer.readLine() ) != null ) {
                if( line.equalsIgnoreCase( "[Machine]" ) ) {
                    while( !( line = buffer.readLine() ).equalsIgnoreCase( "" ) ) {
                        machineParams.add( new StringBuilder( line ).toString() );
                    } 
                }

                if( line.equalsIgnoreCase( "[AnalogInputCard]" ) ) {
                    while( !( line = buffer.readLine() ).equalsIgnoreCase( "" ) ) {
                        analogParams.add( new StringBuilder( line ).toString() );
                    }

                    break;
                }
            }
        }
        catch( IOException ex ) {
            fail( new StringBuilder( "testPutParameter: " ).append( ex.getMessage() ).toString());
        }

        if( map.size() != analogParams.size() ) {
            wrongSize = true;
        }

        for( Map.Entry< String, Integer> set : map.entrySet() ) {
            if( !machineParams.get( index ).startsWith( set.getKey() )) {
                notEqual = true;
                break;
            }

            index++;
        }


        assertEquals( "Block sizes are not equal:", map.size(), machineParams.size() );
        assertFalse( "Block content does not match:", notEqual );
        assertTrue( "Block size is equal to the invalid block:", wrongSize );
    }


    /**
     * Test the setValue method of class ConvertLogic.  Changes parameter values
     * from a known configuration file and verifies the values were properly
     * changed.
     */
    @Test
    public void testSetParameterValue() {
        List< String > block = Arrays.asList( "[Machine]\r\n", "[I/O]\r\n", "[Consumables]\r\n" );
        List< String > key = Arrays.asList( "FrontPanelInstalled=", "MaxOxyPressure(english)="  , "PlasmaElectrode8Installed=" );
        String param;
        String[] set = null;
        boolean isEqual = false;
        int[] value = {10, 20, 40};
        int index;

        System.out.println("testSetParameterValue...");

        try {
            m_setup = new ConvertLogic( m_loadFile, m_operate );
        } catch ( IOException e ) {
            fail( new StringBuilder( "ConvertLogic object failed to open file stream: " ).append( e.getMessage() ).toString() );
        }

        for( int i = 0; i < block.size(); i++ ) {
            m_setup.setParameterValue( block.get( i ), key.get( i ), value[ i ] );

            if(( index = m_setup.getParameterList().indexOf( block.get( i ) )) != -1 ) {
                ListIterator< String > listIterator = m_setup.getParameterList().listIterator( index +1 );

                while( !( param = listIterator.next() ).startsWith( LINE_RETURN ) && listIterator.hasNext() ) {
                    if( param.startsWith( key.get( i ) )) {
                        set = param.split( REGEX );
                        break;
                    }
                }
            }

            try {
                if( Integer.parseInt( set[ 1 ] ) == value[ i ] ) {
                    isEqual = true;
                }
                else {
                    isEqual = false;
                    break;
                }
            }
            catch( NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e ) {
                fail( new StringBuilder( "testSetParameterValue: " ).append( e.getMessage() ).toString());
            }
        }


        assertTrue( "Unable to set parameter value:", isEqual );
    }


    /**
     * Test the setChecksum and getChecksum methods of class ConvertLogic.  
     * Calculates the checksum from a known configuration file and then verifies
     * the calculated checksum equals the checksum in the file.
     */
    @Test
    public void testChecksum() {
        System.out.println("testChecksum...");

        try {
            m_setup = new ConvertLogic( m_loadFile, m_operate );
            m_setup.setChecksum();
        } catch ( IOException e ) {
            fail( new StringBuilder( "ConvertLogic object failed to open file stream: " ).append( e.getMessage() ).toString() );
        }

        String[] checksumLine = m_setup.getParameterList().get( 0 ).toString().split( REGEX );
        
        try {
            assertEquals( "Checksum are not equal:", m_setup.getChecksum(), Integer.parseInt( checksumLine[ 1 ] ));
        }
        catch( NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e ) {
                fail( new StringBuilder( "testChecksum: " ).append( e.getMessage() ).toString());
        }
    }


    /**
     * Test the getValue method of class ConvertLogic.  Passes a known parameter
     * to getValue with a known value and tests the return value. 
     */
    @Test
    public void testGetParameterValue() {
        String[] set = null;
        String param;
        int result;
        int index;
        int value = -1;

        System.out.println("testGetParameterValue...");

        try {
            m_setup = new ConvertLogic( m_loadFile, m_operate );
        } catch (IOException e) {
            fail( new StringBuilder( "ConvertLogic object failed to open file stream: ").append( e.getMessage() ).toString() );
        }

        result = m_setup.getParameterValue( MACHINE, PARAMETER );

        if(( index = m_setup.getParameterList().indexOf( MACHINE ) ) != -1 ) {
                ListIterator< String > listIterator = m_setup.getParameterList().listIterator( index +1 );

                while( !( param = listIterator.next() ).startsWith( LINE_RETURN ) && listIterator.hasNext() ) {
                    if( param.startsWith( PARAMETER ) ) {
                        set = param.split( REGEX );
                        break;
                    }
                }
        }
        else {
            fail( new StringBuilder( "testGetParameterValue; unable to find ").append( PARAMETER ).append( " in parameter list" ).toString() );
        }

        try {
            value = Integer.parseInt( set[ 1 ] );
        }
        catch( NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e ) {
                fail( new StringBuilder( "testGetParameterValue: " ).append( e.getMessage() ).toString());
        }


        assertEquals( "Value are not equal:", result, value );


        boolean isInvalid = false;
        result = m_setup.getParameterValue( MACHINE, INVALID_PARAM );

        if( result == -1 ) {
            isInvalid = true;
        }


        assertTrue( "Value was not -1:", isInvalid );
    }


    /**
     * Test the replaceAllParams method of class ConvertLogic.  Uses a Map of
     * fixed parameters with known values and replaces these parameters within
     * the parameter list.  Verifies the parameters and their values were replaced
     * properly.
     */
    @Test
    public void testReplaceParameters() {
        Map< String, Integer > map = new LinkedHashMap<>();
        int index = 0;
        boolean isNotEqual = false;

        System.out.println("testReplaceParameters...");

        try {
            m_setup = new ConvertLogic( m_loadFile, m_operate );
        } catch ( IOException e ) {
            Logger.getLogger( ConvertLogicTest.class.getName() ).log( Level.SEVERE, "testReplaceParamter; failed to open file stream: ", e );
            fail( new StringBuilder( "ConvertLogic object failed to open file stream: ").append( e.getMessage() ).toString() );
        }

        for( String param : m_params ) {
           map.put( param, index++ ); 
        }

        m_setup.replaceParameters( MACHINE, map );

        index = 0;
        for( String param : m_params ) {
            if( m_setup.getParameterValue( MACHINE, param ) != index++ ) {
                isNotEqual = true;
                break;
            }
        }


        assertFalse( "Unable to replace parameters:", isNotEqual );
    }
}
