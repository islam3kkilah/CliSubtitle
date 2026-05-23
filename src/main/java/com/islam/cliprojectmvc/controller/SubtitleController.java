/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;

import com.islam.cliprojectmvc.service.SubtitleService;
import com.islam.cliprojectmvc.view.FrameView;

import java.io.File;
import java.util.List;

import javax.swing.TransferHandler;
/**
 *
 * @author i3akk
 */
public class SubtitleController {

    private final FrameView view;
    private final SubtitleService subtitleService;

    public SubtitleController(
            FrameView view,
            SubtitleService subtitleService
    ) {

        this.view = view;
        this.subtitleService = subtitleService;

        initDragDrop();
    }

    private void initDragDrop() {

        view.getTable().setTransferHandler(
                new TransferHandler() {

            @Override
            public boolean canImport(
                    TransferSupport support
            ) {

                return support.isDataFlavorSupported(
                        java.awt.datatransfer
                                .DataFlavor
                                .javaFileListFlavor
                );
            }

            @Override
            public boolean importData(
                    TransferSupport support
            ) {

                try {

                    List<File> files =
                            (List<File>) support
                                    .getTransferable()
                                    .getTransferData(
                                            java.awt.datatransfer
                                                    .DataFlavor
                                                    .javaFileListFlavor
                                    );

                    for (File file : files) {

                        if (file.getName()
                                .toLowerCase()
                                .endsWith(".srt")) {

                            subtitleService.loadSubtitles(file);
                        }
                    }

                    return true;

                } catch (Exception ex) {

                    ex.printStackTrace();
                }

                return false;
            }
        });
    }
}