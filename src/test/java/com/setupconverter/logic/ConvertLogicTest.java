/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.setupconverter.logic;

import com.setupconverter.ui.ConvertUI;
import com.setupconverter.ui.ConvertUI.OperateConverter;
import java.io.File;
import java.io.IOException;
import java.util.Map;
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
    private File m_file;
    private ConvertUI m_uiInstance;
    private OperateConverter m_operate;
    private ConvertLogic m_instance;
    
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
        m_file = new File( "./Convert/testFiles/Phoenix.ini" );
        //m_file = new File( "C:\\Users\\Hunter97\\Documents\\myProjects\\Convert\\testFiles\\Phoenix.ini" );
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
        System.out.println( m_file ); 
        m_instance = new ConvertLogic( m_file, m_operate );
        long length = m_file.length();
        System.out.println( length );
        // You could calculate the checksum and compare it to the checksum from the Phoenix.ini file
        System.out.println( m_instance.m_paramList.size() );
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of add method, of class ConvertLogic.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        String blockTitle = "";
        /*Map<String, Integer> map = null;
        ConvertLogic instance = null;
        Map<String, Integer> expResult = null;
        Map<String, Integer> result = instance.add(blockTitle, map);
        assertEquals(expResult, result);*/
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setParameter method, of class ConvertLogic.
     */
    @Test
    public void testSetParameter() {
        System.out.println("setParameter");
        String blockTitle = "";
        String paramName = "";
        int value = 0;
        /*ConvertLogic instance = null;
        instance.setParameter(blockTitle, paramName, value);*/
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setChecksum method, of class ConvertLogic.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetChecksum() throws Exception {
        System.out.println("setChecksum");
        /*ConvertLogic instance = null;
        instance.setChecksum();*/
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChecksum method, of class ConvertLogic.
     */
    @Test
    public void testGetChecksum() {
        System.out.println("getChecksum");
        /*ConvertLogic instance = null;
        int expResult = 0;
        int result = instance.getChecksum();
        assertEquals(expResult, result);*/
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getValue method, of class ConvertLogic.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        String blockTitle = "";
        String paramName = "";
        /*ConvertLogic instance = null;
        int expResult = 0;
        int result = instance.getValue(blockTitle, paramName);
        assertEquals(expResult, result);*/
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of replaceAllParams method, of class ConvertLogic.
     */
    @Test
    public void testReplaceAllParams() {
        System.out.println("replaceAllParams");
        String blockTitle = "";
        /*Map<String, Integer> map = null;
        ConvertLogic instance = null;
        instance.replaceAllParams(blockTitle, map);*/
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of convert method, of class ConvertLogic.
     */
    @Test
    public void testConvert() {
        System.out.println("convert");
        /*ConvertLogic instance = null;
        instance.convert();*/
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setInput method, of class ConvertLogic.
     */
    @Test
    public void testSetInput() {
        System.out.println("setInput");
        /*int typeIndex = 0;
        int numIndex = 0;
        ConvertLogic instance = null;
        instance.setInput(typeIndex, numIndex);*/
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
    @Test
    public void testWrite() throws Exception {
        System.out.println("write");
        /*File file = null;
        ConvertLogic instance = null;
        instance.write(file);*/
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
