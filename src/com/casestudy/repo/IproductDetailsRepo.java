package com.casestudy.repo;

import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.casestudy.misc.ProductIdNotValidException;
import com.casestudy.obj.product.ProductDetailsObj;

public interface IproductDetailsRepo {
	public ProductDetailsObj getProductDetailsRepo(Response response) throws Exception;
	public JSONObject updateProductDetailsRepo(ProductDetailsObj productDtlsObj, String id) throws ProductIdNotValidException, Exception;
	public void addNewProductRepo(ProductDetailsObj productDtlsObj) throws ProductIdNotValidException, Exception;
}
