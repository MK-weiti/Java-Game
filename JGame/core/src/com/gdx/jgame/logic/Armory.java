package com.gdx.jgame.logic;

import java.io.Serializable;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.missiles.BouncingBullet;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.gameObjects.missiles.NormalBullet;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;
import com.gdx.jgame.managers.TextureManager;

public class Armory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3412328135321223411L;
	
	public float initialImpulseNormalBullet = 0.005f;
	public float initialImpulseBouncingBullet = 0.005f;
	
	public NormalBulletDef normalBulletDef = null;
	public BouncingBulletDef bouncingBulletDef = null;
	
	public int maxAmmoNormalBullet = 1;
	public int maxAmmoBouncingBullet = 1;
	
	public int currentAmmoNormalBullet = 1;
	public int currentAmmoBouncingBullet = 0;
	
	private transient TextureManager m_bulletsTextures;
	private transient MissilesManager m_missileManager;
	private transient PlainCharacter m_owner;
	private int m_ownerID;
	
	public Armory(TextureManager bulletsTextures, MissilesManager missileManager) {
		m_bulletsTextures = bulletsTextures;
		m_missileManager = missileManager;
	}
	
	public Armory(Armory copy) {
		initialImpulseNormalBullet = copy.initialImpulseNormalBullet;
		initialImpulseBouncingBullet = copy.initialImpulseBouncingBullet;
		
		if(copy.normalBulletDef != null && copy.normalBulletDef.getOwner() != null) 
			normalBulletDef = new NormalBulletDef(copy.normalBulletDef);
		if(copy.bouncingBulletDef != null && copy.bouncingBulletDef.getOwner() != null) 
			bouncingBulletDef = new BouncingBulletDef(copy.bouncingBulletDef);
		
		maxAmmoNormalBullet = copy.maxAmmoNormalBullet;
		maxAmmoBouncingBullet = copy.maxAmmoBouncingBullet;
		currentAmmoNormalBullet = copy.currentAmmoNormalBullet;
		currentAmmoBouncingBullet = copy.currentAmmoBouncingBullet;
		m_bulletsTextures = copy.m_bulletsTextures;
		m_missileManager = copy.m_missileManager;
		m_owner = copy.m_owner;
		m_ownerID = copy.m_ownerID;
	}
	
	public void setOwner(PlainCharacter owner) {
		m_owner = owner;
		m_ownerID = owner.ID;
		if(normalBulletDef != null) normalBulletDef.setOwner(owner);
		if(bouncingBulletDef != null) bouncingBulletDef.setOwner(owner);		
	}
	
	public void spawnBullet(Camera camera, Vector2 space, Missile.MissileType type) {
		Vector2 imp = null;
		Missile bullet = null;
		
		if(type.equals(Missile.MissileType.NormalBullet) && normalBulletDef != null && 
				currentAmmoNormalBullet > 0) {
			imp = m_owner.initialImpulseAndPosition(camera, space, normalBulletDef, initialImpulseNormalBullet);
			bullet = new NormalBullet(normalBulletDef, m_owner);
			--currentAmmoNormalBullet;
		}
		else if(type.equals(Missile.MissileType.BouncingBullet) && bouncingBulletDef != null &&
				currentAmmoBouncingBullet > 0) {
			imp = m_owner.initialImpulseAndPosition(camera, space, bouncingBulletDef, initialImpulseBouncingBullet);
			bullet = new BouncingBullet(bouncingBulletDef, m_owner);
			--currentAmmoBouncingBullet;
		}
		if(bullet == null) return;
		
		bullet.getBody().applyLinearImpulse(imp, bullet.getBody().getWorldCenter(), true);
		m_missileManager.add(bullet);
	}
	
	public void changeTexture(String textureName, Vector2[] vertices, Missile.MissileType type, float txScale) {
		PolygonShape shape = new PolygonShape();
		shape.set(vertices);
		
		if(type.equals(Missile.MissileType.NormalBullet)) {
			if(normalBulletDef == null) {
				shape.dispose();
				return;
			}
			normalBulletDef.texturePath = textureName;
			normalBulletDef.textureScale = txScale;
			normalBulletDef.fixtureDef.shape = shape;
			normalBulletDef.texture = m_bulletsTextures.get(textureName);
			normalBulletDef.setVertices(vertices);
		}
		else if(type.equals(Missile.MissileType.BouncingBullet)) {
			if(bouncingBulletDef == null) {
				shape.dispose();
				return;
			}
			bouncingBulletDef.texturePath = textureName;
			bouncingBulletDef.textureScale = txScale;
			bouncingBulletDef.fixtureDef.shape = shape;
			bouncingBulletDef.texture = m_bulletsTextures.get(textureName);
			bouncingBulletDef.setVertices(vertices);
		}
		
		shape.dispose();
	}	
	
	public void load(Map <Integer, Object> restoreOwner, TextureManager bulletsTextures, MissilesManager missileManager) {
		m_owner = (PlainCharacter) restoreOwner.get(m_ownerID);
		m_bulletsTextures = bulletsTextures;
		m_missileManager = missileManager;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bouncingBulletDef == null) ? 0 : bouncingBulletDef.hashCode());
		result = prime * result + currentAmmoBouncingBullet;
		result = prime * result + currentAmmoNormalBullet;
		result = prime * result + Float.floatToIntBits(initialImpulseBouncingBullet);
		result = prime * result + Float.floatToIntBits(initialImpulseNormalBullet);
		result = prime * result + m_ownerID;
		result = prime * result + maxAmmoBouncingBullet;
		result = prime * result + maxAmmoNormalBullet;
		result = prime * result + ((normalBulletDef == null) ? 0 : normalBulletDef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Armory other = (Armory) obj;
		if (bouncingBulletDef == null) {
			if (other.bouncingBulletDef != null)
				return false;
		} else if (!bouncingBulletDef.equals(other.bouncingBulletDef))
			return false;
		if (currentAmmoBouncingBullet != other.currentAmmoBouncingBullet)
			return false;
		if (currentAmmoNormalBullet != other.currentAmmoNormalBullet)
			return false;
		if (Float.floatToIntBits(initialImpulseBouncingBullet) != Float
				.floatToIntBits(other.initialImpulseBouncingBullet))
			return false;
		if (Float.floatToIntBits(initialImpulseNormalBullet) != Float.floatToIntBits(other.initialImpulseNormalBullet))
			return false;
		if (m_ownerID != other.m_ownerID)
			return false;
		if (maxAmmoBouncingBullet != other.maxAmmoBouncingBullet)
			return false;
		if (maxAmmoNormalBullet != other.maxAmmoNormalBullet)
			return false;
		if (normalBulletDef == null) {
			if (other.normalBulletDef != null)
				return false;
		} else if (!normalBulletDef.equals(other.normalBulletDef))
			return false;
		return true;
	}
	
}
