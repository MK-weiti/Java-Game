package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.gameObjects.characters.def.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.missiles.MissileAdapter;

public class BasicEnemy extends PlainCharacter{

	public BasicEnemy(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef);
		this.getBody().setUserData(this);
	}	

	@Override
	public void updateObject(Object object) {
		if(object instanceof MissileAdapter) {
			changeHealth(((MissileAdapter) object).getDamage());
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
