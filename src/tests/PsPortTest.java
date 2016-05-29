package tests;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.crypto.Cipher;

import org.junit.Before;
import org.junit.Test;

import middleware.DataReader;
import middleware.PsPort;

public class PsPortTest {
	PsPort port;
	//DataReader dataReader;
	
	@Before
	public void setUp() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Constructor<PsPort> constructor = PsPort.class.getDeclaredConstructor(new Class[] {String.class});
		constructor.setAccessible(true);
		port = constructor.newInstance("middleware.conf");
		//dataReader = strictMock(DataReader.class);
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
	    boolean actual = port.publish(1, "123456".getBytes(), 6);
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
	public void testLeerMensaje() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, UnsupportedEncodingException{
		String expected = "mensaje";
		String [] mensaje = {"id","mensaje","hash"};
						
		Method leerMensaje = PsPort.class.getDeclaredMethod("leerMensaje", String[].class);
		leerMensaje.setAccessible(true);
		String actual = (String) leerMensaje.invoke(port, mensaje);
		
		assertEquals("failure - message not correctly readed", expected, actual);
	}
	
	

}
