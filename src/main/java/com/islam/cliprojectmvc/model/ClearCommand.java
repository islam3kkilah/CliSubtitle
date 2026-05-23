/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.islam.cliprojectmvc.model;

/**
 *
 * @author i3akk
 */
public class ClearCommand implements Commands{
    @Override
    public String execute(String[] args) {
        return "__CLEAR__";
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "clears the terminal screen";
    }
}
