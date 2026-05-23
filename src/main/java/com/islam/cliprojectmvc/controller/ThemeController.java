/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;

import com.islam.cliprojectmvc.view.FrameView;

/**
 *
 * @author i3akk
 */
public class ThemeController {

    public ThemeController(FrameView view) {

        view.getGreenItem().addActionListener(e -> {
            view.applyGreenTheme();
        });

        view.getDraculaItem().addActionListener(e -> {
            view.applyDraculaTheme();
        });
    }
}
