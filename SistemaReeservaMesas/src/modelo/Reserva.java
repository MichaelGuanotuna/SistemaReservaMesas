package modelo;

public class Reserva {

    // Atributos
    private String codigo;
    private String codigoMesa;
    private String cliente;
    private String fecha;
    private String turno;
    private int personas;
    private String estado;

    // Constructor vacío
    public Reserva() {

    }

    // Constructor con parámetros
    public Reserva(String codigo, String codigoMesa,
            String cliente, String fecha, String turno,
            int personas, String estado) {

        this.codigo = codigo;
        this.codigoMesa = codigoMesa;
        this.cliente = cliente;
        this.fecha = fecha;
        this.turno = turno;
        this.personas = personas;
        this.estado = estado;
    }

    // Métodos getters
    public String getCodigo() {
        return codigo;
    }

    public String getCodigoMesa() {
        return codigoMesa;
    }

    public String getCliente() {
        return cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTurno() {
        return turno;
    }

    public int getPersonas() {
        return personas;
    }

    public String getEstado() {
        return estado;
    }

    // Método setter para el estado
    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Representación de la reserva
    @Override
    public String toString() {
        return codigo + " - " + cliente
                + " (" + estado + ")";
    }
}

