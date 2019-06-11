package com.gdx.jgame.gameObjects.missiles.def;

import java.io.Serializable;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.SaveReference;
import com.gdx.jgame.gameObjects.MovingObjectDef;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.managers.TextureManager;

public abstract class MissileDef extends MovingObjectDef implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1609565569277397245L;
	
	protected SaveReference<IDAdapter> owner;
	public int damage = 0;
	
	
	public MissileDef(World world, TextureManager txManager, String texPath, float texScale, Vector2[] vertices) {
		super(world, txManager.get(texPath), texPath, texScale, vertices);
		this.owner = new SaveReference<IDAdapter>();
	}
	
	public MissileDef(MissileDef definition) {
		super(definition);
		this.owner = new SaveReference<IDAdapter>();
		this.owner.setOwner(definition.getOwner());
		this.owner.save();
		damage = definition.damage;
	}
	
	public MissileDef(Missile missile) {
		super(missile);
		this.owner = new SaveReference<IDAdapter>();
		this.owner.setOwner(missile.getOwner());
		this.owner.save();
		damage = missile.getDamage();
	}
	
	public void restore(TextureManager txMan, World world, Map<Integer, IDAdapter> ownerSet) {
		super.restore(txMan, world);
		this.owner.load(ownerSet);
		this.owner.save();
	}

	public IDAdapter getOwner() {
		return this.owner.getOwner();
	}	
	
	public int getOwnerID() {
		return this.owner.getOwnerID();
	}
	
	public void saveOwner() {
		this.owner.save();
	}
	
	public void setOwner(IDAdapter owner) {
		this.owner.setOwner(owner);
	}
	
	public void setAndSaveOwner(IDAdapter owner){
		this.owner.setAndSave(owner);
	}
	
	public int getObjectOwnerHashCode() {
		return this.owner.getSavedHashCode();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + damage;
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	@Override
	public void load(Map<Integer, IDAdapter> restoreOwner) {
		owner.load(restoreOwner);
	}

	@Override
	public void save() {
		owner.save();
	}

}
