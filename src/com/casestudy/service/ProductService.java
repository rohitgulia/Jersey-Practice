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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductService implements Iproduct {
	RedisConnectionSetup redisConn;
	public ProductService(RedisConnectionSetup redisConn) {
		this.redisConn = redisConn;
	}
	
	public ProductService() {}

	@Override
	public ProductDetailsObj getProductDetails(String id) {
		ProductDetailsObj productDtlsObj = new ProductDetailsObj();
		
		String output;
		try {
			RedissonClient redisClient = checkDatabaseConnection();

			RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");

			// get data from external api
			ClientConfig config = new ClientConfig();
			config.connectorProvider(new ApacheConnectorProvider());
			Client client = ClientBuilder.newClient(config);
			Response response = client.target(getBaseURI()).path("v2").path("pdp").path("tcin").path(id)
					.property(ClientProperties.FOLLOW_REDIRECTS, true).request(MediaType.APPLICATION_JSON)
					.get(Response.class);

			if (response.getStatus() != 200)
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());

			output = response.readEntity(String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(output);

			JsonNode productNode;
			if (rootNode.has("product"))
				productNode = rootNode.path("product");
			else
				throw new ProductNotFoundException("Sorry. No Product List!!");

			JsonNode itemNode;
			if (productNode.has("item"))
				itemNode = productNode.path("item");
			else
				throw new ProductNotFoundException("Sorry. Product not found!!");

			if (itemNode.has("tcin"))
				productDtlsObj.setId(Integer.parseInt(itemNode.findValue("tcin").textValue()));
			else
				throw new ProductNotFoundException("Sorry. Product id not found!!");

			if (itemNode.has("product_description") && itemNode.path("product_description").has("title"))
				productDtlsObj.setName(itemNode.path("product_description").findValue("title").textValue());
			else
				throw new ProductNotFoundException("Sorry. Product description not found!!");

			// find product in redis and get price for it
			if (productListInRedis.containsKey(productDtlsObj.getId())) {
				ProductDetailsObj productDtlsObjFromRedis = productListInRedis.get(productDtlsObj.getId());
				productDtlsObj.setCurrent_price(productDtlsObjFromRedis.getCurrent_price());
			}
		} catch (RuntimeException | IOException e) {
			productDtlsObj.setErrorMsg("Product Not Found");
			e.printStackTrace();
		} catch (Exception e) {
			productDtlsObj.setErrorMsg("Product Not Found");
			e.printStackTrace();
		}
		
		return productDtlsObj;
	}

	@Override
	public JSONObject updateProductDetails(ProductDetailsObj productDtlsObj, String id) {
		JSONObject result = new JSONObject();
		try {
			int productId = Integer.parseInt(id);
			RedissonClient redisClient = checkDatabaseConnection();
			
			if(productId < 0 || productDtlsObj.getId() < 0)
				throw new ProductIdNotValidException("Sorry. Product id not valid");
			else if(productDtlsObj.getCurrent_price().getValue() < 0)
				throw new Exception("Sorry. Price cannot be negative");
			
			RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");

			if (productListInRedis.containsKey(productId))
				productListInRedis.replace(productId, productDtlsObj);
			else
				throw new ProductIdNotValidException("Sorry. Product id not valid");

		} catch (ProductIdNotValidException e) {
			result.put("errorMsg", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			result.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		return result.put("result", "success");
	}

	@Override
	public JSONObject addProductDetails(ProductDetailsObj productDtlsObj) {
		JSONObject result = new JSONObject();
		try {
			RedissonClient redisClient = checkDatabaseConnection();
			
			if(productDtlsObj.getId() < 0)
				throw new ProductIdNotValidException("Sorry. Product id not valid");
			else if(productDtlsObj.getCurrent_price().getValue() < 0)
				throw new Exception("Sorry. Price cannot be negative");

			RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");

			if (productListInRedis.containsKey(productDtlsObj.getId()))
				throw new Exception("Sorry. Data already exists");

			productListInRedis.put(productDtlsObj.getId(), productDtlsObj);

		} catch (ProductIdNotValidException e) {
			result.put("errorMsg", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			result.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		return result.put("result", "success");
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://redsky.target.com").build();
	}
	private RedissonClient checkDatabaseConnection() throws Exception {
		if (redisConn == null)
			throw new Exception("Sorry. No Database connection");
		RedissonClient redisClient = redisConn.getRedisson();
		if (redisClient == null)
			throw new Exception("Sorry. No Database connection");
		return redisClient;
	}
	
}
