/**
 * Convert_ini_UI
 * 
 *  UI for INI_Processing Class
 * 
 *  The Class provides a users interface that:
 *      *   Opens a Load or Save dialog filtered for ini files and returns a File object
 *      *   Instantiates a INI_Processing object to convert the file or calculate the files checksum
 *      *   Allows user to select the type of INI_Processing to perform, Checksum or Convert.
 *      *   Closes the utility and disposes of UI components.
 * 
 *  Implements:  ActionListener & IConverterComponents
 *  Extends: JFrame
 */
package com.setupconverter.logic;

import com.setupconverter.logic.IDataAcess.INPUT_NUM;
import com.setupconverter.logic.IDataAcess.OUTPUT;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 
 * @author prwallace
 */
public final class DiagnosticBrds {

    private final Map< String, Integer > m_machineParams = new LinkedHashMap<>();
    private final Map< String, Integer > m_analogInParams = new LinkedHashMap<>();
    private final Map< String, Integer > m_speedParams = new LinkedHashMap<>();
    private final Map< String, Integer > m_outputParams = new LinkedHashMap<>();
    private final Map< String, Integer > m_inputParams = new LinkedHashMap<>();
    private final Map< String, Integer > m_axesParams = new LinkedHashMap<>();
    private final Map< String, Integer > m_thcParams = new LinkedHashMap<>();
    private final Map< String, Integer > m_bevelParams = new LinkedHashMap<>();


    /**
     * 
     */
    public DiagnosticBrds() {

        initializeParamMaps();
    }


