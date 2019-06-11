package com.gdx.jgame.gameObjects.characters;

import java.awt.IllegalComponentStateException;

import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.managers.TextureManager;

public class Player extends PlainCharacter{
	
	public Player(PlayerDef playerDef, TextureManager txBulletManager, MissilesManager missileManager) {
		super(playerDef);
		// must be here because for now only player must have all bullets definitions
		if(playerDef.armory.normalBulletDef == null) throw new IllegalComponentStateException("No normalBulletDef.");
		if(playerDef.armory.bouncingBulletDef == null) throw new IllegalComponentStateException("No bouncingBulletDef.");
	}

	@Override
	public void updateObject(Object object) {
		if(object instanceof Missile) {
			changeHealth(((Missile) object).getDamage());
		}
	}

	@Override
	public boolean isRemovable() {
		if(getHealth() > 0) return false;
		return isDestructible();
	}

	@Override
	public void deleteObject() {
		destoryBody();
	}	
}
