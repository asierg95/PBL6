package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class PsPort {
	
	MulticastSocket conexion;
	int port;
	String ipMulticast[] = new String[5];
	
	PsPort(String direccionFichero){
		int id = -1, cont = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(direccionFichero))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String split[] = line.split("=");
		    	switch(cont){
		    	case 0:
		    		port = Integer.valueOf(split[1]);
		    		cont++;
		    		break;
		    	case 1:
		    		id = Integer.valueOf(split[1]);
		    		cont++;
		    		break;
		    	case 2:
		    		ipMulticast[id] = split[1];
		    		cont--;
		    		break;
		    	default:
		    		break;
		    	}
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	boolean publish(int idData, byte data[], int len){
		boolean enviado;
		
		try {
			InetAddress grupoMulticast = InetAddress.getByName(ipMulticast[idData]);
			System.out.println(ipMulticast[idData]);
			
			byte mensaje[] = crearMenesaje(idData, data);
			
			DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length, grupoMulticast , port);
			conexion.send(paquete);
			
			enviado = true;

		} catch (IOException e) {
			enviado = false;
			e.printStackTrace();
		}
		
		return enviado;
	}
	
	private byte[] crearMenesaje(int idData, byte[] data) {
		byte [] mensaje;
		String id = String.valueOf(idData) + '$';
		mensaje = id.getBytes();

		byte[] combined = new byte[data.length + mensaje.length];

		System.arraycopy(mensaje,0,combined,0,mensaje.length);
		System.arraycopy(data,0,combined,mensaje.length,data.length);
		
		return combined;
	}

	boolean getLastSample(int idData, byte data[], int len){
		return false;
	}
	
	void start(){
		try {
			conexion = new MulticastSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void close(){
		conexion.close();
	}

}

