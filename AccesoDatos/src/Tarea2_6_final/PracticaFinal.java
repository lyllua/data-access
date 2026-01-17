package Tarea2_6_final;

import java.sql.*;

public class PracticaFinal { 

    private static final String url = "";
    private static final String user = "";
    private static final String pass = "";

    public static void main(String[] args) {
        Connection conn = null;

        try {
            System.out.println("Starting...");

            conn = DriverManager.getConnection(url, user, pass);
            conn.setAutoCommit(false);

            System.out.println("\nUpdatable ResultSet Operations...");

            Statement stRS = conn.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            ResultSet rs = stRS.executeQuery("SELECT * FROM CLIENTES");

            if (rs.first()) {
                rs.updateString("APELLIDOS", "GARCIA_MOD");
                rs.updateRow();
                System.out.println(" -> First record modified (GARCIA).");
            }

            if (rs.last()) {
                String deletedLastName = rs.getString("APELLIDOS");
                rs.deleteRow();
                System.out.println(" -> Last record deleted (" + deletedLastName + ").");
            }

            System.out.println(" -> Inserting 2 clients using insertRow()...");

            rs.moveToInsertRow();
            rs.updateString("DNI", "99999999Z");
            rs.updateString("APELLIDOS", "NUEVO_RS_1");
            rs.updateString("CP", "00000");
            rs.insertRow();

            rs.moveToInsertRow();
            rs.updateString("DNI", "88888888Y");
            rs.updateString("APELLIDOS", "NUEVO_RS_2");
            rs.updateString("CP", "00000");
            rs.insertRow();

            rs.moveToCurrentRow();
            rs.close();
            stRS.close();

            System.out.println("\nInserting 100 clients...");

            String sqlBatch = "INSERT INTO CLIENTES (DNI, APELLIDOS, CP) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sqlBatch);

            for (int i = 1; i <= 100; i++) {
                ps.setString(1, "BATCH-" + i);
                ps.setString(2, "Cliente_Lote_" + i); // Data value kept as requested
                ps.setString(3, "280" + (i % 90));

                ps.addBatch();
            }

            int[] results = ps.executeBatch();
            ps.close();

            System.out.println(" -> Batch finished. " + results.length + " records inserted.");


            conn.commit();
            System.out.println("\n[END] COMMIT PERFORMED. Process successful.");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("ERROR DETECTED: rollback performed");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}