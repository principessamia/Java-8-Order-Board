package com.silverbars.marketplace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/*
 * Decided not to have an interface in front on this, since there is only one implementation - sticking to YAGNI
 */
public class OrderBoard {
	
	// Keyed on userId here instead of, say, order type, to reduce list sizes for quicker iteration
	private Map<String, List<Order>> liveOrders = new HashMap<String, List<Order>>();
	
	protected Map<String, List<Order>> registerOrders(List<Order> ordersToRegister) {
		ordersToRegister.forEach(orderToRegister-> {
			String userId = orderToRegister.getUserId();
			if(liveOrders.containsKey(userId)) {
				List<Order> registeredOrdersByType = liveOrders.get(userId);
				registeredOrdersByType.add(orderToRegister);
			}
			else {
				List<Order> orderToAdd = new ArrayList<Order>();
				orderToAdd.add(orderToRegister);
				liveOrders.put(userId, orderToAdd);
			}
		});
		
		return liveOrders;
	}

	protected Map<String, List<Order>> cancelRegisteredOrders(List<Order> ordersToCancel) {
		ordersToCancel.forEach(orderToCancel-> {
			String userId = orderToCancel.getUserId();
			List<Order> liveOrdersByUser = liveOrders.get(userId);
			liveOrdersByUser.remove(orderToCancel); // If I had more time, it would make sense to verify if the actual order was removed and throw an exception if not
			if(liveOrdersByUser.size() == 0) {
				liveOrders.remove(userId);
			}
		});
		
		return liveOrders;		
	}
	
	protected String getLiveOrderSummary() {
		Map<Integer, Double> sortedSellOrders = sumAndSortOrdersByType(Order.OrderType.SELL);

		StringBuilder builder = new StringBuilder();
		builder = formatOrderSummary(builder, sortedSellOrders);
		
		Map<Integer, Double> sortedBuyOrders = sumAndSortOrdersByType(Order.OrderType.BUY);
		
		builder = formatOrderSummary(builder, sortedBuyOrders);	
		return builder.toString();	
	}

	private Map<Integer, Double> sumAndSortOrdersByType(Order.OrderType orderType) {
		List<Order> ordersByType = collectLiveOrdersByOrderType(orderType);
		Map<Integer, Double> summedOrders = sumQuantityForEqualPriceOrders(ordersByType);
		Map<Integer, Double> sortedOrders = null;
		if(orderType == Order.OrderType.SELL) {
			sortedOrders = new TreeMap<Integer, Double>(summedOrders);
		}
		else {
			sortedOrders = new TreeMap<Integer, Double>(Collections.reverseOrder());
			sortedOrders.putAll(summedOrders);
		}
		
		return sortedOrders;
	}

	// Didn't use a formatter since it's overkill here
	private StringBuilder formatOrderSummary(StringBuilder builder, Map<Integer, Double> sortedOrders) {
		sortedOrders.forEach((price, quantity) -> {
			builder.append(quantity)
			.append("kg for £")
			.append(price)
			.append("\n");
		});
		
		return builder;
	}

	private Map<Integer, Double> sumQuantityForEqualPriceOrders(List<Order> orders) {
		Map<Integer, Double> summedOrders = orders.stream()
		  	.collect(Collectors.groupingBy(Order::getPricePerKg,
		  			 Collectors.summingDouble(Order::getQuantity)));
		
		return summedOrders;
	}

	private List<Order> collectLiveOrdersByOrderType(Order.OrderType orderType) {
		List<Order> ordersByType = new ArrayList<Order>();
		
		liveOrders.forEach((userId, ordersByUserId) -> {
			 List<Order> userOrdersByType = ordersByUserId.stream()
					 .filter(order -> order.getOrderType() == orderType)
					 .collect(Collectors.toList());
			 ordersByType.addAll(userOrdersByType);
		});		  
		  
		return ordersByType; 
	}
}
