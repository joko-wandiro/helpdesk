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
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.parser.DTDConstants;

/**
 *
 * @author Lenovo
 */
public class Ticket extends Base {

    JPanel panel_main = new JPanel();
    JPanel panel_buttons = new JPanel();
    JPanel panel_table = new JPanel();
    JPanel panel_pagination = new JPanel();
    JPanel panel_validation = new JPanel();
    JPanel panel_form_create = new JPanel();
    JPanel panel_form_edit = new JPanel();
    JTable table = new JTable();
    Map<String, String> employee_list = new HashMap<String, String>();
    Map<String, String> status_list = new HashMap<String, String>();
    // Panel - Buttons
    JButton btn_create = new JButton("Create");
    JButton btn_edit = new JButton("Edit");
    JButton btn_delete = new JButton("Delete");
    // Panel - Pagination
    JButton btn_first = new JButton("First");
    JButton btn_prev = new JButton("Prev");
    JButton btn_next = new JButton("Next");
    JButton btn_last = new JButton("Last");
    JLabel label_pagination = new JLabel("", JLabel.LEFT);
    // Validation
    JLabel label_validation_error = new JLabel("", JLabel.LEFT);
    // Form - Create
    JLabel label_id_employee = new JLabel("Name", JLabel.LEFT);
    JComboBox input_id_employee = new JComboBox();
    JLabel label_subject = new JLabel("Subject", JLabel.LEFT);
    JTextField input_subject = new JTextField();
    JLabel label_description = new JLabel("Description", JLabel.LEFT);
    JTextArea input_description = new JTextArea();
    JButton btn_save = new JButton("Save");
    // Form - Edit
    JTextField input_id_update = new JTextField();
    JLabel label_id_employee_update = new JLabel("Name", JLabel.LEFT);
    JComboBox input_id_employee_update = new JComboBox();
    JLabel label_subject_update = new JLabel("Subject", JLabel.LEFT);
    JTextField input_subject_update = new JTextField();
    JLabel label_description_update = new JLabel("Description", JLabel.LEFT);
    JTextArea input_description_update = new JTextArea();
    JLabel label_id_status_update = new JLabel("Status", JLabel.LEFT);
    JComboBox input_id_status_update = new JComboBox();
    JButton btn_update = new JButton("Update");
    String address = "jdbc:mysql://localhost:3306/helpdesk";
    String username = "root";
    String password = "root";
    Map<String, String> validation_errors = new HashMap<String, String>();
    String validation_error_message = "";

    // Pagination
    int page = 1;
    int number_of_record = 10;
    int offset = 0;
    int last_page = 1;

    public void setup_table() {
        DefaultTableModel model = new DefaultTableModel();
        // Add columns
        model.addColumn("Id");
        model.addColumn("Name");
        model.addColumn("Department");
        model.addColumn("Subject");
        model.addColumn("Description");
        model.addColumn("Status");
        try {
            page = (page < 1) ? 1 : page;
            page = (page > last_page) ? last_page : page;
            offset = (page - 1) * number_of_record;
            String sql = "SELECT SQL_CALC_FOUND_ROWS tickets.*, employees.name AS employee_name, "
                    + "departments.name AS department_name, status.name AS status_name FROM tickets "
                    + "INNER JOIN employees ON employees.id=tickets.id_employee "
                    + "INNER JOIN departments ON departments.id=employees.id_department "
                    + "INNER JOIN status ON status.id=tickets.id_status "
                    + "ORDER BY tickets.id DESC "
                    + "LIMIT " + offset + ", " + number_of_record;
            Connection con = DriverManager.getConnection(address, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                // Add row
                model.addRow(new Object[]{rs.getString("id"), rs.getString("employee_name"),
                    rs.getString("department_name"), rs.getString("subject"),
                    rs.getString("description"), rs.getString("status_name")});
            }
            // Get total row
            String sql_total = "SELECT FOUND_ROWS() AS total";
            Connection con_total = DriverManager.getConnection(address, username, password);
            Statement stmt_total = con.createStatement();
            ResultSet rs_total = stmt_total.executeQuery(sql_total);
            rs_total.first();
            last_page = (int) Math.ceil((double) rs_total.getInt("total") / number_of_record);
            // Set pagination identifier
            int from_record = offset + 1;
            int to_record = (page == last_page) ? rs_total.getInt("total") : page * number_of_record;
            label_pagination.setText("Showing " + from_record + " to " + to_record + " of "
                    + rs_total.getInt("total") + " entries");
        } catch (Exception e) {
            System.out.println(e);
        }
//        System.exit(0);
        table.setModel(model);
    }

