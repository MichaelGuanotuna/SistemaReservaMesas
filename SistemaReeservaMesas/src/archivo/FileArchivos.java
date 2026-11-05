package archivo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.Mesa;
import modelo.Reserva;

public class FileArchivos {

    // Carpeta y archivos
    private File carpeta;
    private File archivoMesas;
    private File archivoReservas;
    private File archivoCodigos;

    // Constructor
    public FileArchivos() {

        carpeta = new File("datos");

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        archivoMesas = new File(carpeta, "mesas.txt");
        archivoReservas = new File(carpeta, "reservas.txt");
        archivoCodigos = new File(carpeta, "codigos.txt");
    }

    // GUARDAR MESAS
    public void guardarMesas(ArrayList<Mesa> mesas) {

        try (BufferedWriter bw =
                new BufferedWriter(new FileWriter(archivoMesas))) {

            for (int i = 0; i < mesas.size(); i++) {

                Mesa mesa = mesas.get(i);

                bw.write(mesa.getCodigo() + "|"
                        + mesa.getNumero() + "|"
                        + mesa.getCapacidad());

                bw.newLine();
            }

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Error al guardar las mesas.", e);
        }
    }

    // CARGAR MESAS
    public ArrayList<Mesa> cargarMesas() {

        ArrayList<Mesa> mesas = new ArrayList<Mesa>();

        if (!archivoMesas.exists()) {
            return mesas;
        }

        try (BufferedReader br =
                new BufferedReader(new FileReader(archivoMesas))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split("\\|", -1);

                if (datos.length == 3) {

                    String codigo = datos[0];

                    int numero = Integer.parseInt(datos[1]);
                    int capacidad = Integer.parseInt(datos[2]);

                    Mesa mesa = new Mesa(
                            codigo, numero, capacidad);

                    mesas.add(mesa);
                }
            }

        } catch (IOException | NumberFormatException e) {

            throw new IllegalStateException(
                    "Error al cargar las mesas.", e);
        }

        return mesas;
    }

    // GUARDAR RESERVAS
    public void guardarReservas(ArrayList<Reserva> reservas) {

        try (BufferedWriter bw =
                new BufferedWriter(new FileWriter(archivoReservas))) {

            for (int i = 0; i < reservas.size(); i++) {

                Reserva reserva = reservas.get(i);

                bw.write(reserva.getCodigo() + "|"
                        + reserva.getCodigoMesa() + "|"
                        + reserva.getCliente() + "|"
                        + reserva.getFecha() + "|"
                        + reserva.getTurno() + "|"
                        + reserva.getPersonas() + "|"
                        + reserva.getEstado());

                bw.newLine();
            }

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Error al guardar las reservas.", e);
        }
    }

    // CARGAR RESERVAS
    public ArrayList<Reserva> cargarReservas() {

        ArrayList<Reserva> reservas = new ArrayList<Reserva>();

        if (!archivoReservas.exists()) {
            return reservas;
        }

        try (BufferedReader br =
                new BufferedReader(new FileReader(archivoReservas))) {

            String linea;

            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split("\\|", -1);

                if (datos.length == 7) {

                    String codigo = datos[0];
                    String codigoMesa = datos[1];
                    String cliente = datos[2];
                    String fecha = datos[3];
                    String turno = datos[4];

                    int personas = Integer.parseInt(datos[5]);

                    String estado = datos[6];

                    Reserva reserva = new Reserva(
                            codigo,
                            codigoMesa,
                            cliente,
                            fecha,
                            turno,
                            personas,
                            estado);

                    reservas.add(reserva);
                }
            }

        } catch (IOException | NumberFormatException e) {

            throw new IllegalStateException(
                    "Error al cargar las reservas.", e);
        }

        return reservas;
    }

    // GUARDAR ÚLTIMOS CÓDIGOS
    public void guardarCodigos(
            int ultimoCodigoMesa, int ultimoCodigoReserva) {

        try (BufferedWriter bw =
                new BufferedWriter(new FileWriter(archivoCodigos))) {

            bw.write(String.valueOf(ultimoCodigoMesa));
            bw.newLine();

            bw.write(String.valueOf(ultimoCodigoReserva));
            bw.newLine();

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Error al guardar los códigos.", e);
        }
    }

    // CARGAR ÚLTIMOS CÓDIGOS
    public int[] cargarCodigos() {

        int[] codigos = {0, 0};

        if (!archivoCodigos.exists()) {
            return codigos;
        }

        try (BufferedReader br =
                new BufferedReader(new FileReader(archivoCodigos))) {

            String lineaMesa = br.readLine();
            String lineaReserva = br.readLine();

            if (lineaMesa != null) {
                codigos[0] = Integer.parseInt(lineaMesa);
            }

            if (lineaReserva != null) {
                codigos[1] = Integer.parseInt(lineaReserva);
            }

        } catch (IOException | NumberFormatException e) {

            throw new IllegalStateException(
                    "Error al cargar los códigos.", e);
        }

        return codigos;
    }
}
