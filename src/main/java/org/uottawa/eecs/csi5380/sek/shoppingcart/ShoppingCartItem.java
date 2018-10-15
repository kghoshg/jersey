package org.uottawa.eecs.csi5380.sek.shoppingcart;

import org.uottawa.eecs.csi5380.sek.model.Book;

public class ShoppingCartItem {
	private Double amount;
    private Book aBook;
    private Integer quantity;
    private String title;
    private int price;
    private Integer bookid;
    
    public ShoppingCartItem(Book aBook) {
        this.aBook = aBook;
        this.title = aBook.getTitle();
        this.price = aBook.getPrice();
        this.bookid = aBook.getBookid();
        quantity = 1;
    }
    
    public String getTitle(){
    	return this.title;
    }
    
    public int getPrice(){
    	return this.price;
    }
    
    public void setTitle(String title){
    	this.title = title;
    }
    
    public void setBookid(Integer Bookid){
    	this.bookid = Bookid;
    }
    
    public Integer getBookid(){
    	return this.bookid;
    }
    
    public Book getBook() {
        return aBook;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        quantity--;
    }
    
    public Double getAmount(){
    	return this.amount;
    }
    
    public void setAmount(){
    }
    
    public double getTotal() {
    	double amt;
        amt = (this.getQuantity().doubleValue() * aBook.getPrice());
        return amt;
    }
}
