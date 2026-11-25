import java.util.Random;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class Servidor {

    public Random n_random;
    public int numero;

    public Servidor(Random n_random, int numero) {
        this.n_random = n_random;
        this.numero = numero;
    }

    public void num() {
        try (DatagramSocket dt = new DatagramSocket(6666)) {
            byte[] buffer = new byte[1024];
            numero = n_random.nextInt(100) + 1;
            int cuenta = 0;

            while (true) {
                DatagramPacket td = new DatagramPacket(buffer, buffer.length);
                dt.receive(td);
                String texto = new String(td.getData(), 0, td.getLength(), StandardCharsets.UTF_8);

                if (texto.trim().equalsIgnoreCase("ADIOS")) {
                    String despedida = "OK Hasta luego";
                    System.out.println(despedida);
                    byte[] respuesta = despedida.getBytes(StandardCharsets.UTF_8);
                    dt.send(new DatagramPacket(respuesta, respuesta.length, td.getAddress(), td.getPort()));
                    break;
                }

                int adivinar = 0;
                try {
                    adivinar = Integer.parseInt(texto.trim());
                } catch (NumberFormatException e) {
                    String error = "ERR Comando no válido";
                    System.out.println(error);
                    byte[] respuesta = error.getBytes(StandardCharsets.UTF_8);
                    dt.send(new DatagramPacket(respuesta, respuesta.length, td.getAddress(), td.getPort()));
                    break;
                }



                String mensaje = "";

                if (adivinar > numero) {
                    cuenta++;
                    mensaje = "Es mayor que " + adivinar + " te quedan " + (3-cuenta) + " intentos";
                }

                else if (adivinar < numero) {
                    cuenta++;
                    mensaje = "Es menor que " + adivinar + " te quedan " + (3-cuenta) + " intentos";
                }

                else if (adivinar == numero) {
                    cuenta++;
                    mensaje = "Adivinaste en: " + cuenta + " intentos";
                }


                if (cuenta == 3 && adivinar != numero) {
                    mensaje = "PERDISTE";
                }

                System.out.println(mensaje);
                byte[] respuesta = mensaje.getBytes(StandardCharsets.UTF_8);


                dt.send(new DatagramPacket(respuesta, respuesta.length, td.getAddress(), td.getPort()));

                if (adivinar == numero || cuenta == 3) {
                    break;
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
