package com.casestudy.service;

import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import com.casestudy.misc.ProductIdNotValidException;
import com.casestudy.misc.ProductNotFoundException;
import com.casestudy.obj.product.ProductDetailsObj;
import com.casestudy.redis.RedisConnectionSetup;
import com.casestudy.repo.IproductDetailsRepo;
import com.casestudy.repo.ProductDetailsRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductService implements Iproduct {
	IproductDetailsRepo productDtlsRepo;
	public ProductService(RedisConnectionSetup redisConn) {
		productDtlsRepo = new ProductDetailsRepo(redisConn);
	}
	
	public ProductService() {}

	@Override
	public ProductDetailsObj getProductDetails(String id) throws Exception{
			// get data from external api
			ClientConfig config = new ClientConfig();
			config.connectorProvider(new ApacheConnectorProvider());
			Client client = ClientBuilder.newClient(config);
			Response response = client.target(getBaseURI()).path("v2").path("pdp").path("tcin").path(id)
					.property(ClientProperties.FOLLOW_REDIRECTS, true).request(MediaType.APPLICATION_JSON)
					.get(Response.class);

			if (response.getStatus() != 200)
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
		
		return productDtlsRepo.getProductDetailsRepo(response);
	}

	@Override
	public JSONObject updateProductDetails(ProductDetailsObj productDtlsObj, String id) throws ProductIdNotValidException, Exception {
		return productDtlsRepo.updateProductDetailsRepo(productDtlsObj, id);
	}

	@Override
	public void addProductDetails(ProductDetailsObj productDtlsObj) throws ProductIdNotValidException, Exception {
		productDtlsRepo.addNewProductRepo(productDtlsObj);
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://redsky.target.com").build();
	}
	
}