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
		fichConfigGood = "exceptionTest2.conf";
		fichConfigBad = "exceptionTest.conf";
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
		boolean expected = true;
		publish.iniciarConexion(fichConfigGood);
		boolean recived = publish.send(1, "kaixoo".getBytes(), 6);
		org.junit.Assert.assertEquals(expected, recived);
		
	}
	
	@Test
	public void testSendSmallLength(){
	    boolean expected = false;
		publish.iniciarConexion(fichConfigGood);
		boolean recived = publish.send(1, "kaixoo".getBytes(), 4);
		org.junit.Assert.assertEquals(expected, recived);
		
	}
	
	@Test
    public void testSendBigLength(){
	    boolean expected = false;
        publish.iniciarConexion(fichConfigGood);
        boolean recived = publish.send(1, "kaixoo".getBytes(), 8);

        org.junit.Assert.assertEquals(expected, recived);
        
    }
	
	@Test
    public void testSendSmallMsg(){
	    boolean expected = false;
        publish.iniciarConexion(fichConfigGood);
        boolean recived = publish.send(1, "kaix".getBytes(), 6);

        org.junit.Assert.assertEquals(expected, recived);
        
    }
    
    @Test
    public void testSendBigMsg(){
        boolean expected = false;
        publish.iniciarConexion(fichConfigGood);
        boolean recived = publish.send(1, "kaixoooo".getBytes(), 6);
        org.junit.Assert.assertEquals(expected, recived);
        
    }
	
	@Test
	public void testSendLengthNum(){
	    boolean expected = false;
		publish.iniciarConexion(fichConfigBad);
		boolean recived = publish.send(1, "kaixoo".getBytes(), 6);

		org.junit.Assert.assertEquals(expected, recived);
		
	}

}
