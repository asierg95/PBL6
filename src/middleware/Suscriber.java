package middleware;

import java.io.IOException;
import java.net.*;

public class Suscriber {

	public static void main(String[] args) throws IOException {
		boolean exit = false;
		int port = 6868;
		String ipMulticast = "225.4.5.6";
		MulticastSocket conexion = new MulticastSocket(port);
		InetAddress grupoMulticast = InetAddress.getByName(ipMulticast);

		conexion.joinGroup(grupoMulticast);
		
		byte datos[] = new byte[20];
		DatagramPacket paquete = new DatagramPacket(datos, datos.length);
		String dato;

		while(!exit){
			conexion.receive(paquete);
			dato = new String (paquete.getData(), "UTF-8");
			System.out.println("Dato: " +dato +" longitud meirda:"+dato.length() + "IP: "+paquete.getAddress());
		}
		
		conexion.leaveGroup(grupoMulticast);
		conexion.close();
	}

}
