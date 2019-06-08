package com.gdx.jgame.gameObjects.characters.def;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.MovingObjectDef;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.managers.TextureManager;

public class CharacterPolygonDef extends MovingObjectDef implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3557435280659148355L;
	
	public int maxHealth = 5;
	public int m_health = maxHealth;
	public String charGroupName = null;
	
	public CharacterPolygonDef(World world, TextureManager texManager, String texPath, 
			float texScale, String characterGroupName, Vector2[] b2vertices) {	
		super(world, texManager.get(texPath), texPath, texScale, b2vertices);
		charGroupName = characterGroupName;
	}

	public CharacterPolygonDef(PlainCharacter plainCharacter) {
		super(plainCharacter);
		
		maxHealth = plainCharacter.getMaxHealth();
		m_health = plainCharacter.getHealth();
		if(plainCharacter.getGroupName() != null) { // Player do not have a group
			charGroupName = new String(plainCharacter.getGroupName());
		}
	}
	
	public CharacterPolygonDef(CharacterPolygonDef definition) {
		super(definition);
		maxHealth = definition.maxHealth;
		m_health = definition.m_health;
		charGroupName = new String(definition.charGroupName);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((charGroupName == null) ? 0 : charGroupName.hashCode());
		result = prime * result + m_health;
		result = prime * result + maxHealth;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharacterPolygonDef other = (CharacterPolygonDef) obj;
		if (charGroupName == null) {
			if (other.charGroupName != null)
				return false;
		} else if (!charGroupName.equals(other.charGroupName))
			return false;
		if (m_health != other.m_health)
			return false;
		if (maxHealth != other.maxHealth)
			return false;
		return true;
	}
	
}
