/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.model;
import java.util.List;
/**
 *
 * @author i3akk
 */
public class HistoryCommand implements Commands{
    private ConsoleModel model;

    public HistoryCommand(ConsoleModel model) {
        this.model = model;
    }

    @Override
    public String getName() {
        return "history"; // uppercase name for formatting
    }

    @Override
    public String getDescription() {

        return "shows a list of previously entered commands";
    }

    @Override
    public String execute(String[] args) {
        List<String> history = model.getHistory();
        if (history.isEmpty()) return "No commands yet.";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < history.size(); i++) {
            sb.append(i + 1).append(": ").append(history.get(i)).append("\n");
        }
        return sb.toString().trim();
    }
}
