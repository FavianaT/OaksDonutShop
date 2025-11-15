/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;
/**
 *
 * @author Gokhan
 */
public class Order
{
    private int ID;
    private double price;
    private String donutName;

    public Order(int ID, double price, String donutName)
    {
        this.ID = ID;
        this.price = price;
        this.donutName = donutName;
    }

    public int getID() {
        return ID;
    }

    public double getPrice() {
        return price;
    }

    public String getDonutName() {
        return donutName;
    }

    @Override
    public String toString() {
        return "Order{" + "ID=" + ID + ", price=" + price + ", donutName=" + donutName + '}';
    }
}
