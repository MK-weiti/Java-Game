package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.gameObjects.characters.def.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.missiles.MissileAdapter;

public class BasicEnemy extends PlainCharacter{
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;

	public BasicEnemy(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef);
		++m_numberOfObjects;
		this.getBody().setUserData(this);
	}	
	
	public long numberOfObjects() {
		return m_numberOfObjects;
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
