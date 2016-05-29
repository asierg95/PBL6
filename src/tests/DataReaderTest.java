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
	public void testLongitudByteArray() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, UnsupportedEncodingException{
		int expected;
		String mensaje = "prueba";
		byte [] mensajeByte = mensaje.getBytes();
		DataReader datareader = new DataReader(null, null, 100, "=");
		
		Method longitudByteArray = datareader.getClass().getDeclaredMethod("longitudByteArray", byte[].class);
		longitudByteArray.setAccessible(true);
		int actual = (int) longitudByteArray.invoke(datareader, mensajeByte);
		expected = mensajeByte.length;
		assertEquals("failure - byte [] size not correctly calculated", expected, actual);
	}
	
	DataReader ca;
	DataReader ic;
	
	@Before
	public void inicializar(){
		ic = org.easymock.EasyMock.strictMock(DataReader.class);
	}

	@Test
	public void testCuentaAtras() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException{
		
		ic.ejecutar();
		org.easymock.EasyMock.replay();
		
		ca = new DataReader(null, null, 100, "=");
		ca.run();
		org.easymock.EasyMock.verify();
	}
	
	
}
