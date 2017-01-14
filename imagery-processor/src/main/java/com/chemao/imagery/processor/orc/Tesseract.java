package com.chemao.imagery.processor.orc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.chemao.imagery.processor.TesseractException;
import com.chemao.imagery.processor.TesseractParameterException;

public class Tesseract {
	
	private static Logger logger = LogManager.getLogger(Tesseract.class);
	/**
	 *  folder to store local picture files
	 */
	private String folder = "/opt/pic";
	private int timeout = 10;
	
	public String doOcr(String address) throws TesseractException {
		URL url = null;
		try {
			url = new URL(address);
		} catch (MalformedURLException e) {
			logger.info("the address {} is not a url, take it as local path.", address);
			return this.doOcr(new File(address.trim()));
		}
		File file = new File(String.format("%s/%d_%s", 
				this.folder, System.currentTimeMillis(), url.getFile().replaceAll("/", "_").trim()));
		try (InputStream in = url.openStream()) {
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new TesseractParameterException("url's inputstream reads error", e);
		}
		return this.doOcr(file);
	}
	
	public String doOcr(File image) throws TesseractException {
		this.preOrc(image);
		String command = String.format("tesseract %s stdout", image.getAbsolutePath());
		ProcessBuilder builder = new ProcessBuilder(new String[]{"bash", "-c", command});
		Process process = null;
		try {
			process = builder.start();
			if(!process.waitFor(this.timeout, TimeUnit.SECONDS)) {
				throw new TesseractException("tesseract orc timeout");
			}
			Scanner scanner = new Scanner(process.getInputStream());
			scanner.useDelimiter("\\A");
			String result = scanner.hasNext() ? scanner.next() : "";
			scanner.close();
			return result;
		} catch (Exception e) {
			if (e instanceof TesseractException)
				throw (TesseractException)e;
			throw new TesseractException(e);
		} finally {
			if (process != null && process.isAlive()) {
				process.destroy();
			}
			image.delete();
		}
	}
	
	private void preOrc(File image) throws TesseractParameterException {
		if (image.getName().contains(" ")) 
			throw new TesseractParameterException("adress contains illegal character");
		if (!image.exists())
			throw new TesseractParameterException("image doesn't exist");
	}
	
	public String getFolder() {
		return this.folder;
	}
	
	public void setFolder(String folder) {
		this.folder = folder;
	}
	
	public int getTimeout() {
		return this.timeout;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
