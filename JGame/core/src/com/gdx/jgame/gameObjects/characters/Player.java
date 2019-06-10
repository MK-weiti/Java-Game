package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.managers.TextureManager;

public class Player extends PlainCharacter{
	
	public Player(PlayerDef playerDef, TextureManager txBulletManager, MissilesManager missileManager) {
		super(playerDef);
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
