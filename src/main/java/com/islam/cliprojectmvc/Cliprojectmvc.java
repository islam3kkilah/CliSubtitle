/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.islam.cliprojectmvc;

import com.islam.cliprojectmvc.controller.MainController;
import com.islam.cliprojectmvc.controller.TextPaneController;
import com.islam.cliprojectmvc.model.ConsoleModel;
import com.islam.cliprojectmvc.view.FrameView;
import javax.swing.SwingUtilities;

/**
 *
 * @author i3akk
 */
public class Cliprojectmvc {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FrameView view = new FrameView();
            new MainController(view);
            view.appendPrompt();
            view.setVisible(true);
        });
    }
}
