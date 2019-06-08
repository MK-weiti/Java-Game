package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.logic.Armory;

public class BasicEnemy extends PlainCharacter{
	
	Armory m_armory;

	public BasicEnemy(BasicEnemyDef enemyDef) {
		super(enemyDef);
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
