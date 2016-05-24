package middleware;

/**
 * Clase para crear la conexion, suscribirse a datos, escuchar y obtener datos
 * @see #iniciarConexion(String)
 * @see #suscribirseADato(int)
 * @see #escuchar()
 * @see #obtenerDato(int, int)
 * @author Popbl6
 *
 */

public class Suscriber {
	PsPortFactory conexion;
	PsPort port;

	/**
	 * Inicia la conexion socket con la configuracion recogida del fichero
	 * @param direccionFicheroConfiguracion fichero de configuracion
	 */
	public boolean iniciarConexion(String direccionFicheroConfiguracion) {
		boolean respuesta = false;
		conexion = new PsPortFactory();
		port = conexion.getPort(direccionFicheroConfiguracion);
		respuesta = port.start();
		return respuesta;
	}

	/**
	 * Se suscribe a datos
	 * @param idDato el id del dato al que se va a suscribir
	 */
	public boolean suscribirseADato(int idDato) {
		boolean respuesta = false;
		respuesta = port.suscribirADato(idDato);
		return respuesta;
	}
	
	/**
	 * Obtener el ultimo dato publicado
	 * @param idDato id del dato que se quiere obtener
	 * @param lenght la longitud del dato que se quiere obtener
	 * @return el ultimo dato publicado
	 */
	public String obtenerDato(int idDato, int lenght){
		String dato = port.getLastSample(idDato, lenght);
		return dato;
	}
	
	/**
	 * Escucha en el puerto esperando que lleguen nuevos datos
	 */
	public void escuchar(){
		port.escuchar();
	}
	
	

}
