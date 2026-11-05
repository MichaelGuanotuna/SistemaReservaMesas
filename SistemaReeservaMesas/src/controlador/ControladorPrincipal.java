package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import modelo.Mesa;
import modelo.Reserva;
import servicio.GestorReservas;
import vista.VentanaPrincipal;

public class ControladorPrincipal
        implements ActionListener, ListSelectionListener {

    // Atributos
    private VentanaPrincipal ventana;
    private GestorReservas gestor;

    // Constructor
    public ControladorPrincipal(
            VentanaPrincipal ventana,
            GestorReservas gestor) {

        this.ventana = ventana;
        this.gestor = gestor;

        // Eventos de inicio
        ventana.getBtnConsultarResumen()
                .addActionListener(this);

        // Eventos de mesas
        ventana.getBtnRegistrarMesa()
                .addActionListener(this);

        ventana.getBtnLimpiarMesa()
                .addActionListener(this);

        // Eventos de reservas
        ventana.getBtnConsultarDisponibilidad()
                .addActionListener(this);

        ventana.getBtnRegistrarReserva()
                .addActionListener(this);

        ventana.getBtnCancelarReserva()
                .addActionListener(this);

        ventana.getBtnLimpiarReserva()
                .addActionListener(this);

        // Evento de selección de reservaciones
        ventana.getTablaReservas()
                .getSelectionModel()
                .addListSelectionListener(this);

        // Cargar información inicial
        actualizarTablaMesas();
        actualizarTablaReservas();
    }

    // MODELO DELEGADO DE EVENTOS
    @Override
    public void actionPerformed(ActionEvent e) {

        Object origen = e.getSource();

        if (origen == ventana.getBtnConsultarResumen()) {

            consultarResumen();

        } else if (origen == ventana.getBtnRegistrarMesa()) {

            registrarMesa();

        } else if (origen == ventana.getBtnLimpiarMesa()) {

            limpiarMesa();

        } else if (origen ==
                ventana.getBtnConsultarDisponibilidad()) {

            consultarDisponibilidad();

        } else if (origen == ventana.getBtnRegistrarReserva()) {

            registrarReserva();

        } else if (origen == ventana.getBtnCancelarReserva()) {

            cancelarReserva();

        } else if (origen == ventana.getBtnLimpiarReserva()) {

            limpiarReserva();
        }
    }

    // VALIDAR FORMATO DE FECHA
    private boolean fechaValida(String fecha) {

        return fecha != null
                && fecha.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // REGISTRAR MESA
    private void registrarMesa() {

        String textoNumero = ventana.getTxtNumeroMesa()
                .getText().trim();

        String textoCapacidad = ventana.getTxtCapacidad()
                .getText().trim();

        if (textoNumero.isEmpty()
                || textoCapacidad.isEmpty()) {

            JOptionPane.showMessageDialog(ventana,
                    "Complete los datos de la mesa.");
            return;
        }

        try {

            int numero = Integer.parseInt(textoNumero);
            int capacidad = Integer.parseInt(textoCapacidad);

            Mesa mesa = gestor.registrarMesa(
                    numero, capacidad);

            if (mesa != null) {

                JOptionPane.showMessageDialog(ventana,
                        "Mesa registrada correctamente.\n"
                        + "Código: " + mesa.getCodigo());

                actualizarTablaMesas();
                limpiarMesa();

            } else {

                JOptionPane.showMessageDialog(ventana,
                        "No se pudo registrar la mesa.\n"
                        + "Verifique el número y la capacidad.\n"
                        + "El número de mesa no debe repetirse.");
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(ventana,
                    "El número y la capacidad deben ser enteros.");
        }
    }

    // ACTUALIZAR TABLA DE MESAS
    private void actualizarTablaMesas() {

        DefaultTableModel modelo =
                ventana.getModeloMesas();

        modelo.setRowCount(0);

        ArrayList<Mesa> mesas = gestor.getMesas();

        for (int i = 0; i < mesas.size(); i++) {

            Mesa mesa = mesas.get(i);

            Object[] fila = {
                mesa.getCodigo(),
                mesa.getNumero(),
                mesa.getCapacidad()
            };

            modelo.addRow(fila);
        }
    }

    // LIMPIAR FORMULARIO DE MESAS
    private void limpiarMesa() {

        ventana.getTxtCodigoMesa().setText("");
        ventana.getTxtNumeroMesa().setText("");
        ventana.getTxtCapacidad().setText("");

        ventana.getTablaMesas().clearSelection();

        ventana.getTxtNumeroMesa().requestFocus();
    }

    // CONSULTAR DISPONIBILIDAD DE MESAS
    private void consultarDisponibilidad() {

        String fecha = ventana.getTxtFechaReserva()
                .getText().trim();

        String turno = (String) ventana
                .getCmbTurnoReserva().getSelectedItem();

        if (!fechaValida(fecha)) {

            JOptionPane.showMessageDialog(ventana,
                    "Ingrese una fecha con formato AAAA-MM-DD.");
            return;
        }

        // Obtener mesas disponibles
        ArrayList<Mesa> disponibles =
                gestor.obtenerMesasDisponibles(
                        fecha, turno);

        // Limpiar el JComboBox
        ventana.getCmbMesaReserva().removeAllItems();

        // Agregar las mesas disponibles
        for (int i = 0; i < disponibles.size(); i++) {

            Mesa mesa = disponibles.get(i);

            ventana.getCmbMesaReserva().addItem(mesa);
        }

        // No seleccionar una mesa automáticamente
        ventana.getCmbMesaReserva().setSelectedIndex(-1);

        // Mostrar cantidad disponible
        int cantidad = disponibles.size();

        ventana.getLblDisponibilidad().setText(
                "Mesas disponibles: " + cantidad);

        if (cantidad == 0) {

            JOptionPane.showMessageDialog(ventana,
                    "No existen mesas disponibles "
                    + "para la fecha y turno seleccionados.");
        }
    }

    // REGISTRAR RESERVA
    private void registrarReserva() {

        String cliente = ventana.getTxtCliente()
                .getText().trim();

        String fecha = ventana.getTxtFechaReserva()
                .getText().trim();

        String turno = (String) ventana
                .getCmbTurnoReserva().getSelectedItem();

        String textoPersonas = ventana.getTxtPersonas()
                .getText().trim();

        Mesa mesa = (Mesa) ventana
                .getCmbMesaReserva().getSelectedItem();

        if (cliente.isEmpty()
                || fecha.isEmpty()
                || textoPersonas.isEmpty()) {

            JOptionPane.showMessageDialog(ventana,
                    "Complete todos los campos de la reserva.");
            return;
        }

        if (!fechaValida(fecha)) {

            JOptionPane.showMessageDialog(ventana,
                    "La fecha debe tener formato AAAA-MM-DD.");
            return;
        }

        if (mesa == null) {

            JOptionPane.showMessageDialog(ventana,
                    "Consulte la disponibilidad y "
                    + "seleccione una mesa.");
            return;
        }

        try {

            int personas = Integer.parseInt(textoPersonas);

            // Registrar mediante el gestor
            Reserva reserva = gestor.registrarReserva(
                    mesa.getCodigo(),
                    cliente,
                    fecha,
                    turno,
                    personas);

            if (reserva != null) {

                JOptionPane.showMessageDialog(ventana,
                        "Reservación registrada correctamente.\n"
                        + "Código: " + reserva.getCodigo());

                actualizarTablaReservas();
                limpiarReserva();

            } else {

                JOptionPane.showMessageDialog(ventana,
                        "No se pudo registrar la reserva.\n"
                        + "Verifique la capacidad, "
                        + "los datos y la disponibilidad.");

                // Actualizar la lista de mesas
                consultarDisponibilidad();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(ventana,
                    "La cantidad de personas "
                    + "debe ser un número entero.");
        }
    }

    // ACTUALIZAR TABLA DE RESERVAS
    private void actualizarTablaReservas() {

        DefaultTableModel modelo =
                ventana.getModeloReservas();

        modelo.setRowCount(0);

        ArrayList<Reserva> reservas =
                gestor.getReservas();

        for (int i = 0; i < reservas.size(); i++) {

            Reserva reserva = reservas.get(i);

            Mesa mesa = gestor.buscarMesa(
                    reserva.getCodigoMesa());

            String numeroMesa = reserva.getCodigoMesa();

            if (mesa != null) {
                numeroMesa = "Mesa " + mesa.getNumero();
            }

            Object[] fila = {
                reserva.getCodigo(),
                numeroMesa,
                reserva.getCliente(),
                reserva.getFecha(),
                reserva.getTurno(),
                reserva.getPersonas(),
                reserva.getEstado()
            };

            modelo.addRow(fila);
        }
    }

    // CANCELAR RESERVA
    private void cancelarReserva() {

        int fila = ventana.getTablaReservas()
                .getSelectedRow();

        if (fila < 0) {

            JOptionPane.showMessageDialog(ventana,
                    "Seleccione una reserva de la tabla.");
            return;
        }

        // Obtener la fila del modelo
        int filaModelo = ventana.getTablaReservas()
                .convertRowIndexToModel(fila);

        String codigo = ventana.getModeloReservas()
                .getValueAt(filaModelo, 0).toString();

        Reserva reserva = gestor.buscarReserva(codigo);

        if (reserva == null) {

            JOptionPane.showMessageDialog(ventana,
                    "No se encontró la reserva.");
            return;
        }

        if (reserva.getEstado().equals("Cancelada")) {

            JOptionPane.showMessageDialog(ventana,
                    "Esta reserva ya está cancelada.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                ventana,
                "¿Desea cancelar la reserva "
                + codigo + "?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {

            boolean cancelada =
                    gestor.cancelarReserva(codigo);

            if (cancelada) {

                JOptionPane.showMessageDialog(ventana,
                        "Reserva cancelada correctamente.");

                actualizarTablaReservas();

                // Limpiar mesas consultadas anteriormente
                ventana.getCmbMesaReserva().removeAllItems();

                ventana.getLblDisponibilidad().setText(
                        "Consulte nuevamente la disponibilidad.");

            } else {

                JOptionPane.showMessageDialog(ventana,
                        "No se pudo cancelar la reserva.");
            }
        }
    }

    // CONSULTAR RESUMEN DE OCUPACIÓN
    private void consultarResumen() {

        String fecha = ventana.getTxtFechaInicio()
                .getText().trim();

        String turno = (String) ventana
                .getCmbTurnoInicio().getSelectedItem();

        if (!fechaValida(fecha)) {

            JOptionPane.showMessageDialog(ventana,
                    "Ingrese una fecha con formato AAAA-MM-DD.");
            return;
        }

        int totalMesas = gestor.contarMesas();

        int disponibles =
                gestor.contarMesasDisponibles(fecha, turno);

        int activas =
                gestor.contarReservasActivas(fecha, turno);

        int canceladas =
                gestor.contarReservasCanceladas(fecha, turno);

        // Actualizar etiquetas de inicio
        ventana.getLblTotalMesas().setText(
                "Total de mesas: " + totalMesas);

        ventana.getLblMesasDisponibles().setText(
                "Mesas disponibles: " + disponibles);

        ventana.getLblReservasActivas().setText(
                "Reservas activas: " + activas);

        ventana.getLblReservasCanceladas().setText(
                "Reservas canceladas: " + canceladas);
    }

    // LIMPIAR FORMULARIO DE RESERVAS
    private void limpiarReserva() {

        ventana.getTxtCliente().setText("");
        ventana.getTxtFechaReserva().setText("");
        ventana.getTxtPersonas().setText("");

        ventana.getCmbTurnoReserva().setSelectedIndex(0);
        ventana.getCmbMesaReserva().removeAllItems();

        ventana.getLblDisponibilidad().setText(
                "Seleccione una fecha y turno "
                + "para consultar mesas.");

        ventana.getTablaReservas().clearSelection();

        ventana.getTxtCliente().requestFocus();
    }

    // EVENTO DE SELECCIÓN EN JTABLE
    @Override
    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting()) {
            return;
        }

        int fila = ventana.getTablaReservas()
                .getSelectedRow();

        if (fila >= 0) {

            int filaModelo = ventana.getTablaReservas()
                    .convertRowIndexToModel(fila);

            String codigo = ventana.getModeloReservas()
                    .getValueAt(filaModelo, 0).toString();

            Reserva reserva = gestor.buscarReserva(codigo);

            if (reserva != null) {

                // Mostrar los datos de la reserva seleccionada
                ventana.getTxtCliente().setText(
                        reserva.getCliente());

                ventana.getTxtFechaReserva().setText(
                        reserva.getFecha());

                ventana.getTxtPersonas().setText(
                        String.valueOf(reserva.getPersonas()));

                ventana.getCmbTurnoReserva().setSelectedItem(
                        reserva.getTurno());

                // Mostrar la mesa relacionada
                Mesa mesa = gestor.buscarMesa(
                        reserva.getCodigoMesa());

                ventana.getCmbMesaReserva().removeAllItems();

                if (mesa != null) {
                    ventana.getCmbMesaReserva().addItem(mesa);
                }

                ventana.getLblDisponibilidad().setText(
                        "Reserva seleccionada: "
                        + reserva.getEstado());
            }
        }
    }
}

