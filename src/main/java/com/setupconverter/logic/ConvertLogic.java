/**
 * ConverterLogic
 * Paul Wallace
 * June 2014
 * 
 * ConverterLogic provides the logic for the SetupConverter application.  The 
 * class converts the Gain parameters from a configuration file to match a 
 * select drive type.  The class also determines the type of application and
 * configures the I/O to allow homing of that application and control over 
 * the stations if needed.
 *          
 * Main attributes:
 *      *   Accesses the data stored in the DAO interface through the DAO class.
 *      *   Finds the matching parameters within the parameter file and replaces
 *              with the data from the DAO interface.
 *      *   Determines the application type and configures I/O appropriately so
 *              user can satisfy homing and control the cutting.
 *      *   Recalculates the checksum and saves the file to the file system.
 *      *   Implements the IProcess interface, which defines the capabilities of 
 *              of this class.
 * 
 *  Implements:  IProcess
 * 
 */
package com.setupconverter.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import com.setupconverter.logic.IDataAcess.*;
import com.setupconverter.ui.ConvertUI.OperateConverter;

import java.nio.charset.StandardCharsets;
import java.awt.Color;
import java.util.Set;


/**
 * ConvertLogic retrieves/manipulates the data from IDataAccess interface
 * @author prwallace
 */
public class ConvertLogic implements IProcessParameters {
    private static final String REG_EXP = "[=\\s\\.]+";
    private static final String EMPTY_LINE = "\r\n";
    private static final String INPUT = "Input";
    private static final String OUTPUT = "Output";
    private static final String TYPE = "Type=";
    private static final String NUMBER = "Number=";
    private static final String PORT = "Port";

    private final Map< String, Integer > m_IOParamMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_inputTypeMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_inputNumberMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_outputTypeMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_outputNumberMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_linkParamMap = new LinkedHashMap<>();
    private final ArrayList< String > m_paramList = new ArrayList<>();

    private File m_configFile = null;
    private int m_checksum = 0;
    private DataAccessObj m_dataAccess;

    /**
     * EDGE Pro Front Panel Installed
     */
    public boolean m_frontPanelInstalled = false;

    /**
     * One Rotate/Tilt Selected
     */
    public boolean m_bevelInstalled = false;

    /**
     * Dual Rotate/Tile Selected
     */
    public boolean m_dualBevelInstalled = false;

    /**
     * Dual Transverse Selected
     */
    public boolean m_dualTransInstalled = false;

    /**
     * Dual Transverse, one single bevel head installed
     */
    public boolean m_oneRotateTilt = false;

    /**
     * Dual Transverse, no bevel heads installed
     */
    public boolean m_noRotateTilt = false;

    /**
     * SensorTHC axis or axes installed
     */
    public boolean m_sthcInstalled = false;

    /**
     * ArcGlide station or stations installed
     */
    public boolean m_arcGlideInstalled = false;

    /**
     * One pipe axis installed
     */
    public boolean m_isRotatingTrans = false;

    /**
     * Dual gantry axis installed
     */
    public boolean m_dualGantryInstalled = false;

    /**
     * Dual tilting bevel head installed
     */
    public boolean m_dualTiltInstalled = false;

    /**
     * X axis installed on Rail axis
     */
    public boolean m_xOnRail = false;

    /**
     * UI inner class variable
     */
    public OperateConverter m_operate;


    /**
     * Constructor for class ConvertLogic
     * @param file      - Parameter file to be converted
     * @param operate   - UI Inner Class object
     * @throws IOException
     */
    public ConvertLogic( File file, OperateConverter operate ) throws IOException {
        m_configFile = file;
        read( m_configFile );
        m_operate = operate;
    }


