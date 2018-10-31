package org.uottawa.eecs.csi5380.sek.ws.rest;

import javax.persistence.EntityManager;
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

/**
 * RESTful for shopping cart service 
 * @author Kuntal Ghosh
 *
 */

@Path("/shopping_cart_service")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShoppingCartService {
	
	private ShoppingCart aShoppingCart;
	private static EntityManager em = DBUtils.getEntityManager();
	
	public ShoppingCartService(@Context HttpServletRequest request) {
		this.aShoppingCart = new ShoppingCartUtil(request.getSession(true)).getShoppingCart();
	}
	
	/**
	 * It prepares a shopping cart based on user session
	 * @return the shopping cart as JSON object
	 */
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
	
	/**
	 * It adds a book to the current shopping cart 
	 * @param book id
	 * @return a Response object
	 */
	@POST
	@Path("/add_2_cart/{id}")
	public Response.Status add2Cart(@PathParam("id") int id) {
		
		aShoppingCart.addItem(em.createNamedQuery("Book.findById", Book.class)
				  .setParameter("id", id)
				  .getResultList()
				  .get(0));
		return Response.Status.OK;
	}
	
	/**
	 * It removes a book from the current shopping cart
	 * @param book id
	 * @return
	 */
	@POST
	@Path("/remove_from_cart/{id}")
	public Response.Status removeFromCart(@PathParam("id") int id) {
		
		aShoppingCart.update(em.createNamedQuery("Book.findById", Book.class)
				  .setParameter("id", id)
				  .getResultList()
				  .get(0), 0);
		return Response.Status.OK;
	}
	
	/**
	 * It updates the number of copies of the same book
	 * @param book id
	 * @param quantity, the number of copies of a book
	 * @return a Response object
	 */
	@POST
	@Path("/update_cart/{id}/{quantity}")
	public Response.Status updateCart(@PathParam("id") int id, @PathParam("quantity") int quantity) {
		
		aShoppingCart.update(em.createNamedQuery("Book.findById", Book.class)
				  .setParameter("id", id)
				  .getResultList()
				  .get(0), quantity);
		return Response.Status.OK;
	}
	
	/**
	 * It checks if a book is in the current shopping cart or not
	 * @param book id
	 * @return a Response object
	 * @throws JSONException
	 */
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
	
	/**
	 * It empties the current shopping cart
	 * @return a Response Status
	 */
	@GET
	@Path("/empty_cart")
	public Response.Status emptyCart() {
		aShoppingCart.clear();
		return Response.Status.OK;
	}
	
}
