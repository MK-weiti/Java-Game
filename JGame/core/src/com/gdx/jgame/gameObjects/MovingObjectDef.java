package com.gdx.jgame.gameObjects;

import java.io.Serializable;
import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public abstract class MovingObjectDef extends PalpableObjectPolygonDef implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5278441540157154961L;
	
	public float acceleration;
	
	public float maxLinearSpeed, maxLinearAcceleration;
	public float maxAngularSpeed, maxAngularAcceleration;
	public float zeroThreshold;
	
	private HashMap<Integer, Float> ratioAcceleration;
	private HashMap<Integer, Float> ratioMaxVelocity;
	
	public boolean tagged = false;
	public float boundingRadious; // used for Collision Avoidance
	public float boundingToActivateAI;
	
	public MovingObjectDef(World world, Texture texture, String texPath, float texScale, Vector2[] vertices) {
		super(world, texture, texPath, texScale, vertices);
		
		ratioAcceleration = new HashMap<Integer, Float>();
		ratioMaxVelocity = new HashMap<Integer, Float>();
	}
	
	public MovingObjectDef(MovingObjectDef definition) {
		super(definition);
		
		this.acceleration = definition.acceleration;
		ratioAcceleration = new HashMap<Integer, Float>(definition.getRatioAcceleration());
		ratioMaxVelocity = new HashMap<Integer, Float>(definition.getRatioMaxVelocity());
		
		maxLinearSpeed = definition.maxLinearSpeed;
		maxLinearAcceleration = definition.maxLinearAcceleration;
		maxAngularSpeed = definition.maxAngularSpeed;
		maxAngularAcceleration = definition.maxAngularAcceleration;
		zeroThreshold = definition.zeroThreshold;
		tagged = definition.tagged;
		boundingRadious = definition.boundingRadious;
		boundingToActivateAI = definition.boundingToActivateAI;
	}
	
	public MovingObjectDef(MovingObject movingObject) {
		super(movingObject);
		
		this.acceleration = movingObject.getRawAcceleration();
		this.maxLinearSpeed = movingObject.getRawMaxLinearSpeed();
		ratioAcceleration = new HashMap<Integer, Float>(movingObject.getAccelerationRatios());
		ratioMaxVelocity = new HashMap<Integer, Float>(movingObject.getMaxVelocityRatios());
		
		maxLinearAcceleration = movingObject.getMaxLinearAcceleration();
		maxAngularSpeed = movingObject.getMaxAngularSpeed();
		maxAngularAcceleration = movingObject.getMaxAngularAcceleration();
		zeroThreshold = movingObject.getZeroLinearSpeedThreshold();
		tagged = movingObject.isTagged();
		boundingRadious = movingObject.getBoundingRadius();
		boundingToActivateAI = movingObject.getBoundingToActivateAI();
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
		result = prime * result + Float.floatToIntBits(acceleration);
		result = prime * result + Float.floatToIntBits(boundingRadious);
		result = prime * result + Float.floatToIntBits(boundingToActivateAI);
		result = prime * result + Float.floatToIntBits(maxAngularAcceleration);
		result = prime * result + Float.floatToIntBits(maxAngularSpeed);
		result = prime * result + Float.floatToIntBits(maxLinearAcceleration);
		result = prime * result + Float.floatToIntBits(maxLinearSpeed);
		result = prime * result + ((ratioAcceleration == null) ? 0 : ratioAcceleration.hashCode());
		result = prime * result + ((ratioMaxVelocity == null) ? 0 : ratioMaxVelocity.hashCode());
		result = prime * result + (tagged ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(zeroThreshold);
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
		if (Float.floatToIntBits(acceleration) != Float.floatToIntBits(other.acceleration))
			return false;
		if (Float.floatToIntBits(boundingRadious) != Float.floatToIntBits(other.boundingRadious))
			return false;
		if (Float.floatToIntBits(boundingToActivateAI) != Float.floatToIntBits(other.boundingToActivateAI))
			return false;
		if (Float.floatToIntBits(maxAngularAcceleration) != Float.floatToIntBits(other.maxAngularAcceleration))
			return false;
		if (Float.floatToIntBits(maxAngularSpeed) != Float.floatToIntBits(other.maxAngularSpeed))
			return false;
		if (Float.floatToIntBits(maxLinearAcceleration) != Float.floatToIntBits(other.maxLinearAcceleration))
			return false;
		if (Float.floatToIntBits(maxLinearSpeed) != Float.floatToIntBits(other.maxLinearSpeed))
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
		if (tagged != other.tagged)
			return false;
		if (Float.floatToIntBits(zeroThreshold) != Float.floatToIntBits(other.zeroThreshold))
			return false;
		return true;
	}
}
