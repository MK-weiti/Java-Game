package com.gdx.jgame.gameObjects.missiles;

import com.gdx.jgame.gameObjects.*;
import com.gdx.jgame.gameObjects.missiles.def.MissileDef;

public abstract class Missile extends MovingObject implements MisslesMethods{
	
	// only for specifying what kind of missile you want to spawn
	public enum MissileType {
		NormalBullet,
		BouncingBullet;
	}
	
	private Object m_owner;
	private int m_damage;
	
	public Missile(MissileDef bullet, Object owner) {
		super(bullet);
		getBody().setBullet(true);
		if(owner == null) throw new IllegalArgumentException("NormalBullet have not any owner");
		m_owner = bullet.getOwner();
		m_damage = bullet.damage;
	}
	
	public Object getOwner() {
		return m_owner;
	}

	public int getDamage() {
		return m_damage;
	}
	
}
