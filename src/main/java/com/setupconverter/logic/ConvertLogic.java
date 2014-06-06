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

    private final ArrayList< String > m_paramList = new ArrayList<>();

    private final Map< String, Integer > m_IOParamMap = new LinkedHashMap<>();
    private final Map< String, Integer > m_LinkParamMap = new LinkedHashMap<>();

    private DataAccessObj m_dataAccess;
    //private Bench m_bench;

    public OperateConverter m_ui;

    private File m_configFile = null;
    private int m_checksum = 0;

    public boolean m_hasDT = false;
    public int[] m_axisNum = new int[ TOTAL_AXIS_BLOCKS ];


    /**
     * @param file
     * @param ui
     * @throws IOException
     */
    //public ConvertLogic( File file, IComponents ui ) throws IOException {//
    public ConvertLogic( File file, OperateConverter ui ) throws IOException {//
        m_configFile = file;
        loadParams( m_configFile );
        m_ui = ui;
    }


    /**
     * Loads each line of a configuration file into a a List< String >.  Each
     * line is delimited by a carriage return/line feed combination (\r\n}.
     * The File argument is loaded into a BufferedReader that is wrapped by a
     * FileInputStream that uses the standard character set UTF_8.
     * 
     * @param       file - File reference to the configuration file
     * @category    StandardCharsets.UTF_8
     * @throws      IOException
     */
    @ Override
    public final void loadParams( File file ) throws IOException {

        BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( file ), StandardCharsets.UTF_8 ));
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

        //if( buffer != null ) {
            buffer.close();
        //}
    }


    /**
     * Gets all file parameters, starting after the argument block title, and adds
     * the parameters to a Map< String, Integer > and returns the Map.
     * @param map           - Map to store all file parameters
     * @param blockTitle    - Title of block within parameter List (i.e. [Machine])
     */
    public void addParamsToMap( String blockTitle, Map< String, Integer > map  ) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        ListIterator< String > listItr;
        String param;
        String emptyLine = "\r\n";
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            listItr = m_paramList.listIterator( index +1 );
            while( !( param = listItr.next() ).startsWith( emptyLine )  && listItr.hasNext() ) {
                if( !param.equals( emptyLine )) {
                    //StringBuilder sb = new StringBuilder( param.length() );
                    String[] key = param.split( "[=\\s\\.]+" );
                    StringBuilder sb = new StringBuilder( key[0] ).append( "=" );

                    try {
                        map.put( sb.toString(), Integer.parseInt( key[ 1 ] ));
                    }
                    catch( NumberFormatException e ) {
                        m_ui.setStatus( Color.RED,"Exception converting " + sb.toString(), "Key = " + key[ 1 ] + " , set value to 0" );
                        map.put( sb.toString(), 0 );
                    }
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
     * Parses the m_paramList List, starting after the argument block title, for a
     * match to the String argument.  If a match is found, the value of that parameter
     * is returned, otherwise a -1 is returned.
     * @param blockTitle    - Title of parameter block in list (i.e. [Machine])
     * @param paramName     - Parameter setting to be searched for within parameter List
     * @return              - The value of the parameter
     */
    public int getParamValue( String blockTitle, String paramName ) {
        int index;
        int value = -1;
        String param;

        if(( index = m_paramList.indexOf( blockTitle )) != -1) {
            ListIterator< String > listItr = m_paramList.listIterator( index +1 );
            while( listItr.hasNext() ) {
                param = listItr.next();
                if( param.startsWith( paramName )) {
                    String[] key = param.split( "[=\\s\\.]+" );
                    value = Integer.parseInt( key[ 1 ] );
                    break;
                }
            }
        }
        else {
            return value;
        }

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

        ListIterator< String > paramIterator = null;
        List< String > paramSubList = new ArrayList< String >();
        String param = null;
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
     * Parses setup file to determine the type of CNC (Hypath, Pico-path, or SERCOS),
     * the type of THC's installed (STHC or AG), the port settings for serial links,
     * and the types/number of axes installed.  Converts the original parameter file
     * to use the gains settings based on the drive type selected by user and re- 
     * arranges I/O to be used with a switch box.  The parameters are temporarily 
     * saved to Map's and then put back into the original parameter file.  The file
     * checksum is then recalculated.
     * @throws IOException  Is this needed?
     */
    @ Override
    public void convertFile() throws IOException {
        String system = m_ui.getSelectedSystem();
        int axisNum = 0;

        //m_bench = new Bench();
        //m_containers = new DiagnosticBrds();
        m_dataAccess = new DataAccessObj( system );

        // Find/replace THC parameters with parameters from container class
        int sthcTotal = getParamValue( BLOCK.MACHINE.getName(), "SensorTHCInstalled=" );
        if( sthcTotal > 0 ) {
            m_dataAccess.addTHCDefaults();
            for( int iter = 0; iter < sthcTotal; iter++, axisNum++ ) {
                replaceParams( new StringBuilder( "[THC" ).append( iter + 1 ).append( "]\r\n" ).toString(), m_dataAccess.getTHCAxisParams() );
                replaceParams( new StringBuilder( "[THC" ).append( iter + 1 ).append( "]\r\n" ).toString(), m_dataAccess.getAxesParams() );
            }

            replaceParams( BLOCK.AIC.getName(), m_dataAccess.getTHCAnalogParams() );
            replaceParams( BLOCK.MACHINE.getName(), m_dataAccess.getTHCMachineParams() );
        }

        // Find/replace parameters from Speed screen with parameters from container class
        replaceParams( BLOCK.SPEEDS.getName(), m_dataAccess.getSpeedParams() );
 
        // Find/replace serial link settings
        addParamsToMap( BLOCK.LINK.getName(), m_LinkParamMap );
        resetLinkSettings();
        replaceParams( BLOCK.LINK.getName(), m_LinkParamMap );

        // File/replace I/O settings
        addParamsToMap( "[I/O]\r\n", m_IOParamMap );
        mergeIOMaps();
        replaceParams( "[I/O]\r\n", m_IOParamMap );

        axisNum = 0;
        for( int i = 0; i < TOTAL_AXIS_BLOCKS; i++ ) {
            if( i == TOTAL_AXIS_BLOCKS - 1 ) {
                axisNum = 6;
            }

            replaceParams( new StringBuilder( "[Axis" + axisNum + "]\r\n" ).toString(), m_dataAccess.getAxesParams() );
            axisNum++;
        }


        /* Find all THC axis blocks and add to parameter List< String >()
        int totalTHCs = getParamValue( "[Machine]\r\n", "SensorTHCInstalled=" );
        for( int i = 1; i <= totalTHCs; i++ ) {
            //axis = new StringBuilder( "[THC" + i + "]\r\n" );
            //axis.append( "[THC" + i + "]\r\n" );
            replaceParams( new StringBuilder( "[THC" + i + "]\r\n" ).toString(), m_dataAccess.get.getTHCParams() );
        }*/


        // Find all Rotate/Tilt axis blocks and add to parameter List< String >()
        int singleBev = getParamValue( "[Machine]\r\n", "SkewRotatorInstalled=" );
        int dualBev = getParamValue( "[Machine]\r\n", "DualSkewRotatorInstalled=" );
        int noRotateTilt = getParamValue( "[Machine]\r\n", "NoRotateTilt=" );
        int oneRotateTilt = getParamValue( "[Machine]\r\n", "OneRotateTilt=" );

        if( singleBev > 0 && dualBev < 1 || singleBev > 0 && dualBev > 0 && oneRotateTilt > 0  ) {
            replaceParams( "[Rotate]\r\n", m_dataAccess.getBevelParams() );
            replaceParams( "[Tilt]\r\n", m_dataAccess.getBevelParams() );
        }
        else if( singleBev > 0 && dualBev > 0 && noRotateTilt < 1) {
            replaceParams( "[Rotate]\r\n", m_dataAccess.getBevelParams() );
            replaceParams( "[Tilt]\r\n", m_dataAccess.getBevelParams() );
            replaceParams( "[DualRotate]\r\n", m_dataAccess.getBevelParams() );
            replaceParams( "[DualTilt]\r\n", m_dataAccess.getBevelParams() );
        }


        // Find the Dual Gantry axis block and add to parameter List< String >()
        int dualGantryInstalled = getParamValue( "[Machine]\r\n", "DualGantryInstalled=" );
        if( dualGantryInstalled > 0 ) {
            replaceParams( "[DualGantry]\r\n", m_dataAccess.getAxesParams() );
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
        StringBuilder buildStr = null;
        String input = "Input";
        String output = "Output";
        String type = "Type=";
        int inNumLoc = 1;
        int inTypeLoc = 49;
        int outNumLoc = 1;
        int outTypeLoc = 49;

        // Set all input logic to 0
        do {
            entry = itr.next();
            entry.setValue( 0 );
        } while( !entry.getKey().startsWith( "InputLogic20=" ) && itr.hasNext() );


        // Merge default inputs into this map and re-assign unlike inputs of this map starting at input 49
        // Set input type's to 0, if and only if input type is not re-assigned by a default parameter
        do {
            entry = itr.next();
            if( !m_dataAccess.getInputParams().containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                buildStr = new StringBuilder( input + entry.getValue() + type );
                //buildStr.append( input + entry.getValue() + type );
                if( !m_dataAccess.getInputParams().containsKey( buildStr.toString() )) {
                    m_IOParamMap.put( buildStr.toString(), 0 );
                }

                buildStr = new StringBuilder( input + inTypeLoc + type );
                //buildStr.append( input + inTypeLoc + type );
                m_IOParamMap.put( buildStr.toString(), inNumLoc );
                entry.setValue( inTypeLoc++ );
            }
            else if( m_dataAccess.getInputParams().containsKey( entry.getKey() )) {
                if( entry.getValue() > 0 ) {
                    buildStr = new StringBuilder( input + entry.getValue() + type );
                    //buildStr.append( input + entry.getValue() + type );
                    if( !m_dataAccess.getInputParams().containsKey( buildStr.toString() )) {
                        m_IOParamMap.put( buildStr.toString(), 0 );
                    }

                }

                int defaultValue = m_dataAccess.getInputParams().get( entry.getKey() );
                buildStr = new StringBuilder( input + defaultValue + type );
                //buildStr.append( input + defaultValue + type );
                m_IOParamMap.put( buildStr.toString(), inNumLoc );
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
                buildStr = new StringBuilder( output + entry.getValue() + type );
                //buildStr.append( output + entry.getValue() + type );
                if( !m_dataAccess.getOutputParams().containsKey( buildStr.toString() )) {
                    m_IOParamMap.put( buildStr.toString(), 0 );
                }

                buildStr = new StringBuilder( output + outTypeLoc + type );
                //buildStr.append( output + outTypeLoc + type );
                m_IOParamMap.put( buildStr.toString(), outNumLoc );
                entry.setValue( outTypeLoc++ );
            }
            else if( m_dataAccess.getOutputParams().containsKey( entry.getKey() )) {
                if( entry.getValue() > 0 ) {
                    buildStr = new StringBuilder( output + entry.getValue() + type );
                    //buildStr.append( output + entry.getValue() + type );
                    if( !m_dataAccess.getOutputParams().containsKey( buildStr.toString() )) {
                        m_IOParamMap.put( buildStr.toString(), 0 );
                    }
                }

                int defaultValue = m_dataAccess.getOutputParams().get( entry.getKey() );
                buildStr = new StringBuilder( output + defaultValue + type );
                //buildStr.append( output + defaultValue + type );
                m_IOParamMap.put( buildStr.toString(), outNumLoc );
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


