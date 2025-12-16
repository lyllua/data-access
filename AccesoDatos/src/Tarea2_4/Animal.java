package Tarea2_4;

public class Animal {
    private int codigo;
    private String nombre;
    private String tipo;
    private String propietarioDni;

    public Animal(int codigo, String nombre, String tipo, String propietarioDni) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.propietarioDni = propietarioDni;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getPropietarioDni() { return propietarioDni; }
    public void setPropietarioDni(String propietarioDni) { this.propietarioDni = propietarioDni; }

    @Override
    public String toString() {
        return "Animal (id: " + codigo + ", name: '" + nombre + "', type: '" + tipo + "', owner: '" + propietarioDni + "')";
    }
}