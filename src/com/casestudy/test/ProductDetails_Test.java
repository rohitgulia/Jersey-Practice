package com.casestudy.test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.redisson.Redisson;
import org.redisson.api.RMap;

import com.casestudy.api.ProductDetails;
import com.casestudy.client.Consumer;
import com.casestudy.client.Consumer_Test;
import com.casestudy.misc.ProductIdNotValidException;
import com.casestudy.obj.product.ProductDetailsObj;
import com.casestudy.obj.product.ProductPriceObj;
import com.casestudy.redis.RedisConnectionSetup;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.ArgumentMatchers.eq;

public class ProductDetails_Test {
    
	private Consumer_Test consumerTest;
	
//    @Mock
//    RedisConnectionSetup redisConnection;
//    @Mock
//    RMap rmap;
//    @Mock
//    Redisson redisson;
//    @InjectMocks
//    ProductDetails productDtls;
	
    @Rule
    public ExpectedException expectdException = ExpectedException.none();
    
    @Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		consumerTest = new Consumer_Test();
    }
    
//    @Test
//    public void ProductDetails_PostRequest_DataAdded() {
//    	ProductPriceObj productPriceObj = new ProductPriceObj(22.33, "USD");
//    	ProductDetailsObj productDtlsObj = new ProductDetailsObj(60428, "Movie", productPriceObj);
//    	Consumer apiConsumer = new Consumer();
//    	
//    	Response response = apiConsumer.createNewData(productDtlsObj);
//    	
//    	Assert.assertEquals(response.getStatus(), HTTP_CREATED);
//    }
    
    @Test
    public void addNewProductToRedis_negativeProductId_ThrowsProductIdNotValidException() throws ProductIdNotValidException, Exception {
    	expectdException.expect(ProductIdNotValidException.class);
    	
//    	Mockito.when(redisConnection.getRedisson()).thenReturn(redisson);
//    	Mockito.when(rmap.containsKey(anyInt())).thenReturn(true);
//    	Mockito.when(redisConnection.getRedisson().getMap(anyString())).thenReturn(rmap);
//    	
    	//Mockito.when(redisConnection.getRedisson().getMap(anyString()).put(anyDouble(), refEq(productDtlsObj))).thenReturn(anyString());
    	//Mockito.doReturn(anyInt()).when(redisConnection.getRedisson());
    	
    	//Mockito.doReturn(true).when(rmap.containsKey(anyInt()));
    	//Mockito.doReturn(anyInt()).when(redisConnection.getRedisson().getMap(anyString()));
    	//Mockito.doReturn(anyString()).when(redisConnection.getRedisson().getMap(anyString()).put(anyDouble(), productDtlsObj));
    	
    	consumerTest.createNewData_ThrowException();
    }
}
