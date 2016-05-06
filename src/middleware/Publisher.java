package middleware;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Publisher {
	
	public static void main(String[] args) throws IOException {
		
		int port = 6868;
		String ipMulticast = "225.4.5.6";
		
		InetAddress grupoMulticast = InetAddress.getByName(ipMulticast);		
		MulticastSocket conexion = new MulticastSocket();
		
		String mensaje = "Prueba multicast";
		byte [] datos = mensaje.getBytes();		
		int lenght = datos.length;

		DatagramPacket paquete = new DatagramPacket(datos, lenght,grupoMulticast , port);
		conexion.send(paquete);

		conexion.close();
	}
}
