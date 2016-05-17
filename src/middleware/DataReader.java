package middleware;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class DataReader extends Thread{
	MulticastSocket conexion;
	int maxLenght;
	boolean exit = false;
	String separadorMensaje;
	PsPort port;
	
	public DataReader(PsPort port, MulticastSocket conexion, int maxLenght, String separadorMensaje) {
		this.port = port;
		this.conexion = conexion;
		this.maxLenght = maxLenght;
		this.separadorMensaje = separadorMensaje;
	}
	
	public void run() {
		while(!exit){
			try {
				byte datoSocket[] = new byte[maxLenght];
				DatagramPacket paquete = new DatagramPacket(datoSocket, datoSocket.length);
				conexion.receive(paquete);
										
				guardarMensaje(paquete);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void guardarMensaje(DatagramPacket paquete) {
		String mensaje = null;
		int idDato = 0;
		byte [] datoByte;
		int longitudDato = 0;
		byte [] datoByteCompleto;
		String [] arrayMensaje;
		
		datoByteCompleto = paquete.getData();
		longitudDato = longitudByteArray(datoByteCompleto);
		datoByte = copiarByteArray(datoByteCompleto, longitudDato);
		mensaje = byteArraytoString(datoByte);
		arrayMensaje = separarString(mensaje, separadorMensaje);
		idDato = leerIdDato(arrayMensaje);
		mensaje = leerMensaje(arrayMensaje);
		
		port.guardarDato(idDato, mensaje);
		
	}

	private String byteArraytoString(byte [] datoByte) {
		String mensaje = null;
		try {
			mensaje = new String (datoByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return mensaje;
	}

	private String leerMensaje(String [] arrayMensaje) {
		return arrayMensaje[1];
	}

	private int leerIdDato(String [] arrayMensaje) {
		return Integer.valueOf(arrayMensaje[0]);
	}

	private String[] separarString(String dato, String separadormensaje) {
		String [] mensaje;
		
		mensaje = dato.split(separadormensaje);
		
		return mensaje;
	}

	private byte[] copiarByteArray(byte [] datoByteCompleto, int longitud) {
		byte [] datoByte = new byte [longitud];
		for(int i = 0; i<datoByte.length; i++){
			datoByte[i] = datoByteCompleto[i];
		}
		return datoByte;
	}

	private int longitudByteArray(byte[] datoByteCompleto) {
		int longitud = 0;
		while(datoByteCompleto[longitud] != '\0'){
			longitud++;
		}
		return longitud;
	}

}
