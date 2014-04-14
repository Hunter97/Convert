/**
 * 
 */
package com.setupconverter.logic;

import com.setupconverter.ui.IComponents;
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
 * @author prwallace
 *
 */
public class ConverterProcessing implements IProcess {

    private static int TOTAL_AXIS_BLOCKS = 3;

    private final ArrayList< String > m_paramList = new ArrayList< String >();

    private final Map< String, Integer > m_IOParamMap = new LinkedHashMap< String, Integer >();
    private final Map< String, Integer > m_LinkParamMap = new LinkedHashMap< String, Integer >();

    private DiagnosticBrds m_containers;
    //private Bench_Container m_containers;
    private final IComponents m_ui_components;

    private File m_configFile = null;
    private int m_checksum = 0;

    public boolean m_hasDT = false;
    public int[] m_axisNum = new int[ TOTAL_AXIS_BLOCKS ];


    /**
     * @param file
     * @throws IOException
     */
    public ConverterProcessing( File file, IComponents ui ) throws IOException {//
        m_configFile = file;
        loadParams( m_configFile );
        m_ui_components = ui;
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

        if( buffer != null ) {
            buffer.close();
        }
    }


    /**
     * Gets all file parameters between the argument index and the end of
     * this block.  Returns the file parameters as a  Map< String, Integer >().
     * 
     * @param map - Holds each line in block as a Map< String, Integer >
     * @param blockTitle - Name of section ([name]) in configuration file
     */
    public void addParamsToMap( String blockTitle, Map< String, Integer > map  ) throws ArrayIndexOutOfBoundsException, NumberFormatException {

        ListIterator< String > listItr = null;
        String param;
        String emptyLine = "\r\n";
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            listItr = m_paramList.listIterator( index +1 );
            while( !( param = listItr.next() ).startsWith( emptyLine )  && listItr.hasNext() ) {
                if( !param.equals( emptyLine )) {
                    StringBuilder sb = new StringBuilder( param.length() );
                    String[] key = param.split( "[=\\s\\.]+" );
                    sb.append( key[0] + "=" );

                    try {
                        map.put( sb.toString(), Integer.parseInt( key[ 1 ] ));
                    }
                    catch( NumberFormatException e ) {
                        m_ui_components.setStatus( Color.RED,"Exception converting " + sb.toString(), "Key = " + key[ 1 ] + " , set value to 0" );
                        map.put( sb.toString(), 0 );
                    }
                }
            }
        }
    }


    /**
     * Calculates the checksum of a configuration file.  Each character in the
     * List< String >(), beginning at the 2nd index, is summed together.
     * 
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
     * 
     * @return an integer equal to sum of characters in List< String >
     */
    @ Override
    public int getChecksum() {
        return m_checksum;
    }


    /**
     * Gets/returns the value of specified parameter from the configuration file.
     * The function searches the m_paramList List< String >() for a match to the
     * argument paramName, beginning at the index after the blockTitle argument.
     * Gets/Returns a -1 if the parameter is not found.
     * 
     * @param blockTitle
     * @param paramName
     * @return
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
     * Replaces the parameters within the List< String > m_paramList with the
     * like parameters contained in the argument Map< String, Integer > map.
     * The map contains a group of default parameters.  These parameters are
     * part of a block of parameters from a users configuration file. The
     * argument String blockTitle (i.e. [Machine]), designates this block of
     * parameters.
     * 
     * @param blockTitle    - Title of block in configuration file(i.e. [Machine])
     * @param map           - Map containing a set of default parameters
     */
    private void replaceParams( String blockTitle, Map< String, Integer > map ) throws ArrayIndexOutOfBoundsException, NullPointerException {

        ListIterator< String > listItr = null;
        List< String > temp = new ArrayList< String >();
        String param = null;
        String emptyLine = "\r\n";
        int fromIndex;
        int replaceIndex;

        if(( fromIndex = m_paramList.indexOf( blockTitle )) != -1 ) {
            temp.addAll( m_paramList.subList( fromIndex, ( m_paramList.size() - fromIndex )));
            listItr = temp.listIterator();
            while( !( param = listItr.next() ).startsWith( emptyLine ) && listItr.hasNext() ) {
                StringBuilder sb = new StringBuilder();
                for( String key : map.keySet() ) {
                    if( param.startsWith( key )) {
                        replaceIndex = fromIndex + temp.indexOf( param );
                        sb.append( key + map.get( key ) + "\r\n" );
                        m_paramList.set( replaceIndex, sb.toString() );
                        //System.out.format( m_paramList.get( index ));
                        break;
                    }
                }
            }
        }
    }


    /**  Change constructor to receives the arguments for the container class
     * @throws IOException
     */
    @ Override
    public void convertFile() throws IOException {

        m_containers = new DiagnosticBrds();
        //m_containers = new Bench_Container();

        replaceParams( "[Machine]\r\n", m_containers.getMachineParams() );

        replaceParams( "[AnalogInputCard]\r\n", m_containers.getAnalogInputParams() );

        replaceParams( "[Speeds]\r\n" , m_containers.getSpeedParams() );

        addParamsToMap( "[Link]\r\n", m_LinkParamMap );
        mergeLinkMaps();
        replaceParams( "[Link]\r\n", m_LinkParamMap );

        addParamsToMap( "[I/O]\r\n", m_IOParamMap );
        mergeIOMaps();
        replaceParams( "[I/O]\r\n", m_IOParamMap );


        // Find all axis blocks and add to parameter List< String >()
        int axisNum = 0;
        //StringBuilder axis = null;

        for( int i = 0; i < TOTAL_AXIS_BLOCKS; i++ ) {
            if( i == TOTAL_AXIS_BLOCKS - 1 ) {
                axisNum = 6;
            }

            //axis = new StringBuilder( "[Axis" + axisNum + "]\r\n" );
            //axis.append( "[Axis" + axisNum + "]\r\n" );
            replaceParams( new StringBuilder( "[Axis" + axisNum + "]\r\n" ).toString(), m_containers.getAxesParams() );
            axisNum++;
        }


        // Find all THC axis blocks and add to parameter List< String >()
        int totalTHCs = getParamValue( "[Machine]\r\n", "SensorTHCInstalled=" );
        for( int i = 1; i <= totalTHCs; i++ ) {
            //axis = new StringBuilder( "[THC" + i + "]\r\n" );
            //axis.append( "[THC" + i + "]\r\n" );
            replaceParams( new StringBuilder( "[THC" + i + "]\r\n" ).toString(), m_containers.getTHCParams() );
        }


        // Find all Rotate/Tilt axis blocks and add to parameter List< String >()
        int singleBev = getParamValue( "[Machine]\r\n", "SkewRotatorInstalled=" );
        int dualBev = getParamValue( "[Machine]\r\n", "DualSkewRotatorInstalled=" );
        int noRotateTilt = getParamValue( "[Machine]\r\n", "NoRotateTilt=" );
        int oneRotateTilt = getParamValue( "[Machine]\r\n", "OneRotateTilt=" );

        if( singleBev > 0 && dualBev < 1 || singleBev > 0 && dualBev > 0 && oneRotateTilt > 0  ) {
            replaceParams( "[Rotate]\r\n", m_containers.getBevelParams() );
            replaceParams( "[Tilt]\r\n", m_containers.getBevelParams() );
        }
        else if( singleBev > 0 && dualBev > 0 && noRotateTilt < 1) {
            replaceParams( "[Rotate]\r\n", m_containers.getBevelParams() );
            replaceParams( "[Tilt]\r\n", m_containers.getBevelParams() );
            replaceParams( "[DualRotate]\r\n", m_containers.getBevelParams() );
            replaceParams( "[DualTilt]\r\n", m_containers.getBevelParams() );
        }


        // Find the Dual Gantry axis block and add to parameter List< String >()
        int dualGantryInstalled = getParamValue( "[Machine]\r\n", "DualGantryInstalled=" );
        if( dualGantryInstalled > 0 ) {
            replaceParams( "[DualGantry]\r\n", m_containers.getAxesParams() );
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
            if( !m_containers.getInputParams().containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                buildStr = new StringBuilder( input + entry.getValue() + type );
                //buildStr.append( input + entry.getValue() + type );
                if( !m_containers.getInputParams().containsKey( buildStr.toString() )) {
                    m_IOParamMap.put( buildStr.toString(), 0 );
                }

                buildStr = new StringBuilder( input + inTypeLoc + type );
                //buildStr.append( input + inTypeLoc + type );
                m_IOParamMap.put( buildStr.toString(), inNumLoc );
                entry.setValue( inTypeLoc++ );
            }
            else if( m_containers.getInputParams().containsKey( entry.getKey() )) {
                if( entry.getValue() > 0 ) {
                    buildStr = new StringBuilder( input + entry.getValue() + type );
                    //buildStr.append( input + entry.getValue() + type );
                    if( !m_containers.getInputParams().containsKey( buildStr.toString() )) {
                        m_IOParamMap.put( buildStr.toString(), 0 );
                    }

                }

                int defaultValue = m_containers.getInputParams().get( entry.getKey() );
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
            if( !m_containers.getOutputParams().containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                buildStr = new StringBuilder( output + entry.getValue() + type );
                //buildStr.append( output + entry.getValue() + type );
                if( !m_containers.getOutputParams().containsKey( buildStr.toString() )) {
                    m_IOParamMap.put( buildStr.toString(), 0 );
                }

                buildStr = new StringBuilder( output + outTypeLoc + type );
                //buildStr.append( output + outTypeLoc + type );
                m_IOParamMap.put( buildStr.toString(), outNumLoc );
                entry.setValue( outTypeLoc++ );
            }
            else if( m_containers.getOutputParams().containsKey( entry.getKey() )) {
                if( entry.getValue() > 0 ) {
                    buildStr = new StringBuilder( output + entry.getValue() + type );
                    //buildStr.append( output + entry.getValue() + type );
                    if( !m_containers.getOutputParams().containsKey( buildStr.toString() )) {
                        m_IOParamMap.put( buildStr.toString(), 0 );
                    }
                }

                int defaultValue = m_containers.getOutputParams().get( entry.getKey() );
                buildStr = new StringBuilder( output + defaultValue + type );
                //buildStr.append( output + defaultValue + type );
                m_IOParamMap.put( buildStr.toString(), outNumLoc );
                entry.setValue( defaultValue );
            }

            outNumLoc++;
        }
    }


    /**
     * 
     */
    private void mergeLinkMaps() {

        StringBuilder portType = null;
        StringBuilder portNum = null;
        String port = "Port";
        String number = "Number=";
        String type = "Type=";
        int numLoc = 1;
        int typeLoc = 1;

        for( Entry< String, Integer > entry : m_LinkParamMap.entrySet() ){

            portType = new StringBuilder( port + typeLoc + type );
            //portType.append( port + typeLoc + type );

            portNum = new StringBuilder( port + numLoc + number );
            //portNum.append( port + numLoc + number );

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


