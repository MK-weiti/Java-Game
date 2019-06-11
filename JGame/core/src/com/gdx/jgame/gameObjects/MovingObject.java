package com.gdx.jgame.gameObjects;

import java.awt.IllegalComponentStateException;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Utils;

public abstract class MovingObject extends PalpableObject implements Steerable<Vector2>{
	
	// the acceleration which object will have when control over him was taken over.
	private float acceleration;	
	
	private float maxLinearSpeed, maxLinearAcceleration;
	private float maxAngularSpeed, maxAngularAcceleration;
	private float zeroThreshold;
	
	// fields for buffs, debuffs
	private HashMap<Integer, Float> ratioAcceleration;
	private HashMap<Integer, Float> ratioMaxLinearSpeed;
	
	private boolean tagged;
	private float boundingRadious; // used for Collision Avoidance, idk.
	private float boundingToActivateAI;
	private transient BlendedSteering<Vector2> blend;
	
	public MovingObject(MovingObjectDef objectDef, float acceleration) {
		super(objectDef);
		checkArguments(objectDef, acceleration);		
		
		this.acceleration = acceleration;
		ratioAcceleration = new HashMap<Integer, Float>();
		ratioMaxLinearSpeed = new HashMap<Integer, Float>();
		
		setArguments(objectDef);
	}
	
	public MovingObject(MovingObjectDef objectDef) {
		super(objectDef);
		checkArguments(objectDef, objectDef.acceleration);	
		this.acceleration = objectDef.acceleration;
		
		ratioAcceleration = new HashMap<Integer, Float>(objectDef.getRatioAcceleration());
		ratioMaxLinearSpeed = new HashMap<Integer, Float>(objectDef.getRatioMaxVelocity());
		
		setArguments(objectDef);
	}

	private void checkArguments(MovingObjectDef objectDef, float acceleration) {
		if(acceleration == 0) throw new IllegalComponentStateException("Wrong acceleration.");
		if(objectDef.maxLinearSpeed == 0) throw new IllegalComponentStateException("No maxLinearSpeed.");
		if(objectDef.maxLinearAcceleration == 0) throw new IllegalComponentStateException("No maxLinearAcceleration.");
		if(objectDef.maxAngularSpeed == 0) throw new IllegalComponentStateException("No maxAngularSpeed.");
		if(objectDef.maxAngularAcceleration == 0) throw new IllegalComponentStateException("No maxAngularAcceleration.");
		if(objectDef.zeroThreshold == 0) throw new IllegalComponentStateException("No zeroThreshold.");
		if(objectDef.boundingRadious == 0) throw new IllegalComponentStateException("No boundingRadious.");
		if(objectDef.boundingToActivateAI == 0) throw new IllegalComponentStateException("No boundingToActivateAI.");
	}

	private void setArguments(MovingObjectDef objectDef) {
		maxLinearSpeed = objectDef.maxLinearSpeed;
		maxLinearAcceleration = objectDef.maxLinearAcceleration;
		maxAngularSpeed = objectDef.maxAngularSpeed;
		maxAngularAcceleration = objectDef.maxAngularAcceleration;
		zeroThreshold = objectDef.zeroThreshold;
		tagged = objectDef.tagged;
		boundingRadious = objectDef.boundingRadious;
		boundingToActivateAI = objectDef.boundingToActivateAI;
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

	public float getRatioAcceleration() {
		float tmp = 1f;
		for (float next : ratioAcceleration.values()) {
			tmp *= next;
		}
		return tmp;
	}

	public float getRatioMaxLinearSpeed() {
		float tmp = 1f;
		for (float next : ratioMaxLinearSpeed.values()) {
			tmp *= next;
		}
		return tmp;
	}
	
	public void addRatioAcceleration(int ratioId, float newRatio) {
		ratioAcceleration.put(ratioId, newRatio);
	}

	public void addRatioMaxLinearSpeed(int ratioId, float newRatio) {
		ratioMaxLinearSpeed.put(ratioId, newRatio);
	}
	
	public void removeRatioAcceleration(int ratioId) {
		ratioAcceleration.remove(ratioId);
	}
	
	public void removeRatioMaxLinearSpeed(int ratioId) {
		ratioMaxLinearSpeed.remove(ratioId);
	}
	
	void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
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
		while(ratioMaxLinearSpeed.containsKey(result));
		return result;
	}

