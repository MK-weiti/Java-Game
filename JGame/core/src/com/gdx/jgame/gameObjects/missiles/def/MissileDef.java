package com.gdx.jgame.gameObjects.missiles.def;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.gameObjects.MovingObjectDef;
import com.gdx.jgame.gameObjects.PalpableObject;
import com.gdx.jgame.gameObjects.characters.BasicEnemy;
import com.gdx.jgame.gameObjects.characters.Player;
import com.gdx.jgame.gameObjects.characters.def.BasicEnemyDef;
import com.gdx.jgame.gameObjects.characters.def.PlayerDef;
import com.gdx.jgame.gameObjects.missiles.MissileAdapter;
import com.gdx.jgame.gameObjects.missiles.NormalBullet;
import com.gdx.jgame.managers.TextureManager;

public abstract class MissileDef extends MovingObjectDef implements Serializable, MissileDefMethods{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1609565569277397245L;
	protected transient Object owner;
	private int ownerId;
	public int damage = 0;
	
	
	public MissileDef(World world, TextureManager txManager, String texPath, float texScale, Vector2[] vertices, Object owner) {
		super(world, txManager.get(texPath), texPath, texScale, vertices);
		setOwner(owner);
	}
	
	public MissileDef(MissileDef definition) {
		super(definition);
		setOwner(definition.owner);
	}
	
	public MissileDef(MissileAdapter definition) {
		super(definition);
		setOwner(definition.getOwner());
	}

	public Object getOwner() {
		return owner;
	}

	public int getOwnerId() {
		return ownerId;
	}
	
	public void restore(TextureManager txMan, World world, Object owner) {
		super.restore(txMan, world);
		setOwner(owner);
	}
	
	public void setOwner(Object owner) {
		this.owner = owner;
		/*if(owner instanceof Player) {
			PlayerDef tmp = new PlayerDef((Player) owner);
			ownerId = (tmp.hashCode());
		} else if(owner instanceof BasicEnemy) {
			BasicEnemyDef tmp = new BasicEnemyDef((BasicEnemy) owner);
			ownerId = (tmp.hashCode());
		}*/
	}
	
	public void setOwnerID(int hash) {
		ownerId = hash;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ownerId;
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
		if (ownerId != other.ownerId)
			return false;
		return true;
	}

}
