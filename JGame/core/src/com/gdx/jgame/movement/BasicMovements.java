package com.gdx.jgame.movement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Utils;
import com.gdx.jgame.gameObjects.MovingObject;

public class BasicMovements {
	
	public static void movementV0(MovingObject object, Vector2 impulse){
		double x = 0.0, y = 0.0;
		float errorMargin = 0.00001f;
		
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) x = -1.0;
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += 1.0;
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) y = 1.0;
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= 1.0;
		
		double angle = x == 0.0 && y == 0.0 ? 0.0 : (float) Math.atan2(y, x);
		double velChangeX, velChangeY;
		double impulseX, impulseY;
		
		
		if(Math.abs(object.getLinearVelocity().x) <= object.getMinVelocity().x && !Utils.isZero(object.getLinearVelocity().x, errorMargin) ) {
			impulseX = object.getBody().getMass() * -object.getLinearVelocity().x;
		}
		else if(x != 0.0) {
			velChangeX = object.getMaxLinearSpeed() * Math.cos(angle) - (x == 0.0 ? 0.0 : object.getBody().getLinearVelocity().x);
			impulseX = x >= 0.0 ? Math.min(velChangeX * object.getBody().getMass(), object.getImpulse() * x) : Math.max(velChangeX * object.getBody().getMass(), object.getImpulse() * x);
		
		} 
		else {
			velChangeX = -object.getBody().getLinearVelocity().x;
			impulseX = velChangeX * object.getBody().getMass() * object.getBrakingIntensity().x;
		}
		
		
		
		if(Math.abs(object.getLinearVelocity().y) <= object.getMinVelocity().y && !Utils.isZero(object.getLinearVelocity().y, errorMargin)) {
			impulseY = object.getBody().getMass() * -object.getLinearVelocity().y;
		}
		else if(y != 0.0) {
			velChangeY = object.getMaxLinearSpeed() * Math.sin(angle) - (y == 0.0 ? 0.0 : object.getBody().getLinearVelocity().y);
			impulseY = y >= 0.0 ? Math.min(velChangeY * object.getBody().getMass(), object.getImpulse() * y) : Math.max(velChangeY * object.getBody().getMass(), object.getImpulse() * y);
		} 
		else {
			velChangeY = -object.getBody().getLinearVelocity().y;
			impulseY = velChangeY * object.getBody().getMass() * object.getBrakingIntensity().y;
		}
		impulse.set((float) impulseX, (float) impulseY);
	}
}
