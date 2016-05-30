package middleware;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.logging.Logger;

/**
 * Hilo que lee los datos de la conexion socket, los interpreta y los almacena
 * @author Popbl6
 *
 */
public class DataReader extends Thread{
	MulticastSocket conexion;
	int maxLenght;
	private boolean exit = false;
	String separadorMensaje;
	PsPort port;
	private static final Logger LOGGER = Logger.getLogger(DataReader.class.getName());
	
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

	public void ejecutar() {
		while(!exit){
			try {
				byte[] datoSocket = new byte[maxLenght];
				DatagramPacket paquete = new DatagramPacket(datoSocket, datoSocket.length);
				conexion.receive(paquete);			
				guardarMensaje(paquete);
				
			} catch (IOException e) {
				LOGGER.info(e.getMessage());
			}
		}		
	}

	/**
	 * Guarda el mensaje en el array del PsPort
	 * @param paquete el paquete que contiene los datos que se van a almacenar
	 */
	private void guardarMensaje(DatagramPacket paquete) {
		byte [] datoByte;
		int longitudDato;
		byte [] datoByteCompleto;
		
		datoByteCompleto = paquete.getData();
		longitudDato = longitudByteArray(datoByteCompleto);
		datoByte = copiarByteArray(datoByteCompleto, longitudDato);
		
		port.guardarDato(datoByte);
	}

	/**
	 * Copiar byte[] completo a un byte[] de tamaño justo
	 * @param datoByteCompleto el byteArray completo
	 * @param longitud la longitud de los datos utiles dentro del array
	 * @return byteArray con los datos utiles y longitud justa
	 */
	private static byte[] copiarByteArray(byte [] datoByteCompleto, int longitud) {
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
	private static int longitudByteArray(byte[] datoByteCompleto) {
		int longitud = 0;
		while(datoByteCompleto[longitud] != '\0'){
			longitud++;
		}
		return longitud;
	}
	
	public void setExit(boolean exit){
		this.exit = exit;
	}

}