    public void populate_data_input() {
        try {
            // Get employees
            String sql_get_employee = "SELECT * FROM employees";
            Connection con_get_employee = DriverManager.getConnection(address, username, password);
            Statement stmt_get_employee = con_get_employee.createStatement();
            ResultSet rs_get_employee = stmt_get_employee.executeQuery(sql_get_employee);
            while (rs_get_employee.next()) {
                // Add item
                input_id_employee.addItem(rs_get_employee.getString("name"));
                input_id_employee_update.addItem(rs_get_employee.getString("name"));
                employee_list.put(rs_get_employee.getString("name"), rs_get_employee.getString("id"));
            }
            // Get status
            String sql_get_status = "SELECT * FROM status";
            Connection con_get_status = DriverManager.getConnection(address, username, password);
            Statement stmt_get_status = con_get_status.createStatement();
            ResultSet rs_get_status = stmt_get_status.executeQuery(sql_get_status);
            while (rs_get_status.next()) {
                // Add item
                input_id_status_update.addItem(rs_get_status.getString("name"));
                status_list.put(rs_get_status.getString("name"), rs_get_status.getString("id"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Ticket() {
        setLookAndFeel();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setup_table();
        populate_data_input();
        // Main Layout
//        FlowLayout layout = new FlowLayout();
//        setLayout(layout);
        BoxLayout layout = new BoxLayout(panel_main, BoxLayout.Y_AXIS);
        panel_main.setLayout(layout);
        // Layout - Table
//        BoxLayout layout_panel_buttons = new BoxLayout(panel_buttons, BoxLayout.Y_AXIS);
        FlowLayout layout_panel_buttons = new FlowLayout(FlowLayout.LEFT, 10, 10);
        panel_buttons.setLayout(layout_panel_buttons);
        panel_buttons.add(btn_create);
        panel_buttons.add(btn_edit);
        panel_buttons.add(btn_delete);
        panel_buttons.add(new JScrollPane(table));
        add(panel_buttons);
        // Layout - Validation
        GridLayout layout_validation = new GridLayout(1, 1, 10, 10);
        panel_validation.setLayout(layout_validation);
        panel_validation.add(label_validation_error);
        add(panel_validation);
        // Layout - Table
        GridLayout layout_table = new GridLayout(1, 1, 10, 10);
        panel_table.setLayout(layout_table);
        panel_table.add(new JScrollPane(table));
        // Layout - Pagination
        FlowLayout layout_pagination = new FlowLayout(FlowLayout.CENTER, 10, 10);
        panel_pagination.setLayout(layout_pagination);
        panel_pagination.add(btn_first);
        panel_pagination.add(btn_prev);
        panel_pagination.add(btn_next);
        panel_pagination.add(btn_last);
        panel_pagination.add(label_pagination);
        // Form - Create
        label_id_employee.setPreferredSize(new Dimension(400, 20));
        input_id_employee.setPreferredSize(new Dimension(400, 20));
        label_subject.setPreferredSize(new Dimension(400, 20));
        input_subject.setPreferredSize(new Dimension(400, 20));
        label_description.setPreferredSize(new Dimension(400, 20));
        input_description.setPreferredSize(new Dimension(400, 20));
        GridLayout layout_form_create = new GridLayout(7, 1, 10, 10);
        panel_form_create.setLayout(layout_form_create);
        panel_form_create.add(label_id_employee);
        panel_form_create.add(input_id_employee);
        panel_form_create.add(label_subject);
        panel_form_create.add(input_subject);
        panel_form_create.add(label_description);
        panel_form_create.add(input_description);
        panel_form_create.add(btn_save);
        add(panel_form_create);
        // Form - Edit
        input_id_update.setVisible(false);
        label_id_employee_update.setPreferredSize(new Dimension(400, 20));
        input_id_employee_update.setPreferredSize(new Dimension(400, 20));
        label_subject_update.setPreferredSize(new Dimension(400, 20));
        input_subject_update.setPreferredSize(new Dimension(400, 20));
        label_description_update.setPreferredSize(new Dimension(400, 20));
        input_description_update.setPreferredSize(new Dimension(400, 20));
        label_id_status_update.setPreferredSize(new Dimension(400, 20));
        input_id_status_update.setPreferredSize(new Dimension(400, 20));
        GridLayout layout_form_edit = new GridLayout(10, 1, 10, 10);
        panel_form_edit.setLayout(layout_form_edit);
        panel_form_edit.add(input_id_update);
        panel_form_edit.add(label_id_employee_update);
        panel_form_edit.add(input_id_employee_update);
        panel_form_edit.add(label_subject_update);
        panel_form_edit.add(input_subject_update);
        panel_form_edit.add(label_description_update);
        panel_form_edit.add(input_description_update);
        panel_form_edit.add(label_id_status_update);
        panel_form_edit.add(input_id_status_update);
        panel_form_edit.add(btn_update);
        add(panel_form_edit);
        // Add panel to main panel
        panel_main.add(panel_buttons);
        panel_main.add(panel_table);
        panel_main.add(panel_pagination);
        panel_main.add(panel_validation);
        panel_main.add(panel_form_create);
        panel_main.add(panel_form_edit);
        add(panel_main);
        // Add Event Listener
        btn_create.addActionListener(this);
        btn_edit.addActionListener(this);
        btn_delete.addActionListener(this);
        btn_save.addActionListener(this);
        btn_update.addActionListener(this);
        btn_first.addActionListener(this);
        btn_prev.addActionListener(this);
        btn_next.addActionListener(this);
        btn_last.addActionListener(this);
        panel_form_create.setVisible(false);
        panel_form_edit.setVisible(false);
        label_validation_error.setForeground(Color.red);
        // Set window or jframe scrollable
        Container container = getContentPane();
        JScrollPane scroll = new JScrollPane(container);
        setContentPane(scroll);
        setVisible(true);
    }

    public void reset() {
        label_validation_error.setText("");
        input_subject.setText("");
        input_description.setText("");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Ticket ticket_page = new Ticket();
    }

    public void actionPerformed(ActionEvent event) {
        super.actionPerformed(event);
        String command = event.getActionCommand();
        switch (command) {
            case "Create":
                create();
                break;
            case "Save":
                insert();
                break;
            case "Update":
                update();
                break;
            case "Edit":
                edit();
                break;
            case "Delete":
                delete();
                break;
            // Pagination
            case "First":
                page = 1;
                setup_table();
                break;
            case "Prev":
                page--;
                setup_table();
                break;
            case "Next":
                page++;
                setup_table();
                break;
            case "Last":
                page = last_page;
                setup_table();
                break;
        }
    }

    public void create() {
        panel_form_edit.setVisible(false);
        panel_form_create.setVisible(true);
    }

    public boolean validate_insert() {
        int validation_errors = 0;
        validation_error_message = "";
        String subject_text = this.input_subject.getText();
        if (is_required(subject_text)) {
            validation_error_message += is_required_message("Subject");
            validation_errors++;
        }
        String description_text = this.input_description.getText();
        if (is_required(description_text)) {
            validation_error_message += is_required_message("Description");
            validation_errors++;
        }
        if (validation_errors == 0) {
            label_validation_error.setText("");
            return true;
        } else {
            label_validation_error.setText("<html>" + validation_error_message + "</html>");
            return false;
        }
    }

    public void insert() {
        if (validate_insert()) {
            String employee = (String) input_id_employee.getSelectedItem();
            try {
                String sql = "INSERT INTO tickets SET id_employee='" + employee_list.get(employee)
                        + "', subject='" + input_subject.getText()
                        + "', description='" + input_description.getText() + "'";
                Connection con = DriverManager.getConnection(address, username, password);
                PreparedStatement pst = con.prepareStatement(sql);
                pst.execute();
                JOptionPane.showMessageDialog(null, "Penyimpanan Data Berhasil");
                reset();
                setup_table();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public boolean validate_update() {
        int validation_errors = 0;
        validation_error_message = "";
        String subject_text = this.input_subject_update.getText();
        if (is_required(subject_text)) {
            validation_error_message += is_required_message("Subject");
            validation_errors++;
        }
        String description_text = this.input_description_update.getText();
        if (is_required(description_text)) {
            validation_error_message += is_required_message("Description");
            validation_errors++;
        }
        if (validation_errors == 0) {
            label_validation_error.setText("");
            return true;
        } else {
            label_validation_error.setText("<html>" + validation_error_message + "</html>");
            return false;
        }
    }

    public void update() {
        if (validate_update()) {
            String employee = (String) input_id_employee_update.getSelectedItem();
            String status = (String) input_id_status_update.getSelectedItem();
            try {
                String sql = "UPDATE tickets SET id_employee='" + employee_list.get(employee)
                        + "', subject='" + input_subject_update.getText()
                        + "', description='" + input_description_update.getText()
                        + "', id_status='" + status_list.get(status) + "' "
                        + "WHERE id='" + input_id_update.getText() + "'";
                Connection con = DriverManager.getConnection(address, username, password);
                PreparedStatement pst = con.prepareStatement(sql);
                pst.execute();
                JOptionPane.showMessageDialog(null, "Data berhasil diperbaharui.");
                setup_table();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void edit() {
        label_validation_error.setText("");
        panel_form_create.setVisible(false);
        panel_form_edit.setVisible(true);
        int row = table.getSelectedRow();
        String id = table.getValueAt(row, 0).toString();
        try {
            String sql = "SELECT tickets.*, employees.name AS employee_name, "
                    + "departments.name AS department_name, status.name AS status_name FROM tickets "
                    + "INNER JOIN employees ON employees.id=tickets.id_employee "
                    + "INNER JOIN departments ON departments.id=employees.id_department "
                    + "INNER JOIN status ON status.id=tickets.id_status "
                    + "WHERE tickets.id='" + id + "'";
            Connection con = DriverManager.getConnection(address, username, password);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.first();
            input_id_update.setText(rs.getString("id"));
            input_id_employee_update.setSelectedItem(rs.getString("employee_name"));
            input_subject_update.setText(rs.getString("subject"));
            input_description_update.setText(rs.getString("description"));
            input_id_status_update.setSelectedItem(rs.getString("status_name"));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void delete() {
        int row = table.getSelectedRow();
        String id = table.getValueAt(row, 0).toString();
        try {
            String sql = "DELETE FROM tickets WHERE id='" + id + "'";
            Connection con = DriverManager.getConnection(address, username, password);
            PreparedStatement pst = con.prepareStatement(sql);
            pst.execute();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus.");
            setup_table();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
