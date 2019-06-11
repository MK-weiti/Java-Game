package com.gdx.jgame.logic;

import java.io.Serializable;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gdx.jgame.Camera;
import com.gdx.jgame.IDAdapter;
import com.gdx.jgame.SaveReference;
import com.gdx.jgame.gameObjects.characters.PlainCharacter;
import com.gdx.jgame.gameObjects.missiles.BouncingBullet;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.gameObjects.missiles.NormalBullet;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;
import com.gdx.jgame.gameObjects.missiles.def.MissileDef;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;
import com.gdx.jgame.managers.TextureManager;

public class Armory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3412328135321223411L;
	
	public float initialImpulseNormalBullet;
	public float initialImpulseBouncingBullet;
	
	public NormalBulletDef normalBulletDef = null;
	public BouncingBulletDef bouncingBulletDef = null;
	
	public int maxAmmoNormalBullet;
	public int maxAmmoBouncingBullet;
	
	public int currentAmmoNormalBullet;
	public int currentAmmoBouncingBullet;
	
	private transient TextureManager bulletsTextures;
	private transient MissilesManager missileManager;
	private SaveReference<PlainCharacter> owner;
	
	public Armory(TextureManager bulletsTextures, MissilesManager missileManager) {
		this.bulletsTextures = bulletsTextures;
		this.missileManager = missileManager;
		owner = new SaveReference<PlainCharacter>();
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
		this.bulletsTextures = copy.bulletsTextures;
		this.missileManager = copy.missileManager;
		owner = new SaveReference<PlainCharacter>(copy.owner);
	}
	
	public void setOwner(PlainCharacter owner) {
		this.owner.setAndSave(owner);
		if(normalBulletDef != null) normalBulletDef.setOwner(owner);
		if(bouncingBulletDef != null) bouncingBulletDef.setOwner(owner);		
	}
	
	public void spawnBullet(Camera camera, Vector2 space, Missile.MissileType type) {
		Method method = (MissileDef missile, float initialImpulse) -> {
			return ((PlainCharacter) owner.getOwner()).
					initialImpulseAndPosition(camera, space, missile, initialImpulse, 0);
		};
		spawnBulletMethod(space, type, method);
	}
	
	public void spawnBullet(Vector2 target, Vector2 space, Missile.MissileType type) {
		Method method = (MissileDef missile, float initialImpulse) -> {
			return ((PlainCharacter) owner.getOwner())
					.initialImpulseAndPosition(target, space, missile, initialImpulse, ((float) Math.PI/2));
		};
		spawnBulletMethod(space, type, method);
	}
	
	public void spawnBullet(Vector2 space, Missile.MissileType type) {
		Method method = (MissileDef missile, float initialImpulse) -> {
			return ((PlainCharacter) owner.getOwner()).
					initialImpulseAndPosition(space, missile, initialImpulse, ((float) Math.PI/2));
		};
		spawnBulletMethod(space, type, method);
	}
	
	private void spawnBulletMethod(Vector2 space, Missile.MissileType type, Method method) {
		Vector2 imp = null;
		Missile bullet = null;
		
		if(type.equals(Missile.MissileType.NormalBullet) && normalBulletDef != null && 
				currentAmmoNormalBullet > 0) {
			imp = method.run(normalBulletDef, initialImpulseNormalBullet);
			bullet = new NormalBullet(normalBulletDef, owner.getOwner());
			--currentAmmoNormalBullet;
		}
		else if(type.equals(Missile.MissileType.BouncingBullet) && bouncingBulletDef != null &&
				currentAmmoBouncingBullet > 0) {
			imp = method.run(bouncingBulletDef, initialImpulseBouncingBullet);
			bullet = new BouncingBullet(bouncingBulletDef, owner.getOwner());
			--currentAmmoBouncingBullet;
		}
		if(bullet == null) return;
		
		bullet.getBody().applyLinearImpulse(imp, bullet.getBody().getWorldCenter(), true);
		this.missileManager.add(bullet);
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
			normalBulletDef.texture = bulletsTextures.get(textureName);
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
			bouncingBulletDef.texture = bulletsTextures.get(textureName);
			bouncingBulletDef.setVertices(vertices);
		}
		
		shape.dispose();
	}	
	
	public void load(Map <Integer, IDAdapter> restoreOwner, TextureManager bulletsTextures, MissilesManager missileManager) {
		owner.load(restoreOwner);
		this.bulletsTextures = bulletsTextures;
		this.missileManager = missileManager;
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
		result = prime * result + maxAmmoBouncingBullet;
		result = prime * result + maxAmmoNormalBullet;
		result = prime * result + ((normalBulletDef == null) ? 0 : normalBulletDef.hashCode());
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
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
		if (maxAmmoBouncingBullet != other.maxAmmoBouncingBullet)
			return false;
		if (maxAmmoNormalBullet != other.maxAmmoNormalBullet)
			return false;
		if (normalBulletDef == null) {
			if (other.normalBulletDef != null)
				return false;
		} else if (!normalBulletDef.equals(other.normalBulletDef))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}
	
	private interface Method{
		public Vector2 run(MissileDef missile, float initialImpulse);
	}
	
}
