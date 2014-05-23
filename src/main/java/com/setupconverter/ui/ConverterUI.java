/**
 *  ConverterUI
 *  Paul Wallace
 *  May 2014
 * 
 *  ConverterUI provides the user interface for the application SetupConverter.
 * 
 *  Main attributes:
 *      *   Allows user to loads a setup file from the Windows file system.
 *      *   Allows user to re-calculate checksum of loaded setup file.
 *      *   Allows user to convert the gains and I/O of a setup file which then 
 *          can then be used to operate a specific test stand or cutting machine.
 *          The original I/O is relocated to I/O 49 and higher, so not to loose
 *          integrity of file.  Gain and Speed settings are modified from original
 *          file.
 *      *   Allows user to save the converted file when the selected processes
 *          is complete.
 * 
 *  Implements:  ActionListener & IConverterComponents
 *  Extends: JFrame
 */

package com.setupconverter.ui;

import com.setupconverter.logic.ConvertLogic;
import com.setupconverter.ui.IComponents.SYSTEM;
import static com.setupconverter.ui.IComponents.UI.*;

import javax.swing.Box;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import static java.awt.Component.LEFT_ALIGNMENT;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.JLabel;


/**
 * ConverterUI is a UI that provides an interface for the a user to convert
 * a configuration file or to calculate the files checksum.
 * 
 * @author prwallace
 */
public class ConverterUI extends JFrame {

    private ConvertLogic m_process;

    private final JPanel m_mainPanel;
    private final JPanel m_buttonPanel;
    private final JPanel m_radioPanel;
    private final JPanel m_selectionPanel;
    private final JPanel m_statusPanel;

    private final JButton m_loadButton;
    private final JButton m_runButton;
    private final JButton m_saveButton;
    private final JButton m_closeButton;

    private JRadioButton m_cksumRadioBtn;
    private JRadioButton m_convertRadioBtn;
    private final JTextField m_statusTextField;
    private JComboBox m_comboBox;
    private JLabel m_comboBoxLabel;

    private final Toolkit toolKit = Toolkit.getDefaultToolkit();
    private final Dimension screenSize = toolKit.getScreenSize();

    private File m_currentDir = null;
    private File m_loadedFile = null;
    private File m_savedFile = null;

    private final static String INI = "ini";
    private final String[] m_systems;
    private String m_selection;
    private int m_index;

    public boolean m_fileIsLoaded;
    public boolean m_fileIsSaved;
    public boolean m_fileIsConverted;

    public OperateConverter m_operate;


