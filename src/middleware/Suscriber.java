package middleware;

public class Suscriber {
	PsPortFactory conexion;
	PsPort port;
	
	/*public Suscriber(String direccionFicheroConfiguracion){
		iniciarConexion(direccionFicheroConfiguracion);
	}*/

	public void iniciarConexion(String direccionFicheroConfiguracion) {
		conexion = new PsPortFactory();
		port = conexion.getPort(direccionFicheroConfiguracion);
		port.start();
	}

	public void suscribirseADato(int idDato) {
		port.suscribirDato(idDato);		
	}
	
	public String obtenerDato(int idDato, int lenght){
		String dato = port.getLastSample(idDato, null, lenght);
		return dato;
	}
	
	public void escuchar(){
		port.escuchar();
	}
	
	

}
