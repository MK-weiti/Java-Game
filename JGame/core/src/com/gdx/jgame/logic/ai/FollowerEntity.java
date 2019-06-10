package com.gdx.jgame.logic.ai;

import java.io.Serializable;
import java.util.TreeMap;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.gameObjects.MovingObject;

public class FollowerEntity extends AIAdapter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6792245238024456874L;
	
	private transient MovingObject m_behaviorOwner;
	private transient MovingObject m_targer;
	private transient Arrive<Vector2> m_arrive;
	private transient Face<Vector2> m_face;
	private transient BlendedSteering<Vector2> m_blend;
	
	private int ownerId;
	private int targetId;
	
	private float arrive_arrivalTolerance;
	private float arrive_decelerationRadius;
	private float arrive_timeToTarget;
	
	private float face_alignTolerance;
	private float face_decelerationRadius;
	private float face_timeToTarget;
	
	public FollowerEntity(MovingObject behaviorOwner, MovingObject targer) {
		super();
		m_behaviorOwner = behaviorOwner;
		m_targer = targer;
		
		m_arrive = new Arrive<Vector2>(m_behaviorOwner, m_targer);
		m_face = new Face<Vector2>(m_behaviorOwner, m_targer);
		m_blend = new BlendedSteering<Vector2>(m_behaviorOwner);
		
	}
	
	public Arrive<Vector2> getArrive() {
		return m_arrive;
	}
	
	public Face<Vector2> getFace(){
		return m_face;
	}
	
	public void setBehavior() {
		m_blend.add(m_arrive, 1f);
		m_blend.add(m_face, 1f);
		m_behaviorOwner.setBehavior(m_blend);
	}
	
	@Override
	public void save() {
		ownerId = m_behaviorOwner.ID;
		targetId = m_targer.ID;
		
		arrive_arrivalTolerance = m_arrive.getArrivalTolerance();
		arrive_decelerationRadius = m_arrive.getDecelerationRadius();
		arrive_timeToTarget = m_arrive.getTimeToTarget();
		
		face_alignTolerance = m_face.getAlignTolerance();
		face_decelerationRadius = m_face.getDecelerationRadius();
		face_timeToTarget = m_face.getTimeToTarget();
	}
	
	@Override
	public void load(TreeMap<Integer, Object> restoreOwner) {
		m_behaviorOwner = (MovingObject) restoreOwner.get(ownerId);
		m_targer = (MovingObject) restoreOwner.get(targetId);
		
		m_arrive = new Arrive<Vector2>(m_behaviorOwner, m_targer);
		m_face = new Face<Vector2>(m_behaviorOwner, m_targer);
		m_blend = new BlendedSteering<Vector2>(m_behaviorOwner);
		
		m_arrive.setArrivalTolerance(arrive_arrivalTolerance);
		m_arrive.setDecelerationRadius(arrive_decelerationRadius);
		m_arrive.setTimeToTarget(arrive_timeToTarget);
		
		m_face.setAlignTolerance(face_alignTolerance);
		m_face.setDecelerationRadius(face_decelerationRadius);
		m_face.setTimeToTarget(face_timeToTarget);
		
		m_blend.add(m_arrive, 1f);
		m_blend.add(m_face, 1f);
		m_behaviorOwner.setBehavior(m_blend);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(arrive_arrivalTolerance);
		result = prime * result + Float.floatToIntBits(arrive_decelerationRadius);
		result = prime * result + Float.floatToIntBits(arrive_timeToTarget);
		result = prime * result + Float.floatToIntBits(face_alignTolerance);
		result = prime * result + Float.floatToIntBits(face_decelerationRadius);
		result = prime * result + Float.floatToIntBits(face_timeToTarget);
		result = prime * result + ((m_behaviorOwner == null) ? 0 : m_behaviorOwner.hashCode());
		result = prime * result + ((m_targer == null) ? 0 : m_targer.hashCode());
		result = prime * result + ownerId;
		result = prime * result + targetId;
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
		FollowerEntity other = (FollowerEntity) obj;
		if (Float.floatToIntBits(arrive_arrivalTolerance) != Float.floatToIntBits(other.arrive_arrivalTolerance))
			return false;
		if (Float.floatToIntBits(arrive_decelerationRadius) != Float.floatToIntBits(other.arrive_decelerationRadius))
			return false;
		if (Float.floatToIntBits(arrive_timeToTarget) != Float.floatToIntBits(other.arrive_timeToTarget))
			return false;
		if (Float.floatToIntBits(face_alignTolerance) != Float.floatToIntBits(other.face_alignTolerance))
			return false;
		if (Float.floatToIntBits(face_decelerationRadius) != Float.floatToIntBits(other.face_decelerationRadius))
			return false;
		if (Float.floatToIntBits(face_timeToTarget) != Float.floatToIntBits(other.face_timeToTarget))
			return false;
		if (m_behaviorOwner == null) {
			if (other.m_behaviorOwner != null)
				return false;
		} else if (!m_behaviorOwner.equals(other.m_behaviorOwner))
			return false;
		if (m_targer == null) {
			if (other.m_targer != null)
				return false;
		} else if (!m_targer.equals(other.m_targer))
			return false;
		if (ownerId != other.ownerId)
			return false;
		if (targetId != other.targetId)
			return false;
		return true;
	}	
}
