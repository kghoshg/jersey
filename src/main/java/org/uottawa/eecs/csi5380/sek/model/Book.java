package org.uottawa.eecs.csi5380.sek.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * It is Book model class
 * @author Kuntal Ghosh
 *
 */
@Entity
@Table(name = "book")
public class Book implements Serializable{
	
	////////////////////attributes of Book model class//////////////
	@Id
	@Column(name = "bookid", unique = true, columnDefinition = "VARCHAR" )
	private int bookid; 
	private String title; 
	private String author;
	private int price; 
	@Column(name = "category",columnDefinition="ENUM('Biography and Memoir','Business and Finance','Computers', 'Entertainment', 'History', 'Fiction', 'Science Fiction', 'Self-Help', 'Health', 'Science and Nature', 'Poetry')")
	private String category;
	
	///////////////////////getters and setters/////////////////////
	public int getBookid() {
		return bookid;
	}
	public void setBookid(int bookid) {
		this.bookid = bookid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
