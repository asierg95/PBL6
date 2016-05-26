package middleware;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

/**
 * Puerto de la conexión socket
 * @author Popbl6
 *
 */
public class PsPort {
	
	final static int MAXLENGHT = 100;
	final static String SEPARADORMENSAJE = "=";
	final static int INTFALLO = -1;

	MulticastSocket conexion;
	int port;
	String keyString;
	
	ArrayList<String> ipMulticast;
	ArrayList<String> datos;
	ArrayList<Integer> dataLenght;
	ArrayList<InetAddress> grupoMulticast;

	boolean exit;
		
	/**
	 * Constructor PsPort
	 * @param direccionFichero direccion del fichero de configuracion
	 */
	PsPort(String direccionFichero){
		
		dataLenght = new ArrayList<Integer>(Collections.nCopies(60, 0));
		ipMulticast = new ArrayList<String>(Collections.nCopies(60, ""));
		datos = new ArrayList<String>(Collections.nCopies(60, ""));
		grupoMulticast = new ArrayList<InetAddress>(Collections.nCopies(60, null));
		inicializarConfiguracion(direccionFichero);		
	}
	
	/**
	 * Crea la conexion
	 */
	public boolean start(){
		boolean conexionIniciada = false;
		exit = false;
		conexionIniciada = crearConexion(port);
		return conexionIniciada;
	}

	/**
	 * Cierra la conexion
	 */
	public void close(){
		exit = true;
		conexion.close();
	}

	/**
	 * Publica un dato
	 * @param idData id del dato que se publica
	 * @param data dato que se publica
	 * @param len longitud del dato que se publica
	 * @return enviado: true  no-enviado: false
	 */
	public boolean publish(int idData, byte data[], int len){
		boolean enviado;
		byte mensaje[];
		
		try {
			InetAddress grupoMulticast = InetAddress.getByName(ipMulticast.get(idData));
			mensaje = crearMensaje(idData, data);
			byte[] mensajeEncriptado = encriptarDesencriptarMensaje(mensaje, Cipher.ENCRYPT_MODE);


			DatagramPacket paquete = new DatagramPacket(mensajeEncriptado, mensajeEncriptado.length, grupoMulticast , port);

			conexion.send(paquete);
			enviado = true;

		} catch (IllegalArgumentException | IOException e) {
			enviado = false;
		}
		
		return enviado;
	}
	
	/**
	 * Encriptar o desencriptar mensaje
	 * @param mensajeInicial byte[] que se quiere encriptar o desencriptar
	 * @param mode 1=encriptar 2=desencriptar
	 * @return mensaje encriptado o desencriptado
	 */
	private byte[] encriptarDesencriptarMensaje(byte[] mensajeInicial, int mode) {
		byte [] mensajeCifradoDescifrado;
		Cipher cipher = null;
		SecretKey clave = null;

		clave = crearClaveCifrado(keyString);
		cipher = inicializarCipher(mode, clave);
		mensajeCifradoDescifrado = cifradorDescifradorBytes(mensajeInicial, cipher);
			
		return mensajeCifradoDescifrado;
	}

