package program;

import java.awt.*;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import entity.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Random;

/**
 * @author FavianaT
 */

public class Main extends javax.swing.JFrame {
    // Color has been Added to Panels
    private ArrayList<entity.Donut> menu = new ArrayList<entity.Donut>();
    private static DonutDAO donutDAO = new DonutDAO();
    private static OrderDAO orderDAO = new OrderDAO();

    // Helps calculate the program
    private int orderIndex = 0;
    private int donutIndex = 0;

    // Keeps price consistent
    JLabel subtotalAmount;
    JLabel taxAmount;
    JLabel totalAmount;

    private JList<String> donutNames;
    private JScrollPane menuScrollPane;
    private JLabel dynamicField;
    private DefaultListModel<String> listModel;
    private ArrayList<String> recommendedList;
    Random random = new Random();


    public Main(){
        menu.add(new Donut(0,"Glazed Donut", 1.50));
        menu.add(new Donut(1,"Strawberry Frosted Donut", 2.00));
        menu.add(new Donut(2,"Chocolate Frosted Donut", 2.00));
        menu.add(new Donut(3,"Jelly Donut", 3.00));
        menu.add(new Donut(4,"Boston Cream Donut", 3.50));
        menu.add(new Donut(5,"Old Fashioned Donut", 3.00));
        initComponents();
    }

