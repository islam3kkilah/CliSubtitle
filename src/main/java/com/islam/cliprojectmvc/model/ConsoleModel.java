/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.model;
import java.util.*;
/**
 *
 * @author i3akk
 */
public class ConsoleModel {
    private Map<String, Commands> commands = new HashMap<>();
    private List<String> history = new ArrayList<>();

    public ConsoleModel() {
        register("help", new HelpCommand(this));
        register("history", new HistoryCommand(this));
        register("clear", new ClearCommand());
    }

    public void register(String name, Commands command) {
        commands.put(name, command);
    }

    public Set<String> getAvailableCommands() {
        return commands.keySet();
    }

    public Set<Commands> getAllCommands() {
        return new HashSet<>(commands.values());
    }

    public Commands getCommand(String name) {
        return commands.get(name);
    }

    public void addToHistory(String command) {
        history.add(command);
    }

    public List<String> getHistory() {
        return history;
    }

    public String executeCommand(String input) {
        if (input.trim().isEmpty()) return "";

        addToHistory(input); // store in history

        String[] parts = input.split("\\s+");
        String name = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        Commands cmd = commands.get(name);
        if (cmd == null) return "Unknown command: " + name;

        return cmd.execute(args);
    }
}
