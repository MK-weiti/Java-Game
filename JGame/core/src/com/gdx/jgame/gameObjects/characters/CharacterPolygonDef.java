package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.gameObjects.MovingObjectDef;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.logic.Armory;
import com.gdx.jgame.managers.TextureManager;

public class CharacterPolygonDef extends MovingObjectDef implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3557435280659148355L;
	
	public int maxHealth;
	public int health;
	public String charGroupName = null;
	
	public Armory armory = null;
	
	public CharacterPolygonDef(World world, TextureManager charTexManager, TextureManager missileTexManager, 
			MissilesManager missileManager, String texPath, 
			float texScale, String characterGroupName, Vector2[] b2vertices) {	
		super(world, charTexManager.get(texPath), texPath, texScale, b2vertices);
		charGroupName = characterGroupName;
		armory = new Armory(missileTexManager, missileManager);
	}

	public CharacterPolygonDef(PlainCharacter plainCharacter) {
		super(plainCharacter);
		
		maxHealth = plainCharacter.getMaxHealth();
		health = plainCharacter.getHealth();
		if(plainCharacter.getGroupName() != null) { // Player do not have a group
			charGroupName = new String(plainCharacter.getGroupName());
		}
		armory = plainCharacter.armory;
	}
	
	public CharacterPolygonDef(CharacterPolygonDef definition) {
		super(definition);
		maxHealth = definition.maxHealth;
		health = definition.health;
		charGroupName = new String(definition.charGroupName);
		
		armory = definition.armory;
	}
	
	public void restore(World world, Map <Integer, IDAdapter> restoreOwner, 
			TextureManager bulletsTextures, TextureManager txCharMan, MissilesManager missileManager) {
		armory.load(restoreOwner, bulletsTextures, missileManager);
		
		super.restore(txCharMan, world);
		if(armory.bouncingBulletDef != null)
			armory.bouncingBulletDef.restore(bulletsTextures, world);
		if(armory.normalBulletDef != null)
			armory.normalBulletDef.restore(bulletsTextures, world);
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((armory == null) ? 0 : armory.hashCode());
		result = prime * result + ((charGroupName == null) ? 0 : charGroupName.hashCode());
		result = prime * result + health;
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
		if (armory == null) {
			if (other.armory != null)
				return false;
		} else if (!armory.equals(other.armory))
			return false;
		if (charGroupName == null) {
			if (other.charGroupName != null)
				return false;
		} else if (!charGroupName.equals(other.charGroupName))
			return false;
		if (health != other.health)
			return false;
		if (maxHealth != other.maxHealth)
			return false;
		return true;
	}

	@Override
	public void load(Map<Integer, IDAdapter> restoreOwner) {
		return;
	}

	@Override
	public void save() {
		return;
	}
	
}
