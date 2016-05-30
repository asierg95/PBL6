package tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.crypto.Cipher;

import middleware.PsPort;
import middleware.Publisher;
import middleware.Suscriber;

import org.junit.Before;
import org.junit.Test;


public class PsPortTest {

	PsPort port;

	Publisher publisher;      
    Suscriber suscriber;

	
	@Before
	public void setUp() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Constructor<PsPort> constructor = PsPort.class.getDeclaredConstructor(new Class[] {String.class});
		constructor.setAccessible(true);
		port = constructor.newInstance("middleware.conf");

		publisher = new Publisher();
		suscriber = new Suscriber();
	}
	
	@Test
	public void testStart() {
		 boolean expected = true;
		 boolean actual = port.start();
		 assertEquals("Failure - conexion does not start correctly", expected, actual);
	}
	
	@Test
	  public void testCrearMensaje() {
	    byte[] expected = "1=testeo=1219308008".getBytes();
	    byte[] actual = port.crearMensaje(1, "testeo".getBytes());
	    assertArrayEquals("failure - byte arrays not same", expected, actual);
	  }
	
	@Test
	  public void testPublish() {
	    boolean expected = true;
	    port.start();
	    boolean actual = port.publish(1, "123456".getBytes());
	    assertEquals("failure - message not published", expected, actual);
	}	
	
	@Test
	  public void testCrearConexion() {
	    boolean expected = true;
	    port.start();
	    boolean actual = port.crearConexion(6868);
	    assertEquals("failure - conexion not created", expected, actual);
	}	
	
	@Test
	public void testEncriptarDesencriptar() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, UnsupportedEncodingException{
		String expected = "prueba";
		port.start();
		String mensaje = "prueba";
		//Encriptar
		Method encriptar = port.getClass().getDeclaredMethod("encriptarDesencriptarMensaje", byte[].class, int.class);
		encriptar.setAccessible(true);
		byte[] encriptado = (byte[]) encriptar.invoke(port, mensaje.getBytes(), Cipher.ENCRYPT_MODE);
		//Desencriptar
		Method desencriptar = port.getClass().getDeclaredMethod("encriptarDesencriptarMensaje", byte[].class, int.class);
		desencriptar.setAccessible(true);
		byte[] desencriptado = (byte[]) desencriptar.invoke(port, encriptado, Cipher.DECRYPT_MODE);
		
		String actual = new String(desencriptado, "UTF-8");		
		
		assertEquals("failure - encription not correctly encript", expected, actual);
	}
	
	@Test
	public void testGetLastSample() throws InterruptedException{
	    String expected = "kaixoo";
	    publisher.iniciarConexion("middleware.conf");
	    suscriber.iniciarConexion("middleware.conf");
	    suscriber.suscribirseADato(1);
	    suscriber.escuchar();
	    publisher.send(1, expected, 6);
	    Thread.sleep(10);
	    String actual = suscriber.obtenerDato(1, 6);
	    assertEquals(expected, actual);
	}
	
	@Test
	public void TestinicializarVariablesFichero() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String linea = "puerto=123";
		
		Method inicializarVariablesFichero = port.getClass().getDeclaredMethod("inicializarVariablesFichero", String.class);
		inicializarVariablesFichero.setAccessible(true);
		inicializarVariablesFichero.invoke(port,linea);
	}
	
	@Test
	public void TestinicializarVariablesFicheroDefault() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String linea = "  ";
		
		Method inicializarVariablesFichero = port.getClass().getDeclaredMethod("inicializarVariablesFichero", String.class);
		inicializarVariablesFichero.setAccessible(true);
		inicializarVariablesFichero.invoke(port,linea);
	}
	
	@Test
	public void TestinicializarVariablesFicheroIndex() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String linea = "puerto=";
		
		Method inicializarVariablesFichero = port.getClass().getDeclaredMethod("inicializarVariablesFichero", String.class);
		inicializarVariablesFichero.setAccessible(true);
		inicializarVariablesFichero.invoke(port,linea);

	}

}
