/**
 * ConvertLogic
 * Paul Wallace
 * June 2014
 * Rev 1.11
 * 
 * ConvertLogic is the logic class for the SetupConverter application.  The 
 * class converts the Gain parameters based on the selected drive type (Machine
 * Type) and configures the I/O to simplify homing and to simulate cutting.  The
 * I/O configuration is based on the application of the configuration file.  The
 * class creates a new configuration file, with an updated checksum.
 * The class assumes the actual test stand can activate the first 16 inputs and
 * has an I/O loop back connector installed to the back of CNC on I/O 37- 44.
 *     
 * Main attributes:
 *      *   Instantiates a DAO which organizes the data contained in IMachineType
 *              based on the user selected machine type
 *      *   Accesses data through the DAO or IMachineType directly
 *      *   Get/set/replace Axes, Machine, and Speed parameters within the loaded
 *              parameter file
 *      *   Determines the application type and re-configures I/O appropriately so
 *              user can satisfy homing and simulate cutting.
 *      *   Recalculates the checksum and saves the converted file to the file system.
 * 
 *  Implements:  IParameters
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

import com.setupconverter.logic.IMachineParams.*;
import com.setupconverter.ui.ConvertUI.OperateConverter;

import java.nio.charset.StandardCharsets;
import java.awt.Color;


/**
 * ConvertLogic manipulates the data contained in IMachineType to convert
 * parameters within the loaded configuration file based on the selected 
 * machine type from the UI.
 * 
 * @author prwallace
 */
public class ConvertLogic implements IParameters {
    private static final String REG_EXP = "[=\\s\\.]+";
    private static final String LINE_RETURN = "\r\n";
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
    private DataAccessObj m_dataType;
    private final OperateConverter m_operate;

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
     * CBH installed
     */
    public boolean m_cbhInstalled = false;


    /**
     * Constructor for class ConvertLogic
     * @param file      - Parameter file to be converted
     * @param operate   - UI Inner Class object
     * @throws IOException
     */
    public ConvertLogic( File file, OperateConverter operate ) throws IOException {
        m_configFile = file;
        load( m_configFile );
        m_operate = operate;
    }


