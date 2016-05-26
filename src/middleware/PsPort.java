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
	final static int PUERTO = 0;
	final static int ID = 1;
	final static int GRUPO = 2;
	final static int LONGITUD = 3;
	final static int INTFALLO = -1;

	MulticastSocket conexion;
	int port;
	
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
		
		try {
			InetAddress grupoMulticast = InetAddress.getByName(ipMulticast.get(idData));
			byte mensaje[] = crearMensaje(idData, data);
			byte[] mensajeEncriptado = encriptarMensaje(mensaje);
			DatagramPacket paquete = new DatagramPacket(mensajeEncriptado, mensajeEncriptado.length, grupoMulticast , port);
			conexion.send(paquete);
			enviado = true;

		} catch (IllegalArgumentException | IOException e) {
			enviado = false;
		}
		
		return enviado;
	}
	
	private byte[] encriptarMensaje(byte[] mensajeInicial) {
		String StringCifrado = "";
		Cipher cipher = null;
		//CREAR CLAVE DES/ENCRIPTACION
		
				String keystring = "1=O)234F%P0PbL6?¿!AUsz,aje4/42s";
		        SecretKey clave = null;
				try {
					SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
					clave = factory.generateSecret(new DESedeKeySpec(keystring.getBytes()));
			        cipher = Cipher.getInstance("DESede");
				} catch (InvalidKeyException e2) {
					e2.printStackTrace();
				} catch (InvalidKeySpecException e2) {
					e2.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				}
				
			    System.out.println("CLAVE:" + new String(clave.getEncoded()));
				

		//CREAR ENCRIPTADOR
			try {
				cipher = Cipher.getInstance("DESede");
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				e.printStackTrace();
			}
			
			//CIFRAR
				//inicializar en modo cifrado
			    try {
					cipher.init(Cipher.ENCRYPT_MODE, clave);
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				}
			    byte[] mensajeCifrado = null;
			    //Cifrar mensaje
			    try {
					mensajeCifrado = cipher.doFinal(mensajeInicial);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					e.printStackTrace();
				}
			    
				StringCifrado = new String(mensajeCifrado);
				
				System.out.println("--------------- TEXTO CIFRADO ---------------");
				System.out.println(StringCifrado);   // Mostrar texto cifrado
				System.out.println("---------------------------------------------");
				
				
				
				//DESCIFRAR
				//inicializar en modo descifrado   
			    try {
			    	cipher.init(Cipher.DECRYPT_MODE, clave);
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				}
			
			    //Descifrar mensaje
			    byte[] mensajeDescifrado = null;
				try {
					mensajeDescifrado = cipher.doFinal(StringCifrado.getBytes());
				} catch (IllegalBlockSizeException | BadPaddingException e1) {
					e1.printStackTrace();
				}
			    
				String StringDescifrado = new String(mensajeDescifrado);
				System.out.println("--------------- TEXTO DESCIFRADO ---------------");
				System.out.println(StringDescifrado);   // Mostrar texto cifrado
				System.out.println("---------------------------------------------");
			
		return mensajeCifrado;
	}	
	
	/**
	 * Recoge ultimo dato publicado
	 * @param idData id del dato que se quiere
	 * @param len longitud del dato que se quiere
	 * @return
	 */
	public String getLastSample(int idData, int len){
		//System.out.println(datos.get(idData).length());
		//if(datos.get(idData).length() == len){
			return datos.get(idData);
		//}else{
		//	return "-1";
		//}
	}
	
	/**
	 * Crear el mensaje que se va a publicar con el formato adecuado
	 * @param idData id del dato que se va a publicar
	 * @param data dato que se va a publicar
	 * @return el mensaje combinado que se va a publicar
	 */
	public byte[] crearMensaje(int idData, byte[] data) {
		int hash = 0;
		String combinedIdDataString;
		String combinedIdDataHashString;
		String id = String.valueOf(idData);
		
		combinedIdDataString = id + SEPARADORMENSAJE + byteArraytoString(data);
		hash = combinedIdDataString.hashCode();
		System.out.println("HASH: "+hash);
		
		combinedIdDataHashString = combinedIdDataString +SEPARADORMENSAJE+ String.valueOf(hash);
		
		System.out.println("COMBINADOOOOOOOO "+combinedIdDataHashString);
		
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
		int cont = 0;
		int longitud = 0;
		String ip;
		String line;
	    String split[];
	    
		try (BufferedReader br = new BufferedReader(new FileReader(direccionFichero))) {
		    while ((line = br.readLine()) != null) {
		    	try{
		    		 split = line.split(SEPARADORMENSAJE);
		    		 switch(cont){
				    	case PUERTO:
				    		port = Integer.valueOf(split[1]);
				    		cont = ID;
				    		break;
				    	case ID:
				    		id = Integer.valueOf(split[1]);
				    		cont = GRUPO;
				    		break;
				    	case GRUPO:
				    		ip = split[1];
				    		ipMulticast.set(id, ip);
				    		cont = LONGITUD;
				    		break;
				    	case LONGITUD:
				    		longitud = Integer.valueOf(split[1]);
				    		dataLenght.add(id, longitud);
				    		cont = ID;
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
	 * Escucha los datos que se estan publicando
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
		int hash;
		
		datoDescifrado = descifrar(datoByte);
		mensajeCompletoString = byteArraytoString(datoDescifrado);
		arrayMensaje = separarString(mensajeCompletoString, SEPARADORMENSAJE);
		idDato = leerIdDato(arrayMensaje);
		mensaje = leerMensaje(arrayMensaje);
		hash = leerHashMensaje(arrayMensaje);
		System.out.println("HASH RECIBIDO: "+hash);
		String combinadoIdMensaje = idDato +SEPARADORMENSAJE+ mensaje;
		int hashReal = combinadoIdMensaje.hashCode();
		System.out.println("HASH REAL: "+hashReal+"  NUMERO:"+combinadoIdMensaje);
		System.out.println("HASH JEJEJE:"+"1=123456".getBytes().hashCode());
		datos.set(idDato, mensaje);
	}

	private byte[] descifrar(byte[] datoByte) {
		Cipher cipher = null;
		//CREAR CLAVE DES/ENCRIPTACION
				String keystring = "1=O)234F%P0PbL6?¿!AUsz,aje4/42s";
		        SecretKey clave = null;
				try {
					SecretKeyFactory factory = SecretKeyFactory.getInstance("DESede");
					clave = factory.generateSecret(new DESedeKeySpec(keystring.getBytes()));
			        cipher = Cipher.getInstance("DESede");
				} catch (InvalidKeyException e2) {
					e2.printStackTrace();
				} catch (InvalidKeySpecException e2) {
					e2.printStackTrace();
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				}
				
			    System.out.println("CLAVE:" + new String(clave.getEncoded()));
				

		//CREAR ENCRIPTADOR
			try {
				cipher = Cipher.getInstance("DESede");
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				e.printStackTrace();
			}
			
			//DESCIFRAR
			//inicializar en modo descifrado   
		    try {
		    	cipher.init(Cipher.DECRYPT_MODE, clave);
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			}
		
		    System.out.println("DATOOO: "+datoByte);
		    //Descifrar mensaje
		    byte[] mensajeDescifrado = null;
			try {
				mensajeDescifrado = cipher.doFinal(datoByte);
			} catch (IllegalBlockSizeException | BadPaddingException e1) {
				e1.printStackTrace();
			}
		    
			String StringDescifrado = new String(mensajeDescifrado);
			System.out.println("--------------- TEXTO DESCIFRADO ---------------");
			System.out.println(StringDescifrado);   // Mostrar texto cifrado
			System.out.println("---------------------------------------------");
		return mensajeDescifrado;
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

