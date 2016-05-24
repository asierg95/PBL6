package tests;

import org.junit.Test;

import middleware.PsPort;

public class PsPortTest {
	
	@Test
	  public void testAssertArrayEquals() {
		PsPort port = new PsPort("middleware.conf");
	    byte[] expected = "1=kaixo".getBytes();
	    byte[] actual = port.crearMensaje(1, "kaixo".getBytes());
	    org.junit.Assert.assertArrayEquals("failure - byte arrays not same", expected, actual);
	  }

}
