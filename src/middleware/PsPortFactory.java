package middleware;

public class PsPortFactory {
	
	public PsPort getPort(String direccionFichero){
		PsPort port = new PsPort(direccionFichero);
		return port;
	}

}
