package org.uottawa.eecs.csi5380.sek.ws.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.uottawa.eecs.csi5380.sek.model.Book;
import org.uottawa.eecs.csi5380.sek.utils.DBUtils;

/**
 * All RESTfull service for product catalogue service
 */

@Path("/product_catalogue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductCatalogueServices {
	
	private static EntityManager em = DBUtils.getEntityManager();
	
//	public ProductCatalogueServices() {
//		em.getTransaction().begin();
//	}
	
	/**
	 * It lists all the books in the store
	 * @return java.lang.List of books
	 */
	@GET
	@Path("/books")
	public List<Book> list() {
		
		List<Book> bookList = 
				em.createNamedQuery("Book.findAll", Book.class)
		  	  .setMaxResults(50)
		      .getResultList();
		return bookList;
	}
	
	/**
	 * It finds all the distinct book categories of the store
	 * @return java.lang.List of Categories
	 */
	@GET
	@Path("/categories")
	public List<String> categories() {
		
		List<String> categories =  em.createNamedQuery("Book.findAllCategories", String.class)
				  .getResultList();
		return categories;
	}
	
	/**
	 * It finds a book's details by its id 
	 * @param book id
	 * @return a Book object
	 */
	@GET
	@Path("/book/{id}")
	public List<Book> bookDetailById(@PathParam("id") int id) {
		
		List<Book> bookList = em.createNamedQuery("Book.findById", Book.class)
		  .setParameter("id", id)
		  .getResultList();
		return bookList;
	}
	
	/**
	 * It finds books by category
	 * @param Book category
	 * @return java.lang.List of books
	 */
	@GET
	@Path("/books/{category}")
	public List<Book> booksByCategory(@PathParam("category") String category) {
		
		List<Book> bookList = em.createNamedQuery("Book.findByCategory", Book.class)
		  .setParameter("category", category)
		  .getResultList();
		return bookList;
	}
	
	@GET
	@Path("/recommended_books/{id}")
	public List<Book> findRecommendedBooks(@PathParam("id") int id) {
		List<Book> bookList = em.createNamedQuery("Book.findRecommnededBooks", Book.class)
				  .setParameter("id", id)
				  .getResultList();
		return bookList;
	}

}
