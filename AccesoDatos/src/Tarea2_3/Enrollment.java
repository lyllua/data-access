package Tarea2_3;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Enrollment {

    static final String url = "";
    static final String user = "";
    static final String passwd = "";

    public static void main(String[] args) {

        try (Connection connection = DriverManager.getConnection(url, user, passwd);
        Scanner sc = new Scanner(System.in)) {

            System.out.println("Connection success");
            int option = 0;

            while (option != 5) {
                showMenu();
                try {
                    System.out.println("Choose an option: ");
                    option = sc.nextInt();
                    sc.nextLine();

                    switch (option) {
                        case 1:
                            register(connection, sc);
                            break;
                        case 2:
                            delete(connection, sc);
                            break;
                        case 3:
                            enroll(connection, sc);
                            break;
                        case 4:
                            viewSubjects(connection, sc);
                            break;
                        case 5:
                            System.out.println("Closing system...");
                            break;
                        default:
                            System.out.println("Invalid option");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Introduce a number");
                    sc.nextLine();
                }
            }
        } catch (SQLException e) {
            System.out.println("Connection error.");
            e.printStackTrace();
        }
    }

    public static void showMenu() {
        System.out.println("---- MENU ----");
        System.out.println("1. Register an student");
        System.out.println("2. Delete an student");
        System.out.println("3. Enroll an student");
        System.out.println("4. View subjects of an student");
        System.out.println("Exit");
    }

    public static void register(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter id: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.println("Enter last name: ");
            String lastname = sc.nextLine();

            System.out.println("Enter name: ");
            String name = sc.nextLine();

            System.out.println("Enter course: ");
            int course = sc.nextInt();

            System.out.println("Enter degree: ");
            int degree = sc.nextInt();

            String sql = "INSERT INTO alumnos (id_alumno, apellidos, nombre, curso, titulacion) VALUES (?, ?, ?, ?, ?)";

            try (java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setString(2, lastname);
                ps.setString(3, name);
                ps.setInt(4, course);
                ps.setInt(5, degree);

                ps.executeUpdate();
                System.out.println("Student registered.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input format.");
            sc.nextLine();
        }
    }

    public static void delete(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter student id to delete: ");
            int id = sc.nextInt();

            String sql = "DELETE FROM alumnos WHERE id_alumno = ?";

            try (java.sql.PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    System.out.println("Student deleted.");
                } else {
                    System.out.println("id not found");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Enter a valid number.");
            sc.nextLine();
        }
    }

    public static void enroll (Connection connection, Scanner sc) {
        try {
            System.out.println("Enter a student id: ");
            int id = sc.nextInt();

            System.out.println("Enter a subject: ");
            int subject = sc.nextInt();

            String sql = "INSERT INTO alumnos_asignaturas (id_alumno, id_asignatura, cursada) VALUES (?, ?, 0)";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.setInt(2, subject);

                ps.executeUpdate();
                System.out.println("Enrollment done.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Error: Enter valid numbers.");
            sc.nextLine();
        }
    }

    public static void viewSubjects(Connection connection, Scanner sc) {
        try {
            System.out.println("Enter a student id: ");
            int id = sc.nextInt();

            String sql = "SELECT alumnos.nombre, apellidos, asignaturas.nombre, tipo, cursada FROM alumnos JOIN alumnos_asignaturas ON alumnos.id_alumno = alumnos_asignaturas.id_alumno JOIN asignaturas ON alumnos_asignaturas.id_asignatura = asignaturas.id_asignatura WHERE alumnos.id_alumno = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    boolean hasData= false;

                    while(rs.next()) {
                        hasData = true;

                        String name = rs.getString(1);
                        String lastname= rs.getString(2);
                        String subject = rs.getString(3);
                        String type = rs.getString(4);
                        int cursed = rs.getInt(5);

                        System.out.println("Student: " + name + " " + lastname);
                        System.out.println("Subject: " + subject + " (" + type + ")");

                        if(cursed == 1) {
                            System.out.println("Status: Completed");
                        } else {
                            System.out.println("Status: Pending");
                        }
                    }
                    if (!hasData) {
                        System.out.println("No records found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Enter a number");
            sc.nextLine();
        }
    }
}
