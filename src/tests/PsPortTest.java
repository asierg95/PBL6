package tests;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

import middleware.DataReader;
import middleware.PsPort;

import org.easymock.EasyMockSupport;

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
		 org.junit.Assert.assertEquals("Failure - conexion does not start correctly", expected, actual);
	}
	
	@Test
	  public void testCrearMensaje() {
	    byte[] expected = "1=testeo".getBytes();
	    byte[] actual = port.crearMensaje(1, "testeo".getBytes());
	    org.junit.Assert.assertArrayEquals("failure - byte arrays not same", expected, actual);
	  }
	
	@Test
	  public void testPublish() {
	    boolean expected = true;
	    port.start();
	    boolean actual = port.publish(1, "123456".getBytes(), 6);
	    org.junit.Assert.assertEquals("failure - message not published", expected, actual);
	}	
	
	@Test
	  public void testGetLastSample() {
	    String expected = "123456";
	    port.start();
	    port.guardarDato(1, "123456");
	    String actual = port.getLastSample(1, 6);
	    org.junit.Assert.assertEquals("failure - message saved not same as readed", expected, actual);
	}	
	
	@Test
	  public void testCrearConexion() {
	    boolean expected = true;
	    port.start();
	    boolean actual = port.crearConexion(6868);
	    org.junit.Assert.assertEquals("failure - conexion not created", expected, actual);
	}
	
	

	/*
	@Test
	public void testGuardarTiempoEspera() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException{
	
		dataReader.start();
		replayAll();
		
		Constructor<ClienteSocket> constructor = ClienteSocket.class.getDeclaredConstructor(new Class[] {Socket.class, InterfazCliente.class});
		constructor.setAccessible(true);
		client = constructor.newInstance(new Socket(),ic);
		client.guardarTiempoEspera("50");
		verifyAll();
	}*/
	
	
	
	
	

}
