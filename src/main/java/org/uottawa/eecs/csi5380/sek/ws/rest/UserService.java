package org.uottawa.eecs.csi5380.sek.ws.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ValidationException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.exception.ConstraintViolationException;
import org.uottawa.eecs.csi5380.sek.model.User;
import org.uottawa.eecs.csi5380.sek.utils.DBUtils;

/**
 * RESTful User service
 * @author Kuntal Ghosh
 *
 */
@Path("/user_service")
@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class UserService {

	private DBUtils dbUtil;
	HttpSession session;

	public UserService(@Context HttpServletRequest request) {
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
	 * @param user_name
	 * @param email
	 * @param password
	 * @param first_name
	 * @param last_name
	 * @return a Response object
	 * @throws JSONException
	 */
	@POST
	@Path("/create_account")
	public Response createAccount(@FormParam("user_name") String user_name, @FormParam("email") String email,
			@FormParam("password") String password, @FormParam("first_name") String first_name,
			@FormParam("last_name") String last_name) throws JSONException {

		List<User> user = dbUtil.createEm().createNamedQuery("User.findByUserName", User.class)
				.setParameter("user_name", user_name)
				.getResultList();
		
		//checks if the username already exists
		if (!user.isEmpty()) {
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "username has already been taken"))
					.build();
		} else {
			try {
				User aUser = new User();
				aUser.setUserName(user_name);
				aUser.setEmail(email);
				aUser.setPassword(password);
				aUser.setFirstName(first_name);
				aUser.setLastName(last_name);
				dbUtil.createEm().getTransaction().begin();
				dbUtil.createEm().persist(aUser);
				dbUtil.createEm().getTransaction().commit();
				dbUtil.createEm().close();
				//makes the newly created user automatically signed in
				session.setAttribute("signedInUser", aUser);
			} catch (Exception e) {
				return Response.status(Status.OK).entity(new JSONObject().put("msg", e.getMessage()))
						.build();
			}
			return Response.status(Status.CREATED)
					.entity(new JSONObject().put("msg", "account successfully created!")).build();
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
