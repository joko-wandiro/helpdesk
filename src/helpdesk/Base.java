/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpdesk;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import javax.swing.*;

/**
 *
 * @author Lenovo
 */
public class Base extends JFrame implements ActionListener, MouseListener {

    public Base() {
        // Add Menu Bar
        add_menubar();
    }

    public void add_menubar() {
        JMenu menu_pages = new JMenu("Pages");
        menu_pages.setMnemonic(KeyEvent.VK_P);
        JMenuItem menu_pages_category = new JMenuItem("Category");
        menu_pages_category.addActionListener(this);
        menu_pages.add(menu_pages_category);
        JMenuItem menu_pages_department = new JMenuItem("Department");
        menu_pages_department.addActionListener(this);
        menu_pages.add(menu_pages_department);
        JMenuItem menu_pages_status = new JMenuItem("Status");
        menu_pages_status.addActionListener(this);
        menu_pages.add(menu_pages_status);
        JMenuItem menu_pages_employee = new JMenuItem("Employee");
        menu_pages_employee.addActionListener(this);
        menu_pages.add(menu_pages_employee);
        JMenuItem menu_pages_ticket = new JMenuItem("Ticket");
        menu_pages_ticket.addActionListener(this);
        menu_pages.add(menu_pages_ticket);
        JMenuItem menu_pages_exit = new JMenuItem("Exit");
        menu_pages_exit.addActionListener(this);
        menu_pages.add(menu_pages_exit);
        // Create MenuBar
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu_pages);
        setJMenuBar(menuBar);
    }

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        switch (command) {
            case "Category":
                this.setVisible(false);
                Category category = new Category();
                break;
            case "Ticket":
                this.setVisible(false);
                Ticket ticket = new Ticket();
                break;
            case "Department":
                this.setVisible(false);
                Department department = new Department();
                break;
            case "Employee":
                this.setVisible(false);
                Employee employee = new Employee();
                break;
            case "Status":
                this.setVisible(false);
                Status status = new Status();
                break;
            case "Exit":
                System.exit(0);
                break;
        }
    }

    public boolean is_required(String value) {
        return (value.equalsIgnoreCase("")) ? true : false;
    }

    public String is_required_message(String key) {
        return key + " is required.<br/>";
    }

    public boolean is_int(String value) {
        boolean result = false;
        try {
            int var_int = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            result = true;
        }
        return result;
    }

    public String is_int_message(String key) {
        return key + " is not number.<br/>";
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

    public void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"
            );
        } catch (Exception exc) {
            // ignore error
        }
    }

}
