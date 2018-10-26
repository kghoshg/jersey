package org.uottawa.eecs.csi5380.sek.shoppingcart;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.uottawa.eecs.csi5380.sek.model.Book;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShoppingCartTest {
	
	private static Book book1, book2;
	private static ShoppingCart aShoppingCart;
	
	@BeforeClass
	public static void setUp() {
		aShoppingCart = new ShoppingCart();
		book1 = new Book(1,"Electronics commerce 1", "Osman 1", "Computer", 1240);
		book2 = new Book(2,"Electronics commerce 2", "Osman 2", "Computer", 2240);
	}
	
	@Test
	public void test1AddItem() {
		aShoppingCart.addItem(book1);
		aShoppingCart.addItem(book2);
		assertTrue("Number of items in the shopping cart:", aShoppingCart.getNumberOfItems() == 2);
	}
	
	@Test
	public void test2Update() {
		aShoppingCart.update(book1, 5);
		aShoppingCart.update(book2, 5);
		assertTrue("Number of items in the shopping cart:", aShoppingCart.getNumberOfItems() == 10);
	}
	
	@Test
	public void test3Remove() {
		int count = 0;
		aShoppingCart.update(book1, 0);
		for (ShoppingCartItem shoppingCartItem : aShoppingCart.getItems()) {
			if (shoppingCartItem.getBook().getTitle().equalsIgnoreCase("Electronics commerce 1"))
				count++;
		}
		assertTrue("no copy of book1 in the shopping cart:",  count == 0);
	}
	
	@Test
	public void test4Clear() {
		aShoppingCart.clear();
		assertTrue("the shopping cart should be empty:", aShoppingCart.getNumberOfItems() == 0);
	}
}
