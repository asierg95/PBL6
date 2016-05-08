package middleware;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class PsPort {
	
	MulticastSocket conexion;
	String direccionFichero;
	String [] datos;
	
	PsPort(String direccionFichero){
		this.direccionFichero = direccionFichero;
		datos = new String [5];
	}

	public boolean publish(int idData, byte data[], int len){
		return false;
	}
	
	public String getLastSample(int idData, byte data[], int len){
		//if(datos.length == len){
			return datos[idData];
		//}else{return "-1";}
	}
	
	/*Funcion start
	 * es una primera prueba
	 * para la segunda prueba se leera lo necesario del fichero y en la funcion start creara un hilo
	 * para cada dato que sestara escuchando y guardando el utimo dato recibido.
	 * La 3. version pensarla
	 */
	public void start() throws IOException{
		boolean exit = false;
		
		int port = 6868; //leer de fichero
		String ipMulticast = "225.4.5.6"; //leer de fichero la ipMulticast de los datos
		int longitud = 20; //leer de fichero
		conexion = new MulticastSocket(port);
		InetAddress grupoMulticast = InetAddress.getByName(ipMulticast);

		conexion.joinGroup(grupoMulticast);
		
		byte datoSocket[] = new byte[longitud];
		DatagramPacket paquete = new DatagramPacket(datoSocket, datoSocket.length);
		
		Thread leerDato = new Thread() {
			public void run() {
				while(!exit){
					
					try {
						conexion.receive(paquete);
						String dato = new String (paquete.getData(), "UTF-8");
						datos[0] = dato;
						System.out.println("Dato: " +dato +" longitud:"+dato.length() + "IP: "+paquete.getAddress());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		leerDato.start();
	}
	
	public void close(){
		//conexion.leaveGroup(grupoMulticast);
		conexion.close();
	}

}

