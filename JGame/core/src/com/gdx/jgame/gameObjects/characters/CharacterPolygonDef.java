package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.ObjectsID;
import com.gdx.jgame.TextureManager;
import com.gdx.jgame.gameObjects.PalpableObjectPolygonDef;

public class CharacterPolygonDef extends PalpableObjectPolygonDef implements Serializable, ObjectsID{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2433937948903941797L;
	
	private static long m_numberOfObjects = 0;
	public final long ID;
	
	public float maxVelocity = 1f;
	public float acceleration = 0f;
	public int maxHealth = 5;
	public int m_health = maxHealth;
	public CharType charType;
	public String charGroupName = null;
	
	public enum CharType {
		Player,
		Enemy;
	}
	
	public CharacterPolygonDef(World world, TextureManager texManager, String texPath, 
			float texScale, String characterGroupName, Vector2[] b2vertices, CharType charType) {	
		super(world, texManager.get(texPath), texPath, texScale, b2vertices);
		ID = m_numberOfObjects;
		++m_numberOfObjects;
		this.charType = charType;
		charGroupName = characterGroupName;
	}

	public CharacterPolygonDef(CharacterPolygonDef definition) {
		super((PalpableObjectPolygonDef)definition);
		ID = m_numberOfObjects;
		++m_numberOfObjects;
		maxVelocity = definition.maxVelocity;
		acceleration = definition.acceleration;
		maxHealth = definition.maxHealth;
		m_health = definition.m_health;
		charType = definition.charType;
		if(definition.charGroupName != null) { // Player do not have a group
			charGroupName = new String(definition.charGroupName);
		}
	}
	
	public void restoreCharacter(TextureManager txMan, World world) {
		super.restoreObject(txMan, world);
	}
	
	public long numberOfObjects() {
		return m_numberOfObjects;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (ID ^ (ID >>> 32));
		result = prime * result + Float.floatToIntBits(acceleration);
		result = prime * result + ((charGroupName == null) ? 0 : charGroupName.hashCode());
		result = prime * result + ((charType == null) ? 0 : charType.hashCode());
		result = prime * result + m_health;
		result = prime * result + maxHealth;
		result = prime * result + Float.floatToIntBits(maxVelocity);
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
		if (ID != other.ID)
			return false;
		if (Float.floatToIntBits(acceleration) != Float.floatToIntBits(other.acceleration))
			return false;
		if (charGroupName == null) {
			if (other.charGroupName != null)
				return false;
		} else if (!charGroupName.equals(other.charGroupName))
			return false;
		if (charType != other.charType)
			return false;
		if (m_health != other.m_health)
			return false;
		if (maxHealth != other.maxHealth)
			return false;
		if (Float.floatToIntBits(maxVelocity) != Float.floatToIntBits(other.maxVelocity))
			return false;
		return true;
	}

	
}
