package com.casestudy.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONObject;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

import com.casestudy.obj.product.ProductDetailsObj;
import com.casestudy.obj.product.ProductPriceObj;
import com.casestudy.redis.RedisConnectionSetup;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.ws.rs.PathParam;

//http://localhost:8080/CaseStudy/api/products/13860428
@Path("/products")
public class ProductDetails {

	@Inject
	private RedisConnectionSetup redisConn;

	// get product details
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getProductDetails(@NotNull @PathParam("id") String id) {
		ProductDetailsObj productDtlsObj = new ProductDetailsObj();
		String output;
		RedissonClient redisClient = redisConn.getRedisson();

		RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");

		try {
			ClientConfig config = new ClientConfig();
			config.connectorProvider(new ApacheConnectorProvider());
			Client client = ClientBuilder.newClient(config);
			Response response = client.target(getBaseURI()).path("v2")
					.path("pdp")
					.path("tcin")
					.path(id)
					.property(ClientProperties.FOLLOW_REDIRECTS, true).request(MediaType.APPLICATION_JSON).get(Response.class);

			if (response.getStatus() != 200)
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());

			output = response.readEntity(String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(output);

			JsonNode productNode;
			if (rootNode.has("product"))
				productNode = rootNode.path("product");
			else
				throw new Exception("Sorry. No Product List!!");

			JsonNode itemNode;
			if (productNode.has("item"))
				itemNode = productNode.path("item");
			else
				throw new Exception("Sorry. Product not found!!");

			if (itemNode.has("tcin"))
				productDtlsObj.setId(Integer.parseInt(itemNode.findValue("tcin").textValue()));
			else
				throw new Exception("Sorry. Product id not found!!");

			if (itemNode.has("product_description") && itemNode.path("product_description").has("title"))
				productDtlsObj.setName(itemNode.path("product_description").findValue("title").textValue());
			else
				throw new Exception("Sorry. Product description not found!!");

			ProductDetailsObj productDtlsObjFromRedis = productListInRedis.get(13860428);

			productDtlsObj.setCurrent_price(productDtlsObjFromRedis.getCurrent_price());

		} catch (RuntimeException | IOException e) {
			productDtlsObj.setErrorMsg("Product Not Found");
			e.printStackTrace();
		} catch (Exception e) {
			productDtlsObj.setErrorMsg("Product Not Found");
			e.printStackTrace();
		}

		return Response.status(200).entity(productDtlsObj).build();
	}

	// modify existing data in redis
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response updateProductPrice(ProductDetailsObj productDtlsObj, @NotNull @PathParam("id") String id) {
		JSONObject result = new JSONObject();
		try {
			int productId = Integer.parseInt(id);
			if (productId < 0)
				throw new Exception("Sorry. Product id not valid");

			RedissonClient redisClient = redisConn.getRedisson();

			RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");

			if (productListInRedis.containsKey(productId)) {
				productListInRedis.replace(productId, productDtlsObj);
			} else
				throw new Exception("Sorry. Product id not valid");

			result.put("result", "Success");

		} catch (Exception e) {
			result.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}
		return Response.status(200).entity(result.toString()).build();
	}

	// add new data to redis
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addProduct")
	public Response AddNewProductToRedis(ProductDetailsObj productDtlsObj) {
		JSONObject result = new JSONObject();
		try {

			if (productDtlsObj.getId() < 0)
				throw new Exception("Sorry. Product id not valid");

			RedissonClient redisClient = redisConn.getRedisson();

			RMap<Integer, ProductDetailsObj> productListInRedis = redisClient.getMap("productList");
			productListInRedis.put(productDtlsObj.getId(), productDtlsObj);

			result.put("result", "Success");

		} catch (Exception e) {
			result.put("errorMsg", e.getMessage());
			e.printStackTrace();
		}

		return Response.status(200).entity(result.toString()).build();
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://redsky.target.com").build();
	}

}
