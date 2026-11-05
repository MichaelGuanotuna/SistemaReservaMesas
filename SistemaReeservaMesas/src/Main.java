import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controlador.ControladorPrincipal;
import servicio.GestorReservas;
import vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                try {

                    // Crear el gestor del sistema
                    GestorReservas gestor =
                            new GestorReservas();

                    // Crear la interfaz gráfica
                    VentanaPrincipal ventana =
                            new VentanaPrincipal();

                    // Conectar la vista con el gestor
                    new ControladorPrincipal(ventana, gestor);

                    // Mostrar la ventana
                    ventana.setVisible(true);

                } catch (IllegalStateException e) {

                    JOptionPane.showMessageDialog(
                            null,
                            e.getMessage(),
                            "Error al iniciar el sistema",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
