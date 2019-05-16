package com.gdx.jgame.gameObjects.characters;

import com.gdx.jgame.gameObjects.MovingObjectAdapter;

public abstract class PlainCharacter extends MovingObjectAdapter{
	private int m_maxHealth;
	private int m_health;
	
	public PlainCharacter(CharacterPolygonDef characterPolygonDef) {
		super(characterPolygonDef, characterPolygonDef.maxVelocity, characterPolygonDef.acceleration);
		m_maxHealth = characterPolygonDef.maxHealth;
		this.m_health = characterPolygonDef.m_health;
	}

	public int getMaxHealth() {
		return m_maxHealth;
	}
	
	public int getHealth() {
		return m_health;
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
}
