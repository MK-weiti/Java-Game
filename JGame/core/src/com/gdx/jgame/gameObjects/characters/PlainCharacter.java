package com.gdx.jgame.gameObjects.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gdx.jgame.Camera;
import com.gdx.jgame.gameObjects.MovingObject;
import com.gdx.jgame.gameObjects.missiles.def.MissileDef;
import com.gdx.jgame.logic.Armory;
import com.gdx.jgame.logic.ArmoryMethods;

public abstract class PlainCharacter extends MovingObject implements CharacterMethods, ArmoryMethods{
	private int m_maxHealth;
	private int m_health;
	
	Armory m_armory;
	
	private String m_groupName = null;
	
	public PlainCharacter(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef, characterPolygonDef.maxVelocity, characterPolygonDef.acceleration);
		m_maxHealth = characterPolygonDef.maxHealth;
		m_health = characterPolygonDef.m_health;
		m_groupName = characterPolygonDef.charGroupName;
		
		characterPolygonDef.armory.setOwner(this);
		m_armory = new Armory(characterPolygonDef.armory);
	}
	
	public Vector2 initialImpulseAndPosition(Camera camera, Vector2 space, MissileDef missile, float initialImpulse) {
		Vector2 rad = new Vector2(0f, 1f);
		Vector2 bulletPos = new Vector2();
		Vector2 impulse = new Vector2();
		rad.setAngleRad( - this.rawAngleRadOnScreen(camera, new Vector2(Gdx.input.getX(), Gdx.input.getY())));
		
		
		bulletPos.x = this.getPosition().x + (this.getDefaultSprite().getWidth()/2 + space.x) * rad.x;
		bulletPos.y = this.getPosition().y + (this.getDefaultSprite().getHeight()/2 + space.y) * rad.y;
		missile.setPositionInGame(bulletPos);
		missile.setOwner(this);
		
		impulse.x = rad.x * initialImpulse;
		impulse.y = rad.y * initialImpulse;
		
		return impulse;
	}

	public int getMaxHealth() {
		return m_maxHealth;
	}
	
	public float getMaxHealthFloat() {
		return (float) m_maxHealth;
	}
	
	public int getHealth() {
		return m_health;
	}
	
	public float getHealthFloat() {
		return (float) m_health;
	}
	
	public void setMaxHealth(int health) {
		m_maxHealth = health;
	}
	
	public void setHealth(int health) {
		m_health = health;
	}
	
	public void changeHealth(int difference) {
		if(m_health + difference < 0) {
			m_health = 0;
		}
		else if(m_health + difference > m_maxHealth) {
			m_health = m_maxHealth;
		}
		else {
			m_health += difference;
		}
	}
	
	public void giveDamage(int damage) {
		if(m_health - damage < 0) {
			m_health = 0;
		}
		else {
			m_health -= damage;
		}
	}
	
	public void giveHealing(int heal) {
		if(m_health + heal > m_maxHealth) {
			m_health = m_maxHealth;
		}
		else {
			m_health += heal;
		}
	}

	public String getGroupName() {
		return m_groupName;
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
