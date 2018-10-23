package org.uottawa.eecs.csi5380.sek.utils;

import javax.servlet.http.HttpSession;

import org.uottawa.eecs.csi5380.sek.shoppingcart.ShoppingCart;

public class ShoppingCartUtil {
	
	HttpSession session;
	ShoppingCart shoppigCart;
	
	
	public ShoppingCartUtil(HttpSession session){
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
