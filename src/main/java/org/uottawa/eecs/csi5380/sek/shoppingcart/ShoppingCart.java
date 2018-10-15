package org.uottawa.eecs.csi5380.sek.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import org.uottawa.eecs.csi5380.sek.model.Book;

public class ShoppingCart {
	
	 List<ShoppingCartItem> listOfitems;
	    int numberOfItems;
	    Double totalPrice;

	    public ShoppingCart() {
	        listOfitems = new ArrayList<ShoppingCartItem>();
	        numberOfItems = 0;
	        totalPrice = 0.0;
	    }

	    public void addItem(Book aBook) {

	        boolean newItem = true;

	        for (ShoppingCartItem anItem : listOfitems) {

	            if (anItem.getBook().getBookid() == aBook.getBookid()) {

	                newItem = false;
	                anItem.incrementQuantity();
	                anItem.setAmount();
	            }
	        }

	        if (newItem) {
	            ShoppingCartItem sci = new ShoppingCartItem(aBook);
	            listOfitems.add(sci);
	            sci.setAmount();
	        }
	    }
	    

	    public  void update(Book aBook, Integer quantity) {

	        if (quantity >= 0) {

	            ShoppingCartItem item = null;

	            for (ShoppingCartItem sci : listOfitems) {

	                if (sci.getBook().getBookid() == aBook.getBookid()) {

	                    if (quantity != 0) {
	                        sci.setQuantity(quantity);
	                        sci.setAmount();
	                    } else {
	                        item = sci;
	                        item.setAmount();
	                        break;
	                    }
	                }
	            }

	            if (item != null) {
	                // remove from cart
	                listOfitems.remove(item);
	                
	            }
	        }
	    }

	    public List<ShoppingCartItem> getItems() {

	        return listOfitems;
	    }

	    public int getNumberOfItems() {

	        numberOfItems = 0;

	        for (ShoppingCartItem sci : listOfitems) {

	            numberOfItems += sci.getQuantity();
	        }

	        return numberOfItems;
	    }

	    public Double getSubtotal() {

	        Double amount = (Double) 0.0;

	        for (ShoppingCartItem sci : listOfitems) {
	            
	            Book Book = (Book) sci.getBook();
	            amount += (sci.getQuantity().doubleValue() * Book.getPrice());
	        }

	        return amount;
	    }

	    public  Double getTotal() {

	        return totalPrice;
	    }

	    public void clear() {
	        listOfitems.clear();
	        numberOfItems = 0;
	        totalPrice =  0.0;
	    }


}
