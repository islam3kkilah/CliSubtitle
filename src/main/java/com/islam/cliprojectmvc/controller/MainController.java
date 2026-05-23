/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;

import com.islam.cliprojectmvc.service.SubtitleService;
import com.islam.cliprojectmvc.service.VideoService;
import com.islam.cliprojectmvc.view.FrameView;

/**
 *
 * @author i3akk
 */
public class MainController {

    private final FrameView view;

    private final VideoService videoService;
    private final SubtitleService subtitleService;

    public MainController(FrameView view) {

        this.view = view;

        // SERVICES
        videoService = new VideoService(view);
        subtitleService = new SubtitleService(view);

        // CONTROLLERS
        VideoController videoController =
                new VideoController(view, videoService, subtitleService);

        SubtitleController subtitleController =
                new SubtitleController(view, subtitleService);

        TableController tableController =
                new TableController(
                        view,
                        videoService,
                        subtitleService
                );

        ShortcutController shortcutController =
                new ShortcutController(
                        view,
                        videoService,
                        subtitleService
                );

        ThemeController themeController =
                new ThemeController(view);

        WindowController windowController =
                new WindowController(view);
    }
}
