package com.gdx.jgame.logic.ai;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.gameObjects.MovingObject;

public class FollowerEntity {

	private MovingObject m_entity;
	private MovingObject m_target;
	private Arrive<Vector2> m_arrive;
	
	SteeringBehavior<Vector2> behavior;
	
	public FollowerEntity(MovingObject entity, MovingObject target) {
		m_entity = entity;
		m_target = target;
		
		m_arrive = new Arrive<Vector2>(m_entity, m_target);
	}
	
	public Arrive<Vector2> getArrive() {
		return m_arrive;
	}
	
	public void setBehavior() {
		m_entity.setBehavior(m_arrive);
	}
}