	public float getImpulse() {
		return Gdx.graphics.getDeltaTime()*getAcceleration();
	}
	
	public float getRawAcceleration() {
		return acceleration;
	}
	
	HashMap<Integer, Float> getAccelerationRatios(){
		return ratioAcceleration;
	}
	
	HashMap<Integer, Float> getMaxVelocityRatios(){
		return ratioMaxLinearSpeed;
	}
	
	//
	// AI part
	//
	
	
	// TODO
	public boolean canAIrun() {
		return blend != null && ((Arrive<Vector2>) blend.get(0).getBehavior())
				.getTarget().getPosition().dst(getBody().getPosition()) < boundingToActivateAI;
	}
	
	public void updateAI(float deltaTime) {
		if(canAIrun()) {
			SteeringAcceleration<Vector2> steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
			blend.calculateSteering(steerOutput);
			applySteering(deltaTime, steerOutput);
		}
	}
	
	private void applySteering(float deltaTime, SteeringAcceleration<Vector2> steerOutput) {
		boolean anyAccelerations = false;
		
		if(!steerOutput.linear.isZero()) {
			Vector2 force = steerOutput.linear.scl(deltaTime);
			getBody().applyForceToCenter(force, true);
			anyAccelerations = true;
		}
		
		if(steerOutput.angular != 0) {
			getBody().applyTorque(steerOutput.angular * deltaTime, true);
			anyAccelerations = true;
		}
		
		if(anyAccelerations) {
			Vector2 velocity = getBody().getLinearVelocity();
			float currentSpeedSquare = velocity.len2();
			if(currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
				getBody().setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
			}
			
			if(getBody().getAngularVelocity() > maxAngularSpeed) {
				getBody().setAngularVelocity(maxAngularSpeed);
			}
		}		
	}
	
	public void setBehavior(BlendedSteering<Vector2> blend) {
		this.blend = blend;
	}
	
	public BlendedSteering<Vector2> getBehavior() {
		return blend;
	}
	
	public float getBoundingToActivateAI() {
		return boundingToActivateAI;
	}

	public void setBoundingToActivateAI(float m_boundingToActivateAI) {
		this.boundingToActivateAI = m_boundingToActivateAI;
	}

	@Override
	public Vector2 getPosition() {
		return getBody().getPosition();
	}

	@Override
	public float getOrientation() {
		return getBody().getAngle();
	}

	@Override
	public void setOrientation(float orientation) {
		// TODO check if working
		this.rotate(orientation);
	}

	@Override
	public float vectorToAngle(Vector2 vector) {
		return Utils.vectorToAngle(vector);
	}

	@Override
	public Vector2 angleToVector(Vector2 outVector, float angle) {
		return Utils.angleToVector(outVector, angle);
	}

	@Override
	public Location<Vector2> newLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getZeroLinearSpeedThreshold() {
		return zeroThreshold;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		zeroThreshold = value;
	}
	
	public float getRawMaxLinearSpeed() {
		return maxLinearSpeed;
	}

	@Override
	public float getMaxLinearSpeed() {
		float tmp = maxLinearSpeed;
		for (float next : ratioMaxLinearSpeed.values()) {
			tmp *= next;
		}
		return tmp;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.maxAngularAcceleration = maxAngularAcceleration;
	}

	@Override
	public Vector2 getLinearVelocity() {
		return getBody().getLinearVelocity();
	}

	@Override
	public float getAngularVelocity() {
		return getBody().getAngularVelocity();
	}

	@Override
	public float getBoundingRadius() {
		return boundingRadious;
	}

	@Override
	public boolean isTagged() {
		return tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		this.tagged = tagged;
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
		result = prime * result + ((ratioMaxLinearSpeed == null) ? 0 : ratioMaxLinearSpeed.hashCode());
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
		MovingObject other = (MovingObject) obj;
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
		if (ratioMaxLinearSpeed == null) {
			if (other.ratioMaxLinearSpeed != null)
				return false;
		} else if (!ratioMaxLinearSpeed.equals(other.ratioMaxLinearSpeed))
			return false;
		if (tagged != other.tagged)
			return false;
		if (Float.floatToIntBits(zeroThreshold) != Float.floatToIntBits(other.zeroThreshold))
			return false;
		return true;
	}	
}
