/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;

import com.islam.cliprojectmvc.service.SubtitleService;
import com.islam.cliprojectmvc.service.VideoService;
import com.islam.cliprojectmvc.view.FrameView;
import java.awt.Point;
import java.awt.Rectangle;

import java.awt.event.KeyEvent;

import javax.swing.*;


/**
 *
 * @author i3akk
 */
public class ShortcutController {

    public ShortcutController(
            FrameView view,
            VideoService videoService,
            SubtitleService subtitleService
    ) {

        initSpace(view, videoService);
        initCtrlF(view.getTable(),subtitleService);
    }

    private void initSpace(
            FrameView view,
            VideoService videoService
    ) {

        KeyStroke spaceKey =
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_SPACE,
                        0
                );

        view.getVideoComponent()
                .getInputMap(JComponent.WHEN_FOCUSED)
                .put(spaceKey, "togglePlayPause");

        view.getVideoComponent()
                .getActionMap()
                .put("togglePlayPause",
                        new AbstractAction() {

            @Override
            public void actionPerformed(
                    java.awt.event.ActionEvent e
            ) {

                videoService.togglePlayPause();
            }
        });
        
        
    }
    
    private void initCtrlF(
        JTable table,
        SubtitleService subtitleService
) {

    table.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(
                    KeyStroke.getKeyStroke(
                            KeyEvent.VK_F,
                            KeyEvent.CTRL_DOWN_MASK
                    ),
                    "jumpToCurrentSubtitle"
            );

    table.getActionMap().put(
            "jumpToCurrentSubtitle",
            new AbstractAction() {

                @Override
                public void actionPerformed(
                        java.awt.event.ActionEvent e
                ) {

                    int currentSubtitleRow =
                            subtitleService.getCurrentSubtitleRow();

                    if (currentSubtitleRow >= 0
                            && currentSubtitleRow < table.getRowCount()) {

                        // select row
                        table.setRowSelectionInterval(
                                currentSubtitleRow,
                                currentSubtitleRow
                        );

                        // scroll row to top
                        Rectangle rect =
                                table.getCellRect(
                                        currentSubtitleRow,
                                        0,
                                        true
                                );

                        JViewport viewport =
                                (JViewport) table.getParent();

                        viewport.setViewPosition(
                                new Point(
                                        0,
                                        rect.y
                                )
                        );

                        table.requestFocusInWindow();
                    }
                }
            }
    );
}
}
