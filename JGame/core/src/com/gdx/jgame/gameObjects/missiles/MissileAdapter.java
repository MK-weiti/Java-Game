package com.gdx.jgame.gameObjects.missiles;

import com.gdx.jgame.gameObjects.*;
import com.gdx.jgame.gameObjects.missiles.def.MissileDef;

public abstract class MissileAdapter extends MovingObject implements MisslesMethods{
	
	public enum MissileType {
		NormalBullet,
		BouncingBullet;
	}
	
	private Object m_owner;
	private int m_damage;
	
	public MissileAdapter(MissileDef bullet, Object owner) {
		super(bullet);
		getBody().setBullet(true);
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
