package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;

import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.managers.TextureManager;

public class SaveCharacter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2721809369968036105L;
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;
	
	private CharacterSet characterSet;
	
	public SaveCharacter(CharacterSet characterSet) {
		++m_numberOfObjects;
		this.characterSet = characterSet;
		synchronize();
	}
	
	public void synchronize() {
		characterSet.setRealTimeParam();
	}
	
	public void restoreCharacter(TextureManager txMan, World world) {
		characterSet.characterDef.restoreCharacter(txMan, world);
	}
	
	public CharacterPolygonDef getCharacterPolygonDef() {
		return characterSet.characterDef;
	}
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + ((characterSet == null) ? 0 : characterSet.hashCode());
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
		SaveCharacter other = (SaveCharacter) obj;
		if (ID != other.ID)
			return false;
		if (characterSet == null) {
			if (other.characterSet != null)
				return false;
		} else if (!characterSet.equals(other.characterSet))
			return false;
		return true;
	}

	
}
