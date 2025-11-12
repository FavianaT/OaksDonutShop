CREATE TABLE OD_Order (
    Order_ID int NOT NULL PRIMARY KEY,
    Order_Quantity int NOT NULL,
    Order_Date_TIME TIMESTAMP NOT NULL,
    Item_Name VARCHAR(20) NOT NULL,
    Donut_ID int NOT NULL,
    FOREIGN KEY (Donut_ID) REFERENCES OD_Donut(Donut_ID)
);
