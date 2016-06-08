package demo;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

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
			String dato1 = new String (suscriptor.obtenerDato(0, 8), "UTF-8");
			System.out.println("Receptor recibe: "+dato1);
			String dato2 = new String (suscriptor.obtenerDato(1, 6), "UTF-8");
			System.out.println("Receptor2 recibe: "+dato2);
		}
	}

}
