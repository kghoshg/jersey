package org.uottawa.eecs.csi5380.sek.ws.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.uottawa.eecs.csi5380.sek.model.Book;
import org.uottawa.eecs.csi5380.sek.model.DBUtils;

/**
 * REST API
 */

@Path("/product_catalogue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductCatalogueServices {
	
	private DBUtils dbUtil = new DBUtils();
	
	@GET
	@Path("/books")
	public List<Book> list() {
		return dbUtil.createEm().createQuery("select t from Book t", Book.class)
		  .setMaxResults(50)
		  .getResultList();
	}
	
	@GET
	@Path("/categories")
	public List<String> categories() {
		return dbUtil.createEm().createQuery("SELECT DISTINCT t.category FROM Book t", String.class)
		  .getResultList();
	}
	
	@GET
	@Path("/book/{id}")
	public List<Book> bookDetailById(@PathParam("id") int id) {
		return dbUtil.createEm().createQuery("SELECT t FROM Book t WHERE t.bookid = :bookid", Book.class).setParameter("bookid", id)
		  .getResultList();
	}
	
	@GET
	@Path("/books/{category}")
	public List<Book> booksByCategory(@PathParam("category") String category) {
		return dbUtil.createEm().createQuery("SELECT t FROM Book t WHERE t.category = :category", Book.class).setParameter("category", category)
		  .getResultList();
	}
}
