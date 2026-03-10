package Tarea2_5;

import java.sql.*;

public class CStatement {
    public static void main(String[] args) {
        String url = "";
        String user = "";
        String pass = "";

        try (Connection connection = DriverManager.getConnection(url, user, pass)) {

            CallableStatement cst = connection.prepareCall("{call listado_productos_precio(?, ?)}");

            cst.setDouble(1, 30.00);
            cst.setInt(2, 0);
            cst.registerOutParameter(2, Types.INTEGER);

            ResultSet rs = cst.executeQuery();

            while (rs.next()) {
                System.out.println("Product: " + rs.getString("NOMBRE") + " | Price: " + rs.getInt("PRECIO"));
            }

            rs.close();

            int finalTotal = cst.getInt(2);
            System.out.println("Final INOUT value: " + finalTotal);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}