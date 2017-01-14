package com.chemao.imagery.processor;

public class TesseractParameterException extends RuntimeException {

	private static final long serialVersionUID = 3459483306161425726L;

	public TesseractParameterException(String message) {
		super(message);
	}
	
	public TesseractParameterException(String message, Throwable e) {
		super(message, e);
	}

}
