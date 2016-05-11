package demo;

import java.util.Scanner;

import middleware.Publisher;

public class Emisor {

	public static void main(String[] args) {
		String mensaje;
		Publisher publicador = new Publisher();
		boolean exit = false;
		Scanner teclado = new Scanner(System.in);
		int id;
		
		publicador.iniciarConexion();
		
		System.out.println("Ya se pueden mandar mensajes");
		while(!exit){
			mensaje = teclado.next();
			System.out.println("Cual es el id del dato?");
			id = Integer.valueOf(teclado.next());
			publicador.send(id, mensaje);
		}
		
	}

}