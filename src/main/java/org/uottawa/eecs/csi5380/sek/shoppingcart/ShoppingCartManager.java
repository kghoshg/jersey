package org.uottawa.eecs.csi5380.sek.shoppingcart;

import javax.servlet.http.HttpSession;

public class ShoppingCartManager {
	
	HttpSession session;
	ShoppingCart shoppigCart;
	
	
	public ShoppingCartManager(HttpSession session){
		this.session = session;
		this.shoppigCart = (ShoppingCart)session.getAttribute("shoppigCart");
	}
	
	public ShoppingCart getShoppingCart() {
		//Check if a shopping cart exists
		if (shoppigCart == null){
			shoppigCart = new ShoppingCart();
			session.setAttribute("shoppigCart", shoppigCart);
		}
		return shoppigCart;
	}
}
