package middleware;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class PsPort {
	
	MulticastSocket conexion;
	String direccionFichero;
	String [] datos, ipMulticast;
	int dataLenght, port;
	//ArrayList<Integer> dataLenght;
	InetAddress grupoMulticast[];
	boolean exit;
			
	PsPort(String direccionFichero){
		this.direccionFichero = direccionFichero;
		datos = new String [5];
		ipMulticast = new String [5];
	}

	public boolean publish(int idData, byte data[], int len){
		return false;
	}
	
	public String getLastSample(int idData, byte data[], int len){
		//if(datos[idData].length() == len){
			return datos[idData];
		//}else{return "-1";}
	}
	
	public void start(){
		exit = false;

		leerFichero();
		crearConexion();
		suscribirDato(0);
		suscribirDato(1);
		escuchar();
	}
	
	private void escuchar() {
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

	private void leerFichero() {
		port = 6868;
		ipMulticast[0] = "225.4.5.6";
		ipMulticast[1] = "225.4.5.7";
		dataLenght = 20;
	}

	private void crearConexion() {
		try {
			conexion = new MulticastSocket(port);
			grupoMulticast = new InetAddress[5];

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close(){
		exit = true;
		//conexion.leaveGroup(grupoMulticast);
		conexion.close();
	}

}

