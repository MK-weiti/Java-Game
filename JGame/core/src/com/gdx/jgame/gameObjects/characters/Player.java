package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.ObjectsID;

public class Player extends PlainCharacter implements ObjectsID{
	
	private static long m_numberOfObjects = 0;
	public final long ID;
	
	public Player(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef);
		ID = m_numberOfObjects;
		++m_numberOfObjects;
	}
	
	public long m_numberOfObjects() {
		return m_numberOfObjects;
	}
}
