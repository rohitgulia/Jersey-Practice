package com.casestudy.client;

import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.casestudy.misc.ProductIdNotValidException;
import com.casestudy.obj.product.ProductDetailsObj;
import com.casestudy.obj.product.ProductPriceObj;

public class Consumer_Test {

	private static final String URL = "http://localhost:8080/CaseStudy/api/products";

	private Client client = ClientBuilder.newClient();

	public void createNewData_invalidProductId_throwException() throws ProductIdNotValidException, Exception {
		ProductPriceObj productPriceObj = new ProductPriceObj(22.33, "USD");
		ProductDetailsObj productDtlsObj = new ProductDetailsObj(-13860428, "Movie", productPriceObj);
		client.target(URL).path(String.valueOf("addProduct")).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(productDtlsObj, MediaType.APPLICATION_JSON));
	}

	public void createNewData_invalidPriceValue_throwException() throws ProductIdNotValidException, Exception {
		ProductPriceObj productPriceObj = new ProductPriceObj(-22.33, "USD");
		ProductDetailsObj productDtlsObj = new ProductDetailsObj(13860428, "Movie", productPriceObj);
		client.target(URL).path(String.valueOf("addProduct")).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(productDtlsObj, MediaType.APPLICATION_JSON));
	}

	public void updateData_invalidProductId_throwException() throws ProductIdNotValidException, Exception {
		ProductPriceObj productPriceObj = new ProductPriceObj(42.33, "CAD");
		ProductDetailsObj productDtlsObj = new ProductDetailsObj(-13860428, "Movie2", productPriceObj);
		client.target(URL).path("13860428").request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(productDtlsObj, MediaType.APPLICATION_JSON));
	}

	public void updateData_invalidPriceValue_throwException() throws ProductIdNotValidException, Exception {
		ProductPriceObj productPriceObj = new ProductPriceObj(-42.33, "CAD");
		ProductDetailsObj productDtlsObj = new ProductDetailsObj(13860428, "Movie2", productPriceObj);
		client.target(URL).path("13860").request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(productDtlsObj, MediaType.APPLICATION_JSON));
	}
	
	public void getData() {
		Response response = client.target(URL).path("13860428").request(MediaType.APPLICATION_JSON).get();

		ProductDetailsObj productDtlsObj = response.readEntity(ProductDetailsObj.class);
		System.out.println("Status: " + response.getStatus());
		System.out.println("Id: " + productDtlsObj.getId() + "\n" + "Name: " + productDtlsObj.getName() + "\n"
				+ "Value: " + productDtlsObj.getCurrent_price().getValue());
	}
}
