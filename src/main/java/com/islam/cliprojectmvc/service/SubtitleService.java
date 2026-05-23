/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.service;
import com.islam.cliprojectmvc.model.Subtitle;
import com.islam.cliprojectmvc.util.SrtParser;
import com.islam.cliprojectmvc.util.SrtWriter;
import com.islam.cliprojectmvc.view.FrameView;

import java.io.File;
import java.util.List;

/**
 *
 * @author i3akk
 */
public class SubtitleService {

    private final FrameView view;

    private int currentSubtitleRow = -1;

    public SubtitleService(FrameView view) {
        this.view = view;
    }

    public void loadSubtitles(File file) {

        try {

            List<Subtitle> subtitles =
                    SrtParser.parse(file);

            view.getModel().setData(subtitles);

            view.getEmptyPanel().setVisible(false);

            loadSubtitlesIntoVlc();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    public void syncSubtitle(long currentTime) {

        currentSubtitleRow = -1;

        List<Subtitle> subtitles =
                view.getModel().getData();

        for (int i = 0; i < subtitles.size(); i++) {

            Subtitle s = subtitles.get(i);

            if (currentTime >= s.getStartTime()
                    && currentTime <= s.getEndTime()) {

                currentSubtitleRow = i;
                break;
            }
        }
    }

    public void loadSubtitlesIntoVlc() {

        try {

            File srtFile =
                    SrtWriter.writeTemp(
                            view.getModel().getData()
                    );

            view.getVideoComponent()
                    .mediaPlayer()
                    .subpictures()
                    .setSubTitleFile(
                            srtFile.getAbsolutePath()
                    );

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
    
    public int getCurrentSubtitleRow() {
        return currentSubtitleRow;
    }
}
