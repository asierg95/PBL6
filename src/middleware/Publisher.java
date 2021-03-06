package middleware;

/**
 * Clase utilizada para publicar datos haciendo uso del middleware.
 * @see #iniciarConexion()
 * @see #send(int, String, int)
 * @author Popbl6
 */
public class Publisher {
	
    private PsPort pPort;

	/**
	 * Crea una clase con los valores del fichero de configuraci�n e inicia la conexion.
	 */
	public boolean iniciarConexion(String fichConfig) {
		boolean respuesta;
		
		PsPortFactory pFactory = new PsPortFactory();
		
		pPort = pFactory.getPort(fichConfig);		
		pPort.start();
		respuesta = true;
		
		return respuesta;
	}

	/**
	 * Funcion que sirve para mandar mensajes
	 * @param id El canal por el que se van a mandar los mensajes
	 * @param mensaje El texto que se va a enviar
	 * @param len Tama�o del texto enviado
	 * @return true -> el mensaje se envia correctamente, false -> el mensaje no se ha enviado
	 */
	public boolean send(int id, byte [] mensaje, int len) {
		boolean resultado = false;
		int lenght = pPort.getDataLenght().get(id);
		if((len == lenght) && (lenght == mensaje.length) && (pPort.publish(id, mensaje))){
				resultado = true;
		}
		return resultado;
	}	
	
}
