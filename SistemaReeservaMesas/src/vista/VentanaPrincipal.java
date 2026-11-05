package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Mesa;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    // Panel principal
    private JPanel contentPane;
    private JTabbedPane pestanas;

    // Componentes de inicio
    private JTextField txtFechaInicio;
    private JComboBox<String> cmbTurnoInicio;
    private JButton btnConsultarResumen;

    private JLabel lblTotalMesas;
    private JLabel lblMesasDisponibles;
    private JLabel lblReservasActivas;
    private JLabel lblReservasCanceladas;

    // Componentes de mesas
    private JTextField txtCodigoMesa;
    private JTextField txtNumeroMesa;
    private JTextField txtCapacidad;

    private JButton btnRegistrarMesa;
    private JButton btnLimpiarMesa;

    private JTable tablaMesas;
    private DefaultTableModel modeloMesas;

    // Componentes de reservas
    private JTextField txtCliente;
    private JTextField txtFechaReserva;
    private JTextField txtPersonas;

    private JComboBox<String> cmbTurnoReserva;
    private JComboBox<Mesa> cmbMesaReserva;

    private JButton btnConsultarDisponibilidad;
    private JButton btnRegistrarReserva;
    private JButton btnCancelarReserva;
    private JButton btnLimpiarReserva;

    private JTable tablaReservas;
    private DefaultTableModel modeloReservas;

    private JLabel lblDisponibilidad;

    // Constructor
    public VentanaPrincipal() {

        setTitle("Sistema de Gestión de Reservas de Mesas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(100, 100, 950, 650);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(
                new EmptyBorder(10, 10, 10, 10));

        contentPane.setLayout(new BorderLayout());

        setContentPane(contentPane);

        pestanas = new JTabbedPane();
        contentPane.add(pestanas, BorderLayout.CENTER);

        crearPanelInicio();
        crearPanelMesas();
        crearPanelReservas();
    }

    // PESTAÑA INICIO
    private void crearPanelInicio() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel(
                "CONTROL DE RESERVACIONES",
                JLabel.CENTER);

        panel.add(lblTitulo, BorderLayout.NORTH);

        // Panel central
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridLayout(2, 1, 10, 10));

        // Seleccionar fecha y turno
        JPanel panelFiltros = new JPanel();
        panelFiltros.setLayout(new GridLayout(3, 2, 10, 10));

        txtFechaInicio = new JTextField();

        cmbTurnoInicio = new JComboBox<String>();
        cmbTurnoInicio.addItem("12:00");
        cmbTurnoInicio.addItem("15:00");
        cmbTurnoInicio.addItem("19:00");

        btnConsultarResumen = new JButton("Consultar resumen");

        panelFiltros.add(new JLabel("Fecha (AAAA-MM-DD):"));
        panelFiltros.add(txtFechaInicio);

        panelFiltros.add(new JLabel("Turno:"));
        panelFiltros.add(cmbTurnoInicio);

        panelFiltros.add(new JLabel(""));
        panelFiltros.add(btnConsultarResumen);

        panelCentral.add(panelFiltros);

        // Resumen de ocupación
        JPanel panelResumen = new JPanel();
        panelResumen.setLayout(new GridLayout(4, 1, 10, 10));

        lblTotalMesas = new JLabel("Total de mesas: 0");
        lblMesasDisponibles = new JLabel("Mesas disponibles: 0");
        lblReservasActivas = new JLabel("Reservas activas: 0");
        lblReservasCanceladas = new JLabel("Reservas canceladas: 0");

        panelResumen.add(lblTotalMesas);
        panelResumen.add(lblMesasDisponibles);
        panelResumen.add(lblReservasActivas);
        panelResumen.add(lblReservasCanceladas);

        panelCentral.add(panelResumen);

        panel.add(panelCentral, BorderLayout.CENTER);

        pestanas.addTab("Inicio", panel);
    }

    // PESTAÑA MESAS
    private void crearPanelMesas() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Formulario para registrar mesas
        JPanel formulario = new JPanel();
        formulario.setLayout(new GridLayout(3, 2, 10, 10));

        txtCodigoMesa = new JTextField();
        txtCodigoMesa.setEditable(false);

        txtNumeroMesa = new JTextField();
        txtCapacidad = new JTextField();

        formulario.add(new JLabel("Código automático:"));
        formulario.add(txtCodigoMesa);

        formulario.add(new JLabel("Número de mesa:"));
        formulario.add(txtNumeroMesa);

        formulario.add(new JLabel("Capacidad de personas:"));
        formulario.add(txtCapacidad);

        panel.add(formulario, BorderLayout.NORTH);

        // Tabla de mesas
        String[] columnas = {
            "Código", "Número", "Capacidad"
        };

        modeloMesas = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaMesas = new JTable(modeloMesas);

        panel.add(new JScrollPane(tablaMesas),
                BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel();

        btnRegistrarMesa = new JButton("Registrar mesa");
        btnLimpiarMesa = new JButton("Limpiar");

        botones.add(btnRegistrarMesa);
        botones.add(btnLimpiarMesa);

        panel.add(botones, BorderLayout.SOUTH);

        pestanas.addTab("Mesas", panel);
    }

    // PESTAÑA RESERVAS
    private void crearPanelReservas() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));

        // Parte superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BorderLayout(10, 10));

        // Formulario
        JPanel formulario = new JPanel();
        formulario.setLayout(new GridLayout(5, 2, 10, 10));

        txtCliente = new JTextField();
        txtFechaReserva = new JTextField();
        txtPersonas = new JTextField();

        cmbTurnoReserva = new JComboBox<String>();
        cmbTurnoReserva.addItem("12:00");
        cmbTurnoReserva.addItem("15:00");
        cmbTurnoReserva.addItem("19:00");

        cmbMesaReserva = new JComboBox<Mesa>();

        formulario.add(new JLabel("Cliente:"));
        formulario.add(txtCliente);

        formulario.add(new JLabel("Fecha (AAAA-MM-DD):"));
        formulario.add(txtFechaReserva);

        formulario.add(new JLabel("Turno:"));
        formulario.add(cmbTurnoReserva);

        formulario.add(new JLabel("Cantidad de personas:"));
        formulario.add(txtPersonas);

        formulario.add(new JLabel("Mesa:"));
        formulario.add(cmbMesaReserva);

        panelSuperior.add(formulario, BorderLayout.CENTER);

        // Panel de disponibilidad
        JPanel panelDisponibilidad = new JPanel();
        panelDisponibilidad.setLayout(
                new GridLayout(2, 1, 5, 5));

        btnConsultarDisponibilidad =
                new JButton("Consultar disponibilidad");

        lblDisponibilidad = new JLabel(
                "Seleccione una fecha y turno para consultar mesas.");

        panelDisponibilidad.add(btnConsultarDisponibilidad);
        panelDisponibilidad.add(lblDisponibilidad);

        panelSuperior.add(panelDisponibilidad,
                BorderLayout.SOUTH);

        panel.add(panelSuperior, BorderLayout.NORTH);

        // Tabla de reservaciones
        String[] columnas = {
            "Código", "Mesa", "Cliente",
            "Fecha", "Turno", "Personas", "Estado"
        };

        modeloReservas = new DefaultTableModel(columnas, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaReservas = new JTable(modeloReservas);

        panel.add(new JScrollPane(tablaReservas),
                BorderLayout.CENTER);

        // Botones
        JPanel botones = new JPanel();

        btnRegistrarReserva = new JButton("Registrar reserva");
        btnCancelarReserva = new JButton("Cancelar reserva");
        btnLimpiarReserva = new JButton("Limpiar");

        botones.add(btnRegistrarReserva);
        botones.add(btnCancelarReserva);
        botones.add(btnLimpiarReserva);

        panel.add(botones, BorderLayout.SOUTH);

        pestanas.addTab("Reservas", panel);
    }

    // GETTERS DE INICIO

    public JTextField getTxtFechaInicio() {
        return txtFechaInicio;
    }

    public JComboBox<String> getCmbTurnoInicio() {
        return cmbTurnoInicio;
    }

    public JButton getBtnConsultarResumen() {
        return btnConsultarResumen;
    }

    public JLabel getLblTotalMesas() {
        return lblTotalMesas;
    }

    public JLabel getLblMesasDisponibles() {
        return lblMesasDisponibles;
    }

    public JLabel getLblReservasActivas() {
        return lblReservasActivas;
    }

    public JLabel getLblReservasCanceladas() {
        return lblReservasCanceladas;
    }

    // GETTERS DE MESAS

    public JTextField getTxtCodigoMesa() {
        return txtCodigoMesa;
    }

    public JTextField getTxtNumeroMesa() {
        return txtNumeroMesa;
    }

    public JTextField getTxtCapacidad() {
        return txtCapacidad;
    }

    public JButton getBtnRegistrarMesa() {
        return btnRegistrarMesa;
    }

    public JButton getBtnLimpiarMesa() {
        return btnLimpiarMesa;
    }

    public JTable getTablaMesas() {
        return tablaMesas;
    }

    public DefaultTableModel getModeloMesas() {
        return modeloMesas;
    }

    // GETTERS DE RESERVAS

    public JTextField getTxtCliente() {
        return txtCliente;
    }

    public JTextField getTxtFechaReserva() {
        return txtFechaReserva;
    }

    public JTextField getTxtPersonas() {
        return txtPersonas;
    }

    public JComboBox<String> getCmbTurnoReserva() {
        return cmbTurnoReserva;
    }

    public JComboBox<Mesa> getCmbMesaReserva() {
        return cmbMesaReserva;
    }

    public JButton getBtnConsultarDisponibilidad() {
        return btnConsultarDisponibilidad;
    }

    public JButton getBtnRegistrarReserva() {
        return btnRegistrarReserva;
    }

    public JButton getBtnCancelarReserva() {
        return btnCancelarReserva;
    }

    public JButton getBtnLimpiarReserva() {
        return btnLimpiarReserva;
    }

    public JTable getTablaReservas() {
        return tablaReservas;
    }

    public DefaultTableModel getModeloReservas() {
        return modeloReservas;
    }

    public JLabel getLblDisponibilidad() {
        return lblDisponibilidad;
    }

    public JTabbedPane getPestanas() {
        return pestanas;
    }
}
