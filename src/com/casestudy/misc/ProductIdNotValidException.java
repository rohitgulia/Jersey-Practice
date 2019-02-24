package com.casestudy.misc;

public class ProductIdNotValidException extends Exception{
	private static final long serialVersionUID = -523311630981220152L;
	
	public ProductIdNotValidException(String errorMsg) {
		super(errorMsg);
	}

}
