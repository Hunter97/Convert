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

    private final ArrayList< String > m_paramList = new ArrayList<>();

    private final Map< String, Integer > m_IOParamMap = new LinkedHashMap<>();
    //private final EnumMap< INPUT_TYPE, INPUT_NUM > m_inputEnumMap = new EnumMap<>( INPUT_TYPE.class );
    private final Map< String, Integer > m_inputTypeMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_LinkParamMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_inputNumberMap = new LinkedHashMap<>();

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
            StringBuffer sb = new StringBuffer();
            int thisChar;
            int lineFeed = 10;

            while(( thisChar = buffer.read() ) != -1 ) {
                sb.append(( char )thisChar );

                if( thisChar == lineFeed ) {
                    m_paramList.add( sb.toString() );
                    sb = new StringBuffer();
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
        ListIterator< String > listItr;
        String param;
        String emptyLine = "\r\n";
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            listItr = m_paramList.listIterator( index +1 );
            while( !( param = listItr.next() ).startsWith( emptyLine )  && listItr.hasNext() ) {
                if( !param.equals( emptyLine )) {
                    String[] key = param.split( REG_EXP );
                    StringBuilder sb = new StringBuilder( key[0] ).append( "=" );

                    try {
                        map.put( sb.toString(), Integer.parseInt( key[ 1 ] ));
                    }
                    catch( NumberFormatException e ) {
                        m_operate.setStatus( Color.RED,"Exception converting " + sb.toString(), "Key = " + key[ 1 ] + " , set value to 0" );
                        map.put( sb.toString(), 0 );
                    }
                }
            }
        }

        return map;
    }


    /**
     * Changes a parameter value from the parameter list to the argument value.
     * Parses the parameter list, starting after the argument block title, for a
     * match to the String argument.  If a match is found, the argument value is
     * added to the parameter, in place of the original value.
     * @param blockTitle    - Title of parameter block in list (i.e. [Machine]) 
     * @param paramName     - Parameter setting to be searched/changed in parameter list
     * @param value         - Value to replace in parameter list
     */
    public void changeParamValue( String blockTitle, String paramName, int value ) {
        int index;
        String[] key = null;
        String param;
        String emptyLine = "\r\n";

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) { //&& ( index = m_paramList..indexOf( paramName )) > -1 ) {
            ListIterator< String > listItr = m_paramList.listIterator( index +1 );
            while( !( param = listItr.next() ).startsWith( emptyLine ) && listItr.hasNext() ) {
                if( param.startsWith( paramName )) {
                    key = param.split( REG_EXP );
                    break;
                }
            }

            m_paramList.add(index, new StringBuilder( key[0] ).append( value ).toString() );
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
        int index;
        int value = -1;
        String param;
        String emptyLine = "\r\n";

        if(( index = m_paramList.indexOf( blockTitle )) != -1) {
            ListIterator< String > listItr = m_paramList.listIterator( index +1 );
            while( !( param = listItr.next() ).startsWith( emptyLine ) &&  listItr.hasNext() ) {
                if( param.startsWith( paramName )) {
                    String[] key = param.split( REG_EXP );
                    value = Integer.parseInt( key[ 1 ] );
                    break;
                }
            }
        }
        /*else {
            return value;
        }*/

        return value;
    }


    /**
     * Find/replace the parameters within the parameter list with the
     * parameters contained within the argument Map.  Starts searching the parameter 
     * list after the argument block title.
     * @param blockTitle    - Title of the parameter block in list (i.e. [Machine])
     * @param map           - Map containing a set of default parameters
     */
    private void replaceParams( String blockTitle, Map< String, Integer > map ) throws ArrayIndexOutOfBoundsException, NullPointerException {

        ListIterator< String > paramIterator;
        List< String > paramSubList = new ArrayList<>();
        String param;
        String emptyLine = "\r\n";
        int startIndex;
        int replaceIndex;

        if(( startIndex = m_paramList.indexOf( blockTitle )) != -1 ) {
            paramSubList.addAll( m_paramList.subList( startIndex, ( m_paramList.size() - startIndex )));
            paramIterator = paramSubList.listIterator();
            while( !( param = paramIterator.next() ).startsWith( emptyLine ) && paramIterator.hasNext() ) {
                StringBuilder sb = new StringBuilder();
                for( String key : map.keySet() ) {
                    if( param.startsWith( key )) {
                        replaceIndex = startIndex + paramSubList.indexOf( param );
                        sb.append( key ).append( map.get( key )).append( "\r\n" );
                        m_paramList.set( replaceIndex, sb.toString() );
                        //System.out.format( m_paramList.get( index ));
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
     * DataAccessObj which provides access to the data that will be uses in the conversion.
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
        replaceParams( BLOCK.SPEEDS.getName(), m_dataAccess.getSpeedParams() );


        // Convert THC parameters
        if(( sthcTotal = getParamValue( BLOCK.MACHINE.getName(), PARAMETER.STHC.getName() )) > 0 ) {
            m_sthcInstalled = true;
            m_dataAccess.addTHCDefaults();

            for( int i = 0; i < sthcTotal; i++ ) {
                replaceParams( new StringBuilder( "[THC" ).append( i + 1 ).append( "]\r\n" ).toString(), m_dataAccess.getTHCAxisParams() );
                replaceParams( new StringBuilder( "[THC" ).append( i + 1 ).append( "]\r\n" ).toString(), m_dataAccess.getAxesParams() );
            }

            replaceParams( BLOCK.AIC.getName(), m_dataAccess.getTHCAnalogParams() );
            replaceParams( BLOCK.MACHINE.getName(), m_dataAccess.getTHCMachineParams() );

            addInput( row1_NextIndex++, INPUT_NUM.NCS_1.getValue() );

            if( sthcTotal >= 2 ) {
                addInput( row1_NextIndex++, INPUT_NUM.NCS_2.getValue() );

                if( sthcTotal >= 3 ) {
                    addInput( row1_NextIndex++, INPUT_NUM.NCS_3.getValue() );

                    if( sthcTotal >= 4 ) {  // Can have up to 8 STHC's, only supporting 4 at this time.
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
            replaceParams( BLOCK.DUAL_GANTRY.getName(), m_dataAccess.getAxesParams() );
        }


        // Convert X & Y Axes parameters
        replaceParams( BLOCK.AXIS_1.getName(), m_dataAccess.getAxesParams() );
        replaceParams( BLOCK.AXIS_2.getName(), m_dataAccess.getAxesParams() );

        if( m_dualTransInstalled ) {
            replaceParams( BLOCK.AXIS_7.getName(), m_dataAccess.getAxesParams() );

            if( m_isRotatingTrans ) {
                changeParamValue( BLOCK.AXIS_7.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
                changeParamValue( BLOCK.AXIS_7.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );
                addInput( row1_NextIndex++, INPUT_NUM.X_NEG_OT.getValue() );
                addInput( row1_NextIndex + 7, INPUT_NUM.X_POS_OT.getValue() );
                addInput( row1_NextIndex++, INPUT_NUM.Y_NEG_OT.getValue() );
                addInput( row1_NextIndex + 7, INPUT_NUM.Y_POS_OT.getValue() );
                addInput( row1_NextIndex++, INPUT_NUM.ROT_2_HOME.getValue() );
            }
            else {
                if( m_xOnRail ) {
                    addInput( row1_NextIndex++, INPUT_NUM.X_NEG_OT.getValue() );
                    addInput( row1_NextIndex + 7, INPUT_NUM.X_POS_OT.getValue() );
                    addInput( row1_NextIndex++, INPUT_NUM.Y_NEG_OT.getValue() );
                    addInput( row1_NextIndex++, INPUT_NUM.Y_POS_OT.getValue() );
                }
                else {
                    addInput( row1_NextIndex++, INPUT_NUM.Y_NEG_OT.getValue() );
                    addInput( row1_NextIndex + 7, INPUT_NUM.Y_POS_OT.getValue() );
                    addInput( row1_NextIndex++, INPUT_NUM.X_NEG_OT.getValue() );
                    addInput( row1_NextIndex++, INPUT_NUM.X_POS_OT.getValue() );
                }
            }

            addInput( row1_NextIndex + 7, INPUT_NUM.DUAL_HEAD_COLLISION.getValue() );
            addInput( row1_NextIndex++, INPUT_NUM.PARK_HEAD_1.getValue() );
            addInput( row1_NextIndex + 7, INPUT_NUM.PARK_HEAD_2.getValue() );
        }
        else {
            addInput( row1_NextIndex++, INPUT_NUM.X_NEG_OT.getValue() );
            addInput( row1_NextIndex + 7, INPUT_NUM.X_POS_OT.getValue() );
            addInput( row1_NextIndex++, INPUT_NUM.Y_NEG_OT.getValue() );
            addInput( row1_NextIndex + 7, INPUT_NUM.Y_POS_OT.getValue() );
        }


        // Convert Bevel Axes parameters
        if( m_bevelInstalled && ( m_dualBevelInstalled && !m_noRotateTilt || !m_dualBevelInstalled )) {
            changeParamValue( BLOCK.MACHINE.getName(), BEVEL.AUTO_HOME.getName(), BEVEL.AUTO_HOME.getValue() );

            replaceParams( BLOCK.ROTATE.getName(), m_dataAccess.getAxesParams() );
            changeParamValue( BLOCK.ROTATE.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
            changeParamValue( BLOCK.ROTATE.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );

            replaceParams( BLOCK.TILT.getName(), m_dataAccess.getAxesParams() );
            changeParamValue( BLOCK.TILT.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
            changeParamValue( BLOCK.TILT.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );

            
            if( m_dualTiltInstalled ) {
                if( row1_NextIndex < 8 ) {
                    addInput( row1_NextIndex++, INPUT_NUM.TILT_PLUS.getValue() );
                    addInput( row1_NextIndex + 7, INPUT_NUM.TILT_MINUS.getValue() );
                    addInput( row1_NextIndex++, INPUT_NUM.TILT2_POS_OT.getValue() );
                    addInput( row1_NextIndex + 7, INPUT_NUM.TILT2_NEG_OT.getValue() );
                }
                else {
                    addInput( row3_NextIndex++, INPUT_NUM.TILT_PLUS.getValue() );
                    addInput( row3_NextIndex++, INPUT_NUM.TILT_MINUS.getValue() );
                    addInput( row3_NextIndex++, INPUT_NUM.TILT2_POS_OT.getValue() );
                    addInput( row3_NextIndex++, INPUT_NUM.TILT2_NEG_OT.getValue() );
                }
            }
            else {
                if( row1_NextIndex < 8 ) {
                   addInput( row1_NextIndex++, INPUT_NUM.TILT_PLUS.getValue() );
                    addInput( row1_NextIndex + 7, INPUT_NUM.TILT_MINUS.getValue() );
                    addInput( row1_NextIndex++, INPUT_NUM.ROTATE_PLUS.getValue() );
                    addInput( row1_NextIndex + 7, INPUT_NUM.ROTATE_MINUS.getValue() );
                }
                else {
                    addInput( row3_NextIndex++, INPUT_NUM.TILT_PLUS.getValue() );
                    addInput( row3_NextIndex++, INPUT_NUM.TILT_MINUS.getValue() );
                    addInput( row3_NextIndex++, INPUT_NUM.ROTATE_PLUS.getValue() );
                    addInput( row3_NextIndex++, INPUT_NUM.ROTATE_MINUS.getValue() );
                }
            }

            if( m_dualBevelInstalled && !m_oneRotateTilt ) {
                replaceParams( BLOCK.DUAL_ROTATE.getName(), m_dataAccess.getAxesParams() );
                changeParamValue( BLOCK.DUAL_ROTATE.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
                changeParamValue( BLOCK.DUAL_ROTATE.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );

                replaceParams( BLOCK.DUAL_TILT.getName(), m_dataAccess.getAxesParams() );
                changeParamValue( BLOCK.DUAL_TILT.getName(), BEVEL.ENCODER_CNTS.getName(), BEVEL.ENCODER_CNTS.getValue() );
                changeParamValue( BLOCK.DUAL_TILT.getName(), BEVEL.SERVO_ERROR.getName(), BEVEL.SERVO_ERROR.getValue() );

                // Need to add rotate 2 OT's and verify tilt_plus/minus & rotate_plus/minus are OT's.
                // Add check for dual- dual tilting bevel heads
                //addInput( row3_NextIndex++, INPUT_NUM.)
            }
        }

        // Add Drive Disabled and Torch Collsion Inputs
        addInput( row2_NextIndex++, INPUT_NUM.DRIVE_DISABLED.getValue() );

        if( row1_NextIndex < 17 ) {
            addInput( 16, INPUT_NUM.TORCH_COLLISION.getValue() );
        }
        else if( row3_NextIndex < 22 ) {
            addInput( 22, INPUT_NUM.TORCH_COLLISION.getValue() );
        }


        addParamsToMap( BLOCK.IO.getName(), m_IOParamMap );
        mergeIOMaps();
        replaceParams( "[I/O]\r\n", m_IOParamMap );


         // Find/replace serial link settings
        addParamsToMap( BLOCK.LINK.getName(), m_LinkParamMap );
        resetLinkSettings();
        replaceParams( BLOCK.LINK.getName(), m_LinkParamMap );
    }


    /**
     * Add's an input type and input number to the InputType Map and the InputNumber
     * Map.
     * @param typeIndex - The next physical input to be assigned
     * @param numIndex  - The input device to be assign to an input
     */
    public void addInput( int typeIndex, int numIndex ) {
        for( INPUT_TYPE type : INPUT_TYPE.values() ) {
            if( type.getValue() == typeIndex ) {
                for( INPUT_NUM number : INPUT_NUM.values() ) {
                    if( number.getValue() == numIndex ) {
                        m_inputTypeMap.put( type.getName(), number.getValue() );
                        m_inputNumberMap.put( number.getName(), type.getValue() );
                        break;
                    }
                }

                break;
            }
        }
    }
    


    /**
     * Merges default IO parameters from the container class into the local IO Map.
     * Re-assign IO in the local IO Map, if not part of default IO parameters.
     * 
     * @param listItr
     * @param set
     * @param nextSection
     */
    private void mergeIOMaps() {
        Iterator< Entry< String, Integer >> itr = m_IOParamMap.entrySet().iterator();
        Entry< String, Integer > entry;
        StringBuilder inputType;
        StringBuilder inputNumber;
        String input = "Input";
        String output = "Output";
        String type = "Type=";
        String number = "Number=";
        int inNumLoc = 1;
        int inTypeLoc = 49;
        int outNumLoc = 1;
        int outTypeLoc = 49;

        // Set all input logic to normally open
        do {
            entry = itr.next();
            entry.setValue( 0 );
        } while( !entry.getKey().startsWith( "InputLogic20=" ) && itr.hasNext() );


        // Merge default inputs into this map and re-assign unlike inputs of this map starting at input 49
        // Set input type's to 0, if and only if input type is not re-assigned by a default parameter
        do {
            entry = itr.next();
            if( !m_inputNumberMap.containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                inputType = new StringBuilder( input ).append( entry.getValue() ).append( type );
                if( m_inputTypeMap.containsKey( inputType.toString() )) {
                    m_IOParamMap.put( inputType.toString(), inTypeLoc );
                    m_IOParamMap.put( new StringBuilder( input ).append( inTypeLoc++ ).append( number ).toString(), entry.getValue() );
                }

                inputType = new StringBuilder( input + inTypeLoc + type );
                //buildStr.append( input + inTypeLoc + type );
                m_IOParamMap.put( inputType.toString(), inNumLoc );
                entry.setValue( inTypeLoc++ );
            }
            else if( m_dataAccess.getInputParams().containsKey( entry.getKey() )) {
                if( entry.getValue() > 0 ) {
                    inputType = new StringBuilder( input + entry.getValue() + type );
                    //buildStr.append( input + entry.getValue() + type );
                    if( !m_dataAccess.getInputParams().containsKey( inputType.toString() )) {
                        m_IOParamMap.put( inputType.toString(), 0 );
                    }

                }

                int defaultValue = m_dataAccess.getInputParams().get( entry.getKey() );
                inputType = new StringBuilder( input + defaultValue + type );
                //buildStr.append( input + defaultValue + type );
                m_IOParamMap.put( inputType.toString(), inNumLoc );
                entry.setValue( defaultValue );
            }

            inNumLoc++;

        } while( !entry.getKey().startsWith( "Input1Type=" ) && itr.hasNext() );


        // Iterate over all InputTypes to get to output logic then set all output logic to 0
        do {
            if( !entry.getKey().startsWith( "Input" )) {
                entry.setValue( 0 );
            }

            entry = itr.next();

        } while( !entry.getKey().startsWith( "OutputLogic20=" ) && itr.hasNext() );


        // Merge default output into this map and re-assign unlike outputs of this map starting at output 49
        // Set output type's to 0, if and only if output type is not re-assigned by a default parameter
        while( !(( entry = itr.next() ).getKey().startsWith( "Output1Type=" )) && itr.hasNext() ) {
            if( !m_dataAccess.getOutputParams().containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                inputType = new StringBuilder( output + entry.getValue() + type );
                //buildStr.append( output + entry.getValue() + type );
                if( !m_dataAccess.getOutputParams().containsKey( inputType.toString() )) {
                    m_IOParamMap.put( inputType.toString(), 0 );
                }

                inputType = new StringBuilder( output + outTypeLoc + type );
                //buildStr.append( output + outTypeLoc + type );
                m_IOParamMap.put( inputType.toString(), outNumLoc );
                entry.setValue( outTypeLoc++ );
            }
            else if( m_dataAccess.getOutputParams().containsKey( entry.getKey() )) {
                if( entry.getValue() > 0 ) {
                    inputType = new StringBuilder( output + entry.getValue() + type );
                    //buildStr.append( output + entry.getValue() + type );
                    if( !m_dataAccess.getOutputParams().containsKey( inputType.toString() )) {
                        m_IOParamMap.put( inputType.toString(), 0 );
                    }
                }

                int defaultValue = m_dataAccess.getOutputParams().get( entry.getKey() );
                inputType = new StringBuilder( output + defaultValue + type );
                //buildStr.append( output + defaultValue + type );
                m_IOParamMap.put( inputType.toString(), outNumLoc );
                entry.setValue( defaultValue );
            }

            outNumLoc++;
        }
    }


    /**
     * Re-assigns the port settings in the Link parameter map to None.  Sets the
     * serial ports assigned for the HPR to port "None" to allow for simulation. 
     */
    private void resetLinkSettings() {
        StringBuilder portType;
        StringBuilder portNum;
        String port = "Port";
        String number = "Number=";
        String type = "Type=";
        int numLoc = 1;
        int typeLoc = 1;

        for( Entry< String, Integer > entry : m_LinkParamMap.entrySet() ){

            portType = new StringBuilder( port ).append( typeLoc ).append( type );
            portNum = new StringBuilder( port ).append( numLoc ).append( number );

            if( entry.getKey().contains( portType )) {
                if(( entry.getValue() > 2 && entry.getValue() < 5 ) || entry.getValue() > 6 ) {
                    entry.setValue( 0 );
                }
                typeLoc++;
            }

            if( entry.getKey().contains( portNum )) {
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
        StringBuilder checksum = new StringBuilder( "Checksum=" + m_checksum + "\r\n" );

        //checksum.append( "Checksum=" + m_checksum + "\r\n"  );
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


