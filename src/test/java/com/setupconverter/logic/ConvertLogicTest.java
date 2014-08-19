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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Hunter97
 */
public class ConvertLogicTest {
    private ConvertUI m_uiInstance;
    private OperateConverter m_operate;
    private ConvertLogic m_instance;
    private File m_file;
    private static final String m_filePathPass = "./Convert/testFiles/PhoenixPass.ini";
    private static final String m_filePathFail = "./Convert/testFiles/PhoenixFail.ini";
    private static final String LINE_RETURN = "\r\n";
    
    
    /*public ConvertLogicTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }*/
    
    @Before
    public void setUp() {
        m_uiInstance = new ConvertUI();
        m_file = new File( m_filePathPass );
    }
    
    @After
    public void tearDown() {
    }


    /**
     * Testing the read method of class ConvertLogic.  Opens and reads a known
     * configuration file and compares the files checksum, date saved, and size
     * to insure file was properly read and loaded into the ArrayList.  
     */
    @Test
    public void testRead() {
        System.out.println( "tetRead" );
        String line = null;
        int checksum = 0;
        ArrayList< String > tmpList = new ArrayList<>();

        try {
            m_instance = new ConvertLogic( m_file, m_operate );
        } catch (IOException e) {
            fail( new StringBuilder( "read produced an IOException: ").append( e.getMessage() ).toString() );
        }

        try ( BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( m_filePathPass ), StandardCharsets.UTF_8 ))) {
            while(( line = buffer.readLine() ) != null ) {
                tmpList.add( new StringBuilder( line ).append( LINE_RETURN ).toString() );
            }

        }
        catch( IOException ex ) {
            fail( new StringBuilder( "testRead produced an IOException: " ).append( ex.getMessage() ).toString() );
        }

        for( int i = 1; i < m_instance.getParameters().size(); i++ ) {
            for( char ch : m_instance.getParameters().get( i ).toString().toCharArray() ) {
                checksum += ch;
            }
        }

        String[] splitChecksum = tmpList.get( 0 ).split( "[=\\r\\n]" );

        assertEquals( "Checksums not equal:", checksum, Integer.parseInt( splitChecksum[ 1 ] ) );
        assertEquals( "File save date not equal:", m_instance.getParameters().get( 17 ), tmpList.get( 17 ) );
        assertEquals( "Files size not equal:", m_instance.getParameters().size(), tmpList.size() );
    }


    /**
     * Test of add method, of class ConvertLogic.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        ArrayList< String > tmpList

        try {
            m_instance = new ConvertLogic( m_file, m_operate );
        } catch (IOException e) {
            fail( new StringBuilder( "read produced an IOException: ").append( e.getMessage() ).toString() );
        }

        try ( BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( m_filePathPass ), StandardCharsets.UTF_8 ))) {
            while(( line = buffer.readLine() ) != null ) {
                tmpList.add( new StringBuilder( line ).append( LINE_RETURN ).toString() );
            }

        }
        catch( IOException ex ) {
            fail( new StringBuilder( "testRead produced an IOException: " ).append( ex.getMessage() ).toString() );
        }

    }

    /**
     * Test of setParameter method, of class ConvertLogic.
     */
    /*@Test
    public void testSetParameter() {
        System.out.println("setParameter");
        String blockTitle = "";
        String paramName = "";
        int value = 0;
        /*ConvertLogic instance = null;
        instance.setParameter(blockTitle, paramName, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of setChecksum method, of class ConvertLogic.
     * @throws java.lang.Exception
     */
    /*@Test
    public void testSetChecksum() throws Exception {
        System.out.println("setChecksum");
        /*ConvertLogic instance = null;
        instance.setChecksum();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

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
