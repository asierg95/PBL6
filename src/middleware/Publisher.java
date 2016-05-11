package middleware;

public class Publisher {
	
	PsPort pPort;

	public void iniciarConexion() {
		String fichConfig = "middleware.conf";
		PsPortFactory pFactory = new PsPortFactory();
		
		pPort = pFactory.getPort(fichConfig);		
		pPort.start();
		
	}

	public void send(int id, String mensaje) {
		if(pPort.publish(id, mensaje.getBytes(), 8)){
			System.out.println("Mensaje enviado correctamente");
		}else{
			System.out.println("Error al enviar");
		};
	}	
	
}
