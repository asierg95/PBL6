package middleware;

/**
 * La clase PsPortFactory es la intermediaria que creará y devolverá un puerto socket
 * @author Popbl6
 *
 */
public class PsPortFactory {
	
	/**
	 * Crea un puerto de conexion
	 * @param direccionFichero fichero del que se va a coger la configuracion
	 * @return puerto de conexion
	 */
	public PsPort getPort(String direccionFichero){
		PsPort port = new PsPort(direccionFichero);
		return port;
	}

}
