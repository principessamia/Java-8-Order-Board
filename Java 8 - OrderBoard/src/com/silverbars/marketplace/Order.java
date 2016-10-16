package com.silverbars.marketplace;

public class Order {
	
	public enum OrderType {
		SELL, BUY
	}
	
	private String userId;
	private double quantity;
	private int pricePerKg;    // Didn't go with BigDecimal for purposes of this exercise, but would do in the real world for precision
	private OrderType orderType;
	
	public Order() { 
	}
		
	public Order(String userId, double quantity, int pricePerKg, OrderType orderType) {
		this.userId = userId;
		this.quantity = quantity;
		this.pricePerKg = pricePerKg;
		this.orderType = orderType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public int getPricePerKg() {
		return pricePerKg;
	}

	public void setPricePerKg(Integer pricePerKg) {
		this.pricePerKg = pricePerKg;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrdertype(OrderType orderType) {
		this.orderType = orderType;
	}
}
