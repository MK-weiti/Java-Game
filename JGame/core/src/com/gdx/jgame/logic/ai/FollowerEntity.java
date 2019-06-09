package com.gdx.jgame.logic.ai;

import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.Face;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.gameObjects.MovingObject;

public class FollowerEntity {

	private MovingObject m_behaviorOwner;
	private MovingObject m_targer;
	private Arrive<Vector2> m_arrive;
	private Face<Vector2> m_face;
	private BlendedSteering<Vector2> m_blend;
	
	SteeringBehavior<Vector2> behavior;
	
	public FollowerEntity(MovingObject behaviorOwner, MovingObject targer) {
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
}
