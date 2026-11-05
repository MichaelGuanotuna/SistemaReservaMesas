package servicio;

import java.util.ArrayList;

import archivo.FileArchivos;
import modelo.Mesa;
import modelo.Reserva;

public class GestorReservas {

    // Atributos
    private ArrayList<Mesa> mesas;
    private ArrayList<Reserva> reservas;

    private FileArchivos archivos;

    private int ultimoCodigoMesa;
    private int ultimoCodigoReserva;

    // Constructor
    public GestorReservas() {

        archivos = new FileArchivos();

        mesas = archivos.cargarMesas();
        reservas = archivos.cargarReservas();

        int[] codigos = archivos.cargarCodigos();

        ultimoCodigoMesa = codigos[0];
        ultimoCodigoReserva = codigos[1];

        // Revisar los códigos existentes de las mesas
        for (int i = 0; i < mesas.size(); i++) {

            int codigo = Integer.parseInt(
                    mesas.get(i).getCodigo());

            if (codigo > ultimoCodigoMesa) {
                ultimoCodigoMesa = codigo;
            }
        }

        // Revisar los códigos existentes de las reservas
        for (int i = 0; i < reservas.size(); i++) {

            int codigo = Integer.parseInt(
                    reservas.get(i).getCodigo());

            if (codigo > ultimoCodigoReserva) {
                ultimoCodigoReserva = codigo;
            }
        }
    }

    // MÉTODO GENÉRICO CON VARARGS
    @SafeVarargs
    public final <T> void agregarVarios(
            ArrayList<T> lista, T... elementos) {

        for (int i = 0; i < elementos.length; i++) {
            lista.add(elementos[i]);
        }
    }

    // VALIDAR TEXTO
    private boolean textoValido(String texto) {

        return texto != null
                && !texto.trim().isEmpty()
                && !texto.contains("|");
    }

