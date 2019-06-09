package com.gdx.jgame.gameObjects;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Utils;

public abstract class MovingObject extends PalpableObject implements Steerable<Vector2>{
	
	private float m_maxVelocity;
	
	// the acceleration which object will have when control over him was taken over.
	private float m_acceleration;	
	
	private float m_maxLinearSpeed = 1f, m_maxLinearAcceleration;
	private float m_maxAngularSpeed, m_maxAngularAcceleration;
	private float m_zeroThreshold = 0.1f;
	
	// fields for buffs, debuffs
	private HashMap<Integer, Float> m_ratioAcceleration;
	private HashMap<Integer, Float> m_ratioMaxLinearSpeed;
	
	private boolean m_tagged = false;
	private float m_boundingRadious = 1f; // used for Collision Avoidance
	private float m_boundingToActivateAI = 4f;
	private BlendedSteering<Vector2> m_blend;
	
	public MovingObject(PalpableObjectPolygonDef objectDef,
			float maxVelocity, float acceleration) {
		super(objectDef);
		this.m_maxVelocity = maxVelocity;
		this.m_acceleration = acceleration;
		m_ratioAcceleration = new HashMap<Integer, Float>();
		m_ratioMaxLinearSpeed = new HashMap<Integer, Float>();
	}
	
	public MovingObject(MovingObjectDef definition) {
		super(definition);
		this.m_maxVelocity = definition.maxVelocity;
		this.m_acceleration = definition.acceleration;
		
		m_ratioAcceleration = new HashMap<Integer, Float>(definition.getRatioAcceleration());
		m_ratioMaxLinearSpeed = new HashMap<Integer, Float>(definition.getRatioMaxVelocity());
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
		float tmp = m_acceleration;
		for (float next : m_ratioAcceleration.values()) {
			tmp *= next;
		}
		return tmp;
	}

	public float getRatioAcceleration() {
		float tmp = 1f;
		for (float next : m_ratioAcceleration.values()) {
			tmp *= next;
		}
		return tmp;
	}

	public float getRatioMaxLinearSpeed() {
		float tmp = 1f;
		for (float next : m_ratioMaxLinearSpeed.values()) {
			tmp *= next;
		}
		return tmp;
	}
	
	public void addRatioAcceleration(int ratioId, float newRatio) {
		m_ratioAcceleration.put(ratioId, newRatio);
	}

	public void addRatioMaxLinearSpeed(int ratioId, float newRatio) {
		m_ratioMaxLinearSpeed.put(ratioId, newRatio);
	}
	
	public void removeRatioAcceleration(int ratioId) {
		m_ratioAcceleration.remove(ratioId);
	}
	
	public void removeRatioMaxLinearSpeed(int ratioId) {
		m_ratioMaxLinearSpeed.remove(ratioId);
	}
	
	void setAcceleration(float acceleration) {
		this.m_acceleration = acceleration;
	}
	
	public int generateAccelerationId() {
		
		Random generator = new Random();
		int result;
		do {
			result = generator.nextInt();
		}
		while(m_ratioAcceleration.containsKey(result));
		return result;
	}
	
	public int generateMaxVelocityId() {
		Random generator = new Random();
		int result;
		do {
			result = generator.nextInt();
		}
		while(m_ratioMaxLinearSpeed.containsKey(result));
		return result;
	}

	public float getImpulse() {
		return Gdx.graphics.getDeltaTime()*getAcceleration();
	}
	
	public float getRawAcceleration() {
		return m_acceleration;
	}
	
	public float getRawMaxVelocity() {
		return m_maxVelocity;
	}
	
	HashMap<Integer, Float> getAccelerationRatios(){
		return m_ratioAcceleration;
	}
	
	HashMap<Integer, Float> getMaxVelocityRatios(){
		return m_ratioMaxLinearSpeed;
	}
	
	//
	// AI part
	//
	
	
	// TODO
	public boolean canAIrun() {
		return m_blend != null && ((Arrive<Vector2>) m_blend.get(0).getBehavior())
				.getTarget().getPosition().dst(getBody().getPosition()) < m_boundingToActivateAI;
	}
	
