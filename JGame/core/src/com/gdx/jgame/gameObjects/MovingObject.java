package com.gdx.jgame.gameObjects;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public abstract class MovingObject extends PalpableObject{
	
	private float maxVelocity;
	private float acceleration;
	// fields for buffs, debuffs
	private HashMap<Integer, Float> ratioAcceleration;
	private HashMap<Integer, Float> ratioMaxVelocity;
	
	
	public MovingObject(PalpableObjectPolygonDef objectDef,
			float maxVelocity, float acceleration) {
		super(objectDef);
		this.maxVelocity = maxVelocity;
		this.acceleration = acceleration;
		ratioAcceleration = new HashMap<Integer, Float>();
		ratioMaxVelocity = new HashMap<Integer, Float>();
	}
	
	public MovingObject(MovingObjectDef definition) {
		super(definition);
		this.maxVelocity = definition.maxVelocity;
		this.acceleration = definition.acceleration;
		
		ratioAcceleration = new HashMap<Integer, Float>(definition.getRatioAcceleration());
		ratioMaxVelocity = new HashMap<Integer, Float>(definition.getRatioMaxVelocity());
	}
	
	private void applyImpulseWithin(float deltaX, float deltaY) {
		getBody().applyLinearImpulse(new Vector2(deltaX, deltaY), getBody().getWorldCenter(), true);
	}
	
	public float getCurrentVelocity() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(acceleration);
		result = prime * result + Float.floatToIntBits(maxVelocity);
		result = prime * result + ((ratioAcceleration == null) ? 0 : ratioAcceleration.hashCode());
		result = prime * result + ((ratioMaxVelocity == null) ? 0 : ratioMaxVelocity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovingObject other = (MovingObject) obj;
		if (Float.floatToIntBits(acceleration) != Float.floatToIntBits(other.acceleration))
			return false;
		if (Float.floatToIntBits(maxVelocity) != Float.floatToIntBits(other.maxVelocity))
			return false;
		if (ratioAcceleration == null) {
			if (other.ratioAcceleration != null)
				return false;
		} else if (!ratioAcceleration.equals(other.ratioAcceleration))
			return false;
		if (ratioMaxVelocity == null) {
			if (other.ratioMaxVelocity != null)
				return false;
		} else if (!ratioMaxVelocity.equals(other.ratioMaxVelocity))
			return false;
		return true;
	}
	
	
}
