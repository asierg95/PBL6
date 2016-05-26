package tests;

import middleware.Publisher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PublisherTest {
	
	Publisher publish;
	String fichConfigGood, fichConfigBad;
	
	@Before
	public void setUp(){
		publish = new Publisher();
		fichConfigGood = "middleware.conf";
		fichConfigBad = "midelguer.conf";
	}
	
	@After
	public void setDown(){
		publish = null;
		fichConfigGood = null;
		fichConfigBad = null;
		
	}
	
	@Test
	public void testIniciarConexionGood(){
		boolean recived =publish.iniciarConexion(fichConfigGood); 
		org.junit.Assert.assertTrue("Fallo, el mensaje no se ha enviado", recived);
	}
	
	@Test
	public void testSendOK(){
		int expected = 0;
		publish.iniciarConexion(fichConfigGood);
		int recived = publish.send(1, "kaixoo", 6);
		org.junit.Assert.assertEquals(expected, recived);
		
	}
	
	@Test
	public void testSendLengthMsg(){
		int expected = 1;
		publish.iniciarConexion(fichConfigGood);
		int recived = publish.send(1, "asdf", 6);
		org.junit.Assert.assertEquals(expected, recived);
		
	}
	
	@Test
	public void testSendLengthNum(){
		int expected = 1;
		publish.iniciarConexion(fichConfigGood);
		int recived = publish.send(0, "kaixoo", 6);
		org.junit.Assert.assertEquals(expected, recived);
		
	}

}
