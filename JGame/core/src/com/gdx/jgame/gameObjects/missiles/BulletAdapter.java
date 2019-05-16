package com.gdx.jgame.gameObjects.missiles;

import com.gdx.jgame.gameObjects.*;

public abstract class BulletAdapter extends MovingObjectAdapter{
	
	public BulletAdapter(PalpableObjectPolygonDef objectDef, float maxVelocity, 
			float acceleration) {
		super(objectDef, maxVelocity, acceleration);
	}
	
	public void location() {
		// TODO
	}
	
}
