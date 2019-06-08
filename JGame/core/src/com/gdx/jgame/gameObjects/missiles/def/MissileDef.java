package com.gdx.jgame.gameObjects.missiles.def;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.MovingObjectDef;
import com.gdx.jgame.gameObjects.PalpableObject;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.managers.TextureManager;

public abstract class MissileDef extends MovingObjectDef implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1609565569277397245L;
	protected transient Object owner = null;
	// created only from existing object in game
	private int objectOwnerID = -1;
	public int damage = 0;
	
	
	public MissileDef(World world, TextureManager txManager, String texPath, float texScale, Vector2[] vertices, Object owner) {
		super(world, txManager.get(texPath), texPath, texScale, vertices);
		setOwner(owner);
	}
	
	public MissileDef(MissileDef definition) {
		super(definition);
		setOwner(definition.owner);
		damage = definition.damage;
		objectOwnerID = definition.getObjectInGameID();
	}
	
	public MissileDef(Missile missile) {
		super(missile);
		setOwner(missile.getOwner());
		damage = missile.getDamage();
	}

	public Object getOwner() {
		return owner;
	}
	
	public void restore(TextureManager txMan, World world, Object owner) {
		super.restore(txMan, world);
		setOwner(owner);
		objectOwnerID = ((PalpableObject) owner).ID;
	}
	
	public void setOwner(Object owner) {
		this.owner = owner;
		objectOwnerID = ((PalpableObject) owner).ID;
	}
	
	public int getObjectOwnerID() {
		return objectOwnerID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + damage;
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
		MissileDef other = (MissileDef) obj;
		if (damage != other.damage)
			return false;
		return true;
	}

}
