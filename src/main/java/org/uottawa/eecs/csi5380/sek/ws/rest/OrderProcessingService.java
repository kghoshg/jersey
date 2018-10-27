package org.uottawa.eecs.csi5380.sek.ws.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
 * REST API
 */

@Path("/order_processor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderProcessingService {
	
	private DBUtils dbUtil;
	HttpSession session;
	private int numberOfProcessing = 0;
	private final long VALID_CREDIT_CARD = 1234567890123456L;

	public OrderProcessingService(@Context HttpServletRequest request) {
		this.dbUtil = new DBUtils();
		this.session = request.getSession(true);
	}
	
	/**
	 * It signs in a user
	 * @param user_name
	 * @param password
	 * @return a Response object
	 * @throws JSONException
	 */
	@POST
	@Path("/signin")
	public Response signIn(@FormParam("user_name") String user_name, @FormParam("password") String password)
			throws JSONException {
		List<User> user = dbUtil.createEm().createNamedQuery("User.findByUserNamePassword", User.class)
				.setParameter("user_name", user_name)
				.setParameter("password", password)
				.getResultList();
		if (!user.isEmpty()) {
			session.setAttribute("signedInUser", user.get(0));
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Sign in Successful")).build();
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
		List<User> user = dbUtil.createEm().createNamedQuery("User.findByUserName", User.class)
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
				dbUtil.createEm().getTransaction().begin();
				//persisting object
				dbUtil.createEm().persist(aUser);
				dbUtil.createEm().getTransaction().commit();
				dbUtil.createEm().close();
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
	@PUT
	@Path("/process_card")
	public Response processCard(@FormParam("credit_card") String credit_card) throws JSONException {
		
		//increases the counter by 1 every time a credit card transaction is processed
		numberOfProcessing++;
		
		if(!Long.toString(VALID_CREDIT_CARD).equals(credit_card)){
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Credit card transaction unsuccessful!")).build();
		}else if (numberOfProcessing == 5) {
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "It is the 5th transaction, so cancelled!")).build();
		}else {
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Credit card transaction successful!")).build();
		}
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
