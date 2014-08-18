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
     * Test of read method, of class ConvertLogic.
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        m_instance = new ConvertLogic( m_file, m_operate );
        String checksum;

        try ( BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( m_filePathPass ), StandardCharsets.UTF_8 ))) {
            checksum = buffer.readLine();          
        }

        assertEquals( "Checksum equal ", m_instance.m_paramList.get( 0 ), new StringBuffer( checksum ).append( "\n" ).toString() );
    }

    /**
     * Test of add method, of class ConvertLogic.
     */
    /*@Test
    public void testAdd() {
        System.out.println("add");
        String blockTitle = "";
        /*Map<String, Integer> map = null;
        ConvertLogic instance = null;
        Map<String, Integer> expResult = null;
        Map<String, Integer> result = instance.add(blockTitle, map);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

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
