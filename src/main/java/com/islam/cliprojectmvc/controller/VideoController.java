/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;

import com.islam.cliprojectmvc.service.SubtitleService;
import com.islam.cliprojectmvc.service.VideoService;
import com.islam.cliprojectmvc.view.FrameView;

import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

/**
 *
 * @author i3akk
 */
public class VideoController {

    private final FrameView view;
    private final VideoService videoService;
    private final SubtitleService subtitleService;

    private boolean isSeeking = false;

    public VideoController(
            FrameView view,
            VideoService videoService,
            SubtitleService subtitleService
    ) {

        this.view = view;
        this.videoService = videoService;
        this.subtitleService = subtitleService;

        init();
    }

    private void init() {

        initMenu();
        initButtons();
        initSeekBar();
        initMediaEvents();
        initVolumeWheel();
    }

    private void initMenu() {

        view.getOpenVideoItem().addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();

            chooser.setAcceptAllFileFilterUsed(false);

            chooser.setFileFilter(
                    new FileNameExtensionFilter(
                            "Video Files",
                            "mp4", "mkv", "avi", "mov"
                    )
            );

            int result = chooser.showOpenDialog(view);

            if (result == JFileChooser.APPROVE_OPTION) {

                File selectedFile = chooser.getSelectedFile();

                videoService.openVideo(selectedFile);

                SwingUtilities.invokeLater(() -> {
                    subtitleService.loadSubtitlesIntoVlc();
                });
            }
        });

        view.getExitItem().addActionListener(e -> {
            System.exit(0);
        });
    }

    private void initButtons() {

        view.getPlayBtn().addActionListener(e -> {
            videoService.play();
        });

        view.getPauseBtn().addActionListener(e -> {
            videoService.pause();
        });

        view.getStopBtn().addActionListener(e -> {
            videoService.stop();
        });
    }

    private void initSeekBar() {

        view.getSeekBar().addChangeListener(e -> {

            if (view.getSeekBar().getValueIsAdjusting()) {

                isSeeking = true;

                float position =
                        view.getSeekBar().getValue() / 1000f;

                videoService.setPosition(position);

            } else {

                isSeeking = false;
            }
        });
    }

    private void initMediaEvents() {

        view.getVideoComponent()
                .mediaPlayer()
                .events()
                .addMediaPlayerEventListener(
                        new MediaPlayerEventAdapter() {

            @Override
            public void positionChanged(
                    uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer,
                    float newPosition
            ) {

                if (!isSeeking) {

                    SwingUtilities.invokeLater(() -> {

                        view.getSeekBar().setValue(
                                (int) (newPosition * 1000)
                        );
                    });
                }
            }

            @Override
            public void timeChanged(
                    uk.co.caprica.vlcj.player.base.MediaPlayer mediaPlayer,
                    long newTime
            ) {

                SwingUtilities.invokeLater(() -> {

                    long total = mediaPlayer.status().length();

                    view.getStartTime()
                            .setText(videoService.formatTime(newTime));

                    view.getEndTime()
                            .setText(videoService.formatTime(total));

                    subtitleService.syncSubtitle(newTime);

                    // IMPORTANT
                    videoService.checkSegmentEnd(newTime);
                });
            }
                    });
                }

    private void initVolumeWheel() {

        view.getVideoComponent().addMouseWheelListener(e -> {

            int current =
                    view.getVideoComponent()
                            .mediaPlayer()
                            .audio()
                            .volume();

            current += (e.getWheelRotation() < 0)
                    ? 5
                    : -5;

            current = Math.max(0, Math.min(200, current));

            view.getVideoComponent()
                    .mediaPlayer()
                    .audio()
                    .setVolume(current);

            view.getVolumeLabel()
                    .setText("Vol " + current + "%");

            view.getVolumeLabel().setVisible(true);

            new Timer(700, ev -> {
                view.getVolumeLabel().setVisible(false);
            }) {{
                setRepeats(false);
                start();
            }};
        });
    }
    
}