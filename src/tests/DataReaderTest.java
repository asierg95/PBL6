package tests;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;

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
		DataReader datareader = new DataReader(null, null, 100, "=");
		
		Method copiarByteArray = datareader.getClass().getDeclaredMethod("copiarByteArray", byte[].class, int.class);
		copiarByteArray.setAccessible(true);
		byte[] copia = (byte[]) copiarByteArray.invoke(datareader, mensaje.getBytes(), mensaje.getBytes().length);
		String actual = new String(copia, "UTF-8");
		
		assertEquals("failure - byte [] not correctly copied", expected, actual);
	}
	
	@Test
	public void testEjecutar(){
		DataReader datareader = new DataReader(null, null, 100, "=");
		datareader.setExit(true);
		
		datareader.ejecutar();
	}
	
	@Test
	public void testguardarMensaje() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException{
		String datoTest = "testeo";
		DatagramPacket paquete = new DatagramPacket(datoTest.getBytes(),datoTest.length());
		paquete.getData();	
		
		Constructor<PsPort> constructor = PsPort.class.getDeclaredConstructor(new Class[] {String.class});
		constructor.setAccessible(true);
		PsPort port = constructor.newInstance("middleware.conf");
		
		DataReader dr = new DataReader(null, null, 100, "=");
		PsPort drMock;
		
		drMock = strictMock(PsPort.class);
		
		drMock.guardarDato(datoTest.getBytes());
		replayAll();
		
		dr.guardarMensaje(paquete, port);
		verifyAll();
		
		
	}
	
	
	
}
