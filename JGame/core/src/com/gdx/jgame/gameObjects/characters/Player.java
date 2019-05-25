package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.ObjectsID;

public class Player extends PlainCharacter implements ObjectsID{
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;
	
	public Player(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef);
		++m_numberOfObjects;
		this.getBody().setUserData(this);
	}
	
	public long m_numberOfObjects() {
		return m_numberOfObjects;
	}
}
