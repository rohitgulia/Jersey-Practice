package com.casestudy.api;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONObject;
import com.casestudy.obj.product.ProductDetailsObj;
import com.casestudy.redis.RedisConnectionSetup;
import com.casestudy.service.Iproduct;
import com.casestudy.service.ProductService;
import javax.ws.rs.PathParam;

//http://localhost:8080/CaseStudy/api/products
@Path("/products")
public class ProductDetails {
	
	private Iproduct product;

	@Inject
	RedisConnectionSetup redisConn;

	// get product details
	//http://localhost:8080/CaseStudy/api/products/13860428
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response getProductDetails(@NotNull @PathParam("id") String id) {
		product = new ProductService(redisConn);
		ProductDetailsObj productDtlsObj = product.getProductDetails(id);
		return Response.status(200).entity(productDtlsObj).build();
	}

	// modify existing data in redis
	// http://localhost:8080/CaseStudy/api/products/13860428
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Response updateProductPrice(ProductDetailsObj productDtlsObj, @NotNull @PathParam("id") String id) {
		product = new ProductService(redisConn);
		JSONObject result = product.updateProductDetails(productDtlsObj, id);
		return Response.status(200).entity(result.toString()).build();
	}

	// add new data to redis
	// http://localhost:8080/CaseStudy/api/products/addProduct
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/addProduct")
	public Response addNewProductToRedis(ProductDetailsObj productDtlsObj) {
		product = new ProductService(redisConn);
		JSONObject result = product.addProductDetails(productDtlsObj);
		if (result.has("errorMsg"))
			return Response.status(200).entity(result.toString()).build();
		else
			return Response.status(201).entity(result.toString()).build();
	}

}