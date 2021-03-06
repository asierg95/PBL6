package demo;

import java.util.Scanner;

import middleware.Publisher;

public class Emisor {

	public static void main(String[] args) {
		String mensaje;
		Publisher publicador = new Publisher();
		boolean exit = false, respuesta;
		Scanner teclado = new Scanner(System.in);
		int id, len;
		String fichConf = "middleware.conf";
		publicador.iniciarConexion(fichConf);

		
		System.out.println("Ya se pueden mandar mensajes");
		while(!exit){
			System.out.println("Mensaje: ");
			mensaje = teclado.next();
			System.out.println("Cual es el id del dato?");
			id = Integer.valueOf(teclado.next());
			System.out.println("Cual es la longitud del dato?");
			len = Integer.valueOf(teclado.next());
			respuesta = publicador.send(id, mensaje.getBytes(), len);
			System.out.println(respuesta);
		}
	}
}

