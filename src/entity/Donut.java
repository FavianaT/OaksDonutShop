/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.text.DecimalFormat;

/**
 *
 * @author FavianaT
 */
public class Donut
{
    private int ID;
    private String name;
    private double price;
    
    public Donut(int ID, String name, double price)
    {
        this.ID = ID;
        this.name = name;
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return name + " — $"+df.format(price);
    }
}