    /**
     * 
     */
    private void initializeParamMaps() {

        // Analog inputs
        m_analogInParams.put( "SpeedPotInstalled=", 1 );
        m_analogInParams.put( "SpeedPotAnalog1=", 1 );
        m_analogInParams.put( "SpeedPotAnalog2=", 1 );

        // Machine
        m_machineParams.put( "FrontPanelInstalled=", 1 );
        m_machineParams.put( "THCAxis1=", 3 );
        m_machineParams.put( "THCAnalog1=", 0 );
        m_machineParams.put( "AutoHome=", 1 );

        // Speeds
        m_speedParams.put( "MaxSpeed(english)=", 1000 );
        m_speedParams.put( "MaxSpeed(metric)=", 25400 );
        m_speedParams.put( "GainSpeed1(english)=", 100 );
        m_speedParams.put( "GainSpeed1(metric)=", 2540 );
        m_speedParams.put( "GainSpeed2(english)=", 300 );
        m_speedParams.put( "GainSpeed2(metric)=", 7620 );
        m_speedParams.put( "GainSpeed3(english)=", 500 );
        m_speedParams.put( "GainSpeed3(metric)=", 12700 );
        m_speedParams.put( "GainSpeed4(english)=", 1000 );
        m_speedParams.put( "GainSpeed4(metric)=", 25400 );
        m_speedParams.put( "GainSpeed5(english)=", 1000 );
        m_speedParams.put( "GainSpeed5(metric)=", 25400 );
        m_speedParams.put( "AccelRate=", 20 );
        m_speedParams.put( "AccelRate2=", 40 );
        m_speedParams.put( "AccelRate3=", 50 );
        m_speedParams.put( "AccelRate4=", 60 );
        m_speedParams.put( "AccelRate5=", 60 );
        m_speedParams.put( "RotateAccelRate=", 10 );
        m_speedParams.put( "MaxRotateSpeed=", 10 );
        m_speedParams.put( "RotateHighJogSpeed=", 10);
        m_speedParams.put( "RotateLowJogSpeed=", 5 );
        m_speedParams.put( "TiltAccelRate=", 10 );
        m_speedParams.put( "MaxTiltSpeed=", 10 );
        m_speedParams.put( "TiltHighJogSpeed=", 10 );
        m_speedParams.put( "TiltLowJogSpeed=", 5 );
        m_speedParams.put( "THCAccelRate=", 100 );
        m_speedParams.put( "MaxTHCSpeed(english)=", 400 );
        m_speedParams.put( "MaxTHCSpeed(metric)=", 10160 );
        m_speedParams.put( "THCJogSpeed(english)=", 200 );
        m_speedParams.put( "THCJogSpeed(metric)=", 5080 );
        m_speedParams.put( "THCFastHomeSpeed(english)=", 50 );
        m_speedParams.put( "THCFastHomeSpeed(metric)=", 1270 );
        m_speedParams.put( "THCSlowHomeSpeed(english)=", 5 );
        m_speedParams.put( "THCSlowHomeSpeed(metric)=", 127 );

        // Inputs
        m_inputParams.put( "Input10Number=", 9 );
        m_inputParams.put( "Input11Number=", 2 );
        m_inputParams.put( "Input12Number=", 10 );
        m_inputParams.put( "Input13Number=", 3 );
        m_inputParams.put( "Input14Number=", 11 );
        m_inputParams.put( "Input35Number=", 5 );
        m_inputParams.put( "Input36Number=", 13 );
        m_inputParams.put( "Input46Number=", 22 );
        m_inputParams.put( "Input47Number=", 1 );
        m_inputParams.put( "Input83Number=", 19 );
        m_inputParams.put( "Input100Number=", 12 );
        m_inputParams.put( "Input102Number=", 21 );
        m_inputParams.put( "Input103Number=", 6 );
        m_inputParams.put( "Input104Number=", 14 );
        m_inputParams.put( "Input105Number=", 4 );
        m_inputParams.put( "Input106Number=", 40 );
        m_inputParams.put( "Input107Number=", 41 );
        m_inputParams.put( "Input126Number=", 8 );
        m_inputParams.put( "Input127Number=", 16 );
        m_inputParams.put( "Input131Number=", 20 );
        m_inputParams.put( "Input133Number=", 134 );
        m_inputParams.put( "Input134Number=", 135 );
        m_inputParams.put( "Input135Number=", 136 );
        m_inputParams.put( "Input136Number=", 137 );
        m_inputParams.put( "Input137Number=", 18 );
        m_inputParams.put( "Input138Number=", 17 );
        m_inputParams.put( "Input139Number=", 7 );
        m_inputParams.put( "Input140Number=", 15 );
        m_inputParams.put( "Input182Number=", 138 );
        m_inputParams.put( "Input183Number=", 142 );
        m_inputParams.put( "Input202Number=", 139 );
        m_inputParams.put( "Input203Number=", 143 );
        m_inputParams.put( "Input288Number=", 140 );
        m_inputParams.put( "Input289Number=", 144 );
        m_inputParams.put( "Input308Number=", 141 );
        m_inputParams.put( "Input309Number=", 145 );
        m_inputParams.put( "Input356Number=", 130 );
        m_inputParams.put( "Input357Number=", 129 );
        m_inputParams.put( "Input358Number=", 131 );
        m_inputParams.put( "Input377Number=", 133 );
        m_inputParams.put( "Input378Number=", 132 );
        m_inputParams.put( "Input1Type=", INPUT_NUM.NCS_1.getValue() );
        m_inputParams.put( "Input2Type=", INPUT_NUM.X_NEG_OT.getValue() );
        m_inputParams.put( "Input3Type=", INPUT_NUM.Y_NEG_OT.getValue() );
        m_inputParams.put( "Input4Type=", INPUT_NUM.ROT_2_HOME.getValue() );
        m_inputParams.put( "Input5Type=", INPUT_NUM.TILT_POS_OT.getValue() );
        m_inputParams.put( "Input6Type=", INPUT_NUM.TILT2_POS_OT.getValue() );
        m_inputParams.put( "Input7Type=", INPUT_NUM.ROTATE_PLUS.getValue() );
        m_inputParams.put( "Input8Type=", INPUT_NUM.PARK_HEAD_1.getValue() );
        m_inputParams.put( "Input9Type=", INPUT_NUM.DRIVE_DISABLED.getValue() );
        m_inputParams.put( "Input10Type=", INPUT_NUM.X_POS_OT.getValue() );
        m_inputParams.put( "Input11Type=", INPUT_NUM.Y_POS_OT.getValue() );
        m_inputParams.put( "Input12Type=", INPUT_NUM.TORCH_COLLISION.getValue() );
        m_inputParams.put( "Input13Type=", INPUT_NUM.TILT_NEG_OT.getValue() );
        m_inputParams.put( "Input14Type=", INPUT_NUM.TILT2_NEG_OT.getValue() );
        m_inputParams.put( "Input15Type=", INPUT_NUM.ROTATE_MINUS.getValue() );
        m_inputParams.put( "Input16Type=", INPUT_NUM.PARK_HEAD_2.getValue() );
        m_inputParams.put( "Input17Type=", INPUT_NUM.TILT_MINUS.getValue() );
        m_inputParams.put( "Input18Type=", INPUT_NUM.TILT_PLUS.getValue() );
        m_inputParams.put( "Input19Type=", INPUT_NUM.FUME_SELECT.getValue() );
        m_inputParams.put( "Input20Type=", INPUT_NUM.FUME_SENSE.getValue() );
        m_inputParams.put( "Input21Type=", INPUT_NUM.TEST_LIFTER.getValue() );
        m_inputParams.put( "Input22Type=", INPUT_NUM.THC_AUTO_1.getValue() );
        m_inputParams.put( "Input40Type=", INPUT_NUM.CUT_SENSE_1.getValue() );
        m_inputParams.put( "Input41Type=", INPUT_NUM.CUT_SENSE_2.getValue() );
        m_inputParams.put( "Input129Type=", INPUT_NUM.FP_START.getValue() );
        m_inputParams.put( "Input130Type=", INPUT_NUM.FP_STOP.getValue() );
        m_inputParams.put( "Input131Type=", INPUT_NUM.FP_MANUAL.getValue() );
        m_inputParams.put( "Input132Type=", INPUT_NUM.FP_FORWARD_ON_PATH.getValue() );
        m_inputParams.put( "Input133Type=", INPUT_NUM.FP_BACK_ON_PATH.getValue() );
        m_inputParams.put( "Input134Type=", INPUT_NUM.JOYSTICK_UP.getValue() );
        m_inputParams.put( "Input135Type=", INPUT_NUM.JOYSTICK_DOWN.getValue() );
        m_inputParams.put( "Input136Type=", INPUT_NUM.JOYSTICK_LEFT.getValue() );
        m_inputParams.put( "Input137Type=", INPUT_NUM.JOYSTICK_RIGHT.getValue() );
        m_inputParams.put( "Input138Type=", INPUT_NUM.AUTO_SELECT_1.getValue() );
        m_inputParams.put( "Input139Type=", INPUT_NUM.MANUAL_SELECT_1.getValue() );
        m_inputParams.put( "Input140Type=", INPUT_NUM.RAISE_TORCH_1.getValue() );
        m_inputParams.put( "Input141Type=", INPUT_NUM.LOWER_TORCH_1.getValue() );
        m_inputParams.put( "Input142Type=", INPUT_NUM.AUTO_SELECT_2.getValue() );
        m_inputParams.put( "Input143Type=", INPUT_NUM.MANUAL_SELECT_2.getValue() );
        m_inputParams.put( "Input144Type=", INPUT_NUM.RAISE_TORCH_2.getValue() );
        m_inputParams.put( "Input145Type=", INPUT_NUM.LOWER_TORCH_2.getValue() );

        // Outputs
        m_outputParams.put( "Output4Number=", 2 );
        m_outputParams.put( "Output55Number=", 4 );
        m_outputParams.put( "Output57Number=", 3 );
        m_outputParams.put( "Output58Number=", 1 );
        m_outputParams.put( "Output197Number=", 40 );
        m_outputParams.put( "Output198Number=", 41 );
        m_outputParams.put( "Output217Number=", 129 );
        m_outputParams.put( "Output218Number=", 130 );
        m_outputParams.put( "Output472Number=", 5 );
        m_outputParams.put( "Output473Number=", 6 );
        m_outputParams.put( "Output474Number=", 7 );
        m_outputParams.put( "Output475Number=", 8 );
        m_outputParams.put( "Output476Number=", 9 );
        m_outputParams.put( "Output1Type=", OUTPUT.HOLD_IGN1.getValue() );
        m_outputParams.put( "Output2Type=", OUTPUT.THD.getValue() );
        m_outputParams.put( "Output3Type=", OUTPUT.NCE_1.getValue() );
        m_outputParams.put( "Output4Type=", OUTPUT.DRIVE_ENABLE.getValue() );
        m_outputParams.put( "Output5Type=", OUTPUT.VENT_1.getValue() );
        m_outputParams.put( "Output6Type=", OUTPUT.VENT_2.getValue() );
        m_outputParams.put( "Output7Type=", OUTPUT.VENT_3.getValue() );
        m_outputParams.put( "Output8Type=", OUTPUT.VENT_4.getValue() );
        m_outputParams.put( "Output9Type=", OUTPUT.VENT_5.getValue() );
        m_outputParams.put( "Output40Type=", OUTPUT.CUT_CONTROL_1.getValue() );
        m_outputParams.put( "Output41Type=", OUTPUT.CUT_CONTROL_2.getValue() );
        m_outputParams.put(  "Output129Type=", OUTPUT.STATION_ENABLE_LED_1.getValue() );
        m_outputParams.put( "Output130Type=", OUTPUT.STATION_ENABLE_LED_2.getValue() );

        // THC Axes
        m_thcParams.put( "PGain=", 2000 );
        m_thcParams.put( "IGain=", 0 );
        m_thcParams.put( "DGain=", 0 );
        m_thcParams.put( "FGain=", 0 );
        m_thcParams.put( "ServoErrTolerance(english)=", 1 );
        m_thcParams.put( "ServoErrTolerance(metric)=", 25 );
        m_thcParams.put( "StallForceTolerance(english)=", 1 );
        m_thcParams.put( "StallForceTolerance(metric)=", 25 );
        m_thcParams.put( "StallForceOffset(english)=", 0 );
        m_thcParams.put( "StallForceOffset(metric)=", 0 );
        m_thcParams.put( "EncoderCounts(english)=", 1000 );
        m_thcParams.put( "EncoderCounts(metric)=", 39 );
        m_thcParams.put( "DriveType=", 0 );
        m_thcParams.put( "DACPolarity=", 0 );
        m_thcParams.put( "EncoderPolarity=", 0 );
        m_thcParams.put( "EncoderMode=", 2 );
        m_thcParams.put( "SlideLength(english)=", 10 );
        m_thcParams.put( "SlideLength(metric)=", 254 );
        m_thcParams.put( "UseHardStop=", 0 );
        m_thcParams.put( "UseHomeSwitch=", 1 );

        // Bevel Axes
        m_bevelParams.put( "PGain=", 2000 );
        m_bevelParams.put( "IGain=", 0 );
        m_bevelParams.put( "DGain=", 0 );
        m_bevelParams.put( "FGain=", 0 );
        m_bevelParams.put( "ServoErrTolerance(degrees)=", 90 );
        m_bevelParams.put( "EncoderCounts(revs)=", 6000 );
        m_bevelParams.put( "DriveType=", 0 );
        m_bevelParams.put( "DACPolarity=", 0 );
        m_bevelParams.put( "EncoderPolarity=", 0 );
        m_bevelParams.put( "EncoderMode=", 2 );
        m_bevelParams.put( "UseHomeSwitch=", 1 );
        m_bevelParams.put( "HomeDirection=", 0 );
        m_bevelParams.put( "UseMarker=", 0 );
        m_bevelParams.put( "UseSWOvertravelSwitches=", 0 );

        // Axis 1, 2, and 6
        m_axesParams.put( "PGain=", 2000 );
        m_axesParams.put( "IGain=", 0 );
        m_axesParams.put( "DGain=", 0 );
        m_axesParams.put( "FGain=", 0 );
        m_axesParams.put( "VGain=", 0 );
        m_axesParams.put( "PGain2=", 2000 );
        m_axesParams.put( "IGain2=", 0 );
        m_axesParams.put( "DGain2=", 0 );
        m_axesParams.put( "FGain2=", 0 );
        m_axesParams.put( "VGain2=", 0 );
        m_axesParams.put( "PGain3=", 2000 );
        m_axesParams.put( "IGain3=", 0 );
        m_axesParams.put( "DGain3=", 0 );
        m_axesParams.put( "FGain3=", 0 );
        m_axesParams.put( "VGain3=", 0 );
        m_axesParams.put( "PGain4=", 2000 );
        m_axesParams.put( "IGain4=", 0 );
        m_axesParams.put( "DGain4=", 0 );
        m_axesParams.put( "FGain4=", 0 );
        m_axesParams.put( "VGain4=", 0 );
        m_axesParams.put( "PGain5=", 2000 );
        m_axesParams.put( "IGain5=", 0 );
        m_axesParams.put( "DGain5=", 0 );
        m_axesParams.put( "FGain5=", 0 );
        m_axesParams.put( "VGain5=", 0 );
        m_axesParams.put( "ServoErrTolerance(english)=", 1 );
        m_axesParams.put( "ServoErrTolerance(metric)=", 25 );
        m_axesParams.put( "EncoderCounts(english)=", 1000 );
        m_axesParams.put( "EncoderCounts(metric)=", 39 );
        m_axesParams.put( "DriveType=", 0 );
        m_axesParams.put( "DACPolarity=", 0 );
        m_axesParams.put( "EncoderPolarity=", 0 );
        m_axesParams.put( "EncoderMode=", 2 );
        m_axesParams.put( "UseHWOvertravelSwitches=", 1 );
        m_axesParams.put( "UseHomeSwitch=", 1 );
        m_axesParams.put( "HomeToOvertravel=", 1 );
    }


    public Map< String, Integer > getMachineParams() {
        return m_machineParams;
    }


    public Map< String, Integer > getSpeedParams() {
        return m_speedParams;
    }


    public Map< String, Integer > getAnalogInputParams() {
        return m_analogInParams;
    }


    public Map< String, Integer > getAxesParams() {
        return m_axesParams;
    }


    public Map< String, Integer > getTHCParams() {
        return m_thcParams;
    }


    public Map< String, Integer > getBevelParams() {
        return m_bevelParams;
    }


    public Map< String, Integer > getInputParams() {
        return m_inputParams;
    }


    public Map< String, Integer > getOutputParams() {
        return m_outputParams;
    }
}