    // VALIDAR FECHA
    private boolean fechaValida(String fecha) {

        if (fecha == null) {
            return false;
        }

        // Formato AAAA-MM-DD
        return fecha.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // VALIDAR TURNO
    private boolean turnoValido(String turno) {

        return "12:00".equals(turno)
                || "15:00".equals(turno)
                || "19:00".equals(turno);
    }

    // REGISTRAR MESA
    public Mesa registrarMesa(int numero, int capacidad) {

        if (numero <= 0 || capacidad <= 0) {
            return null;
        }

        // Verificar que el número no se repita
        for (int i = 0; i < mesas.size(); i++) {

            Mesa mesa = mesas.get(i);

            if (mesa.getNumero() == numero) {
                return null;
            }
        }

        ultimoCodigoMesa++;

        String codigo = String.format(
                "%04d", ultimoCodigoMesa);

        Mesa nuevaMesa = new Mesa(
                codigo, numero, capacidad);

        // Uso del método genérico
        agregarVarios(mesas, nuevaMesa);

        // Guardar información
        archivos.guardarCodigos(
                ultimoCodigoMesa, ultimoCodigoReserva);

        archivos.guardarMesas(mesas);

        return nuevaMesa;
    }

    // BUSCAR MESA POR CÓDIGO
    public Mesa buscarMesa(String codigo) {

        if (codigo == null) {
            return null;
        }

        for (int i = 0; i < mesas.size(); i++) {

            Mesa mesa = mesas.get(i);

            if (mesa.getCodigo().equals(codigo)) {
                return mesa;
            }
        }

        return null;
    }

    // VERIFICAR DISPONIBILIDAD
    public boolean mesaDisponible(
            String codigoMesa, String fecha, String turno) {

        // Comprobar que los datos sean válidos
        if (buscarMesa(codigoMesa) == null
                || !fechaValida(fecha)
                || !turnoValido(turno)) {

            return false;
        }

        for (int i = 0; i < reservas.size(); i++) {

            Reserva reserva = reservas.get(i);

            if (reserva.getCodigoMesa().equals(codigoMesa)
                    && reserva.getFecha().equals(fecha)
                    && reserva.getTurno().equals(turno)
                    && reserva.getEstado().equals("Activa")) {

                return false;
            }
        }

        return true;
    }

    // REGISTRAR RESERVA
    public Reserva registrarReserva(
            String codigoMesa,
            String cliente,
            String fecha,
            String turno,
            int personas) {

        if (!textoValido(cliente)
                || !fechaValida(fecha)
                || !turnoValido(turno)
                || personas <= 0) {

            return null;
        }

        // Buscar la mesa seleccionada
        Mesa mesa = buscarMesa(codigoMesa);

        if (mesa == null) {
            return null;
        }

        // Verificar la capacidad de la mesa
        if (personas > mesa.getCapacidad()) {
            return null;
        }

        // Verificar que esté disponible
        if (!mesaDisponible(codigoMesa, fecha, turno)) {
            return null;
        }

        ultimoCodigoReserva++;

        String codigo = String.format(
                "%04d", ultimoCodigoReserva);

        Reserva nuevaReserva = new Reserva(
                codigo,
                codigoMesa,
                cliente.trim(),
                fecha,
                turno,
                personas,
                "Activa");

        // Agregar mediante el método genérico
        agregarVarios(reservas, nuevaReserva);

        // Guardar los cambios
        archivos.guardarCodigos(
                ultimoCodigoMesa, ultimoCodigoReserva);

        archivos.guardarReservas(reservas);

        return nuevaReserva;
    }

    // BUSCAR RESERVA POR CÓDIGO
    public Reserva buscarReserva(String codigo) {

        if (codigo == null) {
            return null;
        }

        for (int i = 0; i < reservas.size(); i++) {

            Reserva reserva = reservas.get(i);

            if (reserva.getCodigo().equals(codigo)) {
                return reserva;
            }
        }

        return null;
    }

    // CANCELAR RESERVA
    public boolean cancelarReserva(String codigo) {

        Reserva reserva = buscarReserva(codigo);

        if (reserva == null) {
            return false;
        }

        // No cancelar dos veces
        if (reserva.getEstado().equals("Cancelada")) {
            return false;
        }

        reserva.setEstado("Cancelada");

        archivos.guardarReservas(reservas);

        return true;
    }

    // OBTENER MESAS DISPONIBLES
    public ArrayList<Mesa> obtenerMesasDisponibles(
            String fecha, String turno) {

        ArrayList<Mesa> disponibles =
                new ArrayList<Mesa>();

        if (!fechaValida(fecha) || !turnoValido(turno)) {
            return disponibles;
        }

        for (int i = 0; i < mesas.size(); i++) {

            Mesa mesa = mesas.get(i);

            if (mesaDisponible(
                    mesa.getCodigo(), fecha, turno)) {

                disponibles.add(mesa);
            }
        }

        return disponibles;
    }

    // CONTAR MESAS DISPONIBLES
    public int contarMesasDisponibles(
            String fecha, String turno) {

        return obtenerMesasDisponibles(
                fecha, turno).size();
    }

    // CONTAR RESERVAS ACTIVAS
    public int contarReservasActivas(
            String fecha, String turno) {

        int contador = 0;

        for (int i = 0; i < reservas.size(); i++) {

            Reserva reserva = reservas.get(i);

            if (reserva.getFecha().equals(fecha)
                    && reserva.getTurno().equals(turno)
                    && reserva.getEstado().equals("Activa")) {

                contador++;
            }
        }

        return contador;
    }

    // CONTAR RESERVAS CANCELADAS
    public int contarReservasCanceladas(
            String fecha, String turno) {

        int contador = 0;

        for (int i = 0; i < reservas.size(); i++) {

            Reserva reserva = reservas.get(i);

            if (reserva.getFecha().equals(fecha)
                    && reserva.getTurno().equals(turno)
                    && reserva.getEstado().equals("Cancelada")) {

                contador++;
            }
        }

        return contador;
    }

    // OBTENER RESERVAS DE UN TURNO
    public ArrayList<Reserva> obtenerReservasPorTurno(
            String fecha, String turno) {

        ArrayList<Reserva> resultado =
                new ArrayList<Reserva>();

        for (int i = 0; i < reservas.size(); i++) {

            Reserva reserva = reservas.get(i);

            if (reserva.getFecha().equals(fecha)
                    && reserva.getTurno().equals(turno)) {

                resultado.add(reserva);
            }
        }

        return resultado;
    }

    // GETTERS
    public ArrayList<Mesa> getMesas() {
        return mesas;
    }

    public ArrayList<Reserva> getReservas() {
        return reservas;
    }

    // TOTAL DE MESAS REGISTRADAS
    public int contarMesas() {
        return mesas.size();
    }
}

