package com.gdx.jgame.gameObjects;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class MovingObjectDef extends PalpableObjectPolygonDef implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5278441540157154961L;
	
	public float maxVelocity = 1f;
	public float acceleration = 0f;
	
	private HashMap<Integer, Float> ratioAcceleration;
	private HashMap<Integer, Float> ratioMaxVelocity;
	
	
	public MovingObjectDef(World world, Texture texture, String texPath, float texScale, Vector2[] vertices) {
		super(world, texture, texPath, texScale, vertices);
		
		ratioAcceleration = new HashMap<Integer, Float>();
		ratioMaxVelocity = new HashMap<Integer, Float>();
	}
	
	public MovingObjectDef(MovingObjectDef definition) {
		super(definition);
		
		this.maxVelocity = definition.maxVelocity;
		this.acceleration = definition.acceleration;
		ratioAcceleration = new HashMap<Integer, Float>(definition.getRatioAcceleration());
		ratioMaxVelocity = new HashMap<Integer, Float>(definition.getRatioMaxVelocity());
	}
	
	public MovingObjectDef(MovingObject movingObject) {
		super(movingObject);
		
		this.maxVelocity = movingObject.getRawMaxVelocity();
		this.acceleration = movingObject.getRawAcceleration();
		ratioAcceleration = new HashMap<Integer, Float>(movingObject.getAccelerationRatios());
		ratioMaxVelocity = new HashMap<Integer, Float>(movingObject.getMaxVelocityRatios());
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

	public HashMap<Integer, Float> getRatioAcceleration() {
		return ratioAcceleration;
	}

	public HashMap<Integer, Float> getRatioMaxVelocity() {
		return ratioMaxVelocity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (ID ^ (ID >>> 32));
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MovingObjectDef other = (MovingObjectDef) obj;
		if (ID != other.ID)
			return false;
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
