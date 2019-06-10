package com.gdx.jgame.gameObjects.missiles;

import com.gdx.jgame.gameObjects.missiles.def.BouncingBulletDef;

public class BouncingBullet extends Missile{
	
	private int m_numberOfBounces;

	public BouncingBullet(BouncingBulletDef bullet, Object owner) {
		super(bullet, owner);
		m_numberOfBounces = bullet.numberOfBounces;
	}

	@Override
	public void updateObject(Object object) {
		--m_numberOfBounces;
	}

	@Override
	public boolean isRemovable() {
		if(m_numberOfBounces <= 0) return true;
		return false;
	}

	@Override
	public void deleteObject() {
		destoryBody();
	}

	public int getNumberOfBounces() {
		return m_numberOfBounces;
	}

}
