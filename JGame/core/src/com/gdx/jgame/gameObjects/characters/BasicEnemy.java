package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.gameObjects.characters.def.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.missiles.Missile;

public class BasicEnemy extends PlainCharacter{

	public BasicEnemy(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef);
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
