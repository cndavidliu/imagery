package com.chemao.imagery.processor;

public class TesseractException extends Exception {

	private static final long serialVersionUID = -7565492380357810423L;
	
	public TesseractException(String message) {
		super(message);
	}

	public TesseractException(Throwable e) {
		super(e);
	}

	public TesseractException(String message, Throwable e) {
		super(message, e);
	}

}
