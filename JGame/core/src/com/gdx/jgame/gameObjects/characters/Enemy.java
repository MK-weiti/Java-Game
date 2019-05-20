package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.ObjectsID;

public class Enemy extends PlainCharacter implements ObjectsID{
	
	private static long m_numberOfObjects = 0;
	public final long ID;

	public Enemy(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef);
		ID = m_numberOfObjects;
		++m_numberOfObjects;
	}	
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}
}
