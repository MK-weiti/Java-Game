package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.Methods;
import com.gdx.jgame.gameObjects.characters.def.CharacterPolygonDef;
import com.gdx.jgame.gameObjects.characters.def.PlayerDef;
import com.gdx.jgame.gameObjects.missiles.MissileAdapter;
import com.gdx.jgame.gameObjects.missiles.MissilesManager;
import com.gdx.jgame.gameObjects.missiles.NormalBullet;
import com.gdx.jgame.gameObjects.missiles.BouncingBullet;
import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;
import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;
import com.gdx.jgame.managers.TextureManager;

public class Player extends PlainCharacter{
	
	private float initialImpulseNormalBullet;
	private float initialImpulseBouncingBullet;
	
	private NormalBulletDef m_normalBulletDef;
	private BouncingBulletDef m_bouncingBulletDef;
	private MissilesManager m_missileManager;
	
	public Player(PlayerDef playerDef, TextureManager txBulletManager, MissilesManager missileManager) {
		super(playerDef);
		this.getBody().setUserData(this);
		m_missileManager = missileManager;
		
		initialImpulseNormalBullet = playerDef.initialImpulseNormalBullet;
		initialImpulseBouncingBullet = playerDef.initialImpulseBouncingBullet;
		
		createBulletDef(txBulletManager);
		createmReboundBulletDef(txBulletManager);
	}

	private void createBulletDef(TextureManager txBulletManager) {
		m_normalBulletDef = new NormalBulletDef(getWorld(), txBulletManager, "normalBullet.png", 
				0.05f, Methods.setVerticesToTexture(txBulletManager.get("normalBullet.png"), 0.01f), this);
		m_normalBulletDef.damage = -2;
		m_normalBulletDef.maxVelocity = 40f;
		m_normalBulletDef.acceleration = 5f;
		m_normalBulletDef.fixtureDef.density = 2f;
		m_normalBulletDef.bodyDef.type = BodyType.DynamicBody;
		m_normalBulletDef.bodyDef.fixedRotation = false;
		m_normalBulletDef.fixtureDef.friction = 0f;
		m_normalBulletDef.fixtureDef.restitution = 1f;
	}
	
	private void createmReboundBulletDef(TextureManager txBulletManager) {
		m_bouncingBulletDef = new BouncingBulletDef(getWorld(), txBulletManager, "normalBullet.png", 
				0.05f, Methods.setVerticesToTexture(txBulletManager.get("normalBullet.png"), 0.01f), this);
		m_bouncingBulletDef.numberOfBounces = 3;
		m_bouncingBulletDef.damage = -2;
		m_bouncingBulletDef.maxVelocity = 40f;
		m_bouncingBulletDef.acceleration = 5f;
		m_bouncingBulletDef.fixtureDef.density = 2f;
		m_bouncingBulletDef.bodyDef.type = BodyType.DynamicBody;
		m_bouncingBulletDef.bodyDef.fixedRotation = false;
		m_bouncingBulletDef.fixtureDef.friction = 0f;
		m_bouncingBulletDef.fixtureDef.restitution = 1f;
	}

	public NormalBulletDef getNormalBulletDef() {
		return new NormalBulletDef(m_normalBulletDef);
	}
	
	public void spawnBullet(Camera camera, Vector2 space, MissileAdapter.MissileType type) {
		Vector2 imp = null;
		MissileAdapter bullet = null;
		
		if(type.equals(MissileAdapter.MissileType.NormalBullet)) {
			imp = initialImpulseAndPosition(camera, space, m_normalBulletDef, initialImpulseNormalBullet);
			bullet = new NormalBullet(m_normalBulletDef);
		}
		else if(type.equals(MissileAdapter.MissileType.BouncingBullet)) {
			imp = initialImpulseAndPosition(camera, space, m_bouncingBulletDef, initialImpulseBouncingBullet);
			bullet = new BouncingBullet(m_bouncingBulletDef);
		}
		
		bullet.getBody().applyLinearImpulse(imp, bullet.getBody().getWorldCenter(), true);
		m_missileManager.add(bullet);
	}

	@Override
	public void updateObject(Object object) {
		if(object instanceof MissileAdapter) {
			changeHealth(((MissileAdapter) object).getDamage());
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

	public float getInitialImpulseNormalBullet() {
		return initialImpulseNormalBullet;
	}

	public void setInitialImpulseNormalBullet(float initialImpulseNormalBullet) {
		this.initialImpulseNormalBullet = initialImpulseNormalBullet;
	}

	public float getInitialImpulseBouncingBullet() {
		return initialImpulseBouncingBullet;
	}

	public void setInitialImpulseBouncingBullet(float initialImpulseBouncingBullet) {
		this.initialImpulseBouncingBullet = initialImpulseBouncingBullet;
	}
	
	
}
