package tests;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;

import javax.crypto.Cipher;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Test;

import middleware.DataReader;
import middleware.PsPort;

public class DataReaderTest extends EasyMockSupport{

	@Test
	public void testStart(){
		DataReader dataMock = EasyMock.createMockBuilder(DataReader.class).addMockedMethod("ejecutar").createMock();
		dataMock.ejecutar();
		EasyMock.expectLastCall().once();
		EasyMock.replay(dataMock);
		   
		dataMock.run();
		EasyMock.verify(dataMock);
	}
	
	@Test
	public void testCopiarByteArray() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, UnsupportedEncodingException{
		String expected = "prueba";
		String mensaje = "prueba";
		DataReader datareader = new DataReader(null, null, 100, "=",null);
		
		Method copiarByteArray = datareader.getClass().getDeclaredMethod("copiarByteArray", byte[].class, int.class);
		copiarByteArray.setAccessible(true);
		byte[] copia = (byte[]) copiarByteArray.invoke(datareader, mensaje.getBytes(), mensaje.getBytes().length);
		String actual = new String(copia, "UTF-8");
		
		assertEquals("failure - byte [] not correctly copied", expected, actual);
	}
	
	@Test
	public void testEjecutar(){
		DataReader datareader = new DataReader(null, null, 100, "=", null);
		datareader.setExit(true);
		
		datareader.ejecutar();
	}
	
	@Test
	public void testguardarMensaje() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException{
		
		String datoTest = "1=123456";	
		
		Constructor<PsPort> constructor = PsPort.class.getDeclaredConstructor(new Class[] {String.class});
		constructor.setAccessible(true);
		PsPort port = constructor.newInstance("middleware.conf");
		
		DataReader dr = new DataReader(port, null, 100, "=",null);
		PsPort drMock;
		
		int hash1 = datoTest.hashCode();
        String hashString1 = String.valueOf(hash1);
        String combinedIdDataHashString1 = datoTest +"="+ hashString1;
		
		drMock = strictMock(PsPort.class);
		byte[] datoByte = port.encriptarDesencriptarMensaje(combinedIdDataHashString1.getBytes(), Cipher.ENCRYPT_MODE);
		drMock.guardarDato(datoByte);
		replayAll();
		
		int hash = datoTest.hashCode();
        String hashString = String.valueOf(hash);
        String combinedIdDataHashString = datoTest +"="+ hashString;
        
		byte[] datoByte2 = port.encriptarDesencriptarMensaje(combinedIdDataHashString.getBytes(), Cipher.ENCRYPT_MODE);
        
        DatagramPacket paquete = new DatagramPacket(datoByte2,datoByte2.length);
		paquete.getData();
		
		dr.guardarMensaje(paquete, port);
		verifyAll();
		
		
		
	}
	
	
	
}
