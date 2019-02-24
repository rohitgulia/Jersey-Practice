package com.casestudy.misc;

public class ProductNotFoundException extends Exception{
	private static final long serialVersionUID = -505936309342057188L;

	public ProductNotFoundException(String errorMsg) {
		super(errorMsg);
	}
}
