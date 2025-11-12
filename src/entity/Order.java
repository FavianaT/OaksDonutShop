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
    private int quantity;
    private String dateTime;
    private int donutID;
    
    public Order(int ID, int quantity, String dateTime, int donutID)
    {
        this.ID = ID;
        this.quantity = quantity;
        this.dateTime = dateTime;
        this.donutID = donutID;
    }

    public int getID() {
        return ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDateTime() {
        return dateTime;
    }
    
    public int getDonutID() {
        return donutID;
    }

    @Override
    public String toString() {
        return "Order{" + "ID=" + ID + ", quantity=" + quantity + ", dateTime=" + dateTime + ", donutID=" + donutID + '}';
    }
}
