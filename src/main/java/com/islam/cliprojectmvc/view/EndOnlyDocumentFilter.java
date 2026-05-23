/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.view;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
/**
 *
 * @author i3akk
 */
public class EndOnlyDocumentFilter extends DocumentFilter {

    private JTextPane textPane;

    public EndOnlyDocumentFilter(JTextPane textPane) {
        this.textPane = textPane;
    }

    private void moveCaretToEnd() {
        int end = textPane.getDocument().getLength();
        if (textPane.getCaretPosition() != end) {
            textPane.setCaretPosition(end);
        }
    }

    @Override
    public void insertString(FilterBypass fb, int offset,
                             String string, AttributeSet attr)
            throws BadLocationException {

        moveCaretToEnd();
        fb.insertString(fb.getDocument().getLength(), string, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length,
                        String text, AttributeSet attrs)
            throws BadLocationException {

        moveCaretToEnd();
        fb.replace(fb.getDocument().getLength(), 0, text, attrs);
    }
}
