/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;

import com.islam.cliprojectmvc.view.FrameView;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 *
 * @author i3akk
 */
public class WindowController {

    private final FrameView view;

    public WindowController(FrameView view) {

        this.view = view;

        initResize();
    }

    private void initResize() {

        view.getVideoLayer()
                .addComponentListener(
                        new ComponentAdapter() {

            @Override
            public void componentResized(
                    ComponentEvent e
            ) {

                int w = view.getVideoLayer().getWidth();
                int h = view.getVideoLayer().getHeight();

                // VIDEO
                view.getVideoComponent()
                        .setBounds(0, 0, w, h);

                // VOLUME LABEL
                int labelWidth = 80;
                int margin = 10;

                view.getVolumeLabel().setBounds(
                        w - labelWidth - margin,
                        margin,
                        labelWidth,
                        20
                );

                view.getVideoLayer().revalidate();
                view.getVideoLayer().repaint();
            }
        });
    }
}