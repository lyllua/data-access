package Tarea2_4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    public List<Owner> getOwners() {
        List<Owner> list = new ArrayList<>();
        String sql = "SELECT DNI, Nombre FROM propietarios";

        try (Connection conn = ConnectionDB.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Owner(rs.getString("DNI"), rs.getString("Nombre")));
            }
        } catch (SQLException e) {
            System.err.println("Error getting owners: " + e.getMessage());
        }
        return list;
    }

    public List<Animal> getCats() {
        List<Animal> list = new ArrayList<>();
        String sql = "SELECT Codigo, Nombre, Tipo, Propietario FROM animales WHERE Tipo = ?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "gato");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Animal(
                            rs.getInt("Codigo"),
                            rs.getString("Nombre"),
                            rs.getString("Tipo"),
                            rs.getString("Propietario")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting cats: " + e.getMessage());
        }
        return list;
    }

    public void addAnimal(Animal animal) {
        String checkOwnerSql = "SELECT DNI FROM propietarios WHERE DNI = ?";
        String insertAnimalSql = "INSERT INTO animales (Codigo, Nombre, Tipo, Propietario) VALUES (?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = ConnectionDB.getConnection();
            conn.setAutoCommit(false);

            boolean ownerExists = false;
            try (PreparedStatement checkPs = conn.prepareStatement(checkOwnerSql)) {
                checkPs.setString(1, animal.getPropietarioDni());
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) ownerExists = true;
            }

            if (!ownerExists) {
                System.out.println("Error: Owner " + animal.getPropietarioDni() + " not found. Rolling back.");
                conn.rollback();
                return;
            }

            try (PreparedStatement ps = conn.prepareStatement(insertAnimalSql)) {
                ps.setInt(1, animal.getCodigo());
                ps.setString(2, animal.getNombre());
                ps.setString(3, animal.getTipo());
                ps.setString(4, animal.getPropietarioDni());
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("Success: Animal inserted.");

        } catch (SQLException e) {
            System.err.println("Transaction Error: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public void deleteOwner(String dni) {
        String checkAnimalsSql = "SELECT COUNT(*) FROM animales WHERE Propietario = ?";
        String deleteOwnerSql = "DELETE FROM propietarios WHERE DNI = ?";

        Connection conn = null;

        try {
            conn = ConnectionDB.getConnection();
            conn.setAutoCommit(false);

            int animalCount = 0;
            try (PreparedStatement checkPs = conn.prepareStatement(checkAnimalsSql)) {
                checkPs.setString(1, dni);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    animalCount = rs.getInt(1);
                }
            }

            if (animalCount > 0) {
                System.out.println("Error: Cannot delete owner " + dni + ". Has associated animals. Rolling back.");
                conn.rollback();
                return;
            }

            try (PreparedStatement deletePs = conn.prepareStatement(deleteOwnerSql)) {
                deletePs.setString(1, dni);
                int rows = deletePs.executeUpdate();
                if (rows > 0) {
                    System.out.println("Owner deleted successfully.");
                } else {
                    System.out.println("Owner not found.");
                }
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Error deleting owner: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public void addOwnerWithAudit(Owner owner) {
        String insertOwnerSql = "INSERT INTO propietarios (DNI, Nombre) VALUES (?, ?)";
        String insertAuditSql = "INSERT INTO auditoria_propietarios (dni_propietario, fecha) VALUES (?, NOW())";

        Connection conn = null;
        try {
            conn = ConnectionDB.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(insertOwnerSql)) {
                ps.setString(1, owner.getDni());
                ps.setString(2, owner.getNombre());
                ps.executeUpdate();
            }

            try (PreparedStatement psAudit = conn.prepareStatement(insertAuditSql, Statement.RETURN_GENERATED_KEYS)) {
                psAudit.setString(1, owner.getDni());
                psAudit.executeUpdate();

                try (ResultSet rs = psAudit.getGeneratedKeys()) {
                    if (rs.next()) {
                        System.out.println("Audit record created with ID: " + rs.getInt(1));
                    }
                }
            }

            conn.commit();
            System.out.println("Owner and Audit inserted successfully.");

        } catch (SQLException e) {
            System.err.println("Error adding owner with audit: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
}