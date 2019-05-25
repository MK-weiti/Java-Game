package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.physics.box2d.World;
import com.gdx.jgame.ObjectsID;
import com.gdx.jgame.gameObjects.Methods;
import com.gdx.jgame.gameObjects.missiles.NormalBulletDef;
import com.gdx.jgame.managers.TextureManager;

public class Player extends PlainCharacter implements ObjectsID{
	
	private static long m_numberOfObjects = 0;
	public final long ID = m_numberOfObjects;
	
	private NormalBulletDef m_normalBulletDef;
	private World m_world;
	
	public Player(CharacterPolygonDef characterPolygonDef, TextureManager txBulletManager) {
		super(characterPolygonDef);
		++m_numberOfObjects;
		this.getBody().setUserData(this);
		m_world = characterPolygonDef.world;
		
		m_normalBulletDef = new NormalBulletDef(getWorld(), 
				txBulletManager, "normalBullet.png", 1f, Methods.setVerticesToTexture(txBulletManager.get("normalBullet.png"), 1f));
	}
	
	public long m_numberOfObjects() {
		return m_numberOfObjects;
	}

	public NormalBulletDef getNormalBulletDef() {
		return new NormalBulletDef(m_normalBulletDef);
	}
	
	public void spawnNormalBullet() {
		//m_world.createBody(m_normalBulletDef.bodyDef).createFixture(m_normalBulletDef.fixtureDef);
	}
	
	
}
