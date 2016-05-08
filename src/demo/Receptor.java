package demo;

import java.io.IOException;

import middleware.PsPort;
import middleware.PsPortFactory;

public class Receptor {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		PsPortFactory conexion = new PsPortFactory();
		PsPort port = conexion.getPort("direccionPrueba");
		port.start();
		while(true){
		Thread.sleep(10000);
		String dato = port.getLastSample(0, null, 4);
		System.out.println("Receptor recibe: "+dato);
		}
	}

}
