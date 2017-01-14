package com.chemao.imagery.processor.orc;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.Assert.*;

import static org.hamcrest.core.StringContains.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.chemao.imagery.processor.TesseractException;
import com.chemao.imagery.processor.TesseractParameterException;

public class TesseractTest {
	
	private Tesseract tesseract = new Tesseract();
	private String image = TesseractTest.class.getClassLoader().getResource("")
			.getPath().split("imagery-processor")[0] + "images/code.jpg";
	@Rule
	public ExpectedException expected = ExpectedException.none();
	
	private final String CONTENT = "LSVXL25NXD2154258";
	
	@Test
	public void test_orc() throws IOException, TesseractException {
		this.tesseract.setTimeout(10);
		String target = System.getProperty("user.home") + "/code.jpg";
		Files.copy(new File(image).toPath(), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
		assertTrue(this.tesseract.doOcr(target).contains(CONTENT));
		assertFalse(new File(target).exists());
		
		this.tesseract.setFolder(System.getProperty("user.home"));
		assertTrue(this.tesseract.doOcr(new File(image).toURI().toURL().toString()).contains(CONTENT));
	}
	
	@Test
	public void test_param_exception() throws TesseractException {
		this.expected.expect(TesseractParameterException.class);
		this.tesseract.doOcr("http://www.test.com/test.jpg");
	}
	
	@Test
	public void test_timeout_exception() throws TesseractException, MalformedURLException {
		this.tesseract.setTimeout(-1);
		this.tesseract.setFolder(System.getProperty("user.home"));
		this.expected.expect(TesseractException.class);
		this.expected.expectMessage(containsString("timeout"));
		this.tesseract.doOcr(new File(image).toURI().toURL().toString());
	}
}
