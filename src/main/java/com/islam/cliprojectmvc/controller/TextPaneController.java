/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.controller;
import com.islam.cliprojectmvc.model.ConsoleModel;
import com.islam.cliprojectmvc.view.FrameView;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.event.ActionEvent;
/**
 *
 * @author i3akk
 */
public class TextPaneController {
    private FrameView view;
    private ConsoleModel model;


    public TextPaneController(FrameView view, ConsoleModel model) {
        this.model = model;
        this.view = view;

        view.getTextPane().getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "enterPressed");
        view.getTextPane().getActionMap().put("enterPressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JTextPane textPane = view.getTextPane();


                if (!view.isCaretOnLastLine(textPane)) {
                    textPane.setCaretPosition(textPane.getDocument().getLength());
                    return;
                }

                try {
                    StyledDocument doc = textPane.getStyledDocument();
                    int lineStart = javax.swing.text.Utilities.getRowStart(textPane, doc.getLength());
                    String lineText = doc.getText(lineStart, doc.getLength() - lineStart);

                    // Remove prompt "> " from command
                    String command = lineText.startsWith("> ") ? lineText.substring(2).trim() : lineText.trim();

                    if (!command.isEmpty()) {
                        String result = model.executeCommand(command);

                        if ("__CLEAR__".equals(result)) {
                            view.clear();
                            view.appendPrompt();  // Prompt appears on first line
                            return;               // Skip the extra newline
                        } else {
                            view.append("\n" + result);
                        }


                    }
                    
                    // Add newline and new prompt
                    doc.insertString(doc.getLength(), "\n", null);
                    view.appendPrompt();

                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        view.getTextPane().getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "backspacePressed");
        view.getTextPane().getActionMap().put("backspacePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextPane textPane = view.getTextPane();

                if (!view.isCaretOnLastLine(textPane)) {
                    textPane.setCaretPosition(textPane.getDocument().getLength());
                    return;
                }

                try {
                    int caretPos = textPane.getCaretPosition();
                    int lineStart = javax.swing.text.Utilities.getRowStart(textPane, caretPos);

                    // Prevent deleting prompt (first 2 chars)
                    if (caretPos <= lineStart + 2) return;

                    textPane.getDocument().remove(caretPos - 1, 1);

                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
    
}
