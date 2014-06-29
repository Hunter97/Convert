/**
 *  ConvertUI.java
 *  Paul Wallace
 *  June 2014
 * 
 *  ConvertUI provides the user interface for the application SetupConverter.
 * 
 *  Main attributes:
 *      *   Allows user to load a setup file from the Windows file system.
 *      *   Allows user to re-calculate checksum of loaded setup file and then
 *              save that file with new checksum applied.
 *      *   Allows user to convert the gains and I/O of a setup file which then 
 *              can then be used to operate a specific test stand or cutting machine.
 *              The original I/O is relocated to I/O 49 and higher, so not to loose
 *              integrity of file.  Gain and Speed settings are modified from original
 *              file.
 *      *   Allows user to save the converted file when the selected processes
 *              is complete.
 * 
 *  Innerclass: implements ActionListener & IComponents, handles action events
 *      and file handling between UI and logic class.
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
import javax.swing.JComboBox;
import javax.swing.JLabel;

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


/**
 * ConvertUI provides the UI for the SetupConverter application.
 * @author prwallace
 */
public class ConvertUI extends JFrame {

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

    public boolean m_fileIsLoaded;
    public boolean m_fileIsSaved;
    public boolean m_fileIsConverted;

    /**
     * Handles action events from the UI, displays status messages, and interfaces
     * with logic class.
     */
    public OperateConverter m_operate;


    /**
     * Default constructor for class ConverterUI.  
     * Constructs the UI and instantiates an OperateConverter object.
     */
    public ConvertUI() {
        super( "Convert_INI" );
        m_systems = new String[] { SYSTEM.BENCH.getName(), SYSTEM.HYPATH.getName() };
        m_operate = new OperateConverter();

        // Set frame properties
        JFrame.setDefaultLookAndFeelDecorated( true );
        setPreferredSize( new Dimension( 400, 300 ));
        setLocation( screenSize.width / 3, screenSize.height / 4);
        pack();


        // Free all resources and then close UI
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
                System.exit(0);
            }
        });


        // Add button panel and control buttons
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


        // Add radio panel and radio buttons
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


        // Add action listeners to control buttons
        m_loadButton.addActionListener( m_operate );
        m_runButton.addActionListener( m_operate );
        m_saveButton.addActionListener( m_operate );
        m_closeButton.addActionListener( m_operate );


        // Add panels to the frame
        getContentPane().add( m_buttonPanel, BorderLayout.WEST );
        getContentPane().add( m_mainPanel, BorderLayout.CENTER );
    }


    /**
     *  Add radio buttons and attach an action listener to radio buttons
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


    /**
     * Add combo box and attach an action listener to combo box
     */
    private void createComboBox() {
        m_comboBoxLabel = new JLabel( "Convertion Type:" );
        m_selectionPanel.add( m_comboBoxLabel, addConstraints( 0, 0, 1, 1, 0, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets( 1, 1, 1, 25 )));
        m_comboBox = new JComboBox( m_systems );
        m_comboBox.setSelectedIndex( m_systems.length -1 );
        m_comboBox.setToolTipText("Select conversion type");
        m_selectionPanel.add( m_comboBox, addConstraints( 0, 1, 1, 1, 0, 0.5, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets( 1, 1, 80, 1 )));
        m_selection = (String)m_comboBox.getSelectedItem();

        m_comboBox.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent evt ) {
                m_selection = (String)m_comboBox.getSelectedItem();
            }
        });
    }


    /**
     * Add/return a GridBagConstraint object based on argument values.
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
    private GridBagConstraints addConstraints( int xPos, int yPos, int width, int height, double weight_X, double weight_Y, int anchor, int fill, Insets pad_cells ) {
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
     * Implement action events for UI control and radio buttons, displays status
     * messages in UI, instantiates logic object, and allows user access to Windows
     * file system to load/save setup file.
     */
    public class OperateConverter implements ActionListener, IComponents {

        /**
         * Listen for action events and perform functions based on event.
         * @param evt   - Action event of the UI component
         */
        @ Override
        public void actionPerformed( ActionEvent evt ) {
            UI selection = UI.getType( evt.getActionCommand() );

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

                        this.setStatus( Color.BLACK, new StringBuilder( "Checksum = " ).append( m_process.getChecksum() ).toString(), m_loadedFile.getName() );
                    }

                    if( m_convertRadioBtn.isSelected() && m_convertRadioBtn.isEnabled() && m_fileIsLoaded ) {
                        try {
                            m_process.convert();
                            m_process.setChecksum();
                        }
                        catch( IOException | ArrayIndexOutOfBoundsException | NumberFormatException e ) {
                            this.setStatus( Color.RED, "Exception while converting file", e.getMessage() );
                        }

                        m_fileIsConverted = true;
                        this.setStatus( Color.BLACK, "File converted, ready to save", new StringBuilder( "New checksum = " ).append( m_process.getChecksum() ).toString() );
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
                            m_process.write( m_savedFile );
                        }
                        catch( IOException e ) {
                            this.setStatus( Color.RED, "Exception while saving file", e.getMessage() );
                        }

                        if( m_fileIsConverted ) {
                            m_runButton.setEnabled( false );
                            m_fileIsLoaded = false;
                            m_fileIsConverted = false;
                            this.setStatus( Color.BLACK, "File saved, process complete, load new file", new StringBuilder( "File saved as " ).append( m_savedFile.getName()).toString() );
                        }
                        else {
                            this.setStatus( Color.BLACK, "Save Complete", new StringBuilder( "File saved as " ).append( m_savedFile.getName() ).toString() );
                        }

                        m_fileIsSaved = true;
                        m_saveButton.setEnabled( false );
                    }

                    break;


                case CHECKSUM :
                    if( m_fileIsLoaded ) {
                        this.setStatus( Color.BLACK, new StringBuilder( "Checksum = " ).append( m_process.getChecksum() ).toString(), m_loadedFile.getName() );
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
                    this.setStatus( Color.RED, "Unknown event occured, UI will shut down", null );
                    dispose();
                    System.exit( 0 );
                    break;
            }
        }


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
                fileReturned = fileChooser.showOpenDialog( ConvertUI.this );
            }
            else if( dialogType == JFileChooser.SAVE_DIALOG ) {
                fileReturned = fileChooser.showSaveDialog( ConvertUI.this );
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


        @Override
        public String getSelectedSystem() {
            return m_selection;
        }


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
     * @param args - Command line argument
     */
    public static void main( String[] args ) {
        new ConvertUI().setVisible( true );
    }
}
