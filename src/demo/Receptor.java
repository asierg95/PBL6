package demo;

import java.io.IOException;

import middleware.Suscriber;

public class Receptor {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String ficheroConfiguracion = "middleware.conf";
		Suscriber suscriptor = new Suscriber();
		
		suscriptor.iniciarConexion(ficheroConfiguracion);
		
		suscriptor.suscribirseADato(0);
		suscriptor.suscribirseADato(1);
		
		suscriptor.escuchar();
		
		while(true){
			Thread.sleep(6000);
			String dato1 = suscriptor.obtenerDato(0, 4);
			System.out.println("Receptor recibe: "+dato1);
			String dato2 = suscriptor.obtenerDato(1, 4);
			System.out.println("Receptor2 recibe: "+dato2);
		}
	}

}
