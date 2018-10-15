package org.uottawa.eecs.csi5380.sek.controller;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.uottawa.eecs.csi5380.sek.model.Book;
import org.uottawa.eecs.csi5380.sek.model.DBUtils;
import org.uottawa.eecs.csi5380.sek.shoppingcart.ShoppingCart;
import org.uottawa.eecs.csi5380.sek.shoppingcart.ShoppingCartManager;


@Path("/shopping_cart_controller")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingCartController {
	
	private DBUtils dbUtil;
	private ShoppingCart aShoppingCart;
	
	public ShoppingCartController(@Context HttpServletRequest request) {
		this.dbUtil = new DBUtils();
		this.aShoppingCart = new ShoppingCartManager(request.getSession(true)).getShoppingCart();
	}
	 
	@POST
	@Path("/add_2_cart/{id}")
	public Response.Status add2Cart(@PathParam("id") int id) {
		aShoppingCart.addItem(dbUtil.createEm().createQuery("SELECT t FROM Book t WHERE t.bookid = :bookid", Book.class).
				setParameter("bookid", id).getResultList().get(0));
		return Response.Status.OK;
	}
	
	@POST
	@Path("/remove_from_cart/{id}")
	public void removeFromCart(@PathParam("id") int id) {
		aShoppingCart.update( dbUtil.createEm().createQuery("SELECT t FROM Book t WHERE t.bookid = :bookid", Book.class).
				setParameter("bookid", id).getResultList().get(0), 0);
	}
	
	@POST
	@Path("/update_cart/{id}")
	public void updateCart(@PathParam("id") int id) {
	}
}
