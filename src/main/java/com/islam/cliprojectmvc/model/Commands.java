/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.islam.cliprojectmvc.model;

/**
 *
 * @author i3akk
 */
public interface Commands {
    String getDescription(); // command description
    String execute(String[] args);
    String getName();
}
