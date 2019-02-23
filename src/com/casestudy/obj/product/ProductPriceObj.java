package com.casestudy.obj.product;

public class ProductPriceObj implements java.io.Serializable{

	private static final long serialVersionUID = 4608321875719794351L;

	private String value;
	private String currency_code;
	
	public ProductPriceObj(String value, String currency_code) {
		this.value = value;
		this.currency_code = currency_code;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
}
