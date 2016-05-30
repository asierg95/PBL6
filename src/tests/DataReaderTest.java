package tests;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JLabel;

import org.easymock.EasyMockSupport;

import org.junit.Before;
import org.junit.Test;

import middleware.DataReader;

public class DataReaderTest {
	
	DataReader drMock;
	DataReader dr;
	
	@Before
	public void inicializar(){
		dr = org.easymock.EasyMock.strictMock(DataReader.class);
	}

	@Test
	public void testStart() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException{
		dr.ejecutar();
		org.easymock.EasyMock.replay();
		
		drMock = new DataReader(null, null, 100, "=");
		drMock.start();
		org.easymock.EasyMock.verify();
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
	
	
	
}
