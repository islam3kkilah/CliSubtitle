/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;

import com.islam.cliprojectmvc.model.Subtitle;
import com.islam.cliprojectmvc.service.SubtitleService;
import com.islam.cliprojectmvc.service.VideoService;
import com.islam.cliprojectmvc.view.FrameView;

import java.awt.*;

import javax.swing.JTable;

/**
 *
 * @author i3akk
 */
public class TableController {

    private final FrameView view;

    public TableController(
            FrameView view,
            VideoService videoService,
            SubtitleService subtitleService
    ) {

        this.view = view;

        initMouse(videoService);
        initRenderer();
    }

    private void initMouse(VideoService videoService) {

        view.getTable().addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(
                    java.awt.event.MouseEvent e
            ) {

                int row =
                        view.getTable().rowAtPoint(e.getPoint());

                if (row < 0) return;

                view.getTable()
                        .setRowSelectionInterval(row, row);

                Subtitle s =
                        view.getModel()
                                .getData()
                                .get(row);

                videoService.playSegment(
                        s.getStartTime(),
                        s.getEndTime()
                );
            }
        });
    }

    private void initRenderer() {

        view.getTable().setDefaultRenderer(
                Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {

                Component c =
                        super.getTableCellRendererComponent(
                                table,
                                value,
                                isSelected,
                                hasFocus,
                                row,
                                column
                        );

                if (isSelected) {

                    c.setBackground(Color.PINK);
                    c.setForeground(Color.BLACK);

                } else {

                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }

                return c;
            }
        });
    }
}