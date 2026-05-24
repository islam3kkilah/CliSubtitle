/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;
import com.islam.cliprojectmvc.model.Subtitle;
import com.islam.cliprojectmvc.util.SrtParser;
import com.islam.cliprojectmvc.util.SrtWriter;
import com.islam.cliprojectmvc.view.FrameView;
import java.io.File;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author i3akk
 */
public class MainController {
    private final FrameView view;

    public MainController(FrameView view) {
        this.view = view;
        
        
    }
    
    private void syncSubtitle(long currentTime) {

        if (userSeeking) return;

        syncing = true;

        try {
            List<Subtitle> subtitles = model.getData();

            for (int i = 0; i < subtitles.size(); i++) {

                Subtitle s = subtitles.get(i);

                if (currentTime >= s.getStartTime()
                    && currentTime <= s.getEndTime()) {
                if (i == currentSubtitleRow) {
                    return;
                }
                currentSubtitleRow = i;
                final int row = i;

                SwingUtilities.invokeLater(() -> {
                    if (table.getRowCount() > row) {

                        table.setRowSelectionInterval(row, row);

                        if (autoFollowSubtitle) {
                            table.scrollRectToVisible(table.getCellRect(row, 0, true));
                        }

                        table.repaint();
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

            File srtFile = SrtWriter.writeTemp(model.getData());

            videoComponent.mediaPlayer()
                    .subpictures()
                    .setSubTitleFile(srtFile.getAbsolutePath());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void loadSubtitles(File file) {
        try {
            List<Subtitle> subtitles = SrtParser.parse(file);
            view.getModel.setData(subtitles);
            view.getEmptyPanel.setVisible(false);
            loadSubtitlesIntoVlc();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
