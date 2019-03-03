package com.casestudy.service;

import org.json.JSONObject;

import com.casestudy.obj.product.ProductDetailsObj;

public interface Iproduct {
	public ProductDetailsObj getProductDetails(String id);
	public JSONObject updateProductDetails(ProductDetailsObj productDtlsObj, String id);
	public JSONObject addProductDetails(ProductDetailsObj productDtlsObj);
}
