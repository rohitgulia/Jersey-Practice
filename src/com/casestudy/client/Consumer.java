package com.casestudy.client;

import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.casestudy.obj.product.ProductDetailsObj;
import com.casestudy.obj.product.ProductPriceObj;

public class Consumer {

	private static final String URL = "http://localhost:8080/CaseStudy/api/products";

	private Client client = ClientBuilder.newClient();

	public void createNewData() {
		ProductPriceObj productPriceObj = new ProductPriceObj(22.33, "USD");
		ProductDetailsObj productDtlsObj = new ProductDetailsObj(13860428, "Movie", productPriceObj);
		Response response = client.target(URL).path(String.valueOf("addProduct")).request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(productDtlsObj, MediaType.APPLICATION_JSON));

		System.out.println("Status: "+response.getStatus());
	}

	public void updateData() {
		ProductPriceObj productPriceObj = new ProductPriceObj(42.33, "CAD");
		ProductDetailsObj productDtlsObj = new ProductDetailsObj(13860428, "Movie2", productPriceObj);
		Response response = client.target(URL).path("13860428").request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(productDtlsObj, MediaType.APPLICATION_JSON));

		System.out.println("Status: "+response.getStatus());
	}

	public void getData() {
		Response response = client.target(URL).path("13860428").request(MediaType.APPLICATION_JSON).get();

		ProductDetailsObj productDtlsObj = response.readEntity(ProductDetailsObj.class);
		System.out.println("Status: "+ response.getStatus());
		System.out.println("Id: "+productDtlsObj.getId() +"\n"+ "Name: "+ productDtlsObj.getName() + "\n" + "Value: "+ productDtlsObj.getCurrent_price().getValue());
	}

	public static void main(String[] args) {
		System.out.println("Please enter your choice.");
		System.out.println("1. Create Data");
		System.out.println("2. Update Data");
		System.out.println("3. Get Data");

		Consumer consumer = new Consumer();

		Scanner reader = new Scanner(System.in);
		int choice = reader.nextInt();

		try {
			switch (choice) {
			case 1:
				consumer.createNewData();
				break;
			case 2:
				consumer.updateData();
				break;
			case 3:
				consumer.getData();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
	}
}
