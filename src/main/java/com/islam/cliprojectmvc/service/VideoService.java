/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.service;

import com.islam.cliprojectmvc.view.FrameView;
import java.io.File;

/**
 *
 * @author i3akk
 */
public class VideoService {

    private final FrameView view;

    private long segmentEndTime = -1;

    public VideoService(FrameView view) {
        this.view = view;
    }

    public void openVideo(File file) {

        view.getTopLeftPanel().setVisible(true);

        view.getVideoComponent()
                .mediaPlayer()
                .media()
                .play(file.getAbsolutePath());
    }

    public void play() {
        view.getVideoComponent()
                .mediaPlayer()
                .controls()
                .play();
    }

    public void pause() {
        view.getVideoComponent()
                .mediaPlayer()
                .controls()
                .pause();
    }

    public void stop() {
        view.getVideoComponent()
                .mediaPlayer()
                .controls()
                .stop();
    }

    public void setPosition(float position) {

        view.getVideoComponent()
                .mediaPlayer()
                .controls()
                .setPosition(position);
    }

    public void togglePlayPause() {

        if (view.getVideoComponent()
                .mediaPlayer()
                .status()
                .isPlaying()) {

            pause();

        } else {

            play();
        }
    }

    public void playSegment(
            long start,
            long end
    ) {

        segmentEndTime = end;

        view.getVideoComponent()
                .mediaPlayer()
                .controls()
                .setTime(start);

        play();
    }

    public String formatTime(long millis) {

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
    
    public void checkSegmentEnd(long currentTime) {

        if (segmentEndTime > 0
                && currentTime >= segmentEndTime) {

            pause();

            segmentEndTime = -1;
        }
    }
}
