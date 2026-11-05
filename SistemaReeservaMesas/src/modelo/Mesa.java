package modelo;

public class Mesa {

    // Atributos
    private String codigo;
    private int numero;
    private int capacidad;

    // Constructor vacío
    public Mesa() {

    }

    // Constructor con parámetros
    public Mesa(String codigo, int numero, int capacidad) {
        this.codigo = codigo;
        this.numero = numero;
        this.capacidad = capacidad;
    }

    // Métodos getters
    public String getCodigo() {
        return codigo;
    }

    public int getNumero() {
        return numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    // Métodos setters
    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    // Representación de la mesa
    @Override
    public String toString() {
        return codigo + " - Mesa " + numero
                + " (" + capacidad + " personas)";
    }
}
