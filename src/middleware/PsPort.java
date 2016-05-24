package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class PsPort {
	
	final static int MAXLENGHT = 100;
	final static int ENCABEZADOMENSAJE = 2;
	final static String SEPARADORMENSAJE = "=";

	MulticastSocket conexion;
	int port, len[] = new int[5];
	String ipMulticast[] , datos[];
	//ArrayList<Integer> dataLenght;
	InetAddress grupoMulticast[];
	boolean exit;
			
	public PsPort(String direccionFichero){
		datos = new String [5];
		ipMulticast = new String [5];
		grupoMulticast = new InetAddress[5];
		inicializarConfiguracion(direccionFichero);		
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
			
			byte mensaje[] = crearMensaje(idData, data);
			
			DatagramPacket paquete = new DatagramPacket(mensaje, (len+ENCABEZADOMENSAJE), grupoMulticast , port);
			conexion.send(paquete);
			enviado = true;

		} catch (IllegalArgumentException | IOException e) {
			enviado = false;
		}
		
		return enviado;
	}
	
	public void start(){
		exit = false;
		crearConexion();
	}

	public void close(){
		exit = true;
		//conexion.leaveGroup(grupoMulticast);
		conexion.close();
	}
	
	public byte[] crearMensaje(int idData, byte[] data) {
		byte [] mensaje;
		String id = String.valueOf(idData) + SEPARADORMENSAJE;
		mensaje = id.getBytes();

		byte[] combined = new byte[data.length + mensaje.length];

		System.arraycopy(mensaje,0,combined,0,mensaje.length);
		System.arraycopy(data,0,combined,mensaje.length,data.length);
		
		return combined;
	}

	private void crearConexion() {
		try {
			conexion = new MulticastSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void inicializarConfiguracion(String direccionFichero) {
		int id = -1, cont = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(direccionFichero))) {
		    String line;
		    String split[];
		    while ((line = br.readLine()) != null) {
		    	try{
		    		 split = line.split(SEPARADORMENSAJE);
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
	
	public void escuchar() {
		DataReader dataReader = new DataReader(this, conexion, MAXLENGHT, SEPARADORMENSAJE);
		dataReader.start();
	}
	
	public void suscribirDato(int idDato) {
		try {
			grupoMulticast [idDato] = InetAddress.getByName(ipMulticast[idDato]);
			conexion.joinGroup(grupoMulticast[idDato]);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void guardarDato(int idDato, String mensaje) {
		datos[idDato] = mensaje;
	}
	
	
	
}

