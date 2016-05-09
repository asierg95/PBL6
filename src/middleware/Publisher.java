package middleware;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Publisher {
	
	public static void main(String[] args) throws IOException {
		
		String fichConfig = "middleware.conf", mensaje = "456";
		PsPortFactory pFactory = new PsPortFactory();
		PsPort pPort;
		
		pPort = pFactory.getPort(fichConfig);
		
		pPort.start();
		if(pPort.publish(1, mensaje.getBytes(), 8)){
			//System.out.println("Mensaje enviado correctamente");
		}else{
			System.out.println("Error al enviar");
		};
	}
}
