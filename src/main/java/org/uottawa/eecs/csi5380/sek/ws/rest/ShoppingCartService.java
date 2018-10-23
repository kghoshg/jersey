package org.uottawa.eecs.csi5380.sek.ws.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.uottawa.eecs.csi5380.sek.model.Book;
import org.uottawa.eecs.csi5380.sek.shoppingcart.ShoppingCart;
import org.uottawa.eecs.csi5380.sek.shoppingcart.ShoppingCartItem;
import org.uottawa.eecs.csi5380.sek.utils.DBUtils;
import org.uottawa.eecs.csi5380.sek.utils.ShoppingCartUtil;

@Path("/shopping_cart_service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingCartService {
	
	private DBUtils dbUtil;
	private ShoppingCart aShoppingCart;
	
	public ShoppingCartService(@Context HttpServletRequest request) {
		this.dbUtil = new DBUtils();
		this.aShoppingCart = new ShoppingCartUtil(request.getSession(true)).getShoppingCart();
	}
	
	@GET
	@Path("/show_cart")
	public JSONObject showCart() {
		
		JSONObject shoppingCart = new JSONObject();
		try {
			shoppingCart.put("number_of_items", aShoppingCart.getNumberOfItems());
			shoppingCart.put("total_price", aShoppingCart.getTotalPrice());
	
			JSONArray allBooks = new JSONArray();
			
			for (ShoppingCartItem item : aShoppingCart.getItems()) {
				
				JSONObject book = new JSONObject();
				book.put("id", item.getBookid());
				book.put("title", item.getTitle());
				book.put("price", item.getPrice());
				book.put("quantity", item.getQuantity());
				allBooks.put(book);
			}
			shoppingCart.put("all_books", allBooks);
		} catch (Exception e) {
			// TODO: handle exception
		}
		 return shoppingCart;
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
	public Response.Status removeFromCart(@PathParam("id") int id) {
		aShoppingCart.update( dbUtil.createEm().createQuery("SELECT t FROM Book t WHERE t.bookid = :bookid", Book.class).
				setParameter("bookid", id).getResultList().get(0), 0);
		return Response.Status.OK;
	}
	
	@POST
	@Path("/update_cart/{id}/{quantity}")
	public Response.Status updateCart(@PathParam("id") int id, @PathParam("quantity") int quantity) {
		aShoppingCart.update( dbUtil.createEm().createQuery("SELECT t FROM Book t WHERE t.bookid = :bookid", Book.class).
				setParameter("bookid", id).getResultList().get(0), quantity);
		return Response.Status.OK;
	}
	
	@POST
	@Path("/is_in_cart/{id}")
	public JSONObject isInTheCart(@PathParam("id") int id) throws JSONException{
		boolean isPresent = false;
		for (ShoppingCartItem item : aShoppingCart.getItems()) {
			if(item.getBookid().equals(id)) {
				isPresent = item.getBookid().equals(id);
				break;
			}
		}
		return  new JSONObject().put("is_present", isPresent);
	}
	
	@GET
	@Path("/empty_cart")
	public void emptyCart() {
		aShoppingCart.clear();
	}
	
}
