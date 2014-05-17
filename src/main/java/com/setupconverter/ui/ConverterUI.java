/**
 *  Convert_ini_UI
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

package com.setupconverter.ui;

import com.setupconverter.logic.ConvertLogic;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 * Convert_ini_UI is a UI that provides an interface for the a user to convert
 * a configuration file or to calculate the files checksum.  The UI provides 2
 * radio buttons, Checksum and Convert, to select the desired operation. File
 * manipulation occurs through the use of the Load, Run, and Save buttons.  A
 * A Close button is also provided that will properly dispose of the UI.  The
 * Load button instantiates an INI_Processing object, which contains methods to
 * convert the loaded configuration file and/or recalculate the file checksum.
 * A JFileChooser object is used to load and save the configuration file.
 * 
 * @author prwallace
 */
//@ SuppressWarnings( "serial" )
public class ConverterUI extends JFrame implements ActionListener, IComponents {

    private CoConvertLogic_process;

    private final JPanel m_mainPanel;
    private final JPanel m_buttonPanel;
    private JPanel m_radioPanel;
    private JPanel m_statusPanel;

    private final JButton m_loadButton;
    private final JButton m_runButton;
    private final JButton m_saveButton;
    private final JButton m_closeButton;

    //private ButtonGroup m_radioGroup;
    private JRadioButton m_chksumRadioBtn;
    private JRadioButton m_convertRadioBtn;

    private final JTextField m_statusTextField;
    //private final GridBagConstraints m_gbConstraints;

    private File m_currentDir = null;
    private File m_loadedFile = null;
    private File m_savedFile = null;

    private final static String INI = "ini";

    public boolean m_fileIsLoaded;
    public boolean m_fileIsSaved;
    public boolean m_fileIsConverted;


