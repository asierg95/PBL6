package middleware;

/**
 * Clase utilizada para publicar datos haciendo uso del middleware.
 * @see #iniciarConexion()
 * @see #send(int, String, int)
 * @author Popbl6
 */
public class Publisher {
	
	PsPort pPort;

	/**
	 * Crea una clase con los valores del fichero de configuración e inicia la conexion.
	 */
	public void iniciarConexion() {
		String fichConfig = "middleware.conf";
		PsPortFactory pFactory = new PsPortFactory();
		
		pPort = pFactory.getPort(fichConfig);		
		pPort.start();
		
	}

	/**
	 * Funcion que sirve para mandar mensajes
	 * @param id El canal por el que se van a mandar los mensajes
	 * @param mensaje El texto que se va a enviar
	 * @param len Tamaño del texto enviado
	 * @return 0 = mensaje enviado correctamente, 1 = longitud distinta a la del fichero de configuración, -1 = error desconocido
	 */
	public int send(int id, String mensaje, int len) {
		int resultado;
		if(len != pPort.len[id]){
			if(pPort.publish(id, mensaje.getBytes(), len)){
				resultado = 0;
			}else{
				resultado = -1;
			};
		}else{
			resultado = 1;
		}
		return resultado;
	}	
	
}
