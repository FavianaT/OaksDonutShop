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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;

/**
 * @author Gokhan
 */

public class Main extends javax.swing.JFrame {
    private ArrayList<entity.Donut> menu = new ArrayList<entity.Donut>();
    private static DonutDAO donutDAO = new DonutDAO();
    private static OrderDAO orderDAO = new OrderDAO();
    private int orderIndex = 0;

    private JList<String> donutNames;
    private JScrollPane menuScrollPane;
    private JTextField searchField;
    private DefaultListModel<String> listModel;


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
        setTitle("Oak Donuts Shop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new BorderLayout());

        // TITLE PANEL
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(202, 230, 215));
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(titlePanel, BorderLayout.NORTH);

        JLabel title = new JLabel("Oak Donuts Shop");
        title.setFont(new Font("Calibri", Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        titlePanel.add(title);

        // WEST PANEL
        JPanel westPanel = new JPanel();
        westPanel.setBackground(new Color(182, 227, 207));
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        westPanel.setPreferredSize(new Dimension(150,600));
        add(westPanel, BorderLayout.WEST);
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        searchLabel.setForeground(Color.BLACK);
        westPanel.add(searchLabel);

        searchField = new JTextField();
        Document doc = searchField.getDocument();

        listModel = new DefaultListModel<>();
        donutNames = new JList<String>(listModel);
        
        doc.addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateDonutMenu();
            }
            public void removeUpdate(DocumentEvent e) {
                updateDonutMenu();
            }
            public void changedUpdate(DocumentEvent e) {
            }
        });


        westPanel.add(searchField);


        westPanel.add(Box.createVerticalStrut(450));

        westPanel.setVisible(true);


        // CENTER PANEL

        JPanel centerPanel = new JPanel();
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
        lowerCenterPanel.setLayout(new BoxLayout(lowerCenterPanel, BoxLayout.X_AXIS));
        centerPanel.add(lowerCenterPanel);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1,1,10,1));
        quantitySpinner.setMaximumSize(new Dimension(80,80));
        lowerCenterPanel.add(quantitySpinner);

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
                    quantitySpinner.setValue(1);
                    menuDonutNames.clearSelection();
                } else {
                    System.out.println("No donut selected.");
                }
            }
        });
        lowerCenterPanel.add(addButton);



        // EAST PANEL
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(eastPanel, BorderLayout.EAST);
        

        JTable orderTable = new JTable();

        if (donutDAO.getAll()!=null){
            java.util.List<Donut> currentOrder = donutDAO.getAll();
            for(Donut donut : currentOrder){
                model.addRow(new Object[]{donut.getName(), quantitySpinner.getValue(), donut.getPrice()*(int)quantitySpinner.getValue()});
            }
            orderTable.setModel(model);
        }

        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 300)); // adjust size as you like
        eastPanel.add(orderScroll);

        JPanel lowerEastPanel = new JPanel();
        lowerEastPanel.setLayout(new BorderLayout());

        JPanel lowerEastEastPanel = new JPanel();
        lowerEastEastPanel.setLayout(new BoxLayout(lowerEastEastPanel, BoxLayout.Y_AXIS));
        JLabel subtotalLabel = new JLabel("Subtotal:");
        lowerEastEastPanel.add(subtotalLabel);
        JLabel taxLabel = new JLabel("Tax (6%):");
        lowerEastEastPanel.add(taxLabel);
        JLabel totalPanel = new JLabel("Total:");
        lowerEastEastPanel.add(totalPanel);


        lowerEastPanel.add(lowerEastEastPanel, BorderLayout.WEST);

        JPanel lowerEastWestPanel = new JPanel();
        lowerEastWestPanel.setLayout(new BoxLayout(lowerEastWestPanel, BoxLayout.Y_AXIS));

        JLabel subtotalAmount = new JLabel("$0.00");
        JLabel taxAmount = new JLabel("$0.00");
        JLabel totalAmount = new JLabel("$0.00");
        if(getOrder(orderIndex).getID()!=-1){
            DecimalFormat df = new DecimalFormat("#.00");
            subtotalAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()));
            taxAmount.setText("$"+df.format(getOrder(orderIndex).getPrice()*0.06));
            totalAmount.setText("$"+(df.format((getOrder(orderIndex).getPrice()+getOrder(orderIndex).getPrice()*0.06))));
        }
        lowerEastWestPanel.add(subtotalAmount);
        lowerEastWestPanel.add(taxAmount);
        lowerEastWestPanel.add(totalAmount);

        lowerEastPanel.add(lowerEastWestPanel, BorderLayout.EAST);
        eastPanel.add(lowerEastPanel);
        setVisible(true);

    }

    private void updateOrderTable(DefaultTableModel model, Donut donut, int quanity){
        model.addRow(new Object[]{donut.getName(), quanity,donut.getPrice(),donut.getPrice()*quanity});
    }
    
    /**
     * DONUT CRUD FUNCTIONS
     */
    private static void addDonut(int id, String name, double price) {
        Donut donut;
        donut = new Donut(id, name, price);
        donutDAO.insert(donut);
    }

    private static void updateDonut(int id, String name, double price) {
        Donut donut;
        donut = new Donut(id, name, price);
        donutDAO.update(donut);
    }

    private static void deleteDonut(int id, String name, double price) {
        Donut donut;
        donut = new Donut(id, name, price);
        donutDAO.delete(donut);
    }

    static Donut getDonut(int id) {
        Optional<Donut> donut = donutDAO.get(id);
        return donut.orElseGet(() -> new Donut(-1, "Non-exist", -1));
    }
    

    /**
     * ORDER CRUD FUNCTIONS
     */
    private static void addOrder(int ID, double price, String donutName) {
        Order order;
        order = new Order(ID, price, donutName);
        orderDAO.insert(order);
    }

    private static void updateOrder(int ID, double price, String donutName) {
        Order order;
        order = new Order(ID, price, donutName);
        orderDAO.update(order);
    }

    private static void deleteOrder(int ID, double price, String donutName) {
        Order order;
        order = new Order(ID, price, donutName);
        orderDAO.delete(order);
    }

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
    
    private String[] filterDonutMenu(String query){
        String[] menuStringArr = new String[menu.size()];
        int i = 0;
        int j = menu.size();
        for(;i < j; i++){
            String menuDonutAsString = menu.get(i).toString();
            if(menuDonutAsString.toUpperCase().contains(query.toUpperCase())){
                menuStringArr[i] = menuDonutAsString;
            }
        }
        return menuStringArr;
    }
    
    private void updateDonutMenu(){
        String[] filteredDonuts = filterDonutMenu(searchField.getText());

        // Clear the current list model (this removes existing donuts)
        listModel.clear();
/*
        // Add filtered donuts to the list model
        for (String dount : filteredDonuts) {
            listModel.addElement(donut);  // Adds a donut to the list model
        }
*/
        // Revalidate and repaint the JScrollPane containing the JList
        menuScrollPane.revalidate();
        menuScrollPane.repaint();
    }
    
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

    private Image backgroundImage;


}