    /**
     * Default constructor for class ConverterUI.  
     * Adds JPanel's and all UI components to the JFrame. 
     * Instantiates an OperateConverter object to interact with client and listen
     * for component changes.  Draws and closes UI and its components.
     */
    public ConverterUI() {
        super( "Convert_INI" );
        m_systems = new String[] { SYSTEM.BENCH.getName(), SYSTEM.HYPATH.getName() };
        m_operate = new OperateConverter();
        m_index = m_systems.length + 1;

        // Frame settings
        JFrame.setDefaultLookAndFeelDecorated( true );
        setPreferredSize( new Dimension( 400, 300 ));
        setLocation( screenSize.width / 3, screenSize.height / 4);
        pack();


        // Disposes and Closes UI
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
                System.exit(0);
            }
        });


        // Add button panel and button components
        m_mainPanel = new JPanel( new GridBagLayout());
        m_mainPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder() ) );

        m_buttonPanel = new JPanel( new GridBagLayout());
        m_buttonPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder() ) );

        m_loadButton = new JButton( LOAD.getName() );
        m_loadButton.setToolTipText( "Load user setup file" );
        m_buttonPanel.add( m_loadButton, addConstraints( 0, 0, 0, 0, 0.5, 0.5, GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL, new Insets( 5, 3, 3, 3 )));

        m_runButton = new JButton( RUN.getName() );
        m_runButton.setToolTipText( "Run converter or calculate checksum" );
        m_runButton.setEnabled( false );
        m_buttonPanel.add( m_runButton, addConstraints( 0, 1, 0, 0, 0, 0, GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL, new Insets(  38, 3, 3, 3 )));

        m_saveButton = new JButton( SAVE.getName() );
        m_saveButton.setToolTipText( "Save converted setup file" );
        m_saveButton.setEnabled( false );
        m_buttonPanel.add( m_saveButton, addConstraints( 0, 2, 0, 0, 0, 0, GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL, new Insets( 70, 3, 3, 3 )));

        m_closeButton = new JButton( CLOSE.getName() );
        m_closeButton.setToolTipText( "Close utility" );
        m_buttonPanel.add( m_closeButton, addConstraints( 0, 4, 0, 0, 0, 0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets( 5, 3, 3, 3 )));


        // Add radio panel and radio group
        m_radioPanel = new JPanel( new GridBagLayout());
        m_mainPanel.add( m_radioPanel, addConstraints( 0, 0, 1, 1, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(  5, 3, 3, 3 )));
        createRadioGroup();


        // Add selection panel and pull down list
        m_selectionPanel = new JPanel( new GridBagLayout());
        m_selectionPanel.setPreferredSize(new Dimension( 275, 120 ));
        m_mainPanel.add( m_selectionPanel, addConstraints( 0, 3, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 1, 1, 1, 1 )));
        createComboBox();
        


        // Add status panel and status text field
        m_statusPanel = new JPanel( new GridBagLayout());
        m_mainPanel.add( m_statusPanel, addConstraints( 0, 3, 1, 1, 0, 1, GridBagConstraints.SOUTH, GridBagConstraints.NONE, new Insets( 1, 1, 1, 1 )));

        m_statusTextField = new JTextField( 22 );
        m_statusTextField.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Status", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.CENTER ) );
        m_statusTextField.setEditable( false );
        m_statusTextField.setAlignmentY( LEFT_ALIGNMENT );
        m_statusPanel.add( m_statusTextField, addConstraints( 0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 1, 1, 1, 1 )));


        // Add listeners to each JPanel
        m_loadButton.addActionListener( m_operate );
        m_runButton.addActionListener( m_operate );
        m_saveButton.addActionListener( m_operate );
        m_closeButton.addActionListener( m_operate );


        // Add Panels with layouts to the JFrame
        getContentPane().add( m_buttonPanel, BorderLayout.WEST );
        getContentPane().add( m_mainPanel, BorderLayout.CENTER );
        
        /*m_radioPanel.setBackground(Color.red);
        m_selectionPanel.setBackground(Color.yellow);
        m_statusPanel.setBackground(Color.blue);*/
    }


    /**
     * Creates the checksum and convert radio buttons and adds them to a the
     * m_radioPanel JPanel.  Adds an action listener to both radio buttons.
     */
    private void createRadioGroup() {
        m_cksumRadioBtn = new JRadioButton( CHECKSUM.getName() );
        m_cksumRadioBtn.setActionCommand( CHECKSUM.getName() );
        m_cksumRadioBtn.setToolTipText( "Calculate checksum" );
        m_cksumRadioBtn.setSelected(true);

        m_convertRadioBtn = new JRadioButton( CONVERT.getName() );
        m_convertRadioBtn.setActionCommand( CONVERT.getName() );
        m_convertRadioBtn.setToolTipText( "Converts settings & recalulates checksum"  );

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add( m_cksumRadioBtn );
        radioGroup.add( m_convertRadioBtn );

        Box radioBox = Box.createHorizontalBox();
        radioBox.add( m_cksumRadioBtn );
        radioBox.add( m_convertRadioBtn );
        m_radioPanel.add( radioBox, addConstraints( 0, 0, 1, 1, 0, 0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(  3, 3, 3, 3 )));

        m_cksumRadioBtn.addActionListener( m_operate );
        m_convertRadioBtn.addActionListener( m_operate );
    }


    private void createComboBox() {
        m_comboBoxLabel = new JLabel( "Convertion Type:" );
        m_selectionPanel.add( m_comboBoxLabel, addConstraints( 0, 0, 1, 1, 0, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets( 1, 1, 1, 25 )));
        m_comboBox = new JComboBox( m_systems );
        m_comboBox.setSelectedIndex( m_systems.length -1 );
        //m_systemList.setEnabled( false );
        m_comboBox.setToolTipText("Select conversion type");
        m_selectionPanel.add( m_comboBox, addConstraints( 0, 1, 1, 1, 0, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets( 1, 1, 80, 1 )));

        m_comboBox.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent evt ) {
                m_index = m_comboBox.getSelectedIndex();
                m_selection = (String)m_comboBox.getSelectedItem();
            }
        });
    }


    /**
     * Creates a GridBagConstraint object and adds constraints based on argument
     * values and returns the object.
     * 
     * @param xPos          - Components row location in pane
     * @param yPos          - Components column location in pane
     * @param width         - Spans across columns
     * @param height        - Spans across rows
     * @param weight_X      - Re-distribute component in X direction
     * @param weight_Y      - Re-distribute component in Y direction
     * @param anchor        - Anchor component at location in grid
     * @param fill          - fill display area in horizontal or vertical direction
     * @param pad_cells     - Pad component inside cell from Top, Right, Bottom, and Left
     * @param gbc_prop      - GridBagConstraints object
     * @return              - GridBagConstraints object with constraints set
     */
    private GridBagConstraints addConstraints( int xPos, int yPos, int width, int height, double weight_X, double weight_Y, int anchor,
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
     * class OperateConverter is an inner class for class ConverterUI.   The
     * class provides an action listener method for the UI components.  The class
     * implements the interface IComponents, which allows the client to load an
     * ini file to convert and provides a method for displaying status messages.
     */
    public class OperateConverter implements ActionListener, IComponents {

        /**
         * Override action listener method for the UI components of class ConverterUI.java
         * 
         * @param evt   - Action event of the UI components
         */
        @ Override
        public void actionPerformed( ActionEvent evt ) {
            UI selection = UI.getType( evt.getActionCommand() );
            StringBuilder buildString = null;

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
                            m_process = new ConvertLogic( m_loadedFile, this );
                        }
                        catch( IOException e ) {
                            this.setStatus( Color.RED, "IOException while loading file", e.getMessage() );
                            m_fileIsLoaded = false;
                        }
                    }

                    break;


                case RUN :
                    if( m_cksumRadioBtn.isSelected() && m_fileIsLoaded ) {
                        try {
                            m_process.setChecksum();
                        }
                        catch( IOException e ) {
                            this.setStatus( Color.RED, "IOException while calculating checksum", e.getMessage() );
                        }

                        buildString = new StringBuilder( "Checksum = " ).append( m_process.getChecksum() );
                        this.setStatus( Color.BLACK, buildString.toString(), m_loadedFile.getName() );
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
                        buildString = new StringBuilder( "New checksum = " ).append( m_process.getChecksum() );
                        this.setStatus( Color.BLACK, "File converted, ready to save", buildString.toString() );// Should be using a StringBuilder here?
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
                            buildString = new StringBuilder( "File saved as " ).append( m_savedFile.getName() );
                            this.setStatus( Color.BLACK, "File saved, process complete, load new file", buildString.toString() );
                        }
                        else {
                            buildString = new StringBuilder( "File saved as " ).append( m_savedFile.getName() );
                            this.setStatus( Color.BLACK, "Save Complete", buildString.toString() );
                        }

                        m_fileIsSaved = true;
                        m_saveButton.setEnabled( false );
                    }

                    break;


                case CHECKSUM :
                    if( m_fileIsLoaded ) {
                        buildString = new StringBuilder( "Checksum = " ).append( m_process.getChecksum() );
                        this.setStatus( Color.BLACK, buildString.toString(), m_loadedFile.getName() );
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
                    dispose();
                    System.exit( 0 );
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

        /**
         * Gets/Returns combo box selected index
         * @return  - index selection from combo box
         */
        public int getSelectedIndex() {
            return m_index;
        }


        public String getSelectedSystem() {
            return m_selection;
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
    }


    /**
     * main class, instantiates the UI object.
     * @param args - no command line arguments used
     */
    public static void main( String[] args ) {
        new ConverterUI().setVisible( true );
        //ConverterUI convert = new ConverterUI();
        //convert.setVisible( true );
    }
}
