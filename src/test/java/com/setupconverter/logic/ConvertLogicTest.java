/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.setupconverter.logic;

import com.setupconverter.ui.ConvertUI;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hunter97
 */
public class ConvertLogicTest {
    private static final String m_filePathPass = "./Convert/testFiles/PhoenixPass.ini";
    private static final String m_filePathFail = "./Convert/testFiles/PhoenixFail.ini";
    private static final String LINE_RETURN = "\r\n";
    private static final String REGEX = "[=\\r\\n]";
    private static final String MACHINE = "[Machine]\r\n";
    private ConvertUI m_uiInstance;
    private OperateConverter m_operate;
    private ConvertLogic m_instance;
    private File m_file;

    
    @Before
    public void setUp() {
        m_uiInstance = new ConvertUI();
        m_file = new File( m_filePathPass );
    }


    @After
    public void tearDown() {
    }


    /**
     * Test the read method of class ConvertLogic.  Uses a known configuration
     * file to verify the file can be properly read from and its contents stored
     * to an ArrayList.  
     * Test conditions:
     *  * Instantiates a ConvertLogic object,  which in turn calls the read
     *      method which reads/loads the test configuration file into the parameter
     *      List.
     *  * Reads the same test configuration file within testRead method and loads
     *      into a local ArrayList.
     *  * Compares the checksum, the size of the file, and the date saved,
     *      between both files.  
     */
    @Test
    public void testRead() {
        ArrayList< String > tmpList = new ArrayList<>();
        String line;
        int checksum = 0;

        System.out.println( "tetRead..." );

        try {
            m_instance = new ConvertLogic( m_file, m_operate );
        } catch (IOException e) {
            fail( new StringBuilder( "read method produced an IOException: ").append( e.getMessage() ).toString() );
        }

        try ( BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( m_filePathPass ), StandardCharsets.UTF_8 ))) {
            while(( line = buffer.readLine() ) != null ) {
                tmpList.add( new StringBuilder( line ).append( LINE_RETURN ).toString() );
            }
        }
        catch( IOException ex ) {
            fail( new StringBuilder( "testRead method produced an IOException: " ).append( ex.getMessage() ).toString() );
        }

        for( int i = 1; i < m_instance.getParameters().size(); i++ ) {
            for( char ch : m_instance.getParameters().get( i ).toString().toCharArray() ) {
                checksum += ch;
            }
        }

        String[] splitChecksum = tmpList.get( 0 ).split( REGEX );


        assertEquals( "Checksums not equal:", checksum, Integer.parseInt( splitChecksum[ 1 ] ) );
        assertEquals( "File save date not equal:", m_instance.getParameters().get( 17 ), tmpList.get( 17 ) );
        assertEquals( "File size not equal:", m_instance.getParameters().size(), tmpList.size() );
    }


    /**
     * Test the add method of class ConvertLogic.  Reads in 2 blocks of a known
     * configuration file and compares the sizes of the blocks.  One should be of
     * equal size and the other should be of unequal size.
     * Test conditions:
     *  * Instantiates a ConvertLogic object.
     *  * Call add using ConvertLogic object to add Machine block to a map and
     *      add Machine block to local ArrayList; then compare the size of map to
     *      ArrayList, should be equal.
     *  * Add AnalogInputCard block to local ArrayList and compare the size of
     *      map to ArrayList, should not be equal.
     *  * Verify contents of Map against contents of Machine block ArrayList,
     *      should be equal.
     */
    @Test
    public void testAdd() {
        ArrayList< String > machineParams = new ArrayList<>();
        ArrayList< String > analogParams = new ArrayList<>();
        Map< String, Integer > map = new LinkedHashMap<>();
        String line;
        boolean wrongSize = false;
        boolean notEqual = false;
        int index = 0;

        System.out.println( "testAdd..." );

        try {
            m_instance = new ConvertLogic( m_file, m_operate );
        } catch ( IOException e ) {
            fail( new StringBuilder( "read method produced an IOException: " ).append( e.getMessage() ).toString() );
        }

        m_instance.add( MACHINE, map );

        try ( BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( m_filePathPass ), StandardCharsets.UTF_8 ) )) {
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
            fail( new StringBuilder( "testRead produced an IOException: " ).append( ex.getMessage() ).toString());
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
     * Test the setParameter method of class ConvertLogic.  Changes the value of
     * a known parameter from a known configuration file and then verifies the
     * value has been successfully changed.
     * Test conditions:
     *  * Instantiates a ConvertLogic object.
     *  * Calls setParameters 3 different times using 3 different block titles
     *      with 1 parameter from each block, and a different value for each 
     *      parameter.  The compares the set values against the values sent to
     *      setParameters for a match.
     */
    @Test
    public void testSetParameter() {
        List< String > block = Arrays.asList( "[Machine]\r\n", "[I/O]\r\n", "[Consumables]\r\n" );
        List< String > key = Arrays.asList( "FrontPanelInstalled=", "MaxOxyPressure(english)="  , "PlasmaElectrode8Installed=" );
        String param;
        String[] set = null;
        boolean isEqual = false;
        int[] value = {10, 20, 40};
        int index;

        System.out.println("testSetParameter...");

        try {
            m_instance = new ConvertLogic( m_file, m_operate );
        } catch ( IOException e ) {
            fail( new StringBuilder( "setParameter method produced an IOException: " ).append( e.getMessage() ).toString() );
        }

        for( int i = 0; i < block.size(); i++ ) {
            m_instance.setValue( block.get( i ), key.get( i ), value[ i ] );

            if(( index = m_instance.getParameters().indexOf( block.get( i ) )) != -1 ) {
                ListIterator< String > listIterator = m_instance.getParameters().listIterator( index +1 );

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
                fail( new StringBuilder( "testSetParameter produced an Exception: " ).append( e.getMessage() ).toString());
            }
        }


        assertTrue( "Unable to set parameter value:", isEqual );
    }


    /**
     * Test the setChecksum method of class ConvertLogic.  Calculates the checksum
     * from a known configuration file, then checks the calculated value to the 
     * value stored in the file.
     * Test conditions:
     *  * Instantiates a ConvertLogic object.
     *  * Calculates and sets checksum variable in ConvertLogic object
     *  * Extracts the checksum line directly from the configuration file and splits
     *      out the checksum value.
     *  * Tests the calculated checksum to the checksum from the configuration file.
     */
    @Test
    public void testSetChecksum() {
        System.out.println("testSetChecksum...");

        try {
            m_instance = new ConvertLogic( m_file, m_operate );
            m_instance.setChecksum();
        } catch ( IOException e ) {
            fail( new StringBuilder( "setParameter method produced an IOException: " ).append( e.getMessage() ).toString() );
        }

        String[] checksumLine = m_instance.getParameters().get( 0 ).toString().split( REGEX );
        
        try {
            assertEquals( "Checksum are not equal:", m_instance.getChecksum(), Integer.parseInt( checksumLine[ 1 ] ));
        }
        catch( NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e ) {
                fail( new StringBuilder( "testSetParameter produced an Exception: " ).append( e.getMessage() ).toString());
        }

    }

    /**
     * Test of getChecksum method, of class ConvertLogic.
     */
    /*@Test
    public void testGetChecksum() {
        System.out.println("getChecksum");
        /*ConvertLogic instance = null;
        int expResult = 0;
        int result = instance.getChecksum();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of getValue method, of class ConvertLogic.
     */
    /*@Test
    public void testGetValue() {
        System.out.println("getValue");
        String blockTitle = "";
        String paramName = "";
        /*ConvertLogic instance = null;
        int expResult = 0;
        int result = instance.getValue(blockTitle, paramName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of replaceAllParams method, of class ConvertLogic.
     */
    /*@Test
    public void testReplaceAllParams() {
        System.out.println("replaceAllParams");
        String blockTitle = "";
        /*Map<String, Integer> map = null;
        ConvertLogic instance = null;
        instance.replaceAllParams(blockTitle, map);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of convert method, of class ConvertLogic.
     */
    /*@Test
    public void testConvert() {
        System.out.println("convert");
        /*ConvertLogic instance = null;
        instance.convert();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of setInput method, of class ConvertLogic.
     */
    /*@Test
    public void testSetInput() {
        System.out.println("setInput");
        int typeIndex = 0;
        int numIndex = 0;
        ConvertLogic instance = null;
        instance.setInput(typeIndex, numIndex);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOutput method, of class ConvertLogic.
     */
    /*@Test
    public void testSetOutput() {
        System.out.println("setOutput");
        /*int typeIndex = 0;
        int numIndex = 0;
        ConvertLogic instance = null;
        instance.setOutput(typeIndex, numIndex);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of write method, of class ConvertLogic.
     */
    /*@Test
    public void testWrite() throws Exception {
        System.out.println("write");
        /*File file = null;
        ConvertLogic instance = null;
        instance.write(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
