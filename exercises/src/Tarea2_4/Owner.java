package Tarea2_4;

public class Owner {
    private String dni;
    private String nombre;

    public Owner(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "Owner (dni: '" + dni + "', name: '" + nombre + "')";
    }
}