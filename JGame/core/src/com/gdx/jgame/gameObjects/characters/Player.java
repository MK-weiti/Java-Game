package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.Methods;
import com.gdx.jgame.gameObjects.missiles.Missile;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.gameObjects.missiles.NormalBullet;
import com.gdx.jgame.gameObjects.missiles.BouncingBullet;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;
import com.gdx.jgame.logic.Armory;
import com.gdx.jgame.logic.ArmoryMethods;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;
import com.gdx.jgame.managers.TextureManager;

public class Player extends PlainCharacter implements ArmoryMethods{
	
	Armory m_armory;
	
	public Player(PlayerDef playerDef, TextureManager txBulletManager, MissilesManager missileManager) {
		super(playerDef);
		//m_armory = new Armory(this, txBulletManager, missileManager);
		m_armory = playerDef.armory;
		playerDef.armory.setOwner(this);
		
		//m_armory.initialImpulseNormalBullet = playerDef.initialImpulseNormalBullet;
		//m_armory.initialImpulseBouncingBullet = playerDef.initialImpulseBouncingBullet;
		
		//m_armory.normalBulletDef = createBulletDef(txBulletManager);
		//m_armory.bouncingBulletDef = createmBouncingBulletDef(txBulletManager);
		
		//setAmmo();
	}

	/*private void setAmmo() {
		m_armory.currentAmmoBouncingBullet = 5;
		m_armory.currentAmmoNormalBullet = 10;
	}*/

	/*private NormalBulletDef createBulletDef(TextureManager txBulletManager) {
		NormalBulletDef normalBulletDef;
		normalBulletDef = new NormalBulletDef(getWorld(), txBulletManager, "rocket.png", 
				0.05f, Methods.setVerticesToTexture(txBulletManager.get("rocket.png"), 0.01f), this);
		normalBulletDef.damage = -2;
		normalBulletDef.maxVelocity = 40f;
		normalBulletDef.acceleration = 5f;
		normalBulletDef.fixtureDef.density = 2f;
		normalBulletDef.bodyDef.type = BodyType.DynamicBody;
		normalBulletDef.bodyDef.fixedRotation = false;
		normalBulletDef.fixtureDef.friction = 0f;
		normalBulletDef.fixtureDef.restitution = 1f;
		return normalBulletDef;
	}
	
	private BouncingBulletDef createmBouncingBulletDef(TextureManager txBulletManager) {
		BouncingBulletDef bouncingBulletDef;
		bouncingBulletDef = new BouncingBulletDef(getWorld(), txBulletManager, "normalBullet.png", 
				0.05f, Methods.setVerticesToTexture(txBulletManager.get("normalBullet.png"), 0.01f), this);
		bouncingBulletDef.numberOfBounces = 3;
		bouncingBulletDef.damage = -2;
		bouncingBulletDef.maxVelocity = 40f;
		bouncingBulletDef.acceleration = 5f;
		bouncingBulletDef.fixtureDef.density = 2f;
		bouncingBulletDef.bodyDef.type = BodyType.DynamicBody;
		bouncingBulletDef.bodyDef.fixedRotation = false;
		bouncingBulletDef.fixtureDef.friction = 0f;
		bouncingBulletDef.fixtureDef.restitution = 1f;
		
		return bouncingBulletDef;
	}*/
	
	public void spawnBullet(Camera camera, Vector2 space, Missile.MissileType type) {
		m_armory.spawnBullet(camera, space, type);
	}

	@Override
	public void updateObject(Object object) {
		if(object instanceof Missile) {
			changeHealth(((Missile) object).getDamage());
		}
	}

	@Override
	public boolean isRemovable() {
		if(getHealth() > 0) return false;
		return isDestructible();
	}

	@Override
	public void deleteObject() {
		destoryBody();
	}

	@Override
	public float getInitialImpulseNormalBullet() {
		return m_armory.initialImpulseNormalBullet;
	}

	@Override
	public void setInitialImpulseNormalBullet(float initialImpulseNormalBullet) {
		m_armory.initialImpulseNormalBullet = initialImpulseNormalBullet;
	}

	@Override
	public float getInitialImpulseBouncingBullet() {
		return m_armory.initialImpulseBouncingBullet;
	}

	@Override
	public void setInitialImpulseBouncingBullet(float initialImpulseBouncingBullet) {
		m_armory.initialImpulseBouncingBullet = initialImpulseBouncingBullet;
	}
	
	@Override
	public int getMaxAmmoNormalBullet() {
		return m_armory.maxAmmoNormalBullet;
	}

	@Override
	public void setMaxAmmoNormalBullet(int maxAmmoNormalBullet) {
		m_armory.maxAmmoNormalBullet = maxAmmoNormalBullet;
	}

	@Override
	public int getMaxAmmoBouncingBullet() {
		return m_armory.maxAmmoBouncingBullet;
	}

	@Override
	public void setMaxAmmoBouncingBullet(int maxAmmoBouncingBullet) {
		m_armory.maxAmmoBouncingBullet = maxAmmoBouncingBullet;
	}

	@Override
	public int getCurrentAmmoNormalBullet() {
		return m_armory.currentAmmoNormalBullet;
	}

	@Override
	public void setCurrentAmmoNormalBullet(int currentAmmoNormalBullet) {
		m_armory.currentAmmoNormalBullet = currentAmmoNormalBullet;
	}

	@Override
	public int getCurrentAmmoBouncingBullet() {
		return m_armory.currentAmmoBouncingBullet;
	}

	@Override
	public void setCurrentAmmoBouncingBullet(int currentAmmoBouncingBullet) {
		m_armory.currentAmmoBouncingBullet = currentAmmoBouncingBullet;
	}
	
	
}
