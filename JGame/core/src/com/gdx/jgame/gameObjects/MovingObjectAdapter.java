package com.gdx.jgame.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public abstract class MovingObjectAdapter extends PalpableObject{
	
	public float maxVelocity;	// as a rectangle
	public float acceleration;
	
	public MovingObjectAdapter(Texture defaultTexture, World world, Vector2 pos, float scale,
			float maxVelocity, float acceleration, BodyType bodyType) {
		super(defaultTexture, world, pos, scale, bodyType);
		this.maxVelocity = maxVelocity;
		this.acceleration = acceleration;
	}
	
	private void applyImpulseWithin(float deltaX, float deltaY) {
		body.applyLinearImpulse(new Vector2(deltaX, deltaY), fixture.getBody().getWorldCenter(), true);
	}
	
	public float getActualVelocity() {
		return (float) Math.hypot(body.getLinearVelocity().x, body.getLinearVelocity().y);
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
