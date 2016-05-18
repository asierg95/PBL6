package middleware;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Hilo que lee los datos de la conexion socket, los interpreta y los almacena
 * @author Popbl6
 *
 */
public class DataReader extends Thread{
	MulticastSocket conexion;
	int maxLenght;
	boolean exit = false;
	String separadorMensaje;
	PsPort port;
	
	/**
	 * DataReader constructor
	 * @param port
	 * @param conexion
	 * @param maxLenght
	 * @param separadorMensaje
	 */
	
	public DataReader(PsPort port, MulticastSocket conexion, int maxLenght, String separadorMensaje) {
		this.port = port;
		this.conexion = conexion;
		this.maxLenght = maxLenght;
		this.separadorMensaje = separadorMensaje;
	}
	
	@Override
	public void run() {
		ejecutar();
	}

	private void ejecutar() {
		byte datoSocket[] = new byte[maxLenght];
		while(!exit){
			try {
				DatagramPacket paquete = new DatagramPacket(datoSocket, datoSocket.length);
				conexion.receive(paquete);			
				guardarMensaje(paquete);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
	}

	/**
	 * Guarda el mensaje en el array del PsPort
	 * @param paquete el paquete que contiene los datos que se van a almacenar
	 */
	private void guardarMensaje(DatagramPacket paquete) {
		String mensaje;
		int idDato;
		byte [] datoByte;
		int longitudDato;
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

	/**
	 * Convierte un byteArray en String
	 * @param datoByte el byteArray que se va a convertir en String
	 * @return el mensaje convertido a String
	 */
	private String byteArraytoString(byte [] datoByte) {
		String mensaje = null;
		try {
			mensaje = new String (datoByte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return mensaje;
	}

	/**
	 * Separa el dato del array de strings
	 * @param arrayMensaje el array que contiene el idDato y el dato
	 * @return el dato del array
	 */
	private String leerMensaje(String [] arrayMensaje) {
		return arrayMensaje[1];
	}

	/**
	 * Separa el idDato del array de strings
	 * @param arrayMensaje el array que contiene el idDato y el dato
	 * @return el idDato del array convertido a integer
	 */
	private int leerIdDato(String [] arrayMensaje) {
		return Integer.valueOf(arrayMensaje[0]);
	}

	/**
	 * Separa el un String en un array de String diviendolo con el separador
	 * @param dato el String que se quiere dividir
	 * @param separadormensaje el caracter que va a dividir las partes del String
	 * @return String [] con los Strings separados
	 */
	private String[] separarString(String dato, String separadormensaje) {
		String [] mensaje;
		
		mensaje = dato.split(separadormensaje);
		
		return mensaje;
	}

	/**
	 * Copiar byte[] completo a un byte[] de tamaño justo
	 * @param datoByteCompleto el byteArray completo
	 * @param longitud la longitud de los datos utiles dentro del array
	 * @return byteArray con los datos utiles y longitud justa
	 */
	private byte[] copiarByteArray(byte [] datoByteCompleto, int longitud) {
		byte [] datoByte = new byte [longitud];
		for(int i = 0; i<datoByte.length; i++){
			datoByte[i] = datoByteCompleto[i];
		}
		return datoByte;
	}

	/**
	 * Calcular la longitud de los datos utiles del array
	 * @param datoByteCompleto el array completo que se va a analizar
	 * @return longitud de los datos utiles del array
	 */
	private int longitudByteArray(byte[] datoByteCompleto) {
		int longitud = 0;
		while(datoByteCompleto[longitud] != '\0'){
			longitud++;
		}
		return longitud;
	}

}
