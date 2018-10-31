package org.uottawa.eecs.csi5380.sek.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 * This is a database utility class, which handles @see EntityManager
 * @author Kuntal Ghosh
 *
 */
public class DBUtils {
	@PersistenceContext
	private static EntityManager em;

	/**
	 * It is creates an @see EntityManager
	 * @return an object of @see EntityManager
	 */
	public static EntityManager getEntityManager() {
		if (em == null) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("SEK-CSI5380");
			em = emf.createEntityManager();
			em.getTransaction().begin();
		}
		return em;
	}
}
