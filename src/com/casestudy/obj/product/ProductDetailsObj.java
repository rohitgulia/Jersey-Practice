package com.casestudy.obj.product;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProductDetailsObj implements java.io.Serializable{

	private static final long serialVersionUID = 1996030128960812951L;
	
	private int id;
	private String name;
	private ProductPriceObj current_price;
	private String errorMsg;
	
	public ProductDetailsObj(Integer id, String name, ProductPriceObj current_price) {
		this.id = id;
		this.name = name;
		this.current_price = current_price;
	}
	
	public ProductDetailsObj(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	public ProductDetailsObj() {}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ProductPriceObj getCurrent_price() {
		return current_price;
	}
	public void setCurrent_price(ProductPriceObj current_price) {
		this.current_price = current_price;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}