    @ Override
    public final void read( File file ) throws IOException {
        try ( BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( file ), StandardCharsets.UTF_8 ))) {
            StringBuilder sb = new StringBuilder();
            int thisChar;
            int lineFeed = 10;

            while(( thisChar = buffer.read() ) != -1 ) {
                sb.append(( char )thisChar );
                if( thisChar == lineFeed ) {
                    m_paramList.add( sb.toString() );
                    sb = new StringBuilder();
                }
            }
        }
    }


    @Override
    public Map< String, Integer > add( String blockTitle, Map< String, Integer > map  ) throws NumberFormatException {
        String param;
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( EMPTY_LINE )  && listIterator.hasNext() ) {
                if( !param.equals( EMPTY_LINE )) {
                    String[] set = param.split( REG_EXP );
                    StringBuilder key = new StringBuilder( set[0] ).append( "=" );

                    try {
                        map.put( key.toString(), Integer.parseInt( set[ 1 ] ));
                    }
                    catch( NumberFormatException e ) {
                        m_operate.setStatus( Color.RED, new StringBuilder( "Exception converting " ).append( key.toString() ).toString(), 
                                                        new StringBuilder( "Key = " ).append( set[ 1 ] ).append( " , set value to 0" ).toString() );
                        map.put( key.toString(), 0 );
                    }
                }
            }
        }

        return map;
    }


    @Override
    public void setParameter( String blockTitle, String paramName, int value ) {
        String param;
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( EMPTY_LINE ) && listIterator.hasNext() ) {
                if( param.startsWith( paramName )) {
                    int replaceIndex = listIterator.previousIndex();
                    String[] set = param.split( REG_EXP );
                    m_paramList.set(replaceIndex, new StringBuilder( set[0] ).append( "=" ).append( value ).append( EMPTY_LINE ).toString() );
                    break;
                }
            }
        }
    }


    @ Override
    public void setChecksum() throws IOException {
        m_checksum = 0;
        for( int i = 1; i < m_paramList.size(); i++ ) {
            for( char ch : m_paramList.get( i ).toCharArray() ) {
                m_checksum += ch;
            }
        }
    }


    @ Override
    public int getChecksum() {
        return m_checksum;
    }


    @Override
    public int getValue( String blockTitle, String paramName ) {
        String param;
        int index;
        int value = -1;

        if(( index = m_paramList.indexOf( blockTitle )) != -1) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( EMPTY_LINE ) &&  listIterator.hasNext() ) {
                if( param.startsWith( paramName )) {
                    String[] set = param.split( REG_EXP );
                    value = Integer.parseInt( set[ 1 ] );
                    break;
                }
            }
        }

        return value;
    }


    @Override
    public void replaceAllParams( String blockTitle, Map< String, Integer > map ) {
        String param;
        int startIndex;

        if(( startIndex = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( startIndex +1 );
            while( !( param = listIterator.next() ).startsWith( EMPTY_LINE ) && listIterator.hasNext() ) {
                for( String key : map.keySet() ) {
                    if( param.startsWith( key )) {
                        int replaceIndex = listIterator.previousIndex();
                        m_paramList.set( replaceIndex, new StringBuilder( key ).append( map.get( key ) ).append( EMPTY_LINE ).toString() );
                        break;
                    }
                }
            }
        }
    }


    /**
     * Converts the original parameter file to control the user specified drive 
     * system.  Replaces Axes, I/O, Speed, and Machine settings based on the selected
     * drive type.  I/O is re-arranged to allow user to satisfy homing and simulate
     * cutting by use of a switch box and loop-back jumper CPC.  Instantiates the
     * DataAccessObj which provides access to the specific drive system type.
     */
    public void convert()  {
        String system = m_operate.getSelectedSystem();
        m_dataAccess = new DataAccessObj( system );
        int sthcTotal;
        int agTHCTotal;
        int row1_NextIndex = 1;
        int row2_NextIndex = 9;
        int row3_NextIndex = 17;
        int torchCollisionLoc = 16;


        // Determine specific tools, bevel heads, pipe axes, THC's that are installed in Machine screen
        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.FP.getName() ) > 0 ) {
            m_frontPanelInstalled = true;
        }
       
        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.BEVEL_AXES.getName() ) > 0 ) {
            m_bevelInstalled = true;
        }

        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_BEVEL.getName() ) > 0 ) {
            m_dualBevelInstalled = true;
        }

        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_TRANS.getName() ) > 0 && 
                getValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_BEVEL.getName() ) > 0 ) {
            m_dualTransInstalled = true;
        }

        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.NO_ROTATE_TILT.getName() ) > 0 ) {
            m_noRotateTilt = true;
        }

        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.ONE_ROTATE_TILT.getName() ) > 0 ) {
            m_oneRotateTilt = true;
        }

        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_GANTRY.getName() ) > 0 ) {
            m_dualGantryInstalled = true;
        }

        if( getValue( BLOCK.AXIS_7.getName(), PARAMETER.ROTATING_TRANS.getName() ) > 0 ) {
            m_isRotatingTrans = true;
        }

        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.X_AXIS_ORIENTATION.getName() ) > 0 ) {
            m_xOnRail = true;
        }

        if( getValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_TILTING.getName() ) > 0 ) {
            m_dualTiltInstalled = true;
        }


        // Convert Speed parameters
        replaceAllParams( BLOCK.SPEEDS.getName(), m_dataAccess.getSpeedParams() );


        // Convert THC parameters
        if(( sthcTotal = getValue( BLOCK.MACHINE.getName(), PARAMETER.STHC.getName() )) > 0 ) {
            m_sthcInstalled = true;
            m_dataAccess.addTHCDefaults();

            for( int i = 0; i < sthcTotal; i++ ) {
                replaceAllParams( new StringBuilder( "[THC" ).append( i + 1 ).append( "]\r\n" ).toString(), m_dataAccess.getTHCAxisParams() );
                replaceAllParams( new StringBuilder( "[THC" ).append( i + 1 ).append( "]\r\n" ).toString(), m_dataAccess.getAxesParams() );
            }

            replaceAllParams( BLOCK.AIC.getName(), m_dataAccess.getTHCAnalogParams() );
            replaceAllParams( BLOCK.MACHINE.getName(), m_dataAccess.getTHCMachineParams() );

            setInput( row1_NextIndex++, INPUT_NUM.NCS_1.getValue() );

            if( sthcTotal >= 2 ) {
                setInput( row1_NextIndex++, INPUT_NUM.NCS_2.getValue() );

                if( sthcTotal >= 3 ) {
                    setInput( row1_NextIndex++, INPUT_NUM.NCS_3.getValue() );

                    if( sthcTotal >= 4 ) {  // Only supporting 4 STHC's at this time.
                        setInput( row1_NextIndex++, INPUT_NUM.NCS_4.getValue() );
                    }
                }
            }
        }
        else if(( agTHCTotal = getValue( BLOCK.MACHINE.getName(), PARAMETER.AG.getName() )) > 0 ) {
            m_arcGlideInstalled = true;
            setInput( row1_NextIndex++, INPUT_NUM.RDY_TO_FIRE_1.getValue() );

            if( agTHCTotal >= 2 ) {
                setInput( row1_NextIndex++, INPUT_NUM.RDY_TO_FIRE_2.getValue() );

                if( agTHCTotal >= 3 ) {
                    setInput( row1_NextIndex++, INPUT_NUM.RDY_TO_FIRE_3.getValue() );

                    if( agTHCTotal == 4 ) {
                        setInput( row1_NextIndex++, INPUT_NUM.RDY_TO_FIRE_4.getValue() );
                    }
                }
            }
        }


        // Convert Dual Gantry Axis parameters
        if( m_dualGantryInstalled ) {
            replaceAllParams( BLOCK.DUAL_GANTRY.getName(), m_dataAccess.getAxesParams() );
            replaceAllParams( BLOCK.DUAL_GANTRY.getName(), m_dataAccess.getDualGantryParams() );
        }


        // Add Drive Disabled Input
        setInput( row2_NextIndex++, INPUT_NUM.DRIVE_DISABLED.getValue() );


        // Convert X & Y Axes parameters and homing inputs
        replaceAllParams( BLOCK.AXIS_1.getName(), m_dataAccess.getAxesParams() );
        replaceAllParams( BLOCK.AXIS_2.getName(), m_dataAccess.getAxesParams() );

        if( m_xOnRail ) {
            setInput( row1_NextIndex++, INPUT_NUM.X_NEG_OT.getValue() );
            setInput( row2_NextIndex++, INPUT_NUM.X_POS_OT.getValue() );
            setInput( row1_NextIndex++, INPUT_NUM.Y_NEG_OT.getValue() );
            setInput( row2_NextIndex++, INPUT_NUM.Y_POS_OT.getValue() );
        }
        else {
            setInput( row1_NextIndex++, INPUT_NUM.Y_NEG_OT.getValue() );
            setInput( row2_NextIndex++, INPUT_NUM.Y_POS_OT.getValue() );
            setInput( row1_NextIndex++, INPUT_NUM.X_NEG_OT.getValue() );
            setInput( row2_NextIndex++, INPUT_NUM.X_POS_OT.getValue() );
        }


        // Convert Dual Transverse parameters and add its inputs
        if( m_dualTransInstalled ) {
            replaceAllParams( BLOCK.AXIS_7.getName(), m_dataAccess.getAxesParams() );

            if( m_isRotatingTrans ) {
                setParameter( BLOCK.MACHINE.getName(), BEVEL.AUTO_HOME.getName(), BEVEL.AUTO_HOME.getValue() );
                setParameter( BLOCK.AXIS_7.getName(),  PARAMETER.SERVO_ERROR_EN.getName(), BEVEL.SERVO_ERROR.getValue() );
                setParameter( BLOCK.AXIS_7.getName(), PARAMETER.ENCODER_CNTS_EN.getName(), BEVEL.ENCODER_CNTS.getValue() );
                setParameter( BLOCK.AXIS_7.getName(), PARAMETER.ENCODER_CNTS_M.getName(), BEVEL.ENCODER_CNTS.getValue() );
                setInput( row1_NextIndex++, INPUT_NUM.ROT_2_HOME.getValue() );
                setInput( row2_NextIndex++, INPUT_NUM.DUAL_HEAD_COLLISION.getValue() );
            }
            else {             
                if( m_xOnRail ) {
                    setInput( row1_NextIndex++, INPUT_NUM.Y_POS_OT.getValue() );
                }
                else {
                    setInput( row1_NextIndex++, INPUT_NUM.X_POS_OT.getValue() );
                }

                setInput( row1_NextIndex - 3, INPUT_NUM.DUAL_HEAD_COLLISION.getValue() );
                torchCollisionLoc = row2_NextIndex++;
            }

            if( row1_NextIndex < 8 ) {
                
                setInput( row1_NextIndex++, INPUT_NUM.PARK_HEAD_1.getValue() );
                setInput( row2_NextIndex++, INPUT_NUM.PARK_HEAD_2.getValue() );
            }
            else {
                setInput( row3_NextIndex++, INPUT_NUM.PARK_HEAD_1.getValue() );
                setInput( row3_NextIndex++, INPUT_NUM.PARK_HEAD_2.getValue() );
            }
        }


        // Convert Bevel Axes parameters and add homing inputs
        if( m_bevelInstalled && ( m_dualBevelInstalled && !m_noRotateTilt || !m_dualBevelInstalled )) {  // Single bevel head installed
            setParameter( BLOCK.MACHINE.getName(), BEVEL.AUTO_HOME.getName(), BEVEL.AUTO_HOME.getValue() );

            replaceAllParams( BLOCK.ROTATE.getName(), m_dataAccess.getAxesParams() );
            setParameter( BLOCK.ROTATE.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );
            setParameter( BLOCK.ROTATE.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );

            replaceAllParams( BLOCK.TILT.getName(), m_dataAccess.getAxesParams() );
            setParameter( BLOCK.TILT.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );
            setParameter( BLOCK.TILT.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );

            if( m_dualTiltInstalled ) {
                if( row1_NextIndex < 8 ) {
                    setInput( row1_NextIndex++, INPUT_NUM.TILT_POS_OT.getValue() );
                    setInput( row2_NextIndex++, INPUT_NUM.TILT_NEG_OT.getValue() );
                    setInput( row1_NextIndex++, INPUT_NUM.TILT2_POS_OT.getValue() );
                    setInput( row2_NextIndex++, INPUT_NUM.TILT2_NEG_OT.getValue() );
                }
                else {
                    setInput( row3_NextIndex++, INPUT_NUM.TILT_POS_OT.getValue() );
                    setInput( row3_NextIndex++, INPUT_NUM.TILT2_POS_OT.getValue() ); 
                }
            }
            else {
                if( row1_NextIndex < 8 ) {
                    setInput( row1_NextIndex++, INPUT_NUM.TILT_POS_OT.getValue() );
                    setInput( row2_NextIndex++, INPUT_NUM.TILT_NEG_OT.getValue() );
                    setInput( row1_NextIndex++, INPUT_NUM.ROTATE_HOME.getValue() );
                    torchCollisionLoc = row2_NextIndex++;
                }
                else {
                    setInput( row3_NextIndex++, INPUT_NUM.TILT_POS_OT.getValue() );
                    setInput( row3_NextIndex++, INPUT_NUM.ROTATE_HOME.getValue() );
                }
            }

            if( m_dualBevelInstalled && !m_oneRotateTilt ) { // Dual Bevel heads installed
                if( m_dualTiltInstalled ) {
                    replaceAllParams( BLOCK.DUAL_TILT.getName(), m_dataAccess.getAxesParams() );
                    setParameter( BLOCK.DUAL_TILT.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
                    setParameter( BLOCK.DUAL_TILT.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );

                    if( row1_NextIndex < 8 ) {
                        setInput( row1_NextIndex++, INPUT_NUM.TILT3_POS_OT.getValue() );
                        setInput( row2_NextIndex++, INPUT_NUM.TILT3_NEG_OT.getValue() );
                        setInput( row1_NextIndex++, INPUT_NUM.TILT4_POS_OT.getValue() );
                        setInput( row2_NextIndex++, INPUT_NUM.TILT4_NEG_OT.getValue() );
                    }
                    else {
                        setInput( row3_NextIndex++, INPUT_NUM.TILT3_POS_OT.getValue() );
                        setInput( row3_NextIndex++, INPUT_NUM.TILT4_POS_OT.getValue() );
                    }
                }
                else {
                    replaceAllParams( BLOCK.DUAL_ROTATE.getName(), m_dataAccess.getAxesParams() );
                    setParameter( BLOCK.DUAL_ROTATE.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
                    setParameter( BLOCK.DUAL_ROTATE.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );

                    if( row1_NextIndex < 8 ) {
                        setInput( row1_NextIndex++, INPUT_NUM.TILT3_POS_OT.getValue() );
                        setInput( row2_NextIndex++, INPUT_NUM.TILT3_NEG_OT.getValue() );
                        setInput( row1_NextIndex++, INPUT_NUM.ROT_2_HOME.getValue() );
                        torchCollisionLoc = row2_NextIndex++;
                    }
                }
            }
        }


        // Add Torch Collision input
        if( torchCollisionLoc < 17 ) {
            setInput( torchCollisionLoc, INPUT_NUM.TORCH_COLLISION.getValue() );
        }
        else if( row3_NextIndex < 25 ) {
            setInput( row3_NextIndex, INPUT_NUM.TORCH_COLLISION.getValue() );
        }


        // Re-assign Cut Sense inputs beginning at input 40
        if( getValue( BLOCK.IO.getName(), INPUT_NUM.CUT_MARK_SENSE.getName() ) > 0 ) {
            setInput( 40, INPUT_NUM.CUT_MARK_SENSE.getValue() );
        }
        else if( getValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_1.getName() ) > 0 ) {
            setInput( 40, INPUT_NUM.CUT_SENSE_1.getValue() );

            if( getValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_2.getName() ) > 0 ) {
                setInput( 41, INPUT_NUM.CUT_SENSE_2.getValue() );
            }

            if( getValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_3.getName() ) > 0 ) {
                setInput( 42, INPUT_NUM.CUT_SENSE_3.getValue() );
            }

            if( getValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_4.getName() ) > 0 ) {
                setInput( 43, INPUT_NUM.CUT_SENSE_4.getValue() );
            }
        }

        // Re-assign Cut Control outputs beginning at output 40
        if( getValue( BLOCK.IO.getName(), OUTPUT_NUM.CUT_CONTROL.getName() ) > 0 ) {
            setOutput( 40, OUTPUT_NUM.CUT_CONTROL.getValue() );
        }
        else if( getValue( BLOCK.IO.getName(), OUTPUT_NUM.CUT_CONTROL_1.getName() ) > 0 ) {
                setOutput( 40, OUTPUT_NUM.CUT_CONTROL_1.getValue() );

            if( getValue( BLOCK.IO.getName(), OUTPUT_NUM.CUT_CONTROL_2.getName() ) > 0 ) {
                setOutput( 41, OUTPUT_NUM.CUT_CONTROL_2.getValue() );
            }

            if( getValue( BLOCK.IO.getName(), OUTPUT_NUM.CUT_CONTROL_3.getName() ) > 0 ) {
                setOutput( 42, OUTPUT_NUM.CUT_CONTROL_3.getValue() );
            }

            if( getValue( BLOCK.IO.getName(), OUTPUT_NUM.CUT_CONTROL_4.getName() ) > 0 ) {
                setOutput( 43, OUTPUT_NUM.CUT_CONTROL_4.getValue() );
            }
        }


        // Re-assign Drive Enable output
        setOutput( 24, OUTPUT_NUM.DRIVE_ENABLE.getValue() );


        // Merge in IO settings into parameter file
        add( BLOCK.IO.getName(), m_IOParamMap );
        printIOMaps();
        shuffleIO();
        replaceAllParams( BLOCK.IO.getName(), m_IOParamMap );


        // Set all port settings to none and merge changes into parameter file
        add( BLOCK.LINK.getName(), m_linkParamMap );
        resetPorts();
        replaceAllParams( BLOCK.LINK.getName(), m_linkParamMap );
    }


    /**
     * Add's an input type and input number to the InputType Map and the InputNumber
     * Map.
     * @param typeIndex - The physical input to be assigned to an input device
     * @param numIndex  - The input device to be assign to an input location
     */
    public void setInput( int typeIndex, int numIndex ) {
        m_inputTypeMap.put( new StringBuilder( INPUT ).append( typeIndex ).append( TYPE ).toString(), numIndex );
        m_inputNumberMap.put( new StringBuilder( INPUT ).append( numIndex ).append( NUMBER   ).toString(), typeIndex );
    }


    /**
     * Add's an output type and output number to the OutputType Map and the OutputNumber
     * Map.
     * @param typeIndex - The physical output to be assigned to a device
     * @param numIndex  - The output device to be assigned to an output location
     */
    public void setOutput( int typeIndex, int numIndex ) {
        m_outputTypeMap.put( new StringBuilder( OUTPUT ).append( typeIndex ).append( TYPE ).toString(), numIndex );
        m_outputNumberMap.put( new StringBuilder( OUTPUT ).append( numIndex ).append( NUMBER ).toString() , typeIndex );
    }


    /**
     * Merge I/O from the data access class into the IO Parameter Map that contains
     * all I/O assignments from the parameter file.  Test for like type assignments 
     * (same I/O position) and relocate to I/O 49 and higher.  Test for like number
     * assignments (same device) and set original type assignment to 0 (replaced 
     * by this output assignment).
     */
    private void shuffleIO() {
        Iterator< Entry< String, Integer >> iterator = m_IOParamMap.entrySet().iterator();
        Entry< String, Integer > entry;
        int inTypeLoc = 49;
        int outTypeLoc = 49;

        // Set all input logic to normally open
        while( !( entry = iterator.next() ).getKey().startsWith( "Input1Number=" ) && iterator.hasNext() ) {
            entry.setValue( 0 );
        }

        // Merge assignmented inputs from IO Map into InputNumber and InputType Map's
        while( !( entry = iterator.next() ).getKey().startsWith( new StringBuilder( INPUT ).append( 1 ).append( TYPE ).toString() ) && iterator.hasNext() ) {
            String inputType = new StringBuilder( INPUT ).append( entry.getValue() ).append( TYPE ).toString();
            if( !m_inputNumberMap.containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                int inValue = getValue( BLOCK.IO.getName(), inputType );
                if( m_inputTypeMap.containsKey( inputType )) {
                    m_inputNumberMap.put( entry.getKey(), inTypeLoc );
                    m_inputTypeMap.put( new StringBuilder( INPUT ).append( inTypeLoc++ ).append( TYPE ).toString(), inValue );
                }
                else {
                    m_inputNumberMap.put( entry.getKey(), entry.getValue() );
                    m_inputTypeMap.put( inputType, inValue );
                }
            }
            else if( entry.getValue() > 0 ) {
                if( !m_inputTypeMap.containsKey( inputType ) ) {
                    m_inputTypeMap.put( inputType, 0 );
                }
            }
        }

        // Set all output logic to normally open
        while( !( entry = iterator.next() ).getKey().startsWith( "Output1Number=" ) && iterator.hasNext() ) {
            entry.setValue( 0 );
        }

        // Merge assigned outputs from IO Map into OutputNumber and OutputType Map's
        while( !( entry = iterator.next() ).getKey().startsWith( new StringBuilder( OUTPUT ).append( 1 ).append( TYPE ).toString() ) && iterator.hasNext() ) {
            String outputType = new StringBuilder( OUTPUT ).append( entry.getValue() ).append( TYPE ).toString();
            if( !m_outputNumberMap.containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                int outValue = getValue( BLOCK.IO.getName(), outputType );
                if( m_outputTypeMap.containsKey( outputType )) {
                    m_outputNumberMap.put( entry.getKey(), outTypeLoc );
                    m_outputTypeMap.put( new StringBuilder( OUTPUT ).append( outTypeLoc++ ).append( TYPE ).toString(), outValue );
                }
                else {
                    m_outputNumberMap.put( entry.getKey(), entry.getValue() );
                    m_outputTypeMap.put( outputType, outValue );
                }
            }
            else if( entry.getValue() > 0 ) {
                if( !m_outputTypeMap.containsKey( outputType ) ) {
                    m_outputTypeMap.put( outputType, 0 );
                }
            }
        }

        // Merge Input#Number Map into IO Parameter Map
        for( Entry< String, Integer> map : m_inputNumberMap.entrySet() ) {
            m_IOParamMap.put( map.getKey(), map.getValue() );
        }

        // Merge Input#Type Map into IO Paramerter Map
        for( Entry< String, Integer > map : m_inputTypeMap.entrySet() ) {
            m_IOParamMap.put( map.getKey(), map.getValue() );
        }

        // Merge Output#Number Map into IO Parameter Map
        for( Entry< String, Integer > map : m_outputNumberMap.entrySet() ) {
            m_IOParamMap.put( map.getKey(), map.getValue() );
        }

        // Merge Output#Type Map into IO Parameter Map
        for( Entry< String, Integer > map : m_outputTypeMap.entrySet() ) {
            m_IOParamMap.put( map.getKey(), map.getValue() );
        }
    }


    /**
     * Re-assigns the port settings in the Link parameter map to None.  Sets the
     * serial ports assigned for the HPR, the PMX, or the MaxPro to port "None"
     * to disable serial communications and allow for simulation. 
     */
    private void resetPorts() {
        Iterator< Entry< String, Integer >> iterator = m_linkParamMap.entrySet().iterator();
        Entry< String, Integer > entry;
        int typeLoc = 1;

        while( !( entry = iterator.next() ).getKey().startsWith( new StringBuilder( PORT ).append( 1 ).append( NUMBER ).toString() ) && iterator.hasNext() ) {
            if( entry.getKey().contains( new StringBuilder( PORT ).append( typeLoc++ ).append( TYPE ) )) {
                int value = entry.getValue();
                if(( value > 2 && value < 5 ) || value > 6 ) {
                    entry.setValue( 0 );
                    m_linkParamMap.put(new StringBuilder( PORT ).append( value ).append( NUMBER ).toString(), 0 );
                }
            }
        }
    }


    @ Override
    public void write( File file ) throws IOException {
        try (BufferedWriter buff_writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file ), StandardCharsets.UTF_8 ))) {
            m_paramList.remove( 0 );
            m_paramList.add( 0, new StringBuilder( "Checksum=" ).append( m_checksum ).append( EMPTY_LINE ).toString() );

            for( String str : m_paramList ) {
                buff_writer.write( str );
            }

            buff_writer.close();
        }
    }


    private void printIOMaps() {
        Iterator< Entry< String, Integer >> iterator;
        Entry< String, Integer > entry;

        // Print Input Type Map
        iterator = m_inputTypeMap.entrySet().iterator();
        while( iterator.hasNext() ) {
            entry = iterator.next();
            System.out.format( "%s\t%d\n", entry.getKey(), entry.getValue() );
        }

        // Print Ouptut Type Map
        iterator = m_outputTypeMap.entrySet().iterator();
        while( iterator.hasNext() ) {
            entry = iterator.next();
            System.out.format("%s\t%d\n", entry.getKey(), entry.getValue() );
        }
    }
}


