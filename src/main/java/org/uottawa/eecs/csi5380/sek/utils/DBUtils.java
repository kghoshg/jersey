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
	EntityManager em;

	/**
	 * It is creates an @see EntityManager
	 * @return an object of @see EntityManager
	 */
	public EntityManager createEm() {
		if (em == null) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("SEK-CSI5380");
			em = emf.createEntityManager();
		}
		return em;
	}
}