	/**
	 * 
	 * @param mensaje byte[] que se quiere des/cifrar
	 * @param cipher cifrador que se va a utilizar para des/cifrar
	 * @return mensaje cifrado o descifrado
	 */
	private byte[] cifradorDescifradorBytes(byte[] mensaje, Cipher cipher) {
		byte[] mensajeCifradoDescifrado = null;
		
	    try {
	    	mensajeCifradoDescifrado = cipher.doFinal(mensaje);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return mensajeCifradoDescifrado;
	}

	/**
	 * Crear e inicializar Cipher
	 * @param encript_mode 1=encriptar 2=desencriptar
	 * @param clave SecretKey que usa el cipher para des/encriptar
	 * @return cipher creado e inicializado
	 */
	private Cipher inicializarCipher(int encript_mode, SecretKey clave) {
		Cipher cipher = null;
		
		try {
			cipher = Cipher.getInstance("DESede");
			cipher.init(encript_mode, clave);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
		
		return cipher;
	}

	/**
	 * Crear clave cifrado
	 * @param keyString string utilizado para crear la clave del cifrado
	 * @return
	 */
	private SecretKey crearClaveCifrado(String keyString) {
        SecretKey clave = null;
        SecretKeyFactory factory;
        
		try {
			factory = SecretKeyFactory.getInstance("DESede");
			clave = factory.generateSecret(new DESedeKeySpec(keyString.getBytes()));
		} catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return clave;
	}

	/**
	 * Recoge ultimo dato publicado
	 * @param idData id del dato que se quiere
	 * @return
	 */
	public String getLastSample(int idData){
		return datos.get(idData);
	}
	
	/**
	 * Crear el mensaje que se va a publicar con el formato adecuado
	 * @param idData id del dato que se va a publicar
	 * @param data dato que se va a publicar
	 * @return el mensaje combinado que se va a publicar
	 */
	public byte[] crearMensaje(int idData, byte[] data) {
		int hash = 0;
		String hashString;
		String combinedIdDataString;
		String combinedIdDataHashString;
		String id = String.valueOf(idData);
		
		combinedIdDataString = id + SEPARADORMENSAJE + byteArraytoString(data);
		hash = combinedIdDataString.hashCode();
		hashString = String.valueOf(hash);
		combinedIdDataHashString = combinedIdDataString +SEPARADORMENSAJE+ hashString;
		
		return combinedIdDataHashString.getBytes();
	}

	/**
	 * Crea la conexion MulticastSocket
	 */
	public boolean crearConexion(int puertoConexion) {
		boolean conexionCreada = false;
		
		try {
			conexion = new MulticastSocket(puertoConexion);
			conexionCreada = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return conexionCreada;
	}
	
	/**
	 * Inicializa el sistema con la configuracion del fichero
	 * @param direccionFichero fichero de configuracion
	 */
	public void inicializarConfiguracion(String direccionFichero) {
		int id = INTFALLO;
		int longitud = 0;
		String ip;
		String line;
	    String split[];
	    
		try (BufferedReader br = new BufferedReader(new FileReader(direccionFichero))) {
		    while ((line = br.readLine()) != null) {
		    	try{
		    		 split = line.split(SEPARADORMENSAJE);
		    		 switch(split[0]){
				    	case "puerto":
				    		port = Integer.valueOf(split[1]);
				    		break;
				    	case "clave":
				    		keyString = split[1];
				    		System.out.println(keyString);
				    		break;
				    	case "id":
				    		id = Integer.valueOf(split[1]);
				    		break;
				    	case "grupo":
				    		ip = split[1];
				    		ipMulticast.set(id, ip);
				    		break;
				    	case "long":
				    		longitud = Integer.valueOf(split[1]);
				    		dataLenght.add(id, longitud);
				    		break;
				    	default:
				    		break;
				    	}
		    	}catch(ArrayIndexOutOfBoundsException e){
		    		e.printStackTrace();
		    	}		    	
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Inicia el hilo que escucha los datos que se estan publicando
	 */
	public void escuchar() {
		DataReader dataReader = new DataReader(this, conexion, MAXLENGHT, SEPARADORMENSAJE);
		dataReader.start();
	}
	
	/**
	 * Se suscribe a un dato
	 * @param idDato id del dato al que se quiere suscribir
	 */
	public boolean suscribirADato(int idDato) {
		InetAddress ip = null;
		boolean adecuadamenteSuscrito = false;
		
		try {
			ip = InetAddress.getByName(ipMulticast.get(idDato));
			grupoMulticast.set(idDato, ip);
			conexion.joinGroup(grupoMulticast.get(idDato));
			adecuadamenteSuscrito = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return adecuadamenteSuscrito;
	}

	/**
	 * Guarda el dato para posteriormente poder leerlo
	 * @param idDato id del dato que se va a guardar
	 * @param mensaje el dato que se va a guardar
	 */
	public void guardarDato(byte [] datoByte) {
		String mensajeCompletoString;
		String mensaje;
		int idDato;
		String [] arrayMensaje;
		byte[] datoDescifrado;
		int hashRecibido;
		int hashCalculado;
		String combinadoIdMensaje;
		
		datoDescifrado = encriptarDesencriptarMensaje(datoByte, Cipher.DECRYPT_MODE);
		mensajeCompletoString = byteArraytoString(datoDescifrado);
		arrayMensaje = separarString(mensajeCompletoString, SEPARADORMENSAJE);
		idDato = leerIdDato(arrayMensaje);
		mensaje = leerMensaje(arrayMensaje);
		hashRecibido = leerHashMensaje(arrayMensaje);
		combinadoIdMensaje = idDato +SEPARADORMENSAJE+ mensaje;
		hashCalculado = combinadoIdMensaje.hashCode();
		
		if(hashRecibido == hashCalculado){
			datos.set(idDato, mensaje);
		}
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
	 * Separa el hash del array de strings
	 * @param arrayMensaje el array que contiene el idDato, el dato y el hash
	 * @return el dato del array
	 */
	private int leerHashMensaje(String[] arrayMensaje) {
		return Integer.valueOf(arrayMensaje[2]);
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
}

