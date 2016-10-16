package com.silverbars.marketplace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OrderBoardTests {
	
	private OrderBoard orderBoard;
	List<Order> ordersToRegister = null;
	
	@Before
	public void setUp() {
		orderBoard = new OrderBoard();
		ordersToRegister = new ArrayList<Order>();

	}
	
	@Test
	public void registerSingleSellOrder() {
		String userId = "user1";
		ordersToRegister.add(new Order(userId, 3.5, 306, Order.OrderType.SELL));
		Map<String, List<Order>> registeredOrders = orderBoard.registerOrders(ordersToRegister);
		Assert.assertTrue(registeredOrders.containsKey(userId));
		Assert.assertEquals(1, registeredOrders.get(userId).size());	
		Assert.assertNull(registeredOrders.get("user2"));		
	}
	
	@Test
	public void registerMultipleSellOrders() {
		ordersToRegister.add(new Order("user1", 3.5, 306, Order.OrderType.SELL));
		ordersToRegister.add(new Order("user2", 1.2, 310, Order.OrderType.SELL));
		ordersToRegister.add(new Order("user3", 1.5, 307, Order.OrderType.SELL));
		ordersToRegister.add(new Order("user4", 2.0, 306, Order.OrderType.SELL));
		Map<String, List<Order>> registeredOrders = orderBoard.registerOrders(ordersToRegister);
		Assert.assertEquals(1, registeredOrders.get("user1").size());	
		Assert.assertEquals(1, registeredOrders.get("user2").size());	
		Assert.assertEquals(1, registeredOrders.get("user3").size());	
		Assert.assertEquals(1, registeredOrders.get("user4").size());	
		Assert.assertNull(registeredOrders.get("user5"));		
	}
	
	@Test
	public void registerSingleBuyOrder() {
		ordersToRegister.add(new Order("user1", 3.8, 306, Order.OrderType.BUY));
		Map<String, List<Order>> registeredOrders = orderBoard.registerOrders(ordersToRegister);
		Assert.assertTrue(registeredOrders.containsKey("user1"));
		Assert.assertEquals(1, registeredOrders.get("user1").size());	
		Assert.assertNull(registeredOrders.get("user2"));			
	}
	
	@Test
	public void registerMultipleBuyOrders() {
		ordersToRegister.add(new Order("user1", 3.3, 306, Order.OrderType.BUY));
		ordersToRegister.add(new Order("user2", 2.2, 310, Order.OrderType.BUY));
		ordersToRegister.add(new Order("user3", 4.5, 307, Order.OrderType.BUY));
		ordersToRegister.add(new Order("user4", 7.0, 306, Order.OrderType.BUY));
		Map<String, List<Order>> registeredOrders = orderBoard.registerOrders(ordersToRegister);
		Assert.assertTrue(registeredOrders.containsKey("user3"));
		Assert.assertEquals(1, registeredOrders.get("user1").size());	
		Assert.assertEquals(1, registeredOrders.get("user2").size());	
		Assert.assertEquals(1, registeredOrders.get("user3").size());	
		Assert.assertEquals(1, registeredOrders.get("user4").size());	
		Assert.assertNull(registeredOrders.get("user5"));		
	}
	
	@Test
	public void registerMultipleBuyAndSellOrders() {
		ordersToRegister.add(new Order("user1", 3.3, 306, Order.OrderType.BUY));
		ordersToRegister.add(new Order("user2", 2.2, 312, Order.OrderType.SELL));
		ordersToRegister.add(new Order("user3", 6.2, 310, Order.OrderType.BUY));
		ordersToRegister.add(new Order("user4", 2.7, 424, Order.OrderType.SELL));
		Map<String, List<Order>> registeredOrders = orderBoard.registerOrders(ordersToRegister);
		Assert.assertTrue(registeredOrders.containsKey("user2"));
		Assert.assertEquals(1, registeredOrders.get("user1").size());	
		Assert.assertEquals(1, registeredOrders.get("user2").size());	
		Assert.assertEquals(1, registeredOrders.get("user3").size());	
		Assert.assertEquals(1, registeredOrders.get("user4").size());		
	}
	
	@Test
	public void registerSameOrderSameUser() {
		ordersToRegister.add(new Order("user1", 3.3, 306, Order.OrderType.BUY));
		ordersToRegister.add(new Order("user1", 3.3, 306, Order.OrderType.BUY));
		ordersToRegister.add(new Order("user3", 6.2, 310, Order.OrderType.BUY));
		Map<String, List<Order>> registeredOrders = orderBoard.registerOrders(ordersToRegister);
		Assert.assertTrue(registeredOrders.containsKey("user1"));
		Assert.assertEquals(2, registeredOrders.get("user1").size());	
		Assert.assertEquals(1, registeredOrders.get("user3").size());	
	}
	
	@Test
	public void registerDifferentOrderSameUser() {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order("user1", 8.5, 306, Order.OrderType.BUY));
		orders.add(new Order("user1", 3.3, 306, Order.OrderType.BUY));
		Map<String, List<Order>> registeredOrders = orderBoard.registerOrders(orders);
		Assert.assertEquals(2, registeredOrders.get("user1").size());	
	}
	
	@Test
	public void cancelRegisteredOrders() {
		Order order1 = new Order("user1", 3.5, 306, Order.OrderType.SELL);
		ordersToRegister.add(order1);
		Order order2 = new Order("user2", 1.2, 310, Order.OrderType.BUY);
		ordersToRegister.add(order2);
		Order order3 = new Order("user3", 1.5, 307, Order.OrderType.SELL);
		ordersToRegister.add(order3);
		orderBoard.registerOrders(ordersToRegister);
		
		List<Order> ordersToCancel = new ArrayList<Order>();
		ordersToCancel.add(order2);
		ordersToCancel.add(order3);
		Map<String, List<Order>> liveOrders = orderBoard.cancelRegisteredOrders(ordersToCancel);
		Assert.assertEquals(1, liveOrders.get("user1").size());		
		Assert.assertNull(liveOrders.get("user2"));		
		Assert.assertNull(liveOrders.get("user3"));		
	}
	
	@Test
	public void cancelRegisteredOrderWhenSameUserHasPlacedDifferentOrders() {
		Order order1 = new Order("user1", 2.8, 310, Order.OrderType.SELL);
		ordersToRegister.add(order1);
		Order order2 = new Order("user1", 3.5, 508, Order.OrderType.SELL);
		ordersToRegister.add(order2);
		Order order3 = new Order("user1", 5.7, 306, Order.OrderType.BUY);
		ordersToRegister.add(order3);
		orderBoard.registerOrders(ordersToRegister);
	
		List<Order> ordersToCancel = new ArrayList<Order>();
		ordersToCancel.add(order1);
		Map<String, List<Order>> liveOrders = orderBoard.cancelRegisteredOrders(ordersToCancel);
		Assert.assertEquals(2, liveOrders.get("user1").size());		
	}
	
	/**
	 * In the real world, we would have an id, timestamp etc on the order to be able 
	 * to verify exact order, here I'm just removing the first match
	 */
	@Test
	public void cancelRegisteredOrdersWhenSameUserHasPlacedSameOrderTwice() {
		Order order1 = new Order("user1", 3.5, 306, Order.OrderType.SELL);
		ordersToRegister.add(order1);
		Order order2 = new Order("user1", 3.5, 306, Order.OrderType.SELL);
		ordersToRegister.add(order2);
		orderBoard.registerOrders(ordersToRegister);
		
		List<Order> ordersToCancel = new ArrayList<Order>();
		ordersToCancel.add(order2);
		Map<String, List<Order>> liveOrders = orderBoard.cancelRegisteredOrders(ordersToCancel);
		Assert.assertEquals(1, liveOrders.get("user1").size());	
	}	
	
	@Test
	public void liveOrderSummaryWhenRegisteringOrders() {
		List<Order> orders = new ArrayList<Order>();
		orders.add(new Order("user1", 3.3, 306, Order.OrderType.BUY));
		orders.add(new Order("user2", 6.2, 550, Order.OrderType.BUY));
		orders.add(new Order("user3", 5.2, 424, Order.OrderType.SELL));
		orders.add(new Order("user4", 4.4, 101, Order.OrderType.BUY));
		orders.add(new Order("user5", 6.2, 310, Order.OrderType.SELL));
		orders.add(new Order("user6", 2.7, 312, Order.OrderType.SELL));
		orderBoard.registerOrders(orders); 
		String summary = orderBoard.getLiveOrderSummary();
		Assert.assertEquals("6.2kg for £310\n2.7kg for £312\n5.2kg for £424\n6.2kg for £550\n3.3kg for £306\n4.4kg for £101\n", summary);
	}
	
	@Test
	public void liveOrderSummaryWhenRegisteringAndCancellingOrders() {
		Order order1 = new Order("user1", 3.3, 306, Order.OrderType.BUY);
		ordersToRegister.add(order1);
		Order order2 = new Order("user2", 5.2, 424, Order.OrderType.SELL);
		ordersToRegister.add(order2);
		Order order3 = new Order("user3", 6.2, 310, Order.OrderType.BUY);
		ordersToRegister.add(order3);
		Order order4 = new Order("user4", 5.2, 424, Order.OrderType.SELL);
		ordersToRegister.add(order4);
		Order order5 = new Order("user5", 4.4, 101, Order.OrderType.BUY);
		ordersToRegister.add(order5);
		orderBoard.registerOrders(ordersToRegister);
		
		List<Order> ordersToCancel = new ArrayList<Order>();
		ordersToCancel.add(order1);
		ordersToCancel.add(order4);
		orderBoard.cancelRegisteredOrders(ordersToCancel);
		String summary = orderBoard.getLiveOrderSummary();
		Assert.assertEquals("5.2kg for £424\n6.2kg for £310\n4.4kg for £101\n", summary);
	}
	
	@Test
	public void liveOrderSummaryForOrdersWithSamePriceInDifferentOrderTypes() {
		Order order1 = new Order("user1", 2.5, 306, Order.OrderType.BUY);
		ordersToRegister.add(order1);
		Order order2 = new Order("user2", 1.2, 310, Order.OrderType.BUY);
		ordersToRegister.add(order2);
		Order order3 = new Order("user1", 3.5, 306, Order.OrderType.BUY);
		ordersToRegister.add(order3);
		Order order4 = new Order("user2", 1.2, 310, Order.OrderType.SELL);
		ordersToRegister.add(order4);
		Order order5 = new Order("user3", 1.5, 310, Order.OrderType.SELL);
		ordersToRegister.add(order5);
		Order order6 = new Order("user4", 2.0, 306, Order.OrderType.SELL);
		ordersToRegister.add(order6);
		orderBoard.registerOrders(ordersToRegister);
		String summary = orderBoard.getLiveOrderSummary();
		Assert.assertEquals("2.0kg for £306\n2.7kg for £310\n1.2kg for £310\n6.0kg for £306\n", summary);
	}
	
	@Test
	public void liveOrderSummaryForOrdersWithSamePriceSameOrderTypes() {
		Order order1 = new Order("user1", 3.5, 306, Order.OrderType.SELL);
		ordersToRegister.add(order1);
		Order order2 = new Order("user2", 1.2, 310, Order.OrderType.SELL);
		ordersToRegister.add(order2);
		Order order3 = new Order("user3", 1.5, 307, Order.OrderType.SELL);
		ordersToRegister.add(order3);
		Order order4 = new Order("user4", 2.0, 306, Order.OrderType.SELL);
		ordersToRegister.add(order4);
		orderBoard.registerOrders(ordersToRegister);
		String summary = orderBoard.getLiveOrderSummary();
		Assert.assertEquals("5.5kg for £306\n1.5kg for £307\n1.2kg for £310\n", summary);
		System.out.println(summary);	
	}
}