    private void initComponents(){
        // Creates the GUI screen
        setTitle("Oak Donuts Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        // Title Panel (Only has the title)
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(202, 230, 215));
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(titlePanel, BorderLayout.NORTH);

        JLabel title = new JLabel("Oak Donuts Shop");
        title.setFont(new Font("Calibri", Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        titlePanel.add(title);

        // West Panel (Simply contains the dynamic texts that gets chosen randomly with each program run)
        JPanel westPanel = new JPanel();
        westPanel.setBackground(new Color(182, 227, 207));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        westPanel.setPreferredSize(new Dimension(150,600));
        add(westPanel, BorderLayout.WEST);

        JLabel dynamincLabel = new JLabel("Recommended Today:");
        dynamincLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        dynamincLabel.setForeground(Color.BLACK);
        westPanel.add(dynamincLabel);

        dynamicField = new JLabel("");
        
        // Recommended Donuts are Random Every Program Run
        
        recommendedList = new ArrayList<>();
        recommendedList.add("Glazed Donut");
        recommendedList.add("Strawberry Frosted Donut");
        recommendedList.add("Chocolate Frosted Donut");
        recommendedList.add("Jelly Donut");
        recommendedList.add("Boston Cream Donut");
        recommendedList.add("Old Fashioned Donut");
        dynamicField.setText(recommendedList.get(random.nextInt(recommendedList.size())));
        dynamicField.setFont(new Font("Calibri", Font.BOLD, 12));

        westPanel.add(dynamicField);


        westPanel.add(Box.createVerticalStrut(450));

        westPanel.setVisible(true);

        // Center Panel (Contains the menu, the quantity, and the add to order button)

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(182, 227, 207));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(centerPanel, BorderLayout.CENTER);

        JLabel menulabel= new JLabel("Menu");
        menulabel.setFont(new Font("Calibri", Font.BOLD, 16));
        menulabel.setForeground(Color.BLACK);
        centerPanel.add(menulabel);

        String[] menuStringArr = new String[menu.size()];
        for(int i = 0; i < menu.size(); i++){
            menuStringArr[i] = menu.get(i).toString();
        }
        JList<String> menuDonutNames = new JList<String>(menuStringArr);
        JScrollPane menuScrollPane = new JScrollPane(menuDonutNames);
        centerPanel.add(menuScrollPane);

        JPanel lowerCenterPanel = new JPanel();
        lowerCenterPanel.setBackground(new Color(182, 227, 207));
        lowerCenterPanel.setLayout(new BoxLayout(lowerCenterPanel, BoxLayout.X_AXIS));
        centerPanel.add(lowerCenterPanel);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        quantitySpinner.setMaximumSize(new Dimension(80,80));
        lowerCenterPanel.add(quantitySpinner);

        // Add to order button takes the donut fron the donut menu before adding it to the order
        // while also looking at the quantity and updating price
        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedDonut = menuDonutNames.getSelectedValue();
                int selectedDonutIndex = menuDonutNames.getSelectedIndex();
                if (selectedDonut != null) {
                    System.out.println("Selected donut: " + selectedDonut);

                    System.out.println("Selected donut index: " + selectedDonutIndex);
                    Donut i = menu.get(selectedDonutIndex);
                    addDonut(i.getID(), i.getName(), i.getPrice());
                    for(int j = 0; j<(int)quantitySpinner.getValue(); j++){
                        if(getOrder(orderIndex).getID()==-1){
                            addOrder(orderIndex, i.getPrice(), i.getName());
                            System.out.println("NEW ORDER " + getOrder(orderIndex).getID());
                        }else{
                            updateOrder(orderIndex,getOrder(orderIndex).getPrice()+i.getPrice(),i.getName());
                            System.out.println("UPDATE ORDER " + getOrder(orderIndex).getID());
                        }
                    }
                    updateOrderTable(model, i, (int)quantitySpinner.getValue());
                    updatePrices(subtotalAmount,taxAmount,totalAmount);
                    quantitySpinner.setValue(1);
                    menuDonutNames.clearSelection();
                } else {
                    System.out.println("No donut selected.");
                }
            }
        });
        lowerCenterPanel.add(addButton);



        // East Panel (Contains the most of each panel. The order table, the subcost, tax, total cost, the clear, and checkout)
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(new Color(182, 227, 207));
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(eastPanel, BorderLayout.EAST);


        JTable orderTable = new JTable();
        orderTable.setBackground(new Color(220, 242, 232));

        int o = 0;
        while(getOrder(o).getID()!=-1){
            o++;
        }
        orderIndex=o;

        int i = 0;
        while(getDonut(i).getID()!=-1) {
            Donut donut = getDonut(i);
            boolean found = false;
            for (int j = 0; j < model.getRowCount(); j++) {
                Donut previouslyAddedDonut = getDonut(j);
                System.out.print(previouslyAddedDonut.getName() + " == " + donut.getName());
                if (previouslyAddedDonut.getName().equals(donut.getName()) ) {
                    model.setValueAt((int) model.getValueAt(j, 2) + 1, j, 2);
                    model.setValueAt(Double.parseDouble((model.getValueAt(j, 4).toString().replace("$", ""))) + donut.getPrice(), j, 4);
                    found = true;
                    break;
                }
            }
            DecimalFormat df = new DecimalFormat("#.00");
            if (found == false)
                model.addRow(new Object[]{donut.getName(), "$" + df.format(donut.getPrice()), "$" + df.format(donut.getPrice())});
            i++;
        }
        donutIndex = i;
        orderTable.setModel(model);
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(350);

        
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 300));
        eastPanel.add(orderScroll);

        JPanel lowerEastPanel = new JPanel();
        lowerEastPanel.setBackground(new Color(182, 227, 207));
        lowerEastPanel.setLayout(new BorderLayout());

        JPanel lowerEastWestPanel = new JPanel();
        lowerEastWestPanel.setBackground(new Color(182, 227, 207));
        lowerEastWestPanel.setLayout(new BoxLayout(lowerEastWestPanel, BoxLayout.Y_AXIS));
        JLabel subtotalLabel = new JLabel("Subtotal:");
        lowerEastWestPanel.add(subtotalLabel);
        JLabel taxLabel = new JLabel("Tax (6%):");
        lowerEastWestPanel.add(taxLabel);
        JLabel totalPanel = new JLabel("Total:");
        lowerEastWestPanel.add(totalPanel);
        
        lowerEastPanel.add(lowerEastWestPanel, BorderLayout.WEST);

        JPanel lowerEastEastPanel = new JPanel();
        lowerEastEastPanel.setBackground(new Color(182, 227, 207));
        lowerEastEastPanel.setLayout(new BoxLayout(lowerEastEastPanel, BoxLayout.Y_AXIS));

        subtotalAmount = new JLabel("$0.00");
        taxAmount = new JLabel("$0.00");
        totalAmount = new JLabel("$0.00");
        if(getOrder(orderIndex).getID()!=-1) updatePrices(subtotalAmount, taxAmount, totalAmount);

        lowerEastEastPanel.add(subtotalAmount);
        lowerEastEastPanel.add(taxAmount);
        lowerEastEastPanel.add(totalAmount);

        // Button works to clear the order table and reset cost back to $0
        JButton clearButton = new JButton("Delete");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int i = donutIndex - 1;
                Donut donut;
                while(getDonut(i).getID() != -1) {
                    donut = getDonut(i);
                    deleteDonut(donut.getID(), donut.getName(), donut.getPrice());
                    i--;
                }
                Order order = getOrder(orderIndex);
                deleteOrder(order.getID(), order.getPrice(), order.getDonutName());
                donutIndex=0;
                model.setRowCount(0);

                subtotalAmount.setText("$0.00");
                taxAmount.setText("$0.00");
                totalAmount.setText("$0.00");
            }
        });
        lowerEastWestPanel.add(clearButton);
        
        // This works as a checkout, returning two different types of messages depending on if order is empty or not
        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (model.getRowCount() > 0){
                    int i = donutIndex - 1;
                    Donut donut;
                    while (getDonut(i).getID() >= 0) {
                        donut = getDonut(i);
                        deleteDonut(donut.getID(), donut.getName(), donut.getPrice());
                        i--;
                    }

                    model.setRowCount(0);

                    DecimalFormat df = new DecimalFormat("#.00");
                    JOptionPane checkoutPop = new JOptionPane();
                    checkoutPop.showMessageDialog(null, "Thank you for ordering!\n Your total will be $" + df.format((getOrder(orderIndex).getPrice())*0.06 + getOrder(orderIndex).getPrice()));
                    donutIndex = 0;
                    orderIndex--;

                    subtotalAmount.setText("$0.00");
                    taxAmount.setText("$0.00");
                    totalAmount.setText("$0.00");
                } else if (model.getRowCount() == 0) {
                    JOptionPane emptyPop = new JOptionPane();
                    JOptionPane.showMessageDialog(null, "Your order is empty");
                }

            }
        });
        lowerEastEastPanel.add(checkoutButton);

        lowerEastPanel.add(lowerEastEastPanel, BorderLayout.EAST);
        eastPanel.add(lowerEastPanel);
        setVisible(true);

    }
    
    // Function that helps update the order table when a donut is added to order
    private void updateOrderTable(DefaultTableModel model, Donut donut, int quanity){
        model.addRow(new Object[]{donut.getName(), quanity,donut.getPrice(),donut.getPrice()*quanity});
    }

    // Function that updates the prices of the subtotal, tax, and total
    private void updatePrices(JLabel subtotalAmount, JLabel taxAmount, JLabel totalAmount){
        DecimalFormat df = new DecimalFormat("#.00");
        subtotalAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()));
        taxAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()*0.06));
        totalAmount.setText("$"+(df.format((getOrder(orderIndex).getPrice()+getOrder(orderIndex).getPrice()*0.06))));
    }

    // These are the donut crud functions

    // This function takes inputs before adding the donut to the donut database
    private static void addDonut(int id, String name, double price) {
        Donut donut;
        donut = new Donut(id, name, price);
        donutDAO.insert(donut);
    }

    // This function takes inputs before updating the donut in the donut database
    private static void updateDonut(int id, String name, double price) {
        Donut donut;
        donut = new Donut(id, name, price);
        donutDAO.update(donut);
    }

    // This function takes inputs before deleting the donut from the donut database
    private static void deleteDonut(int id, String name, double price) {
        Donut donut;
        donut = new Donut(id, name, price);
        donutDAO.delete(donut);
    }

    // This function takes an input of the id before looking through the donut database and returning the donut
    static Donut getDonut(int id) {
        Optional<Donut> donut = donutDAO.get(id);
        return donut.orElseGet(() -> new Donut(-1, "Non-exist", -1));
    }


    // These are the order crud functions

    // This function takes inputs before inserting the new order into order database
    private static void addOrder(int ID, double price, String donutName) {
        Order order;
        order = new Order(ID, price, donutName);
        orderDAO.insert(order);
    }

    // This function takes inputs before updating the existing order in the order database
    private static void updateOrder(int ID, double price, String donutName) {
        Order order;
        order = new Order(ID, price, donutName);
        orderDAO.update(order);
    }

    // This function takes inputs before deleting the order from the order database
    private static void deleteOrder(int ID, double price, String donutName) {
        Order order;
        order = new Order(ID, price, donutName);
        orderDAO.delete(order);
    }

    // This function takes an input of the id before looking through the order database and returning the order
    static Order getOrder(int id) {
        Optional<Order> order = orderDAO.get(id);
        if (order.isPresent()) {
            return order.get();
        } else {
            System.out.println("No order found with ID: " + id);
            return order.orElseGet(() -> new Order(-1, -1, "Non-exist"));
        }
    }

    public static void main(String args[]) {
        donutDAO = new DonutDAO();
        orderDAO = new OrderDAO();

        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    // This operates as the default model that the order table takes
    javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(
        new Object[][]{
        },
        new String[]{
            "Donut", "Quantity", "Price", "Total"
        }
    ) {
        boolean[] canEdit = new boolean[]{
                false, false, false, false
        };
    };

}