    /**
     * Default constructor.  Instantiates and adds JPanel's to the JFrame for
     * the JButton's, JTextField, and JRadioButton's.  Instantiates the Load,
     * Run, Save, and Close JButton's and a GridBagConstraints object to set
     * constraints for each JButtons in its associated JPanel.  Adds Action-
     * Listener's for the UI components.
     */
    public ConverterUI() {

        super( "Convert_INI" );
        JFrame.setDefaultLookAndFeelDecorated( true );
        this.setResizable( true );

        // Add/Configure JPanels
        m_mainPanel = new JPanel();
        //radioPanel = new JPanel( new GridBagLayout());
        //radioPanel.setLayout( new FlowLayout( FlowLayout.CENTER, 20, 40 ) );
        //radioPanel.setPreferredSize( new Dimension( 290, 80 ) );
        m_mainPanel.setBackground(Color.yellow);
        m_mainPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder() ) );

        m_buttonPanel = new JPanel( new GridBagLayout());
        //m_gbConstraints = new GridBagConstraints();
        //buttonPanel.setPreferredSize( new Dimension( 90, 0) );
        m_buttonPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder() ) );


        // Add/Configure UI components to the their associated JPanel
        createRadioGroup();
        

        m_loadButton = new JButton( UI.LOAD.getName() );
        m_loadButton.setToolTipText( "Load user setup file" );
        m_buttonPanel.add( m_loadButton, setConstraints( 0, 0, 0, 0, 0.5, 0.5, GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL, new Insets( 5, 3, 3, 3 )));

        m_runButton = new JButton( UI.RUN.getName() );
        m_runButton.setToolTipText( "Run converter or calculate checksum" );
        m_runButton.setEnabled( false );
        m_buttonPanel.add( m_runButton, setConstraints( 0, 1, 0, 0, 0, 0, GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL, new Insets(  38, 3, 3, 3 )));

        m_saveButton = new JButton( UI.SAVE.getName() );
        m_saveButton.setToolTipText( "Save converted setup file" );
        m_saveButton.setEnabled( false );
        m_buttonPanel.add( m_saveButton, setConstraints( 0, 2, 0, 0, 0, 0, GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL, new Insets( 70, 3, 3, 3 )));

        m_closeButton = new JButton( UI.CLOSE.getName() );
        m_closeButton.setToolTipText( "Close utility" );
        m_buttonPanel.add( m_closeButton, setConstraints( 0, 4, 0, 0, 0, 0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets( 5, 3, 3, 3 )));

        m_statusTextField = new JTextField( 22 );
        m_statusTextField.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder( EtchedBorder.RAISED, Color.LIGHT_GRAY,
                Color.GRAY ), "Status", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.CENTER ) );
        m_statusTextField.setEditable( false );
        m_statusTextField.setAlignmentY( LEFT_ALIGNMENT );
        m_statusPanel = new JPanel( new GridBagLayout());
        m_statusPanel.setPreferredSize(new Dimension(200, 20));
        //m_statusPanel.add( m_statusTextField, setConstraints( 0, 0, 0, 0, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets( 1, 1, 1, 1 )));
        m_statusPanel.add( m_statusTextField );
        m_statusPanel.setBackground(Color.red);

        m_mainPanel.add( m_radioPanel, setConstraints( 0, 0, 0, 0, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(  50, 3, 3, 3 )));
        m_radioPanel.setBackground(Color.yellow);
        m_mainPanel.add( m_statusPanel, setConstraints( 0, 1, 0, 0, 0, 0, GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL, new Insets( 100, 1, 1, 1 )) );


        // Add listeners to each JPanel
        m_loadButton.addActionListener( this );
        m_runButton.addActionListener( this );
        m_saveButton.addActionListener( this );
        m_closeButton.addActionListener( this );


        // Add Panels with layouts to the JFrame
        getContentPane().add( m_buttonPanel, BorderLayout.WEST );
        getContentPane().add( m_mainPanel, BorderLayout.CENTER );
        
    }


    /**
     * Sets/returns the GridBagConstraints of a component within a JPanel.
     * 
     * @param xPos          - Components column location in pane
     * @param yPos          - Components row location in pane
     * @param width         - Spans across columns
     * @param height        - Spans across rows
     * @param weight_X      - Re-distribute component in X direction
     * @param weight_Y      - Re-distribute component in Y direction
     * @param anchor        - Anchor component at location in grid
     * @param fill          - Causes component to fill display area in horizontal or vertical direction
     * @param pad_cells     - Pad remainder of grid location with white space
     * @param gbc_prop      - GridBagConstraints object
     * @return              - GridBagConstraints object with constraints set
     */
    private GridBagConstraints setConstraints( int xPos, int yPos, int width, int height, double weight_X, double weight_Y, int anchor,
            int fill, Insets pad_cells ) {
        
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = xPos;
        gbc.gridy = yPos;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.weightx = weight_X;
        gbc.weighty = weight_Y;
        gbc.anchor = anchor;
        gbc.fill = fill;
        gbc.insets = pad_cells;

        return gbc;
    }


    /**
     * Instantiate's and sets the properties for the Checksum and Convert
     * JRadioButton's.  Add's the Checksum and Convert JRadioButtons' to the
     * argument JPanel.
     * 
     * @param panel - JPanel for Checksum & Convert JRadioButton's
     */
    private void createRadioGroup() {

        m_chksumRadioBtn = new JRadioButton( UI.CHECKSUM.getName() );
        m_chksumRadioBtn.setMnemonic(KeyEvent.VK_B);
        m_chksumRadioBtn.setActionCommand( UI.CHECKSUM.getName() );
        m_chksumRadioBtn.setToolTipText( "Calculate checksum" );
        m_chksumRadioBtn.setSelected(true);

        m_convertRadioBtn = new JRadioButton( UI.CONVERT.getName() );
        m_convertRadioBtn.setMnemonic(KeyEvent.VK_C);
        m_convertRadioBtn.setActionCommand( UI.CONVERT.getName() );
        m_convertRadioBtn.setToolTipText( "Converts settings & recalulates checksum"  );

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add( m_chksumRadioBtn );
        radioGroup.add( m_convertRadioBtn );
        
        m_radioPanel = new JPanel( new GridBagLayout());
        m_radioPanel.setPreferredSize(new Dimension( 100, 20 ));
        m_radioPanel.add( m_chksumRadioBtn );
        m_radioPanel.add(  m_convertRadioBtn );
        //panel.add( groupPanel, setConstraints( 0, 1, 0, 0, 0.5, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(  38, 3, 3, 3 )));

        m_chksumRadioBtn.addActionListener(this);
        m_convertRadioBtn.addActionListener(this);

        //panel.add( m_chksumRadioBtn );
        //panel.add(  m_convertRadioBtn );
    }



    /**
     * An Overridden method that returns the action event of the UI components.
     * A switch statement is used to determine the action event.  The method
     * instantiates a INI_Processing object and calls its methods to calculate
     * the checksum and/or convert the file.  Gets/Sets a File object to load
     * or Save a configuration file.  Disposes and exits utility.
     * 
     * @param evt   - Action event of the UI components
     */
    @ Override
    public void actionPerformed( ActionEvent evt ) {

        UI selection = UI.getType( evt.getActionCommand() );
        StringBuilder buildStr = null;

        switch ( selection ) {

            case LOAD :
                m_loadedFile = this.getFile( JFileChooser.OPEN_DIALOG, INI );

                if( m_loadedFile == null || !m_loadedFile.isFile() ) {
                    this.setStatus( Color.RED, "Load cancelled or file not valid", null );
                }
                else {
                    m_fileIsLoaded = true;
                    m_runButton.setEnabled( m_fileIsLoaded );
                    this.setStatus( Color.BLACK, "Load Complete", m_loadedFile.getName() );

                    try {
                        m_process = new ConvConvertLogicloadedFile, this );
                    }
                    catch( IOException e ) {
                        this.setStatus( Color.RED, "IOException while loading file", e.getMessage() );
                        m_fileIsLoaded = false;
                    }
                }

                break;


            case RUN :
                if( m_chksumRadioBtn.isSelected() && m_fileIsLoaded ) {
                    try {
                        m_process.setChecksum();
                    }
                    catch( IOException e ) {
                        this.setStatus( Color.RED, "IOException while calculating checksum", e.getMessage() );
                    }

                    buildStr = new StringBuilder( "Checksum = " + m_process.getChecksum() );
                    this.setStatus( Color.BLACK, buildStr.toString(), m_loadedFile.getName() );
                }

                if( m_convertRadioBtn.isSelected() && m_convertRadioBtn.isEnabled() && m_fileIsLoaded ) {
                    try {
                        m_process.convertFile();
                        m_process.setChecksum();
                    }
                    catch( IOException | ArrayIndexOutOfBoundsException | NumberFormatException e ) {
                        this.setStatus( Color.RED, "Exception while converting file", e.getMessage() );
                    }

                    m_fileIsConverted = true;
                    buildStr = new StringBuilder( "New checksum = " + m_process.getChecksum() );
                    this.setStatus( Color.BLACK, "File converted, ready to save", buildStr.toString() );// Should be using a StringBuilder here?
                }

                m_saveButton.setEnabled( m_fileIsLoaded );
                break;


            case SAVE :
                m_savedFile = this.getFile( JFileChooser.SAVE_DIALOG, INI );

                if( m_savedFile == null ) {
                    this.setStatus( Color.RED, "Save cancelled or file not valid", null );
                }
                else {
                    m_savedFile.setWritable( true );

                    try {
                        m_process.writeParam( m_savedFile );
                    }
                    catch( IOException e ) {
                        this.setStatus( Color.RED, "Exception while saving file", e.getMessage() );
                    }

                    if( m_fileIsConverted ) {
                        m_runButton.setEnabled( false );
                        m_fileIsLoaded = false;
                        m_fileIsConverted = false;
                        buildStr = new StringBuilder( "File savedd as " + m_savedFile.getName() );
                        this.setStatus( Color.BLACK, "File saved, process complete, load new file", buildStr.toString() );
                    }
                    else {
                        buildStr = new StringBuilder( "File saved as " + m_savedFile.getName() );
                        this.setStatus( Color.BLACK, "Save Complete", buildStr.toString() );
                    }

                    m_fileIsSaved = true;
                    m_saveButton.setEnabled( false );
                }

                break;


            case CHECKSUM :
                if( m_fileIsLoaded ) {
                    buildStr = new StringBuilder( "Checksum = " + m_process.getChecksum() );
                    this.setStatus( Color.BLACK, buildStr.toString(), m_loadedFile.getName() );
                }
                else {
                    this.setStatus( Color.BLACK, "Checksum = 0", "No file loaded" );
                }

                break;


            case CONVERT :
                if( m_fileIsLoaded ) {
                    this.setStatus( Color.BLACK, "Select Run to convert file", m_loadedFile.getName() );
                }
                else {
                    this.setStatus( Color.BLACK, "Convert selected, no file loaded to convert", null );
                }

                break;


            case CLOSE :

            default:
                this.dispose();
                break;
        }
    }


    /**
     * (non-Javadoc)
     * @see convert_ini_files.IConverterComponents#File getFile( int dialogType, String ext )
     */
    @ Override
    public File getFile( int dialogType, String ext ) {

        JFileChooser fileChooser = new JFileChooser();
        File file = null;
        int fileReturned = JFileChooser.ERROR_OPTION;

        fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
        fileChooser.setFileFilter( new FileNameExtensionFilter( ext.toUpperCase() + " Files", ext.toLowerCase() ));
        fileChooser.setAcceptAllFileFilterUsed( false );

        if( m_currentDir != null ) {
            fileChooser.setCurrentDirectory( m_currentDir );
        }

        if( dialogType == JFileChooser.OPEN_DIALOG ) {
            fileReturned = fileChooser.showOpenDialog( ConverterUI.this );
        }
        else if( dialogType == JFileChooser.SAVE_DIALOG ) {
            fileReturned = fileChooser.showSaveDialog( ConverterUI.this );
        }

        if ( fileReturned == JFileChooser.APPROVE_OPTION ) {
            File userFile;
            userFile = fileChooser.getSelectedFile();
            if( !userFile.getName().toLowerCase().endsWith( ".ini" ) ) {
                StringBuilder sb = new StringBuilder();
                sb.append( userFile.getPath() ).append( ".ini" );
                file = new File( sb.toString() );
            }
            else {
                file = userFile;
            }
            m_currentDir = fileChooser.getCurrentDirectory();
        }

        return file;
    }


    /* (non-Javadoc)
     * @see convert_ini_files.IConverterComponents#setStatus(java.awt.Color, java.lang.String, java.lang.String)
     */
    @ Override
    public void setStatus( Color color, String message, String tip ) {

        m_statusTextField.setForeground( color );
        m_statusTextField.setText( message );

        if( tip != null ) {
            m_statusTextField.setToolTipText( tip );
        }
    }


    /**
     * main class, instantiates the UI object and set UI attributes.
     * 
     * @param args - no command line arguments used
     */
    public static void main( String[] args ) {
        ConverterUI gridLayout = new ConverterUI();
        gridLayout.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        gridLayout.setSize( 400, 300 );
        gridLayout.setLocationRelativeTo( gridLayout );
        gridLayout.setVisible(true);
        //gridLayout.pack();
    }
}
