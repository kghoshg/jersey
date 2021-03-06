package org.uottawa.eecs.csi5380.sek.ws.rest;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.uottawa.eecs.csi5380.sek.model.Address;
import org.uottawa.eecs.csi5380.sek.model.User;
import org.uottawa.eecs.csi5380.sek.utils.DBUtils;

/**
 * REST API for Order Processing Service
 */

@Path("/order_processor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderProcessingService {
	
	HttpSession session;
	private static EntityManager em = DBUtils.getEntityManager();
	private final long VALID_CREDIT_CARD = 1234567890123456L;

	public OrderProcessingService(@Context HttpServletRequest request) {
		this.session = request.getSession(true);
	}
	
	/**
	 * It signs in a user
	 * @param user_name
	 * @param password
	 * @return a Response object
	 * @throws JSONException
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@POST
	@Path("/signin")
	public Response signIn(String form_data)
			throws JSONException, JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		User formData = objectMapper.readValue(form_data, new TypeReference<User>(){});
		List<User> user = em.createNamedQuery("User.findByUserNamePassword", User.class)
				.setParameter("user_name", formData.getUserName())
				.setParameter("password", formData.getPassword())
				.getResultList();
		if (!user.isEmpty()) {
			session.setAttribute("signedInUser", user.get(0));
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Sign in Successful")
					.put("user_id", user.get(0).getId()))
					.build();
		} else {
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Sign in unsuccessful")).build();
		}
	}
	
	/**
	 * It creates a user account
	 * @param String form_data
	 * @return a Response object
	 * @throws JSONException
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@POST
	@Path("/create_account")
	public Response createAccount(String form_data) throws JSONException, JsonParseException, JsonMappingException, IOException {
		
		ObjectMapper objectMapper = new ObjectMapper();
		User formData = objectMapper.readValue(form_data, new TypeReference<User>(){});
		List<User> user = em.createNamedQuery("User.findByUserName", User.class)
				.setParameter("user_name", formData.getUserName())
				.getResultList();
		//checks if the username already exists
		if (!user.isEmpty()) {
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "username has already been taken"))
					.build();
		} else {
			try {
				User aUser = new User();
				aUser.setUserName(formData.getUserName());
				aUser.setPassword(formData.getPassword());
				aUser.setFirstName(formData.getFirstName());
				aUser.setLastName(formData.getLastName());
				//associating addresses to user
				for (Address address : formData.getAddress()) {
					address.setUser(aUser);
				}
				aUser.setAddress(formData.getAddress());
				//persisting object
				em.persist(aUser);
				em.getTransaction().commit();
				//makes the newly created user automatically signed in
				session.setAttribute("signedInUser", aUser);
			} catch (Exception e) {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity(new JSONObject().put("msg", e.getMessage()))
						.build();
			}
			return Response.status(Status.CREATED)
					.entity(new JSONObject().put("msg", "account successfully created!")).build();
		}
	}
	
	/**
	 * It processes the credit card transaction
	 * @throws JSONException 
	 */
	@POST
	@Path("/process_card")
	public Response processCard(@QueryParam("credit_card") String credit_card) throws JSONException {

		//increases the counter by 1 every time a credit card transaction is processed
		if(session.getAttribute("numberOfProcessing") != null) {
			session.setAttribute("numberOfProcessing", Integer.parseInt(session.getAttribute("numberOfProcessing").toString()) + 1);
		}else {
			session.setAttribute("numberOfProcessing", 1);
		}
		
		if(!Long.toString(VALID_CREDIT_CARD).equals(credit_card) && session.getAttribute("numberOfProcessing").toString() != "5"){
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Credit card transaction unsuccessful!")).build();
		}else if (session.getAttribute("numberOfProcessing").toString() == "5") {
			session.setAttribute("numberOfProcessing", 0);
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "It is the 5th transaction, so cancelled!")).build();
		}else {
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Credit card transaction successful!")).build();
		}
	}
	
	/**It finds the deatils of a user by his/her id
	 */
	@POST
	@Path("/user_detail/{user_id}")
	public List<User> userDetailById(@PathParam("user_id") int user_id) {
		
		List<User> user = em.createNamedQuery("User.findById", User.class)
				.setParameter("user_id", user_id)
				.getResultList();
		return user;
	}
	
	/**
	 * It checks if a user is signed in or not
	 * @return true or false
	 */
	@GET
	@Path("/is_signed_in")
	public boolean isSignedIn() {
		return session.getAttribute("signedInUser") != null;
	}
	
	/**
	 * It signs a user out
	 * @return a Response object
	 * @throws JSONException
	 */
	@GET
	@Path("/signout")
	public Response signOut() throws JSONException {
		session.invalidate();
		return Response.status(Status.OK).entity(new JSONObject().put("msg", "Sign out successful")).build();
	}
}
