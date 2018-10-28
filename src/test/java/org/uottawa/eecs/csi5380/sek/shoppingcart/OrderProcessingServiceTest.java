package org.uottawa.eecs.csi5380.sek.shoppingcart;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uottawa.eecs.csi5380.sek.utils.ClientHelper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class OrderProcessingServiceTest {
	
	private static Client aJerseyClient;
	
	@BeforeClass
	public static void setUp() {
		aJerseyClient  = ClientHelper.createClient();
	}
	
	@Test
	public void testUserSignIn() throws JsonParseException, JsonMappingException, ClientHandlerException, UniformInterfaceException, IOException {
		WebResource webResource = aJerseyClient
				   .resource("https://localhost:8443/bookstore/rest/order_processor/signin");
		
		String input = "{\"userName\": \"kghosh\", "
                + "\"password\":\"12345\"}";

		ClientResponse response = webResource.accept("application/json")
		.type("application/json").post(ClientResponse.class, input);
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.getEntity(String.class));
		String msg = jsonNode.get("msg").asText();
				
		assertTrue("User successfully signed in!", msg.equalsIgnoreCase("Sign in Successful"));
	}
	
	@Test
	public void testProcessCard() throws JsonProcessingException, ClientHandlerException, UniformInterfaceException, IOException {
		
		final long INVALID_CREDIT_CARD = 0000000000000000L;
		
		WebResource webResource = ClientHelper.createClient()
				   .resource("https://localhost:8443/bookstore/rest/order_processor/process_card").queryParam("credit_card", Long.toString(INVALID_CREDIT_CARD));
		
		ClientResponse response = webResource.accept("application/json")
				.post(ClientResponse.class);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(response.getEntity(String.class));
		String msg = jsonNode.get("msg").asText();
		assertTrue("Credit card transaction unsuccessful!", msg.equalsIgnoreCase("Credit card transaction unsuccessful!"));
		response.close();
	}
}
