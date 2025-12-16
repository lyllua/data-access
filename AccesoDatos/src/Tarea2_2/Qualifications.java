package Tarea2_2;

import java.sql.*;

public class Qualifications {
    public static void main(String[] args) {

        String url = "";
        String user = "";
        String pass = "";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             Statement st = con.createStatement()) {

            System.out.println("Conexión correcta a MySQL");

            String crear = "CREATE TABLE IF NOT EXISTS NotasFinales (" + "Mat INT, Cod INT, NotaMedia DECIMAL(5,2))";
            st.executeUpdate(crear);
            st.executeUpdate("DELETE FROM NotasFinales");
            String insertar = "INSERT INTO NotasFinales (Mat, Cod, NotaMedia) " +
            "SELECT Mat, Cod, (Nota1 + Nota2 + Nota3)/3 FROM Notas";
            st.executeUpdate(insertar);
            String consulta =
                    "SELECT A.Nombre AS Alumno, S.NombreAsig AS Asignatura, " +
                            "N.Nota1, N.Nota2, N.Nota3, NF.NotaMedia " +
                            "FROM Alumnos A " +
                            "JOIN Notas N ON A.Mat = N.Mat " +
                            "JOIN Asignaturas S ON S.Cod = N.Cod " +
                            "JOIN NotasFinales NF ON NF.Mat = N.Mat AND NF.Cod = N.Cod";
            System.out.printf("%-20s %-15s %-8s %-8s %-8s %-10s\n",
                    "Nombre Alumno", "Asignatura", "Nota1", "Nota2", "Nota3", "Media");
            System.out.println("--------------------------------------------------------------------------");
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                System.out.printf("%-20s %-15s %-8.2f %-8.2f %-8.2f %-10.2f\n",
                        rs.getString("Alumno"),
                        rs.getString("Asignatura"),
                        rs.getDouble("Nota1"),
                        rs.getDouble("Nota2"),
                        rs.getDouble("Nota3"),
                        rs.getDouble("NotaMedia"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
