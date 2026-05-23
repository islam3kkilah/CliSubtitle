/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.view;

/**
 *
 * @author i3akk
 */

import com.islam.cliprojectmvc.model.SubtitleTableModel;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class FrameView extends JFrame {
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenu videoMenu;
    JMenu helpMenu;
    JMenu consoleThemeMenu;
    JMenu themesMenu;
    
    JRadioButtonMenuItem consolasItem;
    JRadioButtonMenuItem kawkabItem;
    
    JMenuItem exitItem;
    JMenuItem openVideoItem;
    JMenuItem helpItem;
    
    JRadioButtonMenuItem  draculaItem;
    JRadioButtonMenuItem GreenItem;
    
    JLabel startTime;
    JLabel endTime;
    JSlider seekBar;
    JButton playBtn;
    JButton stopBtn;
    JButton pauseBtn;
    
    EmbeddedMediaPlayerComponent videoComponent;
    JLabel volumeLabel;
    
    JTabbedPane tabbedPane;
    
    JLabel emptyLabel;
    
    JTable table;
    SubtitleTableModel model;
    JScrollBar tableScrollBar;
    
    JTextPane area;
    JScrollPane areaScrollPane;
    
    JToolBar toolBar;
    JButton newButton;
    JButton openButton;
    JButton saveButton;
    
    private Font kawkabFont;
    private Font consolasFont;
    
    JLayeredPane videoLayer;
    JPanel controlBar;
    JPanel centerControls;
    JPanel leftControls;
    JPanel topLeftPanel;
    JPanel emptyPanel;
    JPanel topRightPanel;
    JPanel wholeTopPanel;
    JPanel toolbarAndTopPanel;
    JPanel sumOfPanel;
    public FrameView() {
        //================== menu bar ========================
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        videoMenu = new JMenu("Video");
        themesMenu = new JMenu("Styling");
        helpMenu = new JMenu("Help");
        
        consoleThemeMenu = new JMenu("Default Themes");
        consolasItem = new JRadioButtonMenuItem("Consolas");
        kawkabItem = new JRadioButtonMenuItem("Kawkab");
        exitItem = new JMenuItem("Exit");
        openVideoItem = new JMenuItem("Open Video");
        helpItem = new JMenuItem("about");
        draculaItem = new JRadioButtonMenuItem("Dracula Theme");
        GreenItem = new JRadioButtonMenuItem("Green Theme");
        //============ toolbar ================================
        toolBar = new JToolBar();
        newButton = new JButton("New");
        openButton = new JButton("Open");
        saveButton = new JButton("Save");
        //============ players buttons =========================
        startTime = new JLabel("00:00:00");
        endTime = new JLabel("00:00:00");
        
        seekBar = new JSlider(0, 1000, 0);
        seekBar.setFocusable(false);
        seekBar.setBorder(null);
        seekBar.setOpaque(false);
        seekBar.setPaintTicks(false);
        seekBar.setPaintLabels(false);
        seekBar.setPreferredSize(new Dimension(300, 20));
        
        ImageIcon playIcon = new ImageIcon(
            getClass().getResource("/icons/play-16.png")
        );
        
        ImageIcon pauseIcon = new ImageIcon(
            getClass().getResource("/icons/pause-16.png")
        );
        
        ImageIcon stopIcon = new ImageIcon(
            getClass().getResource("/icons/stop-16.png")
        );
        
        playBtn = new JButton(playIcon);
        stopBtn = new JButton(stopIcon);
        pauseBtn = new JButton(pauseIcon);
        //=================== vlcj =============================
        videoComponent = new EmbeddedMediaPlayerComponent();
        volumeLabel = new JLabel();
        
        emptyLabel = new JLabel(
            "<html><pre style='font-family:monospace;'>"
            + loadAscii("/ascii/reader.txt")
            + "</pre></html>"
        );
        //====================== JTable ========================
        tabbedPane = new JTabbedPane();
        model = new SubtitleTableModel();
        table = new JTable(model);
        //====================== JTextPanel ====================
        area = new JTextPane();
        areaScrollPane = new JScrollPane(area);
        tabbedPane.add(areaScrollPane, "Console");
        // ====================== JPanel =======================
        leftControls = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        centerControls = new JPanel(new BorderLayout(7, 0));
        controlBar = new JPanel(new BorderLayout());
        videoLayer = new JLayeredPane();
        topLeftPanel = new JPanel(new BorderLayout());
        emptyPanel = new JPanel(new BorderLayout());
        
        
        
        setJMenuBar(menuBar);
        setSize(1200,700);
        setTitle("CliSUB");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        applyDraculaTheme();
        draculaItem.setSelected(true);
    }

    private void initComponents() {
        //============= vlcj ============================
        volumeLabel.setBackground(new Color(0, 0, 0, 0));
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        volumeLabel.setOpaque(false); // no gray background
        volumeLabel.setVisible(false);
        
        playBtn.setFocusable(false);
        playBtn.setPreferredSize(new Dimension(23, 23));
        playBtn.setMinimumSize(new Dimension(23, 23));
        playBtn.setMaximumSize(new Dimension(23, 23));
        
        pauseBtn.setFocusable(false);
        pauseBtn.setPreferredSize(new Dimension(23, 23));
        pauseBtn.setMinimumSize(new Dimension(23, 23));
        pauseBtn.setMaximumSize(new Dimension(23, 23));
        
        stopBtn.setFocusable(false);
        stopBtn.setPreferredSize(new Dimension(23, 23));
        stopBtn.setMinimumSize(new Dimension(23, 23));
        stopBtn.setMaximumSize(new Dimension(23, 23));
        
        //============== menu bar =================
        ButtonGroup themeGroup = new ButtonGroup();
        themeGroup.add(draculaItem);
        themeGroup.add(GreenItem);
        draculaItem.setSelected(true);
        
        consoleThemeMenu.add(draculaItem);
        consoleThemeMenu.add(GreenItem);
        fileMenu.add(exitItem);
        themesMenu.add(consoleThemeMenu);
        helpMenu.add(helpItem);
        videoMenu.add(openVideoItem);
        
        menuBar.add(fileMenu);
        menuBar.add(videoMenu);
        menuBar.add(themesMenu);
        menuBar.add(helpMenu);
        //=============== toolbar =============
        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        //=============== JTextPane ===========
        kawkabFont = loadFont("/fonts/KawkabMono-Regular.ttf", 11f);
        consolasFont = loadFont("/fonts/Consolas-Regular.ttf", 14f);
        //=================== JTable ==========
        
        emptyLabel.setHorizontalAlignment(JLabel.CENTER);
        emptyLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emptyLabel.setForeground(Color.GRAY);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setColumnSelectionAllowed(false);
        JTableHeader header = table.getTableHeader();
        table.setDropMode(javax.swing.DropMode.ON);
        table.setFillsViewportHeight(true);
        table.setDropMode(DropMode.ON);
        Color bg = new Color(238, 238, 238);
        table.setBackground(bg);
        table.setOpaque(true);
        table.setSelectionBackground(Color.WHITE);
        table.setSelectionForeground(Color.BLACK);
        table.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.GRAY));
        table.getTableHeader().setResizingAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setMinWidth(55);
        table.getColumnModel().getColumn(1).setMaxWidth(90);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        
        table.getColumnModel().getColumn(2).setMinWidth(55);
        table.getColumnModel().getColumn(2).setMaxWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getTableHeader().setPreferredSize(
            new Dimension(
                table.getTableHeader().getPreferredSize().width,
                25 // height
            )
        );
        
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                label.setBackground(new Color(193, 213, 232)); 
                label.setForeground(Color.BLACK);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(BorderFactory.createMatteBorder(
                    0, 0, 0, 1, Color.LIGHT_GRAY
                ));

                return label;
            }
        });
        header.setBorder(BorderFactory.createRaisedBevelBorder());
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
        
        
        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                java.awt.Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(new Color(201, 235, 206)); // light green
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(102, 205, 170)); // selected green-ish
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
        //=============== JPanels  ============
        leftControls.setOpaque(false);
        leftControls.add(playBtn);
        leftControls.add(pauseBtn);
        leftControls.add(stopBtn);
        
        centerControls.setOpaque(false);
        centerControls.add(startTime, BorderLayout.WEST);
        centerControls.add(seekBar, BorderLayout.CENTER);
        centerControls.add(endTime, BorderLayout.EAST);
        
        controlBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        controlBar.setBackground(new Color(241, 250, 140));
        controlBar.setOpaque(true);
        controlBar.add(leftControls, BorderLayout.WEST);
        controlBar.add(centerControls, BorderLayout.CENTER);
        
        videoLayer.setOpaque(false);
        videoLayer.setBackground(new Color(0, 0, 0, 0));
        videoLayer.setLayout(null);
        videoLayer.add(videoComponent, Integer.valueOf(0));
        videoLayer.add(volumeLabel, Integer.valueOf(1));
        videoComponent.setBounds(0, 0, 800, 500);
        volumeLabel.setBounds(10, 10, 80, 20);
        
        topLeftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Player",TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.PLAIN, 12), Color.BLACK));
        topLeftPanel.add(videoLayer, BorderLayout.CENTER);
        topLeftPanel.add(controlBar, BorderLayout.SOUTH);
        topLeftPanel.setOpaque(false);
        topLeftPanel.setVisible(false);
        JPanel BottomPanel = new JPanel(new BorderLayout());
        BottomPanel.add(tabbedPane, BorderLayout.CENTER);
        
        emptyPanel.setOpaque(false);
        emptyPanel.setAlignmentX(0.5f);
        emptyPanel.setAlignmentY(0.5f);
        emptyPanel.add(emptyLabel);
        
        JScrollPane JTableScroll = new JScrollPane(table);
        JTableScroll.setOpaque(true);
        JTableScroll.setAlignmentX(0.5f);
        JTableScroll.setAlignmentY(0.5f);
        
        tableScrollBar = JTableScroll.getVerticalScrollBar();
        
        JPanel tableLayer = new JPanel();
        tableLayer.setLayout(new OverlayLayout(tableLayer));
        tableLayer.add(emptyPanel);
        tableLayer.add(JTableScroll);
        
        topRightPanel = new JPanel(new BorderLayout());
        topRightPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Reader",TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.PLAIN, 12), Color.BLACK));
        topRightPanel.add(tableLayer, BorderLayout.CENTER);
        
        wholeTopPanel = new JPanel(new BorderLayout());
        wholeTopPanel.add(topLeftPanel, BorderLayout.WEST);
        wholeTopPanel.add(topRightPanel, BorderLayout.CENTER);
        
        toolbarAndTopPanel = new JPanel(new BorderLayout());
        toolbarAndTopPanel.add(toolBar, BorderLayout.NORTH);
        toolbarAndTopPanel.add(wholeTopPanel, BorderLayout.CENTER);
        
        sumOfPanel = new JPanel(new GridLayout(2,1));
        sumOfPanel.add(toolbarAndTopPanel);
        sumOfPanel.add(BottomPanel);
        
        
        
        add(sumOfPanel);
        StyledDocument doc = area.getStyledDocument();
        ((AbstractDocument) doc).setDocumentFilter(
                new EndOnlyDocumentFilter(area)
        );
    }
    
    public JMenuItem getExitItem() {
        return exitItem;
    }

    public JMenuItem getOpenVideoItem() {
        return openVideoItem;
    }

    public JMenuItem getHelpItem() {
        return helpItem;
    }
    
    public JRadioButtonMenuItem getDraculaItem() {
        return draculaItem;
    }

    public JRadioButtonMenuItem getGreenItem() {
        return GreenItem;
    }
    
    public JScrollBar getTableScrollBar() {
        return tableScrollBar;
    }
    
    public JLayeredPane getVideoLayer(){
        return videoLayer;
    }
    public EmbeddedMediaPlayerComponent getVideoComponent(){
        return videoComponent;
    }
    
    public JPanel getTopLeftPanel(){
        return topLeftPanel; 
    }
    
    public SubtitleTableModel getModel(){
        return model;
    }
    
    public JButton getPlayBtn(){
        return playBtn;
    }
    
    public JButton getStopBtn(){
        return stopBtn;
    }
    
    public JButton getPauseBtn(){
        return pauseBtn;
    }
    
    public JLabel getVolumeLabel(){
        return  volumeLabel;
    }
    
    public JTable getTable(){
        return table;
    }
    
    public JPanel getEmptyPanel(){
        return emptyPanel;
    }
    
    public JSlider getSeekBar(){
        return seekBar;
    }
    
    public JLabel getStartTime(){
        return startTime;
    }
    
    public JLabel getEndTime(){
        return endTime;
    }
    
    public JTextPane getArea(){
        return area;
    }
    
    public JTextPane getTextPane(){
        return area;
    }
    //==================loadFont===========================
    private Font loadFont(String path, float size) {
        try (InputStream is = getClass().getResourceAsStream(path)) {

            Font font = Font.createFont(Font.TRUETYPE_FONT, is)
                    .deriveFont(size);

            GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .registerFont(font);

            return font;

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Monospaced", Font.PLAIN, (int) size);
        }
    }
    
    //==================loadAscii===========================
    private String loadAscii(String path) {
        try (java.io.InputStream is = getClass().getResourceAsStream(path);
             java.util.Scanner sc = new java.util.Scanner(is)) {

            sc.useDelimiter("\\A");
            return sc.hasNext() ? sc.next() : "";

        } catch (Exception e) {
            e.printStackTrace();
            return "ASCII not found";
        }
    }
    
    //==================applyTextPaneTheme===========================
    private void applyTextPaneTheme(Color bg,Color fg,Color caret,Color selectionBg,Color selectionFg,Font font) {

        area.setBackground(bg);

        area.setForeground(fg);

        area.setCaretColor(caret);

        area.setSelectionColor(selectionBg);

        area.setSelectedTextColor(selectionFg);

        area.setFont(font);

        // CHANGE OLD TEXT
        javax.swing.text.StyledDocument doc =
                area.getStyledDocument();

        javax.swing.text.SimpleAttributeSet attrs =
                new javax.swing.text.SimpleAttributeSet();

        javax.swing.text.StyleConstants
                .setForeground(attrs, fg);

        javax.swing.text.StyleConstants
                .setFontFamily(attrs, font.getFamily());

        javax.swing.text.StyleConstants
                .setFontSize(attrs, font.getSize());

        doc.setCharacterAttributes(
                0,
                doc.getLength(),
                attrs,
                true
        );

        // CHANGE FUTURE TEXT
        area.setCharacterAttributes(attrs, true);
    }
    
    public void applyGreenTheme() {
        applyTextPaneTheme(Color.BLACK,Color.GREEN,Color.WHITE,UIManager.getColor("TextPane.selectionBackground"),UIManager.getColor("TextPane.selectionForeground"),kawkabFont);
    }
    
    public void applyDraculaTheme() {
        applyTextPaneTheme(new Color(40, 42, 54),new Color(241, 250, 140), Color.WHITE, new Color(98, 114, 164),Color.WHITE,kawkabFont);
    }
    
    public boolean isCaretOnLastLine(JTextPane textPane) {
        try {
            int caret = textPane.getCaretPosition();
            System.out.println(caret);
            int docLength = textPane.getDocument().getLength();
            System.out.println(docLength);
            int caretLine = javax.swing.text.Utilities.getRowStart(textPane, caret);
            System.out.println(caretLine);
            int lastLine  = javax.swing.text.Utilities.getRowStart(textPane, docLength);
            System.out.println(lastLine);

            return caretLine == lastLine;
        } catch (BadLocationException e) {
            return false;
        }
    }

    public void appendPrompt() {
        SwingUtilities.invokeLater(() -> {
            try {
                StyledDocument doc = area.getStyledDocument();
                String prompt = "> ";
                doc.insertString(doc.getLength(), prompt, null);
                area.setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });
    }

    public String getCurrentLineText(JTextPane textPane) {
        try {
            int caretPos = textPane.getCaretPosition();
            int lineStart = javax.swing.text.Utilities.getRowStart(textPane, caretPos);
            int lineEnd = javax.swing.text.Utilities.getRowEnd(textPane, caretPos);
            return textPane.getDocument().getText(lineStart, lineEnd - lineStart);
        } catch (BadLocationException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void append(String text) {
        try {
            Document doc = area.getDocument();
            doc.insertString(doc.getLength(), text, null);
            area.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        area.setText("");
    }
}