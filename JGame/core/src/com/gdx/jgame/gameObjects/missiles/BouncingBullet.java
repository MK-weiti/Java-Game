package com.gdx.jgame.gameObjects.missiles;

import java.awt.IllegalComponentStateException;

import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;

public class BouncingBullet extends Missile{
	
	private int numberOfBounces;

	public BouncingBullet(BouncingBulletDef bullet, IDAdapter owner) {
		super(bullet, owner);
		if(bullet.numberOfBounces == 0) throw new IllegalComponentStateException("No numberOfBounces.");
		
		numberOfBounces = bullet.numberOfBounces;
	}

	@Override
	public void updateObject(Object object) {
		--numberOfBounces;
	}

	@Override
	public boolean isRemovable() {
		if(numberOfBounces <= 0) return true;
		return false;
	}

	@Override
	public void deleteObject() {
		destoryBody();
	}

	public int getNumberOfBounces() {
		return numberOfBounces;
	}

}
