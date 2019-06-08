package com.gdx.jgame.gameObjects.missiles;

import com.gdx.jgame.gameObjects.missiles.def.NormalBulletDef;

public class NormalBullet extends Missile{	
	
	public NormalBullet(NormalBulletDef missileDef) {
		super(missileDef, missileDef.getOwner());
	}

	@Override
	public void updateObject(Object object) {
		return;
	}

	@Override
	public boolean isRemovable() {
		return isDestructible();
	}

	@Override
	public void deleteObject() {
		destoryBody();
	}
	
}
