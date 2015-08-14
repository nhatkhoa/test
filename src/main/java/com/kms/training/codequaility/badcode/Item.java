package com.kms.training.codequaility.badcode;

public class Item {
	private String name;
	private Double speed;
	private int count;

	public Item(String name, Double speed, int count) {
		this.name = name;
		this.speed = speed;
		this.count = count;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
}