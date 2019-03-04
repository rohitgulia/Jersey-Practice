package com.casestudy.repo;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import com.casestudy.misc.ProductIdNotValidException;
import com.casestudy.misc.ProductNotFoundException;
import com.casestudy.obj.product.ProductDetailsObj;
import com.casestudy.redis.RedisConnectionSetup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProductDetailsRepo implements IproductDetailsRepo {

	RedisConnectionSetup redisConn;

	public ProductDetailsRepo(RedisConnectionSetup redisConn) {
		this.redisConn = redisConn;
	}

	public ProductDetailsRepo() {
	}

	public ProductDetailsObj getProductDetailsRepo(Response response) throws Exception {
		String output;
		ProductDetailsObj productDtlsObj = new ProductDetailsObj();
		RedissonClient redisClient = checkDatabaseConnection();

		RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");
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
		return productDtlsObj;
	}

	@Override
	public JSONObject updateProductDetailsRepo(ProductDetailsObj productDtlsObj, String id)
			throws ProductIdNotValidException, Exception {
		JSONObject result = new JSONObject();
		int productId = Integer.parseInt(id);
		RedissonClient redisClient = checkDatabaseConnection();

		if (productId < 0 || productDtlsObj.getId() < 0)
			throw new ProductIdNotValidException("Sorry. Product id not valid");
		else if (productDtlsObj.getCurrent_price().getValue() < 0)
			throw new Exception("Sorry. Price cannot be negative");

		RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");

		if (productListInRedis.containsKey(productId))
			productListInRedis.replace(productId, productDtlsObj);
		else
			throw new ProductIdNotValidException("Sorry. Product id not valid");

		result.put("result", "success");
		return result;
	}

	@Override
	public void addNewProductRepo(ProductDetailsObj productDtlsObj) throws ProductIdNotValidException, Exception {
		RedissonClient redisClient = checkDatabaseConnection();

		if (productDtlsObj.getId() < 0)
			throw new ProductIdNotValidException("Sorry. Product id not valid");
		else if (productDtlsObj.getCurrent_price().getValue() < 0)
			throw new Exception("Sorry. Price cannot be negative");

		RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");

		if (productListInRedis.containsKey(productDtlsObj.getId()))
			throw new Exception("Sorry. Data already exists");

		productListInRedis.put(productDtlsObj.getId(), productDtlsObj);
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
