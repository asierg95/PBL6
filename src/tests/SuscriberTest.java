package tests;

import middleware.Publisher;
import middleware.Suscriber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SuscriberTest {

	Suscriber suscriber;
	Publisher publish;
	String fichConfigGood, fichConfigBad;
	int idDato;
	
	@Before
	public void setUp(){
		suscriber = new Suscriber();
		publish = new Publisher();
		fichConfigGood = "exceptionTest2.conf";
		fichConfigBad = "exceptionTest.conf";
		idDato = 1;
	}
	
	@After
	public void setDown(){
		suscriber = null;
		publish = null;
		fichConfigGood = null;
		fichConfigBad = null;
		
	}
	
	@Test
	public void testIniciarConexionGood(){
		boolean recived = suscriber.iniciarConexion(fichConfigGood); 
		org.junit.Assert.assertTrue("Fallo, el mensaje no se ha enviado", recived);
	}
	
	@Test
	public void testSuscribirseADato(){
		suscriber.iniciarConexion(fichConfigGood);
		boolean actual = suscriber.suscribirseADato(idDato);
		org.junit.Assert.assertTrue("Fallo, el mensaje no se ha enviado", actual);		
	}
	
	@Test
	public void testEscuchar(){
		suscriber.iniciarConexion(fichConfigGood);
		
	}
	
	@Test
	public void testObtenerDato() throws InterruptedException{
		suscriber.iniciarConexion(fichConfigGood);
		suscriber.suscribirseADato(idDato);
		suscriber.escuchar();
		publish.iniciarConexion(fichConfigGood);
		publish.send(idDato, "kaixoo", 6);
		String expected = "kaixoo";
		Thread.sleep(10);
		String actual = suscriber.obtenerDato(idDato, 6);
		
		org.junit.Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testObtenerDatoLength() throws InterruptedException{
		suscriber.iniciarConexion(fichConfigGood);
		suscriber.suscribirseADato(idDato);
		suscriber.escuchar();
		publish.iniciarConexion(fichConfigGood);
		publish.send(idDato, "kaixoo", 6);
		String expected = "-1";
		Thread.sleep(10);
		String actual = suscriber.obtenerDato(idDato, 4);
		
		org.junit.Assert.assertEquals(expected, actual);
	}
}
