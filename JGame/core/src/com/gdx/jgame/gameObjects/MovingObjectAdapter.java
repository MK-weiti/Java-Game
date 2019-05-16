package com.gdx.jgame.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public abstract class MovingObjectAdapter extends PalpableObject{
	
	public float maxVelocity;
	public float acceleration;
	
	public MovingObjectAdapter(PalpableObjectPolygonDef objectDef,
			float maxVelocity, float acceleration) {
		super(objectDef);
		this.maxVelocity = maxVelocity;
		this.acceleration = acceleration;
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
		return acceleration;
	}
	
	public float getImpulse() {
		return Gdx.graphics.getDeltaTime()*acceleration;
	}
}