    /**
     * Converts gain, speed, and I/O parameters from the original configuration
     * file to control the user specified drive system as well as control homing,
     * cutting, and other I/O operations specific to the configuration files
     * application.
     */
    public void convert()  {
        String system = m_operate.getSelectedSystem();
        m_dataType = new DataAccessObj( system );
        int sthcTotal;
        int agTHCTotal;
        int row1NextIndex = 1;
        int row2NextIndex = 10;
        int row3_NextIndex = 17;
        int torchCollisionLoc = 16;


        // Adjust row 1 index if cut sense is assigned to input 1
        if( m_dataType.getCutSenseLoc() == 1 ) {
            row1NextIndex++;
        }

        // Determine the specific tools installed; bevel heads, pipe axes, THC's, etc
        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.FP.getName() ) > 0 ) {
            m_frontPanelInstalled = true;
        }
       
        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.BEVEL_AXES.getName() ) > 0 ) {
            m_bevelInstalled = true;
        }

        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.DUAL_BEVEL.getName() ) > 0 ) {
            m_dualBevelInstalled = true;
        }

        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.DUAL_TRANS.getName() ) > 0 && 
                getParameterValue( BlockTitle.MACHINE.getName(), Machine.DUAL_BEVEL.getName() ) > 0 ) {
            m_dualTransInstalled = true;
        }

        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.NO_ROTATE_TILT.getName() ) > 0 ) {
            m_noRotateTilt = true;
        }

        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.ONE_ROTATE_TILT.getName() ) > 0 ) {
            m_oneRotateTilt = true;
        }

        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.DUAL_GANTRY.getName() ) > 0 ) {
            m_dualGantryInstalled = true;
        }

        if( getParameterValue( BlockTitle.AXIS_7.getName(), Machine.ROTATING_TRANS.getName() ) > 0 ) {
            m_isRotatingTrans = true;
        }

        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.X_AXIS_ORIENTATION.getName() ) > 0 ) {
            m_xOnRail = true;
        }

        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.DUAL_TILTING.getName() ) > 0 ) {
            m_dualTiltInstalled = true;
        }

        if( getParameterValue( BlockTitle.MACHINE.getName(), Machine.CBH.getName() ) > 0 ) {
            m_cbhInstalled = true;
        }


        // Convert Speed parameters
        replaceParameters( BlockTitle.SPEEDS.getName(), Speed.toMap() );


        // Convert THC parameters
        if(( sthcTotal = getParameterValue( BlockTitle.MACHINE.getName(), Machine.STHC.getName() )) > 0 ) {
            m_sthcInstalled = true;
            m_dataType.addTHCDefaults();

            for( int i = 0; i < sthcTotal; i++ ) {
                replaceParameters( new StringBuilder( "[THC" ).append( i + 1 ).append( "]\r\n" ).toString(), m_dataType.getTHCAxisParams() );
            }

            replaceParameters( BlockTitle.AIC.getName(), m_dataType.getTHCAnalogParams() );
            replaceParameters( BlockTitle.MACHINE.getName(), m_dataType.getTHCMachineParams() );

            addInput( row1NextIndex++, Input.NCS_1.getValue(), !m_dataType.isEDGETi() );
            addOutput( m_dataType.getTHCTorqueLimitLoc(), Output.THC_TorqueLimit.getValue(), !m_dataType.isEDGETi() );

            if( sthcTotal >= 2 ) {
                addInput( row1NextIndex++, Input.NCS_2.getValue(), !m_dataType.isEDGETi() );
                row2NextIndex++;

                if( sthcTotal >= 3 ) {
                    addInput( row1NextIndex++, Input.NCS_3.getValue(), !m_dataType.isEDGETi() );
                    row2NextIndex++;

                    if( sthcTotal >= 4 ) {  // Only supporting 4 STHC's at this time.
                        addInput( row1NextIndex++, Input.NCS_4.getValue(), !m_dataType.isEDGETi() );
                        row2NextIndex++;
                    }
                }
            }
        }
        else if(( agTHCTotal = getParameterValue( BlockTitle.MACHINE.getName(), Machine.ARC_GLIDE.getName() )) > 0 ) {
            m_arcGlideInstalled = true;
            addInput( row1NextIndex++, Input.RDY_TO_FIRE_1.getValue(), !m_dataType.isEDGETi() );

            if( agTHCTotal >= 2 ) {
                addInput( row1NextIndex++, Input.RDY_TO_FIRE_2.getValue(), !m_dataType.isEDGETi() );

                if( agTHCTotal >= 3 ) {
                    addInput( row1NextIndex++, Input.RDY_TO_FIRE_3.getValue(), !m_dataType.isEDGETi() );

                    if( agTHCTotal == 4 ) {
                        addInput( row1NextIndex++, Input.RDY_TO_FIRE_4.getValue(), !m_dataType.isEDGETi() );
                    }
                }
            }
        }
        else {
            row1NextIndex++;  // Shift to 2nd input if no THC's installed
        }


        // Convert Dual Gantry Axis parameters
        if( m_dualGantryInstalled ) {
            replaceParameters( BlockTitle.DUAL_GANTRY.getName(), m_dataType.getAxesParams() );
            replaceParameters( BlockTitle.DUAL_GANTRY.getName(), m_dataType.getDualGantryParams() );
        }


        // Convert X & Y Axes parameters
        replaceParameters( BlockTitle.AXIS_1.getName(), m_dataType.getAxesParams() );
        replaceParameters( BlockTitle.AXIS_2.getName(), m_dataType.getAxesParams() );


        // Convert I/O
        addInput( 9, Input.DRIVE_DISABLED.getValue(), !m_dataType.isEDGETi() );

        /* X/Y Negative OT's can be assigned as a home switch or as an OT.  This    *
         * results in 2 possible Input#Number assignments for a single device.  For *
         * simplicity, set 2nd possible assignment to 0(Input19Number=0,            *
         * Input20Number=0)                                                         */
        int homeValue = getParameterValue( BlockTitle.IO.getName(), Input.X_NEG_OT.getName() );
        if( homeValue > 0 ) {
            setParameterValue( BlockTitle.IO.getName(), Input.X_NEG_OT.getName(), 0 );
            setParameterValue( BlockTitle.IO.getName(), new StringBuilder( INPUT ).append( homeValue ).append( TYPE).toString(), 0 );
        }

        homeValue = getParameterValue ( BlockTitle.IO.getName(), Input.Y_NEG_OT.getName() );
        if( homeValue > 0 ) {
            setParameterValue( BlockTitle.IO.getName(), Input.Y_NEG_OT.getName(), 0 );
            setParameterValue( BlockTitle.IO.getName(), new StringBuilder( INPUT ).append( homeValue ).append( TYPE).toString(), 0 );
        }

        // Set homing of X/Y axes, either to OT's or Home switches.
        if( m_xOnRail ) {
            addInput( row1NextIndex++, Input.X_HOME_NEG_OT.getValue(), !m_dataType.isEDGETi() );
            addInput( row1NextIndex++, Input.Y_HOME_NEG_OT.getValue(), !m_dataType.isEDGETi() );

            addInput( row2NextIndex++, Input.X_POS_OT.getValue(), !m_dataType.isEDGETi() );
            addInput( row2NextIndex++, Input.Y_POS_OT.getValue(), !m_dataType.isEDGETi() );
        }
        else {
            addInput( row1NextIndex++, Input.Y_HOME_NEG_OT.getValue(), !m_dataType.isEDGETi() );
            addInput( row1NextIndex++, Input.X_HOME_NEG_OT.getValue(), !m_dataType.isEDGETi() );

            addInput( row2NextIndex++, Input.Y_POS_OT.getValue(), !m_dataType.isEDGETi() );
            addInput( row2NextIndex++, Input.X_POS_OT.getValue(), !m_dataType.isEDGETi() );
        }


    // Convert CBH parameters
    if( m_cbhInstalled ) {
        setParameterValue( BlockTitle.CBH.getName(), Bevel.AUTO_HOME.getName(), Bevel.AUTO_HOME.getValue() );
        replaceParameters( BlockTitle.CBH.getName(), m_dataType.getAxesParams() );
        setParameterValue( BlockTitle.CBH.getName(), Bevel.SERVO_ERROR.getName(), Bevel.SERVO_ERROR.getValue() );
        setParameterValue( BlockTitle.CBH.getName(), Bevel.ENCODER_CNTS.getName(), Bevel.ENCODER_CNTS.getValue() );
        setParameterValue( BlockTitle.CBH.getName(), Machine.HOME_DIRECTION.getName(), 0 );

        if( !m_dataType.isEDGETi() ) {
            addInput( row1NextIndex++, Input.CBH_HOME.getValue(), !m_dataType.isEDGETi() );
        }
    }


    // Convert Dual Transverse parameters and add its inputs
    if( m_dualTransInstalled ) {
        replaceParameters( BlockTitle.AXIS_7.getName(), m_dataType.getAxesParams() );

        if( m_isRotatingTrans ) {
            setParameterValue( BlockTitle.MACHINE.getName(), Bevel.AUTO_HOME.getName(), Bevel.AUTO_HOME.getValue() );
            setParameterValue( BlockTitle.AXIS_7.getName(),  Machine.SERVO_ERROR_EN.getName(), Bevel.SERVO_ERROR.getValue() );
            setParameterValue( BlockTitle.AXIS_7.getName(), Machine.ENCODER_CNTS_EN.getName(), Bevel.ENCODER_CNTS.getValue() );
            setParameterValue( BlockTitle.AXIS_7.getName(), Machine.ENCODER_CNTS_M.getName(), Bevel.ENCODER_CNTS.getValue() );
            addInput( row1NextIndex++, Input.ROT_2_HOME.getValue(), !m_dataType.isEDGETi() );
            addInput( row2NextIndex++, Input.DUAL_HEAD_COLLISION.getValue(), !m_dataType.isEDGETi() );
        }
        else {             
            if( m_xOnRail ) {
                addInput( row1NextIndex++, Input.Y_POS_OT.getValue(), !m_dataType.isEDGETi() );
            }
            else {
                addInput( row1NextIndex++, Input.X_POS_OT.getValue(), !m_dataType.isEDGETi() );
            }

            addInput( row2NextIndex - 1, Input.DUAL_HEAD_COLLISION.getValue(), !m_dataType.isEDGETi() );
            torchCollisionLoc = row2NextIndex++;
        }

        if( row1NextIndex < 8 ) {

            addInput( row1NextIndex++, Input.PARK_HEAD_1.getValue(), !m_dataType.isEDGETi() );
            addInput( row2NextIndex++, Input.PARK_HEAD_2.getValue(), !m_dataType.isEDGETi() );
        }
        else {
            addInput( row3_NextIndex++, Input.PARK_HEAD_1.getValue(), !m_dataType.isEDGETi() );
            addInput( row3_NextIndex++, Input.PARK_HEAD_2.getValue(), !m_dataType.isEDGETi() );
        }
    }


        // Convert Bevel Axes parameters and add homing inputs
        if( m_bevelInstalled && ( m_dualBevelInstalled && !m_noRotateTilt || !m_dualBevelInstalled )) {  // Single bevel head installed
            setParameterValue( BlockTitle.MACHINE.getName(), Bevel.AUTO_HOME.getName(), Bevel.AUTO_HOME.getValue() );

            replaceParameters( BlockTitle.ROTATE.getName(), m_dataType.getAxesParams() );
            setParameterValue( BlockTitle.ROTATE.getName(), Bevel.SERVO_ERROR.getName(), Bevel.SERVO_ERROR.getValue() );
            setParameterValue( BlockTitle.ROTATE.getName(), Bevel.ENCODER_CNTS.getName(), Bevel.ENCODER_CNTS.getValue() );

            replaceParameters( BlockTitle.TILT.getName(), m_dataType.getAxesParams() );
            setParameterValue( BlockTitle.TILT.getName(), Bevel.SERVO_ERROR.getName(), Bevel.SERVO_ERROR.getValue() );
            setParameterValue( BlockTitle.TILT.getName(), Bevel.ENCODER_CNTS.getName(), Bevel.ENCODER_CNTS.getValue() );

            if( m_dualTiltInstalled ) {
                if( row1NextIndex < 8 ) {
                    addInput( row1NextIndex++, Input.TILT_POS_OT.getValue(), !m_dataType.isEDGETi() );
                    addInput( row2NextIndex++, Input.TILT_NEG_OT.getValue(), !m_dataType.isEDGETi() );
                    addInput( row1NextIndex++, Input.TILT2_POS_OT.getValue(), !m_dataType.isEDGETi() );
                    addInput( row2NextIndex++, Input.TILT2_NEG_OT.getValue(), !m_dataType.isEDGETi() );
                }
                else {
                    addInput( row3_NextIndex++, Input.TILT_POS_OT.getValue(), !m_dataType.isEDGETi() );
                    addInput( row3_NextIndex++, Input.TILT2_POS_OT.getValue(), !m_dataType.isEDGETi() ); 
                }
            }
            else {
                if( row1NextIndex < 8 ) {
                    addInput( row1NextIndex++, Input.TILT_POS_OT.getValue(), !m_dataType.isEDGETi() );
                    addInput( row2NextIndex++, Input.TILT_NEG_OT.getValue(), !m_dataType.isEDGETi() );
                    addInput( row1NextIndex++, Input.ROTATE_HOME.getValue(), !m_dataType.isEDGETi() );
                    torchCollisionLoc = row2NextIndex++;
                }
                else {
                    addInput( row3_NextIndex++, Input.TILT_POS_OT.getValue(), !m_dataType.isEDGETi() );
                    addInput( row3_NextIndex++, Input.ROTATE_HOME.getValue(), !m_dataType.isEDGETi() );
                }
            }

            if( m_dualBevelInstalled && !m_oneRotateTilt ) { // Dual Bevel heads installed
                if( m_dualTiltInstalled ) {
                    replaceParameters( BlockTitle.DUAL_TILT.getName(), m_dataType.getAxesParams() );
                    setParameterValue( BlockTitle.DUAL_TILT.getName(), Bevel.ENCODER_CNTS.getName(), Bevel.ENCODER_CNTS.getValue() );
                    setParameterValue( BlockTitle.DUAL_TILT.getName(), Bevel.SERVO_ERROR.getName(), Bevel.SERVO_ERROR.getValue() );

                    if( row1NextIndex < 8 ) {
                        addInput( row1NextIndex++, Input.TILT3_POS_OT.getValue(), !m_dataType.isEDGETi() );
                        addInput( row2NextIndex++, Input.TILT3_NEG_OT.getValue(), !m_dataType.isEDGETi() );
                        addInput( row1NextIndex++, Input.TILT4_POS_OT.getValue(), !m_dataType.isEDGETi() );
                        addInput( row2NextIndex++, Input.TILT4_NEG_OT.getValue(), !m_dataType.isEDGETi() );
                    }
                    else {
                        addInput( row3_NextIndex++, Input.TILT3_POS_OT.getValue(), !m_dataType.isEDGETi() );
                        addInput( row3_NextIndex++, Input.TILT4_POS_OT.getValue(), !m_dataType.isEDGETi() );
                    }
                }
                else {
                    replaceParameters( BlockTitle.DUAL_ROTATE.getName(), m_dataType.getAxesParams() );
                    setParameterValue( BlockTitle.DUAL_ROTATE.getName(), Bevel.ENCODER_CNTS.getName(), Bevel.ENCODER_CNTS.getValue() );
                    setParameterValue( BlockTitle.DUAL_ROTATE.getName(), Bevel.SERVO_ERROR.getName(), Bevel.SERVO_ERROR.getValue() );

                    if( row1NextIndex < 8 ) {
                        addInput( row1NextIndex++, Input.TILT3_POS_OT.getValue(), !m_dataType.isEDGETi() );
                        addInput( row2NextIndex++, Input.TILT3_NEG_OT.getValue(), !m_dataType.isEDGETi() );
                        addInput( row1NextIndex++, Input.ROT_2_HOME.getValue(), !m_dataType.isEDGETi() );
                        torchCollisionLoc = row2NextIndex++;
                    }
                }
            }
        }


        // Add Torch Collision input
        if( torchCollisionLoc < 17 ) {
            addInput( torchCollisionLoc, Input.TORCH_COLLISION.getValue(), !m_dataType.isEDGETi() );
        }
        else if( row3_NextIndex < 25 ) {
            addInput( row3_NextIndex, Input.TORCH_COLLISION.getValue(), !m_dataType.isEDGETi() );
        }


        // Re-assign Cut Sense inputs
        if( getParameterValue( BlockTitle.IO.getName(), Input.CUT_MARK_SENSE.getName() ) > 0 ) {
            addInput( m_dataType.getCutSenseLoc(), Input.CUT_MARK_SENSE.getValue(), true );
        }
        else if( getParameterValue( BlockTitle.IO.getName(), Input.CUT_SENSE_1.getName() ) > 0 ) {
            addInput( m_dataType.getCutSenseLoc(), Input.CUT_SENSE_1.getValue(), true );

            if( getParameterValue( BlockTitle.IO.getName(), Input.CUT_SENSE_2.getName() ) > 0 ) {
                addInput( m_dataType.getCutSenseLoc() + 1, Input.CUT_SENSE_2.getValue(), !m_dataType.isEDGETi() );
            }

            if( getParameterValue( BlockTitle.IO.getName(), Input.CUT_SENSE_3.getName() ) > 0 ) {
                addInput( m_dataType.getCutSenseLoc() + 2, Input.CUT_SENSE_3.getValue(), !m_dataType.isEDGETi() );
            }

            if( getParameterValue( BlockTitle.IO.getName(), Input.CUT_SENSE_4.getName() ) > 0 ) {
                addInput( m_dataType.getCutSenseLoc() + 3, Input.CUT_SENSE_4.getValue(), !m_dataType.isEDGETi() );
            }
        }


        // Re-assign Cut Control outputs beginning at output 40
        if( getParameterValue( BlockTitle.IO.getName(), Output.CUT_CONTROL.getName() ) > 0 || m_arcGlideInstalled ) {
            addOutput( m_dataType.getCutControlLoc(), Output.CUT_CONTROL.getValue(), !m_dataType.isEDGETi() );
        }
        else if( getParameterValue( BlockTitle.IO.getName(), Output.CUT_CONTROL_1.getName() ) > 0 ) {
                addOutput( m_dataType.getCutControlLoc(), Output.CUT_CONTROL_1.getValue(), !m_dataType.isEDGETi() );

            if( getParameterValue( BlockTitle.IO.getName(), Output.CUT_CONTROL_2.getName() ) > 0 ) {
                addOutput( m_dataType.getCutControlLoc() + 1, Output.CUT_CONTROL_2.getValue(), !m_dataType.isEDGETi() );
            }

            if( getParameterValue( BlockTitle.IO.getName(), Output.CUT_CONTROL_3.getName() ) > 0 ) {
                addOutput( m_dataType.getCutControlLoc() + 2, Output.CUT_CONTROL_3.getValue(), !m_dataType.isEDGETi() );
            }

            if( getParameterValue( BlockTitle.IO.getName(), Output.CUT_CONTROL_4.getName() ) > 0 ) {
                addOutput( m_dataType.getCutControlLoc() + 3, Output.CUT_CONTROL_4.getValue(), !m_dataType.isEDGETi() );
            }
        }


        // Re-assign Drive Enable output
        addOutput( 24, Output.DRIVE_ENABLE.getValue(), !m_dataType.isEDGETi() );


        // Merge in IO settings into parameter file
        putParameters( BlockTitle.IO.getName(), m_IOParamMap );
        shuffleIO();
        replaceParameters( BlockTitle.IO.getName(), m_IOParamMap );


        // Set all port settings to none and merge changes into parameter file
        putParameters( BlockTitle.LINK.getName(), m_linkParamMap );
        resetPorts();
        replaceParameters( BlockTitle.LINK.getName(), m_linkParamMap );
    }


    /**
     * Adds an input type and input number to the InputType Map and the InputNumber
     * Map.  Test for like input type assignments and remove from map if found.
     * Verifies machine type requires the inputs to be shifted (non-EDGEPro Ti).
     * @param typeIndex - The input location assigned to the input device
     * @param numberIndex  - The input device assigned to the input location
     */
    public void addInput( int typeIndex, int numberIndex, boolean addInput ) {
        if( addInput ) {
            Integer prevValue = m_inputTypeMap.put( new StringBuilder( INPUT ).append( typeIndex ).append( TYPE ).toString(), numberIndex );

            if( prevValue != null ) {
                m_inputNumberMap.remove( new StringBuilder( INPUT ).append( prevValue ).append( NUMBER   ).toString() );
            }

            m_inputNumberMap.put( new StringBuilder( INPUT ).append( numberIndex ).append( NUMBER   ).toString(), typeIndex );
        }
    }


    /**
     * Adds an output type and output number to the OutputType Map and the OutputNumber
     * Map.  Test for like output type assignments and remove from map if found.
     * Verifies machine type requires the outputs to be shifted (non-EDGEPro Ti).
     * @param typeIndex - The output location assigned to the output device
     * @param numIndex  - The output device assigned to the output location
     */
    public void addOutput( int typeIndex, int numIndex, boolean addOutput ) {
        if( addOutput ) {
            Integer prevValue = m_outputTypeMap.put( new StringBuilder( OUTPUT ).append( typeIndex ).append( TYPE ).toString(), numIndex );

            if( prevValue != null ) {
                m_outputNumberMap.remove(new StringBuilder( OUTPUT ).append( prevValue ).append( NUMBER ).toString() );
            }

            m_outputNumberMap.put( new StringBuilder( OUTPUT ).append( numIndex ).append( NUMBER ).toString() , typeIndex );
        }
    }


    /**
     * Re-arranges the I/O parameters within the IO Parameter Map to facilitate
     * homing and simulating of a cut.  Merges the Input and Output maps into the
     * I/O Parameter Map.  Sets the input and output logic to normally open (non-
     * EDGEPro Ti).  Test for like type assignments (same I/O location) and relocates
     * to I/O 49 and higher.  Test for like number assignments (same device) and
     * set original type assignment to 0 (replaced by this assignment).  The final
     * map of I/O parameters represents the new I/O assignment for this configuration.
     */
    private void shuffleIO() {
        Iterator< Entry< String, Integer >> iterator = m_IOParamMap.entrySet().iterator();
        Entry< String, Integer > entry;
        int inTypeLoc = 49;
        int outTypeLoc = 49;

        // Set input logic
        while( !( entry = iterator.next() ).getKey().startsWith( "Input1Number=" ) && iterator.hasNext() && !m_dataType.isEDGETi() ) {
            entry.setValue( 0 );
        }

        // Merge assignmented inputs from IO Map into InputNumber and InputType Map's
        while( !( entry = iterator.next() ).getKey().startsWith( new StringBuilder( INPUT ).append( 1 ).append( TYPE ).toString() ) && iterator.hasNext() ) {
            String inputType = new StringBuilder( INPUT ).append( entry.getValue() ).append( TYPE ).toString();
            if( !m_inputNumberMap.containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                int inValue = getParameterValue( BlockTitle.IO.getName(), inputType );
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

        // Set output logic
        while( !( entry = iterator.next() ).getKey().startsWith( "Output1Number=" ) && iterator.hasNext() && !m_dataType.isEDGETi() ) {
            entry.setValue( 0 );
        }

        // Merge assigned outputs from IO Map into OutputNumber and OutputType Map's
        while( !( entry = iterator.next() ).getKey().startsWith( new StringBuilder( OUTPUT ).append( 1 ).append( TYPE ).toString() ) && iterator.hasNext() ) {
            String outputType = new StringBuilder( OUTPUT ).append( entry.getValue() ).append( TYPE ).toString();
            if( !m_outputNumberMap.containsKey( entry.getKey() ) && entry.getValue() > 0 ) {
                int outValue = getParameterValue( BlockTitle.IO.getName(), outputType );
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
    public final void load( File file ) throws IOException {
        String line;
        BufferedReader buffer = new BufferedReader( new InputStreamReader( new FileInputStream( file ), StandardCharsets.UTF_8 ));
        while(( line = buffer.readLine() ) != null ) {
            m_paramList.add( new StringBuilder( line ).append( LINE_RETURN ).toString() );
        }
    }


    @Override
    public void putParameters( String blockTitle, Map< String, Integer > map  ) {
        String param;
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( LINE_RETURN )  && listIterator.hasNext() ) {
                if( !param.equals( LINE_RETURN )) {
                    String[] set = param.split( REG_EXP );
                    StringBuilder key = new StringBuilder( set[0] ).append( "=" );

                    try {
                        map.put( key.toString(), Integer.parseInt( set[ 1 ] ));
                    }
                    catch( NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e ) {
                        m_operate.setStatus( Color.RED, new StringBuilder( "Exception in add " ).append( e.getMessage() ).toString(), 
                                                        new StringBuilder( "Key = " ).append( set[ 1 ] ).append( " , set value to 0" ).toString() );
                        map.put( key.toString(), 0 );
                    }
                }
            }
        }
    }


    @Override
    public void setParameterValue( String blockTitle, String paramName, int value ) {
        String param;
        int index;

        if(( index = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( LINE_RETURN ) && listIterator.hasNext() ) {
                if( param.startsWith( paramName )) {
                    int replaceIndex = listIterator.previousIndex();
                    String[] set = param.split( REG_EXP );
                    m_paramList.set(replaceIndex, new StringBuilder( set[0] ).append( "=" ).append( value ).append( LINE_RETURN ).toString() );
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
    public ArrayList getParameterList() {
        return m_paramList;
    }


    @Override
    public int getParameterValue( String blockTitle, String paramName ) {
        String param;
        int index;
        int value = -1;

        if(( index = m_paramList.indexOf( blockTitle )) != -1) {
            ListIterator< String > listIterator = m_paramList.listIterator( index +1 );
            while( !( param = listIterator.next() ).startsWith( LINE_RETURN ) &&  listIterator.hasNext() ) {
                if( param.startsWith( paramName )) {
                    String[] set = param.split( REG_EXP );

                    try {
                        value = Integer.parseInt( set[ 1 ] );
                    }
                    catch( NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e ) {
                        m_operate.setStatus( Color.RED, new StringBuilder( "Exception in getValue: " ).append( e.getMessage() ).toString(), 
                                                        new StringBuilder( "Returned -1" ).toString() );
                    }

                    break;
                }
            }
        }

        return value;
    }


    @Override
    public void replaceParameters( String blockTitle, Map< String, Integer > map ) {
        String param;
        int startIndex;

        if(( startIndex = m_paramList.indexOf( blockTitle )) != -1 ) {
            ListIterator< String > listIterator = m_paramList.listIterator( startIndex +1 );
            while( !( param = listIterator.next() ).startsWith( LINE_RETURN ) && listIterator.hasNext() ) {
                for( String key : map.keySet() ) {
                    if( param.startsWith( key )) {
                        int replaceIndex = listIterator.previousIndex();
                        m_paramList.set( replaceIndex, new StringBuilder( key ).append( map.get( key ) ).append( LINE_RETURN ).toString() );
                        break;
                    }
                }
            }
        }
    }


    @ Override
    public void save( File file ) throws IOException {
        try (BufferedWriter buff_writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( file ), StandardCharsets.UTF_8 ))) {
            m_paramList.set( 0, new StringBuilder( "Checksum=" ).append( m_checksum ).append( LINE_RETURN ).toString() );

            for( String str : m_paramList ) {
                buff_writer.write( str );
            }

            buff_writer.close();
        }
    }


    /**
     * For Debug use, prints the argument Map
     * @param map   - Map to print
     */
    public void printMap( Map< String, Integer > map ) {
        Iterator< Entry< String, Integer >> iterator;
        Entry< String, Integer > entry;

        iterator = map.entrySet().iterator();
        while( iterator.hasNext() ) {
            entry = iterator.next();
            System.out.format( "%s\t%d\n", entry.getKey(), entry.getValue() );
        }
    }
}


