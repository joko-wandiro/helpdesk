/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.parser.DTDConstants;

/**
 *
 * @author Lenovo
 */
public class Login extends JFrame implements ActionListener, MouseListener {

    JPanel panel_login = new JPanel();
    // Form - Login
    JLabel label_email = new JLabel("Email", JLabel.LEFT);
    JTextField input_email = new JTextField();
    JLabel label_password = new JLabel("Password", JLabel.LEFT);
    JPasswordField input_password = new JPasswordField();
    JButton btn_login = new JButton("Login");
    // Database - Configuration
    String address = "jdbc:mysql://localhost:3306/helpdesk";
    String username = "root";
    String password = "root";

    public Login() {
        super("Login");
        setLookAndFeel();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Main Layout
        FlowLayout layout = new FlowLayout();
        setLayout(layout);
        // Form - Create
        label_email.setPreferredSize(new Dimension(400, 20));
        input_email.setPreferredSize(new Dimension(400, 20));
        label_password.setPreferredSize(new Dimension(400, 20));
        input_password.setPreferredSize(new Dimension(400, 20));
        GridLayout layout_login = new GridLayout(5, 1, 10, 10);
        panel_login.setLayout(layout_login);
        panel_login.add(label_email);
        panel_login.add(input_email);
        panel_login.add(label_password);
        panel_login.add(input_password);
        panel_login.add(btn_login);
        add(panel_login);
        // Add Event Listener
        btn_login.addActionListener(this);
        setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Login app = new Login();
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        switch (command) {
            case "Login":
                login();
                break;
        }
    }

    public void login() {
        try {
            String input_email_text = input_email.getText();
            String input_password_text = input_password.getText();
            String sql = "SELECT * FROM admins WHERE email='" + input_email_text + "' AND password=MD5('"
                    + input_password_text + "')";
            Connection con = DriverManager.getConnection(address, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Boolean is_valid = rs.first();
            if (is_valid) {
                // Open Dashboard
                this.hide();
                Category category_page = new Category();
            } else {
                JOptionPane.showMessageDialog(null, "Email or password is incorrect");
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"
            );
        } catch (Exception exc) {
            // ignore error
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
