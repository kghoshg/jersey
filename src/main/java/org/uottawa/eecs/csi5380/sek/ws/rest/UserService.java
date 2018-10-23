package org.uottawa.eecs.csi5380.sek.ws.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.uottawa.eecs.csi5380.sek.model.User;
import org.uottawa.eecs.csi5380.sek.utils.DBUtils;

@Path("/user_service")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

	private DBUtils dbUtil;
	HttpSession session;

	public UserService(@Context HttpServletRequest request) {
		this.dbUtil = new DBUtils();
		this.session = request.getSession(true);
	}

	@POST
	@Path("/signin")
	public Response signIn(@FormParam("user_name") String user_name, @FormParam("password") String password)
			throws JSONException {
		List<User> user = dbUtil.createEm()
				.createQuery("SELECT t FROM User t WHERE t.userName = :user_name AND t.password = :password",
						User.class)
				.setParameter("user_name", user_name).setParameter("password", password).getResultList();
		if (!user.isEmpty()) {
			session.setAttribute("signedInUser", user.get(0));
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Successful")).build();
		} else {
			return Response.status(Status.OK).entity(new JSONObject().put("msg", "Sign in unsuccessful")).build();
		}
	}

	@POST
	@Path("/create_account")
	public Response createAccount(@FormParam("user_name") String user_name, @FormParam("email") String email,
			@FormParam("password") String password, @FormParam("first_name") String first_name,
			@FormParam("last_name") String last_name) throws JSONException {

		List<User> user = dbUtil.createEm()
				.createQuery("SELECT t FROM User t WHERE t.userName = :user_name", User.class)
				.setParameter("user_name", user_name)
				.getResultList();
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
			} catch (Exception e) {
				System.out.println("*************************************" + e);
			}
			return Response.status(Status.CREATED)
					.entity(new JSONObject().put("msg", "account successfully created!")).build();
		}
	}

	@GET
	@Path("/is_signed_in")
	public boolean isSignedIn() {
		return session.getAttribute("signedInUser") != null;
	}

	@GET
	@Path("/signout")
	public Response signOut() throws JSONException {
		session.invalidate();
		return Response.status(Status.OK).entity(new JSONObject().put("msg", "Sign out successful")).build();
	}

}
