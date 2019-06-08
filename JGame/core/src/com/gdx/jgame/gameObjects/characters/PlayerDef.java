package com.gdx.jgame.gameObjects.characters;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.characters.def.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.logic.Armory;
import com.gdx.jgame.managers.TextureManager;

public class PlayerDef extends CharacterPolygonDef implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7507812240552005946L;
	
	public Armory armory = null;
	
	public PlayerDef(World world, TextureManager charTexManager, TextureManager missileTexManager, MissilesManager missileManager,
			String texPath, float texScale, String characterGroupName, Vector2[] b2vertices) {
		super(world,  charTexManager, texPath, texScale, characterGroupName, b2vertices);
		armory = new Armory(missileTexManager, missileManager);
	}
	
	public PlayerDef(PlayerDef definition) {
		super(definition);
		armory = definition.armory;
	}
	
	public PlayerDef(Player player) {
		super(player);
		armory = player.m_armory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((armory == null) ? 0 : armory.hashCode());
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
		PlayerDef other = (PlayerDef) obj;
		if (armory == null) {
			if (other.armory != null)
				return false;
		} else if (!armory.equals(other.armory))
			return false;
		return true;
	}
}
