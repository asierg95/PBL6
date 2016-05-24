package tests;

import org.junit.Before;
import org.junit.Test;

import middleware.PsPort;

public class PsPortTest {
	PsPort port;
	
	@Before
	public void setUp(){
		port = new PsPort("middleware.conf");
	}
	
	@Test
	  public void testAssertArrayEquals() {
	    byte[] expected = "1=kaixo".getBytes();
	    byte[] actual = port.crearMensaje(1, "kaixo".getBytes());
	    org.junit.Assert.assertArrayEquals("failure - byte arrays not same", expected, actual);
	  }
	
	

}
