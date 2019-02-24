package com.casestudy.test;

import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.casestudy.client.Consumer;
import com.casestudy.misc.ProductIdNotValidException;
import com.casestudy.obj.product.ProductDetailsObj;
import com.casestudy.obj.product.ProductPriceObj;

public class ProductDetails_Test {
	public static final int HTTP_CREATED = 201;
    
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
    
    @Test(expected = Exception.class)
    public void ProductDetails_PostRequest_ThrowsProductIdNotValidException() {
    	ProductPriceObj productPriceObj = new ProductPriceObj(22.33, "USD");
    	ProductDetailsObj productDtlsObj = new ProductDetailsObj(-13860428, "Movie", productPriceObj);
    	Consumer apiConsumer = new Consumer();
    	
    	apiConsumer.createNewData(productDtlsObj);
    }
}
