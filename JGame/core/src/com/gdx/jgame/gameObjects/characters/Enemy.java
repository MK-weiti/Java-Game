package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.ObjectsID;

public class Enemy extends PlainCharacter implements ObjectsID{
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;

	public Enemy(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef);
		++m_numberOfObjects;
		this.getBody().setUserData(this);
	}	
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}
}
