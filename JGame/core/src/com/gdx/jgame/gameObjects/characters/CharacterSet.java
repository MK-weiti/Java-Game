package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;

import com.gdx.jgame.ObjectsID;
/*
 * Simple aggregate for data and object
 */
public class CharacterSet implements Serializable, ObjectsID{
	/**
	 * 
	 */
	private static final long serialVersionUID = 946959300540020496L;
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;
	
	public CharacterPolygonDef characterDef;	// data for saving 
	public transient PlainCharacter character;	// as actual object
	
	public CharacterSet(CharacterPolygonDef characterDef, PlainCharacter character) {
		++m_numberOfObjects;
		this.characterDef = characterDef;
		this.character = character;
	}
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}
	
	public void setRealTimeParam() {
		characterDef = new CharacterPolygonDef(character);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + ((characterDef == null) ? 0 : characterDef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharacterSet other = (CharacterSet) obj;
		if (ID != other.ID)
			return false;
		if (characterDef == null) {
			if (other.characterDef != null)
				return false;
		} else if (!characterDef.equals(other.characterDef))
			return false;
		return true;
	}	
}
