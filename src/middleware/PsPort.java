package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class PsPort {
	
	MulticastSocket conexion;
	int port, len[] = new int[5], dataLenght;
	String ipMulticast[] , datos[];
	//ArrayList<Integer> dataLenght;
	InetAddress grupoMulticast[];
	boolean exit;
			
	PsPort(String direccionFichero){
		//inicializarConfiguracion(direccionFichero);
		datos = new String [5];
		ipMulticast = new String [5];
		grupoMulticast = new InetAddress[5];
	}
	
	public String getLastSample(int idData, byte data[], int len){
		//if(datos[idData].length() == len){
			return datos[idData];
		//}else{return "-1";}
	}

	boolean publish(int idData, byte data[], int len){
		boolean enviado;
		
		try {
			InetAddress grupoMulticast = InetAddress.getByName(ipMulticast[idData]);
			
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
	
	public void start(){
		exit = false;

		leerFichero();
		crearConexion();
	}

	public void close(){
		exit = true;
		//conexion.leaveGroup(grupoMulticast);
		conexion.close();
	}
	
	private byte[] crearMenesaje(int idData, byte[] data) {
		byte [] mensaje;
		String id = String.valueOf(idData) + '=';
		mensaje = id.getBytes();

		byte[] combined = new byte[data.length + mensaje.length];

		System.arraycopy(mensaje,0,combined,0,mensaje.length);
		System.arraycopy(data,0,combined,mensaje.length,data.length);
		
		return combined;
	}
	
	private void leerFichero() {
		port = 6868;
		ipMulticast[0] = "225.4.5.6";
		ipMulticast[1] = "225.4.5.7";
		dataLenght = 10;
	}

	private void crearConexion() {
		try {
			conexion = new MulticastSocket(port);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * private void inicializarConfiguracion(String direccionFichero) {
		int id = -1, cont = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(direccionFichero))) {
		    String line;
		    String split[];
		    while ((line = br.readLine()) != null) {
		    	try{
		    		 split = line.split("=");
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
				    		cont++;
				    		break;
				    	case 3:
				    		len[id] = Integer.valueOf(split[1]);
				    		cont = 1;
				    		break;
				    	default:
				    		break;
				    	}
		    	}catch(ArrayIndexOutOfBoundsException e){}		    	
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	
	public void escuchar() {
		Thread leerDato = new Thread() {
			public void run() {
				while(!exit){
					try {
						byte datoSocket[] = new byte[dataLenght];
						DatagramPacket paquete = new DatagramPacket(datoSocket, datoSocket.length);
						conexion.receive(paquete);
						String dato = new String (paquete.getData(), "UTF-8");
						String [] mensaje = dato.split("=");
						datos[Integer.valueOf(mensaje[0])] = mensaje[1];
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		leerDato.start();
	}
	
	public void suscribirDato(int idDato) {
		try {
			grupoMulticast [idDato] = InetAddress.getByName(ipMulticast[idDato]);
			conexion.joinGroup(grupoMulticast[idDato]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

