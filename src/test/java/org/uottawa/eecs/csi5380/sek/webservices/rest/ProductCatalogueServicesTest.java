package org.uottawa.eecs.csi5380.sek.webservices.rest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uottawa.eecs.csi5380.sek.model.Book;
import org.uottawa.eecs.csi5380.sek.utils.ClientHelper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * This class to test all the RESTFul APIs for Product Catalogue Services
 * @author Kuntal Ghosh
 *
 */

public class ProductCatalogueServicesTest {
	
	private static Client aJerseyClient;
	
	//setting up
	@BeforeClass
	public static void setUp() {
		aJerseyClient  = ClientHelper.createClient();
	}
	
	// It tests the product catalogue service for list of all books in the store
	@Test
	public void testAllBooks() throws JsonParseException, org.codehaus.jackson.map.JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException {
		
		WebResource webResource = aJerseyClient
				   .resource("https://localhost:8443/bookstore/rest/product_catalogue/books");

				ClientResponse response = webResource.accept("application/json")
		                   .get(ClientResponse.class);
				ObjectMapper objectMapper = new ObjectMapper();
				List<Book> bookList = objectMapper.readValue(response.getEntity(String.class), new TypeReference<List<Book>>(){});
				
				assertTrue("total number of books in DB:", bookList.size() >= 6);
	}
	
	//It tests the product catalogue service for list of distinct book categories in the store
	@Test
	public void testAllCategories() throws JsonParseException, org.codehaus.jackson.map.JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException {
		WebResource webResource = aJerseyClient
				   .resource("https://localhost:8443/bookstore/rest/product_catalogue/categories");

				ClientResponse response = webResource.accept("application/json")
		                   .get(ClientResponse.class);
				ObjectMapper objectMapper = new ObjectMapper();
				List<String> categories = objectMapper.readValue(response.getEntity(String.class), new TypeReference<List<String>>(){});
				
				assertTrue("total number of distinct categories in book table:", categories.size() >= 4);
	}
	
	//It tests the product catalogue service for finding a book by its id in the store
	@Test
	public void testFindBookById() throws JsonParseException, org.codehaus.jackson.map.JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException {
		WebResource webResource = aJerseyClient
				   .resource("https://localhost:8443/bookstore/rest/product_catalogue/book/0132350882");

				ClientResponse response = webResource.accept("application/json")
		                   .get(ClientResponse.class);
				ObjectMapper objectMapper = new ObjectMapper();
				List<Book> bookList = objectMapper.readValue(response.getEntity(String.class), new TypeReference<List<Book>>(){});
				
				assertTrue("total number of distinct categories in book table:", bookList.size() == 1);
	}
	
	//It tests the product catalogue service for finding a book by category in the store
	@Test
	public void testFindBookByCategory() throws JsonParseException, org.codehaus.jackson.map.JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException {
		WebResource webResource = aJerseyClient
				   .resource("https://localhost:8443/bookstore/rest/product_catalogue/books/Computers");

				ClientResponse response = webResource.accept("application/json")
		                   .get(ClientResponse.class);
				ObjectMapper objectMapper = new ObjectMapper();
				List<Book> bookList = objectMapper.readValue(response.getEntity(String.class), new TypeReference<List<Book>>(){});
				
				assertTrue("total number of distinct categories in book table:", bookList.size() == 2);
	}
}
