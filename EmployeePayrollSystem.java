import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class Employee {
    int id;
    String name, department;
    double salary;

    Employee() {
        this(0, "", 0, "");
    }

    Employee(int id, String name, double salary, String department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
    }

    Employee(Employee e) {
        this(e.id, e.name, e.salary, e.department);
    }

    double getNetSalary() {
        return salary * 1.05;
    }

    public String toString() {
        return id + "," + name + "," + salary + "," + department;
    }

    String details() {
        return "ID: " + id + " | Name: " + name
             + " | Department: " + department
             + "\nBasic Salary: Rs. " + salary
             + " | Net Salary: Rs. " + String.format("%.2f", getNetSalary())
             + "\n---------------------------------------------\n";
    }
}

public class EmployeePayrollSystem extends JFrame {
    JTextField id = new JTextField();
    JTextField name = new JTextField();
    JTextField salary = new JTextField();

    JComboBox<String> dept = new JComboBox<>(
        new String[]{"IT", "HR", "Sales", "Finance", "Marketing"});

    JTextArea output = new JTextArea();
    JLabel count = new JLabel("Total Employees: 0");

    ArrayList<Employee> employees = new ArrayList<>();
    Color blue = new Color(25, 65, 120);

    EmployeePayrollSystem() {
        setTitle("Employee Payroll System");
        setSize(650, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        main.setBackground(new Color(240, 244, 250));

        JLabel title = new JLabel("EMPLOYEE PAYROLL SYSTEM", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(blue);
        title.setBorder(BorderFactory.createEmptyBorder(18, 5, 18, 5));
        main.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createTitledBorder("Employee Details"));
        form.setBackground(Color.WHITE);

        form.add(new JLabel("Employee ID:"));
        form.add(id);
        form.add(new JLabel("Employee Name:"));
        form.add(name);
        form.add(new JLabel("Basic Salary (Rs.):"));
        form.add(salary);
        form.add(new JLabel("Department:"));
        form.add(dept);

        output.setEditable(false);
        output.setFont(new Font("Monospaced", Font.PLAIN, 13));
        output.setBackground(Color.WHITE);

        JPanel buttons = new JPanel(new GridLayout(1, 3, 8, 8));
        String[] labels = {"Add Employee", "Save Records", "Clear Fields"};
        Color[] colors = {new Color(35, 135, 80), blue, Color.GRAY};

        for (int i = 0; i < labels.length; i++) {
            JButton b = new JButton(labels[i]);
            b.setBackground(colors[i]);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            buttons.add(b);

            final int action = i;
            b.addActionListener(e -> {
                switch (action) {
                    case 0 -> addEmployee();
                    case 1 -> saveEmployees();
                    case 2 -> clearFields();
                }
            });
        }

        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.add(buttons, BorderLayout.CENTER);
        count.setFont(new Font("Arial", Font.BOLD, 14));
        count.setForeground(blue);
        bottom.add(count, BorderLayout.SOUTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(form, BorderLayout.NORTH);
        center.add(new JScrollPane(output), BorderLayout.CENTER);
        center.add(bottom, BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);
        add(main);
        setVisible(true);
    }

    void addEmployee() {
        try {
            int empId = Integer.parseInt(id.getText().trim());
            String empName = name.getText().trim();
            double empSalary = Double.parseDouble(salary.getText().trim());

            if (empId <= 0 || empName.isEmpty()
                    || empSalary < 0 || !Double.isFinite(empSalary)) {
                JOptionPane.showMessageDialog(this, "Enter valid details.");
                return;
            }

            for (Employee e : employees) {
                if (e.id == empId) {
                    JOptionPane.showMessageDialog(this, "ID already exists.");
                    return;
                }
            }

            Employee e = new Employee(empId, empName, empSalary,
                                      dept.getSelectedItem().toString());
            employees.add(new Employee(e));

            output.append(e.details());
            count.setText("Total Employees: " + employees.size());

            JOptionPane.showMessageDialog(this,
                "Employee added!\nNet Salary: Rs. "
                + String.format("%.2f", e.getNetSalary()));

            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "ID must be an integer and salary must be numeric.");
        }
    }

    void saveEmployees() {
        try (BufferedWriter w = new BufferedWriter(
                new FileWriter("employees.txt"))) {
            for (Employee e : employees) {
                w.write(e.toString());
                w.newLine();
            }
            JOptionPane.showMessageDialog(this, "Records saved!");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file.");
        }
    }

    void clearFields() {
        id.setText("");
        name.setText("");
        salary.setText("");
        dept.setSelectedIndex(0);
        id.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeePayrollSystem());
    }
}