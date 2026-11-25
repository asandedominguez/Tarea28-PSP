import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        try (DatagramSocket st = new DatagramSocket()) {

            for (int i = 0; i < 3; i++) {

                Scanner scanner = new Scanner(System.in);
                System.out.println("Dame un número para intentar adivinar: ");
                String intento = scanner.nextLine();

                try {
                    InetAddress server = InetAddress.getByName("localhost");
                    byte[] datos = intento.getBytes(StandardCharsets.UTF_8);
                    st.send(new DatagramPacket(datos, datos.length, server, 6666));

                    byte[] espacio = new byte[1024];
                    DatagramPacket respuesta = new DatagramPacket(espacio, espacio.length);

                    try {
                        st.receive(respuesta);
                        String mensaje = new String(espacio, 0, respuesta.getLength(), StandardCharsets.UTF_8);
                        System.out.println(mensaje);
                        if (mensaje.startsWith("Adivinaste")) {
                            break;
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());

                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
