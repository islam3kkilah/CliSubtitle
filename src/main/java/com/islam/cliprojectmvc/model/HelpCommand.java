/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.model;
import java.util.Set;
import java.util.TreeSet;
/**
 *
 * @author i3akk
 */
public class HelpCommand implements Commands{
    private ConsoleModel model;

    @Override
    public String getName() {
        return "help";
    }

    public HelpCommand(ConsoleModel model) {
        this.model = model;
    }


    public String getDescription() {
        String description= "Provides help information for terminal command";
        return description;
    }


    @Override
    public String execute(String[] args) {
        if (args.length > 0) {
            Commands cmd = model.getCommand(args[0].toLowerCase());
            if (cmd == null) return "Unknown command: " + args[0];
            return cmd.getName() + " - " + cmd.getDescription();
        }

        // Otherwise: list all commands nicely
        Set<Commands> commands = new TreeSet<>((c1, c2) -> c1.getName().compareTo(c2.getName()));

        commands.addAll(model.getAllCommands()); // method to return all command objects

        StringBuilder sb = new StringBuilder();
        sb.append("For more information on a specific command, type 'help <command>'.\n");
        for (Commands cmd : commands) {
            // Pad the name to 15 characters for alignment
            sb.append(String.format("%-15s %s\n", cmd.getName(), cmd.getDescription()));
        }
        return sb.toString().trim();

    }
}

