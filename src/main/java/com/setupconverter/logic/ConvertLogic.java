/**
 * 
 */
package com.setupconverter.logic;

import com.setupconverter.logic.IDataAcess.*;
import com.setupconverter.ui.ConverterUI.OperateConverter;

import java.awt.Color;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 
 * @author prwallace
 */
public class ConvertLogic implements IProcess {

    private static final int TOTAL_AXIS_BLOCKS = 3;
    private static final String REG_EXP = "[=\\s\\.]+";
    private static final String EMPTY_LINE = "\r\n";
    private static final String INPUT = "Input";
    private static final String OUTPUT = "Output";
    private static final String TYPE = "Type=";
    private static final String NUMBER = "Number=";
    private static final String PORT = "Port";

    private final ArrayList< String > m_paramList = new ArrayList<>();

    private final Map< String, Integer > m_IOParamMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_inputTypeMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_inputNumberMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_outputTypeMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_outputNumberMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_LinkParamMap = new LinkedHashMap<>();

    private DataAccessObj m_dataAccess;
    public OperateConverter m_operate;

    private File m_configFile = null;
    private int m_checksum = 0;

    public boolean m_frontPanelInstalled = false;
    public boolean m_bevelInstalled = false;
    public boolean m_dualBevelInstalled = false;
    public boolean m_dualTransInstalled = false;
    public boolean m_oneRotateTilt = false;
    public boolean m_noRotateTilt = false;
    public boolean m_sthcInstalled = false;
    public boolean m_arcGlideInstalled = false;
    public boolean m_isRotatingTrans = false;
    public boolean m_dualGantryInstalled = false;
    public boolean m_dualTiltInstalled = false;
    public boolean m_xOnRail = false;



    /**
     * Constructor for class ConvertLogic
     * @param file      - Parameter file to be converted
     * @param operate   - UI Inner Class object
     * @throws IOException
     */
    public ConvertLogic( File file, OperateConverter operate ) throws IOException {//
        m_configFile = file;
        loadParams( m_configFile );
        m_operate = operate;
    }


