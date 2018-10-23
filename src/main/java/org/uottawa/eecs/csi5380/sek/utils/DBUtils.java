package org.uottawa.eecs.csi5380.sek.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;


public class DBUtils {
	@PersistenceContext
	EntityManager em;

	public EntityManager createEm() {
		if (em == null) {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("SEK-CSI5380");
			em = emf.createEntityManager();
		}
		return em;
	}
}
