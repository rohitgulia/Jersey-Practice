package com.casestudy.service;

import org.json.JSONObject;

import com.casestudy.misc.ProductIdNotValidException;
import com.casestudy.obj.product.ProductDetailsObj;

public interface Iproduct {
	public ProductDetailsObj getProductDetails(String id) throws Exception;
	public JSONObject updateProductDetails(ProductDetailsObj productDtlsObj, String id) throws ProductIdNotValidException, Exception;
	public void addProductDetails(ProductDetailsObj productDtlsObj) throws ProductIdNotValidException, Exception;
}