    /**
     * Loads each line of a configuration file into a a List< String >.  Each
     * line is delimited by a carriage return/line feed combination (\r\n}.
     * The File argument is loaded into a BufferedReader that is wrapped by a
     * FileInputStream that uses the standard character set UTF_8.
     * 
     * @param file  - File reference to the configuration file
     * @category StandardCharsets.UTF_8
     * @throws IOException
     */
    @ Override
    public final void loadParams( File file ) throws IOException {
        try (BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( file ), StandardCharsets.UTF_8 ))) {
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


    /**
     * Adds a block of parameters from the parameter List to a Map< String, Integer >
     * and returns the resultant Map.  The block is specified by the argument block
     * title.
     * @param blockTitle    - Title of parameter block (i.e. [Machine])
     * @param map           - Map to store a block of parameters from parameter List
     * @return              - Argument Map, containing added parameters
     * @throws ArrayIndexOutOfBoundsException
     * @throws NumberFormatException
     */
    public Map< String, Integer > addParamsToMap( String blockTitle, Map< String, Integer > map  ) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        String param;
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( EMPTY_LINE )  && listIterator.hasNext() ) {
                if( !param.equals( EMPTY_LINE )) {
                    String[] key = param.split( REG_EXP );
                    StringBuilder sb = new StringBuilder( key[0] ).append( "=" );

                    try {
                        map.put( sb.toString(), Integer.parseInt( key[ 1 ] ));
                    }
                    catch( NumberFormatException e ) {
                        m_operate.setStatus( Color.RED, new StringBuilder( "Exception converting " ).append( sb.toString() ).toString(), 
                                                        new StringBuilder( "Key = " ).append( key[ 1 ] ).append( " , set value to 0" ).toString() );
                        map.put( sb.toString(), 0 );
                    }
                }
            }
        }

        return map;
    }


    /**
     * Changes the value of a parameter from the parameter list to the argument value.
     * Parses the parameter list, starting after the argument block title, for a
     * match to the String argument.  If a match is found, the argument value is
     * set to the parameter, in place of the original value.
     * @param blockTitle    - Title of parameter block in list (i.e. [Machine]) 
     * @param paramName     - Parameter setting to be searched/changed in parameter list
     * @param value         - Value to replace in parameter list
     */
    public void changeParamValue( String blockTitle, String paramName, int value ) {
        String param;
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( EMPTY_LINE ) && listIterator.hasNext() ) {
                if( param.startsWith( paramName )) {
                    int replaceIndex = listIterator.previousIndex();
                    String[] key = param.split( REG_EXP );
                    m_paramList.set(replaceIndex, new StringBuilder( key[0] ).append( value ).toString() );
                    break;
                }
            }
        }
    }


    /**
     * Calculates the checksum of a configuration file.  Each character in the
     * List< String >(), beginning at the 2nd index, is summed together.
     * @throws IOException
     */
    @ Override
    public void setChecksum() throws IOException {
        m_checksum = 0;
        for( int i = 1; i < m_paramList.size(); i++ ) {
            for( char ch : m_paramList.get( i ).toCharArray() ) {
                m_checksum += ch;
            }
        }
    }


    /**
     * Gets/returns the checksum of the configuration file.
     * @return an integer equal to sum of characters in List< String >
     */
    @ Override
    public int getChecksum() {
        return m_checksum;
    }


    /**
     * Gets/returns the value of the argument parameter from within the parameter
     * List. Parses the m_paramList List, starting after the argument block title,
     * for a match to the String argument.  If a match is found, the value of that
     * parameter is returned, otherwise a -1 is returned.
     * @param blockTitle    - Title of parameter block in list (i.e. [Machine])
     * @param paramName     - Parameter setting to be searched for within parameter List
     * @return              - The value of the parameter
     */
    public int getParamValue( String blockTitle, String paramName ) {
        String param;
        int index;
        int value = -1;

        if(( index = m_paramList.indexOf( blockTitle )) != -1) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( EMPTY_LINE ) &&  listIterator.hasNext() ) {
                if( param.startsWith( paramName )) {
                    String[] key = param.split( REG_EXP );
                    value = Integer.parseInt( key[ 1 ] );
                    break;
                }
            }
        }

        return value;
    }


    /**
     * Find/replace the parameters within the parameter list with the
     * parameters contained within the argument Map.  Starts searching the parameter 
     * list after the argument block title.
     * @param blockTitle    - Title of the parameter block in list (i.e. [Machine])
     * @param map           - Map containing the set of parameters to replace
     */
    private void replaceAllParams( String blockTitle, Map< String, Integer > map ) throws ArrayIndexOutOfBoundsException, NullPointerException {
        String param;
        int startIndex;

        if(( startIndex = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( startIndex +1 );
            while( !( param = listIterator.next() ).startsWith( EMPTY_LINE ) && listIterator.hasNext() ) {
                for( String key : map.keySet() ) {
                    if( param.startsWith( key )) {
                        int replaceIndex = listIterator.previousIndex();
                        m_paramList.set( replaceIndex, new StringBuilder( key ).append( map.get( key ) ).append( EMPTY_LINE ).toString() );
                        System.out.format( m_paramList.get( replaceIndex ));
                        break;
                    }
                }
            }
        }
    }


    /**  
     * Converts the original parameter file to control the user specified drive 
     * system.  Replaces Axes, I/O, Speed, and Machine settings based on the selected
     * drive type.  I/O is re-arranges to allow user to satisfy homing and simulate
     * cutting, by use of a switch box and loop-back jumper CPC.  Instantiates the
     * DataAccessObj which provides access to the specific drive system type.
     * @throws IOException  Is this needed?
     */
    @ Override
    public void convertFile() throws IOException {
        String system = m_operate.getSelectedSystem();
        m_dataAccess = new DataAccessObj( system );
        int sthcTotal;
        int agTHCTotal;
        int row1_NextIndex = 1;
        int row2_NextIndex = 9;
        int row3_NextIndex = 17;
        int torchCollisionLoc = 16;
        int typeLoc;


        // Determine specific tools, bevel heads, pipe axes, THC's that are installed in Machine screen
        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.FP.getName() ) > 0 ) {
            m_frontPanelInstalled = true;
        }
       
        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.BEVEL_AXES.getName() ) > 0 ) {
            m_bevelInstalled = true;
        }

        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_BEVEL.getName() ) > 0 ) {
            m_dualBevelInstalled = true;
        }

        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_TRANS.getName() ) > 0 ) {
            m_dualTransInstalled = true;
        }

        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.NO_ROTATE_TILT.getName() ) > 0 ) {
            m_noRotateTilt = true;
        }

        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.ONE_ROTATE_TILT.getName() ) > 0 ) {
            m_oneRotateTilt = true;
        }

        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_GANTRY.getName() ) > 0 ) {
            m_dualGantryInstalled = true;
        }

        if( getParamValue( BLOCK.AXIS_7.getName(), PARAMETER.ROTATING_TRANS.getName() ) > 0 ) {
            m_isRotatingTrans = true;
        }

        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.X_AXIS_ORIENTATION.getName() ) > 0 ) {
            m_xOnRail = true;
        }

        if( getParamValue( BLOCK.MACHINE.getName(), PARAMETER.DUAL_TILTING.getName() ) > 0 ) {
            m_dualTiltInstalled = true;
        }


        // Convert Speed parameters
        replaceAllParams( BLOCK.SPEEDS.getName(), m_dataAccess.getSpeedParams() );


        // Convert THC parameters
        if(( sthcTotal = getParamValue( BLOCK.MACHINE.getName(), PARAMETER.STHC.getName() )) > 0 ) {
            m_sthcInstalled = true;
            m_dataAccess.addTHCDefaults();

            for( int i = 0; i < sthcTotal; i++ ) {
                replaceAllParams( new StringBuilder( "[THC" ).append( i + 1 ).append( "]\r\n" ).toString(), m_dataAccess.getTHCAxisParams() );
                replaceAllParams( new StringBuilder( "[THC" ).append( i + 1 ).append( "]\r\n" ).toString(), m_dataAccess.getAxesParams() );
            }

            replaceAllParams( BLOCK.AIC.getName(), m_dataAccess.getTHCAnalogParams() );
            replaceAllParams( BLOCK.MACHINE.getName(), m_dataAccess.getTHCMachineParams() );

            addInput( row1_NextIndex++, INPUT_NUM.NCS_1.getValue() );

            if( sthcTotal >= 2 ) {
                addInput( row1_NextIndex++, INPUT_NUM.NCS_2.getValue() );

                if( sthcTotal >= 3 ) {
                    addInput( row1_NextIndex++, INPUT_NUM.NCS_3.getValue() );

                    if( sthcTotal >= 4 ) {  // Only supporting 4 STHC's at this time.
                        addInput( row1_NextIndex++, INPUT_NUM.NCS_4.getValue() );
                    }
                }
            }
        }
        else if(( agTHCTotal = getParamValue( BLOCK.MACHINE.getName(), PARAMETER.AG.getName() )) > 0 ) {
            m_arcGlideInstalled = true;
            addInput( row1_NextIndex++, INPUT_NUM.RDY_TO_FIRE_1.getValue() );

            if( agTHCTotal >= 2 ) {
                addInput( row1_NextIndex++, INPUT_NUM.RDY_TO_FIRE_2.getValue() );

                if( agTHCTotal >= 3 ) {
                    addInput( row1_NextIndex++, INPUT_NUM.RDY_TO_FIRE_3.getValue() );

                    if( agTHCTotal == 4 ) {
                        addInput( row1_NextIndex++, INPUT_NUM.RDY_TO_FIRE_4.getValue() );
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
        addInput( row2_NextIndex++, INPUT_NUM.DRIVE_DISABLED.getValue() );


        // Convert X & Y Axes parameters and homing inputs
        replaceAllParams( BLOCK.AXIS_1.getName(), m_dataAccess.getAxesParams() );
        replaceAllParams( BLOCK.AXIS_2.getName(), m_dataAccess.getAxesParams() );

        if( m_xOnRail ) {
            addInput( row1_NextIndex++, INPUT_NUM.X_NEG_OT.getValue() );
            addInput( row2_NextIndex++, INPUT_NUM.X_POS_OT.getValue() );
            addInput( row1_NextIndex++, INPUT_NUM.Y_NEG_OT.getValue() );
            addInput( row2_NextIndex++, INPUT_NUM.Y_POS_OT.getValue() );
        }
        else {
            addInput( row1_NextIndex++, INPUT_NUM.Y_NEG_OT.getValue() );
            addInput( row2_NextIndex++, INPUT_NUM.Y_POS_OT.getValue() );
            addInput( row1_NextIndex++, INPUT_NUM.X_NEG_OT.getValue() );
            addInput( row2_NextIndex++, INPUT_NUM.X_POS_OT.getValue() );
        }


        // Convert Dual Transverse parameters and add its inputs
        if( m_dualTransInstalled ) {
            replaceAllParams( BLOCK.AXIS_7.getName(), m_dataAccess.getAxesParams() );

            if( m_isRotatingTrans ) {
                changeParamValue( BLOCK.AXIS_7.getName(),  "EncoderCounts(english)=", BEVEL.ENCODER_CNTS.getValue() );
                changeParamValue( BLOCK.AXIS_7.getName(), "EncoderCounts(metric)=", BEVEL.ENCODER_CNTS.getValue() );
                addInput( row1_NextIndex++, INPUT_NUM.ROT_2_HOME.getValue() );
                addInput( row2_NextIndex++, INPUT_NUM.DUAL_HEAD_COLLISION.getValue() );
            }
            else {             
                if( m_xOnRail ) {
                    addInput( row1_NextIndex++, INPUT_NUM.Y_POS_OT.getValue() );
                }
                else {
                    addInput( row1_NextIndex++, INPUT_NUM.X_POS_OT.getValue() );
                }

                addInput( row1_NextIndex - 3, INPUT_NUM.DUAL_HEAD_COLLISION.getValue() );
                torchCollisionLoc = row2_NextIndex++;
            }

            if( row1_NextIndex < 8 ) {
                
                addInput( row1_NextIndex++, INPUT_NUM.PARK_HEAD_1.getValue() );
                addInput( row2_NextIndex++, INPUT_NUM.PARK_HEAD_2.getValue() );
            }
            else {
                addInput( row3_NextIndex++, INPUT_NUM.PARK_HEAD_1.getValue() );
                addInput( row3_NextIndex++, INPUT_NUM.PARK_HEAD_2.getValue() );
            }
        }


        // Convert Bevel Axes parameters and add homing inputs
        if( m_bevelInstalled && ( m_dualBevelInstalled && !m_noRotateTilt || !m_dualBevelInstalled )) {  // Single bevel head installed
            changeParamValue( BLOCK.MACHINE.getName(), BEVEL.AUTO_HOME.getName(), BEVEL.AUTO_HOME.getValue() );

            replaceAllParams( BLOCK.ROTATE.getName(), m_dataAccess.getAxesParams() );
            changeParamValue( BLOCK.ROTATE.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );
            changeParamValue( BLOCK.ROTATE.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );

            replaceAllParams( BLOCK.TILT.getName(), m_dataAccess.getAxesParams() );
            changeParamValue( BLOCK.TILT.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );
            changeParamValue( BLOCK.TILT.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );

            if( m_dualTiltInstalled ) {
                if( row1_NextIndex < 8 ) {
                    addInput( row1_NextIndex++, INPUT_NUM.TILT_POS_OT.getValue() );
                    addInput( row2_NextIndex++, INPUT_NUM.TILT_NEG_OT.getValue() );
                    addInput( row1_NextIndex++, INPUT_NUM.TILT2_POS_OT.getValue() );
                    addInput( row2_NextIndex++, INPUT_NUM.TILT2_NEG_OT.getValue() );
                }
                else {
                    addInput( row3_NextIndex++, INPUT_NUM.TILT_POS_OT.getValue() );
                    addInput( row3_NextIndex++, INPUT_NUM.TILT2_POS_OT.getValue() ); 
                }
            }
            else {
                if( row1_NextIndex < 8 ) {
                    addInput( row1_NextIndex++, INPUT_NUM.TILT_POS_OT.getValue() );
                    addInput( row2_NextIndex++, INPUT_NUM.TILT_NEG_OT.getValue() );
                    addInput( row1_NextIndex++, INPUT_NUM.ROTATE_HOME.getValue() );
                    torchCollisionLoc = row2_NextIndex++;
                }
                else {
                    addInput( row3_NextIndex++, INPUT_NUM.TILT_POS_OT.getValue() );
                    addInput( row3_NextIndex++, INPUT_NUM.ROTATE_HOME.getValue() );
                }
            }

            if( m_dualBevelInstalled && !m_oneRotateTilt ) { // Dual Bevel heads installed
                if( m_dualTiltInstalled ) {
                    replaceAllParams( BLOCK.DUAL_TILT.getName(), m_dataAccess.getAxesParams() );
                    changeParamValue( BLOCK.DUAL_TILT.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
                    changeParamValue( BLOCK.DUAL_TILT.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );

                    if( row1_NextIndex < 8 ) {
                        addInput( row1_NextIndex++, INPUT_NUM.TILT3_POS_OT.getValue() );
                        addInput( row2_NextIndex++, INPUT_NUM.TILT3_NEG_OT.getValue() );
                        addInput( row1_NextIndex++, INPUT_NUM.TILT4_POS_OT.getValue() );
                        addInput( row2_NextIndex++, INPUT_NUM.TILT4_NEG_OT.getValue() );
                    }
                    else {
                        addInput( row3_NextIndex++, INPUT_NUM.TILT3_POS_OT.getValue() );
                        addInput( row3_NextIndex++, INPUT_NUM.TILT4_POS_OT.getValue() );
                    }
                }
                else {
                    replaceAllParams( BLOCK.DUAL_ROTATE.getName(), m_dataAccess.getAxesParams() );
                    changeParamValue( BLOCK.DUAL_ROTATE.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
                    changeParamValue( BLOCK.DUAL_ROTATE.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );

                    if( row1_NextIndex < 8 ) {
                        addInput( row1_NextIndex++, INPUT_NUM.TILT3_POS_OT.getValue() );
                        addInput( row2_NextIndex++, INPUT_NUM.TILT3_NEG_OT.getValue() );
                        addInput( row1_NextIndex++, INPUT_NUM.ROT_2_HOME.getValue() );
                        torchCollisionLoc = row2_NextIndex++;
                    }
                }
            }
        }


        // Add Torch Collision input
        if( torchCollisionLoc < 17 ) {
            addInput( torchCollisionLoc, INPUT_NUM.TORCH_COLLISION.getValue() );
        }
        else if( row3_NextIndex < 25 ) {
            addInput( row3_NextIndex, INPUT_NUM.TORCH_COLLISION.getValue() );
        }


        // Re-assign Cut Sense inputs beginning at input 40
        if( getParamValue( BLOCK.IO.getName(), INPUT_NUM.CUT_MARK_SENSE.getName() ) > 0 ) {
            addInput( 40, INPUT_NUM.CUT_MARK_SENSE.getValue() );
        }
        else if( getParamValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_1.getName() ) > 0 ) {
            addInput( 40, INPUT_NUM.CUT_SENSE_1.getValue() );

            if( getParamValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_2.getName() ) > 0 ) {
                addInput( 41, INPUT_NUM.CUT_SENSE_2.getValue() );
            }

            if( getParamValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_3.getName() ) > 0 ) {
                addInput( 42, INPUT_NUM.CUT_SENSE_3.getValue() );
            }

            if( getParamValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_4.getName() ) > 0 ) {
                addInput( 43, INPUT_NUM.CUT_SENSE_4.getValue() );
            }
        }

        // Re-assign Cut Control outputs beginning at output 40
        if( getParamValue( BLOCK.IO.getName(), OUTPUT_NUM.CUT_CONTROL.getName() ) > 0 ) {
            addOutput( 40, OUTPUT_NUM.CUT_CONTROL.getValue() );
        }
        else if( getParamValue( BLOCK.IO.getName(), OUTPUT_NUM.CUT_CONTROL.getName() ) > 0 ) {
            addOutput( 40, OUTPUT_NUM.CUT_CONTROL_1.getValue() );

            if( getParamValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_2.getName() ) > 0 ) {
                addOutput( 41, OUTPUT_NUM.CUT_CONTROL_2.getValue() );
            }

            if( getParamValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_3.getName() ) > 0 ) {
                addOutput( 42, OUTPUT_NUM.CUT_CONTROL_3.getValue() );
            }

            if( getParamValue( BLOCK.IO.getName(), INPUT_NUM.CUT_SENSE_4.getName() ) > 0 ) {
                addOutput( 43, OUTPUT_NUM.CUT_CONTROL_4.getValue() );
            }
        }


        // Re-assign Drive Enable output
        addOutput( 25, OUTPUT_NUM.DRIVE_ENABLE.getValue() );


        // Merge in IO settings into parameter file
        addParamsToMap( BLOCK.IO.getName(), m_IOParamMap );
        shuffleIO();
        replaceAllParams( BLOCK.IO.getName(), m_IOParamMap );


        // Set all port settings to none and merge changes into parameter file
        addParamsToMap( BLOCK.LINK.getName(), m_LinkParamMap );
        resetLinkSettings();
        replaceAllParams( BLOCK.LINK.getName(), m_LinkParamMap );
    }


    /**
     * Add's an input type and input number to the InputType Map and the InputNumber
     * Map.
     * @param typeIndex - The physical input to be assigned to an input device
     * @param numIndex  - The input device to be assign to an input location
     */
    public void addInput( int typeIndex, int numIndex ) {
        m_inputTypeMap.put( new StringBuilder( INPUT ).append( typeIndex ).append( TYPE ).toString(), numIndex );
        m_inputNumberMap.put( new StringBuilder( INPUT ).append( numIndex ).append( NUMBER   ).toString(), typeIndex );
    }


    /**
     * Add's an output type and output number to the OutputType Map and the OutputNumber
     * Map.
     * @param typeIndex - The physical output to be assigned to a device
     * @param numIndex  - The output device to be assigned to an output location
     */
    public void addOutput( int typeIndex, int numIndex ) {
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
                int inValue = getParamValue( BLOCK.IO.getName(), inputType );
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
                int outValue = getParamValue( BLOCK.IO.getName(), outputType );
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
     * serial ports assigned for the HPR to port "None" to allow for simulation. 
     */
    private void resetLinkSettings() {
        int numLoc = 1;
        int typeLoc = 1;

        for( Entry< String, Integer > entry : m_LinkParamMap.entrySet() ){
            if( entry.getKey().contains( new StringBuilder( PORT ).append( typeLoc ).append( TYPE ) )) {
                if(( entry.getValue() > 2 && entry.getValue() < 5 ) || entry.getValue() > 6 ) {
                    entry.setValue( 0 );
                }
                typeLoc++;
            }

            if( entry.getKey().contains( new StringBuilder( PORT ).append( numLoc ).append( NUMBER ) )) {
                if(( numLoc > 2 && numLoc < 5 ) || numLoc > 6  ) {
                    entry.setValue( 0 );
                }
            }
        }
    }


    /**
     * @param file
     * @throws IOException
     */
    @ Override
    public void writeParam( File file ) throws IOException {

        BufferedWriter buff_writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file ), StandardCharsets.UTF_8 ));
        StringBuilder checksum = new StringBuilder( "Checksum=" ).append( m_checksum ).append( EMPTY_LINE );

        m_paramList.remove( 0 );
        m_paramList.add( 0, checksum.toString() );

        for( String str : m_paramList ) {
            buff_writer.write( str );
        }

        if( buff_writer != null ) {
            buff_writer.close();
        }
    }
}


