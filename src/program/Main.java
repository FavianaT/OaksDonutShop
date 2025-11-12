package program;

import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import entity.*;
import javax.swing.*;

/**
 * @author Gokhan
 */

public class Main extends javax.swing.JFrame {
    private ArrayList<entity.Donut> menu = new ArrayList<entity.Donut>();
    private static DonutDAO donutDAO = new DonutDAO();

    public Main(){
        menu.add(new Donut(0,"Glazed Donut", 1.49));
        menu.add(new Donut(1,"Strawberry Frosted Donut", 1.80));
        menu.add(new Donut(2,"Chocolate Frosted Donut", 1.80));
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
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(titlePanel, BorderLayout.NORTH);

        JLabel title = new JLabel("Oak Donuts Shop");
        title.setFont(new Font("Calibri", Font.BOLD, 20));
        title.setForeground(Color.BLACK);
        titlePanel.add(title);
        titlePanel.setVisible(true);

        // WEST PANEL
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        westPanel.setPreferredSize(new Dimension(150,600));
        add(westPanel, BorderLayout.WEST);

        JLabel fitlersLabel= new JLabel("Filters");
        fitlersLabel.setFont(new Font("Calibri", Font.BOLD, 16));
        fitlersLabel.setForeground(Color.BLACK);
        westPanel.add(fitlersLabel);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        categoryLabel.setForeground(Color.BLACK);
        westPanel.add(categoryLabel);

        JComboBox<String> categoryComboBox = new JComboBox<>(new String[]{"All","1","2"});
        westPanel.add(categoryComboBox);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Calibri", Font.BOLD, 12));
        searchLabel.setForeground(Color.BLACK);
        westPanel.add(searchLabel);

        JTextField searchField = new JTextField();
        westPanel.add(searchField);

        westPanel.add(Box.createVerticalStrut(305));

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
        centerPanel.setVisible(true);

        // EAST PANEL
        JPanel eastPanel = new JPanel();
        eastPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(eastPanel, BorderLayout.EAST);
        eastPanel.setVisible(true);

        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedDonut = menuDonutNames.getSelectedValue();
                int selectedDonutIndex = menuDonutNames.getSelectedIndex();
                if (selectedDonut != null) {
                    // Perform actions with the selected item
                    System.out.println("Selected Donut: " + selectedDonut);

                    System.out.println("Selected donut index: " + selectedDonutIndex);
                    Donut i = menu.get(selectedDonutIndex);
                    addDonut(i.getID(), i.getName(), i.getPrice());
                    initComponents();
                } else {
                    System.out.println("No item selected.");
                }
            }
        });
        centerPanel.add(addButton);

        JTable orderTable = new JTable();
        javax.swing.table.DefaultTableModel dtm = new javax.swing.table.DefaultTableModel(
                new Object [][] {

                },
                new String [] {
                        "Donut", "Qty", "Price", "Total"
                }
        ) {
            boolean[] canEdit = new boolean [] {
                    false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        if (donutDAO.getAll()!=null){
            java.util.List<Donut> currentOrder = donutDAO.getAll();
            for(Donut donut : currentOrder){
                dtm.addRow(new Object[]{donut.getName(), 1, donut.getPrice(), donut.getPrice()*1});
            }
            orderTable.setModel(dtm);
        }

        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setPreferredSize(new Dimension(400, 400)); // adjust size as you like
        eastPanel.add(orderScroll);

        setVisible(true);

    }


    /**
     * ITEM CRUD FUNCTIONS
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

    public static void main(String args[]) {
        donutDAO = new DonutDAO();

        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
