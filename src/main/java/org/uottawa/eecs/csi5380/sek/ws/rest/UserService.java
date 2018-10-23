package org.uottawa.eecs.csi5380.sek.ws.rest;

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

import org.uottawa.eecs.csi5380.sek.model.User;
import org.uottawa.eecs.csi5380.sek.utils.DBUtils;

@Path("/user_service")
@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class UserService {
	
	private DBUtils dbUtil;
	HttpSession session;
	
	public UserService(@Context HttpServletRequest request) {
		this.dbUtil = new DBUtils();
		this.session = request.getSession(true);
	}
	
	@POST
	@Path("/signin")
	public Response.Status signIn(@FormParam("user_name") String user_name,
            @FormParam("password") String password) {
		User user = dbUtil.createEm().createQuery("SELECT t FROM User t WHERE t.bookid = :bookid", User.class)
		.setParameter("user_name", user_name)
		.setParameter("password", password)
		.getResultList().get(0);
		if(user != null) {
			session.setAttribute("signedInUser","user");
		}
		return Response.Status.OK;
	}
	
	@POST
	@Path("/create_account")
	public Response.Status createAccount(@FormParam("user_name") String user_name,
            @FormParam("email") String email, @FormParam("password") String password, 
            @FormParam("first_name") String first_name, @FormParam("last_name") String last_name) {
		try {
		User aUser = new User();
		aUser.setUserName(user_name);
		aUser.setEmail(email);
		aUser.setFirstName(first_name);
		aUser.setLastName(last_name);
		dbUtil.createEm().getTransaction().begin();
		dbUtil.createEm().persist(aUser);
		dbUtil.createEm().getTransaction().commit();
		dbUtil.createEm().close();
		}catch(Exception e) {
			System.out.println("*************************************" + e);
		}
		return Response.Status.OK;
	}
	
	@GET
	@Path("/signout")
	public void signOut() {
		
	}

}

