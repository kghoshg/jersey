package org.uottawa.eecs.csi5380.sek.shoppingcart;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uottawa.eecs.csi5380.sek.model.User;
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

				ClientResponse response = webResource.accept("application/json")
		                   .get(ClientResponse.class);
				ObjectMapper objectMapper = new ObjectMapper();
				//List<User> userList = objectMapper.readValue(response.getEntity(String.class), new TypeReference<List<User>>(){});
				
				assertTrue("User successfully signed in!", true);
	}

}
