/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;
import com.islam.cliprojectmvc.model.ConsoleModel;
import com.islam.cliprojectmvc.model.Subtitle;
import com.islam.cliprojectmvc.util.SrtParser;
import com.islam.cliprojectmvc.util.SrtWriter;
import com.islam.cliprojectmvc.view.FrameView;
import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

/**
 *
 * @author i3akk
 */
public class MainController {
    private final FrameView view;
    private volatile boolean userSeeking = false;
    private boolean syncing = false;
    private int currentSubtitleRow = -1;
    private boolean autoFollowSubtitle = true;
    private long segmentEndTime = -1;
    boolean isSeeking = false;
    private boolean previewMode = false;
    
    ConsoleModel model = new ConsoleModel();
    TextPaneController paneController;
    public MainController(FrameView view) {
        model = new ConsoleModel();
        paneController = new TextPaneController(view, model);
        this.view = view;
        
        view.getExitItem().addActionListener(e -> System.exit(0));
        view.getOpenVideoItem().addActionListener(e -> {

        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(
            new FileNameExtensionFilter(
                "Video Files",
                "mp4", "mkv", "avi", "mov", "wmv", "flv", "webm"
            )
        );

        int result = chooser.showOpenDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {

            File selectedFile = chooser.getSelectedFile();
            view.getVideoLayer().revalidate();
            view.getVideoLayer().repaint();
            view.getVideoComponent().setBounds(
            0,
            0,
            view.getVideoLayer().getWidth(),
            view.getVideoLayer().getHeight()
        );

            view.getTopLeftPanel().setVisible(true);
            view.getVideoComponent().mediaPlayer()
                .media()
                .play(selectedFile.getAbsolutePath());
            
            SwingUtilities.invokeLater(() -> {
                loadSubtitlesIntoVlc();
            });
            
        }
    });
        view.getVideoComponent().mediaPlayer().events()
        .addMediaPlayerEventListener(new MediaPlayerEventAdapter() {

            @Override
            public void playing(
                    uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {

                SwingUtilities.invokeLater(() -> {
                    view.getPlayBtn().setIcon(view.getPauseIcon());
                });
            }

            @Override
            public void paused(
                    uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {

                SwingUtilities.invokeLater(() -> {
                    view.getPlayBtn().setIcon(view.getPlayIcon());
                });
            }

            @Override
            public void stopped(
                    uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer) {

                SwingUtilities.invokeLater(() -> {
                    view.getPlayBtn().setIcon(view.getPlayIcon());
                });
            }
        });
        
        view.getPlayBtn().addActionListener(e -> {

            boolean playing =
                    view.getVideoComponent()
                            .mediaPlayer()
                            .status()
                            .isPlaying();

            if (playing) {

                view.getVideoComponent()
                        .mediaPlayer()
                        .controls()
                        .pause();

            } else {

                view.getVideoComponent()
                        .mediaPlayer()
                        .controls()
                        .play();
            }
        });
        
        view.getStopBtn().addActionListener(e -> {
            view.getVideoComponent().mediaPlayer().controls().stop();
        });
        
        view.getSeekBar().addChangeListener(e -> {
            if (view.getSeekBar().getValueIsAdjusting()) {
                isSeeking = true;

                float position = view.getSeekBar().getValue() / 1000f;
                view.getVideoComponent().mediaPlayer().controls().setPosition(position);
            }else {
                isSeeking = false;
            }

        });
        
        view.getVideoComponent().mediaPlayer().events().addMediaPlayerEventListener( new MediaPlayerEventAdapter() {

                @Override
                public void positionChanged(
                        uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer,
                        float newPosition) {

                    if (!isSeeking) {

                        SwingUtilities.invokeLater(() -> {
                            view.getSeekBar().setValue((int) (newPosition * 1000));
                        });

                    }
                }
            }
        );
        
        view.getVideoComponent().mediaPlayer().events().addMediaPlayerEventListener(
            new uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter() {

                @Override
                public void timeChanged(
                        uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer,
                        long newTime) {

                    SwingUtilities.invokeLater(() -> {

                        long total = mediaPlayer.status().length();

                        view.getStartTime().setText(formatTime(newTime));
                        view.getEndTime().setText(formatTime(total));
                        syncSubtitle(newTime);
                        if (segmentEndTime > 0 && newTime >= segmentEndTime) {
                            mediaPlayer.controls().pause(); // or stop()
                            segmentEndTime = -1; // reset
                            previewMode = false;

                        }

                    });
                }
            }
        );
        
        view.getVideoComponent().addMouseWheelListener(e -> {

            int current = view.getVideoComponent().mediaPlayer().audio().volume();

            current += (e.getWheelRotation() < 0) ? 5 : -5;
            current = Math.max(0, Math.min(200, current));
            view.getVideoComponent().mediaPlayer().audio().setVolume(current);

            view.getVolumeLabel().setText("Vol " + current + "%");
            view.getVolumeLabel().setVisible(true);
            view.getVolumeLabel().setSize(view.getVolumeLabel().getPreferredSize());
            
            // hide after short delay
            new javax.swing.Timer(700, ev -> view.getVolumeLabel().setVisible(false)) {{
                setRepeats(false);
                start();
            }};
        });
        
        
        
        view.getTable().addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {

                if (SwingUtilities.isLeftMouseButton(e)) {

                    int row = view.getTable().rowAtPoint(e.getPoint());

                    if (row < 0) {
                        return;
                    }

                    // force single row only
                    view.getTable().clearSelection();
                    view.getTable().setRowSelectionInterval(row, row);

                    Subtitle s = view.getModel().getData().get(row);

                    previewMode = true;
                    currentSubtitleRow = row;
                    segmentEndTime = s.getEndTime();

                    view.getVideoComponent()
                            .mediaPlayer()
                            .controls()
                            .setTime(s.getStartTime());

                    view.getVideoComponent()
                            .mediaPlayer()
                            .controls()
                            .play();
                }
            }
        });
        
        view.getTable().setTransferHandler(new javax.swing.TransferHandler() {

            @Override
            public boolean canImport(TransferHandler.TransferSupport support) {
                return support.isDataFlavorSupported(
                        java.awt.datatransfer.DataFlavor.javaFileListFlavor
                );
            }

            @Override
            public boolean importData(TransferHandler.TransferSupport support) {
                try {
                    List<File> files =
                            (List<File>) support.getTransferable()
                                    .getTransferData(
                                            java.awt.datatransfer.DataFlavor.javaFileListFlavor
                                    );

                    for (File file : files) {
                        if (file.getName().toLowerCase().endsWith(".srt")) {
                            loadSubtitles(file);
                        }
                    }

                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        
        view.getGreenItem().addActionListener(e -> {
            view.applyGreenTheme();
        });

        view.getDraculaItem().addActionListener(e -> {
            view.applyDraculaTheme();
        });
        
        view.getTable().setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {

            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                java.awt.Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                Color pink = new Color(255, 182, 193);

                if (row == currentSubtitleRow) {
                    c.setBackground(pink);
                    c.setForeground(Color.BLACK);

                } else if (isSelected) {
                    c.setBackground(pink);
                    c.setForeground(Color.BLACK);

                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
        
        KeyStroke spaceKey = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);

        view.getVideoComponent().getInputMap(JComponent.WHEN_FOCUSED).put(spaceKey, "togglePlayPause");

        view.getVideoComponent().getActionMap().put("togglePlayPause", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

                if (view.getVideoComponent().mediaPlayer().status().isPlaying()) {
                    view.getVideoComponent().mediaPlayer().controls().pause();
                } else {
                    view.getVideoComponent().mediaPlayer().controls().play();
                }
            }
        });
        
        view.getVideoLayer().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                int w = view.getVideoLayer().getWidth();
                int h = view.getVideoLayer().getHeight();

                view.getVideoComponent().setBounds(0, 0, w, h);

                // RIGHT TOP CORNER POSITION
                int labelWidth = 80;
                int margin = 10;

                view.getVolumeLabel().setBounds(
                        w - labelWidth - margin,  // X (right side)
                        margin,                   // Y (top)
                        labelWidth,
                        20
                );

                view.getVideoLayer().revalidate();
                view.getVideoLayer().repaint();
            }
        });
    
        
        view.getTable().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
         .put(
             javax.swing.KeyStroke.getKeyStroke(
                     java.awt.event.KeyEvent.VK_F,
                     java.awt.event.InputEvent.CTRL_DOWN_MASK
             ),
             "jumpToCurrentSubtitle"
         );
        
        view.getTable().getActionMap().put(
            "jumpToCurrentSubtitle",
            new javax.swing.AbstractAction() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {

                    if (currentSubtitleRow >= 0
                            && currentSubtitleRow < view.getTable().getRowCount()) {

                        // Select highlighted row
                        view.getTable().setRowSelectionInterval(
                                currentSubtitleRow,
                                currentSubtitleRow
                        );

                        // Get row rectangle
                        java.awt.Rectangle rect =
                                view.getTable().getCellRect(
                                        currentSubtitleRow,
                                        0,
                                        true
                                );

                        // Get viewport
                        JViewport viewport =
                                (JViewport) view.getTable().getParent();

                        // Move row to TOP
                        viewport.setViewPosition(
                                new java.awt.Point(0, rect.y)
                        );

                        view.getTable().requestFocusInWindow();
                    }
                }
            }
        );
    }
    
   private void syncSubtitle(long currentTime) {
        if (userSeeking || previewMode) return;
        syncing = true;

        try {
            List<Subtitle> subtitles = view.getModel().getData();

            for (int i = 0; i < subtitles.size(); i++) {

                Subtitle s = subtitles.get(i);

                if (currentTime >= s.getStartTime()
                        && currentTime < s.getEndTime()) {

                    if (i == currentSubtitleRow) {
                        return;
                    }

                    currentSubtitleRow = i;
                    final int row = i;

                    SwingUtilities.invokeLater(() -> {

                        if (view.getTable().getRowCount() > row) {

                            view.getTable().setRowSelectionInterval(row, row);

                            if (autoFollowSubtitle) {
                                view.getTable().scrollRectToVisible(
                                        view.getTable().getCellRect(row, 0, true)
                                );
                            }

                            view.getTable().repaint();
                        }
                    });

                    break;
                }
            }

        } finally {
            syncing = false;
        }
    }
   
    private void loadSubtitlesIntoVlc() {

        try {

            File srtFile = SrtWriter.writeTemp(view.getModel().getData());

            view.getVideoComponent().mediaPlayer()
                    .subpictures()
                    .setSubTitleFile(srtFile.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void loadSubtitles(File file) {
        try {
            List<Subtitle> subtitles = SrtParser.parse(file);
            view.getModel().setData(subtitles);
            view.getEmptyPanel().setVisible(false);
            loadSubtitlesIntoVlc();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String formatTime(long millis) {

        long totalSeconds = millis / 1000;

        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format(
                "%02d:%02d:%02d",
                hours,
                minutes,
                seconds
        );
    }
}