	public void updateAI(float deltaTime) {
		if(canAIrun()) {
			SteeringAcceleration<Vector2> steerOutput = new SteeringAcceleration<Vector2>(new Vector2());
			m_blend.calculateSteering(steerOutput);
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
			if(currentSpeedSquare > m_maxLinearSpeed * m_maxLinearSpeed) {
				getBody().setLinearVelocity(velocity.scl(m_maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
			}
			
			if(getBody().getAngularVelocity() > m_maxAngularSpeed) {
				getBody().setAngularVelocity(m_maxAngularSpeed);
			}
		}		
	}
	
	public void setBehavior(BlendedSteering<Vector2> blend) {
		m_blend = blend;
	}
	
	public BlendedSteering<Vector2> getBehavior() {
		return m_blend;
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
		float tmp = m_maxLinearSpeed;
		for (float next : m_ratioMaxLinearSpeed.values()) {
			tmp *= next;
		}
		return tmp;
	}

	@Override
	public void setMaxLinearSpeed(float maxLinearSpeed) {
		this.m_maxLinearSpeed = maxLinearSpeed;
	}

	@Override
	public float getMaxLinearAcceleration() {
		return m_maxLinearAcceleration;
	}

	@Override
	public void setMaxLinearAcceleration(float maxLinearAcceleration) {
		this.m_maxLinearAcceleration = maxLinearAcceleration;
	}

	@Override
	public float getMaxAngularSpeed() {
		return m_maxAngularSpeed;
	}

	@Override
	public void setMaxAngularSpeed(float maxAngularSpeed) {
		this.m_maxAngularSpeed = maxAngularSpeed;
	}

	@Override
	public float getMaxAngularAcceleration() {
		return m_maxAngularAcceleration;
	}

	@Override
	public void setMaxAngularAcceleration(float maxAngularAcceleration) {
		this.m_maxAngularAcceleration = maxAngularAcceleration;
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
		result = prime * result + Float.floatToIntBits(m_acceleration);
		result = prime * result + Float.floatToIntBits(m_boundingRadious);
		result = prime * result + Float.floatToIntBits(m_boundingToActivateAI);
		result = prime * result + (m_tagged ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(m_zeroThreshold);
		result = prime * result + Float.floatToIntBits(m_maxAngularAcceleration);
		result = prime * result + Float.floatToIntBits(m_maxAngularSpeed);
		result = prime * result + Float.floatToIntBits(m_maxLinearAcceleration);
		result = prime * result + Float.floatToIntBits(m_maxLinearSpeed);
		result = prime * result + Float.floatToIntBits(m_maxVelocity);
		result = prime * result + ((m_ratioAcceleration == null) ? 0 : m_ratioAcceleration.hashCode());
		result = prime * result + ((m_ratioMaxLinearSpeed == null) ? 0 : m_ratioMaxLinearSpeed.hashCode());
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
		if (Float.floatToIntBits(m_acceleration) != Float.floatToIntBits(other.m_acceleration))
			return false;
		if (Float.floatToIntBits(m_boundingRadious) != Float.floatToIntBits(other.m_boundingRadious))
			return false;
		if (Float.floatToIntBits(m_boundingToActivateAI) != Float.floatToIntBits(other.m_boundingToActivateAI))
			return false;
		if (m_tagged != other.m_tagged)
			return false;
		if (Float.floatToIntBits(m_zeroThreshold) != Float.floatToIntBits(other.m_zeroThreshold))
			return false;
		if (Float.floatToIntBits(m_maxAngularAcceleration) != Float.floatToIntBits(other.m_maxAngularAcceleration))
			return false;
		if (Float.floatToIntBits(m_maxAngularSpeed) != Float.floatToIntBits(other.m_maxAngularSpeed))
			return false;
		if (Float.floatToIntBits(m_maxLinearAcceleration) != Float.floatToIntBits(other.m_maxLinearAcceleration))
			return false;
		if (Float.floatToIntBits(m_maxLinearSpeed) != Float.floatToIntBits(other.m_maxLinearSpeed))
			return false;
		if (Float.floatToIntBits(m_maxVelocity) != Float.floatToIntBits(other.m_maxVelocity))
			return false;
		if (m_ratioAcceleration == null) {
			if (other.m_ratioAcceleration != null)
				return false;
		} else if (!m_ratioAcceleration.equals(other.m_ratioAcceleration))
			return false;
		if (m_ratioMaxLinearSpeed == null) {
			if (other.m_ratioMaxLinearSpeed != null)
				return false;
		} else if (!m_ratioMaxLinearSpeed.equals(other.m_ratioMaxLinearSpeed))
			return false;
		return true;
	}
	
	
}
