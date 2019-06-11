package com.gdx.jgame.gameObjects.missiles;

import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.SaveReference;
import com.gdx.jgame.gameObjects.*;
import com.gdx.jgame.gameObjects.missiles.def.MissileDef;

public abstract class Missile extends MovingObject implements MisslesMethods{
	
	// only for specifying what kind of missile you want to spawn
	public enum MissileType {
		NormalBullet,
		BouncingBullet;
	}
	
	private SaveReference<IDAdapter> owner;
	private int m_damage;
	
	public Missile(MissileDef bullet, IDAdapter owner) {
		super(bullet);
		this.owner = new SaveReference<IDAdapter>(owner);
		getBody().setBullet(true);
		if(owner == null) throw new IllegalArgumentException("NormalBullet have not any owner");
		m_damage = bullet.damage;
		bullet.setOwner(owner);
	}
	
	public IDAdapter getOwner() {
		return this.owner.getOwner();
	}
	
	public int getOwnerID() {
		return owner.getOwnerID();
	}

	public int getDamage() {
		return m_damage;
	}
	
}
