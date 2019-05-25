package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;

import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.managers.TextureManager;

public class SaveCharacter implements Serializable{
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;
	
	//private CharacterSet characterSet;
	private CharacterPolygonDef characterDef;
	
	public SaveCharacter(PlainCharacter character) {
		++m_numberOfObjects;
		characterDef = new CharacterPolygonDef(character);
		//this.characterSet = characterSet;
		//synchronize();
	}
	
	public SaveCharacter(CharacterSet characterSet) {
		++m_numberOfObjects;
		characterDef = new CharacterPolygonDef(characterSet.character);
		//this.characterSet = characterSet;
		//synchronize();
	}
	
	/*public void synchronize() {
		//characterSet.setRealTimeParam();
	}*/
	
	public void restoreCharacter(TextureManager txMan, World world) {
		//characterSet.characterDef.restoreCharacter(txMan, world);
		characterDef.restoreCharacter(txMan, world);
	}
	
	public CharacterPolygonDef getCharacterPolygonDef() {
		//return characterSet.characterDef;
		return characterDef;
	}
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}

	
}
