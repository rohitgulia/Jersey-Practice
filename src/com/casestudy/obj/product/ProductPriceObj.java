package com.casestudy.obj.product;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductPriceObj implements java.io.Serializable{

	private static final long serialVersionUID = 4608321875719794351L;

	private double value;
	private String currency_code;
	
	public ProductPriceObj(double value, String currency_code) {
		this.value = value;
		this.currency_code = currency_code;
	}
	
	public ProductPriceObj() {}
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getCurrency_code() {
		return currency_code;
	}
	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}
}
