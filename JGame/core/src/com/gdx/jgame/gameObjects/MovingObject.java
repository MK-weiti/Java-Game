package com.gdx.jgame.gameObjects;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Utils;

public abstract class MovingObject extends PalpableObject implements Steerable<Vector2>{
	
	private float maxVelocity;
	private float acceleration;
	
	private float maxLinearSpeed, maxLinearAcceleration;
	private float maxAngularSpeed, maxAngularAcceleration;
	
	// fields for buffs, debuffs
	private HashMap<Integer, Float> ratioAcceleration;
	private HashMap<Integer, Float> ratioMaxVelocity;
	
	private boolean m_tagged = false;
	private float m_boundingRadious = 1f; // used for Collision Avoidance
	private float m_zeroThreshold = 0.1f;
	private float m_boundingToActivateAI = 4f;
	private Arrive<Vector2> m_behavior = null;	
	
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
	
	
	
	
	
	public void updateAI(float deltaTime) {
		if(m_behavior != null && m_behavior.getTarget().getPosition().dst(getBody().getPosition()) < m_boundingToActivateAI) {
			SteeringAcceleration<Vector2> steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
			m_behavior.calculateSteering(steerOutput);
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
		
		if(anyAccelerations) {
			Vector2 velocity = getBody().getLinearVelocity();
			float currentSpeedSquare = velocity.len2();
			if(currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
				getBody().setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
			}
		}
	}
	
	public void setBehavior(Arrive<Vector2> behavior) {
		m_behavior = behavior;
	}
	
	public SteeringBehavior<Vector2> getBehaviour() {
		return m_behavior;
	}
	
	public float getBoundingToActivateAI() {
		return m_boundingToActivateAI;
	}

	public void setBoundingToActivateAI(float m_boundingToActivateAI) {
		this.m_boundingToActivateAI = m_boundingToActivateAI;
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
		return m_zeroThreshold;
	}

	@Override
	public void setZeroLinearSpeedThreshold(float value) {
		m_zeroThreshold = value;
	}

	@Override
	public float getMaxLinearSpeed() {
		float tmp = maxLinearSpeed;
		for (float next : ratioMaxVelocity.values()) {
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
		return m_boundingRadious;
	}

	@Override
	public boolean isTagged() {
		return m_tagged;
	}

	@Override
	public void setTagged(boolean tagged) {
		m_tagged = tagged;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(acceleration);
		result = prime * result + Float.floatToIntBits(m_boundingRadious);
		result = prime * result + Float.floatToIntBits(m_boundingToActivateAI);
		result = prime * result + (m_tagged ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(m_zeroThreshold);
		result = prime * result + Float.floatToIntBits(maxAngularAcceleration);
		result = prime * result + Float.floatToIntBits(maxAngularSpeed);
		result = prime * result + Float.floatToIntBits(maxLinearAcceleration);
		result = prime * result + Float.floatToIntBits(maxLinearSpeed);
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
		MovingObject other = (MovingObject) obj;
		if (Float.floatToIntBits(acceleration) != Float.floatToIntBits(other.acceleration))
			return false;
		if (Float.floatToIntBits(m_boundingRadious) != Float.floatToIntBits(other.m_boundingRadious))
			return false;
		if (Float.floatToIntBits(m_boundingToActivateAI) != Float.floatToIntBits(other.m_boundingToActivateAI))
			return false;
		if (m_tagged != other.m_tagged)
			return false;
		if (Float.floatToIntBits(m_zeroThreshold) != Float.floatToIntBits(other.m_zeroThreshold))
			return false;
		if (Float.floatToIntBits(maxAngularAcceleration) != Float.floatToIntBits(other.maxAngularAcceleration))
			return false;
		if (Float.floatToIntBits(maxAngularSpeed) != Float.floatToIntBits(other.maxAngularSpeed))
			return false;
		if (Float.floatToIntBits(maxLinearAcceleration) != Float.floatToIntBits(other.maxLinearAcceleration))
			return false;
		if (Float.floatToIntBits(maxLinearSpeed) != Float.floatToIntBits(other.maxLinearSpeed))
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
