package org.uottawa.eecs.csi5380.sek.ws.rest;

import java.util.List;

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
	
	private DBUtils dbUtil = new DBUtils();
	
	/**
	 * It lists all the books in the store
	 * @return java.lang.List of books
	 */
	@GET
	@Path("/books")
	public List<Book> list() {
		return dbUtil.createEm().createNamedQuery("Book.findAll", Book.class)
		  .setMaxResults(50)
		  .getResultList();
	}
	
	/**
	 * It finds all the distinct book categories of the store
	 * @return java.lang.List of Categories
	 */
	@GET
	@Path("/categories")
	public List<String> categories() {
		  return dbUtil.createEm().createNamedQuery("Book.findAllCategories", String.class)
				  .getResultList();
	}
	
	/**
	 * It finds a book's details by its id 
	 * @param book id
	 * @return a Book object
	 */
	@GET
	@Path("/book/{id}")
	public List<Book> bookDetailById(@PathParam("id") int id) {
		return dbUtil.createEm().createNamedQuery("Book.findById", Book.class)
		  .setParameter("id", id)
		  .getResultList();
	}
	
	/**
	 * It finds books by category
	 * @param Book category
	 * @return java.lang.List of books
	 */
	@GET
	@Path("/books/{category}")
	public List<Book> booksByCategory(@PathParam("category") String category) {
		return dbUtil.createEm().createNamedQuery("Book.findByCategory", Book.class)
		  .setParameter("category", category)
		  .getResultList();
	}

}
