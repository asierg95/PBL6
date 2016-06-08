package tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.crypto.Cipher;
import static org.hamcrest.CoreMatchers.is;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import middleware.PsPort;
import middleware.Publisher;
import middleware.Suscriber;


public class PsPortTest extends EasyMockSupport{

	PsPort port;

	Publisher publisher;      
    Suscriber suscriber;
    
    String confFile = "exceptionTest2.conf";

	
	@Before
	public void setUp() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Constructor<PsPort> constructor = PsPort.class.getDeclaredConstructor(new Class[] {String.class});
		constructor.setAccessible(true);
		port = constructor.newInstance("exceptionTest2.conf");

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
	    byte[] expected = "kaixoo".getBytes();
	    publisher.iniciarConexion("exceptionTest2.conf");
	    suscriber.iniciarConexion("exceptionTest2.conf");
	    suscriber.suscribirseADato(1);
	    suscriber.escuchar();
	    publisher.send(1, expected, 6);
	    Thread.sleep(10);
	    byte[] actual = suscriber.obtenerDato(1, 6);
	    assertArrayEquals(expected, actual);
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
	public void TestinicializarVariablesFicheroIndexPrivado() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String linea = "puerto=";
		
		Method inicializarVariablesFichero = port.getClass().getDeclaredMethod("inicializarVariablesFichero", String.class);
		inicializarVariablesFichero.setAccessible(true);
		
		try{
			inicializarVariablesFichero.invoke(port,linea);
            fail("Expected an ArrayIndexOutOfBoundsException to be thrown");
        } catch (InvocationTargetException anInvocationTargetException) {
          assertThat(anInvocationTargetException.getCause().getMessage(), is("arrayIndexOutOfBounds"));        
        }  
	}

	@Test
	public void TestbyteArraytoString() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String mensaje = "PruebaTest";
		String expected = "PruebaTest";
		
		Method byteArraytoString = port.getClass().getDeclaredMethod("byteArraytoString", byte[].class);
		byteArraytoString.setAccessible(true);
		String actual = (String) byteArraytoString.invoke(port,mensaje.getBytes());
		assertEquals("failure - byte[] message not correctly cast to String", expected, actual);
	}
	
	@Test
	public void TestSepararString() throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		String mensaje = "mensaje1=mensaje2=mensaje3";
		String separador = "=";
		String [] expected = {"mensaje1","mensaje2","mensaje3"};
		
		Method separarString = port.getClass().getDeclaredMethod("separarString", String.class, String.class);
		separarString.setAccessible(true);
		String [] actual = (String[]) separarString.invoke(port,mensaje,separador);
		assertArrayEquals(expected, actual);
	}
}
