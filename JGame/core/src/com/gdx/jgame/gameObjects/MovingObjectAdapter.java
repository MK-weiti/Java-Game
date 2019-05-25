package com.gdx.jgame.gameObjects;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public abstract class MovingObjectAdapter extends PalpableObject{
	
	private float maxVelocity;
	private float acceleration;
	// fields for buffs, debuffs
	private HashMap<Integer, Float> ratioAcceleration;
	private HashMap<Integer, Float> ratioMaxVelocity;
	
	
	public MovingObjectAdapter(PalpableObjectPolygonDef objectDef,
			float maxVelocity, float acceleration) {
		super(objectDef);
		this.maxVelocity = maxVelocity;
		this.acceleration = acceleration;
		ratioAcceleration = new HashMap<Integer, Float>();
		ratioMaxVelocity = new HashMap<Integer, Float>();
	}
	
	public MovingObjectAdapter(MovingObjectDef definition) {
		super(definition);
		this.maxVelocity = definition.maxVelocity;
		this.acceleration = definition.acceleration;
		
		ratioAcceleration = new HashMap<Integer, Float>(definition.getRatioAcceleration());
		ratioMaxVelocity = new HashMap<Integer, Float>(definition.getRatioMaxVelocity());
	}
	
	private void applyImpulseWithin(float deltaX, float deltaY) {
		getBody().applyLinearImpulse(new Vector2(deltaX, deltaY), getBody().getWorldCenter(), true);
	}
	
	public float getActualVelocity() {
		return (float) Math.hypot(getBody().getLinearVelocity().x, getBody().getLinearVelocity().y);
	}
		
	public void applyImpulse(Vector2 delta) {
		applyImpulseWithin(delta.x, delta.y);
	}
	
	public void applyImpulse(float deltaX, float deltaY) {
		applyImpulseWithin(deltaX, deltaY);
	}	
	
	public float getAcceleration() {
		float tmp = acceleration;
		for (float next : ratioAcceleration.values()) {
			tmp *= next;
		}
		return tmp;
	}
	
	public float getMaxVelocity() {
		float tmp = maxVelocity;
		for (float next : ratioMaxVelocity.values()) {
			tmp *= next;
		}
		return tmp;
	}

	public float getRatioAcceleration() {
		float tmp = 1f;
		for (float next : ratioAcceleration.values()) {
			tmp *= next;
		}
		return tmp;
	}

	public float getRatioMaxVelocity() {
		float tmp = 1f;
		for (float next : ratioMaxVelocity.values()) {
			tmp *= next;
		}
		return tmp;
	}
	
	public void addRatioAcceleration(int ratioId, float newRatio) {
		ratioAcceleration.put(ratioId, newRatio);
	}

	public void addRatioMaxVelocity(int ratioId, float newRatio) {
		ratioMaxVelocity.put(ratioId, newRatio);
	}
	
	public void removeRatioAcceleration(int ratioId) {
		ratioAcceleration.remove(ratioId);
	}
	
	public void removeRatioMaxVelocity(int ratioId) {
		ratioMaxVelocity.remove(ratioId);
	}
	
	void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}
	
	void setMaxVelocity(float maxVelocity) {
		this.maxVelocity = maxVelocity;
	}
	
	public int generateAccelerationId() {
		
		Random generator = new Random();
		int result;
		do {
			result = generator.nextInt();
		}
		while(ratioAcceleration.containsKey(result));
		return result;
	}
	
	public int generateMaxVelocityId() {
		Random generator = new Random();
		int result;
		do {
			result = generator.nextInt();
		}
		while(ratioMaxVelocity.containsKey(result));
		return result;
	}

	public float getImpulse() {
		return Gdx.graphics.getDeltaTime()*getAcceleration();
	}
	
	public float getRawAcceleration() {
		return acceleration;
	}
	
	public float getRawMaxVelocity() {
		return maxVelocity;
	}
	
	HashMap<Integer, Float> getAccelerationRatios(){
		return ratioAcceleration;
	}
	
	HashMap<Integer, Float> getMaxVelocityRatios(){
		return ratioMaxVelocity;
	}
}
